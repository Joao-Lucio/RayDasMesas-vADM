package com.example.raydasmesas_28_01_2020.adapter;

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

public class EntregueRecyclerViewAdapter extends FirebaseRecyclerAdapter<Agendamento,AgendamentoViewHolder> {

    private SimpleDateFormat formato;
    private DatabaseReference mDatabase;
    private String pago = "Pago";
    private String status = "Entregue";

    public EntregueRecyclerViewAdapter(Class<Agendamento> modelClass, int modelLayout, Class<AgendamentoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);
        formato = new SimpleDateFormat("dd/MM/yyyy");
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    protected void populateViewHolder(final AgendamentoViewHolder agendamentoViewHolder, final Agendamento agendamento, int i) {

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
        agendamentoViewHolder.btExcluir.setVisibility(View.GONE);

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

        agendamentoViewHolder.imageButton_Seta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agendamentoViewHolder.rlButoes.getVisibility() == View.GONE){
                    agendamentoViewHolder.rlButoes.setVisibility(View.VISIBLE);
                    agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_cima);
                }else {
                    agendamentoViewHolder.rlButoes.setVisibility(View.GONE);
                    agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_baixo);
                }
            }
        });

        if(agendamentoViewHolder.tvStatus.getText().toString().contains("Pago")){
            agendamentoViewHolder.checkBox_Pago.setChecked(true);
            agendamentoViewHolder.checkBox_Pago.setEnabled(false);
        }
        if(agendamentoViewHolder.tvStatus.getText().toString().contains("C")){
            agendamentoViewHolder.checkBox_Concluido.setChecked(true);
            agendamentoViewHolder.checkBox_Concluido.setEnabled(false);
        }

        agendamentoViewHolder.btAtualizarStatus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(agendamentoViewHolder.checkBox_Pago.isChecked()){
                    if(!agendamento.getStatus().contains("Pago")){
                        agendamento.setStatus(status+"/"+pago);
                        agendamentoViewHolder.tvStatus.setText(agendamento.getStatus());
                        try{
                            mDatabase.child("entregue").child(agendamento.getUid()).setValue(agendamento);
                            agendamentoViewHolder.ll_infor.setVisibility(View.GONE);
                        }catch (Exception e){}
                    }
                }

                if(agendamentoViewHolder.checkBox_Concluido.isChecked()){
                    if(agendamentoViewHolder.checkBox_Pago.isChecked()){
                        agendamento.setStatus("Concluído"+"/"+"Pago");
                        try{
                            mDatabase.child("historico").child(agendamento.getUid()).setValue(agendamento);
                            mDatabase.child("entregue").child(agendamento.getUid()).removeValue();
                            Toast.makeText(agendamentoViewHolder.context, "Agendamento trasferido para à aba HISTÓRICO!", Toast.LENGTH_LONG).show();
                            agendamentoViewHolder.ll_infor.setVisibility(View.GONE);
                        }catch (Exception e){
                            Toast.makeText(agendamentoViewHolder.context, "Ocorreu um erro ao trasferir agendamento para à aba HISTÓRICO!", Toast.LENGTH_LONG).show();
                        }
                    }else {
                        if(!agendamentoViewHolder.tvStatus.getText().toString().contains("Sem")){
                            agendamento.setStatus("Concluído/Sem Pagamento");
                            try{
                                mDatabase.child("entregue").child(agendamento.getUid()).setValue(agendamento);
                                agendamentoViewHolder.ll_infor.setVisibility(View.GONE);

                            }catch (Exception e){}
                        }
                        Toast.makeText(agendamentoViewHolder.context, "Só falta receber o pagamento para finalizar aluguel!", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        agendamentoViewHolder.btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(agendamentoViewHolder.context,Activity_Add_Alter_Agendamento.class);
                intent.putExtra("agendamento",agendamento);
                intent.putExtra("tipo",agendamento.getTipoAluguel());
                intent.putExtra("editado","sim");
                intent.putExtra("tela","entregue");
                agendamentoViewHolder.context.startActivity(intent);
                agendamentoViewHolder.rlButoes.setVisibility(View.GONE);
                agendamentoViewHolder.imageButton_Seta.setImageResource(R.drawable.ic_seta_baixo);
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
