package com.example.raydasmesas_28_01_2020;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.domain.Promocao;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class Activity_Add_Alter_Promocao extends AppCompatActivity {

    private Toolbar toolbar_main;
    private TextView tv_definir_data_inicio, tv_definir_data_fim;
    private DatePickerDialog.OnDateSetListener mDateSetListenerDataInicio, mDateSetListenerDataFim;
    private Calendar cal;
    private int dia, mes, ano;
    private Spinner sp_tipo;
    private String auxmes, auxdia, item, data;
    private TextInputEditText textInput_ed_Nome, textInput_ed_Descricao, textInput_ed_Desconto, textInput_ed_quantidade;
    private Promocao promocao;
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private Date mData = new Date();
    private Date mAuxDataInicio = new Date();
    private Date mAuxDataFim = new Date();

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__alter__promocao);

        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar_main.setTitle("Adicionar Promoção");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(!isConnected(this)){
            Toast.makeText(this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        //        ================ Pegando o dia a hora atuais ===========================
        cal = Calendar.getInstance();
        ano = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);

        //        ========================== Definindo Data Inicio ===============================
        tv_definir_data_inicio = (TextView) findViewById(R.id.tv_dataInicio);
        int auxMes = mes + 1;
        auxmes = String.valueOf(auxMes);
        auxdia = String.valueOf(dia);
        if (auxMes <= 9) {
            auxmes = "0" + auxMes;
        }
        if (dia <= 9) {
            auxdia = "0" + dia;
        }
        tv_definir_data_inicio.setText(auxdia + "/" + auxmes + "/" + ano);
        try {
            mData = formato.parse(tv_definir_data_inicio.getText().toString());
        } catch (Exception e) {
        }

        tv_definir_data_inicio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        Activity_Add_Alter_Promocao.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerDataInicio,
                        ano, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerDataInicio = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                int auxMes = mes + 1;
                auxmes = String.valueOf(auxMes);
                auxdia = String.valueOf(dia);
                if (auxMes <= 9) {
                    auxmes = "0" + auxMes;
                }
                if (dia <= 9) {
                    auxdia = "0" + dia;
                }
                data = auxdia + "/" + auxmes + "/" + ano;
                tv_definir_data_inicio.setText(data);
            }
        };

