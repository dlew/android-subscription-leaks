package net.danlew.rxsubscriptions;

import android.os.Bundle;
import butterknife.ButterKnife;
import rx.Observable;
import rx.android.app.RxActivity;
import rx.android.lifecycle.LifecycleObservable;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class LifecycleActivity extends RxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lifecycle);

        ButterKnife.inject(this);

        // LifecycleObservable's binds automatically unsubscribe when the corresponding
        // lifecycle event occurs - in this case, onDestroy.
        LifecycleObservable.bindActivityLifecycle(lifecycle(), Observable.interval(1, TimeUnit.SECONDS))
            .subscribe(new Action1<Long>() {
                @Override public void call(Long aLong) {
                    Timber.d("LifecycleActivity received: " + aLong);
                }
            });
    }
}
