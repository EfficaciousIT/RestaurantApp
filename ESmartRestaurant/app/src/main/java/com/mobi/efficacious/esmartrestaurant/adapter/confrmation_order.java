package com.mobi.efficacious.esmartrestaurant.adapter;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.activity.MainActivity;
import com.mobi.efficacious.esmartrestaurant.entity.OrderDetail;
import com.mobi.efficacious.esmartrestaurant.fragment.EditOrderDetailsActivity;

import java.util.ArrayList;

public class confrmation_order extends ArrayAdapter<OrderDetail> {
    Context context;
    static int layoutResourceId;
    ArrayList<OrderDetail> order = new ArrayList<OrderDetail>();
    TextView txtfood;
    TextView Qty;
    TextView description;
    RelativeLayout order_edit;

    public confrmation_order(Context context, ArrayList<OrderDetail> ord) {
        super(context, layoutResourceId, ord);
        this.context = context;
        this.order = ord;

    }

    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row = inflater.inflate(R.layout.confrm_menu_dialogbox, parent, false);
        txtfood = (TextView) row.findViewById(R.id.item_Name_order);
        Qty = (TextView) row.findViewById(R.id.qty_order);
        description = (TextView) row.findViewById(R.id.item_description);
        order_edit = (RelativeLayout) row.findViewById(R.id.top_Order);
        order_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!order.get(position).getKitchenStaus().contentEquals("Completed"))
                {
                    ((Activity)context).finish();
                    String description=order.get(position).getVchFoodDescription();
                    if (description != null && !description.isEmpty() && !description.equals("null"))
                    {
                        description=order.get(position).getVchFoodDescription();
                    }else
                    {
                        description=" ";
                    }
                    EditOrderDetailsActivity editOrderDetailsActivity = new EditOrderDetailsActivity();
                    Bundle args = new Bundle();
                    args.putString("Id", order.get(position).getId());
                    args.putString("MenuName", order.get(position).getMenu_Name());
                    args.putString("Qty", order.get(position).getQty());
                    args.putString("Price", order.get(position).getPrice());
                    args.putString("OrderId", order.get(position).getOrder_Id());
                    args.putString("TableName", order.get(position).getTable_Name());
                    args.putString("FoodDescription",description);
                    args.putString("pagename","OrderConfirmation");
                    editOrderDetailsActivity.setArguments(args);
                    MainActivity.fragmentManager.beginTransaction().replace(R.id.content_main, editOrderDetailsActivity).commitAllowingStateLoss();

                }
            }
        });
        txtfood.setText(order.get(position).getMenu_Name());
        Qty.setText(order.get(position).getQty());
        description.setText(order.get(position).getVchFoodDescription());
        return row;
    }

}