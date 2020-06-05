package com.example.raydasmesas_28_01_2020.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.Activity_Add_Alter_Produto;
import com.example.raydasmesas_28_01_2020.R;
import com.example.raydasmesas_28_01_2020.domain.Produto;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

public class ProdutoRecyclerViewAdapter extends FirebaseRecyclerAdapter<Produto,ProdutoViewHolder> {

    private DatabaseReference db;

    public ProdutoRecyclerViewAdapter(Class<Produto> modelClass, int modelLayout, Class<ProdutoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);
    }

    @Override
    protected void populateViewHolder(final ProdutoViewHolder produtoViewHolder, final Produto produto, int i) {

        if(produto.getTipo().contains("M")){
            produtoViewHolder.tvDestaque.setText("M");
        }else produtoViewHolder.tvDestaque.setText("C");
        produtoViewHolder.tvTipo.setText(produto.getTipo());
        produtoViewHolder.tvQuantidade.setText(String.valueOf(produto.getQuantidade()));
        produtoViewHolder.tvCaracteristica.setText(produto.getCaracteristica());

        produtoViewHolder.ibSeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(produtoViewHolder.rlButoes.getVisibility() == View.GONE){
                    produtoViewHolder.rlButoes.setVisibility(View.VISIBLE);
                    produtoViewHolder.view.setVisibility(View.VISIBLE);
                    produtoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_cima);
                }else{
                    produtoViewHolder.rlButoes.setVisibility(View.GONE);
                    produtoViewHolder.view.setVisibility(View.GONE);
                    produtoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_baixo);
                }
            }
        });

        produtoViewHolder.btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(produtoViewHolder.context, Activity_Add_Alter_Produto.class);
                it.putExtra("produto", produto);
                produtoViewHolder.context.startActivity(it);
                produtoViewHolder.rlButoes.setVisibility(View.GONE);
                produtoViewHolder.view.setVisibility(View.GONE);
                produtoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_baixo);
            }
        });

        produtoViewHolder.btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder alerta = new AlertDialog.Builder(produtoViewHolder.context);
                alerta.setTitle("Aviso")
                        .setIcon(R.drawable.ic_error)
                        .setMessage("Ao excluir produto, não será possivel recuperar seus dados. Tem certeza que quer continuar?")
                        .setCancelable(false)
                        .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try{
                                    db.child("produto").child(produto.getUid()).removeValue();
                                    Toast.makeText(produtoViewHolder.context, "Produto excluido com sucesso!", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(produtoViewHolder.context, "Ocorreu um erro ao excluir produto!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alerta.show();
            }
        });
    }
}
