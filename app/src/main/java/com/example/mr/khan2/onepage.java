package com.example.mr.khan2;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TableLayout;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class onepage extends AppCompatActivity {

    private FirebaseAuth firebaseauth;
    private Toolbar mtoolbar;
    private ViewPager viewPager;
    private SectionsPagerAdapter sectionsPagerAdapter;
    private TabLayout tabLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_onepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.main_app_bar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Whatapp");


        //------------------------------------------------------firebase
        firebaseauth = FirebaseAuth.getInstance();

        if (firebaseauth.getCurrentUser() == null)
        {
            Toast.makeText(onepage.this, "Verify Phone first", Toast.LENGTH_SHORT).show();


            Intent intent = new Intent(onepage.this,MainActivity.class);

            startActivity(intent);
            finish();

        }

        //------------------------------------------------------

        //--------------------creting tabs
        viewPager = (ViewPager) findViewById(R.id.tap_pager);
        sectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        viewPager.setAdapter(sectionsPagerAdapter);
        tabLayout = (TabLayout) findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(viewPager);

    }


    //====================
    public void onClickB(View view)
    {
        firebaseauth.signOut();
        Toast t1 = Toast.makeText(onepage.this,"Signed out",Toast.LENGTH_SHORT);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu,menu);


        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        if(item.getItemId() == R.id.Log_out_item1)
        {
            firebaseauth.signOut();
            Toast t1 = Toast.makeText(onepage.this,"Signed out",Toast.LENGTH_SHORT);

            Intent intent = new Intent(onepage.this,MainActivity.class);

            startActivity(intent);
        }
        else if(item.getItemId() == R.id.main_account_setting)
        {

            Intent intent = new Intent(onepage.this,SettingsActivity.class);

            startActivity(intent);
        }


        else if(item.getItemId() == R.id.menu_delete)
        {
            firebaseauth.getCurrentUser().delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        Toast.makeText(onepage.this,"The account is deleted.",Toast.LENGTH_LONG).show();
                    }
                    else {
                        Toast.makeText(onepage.this,"Account could not be deleted..",Toast.LENGTH_LONG).show();
                    }
                }
            });

            ActivityCompat.finishAffinity(this);
        }

        else if(item.getItemId() == R.id.main_all_users)
        {
            Intent intent = new Intent(onepage.this,InviteFriend.class);
            //  intent.putExtra("Phone1", UserID);

            startActivity(intent);
        }




        return true;
    }
}
