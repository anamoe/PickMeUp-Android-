package com.example.proyekakhir_khoirulanam.Agenda;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.proyekakhir_khoirulanam.Adapter.AgendaAdapterView;
import com.example.proyekakhir_khoirulanam.Beranda.BerandaPetugasLapangan;
import com.example.proyekakhir_khoirulanam.Constructor.Agenda;
import com.example.proyekakhir_khoirulanam.Masuk;
import com.example.proyekakhir_khoirulanam.Model.ModelAgenda;
import com.example.proyekakhir_khoirulanam.R;

import java.util.ArrayList;

public class LihatAgendaPL extends AppCompatActivity {
    RecyclerView rvNama;
    AgendaAdapterView agendaAdapter;
    ArrayList<Agenda> agendaArrayList;
    RequestQueue queue;
    Toolbar toolbar;
    SharedPreferences sharedpreferences;
    String id,nama;
    public final static String TAG_NAMA = "username";
    public final static String TAG_ID = "id";
    SwipeRefreshLayout swLayout;
    LinearLayout llayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lihat_agenda_p_l);

        swLayout = (SwipeRefreshLayout) findViewById(R.id.swlayout);

        llayout = (LinearLayout) findViewById(R.id.swipe);
        swLayout.setColorSchemeResources(R.color.ecoranger,R.color.petugaslapangan);

        // Mengeset listener yang akan dijalankan saat layar di refresh/swipe
        swLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                // Handler untuk menjalankan jeda selama 5 detik
                new Handler().postDelayed(new Runnable() {
                    @Override public void run() {

                        // Berhenti berputar/refreshing
                        swLayout.setRefreshing(false);
                    }
                }, 5000);
            }
        });

        toolbar = (Toolbar)findViewById(R.id.toolbar);
        toolbar.setTitle(" Agenda");
        toolbar.setTitleTextColor(getResources().getColor(R.color.white));
        setSupportActionBar(toolbar);
        sharedpreferences = getSharedPreferences(Masuk.my_shared_preferences, Context.MODE_PRIVATE);
        id = getIntent().getStringExtra(TAG_ID);
        nama = getIntent().getStringExtra(TAG_NAMA);
        //Set icon to toolbar
        toolbar.setNavigationIcon(R.drawable.back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inten = new Intent(LihatAgendaPL.this, BerandaPetugasLapangan.class);
               inten.putExtra(TAG_ID, id);
                inten.putExtra(TAG_NAMA, nama);
                finish();
                startActivity(inten);
            }
        });

        agendaArrayList = new ArrayList<>();
        rvNama = findViewById(R.id.rv_Agenda);
        queue = Volley.newRequestQueue(this);
        rvNama.setLayoutManager(new LinearLayoutManager(this));
        agendaAdapter = new AgendaAdapterView();

        ModelAgenda model = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(ModelAgenda.class);
        model.simpan(queue,this);
        model.Ambil().observe(this, new Observer<ArrayList<Agenda>>() {
            @Override
            public void onChanged(ArrayList<Agenda> agenda) {
                agendaAdapter.adapter(agenda);
            }
        });
        rvNama.setHasFixedSize(true);
        rvNama.setAdapter(agendaAdapter);
        agendaAdapter.notifyDataSetChanged();

    }

}