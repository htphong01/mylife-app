package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.htphong.mylife.Fragments.TaskManagementUserFragment;
import com.htphong.mylife.R;

public class TaskManagementActivity extends AppCompatActivity {

    private FragmentManager fragmentManager;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_management);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameTaskManagementContainer, new TaskManagementUserFragment()).commit();
        init();
    }

    private void init() {


        btnBack = findViewById(R.id.task_management_back_btn);
        btnBack.setOnClickListener(v -> {
            startActivity(new Intent(TaskManagementActivity.this, ChattingSettingActivity.class));
        });
    }
}