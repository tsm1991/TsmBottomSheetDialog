package com.tsm.tsmbottomsheetdialog;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.SeekBar;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tsm.tsmbottomsheetdialog.motion_layout.TsmMotionLayoutActivity;
import com.tsm.tsmbottomsheetdialog.recycler.RecyclerViewActivity;
import com.tsm.tsmbottomsheetdialog.recycler.record.TsmRecordActivity;
import com.tsm.tsmbottomsheetdialog.scroll.ScrollActivity;
import com.tsm.tsmbottomsheetdialog.scroll.TsmBean;
import com.tsm.tsmbottomsheetdialog.skeleton.SkeletonActivity;
import com.tsm.tsmbottomsheetdialog.statusbar.StatusBarUtils;
import com.tsm.tsmbottomsheetdialog.titlebar.TitlebarActivity;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {

    private BaseQuickAdapter<String, BaseViewHolder> adapter;
    private RecyclerView recycler_view_main;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        StatusBarUtils.fitStatusBar(this, true);

        recycler_view_main = findViewById(R.id.recycler_view_main);
        adapter = new BaseQuickAdapter<String, BaseViewHolder>(R.layout.item_simple_test, getList()) {
            @Override
            protected void convert(@NonNull BaseViewHolder baseViewHolder, String s) {
                baseViewHolder.setText(R.id.tv_item, s);
            }
        };

        adapter.setOnItemClickListener(this);
        recycler_view_main.setAdapter(adapter);
        ToastUtils.INSTANCE.showSuccess(this,"????????????");
    }

    private List<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("??????BottomSheetDialog");
        list.add("RecyclerView????????????");
        list.add("RecyclerView????????????");
        list.add("????????????");
        list.add("MotionLayout????????????");
        list.add("Skeleton ?????????");
        list.add("????????????");
        return list;
    }

    @Override
    public void onItemClick(@NonNull BaseQuickAdapter<?, ?> adapter, @NonNull View view, int position) {
        switch (position) {
            case 0:
                new TsmBottomSheetDialog(this).show();
                break;
            case 1:
                startActivity(new Intent(MainActivity.this, RecyclerViewActivity.class));
                break;
            case 2:
                startActivity(new Intent(this, TsmRecordActivity.class));
                break;
            case 3:
                startActivity(new Intent(this, TitlebarActivity.class));
                break;
            case 4:
                startActivity(new Intent(this, TsmMotionLayoutActivity.class));
                break;
            case 5:
                startActivity(new Intent(this, SkeletonActivity.class));
                break;
            case 6:
                startActivity(new Intent(this, ScrollActivity.class));
                break;
        }
    }
}