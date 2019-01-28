package com.pindelia.android.vigilsoft.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.pindelia.android.vigilsoft.R;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * TODO: to be replace by the new ListAdapter
 */
public class RecycleViewAdapter extends RecyclerView.Adapter<RecycleViewAdapter.ViewHolder> {
    public static String nature_piece;
    private List<RowItem> rowItems;
    private Context context;


    public RecycleViewAdapter(List<RowItem> rowItems, Context ctx) {
        this.rowItems = rowItems;
        this.context = ctx;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        // create a new view
        @SuppressLint("InflateParams")
        View itemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.list_row, null);

        // create ViewHolder
        return new ViewHolder(itemLayoutView);

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int position) {

        RowItem row = rowItems.get(position);
        viewHolder.label.setText(row.getLabel());
        viewHolder.imgobject.setImageResource(row.getImgobject());
        viewHolder.rowItem = row;

    }

    @Override
    public int getItemCount() {
        return rowItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.mlabel)
        TextView label;
        @BindView(R.id.imgobject)
        ImageView imgobject;

        RowItem rowItem;

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String label = rowItem.getLabel();
                    if (label.equalsIgnoreCase("CARTE D'IDENTITE NATIONALE/CNIB")) {

                    } else if (label.equalsIgnoreCase("PASSEPORT NATIONAL ET INTERNATIONAL")) {

                    }

                }
            });

        }


    }
}
