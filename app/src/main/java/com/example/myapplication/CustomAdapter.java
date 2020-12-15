package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder> {

  private ArrayList<Dictionary> mList;
  private Context mContext;

  // 커스텀 ViewHolder class를 생성하자.
  public class CustomViewHolder extends RecyclerView.ViewHolder
          implements View.OnCreateContextMenuListener {
    protected TextView time;
    protected TextView penalty;
    protected TextView name;

    // Constructor
    // 복제하고 싶은 item_list 레이아웃이 3개의 텍스트뷰를 가졌으므로 마찬가지로 3개의 텍스트뷰를 생성하고 각각 findViewById로 찾아서 assign한다.
    public CustomViewHolder(View view) {
      super(view);
      this.time = (TextView) view.findViewById(R.id.time_listitem);
      this.penalty = (TextView) view.findViewById(R.id.penalty_listitem);
      this.name = (TextView) view.findViewById(R.id.name_listitem);

      view.setOnCreateContextMenuListener(this); //2. OnCreateContextMenuListener 리스너를 현재 클래스에서 구현한다고 설정
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
      // 3. 컨텍스트 메뉴를 생성하고 메뉴 항목 선택시 호출되는 리스너를 등록. ID 1001, 1002로 어떤 메뉴를 선택했는지 리스너에서 구분

      MenuItem Edit = menu.add(Menu.NONE, 1001, 1, "편집");
      MenuItem Delete = menu.add(Menu.NONE, 1002, 2, "삭제");
      Edit.setOnMenuItemClickListener(onEditMenu);
      Delete.setOnMenuItemClickListener(onEditMenu);
    }

    // 4. 컨텍스트 메뉴에서 항목 클릭시 동작을 설정
    private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {
      @Override
      public boolean onMenuItemClick(MenuItem item) {


        switch (item.getItemId()) {
          case 1001:  // 5. 편집 항목을 선택시
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);

            // 다이얼로그를 보여주기 위해 edit_box.xml 파일을 사용
            View view = LayoutInflater.from(mContext)
                    .inflate(R.layout.edit_box, null, false);
            builder.setView(view);
            final Button ButtonSubmit = (Button) view.findViewById(R.id.button_dialog_submit);
            final EditText editTextTime = (EditText) view.findViewById(R.id.edittext_time);
            final EditText editTextPenalty = (EditText) view.findViewById(R.id.edittext_penalty);
            final EditText editTextName = (EditText) view.findViewById(R.id.edittext_name);

            // 6. 해당 줄에 입력되어 있던 데이터를 불러와서 다이얼로그에 보여줌
            editTextTime.setText(mList.get(getAdapterPosition()).getTime());
            editTextPenalty.setText(mList.get(getAdapterPosition()).getPenalty());
            editTextName.setText(mList.get(getAdapterPosition()).getName());

            final AlertDialog dialog = builder.create();
            ButtonSubmit.setOnClickListener(new View.OnClickListener() {

              // 7. 수정 버튼을 클릭하면 현재 UI에 입력되어 있는 내용으로
              public void onClick(View v) {
                String strID = editTextTime.getText().toString();
                String strEnglish = editTextPenalty.getText().toString();
                String strKorean = editTextName.getText().toString();

                Dictionary dict = new Dictionary(strID, strEnglish, strKorean);

                // 8. ArrayList에 있는 데이터를 변경
                mList.set(getAdapterPosition(), dict);

                // 9. 어댑터에서 RecyclerView에 반영
                notifyItemChanged(getAdapterPosition());

                dialog.dismiss();
              }
            });

            dialog.show();

            break;

          case 1002:

            mList.remove(getAdapterPosition());
            notifyItemRemoved(getAdapterPosition());
            notifyItemRangeChanged(getAdapterPosition(), mList.size());

            break;

        }
        return true;
      }
    };
  }

  // 3가지 Override Method를 작성
  // 먼저 onCreateViewHolder. ViewGroup parent는 xml에서 만들어 놓은 recyclerView다.
  public CustomViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    // recyclerView(parent) 내에 새로운 view를 만들자.
    View view = LayoutInflater.from(viewGroup.getContext())
            .inflate(R.layout.item_list, viewGroup, false);

    // 해당 뷰를 이용해 뷰홀더 인스턴스를 만든다.
    // 이를 통해 뷰홀더 내 textView와 item_list내 textView가 연결된다.
    // 만들어진 뷰홀더를 반환한다.
    CustomViewHolder viewHolder = new CustomViewHolder(view);

    return viewHolder;
  }

  // onBindViewHolder를 통해 데이터를 set한다.
  @Override
  public void onBindViewHolder(@NonNull CustomViewHolder viewholder, int position) {

    viewholder.time.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    viewholder.penalty.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);
    viewholder.name.setTextSize(TypedValue.COMPLEX_UNIT_SP, 25);

    viewholder.time.setGravity(Gravity.CENTER);
    viewholder.penalty.setGravity(Gravity.CENTER);
    viewholder.name.setGravity(Gravity.CENTER);

    viewholder.time.setText(mList.get(position).getTime());
    viewholder.penalty.setText(mList.get(position).getPenalty());
    viewholder.name.setText(mList.get(position).getName());
  }

  // adapter에 의해 가장 먼저 실행될 메쏘드.
  // 아이템의 개수를 반환한다.
  @Override
  public int getItemCount() {
    return (null != mList ? mList.size() : 0);
  }

  // 커스텀 어답터 생성자.
  public CustomAdapter(Context context, ArrayList<Dictionary> list) {
    this.mList = list;
    mContext = context;
  }
}