package srinivasu.sams.helper;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by venky on 20-Aug-17.
 */

public class Preferences  {
    public static Context context;

    public static void setVendor(String vendor_id, String vendor_name, String crew_person_id,Context mycontext){
        context = mycontext;
        SharedPreferences.Editor vendor = mycontext.getSharedPreferences("VENDOR", mycontext.MODE_PRIVATE).edit();
        vendor.putString("vendor_id",vendor_id);
        vendor.putString("vendor_name",vendor_name);
        vendor.putString("crew_person_id",crew_person_id);
        vendor.commit();
    }
    public static String getVendorid(){
        SharedPreferences vendorid = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return vendorid.getString("vendor_id","");
    }
    public static String getVendorname(){
        SharedPreferences vendorname = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return vendorname.getString("vendor_name","");
    }
    public static String getCrewpersonid(){
        SharedPreferences crewpersonid = context.getSharedPreferences("VENDOR",context.MODE_PRIVATE);
        return crewpersonid.getString("crew_person_id","");
    }
    public static void setProject(String key,String user_id,String crew_person_id){
        SharedPreferences.Editor project = context.getSharedPreferences("PROJECT", context.MODE_PRIVATE).edit();
        project.putString("key",key);
        project.putString("user_id",user_id);
        project.putString("crew_person_id",crew_person_id);
        project.commit();
    }
    public static String getKey(){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("key","");
    }
    public static String getUserid(){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("user_id","");
    }
    public static String getCrewPersonid_project(){
        SharedPreferences key = context.getSharedPreferences("PROJECT",context.MODE_PRIVATE);
        return key.getString("crew_person_id","");
    }
    public static void setProjectId(String projectId){
        SharedPreferences.Editor project = context.getSharedPreferences("PROJECT_ID", context.MODE_PRIVATE).edit();
        project.putString("project_id",projectId);
        project.commit();
    }
    public static String getProjectId(){
        SharedPreferences projectid = context.getSharedPreferences("PROJECT_ID",context.MODE_PRIVATE);
        return projectid.getString("project_id","");
    }
    public static void setSelection(String selection){
        SharedPreferences.Editor type = context.getSharedPreferences("TYPE", context.MODE_PRIVATE).edit();
        type.putString("type",selection);
        type.commit();
    }
    public static String getSelection(){
        SharedPreferences type = context.getSharedPreferences("TYPE",context.MODE_PRIVATE);
        return type.getString("type","");
    }

}
