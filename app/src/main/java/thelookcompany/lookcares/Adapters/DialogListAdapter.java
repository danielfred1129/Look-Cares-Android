package thelookcompany.lookcares.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import thelookcompany.lookcares.R;
import thelookcompany.lookcares.datamodel.DialogListItem;

/**
 * Created by buddy on 11/8/2016.
 */

public class DialogListAdapter  extends BaseAdapter {

    ArrayList<DialogListItem> listdata;
    public int selectedPosition = -1;

    public DialogListAdapter(ArrayList <DialogListItem> listdata) {
        this.listdata = listdata;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        //return listdata.size();
        return listdata.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        if (listdata != null)
            return listdata.get(position);
        else
            return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public void setSelected(int position) {
        selectedPosition = position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        DialogListAdapter.ViewHolder holder = new DialogListAdapter.ViewHolder();

        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = (LinearLayout) inflater.inflate(R.layout.listitem_dialog, parent, false);
            holder.imgView = (ImageView) view.findViewById(R.id.img_icon_dialoglist);
            holder.txtView = (TextView) view.findViewById(R.id.txt_address_dialoglist);
            view.setTag(holder);
        } else {

            holder = (ViewHolder) view.getTag();
        }

        DialogListItem item = listdata.get(position);
        if (item.img.equals("client"))
            holder.imgView.setImageResource(R.drawable.user_listview);
        else if (item.img.equals("location"))
            holder.imgView.setImageResource(R.drawable.pin);
        holder.txtView.setText(item.address);
        if (selectedPosition == position) {
            view.setBackgroundColor(Color.rgb(242,242,242));
        }
        else
        {
            view.setBackgroundColor(Color.WHITE);
        }

        return view;
    }

    public ArrayList <DialogListItem> getData() {
        return this.listdata;
    }

    public void addData(DialogListItem item) {
        this.listdata.add(item);
        notifyDataSetChanged();
    }

    public void clearData() {
        this.listdata.clear();
        notifyDataSetChanged();
    }

    class ViewHolder {
        public ImageView imgView;
        public TextView txtView;
    }
}