package os.running.leaderboard.app.main.presenter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.StringRes;

import os.running.leaderboard.app.Login;
import os.running.leaderboard.app.R;
import os.running.leaderboard.app.base.Database;
import os.running.leaderboard.app.base.Runtastic;
import os.running.leaderboard.app.base.presenter.BasePresenter;
import os.running.leaderboard.app.base.view.BaseView;
import os.running.leaderboard.app.fragment.LeaderBoard;
import os.running.leaderboard.app.fragment.LiveSessions;
import os.running.leaderboard.app.friends.view.FriendsFragment;
import os.running.leaderboard.app.main.view.MainActivity;
import os.running.leaderboard.app.util.Navigation;

/**
 * presenter of main activity
 * Created by garth_000 on 25.09.2016.
 */
public class MainPresenter implements BasePresenter
{
    private static MainPresenter instance;

    private MainActivity view;

    private MainPresenter()
    {}

    public static MainPresenter getInstance()
    {
        if (instance == null) {
            instance = new MainPresenter();
        }

        return instance;
    }

    @Override
    public void onStart(BaseView view)
    {
        this.view = (MainActivity)view;
        updateUi();
    }

    @Override
    public void onStop()
    {
        this.view = null;
    }

    public void onBackStackChanged()
    {
        if (this.view == null) {
            return;
        }

        if (this.view.isDrawerOpen()) {
            this.view.closeDrawer();
        } else {
            this.view.useBackStack();
        }
    }

    public void onBackPressed()
    {
        if (this.view.isDrawerOpen()) {
            this.view.closeDrawer();
        } else {
            this.view.getSupportFragmentManager().popBackStack();
        }
    }

    public void createMessage(@StringRes int message)
    {
        this.view.showSnackBar(message);
    }

    public void onActivityResult(int requestCode)
    {

        // TODO: add the data on local in presenter

        if (requestCode == 1) {
            updateUi();
        }
    }

    private void updateUi()
    {
        if (this.view == null) {
            return;
        }

        final Runtastic runtastic = new Runtastic(this.view);
        if (runtastic.hasLogin()) {
            Database DB = Database.getInstance(this.view);

            String accountData = DB.getAccountData("firstName");
            if (accountData != null) {
                this.view.setUsername(accountData);
            }

            accountData = DB.getAccountData("avatarUrl");
            if (accountData != null) {
                this.view.setAvatar(accountData);
            }
        } else {
            this.view.setUsername(R.string.default_login);
            this.view.setAvatar();
        }
    }

    public void onLoginClick(Context context)
    {
        if (this.view == null) {
            return;
        }

        Intent intent = new Intent(context, Login.class);
        this.view.startActivityForResult(intent, 1);
    }

    public void onNavigationItemSelected(@IdRes int menuId)
    {
        this.view.closeDrawer();

        switch (menuId) {

            // Replacing the main content with ContentFragment Which is our Inbox View;
            case R.id.menu_leader_board:

                // TODO add a back stack reset
                Navigation.from(this.view).replace(LeaderBoard.class.getName());

                break;
            case R.id.menu_sessions:

                // TODO add a back stack reset
                Navigation.from(this.view).replace(LiveSessions.class.getName());

                break;
            case R.id.menu_friends:

                // TODO add a back stack reset
                Navigation.from(this.view).replace(FriendsFragment.class.getName());

                break;
            case R.id.menu_runtastic_apps:

                // TODO add to navigation class

                Uri appUrl = Uri.parse("https://play.google.com/store/apps/dev?id=8438666261259599516");
                Intent appsIntent = new Intent(Intent.ACTION_VIEW, appUrl);
                if (appsIntent.resolveActivity(this.view.getPackageManager()) != null) {
                    this.view.startActivity(appsIntent);
                }

                break;
            case R.id.menu_runtastic_url:

                // TODO add to navigation class

                Uri pageUrl = Uri.parse("http://www.runtastic.com");
                Intent pageIntent = new Intent(Intent.ACTION_VIEW, pageUrl);
                if (pageIntent.resolveActivity(this.view.getPackageManager()) != null) {
                    this.view.startActivity(pageIntent);
                }

                break;
                /*case R.id.menu_settings:

                    // default output TODO add a settings view
                    Toast.makeText(getApplicationContext(), "Coming soon ...", Toast.LENGTH_LONG).show();

                    return true;*/
            default:

                break;
        }
    }
}
