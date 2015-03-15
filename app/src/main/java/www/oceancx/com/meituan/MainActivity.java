package www.oceancx.com.meituan;

import android.os.Debug;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;


public class MainActivity extends ActionBarActivity {

    Toolbar mToolbar;

    Spinner spinner1;
    Spinner spinner2;
    Spinner spinner3;
    Spinner spinner4;

    SpinnerAdapter spinnerAdapter1;
    SpinnerAdapter spinnerAdapter2;
    SpinnerAdapter spinnerAdapter3;
    SpinnerAdapter spinnerAdapter4;

    PopupWindow popupWindow1;
    Button button1;
    ListView left_lv;
    ListView right_lv;
    ListView list;

    class Item {
        public String title;
        public int type;
        public int count;
        public boolean showed;
        public ArrayList<String> contents;
    }

    private ArrayList<Item> items;

    private BaseAdapter adapter = new BaseAdapter() {
        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Item getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Item item=getItem(position);

            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.list_item, null);
                ((LinearLayout) convertView).getChildAt(1).setTag(0);

            }

            TextView header= (TextView) convertView.findViewById(R.id.header);
            header.setText(item.title);


            if( item.count > 2 && !item.showed){
                Button add = (Button) convertView.findViewById(R.id.add);
                add.setText("展开其余" + (item.count - 2) + "项");
                add.setVisibility(View.VISIBLE);
                add.setTag((item));

                LinearLayout ll = (LinearLayout)convertView.findViewById(R.id.ll);
                ll.removeAllViews();
                for(int i=1;i<3;i++){
                    TextView tv=new TextView(MainActivity.this);
                    tv.setText("这是第" + i + "项");
                    ll.addView(tv);
                }
                ll.setTag(2);

                add.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //被点击的话 就展开剩余的项数
                        Button bt = (Button)v;
                        Item im= (Item) bt.getTag();
                        im.showed = true;

                        // 展开这些项
                        LinearLayout ll = (LinearLayout) ((LinearLayout) bt.getParent()).getChildAt(1);
                        int llct= (int) ll.getTag();

                        for(int i=0;i<im.count-2;i++){
                            TextView tv=new TextView(MainActivity.this);
                            tv.setText("这是第" + (i+3) + "项");
                            ll.addView(tv);
                            llct++;
                        }
                        ll.setTag(llct);
                        bt.setVisibility(View.GONE);
                    }
                });
            }else {
                //BT 已经点过了
                Button bt= (Button) convertView.findViewById(R.id.add);
                bt.setVisibility(View.GONE);
                LinearLayout ll = (LinearLayout) convertView.findViewById(R.id.ll);
                ll.removeAllViews();

                for(int i=0;i<item.count;i++){
                    TextView tv=new TextView(MainActivity.this);
                    tv.setText("这是第"+(i+1)+"项");
                    ll.addView(tv);
                }
            }


            return convertView;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        spinner2 = (Spinner) findViewById(R.id.spinner2);
        spinner3 = (Spinner) findViewById(R.id.spinner3);
        spinner4 = (Spinner) findViewById(R.id.spinner4);

//        ArrayAdapter<CharSequence> spinnerAdapter1 = ArrayAdapter.createFromResource(this, R.array.movie, android.R.layout.simple_spinner_item);
//        spinnerAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout);
//        spinner1.setAdapter(spinnerAdapter1);

        //  spinnerAdapter1.setDropDownViewResource(R.layout.spinner_dropdown_layout);

        popupWindow1 = new PopupWindow(this);
        popupWindow1.setWindowLayoutMode(View.MeasureSpec.makeMeasureSpec(1080, View.MeasureSpec.AT_MOST), View.MeasureSpec.makeMeasureSpec(1000, View.MeasureSpec.AT_MOST));
        View v = getLayoutInflater().inflate(R.layout.spinner_dropdown_layout, null);
        popupWindow1.setContentView(v);
        left_lv = (ListView) v.findViewById(R.id.left_lv);
        right_lv = (ListView) v.findViewById(R.id.right_lv);

        final ArrayAdapter<CharSequence> left_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, android.R.id.text1, Arrays.asList(getResources().getTextArray(R.array.movie)));
        left_lv.setAdapter(left_adapter);


        final ArrayAdapter<CharSequence> right_adapter = new ArrayAdapter<CharSequence>(this, android.R.layout.simple_list_item_1, android.R.id.text1);
        right_lv.setAdapter(right_adapter);
        left_lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 3) {
                    //重新生成 新的adpter 然后填充来另外一个表里面
                    button1.setText(left_adapter.getItem(3));
                    right_adapter.clear();
                    right_adapter.addAll(getResources().getTextArray(R.array.nearest));
                    right_adapter.notifyDataSetChanged();
                } else if (position == 4) {
                    button1.setText(left_adapter.getItem(4));
                    right_adapter.clear();
                    right_adapter.addAll(getResources().getTextArray(R.array.movie));
                    right_adapter.notifyDataSetChanged();
                } else {
                    button1.setText(left_adapter.getItem(position));
                    right_adapter.clear();
                    right_adapter.notifyDataSetChanged();
                }
            }
        });

        button1 = (Button) findViewById(R.id.button1);


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //显示popupwindow
                if (popupWindow1.isShowing()) {
                    popupWindow1.dismiss();
                    return;
                }
                DebugLog.e("show as drop down");
                popupWindow1.showAsDropDown(button1);
            }
        });


        ArrayAdapter<CharSequence> spinnerAdapter2 = ArrayAdapter.createFromResource(this, R.array.whole_city_cbd, android.R.layout.simple_spinner_item);
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner2.setAdapter(spinnerAdapter2);

        ArrayAdapter<CharSequence> spinnerAdapter3 = ArrayAdapter.createFromResource(this, R.array.nearest, android.R.layout.simple_spinner_item);
        spinnerAdapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner3.setAdapter(spinnerAdapter3);

        ArrayAdapter<CharSequence> spinnerAdapter4 = ArrayAdapter.createFromResource(this, R.array.select, android.R.layout.simple_spinner_item);
        spinnerAdapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner4.setAdapter(spinnerAdapter4);


        items = new ArrayList<Item>();


        Item a1 = new Item();
        a1.count = 7;
        a1.showed = false;
        a1.contents=new ArrayList<String>();
        a1.title ="牛逼";

        Item a2 = new Item();
        a2.count = 5;
        a2.showed = false;
        a2.contents=new ArrayList<String>();
        a2.title="哄哄";


        String[] counts=getResources().getStringArray(R.array.counts);
        String[] titles=getResources().getStringArray(R.array.whole_city_cbd);

        for(int i=0;i<15;i++){
            Item it=new Item();
            it.count= Integer.valueOf(counts[i]);
            it.showed=false;
            it.title =titles[i];
            items.add(it);
        }



        list = (ListView) findViewById(R.id.list);
        list.setAdapter(adapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
