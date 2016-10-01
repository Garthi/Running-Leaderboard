package os.running.leaderboard.app.base.presenter;

import os.running.leaderboard.app.base.view.BaseView;

public interface BasePresenter
{
    void onStart(BaseView view);

    void onStop();
}
