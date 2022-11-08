package com.tsm.tsmbottomsheetdialog;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.listener.OnItemClickListener;
import com.chad.library.adapter.base.viewholder.BaseViewHolder;
import com.tsm.tsmbottomsheetdialog.drawable.DrawableActivity;
import com.tsm.tsmbottomsheetdialog.motion_layout.TsmMotionLayoutActivity;
import com.tsm.tsmbottomsheetdialog.recycler.RecyclerViewActivity;
import com.tsm.tsmbottomsheetdialog.scroll.ScrollActivity;
import com.tsm.tsmbottomsheetdialog.skeleton.SkeletonActivity;
import com.tsm.tsmbottomsheetdialog.statusbar.StatusBarUtils;
import com.tsm.tsmbottomsheetdialog.titlebar.TitlebarActivity;

import java.util.ArrayList;
import java.util.List;

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
//        ToastUtils.INSTANCE.showSuccess(this,"成功消息");
    }

    private List<String> getList() {
        ArrayList<String> list = new ArrayList<>();
        list.add("使用BottomSheetDialog");
        list.add("RecyclerView同步背景");
        list.add("组件化Drawable属性使用");
        list.add("");
        list.add("MotionLayout运动布局");
        list.add("Skeleton 骨架屏");
        list.add("嵌套滑动");
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
                startActivity(new Intent(MainActivity.this, DrawableActivity.class));
                break;
            case 3:
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