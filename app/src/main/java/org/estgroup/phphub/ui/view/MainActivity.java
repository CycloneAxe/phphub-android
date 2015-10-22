package org.estgroup.phphub.ui.view;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.common.service.NotificationService;
import org.estgroup.phphub.ui.view.topic.TopicsFragment;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private NotificationService notificationService;

    private ServiceConnection serviceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            System.out.println("onServiceConnected()");
            NotificationService.MyBinder binder = (NotificationService.MyBinder) service;
            notificationService = binder.getService();

            notificationService.getNotification();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTabView();

        Intent bindIntent = new Intent(MainActivity.this, NotificationService.class);
        bindService(bindIntent, serviceConnection, BIND_AUTO_CREATE);
    }

    protected void setupTabView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final int[] tabIcons = {R.drawable.ic_recommended, R.drawable.ic_topics, R.drawable.ic_wiki, R.drawable.ic_me};
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add(R.string.recommended, RecommendedFragment.class)
                .add(R.string.topics, TopicsFragment.class)
                .add(R.string.wiki, WikiFragment.class)
                .add(R.string.me, MeFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                pages);

        viewPager.setOffscreenPageLimit(pages.size());
        viewPager.setAdapter(adapter);
        viewpagerTab.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter pagerAdapter) {
                View view = inflater.inflate(R.layout.custom_tab_icon, container, false);
                ImageView iconView = (ImageView) view.findViewById(R.id.iv_icon);
                iconView.setBackgroundResource(tabIcons[position % tabIcons.length]);
                return view;
            }
        });
        viewpagerTab.setViewPager(viewPager);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.main;
    }
}