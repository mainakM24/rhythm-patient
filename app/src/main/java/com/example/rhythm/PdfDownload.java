package com.example.rhythm;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

public class PdfDownload extends Fragment {

    private static final int REQUEST_CODE = 1232;
    BarChart barChart;
    PieChart pieChart;
    Button downloadButton;
    CardView cardView;
    TableLayout col1, col2;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pdf_download, container, false);

        //layouts
        col1 = view.findViewById(R.id.pdfTableColoumn1);
        col2 = view.findViewById(R.id.pdfTableColoumn2);

        //getting bundle for graphs
        Bundle bundle = getArguments();
        assert bundle != null;
        SessionDataModel session = (SessionDataModel) bundle.getSerializable("session");


        //getting data for patient details
        new DataService(getContext()).getUserDetails(new DataService.UserDetailsResponseListener() {
            @Override
            public void onResponse(UserDataModel user) {

                setTableText(0,user.patient_id);
                setTableText(1,user.p_name);
                setTableText(2,user.p_dob);
                setTableText(3,user.p_sex);
                setTableText(4,user.p_street_name);
                setTableText(5,user.p_mobile);
                setTableText(6,user.p_email);
            }

            @Override
            public void onError(String message) {
            }
        });


        //initialize
        barChart = view.findViewById(R.id.barChartDownload);
        pieChart = view.findViewById(R.id.pieChart);
        downloadButton = view.findViewById(R.id.downloadButton);
        cardView = view.findViewById(R.id.pdfCardView);

        //calling chart function
        assert session != null;
        createBarChart(session);
        createPieChart(session);


        //on press download button
        downloadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ask for write permission
                askPermissions();

                //download PDF
                downloadPDF(session.session_id);
            }
        });

        return view;
    }

    private void setTableText(int row, String text) {
        TableRow tr = (TableRow) col1.getChildAt(row);
        TextView tv = (TextView) tr.getChildAt(1);
        tv.setText(text);
    }

    private void askPermissions() {
        ActivityCompat.requestPermissions(requireActivity(), new String[]{WRITE_EXTERNAL_STORAGE}, REQUEST_CODE);
    }

    private void createBarChart(@NonNull SessionDataModel session) {

        List<String> xValues = Arrays.asList("Min bpm", "Median bpm", "Max bpm");

        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0, session.min_bpm_count));
        entries.add(new BarEntry(1, Float.parseFloat(session.median_bpm_count)));
        entries.add(new BarEntry(2, session.max_bpm_count));

        BarDataSet barDataSet = new BarDataSet(entries, "");
        barDataSet.setColors(ColorTemplate.MATERIAL_COLORS);

        BarData barData = new BarData(barDataSet);

        barChart.setData(barData);
        barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xValues));
        barChartCustomization(barChart);
        barChart.invalidate();
    }

    private void createPieChart(@NonNull SessionDataModel session) {

        ArrayList<PieEntry> pieEntryList;

        int normalBeatCount = Integer.parseInt(session.normal_beat_count);
        int apBeatCount = session.ap_beat_count;


//        pieChart = view.findViewById(R.id.pieChart);
        pieEntryList = new ArrayList<>();
        pieEntryList.add(new PieEntry(apBeatCount, "ap_beat_count"));
        pieEntryList.add(new PieEntry(normalBeatCount, "normal_beat_count"));

        PieDataSet pieDataSet = new PieDataSet(pieEntryList, "Pie chart");
        PieData pieData = new PieData(pieDataSet);
        pieDataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        pieDataSet.setValueTextSize(16f);



        pieChart.setData(pieData);
        pieChart.setDrawHoleEnabled(false);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setUsePercentValues(true);
        pieChart.getDescription().setEnabled(false);
        pieChart.invalidate();
    }

    private void barChartCustomization(@NonNull BarChart barChart) {
        //barChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(sessionIds));
        //barChart.getXAxis().setCenterAxisLabels(true);
        barChart.getXAxis().setGranularity(1f);
        barChart.getXAxis().setGranularityEnabled(true);
//        barChart.getXAxis().setAxisLineColor(Color.WHITE);
//        barChart.getXAxis().setAvoidFirstLastClipping(false);
//        barChart.getXAxis().setTextColor(Color.WHITE);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
//        barChart.getXAxis().setAxisMinimum(0);
//        barChart.getXAxis().setXOffset(0);
//        barChart.getXAxis().setDrawLabels(false);
//        barChart.getAxisLeft().setTextColor(Color.WHITE);
        barChart.getAxisRight().setEnabled(false);
        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDragEnabled(true);
        barChart.getDescription().setEnabled(false);
//        barChart.setVisibleXRangeMaximum(3);
//        barChart.groupBars(0, 0.1f, 0.0f);
//        barChart.setFitBars(true);
    }

    public void downloadPDF(String sessionID) {
        // Inflate the XML layout file
        View view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_pdf_download, cardView, false);
        DisplayMetrics displayMetrics = new DisplayMetrics();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Objects.requireNonNull(requireContext().getDisplay()).getRealMetrics(displayMetrics);
        } else
            requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        view.measure(View.MeasureSpec.makeMeasureSpec(displayMetrics.widthPixels, View.MeasureSpec.EXACTLY),
                View.MeasureSpec.makeMeasureSpec(displayMetrics.heightPixels, View.MeasureSpec.EXACTLY));

        view.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        // Create a new PdfDocument instance
        PdfDocument document = new PdfDocument();

        // Obtain the width and height of the view
        int viewWidth = view.getMeasuredWidth();
        int viewHeight = view.getMeasuredHeight();
        //int viewWidth = 1080;
        //int viewHeight = 1920;

        // Create a PageInfo object specifying the page attributes
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo.Builder(viewWidth, viewHeight, 1).create();

        // Start a new page
        PdfDocument.Page page = document.startPage(pageInfo);

        // Get the Canvas object to draw on the page
        Canvas canvas = page.getCanvas();

        // Create a Paint object for styling the view
        Paint paint = new Paint();
        paint.setColor(Color.WHITE);

        // Draw the view on the canvas
        cardView.draw(canvas);

        // Finish the page
        document.finishPage(page);

        // Specify the path and filename of the output PDF file
        File downloadsDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        String fileName = "Report-" + sessionID + ".pdf";
        File filePath = new File(downloadsDir, fileName);

        try {
            // Save the document to a file
            FileOutputStream fos = new FileOutputStream(filePath);
            document.writeTo(fos);
            document.close();
            fos.close();
            // PDF conversion successful
            Toast.makeText(getContext(), "XML to PDF Conversion Successful", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
            // Error occurred while converting to PDF
        }
    }

}