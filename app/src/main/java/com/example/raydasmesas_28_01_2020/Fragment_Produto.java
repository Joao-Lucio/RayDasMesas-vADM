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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.ProdutoRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.adapter.ProdutoViewHolder;
import com.example.raydasmesas_28_01_2020.domain.Produto;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class Fragment_Produto extends Fragment {

    private ImageView iv_add_produto;
    private Intent it = new Intent();
    private DatabaseReference mDatabase;
    private RecyclerView rvProduto;
    private ProdutoRecyclerViewAdapter adapter;
    private TextView tvNenhum;

    public Fragment_Produto() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment__produto, container, false);
        iv_add_produto = (ImageView) root.findViewById(R.id.iv_add_produto);
        iv_add_produto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                it.setClass(getContext(),Activity_Add_Alter_Produto.class);
                startActivity(it);
            }
        });

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvProduto = (RecyclerView) root.findViewById(R.id.rv_produto);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void populaRecyclerView(){
        rvProduto.setHasFixedSize(true);
        rvProduto.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new ProdutoRecyclerViewAdapter(Produto.class,R.layout.item_produto, ProdutoViewHolder.class,mDatabase.child("produto"));
        rvProduto.setAdapter(adapter);
    }

    public void verificaQuantidade(){
        mDatabase.child("produto").addValueEventListener(new ValueEventListener() {
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
    public void onResume() {
        super.onResume();

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
