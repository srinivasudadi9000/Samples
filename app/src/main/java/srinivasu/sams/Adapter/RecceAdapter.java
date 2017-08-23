package srinivasu.sams.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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
import srinivasu.sams.validation.Validation;

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

        Bitmap bmImage = null;
        if (!Validation.internet(context)) {
            BitmapFactory.Options opt = new BitmapFactory.Options();
            //opt.inSampleSize = 8;
            //opt.inMutable = true;
             bmImage = BitmapFactory.decodeFile(receelist.get(position).getRecce_image().toString(), opt);
            holder.recce_img.setImageBitmap(bmImage);
           Log.d("imagepath",receelist.get(position).getRecce_id().toString()+" " +receelist.get(position).getRecce_image().toString());
            //Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            Picasso.with(context)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_uploads/"+receelist.get(position).getRecce_image().toString())
                    .memoryPolicy(MemoryPolicy.NO_CACHE )
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(holder.recce_img);
        }


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

                    String width="",height="",umoid="",productname="",mainpic="",image1="",image2="",image3="",image4="",address="",
                            latitude="",longitude="";
                    Intent i = new Intent(context,Update_Recce.class);
                    i.putExtra("recce_id",receelist.get(getAdapterPosition()).getRecce_id().toString());
                    if (receelist.get(getAdapterPosition()).getWidth().toString()!=null){
                        i.putExtra("width",receelist.get(getAdapterPosition()).getWidth().toString());
                    }else {
                        i.putExtra("width",width);
                    }
                    if (receelist.get(getAdapterPosition()).getHeight().toString() !=null){
                        i.putExtra("height",receelist.get(getAdapterPosition()).getHeight().toString());
                    }else {
                        i.putExtra("height",height);
                    }
                    if (receelist.get(getAdapterPosition()).getUom_id().toString() != null){
                        i.putExtra("uomid",receelist.get(getAdapterPosition()).getUom_id().toString());
                    }else {
                        i.putExtra("uomid",umoid);
                    }
                    if (receelist.get(getAdapterPosition()).getProduct_name().toString() != null){
                        i.putExtra("productname",receelist.get(getAdapterPosition()).getProduct_name().toString());
                    }else {
                        i.putExtra("productname",productname);
                    }if (receelist.get(getAdapterPosition()).getRecce_image().toString() != null){
                        i.putExtra("mainpic",receelist.get(getAdapterPosition()).getRecce_image().toString());
                    }else {
                        i.putExtra("mainpic",mainpic);
                    }

                    if (receelist.get(getAdapterPosition()).getRecce_image_1().toString() != null){
                        i.putExtra("image1",receelist.get(getAdapterPosition()).getRecce_image_1());
                    }else {
                        i.putExtra("image1",image1);
                    }if (receelist.get(getAdapterPosition()).getRecce_image_2().toString() != null){
                        i.putExtra("image2",receelist.get(getAdapterPosition()).getRecce_image_2());
                    }else {
                        i.putExtra("image2",image2);
                    }
                    if (receelist.get(getAdapterPosition()).getRecce_image_3().toString() != null){
                        i.putExtra("image3",receelist.get(getAdapterPosition()).getRecce_image_3());
                    }else {
                        i.putExtra("image3",image3);
                    }if (receelist.get(getAdapterPosition()).getRecce_image_4().toString() != null){
                        i.putExtra("image4",receelist.get(getAdapterPosition()).getRecce_image_4());
                    }else {
                        i.putExtra("image4",image4);
                    }

                    if (receelist.get(getAdapterPosition()).getOutlet_address().toString() != null){
                        i.putExtra("address",receelist.get(getAdapterPosition()).getOutlet_address());
                    }else {
                        i.putExtra("address",address);
                    }if (receelist.get(getAdapterPosition()).getLatitude().toString() != null){
                        i.putExtra("latitude",receelist.get(getAdapterPosition()).getLatitude());
                    }else {
                        i.putExtra("latitude",latitude);
                    }
                    if (receelist.get(getAdapterPosition()).getLongitude().toString() != null){
                        i.putExtra("longitude",receelist.get(getAdapterPosition()).getLongitude());
                    }else {
                        i.putExtra("longitude",longitude);
                    }
                    itemView.getContext().startActivity(i);
                    ((Activity)itemView.getContext()).finish();
                }
            });

        }
    }


}
