package net.danlew.rxsubscriptions;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import rx.Observable;
import rx.functions.Action1;
import timber.log.Timber;

import java.util.concurrent.TimeUnit;

public class LeakingActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_leaking);

        // This is a hot Observable that never ends;
        // thus LeakingActivity can never be reclaimed
        Observable.interval(1, TimeUnit.SECONDS)
            .subscribe(new Action1<Long>() {
                @Override public void call(Long aLong) {
                    Timber.d("LeakingActivity received: " + aLong);
                }
            });
    }
}
