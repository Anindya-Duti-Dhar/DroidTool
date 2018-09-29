package duti.com.droidtool.activities;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.LargeValueFormatter;

import java.util.ArrayList;
import java.util.List;

import antonkozyriatskyi.circularprogressindicator.CircularProgressIndicator;
import duti.com.droidtool.R;
import duti.com.droidtool.database.Repository;
import duti.com.droidtool.dtlib.DateTimeManager;
import duti.com.droidtool.model.InternetDataUsages;


public class InternetDataUsagesActivity extends BaseActivity<InternetDataUsages> {

    Repository<InternetDataUsages> repo = new Repository<InternetDataUsages>(this, new InternetDataUsages());

    //Defining Variables
    private BarChart mChart;
    private float barWidth = 0.4f;
    private BarData mData;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_internet_data_usages);
        register(this, R.id.toolbar, R.string.internet_usages);

        CircularProgressIndicator circularProgress = findViewById(R.id.today_data);
        circularProgress.setMaxProgress(500);
        circularProgress.setCurrentProgress(146);

        mChart = (BarChart) findViewById(R.id.chart);

        // chart pre-settings
        chartPreSettings();

        // bind up X-axis properties
        setXAxis();

        // bind bar chart data bind
        setData();

        // chart  post-settings
        chartPostSettings();

        dt.dateTime.monthSetResponseListener(new DateTimeManager.onMonthSetListener() {
            @Override
            public void onMonthSet(String date, TextView tvDate) {
                dt.msg(date);
            }
        });

        dt.dateTime.monthPicker(R.id.month_picker);

        dt.ui.imageButton.getRes(R.id.next_part_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt.ui.imageButton.getRes(R.id.next_part_month).setBackground(dt.c.getResources().getDrawable(R.drawable.ic_action_right_selected));
                dt.ui.imageButton.getRes(R.id.prev_part_month).setBackground(dt.c.getResources().getDrawable(R.drawable.ic_action_chevron_left));
                if(getXAxisValues().size()>30) dt.ui.textView.getObject(R.id.bar_bottom_label).setText(dt.gStr(R.string.internet_usages_16_31));
                else if(getXAxisValues().size()<30) dt.ui.textView.getObject(R.id.bar_bottom_label).setText(dt.gStr(R.string.internet_usages_16_29));
                else dt.ui.textView.getObject(R.id.bar_bottom_label).setText(dt.gStr(R.string.internet_usages_16_30));
                mChart.animateXY(1000, 1000);
                mChart.setVisibleXRangeMaximum(16);
                mChart.moveViewToX(15);
                mChart.invalidate();
            }
        });

        dt.ui.imageButton.getRes(R.id.prev_part_month).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt.ui.imageButton.getRes(R.id.prev_part_month).setBackground(dt.c.getResources().getDrawable(R.drawable.ic_action_left_selected));
                dt.ui.imageButton.getRes(R.id.next_part_month).setBackground(dt.c.getResources().getDrawable(R.drawable.ic_action_chevron_right));
                dt.ui.textView.getObject(R.id.bar_bottom_label).setText(dt.gStr(R.string.internet_usages_1_15));
                mChart.animateXY(1000, 1000);
                mChart.setVisibleXRangeMaximum(15);
                mChart.moveViewToX(-1);
                mChart.invalidate();
            }
        });

    }

    private void chartPostSettings() {
        mChart.animateXY(1000, 1000);
        mChart.setData(mData);
        mChart.getBarData().setBarWidth(barWidth);
        mChart.getData().setHighlightEnabled(true);
        mChart.setFitBars(true);
        mChart.setVisibleXRangeMaximum(15);
        mChart.invalidate();
    }

    // chart pre-settings
    private void chartPreSettings() {
        mChart.setTouchEnabled(false);
        mChart.setDragEnabled(false);
        mChart.setScaleEnabled(false);
        mChart.setScaleXEnabled(false);
        mChart.setScaleYEnabled(false);
        mChart.setPinchZoom(false);
        mChart.setDoubleTapToZoomEnabled(false);
        mChart.setDescription(null);
        mChart.setDrawBarShadow(false);
        mChart.getLegend().setEnabled(false);
        mChart.setDrawGridBackground(false);
        mChart.getAxisRight().setEnabled(false);
        mChart.getAxisLeft().setEnabled(false);
        mChart.getXAxis().setDrawLabels(true);
    }

    // bind bar chart data bind
    private void setData() {
        BarDataSet set = new BarDataSet(getEntries(), "Monthly Report");
        set.setColors(new int[]{R.color.md_blue_700, R.color.md_deep_purple_300}, this);
        mData = new BarData(set);
        mChart.setData(mData);
        mData.setValueFormatter(new LargeValueFormatter());
    }

    // bind up X-axis properties
    private void setXAxis() {
        XAxis xAxis = mChart.getXAxis();
        xAxis.setGranularity(1f);
        xAxis.setGranularityEnabled(true);
        xAxis.setCenterAxisLabels(false);
        xAxis.setDrawGridLines(false);
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IndexAxisValueFormatter(getXAxisValues()));
        xAxis.setLabelCount(30);
    }

    private List<BarEntry> getEntries() {
        List<BarEntry> entries = new ArrayList<>();
        //Float.parseFloat(String.valueOf(helper.getTotalDataCount(TABLE_REGISTRATION)))
        entries.add(new BarEntry(0f, 60f));
        entries.add(new BarEntry(1f, 80f));
        entries.add(new BarEntry(2f, 45f));
        entries.add(new BarEntry(3f, 70f));
        entries.add(new BarEntry(4f, 55f));
        entries.add(new BarEntry(5f, 60f));
        entries.add(new BarEntry(6f, 80f));
        entries.add(new BarEntry(7f, 45f));
        entries.add(new BarEntry(8f, 70f));
        entries.add(new BarEntry(9f, 55f));
        entries.add(new BarEntry(10f, 60f));
        entries.add(new BarEntry(11f, 80f));
        entries.add(new BarEntry(12f, 45f));
        entries.add(new BarEntry(13f, 70f));
        entries.add(new BarEntry(14f, 55f));
        entries.add(new BarEntry(15f, 60f));
        entries.add(new BarEntry(16f, 80f));
        entries.add(new BarEntry(17f, 45f));
        entries.add(new BarEntry(18f, 70f));
        entries.add(new BarEntry(19f, 55f));
        entries.add(new BarEntry(20f, 55f));
        entries.add(new BarEntry(21f, 60f));
        entries.add(new BarEntry(22f, 80f));
        entries.add(new BarEntry(23f, 45f));
        entries.add(new BarEntry(24f, 70f));
        entries.add(new BarEntry(25f, 60f));
        entries.add(new BarEntry(26f, 80f));
        entries.add(new BarEntry(27f, 45f));
        entries.add(new BarEntry(28f, 70f));
        entries.add(new BarEntry(29f, 55f));
        //entries.add(new BarEntry(30f, 55f));
        return entries;
    }

    private ArrayList getXAxisValues() {
        ArrayList xVals = new ArrayList();
        for (int i = 1; i < 31; i++) {
            xVals.add(String.valueOf(i));
        }
        return xVals;
    }

    @Override
    protected void onOptionsItemClick(MenuItem item) {

    }

    @Override
    protected int onOptionsMenuInflate() {
        return 0;
    }

    // back arrow action
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    // back button press method
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
