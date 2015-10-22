package org.estgroup.phphub.common.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.common.App;
import org.estgroup.phphub.common.util.Utils;
import org.estgroup.phphub.model.UserModel;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;
import javax.inject.Named;

import eu.unicate.retroauth.AuthAccountManager;
import retrofit.RetrofitError;
import retrofit.client.Response;
import rx.Observable;
import rx.Subscriber;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static org.estgroup.phphub.common.qualifier.AuthType.AUTH_TYPE_USER;

public class NotificationService extends Service {

    public static int UPDATE_INTERVAL = 3000;
    private Timer timer = new Timer();

    @Inject
    AuthAccountManager authAccountManager;

    @Inject
    AccountManager accountManager;

    @Inject
    UserModel userModel;

    NotificationListener listener;

    String tokenType, accountType;

    Account[] accounts;

    public static interface NotificationListener {
        void onNotificationServiceSuccess(List<Notification> notificationList);

        void onNotificationServiceError(RetrofitError error);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplication()).getApiComponent().inject(this);

        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);
    }

    public void setListener(NotificationListener listener) {
        this.listener = listener;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        timer.cancel();
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return START_STICKY;
    }

    public void getNotification() {
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (Utils.logined(NotificationService.this, accountManager)) {
                    userModel.once()
                            .setToken(authAccountManager.getAuthToken(accounts[0], accountType, tokenType))
                            .getMyNotifications()
                            .map(new Func1<NotificationEntity, List<Notification>>() {
                                @Override
                                public List<Notification> call(NotificationEntity notificationEntity) {
                                    return notificationEntity.getData();
                                }
                            })
                            .subscribe(new Action1<List<Notification>>() {
                                @Override
                                public void call(List<Notification> notificationList) {
                                    listener.onNotificationServiceSuccess(notificationList);
                                }
                            });
                }
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
