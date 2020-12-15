package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.anychart.AnyChart;
import com.anychart.AnyChartView;
import com.anychart.chart.common.dataentry.DataEntry;
import com.anychart.chart.common.dataentry.ValueDataEntry;
import com.anychart.charts.Pie;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WriteMemo extends AppCompatActivity {
  private ArrayList<Dictionary> mArrayList;
  private ArrayList<Dictionary> mArrayList2;
  int[] timelist;
  String[] namelist;
  AnyChartView anyChartView;
  TextView highlight;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.memo);
    setTitle("지각 벌칙 어플");

    Intent intent = getIntent();
    mArrayList = (ArrayList<Dictionary>) intent.getSerializableExtra("MArrayList");

    int totalElements = mArrayList.size();// arrayList의 요소의 갯수를 구한다.
    timelist = new int[totalElements];
    namelist = new String[totalElements];
    for (int index = 0; index < totalElements; index++) {
      Dictionary dict = mArrayList.get(index);
      timelist[index] = Integer.parseInt(dict.getTime());
      namelist[index] = dict.getName();
    }

    highlight = findViewById(R.id.highlight);
    anyChartView = findViewById(R.id.chartView);
    setupPieChart();

    mArrayList2 = new ArrayList<>();
  }

  public void setupPieChart() {
    Pie pie = AnyChart.pie();
    List<DataEntry> dataEntries = new ArrayList<>();

    for (int i=0; i < mArrayList.size(); i++){
      dataEntries.add(new ValueDataEntry(namelist[i], timelist[i]));
    }
    pie.data(dataEntries);
    pie.title("당첨 확률");
    pie.legend().enabled(false);
    anyChartView.setChart(pie);
  }

  public void onClick(final View v) {
    switch(v.getId()){
      case R.id.button_to_choose:
        int[] end = new int[timelist.length];
        int start = 0;
        for (int i = 0; i < timelist.length; i++) {
          start += timelist[i];
          end[i] = start;
        }
        Random rd = new Random(); // 랜덤 객체 생성
        int randomnum = rd.nextInt(end[end.length-1]);

        for (int i = 0; i < end.length; i++) {
          if (randomnum < end[i]){
            Dictionary dict = mArrayList.get(i);
            mArrayList2.add(dict); //마지막 줄에 삽입됨

            highlight.setText("결과 : "+dict.getName() + " 벌칙 당첨");

            Toast toast = Toast.makeText(getApplicationContext(), dict.getName() + " 당첨!!!", Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();
            break;
          }
        }
        break;
      case R.id.button_to_result:
        if (mArrayList2.size() > 0) {
          Intent intent = new Intent(WriteMemo.this,
                  SeeResult.class);
          intent.putExtra("MArrayList2", mArrayList2);
          startActivity(intent);
        }
        break;
    }
  }
}
