package com.example.proyekakhir_khoirulanam.Beranda;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.proyekakhir_khoirulanam.Agenda.LihatAgenda;
import com.example.proyekakhir_khoirulanam.Feedback.LihatFeedback;
import com.example.proyekakhir_khoirulanam.Hadiah.LihatHadiah;
import com.example.proyekakhir_khoirulanam.KontenAnimasi.LihatKontenAnimasi;
import com.example.proyekakhir_khoirulanam.Hadiah.LihatTransaksi;
import com.example.proyekakhir_khoirulanam.Masuk;
import com.example.proyekakhir_khoirulanam.AppController.Preferences;
import com.example.proyekakhir_khoirulanam.Profil.Profil;
import com.example.proyekakhir_khoirulanam.R;

import org.json.JSONArray;
import org.json.JSONObject;

public class BerandaMasyarakat extends AppCompatActivity {
    String  nama,id,email,point;
    ImageView tukarkode,tukarhadiah,konten,feedback,agenda,keluar,profil;
    SharedPreferences sharedpreferences;
    TextView txt_nama,emailku,poin;
    public final static String TAG_NAMA = "username";
    public final static String TAG_ID = "id";
    public final static String TAG_EMAIL = "email";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_beranda_masyarakat);

        getPoin();
        getProfil();
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        profil=findViewById(R.id.profil);
        txt_nama = findViewById(R.id.username);
        emailku =findViewById(R.id.txt_nama_dashboard);

        sharedpreferences = getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        id=getIntent().getStringExtra(TAG_ID);

        Preferences.setid(getBaseContext(),getIntent().getStringExtra(TAG_ID));

        nama = getIntent().getStringExtra(TAG_NAMA);
        email = getIntent().getStringExtra(TAG_EMAIL);
        tukarkode =findViewById(R.id.tukarkode);
        tukarhadiah =findViewById(R.id.tukarhadiah);
        konten=findViewById(R.id.kontenanimasi);
        feedback = findViewById(R.id.feedback);
        agenda=findViewById(R.id.agenda);
        poin =findViewById(R.id.poinsesion);

        //setText
        txt_nama.setText(nama);
        profil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent profils = new Intent(BerandaMasyarakat.this, Profil.class);
                profils.putExtra(TAG_ID, id);

                startActivity(profils);
            }
        });

        tukarkode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent kode= new Intent(BerandaMasyarakat.this, LihatTransaksi.class);
                startActivity(kode);
            }
        });
        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent feedback = new Intent(BerandaMasyarakat.this, LihatFeedback.class);
                startActivity(feedback);
            }
        });
        konten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent konten = new Intent(BerandaMasyarakat.this, LihatKontenAnimasi.class);
                startActivity(konten);
            }
        });
        tukarhadiah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tukarhadiah = new Intent(BerandaMasyarakat.this, LihatHadiah.class);
                tukarhadiah.putExtra(TAG_ID, id);


                startActivity(tukarhadiah);
            }
        });
        agenda.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent agenda = new Intent(BerandaMasyarakat.this, LihatAgenda.class);
                startActivity(agenda);
            }
        });


    }

    private void getProfil() {
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.43.229/relasi/public/api/show/"+getIntent().getStringExtra(TAG_ID);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                try {

                    for( int i=0; i < response.length();i++){
                        JSONObject data = response.getJSONObject(i);
                        Glide.with(getBaseContext())
                                .load( "http://192.168.43.229/relasi/public/foto_user/"+data.getString("file") )
                                .apply(new RequestOptions().centerCrop())
                                .into(profil);
                    }



                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(arrayRequest);

    }

    private void getPoin() {

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        String url = "http://192.168.43.229/relasi/public/api/show/"+getIntent().getStringExtra(TAG_ID);

        JsonArrayRequest arrayRequest = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                try {

                    for( int i=0; i < response.length();i++){
                        JSONObject data = response.getJSONObject(i);
                        poin.setText(data.getString("poin")+"Poin");
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getBaseContext(), error.toString(), Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(arrayRequest);
    }
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(false);
        builder.setMessage("Keluar aplikasi?");
        builder.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user pressed "yes", then he is allowed to exit from application
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });
        builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //if user select "No", just cancel this dialog and continue with app
                dialog.cancel();
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
