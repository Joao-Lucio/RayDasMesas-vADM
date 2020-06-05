package com.example.raydasmesas_28_01_2020;


import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.raydasmesas_28_01_2020.domain.Agendamento;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.Chart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LegendEntry;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Fragment_Grafico extends Fragment {

    private Context context;
    private BarChart graficoAnual;
    private LineChart graficoTemporal,graficoQtd;
    private DatabaseReference mDatabase;
    private CheckBox cb2015,cb2016,cb2017,cb2018,cb2019,cb2020;
    private CheckBox cbQtd2015,cbQtd2016,cbQtd2017,cbQtd2018,cbQtd2019,cbQtd2020;
    private List<Agendamento> mListHistorico = new ArrayList<>();
    private float jan15 = 0,fev15 = 0,mar15 = 0,abr15 = 0,mai15 = 0, jun15 = 0,jul15 = 0, ago15 = 0, set15 = 0, out15 = 0,nov15 = 0,dez15 = 0;
    private float jan16 = 0,fev16 = 0,mar16 = 0,abr16 = 0,mai16 = 0, jun16 = 0,jul16 = 0, ago16 = 0, set16 = 0, out16 = 0,nov16 = 0,dez16 = 0;
    private float jan17 = 0,fev17 = 0,mar17 = 0,abr17 = 0,mai17 = 0, jun17 = 0,jul17 = 0, ago17 = 0, set17 = 0, out17 = 0,nov17 = 0,dez17 = 0;
    private float jan18 = 0,fev18 = 0,mar18 = 0,abr18 = 0,mai18 = 0, jun18 = 0,jul18 = 0, ago18 = 0, set18 = 0, out18 = 0,nov18 = 0,dez18 = 0;
    private float jan19 = 0,fev19 = 0,mar19 = 0,abr19 = 0,mai19 = 0, jun19 = 0,jul19 = 0, ago19 = 0, set19 = 0, out19 = 0,nov19 = 0,dez19 = 0;
    private float jan20 = 0,fev20 = 0,mar20 = 0,abr20 = 0,mai20 = 0, jun20 = 0,jul20 = 0, ago20 = 0, set20 = 0, out20 = 0,nov20 = 0,dez20 = 0;
    private int qtdJan15 = 0,qtdFev15 = 0,qtdMar15 = 0,qtdAbr15 = 0,qtdMai15 = 0,qtdJun15 = 0,qtdJul15 = 0,qtdAgo15 = 0,qtdSet15 = 0,qtdOut15 = 0,qtdNov15 = 0,qtdDez15 = 0;
    private int qtdJan16 = 0,qtdFev16 = 0,qtdMar16 = 0,qtdAbr16 = 0,qtdMai16 = 0,qtdJun16 = 0,qtdJul16 = 0,qtdAgo16 = 0,qtdSet16 = 0,qtdOut16 = 0,qtdNov16 = 0,qtdDez16 = 0;
    private int qtdJan17 = 0,qtdFev17 = 0,qtdMar17 = 0,qtdAbr17 = 0,qtdMai17 = 0,qtdJun17 = 0,qtdJul17 = 0,qtdAgo17 = 0,qtdSet17 = 0,qtdOut17 = 0,qtdNov17 = 0,qtdDez17 = 0;
    private int qtdJan18 = 0,qtdFev18 = 0,qtdMar18 = 0,qtdAbr18 = 0,qtdMai18 = 0,qtdJun18 = 0,qtdJul18 = 0,qtdAgo18 = 0,qtdSet18 = 0,qtdOut18 = 0,qtdNov18 = 0,qtdDez18 = 0;
    private int qtdJan19 = 0,qtdFev19 = 0,qtdMar19 = 0,qtdAbr19 = 0,qtdMai19 = 0,qtdJun19 = 0,qtdJul19 = 0,qtdAgo19 = 0,qtdSet19 = 0,qtdOut19 = 0,qtdNov19 = 0,qtdDez19 = 0;
    private int qtdJan20 = 0,qtdFev20 = 0,qtdMar20 = 0,qtdAbr20 = 0,qtdMai20 = 0,qtdJun20 = 0,qtdJul20 = 0,qtdAgo20 = 0,qtdSet20 = 0,qtdOut20 = 0,qtdNov20 = 0,qtdDez20 = 0;
    private String[] meses = new String[]{"JAN","FEV","MAR","ABR","MAI","JUN","JUL","AGO","SET","OUT","NOV","DEZ"};
    private String[] anos = new String[]{"2015","2016","2017","2018","2019","2020"};
    private List<Float> valores2015 = new ArrayList<>();
    private List<Float> valores2016 = new ArrayList<>();
    private List<Float> valores2017 = new ArrayList<>();
    private List<Float> valores2018 = new ArrayList<>();
    private List<Float> valores2019 = new ArrayList<>();
    private List<Float> valores2020 = new ArrayList<>();
    private List<Integer> qtd2015 = new ArrayList<>();
    private List<Integer> qtd2016 = new ArrayList<>();
    private List<Integer> qtd2017 = new ArrayList<>();
    private List<Integer> qtd2018 = new ArrayList<>();
    private List<Integer> qtd2019 = new ArrayList<>();
    private List<Integer> qtd2020 = new ArrayList<>();

    public Fragment_Grafico() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        context = container.getContext();
        View root = inflater.inflate(R.layout.fragment__grafico, container, false);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        if(!isConnected(getContext())){
            Toast.makeText(getContext(), "Conecte-se a uma rede com Internet!", Toast.LENGTH_LONG).show();
        }

        iniComponentes(root);
        carregarGraficos();

        return root;
    }

    public void carregarGraficos(){
        mDatabase.child("historico").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NotNull DataSnapshot dataSnapshot) {
                mListHistorico.clear();
                for(DataSnapshot obj : dataSnapshot.getChildren()){
                    Agendamento a = obj.getValue(Agendamento.class);
                    mListHistorico.add(a);
                }

                for(int i = 0; i < mListHistorico.size();i++){
                    Agendamento aux = mListHistorico.get(i);
                    setValores(aux);
                }
                addNaLista();
                plotarGraficoAnual();
                plotarGraficoTemporal();
                plotarGraficoQtd();

            }

            @Override
            public void onCancelled(@NotNull DatabaseError databaseError) {

            }
        });
    }

    public void setValores(Agendamento a){
        String ano = a.getAnoString();
        int mes = a.getData().getMonth();
        switch (mes+1){
            case 1: if(ano.equals("2015")){
                jan15 += a.getValor();
                qtdJan15++;
            }else if(ano.equals("2016")){
                jan16 += a.getValor();
                qtdJan16++;
            }else if(ano.equals("2017")){
                jan17 += a.getValor();
                qtdJan17++;
            }else if(ano.equals("2018")){
                jan18 += a.getValor();
                qtdJan18++;
            }else if(ano.equals("2019")){
                jan19 += a.getValor();
                qtdJan19++;
            }else if(ano.equals("2020")){
                jan20 += a.getValor();
                qtdJan20++;
            }
                break;
            case 2:if(ano.equals("2015")){
                fev15 += a.getValor();
                qtdFev15++;
            }else if(ano.equals("2016")){
                fev16 += a.getValor();
                qtdFev16++;
            }else if(ano.equals("2017")){
                fev17 += a.getValor();
                qtdFev17++;
            }else if(ano.equals("2018")){
                fev18 += a.getValor();
                qtdFev18++;
            }else if(ano.equals("2019")){
                fev19 += a.getValor();
                qtdFev19++;
            }else if(ano.equals("2020")){
                fev20 += a.getValor();
                qtdFev20++;
            }
                break;
            case 3:if(ano.equals("2015")){
                mar15 += a.getValor();
                qtdMar15++;
            }else if(ano.equals("2016")){
                mar16 += a.getValor();
                qtdMar16++;
            }else if(ano.equals("2017")){
                mar17 += a.getValor();
                qtdMar17++;
            }else if(ano.equals("2018")){
                mar18 += a.getValor();
                qtdMar18++;
            }else if(ano.equals("2019")){
                mar19 += a.getValor();
                qtdMar19++;
            }else if(ano.equals("2020")){
                mar20 += a.getValor();
                qtdMar20++;
            }
                break;
            case 4:if(ano.equals("2015")){
                abr15 += a.getValor();
                qtdAbr15++;
            }else if(ano.equals("2016")){
                abr16 += a.getValor();
                qtdAbr16++;
            }else if(ano.equals("2017")){
                abr17 += a.getValor();
                qtdAbr17++;
            }else if(ano.equals("2018")){
                abr18 += a.getValor();
                qtdAbr18++;
            }else if(ano.equals("2019")){
                abr19 += a.getValor();
                qtdAbr19++;
            }else if(ano.equals("2020")){
                abr20 += a.getValor();
                qtdAbr20++;
            }
                break;
            case 5:if(ano.equals("2015")){
                mai15 += a.getValor();
                qtdMai15++;
            }else if(ano.equals("2016")){
                mai16 += a.getValor();
                qtdMai16++;
            }else if(ano.equals("2017")){
                mai17 += a.getValor();
                qtdMai17++;
            }else if(ano.equals("2018")){
                mai18 += a.getValor();
                qtdMai18++;
            }else if(ano.equals("2019")){
                mai19 += a.getValor();
                qtdMai19++;
            }else if(ano.equals("2020")){
                mai20 += a.getValor();
                qtdMai20++;
            }
                break;
            case 6:if(ano.equals("2015")){
                jun15 += a.getValor();
                qtdJun15++;
            }else if(ano.equals("2016")){
                jun16 += a.getValor();
                qtdJun16++;
            }else if(ano.equals("2017")){
                jun17 += a.getValor();
                qtdJun17++;
            }else if(ano.equals("2018")){
                jun18 += a.getValor();
                qtdJun18++;
            }else if(ano.equals("2019")){
                jun19 += a.getValor();
                qtdJun19++;
            }else if(ano.equals("2020")){
                jun20 += a.getValor();
                qtdJun20++;
            }
                break;
            case 7:if(ano.equals("2015")){
                jul15 += a.getValor();
                qtdJul15++;
            }else if(ano.equals("2016")){
                jul16 += a.getValor();
                qtdJul16++;
            }else if(ano.equals("2017")){
                jul17 += a.getValor();
                qtdJul17++;
            }else if(ano.equals("2018")){
                jul18 += a.getValor();
                qtdJul18++;
            }else if(ano.equals("2019")){
                jul19 += a.getValor();
                qtdJul19++;
            }else if(ano.equals("2020")){
                jul20 += a.getValor();
                qtdJul20++;
            }
                break;
            case 8:if(ano.equals("2015")){
                ago15 += a.getValor();
                qtdAgo15++;
            }else if(ano.equals("2016")){
                ago16 += a.getValor();
                qtdAgo16++;
            }else if(ano.equals("2017")){
                ago17 += a.getValor();
                qtdAgo17++;
            }else if(ano.equals("2018")){
                ago18 += a.getValor();
                qtdAgo18++;
            }else if(ano.equals("2019")){
                ago19 += a.getValor();
                qtdAgo19++;
            }else if(ano.equals("2020")){
                ago20 += a.getValor();
                qtdAgo20++;
            }
                break;
            case 9:if(ano.equals("2015")){
                set15 += a.getValor();
                qtdSet15++;
            }else if(ano.equals("2016")){
                set16 += a.getValor();
                qtdSet16++;
            }else if(ano.equals("2017")){
                set17 += a.getValor();
                qtdSet17++;
            }else if(ano.equals("2018")){
                set18 += a.getValor();
                qtdSet18++;
            }else if(ano.equals("2019")){
                set19 += a.getValor();
                qtdSet19++;
            }else if(ano.equals("2020")){
                set20 += a.getValor();
                qtdSet20++;
            }
                break;
            case 10:if(ano.equals("2015")){
                out15 += a.getValor();
                qtdOut15++;
            }else if(ano.equals("2016")){
                out16 += a.getValor();
                qtdOut16++;
            }else if(ano.equals("2017")){
                out17 += a.getValor();
                qtdOut17++;
            }else if(ano.equals("2018")){
                out18 += a.getValor();
                qtdOut18++;
            }else if(ano.equals("2019")){
                out19 += a.getValor();
                qtdOut19++;
            }else if(ano.equals("2020")){
                out20 += a.getValor();
                qtdOut20++;
            }
                break;
            case 11:if(ano.equals("2015")){
                nov15 += a.getValor();
                qtdNov15++;
            }else if(ano.equals("2016")){
                nov16 += a.getValor();
                qtdNov16++;
            }else if(ano.equals("2017")){
                nov17 += a.getValor();
                qtdNov17++;
            }else if(ano.equals("2018")){
                nov18 += a.getValor();
                qtdNov18++;
            }else if(ano.equals("2019")){
                nov19 += a.getValor();
                qtdNov19++;
            }else if(ano.equals("2020")){
                nov20 += a.getValor();
                qtdNov20++;
            }
                break;
            case 12:if(ano.equals("2015")){
                dez15 += a.getValor();
                qtdDez15++;
            }else if(ano.equals("2016")){
                dez16 += a.getValor();
                qtdDez16++;
            }else if(ano.equals("2017")){
                dez17 += a.getValor();
                qtdDez17++;
            }else if(ano.equals("2018")){
                dez18 += a.getValor();
                qtdDez18++;
            }else if(ano.equals("2019")){
                dez19 += a.getValor();
                qtdDez19++;
            }else if(ano.equals("2020")){
                dez20 += a.getValor();
                qtdDez20++;
            }
                break;
        }
    }

    public void addNaLista(){
        valores2015.add(jan15);
        valores2015.add(fev15);
        valores2015.add(mar15);
        valores2015.add(abr15);
        valores2015.add(mai15);
        valores2015.add(jun15);
        valores2015.add(jul15);
        valores2015.add(ago15);
        valores2015.add(set15);
        valores2015.add(out15);
        valores2015.add(nov15);
        valores2015.add(dez15);

        valores2016.add(jan16);
        valores2016.add(fev16);
        valores2016.add(mar16);
        valores2016.add(abr16);
        valores2016.add(mai16);
        valores2016.add(jun16);
        valores2016.add(jul16);
        valores2016.add(ago16);
        valores2016.add(set16);
        valores2016.add(out16);
        valores2016.add(nov16);
        valores2016.add(dez16);

        valores2017.add(jan17);
        valores2017.add(fev17);
        valores2017.add(mar17);
        valores2017.add(abr17);
        valores2017.add(mai17);
        valores2017.add(jun17);
        valores2017.add(jul17);
        valores2017.add(ago17);
        valores2017.add(set17);
        valores2017.add(out17);
        valores2017.add(nov17);
        valores2017.add(dez17);

        valores2018.add(jan18);
        valores2018.add(fev18);
        valores2018.add(mar18);
        valores2018.add(abr18);
        valores2018.add(mai18);
        valores2018.add(jun18);
        valores2018.add(jul18);
        valores2018.add(ago18);
        valores2018.add(set18);
        valores2018.add(out18);
        valores2018.add(nov18);
        valores2018.add(dez18);

        valores2019.add(jan19);
        valores2019.add(fev19);
        valores2019.add(mar19);
        valores2019.add(abr19);
        valores2019.add(mai19);
        valores2019.add(jun19);
        valores2019.add(jul19);
        valores2019.add(ago19);
        valores2019.add(set19);
        valores2019.add(out19);
        valores2019.add(nov19);
        valores2019.add(dez19);

        valores2020.add(jan20);
        valores2020.add(fev20);
        valores2020.add(mar20);
        valores2020.add(abr20);
        valores2020.add(mai20);
        valores2020.add(jun20);
        valores2020.add(jul20);
        valores2020.add(ago20);
        valores2020.add(set20);
        valores2020.add(out20);
        valores2020.add(nov20);
        valores2020.add(dez20);

        qtd2015.add(qtdJan15);
        qtd2015.add(qtdFev15);
        qtd2015.add(qtdMar15);
        qtd2015.add(qtdAbr15);
        qtd2015.add(qtdMai15);
        qtd2015.add(qtdJun15);
        qtd2015.add(qtdJul15);
        qtd2015.add(qtdAgo15);
        qtd2015.add(qtdSet15);
        qtd2015.add(qtdOut15);
        qtd2015.add(qtdNov15);
        qtd2015.add(qtdDez15);

        qtd2016.add(qtdJan16);
        qtd2016.add(qtdFev16);
        qtd2016.add(qtdMar16);
        qtd2016.add(qtdAbr16);
        qtd2016.add(qtdMai16);
        qtd2016.add(qtdJun16);
        qtd2016.add(qtdJul16);
        qtd2016.add(qtdAgo16);
        qtd2016.add(qtdSet16);
        qtd2016.add(qtdOut16);
        qtd2016.add(qtdNov16);
        qtd2016.add(qtdDez16);

        qtd2017.add(qtdJan17);
        qtd2017.add(qtdFev17);
        qtd2017.add(qtdMar17);
        qtd2017.add(qtdAbr17);
        qtd2017.add(qtdMai17);
        qtd2017.add(qtdJun17);
        qtd2017.add(qtdJul17);
        qtd2017.add(qtdAgo17);
        qtd2017.add(qtdSet17);
        qtd2017.add(qtdOut17);
        qtd2017.add(qtdNov17);
        qtd2017.add(qtdDez17);

        qtd2018.add(qtdJan18);
        qtd2018.add(qtdFev18);
        qtd2018.add(qtdMar18);
        qtd2018.add(qtdAbr18);
        qtd2018.add(qtdMai18);
        qtd2018.add(qtdJun18);
        qtd2018.add(qtdJul18);
        qtd2018.add(qtdAgo18);
        qtd2018.add(qtdSet18);
        qtd2018.add(qtdOut18);
        qtd2018.add(qtdNov18);
        qtd2018.add(qtdDez18);

        qtd2019.add(qtdJan19);
        qtd2019.add(qtdFev19);
        qtd2019.add(qtdMar19);
        qtd2019.add(qtdAbr19);
        qtd2019.add(qtdMai19);
        qtd2019.add(qtdJun19);
        qtd2019.add(qtdJul19);
        qtd2019.add(qtdAgo19);
        qtd2019.add(qtdSet19);
        qtd2019.add(qtdOut19);
        qtd2019.add(qtdNov19);
        qtd2019.add(qtdDez19);

        qtd2020.add(qtdJan20);
        qtd2020.add(qtdFev20);
        qtd2020.add(qtdMar20);
        qtd2020.add(qtdAbr20);
        qtd2020.add(qtdMai20);
        qtd2020.add(qtdJun20);
        qtd2020.add(qtdJul20);
        qtd2020.add(qtdAgo20);
        qtd2020.add(qtdSet20);
        qtd2020.add(qtdOut20);
        qtd2020.add(qtdNov20);
        qtd2020.add(qtdDez20);
    }

    public void plotarGraficoAnual(){
        ArrayList<Entry> linhaAnual = new ArrayList<>();
        int total2015 = 0;
        int total2016 = 0;
        int total2017 = 0;
        int total2018 = 0;
        int total2019 = 0;
        int total2020 = 0;
        for(int i = 0; i < valores2015.size();i++){
            total2015 += valores2015.get(i);
        }
        for(int i = 0; i < valores2016.size();i++){
            total2016 += valores2016.get(i);
        }
        for(int i = 0; i < valores2017.size();i++){
            total2017 += valores2017.get(i);
        }
        for(int i = 0; i < valores2018.size();i++){
            total2018 += valores2018.get(i);
        }
        for(int i = 0; i < valores2019.size();i++){
            total2019 += valores2019.get(i);
        }
        for(int i = 0; i < valores2020.size();i++){
            total2020 += valores2020.get(i);
        }

        List<BarEntry> entradas = new ArrayList<>();
        entradas.add(new BarEntry(0,total2015));
        entradas.add(new BarEntry(1,total2016));
        entradas.add(new BarEntry(2,total2017));
        entradas.add(new BarEntry(3,total2018));
        entradas.add(new BarEntry(4,total2019));
        entradas.add(new BarEntry(5,total2020));

        // Mandamos os dados para cria ografico
        BarDataSet dados = new BarDataSet(entradas,"Graficos de Barras");
        dados.setValueTextColor(Color.BLACK);
        dados.setValueTextSize(12);
        BarData data = new BarData(dados);

        dados.setColors(ColorTemplate.COLORFUL_COLORS);// Cor das Barras
        data.setBarWidth(0.9f);// Esparçamento entre as barras
        graficoAnual.setData(data);
        graficoAnual.setFitBars(true); // Colocando as barras centradas
        axisX(graficoAnual.getXAxis(),"anual");
        axisRight(graficoAnual.getAxisRight());
        axisLeft(graficoAnual.getAxisLeft());
        graficoAnual.getLegend().setEnabled(false);
        graficoAnual.getDescription().setEnabled(false);
        graficoAnual.invalidate(); // Atualizaão do hacer
        graficoAnual.setVisibility(View.GONE);
        graficoAnual.setVisibility(View.VISIBLE);
    }

    public void plotarGraficoTemporal(){
        ArrayList<Entry> linha2015 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2015.get(i));
            linha2015.add(entry);
        }
        final LineDataSet set1 = new LineDataSet(linha2015,"2015");

        ArrayList<Entry> linha2016 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2016.get(i));
            linha2016.add(entry);
        }
        final LineDataSet set2 = new LineDataSet(linha2016,"2016");
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);

        ArrayList<Entry> linha2017 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2017.get(i));
            linha2017.add(entry);
        }
        final LineDataSet set3 = new LineDataSet(linha2017,"2017");
        set3.setColor(Color.GREEN);
        set3.setCircleColor(Color.GREEN);

        ArrayList<Entry> linha2018 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2018.get(i));
            linha2018.add(entry);
        }
        final LineDataSet set4 = new LineDataSet(linha2018,"2018");
        set4.setColor(Color.MAGENTA);
        set4.setCircleColor(Color.MAGENTA);

        ArrayList<Entry> linha2019 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2019.get(i));
            linha2019.add(entry);
        }
        final LineDataSet set5 = new LineDataSet(linha2019,"2019");
        set5.setColor(Color.YELLOW);
        set5.setCircleColor(Color.YELLOW);

        ArrayList<Entry> linha2020 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,valores2020.get(i));
            linha2020.add(entry);
        }
        final LineDataSet set6 = new LineDataSet(linha2020,"2020");
        set6.setColor(Color.GRAY);
        set6.setCircleColor(Color.GRAY);

        set1.setLineWidth(3f);
        set2.setLineWidth(3f);
        set3.setLineWidth(3f);
        set4.setLineWidth(3f);
        set5.setLineWidth(3f);
        set6.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set2.setValueTextSize(10f);
        set3.setValueTextSize(10f);
        set4.setValueTextSize(10f);
        set5.setValueTextSize(10f);
        set6.setValueTextSize(10f);
        set2.setVisible(false);
        set3.setVisible(false);
        set4.setVisible(false);
        set5.setVisible(false);
        set6.setVisible(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);
        dataSets.add(set6);

        LineData data = new LineData(dataSets);

        graficoTemporal.setDragEnabled(true);
        graficoTemporal.setScaleEnabled(false);
        axisX(graficoTemporal.getXAxis());
        graficoTemporal.getDescription().setEnabled(false);
        graficoTemporal.setData(data);

        graficoTemporal.setVisibility(View.GONE);
        graficoTemporal.setVisibility(View.VISIBLE);
        cb2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2015.isChecked()){
                    set1.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set1.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

        cb2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2016.isChecked()){
                    set2.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set2.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

        cb2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2017.isChecked()){
                    set3.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set3.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

        cb2018.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2018.isChecked()){
                    set4.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set4.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

        cb2019.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2019.isChecked()){
                    set5.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set5.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

        cb2020.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cb2020.isChecked()){
                    set6.setVisible(true);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }else{
                    set6.setVisible(false);
                    graficoTemporal.setVisibility(View.GONE);
                    graficoTemporal.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    public void plotarGraficoQtd(){

        ArrayList<Entry> linha2015 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2015.get(i));
            linha2015.add(entry);
        }
        final LineDataSet set1 = new LineDataSet(linha2015,"2015");

        ArrayList<Entry> linha2016 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2016.get(i));
            linha2016.add(entry);
        }
        final LineDataSet set2 = new LineDataSet(linha2016,"2016");
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.RED);

        ArrayList<Entry> linha2017 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2017.get(i));
            linha2017.add(entry);
        }
        final LineDataSet set3 = new LineDataSet(linha2017,"2017");
        set3.setColor(Color.GREEN);
        set3.setCircleColor(Color.GREEN);

        ArrayList<Entry> linha2018 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2018.get(i));
            linha2018.add(entry);
        }
        final LineDataSet set4 = new LineDataSet(linha2018,"2018");
        set4.setColor(Color.MAGENTA);
        set4.setCircleColor(Color.MAGENTA);

        ArrayList<Entry> linha2019 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2019.get(i));
            linha2019.add(entry);
        }
        final LineDataSet set5 = new LineDataSet(linha2019,"2019");
        set5.setColor(Color.YELLOW);
        set5.setCircleColor(Color.YELLOW);

        ArrayList<Entry> linha2020 = new ArrayList<>();
        for(int i = 0; i < 12;i++){
            Entry entry = new Entry(i,qtd2020.get(i));
            linha2020.add(entry);
        }
        final LineDataSet set6 = new LineDataSet(linha2020,"2020");
        set6.setColor(Color.GRAY);
        set6.setCircleColor(Color.GRAY);

        set1.setLineWidth(3f);
        set2.setLineWidth(3f);
        set3.setLineWidth(3f);
        set4.setLineWidth(3f);
        set5.setLineWidth(3f);
        set6.setLineWidth(3f);
        set1.setValueTextSize(10f);
        set2.setValueTextSize(10f);
        set3.setValueTextSize(10f);
        set4.setValueTextSize(10f);
        set5.setValueTextSize(10f);
        set6.setValueTextSize(10f);
        set2.setVisible(false);
        set3.setVisible(false);
        set4.setVisible(false);
        set5.setVisible(false);
        set6.setVisible(false);

        ArrayList<ILineDataSet> dataSets = new ArrayList<>();
        dataSets.add(set1);
        dataSets.add(set2);
        dataSets.add(set3);
        dataSets.add(set4);
        dataSets.add(set5);
        dataSets.add(set6);

        LineData data = new LineData(dataSets);

        graficoQtd.setDragEnabled(true);
        graficoQtd.setScaleEnabled(false);
        axisX(graficoQtd.getXAxis());
        graficoQtd.getDescription().setEnabled(false);
        graficoQtd.setData(data);

        graficoQtd.setVisibility(View.GONE);
        graficoQtd.setVisibility(View.VISIBLE);
        cbQtd2015.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2015.isChecked()){
                    set1.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set1.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbQtd2016.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2016.isChecked()){
                    set2.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set2.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbQtd2017.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2017.isChecked()){
                    set3.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set3.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbQtd2018.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2018.isChecked()){
                    set4.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set4.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbQtd2019.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2019.isChecked()){
                    set5.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set5.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

        cbQtd2020.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(cbQtd2020.isChecked()){
                    set6.setVisible(true);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }else{
                    set6.setVisible(false);
                    graficoQtd.setVisibility(View.GONE);
                    graficoQtd.setVisibility(View.VISIBLE);
                }
            }
        });

    }

    private void axisX(XAxis axis){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(meses));
        //axis.setEnabled(false);//tira os titulos em baixo de cada barra
    }

    private void axisX(XAxis axis,String anual){
        axis.setGranularityEnabled(true);
        axis.setPosition(XAxis.XAxisPosition.BOTTOM);
        axis.setValueFormatter(new IndexAxisValueFormatter(anos));
        //axis.setEnabled(false);//tira os titulos em baixo de cada barra
    }

    private void axisRight(YAxis axis){
        axis.setEnabled(false);
    }

    private void axisLeft(YAxis axis){
        axis.setSpaceTop(100);
        axis.setAxisMinimum(0);
        //axis.setGranularity(20);// vai aumentando de vinte em vinte
    }

    private void iniComponentes(View root){
        graficoAnual = (BarChart) root.findViewById(R.id.graficoAnual);
        graficoQtd = (LineChart) root.findViewById(R.id.graficoQtd);
        graficoTemporal = (LineChart) root.findViewById(R.id.grafico_temporal);
        cb2015 = (CheckBox) root.findViewById(R.id.cb_2015);
        cb2016 = (CheckBox) root.findViewById(R.id.cb_2016);
        cb2017 = (CheckBox) root.findViewById(R.id.cb_2017);
        cb2018 = (CheckBox) root.findViewById(R.id.cb_2018);
        cb2019 = (CheckBox) root.findViewById(R.id.cb_2019);
        cb2020 = (CheckBox) root.findViewById(R.id.cb_2020);
        cbQtd2015 = (CheckBox) root.findViewById(R.id.cb_qtd_2015);
        cbQtd2016 = (CheckBox) root.findViewById(R.id.cb_qtd_2016);
        cbQtd2017 = (CheckBox) root.findViewById(R.id.cb_qtd_2017);
        cbQtd2018 = (CheckBox) root.findViewById(R.id.cb_qtd_2018);
        cbQtd2019 = (CheckBox) root.findViewById(R.id.cb_qtd_2019);
        cbQtd2020 = (CheckBox) root.findViewById(R.id.cb_qtd_2020);
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
