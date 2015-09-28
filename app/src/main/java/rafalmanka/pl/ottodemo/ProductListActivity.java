package rafalmanka.pl.ottodemo;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.otto.Subscribe;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ProductListActivity.class.getSimpleName();
    private static final String KEY_PRODUCTS = "KEY_PRODUCTS";
    @Nullable
    private ListView mListView;
    @Nullable
    private ProductListActivityAdapter mAdapter;
    @Nullable
    private ArrayList<Product> mProducts;

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(KEY_PRODUCTS, mProducts);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        if (savedInstanceState == null) {
            mProducts = MockProducts.createMockProducts();
        } else {
            //noinspection unchecked
            mProducts = (ArrayList<Product>) savedInstanceState.getSerializable(KEY_PRODUCTS);
        }

        mListView = (ListView) findViewById(R.id.listView);
        if (mListView != null) {
            mListView.setAdapter(getAdapter());
            mListView.setOnItemClickListener(this);
        }

        BusProvider.getInstance().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BusProvider.getInstance().unregister(this);
    }

    @Subscribe
    public void onEventProductChanged(EditProductActivity.EventProductChanged event) {
        try {
            getAdapter().updateProduct(event.getProduct());
            getAdapter().notifyDataSetChanged();
        } catch (ProductListActivityException e) {
            Log.e(TAG, "could not update products", e);
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (mListView != null) {
            Product product = (Product) mListView.getItemAtPosition(position);
            Intent intent = ProductDetailsActivity.createIntent(this, product);
            startActivity(intent);
        }

    }

    @NonNull
    public ProductListActivityAdapter getAdapter() {
        if (mAdapter == null) {
            mAdapter = new ProductListActivityAdapter();
        }
        return mAdapter;
    }

    public ArrayList<Product> getProducts() throws ProductListActivityException {
        if (mProducts == null) {
            throw new ProductListActivityException("products are not set");
        }
        return mProducts;
    }

    private class ProductListActivityAdapter extends BaseAdapter {


        @Override
        public int getCount() {
            try {
                return getProducts().size();
            } catch (ProductListActivityException e) {
                return 0;
            }
        }

        @Override
        @Nullable
        public Product getItem(int position) {
            try {
                return getProducts().get(position);
            } catch (ProductListActivityException e) {
                return null;
            }
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolderItem viewHolder;

            if (convertView == null) {
                convertView = LayoutInflater.from(ProductListActivity.this).inflate(R.layout.row_product, parent, false);
                viewHolder = new ViewHolderItem(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolderItem) convertView.getTag();
            }

            Product product = getItem(position);
            if (product != null) {
                viewHolder.txtDescription.setText(product.getDescription());
                viewHolder.txtTitle.setText(product.getName());
                viewHolder.txtPrice.setText(String.valueOf((product.getPrice() / 100)));

                try {
                    Picasso.with(ProductListActivity.this).load(product.getImageUrl()).into(viewHolder.ivImage);
                } catch (Product.ProductException e) {
                    Log.d(TAG, "product image could not be set");
                }
            }

            return convertView;
        }

        public void updateProduct(Product product) throws ProductListActivityException {
            ArrayList<Product> products = getProducts();
            for (int i = 0; i < products.size(); i++) {
                if (products.get(i).getId() == product.getId()) {
                    products.set(i, product);
                    notifyDataSetChanged();
                    return;
                }
            }
        }


        private class ViewHolderItem {

            private final ImageView ivImage;
            private final TextView txtTitle;
            private final TextView txtPrice;
            private final TextView txtDescription;

            public ViewHolderItem(View convertView) {
                ivImage = (ImageView) convertView.findViewById(R.id.ivImage);
                txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
                txtPrice = (TextView) convertView.findViewById(R.id.txtPrice);
                txtDescription = (TextView) convertView.findViewById(R.id.txtDescription);
            }
        }
    }

    private class ProductListActivityException extends Exception {
        public ProductListActivityException(String s) {
            super(s);
        }
    }
}
