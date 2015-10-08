package org.phphub.app.ui.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.github.pwittchen.prefser.library.Prefser;
import com.ogaclejapan.smarttablayout.SmartTabLayout;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItemAdapter;
import com.ogaclejapan.smarttablayout.utils.v4.FragmentPagerItems;

import org.phphub.app.R;
import org.phphub.app.api.entity.element.Token;
import org.phphub.app.common.base.BaseActivity;
import org.phphub.app.ui.presenter.MainPresenter;
import org.phphub.app.ui.view.topic.TopicsFragment;

import javax.inject.Inject;

import butterknife.Bind;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;
import static org.phphub.app.common.Constant.*;

@RequiresPresenter(MainPresenter.class)
public class MainActivity extends BaseActivity<MainPresenter> {
    @Bind(R.id.viewpager)
    ViewPager viewPager;

    @Bind(R.id.viewpagertab)
    SmartTabLayout viewpagerTab;

    private ProgressDialog dialog;

    @Inject
    Prefser prefser;

    boolean tabHasCreated;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        final PresenterFactory<MainPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<MainPresenter>() {
            @Override
            public MainPresenter createPresenter() {
                MainPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });

        super.onCreate(savedInstanceState);

        getAppComponent().inject(this);
        String clientToken = prefser.get(CLIENT_TOKEN, String.class, "");
        if (TextUtils.isEmpty(clientToken)) {
            dialog = new ProgressDialog(this);
            dialog.setMessage("数据初始化……");
            dialog.setCancelable(false);
            dialog.show();
            getPresenter().request();
        } else {
            setupTabView();
        }
    }

    protected void setupTabView() {
        if (tabHasCreated) {
            return;
        }
        tabHasCreated = true;

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

    public void storageClientToken(Token token) {
        dialog.dismiss();
        prefser.put(CLIENT_TOKEN, token.getToken());
        setupTabView();
    }
}