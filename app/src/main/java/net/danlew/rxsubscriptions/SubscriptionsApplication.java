package net.danlew.rxsubscriptions;

import android.app.Application;
import com.squareup.leakcanary.LeakCanary;
import timber.log.Timber;

public class SubscriptionsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        LeakCanary.install(this);

        Timber.plant(new Timber.DebugTree());
    }
}
