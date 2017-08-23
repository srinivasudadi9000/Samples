package srinivasu.sams.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import srinivasu.sams.R;
import srinivasu.sams.model.Products;

/**
 * Created by venky on 11-Aug-17.
 */

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.Recceholder> {
    private List<Products> productses;
    private int rowLayout;
    public Context context;

    public ProductAdapter(List<Products> productses, int rowLayout, Context context) {
        this.productses = productses;
        this.rowLayout = rowLayout;
        this.context = context;
    }

    @Override
    public Recceholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Recceholder(view);
    }


    @Override
    public void onBindViewHolder(Recceholder holder, int position) {
        Toast.makeText(context,productses.get(position).getProduct_name().toString(),Toast.LENGTH_SHORT).show();
        holder.product_name_tv.setText(productses.get(position).getProduct_name().toString());


    }

    @Override
    public int getItemCount() {
        return productses.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder {
        TextView product_name_tv;

        //@BindView(R.id.recce_img) ImageView ;
        public Recceholder(final View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            product_name_tv = (TextView) itemView.findViewById(R.id.product_name);


        }
    }
}
