package com.example.personalcenter;

import android.os.Bundle;
import android.view.MenuItem;

import com.example.personalcenter.ui.dashboard.DashboardFragment;
import com.example.personalcenter.ui.home.HomeFragment;
import com.example.personalcenter.ui.notifications.NotificationsFragment;
import com.example.personalcenter.ui.personalCenter.PersonalFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity {

    private HomeFragment homeFragment = new HomeFragment();
    private PersonalFragment personalFragment = new PersonalFragment();
    private NotificationsFragment notificationsFragment = new NotificationsFragment();
    private DashboardFragment dashboardFragment = new DashboardFragment();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        navView.setSelectedItemId(R.id.navigation_home);            //默认选中home页面
        replaceFragment(homeFragment);

        navView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.navigation_home:
                        replaceFragment(homeFragment);
                        break;
                    case R.id.navigation_dashboard:
                        replaceFragment(dashboardFragment);
                        break;
                    case R.id.navigation_personal:
                        replaceFragment(personalFragment);
                        break;
                    case R.id.navigation_notifications:
                        replaceFragment(notificationsFragment);
                        break;
                    default:
                        break;
                }
                return true;
            }
        });



    }


    /**
     * 动态切换fragment
     * @param fragment
     */
    public void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment,fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

}