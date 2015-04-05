package net.danlew.rxsubscriptions;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.widget.TextView;
import butterknife.ButterKnife;
import butterknife.InjectView;
import rx.Observable;
import rx.android.app.AppObservable;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class BindActivity extends ActionBarActivity {

    private static final String FIX_LEAK = "FIX_LEAK";

    @InjectView(R.id.text) TextView mTextView;

    public static Intent createIntent(Context context, boolean fixLeak) {
        Intent intent = new Intent(context, BindActivity.class);
        intent.putExtra(FIX_LEAK, fixLeak);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_bind);

        ButterKnife.inject(this);

        boolean fixLeak = getIntent().getBooleanExtra(FIX_LEAK, false);
        mTextView.setText(fixLeak ? R.string.explanation_bind_no_leak : R.string.explanation_bind_leak);

        if (fixLeak) {
            // This one won't leak (for long) because it catches the first emission after
            // the Activity is destroyed and unsubscribes automatically.
            AppObservable.bindActivity(this, Observable.interval(1, TimeUnit.SECONDS))
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        Timber.d("BindActivity received: " + aLong);
                    }
                });
        }
        else {
            // This will leak for a while because it takes a day to emit anything,
            // thus bindActivity() won't know to unsubscribe until then.
            AppObservable.bindActivity(this, Observable.interval(1, TimeUnit.DAYS))
                .subscribe(new Action1<Long>() {
                    @Override public void call(Long aLong) {
                        Timber.d("BindActivity received: " + aLong);
                    }
                });
        }
    }
}
