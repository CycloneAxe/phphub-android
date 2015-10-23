package org.estgroup.phphub.common.service;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.squareup.otto.Bus;
import com.squareup.otto.Produce;

import org.estgroup.phphub.R;
import org.estgroup.phphub.api.entity.NotificationEntity;
import org.estgroup.phphub.api.entity.element.Notification;
import org.estgroup.phphub.common.App;
import org.estgroup.phphub.common.event.NotificationChangeEvent;
import org.estgroup.phphub.common.provider.BusProvider;
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

    public static int notificationLength;

    @Inject
    AuthAccountManager authAccountManager;

    @Inject
    AccountManager accountManager;

    @Inject
    UserModel userModel;

    String tokenType, accountType;

    Account[] accounts;

    @Override
    public void onCreate() {
        super.onCreate();
        ((App) getApplication()).getApiComponent().inject(this);
        BusProvider.getInstance().register(this);

        accountType = getString(R.string.auth_account_type);
        tokenType = getString(R.string.auth_token_type);
        accounts = accountManager.getAccountsByType(accountType);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        BusProvider.getInstance().unregister(this);
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
                                    notificationLength = notificationList.size();
                                    BusProvider.getInstance().post(notificationChangeEvent());
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

    @Produce public NotificationChangeEvent notificationChangeEvent() {
        return new NotificationChangeEvent(notificationLength);
    }
}
