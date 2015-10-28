package org.estgroup.phphub.ui.view.topic;

import android.accounts.AccountManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.facebook.drawee.view.SimpleDraweeView;
import com.kennyc.view.MultiStateView;
import com.kmshack.topscroll.TopScrollHelper;
import com.orhanobut.logger.Logger;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.common.SocializeConstants;
import com.umeng.socialize.controller.UMServiceFactory;
import com.umeng.socialize.controller.UMSocialService;
import com.umeng.socialize.media.QQShareContent;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;
import com.umeng.socialize.weixin.media.CircleShareContent;
import com.umeng.socialize.weixin.media.WeiXinShareContent;

import org.estgroup.phphub.BuildConfig;
import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Link;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.api.entity.element.User;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.TopicDetailPresenter;
import org.estgroup.phphub.widget.AnimateDialog;

import java.net.MalformedURLException;
import java.net.URL;

import butterknife.Bind;
import butterknife.OnClick;
import cn.bingoogolapple.badgeview.BGABadgeLinearLayout;
import cn.pedant.SweetAlert.SweetAlertDialog;
import eu.unicate.retroauth.AuthAccountManager;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

import static com.kennyc.view.MultiStateView.OnClickListener;
import static com.kennyc.view.MultiStateView.VIEW_STATE_CONTENT;
import static com.kennyc.view.MultiStateView.VIEW_STATE_ERROR;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_FAVORITE;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_FAVORITE_DEL;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_FOLLOW;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_FOLLOW_DEL;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_VOTE_DOWN;
import static org.estgroup.phphub.common.qualifier.TopicDetailType.TOPIC_DETAIL_TYPE_VOTE_UP;

