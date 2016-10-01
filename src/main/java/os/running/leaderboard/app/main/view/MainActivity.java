package os.running.leaderboard.app.main.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.StringRes;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.view.BaseView;
import os.running.leaderboard.app.fragment.LeaderBoard;
import os.running.leaderboard.app.main.presenter.MainPresenter;
import os.running.leaderboard.app.util.Navigation;

public class MainActivity extends AppCompatActivity implements BaseView
{
    private MainPresenter presenter;

    private Toolbar toolbar;
    private NavigationView navigation;
    private DrawerLayout drawerLayout;
    
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        // get components
        toolbar = (Toolbar)findViewById(R.id.toolbar);
        navigation = (NavigationView)findViewById(R.id.navigation);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);

        // create presenter
        presenter = MainPresenter.getInstance();
        presenter.onStart(this);

        // init view
        initToolBar();
        initNavigationMenu();
        configureBackStackListener();
        
        // Initializing default fragment
        Navigation.from(this).to(LeaderBoard.class.getName());
    }

    @Override
    protected void onStart()
    {
        super.onStart();
        presenter.onStart(this);
    }

    @Override
    protected void onStop()
    {
        presenter.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        presenter.onActivityResult(requestCode);
    }

    @Override
    public void onBackPressed()
    {
        presenter.onBackPressed();
    }

    private void initToolBar()
    {
        setSupportActionBar(toolbar);
    }

    private void initNavigationMenu()
    {
        // Setting Navigation Item Listener to handle the item click of the navigation menu
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener()
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

                presenter.onNavigationItemSelected(menuItem.getItemId());

                return true;
            }
        });

        // Initializing Drawer Layout and ActionBarToggle
        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(
                this, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        );
        actionBarDrawerToggle.syncState();
        drawerLayout.setDrawerListener(actionBarDrawerToggle);

        TextView text = (TextView)this.findViewById(R.id.username);
        text.setText(R.string.default_login);
        text.setClickable(true);
        text.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                presenter.onLoginClick(view.getContext());
            }
        });

        setUsername(R.string.default_login);
        setAvatar();

        // open login
        presenter.onLoginClick(this.getBaseContext());
    }

    private void configureBackStackListener()
    {
        FragmentManager fm = getSupportFragmentManager();
        fm.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener()
        {
            @Override
            public void onBackStackChanged()
            {
                presenter.onBackStackChanged();
            }
        });
    }

    public void showSnackBar(@StringRes int message)
    {
        Snackbar.make(this.findViewById(R.id.drawer_layout), message, Snackbar.LENGTH_LONG).show();
    }

    public boolean isDrawerOpen()
    {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    public void closeDrawer()
    {
        drawerLayout.closeDrawers();
    }

    public void useBackStack()
    {
        if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
            finish();
        }
    }

    public void setUsername(@StringRes int username)
    {
        setUsername(getString(username));
    }

    public void setUsername(String Username)
    {
        ((TextView)this.findViewById(R.id.username)).setText(Username);
    }

    public void setAvatar()
    {
        CircleImageView image = (CircleImageView)this.findViewById(R.id.profile_image);
        image.setImageResource(R.drawable.default_profile);
    }

    public void setAvatar(String avatarUri)
    {
        CircleImageView image = (CircleImageView)this.findViewById(R.id.profile_image);
        Picasso.with(this).load(avatarUri).placeholder(R.drawable.default_profile).into(image);
    }
}