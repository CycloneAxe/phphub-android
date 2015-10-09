package org.phphub.app.ui.view.topic;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;

import org.phphub.app.R;
import org.phphub.app.common.base.BaseActivity;

public class TopicDetailsActivity extends BaseActivity {
    private static final String INTENT_EXTRA_PARAM_TOPIC_ID = "topic_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
