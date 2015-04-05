package net.danlew.rxsubscriptions;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
    }

    @OnClick(R.id.leaking_subscription)
    void leakSubscription() {
        startActivity(LeakingActivity.createIntent(this, false));
    }

    @OnClick(R.id.leaking_subscription_fixed)
    void leakSubscriptionWithFix() {
        startActivity(LeakingActivity.createIntent(this, true));
    }

    @OnClick(R.id.temp_leaking_subscription)
    void tempLeakSubscription() {
        startActivity(TempLeakingActivity.createIntent(this, false));
    }

    @OnClick(R.id.temp_leaking_subscription_fixed)
    void tempLeakSubscriptionWithFix() {
        startActivity(TempLeakingActivity.createIntent(this, true));
    }

    @OnClick(R.id.bind_activity_leak)
    void bindActivityLeak() {
        startActivity(BindActivity.createIntent(this, false));
    }

    @OnClick(R.id.bind_activity_no_leak)
    void bindActivityNoLeak() {
        startActivity(BindActivity.createIntent(this, true));
    }

    @OnClick(R.id.lifecycle_observable)
    void lifecycleObservable() {
        startActivity(new Intent(this, LifecycleActivity.class));
    }

    @OnClick(R.id.never_leaks)
    void neverLeaks() {
        startActivity(new Intent(this, NeverActivity.class));
    }

}
