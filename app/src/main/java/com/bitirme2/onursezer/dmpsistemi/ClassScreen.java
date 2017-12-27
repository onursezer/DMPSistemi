package com.bitirme2.onursezer.dmpsistemi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;

public class ClassScreen extends AppCompatActivity {

    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_class_screen);

     /*   Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("sezer");
        setSupportActionBar(toolbar);*/

        Gson gS = new Gson();
        String target = getIntent().getStringExtra("CLASS");
        ClassBean classBean = gS.fromJson(target, ClassBean.class);
        String userBean = getIntent().getStringExtra("USER");
        User user = gS.fromJson(userBean, User.class);
        String status = getIntent().getStringExtra("STATUS");
        String userName = null;
        if(status.equals("0"))
        {
            userName = user.getName() + " " + user.getSurname() + "  [Öğretmen]";
        }
        else
            userName = user.getName() + " " + user.getSurname();

        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager(),userName,classBean.getClassId(),
                classBean.getTeacher().getName() + " " + classBean.getTeacher().getSurname(), classBean.getTeacher().getEmail(), status, userBean);

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.container);
        mViewPager.setAdapter(mSectionsPagerAdapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(mViewPager);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_class_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
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
            View rootView = inflater.inflate(R.layout.fragment_class_screen, container, false);
            TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));
            return rootView;
        }
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        String name,id, teacherName, teacherMail, status,userBean;

        public SectionsPagerAdapter(FragmentManager fm, String name,String id, String teacherName, String teacherMail, String status,String userBean) {
            super(fm);
            this.name = name;
            this.id = id;
            this.teacherMail = teacherMail;
            this.teacherName = teacherName;
            this.status = status;
            this.userBean = userBean;
        }

        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    System.out.println("tab1");
                    return  Tab1.newInstance(name, id);
                case 1:
                    System.out.println("tab2");
                    return  Tab2.newInstance(id,status,userBean);
                case 2:
                    System.out.println("tab3");
                    return  Tab3.newInstance( id, teacherName, teacherMail );
            }
            return null;
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "Sohbet";
                case 1:
                    return "Ödev";
                case 2:
                    return "Hakkında";
            }
            return null;
        }
    }
}