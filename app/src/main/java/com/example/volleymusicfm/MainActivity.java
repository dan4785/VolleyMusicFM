package com.example.volleymusicfm;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private TextView txSummary, txUrl;
    private String URL;
    private EditText etArtista;
    private Button btnBuscar;

    private ArrayList<String> tagsArray = new ArrayList<>();
    private ArrayList<Artista> artistasArray = new ArrayList<Artista>();

    private LinearLayout llTags;
    private LinearLayout llArtistasAfines;

    private Adaptador adaptador;
    private ListView lsArtisas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        etArtista = findViewById(R.id.etArtista);
        btnBuscar = findViewById(R.id.btnBuscar);
        txSummary = findViewById(R.id.txSummary);
        txUrl = findViewById(R.id.txUrl);
        llTags = findViewById(R.id.llTags);
        lsArtisas = findViewById(R.id.lsArtistas);
        llArtistasAfines = findViewById(R.id.llArtistasAfines);

        btnBuscar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                URL = "http://ws.audioscrobbler.com/2.0/?method=artist.getinfo&artist=" + etArtista.getText() + "&api_key=abb9946f0a11e20176d1b526979fb04b&format=json";
                RequestQueue request = Volley.newRequestQueue(MainActivity.this);

                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            tagsArray.clear();
                            artistasArray.clear();

                            JSONObject artista = response.getJSONObject("artist");

                            String urlArtista = artista.getString("url");
                            txUrl.setText(urlArtista);

                            txUrl.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {

                                    Intent intent = new Intent(Intent.ACTION_VIEW,Uri.parse(urlArtista));
                                    startActivity(intent);
                                }
                            });

                            JSONObject tagsJSON = artista.getJSONObject("tags");


                            JSONArray tag = tagsJSON.getJSONArray("tag");

                            for (int i = 0; i < tag.length(); i++) {
                                String tagName = tag.getJSONObject(i).getString("name");
                                tagsArray.add(tagName);

                            }

                            for (String s : tagsArray
                                 ) {
                                TextView textView = new TextView(MainActivity.this);
                                textView.setText(s + " ");
                                textView.setTextSize(20);

                                llTags.addView(textView);
                            }

                            JSONObject bioJSON = artista.getJSONObject("bio");
                            String summary = bioJSON.getString("summary");
                            txSummary.setText(summary);

                            JSONObject similarJSON = artista.getJSONObject("similar");

                            JSONArray artistasSimilares = similarJSON.getJSONArray("artist");


                            for (int i = 0; i <artistasSimilares.length() ; i++) {
                                String artistaName = artistasSimilares.getJSONObject(i).getString("name");
                                String urlArtistaSimilar = artistasSimilares.getJSONObject(i).getString("url");
                                Artista artista1 = new Artista(artistaName, urlArtistaSimilar);

                                artistasArray.add(artista1);
                            }

                            adaptador = new Adaptador(getApplicationContext(), artistasArray);

                            TextView txArtisaAfines = new TextView(MainActivity.this);
                            txArtisaAfines.setText("ARTISTAS AFINES");
                            txArtisaAfines.setTextSize(25);
                            txArtisaAfines.setGravity(1);

                            llArtistasAfines.addView(txArtisaAfines);

                            lsArtisas.setAdapter(adaptador);

                            Log.i("SALIDA",urlArtista + " " + tagsArray.get(1) + "\n" + summary +  "\n" + artistasArray.get(1).toString());

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERROR", error.getMessage());
                    }
                });
                request.add(jsonObjectRequest);
            }
        });
    }
}