package org.estgroup.phphub.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.logger.Logger;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.element.Node;
import org.estgroup.phphub.api.entity.element.Topic;
import org.estgroup.phphub.common.base.BaseActivity;
import org.estgroup.phphub.ui.presenter.TopicPublishPresenter;
import org.estgroup.phphub.widget.AnimateDialog;
import org.estgroup.phphub.widget.LoopView.LoopListener;
import org.estgroup.phphub.widget.LoopView.LoopView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.OnClick;
import cn.pedant.SweetAlert.SweetAlertDialog;
import nucleus.factory.PresenterFactory;
import nucleus.factory.RequiresPresenter;

@RequiresPresenter(TopicPublishPresenter.class)
public class TopicPublishActivity extends BaseActivity<TopicPublishPresenter> {

    @Bind(R.id.et_topic_title)
    EditText topicTitleView;

    @Bind(R.id.et_topic_body)
    EditText topicBodyView;

    @Bind(R.id.tv_select_node)
    TextView selectNodeView;

    Topic topicInfo;

    List<Node> nodes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        topicInfo = new Topic();
        nodes = new ArrayList<Node>();
        getPresenter().nodeRequest();
    }

    public static Intent getCallingIntent(Context context) {
        Intent callingIntent = new Intent(context, TopicPublishActivity.class);
        return callingIntent;
    }

    @Override
    protected void injectorPresenter() {
        super.injectorPresenter();
        final PresenterFactory<TopicPublishPresenter> superFactory = super.getPresenterFactory();
        setPresenterFactory(new PresenterFactory<TopicPublishPresenter>() {
            @Override
            public TopicPublishPresenter createPresenter() {
                TopicPublishPresenter presenter = superFactory.createPresenter();
                getApiComponent().inject(presenter);

                return presenter;
            }
        });
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.publish_topic;
    }

    @Override
    protected CharSequence getTitleName() {
        return getString(R.string.publish_topic);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.menu_posting, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_publish) {
            validationContent();
        }
        return super.onOptionsItemSelected(item);
    }

    public void validationContent() {
        SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        SweetAlertDialog loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        String topicTitle = topicTitleView.getText().toString();
        String topicBody = topicBodyView.getText().toString();
        int nodeId = Integer.parseInt(selectNodeView.getTag().toString());

        if (topicTitle.trim().length() < 2) {
            errorDialog.setTitleText("Oops...");
            errorDialog.setContentText(getString(R.string.title_input_error));
            errorDialog.show();
            return;
        } else if (topicBody.trim().length() < 2) {
            errorDialog.setTitleText("Oops...");
            errorDialog.setContentText(getString(R.string.body_input_error));
            errorDialog.show();
            return;
        } else if (nodeId == 0) {
            errorDialog.setTitleText("Oops...");
            errorDialog.setContentText(getString(R.string.node_input_error));
            errorDialog.show();
            return;
        }

        topicInfo.setTitle(topicTitle);
        topicInfo.setBody(topicBody);
        topicInfo.setNodeId(nodeId);

        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#4394DA"));
        loadingDialog.setContentText(getString(R.string.submitting));
        loadingDialog.setCancelable(false);
        loadingDialog.show();

        getPresenter().request(topicInfo);
    }

    @OnClick(R.id.tv_select_node)
    public void selectNode() {
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);

        RelativeLayout rootview = new RelativeLayout(this);

        LoopView loopView = new LoopView(this);


        ArrayList<String> list = new ArrayList();
        for (Node node : nodes) {
            list.add(node.getName());
        }

        //设置是否循环播放
        loopView.setNotLoop();
        //滚动监听
        loopView.setListener(new LoopListener() {
            @Override
            public void onItemSelect(int item) {
                selectNodeView.setTag(nodes.get(item).getId());
                selectNodeView.setText(getString(R.string.node_info, "[" + String.valueOf(nodes.get(item).getName()) + "]"));
            }
        });
        loopView.setArrayList(list);
        loopView.setPosition(5);
        //设置字体大小
        loopView.setColorChecked(Color.parseColor("#4394DA"));
        loopView.setTextSize(16);
        rootview.addView(loopView, layoutParams);

        AnimateDialog animateDialog = new AnimateDialog(this);
        animateDialog.setTitle(R.string.choose_node);
        animateDialog.popupDialog(rootview, 0.8f, 0.5f);
    }

    public void onPublishSuccessful(Topic topic) {
        finish();
        navigator.navigateToTopicDetails(this, topic.getId());
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.getMessage());
        Toast.makeText(this, getString(R.string.publish_error), Toast.LENGTH_SHORT).show();
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
