package org.phphub.app;

import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v13.FragmentPagerItems;

import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.common.widget.TintableImageView;
import org.phphub.app.ui.MeFragment;
import org.phphub.app.ui.RecommendedFragment;
import org.phphub.app.ui.TopicsFragment;
import org.phphub.app.ui.WikiFragment;

import java.util.ArrayList;

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
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getFragmentManager(),
                FragmentPagerItems.with(this)
                        .add("recommended", RecommendedFragment.class)
                        .add("topics", TopicsFragment.class)
                        .add("wiki", WikiFragment.class)
                        .add("me", MeFragment.class)
                        .create());

        viewPagerView.setAdapter(adapter);
        viewpagerTabView.setCustomTabView(new SmartTabLayout.TabProvider() {
            @Override
            public View createTabView(ViewGroup container, int position, PagerAdapter pagerAdapter) {
                View view = LayoutInflater.from(MainActivity.this)
                                    .inflate(R.layout.custom_tab_icon, container, false);
                TintableImageView iconView = (TintableImageView) view.findViewById(R.id.tiv_icon);
                switch (position) {
                    case 0:
                        iconView.setImageResource(R.mipmap.ic_recommended);
                        break;
                    case 1:
                        iconView.setImageResource(R.mipmap.ic_topics);
                        break;
                    case 2:
                        iconView.setImageResource(R.mipmap.ic_wiki);
                        break;
                    case 3:
                        iconView.setImageResource(R.mipmap.ic_me);
                        break;
                    default:
                        throw new IllegalStateException("Invalid position: " + position);
                }
                return view;
            }
        });
        viewpagerTabView.setViewPager(viewPagerView);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }
}
