package os.running.leaderboard.app.base;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Martin "Garth" Zander <garth@new-crusader.de>
 */
public abstract class AbstractPagerAdapter extends FragmentStatePagerAdapter
{
    protected int PAGE_COUNT = 0;

    protected Integer tabTitles[] = new Integer[] {};
    
    protected Context context;

    protected Map<Integer, AbstractPagerPage> fragment = new HashMap<Integer, AbstractPagerPage>();

    public AbstractPagerAdapter(FragmentManager fm, Context context)
    {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount()
    {
        return this.PAGE_COUNT;
    }

    @Override
    public abstract Fragment getItem(int position);

    @Override
    public CharSequence getPageTitle(int position)
    {
        // Generate title based on item position
        return context.getString(this.tabTitles[position]);
    }
}
