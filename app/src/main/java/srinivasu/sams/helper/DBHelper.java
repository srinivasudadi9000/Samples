package srinivasu.sams.helper;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.util.List;

import srinivasu.sams.model.Installation;
import srinivasu.sams.model.Recce;

/**
 * Created by venky on 20-Aug-17.
 */

public class DBHelper {
    SQLiteDatabase db;
    Context context;
    public DBHelper(List<Recce> recces ,Context context){
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS recce(recce_id VARCHAR unique,project_id VARCHAR," +
                "product_name VARCHAR,zone_id VARCHAR,uom_id VARCHAR,uom_name VARCHAR," +
                "recce_date VARCHAR,outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,recce_image VARCHAR,recce_image_1 VARCHAR,recce_image_2 VARCHAR," +
                "recce_image_3 VARCHAR,recce_image_4 VARCHAR," +
                "product0 VARCHAR,uoms VARCHAR,recce_image_upload_status VARCHAR);");
        String size = String.valueOf(recces.size());
        Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < recces.size(); i++) {
            ;
            if (validaterecord(recces.get(i).getRecce_id()).equals("notvalidate")) {
                String index = String.valueOf(i);
                // Toast.makeText(getApplicationContext(), index.toString() + "  r=  " + recces.get(i).getRecce_id(), Toast.LENGTH_SHORT).show();
                insertRecce(recces.get(i).getRecce_id(), recces.get(i).getProject_id(), recces.get(i).getProduct_name().replaceAll("'", ""), recces.get(i).getZone_id()
                        , recces.get(i).getUom_id(), recces.get(i).getUom_name(), recces.get(i).getRecce_date(), recces.get(i).getOutlet_name().replaceAll("'", "")
                        , recces.get(i).getOutlet_owner_name(), recces.get(i).getOutlet_address().replaceAll("'", ""), recces.get(i).getLongitude()
                        , recces.get(i).getLatitude(), recces.get(i).getWidth(), recces.get(i).getHeight(), recces.get(i).getWidth_feet()
                        , recces.get(i).getHeight_feet(), recces.get(i).getWidth_inches(), recces.get(i).getHeight_inches(), recces.get(i).getRecce_image()
                        , recces.get(i).getRecce_image_1(), recces.get(i).getRecce_image_2(), recces.get(i).getRecce_image_3()
                        , recces.get(i).getRecce_image_4(), recces.get(i).getProduct0(), recces.get(i).getHeight()
                        , recces.get(i).getRecce_image_upload_status());
            }
        }
        //viewmydb();
    }




    public DBHelper(List<Installation> installations , Context context,String install){
        this.context = context;
        db = context.openOrCreateDatabase("SAMS", Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS recce(recce_id VARCHAR unique,project_id VARCHAR," +
                "vendor_id VARCHAR,crew_person_id VARCHAR,recce_date VARCHAR," +
                "outlet_name VARCHAR,outlet_owner_name VARCHAR," +
                "outlet_address VARCHAR,longitude VARCHAR,latitude VARCHAR,recce_image VARCHAR,installation_date VARCHAR" +
                ",installation_image VARCHAR,installation_remarks VARCHAR ,width VARCHAR," +
                "height VARCHAR,width_feet VARCHAR,height_feet VARCHAR,width_inches VARCHAR," +
                "height_inches VARCHAR,product_name VARCHAR,product0 VARCHAR,installation_image_upload_status VARCHAR,recce_image_path VARCHAR);");
        String size = String.valueOf(installations.size());
        Toast.makeText(context.getApplicationContext(), size, Toast.LENGTH_SHORT).show();
        for (int i = 0; i < installations.size(); i++) {
            ;
            if (validaterecord(installations.get(i).getRecce_id()).equals("notvalidate")) {
                String index = String.valueOf(i);
                // Toast.makeText(getApplicationContext(), index.toString() + "  r=  " + recces.get(i).getRecce_id(), Toast.LENGTH_SHORT).show();
                insertInstall(installations.get(i).getRecce_id(), installations.get(i).getProject_id(),
                        installations.get(i).getVendor_id(), installations.get(i).getCrew_person_id()
                        , installations.get(i).getRecce_date(), installations.get(i).getOutlet_name(),
                        installations.get(i).getOutlet_owner_name(), installations.get(i).getOutlet_address().replaceAll("'", "")
                        , installations.get(i).getLongitude(), installations.get(i).getLatitude().replaceAll("'", ""),
                        installations.get(i).getRecce_image(), installations.get(i).getInstallation_date(),
                        installations.get(i).getInstallation_image(), installations.get(i).getInstallation_remarks(),
                        installations.get(i).getWidth(),installations.get(i).getHeight(),installations.get(i).getWidth_feet(),
                        installations.get(i).getHeight_feet(), installations.get(i).getWidth_inches(),
                        installations.get(i).getHeight_inches(), installations.get(i).getProduct_name(),
                        installations.get(i).getProduct0(),installations.get(i).getInstallation_image_upload_status(),
                        installations.get(i).getRecce_image());
            }
        }
        //viewmydb();
    }


    public void insertRecce(String recce_id, String project_id, String product_name, String zone_id, String uom_id, String uom_name,
                            String recce_date, String outlet_name, String outlet_owner_name, String outlet_address, String longitude,
                            String latitude, String width, String height, String width_feet, String height_feet, String width_inches,
                            String height_inches, String recce_image, String recce_image_1, String recce_image_2, String recce_image_3,
                            String recce_image_4, String product0, String uoms, String recce_image_upload_status) {
        db.execSQL("INSERT INTO recce VALUES('" + recce_id + "','" + project_id + "','" + product_name + "','" + zone_id + "','" +
                uom_id + "','" + uom_name + "','" + recce_date + "','" + outlet_name + "','" + outlet_owner_name + "','" + outlet_address
                + "','" + longitude + "','" + latitude + "','" + width + "','" + height + "','" + width_feet + "','" + height_feet
                + "','" + width_inches + "','" + height_inches + "','" + recce_image + "','" + recce_image_1 + "','" + recce_image_2 + "','" +
                recce_image_3 + "','" + recce_image_4 + "','" + product0 + "','" + uoms + "','" + recce_image_upload_status + "');");

    }

    public void insertInstall(String recce_id,String project_id,String vendor_id,String crew_person_id,String recce_date,
                              String outlet_name,String outlet_owner_name,String outlet_address,String longitude,String latitude,
                              String recce_image,String installation_date,String installation_image,String installation_remarks,
                              String width,String height,String width_feet,String height_feet,String width_inches,
                              String height_inches,String product_name,String product0,String installation_image_upload_status,
                              String recce_image_path) {
        db.execSQL("INSERT INTO install VALUES('" + recce_id + "','" + project_id + "','" + vendor_id + "','" + crew_person_id + "','" +
                recce_date + "','" + outlet_name + "','" + outlet_owner_name + "','" + outlet_address + "','" + longitude + "','" + outlet_address
                + "','" + latitude + "','" + recce_image + "','" + installation_date + "','" + installation_image + "','" + installation_remarks
                + "','" + width + "','" + height + "','" + width_feet + "','" + height_feet + "','" + width_inches + "','" +
                height_inches + "','" + product_name + "','" + product0 + "','" + installation_image_upload_status + "','" + recce_image_path + "');");

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


}
