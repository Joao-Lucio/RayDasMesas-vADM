package com.example.raydasmesas_28_01_2020;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.example.raydasmesas_28_01_2020.domain.Produto;
import com.example.raydasmesas_28_01_2020.domain.Promocao;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class Activity_Add_Alter_Agendamento extends AppCompatActivity implements LocationListener {

    private Toolbar toolbar_main;
    private TextView tvData, tvHora, tv_qtdMesas, tv_qtdCadeiras, tv_desconto, tv_fecha, tv_infor_disponivel, tv_DataAluguel,tvRuaInfor;
    private Button btAgendar, btAlterar;
    private TextInputEditText textInput_edQuantidade, textInput_edPecaMaisQuantidade, textInput_edBairro, textInput_edRua, textInput_edNumero, textInput_edReferencia, textInput_edTotal, textInput_edContato, textInput_edNome,textInput_edBairroAtual,textInput_edRuaAtual,textInput_edNumeroAtual,textInput_edReferenciaAtual;
    private RelativeLayout rl_etapa1, rl_etapa2, rl_etapa3;
    private LinearLayout ll_pecas_mais, ll_qtd_tipo, ll_infor,ll_enderecoPersonalizado,ll_enderecoAtual;
    private RadioGroup rg_pecas_mais;
    private RadioButton rb_pecasMais_sim;
    private Spinner sp_tipo,sp_endereco;
    private String itemTipo, tipoAluguel,mEditado,mTela;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog timePickerDialog;
    private Calendar cal;
    private int dia, mes, ano, hora, min;
    private int desconto = 0, quantidadeMesasTotal = 0, quantidadeCadeirasTotal = 0, quantidadeMesasDisponivel = 0, quantidadeCadeirasDisponivel = 0, jogoCompleto = 0, cadeiraRestante = 0, mesaRestante = 0;
    private int qtdMesas = 0, qtdCadeiras = 0, maisMesa = 0, maisCadeira = 0;
    private int alteraJogos,alteraPecasMesas,alteraPecasCadeiras;
    private float valor = 0;
    private Date mData = new Date();
    private Date mAuxData = new Date();
    private Intent it;
    private DatabaseReference mDatabase;
    private SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
    private Agendamento agendamento;
    private String tipoEndereco,bairro,rua,numero,referencia;
    private Double lat = 0.0, lon = 0.0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity__add__alter__agendamento);

        toolbar_main = (Toolbar) findViewById(R.id.toolbar_main);
        toolbar_main.setTitle("Adicionar Aluguel");
        toolbar_main.setTitleTextColor(Color.parseColor("#ffffff"));
        setSupportActionBar(toolbar_main);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        inicComponentes();

        if(!isConnected(this)){
            Toast.makeText(this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        getDataHoraAtual();
        setData();
        setHora();
        setSpinnerTipo();
        setSpinnerEndereco();

        it = getIntent();
        tipoAluguel = it.getStringExtra("tipo");
        mEditado = it.getStringExtra("editado");
        mTela = it.getStringExtra("tela");

        Agendamento a = (Agendamento) it.getSerializableExtra("agendamento");
        if (a != null) {
            toolbar_main.setTitle("Alterar Agendamento");
            preenceCampos(a);
        }

        if (tipoAluguel.contains("j")) {
            exibiLayout();
            getDisponivel(tvData.getText().toString());
        } else {
            getDisponivel(tvData.getText().toString());
        }

    }

    public void getDisponivel(final String data) {
        quantidadeMesasTotal = 0;
        quantidadeCadeirasTotal = 0;
        quantidadeMesasDisponivel = 0;
        quantidadeCadeirasDisponivel = 0;
        jogoCompleto = 0;
        cadeiraRestante = 0;
        mesaRestante = 0;
        mDatabase.child("agendamento").orderByChild("dataString").equalTo(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    Agendamento a = obj.getValue(Agendamento.class);
                    quantidadeMesasDisponivel = quantidadeMesasDisponivel + a.getQtdMesas();
                    quantidadeCadeirasDisponivel = quantidadeCadeirasDisponivel + a.getQtdCadeiras();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });

        mDatabase.child("entregue").orderByChild("dataString").equalTo(data).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Agendamento a = obj.getValue(Agendamento.class);
                    quantidadeMesasDisponivel = quantidadeMesasDisponivel + a.getQtdMesas();
                    quantidadeCadeirasDisponivel = quantidadeCadeirasDisponivel + a.getQtdCadeiras();
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });

        mDatabase.child("produto").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    Produto p = obj.getValue(Produto.class);
                    if (p.getTipo().contains("M")) {
                        quantidadeMesasTotal = quantidadeMesasTotal + p.getQuantidade();
                    } else if (p.getTipo().contains("C")) {
                        quantidadeCadeirasTotal = quantidadeCadeirasTotal + p.getQuantidade();
                    }
                }

                if (tipoAluguel.contains("j")) {
                    quantidadeMesasDisponivel = quantidadeMesasTotal - quantidadeMesasDisponivel;
                    quantidadeCadeirasDisponivel = quantidadeCadeirasTotal - quantidadeCadeirasDisponivel;
                    if (quantidadeMesasDisponivel > 0 && quantidadeCadeirasDisponivel > 3) {
                        jogoCompleto = quantidadeCadeirasDisponivel / 4;
                        if(jogoCompleto > quantidadeMesasDisponivel){
                            jogoCompleto = quantidadeMesasDisponivel;
                            cadeiraRestante = quantidadeCadeirasDisponivel - (jogoCompleto*4);
                        }else {
                            cadeiraRestante = quantidadeCadeirasDisponivel % 4;
                        }
                        mesaRestante = quantidadeMesasDisponivel - jogoCompleto;
                        if (cadeiraRestante == 0 && mesaRestante == 0) {
                            tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + jogoCompleto + " Jogos Completos!");
                        }else if(cadeiraRestante != 0 && mesaRestante != 0){
                            tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + jogoCompleto + " Jogos Completos, mais " + mesaRestante + " Mesas e " + cadeiraRestante + " Cadeiras!");
                        } else if (cadeiraRestante == 0 && mesaRestante != 0) {
                            tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + jogoCompleto + " Jogos Completos e mais " + mesaRestante + " Mesas!");
                        } else {
                            tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + jogoCompleto + " Jogos Completos e mais " + cadeiraRestante + " Cadeiras!");
                        }
                    } else {
                        if (quantidadeMesasDisponivel == 0 && quantidadeCadeirasDisponivel == 0) {
                            tv_infor_disponivel.setText("Não temos mais peças disponives para o dia " + data);
                        } else if(quantidadeMesasDisponivel > 0){
                            tv_infor_disponivel.setText("Não temos mais jogos disponiveis para a data " + data + ", temos somente " + quantidadeMesasDisponivel + " mesas disponives, caso queira somente as mesas volte e selecione o aluguel somente mesas!");
                        }else {
                            tv_infor_disponivel.setText("Não temos mais jogos disponiveis para a data " + data + ", temos somente " + quantidadeCadeirasDisponivel + " cadeiras disponives, caso queira somente as cadeiras volte e selecione o aluguel somente cadeiras!");
                        }
                    }
                } else if (tipoAluguel.contains("m")) {
                    quantidadeMesasDisponivel = quantidadeMesasTotal - quantidadeMesasDisponivel;
                    if (quantidadeMesasDisponivel > 0) {
                        tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + quantidadeMesasDisponivel + " Mesas!");
                    } else {
                        tv_infor_disponivel.setText("Não temos mais Mesas disponiveis para a data " + data + "!");
                    }
                } else {
                    quantidadeCadeirasDisponivel = quantidadeCadeirasTotal - quantidadeCadeirasDisponivel;
                    if (quantidadeCadeirasDisponivel > 0) {
                        tv_infor_disponivel.setText("Temos disponivel para o dia " + data + " apenas " + quantidadeCadeirasDisponivel + " Cadeiras!");
                    } else {
                        tv_infor_disponivel.setText("Não temos mais Cadeiras disponiveis para a data " + data + "!");
                    }
                }

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    public boolean getTemDisponivel(final int qtd) {
        qtdMesas = 0;
        qtdCadeiras = 0;
        maisMesa = 0;
        maisCadeira = 0;
        desconto = 0;

        Date data = new Date();
        try {
            data = formato.parse(tvData.getText().toString());
        } catch (Exception e) {
        }
        final Date auxData = data;

        if (tipoAluguel.contains("j")) {
            if (qtd > jogoCompleto) {
                Toast.makeText(this, "Não temos essa quantidade disponivel para essa data!", Toast.LENGTH_SHORT).show();
            } else if (!textInput_edPecaMaisQuantidade.getText().toString().equals("")) {
                if (itemTipo.contains("M")) {
                    maisMesa = Integer.parseInt(textInput_edPecaMaisQuantidade.getText().toString());
                    if (maisMesa <= mesaRestante || (maisMesa + qtd) <= (jogoCompleto + mesaRestante)) {
                        qtdMesas = qtd;
                        qtdCadeiras = qtd * 4;
                        verificaPromocao("Jogo Completo", auxData, qtd);
                        rl_etapa1.setVisibility(View.GONE);
                        rl_etapa2.setVisibility(View.VISIBLE);
                        return true;
                    } else if (maisMesa > mesaRestante) {
                        Toast.makeText(this, "Não temos essa quantidade disponivel para essa data!", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    maisCadeira = Integer.parseInt(textInput_edPecaMaisQuantidade.getText().toString());
                    if (maisCadeira <= cadeiraRestante || ((maisCadeira) + (qtd * 4)) <= ((jogoCompleto * 4) + cadeiraRestante)) {
                        qtdMesas = qtd;
                        qtdCadeiras = (qtd * 4) + maisCadeira;
                        verificaPromocao("Jogo Completo", auxData, qtd);
                        rl_etapa1.setVisibility(View.GONE);
                        rl_etapa2.setVisibility(View.VISIBLE);
                        return true;
                    } else if (maisCadeira > cadeiraRestante) {
                        Toast.makeText(this, "Não temos essa quantidade disponivel para essa data!", Toast.LENGTH_SHORT).show();
                    }
                }
            } else {
                qtdMesas = qtd;
                qtdCadeiras = qtd * 4;
                verificaPromocao("Jogo Completo", auxData, qtd);
                rl_etapa1.setVisibility(View.GONE);
                rl_etapa2.setVisibility(View.VISIBLE);
                return true;
            }
        } else if (tipoAluguel.contains("m")) {
            if (qtd > quantidadeMesasDisponivel) {
                Toast.makeText(this, "Não temos essa quantidade disponivel para essa data!", Toast.LENGTH_SHORT).show();
            } else {
                verificaPromocao("Mesa", auxData, qtd);
                qtdMesas = qtd;
                rl_etapa1.setVisibility(View.GONE);
                rl_etapa2.setVisibility(View.VISIBLE);
                return true;
            }
        } else {
            if (qtd > quantidadeCadeirasDisponivel) {
                Toast.makeText(this, "Não temos essa quantidade disponivel para essa data!", Toast.LENGTH_SHORT).show();
            } else {
                verificaPromocao("Cadeira", auxData, qtd);
                qtdCadeiras = qtd;
                rl_etapa1.setVisibility(View.GONE);
                rl_etapa2.setVisibility(View.VISIBLE);
                return true;
            }
        }
        return false;
    }

    public void verificaPromocao(String tipo, final Date auxData, final int qtd) {
        desconto = 0;
        mDatabase.child("promocao").orderByChild("tipo").equalTo(tipo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                for (DataSnapshot obj : dataSnapshot.getChildren()) {
                    Promocao p = obj.getValue(Promocao.class);
                    Date dataInicio = p.getData_inic();
                    Date dataFim = p.getData_fim();
                    if (auxData.equals(dataInicio) || auxData.after(dataInicio)) {
                        if (auxData.equals(dataFim) || auxData.before(dataFim)) {
                            if (qtd >= p.getQuantidade()) {
                                if (desconto < p.getDesconto()) {
                                    desconto = p.getDesconto();
                                }
                                tv_desconto.setText(desconto + "%");
                            }

                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
        }

        return true;
    }

    // Inicializar Componentes fazendo a ligação do componentes com o layout
    public void inicComponentes() {
        tvData = (TextView) findViewById(R.id.tv_data);
        tvHora = (TextView) findViewById(R.id.tv_horario);
        textInput_edQuantidade = (TextInputEditText) findViewById(R.id.textInput_edQuantidade);
        rl_etapa1 = (RelativeLayout) findViewById(R.id.rl_etapa1);
        rl_etapa2 = (RelativeLayout) findViewById(R.id.rl_etapa2);
        rl_etapa3 = (RelativeLayout) findViewById(R.id.rl_etapa3);
        ll_pecas_mais = (LinearLayout) findViewById(R.id.ll_pecas_mais);
        rg_pecas_mais = (RadioGroup) findViewById(R.id.rg_pecas_mais);
        ll_qtd_tipo = (LinearLayout) findViewById(R.id.ll_qtd_tipo);
        textInput_edPecaMaisQuantidade = (TextInputEditText) findViewById(R.id.textInput_edPecaMaisQuantidade);
        sp_tipo = (Spinner) findViewById(R.id.sp_tipo);
        textInput_edBairro = (TextInputEditText) findViewById(R.id.textInput_edBairro);
        textInput_edRua = (TextInputEditText) findViewById(R.id.textInput_edRua);
        textInput_edNumero = (TextInputEditText) findViewById(R.id.textInput_edNumero);
        textInput_edReferencia = (TextInputEditText) findViewById(R.id.textInput_edReferencia);
        tv_qtdCadeiras = (TextView) findViewById(R.id.tv_qtdCadeiras);
        tv_qtdMesas = (TextView) findViewById(R.id.tv_qtdMesas);
        tv_desconto = (TextView) findViewById(R.id.tv_desconto);
        textInput_edTotal = (TextInputEditText) findViewById(R.id.textInput_edTotal);
        textInput_edContato = (TextInputEditText) findViewById(R.id.textInput_edContato);
        tv_infor_disponivel = (TextView) findViewById(R.id.tv_infor_disponivel);
        tv_fecha = (TextView) findViewById(R.id.tv_fecha);
        ll_infor = (LinearLayout) findViewById(R.id.ll_infor);
        textInput_edNome = (TextInputEditText) findViewById(R.id.textInput_edNome);
        tv_DataAluguel = (TextView) findViewById(R.id.tv_DataAluguel);
        btAgendar = (Button) findViewById(R.id.btAgendar);
        btAlterar = (Button) findViewById(R.id.btAlterar);
        rb_pecasMais_sim = (RadioButton) findViewById(R.id.rb_pecas_sim);
        sp_endereco = (Spinner) findViewById(R.id.sp_endereco);
        textInput_edBairroAtual = (TextInputEditText) findViewById(R.id.textInput_edBairroAtual);
        textInput_edRuaAtual = (TextInputEditText) findViewById(R.id.textInput_edRuaAtual);
        ll_enderecoPersonalizado = (LinearLayout) findViewById(R.id.ll_enderecoPersonalizado);
        ll_enderecoAtual = (LinearLayout) findViewById(R.id.ll_enderecoAtual);
        tvRuaInfor = (TextView) findViewById(R.id.tvRuaInfor);
        textInput_edReferenciaAtual = (TextInputEditText) findViewById(R.id.textInput_edReferenciaAtual);
        textInput_edNumeroAtual = (TextInputEditText) findViewById(R.id.textInput_edNumeroAtual);

        SimpleMaskFormatter smf = new SimpleMaskFormatter("(NN) NNNNN-NNNN");
        MaskTextWatcher mtw = new MaskTextWatcher(textInput_edContato,smf);
        textInput_edContato.addTextChangedListener(mtw);

        acoes();
    }

    public void acoes() {
        tv_fecha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ll_infor.setVisibility(View.GONE);
            }
        });
    }

    // Pegando a data e Hora Atual
    public void getDataHoraAtual() {
        //        ================ Pegando o dia a hora atuais ===========================
        cal = Calendar.getInstance();
        ano = cal.get(Calendar.YEAR);
        mes = cal.get(Calendar.MONTH);
        dia = cal.get(Calendar.DAY_OF_MONTH);
        hora = cal.get(Calendar.HOUR_OF_DAY);
        min = cal.get(Calendar.MINUTE);

    }

    // Definindo Data
    public void setData() {
        int auxMes = mes + 1;
        String mMes = String.valueOf(auxMes);
        String mDia = String.valueOf(dia);
        if (auxMes <= 9) {
            mMes = "0" + mMes;
        }
        if (dia <= 9) {
            mDia = "0" + mDia;
        }
        tvData.setText(mDia + "/" + mMes + "/" + ano);
        try {
            mData = formato.parse(tvData.getText().toString());
        } catch (Exception e) {
        }
        tvData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog dialog = new DatePickerDialog(
                        Activity_Add_Alter_Agendamento.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        ano, mes, dia);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int ano, int mes, int dia) {
                int auxMes = mes + 1;
                String mMes = String.valueOf(auxMes);
                String mDia = String.valueOf(dia);
                String data = "";
                if (auxMes <= 9) {
                    mMes = "0" + mMes;
                }
                if (dia <= 9) {
                    mDia = "0" + mDia;
                }
                data = mDia + "/" + mMes + "/" + ano;
                tvData.setText(data);
                getDisponivel(data);
            }
        };
    }

    // Definindo Horario
    public void setHora() {
        String mMin = String.valueOf(min);
        if (min <= 9) {
            mMin = "0" + min;
        }
        tvHora.setText(hora + ":" + mMin);

        tvHora.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                timePickerDialog = new TimePickerDialog(Activity_Add_Alter_Agendamento.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int hora_do_dia, int minutes) {

                        tvHora.setText(String.format("%02d:%02d", hora_do_dia, minutes));

                    }
                }, hora, min, true);
                timePickerDialog.show();
            }
        });
    }

    public void setSpinnerTipo() {
        ArrayAdapter<CharSequence> arraySpinner = ArrayAdapter.createFromResource(this, R.array.peca_Tipo, android.R.layout.simple_list_item_activated_1);
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
    }

    public void setSpinnerEndereco(){
        ArrayAdapter<CharSequence> arraySpinner = ArrayAdapter.createFromResource(this, R.array.tipoEndereco, android.R.layout.simple_list_item_activated_1);
        sp_endereco.setAdapter(arraySpinner);
        sp_endereco.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position == 0){
                    ll_enderecoPersonalizado.setVisibility(View.VISIBLE);
                    ll_enderecoAtual.setVisibility(View.GONE);
                }else if(position == 1){
                    ll_enderecoAtual.setVisibility(View.VISIBLE);
                    ll_enderecoPersonalizado.setVisibility(View.GONE);
                    localizacaoAtual();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    public void localizacaoAtual(){

        startGettingLocations();

    }

    public void botaoCancelarEtapa1(View v) {
        finish();
    }

    public void botaoContinuarEtapa1(View v) {
        try {
            mAuxData = formato.parse(tvData.getText().toString());
        } catch (Exception e) {
        }

        if (textInput_edQuantidade.getText().toString().equals("")) {
            Toast.makeText(this, "Informe a quantidade!", Toast.LENGTH_SHORT).show();
            return;
        } else if (mAuxData.before(mData)) {
            Toast.makeText(this, "Data escolhida já passou!", Toast.LENGTH_SHORT).show();
            return;
        } else {
            int qtd = Integer.parseInt(textInput_edQuantidade.getText().toString());
            if (qtd == 0) {
                Toast.makeText(this, "Informe a quantidade!", Toast.LENGTH_SHORT).show();
                return;
            } else {
                if(agendamento != null){
                    if(mAuxData.equals(agendamento.getData())){
                        if(tipoAluguel.contains("j")){
                            int pecasMais = 0;
                            if(!textInput_edPecaMaisQuantidade.getText().toString().equals("")){
                                pecasMais = Integer.parseInt(textInput_edPecaMaisQuantidade.getText().toString());
                            }
                            if(qtd <= (alteraJogos+jogoCompleto+alteraPecasMesas+mesaRestante) && qtd*4 <= ((jogoCompleto*4)+agendamento.getQtdCadeiras()+cadeiraRestante)){
                                if(pecasMais != 0 && !textInput_edPecaMaisQuantidade.getText().toString().equals("")){
                                    if(itemTipo.contains("M")){
                                        int aux = (alteraJogos+jogoCompleto)-qtd;
                                        if(pecasMais <= aux+mesaRestante+alteraPecasMesas){
                                            verificaPromocao("Jogo Completo",mAuxData,qtd);
                                            tv_qtdMesas.setText(String.valueOf(qtd+pecasMais));
                                            qtdMesas = qtd;
                                            maisMesa = pecasMais;
                                            qtdCadeiras = qtd * 4;
                                            tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                                            String ano = tvData.getText().toString().substring(8);
                                            String diaMes = tvData.getText().toString().substring(0,6);
                                            tv_DataAluguel.setText(diaMes+ano);
                                            valor = 0;
                                            getPreco("precoJogo", "j", valor);
                                            rl_etapa1.setVisibility(View.GONE);
                                            rl_etapa2.setVisibility(View.VISIBLE);
                                            return;
                                        }
                                    }else{
                                        int aux = ((alteraJogos+jogoCompleto)-qtd)*4;
                                        if(pecasMais <= aux+cadeiraRestante+alteraPecasCadeiras){
                                            verificaPromocao("Jogo Completo",mAuxData,qtd);
                                            tv_qtdMesas.setText(String.valueOf(qtd));
                                            qtdMesas = qtd;
                                            maisCadeira = pecasMais;
                                            qtdCadeiras = (qtd * 4)+pecasMais;
                                            tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                                            String ano = tvData.getText().toString().substring(8);
                                            String diaMes = tvData.getText().toString().substring(0,6);
                                            tv_DataAluguel.setText(diaMes+ano);
                                            valor = 0;
                                            getPreco("precoJogo", "j", valor);
                                            rl_etapa1.setVisibility(View.GONE);
                                            rl_etapa2.setVisibility(View.VISIBLE);
                                            return;
                                        }
                                    }
                                }else{
                                    verificaPromocao("Jogo Completo",mAuxData,qtd);
                                    tv_qtdMesas.setText(String.valueOf(qtd));
                                    qtdMesas = qtd;
                                    qtdCadeiras = qtd * 4;
                                    tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                                    String ano = tvData.getText().toString().substring(8);
                                    String diaMes = tvData.getText().toString().substring(0,6);
                                    tv_DataAluguel.setText(diaMes+ano);
                                    valor = 0;
                                    getPreco("precoJogo", "j", valor);
                                    rl_etapa1.setVisibility(View.GONE);
                                    rl_etapa2.setVisibility(View.VISIBLE);
                                    return;
                                }
                            }
                        }else if(tipoAluguel.contains("m")){
                            if(qtd <= agendamento.getQtdMesas()+quantidadeMesasDisponivel){
                                verificaPromocao("Mesa",mAuxData,qtd);
                                tv_qtdMesas.setText(String.valueOf(qtd));
                                qtdMesas = 0;
                                tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                                String ano = tvData.getText().toString().substring(8);
                                String diaMes = tvData.getText().toString().substring(0,6);
                                tv_DataAluguel.setText(diaMes+ano);
                                valor = 0;
                                getPreco("precoMesa", "m", valor);
                                rl_etapa1.setVisibility(View.GONE);
                                rl_etapa2.setVisibility(View.VISIBLE);
                                return;
                            }
                        }else{
                            if(qtd <= agendamento.getQtdCadeiras()+quantidadeCadeirasDisponivel){
                                verificaPromocao("Cadeira",mAuxData,qtd);
                                tv_qtdMesas.setText(String.valueOf(qtdMesas));
                                qtdCadeiras = qtd;
                                tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                                String ano = tvData.getText().toString().substring(8);
                                String diaMes = tvData.getText().toString().substring(0,6);
                                tv_DataAluguel.setText(diaMes+ano);
                                valor = 0;
                                getPreco("precoCadeira", "c", valor);
                                rl_etapa1.setVisibility(View.GONE);
                                rl_etapa2.setVisibility(View.VISIBLE);
                                return;
                            }
                        }
                    }

                    }
                }

            if(getTemDisponivel(qtd)){
                tv_qtdMesas.setText(String.valueOf(qtdMesas + maisMesa));
                tv_qtdCadeiras.setText(String.valueOf(qtdCadeiras));
                String ano = tvData.getText().toString().substring(8);
                String diaMes = tvData.getText().toString().substring(0,6);
                tv_DataAluguel.setText(diaMes+ano);
                valor = 0;
                if (tipoAluguel.contains("j")) {
                    getPreco("precoJogo", "j", valor);
                } else if (tipoAluguel.contains("m")) {
                    getPreco("precoMesa", "m", valor);
                    qtdMesas = 0;
                } else {
                    getPreco("precoCadeira", "c", valor);
                }
            }

            }
        }

    public void calculaTotal(final float val) {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                float total = (val * desconto) / 100;
                valor = val - total;
                textInput_edTotal.setText(String.valueOf(valor));
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }

    // Exibir layout de pecas a mais se o aluguel for de Jogo Completo
    public void exibiLayout() {
        ll_pecas_mais.setVisibility(View.VISIBLE);
    }

    public void rbClick_pecas_mais(View v) {
        int radiobuttonid = rg_pecas_mais.getCheckedRadioButtonId();
        switch (radiobuttonid) {
            case R.id.rb_pecas_sim:
                ll_qtd_tipo.setVisibility(View.VISIBLE);
                break;
            case R.id.rb_pecas_nao:
                textInput_edPecaMaisQuantidade.setText("");
                ll_qtd_tipo.setVisibility(View.GONE);
                break;
        }
    }

    public void botaoVoltarEtapa2(View v) {
        rl_etapa2.setVisibility(View.GONE);
        rl_etapa1.setVisibility(View.VISIBLE);
    }

    public void botaoContinuarEtapa2(View v) {
        if(sp_endereco.getSelectedItemPosition() == 0){
            if (textInput_edBairro.getText().toString().equals("") || textInput_edRua.getText().toString().equals("") || textInput_edNumero.getText().toString().equals("") || textInput_edReferencia.getText().toString().equals("") || textInput_edNome.getText().toString().equals("") || textInput_edContato.getText().toString().equals("")) {
                Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }
            tipoEndereco = "per";
        }else {
            if (textInput_edBairroAtual.getText().toString().equals("") || textInput_edRuaAtual.getText().toString().equals("") || textInput_edNumeroAtual.getText().toString().equals("") || textInput_edReferenciaAtual.getText().toString().equals("") || textInput_edNome.getText().toString().equals("") || textInput_edContato.getText().toString().equals("")) {
                Toast.makeText(this, "Por favor preencha todos os campos!", Toast.LENGTH_SHORT).show();
                return;
            }
            tipoEndereco = "atu";
        }
        if(textInput_edContato.getText().toString().length() < 15){
            Toast.makeText(this, "Por favor preencha o campo de número de contato corretamente!", Toast.LENGTH_LONG).show();
            return;
        }
        rl_etapa2.setVisibility(View.GONE);
        rl_etapa3.setVisibility(View.VISIBLE);
    }

    public void botaoVoltarEtapa3(View v) {
        rl_etapa3.setVisibility(View.GONE);
        rl_etapa2.setVisibility(View.VISIBLE);
    }

    public void getPreco(String tipo, final String aux, final float val) {
        valor = val;
        mDatabase.child("variados").child("precos").child(tipo).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                float preco = dataSnapshot.getValue(float.class);

                if (aux.contains("j")) {
                    if (maisCadeira == 0 && maisMesa == 0) {
                        valor += qtdMesas * preco;
                        calculaTotal(valor);
                    }
                    if (maisMesa != 0) {
                        valor += qtdMesas * preco;
                        getPreco("precoMesa", "m", valor);
                    } else if (maisCadeira != 0) {
                        valor += qtdMesas * preco;
                        getPreco("precoCadeira", "c", valor);
                    }
                } else if (aux.contains("m")) {
                    if (maisMesa == 0) {
                        maisMesa = Integer.parseInt(textInput_edQuantidade.getText().toString());
                    }
                    valor += maisMesa * preco;
                    calculaTotal(valor);
                } else {
                    if (maisCadeira == 0) {
                        maisCadeira = Integer.parseInt(textInput_edQuantidade.getText().toString());
                    }
                    valor += maisCadeira * preco;
                    calculaTotal(valor);
                }
            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }

    public void botaoAgendarEtapa3(View v) {
        Date data = new Date();
        try {
            data = formato.parse(tvData.getText().toString());
        } catch (Exception e) {
        }
        Agendamento agenda = new Agendamento();
        agenda.setQtdMesas(qtdMesas + maisMesa);
        agenda.setQtdCadeiras(qtdCadeiras);
        agenda.setData(data);
        agenda.setHorario(tvHora.getText().toString());
        agenda.setDesconto(desconto);
        agenda.setStatus("Agendado");
        agenda.setValor(valor);
        agenda.setTelefone_responsavel(textInput_edContato.getText().toString());
        agenda.setTipoAluguel(tipoAluguel);
        agenda.setDataString(tvData.getText().toString());
        agenda.setAnoString(tvData.getText().toString().substring(6));
        agenda.setUltmodificou("adm");
        agenda.setAdmfoinot("sim");
        agenda.setClientefoinot("nao");
        agendamento.setNome_responsavel(textInput_edNome.getText().toString());
        agenda.setUid(UUID.randomUUID().toString());
        if(tipoEndereco.contains("p")){
            agenda.setBairro(textInput_edBairro.getText().toString());
            agenda.setRua(textInput_edRua.getText().toString());
            agenda.setNumero(Integer.parseInt(textInput_edNumero.getText().toString()));
            agenda.setReferencia(textInput_edReferencia.getText().toString());
            agenda.setLat(0.0);
            agenda.setLon(0.0);
        }else if(tipoEndereco.contains("a")){
            agenda.setBairro(textInput_edBairroAtual.getText().toString());
            agenda.setRua(textInput_edRuaAtual.getText().toString());
            agenda.setNumero(Integer.parseInt(textInput_edNumeroAtual.getText().toString()));
            agenda.setReferencia(textInput_edReferenciaAtual.getText().toString());
            agenda.setLat(lat);
            agenda.setLon(lon);
        }

        if(isConnected(Activity_Add_Alter_Agendamento.this)){
            try {
                mDatabase.child("agendamento").child(agenda.getUid()).setValue(agenda);
                Toast.makeText(Activity_Add_Alter_Agendamento.this, "Agendamento Realizado com Sucesso!", Toast.LENGTH_LONG).show();
                finish();
            } catch (Exception e) {
                Toast.makeText(Activity_Add_Alter_Agendamento.this, "Ocorreu um erro ao agendar novo aluguel: " + e, Toast.LENGTH_LONG).show();
            }
        }else{
            Toast.makeText(Activity_Add_Alter_Agendamento.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }
    }

    public void botaoAlterarEtapa3(View v) {
        Date data = new Date();
        try {
            data = formato.parse(tvData.getText().toString());
        } catch (Exception e) {
        }

        agendamento.setQtdMesas(qtdMesas + maisMesa);
        agendamento.setQtdCadeiras(qtdCadeiras);
        agendamento.setData(data);
        agendamento.setHorario(tvHora.getText().toString());
        agendamento.setDesconto(desconto);
        agendamento.setStatus(agendamento.getStatus());
        agendamento.setValor(valor);
        agendamento.setNome_responsavel(textInput_edNome.getText().toString());
        agendamento.setTelefone_responsavel(textInput_edContato.getText().toString());
        agendamento.setTipoAluguel(agendamento.getTipoAluguel());
        agendamento.setDataString(tvData.getText().toString());
        agendamento.setAnoString(tvData.getText().toString().substring(6));
        agendamento.setUltmodificou("adm");
        agendamento.setAdmfoinot("sim");
        agendamento.setClientefoinot("nao");
        if(tipoEndereco.contains("p")){
            agendamento.setBairro(textInput_edBairro.getText().toString());
            agendamento.setRua(textInput_edRua.getText().toString());
            agendamento.setNumero(Integer.parseInt(textInput_edNumero.getText().toString()));
            agendamento.setReferencia(textInput_edReferencia.getText().toString());
            if(!textInput_edBairro.getText().toString().equals(bairro) || textInput_edRua.getText().toString().equals(rua) || textInput_edNumero.getText().toString().equals(numero)){
                agendamento.setLat(0.0);
                agendamento.setLon(0.0);
            }
        }else if(tipoEndereco.contains("a")){
            agendamento.setBairro(textInput_edBairroAtual.getText().toString());
            agendamento.setRua(textInput_edRuaAtual.getText().toString());
            agendamento.setNumero(Integer.parseInt(textInput_edNumeroAtual.getText().toString()));
            agendamento.setReferencia(textInput_edReferenciaAtual.getText().toString());
            agendamento.setLat(lat);
            agendamento.setLon(lon);
        }

        if(isConnected(Activity_Add_Alter_Agendamento.this)){
            if(mTela.contains("a")){
                try {
                    mDatabase.child("agendamento").child(agendamento.getUid()).setValue(agendamento);
                    Toast.makeText(Activity_Add_Alter_Agendamento.this, "Agendamento Alterado com Sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(Activity_Add_Alter_Agendamento.this, "Ocorreu um erro ao alterar aluguel: " + e, Toast.LENGTH_LONG).show();
                }
            }else if(mTela.contains("r")){
                try {
                    mDatabase.child("entregue").child(agendamento.getUid()).setValue(agendamento);
                    Toast.makeText(Activity_Add_Alter_Agendamento.this, "Agendamento Alterado com Sucesso!", Toast.LENGTH_LONG).show();
                    finish();
                } catch (Exception e) {
                    Toast.makeText(Activity_Add_Alter_Agendamento.this, "Ocorreu um erro ao alterar aluguel: " + e, Toast.LENGTH_LONG).show();
                }
            }
        }else{
            Toast.makeText(Activity_Add_Alter_Agendamento.this, "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }
    }

    public void preenceCampos(Agendamento a) {
        this.agendamento = a;
        btAgendar.setVisibility(View.GONE);
        btAlterar.setVisibility(View.VISIBLE);

        if (a.getTipoAluguel().contains("j")) {
            int mesas = a.getQtdMesas();
            int cadeiras = a.getQtdCadeiras();
            if ((mesas * 4) > cadeiras) {
                rb_pecasMais_sim.setChecked(true);
                ll_qtd_tipo.setVisibility(View.VISIBLE);
                textInput_edQuantidade.setText(String.valueOf((cadeiras / 4)));
                textInput_edPecaMaisQuantidade.setText(String.valueOf(mesas - (cadeiras / 4)));
                alteraJogos = Integer.parseInt(textInput_edQuantidade.getText().toString());
                alteraPecasMesas = Integer.parseInt(textInput_edPecaMaisQuantidade.getText().toString());
            } else if((mesas * 4) < cadeiras) {
                rb_pecasMais_sim.setChecked(true);
                sp_tipo.setSelection(1);
                ll_qtd_tipo.setVisibility(View.VISIBLE);
                textInput_edQuantidade.setText(String.valueOf(mesas));
                textInput_edPecaMaisQuantidade.setText(String.valueOf(cadeiras - (mesas * 4)));
                alteraJogos = Integer.parseInt(textInput_edQuantidade.getText().toString());
                alteraPecasCadeiras = Integer.parseInt(textInput_edPecaMaisQuantidade.getText().toString());
            }else if((cadeiras % mesas) == 0){
                textInput_edQuantidade.setText(String.valueOf(mesas));
                alteraJogos = Integer.parseInt(textInput_edQuantidade.getText().toString());
            }
        } else if (a.getTipoAluguel().contains("c")) {
            textInput_edQuantidade.setText(String.valueOf(a.getQtdCadeiras()));
        } else {
            textInput_edQuantidade.setText(String.valueOf(a.getQtdMesas()));
        }

        bairro = a.getBairro();
        rua = a.getRua();
        numero = String.valueOf(a.getNumero());
        referencia = a.getReferencia();
        tvData.setText(a.getDataString());
        tvHora.setText(a.getHorario());
        textInput_edBairro.setText(bairro);
        textInput_edRua.setText(rua);
        textInput_edNumero.setText(numero);
        textInput_edReferencia.setText(referencia);
        textInput_edNome.setText(a.getNome_responsavel());
        textInput_edContato.setText(a.getTelefone_responsavel());
    }

    public static boolean isConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if ( cm != null ) {
            NetworkInfo ni = cm.getActiveNetworkInfo();

            return ni != null && ni.isConnected();
        }

        return false;
    }

    @Override
    public void onLocationChanged(Location location) {

        Geocoder geocoder;
        Address address = null;
        List<Address> addresses;
        lat = location.getLatitude();
        lon = location.getLongitude();

        geocoder = new Geocoder(getApplicationContext());
        try {
            addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                address = addresses.get(0);
                textInput_edBairroAtual.setText(address.getSubLocality());
                textInput_edRuaAtual.setText(address.getThoroughfare());
                tvRuaInfor.setText("Se o nome da rua for diferente selecione opção Personalizado e depois a opção Minha Localização para atualizar!");
            }
        }catch (IOException e){}

    }

    private ArrayList findUnAskedPermissions(ArrayList<String> wanted){
        ArrayList result = new ArrayList();

        for(String perm : wanted){
            if(!hasPermission(perm)){
                result.add(perm);
            }
        }

        return result;
    }

    private boolean hasPermission(String permission){
        if(canAskPermission()){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                return (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED);
            }
        }
        return true;
    }

    private boolean canAskPermission(){
        return (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1);
    }

    public void showSettingsAlert(){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);
        alertDialog.setTitle("GPS Desativado");
        alertDialog.setMessage("Ativar GPS?");
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                startActivity(intent);
                sp_endereco.setSelection(0);
            }
        });
        alertDialog.setNegativeButton("Não", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                sp_endereco.setSelection(0);
            }
        });
        alertDialog.show();
    }

    private void startGettingLocations(){
        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        boolean isGPS = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetwork = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean cancGetLocation = true;
        int ALL_PERMISSIONS_RESULT = 10;
        long MIN_DISTANCE_FOR_UPDATES = 10; // Distancia em Metros
        long MIN_TIME_Bw_UPDATES = 1000 * 10; // Time em milissegundos

        ArrayList<String> permissions = new ArrayList<>();
        ArrayList<String> permissionsToRequest;

        permissions.add(Manifest.permission.ACCESS_FINE_LOCATION);
        permissions.add(Manifest.permission.ACCESS_COARSE_LOCATION);
        permissionsToRequest = findUnAskedPermissions(permissions);

        // Verificando se o gps e internet tão ativos
        if(!isGPS && !isNetwork){
            showSettingsAlert();
        }else{
            // Verificando Permissions

            // verifique as permissões para versões posteriores
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                if(permissionsToRequest.size() > 0){
                    requestPermissions(permissionsToRequest.toArray(new String[permissionsToRequest.size()]),ALL_PERMISSIONS_RESULT);
                    sp_endereco.setSelection(0);
                    cancGetLocation = false;
                }
            }
        }

        // verifica se a localização exata e a localização aproximada foram concedidas
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            Toast.makeText(this, "Permissão Negada", Toast.LENGTH_SHORT).show();
            return;
        }

        // começa a solicitar atualizações de local
        if(cancGetLocation){
            if(isGPS){
                sp_endereco.setSelection(0);
                sp_endereco.setSelection(1);
                sp_endereco.setSelection(0);
                sp_endereco.setSelection(1);
                Toast.makeText(this, "Aguarde um momento, pegando localização.", Toast.LENGTH_LONG).show();
                lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,MIN_TIME_Bw_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);
            }else if(isNetwork){
                // do provedor de rede
                sp_endereco.setSelection(0);
                sp_endereco.setSelection(1);
                sp_endereco.setSelection(0);
                sp_endereco.setSelection(1);
                Toast.makeText(this, "Aguarde um momento, pegando localização.", Toast.LENGTH_LONG).show();
                lm.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,MIN_TIME_Bw_UPDATES,MIN_DISTANCE_FOR_UPDATES,this);
            }
        }else{
            Toast.makeText(this, "Não é possível obter a Localização", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

        textInput_edBairroAtual.setText("");
        textInput_edRuaAtual.setText("");
        textInput_edNumero.setText("");
        textInput_edReferencia.setText("");
        tvRuaInfor.setText("");

    }
}
