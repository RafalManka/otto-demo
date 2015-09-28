package rafalmanka.pl.ottodemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;


/**
 * Created by rafal on 9/28/15.
 */
public class ProductDetailsActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = ProductDetailsActivity.class.getSimpleName();
    private static final String KEY_PRODUCT = "KEY_PRODUCT";

    @Nullable
    private Product mProduct;
    @Nullable
    private ImageView ivMainImage;
    @Nullable
    private ImageView ivAvatar;
    @Nullable
    private TextView txtUserName;
    @Nullable
    private TextView txtLocation;
    @Nullable
    private TextView txtTitle;
    @Nullable
    private TextView txtLikes;
    @Nullable
    private TextView txtPrice;
    @Nullable
    private TextView txtDescription;

    public static Intent createIntent(Activity activity, Product product) {
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_PRODUCT, product);
        Intent intent = new Intent(activity, ProductDetailsActivity.class);
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
        setContentView(R.layout.activity_product_details);
        if (savedInstanceState == null) {
            mProduct = (Product) getIntent().getSerializableExtra(KEY_PRODUCT);
        } else {
            mProduct = (Product) savedInstanceState.getSerializable(KEY_PRODUCT);
        }

        setupView();

        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onEventProductChanged(EditProductActivity.EventProductChanged event){
        mProduct = event.getProduct();
        refreshUI();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    private void setupView() {
        ivMainImage = (ImageView) findViewById(R.id.ivMainImage);
        ivAvatar = (ImageView) findViewById(R.id.ivAvatar);

        txtUserName = (TextView) findViewById(R.id.txtUserName);
        txtLocation = (TextView) findViewById(R.id.txtLocation);
        txtTitle = (TextView) findViewById(R.id.txtTitle);
        txtLikes = (TextView) findViewById(R.id.txtLikes);
        txtPrice = (TextView) findViewById(R.id.txtPrice);
        txtDescription = (TextView) findViewById(R.id.txtDescription);

        View btnEdit = findViewById(R.id.btnEdit);
        if (btnEdit != null) {
            btnEdit.setOnClickListener(this);
        }
        refreshUI();
    }

    private void refreshUI() {
        try {
            Product product = getProduct();
            refreshImages(product);
            refreshTexts(product);
        } catch (ProductDetailsActivityException e) {
            Log.e(TAG, "ProductDetailsActivityException", e);
        }
    }

    private void refreshTexts(@NonNull Product product) {

        try {
            if (txtUserName != null) {
                txtUserName.setText(product.getUser().getName());
            }
            if (txtLocation != null) {
                txtLocation.setText(product.getUser().getLocation());
            }
        } catch (Product.ProductException e) {
            Log.e(TAG, "the user is not set", e);
        }

        if (txtDescription != null) {
            txtDescription.setText(product.getDescription());
        }
        if (txtTitle != null) {
            txtTitle.setText(product.getName());
        }
        if (txtLikes != null) {
            txtLikes.setText(product.getLikes() + " " + getString(R.string.activity_product_details_likes_postfix));
        }
        if (txtPrice != null) {
            txtPrice.setText(product.getDisplayPrice() + " " + getString(R.string.activity_product_details_currency_postfix));
        }
    }

    private void refreshImages(@NonNull Product product) {
        try {
            Picasso.with(this).load(product.getImageUrl()).into(ivMainImage);
        } catch (Product.ProductException e) {
            Log.e(TAG, "Main image could not be loaded", e);
        }

        try {
            Picasso.with(this).load(product.getUser().getAvatar()).into(ivAvatar);
        } catch (Product.ProductException | User.UserException e) {
            Log.e(TAG, "Avatar could not be loaded", e);
        }
    }

    @NonNull
    public Product getProduct() throws ProductDetailsActivityException {
        if (mProduct == null) {
            throw new ProductDetailsActivityException("product is not set");
        }
        return mProduct;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnEdit:
                onEditProductButtonClicked();
                break;
        }
    }

    private void onEditProductButtonClicked() {
        try {
            startActivity(EditProductActivity.createIntent(this, getProduct()));
        } catch (ProductDetailsActivityException | EditProductActivity.EditProductActivityException e) {
            Log.e(TAG, "could not create edit activity");
        }
    }

    private class ProductDetailsActivityException extends Exception {
        public ProductDetailsActivityException(String s) {
            super(s);
        }
    }
}