@DeepLink("phphub://topics")
@RequiresPresenter(TopicDetailPresenter.class)
public class TopicDetailsActivity extends BaseActivity<TopicDetailPresenter> implements
        OnClickListener {
    private static final String INTENT_EXTRA_PARAM_TOPIC_ID = "topic_id";

    private static final String INTENT_EXTRA_DEEPLINK_PARAM_ID = "id";

    int topicId;

    Topic topicInfo;

    @Bind(R.id.multiStateView)
    MultiStateView multiStateView;

    @Bind(R.id.wv_content)
    WebView topicContentView;

    @Bind(R.id.tv_username)
    TextView userNameView;

    @Bind(R.id.tv_sign)
    TextView signView;

    @Bind(R.id.sdv_avatar)
    SimpleDraweeView avatarView;

    @Bind(R.id.tv_praise_count)
    TextView PraiseView;

    @Bind(R.id.bga_llyt_reply_count)
    BGABadgeLinearLayout replyCountView;

    @Bind(R.id.iv_topic_up)
    ImageView voteUpView;

    @Bind(R.id.iv_topic_down)
    ImageView voteDownView;

    @Bind(R.id.iv_favorite_icon)
    ImageView favoriteView;

    @Bind(R.id.iv_following_icon)
    ImageView followView;

    @Bind(R.id.iv_replys_icon)
    ImageView replysView;

    @Bind(R.id.iv_count_icon)
    ImageView countView;

    AccountManager accountManager;

    AuthAccountManager authAccountManager;

    String accountType, tokenType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accountManager = AccountManager.get(this);
        authAccountManager = new AuthAccountManager(this, accountManager);

        Intent intent = getIntent();
        if (getIntent().getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            Bundle params = intent.getExtras();
            if (params != null && params.getString(INTENT_EXTRA_DEEPLINK_PARAM_ID) != null) {
                String value = params.getString(INTENT_EXTRA_DEEPLINK_PARAM_ID);
                if (!TextUtils.isEmpty(value)) {
                    topicId = Integer.valueOf(value);
                }
            }
        } else {
            topicId = intent.getIntExtra(INTENT_EXTRA_PARAM_TOPIC_ID, 0);
        }

        Logger.d("topic id : %d", topicId);

        if (topicId > 0) {
            getPresenter().request(topicId);
        }

        TopScrollHelper.getInstance(getApplicationContext())
                .addTargetScrollView(topicContentView);

        topicContentView.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                try {
                    URL webUrl = new URL(url);
                    if (webUrl.getHost().equals("phphub.org")) {
                        String[] strings = webUrl.getPath().split("/");
                        if (strings[1].equals("topics") && Integer.parseInt(strings[2]) > 0) {
                            navigator.navigateToTopicDetails(TopicDetailsActivity.this, Integer.parseInt(strings[2]));
                        } else if (strings[1].equals("users") && Integer.parseInt(strings[2]) > 0) {
                            navigator.navigateToUserSpace(TopicDetailsActivity.this, Integer.parseInt(strings[2]));
                        }

                    } else {
                        navigator.navigateToWebView(TopicDetailsActivity.this, url);
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                return true;
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TopScrollHelper.getInstance(getApplicationContext())
                .removeTargetScrollView(topicContentView);
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<TopicDetailPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicDetailPresenter>() {
            @Override
            public TopicDetailPresenter createPresenter() {
                TopicDetailPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);
                return presenter;
            }
        });
    }

    public void initView(Topic topic) {
        this.topicInfo = topic;

        Link link = topic.getLinks();
        User user = topic.getUser().getData();
        String voteCount = topic.getVoteCount() > 99 ? "99+" : String.valueOf(topic.getVoteCount());
        String replyCount = topic.getReplyCount() > 99 ? "99+" : String.valueOf(topic.getReplyCount());

        avatarView.setImageURI(Uri.parse(user.getAvatar()));
        userNameView.setText(user.getName());
        signView.setText(user.getSignature());
        PraiseView.setText(voteCount);
        topicContentView.loadUrl(link.getDetailsWebView(), getHttpHeaderAuth());
        topicContentView.getSettings().setJavaScriptEnabled(true);
        replyCountView.showTextBadge(replyCount);

        if (topic.isVoteUp()) {
            voteUpView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        } else if (topic.isVoteDown()) {
            voteDownView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        }

        if (topic.isFavorite()) {
            favoriteView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        }

        if (topic.isAttention()) {
            followView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        }

        multiStateView.setViewState(VIEW_STATE_CONTENT);
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.topic_details;
    }

    public static Intent getCallingIntent(Context context, int TopicId) {
        Intent callingIntent = new Intent(context, TopicDetailsActivity.class);
        callingIntent.putExtra(INTENT_EXTRA_PARAM_TOPIC_ID, TopicId);
        return callingIntent;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        getMenuInflater().inflate(R.menu.menu_topic, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.menu_share:
                onShareItemSelected();
                break;

            case R.id.menu_report:
                SweetAlertDialog successDialog = new SweetAlertDialog(this, SweetAlertDialog.SUCCESS_TYPE);

                successDialog.setTitleText(getString(R.string.success_title));
                successDialog.setContentText(getString(R.string.report_success));
                successDialog.show();

                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onShareItemSelected() {
        SocializeConstants.APPKEY = BuildConfig.UMENG_APPKEY;
        final UMSocialService socialService = UMServiceFactory.getUMSocialService("com.umeng.share");
        // Remove Tencent Weibo and QZone from share panel.
        socialService.getConfig().removePlatform(SHARE_MEDIA.TENCENT);
        socialService.getConfig().removePlatform(SHARE_MEDIA.QZONE);

        String webLink = this.topicInfo.getLinks().getWebURL();
        String shareContent = this.topicInfo.getTitle() + " " + webLink;
        String title = getString(R.string.share_topic, this.topicInfo.getUser().getData().getName());

        socialService.setShareContent(shareContent);

        if (!BuildConfig.QQ_APPID.isEmpty() && !BuildConfig.QQ_APPKEY.isEmpty()) {
            // Add QQ
            UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(this, BuildConfig.QQ_APPID, BuildConfig.QQ_APPKEY);
            qqSsoHandler.addToSocialSDK();

            QQShareContent qqShareContent = new QQShareContent();
            qqShareContent.setShareContent(shareContent);
            qqShareContent.setTitle(title);
            qqShareContent.setTargetUrl(this.topicInfo.getLinks().getWebURL());

            socialService.setShareMedia(qqShareContent);
        } else {
            socialService.getConfig().removePlatform(SHARE_MEDIA.QQ);
        }

        if (!BuildConfig.WX_APPID.isEmpty() && !BuildConfig.WX_SECRET.isEmpty()) {
            // Add WeiChat
            UMWXHandler wxHandler = new UMWXHandler(this, BuildConfig.WX_APPID, BuildConfig.WX_SECRET);
            wxHandler.addToSocialSDK();

            // Add WeChat Circle
            UMWXHandler wxCircleHandler = new UMWXHandler(this, BuildConfig.WX_APPID, BuildConfig.WX_SECRET);
            wxCircleHandler.setToCircle(true);
            wxCircleHandler.addToSocialSDK();

            //设置微信分享内容
            WeiXinShareContent weixinContent = new WeiXinShareContent();
            weixinContent.setShareContent(shareContent);
            weixinContent.setTitle(title);
            weixinContent.setTargetUrl(this.topicInfo.getLinks().getWebURL());
            weixinContent.setShareImage(new UMImage(this, topicInfo.getUser().getData().getAvatar()));
            socialService.setShareMedia(weixinContent);

            Logger.d(topicInfo.getUser().getData().getAvatar());
            //设置微信朋友圈分享内容
            CircleShareContent circleMedia = new CircleShareContent();
            circleMedia.setShareContent(shareContent);
            circleMedia.setTitle(title);
            circleMedia.setTargetUrl(this.topicInfo.getLinks().getWebURL());
            circleMedia.setShareImage(new UMImage(this, topicInfo.getUser().getData().getAvatar()));
            socialService.setShareMedia(circleMedia);
        }else{
            socialService.getConfig().removePlatform(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE);
        }

        socialService.openShare(this, false);
    }

    public void onNetworkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
        multiStateView.setViewState(VIEW_STATE_ERROR);
    }

    @OnClick(R.id.rlyt_vote_topic)
    public void popupVoteView() {

        final AnimateDialog alertDialog = new AnimateDialog(this);
        alertDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        alertDialog.popupDialog(R.layout.dialog_vote, 0.642f, 0.168f, new AnimateDialog.DialogClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.iv_vote_up:
                        if (!isLogin()) {
                            needLogin();
                            return;
                        }
                        if (topicInfo.isVoteUp()) {
                            ((ImageView) view).setColorFilter(getResources().getColor(R.color.blue_a5), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            ((ImageView) view).setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                        }
                        getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_VOTE_UP);
                        alertDialog.dismiss();
                        break;
                    case R.id.iv_vote_down:
                        if (!isLogin()) {
                            needLogin();
                            return;
                        }
                        if (topicInfo.isVoteUp()) {
                            ((ImageView) view).setColorFilter(getResources().getColor(R.color.blue_a5), PorterDuff.Mode.SRC_ATOP);
                        } else {
                            ((ImageView) view).setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                        }
                        getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_VOTE_DOWN);
                        alertDialog.dismiss();
                        break;
                }
            }
        });

        ImageView voteUp = (ImageView) alertDialog.findViewById(R.id.iv_vote_up);
        ImageView voteDown = (ImageView) alertDialog.findViewById(R.id.iv_vote_down);

        voteUp.setOnClickListener(alertDialog);
        voteDown.setOnClickListener(alertDialog);

        if (topicInfo.isVoteUp()) {
            voteUp.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        }

        if (topicInfo.isVoteDown()) {
            voteDown.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
        }

        alertDialog.show();
    }

    public void setOptionState(String optionType, boolean isSuccess) {

        System.out.println(optionType);
        switch (optionType) {
            case TOPIC_DETAIL_TYPE_FAVORITE:
                topicInfo.setFavorite(true);
                Toast.makeText(this, getString(R.string.favorite_success), Toast.LENGTH_SHORT).show();
                favoriteView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                break;
            case TOPIC_DETAIL_TYPE_FAVORITE_DEL:
                topicInfo.setFavorite(false);
                Toast.makeText(this, getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                favoriteView.setColorFilter(getResources().getColor(R.color.blue_a4), PorterDuff.Mode.SRC_ATOP);
                break;
            case TOPIC_DETAIL_TYPE_FOLLOW:
                topicInfo.setAttention(true);
                Toast.makeText(this, getString(R.string.follow_success), Toast.LENGTH_SHORT).show();
                followView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                break;
            case TOPIC_DETAIL_TYPE_FOLLOW_DEL:
                topicInfo.setAttention(false);
                Toast.makeText(this, getString(R.string.cancel_success), Toast.LENGTH_SHORT).show();
                followView.setColorFilter(getResources().getColor(R.color.blue_a4), PorterDuff.Mode.SRC_ATOP);
                break;
            case TOPIC_DETAIL_TYPE_VOTE_UP:
                topicInfo.setVoteUp(isSuccess);
                String msg = isSuccess ? getString(R.string.vote_up_success) : getString(R.string.cancel_success);
                Toast.makeText(this, msg, Toast.LENGTH_SHORT).show();
                int count = Integer.parseInt(PraiseView.getText().toString());

                if (isSuccess) {
                    voteUpView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                    voteDownView.setColorFilter(getResources().getColor(R.color.gray_c9), PorterDuff.Mode.SRC_ATOP);
                    count += 1;
                } else {
                    voteUpView.setColorFilter(getResources().getColor(R.color.gray_c9), PorterDuff.Mode.SRC_ATOP);
                    if (count > 0) {
                        count -= 1;
                    }
                }

                PraiseView.setText(String.valueOf(count));

                break;
            case TOPIC_DETAIL_TYPE_VOTE_DOWN:
                topicInfo.setVoteDown(isSuccess);
                String msgDown = isSuccess ? getString(R.string.vote_down_success) : getString(R.string.cancel_success);
                Toast.makeText(this, msgDown, Toast.LENGTH_SHORT).show();
                int downCount = Integer.parseInt(PraiseView.getText().toString());

                if (isSuccess) {
                    voteDownView.setColorFilter(getResources().getColor(R.color.icon_enabled), PorterDuff.Mode.SRC_ATOP);
                    voteUpView.setColorFilter(getResources().getColor(R.color.gray_c9), PorterDuff.Mode.SRC_ATOP);
                    if (downCount > 0) {
                        downCount -= 1;
                    }
                } else {
                    voteDownView.setColorFilter(getResources().getColor(R.color.gray_c9), PorterDuff.Mode.SRC_ATOP);
                    downCount += 1;
                }

                PraiseView.setText(String.valueOf(downCount));

                break;
        }
    }

    @OnClick({
            R.id.sdv_avatar,
            R.id.llyt_userInfo,
            R.id.iv_favorite_icon,
            R.id.iv_following_icon,
            R.id.iv_replys_icon,
            R.id.iv_count_icon,
            R.id.bga_llyt_reply_count
    })
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sdv_avatar:
            case R.id.llyt_userInfo:
                navigator.navigateToUserSpace(this, topicInfo.getUserId());
                break;
            case R.id.iv_favorite_icon:
                if (!isLogin()) {
                    needLogin();
                    return;
                }
                if (topicInfo.isFavorite()) {
                    getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_FAVORITE_DEL);
                } else {
                    getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_FAVORITE);
                }
                break;
            case R.id.iv_following_icon:
                if (!isLogin()) {
                    needLogin();
                    return;
                }
                if (topicInfo.isAttention()) {
                    getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_FOLLOW_DEL);
                } else {
                    getPresenter().eventRequest(topicId, TOPIC_DETAIL_TYPE_FOLLOW);
                }
                break;

            case R.id.iv_replys_icon:
                if (!isLogin()) {
                    needLogin();
                    return;
                }

                navigator.navigateToReplyTopic(this, topicId, topicInfo.getLinks().getRepliesWebView());
                break;
            case R.id.iv_count_icon:
            case R.id.bga_llyt_reply_count:
                navigator.navigateToUserReply(this, topicId, topicInfo.getLinks().getRepliesWebView());

                break;
        }
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
    }

    private void needLogin() {
        authAccountManager.addAccount(this, accountType, tokenType);
    }
}
