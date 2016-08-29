package com.panxiong.qqlayoumenu.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.panxiong.qqlayoumenu.R;

public class TestActivity extends AppCompatActivity {
    private LeftMenuLayout mLeftMenuLayout;
    private Button mBtTestClickLeft;
    private Button mBtTestClickRight;
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        mLeftMenuLayout = (LeftMenuLayout) findViewById(R.id.leftMenuLayout);
        mBtTestClickLeft = (Button) findViewById(R.id.btTestClickLeft);
        mBtTestClickRight = (Button) findViewById(R.id.btTestClickRight);
        mListView = (ListView) findViewById(R.id.listView);

        mBtTestClickLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftMenuLayout.closeMenu();
            }
        });
        mBtTestClickRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mLeftMenuLayout.openMenu();
            }
        });

        mLeftMenuLayout.setMenuChangedListener(new LeftMenuLayout.MenuChangedListener() {
            @Override
            public void onChanged(boolean isOpen) {
                Log.e("TestActivity", "抽屉是否打开: " + isOpen);
            }
        });

        String[] arr = new String[20];
        for (int i = 0; i < 20; i++) {
            arr[i] = "item " + i;
        }
        ArrayAdapter arrayAdapter = new ArrayAdapter(
                this, android.R.layout.simple_expandable_list_item_1, arr);
        mListView.setAdapter(arrayAdapter);
    }
}
