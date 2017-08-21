package srinivasu.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venky on 11-Aug-17.
 */

public class Appopen {
    @SerializedName("vendors_list")
    List<Vendor> vendors_list;

    @SerializedName("status")
    JsonElement status;

    public List<Vendor> getVendors_list() {
        return vendors_list;
    }

    public JsonElement getStatus() {
        return status;
    }
}
