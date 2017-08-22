package srinivasu.sams;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivasu.sams.Adapter.InstallAdapter;
import srinivasu.sams.helper.DBHelper;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.GetInstall;
import srinivasu.sams.model.Installation;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;
import srinivasu.sams.validation.Validation;

public class Install_display extends Activity {
    @BindView(R.id.install_recyler)
    RecyclerView install_recyler;
    List<Installation> installations = null;
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_display);
        ButterKnife.bind(this);
        install_recyler.setLayoutManager(new LinearLayoutManager(this));

        if (!Validation.internet(Install_display.this)) {
            getInstall_from_local();
            Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            getInstalllist();
        }
    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }

    public void getInstalllist() {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<GetInstall> call = apiService.getInstall(Preferences.getKey(), Preferences.getUserid(),
                getIntent().getStringExtra("projectid"), Preferences.getCrewpersonid());
        call.enqueue(new Callback<GetInstall>() {
            @Override
            public void onResponse(Call<GetInstall> call, Response<GetInstall> response) {
                installations = response.body().getRecces();
                install_recyler.setAdapter(new InstallAdapter(installations, R.layout.install_single, getApplicationContext()));
                new DBHelper( installations , Install_display.this,"install");

            }

            @Override
            public void onFailure(Call<GetInstall> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    public  void getInstall_from_local() {
        ArrayList<Installation> install_offline = new ArrayList<Installation>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Toast.makeText(Install_display.this, "view my db install data ", Toast.LENGTH_SHORT).show();
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
        Cursor c = db.rawQuery("SELECT * FROM install WHERE project_id='"+getIntent().getStringExtra("projectid").toString()+"'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex("recce_id"));
                String recce_id=c.getString(c.getColumnIndex("recce_id"));
                String project_id=c.getString(c.getColumnIndex("project_id"));
               // String vendor_id=c.getString(c.getColumnIndex("vendor_id"));
                String crew_person_id=c.getString(c.getColumnIndex("crew_person_id"));
                String recce_date=c.getString(c.getColumnIndex("recce_date"));
                String outlet_name=c.getString(c.getColumnIndex("outlet_name"));
                String outlet_owner_name=c.getString(c.getColumnIndex("outlet_owner_name"));
                String outlet_address=c.getString(c.getColumnIndex("outlet_address"));
                String longitude=c.getString(c.getColumnIndex("longitude"));
                String latitude=c.getString(c.getColumnIndex("latitude"));
                String recce_image=c.getString(c.getColumnIndex("recce_image"));
                String width=c.getString(c.getColumnIndex("width"));
                String height=c.getString(c.getColumnIndex("height"));
                String width_feet=c.getString(c.getColumnIndex("width_feet"));
                String height_feet=c.getString(c.getColumnIndex("height_feet"));
                String width_inches=c.getString(c.getColumnIndex("width_inches"));
                String height_inches=c.getString(c.getColumnIndex("height_inches"));
                String product_name=c.getString(c.getColumnIndex("product_name"));
                String product0=c.getString(c.getColumnIndex("product0"));
                String installation_date=c.getString(c.getColumnIndex("installation_date"));
                String installation_image=c.getString(c.getColumnIndex("installation_image"));
                String installation_remarks=c.getString(c.getColumnIndex("installation_remarks"));
                String installation_image_upload_status=c.getString(c.getColumnIndex("installation_image_upload_status"));
               // String recce_image_path=c.getString(c.getColumnIndex("recce_image_path"));

                install_offline.add(new Installation(recce_id,project_id,null,crew_person_id,recce_date,outlet_name,
                        outlet_owner_name,outlet_address,longitude,latitude,recce_image,installation_date,installation_image,
                        installation_remarks, width,height,width_feet,height_feet,
                        width_inches,height_inches,product_name,product0,installation_image_upload_status,null));
                Toast.makeText(Install_display.this, " " + name, Toast.LENGTH_SHORT).show();
                Log.d("values",name);
                //  list.add(name);
                c.moveToNext();
            }
        }
        install_recyler.setAdapter(new InstallAdapter(install_offline, R.layout.recee_single, getApplicationContext()));
    }



}
