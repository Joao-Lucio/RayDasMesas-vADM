package com.example.raydasmesas_28_01_2020.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.Activity_Add_Alter_Agendamento;
import com.example.raydasmesas_28_01_2020.MapsActivity;
import com.example.raydasmesas_28_01_2020.R;
import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class HistoricoRecyclerViewAdapter extends FirebaseRecyclerAdapter<Agendamento,AgendamentoViewHolder> {

    private SimpleDateFormat formato;
    private DatabaseReference mDatabase;
    private String pago = "Pago";
    private String status;

    public HistoricoRecyclerViewAdapter(Class<Agendamento> modelClass, int modelLayout, Class<AgendamentoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void populateViewHolder(final AgendamentoViewHolder agendamentoViewHolder, final Agendamento agendamento, int i) {

        status = agendamento.getStatus().toString();
        agendamentoViewHolder.tvQtdMesas.setText(String.valueOf(agendamento.getQtdMesas()));
        agendamentoViewHolder.tvQtdCadeiras.setText(String.valueOf(agendamento.getQtdCadeiras()));
        agendamentoViewHolder.tvData.setText(formato.format(agendamento.getData()));
        agendamentoViewHolder.tvHorario.setText(agendamento.getHorario());
        agendamentoViewHolder.tvDesconto.setText(agendamento.getDesconto()+"%");
        agendamentoViewHolder.tvRua.setText(agendamento.getRua());
        agendamentoViewHolder.tvNumero.setText(String.valueOf(agendamento.getNumero()));
        agendamentoViewHolder.tvBairro.setText(agendamento.getBairro());
        agendamentoViewHolder.tvReferencia.setText(agendamento.getReferencia());
        agendamentoViewHolder.tvContatoResponsavel.setText(agendamento.getTelefone_responsavel());
        if(agendamento.getNome_responsavel().contains(" ")){
            agendamentoViewHolder.tvNomeResponsavel.setText(agendamento.getNome_responsavel().substring(0,agendamento.getNome_responsavel().indexOf(" ")));
        }else{
            agendamentoViewHolder.tvNomeResponsavel.setText(agendamento.getNome_responsavel());
        }
        agendamentoViewHolder.tvUID.setText(agendamento.getUid());
        agendamentoViewHolder.tvStatus.setText(agendamento.getStatus());
        agendamentoViewHolder.tvValor.setText("R$ "+agendamento.getValor());
        agendamentoViewHolder.checkBox_Concluido.setVisibility(View.VISIBLE);
        agendamentoViewHolder.llButoes.setVisibility(View.GONE);

        agendamentoViewHolder.checkBox_Concluido.setChecked(true);
        agendamentoViewHolder.checkBox_Pago.setChecked(true);
        agendamentoViewHolder.checkBox_Concluido.setEnabled(false);
        agendamentoViewHolder.checkBox_Pago.setEnabled(false);
        agendamentoViewHolder.btAtualizarStatus.setEnabled(false);

        agendamentoViewHolder.switch_endereco.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    agendamentoViewHolder.ll_endereco.setVisibility(View.VISIBLE);
                }else agendamentoViewHolder.ll_endereco.setVisibility(View.GONE);
            }
        });

        agendamentoViewHolder.switch_infor.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked == true){
                    agendamentoViewHolder.ll_infor.setVisibility(View.VISIBLE);
                }else agendamentoViewHolder.ll_infor.setVisibility(View.GONE);
            }
        });

        if(!agendamento.getLat().equals(0.0) && !agendamento.getLon().equals(0.0)){
            agendamentoViewHolder.tvLocalizacao.setVisibility(View.VISIBLE);
        }

        agendamentoViewHolder.tvLocalizacao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(agendamentoViewHolder.context, MapsActivity.class);
                it.putExtra("lat",agendamento.getLat());
                it.putExtra("lon",agendamento.getLon());
                agendamentoViewHolder.context.startActivity(it);
            }
        });

    }
}
