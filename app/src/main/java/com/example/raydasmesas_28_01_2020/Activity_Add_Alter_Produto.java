package com.example.raydasmesas_28_01_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.domain.Produto;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.util.UUID;

public class Activity_Add_Alter_Produto extends AppCompatActivity {

    private Toolbar toolbar_main;
    private Spinner sp_tipo;
    private TextInputEditText ed_quantidade, ed_caracteristica;

    private DatabaseReference mDatabase;
    private String itemTipo;
    private Produto produto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__alter__produto);

        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar_main.setTitle("Adicionar Produto");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!isConnected(this)){
            Toast.makeText(this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        sp_tipo = (Spinner) findViewById(R.id.sp_tipo);
        ArrayAdapter<CharSequence> arraySpinner = ArrayAdapter.createFromResource(this, R.array.produto_tipo, android.R.layout.simple_list_item_activated_1);
        sp_tipo.setAdapter(arraySpinner);
        sp_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemTipo = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ed_quantidade = (TextInputEditText) findViewById(R.id.ed_quantidade);
        ed_caracteristica = (TextInputEditText) findViewById(R.id.ed_caracteristica);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent it = getIntent();
        Produto p = (Produto) it.getSerializableExtra("produto");
        if (p != null) {
            toolbar_main.setTitle("Alterar Produto");
            preenceCampos(p);

        }

    }

    public void preenceCampos(Produto p) {
        this.produto = p;
        Button btAdicionar = (Button) findViewById(R.id.bt_Adicionar);
        Button btSalvar = (Button) findViewById(R.id.bt_Salvar);
        ed_caracteristica.setText(p.getCaracteristica());
        ed_quantidade.setText(String.valueOf(p.getQuantidade()));
        if (p.getTipo().contains("M")) {
            sp_tipo.setSelection(0);
        } else {
            sp_tipo.setSelection(1);
        }
        btAdicionar.setVisibility(View.GONE);
        btSalvar.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    public void fechaActivity(View v) {
        this.finish();
    }

    public void addProduto(View v) {

        if (ed_quantidade.getText().toString().equals("") || ed_caracteristica.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
            Produto p = new Produto();
            p.setUid(UUID.randomUUID().toString());
            p.setCaracteristica(ed_caracteristica.getText().toString());
            p.setQuantidade(Integer.parseInt(ed_quantidade.getText().toString()));
            p.setTipo(itemTipo);

            if(isConnected(Activity_Add_Alter_Produto.this)){
                try {
                    mDatabase.child("produto").child(p.getUid()).setValue(p); // a primeira é o nome da tabela, segundo o id, terceiro o objeto
                    Toast.makeText(Activity_Add_Alter_Produto.this, "Produto Adicionado com Sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(Activity_Add_Alter_Produto.this, "Ocorreu um erro ao adicionar novo produto!", Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(Activity_Add_Alter_Produto.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void alterProduto(View v) {
        produto.setCaracteristica(ed_caracteristica.getText().toString());
        produto.setQuantidade(Integer.parseInt(ed_quantidade.getText().toString()));
        produto.setTipo(itemTipo);

        if(isConnected(Activity_Add_Alter_Produto.this)){
            try {
                mDatabase.child("produto").child(produto.getUid()).setValue(produto);
                Toast.makeText(this, "Produto Alterado com Sucesso!", Toast.LENGTH_SHORT).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(this, "Ocorreu um erro ao alterar produto!", Toast.LENGTH_SHORT).show();
            }
        }else{
            Toast.makeText(Activity_Add_Alter_Produto.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }
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
