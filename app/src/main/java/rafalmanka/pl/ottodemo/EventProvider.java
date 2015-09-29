package rafalmanka.pl.ottodemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;

/**
 * Created by rafal on 9/29/15.
 */
public class EventProvider {

    private EventProvider() {
    }

    @Nullable
    private static Bus mBus;

    @NonNull
    public static Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }

}
