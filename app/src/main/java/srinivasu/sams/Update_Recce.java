package srinivasu.sams;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Part;
import retrofit2.http.Query;
import srinivasu.sams.helper.Calulations;
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.UploadRecce;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;
import srinivasu.sams.validation.Validation;

public class Update_Recce extends Activity {
    @BindView(R.id.tvRwidth)
    EditText tvRwidth;
    @BindView(R.id.tvRheight)
    EditText tvRheight;
    @BindView(R.id.tvRwf)
    EditText tvRwf;
    @BindView(R.id.tvRwi)
    EditText tvRwi;
    @BindView(R.id.tvRhf)
    EditText tvRhf;
    @BindView(R.id.tvRhi)
    EditText tvRhi;
    @BindView(R.id.ivRecceImage)
    ImageView ivRecceImage;
    @BindView(R.id.ivOtherImage1)
    ImageView ivOtherImage1;
    @BindView(R.id.ivOtherImage2)
    ImageView ivOtherImage2;
    @BindView(R.id.ivOtherImage3)
    ImageView ivOtherImage3;
    @BindView(R.id.ivOtherImage4)
    ImageView ivOtherImage4;
    @BindView(R.id.spnOums)
    Spinner spnOums;
    String iv_urlRC = null;
    Bitmap bmImage_main = null;
    String width, height, uomid, productname;
    String mainpic, image1, image2, image3, image4,address,latitude,longitude,width_feet,height_feet,width_inch,height_inch;
    File file;
    public int R_IMAGE = 2, O_IMAGE1 = 3, O_IMAGE2 = 4, O_IMAGE3 = 5, O_IMAGE4 = 6;
    protected Uri iv_url1 = null, iv_url2 = null, iv_url3 = null, iv_url4 = null;
    File otherImagefile1 = null, otherImagefile2 = null, otherImagefile3 = null, otherImagefile4 = null, RimgFile = null;
    String compress_image4 = null, compress_image3 = null, compress_image2 = null, compress_1 = null;
    SQLiteDatabase db;
    ArrayAdapter<String> uomsadapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update__recce);
        ButterKnife.bind(this);
        spnOums = (Spinner) findViewById(R.id.spnOums);

        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(Update_Recce.this, new String[]{Manifest.permission.CAMERA, Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 1257);
        }
        width = (getIntent().getStringExtra("width"));
        height = (getIntent().getStringExtra("height"));
        uomid = (getIntent().getStringExtra("uomid"));
        productname = getIntent().getStringExtra("productname");
        mainpic = getIntent().getStringExtra("mainpic");
        image1 = getIntent().getStringExtra("image1");
        image2 = getIntent().getStringExtra("image2");
        image3 = getIntent().getStringExtra("image3");
        image4 = getIntent().getStringExtra("image4");
        address = getIntent().getStringExtra("address");
        latitude = getIntent().getStringExtra("latitude");
        longitude = getIntent().getStringExtra("longitude");
        otherImagefile1 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile2 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile3 = new File(getExternalCacheDir(), "noimage.png");
        otherImagefile4 = new File(getExternalCacheDir(), "noimage.png");
        displayimages();
        if (!otherImagefile1.exists()) {
            try {
                otherImagefile1.createNewFile();
                // iv_url1 = Uri.fromFile(file);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        spnOums.setSelection(Integer.parseInt(uomid));
        spnOums.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

                if (!tvRwidth.getText().toString().isEmpty() && !tvRheight.getText().toString().isEmpty()) {

                    switch (i) {
                        case 0:
                            Toast.makeText(Update_Recce.this, "Please Select type of uom", Toast.LENGTH_SHORT).show();
                            break;
                        case 1:
                            uomid="1";
                            tvRhi.setText(String.valueOf(Calulations.mm_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.mm_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.mm_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.mm_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 2:
                            uomid="2";
                            tvRhi.setText(String.valueOf(Calulations.mm_cm_m_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.mm_cm_m_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches

                            tvRhf.setText(String.valueOf(Calulations.cm_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.cm_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 3:
                            uomid="3";
                            tvRhi.setText(String.valueOf(Calulations.inch_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.inch_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.inch_to_feets(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.inch_to_feets(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 4:
                            uomid="4";
                            tvRhi.setText(String.valueOf(Calulations.m_feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.m_feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.m_to_feets(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.m_to_feets(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 5:
                            uomid="5";
                            tvRhi.setText(String.valueOf(Calulations.feet_to_inches(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwi.setText(String.valueOf(Calulations.feet_to_inches(Double.parseDouble(tvRwidth.getText().toString()))));     //set widht inches
                            tvRhf.setText(String.valueOf(Calulations.feet_to_feet(Double.parseDouble(tvRheight.getText().toString()))));   //set height
                            tvRwf.setText(String.valueOf(Calulations.feet_to_feet(Double.parseDouble(tvRwidth.getText().toString()))));//set widht inches
                            break;
                        case 6:
                            uomid="6";
                            tvRhi.setText("0");   //set height
                            tvRwi.setText("0");     //set widht inches
                            tvRhf.setText("0");   //set height
                            tvRwf.setText("0");//set widht inches
                            break;

                    }

                } else {
                    Toast.makeText(Update_Recce.this, "Please add image!!", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    @OnClick(R.id.back_update)
    public void back() {
        finish();
    }

    public void displayimages() {
        tvRwidth.setText(width);
        tvRheight.setText(height);
        Bitmap bmImage = null;
        if (!Validation.internet(Update_Recce.this)) {
            bmImage = BitmapFactory.decodeFile(mainpic.toString(), null);
            ivRecceImage.setImageBitmap(bmImage);
            bmImage = BitmapFactory.decodeFile(image1.toString(), null);
            ivOtherImage1.setImageBitmap(bmImage);
            bmImage = BitmapFactory.decodeFile(image2.toString(), null);
            ivOtherImage2.setImageBitmap(bmImage);
            bmImage = BitmapFactory.decodeFile(image3.toString(), null);
            ivOtherImage3.setImageBitmap(bmImage);
            bmImage = BitmapFactory.decodeFile(image4.toString(), null);
            ivOtherImage4.setImageBitmap(bmImage);

        } else {
            Picasso.with(Update_Recce.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_uploads/" + mainpic)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(ivRecceImage);
            Picasso.with(Update_Recce.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_images_1_uploads/" + image1)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(ivOtherImage1);

            Picasso.with(Update_Recce.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_images_2_uploads/" + image2)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(ivOtherImage2);

            Picasso.with(Update_Recce.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_images_3_uploads/" + image3)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(ivOtherImage3);
            Picasso.with(Update_Recce.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_images_4_uploads/" + image4)
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(ivOtherImage4);

        }

    }

    @OnClick(R.id.ivOtherImage4)
    public void imagefour() {

     /*   Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile4 = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!otherImagefile4.exists()) {
            otherImagefile4.mkdirs();
        }

        iv_url4 = Uri.parse(otherImagefile4.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        //compress_image4 = otherImagefile4.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg";
        intent.putExtra(MediaStore.EXTRA_OUTPUT, compress_image4);
        startActivityForResult(intent, O_IMAGE4);*/


        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile4 = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url4 = Uri.fromFile(otherImagefile4);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url4);
        startActivityForResult(intent, O_IMAGE4);
    }

    @OnClick(R.id.ivOtherImage3)
    public void imagethree() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile3 = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url3 = Uri.fromFile(otherImagefile3);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url3);
        startActivityForResult(intent, O_IMAGE3);
    }

    @OnClick(R.id.ivOtherImage2)
    public void imagtwo() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile2 = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url2 = Uri.fromFile(otherImagefile2);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url2);
        startActivityForResult(intent, O_IMAGE2);
    }

    @OnClick(R.id.ivOtherImage1)
    public void imageone() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        otherImagefile1 = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url1 = Uri.fromFile(otherImagefile1);

        Log.d("hu", otherImagefile1.toString());
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url1);
        startActivityForResult(intent, O_IMAGE1);
    }

    @OnClick(R.id.ivRecceImage)
    public void recceMan() {
        Toast.makeText(getBaseContext(), "hello", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(Update_Recce.this, CanvasEdit.class);
        intent.putExtra("width", tvRwidth.getText().toString());
        intent.putExtra("height", tvRheight.getText().toString());
        intent.putExtra("product_name", productname);
        intent.putExtra("uomname", "1");
        startActivityForResult(intent, 1);
    }

    @OnClick(R.id.done_rl)
    public void done() {

        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey());
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid());
        RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project());
        RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("recce_id").toString());
        /*Log.d("iv_url1", iv_url1.toString());
        Log.d("iv_url2", iv_url2.toString());
        Log.d("iv_url3", iv_url3.toString());
        Log.d("iv_url4", iv_url4.toString());*/
        if (iv_urlRC == null) {
            Toast.makeText(getBaseContext(), "please select Main image", Toast.LENGTH_SHORT).show();
        } else if (iv_url1 == null) {
            Toast.makeText(getBaseContext(), "please fill all details", Toast.LENGTH_SHORT).show();
        } else {
            width = tvRwidth.getText().toString();
            height = tvRheight.getText().toString();
            width_feet=tvRwf.getText().toString();
            height_feet=tvRhf.getText().toString();
            width_inch=tvRwi.getText().toString();
            height_inch= tvRhi.getText().toString();

            File mainpic = new File(iv_urlRC.toString());
            compressImage(mainpic.getAbsolutePath().toString());
            MultipartBody.Part filePart = MultipartBody.Part.createFormData("recce_image", mainpic.getName(),
                    RequestBody.create(MediaType.parse("image/*"), mainpic));
            MultipartBody.Part imageFilePart1 = MultipartBody.Part.createFormData("recce_image_1", otherImagefile1.getName(),
                    RequestBody.create(MediaType.parse("image/*"), otherImagefile1));
            MultipartBody.Part imageFilePart2 = MultipartBody.Part.createFormData("recce_image_2", otherImagefile2.getName(),
                    RequestBody.create(MediaType.parse("image/*"), otherImagefile2));
            MultipartBody.Part imageFilePart3 = MultipartBody.Part.createFormData("recce_image_3", otherImagefile3.getName(),
                    RequestBody.create(MediaType.parse("image/*"), otherImagefile3));
            MultipartBody.Part imageFilePart4 = MultipartBody.Part.createFormData("recce_image_4", otherImagefile4.getName(),
                    RequestBody.create(MediaType.parse("image/*"), otherImagefile4));
            RequestBody lat = RequestBody.create(MediaType.parse("text/plain"), latitude);
            RequestBody log = RequestBody.create(MediaType.parse("text/plain"), longitude);
            RequestBody address_s = RequestBody.create(MediaType.parse("text/plain"), address);
            uploadRecce(uomid, width, height
                    , width_feet, height_feet, width_inch
                    ,height_inch, key, userid, crew_person_id, recce_id, filePart, imageFilePart1,
                    imageFilePart2, imageFilePart3, imageFilePart4, lat, log, address_s);

            Log.d("updateimagereccepath", mainpic.getAbsolutePath().toString());
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1257:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    Toast.makeText(getApplicationContext(), "Please Allow Permissions to continue.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inMutable = true;
                //this.canvas.setBackground(getResources().getDrawable(R.drawable.draw));
                Log.d("hh", Uri.parse(data.getExtras().get("imagePath").toString()).toString());
                Bitmap bmImage = BitmapFactory.decodeFile(data.getExtras().get("imagePath").toString(), opt);
                iv_urlRC = data.getExtras().get("imagePath").toString();
                //ivRecceImage.setImageBitmap(bmImage);
                if (data.getExtras().containsKey("startX")) {
                    int startX = (int) data.getExtras().getFloat("startX");
                    int startY = (int) data.getExtras().getFloat("startY");
                    int endX = (int) data.getExtras().getFloat("controlX");
                    int endY = (int) data.getExtras().getFloat("controlY");
                    Rect rect = new Rect(startX, startY, endX, endY);
                    //calculate height and length
                    tvRwidth.setText(data.getExtras().getString("Width") + "");
                    tvRheight.setText(data.getExtras().getString("Height") + "");

                    try {
                        BitmapFactory.Options opt_hel = new BitmapFactory.Options();
                        opt_hel.inSampleSize = 8;
                        opt_hel.inMutable = true;
                        bmImage_main = BitmapFactory.decodeFile(iv_urlRC.toString(), opt);
                        ivRecceImage.setImageBitmap(bmImage_main);
                        compressImage(iv_urlRC.toString());
                        //ivOtherImage1.setImageBitmap(bmImage_main);
                    } catch (Exception e) {
                        Log.e("error", e.getMessage());
                    }
                }
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == O_IMAGE1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(iv_url1.getPath().toString(), opt);
                ivOtherImage1.setImageBitmap(bmImage);
                compressImage(otherImagefile1.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("error", e.getMessage());
            }
        } else if (requestCode == O_IMAGE2 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(iv_url2.getPath().toString(), opt);
                ivOtherImage2.setImageBitmap(bmImage);
                compressImage(otherImagefile2.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("msg", e.getMessage());
            }
        } else if (requestCode == O_IMAGE3 && resultCode == RESULT_OK) {

            BitmapFactory.Options opt = new BitmapFactory.Options();
            opt.inSampleSize = 8;
            opt.inMutable = true;
            Bitmap bmImage = BitmapFactory.decodeFile(iv_url3.getPath().toString(), opt);
            ivOtherImage3.setImageBitmap(bmImage);
            compressImage(otherImagefile3.getAbsolutePath().toString());
        } else if (requestCode == O_IMAGE4 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(iv_url4.getPath().toString(), opt);
                ivOtherImage4.setImageBitmap(bmImage);
                compressImage(otherImagefile4.getAbsolutePath().toString());
            } /*catch (OutOfMemoryError ome) {
                ome.printStackTrace();
            }*/ catch (Exception e) {
                Log.e("e", e.getMessage());
            }

        }
    }


    public void uploadRecce(@Query("uom_id") String uom_id, @Query("width") String width,
                            @Query("height") String height, @Query("w_f") String w_f, @Query("h_f") String h_f,
                            @Query("w_i") String w_i, @Query("h_i") String h_i, @Part("key") RequestBody key,
                            @Part("user_id") RequestBody user_id, @Part("h_i") RequestBody crew_person_id,
                            @Part("recce_id") RequestBody recce_id,
                            @Part MultipartBody.Part recce_image,
                            @Part MultipartBody.Part recce_image_1,
                            @Part MultipartBody.Part recce_image_2,
                            @Part MultipartBody.Part recce_image_3,
                            @Part MultipartBody.Part recce_image_4,
                            @Part("latitude") final RequestBody lat,
                            @Part("longitude") RequestBody longi, @Part("outlet_address") RequestBody outlet_address) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadRecce> call = apiService.getUploadRecce(uom_id, width, height, w_f, h_f, w_i, h_i, key,
                user_id, crew_person_id, recce_id, recce_image, recce_image_1, recce_image_2, recce_image_3, recce_image_4,
                lat, longi, outlet_address);
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
                updateRecce_Localdb(uomid, tvRwidth.getText().toString(), tvRheight.getText().toString()
                        , "22", "22", "2", "2", Preferences.getKey(), Preferences.getUserid(), Preferences.getCrewPersonid_project()
                        , getIntent().getStringExtra("recce_id").toString(), iv_urlRC,
                        otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                        , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                        latitude, longitude, address, Preferences.getProjectId(), "online_update", "COMPLETED");

            }

            @Override
            public void onFailure(Call<UploadRecce> call, Throwable throwable) {
                Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                Log.d("message_image", throwable.toString());
                updateRecce_Localdb(uomid, tvRwidth.getText().toString(), tvRheight.getText().toString()
                        , "22", "22", "2", "2", Preferences.getKey(), Preferences.getUserid(), Preferences.getCrewPersonid_project()
                        , getIntent().getStringExtra("recce_id").toString(), iv_urlRC,
                        otherImagefile1.getAbsolutePath().toString(), otherImagefile2.getAbsolutePath().toString()
                        , otherImagefile3.getAbsolutePath().toString(), otherImagefile4.getAbsolutePath().toString(),
                        "20.22", "20.22", "vizag", Preferences.getProjectId(), "offline_update", "COMPLETED");

            }
        });
    }

    public String getFilename() {
        File file = new File(Environment.getExternalStorageDirectory().getPath(), "MyFolder/Images");
        if (!file.exists()) {
            file.mkdirs();
        }
        String uriSting = (file.getAbsolutePath() + "/" + System.currentTimeMillis() + ".jpg");
        return uriSting;

    }

    private String getRealPathFromURI(String contentURI) {
        Uri contentUri = Uri.parse(contentURI);
        Cursor cursor = getContentResolver().query(contentUri, null, null, null, null);
        if (cursor == null) {
            return contentUri.getPath();
        } else {
            cursor.moveToFirst();
            int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            return cursor.getString(index);
        }
    }

    public int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }
        final float totalPixels = width * height;
        final float totalReqPixelsCap = reqWidth * reqHeight * 2;
        while (totalPixels / (inSampleSize * inSampleSize) > totalReqPixelsCap) {
            inSampleSize++;
        }

        return inSampleSize;
    }

    // Uploading Image/Video
    public String compressImage(String imageUri) {

        String filePath = getRealPathFromURI(imageUri);
        Bitmap scaledBitmap = null;

        BitmapFactory.Options options = new BitmapFactory.Options();

//      by setting this field as true, the actual bitmap pixels are not loaded in the memory. Just the bounds are loaded. If
//      you try the use the bitmap here, you will get null.
        options.inJustDecodeBounds = true;
        Bitmap bmp = BitmapFactory.decodeFile(filePath, options);

        int actualHeight = options.outHeight;
        int actualWidth = options.outWidth;

//      max Height and width values of the compressed image is taken as 816x612

        float maxHeight = 816.0f;
        float maxWidth = 612.0f;
        float imgRatio = actualWidth / actualHeight;
        float maxRatio = maxWidth / maxHeight;

//      width and height values are set maintaining the aspect ratio of the image

        if (actualHeight > maxHeight || actualWidth > maxWidth) {
            if (imgRatio < maxRatio) {
                imgRatio = maxHeight / actualHeight;
                actualWidth = (int) (imgRatio * actualWidth);
                actualHeight = (int) maxHeight;
            } else if (imgRatio > maxRatio) {
                imgRatio = maxWidth / actualWidth;
                actualHeight = (int) (imgRatio * actualHeight);
                actualWidth = (int) maxWidth;
            } else {
                actualHeight = (int) maxHeight;
                actualWidth = (int) maxWidth;

            }
        }

//      setting inSampleSize value allows to load a scaled down version of the original image

        options.inSampleSize = calculateInSampleSize(options, actualWidth, actualHeight);

//      inJustDecodeBounds set to false to load the actual bitmap
        options.inJustDecodeBounds = false;

//      this options allow android to claim the bitmap memory if it runs low on memory
        options.inPurgeable = true;
        options.inInputShareable = true;
        options.inTempStorage = new byte[16 * 1024];

        try {
//          load the bitmap from its path
            bmp = BitmapFactory.decodeFile(filePath, options);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();

        }
        try {
            scaledBitmap = Bitmap.createBitmap(actualWidth, actualHeight, Bitmap.Config.ARGB_8888);
        } catch (OutOfMemoryError exception) {
            exception.printStackTrace();
        }

        float ratioX = actualWidth / (float) options.outWidth;
        float ratioY = actualHeight / (float) options.outHeight;
        float middleX = actualWidth / 2.0f;
        float middleY = actualHeight / 2.0f;

        Matrix scaleMatrix = new Matrix();
        scaleMatrix.setScale(ratioX, ratioY, middleX, middleY);

        Canvas canvas = new Canvas(scaledBitmap);
        canvas.setMatrix(scaleMatrix);
        canvas.drawBitmap(bmp, middleX - bmp.getWidth() / 2, middleY - bmp.getHeight() / 2, new Paint(Paint.FILTER_BITMAP_FLAG));

//      check the rotation of the image and display it properly
        ExifInterface exif;
        try {
            exif = new ExifInterface(filePath);

            int orientation = exif.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 0);
            Log.d("EXIF", "Exif: " + orientation);
            Matrix matrix = new Matrix();
            if (orientation == 6) {
                matrix.postRotate(90);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 3) {
                matrix.postRotate(180);
                Log.d("EXIF", "Exif: " + orientation);
            } else if (orientation == 8) {
                matrix.postRotate(270);
                Log.d("EXIF", "Exif: " + orientation);
            }
            scaledBitmap = Bitmap.createBitmap(scaledBitmap, 0, 0,
                    scaledBitmap.getWidth(), scaledBitmap.getHeight(), matrix,
                    true);
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileOutputStream out = null;
        // String filename = getFilename();
        try {
            out = new FileOutputStream(imageUri);

//          write the compressed bitmap at the destination specified by filename.
            scaledBitmap.compress(Bitmap.CompressFormat.JPEG, 25, out);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return imageUri;

    }

    public void updateRecce_Localdb(String uom_id, String width, String key, String userid, String crewpersonid, String height, String width_feet, String height_feet,
                                    String width_inches, String height_inches, String recce_id, String recce_image,
                                    String recce_image_1, String recce_image_2, String recce_image_3, String recce_image_4,
                                    String latitude, String longitude, String outlet_address, String project_id, String uoms, String status) {

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("UPDATE recce SET uom_id='" + uom_id + "',width='" + width + "',key='" + key + "',userid='" + userid
                + "',crewpersonid='" + crewpersonid +
                "',height='" + height + "',width_feet='" + width_feet + "',uoms='" + uoms +
                "',height_feet='" + height_feet + "',width_inches='" + width_inches +
                "',height_inches='" + height_inches +
                "',recce_image='" + recce_image + "',recce_image_1='" + recce_image_1 +
                "',recce_image_2='" + recce_image_2 + "',recce_image_3='" + recce_image_3 +
                "',recce_image_4='" + recce_image_4 + "',latitude='" + latitude
                + "',recce_image_upload_status='" + status + "',longitude='" + longitude + "',outlet_address='" + outlet_address + "'" + "WHERE recce_id=" + recce_id);
        db.close();
        Log.d("success", "successfully updated recce");
    }


}