//        ========================== Definindo Data Inicio Fim ===============================
        tv_definir_data_fim = (TextView) findViewById(R.id.tv_data_fim);
        tv_definir_data_fim.setText(auxdia + "/" + auxmes + "/" + ano);
        tv_definir_data_fim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        Activity_Add_Alter_Promocao.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListenerDataFim,
                        ano, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListenerDataFim = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                int auxMes = mes + 1;
                auxmes = String.valueOf(auxMes);
                auxdia = String.valueOf(dia);
                if (mes <= 9) {
                    auxmes = "0" + auxMes;
                }
                if (dia <= 9) {
                    auxdia = "0" + dia;
                }
                data = auxdia + "/" + auxmes + "/" + ano;
                tv_definir_data_fim.setText(data);
            }
        };

        sp_tipo = (Spinner) findViewById(R.id.sp_tipo);
        ArrayAdapter<CharSequence> arraySpinner = ArrayAdapter.createFromResource(this, R.array.promocao_tipo, android.R.layout.simple_list_item_activated_1);
        sp_tipo.setAdapter(arraySpinner);
        sp_tipo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item = adapterView.getItemAtPosition(i).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        textInput_ed_Nome = (TextInputEditText) findViewById(R.id.textInput_ed_Nome);
        textInput_ed_Descricao = (TextInputEditText) findViewById(R.id.textInput_ed_Descricao);
        textInput_ed_Desconto = (TextInputEditText) findViewById(R.id.textInput_ed_Desconto);
        textInput_ed_quantidade = (TextInputEditText) findViewById(R.id.textInput_ed_quantidade);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        Intent it = getIntent();
        Promocao p = (Promocao) it.getSerializableExtra("promocao");
        if (p != null) {
            toolbar_main.setTitle("Alterar Promoção");
            preenceCampos(p);

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

    public void addPromocao(View v) {
        if (textInput_ed_Nome.getText().toString().equals("") || textInput_ed_Descricao.getText().toString().equals("") || textInput_ed_Desconto.getText().toString().equals("") || textInput_ed_quantidade.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
            Date dt_inc = new Date();
            Date dt_fim = new Date();

            try {
                dt_inc = formato.parse(tv_definir_data_inicio.getText().toString());
                dt_fim = formato.parse(tv_definir_data_fim.getText().toString());
            } catch (Exception e) {
            }

            if (dt_inc.before(mData) || dt_fim.before(mData) || dt_fim.before(dt_inc)) {
                Toast.makeText(this, "Escolha Datas válidas!", Toast.LENGTH_SHORT).show();
            } else {
                Promocao p = new Promocao();
                p.setUid(UUID.randomUUID().toString());
                p.setData_inic(dt_inc);
                p.setData_fim(dt_fim);
                p.setNome(textInput_ed_Nome.getText().toString());
                p.setDescricao(textInput_ed_Descricao.getText().toString());
                p.setDesconto(Integer.parseInt(textInput_ed_Desconto.getText().toString()));
                p.setQuantidade(Integer.parseInt(textInput_ed_quantidade.getText().toString()));
                p.setTipo(item);

                if(isConnected(Activity_Add_Alter_Promocao.this)){
                    try {
                        mDatabase.child("promocao").child(p.getUid()).setValue(p); // a primeira é o nome da tabela, segundo o id, terceiro o objeto
                        Toast.makeText(Activity_Add_Alter_Promocao.this, "Promoção Adicionada com Sucesso!", Toast.LENGTH_LONG).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(Activity_Add_Alter_Promocao.this, "Ocorreu um erro ao adicionar nova promoção: " + e, Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(Activity_Add_Alter_Promocao.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    public void cancelar(View v) {
        this.finish();
    }

    public void preenceCampos(Promocao p) {
        this.promocao = p;
        Button btAdicionar = (Button) findViewById(R.id.bt_Adicionar);
        Button btSalvar = (Button) findViewById(R.id.bt_Salvar);
        tv_definir_data_inicio.setText(formato.format(p.getData_inic()));
        tv_definir_data_fim.setText(formato.format(p.getData_fim()));
        textInput_ed_Nome.setText(p.getNome());
        textInput_ed_Descricao.setText(p.getDescricao());
        textInput_ed_Desconto.setText(String.valueOf(p.getDesconto()));
        textInput_ed_quantidade.setText(String.valueOf(p.getQuantidade()));
        if (p.getTipo().contains("J")) {
            sp_tipo.setSelection(0);
        } else if (p.getTipo().contains("M")) {
            sp_tipo.setSelection(1);
        } else sp_tipo.setSelection(2);
        btAdicionar.setVisibility(View.GONE);
        btSalvar.setVisibility(View.VISIBLE);
    }

    public void alterPromocao(View v) {
        if (textInput_ed_Nome.getText().toString().equals("") || textInput_ed_Descricao.getText().toString().equals("") || textInput_ed_Desconto.getText().toString().equals("") || textInput_ed_quantidade.getText().toString().equals("")) {
            Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
        } else {
            Date dt_inc = new Date();
            Date dt_fim = new Date();

            try {
                dt_inc = formato.parse(tv_definir_data_inicio.getText().toString());
                dt_fim = formato.parse(tv_definir_data_fim.getText().toString());
            } catch (Exception e) {
            }

            if (dt_fim.before(mData) || dt_fim.before(dt_inc)) {
                Toast.makeText(this, "Escolha Datas válidas!", Toast.LENGTH_SHORT).show();
            } else {
                promocao.setData_inic(dt_inc);
                promocao.setData_fim(dt_fim);
                promocao.setNome(textInput_ed_Nome.getText().toString());
                promocao.setDescricao(textInput_ed_Descricao.getText().toString());
                promocao.setDesconto(Integer.parseInt(textInput_ed_Desconto.getText().toString()));
                promocao.setQuantidade(Integer.parseInt(textInput_ed_quantidade.getText().toString()));
                promocao.setTipo(item);

                if(isConnected(Activity_Add_Alter_Promocao.this)){
                    try {
                        mDatabase.child("promocao").child(promocao.getUid()).setValue(promocao);
                        Toast.makeText(this, "Promoção alterada com sucesso!", Toast.LENGTH_SHORT).show();
                        finish();
                    } catch (Exception e) {
                        Toast.makeText(this, "Ocorreu um erro ao alterar promoção!", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(Activity_Add_Alter_Promocao.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
                }
            }
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
