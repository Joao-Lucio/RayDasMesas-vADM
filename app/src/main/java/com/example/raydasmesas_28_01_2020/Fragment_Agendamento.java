package com.example.raydasmesas_28_01_2020;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.AgendamentoRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.adapter.AgendamentoViewHolder;
import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Agendamento extends Fragment {

    private ImageView iv_add_Agendamento;
    private Context context;
    private Intent it = new Intent();
    private LinearLayout ll_JogoCompleto,ll_SomenteMesa,ll_SomenteCadeira;
    private TextView tvNenhum;
    private RecyclerView rv_agendamento;
    private DatabaseReference mDatabase;
    private AgendamentoRecyclerViewAdapter adapter;
    private CardView card_opcoes;

    public Fragment_Agendamento() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = getContext();
        View root = inflater.inflate(R.layout.fragment__agendamento, container, false);

        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        iniComponentes(root);
        acoes();

        mDatabase = FirebaseDatabase.getInstance().getReference();
        populaRecyclerView();
        verificaQuantidade();

        return root;
    }

    public void iniComponentes(View root){
        card_opcoes = (CardView) root.findViewById(R.id.card_opcoes);
        ll_JogoCompleto = (LinearLayout) root.findViewById(R.id.ll_JogoCompleto);
        ll_SomenteMesa = (LinearLayout) root.findViewById(R.id.ll_SomenteMesa);
        ll_SomenteCadeira = (LinearLayout) root.findViewById(R.id.ll_SomenteCadeira);
        rv_agendamento = (RecyclerView) root.findViewById(R.id.rv_agendamento);
        iv_add_Agendamento = (ImageView) root.findViewById(R.id.iv_add_Agendamento);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);
    }

    public void acoes(){
        iv_add_Agendamento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(card_opcoes.getVisibility() == View.GONE){
                    card_opcoes.setVisibility(View.VISIBLE);
                }else{
                    card_opcoes.setVisibility(View.GONE);
                }

            }
        });

        ll_JogoCompleto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("jogo");
                card_opcoes.setVisibility(View.GONE);
            }
        });

        ll_SomenteMesa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("mesa");
                card_opcoes.setVisibility(View.GONE);
            }
        });

        ll_SomenteCadeira.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chamaActivity("cadeira");
                card_opcoes.setVisibility(View.GONE);
            }
        });
    }

    public void chamaActivity (String tipo){
        it.setClass(context,Activity_Add_Alter_Agendamento.class);
        it.putExtra("tipo",tipo);
        it.putExtra("editado","nao");
        startActivity(it);
    }

    public void populaRecyclerView(){
        rv_agendamento.setHasFixedSize(true);
        rv_agendamento.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AgendamentoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("agendamento").orderByChild("data/month"));
        rv_agendamento.setAdapter(adapter);
    }

    public void verificaQuantidade(){
        mDatabase.child("agendamento").addValueEventListener(new ValueEventListener() {
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
