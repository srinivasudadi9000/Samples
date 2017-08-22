package srinivasu.sams;

import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivasu.sams.Adapter.RecceAdapter;
import srinivasu.sams.helper.DBHelper;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.GetRecce;
import srinivasu.sams.model.Recce;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;
import srinivasu.sams.validation.Validation;

public class Recces_display extends Activity {
    @BindView(R.id.recee_recyler)
    RecyclerView recee_recyler;
    SQLiteDatabase db;
    List<Recce> recces = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recces_display);
        ButterKnife.bind(this);
        recee_recyler.setLayoutManager(new LinearLayoutManager(this));
        //recee_recyler.addOnItemTouchListener(new Recces_display.DrawerItemClickListener());
        recee_recyler.setHasFixedSize(true);
        recee_recyler.setItemViewCacheSize(20);
        recee_recyler.setDrawingCacheEnabled(true);
        recee_recyler.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Log.d("key", Preferences.getKey());
        Log.d("userid", Preferences.getUserid());
        Log.d("crewpersionid", Preferences.getCrewPersonid_project());
        Log.d("projectid", getIntent().getStringExtra("projectid").toString());

       //
        if (!Validation.internet(Recces_display.this)) {
            getRecces_from_local();
            Toast.makeText(getBaseContext(), "local db recces", Toast.LENGTH_LONG).show();
        } else {
            getRecceslist();
        }

    }

    @OnClick(R.id.back)
    public void back() {
        finish();
    }
    public  void getRecces_from_local() {
        ArrayList<Recce> recces_offline = new ArrayList<Recce>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Toast.makeText(Recces_display.this, "view my db", Toast.LENGTH_SHORT).show();
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
        Cursor c = db.rawQuery("SELECT * FROM recce WHERE project_id='"+getIntent().getStringExtra("projectid").toString()+"'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {
                String name = c.getString(c.getColumnIndex("recce_id"));
                String recce_id=c.getString(c.getColumnIndex("recce_id"));
                String project_id=c.getString(c.getColumnIndex("project_id"));
                String product_name=c.getString(c.getColumnIndex("product_name"));
                String zone_id=c.getString(c.getColumnIndex("zone_id"));
                String uom_id=c.getString(c.getColumnIndex("uom_id"));
                String uom_name=c.getString(c.getColumnIndex("uom_name"));
                String recce_date=c.getString(c.getColumnIndex("recce_date"));
                String outlet_name=c.getString(c.getColumnIndex("outlet_name"));
                String outlet_owner_name=c.getString(c.getColumnIndex("outlet_owner_name"));
                String outlet_address=c.getString(c.getColumnIndex("outlet_address"));
                String longitude=c.getString(c.getColumnIndex("longitude"));
                String latitude=c.getString(c.getColumnIndex("latitude"));
                String width=c.getString(c.getColumnIndex("width"));
                String height=c.getString(c.getColumnIndex("height"));
                String width_feet=c.getString(c.getColumnIndex("width_feet"));
                String height_feet=c.getString(c.getColumnIndex("height_feet"));
                String width_inches=c.getString(c.getColumnIndex("width_inches"));
                String height_inches=c.getString(c.getColumnIndex("height_inches"));
                String recce_image=c.getString(c.getColumnIndex("recce_image"));
                String recce_image_1=c.getString(c.getColumnIndex("recce_image_1"));
                String recce_image_2=c.getString(c.getColumnIndex("recce_image_2"));
                String recce_image_3=c.getString(c.getColumnIndex("recce_image_3"));
                String recce_image_4=c.getString(c.getColumnIndex("recce_image_4"));
                String product0=c.getString(c.getColumnIndex("product0"));
               // JsonElement uoms=c.getString(c.getColumnIndex("uoms"));
                String recce_image_upload_status = c.getString(c.getColumnIndex("recce_image_upload_status"));
                recces_offline.add(new Recce(recce_id,project_id,product_name,zone_id,uom_id,uom_name,recce_date,outlet_name,
                        outlet_owner_name,outlet_address,longitude,latitude,width,height,width_feet,height_feet,
                        width_inches,height_inches,recce_image,recce_image_1,recce_image_2,recce_image_3,recce_image_4,
                        product0,null,recce_image_upload_status));
                Log.d("values",name);
                //  list.add(name);
                c.moveToNext();
            }
        }
        recee_recyler.setAdapter(new RecceAdapter(recces_offline, R.layout.recee_single, getApplicationContext()));
    }

    public void getRecceslist() {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<GetRecce> call = apiService.getRecces(Preferences.getKey(), Preferences.getUserid(),
                getIntent().getStringExtra("projectid").toString(), Preferences.getCrewPersonid_project());
        // Call<GetRecce> call = apiService.getRecces("vwqoBF2z3p6k5yCMsoSF3hlI1wisRecY", "50", getIntent().getStringExtra("projectid"), "33");
        //   Call<GetRecce> call = apiService.getRecces("vwqoBF2z3p6k5yCMsoSF3hlI1wisRecY", "50", getIntent().getStringExtra("projectid"), "33");
        call.enqueue(new Callback<GetRecce>() {
            @Override
            public void onResponse(Call<GetRecce> call, Response<GetRecce> response) {
                // String size = String.valueOf(response.body().getList().size());
               /* List<Umo> Umo = response.body().getUoms_list();
                for (int j = 0; j < Umo.size(); j++) {
                    Toast.makeText(getBaseContext(), "   " + Umo.get(j).getUom_name(), Toast.LENGTH_SHORT).show();

                }*/
                recces = response.body().getRecces();
                new DBHelper(recces,Recces_display.this);
                recee_recyler.setAdapter(new RecceAdapter(recces, R.layout.recee_single, getApplicationContext()));

            }

            @Override
            public void onFailure(Call<GetRecce> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });

//

    }



    public String validaterecord(String recceid){

        Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+recceid+"'", null);
        if(c.moveToFirst())
        {
            return "validate";
        }else {
           return "notvalidate";
        }
    }

    private void clearPreferences() {
        try {
            // clearing app data
            Runtime runtime = Runtime.getRuntime();
            runtime.exec("pm clear YOUR_APP_PACKAGE_GOES HERE");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
