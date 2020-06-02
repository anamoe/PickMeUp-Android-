package com.example.proyekakhir_khoirulanam.Model;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.proyekakhir_khoirulanam.Constructor.Animasi;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class ModelKontenAnimasi extends ViewModel {
    private MutableLiveData<ArrayList<Animasi>> animasi = new MutableLiveData<>();
    public void simpan (RequestQueue queue, final Context context){
        final ArrayList<Animasi> animasiArrayList= new ArrayList<>();
        String url = "http://192.168.43.229/relasi/public/api/lihatkonten";
        JsonObjectRequest request =  new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                JSONArray data = null;
                try {
                    data = response.getJSONArray("upload");
                    for (int i =0; i <data.length(); i++){
                        JSONObject objek =data.getJSONObject(i);
                        int id = objek.getInt("id");
                        String title = objek.getString("nama_konten");
                        String keterangan =objek.getString("deskripsi");
                        String image = objek.getString("file");
                        Animasi animasi = new Animasi(id, title,keterangan,image);
                        animasiArrayList.add(animasi);

                    }
                    animasi.postValue(animasiArrayList);

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        queue.add(request);
    }
    public LiveData<ArrayList<Animasi>> Ambil(){return animasi;}

}