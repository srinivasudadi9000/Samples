package srinivasu.sams.rest;

import android.util.Log;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by USER on 25-07-2017.
 */

public class ApiClient {
    public static final String BASE_URL = "http://sams.mmos.in/samsdev/web/";
    private static Retrofit retrofit = null;

    public static Retrofit getSams(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        Log.d("callme",retrofit.toString());
        return retrofit;
    }
}
