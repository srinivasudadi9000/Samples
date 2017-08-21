package srinivasu.sams.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;

import srinivasu.sams.R;
import srinivasu.sams.Update_Recce;
import srinivasu.sams.model.Recce;

/**
 * Created by venky on 11-Aug-17.
 */

public class RecceAdapter extends RecyclerView.Adapter<RecceAdapter.Recceholder>{
    private List<Recce> receelist;
    private int rowLayout;
    public Context context;

    public RecceAdapter(List<Recce> receelist, int rowLayout, Context context) {
        //this.receelist.clear();
        //receelist.addAll(receelist);
        this.receelist = receelist;
        this.rowLayout = rowLayout;
        this.context = context;
    }
    @Override
    public Recceholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Recceholder(view);
    }



    @Override
    public void onBindViewHolder(Recceholder holder, final int position) {
        holder.outletname_tv.setText(receelist.get(position).getOutlet_name().toString());
        holder.outletaddress_tv.setText(receelist.get(position).getOutlet_address().toString());
        holder.productname_tv.setText(receelist.get(position).getProduct_name().toString());
        holder.height_width_tv.setText(receelist.get(position).getHeight().toString()+"X"+receelist.get(position).getWidth().toString());
        holder.recce_status_tv.setText(receelist.get(position).getRecce_image_upload_status().toString());
      //  holder.recce_img.setText(receelist.get(position).getOutlet_name().toString());


        Picasso.with(context)
                .load("http://128.199.131.14/samsdev/web/image_uploads/recce_uploads/"+receelist.get(position).getRecce_image().toString())
                .memoryPolicy(MemoryPolicy.NO_CACHE )
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .resize(512, 512)
                .error(R.drawable.dummy)
                .noFade()
                .into(holder.recce_img);

        if (receelist.get(position).getRecce_image_upload_status().equals("Completed")){
            holder.recce_img.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
        if (receelist.get(position).getOutlet_name().equals("SV MOBILE")){
            holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }
    }

    @Override
    public int getItemCount() {
return receelist.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder{
        TextView outletname_tv,outletaddress_tv,productname_tv,height_width_tv,recce_status_tv,recce_edit_tv;
        ImageView recce_img;
        //@BindView(R.id.recce_img) ImageView ;
        public Recceholder(final View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            outletname_tv  = (TextView)itemView.findViewById(R.id.outletname_tv);
            outletaddress_tv  = (TextView)itemView.findViewById(R.id.outletaddress_tv);
            productname_tv  = (TextView)itemView.findViewById(R.id.productname_tv);
            height_width_tv  = (TextView)itemView.findViewById(R.id.height_width_tv);
            recce_status_tv  = (TextView)itemView.findViewById(R.id.recce_status_tv);
            recce_edit_tv  = (TextView)itemView.findViewById(R.id.recce_edit_tv);
            recce_img = (ImageView)itemView.findViewById(R.id.recce_img);

            recce_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context,Update_Recce.class);
                    i.putExtra("recce_id",receelist.get(getAdapterPosition()).getRecce_id().toString());
                    i.putExtra("width",receelist.get(getAdapterPosition()).getWidth().toString());
                    i.putExtra("height",receelist.get(getAdapterPosition()).getHeight().toString());
                    i.putExtra("uomid",receelist.get(getAdapterPosition()).getUom_id().toString());
                    i.putExtra("productname",receelist.get(getAdapterPosition()).getProduct_name().toString());
                    i.putExtra("mainpic",receelist.get(getAdapterPosition()).getRecce_image().toString());
                    i.putExtra("image1",receelist.get(getAdapterPosition()).getRecce_image_1());
                    i.putExtra("image2",receelist.get(getAdapterPosition()).getRecce_image_2());
                    i.putExtra("image3",receelist.get(getAdapterPosition()).getRecce_image_3());
                    i.putExtra("image4",receelist.get(getAdapterPosition()).getRecce_image_4());
                    itemView.getContext().startActivity(i);
                }
            });

        }
    }


}
