package com.example.raydasmesas_28_01_2020;


import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.AgendamentoViewHolder;
import com.example.raydasmesas_28_01_2020.adapter.EntregueRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Entregue extends Fragment {

    private Context context;
    private RecyclerView rvEntregue;
    private EntregueRecyclerViewAdapter adapter;
    private DatabaseReference mDatabase;
    private TextView tvNenhum;

    public Fragment_Entregue() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment__entregue, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rvEntregue = (RecyclerView) root.findViewById(R.id.rv_entregue);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void populaRecyclerView(){
        rvEntregue.setHasFixedSize(true);
        rvEntregue.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new EntregueRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("entregue"));
        rvEntregue.setAdapter(adapter);
    }

    public void verificaQuantidade(){
        mDatabase.child("entregue").addValueEventListener(new ValueEventListener() {
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
