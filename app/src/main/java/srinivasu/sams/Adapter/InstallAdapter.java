package srinivasu.sams.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import srinivasu.sams.Update_Install;
import srinivasu.sams.model.Installation;
import srinivasu.sams.validation.Validation;

/**
 * Created by venky on 11-Aug-17.
 */

public class InstallAdapter extends RecyclerView.Adapter<InstallAdapter.Recceholder> {
    private List<Installation> installations;
    private int rowLayout;
    public Context context;

    public InstallAdapter(List<Installation> installations, int rowLayout, Context context) {
        this.installations = installations;
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
        holder.outletname_tv.setText(installations.get(position).getOutlet_name().toString());
        holder.outletaddress_tv.setText(installations.get(position).getOutlet_address().toString());
        holder.productname_tv.setText(installations.get(position).getProduct_name().toString());
        holder.height_width_tv.setText(installations.get(position).getHeight().toString() + "X" + installations.get(position).getWidth().toString());
        holder.recce_status_tv.setText(installations.get(position).getInstallation_image_upload_status().toString());

        if (installations.get(position).getInstallation_image_upload_status().equals("Completed")){
            holder.recce_img.setScaleType(ImageView.ScaleType.FIT_XY);
        }else {
            holder.recce_img.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        }

        Bitmap bmImage = null;
        if (!Validation.internet(context)) {
            bmImage = BitmapFactory.decodeFile(installations.get(position).getRecce_image().toString(), null);
            holder.recce_img.setImageBitmap(bmImage);

        } else {
            Picasso.with(context)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/install_uploads/" + installations.get(position).getInstallation_image().toString())
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.install_dummy)
                    .noFade()
                    .into(holder.recce_img);
        }

    }

    @Override
    public int getItemCount() {
        return installations.size();
    }

    public class Recceholder extends RecyclerView.ViewHolder {
        TextView outletname_tv, outletaddress_tv, productname_tv, height_width_tv, recce_status_tv, recce_edit_tv;
        ImageView recce_img;

        //@BindView(R.id.recce_img) ImageView ;
        public Recceholder(final View itemView) {
            super(itemView);
            //ButterKnife.bind(itemView);
            outletname_tv = (TextView) itemView.findViewById(R.id.outletname_tv);
            outletaddress_tv = (TextView) itemView.findViewById(R.id.outletaddress_tv);
            productname_tv = (TextView) itemView.findViewById(R.id.productname_tv);
            height_width_tv = (TextView) itemView.findViewById(R.id.height_width_tv);
            recce_status_tv = (TextView) itemView.findViewById(R.id.recce_status_tv);
            recce_edit_tv = (TextView) itemView.findViewById(R.id.recce_edit_tv);
            recce_img = (ImageView) itemView.findViewById(R.id.recce_img);

            recce_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                 //   Toast.makeText(v.getContext(), "install adapter ra babu", Toast.LENGTH_SHORT).show();
                    String installdate="",installremark="",recce_image="",install_image="";
                    Intent updateinstall = new Intent(context, Update_Install.class);
                    updateinstall.putExtra("recce_id",installations.get(getAdapterPosition()).getRecce_id().toString());
                    if (installations.get(getAdapterPosition()).getInstallation_date().toString()!=null){
                        updateinstall.putExtra("install_date",installations.get(getAdapterPosition()).getInstallation_date().toString());
                    }else {
                        updateinstall.putExtra("install_date",installdate);
                    }
                    if (installations.get(getAdapterPosition()).getInstallation_remarks().toString() !=null){
                        updateinstall.putExtra("install_remark",installations.get(getAdapterPosition()).getInstallation_remarks().toString());

                    }else {
                        updateinstall.putExtra("install_remark",installremark);

                    }

                    if (installations.get(getAdapterPosition()).getRecce_image().toString()!=null){
                        updateinstall.putExtra("recce_image",installations.get(getAdapterPosition()).getRecce_image().toString());
                    }else {
                        updateinstall.putExtra("recce_image",recce_image);
                    }

                    if (installations.get(getAdapterPosition()).getInstallation_image().toString()!=null){
                        updateinstall.putExtra("install_image",installations.get(getAdapterPosition()).getInstallation_image().toString());
                    }else {
                        updateinstall.putExtra("install_image",install_image);
                    }

                    ;
                    itemView.getContext().startActivity(updateinstall);
                }
            });

        }
    }
}
