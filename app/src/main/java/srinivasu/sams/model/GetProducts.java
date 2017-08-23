package srinivasu.sams.model;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by venky on 11-Aug-17.
 */

public class GetProducts {
    @SerializedName("key")
    JsonElement key;

    @SerializedName("user_id")
    JsonElement user_id;

    @SerializedName("crew_person_name")
    String crew_person_name;

    @SerializedName("crew_person_id")
    JsonElement crew_person_id;

    @SerializedName("products_list")
    List<Products> productses;


    public JsonElement getCrew_person_id() {
        return crew_person_id;
    }

    public String getCrew_person_name() {
        return crew_person_name;
    }

    public JsonElement getKey() {
        return key;
    }

    public JsonElement getUser_id() {
        return user_id;
    }

    public List<Products> getProductses() {
        return productses;
    }
}
