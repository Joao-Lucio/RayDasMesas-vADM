package com.example.raydasmesas_28_01_2020;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.PromocaoRecyclerAdapter;
import com.example.raydasmesas_28_01_2020.adapter.PromocaoViewHolder;
import com.example.raydasmesas_28_01_2020.domain.Promocao;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Promocao extends Fragment {

    private ImageView iv_add_promocao;
    private Intent it = new Intent();
    private DatabaseReference mDatabase;
    private RecyclerView rvPromocao;
    private PromocaoRecyclerAdapter adapter;
    private TextView tvNenhum;

    public Fragment_Promocao() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__promocao, container, false);
        iv_add_promocao = (ImageView) root.findViewById(R.id.iv_add_promocao);
        iv_add_promocao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it.setClass(getContext(),Activity_Add_Alter_Promocao.class);
                startActivity(it);
            }
        });
        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }
        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvPromocao = (RecyclerView) root.findViewById(R.id.rv_promocao);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void populaRecyclerView(){
        rvPromocao.setHasFixedSize(true);
        rvPromocao.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new PromocaoRecyclerAdapter(Promocao.class,R.layout.item_promocao, PromocaoViewHolder.class,mDatabase.child("promocao"));
        rvPromocao.setAdapter(adapter);
    }

    public void verificaQuantidade(){
        mDatabase.child("promocao").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getChildrenCount() == 0){
                    tvNenhum.setVisibility(View.VISIBLE);
                }else{
                    tvNenhum.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        adapter.cleanup();
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isConnected();
        }

        return false;
    }
}
