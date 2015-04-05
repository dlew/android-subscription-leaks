package net.danlew.rxsubscriptions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class TempLeakingActivity extends ActionBarActivity {

    private static final String FIX_LEAK = "FIX_LEAK";

    private Subscription mSubscription;

    public static Intent createIntent(Context context, boolean fixLeak) {
        Intent intent = new Intent(context, TempLeakingActivity.class);
        intent.putExtra(FIX_LEAK, fixLeak);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaking_temp);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // We create a delayed-reaction Observable as we quit; thus this Activity
        // will leak for about 10 seconds past the expiration point.
        mSubscription = Observable.just(1L)
            .delay(10, TimeUnit.SECONDS)
            .subscribe(new Action1<Long>() {
                @Override public void call(Long aLong) {
                    Timber.d("TempLeakingActivity received: " + aLong);
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (getIntent().getBooleanExtra(FIX_LEAK, false)) {
            mSubscription.unsubscribe();
        }
    }
}
