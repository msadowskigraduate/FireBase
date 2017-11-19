package com.s17983.msadowski.miniproject;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.List;

//import android.graphics.Paint;

public class ProductListAdapter extends ArrayAdapter<Product> {
    private Activity context;
    private List<Product> products;
    private ProductDatabase productDatabase;

    public ProductListAdapter(Activity context, List<Product> products, ProductDatabase productDatabase) {
        super(context, R.layout.product_list_item, products);
        this.context = context;
        this.products = products;
        this.productDatabase = productDatabase;
    }

    static class ViewHolder {
        public TextView tvProductName;
        public TextView tvProductPrice;
        public TextView tvProductQuantity;
        public CheckBox cbProductBought;
        public CheckBox cbDelete;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        View rowView = convertView;

        if (rowView == null) {
            final LayoutInflater layoutInflater = context.getLayoutInflater();
            rowView = layoutInflater.inflate(R.layout.product_list_item, null, true);
            viewHolder = new ViewHolder();
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
                            productDatabase.deleteProduct(product.getProductId());
                            products.remove(position);
                            notifyDataSetChanged();
                        }
                    }
            );
            rowView.setTag(viewHolder);

        } else {
            viewHolder = (ViewHolder) rowView.getTag();
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

        int textColor = OptionsActivity.colorValue;
        String textSize = OptionsActivity.fontValue;
        viewHolder.tvProductName.setTextSize(Float.parseFloat(textSize));
        viewHolder.tvProductName.setTextColor(textColor);
        viewHolder.tvProductPrice.setTextSize(Float.parseFloat(textSize));
        viewHolder.tvProductPrice.setTextColor(textColor);
        viewHolder.tvProductQuantity.setTextSize(Float.parseFloat(textSize));
        viewHolder.tvProductQuantity.setTextColor(textColor);
        viewHolder.cbProductBought.setTextSize(Float.parseFloat(textSize));
        viewHolder.cbProductBought.setTextColor(textColor);
        return rowView;
    }

}
