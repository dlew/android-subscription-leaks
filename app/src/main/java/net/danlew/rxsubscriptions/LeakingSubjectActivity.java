package net.danlew.rxsubscriptions;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import rx.Observable;
import rx.functions.Action1;
import rx.subjects.PublishSubject;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class LeakingSubjectActivity extends Activity {

    private static final String FIX_LEAK = "FIX_LEAK";

    private PublishSubject<Long> mSubject = PublishSubject.create();

    public static Intent createIntent(Context context, boolean fixLeak) {
        Intent intent = new Intent(context, LeakingSubjectActivity.class);
        intent.putExtra(FIX_LEAK, fixLeak);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaking_subject);

        // This is similar to LeakingActivity, but it routes all notifications through a Subject
        Observable.interval(1, TimeUnit.SECONDS).subscribe(mSubject);

        // This should leak because the original Observable never terminates
        mSubject.subscribe(new Action1<Long>() {
            @Override
            public void call(Long aLong) {
                Timber.d("LeakingSubjectActivity received: " + aLong);
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
