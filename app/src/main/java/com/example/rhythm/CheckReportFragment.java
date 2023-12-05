package com.example.rhythm;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.StackedValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class CheckReportFragment extends Fragment {

    public PieChart pieChart;
    BarChart barChart;
    TableLayout reportTable;
    ProgressBar progressBar;
    ArrayList<PieEntry> pieEntryList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_check_report, container, false);
        reportTable = view.findViewById(R.id.reportTable);
        pieChart = view.findViewById(R.id.pieChart);
        barChart = view.findViewById(R.id.barChart);
        progressBar = view.findViewById(R.id.progressBarCR);


        DataService dataService = new DataService(getContext());
        dataService.getSessionDetails(new DataService.SessionDetailsResponseListener() {
            @Override
            public void onResponse(SessionDataModel[] session) {


                createTable(session);

                createPieChart(session);

                createBarchart(session);

                progressBar.setVisibility(View.GONE);

            }
            @Override
            public void onError(String message) {

            }
        });

        Button bt_7daysAgo = view.findViewById(R.id.days_7);
        Button bt_30daysAgo = view.findViewById(R.id.days_30);
        Button bt_1yearAgo = view.findViewById(R.id.year_1);

        bt_7daysAgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.days = 7;
                refreshPage(dataService, view);

            }
        });

        bt_30daysAgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.days = 30;
                refreshPage(dataService, view);

            }
        });

        bt_1yearAgo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dataService.days = 1;
                refreshPage(dataService, view);

            }
        });

        return view;
    }

    private void refreshPage(@NonNull DataService dataService, View view) {

        dataService.getSessionDetails(new DataService.SessionDetailsResponseListener() {
            @Override
            public void onResponse(SessionDataModel[] session) {

                if(session.length != 0){

                    barChart.notifyDataSetChanged();
                    pieChart.notifyDataSetChanged();
                    barChart.clear();
                    pieChart.clear();
                    reportTable.invalidate();
                    reportTable.removeViews(1, reportTable.getChildCount()-1);

                    createTable(session);

                    createPieChart(session);

                    createBarchart(session);

                    progressBar.setVisibility(View.GONE);

                } else {
                    Toast.makeText(getContext(), "No data available", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onError(String message) {

            }
        });
    }

    private void createTable(@NonNull SessionDataModel[] session) {
//        reportTable = view.findViewById(R.id.reportTable);
        for (int i = 1; i <= session.length; i++ ){

            TableRow tr = new TableRow(getContext());
            tr.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            TextView viewReport = new TextView(getContext());
            viewReport.setGravity(Gravity.CENTER);
            TextView sessionDetails = new TextView(getContext());
            sessionDetails.setGravity(Gravity.CENTER);
            TextView startedAt = new TextView(getContext());
            startedAt.setGravity(Gravity.CENTER);
            TextView duration = new TextView(getContext());
            duration.setGravity(Gravity.CENTER);
            TextView average = new TextView(getContext());
            average.setGravity(Gravity.CENTER);
            TextView total = new TextView(getContext());
            total.setGravity(Gravity.CENTER);
            TextView normal = new TextView(getContext());
            normal.setGravity(Gravity.CENTER);

            viewReport.setText("View Report");
            viewReport.setTextColor(Color.CYAN);
            sessionDetails.setText(session[i-1].session_id);
            String input = session[i-1].start_time;
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'");
            String date = LocalDateTime.parse(input, formatter).format(DateTimeFormatter.ofPattern("dd MMM yyyy hh:mm a"));
            startedAt.setText(date);
            duration.setText(session[i-1].duration);
            average.setText(session[i-1].median_bpm_count);
            total.setText(session[i-1].total_analyzed_beat);
            int normalBeat = Integer.parseInt(session[i - 1].normal_beat_count);
            int totalBeat = Integer.parseInt(session[i - 1].total_analyzed_beat);
            if (totalBeat == 0){
                normalBeat = 0;
            } else {
                normalBeat = (normalBeat*100)/totalBeat;
            }

            String normalBeatCount = Integer.toString(normalBeat);
            normal.setText(normalBeatCount + "%");

            tr.addView(viewReport);
            tr.addView(sessionDetails);
            tr.addView(startedAt);
            tr.addView(duration);
            tr.addView(average);
            tr.addView(total);
            tr.addView(normal);
            tr.setPadding(10, 10, 10, 10);

            SessionDataModel currentSession = session[i-1];
            viewReport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    Bundle bundle = new Bundle();
                    bundle.putSerializable("session", currentSession);
                    Fragment fragment = new PdfDownload();
                    fragment.setArguments(bundle);
                    FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
                    transaction.replace(R.id.frameLayout, fragment );
                    transaction.addToBackStack(null);
                    transaction.commit();

                }
            });

            reportTable.addView(tr);
        }
    }

    private void createBarchart(@NonNull SessionDataModel[] session) {

        //String[] sessionIds = new String[3];

        ArrayList<BarEntry> group1 = new ArrayList<>();
        ArrayList<BarEntry> group2 = new ArrayList<>();
        ArrayList<BarEntry> group3 = new ArrayList<>();

        for (SessionDataModel sessionDataModel : session) {
            float x = 0;
            group1.add(new BarEntry(x++, sessionDataModel.ap_beat_count));
            group2.add(new BarEntry(x++, sessionDataModel.int_normal_beat_count));
            group3.add(new BarEntry(x++, sessionDataModel.svf_beat_count));
            //sessionIds[i++] = sessionDataModel.session_id;
        }
        BarDataSet set1 = new BarDataSet(group1, "AP");
        BarDataSet set2 = new BarDataSet(group2, "NORMAL");
        BarDataSet set3 = new BarDataSet(group3, "SVF");


        set1.setColors(ColorTemplate.VORDIPLOM_COLORS);
        set2.setColors(ColorTemplate.LIBERTY_COLORS);
        set3.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData data = new BarData(set1, set2, set3);

        data.setBarWidth(0.25f);
        data.setValueTextColor(Color.WHITE);

        barChart.setData(data);
        barChartCustomization(barChart);
        barChart.invalidate();

    }

    private void createPieChart(@NonNull SessionDataModel[] session) {

        Integer normalBeatCount = 0;
        Integer apBeatCount = 0;
        Integer svfBeatCount = 0;
        for (SessionDataModel sessionDataModel : session) {
            normalBeatCount += sessionDataModel.int_normal_beat_count;
            svfBeatCount += sessionDataModel.svf_beat_count;
            apBeatCount += sessionDataModel.ap_beat_count;
        }

//        pieChart = view.findViewById(R.id.pieChart);
        pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(apBeatCount, "ap_beat_count"));
        pieEntryList.add(new PieEntry(normalBeatCount, "normal_beat_count"));
        pieEntryList.add(new PieEntry(svfBeatCount, "svf_beat_count"));

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Pie chart");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.VORDIPLOM_COLORS);
        pieDataSet.setValueTextSize(16f);



        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        //pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void barChartCustomization(BarChart barChart) {
//        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(sessionIds));
        barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
        barChart.getXAxis().setAxisLineColor(Color.WHITE);
        barChart.getXAxis().setAvoidFirstLastClipping(false);
        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.getXAxis().setAxisMinimum(0);
        barChart.getXAxis().setXOffset(0);
        barChart.getXAxis().setDrawLabels(false);
        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDragEnabled(true);
        barChart.getDescription().setEnabled(false);
        barChart.setVisibleXRangeMaximum(3);
        barChart.groupBars(0, 0.1f, 0.0f);
        barChart.setFitBars(true);
    }

}