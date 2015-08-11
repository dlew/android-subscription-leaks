package net.danlew.rxsubscriptions;

import android.os.Bundle;
import com.trello.rxlifecycle.components.RxActivity;
import rx.Observable;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class LifecycleActivity extends RxActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_lifecycle);

        // RxActivity binds automatically unsubscribe when the corresponding
        // lifecycle event occurs - in this case, onDestroy.
        Observable.interval(1, TimeUnit.SECONDS)
            .compose(this.<Long>bindToLifecycle())
            .subscribe(new Action1<Long>() {
                @Override
                public void call(Long aLong) {
                    Timber.d("LifecycleActivity received: " + aLong);
                }
            });
    }
}
