package rafalmanka.pl.ottodemo;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.squareup.otto.Bus;

/**
 * Created by rafal on 9/28/15.
 */
public class BusProvider {

    @Nullable
    private static Bus mBus;

    private BusProvider() {
        // singleton
    }

    @NonNull
    public static Bus getInstance() {
        if (mBus == null) {
            mBus = new Bus();
        }
        return mBus;
    }
}
