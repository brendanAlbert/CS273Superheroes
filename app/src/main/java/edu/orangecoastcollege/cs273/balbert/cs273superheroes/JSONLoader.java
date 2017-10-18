package edu.orangecoastcollege.cs273.balbert.cs273superheroes;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by brendantyleralbert on 10/17/17.
 */

public class JSONLoader {

    public static List<Superhero> loadJSONFromAsset(Context context) throws IOException {
        List<Superhero> allHeroesList = new ArrayList<>();
        String json = null;
        InputStream is = context.getAssets().open("cs273superheroes.json");
        int size = is.available();
        byte[] buffer = new byte[size];
        is.read(buffer);
        is.close();
        json = new String(buffer, "UTF-8");

        try {
            JSONObject jsonRootObject = new JSONObject(json);
            JSONArray allHeroesJSON = jsonRootObject.getJSONArray("CS273Superheroes");
            int length = allHeroesJSON.length();
            for (int i = 0; i < length; ++i)
            {
                JSONObject jsonHeroObject = allHeroesJSON.getJSONObject(i);
                String name = jsonHeroObject.getString("Name");
                String username = jsonHeroObject.getString("Username");
                String superpower = jsonHeroObject.getString("Superpower");
                String oneThing = jsonHeroObject.getString("OneThing");
                Superhero superhero = new Superhero(name, username, superpower, oneThing);
                allHeroesList.add(superhero);
            }
        } catch (JSONException e) {
            Log.e("Superhero Quiz", e.getMessage());
        }

        return allHeroesList;
    }
}
