package net.danlew.rxsubscriptions;

import android.app.Application;
import timber.log.Timber;

public class SubscriptionsApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        Timber.plant(new Timber.DebugTree());
    }
}
