package lemus.com.bast_software.controlremoto;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by mekaku on 07/11/2017.
 */

public class DeviceListViewPageAdapter extends FragmentStatePagerAdapter {
    public DeviceListViewPageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return new DeviceListFragment();
    }

    @Override
    public int getCount() {
        return 3;
    }
}
