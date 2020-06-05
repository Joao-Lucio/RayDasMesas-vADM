package com.example.raydasmesas_28_01_2020.adapter;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.Activity_Add_Alter_Promocao;
import com.example.raydasmesas_28_01_2020.R;
import com.example.raydasmesas_28_01_2020.domain.Promocao;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;

public class PromocaoRecyclerAdapter extends FirebaseRecyclerAdapter<Promocao,PromocaoViewHolder> {

    private SimpleDateFormat formato;
    private DatabaseReference db;

    public PromocaoRecyclerAdapter(Class<Promocao> modelClass, int modelLayout, Class<PromocaoViewHolder> viewHolderClass, Query ref){
        super(modelClass, modelLayout, viewHolderClass, ref);
        formato = new SimpleDateFormat("dd/MM/yyyy");
    }

    @Override
    protected void populateViewHolder(final PromocaoViewHolder promocaoViewHolder, final Promocao promocao, int i) {

        promocaoViewHolder.tvDataInicio.setText(formato.format(promocao.getData_inic()));
        promocaoViewHolder.tvDataFim.setText(formato.format(promocao.getData_fim()));
        promocaoViewHolder.tvNome.setText(promocao.getNome());
        promocaoViewHolder.tvDescricao.setText(promocao.getDescricao());

        promocaoViewHolder.ibSeta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(promocaoViewHolder.rlButoes.getVisibility() == View.GONE){
                    promocaoViewHolder.rlButoes.setVisibility(View.VISIBLE);
                    promocaoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_cima);
                    promocaoViewHolder.view.setVisibility(View.VISIBLE);
                }else{
                    promocaoViewHolder.rlButoes.setVisibility(View.GONE);
                    promocaoViewHolder.view.setVisibility(View.GONE);
                    promocaoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_baixo);
                }
            }
        });

        promocaoViewHolder.btAlterar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(promocaoViewHolder.context, Activity_Add_Alter_Promocao.class);
                it.putExtra("promocao", promocao);
                promocaoViewHolder.context.startActivity(it);
                promocaoViewHolder.rlButoes.setVisibility(View.GONE);
                promocaoViewHolder.view.setVisibility(View.GONE);
                promocaoViewHolder.ibSeta.setImageResource(R.drawable.ic_seta_baixo);
            }
        });

        promocaoViewHolder.btExcluir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db = FirebaseDatabase.getInstance().getReference();
                AlertDialog.Builder alerta = new AlertDialog.Builder(promocaoViewHolder.context);
                alerta.setTitle("Aviso")
                        .setIcon(R.drawable.ic_error)
                        .setMessage("Ao excluir a promoção, não será possivel recuperar seus dados. Tem certeza que quer continuar?")
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
                                    db.child("promocao").child(promocao.getUid()).removeValue();
                                    Toast.makeText(promocaoViewHolder.context, "Promoção excluida com sucesso!", Toast.LENGTH_SHORT).show();
                                }catch (Exception e){
                                    Toast.makeText(promocaoViewHolder.context, "Ocorreu um erro ao excluir promoção!", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                AlertDialog alertDialog = alerta.create();
                alerta.show();
            }
        });

    }
}
