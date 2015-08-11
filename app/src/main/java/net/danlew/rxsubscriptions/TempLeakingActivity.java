package net.danlew.rxsubscriptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import butterknife.Bind;
import butterknife.ButterKnife;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class TempLeakingActivity extends Activity {

    private static final String FIX_LEAK = "FIX_LEAK";

    @Bind(R.id.explanation) TextView mExplanationView;

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

        ButterKnife.bind(this);

        int explanationId = getIntent().getBooleanExtra(FIX_LEAK, false) ?
            R.string.explanation_leak_temp_fixed : R.string.explanation_leak_temp;
        mExplanationView.setText(explanationId);
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
