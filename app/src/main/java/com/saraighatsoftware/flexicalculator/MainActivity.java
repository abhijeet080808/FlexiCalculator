package com.saraighatsoftware.flexicalculator;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.TypefaceSpan;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FragmentPager fragment_pager =
                new FragmentPager(getSupportFragmentManager(), getApplicationContext());
        ViewPager view_pager = (ViewPager) findViewById(R.id.page_container);
        view_pager.setAdapter(fragment_pager);
        CustomTabLayout tabLayout = (CustomTabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(view_pager);

        ActionBar action_bar  = getSupportActionBar();
        if (action_bar != null) {
            String title = getString(R.string.toolbar_name);
            SpannableString ss = new SpannableString(title);
            TypefaceSpan type_span = new CustomTypefaceSpan(FontCache.GetSemiBold(getApplicationContext()));
            ss.setSpan(type_span, 0, title.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
            action_bar.setTitle(ss);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
