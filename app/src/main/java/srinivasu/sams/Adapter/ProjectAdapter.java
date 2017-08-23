package srinivasu.sams.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.List;

import srinivasu.sams.Install_display;
import srinivasu.sams.R;
import srinivasu.sams.Recces_display;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.Projects;

/**
 * Created by venky on 11-Aug-17.
 */

public class ProjectAdapter extends RecyclerView.Adapter<ProjectAdapter.Projectholder>{
    private List<Projects> projectsList;
    private int rowLayout;
    private Context context;

    public ProjectAdapter(List<Projects> projectsList, int rowLayout, Context context) {
        this.projectsList = projectsList;
        this.rowLayout = rowLayout;
        this.context = context;
    }    @Override
    public Projectholder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new Projectholder(view);
    }

    @Override
    public void onBindViewHolder(final Projectholder holder, final int position) {
        holder.project_name_tv.setText(projectsList.get(position).getProject_name().toString());
    }

    @Override
    public int getItemCount() {
        return projectsList.size();
    }

    public class Projectholder extends RecyclerView.ViewHolder{
        TextView project_name_tv;
        RelativeLayout project_single_card;
        public Projectholder(final View itemView) {
            super(itemView);
            project_single_card = (RelativeLayout)itemView.findViewById(R.id.project_single_card);
            project_name_tv= (TextView)itemView.findViewById(R.id.project_name_tv);
            project_single_card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Preferences.setProjectId(projectsList.get(getAdapterPosition()).getProject_id().toString());
                    if (Preferences.getSelection().equals("RECCE")){
                        Intent recce_display = new Intent(context,Recces_display.class);
                        recce_display.putExtra("projectid",projectsList.get(getAdapterPosition()).getProject_id().toString());
                        itemView.getContext().startActivity(recce_display);
                    }else {
                        Intent install_display = new Intent(context,Install_display.class);
                        install_display.putExtra("projectid",projectsList.get(getAdapterPosition()).getProject_id().toString());
                        itemView.getContext().startActivity(install_display);
                    }

                }
            });

        }
    }
}
