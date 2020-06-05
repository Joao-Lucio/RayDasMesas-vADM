package com.example.raydasmesas_28_01_2020;


import android.content.Context;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.AgendamentoRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.adapter.AgendamentoViewHolder;
import com.example.raydasmesas_28_01_2020.adapter.HistoricoRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Fragment_Historico extends Fragment {

    private Context context;
    private RecyclerView rvHistorico;
    private HistoricoRecyclerViewAdapter adapter;
    private DatabaseReference mDatabase;
    private Spinner spAno;
    private String ano;
    private TextView tvNenhum;

    public Fragment_Historico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment__historico, container, false);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        spAno = (Spinner) root.findViewById(R.id.sp_Ano);
        rvHistorico = (RecyclerView) root.findViewById(R.id.rv_historico);
        tvNenhum = (TextView) root.findViewById(R.id.tvNenhum);

        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        populaRecyclerView();

        return root;
    }

    public void populaRecyclerView(){
        rvHistorico.setHasFixedSize(true);
        rvHistorico.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new HistoricoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("historico"));
        //rvHistorico.setAdapter(adapter);
        acoes();
    }

    public void acoes(){
        ArrayAdapter<CharSequence> arraySpinner = ArrayAdapter.createFromResource(context, R.array.spinner_anoTodos, android.R.layout.simple_list_item_activated_1);
        spAno.setAdapter(arraySpinner);
        spAno.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                adapter.cleanup();
                ano = parent.getItemAtPosition(position).toString();
                if(ano.contains("Todos")){
                    adapter = new HistoricoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("historico"));
                }else{
                    adapter = new HistoricoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("historico").orderByChild("anoString").equalTo(ano));
                    mDatabase.child("historico").orderByChild("anoString").equalTo(ano).addValueEventListener(new ValueEventListener() {
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
                rvHistorico.setAdapter(adapter);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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
