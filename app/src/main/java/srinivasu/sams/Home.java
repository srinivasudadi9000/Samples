package srinivasu.sams;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import srinivasu.sams.helper.Preferences;

public class Home extends Activity {
    @BindView(R.id.crewnames_tv)
    TextView crewnames_tv;
    @BindView(R.id.lastSync_tv)
    TextView lastSync_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);
        ButterKnife.bind(this);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
        String format = simpleDateFormat.format(new Date());
        lastSync_tv.setText("Last Sync Date : "+format.toString());
        crewnames_tv.setText(Preferences.getVendorname());
        Toast.makeText(getBaseContext(),Preferences.getKey(),Toast.LENGTH_LONG).show();

    }

    @OnClick({ R.id.Recce_btn, R.id.Installtion_btn, R.id.Sync_btn ,R.id.logout_btn })
    public void buttonClicks(View view) {

        switch(view.getId()) {
            case R.id.Recce_btn:
               /* Intent i = new Intent(Home.this,Recces_display.class);
                startActivity(i);*/
                Preferences.setSelection("RECCE");
                Intent i = new Intent(Home.this,Project.class);
                startActivity(i);
                break;

            case R.id.Installtion_btn:
                Preferences.setSelection("INSTALL");
                Intent instal = new Intent(Home.this,Project.class);
                startActivity(instal);
                break;

            case R.id.Sync_btn:
                Intent sync = new Intent(Home.this,Sync.class);
                startActivity(sync);
                break;
            case R.id.logout_btn:
                Toast.makeText(this, "Button3 clicked!", Toast.LENGTH_SHORT).show();
                break;

        }
    }
}
