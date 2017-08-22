package srinivasu.sams;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;
import retrofit2.http.Query;
import srinivasu.sams.helper.DBHelper;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.Recce;
import srinivasu.sams.model.UploadRecce;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;

public class Sync extends AppCompatActivity {
    SQLiteDatabase db;
    String rec_id,project_id,product_name,uom_id,uom_name,outlet_name,outlet_address,longitude,latitude,
            width,height,width_feet,height_feet,width_inches,height_inches,recce_image,recce_image_1,
            recce_image_2,recce_image_3,recce_image_4,recce_image_upload_status,product0;
    File RimgFile=null,otherImagefile1=null,otherImagefile2=null,otherImagefile3=null,otherImagefile4=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sync);
        getRecces_from_local();
    }

    public void getRecces_from_local() {
        ArrayList<Recce> recces_offline = new ArrayList<Recce>();
        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        Toast.makeText(Sync.this, "view my db", Toast.LENGTH_SHORT).show();
        //  Cursor c=db.rawQuery("SELECT * FROM recce WHERE recce_id='"+email+"' and resume='"+resumename+"'", null);
        Cursor c = db.rawQuery("SELECT * FROM recce WHERE uoms='offline_update'", null);

        if (c.moveToFirst()) {
            while (!c.isAfterLast()) {

                 rec_id = c.getString(c.getColumnIndex("recce_id"));
                 project_id = c.getString(c.getColumnIndex("project_id"));
                 product_name = c.getString(c.getColumnIndex("product_name"));
                 uom_id = c.getString(c.getColumnIndex("uom_id"));
                 uom_name = c.getString(c.getColumnIndex("uom_name"));
                 outlet_name = c.getString(c.getColumnIndex("outlet_name"));
                 outlet_address = c.getString(c.getColumnIndex("outlet_address"));
                 longitude = c.getString(c.getColumnIndex("longitude"));
                 latitude = c.getString(c.getColumnIndex("latitude"));
                 width = c.getString(c.getColumnIndex("width"));
                 height = c.getString(c.getColumnIndex("height"));
                 width_feet = c.getString(c.getColumnIndex("width_feet"));
                 height_feet = c.getString(c.getColumnIndex("height_feet"));
                 width_inches = c.getString(c.getColumnIndex("width_inches"));
                 height_inches = c.getString(c.getColumnIndex("height_inches"));
                recce_image = c.getString(c.getColumnIndex("recce_image"));
                 recce_image_1 = c.getString(c.getColumnIndex("recce_image_1"));
                 recce_image_2 = c.getString(c.getColumnIndex("recce_image_2"));
                 recce_image_3 = c.getString(c.getColumnIndex("recce_image_3"));
                 recce_image_4 = c.getString(c.getColumnIndex("recce_image_4"));
                 product0 = c.getString(c.getColumnIndex("product0"));
                // JsonElement uoms=c.getString(c.getColumnIndex("uoms"));
                 recce_image_upload_status = c.getString(c.getColumnIndex("recce_image_upload_status"));
                Log.d("outlet_name_sync", outlet_name);
                //  list.add(name);
                RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey());
                RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid());
                RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), rec_id);
                RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project());


                 RimgFile = new File(recce_image);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData("recce_image", RimgFile.getName(),
                        RequestBody.create(MediaType.parse("image/*"), RimgFile));
                  otherImagefile1 = new File(recce_image_1);
                MultipartBody.Part imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile1));
                  otherImagefile2 = new File(recce_image_2);
                MultipartBody.Part imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile2));
                  otherImagefile3 = new File(recce_image_3);
                MultipartBody.Part imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile3));
                  otherImagefile4 = new File(recce_image_4);
                MultipartBody.Part imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                        RequestBody.create(MediaType.parse("image/*"), otherImagefile4));
                RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude);
                RequestBody logtitude = RequestBody.create(MediaType.parse("text/plain"), longitude);
                RequestBody address = RequestBody.create(MediaType.parse("text/plain"), outlet_address);
                uploadRecce(uom_id, width, height
                        , width_feet, height_feet, width_inches, height_inches, key, userid, crew_person_id, recce_id, filePart, imageFilePart1,
                        imageFilePart2, imageFilePart3, imageFilePart4, lat, logtitude, address);


                c.moveToNext();

            }
        }
    }

    public void uploadRecce(@Query("uom_id") final String uom_id, @Query("width") final String width,
                            @Query("height") final String height, @Query("w_f") String w_f, @Query("h_f") String h_f,
                            @Query("w_i") String w_i, @Query("h_i") String h_i, @Part("key") RequestBody key,
                            @Part("user_id") RequestBody user_id, @Part("h_i") RequestBody crew_person_id,
                            @Part("recce_id") final RequestBody recce_id,
                            @Part MultipartBody.Part recce_image,
                            @Part MultipartBody.Part recce_image_1,
                            @Part MultipartBody.Part recce_image_2,
                            @Part MultipartBody.Part recce_image_3,
                            @Part MultipartBody.Part recce_image_4,
                            @Part("latitude") RequestBody latitude,
                            @Part("longitude") RequestBody longitude, @Part("outlet_address") RequestBody outlet_address) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadRecce> call = apiService.getUploadRecce(uom_id, width, height, w_f, h_f, w_i, h_i, key,
                user_id, crew_person_id, recce_id, recce_image, recce_image_1, recce_image_2, recce_image_3, recce_image_4,
                latitude, longitude, outlet_address);
        call.enqueue(new Callback<UploadRecce>() {
            @Override
            public void onResponse(Call<UploadRecce> call, Response<UploadRecce> response) {
                Log.d("Success", "success " + response.code());
                Log.d("Success", "success " + response.message());

                Toast.makeText(getBaseContext(), " " + response.code(), Toast.LENGTH_SHORT).show();
                Toast.makeText(getBaseContext(), "   " + response.body().getCrew_person_name(), Toast.LENGTH_SHORT).show();
                // Log.d("image",response.body().getCrew_person_name().toString());
                if (response.isSuccessful()) {
                    Toast.makeText(getBaseContext(), "successfull ", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getBaseContext(), "Notvsuccessful ", Toast.LENGTH_SHORT).show();
                }


                DBHelper.updateRecce_Localdb(uom_id, width, Preferences.getKey(), Preferences.getUserid()
                        , Preferences.getCrewPersonid_project(), height
                        , width_feet, height_feet, width_inches, height_inches, rec_id, RimgFile.getAbsolutePath().toString(),
                        otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                        , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                        "20.22", "20.22", "vizag", Preferences.getProjectId(), "online_update", "COMPLETED",Sync.this);

            }

            @Override
            public void onFailure(Call<UploadRecce> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("message_image", throwable.toString());
                DBHelper.updateRecce_Localdb(uom_id, width, Preferences.getKey(), Preferences.getUserid()
                        , Preferences.getCrewPersonid_project(), height
                        , width_feet, height_feet, width_inches, height_inches, rec_id, RimgFile.getAbsolutePath().toString(),
                        otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                        , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                        "20.22", "20.22", "vizag", Preferences.getProjectId(), "offline_update", "NOT COMPLETED",Sync.this);

            }
        });
    }


}
