package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class SeeResult extends AppCompatActivity {
  private ArrayList<Dictionary> mArrayList2;
  private CustomAdapter mAdapter;

  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.result);
    setTitle("지각 벌칙 어플");

    Intent intent = getIntent();
    mArrayList2 = (ArrayList<Dictionary>) intent.getSerializableExtra("MArrayList2");

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list2);
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLinearLayoutManager);

    mAdapter = new CustomAdapter(this, mArrayList2);
    mRecyclerView.setAdapter(mAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
            mLinearLayoutManager.getOrientation());
    mRecyclerView.addItemDecoration(dividerItemDecoration);

    int totalElements = mArrayList2.size();
    TextView textView = (TextView)findViewById(R.id.textView);
    textView.setText("최종 " + totalElements + "번 뽑기 기록");
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if (item.getItemId()==R.id.action_share) {
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType("text/plain");
      String text = "{몇 분, 벌칙, 사람}\n";
      for (Dictionary dict : mArrayList2) {
        text += "{"+ dict.getTime() + "분, " + dict.getPenalty() + ", " + dict.getName() + "}\n";
      }
      intent.putExtra(Intent.EXTRA_TEXT, text);
      Intent chooser = Intent.createChooser(intent, "친구에게 공유하기");
      startActivity(chooser);

      return true;
    }
    else {
      return super.onOptionsItemSelected(item);
    }
  }
}
