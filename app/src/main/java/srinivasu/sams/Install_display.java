package srinivasu.sams;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import srinivasu.sams.Adapter.InstallAdapter;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.GetInstall;
import srinivasu.sams.model.Installation;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;

public class Install_display extends Activity {
    @BindView(R.id.install_recyler)
    RecyclerView install_recyler;
    List<Installation> installations = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.install_display);
        ButterKnife.bind(this);
        install_recyler.setLayoutManager(new LinearLayoutManager(this));
        getInstalllist();
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
            }

            @Override
            public void onFailure(Call<GetInstall> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


}
