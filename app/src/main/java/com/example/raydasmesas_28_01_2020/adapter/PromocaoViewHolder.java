package com.example.raydasmesas_28_01_2020.adapter;

import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.raydasmesas_28_01_2020.R;
import com.google.firebase.database.annotations.NotNull;

public class PromocaoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvNome;
    public TextView tvDataInicio;
    public TextView tvDataFim;
    public TextView tvDescricao;
    public RelativeLayout rlButoes;
    public Button btAlterar,btExcluir;
    public ImageButton ibSeta;
    public Context context;
    public View view;

    public PromocaoViewHolder(@NotNull View itemView) {
        super(itemView);

        tvNome = (TextView) itemView.findViewById(R.id.item_promocao_nome);
        tvDataInicio = (TextView) itemView.findViewById(R.id.item_promocao_dataInicio);
        tvDataFim = (TextView) itemView.findViewById(R.id.item_promocao_dataFim);
        tvDescricao = (TextView) itemView.findViewById(R.id.item_promocao_descricao);
        rlButoes = (RelativeLayout) itemView.findViewById(R.id.item_promocao_rlButoes);
        btAlterar = (Button) itemView.findViewById(R.id.item_promocao_btAlterar);
        btExcluir = (Button) itemView.findViewById(R.id.item_promocao_btExcluir);
        ibSeta = (ImageButton) itemView.findViewById(R.id.item_promocao_ibSeta);
        view = (View) itemView.findViewById(R.id.view);
        context = itemView.getContext();
    }
}
