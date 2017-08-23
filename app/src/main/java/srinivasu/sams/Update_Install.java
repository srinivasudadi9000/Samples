package srinivasu.sams;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
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
import srinivasu.sams.helper.Preferences;
import srinivasu.sams.model.UploadInstall;
import srinivasu.sams.rest.ApiClient;
import srinivasu.sams.rest.ApiInterface;
import srinivasu.sams.validation.Validation;

public class Update_Install extends Activity {
    @BindView(R.id.recceImage) ImageView recceImage;
    @BindView(R.id.recceInstallImage) ImageView recceInstallImage;
    @BindView(R.id.InstallationRemarks_et)  EditText InstallationRemarks_et;
    @BindView(R.id.InstallationDate_et)  EditText InstallationDate_et;
    SQLiteDatabase db;
    File installimage = null;
    protected Uri iv_url1 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update__install);
        ButterKnife.bind(this);

        InstallationDate_et.setText(getIntent().getStringExtra("install_date").toString());
        InstallationRemarks_et.setText(getIntent().getStringExtra("install_remark").toString());

        Bitmap bmImage = null;
        if (!Validation.internet(Update_Install.this)) {
            bmImage = BitmapFactory.decodeFile(getIntent().getStringExtra("install_image"), null);
            recceInstallImage.setImageBitmap(bmImage);

        } else {
            Picasso.with(Update_Install.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/recce_uploads/" + getIntent().getStringExtra("recce_image"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(recceImage);

            Picasso.with(Update_Install.this)
                    .load("http://128.199.131.14/samsdev/web/image_uploads/install_uploads/" +getIntent().getStringExtra("install_image"))
                    .memoryPolicy(MemoryPolicy.NO_CACHE)
                    .networkPolicy(NetworkPolicy.NO_CACHE)
                    .resize(512, 512)
                    .error(R.drawable.dummy)
                    .noFade()
                    .into(recceInstallImage);
        }
    }

    @OnClick(R.id.update_install_btn)
    public void updateInstallrecce(){

       /* RequestBody key = RequestBody.create(MediaType.parse("text/plain"), "vwqoBF2z3p6k5yCMsoSF3hlI1wisRecY");
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), "50");
        RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), "33");
        RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), "2312");
        RequestBody project_id = RequestBody.create(MediaType.parse("text/plain"), "4");*/
        RequestBody key = RequestBody.create(MediaType.parse("text/plain"), Preferences.getKey());
        RequestBody userid = RequestBody.create(MediaType.parse("text/plain"), Preferences.getUserid());
        RequestBody crew_person_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getCrewPersonid_project());
        RequestBody recce_id = RequestBody.create(MediaType.parse("text/plain"), getIntent().getStringExtra("recce_id").toString());
        RequestBody project_id = RequestBody.create(MediaType.parse("text/plain"), Preferences.getProjectId().toString());
        MultipartBody.Part imageFilePart1 = MultipartBody.Part.createFormData("installation_image", installimage.getName(),
                RequestBody.create(MediaType.parse("image/*"), installimage));
        if (iv_url1.toString()!=null){
            updateInstall(InstallationDate_et.getText().toString(),InstallationRemarks_et.getText().toString(),
                    key,userid,crew_person_id,recce_id,project_id,imageFilePart1);
        }else {
            Toast.makeText(getBaseContext(),"please fill all the details",Toast.LENGTH_SHORT).show();
        }
        //updateInstall("21-2-2017","asdfasdf",key,userid,crew_person_id,recce_id,project_id,imageFilePart1);
    }
    @OnClick(R.id.mybutton_click)
    public void backme(){
        Intent project= new Intent(Update_Install.this,Install_display.class);
        startActivity(project);
        finish();
    }
    @OnClick(R.id.recceInstallImage)
    public void installImage_new(){
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        installimage = new File(getExternalCacheDir(),
                String.valueOf(System.currentTimeMillis()) + ".jpg");
        iv_url1 = Uri.fromFile(installimage);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, iv_url1);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            try {
                BitmapFactory.Options opt = new BitmapFactory.Options();
                opt.inSampleSize = 8;
                opt.inMutable = true;
                Bitmap bmImage = BitmapFactory.decodeFile(iv_url1.getPath().toString(), opt);
                recceInstallImage.setImageBitmap(bmImage);
                compressImage(installimage.getAbsolutePath().toString());
                //compressImage(installimage.getAbsolutePath().toString());
            } catch (Exception e) {
                Log.e("e", e.getMessage());
            }

        }
    }

    public void updateInstall(@Query("installation_date") final String installation_date,
                              @Query("installation_remarks") final String installation_remarks,
                              @Part("key") RequestBody key,
                              @Part("user_id") RequestBody user_id, @Part("crew_person_id") RequestBody crew_person_id,
                              @Part("recce_id") final RequestBody recce_id, @Part("project_id") final RequestBody project_id,
                              @Part final MultipartBody.Part installation_image) {
        ApiInterface apiService = ApiClient.getSams().create(ApiInterface.class);
        Call<UploadInstall> call = apiService.getUploadInstall(installation_date,installation_remarks, key,
                user_id,  crew_person_id, recce_id,project_id,installation_image);
        call.enqueue(new Callback<UploadInstall>() {
            @Override
            public void onResponse(Call<UploadInstall> call, Response<UploadInstall> response) {
                String hello = String.valueOf(response.body().getCrew_person_name());
               // Toast.makeText(getBaseContext(),hello,Toast.LENGTH_SHORT).show();

               /* if (response.isSuccessful()){
                    Toast.makeText(getBaseContext(),"successfull ",Toast.LENGTH_SHORT).show();
                    finish();
                }else {
                    Toast.makeText(getBaseContext(),"Notvsuccessful ",Toast.LENGTH_SHORT).show();
                }*/
                updateInstall_Localdb(installation_date,installation_remarks,Preferences.getKey(),Preferences.getUserid(),
                        Preferences.getCrewpersonid(),getIntent().getStringExtra("recce_id").toString(),
                        Preferences.getProjectId(),installimage.getAbsolutePath().toString(),"online_update");
            }

            @Override
            public void onFailure(Call<UploadInstall> call, Throwable throwable) {
              //  Toast.makeText(getBaseContext(), throwable.toString(), Toast.LENGTH_SHORT).show();
                updateInstall_Localdb(installation_date,installation_remarks,Preferences.getKey(),Preferences.getUserid(),
                        Preferences.getCrewpersonid(),getIntent().getStringExtra("recce_id").toString(),
                        Preferences.getProjectId(),installimage.getAbsolutePath().toString(),"offline_update");

                Log.d("message_image",throwable.toString());
            }
        });
    }


    public void updateInstall_Localdb(String install_date, String install_remark, String key, String userid, String crewpersonid,
                                      String recce_id, String project_id, String imagefilepart1,String mode) {

        db = openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);

        db.execSQL("UPDATE install SET installation_date='" + install_date + "',installation_remarks='" + install_remark + "',key='" + key + "',userid='" + userid
                + "',crew_person_id='" + crewpersonid +
                "',project_id='" + project_id + "',installation_image='" + imagefilepart1 + "',product0='" + mode +"'"+
                " WHERE recce_id=" + recce_id);



        db.close();
        Log.d("success", "successfully updated recce");
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
}
