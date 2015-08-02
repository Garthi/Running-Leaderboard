package os.running.leaderboard.app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;
import os.running.leaderboard.app.fragment.Friends;
import os.running.leaderboard.app.fragment.Leaderboard;

public class Main extends AppCompatActivity
{
    private DrawerLayout drawerLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
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
        
        // Initializing default fragment
        Leaderboard fragment = Leaderboard.newInstance();
        android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.add(R.id.content, fragment);
        fragmentTransaction.commit();
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

                    Leaderboard leaderboard = Leaderboard.newInstance();
                    fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, leaderboard);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    
                    return true;
                case R.id.menu_friends:
                    
                    Friends friends = Friends.newInstance();
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
                case R.id.menu_settings:
                    
                    // default output TODO add a settings view
                    Toast.makeText(getApplicationContext(), "open settings", Toast.LENGTH_SHORT).show();
                    
                    return true;
                default:
                    
                    return true;
            }
        }
    }
}
