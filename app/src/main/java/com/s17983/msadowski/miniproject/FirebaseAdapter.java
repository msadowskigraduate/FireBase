package com.s17983.msadowski.miniproject;

import android.app.Activity;
import android.content.Context;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

/**
 * Created by sadowsm3 on 11.12.2017.
 */

public class FirebaseAdapter extends ArrayAdapter<Product> {

    private List<Product> products;
    private Activity context;

    public static class ViewHolder {
        public TextView tvProductName;
        public TextView tvProductPrice;
        public TextView tvProductQuantity;
        public CheckBox cbProductBought;
        public CheckBox cbDelete;
    }

    public FirebaseAdapter(Activity context, List<Product> products) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
        this.products = products;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final FirebaseAdapter.ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            final LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.product_list_item, null, true);
            viewHolder = new FirebaseAdapter.ViewHolder();
            viewHolder.tvProductName = (TextView) rowView.findViewById(R.id.tvProductName);
            viewHolder.tvProductPrice = (TextView) rowView.findViewById(R.id.tvProductPrice);
            viewHolder.tvProductQuantity = (TextView) rowView.findViewById(R.id.tvProductQuantity);
            viewHolder.cbProductBought = (CheckBox) rowView.findViewById(R.id.cbProductBought);
            viewHolder.cbDelete = (CheckBox) rowView.findViewById(R.id.bDelete);
            viewHolder.cbDelete.setTag(position);
            viewHolder.cbDelete.setOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Product product = products.get(position);
                            products.remove(position);
                            notifyDataSetChanged();
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

        if (product.isBought()) {
            viewHolder.cbProductBought.setChecked(true);
        } else {
            viewHolder.cbProductBought.setChecked(false);
        }
        return rowView;
    }
}
