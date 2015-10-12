package org.phphub.app.ui.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.widget.TextView;
import android.widget.Toast;

import com.levelmoney.velodrome.Velodrome;
import com.levelmoney.velodrome.annotations.OnActivityResult;

import static org.phphub.app.common.Constant.*;

import org.phphub.app.R;
import org.phphub.app.common.App;
import org.phphub.app.common.Navigator;

import javax.inject.Inject;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import eu.unicate.retroauth.AuthenticationActivity;

public class LoginActivity extends AuthenticationActivity {
    private final static int CODE_SCANNER = 100;

    @Bind(R.id.toolbar)
    Toolbar toolbarView;

    @Bind(R.id.toolbar_title)
    TextView toolbarTitleView;

    @Inject
    Navigator navigator;

    @Override
    protected void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        setContentView(R.layout.login);
        ButterKnife.bind(this);

        initializeToolbar();
        ((App) getApplication()).getAppComponent().inject(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Velodrome.handleResult(this, requestCode, resultCode, data);
    }

    protected void initializeToolbar() {
        setSupportActionBar(toolbarView);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbarTitleView.setText(R.string.please_login);
    }

    @OnClick(R.id.btn_scanner)
    public void navigateToScanner() {
        navigator.navigateToScanner(this, CODE_SCANNER);
    }

    @OnActivityResult(CODE_SCANNER)
    public void onScanner(Intent data) {
        String username = data.getStringExtra(USERNAME_KEY),
                loginToken = data.getStringExtra(LOGIN_TOKEN_KEY);

        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(loginToken)) {
            Toast.makeText(this, "用户名或密码错误", Toast.LENGTH_SHORT).show();
            return;
        }
    }
}