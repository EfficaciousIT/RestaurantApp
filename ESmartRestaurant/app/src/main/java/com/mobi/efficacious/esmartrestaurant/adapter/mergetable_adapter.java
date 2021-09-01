package com.mobi.efficacious.esmartrestaurant.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobi.efficacious.esmartrestaurant.R;
import com.mobi.efficacious.esmartrestaurant.entity.Tables;

import java.util.ArrayList;

public class mergetable_adapter extends ArrayAdapter<Tables> {
    Context context;
    static int layoutResourceId;
    ArrayList<Tables> table = new ArrayList<Tables>();
    TextView txtTitle;
    ImageView imageItem;
    String TableName;
    CheckBox tableChk;

    public mergetable_adapter(Context context, ArrayList<Tables> tables) {
        super(context, layoutResourceId, tables);
        this.context = context;
        this.table = tables;
    }

    public View getView(final int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.mergetable_adpter, parent, false);
        TableName = table.get(position).getTable_Name();
        txtTitle = (TextView) rowView.findViewById(R.id.item_text);
        imageItem = (ImageView) rowView.findViewById(R.id.item_image);
        tableChk=(CheckBox)rowView.findViewById(R.id.chkbox) ;
        txtTitle.setText(table.get(position).getTable_Name());
        tableChk.setChecked(table.get(position).isMeregeTableChkbx());
        tableChk.setTag(table.get(position));

        imageItem.setImageResource(R.mipmap.table);
        tableChk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(((CheckBox) v).isChecked())
                {
                    CheckBox cb=(CheckBox) v;
                    Tables contact=(Tables) cb.getTag();
                    contact.setMeregeTableChkbx(cb.isChecked());
                    table.get(position).setMeregeTableChkbx(cb.isChecked());
                }else
                {
                    table.get(position).setMeregeTableChkbx(false);
                    tableChk.setChecked(table.get(position).isMeregeTableChkbx());
                    tableChk.setTag(table.get(position));
                }

            }
        });
        return rowView;
    }
    public ArrayList<Tables> getMergeTable()
    {
        return table;
    }
}