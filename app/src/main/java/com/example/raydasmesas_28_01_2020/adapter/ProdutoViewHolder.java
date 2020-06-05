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

public class ProdutoViewHolder extends RecyclerView.ViewHolder {

    public TextView tvDestaque;
    public TextView tvTipo;
    public TextView tvQuantidade;
    public TextView tvCaracteristica;
    public ImageButton ibSeta;
    public RelativeLayout rlButoes;
    public Button btAlterar;
    public Button btExcluir;
    public View view;
    public Context context;

    public ProdutoViewHolder(@NotNull View itemView) {
        super(itemView);

        tvDestaque = (TextView) itemView.findViewById(R.id.item_produto_tvDestaque);
        tvTipo = (TextView) itemView.findViewById(R.id.item_produto_tvTipo);
        tvQuantidade = (TextView) itemView.findViewById(R.id.item_produto_tvQuantidade);
        tvCaracteristica = (TextView) itemView.findViewById(R.id.item_produto_tvCaracteristica);
        view = (View) itemView.findViewById(R.id.view);

        ibSeta = (ImageButton) itemView.findViewById(R.id.item_produto_ibSeta);
        rlButoes = (RelativeLayout) itemView.findViewById(R.id.item_produto_rlButoes);
        btAlterar = (Button) itemView.findViewById(R.id.item_produto_btAlterar);
        btExcluir = (Button) itemView.findViewById(R.id.item_produto_btExcluir);
        context = itemView.getContext();
    }
}
