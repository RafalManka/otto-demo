package rafalmanka.pl.ottodemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class ProductListActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private static final String TAG = ProductListActivity.class.getSimpleName();
    @Nullable
    private ListView mListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);
        mListView = (ListView) findViewById(R.id.listView);
        if (mListView != null) {
            mListView.setAdapter(new ProductListActivityAdapter());
            mListView.setOnItemClickListener(this);
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

    private class ProductListActivityAdapter extends BaseAdapter {

        private final ArrayList<Product> _products;

        ProductListActivityAdapter() {
            _products = MockProducts.createMockProducts();
        }

        @Override
        public int getCount() {
            return _products.size();
        }

        @Override
        public Product getItem(int position) {
            return _products.get(position);
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
            viewHolder.txtDescription.setText(product.getDescription());
            viewHolder.txtTitle.setText(product.getName());
            viewHolder.txtPrice.setText(String.valueOf((product.getPrice() / 100)));

            try {
                Picasso.with(ProductListActivity.this).load(product.getImageUrl()).into(viewHolder.ivImage);
            } catch (Product.ProductException e) {
                Log.d(TAG, "product image could not be set");
            }

            return convertView;
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
}
