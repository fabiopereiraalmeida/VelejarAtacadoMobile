package br.com.grupocaravela.velejar.atacadomobile;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidVendaCabecalhoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidVendaDetalheDAO;
import br.com.grupocaravela.velejar.atacadomobile.fragments.VendasFinalizadasHistoricoFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;

public class HistoricoVendasActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private VendasFinalizadasHistoricoFragment frag;

    private int posicao = 0;
    private boolean finalLista = false;
    private Toolbar mainToolbarTop;

    private ImageLoader mImageLoader;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private int idVendaCabecalho;
    private Long ultimoIdVendaCabecalho;

    private int ano, mes, dia;
    private Calendar cDataInicio = Calendar.getInstance();
    private Calendar cDataFim = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_historico_vendas);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Historico de vendas enviadas");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(HistoricoVendasActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        listarAndroidVendaCabecalho();
    }

    private void listarAndroidVendaCabecalho() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                frag = (VendasFinalizadasHistoricoFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
                if (frag == null) {
                    frag = new VendasFinalizadasHistoricoFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_historico, frag, "mainFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    public List<AndroidVendaCabecalho> getSetAndroidVendaCabecalhoList(int qtd) {
        List<AndroidVendaCabecalho> listAux = new ArrayList<>();
        ArrayList<AndroidVendaCabecalho> listAndroidVendaCabecalho = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado  " +
                                "FROM android_venda_cabecalho WHERE enviado LIKE '1'", null);

        cursor.moveToFirst();

        int tamanhoListaAndroidVendaCabecalho = cursor.getCount();

        for (int i = 0; i < tamanhoListaAndroidVendaCabecalho; i++) {

            AndroidVendaCabecalho avc = new AndroidVendaCabecalho();

            avc.setId(cursor.getLong(0));
            avc.setCodVenda(cursor.getLong(1));
            avc.setObservacao(cursor.getString(2));
            avc.setCliente(cursor.getLong(3));
            avc.setEntrada(cursor.getDouble(4));
            avc.setJuros(cursor.getDouble(5));
            avc.setValorParcial(cursor.getDouble(6));
            avc.setValorDesconto(cursor.getDouble(7));
            avc.setValorTotal(cursor.getDouble(8));
            try{
                String d = cursor.getString(9);
                avc.setDataVenda(d);
            }catch (Exception e){
            }
            try{
                String d = cursor.getString(10);
                avc.setDataPrimeiroVencimento(d);
            }catch (Exception e){
            }
            avc.setUsuario(cursor.getLong(11));
            avc.setFormaPagamento(cursor.getLong(12));
            avc.setEmpresa(cursor.getLong(13));
            if (cursor.getString(14).equals("1")){
                avc.setVendaAprovada(true);
            }else{
                avc.setVendaAprovada(false);
            }
            if (cursor.getString(15).equals("1")){
                avc.setEnviado(true);
            }else{
                avc.setEnviado(false);
            }

            listAndroidVendaCabecalho.add(avc);

            cursor.moveToNext();
        }

        Log.i("TAMANHO", "TAMANHO DO LISTANDROIDVENDACABECALHO " + listAndroidVendaCabecalho.size() + " ");
        if (finalLista == false) {

            for (int y = 0; y < qtd; y++) {
                if (posicao == listAndroidVendaCabecalho.size()) {
                    //posicao = 1;
                    finalLista = true;
                } else {
                    listAux.add(listAndroidVendaCabecalho.get(posicao));
                    posicao++;
                    //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                }

            }
        }
        return (listAux);
    }

    public List<AndroidVendaCabecalho> getSetAndroidVendaDetalheListCompleta() {
        ArrayList<AndroidVendaCabecalho> listAndroidVendaDetalhes = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado  " +
                                "FROM android_venda_cabecalho WHERE enviado LIKE '1'", null);

        cursor.moveToFirst();

        int tamanhoListaAndroidVendaDetalhe = cursor.getCount();

        for (int i = 0; i < tamanhoListaAndroidVendaDetalhe; i++) {

            AndroidVendaCabecalho avc = new AndroidVendaCabecalho();

            avc.setId(cursor.getLong(0));
            avc.setCodVenda(cursor.getLong(1));
            avc.setObservacao(cursor.getString(2));
            avc.setCliente(cursor.getLong(3));
            avc.setEntrada(cursor.getDouble(4));
            avc.setJuros(cursor.getDouble(5));
            avc.setValorParcial(cursor.getDouble(6));
            avc.setValorDesconto(cursor.getDouble(7));
            avc.setValorTotal(cursor.getDouble(8));
            try{
                String d = cursor.getString(9);
                avc.setDataVenda(d);
            }catch (Exception e){
            }
            try{
                String d = cursor.getString(10);
                avc.setDataPrimeiroVencimento(d);
            }catch (Exception e){
            }
            avc.setUsuario(cursor.getLong(11));
            avc.setFormaPagamento(cursor.getLong(12));
            avc.setEmpresa(cursor.getLong(13));
            if (cursor.getString(14).equals("1")){
                avc.setVendaAprovada(true);
            }else{
                avc.setVendaAprovada(false);
            }
            if (cursor.getString(15).equals("1")){
                avc.setEnviado(true);
            }else{
                avc.setEnviado(false);
            }

            listAndroidVendaDetalhes.add(avc);

            cursor.moveToNext();
            //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
        }

        return (listAndroidVendaDetalhes);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_historico_vendas, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        initDateTimeData();
        this.cDataInicio.set(ano, mes, dia);
        this.cDataFim.set(ano, mes, dia);

        switch (item.getItemId()) {

            case R.id.action_limpar_historico:
                //##############################################
                AlertDialog.Builder alertDialogBuilder1 = new AlertDialog.Builder(HistoricoVendasActivity.this);
                alertDialogBuilder1.setTitle("LIMPAR HISTÓRICO");

                alertDialogBuilder1
                        .setMessage(
                                "Gostaria de limpar o historico?")
                        .setCancelable(false)
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        limparHistorico();
                                        frag.atualizarLista();

                                    }
                                })
                        .setNegativeButton("Não",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // se n�o for precionado ele apenas termina o
                                        // dialog
                                        // e fecha a janelinha
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog1 = alertDialogBuilder1.create();

                alertDialog1.show();
                //##############################################

                //limparHistorico();
                break;

            case R.id.action_reenviar_tudo:
                //##############################################
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(HistoricoVendasActivity.this);
                alertDialogBuilder.setTitle("REENVIAR TODAS AS VENDA DO HISTÓRICO");

                alertDialogBuilder
                        .setMessage(
                                "Gostaria de reenviar todas as vendas do historico?")
                        .setCancelable(false)
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        new ReenviarTodoHistorico().execute();

                                    }
                                })
                        .setNegativeButton("Não",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        // se n�o for precionado ele apenas termina o
                                        // dialog
                                        // e fecha a janelinha
                                        dialog.cancel();
                                    }
                                });

                AlertDialog alertDialog = alertDialogBuilder.create();

                alertDialog.show();
                //##############################################
                //new ReenviarTodoHistorico().execute();
                break;

            case R.id.action_data_inicial_historico:
                //##############################################
                DatePickerDialog datePickerInicioDialog = new DatePickerDialog().newInstance(
                        cDataInicio.get(Calendar.YEAR),
                        cDataInicio.get(Calendar.MONTH),
                        cDataInicio.get(Calendar.DAY_OF_MONTH)
                );

                List<Calendar> dayListInicio = new LinkedList<>();
                Calendar[] daysArrayInicio;
                Calendar cAuxInicio = Calendar.getInstance();

                datePickerInicioDialog.setOnCancelListener(datePickerInicioDialog);
                datePickerInicioDialog.show(getFragmentManager(), "Informe a data");
                break;


            case R.id.action_data_final_historico:
                //##############################################
                DatePickerDialog datePickerFimDialog = new DatePickerDialog().newInstance(
                        this,
                        cDataFim.get(Calendar.YEAR),
                        cDataFim.get(Calendar.MONTH),
                        cDataFim.get(Calendar.DAY_OF_MONTH)
                );

                List<Calendar> dayListFim = new LinkedList<>();
                Calendar[] daysArrayFim;
                Calendar cAuxFim = Calendar.getInstance();

                datePickerFimDialog.setOnCancelListener(this);
                datePickerFimDialog.show(getFragmentManager(), "Informe a data");

                break;
        }

        //RETORNA A PAGINA ANTERIOR COM A SETA DO PAINEL DO TOPO
        if (id == android.R.id.home) {
            finish();
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }

    private void initDateTimeData(){
        if (ano == 0){
            Calendar c = Calendar.getInstance();
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    private void limparHistorico(){

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                    "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado  " +
                                    "FROM android_venda_cabecalho WHERE enviado LIKE '1'", null);

            if (cursor.moveToFirst()){

                for (int i=0; i< cursor.getCount(); i++){

                    Cursor cursorDetalhes = db
                            .rawQuery(
                                    "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id " +
                                            "FROM android_venda_detalhe where android_venda_cabecalho_id like '"+ String.valueOf(cursor.getInt(0)) +"'", null); // where android_venda_cabecalho_id like '"+ id +"'

                    if (cursorDetalhes.moveToFirst()) {
                        for (int j=0; j< cursorDetalhes.getCount(); j++){
                            db.delete("android_venda_detalhe", "_id=?", new String[]{String.valueOf(cursorDetalhes.getInt(0))});
                            cursorDetalhes.moveToNext();
                        }
                    }

                    db.delete("android_venda_cabecalho", "_id=?", new String[]{String.valueOf(cursor.getInt(0))});
                    cursor.moveToNext();
                }
            }

            Toast.makeText(HistoricoVendasActivity.this, "O histórico foi limpo com sucesso!", Toast.LENGTH_SHORT).show();

        }catch (Exception e){
            Toast.makeText(HistoricoVendasActivity.this, "Erro ao limpar o histórico!", Toast.LENGTH_SHORT).show();
        }
    }

    private void enviarAndroidVendaCabecalho(){

        idVendaCabecalho = 0;

        //####################################################################
        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado " +
                                "FROM android_venda_cabecalho where venda_aprovada like '1'AND enviado like '1'", null);

        if (cursor.moveToFirst()){

            for (int i=0; i< cursor.getCount(); i++){
                AndroidVendaCabecalho androidVendaCabecalho = new AndroidVendaCabecalho();

                //androidVendaCabecalho.setCodVenda(cursor.getLong(1));
                androidVendaCabecalho.setObservacao(cursor.getString(2));
                androidVendaCabecalho.setCliente(cursor.getLong(3));
                androidVendaCabecalho.setEntrada(cursor.getDouble(4));
                androidVendaCabecalho.setJuros(cursor.getDouble(5));
                androidVendaCabecalho.setValorParcial(cursor.getDouble(6));
                androidVendaCabecalho.setValorDesconto(cursor.getDouble(7));
                androidVendaCabecalho.setValorTotal(cursor.getDouble(8));

                //Log.d("IMPORTANTE", "DATA ATUAL ++: " + cursor.getLong(9));
                try {
                    Date dt = formatDataHoraBRA.parse(cursor.getString(9));
                    //androidVendaCabecalho.setDataVenda(cursor.getString(9));
                    androidVendaCabecalho.setDataVenda(formatSoap.format(dt));
                }catch (Exception e){
                    androidVendaCabecalho.setDataVenda(dateFormat.format(dataAtual()));
                }

                try {
                    androidVendaCabecalho.setDataPrimeiroVencimento(cursor.getString(10));
                }catch (Exception e){
                    //androidVendaCabecalho.setDataPrimeiroVencimento(dataAtual().toString());
                }

                androidVendaCabecalho.setUsuario(cursor.getLong(11));
                androidVendaCabecalho.setFormaPagamento(cursor.getLong(12));
                //Log.d("IMPORTANTE", "EMPRESA ++: " + cursor.getLong(13));
                androidVendaCabecalho.setEmpresa(cursor.getLong(13));

                AndroidVendaCabecalhoDAO androidVendaCabecalhoDAO = new AndroidVendaCabecalhoDAO();

                boolean result = androidVendaCabecalhoDAO.inserirAndroidVendaCabecalho(androidVendaCabecalho, Configuracao.getCnpj());

                if (result){

                    ultimoIdVendaCabecalho = androidVendaCabecalhoDAO.buscarUltimoIdVendaCabecalho(Configuracao.getCnpj());
                    idVendaCabecalho = cursor.getInt(0);

                    //ContentValues vcCabecalho = new ContentValues();

                    //db.delete("android_venda_cabecalho", "_id=?", new String[]{String.valueOf(cursor.getInt(0))});

                    enviarAndroidVendaDetalhe(cursor.getInt(0));
                }

                cursor.moveToNext();
            }
        }


        //###########################################################################################
    }

    private void enviarAndroidVendaDetalhe(int id){

        //####################################################################
        Cursor cursorDetalhes = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '"+ id +"'", null); // where android_venda_cabecalho_id like '"+ id +"'

        if (cursorDetalhes.moveToFirst()) {

            for (int i = 0; i < cursorDetalhes.getCount(); i++) {
                AndroidVendaDetalhe androidVendaDetalhe = new AndroidVendaDetalhe();

                androidVendaDetalhe.setVendaCabecalho(ultimoIdVendaCabecalho);
                androidVendaDetalhe.setProduto(cursorDetalhes.getLong(2));
                androidVendaDetalhe.setQuantidade(cursorDetalhes.getDouble(3));
                androidVendaDetalhe.setValorParcial((cursorDetalhes.getDouble(4) / cursorDetalhes.getDouble(3)));
                androidVendaDetalhe.setValorDesconto(cursorDetalhes.getDouble(5));
                androidVendaDetalhe.setValorTotal(cursorDetalhes.getDouble(6));
                androidVendaDetalhe.setEmpresa(cursorDetalhes.getLong(7));

                AndroidVendaDetalheDAO androidVendaDetalheDAO = new AndroidVendaDetalheDAO();

                boolean result = androidVendaDetalheDAO.inserirAndroidVendaDetalhe(androidVendaDetalhe, Configuracao.getCnpj());
                Log.d("CaculeCompreFacil", result + "");

                if (result) {

                    //db.delete("android_venda_detalhe", "_id=?", new String[]{String.valueOf(cursorDetalhes.getInt(0))});

                }
                cursorDetalhes.moveToNext();
            }
        }
        //###########################################################################################
    }

    private Date dataAtual() {

        Date hoje = new Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }

    public class ReenviarTodoHistorico extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(HistoricoVendasActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Enviando vendas, favor aguardar....");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean resultado = false;

            try {

                enviarAndroidVendaCabecalho();
                resultado = true;

                //####################################################################
            }catch (Exception e){
                resultado = false;
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true){
                Toast.makeText(HistoricoVendasActivity.this, "Reenvio de todas as vendas do histórico comcluido com sucesso!!!", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(HistoricoVendasActivity.this, "Falha no reenvio de todas as vendas do histórico!!!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();
        }
    }

}
