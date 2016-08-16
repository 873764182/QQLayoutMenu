package com.panxiong.qqlayoumenu.widget;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.panxiong.qqlayoumenu.R;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        // Activity里不需要实现任何管理侧滑菜单的代码
    }
}
