package com.tsm.tsmbottomsheetdialog;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        findViewById(R.id.tv_show_bottom_sheet).setOnClickListener(view -> {
            new TsmBottomSheetDialog(this).show();
        });
    }
}