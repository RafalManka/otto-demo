package rafalmanka.pl.ottodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

/**
 * Created by rafal on 9/28/15.
 */
public class ProductDetailsActivity {

    private static final String KEY_PRODUCT = "KEY_PRODUCT";

    public static Intent createIntent(Activity activity, Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        Intent intent = new Intent(activity, ProductDetailsActivity.class);
        intent.putExtras(bundle);
        return intent;
    }
}
