package com.example.raydasmesas_28_01_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.adapter.AgendamentoRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.adapter.AgendamentoViewHolder;
import com.example.raydasmesas_28_01_2020.adapter.EntregueRecyclerViewAdapter;
import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

public class Tela_Auxiliar_Click_Notificacao extends AppCompatActivity {

    private Toolbar toolbar_main;
    private RecyclerView rv_tela_auxiliar;
    private Intent it;
    private String idAluguel;
    private String tela;
    private DatabaseReference mDatabase;
    private AgendamentoRecyclerViewAdapter adapterAgendamento;
    private EntregueRecyclerViewAdapter adapterEntregue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tela__auxiliar__click__notificacao);

        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar_main.setTitle("Aluguel da Notificação");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        rv_tela_auxiliar = (RecyclerView) findViewById(R.id.rv_tela_auxiliar);
        it = getIntent();
        if(!isConnected(Tela_Auxiliar_Click_Notificacao.this)){
            Toast.makeText(Tela_Auxiliar_Click_Notificacao.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }
        idAluguel = it.getStringExtra("id");
        tela = it.getStringExtra("tela");
        populaRecyclerView();

    }

    public void populaRecyclerView(){
        rv_tela_auxiliar.setHasFixedSize(true);
        rv_tela_auxiliar.setLayoutManager(new LinearLayoutManager(Tela_Auxiliar_Click_Notificacao.this));
        if(tela.contains("a")){
            adapterAgendamento = new AgendamentoRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("agendamento").orderByChild("uid").equalTo(idAluguel));
            rv_tela_auxiliar.setAdapter(adapterAgendamento);
        }else{
            adapterEntregue = new EntregueRecyclerViewAdapter(Agendamento.class,R.layout.item_agendamento, AgendamentoViewHolder.class,mDatabase.child("entregue").orderByChild("uid").equalTo(idAluguel));
            rv_tela_auxiliar.setAdapter(adapterEntregue);
        }

    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
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
