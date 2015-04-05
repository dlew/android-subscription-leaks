package net.danlew.rxsubscriptions;

import android.app.Activity;
import android.os.Bundle;
import rx.Observable;

public class NeverActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_never);

        // This Observable doesn't leak the Activity, even though it feels like it ought to.
        //
        // That's because it doesn't have any links (implicit or explicit) to the
        // containing Activity, unlike the rest of the examples.
        Observable.never().subscribe();
    }
}
