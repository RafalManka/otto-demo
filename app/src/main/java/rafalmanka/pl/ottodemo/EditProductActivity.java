package rafalmanka.pl.ottodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


/**
 * Created by rafal on 9/28/15.
 */
public class EditProductActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = EditProductActivity.class.getSimpleName();

    private static final String KEY_PRODUCT = "KEY_PRODUCT";
    @Nullable
    private Product mProduct;
    @Nullable
    private EditText etTitle;
    @Nullable
    private EditText etPrice;
    @Nullable
    private EditText etDescription;

    public static Intent createIntent(Activity activity, Product product) throws EditProductActivityException {
        if (product == null) {
            throw new EditProductActivityException("product can not be null");
        }
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        Intent intent = new Intent(activity, EditProductActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_PRODUCT, mProduct);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_edit);
        if (savedInstanceState == null) {
            mProduct = (Product) getIntent().getSerializableExtra(KEY_PRODUCT);
        } else {
            mProduct = (Product) savedInstanceState.getSerializable(KEY_PRODUCT);
        }
        setupView();
    }

    private void setupView() {
        etTitle = (EditText) findViewById(R.id.etTitle);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDescription = (EditText) findViewById(R.id.etDescription);

        View btnSave = findViewById(R.id.btnSave);
        if (btnSave != null) {
            btnSave.setOnClickListener(this);
        }
        refreshUI();
    }

    private void refreshUI() {
        try {
            Product product = getProduct();

            if (etTitle != null) {
                etTitle.setText(product.getName());
            }
            if (etDescription != null) {
                etDescription.setText(product.getDescription());
            }
            if (etPrice != null) {
                etPrice.setText(String.valueOf(product.getDisplayPrice()));
            }
        } catch (EditProductActivityException e) {
            Log.e(TAG, "could not refresh view");
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSave:
                onButtonSaveClicked();
                break;
        }
    }

    private void onButtonSaveClicked() {
        saveValues();

        try {
            BusProvider.getInstance().post(new EventProductChanged(this, getProduct()));
        } catch (EditProductActivityException e) {
            Log.e(TAG, "could not send event", e);
        }

        onBackPressed();
    }

    private void saveValues() {
        try {
            Product product = getProduct();

            if (etPrice != null) {
                try {
                    product.setPrice(etPrice.getText().toString());
                } catch (Product.ProductException e) {
                    Toast.makeText(this, R.string.toast_error_price_in_wrong_format, Toast.LENGTH_LONG).show();
                }
            }
            if (etTitle != null) {
                product.setName(etTitle.getText().toString());
            }
            if (etDescription != null) {
                product.setDescription(etDescription.getText().toString());
            }
        } catch (EditProductActivityException e) {
            Log.e(TAG, "product is not set", e);
        }
    }

    @NonNull
    public Product getProduct() throws EditProductActivityException {
        if (mProduct == null) {
            throw new EditProductActivityException("product is not set");
        }
        return mProduct;
    }

    public static class EditProductActivityException extends Exception {

        public EditProductActivityException(String s) {
            super(s);
        }
    }

    public class EventProductChanged {

        private final Object sender;
        private final Product product;

        public EventProductChanged(Object sender, Product product) {
            this.sender = sender;
            this.product = product;
        }

        public Product getProduct() {
            return product;
        }
    }
}
