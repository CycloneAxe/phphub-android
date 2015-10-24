package org.estgroup.phphub.ui.view;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;
import com.squareup.otto.Subscribe;

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.common.event.NotificationChangeEvent;
import org.estgroup.phphub.common.provider.BusProvider;
import org.estgroup.phphub.ui.view.topic.TopicsFragment;

import butterknife.Bind;
import cn.bingoogolapple.badgeview.BGABadgeFrameLayout;

public class MainActivity extends BaseActivity  {
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private BGABadgeFrameLayout meIconView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        BusProvider.getInstance().register(this);
        setupTabView();

        meIconView = (BGABadgeFrameLayout) viewpagerTab.getTabAt(3).findViewById(R.id.badgeView);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
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

    @Subscribe public void onNotificationChangeMe(NotificationChangeEvent event) {
        if (meIconView == null) {
            return;
        }

        int lenght = event.getNotificationLength();

        if (lenght > 0) {
            meIconView.showTextBadge(String.valueOf(lenght));
        } else {
            meIconView.hiddenBadge();
        }
    }

}