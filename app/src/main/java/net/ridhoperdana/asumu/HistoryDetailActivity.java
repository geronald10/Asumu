package net.ridhoperdana.asumu;

import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.ChartData;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistoryDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_detail);
        final ExpandableLayout expandableLayout = (ExpandableLayout)findViewById(R.id.expandable_layout);
        final ExpandableLayout expandableLayoutSaving = (ExpandableLayout)findViewById(R.id.expandable_layout_saving);
        ConstraintLayout expenseButton = (ConstraintLayout)findViewById(R.id.expense_button);
        ConstraintLayout savingButton = (ConstraintLayout)findViewById(R.id.saving_button);
        Random random = new Random();

        LineChart chart = (LineChart)findViewById(R.id.chart);
        LineChart chartsaving = (LineChart)findViewById(R.id.char_saving);
        List<Entry> list = new ArrayList<>();
        int max=100000;
        int min=10000;
        int nilai_random;
        for (int i=0; i<30; i++)
        {
            nilai_random = random.nextInt(max - min +1) +min;
            Entry entry1 = new Entry(i, nilai_random);
            list.add(entry1);
        }

        makeLineGraph(chart, list, "Expense");
        makeLineGraph(chartsaving, list, "Saving");

        expandableLayout.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("coba", "bisaaaa");
            }
        });
        expandableLayoutSaving.setOnExpansionUpdateListener(new ExpandableLayout.OnExpansionUpdateListener() {
            @Override
            public void onExpansionUpdate(float expansionFraction, int state) {
                Log.d("cobasave", "bisaaaasave");
            }
        });
        expenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandableLayout.isExpanded())
                    expandableLayout.collapse();
                else
                    expandableLayout.expand();
            }
        });
        savingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(expandableLayoutSaving.isExpanded())
                    expandableLayoutSaving.collapse();
                else
                    expandableLayoutSaving.expand();
            }
        });
    }

    private void makeLineGraph(LineChart chart, List<Entry> list, String namaGraph)
    {
        LineDataSet lineDataSet = new LineDataSet(list, "bukan");
        lineDataSet.setLineWidth(3);
        lineDataSet.setValueTextSize(10);
        int colorValue;
        if(namaGraph=="Expense")
        {
            colorValue = Color.parseColor("#f44336");
            lineDataSet.setColor(colorValue);
        }
        else
        {
            colorValue = Color.parseColor("#4CAF50");
            lineDataSet.setColor(colorValue);
//            Log.d("bukan", "expense");
        }
        lineDataSet.setAxisDependency(YAxis.AxisDependency.RIGHT);
        List<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(lineDataSet);
        LineData lineData = new LineData(dataSets);
        chart.setData(lineData);
        chart.invalidate();
    }
}
