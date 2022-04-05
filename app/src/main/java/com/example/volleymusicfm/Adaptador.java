package com.example.volleymusicfm;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class Adaptador extends BaseAdapter {

    private Context miContexto;
    private ArrayList<Artista> miArrayList;

    public Adaptador(Context miContexto, ArrayList<Artista> miArrayList){
        this.miContexto=miContexto;
        this.miArrayList=miArrayList;

    }

    @Override
    public int getCount() {
        //return 0;
        return miArrayList.size();
    }

    @Override
    public Object getItem(int i) {
        //return null;
        return miArrayList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        //return null;
        LayoutInflater layoutInflater=LayoutInflater.from(miContexto);
        view=layoutInflater.inflate(R.layout.item_listview, null);


        TextView nombre = view.findViewById(R.id.txArtistaList);
        TextView url = view.findViewById(R.id.txUrlList);

        nombre.setText(miArrayList.get(i).getName());
        url.setText(miArrayList.get(i).getUrl());

        return view;

    }
}
