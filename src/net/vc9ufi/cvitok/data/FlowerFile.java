package net.vc9ufi.cvitok.data;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class FlowerFile {

    @SerializedName("Name")
    String name;

    @SerializedName("Petals")
    ArrayList<Parameters> petals = new ArrayList<>();

    @SerializedName("Background")
    float[] background;

    @SerializedName("Light")
    Light light;

    public static FlowerFile getFlower(String json) {
        Gson gson = new Gson();
        FlowerFile flower = null;
        try {
            flower = gson.fromJson(json, FlowerFile.class);
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        return flower;
    }

    public String toJSON() {
        return new Gson().toJson(this);
    }
}
