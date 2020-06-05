package com.example.raydasmesas_28_01_2020.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.raydasmesas_28_01_2020.R;
import com.google.firebase.database.annotations.NotNull;

public class AgendamentoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvQtdMesas;
    public TextView tvQtdCadeiras;
    public TextView tvData;
    public TextView tvDesconto;
    public TextView tvHorario;
    public TextView tvStatus;
    public TextView tvValor;
    public TextView tvRua;
    public TextView tvNumero;
    public TextView tvBairro;
    public TextView tvReferencia;
    public TextView tvContatoResponsavel;
    public TextView tvNomeResponsavel;
    public TextView tvLocalizacao;
    public TextView tvUID;
    public Switch switch_endereco, switch_infor;
    public LinearLayout ll_endereco, ll_infor,llButoes;
    public CheckBox checkBox_Entregue, checkBox_Pago,checkBox_Concluido;
    public Button btAlterar,btExcluir,btAtualizarStatus;
    public ImageButton imageButton_Seta;
    public RelativeLayout rlButoes;
    public Context context;

    public AgendamentoViewHolder(@NotNull View itemView) {
        super(itemView);

        context = itemView.getContext();
        tvQtdMesas = (TextView) itemView.findViewById(R.id.item_agendamento_QtdMesas);
        tvQtdCadeiras = (TextView) itemView.findViewById(R.id.item_agendamento_QtdCadeiras);
        tvData = (TextView) itemView.findViewById(R.id.item_agendamento_Data);
        tvHorario = (TextView) itemView.findViewById(R.id.item_agendamento_Horario);
        tvDesconto = (TextView) itemView.findViewById(R.id.item_agendamento_Desconto);
        tvStatus = (TextView) itemView.findViewById(R.id.item_agendamento_Status);
        tvValor = (TextView) itemView.findViewById(R.id.item_agendamento_Valor);
        tvRua = (TextView) itemView.findViewById(R.id.item_agendamento_Rua);
        tvNumero = (TextView) itemView.findViewById(R.id.item_agendamento_Numero);
        tvBairro = (TextView) itemView.findViewById(R.id.item_agendamento_Bairro);
        tvReferencia = (TextView) itemView.findViewById(R.id.item_agendamento_Referencia);
        tvContatoResponsavel = (TextView) itemView.findViewById(R.id.item_agendamento_ContatoResponsavel);
        tvNomeResponsavel = (TextView) itemView.findViewById(R.id.item_agendamento_NomeResponsavel);
        tvUID = (TextView) itemView.findViewById(R.id.item_agendamento_UID);
        switch_endereco = (Switch) itemView.findViewById(R.id.switch_endereco);
        switch_infor = (Switch) itemView.findViewById(R.id.switch_infor);
        ll_endereco = (LinearLayout) itemView.findViewById(R.id.ll_endereco);
        ll_infor = (LinearLayout) itemView.findViewById(R.id.ll_infor);
        checkBox_Entregue = (CheckBox) itemView.findViewById(R.id.item_agendamento_cb_Entregue);
        checkBox_Pago = (CheckBox) itemView.findViewById(R.id.item_agendamento_cb_Pago);
        checkBox_Concluido = (CheckBox) itemView.findViewById(R.id.item_agendamento_cb_Concluido);
        btAlterar = (Button) itemView.findViewById(R.id.item_agendamento_btAlterar);
        btExcluir = (Button) itemView.findViewById(R.id.item_agendamento_btExcluir);
        imageButton_Seta = (ImageButton) itemView.findViewById(R.id.item_agendamento_ibSeta);
        rlButoes = (RelativeLayout) itemView.findViewById(R.id.item_agendamento_rlButoes);
        llButoes = (LinearLayout) itemView.findViewById(R.id.item_agendamento_llButoes);
        btAtualizarStatus = (Button) itemView.findViewById(R.id.item_agendamento_btAtuaizarStatus);
        tvLocalizacao = (TextView) itemView.findViewById(R.id.tvLocalizacao);
    }
}
