package com.example.traductor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;
import android.view.FrameStats;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {
    private TabLayout tab_layaut1;
    private ViewPager2 viewpager2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tab_layaut1=findViewById(R.id.tab_layaut1);
        viewpager2=findViewById(R.id.viewpager2);

        viewpager2.setAdapter(new AdaptarFragmentos(getSupportFragmentManager(),getLifecycle()));
        viewpager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                tab_layaut1.selectTab(tab_layaut1.getTabAt(position));
            }
        });
        tab_layaut1.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewpager2.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    class AdaptarFragmentos extends FragmentStateAdapter {


        public AdaptarFragmentos(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case 0:return new english_P();
                case 1:return new france_P();
                default:return new esp_P();
            }
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}