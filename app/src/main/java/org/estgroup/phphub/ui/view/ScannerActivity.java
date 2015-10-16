package org.estgroup.phphub.ui.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;
import static org.estgroup.phphub.common.Constant.*;

public class ScannerActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler {
    private ZXingScannerView scannerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        scannerView = new ZXingScannerView(this);
        setContentView(scannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        scannerView.setResultHandler(this);
        scannerView.startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        scannerView.stopCamera();
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, ScannerActivity.class);
    }

    @Override
    public void handleResult(Result result) {
        String s = result.getText(), username = "", loginToken = "";
        if (!TextUtils.isEmpty(s) && s.contains(",")) {
            String[] data = s.split(",", 2);
            if (data.length == 2) {
                username = data[0];
                loginToken = data[1];
            }
        }
        Intent intent = new Intent();
        intent.putExtra(USERNAME_KEY, username);
        intent.putExtra(LOGIN_TOKEN_KEY, loginToken);
        setResult(RESULT_OK, intent);
        finish();
    }
}
