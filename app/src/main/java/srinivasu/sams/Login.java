package srinivasu.sams;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivasu.sams.Adapter.Login_spinner;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.Appopen;
import srinivasu.sams.model.Vendor;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;

public class Login extends Activity {
    ArrayList<String> items;
    Login_spinner login_spinner;
    List<Vendor> vendors;
    @BindView(R.id.spinner_login)
    Spinner spinner_login;
    @BindView(R.id.login_btn)
    Button login_btn;
    String validation_vendor_name = null;
    TelephonyManager manager;
    String imenumber1=null, imenumber2=null;

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        ButterKnife.bind(this);
        //Preferences.setProject("key","65","2");

        spinner_login.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int i, long id) {
                validation_vendor_name = vendors.get(i).getVendor_name().toString();
                //   Toast.makeText(getBaseContext(), vendors.get(i).getVendor_name().toString(), Toast.LENGTH_SHORT).show();
                Preferences.setVendor(vendors.get(i).getVendor_id(), vendors.get(i).getVendor_name(), vendors.get(i).getCrew_person_id(), Login.this);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Preferences.setVendor(null, null, null, Login.this);

            }
        });
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_NETWORK_STATE, Manifest.permission.READ_PHONE_STATE}, 0);
        }
        //getProjects();
        //getRecceslist();
        // getInstalllist();
        manager = (TelephonyManager) getSystemService(Login.TELEPHONY_SERVICE);
        imenumber1 = manager.getDeviceId(0);
        imenumber2 = manager.getDeviceId(1);
        appLogin();
       // Toast.makeText(getBaseContext(), Preferences.getKey(), Toast.LENGTH_LONG).show();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            //resume tasks needing this permission
        }
    }

    @OnClick(R.id.login_btn)
    public void onclick() {
        if (validation_vendor_name !=null){
            Intent i = new Intent(Login.this, Home.class);
            startActivity(i);
        }else {
            Toast.makeText(getBaseContext(),"You are not authorized to proceed !!!",Toast.LENGTH_LONG).show();
        }
        /*Intent i = new Intent(Login.this, Home.class);
        startActivity(i);*/
    }


    public void appLogin() {

        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
       Call<Appopen> call = apiService.getVendors("862114032689487", "862114032689495");
        //Call<Appopen> call = apiService.getVendors(imenumber1, imenumber2);
        call.enqueue(new Callback<Appopen>() {
            @Override
            public void onResponse(Call<Appopen> call, Response<Appopen> response) {
                // String size = String.valueOf(response.body().getList().size());

                String result = String.valueOf(response.code());
                if (result.equals("200")){
                //    Toast.makeText(getBaseContext(), "   " + response.code(), Toast.LENGTH_SHORT).show();
                    vendors = response.body().getVendors_list();
                    login_spinner = new Login_spinner(Login.this, vendors);
                    spinner_login.setAdapter(login_spinner);
                }else{
                    Toast.makeText(getBaseContext(),"you are not authorized person",Toast.LENGTH_SHORT).show();
                }
/*
                for (int i = 0; i < weather.size(); i++) {
                    Toast.makeText(getBaseContext(), weather.get(i).getVendor_name().toString(), Toast.LENGTH_SHORT).show();
                }
*/
            }

            @Override
            public void onFailure(Call<Appopen> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), "Please Check Your Internet Connection ", Toast.LENGTH_SHORT).show();
            }
        });
    }


}
