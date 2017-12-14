package com.s17983.msadowski.miniproject;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;

import java.util.List;

import javax.inject.Inject;

import lombok.Builder;

/**
 * Created by sadowsm3 on 11.12.2017.
 */

public class FirebaseAdapter extends ArrayAdapter<Product> {

    private List<Product> products;
    private Activity context;
    private DatabaseReference mFirebaseDatabase;

    @Inject
    public FirebaseAdapter(Activity context, List<Product> products, DatabaseReference mFirebaseDatabase) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
        this.products = products;
        this.mFirebaseDatabase = mFirebaseDatabase;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final FirebaseAdapter.ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            final LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.product_list_item, null, true);
            viewHolder = ViewHolder.builder()
                    .tvProductName((TextView) rowView.findViewById(R.id.tvProductName))
                    .tvProductPrice((TextView) rowView.findViewById(R.id.tvProductPrice))
                    .tvProductQuantity((TextView) rowView.findViewById(R.id.tvProductQuantity))
                    .cbProductBought((CheckBox) rowView.findViewById(R.id.cbProductBought))
                    .cbDelete((CheckBox) rowView.findViewById(R.id.bDelete))
                    .build();

            viewHolder.cbDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Product product = products.get(position);
                            Log.e(getContext().getClass().getName(), "Deleting item of ID: " + product.getProductId() + " position: " + position);
                            mFirebaseDatabase.child(product.getProductId()).removeValue();
                            products.remove(position);
                            notifyDataSetChanged();
                            Log.e(getContext().getClass().getName(), "Deleted!");
                        }
                    }
            );
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (FirebaseAdapter.ViewHolder) rowView.getTag();
        }
        Product product = products.get(position);
        viewHolder.tvProductName.setText(product.getProductName());
        viewHolder.tvProductPrice.setText(String.valueOf(product.getProductPrice()));
        viewHolder.tvProductQuantity.setText(String.valueOf(product.getProductQuantity()));
        viewHolder.cbProductBought.setChecked(product.isBought());
        return rowView;
    }

    @Builder
    public static class ViewHolder {
        public TextView tvProductName;
        public TextView tvProductPrice;
        public TextView tvProductQuantity;
        public CheckBox cbProductBought;
        public CheckBox cbDelete;
    }
}
