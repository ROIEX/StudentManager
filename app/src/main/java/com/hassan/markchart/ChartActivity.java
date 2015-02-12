package com.hassan.markchart;

import android.graphics.Color;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.SeriesSelection;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;
import org.apache.poi.poifs.crypt.DataSpaceMapUtils;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends ActionBarActivity implements RadioGroup.OnCheckedChangeListener{
    private static int[] COLORS = new int[] { Color.GREEN, Color.BLUE,Color.MAGENTA, Color.CYAN, Color.BLACK, Color.RED, Color.WHITE, Color.YELLOW };

    private static ArrayList<Double> VALUES = new ArrayList<Double>();

    private static ArrayList<String> NAME_LIST = new ArrayList<String>();

    private CategorySeries mSeries = new CategorySeries("");

    private DefaultRenderer mRenderer = new DefaultRenderer();

    private XYMultipleSeriesRenderer xyRenderer= new XYMultipleSeriesRenderer();

    private XYMultipleSeriesDataset dataSet=new XYMultipleSeriesDataset();

    private GraphicalView mChartViewPie;
    private GraphicalView mChartViewBar;

    private LinearLayout layoutPie;
    private LinearLayout layoutBar;


    RadioGroup radioGroup;

    LinearLayout layout;

    SimpleDBHelper helper;
    List<Student> students;

    RadioButton rbPieChart;
    RadioButton rbColumns;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        NAME_LIST.clear();
        dataSet.clear();
        VALUES.clear();
        setContentView(R.layout.activity_chart);
        RadioButton defaultRadioButton=(RadioButton)findViewById(R.id.rbPieChart);
        defaultRadioButton.setChecked(true);
        radioGroup=(RadioGroup)findViewById(R.id.radioGroup);
        radioGroup.setOnCheckedChangeListener(this);
        helper=SimpleDBHelper.getInstance(this);
        students=helper.getAllStudents();
        for(Student student : students)
        {
            if(!NAME_LIST.contains(student.rollNumber.toString())) {
                NAME_LIST.add(student.rollNumber.toString());
                VALUES.add(0.0);
            }
        }

        int iterator = 0;
        int count = 0;
        for(String name : NAME_LIST)
        {

            for(Student student : students)
            {
                if(Integer.parseInt(NAME_LIST.get(iterator))==(student.rollNumber))
                {
                    count++;
                    VALUES.set(iterator,VALUES.get(iterator)+student.mark);
                }
            }
            VALUES.set(iterator,VALUES.get(iterator)/count);
            count=0;
            iterator++;
        }
//        mRenderer.setApplyBackgroundColor(true);
//        mRenderer.setBackgroundColor(Color.argb(100, 50, 50, 50));
        mRenderer.setChartTitleTextSize(20);
        mRenderer.setLabelsTextSize(15);
        mRenderer.setLegendTextSize(15);
        mRenderer.setMargins(new int[]{20, 30, 15, 0});
        mRenderer.setZoomButtonsVisible(true);
        mRenderer.setStartAngle(90);
        for (int i = 0; i < VALUES.size(); i++) {
            mSeries.add(NAME_LIST.get(i), VALUES.get(i));
            SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();


            renderer.setColor(COLORS[(mSeries.getItemCount() - 1) % COLORS.length]);

            mRenderer.addSeriesRenderer(renderer);
        }

        XYSeriesRenderer xRenderer=new XYSeriesRenderer();
        xRenderer.setColor(Color.RED);
        xRenderer.setDisplayChartValues(true);
        xRenderer.setChartValuesSpacing((float) 5.5d);
        xRenderer.setLineWidth((float) 10.5d);
        xyRenderer.setApplyBackgroundColor(true);
        xyRenderer.setMarginsColor(getResources().getColor(R.color.background_material_light));
        xyRenderer.setBarSpacing(0.5);
        xyRenderer.setXTitle(getString(R.string.rollnum_string));
        xyRenderer.setYTitle(getString(R.string.marks_string));
        xyRenderer.setShowLegend(false);
        xyRenderer.addSeriesRenderer(xRenderer);

        dataSet.addSeries(mSeries.toXYSeries());
        if (mChartViewPie != null) {
            mChartViewPie.repaint();
        }
        if (mChartViewBar != null) {
            mChartViewBar.repaint();
        }
        rbPieChart=(RadioButton)findViewById(R.id.rbPieChart);
        rbColumns=(RadioButton)findViewById(R.id.rbColumns);
        helper.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mChartViewPie == null) {
            layoutPie = (LinearLayout)findViewById(R.id.layout_Pie);
            layoutBar = (LinearLayout)findViewById(R.id.layout_Bar);
            mChartViewPie = ChartFactory.getPieChartView(this, mSeries, mRenderer);
            mChartViewBar = ChartFactory.getBarChartView(this, dataSet, xyRenderer, BarChart.Type.DEFAULT);
            mRenderer.setClickEnabled(true);
            mRenderer.setSelectableBuffer(10);

            layoutPie.addView(mChartViewPie, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT,1f));
            layoutBar.addView(mChartViewBar, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT,1f));
        }
        else {
            mChartViewPie.repaint();
            mChartViewBar.repaint();
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        layoutPie=(LinearLayout)findViewById(R.id.layout_Pie);
        layoutBar=(LinearLayout)findViewById(R.id.layout_Bar);
        if(checkedId==R.id.rbPieChart){
            layoutPie.setVisibility(View.VISIBLE);
            layoutBar.setVisibility(View.GONE);
        }else if(checkedId==R.id.rbColumns){
            layoutPie.setVisibility(View.GONE);
            layoutBar.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
