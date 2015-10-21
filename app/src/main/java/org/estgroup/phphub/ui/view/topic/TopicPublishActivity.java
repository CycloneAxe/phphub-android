package org.estgroup.phphub.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobsandgeeks.saripaar.QuickRule;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.Length;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Pattern;
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
public class TopicPublishActivity extends BaseActivity<TopicPublishPresenter> implements
        Validator.ValidationListener {

    private final static String LEFT_BRACKETS = "[", RIGHT_BRACKETS = "]";

    @Bind(R.id.et_topic_title)
    @NotEmpty
    @Order(1)
    @Length(min = 2, trim = true, messageResId = R.string.title_input_error)
    EditText topicTitleView;

    @NotEmpty
    @Order(2)
    @Length(min = 2, trim = true, messageResId = R.string.body_input_error)
    @Bind(R.id.et_topic_body)
    EditText topicBodyView;

    int nodeId = 0;

    @Order(3)
    @Bind(R.id.tv_select_node)
    TextView selectNodeView;

    Topic topic = new Topic();

    List<Node> nodes = new ArrayList<>();

    Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        validator = new Validator(this);
        validator.setValidationListener(this);
        validator.put(selectNodeView, new QuickRule<TextView>() {
            @Override
            public boolean isValid(TextView view) {
                return view.getText().toString().contains(LEFT_BRACKETS);
            }

            @Override
            public String getMessage(Context context) {
                return getString(R.string.node_input_error);
            }
        });

        getPresenter().request();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TopicPublishActivity.class);
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
            nodeId = Integer.parseInt(selectNodeView.getTag().toString());
            validator.validate();
        }
        return super.onOptionsItemSelected(item);
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
                selectNodeView.setText(getString(R.string.node_info, LEFT_BRACKETS + String.valueOf(nodes.get(item).getName()) + RIGHT_BRACKETS));
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

    @Override
    public void onValidationSucceeded() {
        SweetAlertDialog loadingDialog = new SweetAlertDialog(this, SweetAlertDialog.PROGRESS_TYPE);
        String topicTitle = topicTitleView.getText().toString();
        String topicBody = topicBodyView.getText().toString();

        topic.setTitle(topicTitle);
        topic.setBody(topicBody);
        topic.setNodeId(nodeId);

        getPresenter().publish(topic);

        loadingDialog.getProgressHelper().setBarColor(Color.parseColor("#4394DA"));
        loadingDialog.setContentText(getString(R.string.submitting));
        loadingDialog.setCancelable(false);
        loadingDialog.show();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        final SweetAlertDialog errorDialog = new SweetAlertDialog(this, SweetAlertDialog.ERROR_TYPE);
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                errorDialog.setTitleText("Oops...");
                errorDialog.setContentText(message);
                errorDialog.show();
            }
        }
    }

    public void onPublishSuccessful(Topic topic) {
        finish();
        navigator.navigateToTopicDetails(this, topic.getId());
    }

    public void onNetWorkError(Throwable throwable) {
        Logger.e(throwable.toString());
        Toast.makeText(this, getString(R.string.publish_error), Toast.LENGTH_SHORT).show();
    }

    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }
}
