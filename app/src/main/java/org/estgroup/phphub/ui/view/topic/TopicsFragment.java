package org.estgroup.phphub.ui.view.topic;

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

import org.estgroup.phphub.R;
import org.estgroup.phphub.common.base.LazyFragment;
import org.estgroup.phphub.common.qualifier.TopicType;

import butterknife.Bind;
import static org.estgroup.phphub.common.qualifier.TopicType.*;

public class TopicsFragment extends LazyFragment {

    private boolean isPrepared;

    private boolean adapterHasCreated;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.topic_viewpager_list, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        isPrepared = true;
    }

    @Override
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || adapterHasCreated) {
            return;
        }

        FragmentPagerItems pages = FragmentPagerItems.with(getActivity())
                .add(R.string.recent, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_RECENT))
                .add(R.string.vote, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_VOTE))
                .add(R.string.jobs, TopicFragment.class, getTopicTypeBundle(TOPIC_TYPE_JOBS))
                .create();
        FragmentPagerItemAdapter adapter = new FragmentPagerItemAdapter(
                getChildFragmentManager(),
                pages);

        viewPager.setOffscreenPageLimit(pages.size());
        viewPager.setAdapter(adapter);
        viewpagerTab.setViewPager(viewPager);

        adapterHasCreated = true;
    }

    private Bundle getTopicTypeBundle(@TopicType String topicType) {
        return new Bundler().putString(TopicFragment.TOPIC_TYPE_KEY, topicType).get();
    }

}