package com.htphong.mylife;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.htphong.mylife.Fragments.AccountFragment;
import com.htphong.mylife.Fragments.AddFriendInvitationFragment;
import com.htphong.mylife.Fragments.HomeFragment;
import com.htphong.mylife.Fragments.ListChatRoomFragment;
import com.htphong.mylife.Fragments.MenuFragment;
import com.htphong.mylife.Fragments.NotificationFragment;

public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private FragmentManager fragmentManager;
    private FloatingActionButton fab;
    private BottomNavigationView bottomNav;
    private ImageButton btnSearch, btnMessage, btnMenu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new HomeFragment()).commit();
        init();
    }

    public void init() {
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddPostActivity.class);
                startActivity(intent);
            }
        });
        bottomNav = findViewById(R.id.bottom_nav);
        bottomNav.setOnNavigationItemSelectedListener(this);

        btnSearch = findViewById(R.id.btn_search);
        btnMessage = findViewById(R.id.btn_message);
        btnMenu = findViewById(R.id.btn_menu);
        btnSearch.setOnClickListener(this);
        btnMessage.setOnClickListener(this);
        btnMenu.setOnClickListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.item_home: {
                if(bottomNav.getMenu().getItem(0).isChecked()) {
                    HomeFragment.recyclerView.smoothScrollToPosition(0);
                } else {
                    bottomNav.getMenu().getItem(0).setChecked(true);
                    fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new HomeFragment()).commit();
                }
                break;
            }
            case R.id.item_add_friend: {
                bottomNav.getMenu().getItem(1).setChecked(true);
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new AddFriendInvitationFragment()).commit();
                break;
            }
            case R.id.item_notification: {
                bottomNav.getMenu().getItem(3).setChecked(true);
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new NotificationFragment()).commit();
                break;
            }
            case R.id.item_account: {
                bottomNav.getMenu().getItem(4).setChecked(true);
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new AccountFragment()).commit();
                break;
            }
        }

        return false;
    }

    private void unCheckMenuItem() {
        bottomNav.getMenu().getItem(2).setChecked(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_search: {
                unCheckMenuItem();
                startActivity(new Intent(HomeActivity.this, SearchActivity.class));
                break;
            }

            case R.id.btn_message: {
                unCheckMenuItem();
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new ListChatRoomFragment()).commit();
                break;
            }

            case R.id.btn_menu: {
                unCheckMenuItem();
                fragmentManager.beginTransaction().replace(R.id.frameHomeContainer, new MenuFragment()).commit();
            }
        }
    }
}