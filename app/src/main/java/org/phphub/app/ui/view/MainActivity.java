package org.phphub.app.ui.view;

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

import org.phphub.app.R;
import org.phphub.app.common.base.BaseActivity;

import butterknife.Bind;

public class MainActivity extends BaseActivity {
    @Bind(R.id.viewpager)
    ViewPager viewPagerView;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTabView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupTabView();
    }

    protected void setupTabView() {
        final LayoutInflater inflater = LayoutInflater.from(this);
        final int[] tabIcons = {R.drawable.ic_recommended, R.drawable.ic_topics, R.drawable.ic_wiki, R.drawable.ic_me};
        FragmentPagerItems pages = FragmentPagerItems.with(this)
                .add("recommended", RecommendedFragment.class)
                .add("topics", TopicsFragment.class)
                .add("wiki", WikiFragment.class)
                .add("me", MeFragment.class)
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getSupportFragmentManager(),
                pages);

        viewPagerView.setOffscreenPageLimit(pages.size());
        viewPagerView.setAdapter(adapter);
        viewpagerTabView.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter pagerAdapter) {
                View view = inflater.inflate(R.layout.custom_tab_icon, container, false);
                ImageView iconView = (ImageView) view.findViewById(R.id.iv_icon);
                iconView.setBackgroundResource(tabIcons[position % tabIcons.length]);
                return view;
            }
        });
        viewpagerTabView.setViewPager(viewPagerView);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.main;
    }
}
