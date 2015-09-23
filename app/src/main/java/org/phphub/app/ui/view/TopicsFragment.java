package org.phphub.app.ui.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.Bundler;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseFragment;
import org.phphub.app.common.qualified.TopicType;

import butterknife.Bind;
import static org.phphub.app.common.qualified.TopicType.*;

public class TopicsFragment extends BaseFragment {
    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTabView;

    @Bind(R.id.viewpager)
    ViewPager viewPagerView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topics, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        FragmentPagerItems pages = FragmentPagerItems.with(getActivity())
                .add(R.string.recent, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_RECENT))
                .add(R.string.vote, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_VOTE))
                .add(R.string.nobody, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_NOBODY))
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                pages);

        viewPagerView.setOffscreenPageLimit(pages.size());
        viewPagerView.setAdapter(adapter);
        viewpagerTabView.setViewPager(viewPagerView);
    }

    private Bundle getTopicTypeBundle(@TopicType String topicType) {
        return new Bundler().putString(TopicFragment.TOPIC_TYPE_KEY, topicType).get();
    }

}