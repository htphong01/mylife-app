package com.htphong.mylife.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.htphong.mylife.Fragments.AccountBasicInformationFragment;
import com.htphong.mylife.R;

public class AccountInformationActivity extends AppCompatActivity implements View.OnClickListener {
    private FragmentManager fragmentManager;
    private ImageButton btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account_information);
        init();
    }

    private void init() {
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameAccountInformationContainer, new AccountBasicInformationFragment()).commit();
        btnBack= findViewById(R.id.account_infor_back_btn);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        super.onBackPressed();
    }
}