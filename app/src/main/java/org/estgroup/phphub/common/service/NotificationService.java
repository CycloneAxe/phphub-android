package org.estgroup.phphub.common.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.orhanobut.logger.Logger;
import com.squareup.otto.Produce;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.common.event.NotificationChangeEvent;
import org.estgroup.phphub.common.provider.BusProvider;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.UserModel;

import eu.unicate.retroauth.AuthAccountManager;
import rx.functions.Func1;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_USER;

public class NotificationService extends Service {

    private AccountManager accountManager;

    private String accountType, tokenType;

    private Account[] accounts;

    public static int UPDATE_INTERVAL = 3000;
    private Timer timer = new Timer();

    @Inject
    @Named(AUTH_TYPE_USER)
    UserModel userModel;

    @Inject
    AuthAccountManager authAccountManager;

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("onCreate");
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("onStartCommand");
        if (Utils.logined(this, AccountManager.get(this))) {
            this.accountManager = AccountManager.get(this);
            this.accountType = getString(R.string.auth_account_type);
            this.tokenType = getString(R.string.auth_token_type);
            this.accounts = accountManager.getAccountsByType(accountType);

            getNotification();
        }

        return START_STICKY;
    }

    public void getNotification() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                System.out.println("MyService");
            }
        }, 0, UPDATE_INTERVAL);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return new MyBinder();
    }

    public class MyBinder extends Binder {
        public NotificationService getService() {
            return NotificationService.this;
        }
    }
}
