package os.running.leaderboard.app;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.NavigationView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;
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
            Log.d("lb", "group id: " + menuItem.getGroupId() + " R.id: " + R.id.group_settings);
            // Checking if the item is in checked state or not, if not make it in checked state
            if (menuItem.getGroupId() != R.id.group_settings) {
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
            }

            // Closing drawer on item click
            drawerLayout.closeDrawers();

            // Check to see which item was being clicked and perform appropriate action
            switch (menuItem.getItemId()) {

                // Replacing the main content with ContentFragment Which is our Inbox View;
                case R.id.menu_leader_board:

                    Leaderboard fragment = Leaderboard.newInstance();
                    android.support.v4.app.FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                    fragmentTransaction.replace(R.id.content, fragment);
                    fragmentTransaction.addToBackStack(null);
                    fragmentTransaction.commit();
                    
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
