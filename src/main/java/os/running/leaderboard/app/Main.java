package os.running.leaderboard.app;

import android.annotation.TargetApi;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import com.squareup.picasso.Picasso;
import de.hdodenhof.circleimageview.CircleImageView;
import os.running.leaderboard.app.base.Database;
import os.running.leaderboard.app.base.Runtastic;
import os.running.leaderboard.app.fragment.Friends;
import os.running.leaderboard.app.fragment.LeaderBoard;
import os.running.leaderboard.app.fragment.LiveSessions;
import os.running.leaderboard.app.fragment.PumpIt;

public class Main extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    public static AppCompatActivity activity;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        Main.activity = this;
        
        // Initializing Toolbar and setting it as the actionbar
        Toolbar toolbar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        
        // Setting Navigation Item Listener to handle the item click of the navigation menu
        NavigationView navigation = (NavigationView)findViewById(R.id.navigation);
        navigation.setNavigationItemSelectedListener(new NavigationItemListener());

        // Initializing Drawer Layout and ActionBarToggle
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        updateNavigationHeader();
        
        // Initializing default fragment
        LeaderBoard fragment = new LeaderBoard();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
        {
            @Override
            public void onBackStackChanged()
            {
                if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
                    drawerLayout.closeDrawers();
                } else {
                    if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                        finish();
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        
        if (requestCode == 1) {
            // update navigation header
            updateNavigationHeader(false);
        }
    }

    private void updateNavigationHeader()
    {
        updateNavigationHeader(true);
    }
    
    private void updateNavigationHeader(Boolean withLogin)
    {
        // update navigation header
        final Runtastic runtastic = new Runtastic(this);
        if (runtastic.hasLogin()) {
            /*new Thread(new Runnable() {
                public void run() {
                    // get session with automatic login
                    runtastic.login(true);
                }
            }).start();*/

            Database DB = Database.getInstance(this);

            String accountData = DB.getAccountData("firstName");
            if (accountData != null) {
                ((TextView)this.findViewById(R.id.username)).setText(accountData);
            }

            accountData = DB.getAccountData("avatarUrl");
            if (accountData != null) {
                CircleImageView image = (CircleImageView)this.findViewById(R.id.profile_image);
                Picasso.with(this).load(accountData).placeholder(R.drawable.default_profile).into(image);
            }
        } else {

            TextView text = (TextView)this.findViewById(R.id.username);
            text.setText(R.string.default_login);
            text.setClickable(true);
            text.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    Intent intent = new Intent(view.getContext(), Login.class);
                    startActivityForResult(intent, 1);
                }
            });
            ((CircleImageView)this.findViewById(R.id.profile_image)).setImageResource(R.drawable.default_profile);

            if (withLogin) {
                Intent intent = new Intent(this, Login.class);
                startActivityForResult(intent, 1);
            }

        }
    }
    private class NavigationItemListener implements NavigationView.OnNavigationItemSelectedListener
    {
        @Override
        public boolean onNavigationItemSelected(MenuItem menuItem)
        {
            // Checking if the item is in checked state or not, if not make it in checked state
            if (menuItem.isChecked()) {
                menuItem.setChecked(false);
            } else {
                menuItem.setChecked(true);
            }

            // Closing drawer on item click
            drawerLayout.closeDrawers();
            
            android.support.v4.app.FragmentTransaction fragmentTransaction;
            
            // Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {

                // Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.menu_leader_board:

                    LeaderBoard leaderBoard = new LeaderBoard();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, leaderBoard);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    
                    return true;
                case R.id.menu_sessions:

                    LiveSessions liveSessions = new LiveSessions();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, liveSessions);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    
                    return true;
                case R.id.menu_pump_it:

                    PumpIt pumpIt = new PumpIt();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, pumpIt);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();

                    return true;
                case R.id.menu_friends:
                    
                    Friends friends = new Friends();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, friends);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    
                    return true;
                case R.id.menu_runtastic_apps:

                    Uri appUrl = Uri.parse("https://play.google.com/store/apps/dev?id=8438666261259599516");
                    Intent appsIntent = new Intent(Intent.ACTION_VIEW, appUrl);
                    if (appsIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(appsIntent);
                    }
                    
                    return true;
                case R.id.menu_runtastic_url:

                    Uri pageUrl = Uri.parse("http://www.runtastic.com");
                    Intent pageIntent = new Intent(Intent.ACTION_VIEW, pageUrl);
                    if (pageIntent.resolveActivity(getPackageManager()) != null) {
                        startActivity(pageIntent);
                    }
                    
                    return true;
                /*case R.id.menu_settings:
                    
                    // default output TODO add a settings view
                    Toast.makeText(getApplicationContext(), "Coming soon ...", Toast.LENGTH_LONG).show();
                    
                    return true;*/
                default:
                    
                    return true;
            }
        }
    }

    @Override @TargetApi(9)
    public void onBackPressed()
    {
        if (drawerLayout.isDrawerOpen(Gravity.LEFT)) {
            drawerLayout.closeDrawers();
        } else {
            getSupportFragmentManager().popBackStack();
        }
    }
}
