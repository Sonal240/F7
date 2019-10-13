package com.example.rohan.f7;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.browser.customtabs.CustomTabsIntent;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rohan.f7.Fragments.Fri;
import com.example.rohan.f7.Fragments.Mon;
import com.example.rohan.f7.Fragments.Sat;
import com.example.rohan.f7.Fragments.Thu;
import com.example.rohan.f7.Fragments.Tue;
import com.example.rohan.f7.Fragments.Wed;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    DatabaseReference databaseReference;

    AdView adView;


    private SectionsPagerAdapter mSectionsPagerAdapter;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        adView = findViewById(R.id.bannerAd);
        MobileAds.initialize(this, "ca-app-pub-3940256099942544~3347511713");

        AdRequest adRequest= new AdRequest.Builder().build();
        adView.loadAd(adRequest);

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());


        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));

        Calendar c = Calendar.getInstance();
        int dayofweek = c.get(Calendar.DAY_OF_WEEK);
        if(dayofweek==1 || dayofweek==2)
        {
            mViewPager.setCurrentItem(0,true);
        }
        else{
            mViewPager.setCurrentItem(dayofweek-2,true);
        }


        databaseReference = FirebaseDatabase.getInstance().getReference();
        final TinyDB tinyDB= new TinyDB(this);
        databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    GenericTypeIndicator<ArrayList<ArrayList<SubjectDetails>>> t = new GenericTypeIndicator<ArrayList<ArrayList<SubjectDetails>>>() {
                    };

                    if (tinyDB.getSubjectDetails("SEMESTER_5")==null)
                    {
                        tinyDB.putSubjectDetails("SEMESTER_5",dataSnapshot.child("SEMESTER_5").getValue(t));

                    }else if ((tinyDB.getSubjectDetails("SEMESTER_5")!=dataSnapshot.child("SEMESTER_5").getValue(t))){
                        tinyDB.putSubjectDetails("SEMESTER_5",dataSnapshot.child("SEMESTER_5").getValue(t));

                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {


                }
            });


        if (tinyDB.getSubjectDetails("SEMESTER_5")!=null)
        {
            //Toast.makeText(this, ""+tinyDB.getSubjectDetails("SEMESTER_5").get(0).get(0).getSubjectName(), Toast.LENGTH_SHORT).show();
        }

        if (tinyDB.getChoices("ELECTIVES")==null){
            Toast.makeText(this, "CHOOSE ELECTIVES", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(MainActivity.this, ChooseElective.class));
            finish();

        }


    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.menu_main2, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


        if (id == R.id.action_settings) {

            CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
            CustomTabsIntent customTabsIntent = builder.build();
            customTabsIntent.launchUrl(this, Uri.parse("https://webkiosk.jiit.ac.in/"));
        }
        if (id==R.id.changeElective){
           startActivity(new Intent(MainActivity.this, ChooseElective.class));
           finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public static class PlaceholderFragment extends Fragment {

        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }


        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }


    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            if(position==0)
            {
                Mon t1= new Mon();
                return t1;
            }
            else if (position==1)
            {
                Tue t2= new Tue();
                return t2;
            }
            else if (position==2)
            {
                Wed t3= new Wed();
                return t3;
            }
            else if (position==3)
            {
                Thu t4= new Thu();
                return t4;
            }
            else if (position==4)
            {
                Fri t5= new Fri();
                return t5;
            }
            else if (position==5)
            {
                Sat t6= new Sat();
                return t6;
            }

            return null;
        }

        @Override
        public int getCount() {

            return 6;
        }
    }

}
