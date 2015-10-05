package net.danlew.rxsubscriptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import java.util.concurrent.TimeUnit;
import rx.functions.Action1;
import rx.subjects.BehaviorSubject;
import timber.log.Timber;

public class LeakingSubjectActivity extends Activity {

    private static final String FIX_LEAK = "FIX_LEAK";

    private BehaviorSubject<Long> mSubject = BehaviorSubject.create(1L);

    public static Intent createIntent(Context context, boolean fixLeak) {
        Intent intent = new Intent(context, LeakingSubjectActivity.class);
        intent.putExtra(FIX_LEAK, fixLeak);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaking_subject);

        // This should leak because it's constantly looping on itself
        mSubject
            .delay(1, TimeUnit.SECONDS)
            .subscribe(new Action1<Long>() {
                @Override public void call(Long aLong) {
                    Timber.d("LeakingSubjectActivity received: " + aLong);
                    mSubject.onNext(aLong + 1);
                }
            });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // If we call Subject.onCompleted(), then any stream it was participating in is terminated
        // This is a handy way of terminating streams without having to manually handle the Subscription
        if (getIntent().getBooleanExtra(FIX_LEAK, false)) {
            mSubject.onCompleted();
        }
    }
}
