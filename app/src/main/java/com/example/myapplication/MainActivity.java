package com.example.myapplication;
import android.Manifest;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
  private int MY_PERMISSIONS_REQUEST_SMS_RECEIVE = 10;
  BroadcastReceiver receiver = new BroadcastReceiver() {
    @Override
    public void onReceive(Context context, Intent intent) {
      if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
        String smsSender = "";
        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
          smsSender += smsMessage.getOriginatingAddress();
        }
        Toast.makeText(getApplicationContext(), smsSender + "에서 문자옴", Toast.LENGTH_LONG).show();
      }
    }
  };

  private ArrayList<Dictionary> mArrayList;
  private CustomAdapter mAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    setTitle("지각 벌칙 어플");
    ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS},
            MY_PERMISSIONS_REQUEST_SMS_RECEIVE);

    RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview_main_list);
    LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
    mRecyclerView.setLayoutManager(mLinearLayoutManager);

    mArrayList = new ArrayList<>();

    mAdapter = new CustomAdapter(this, mArrayList);
    mRecyclerView.setAdapter(mAdapter);

    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(mRecyclerView.getContext(),
            mLinearLayoutManager.getOrientation());
    mRecyclerView.addItemDecoration(dividerItemDecoration);

    Button buttonInsert = (Button) findViewById(R.id.button_main_insert);
    // 1. 화면 아래쪽에 있는 데이터 추가 버튼을 클릭하면
    buttonInsert.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // 2. 레이아웃 파일 edit_box.xml 을 불러와서 화면에 다이얼로그를 보여줌
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        View view = LayoutInflater.from(MainActivity.this)
                .inflate(R.layout.edit_box, null, false);
        builder.setView(view);

        final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
        final EditText editTextTIME = (EditText) view.findViewById(R.id.edittext_time);
        final EditText editTextPenalty = (EditText) view.findViewById(R.id.edittext_penalty);
        final EditText editTextName = (EditText) view.findViewById(R.id.edittext_name);

        ButtonSubmit.setText("삽입");

        final AlertDialog dialog = builder.create();

        // 3. 다이얼로그에 있는 삽입 버튼을 클릭하면
        ButtonSubmit.setOnClickListener(new View.OnClickListener() {
          public void onClick(View v) {
            // 4. 사용자가 입력한 내용을 가져와서
            String strTIME = editTextTIME.getText().toString();
            String strPenalty = editTextPenalty.getText().toString();
            String strName = editTextName.getText().toString();

            // 5. ArrayList에 추가하고
            Dictionary dict = new Dictionary(strTIME, strPenalty, strName);
            //mArrayList.add(0, dict); //첫번째 줄에 삽입됨
            mArrayList.add(dict); //마지막 줄에 삽입됨

            // 6. 어댑터에서 RecyclerView에 반영
            //mAdapter.notifyItemInserted(0);
            mAdapter.notifyDataSetChanged();

            dialog.dismiss();
          }
        });

        dialog.show();
      }
    });
  }

  public void onClick(final View v) {

    if (mArrayList.size() > 0) {

      // 시간이나 이름을 입력하지 않으면 룰렛 못 돌림.
      int mult1=1, mult2=1;
      for (Dictionary dict : mArrayList) {
        mult1 *= dict.getTime().length();
        mult2 *= dict.getName().length();
      }
      if (mult1 != 0 & mult2 != 0) {
        Intent intent = new Intent(MainActivity.this,
                WriteMemo.class);
        intent.putExtra("MArrayList", mArrayList);
        startActivity(intent);
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.action_bar, menu);
    return super.onCreateOptionsMenu(menu);
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    if ((item.getItemId()==R.id.action_share) & (mArrayList.size() > 0)) {
      Intent intent = new Intent(Intent.ACTION_SEND);
      intent.setType("text/plain");
      String text = "{몇 분, 벌칙, 사람}\n";
      for (Dictionary dict : mArrayList) {
        text += "{"+ dict.getTime() + ", " + dict.getPenalty() + ", " + dict.getName() + "}\n";
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

  public void onResume(){
    super.onResume();
    IntentFilter filter = new IntentFilter();
    filter.addAction("android.provider.Telephony.SMS_RECEIVED");
    registerReceiver(receiver, filter);
  }
  public void onPause() {
    super.onPause();
    unregisterReceiver(receiver);
  }
}