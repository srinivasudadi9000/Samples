package srinivasu.sams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import srinivasu.sams.helper.DBHelper;
import srinivasu.sams.helper.Preferences;

public class SplashScreen extends Activity {
ImageView myspla;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        myspla = (ImageView)findViewById(R.id.myspla);
        new DBHelper("","","","","",SplashScreen.this);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Preferences.setVendor(null,null,null,SplashScreen.this);
                if (Preferences.getVendorid().equals("")){
                    Intent i = new Intent(SplashScreen.this,Login.class);
                    startActivity(i);
                }else {
                    Intent i = new Intent(SplashScreen.this,Home.class);
                    startActivity(i);
                }
            }
        },2000);



    }
}
