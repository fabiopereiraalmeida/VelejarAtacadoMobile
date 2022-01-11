package br.com.grupocaravela.velejar.atacadomobile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.CaixaFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidCaixa;

public class CaixaActivity extends ActionBarActivity {

    private Toolbar mainToolbarTop;

    private Cursor cursor;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;
    private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28
    private AndroidCaixa androidCaixa = null;

    private TextView tvTotal, tvQtdItens, tvNomeCliente, tvValor, tvDataRecebido;

    private ImageLoader mImageLoader;

    private CaixaFragment frag;

    private Intent intent;

    private int idAndroidVendaCabecalho;

    private boolean finalLista = false;
    private int posicao = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caixa);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Toobar superior
        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Caixa!");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvQtdItens = (TextView) findViewById(R.id.tv_caixa_qtd_recebimentos);
        tvTotal = (TextView) findViewById(R.id.tv_venda_valor_parcial);
        tvValor = (TextView) findViewById(R.id.tv_caixa_valor);
        tvDataRecebido = (TextView) findViewById(R.id.tv_caixa_data_recebimento);
        tvNomeCliente = (TextView) findViewById(R.id.tv_caixa_nome_cliente);

        androidCaixa = new AndroidCaixa(); //Cria novo

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        finalLista = false;
        posicao = 0;
        idAndroidVendaCabecalho = 0;

        intent = getIntent();

        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                        //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(CaixaActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        listarAndroidCaixa();
        atualizarValores();

        //verificaAcao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
/*
        Intent it = null;

        switch (item.getItemId()) {

            case R.id.action_novo_cliente:
                it = new Intent(this, AddClientesActivity.class);

                startActivityForResult(it, 112);

                break;

            case R.id.action_add_produto:
                it = new Intent(this, AddProdutosActivity.class);
                startActivityForResult(it, 113);
                break;

            case R.id.action_finalizar:

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("FINALIZAR VENDA");

                alertDialogBuilder
                        .setMessage(
                                "Gostaria de finalizar a venda atual?")
                        .setCancelable(false)
                        .setPositiveButton("Sim",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {

                                        finalizarVenda();

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
                break;
        }

*/
        //RETORNA A PAGINA ANTERIOR COM A SETA DO PAINEL DO TOPO
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }
/*
    public void atualizaLista() {

        db = openOrCreateDatabase("db_grupo_caravela.db", Context.MODE_PRIVATE,
                null);

        Cursor cursor = db
                .rawQuery(
                        "SELECT _id, quantidade, valor_desconto, valor_parcial, valor_total, android_venda_cabecalho_id, produto_id " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);


    }
*/
    public List<AndroidCaixa> getSetAndroidCaixaList(int qtd) {

        Log.i("CAIXA", "PASSEI AKI 01");

        List<AndroidCaixa> listAux = new ArrayList<>();
        ArrayList<AndroidCaixa> listAndroidCaixa = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, data_recebimento, data_transmissao, valor, android_venda_cabecalho_id, venda_cabecalho_id, " +
                                "usuario_id, cliente_id FROM android_caixa", null);

        cursor.moveToFirst();

        int tamanhoListaAndroidContasReceber = cursor.getCount();

        for (int i = 0; i < tamanhoListaAndroidContasReceber; i++) {

            AndroidCaixa ac = new AndroidCaixa();

            ac.setId(cursor.getLong(0));
            try{
                String d = cursor.getString(1);
                ac.setDataRecebimento(d);

            }catch (Exception e){
                ac.setDataRecebimento(null);
            }
            ac.setDataTransmissao(null);
            ac.setValor(cursor.getDouble(3));
            ac.setAndroidVendaCabecalho(cursor.getLong(4));
            ac.setVendaCabecalho(cursor.getLong(5));
            ac.setUsuario(cursor.getLong(6));
            ac.setCliente(cursor.getLong(7));

            listAndroidCaixa.add(ac);

            cursor.moveToNext();
        }

        Log.i("TAMANHO", "TAMANHO DO LIST" + listAndroidCaixa.size() + " ");
        if (finalLista == false) {

            for (int y = 0; y < qtd; y++) {
                if (posicao == listAndroidCaixa.size()) {
                    //posicao = 1;
                    finalLista = true;
                } else {
                    listAux.add(listAndroidCaixa.get(posicao));
                    posicao++;
                    //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                }

            }
        }

        return (listAux);
    }

    public List<AndroidCaixa> getSetAndroidCaixaListCompleta() {
        ArrayList<AndroidCaixa> listAndroidCaixa = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, data_recebimento, data_transmissao, valor, android_venda_cabecalho_id, venda_cabecalho_id, " +
                                "usuario_id, cliente_id FROM android_caixa", null);

        cursor.moveToFirst();

        int tamanhoListaAndroidCaixa = cursor.getCount();

        for (int i = 0; i < tamanhoListaAndroidCaixa; i++) {

            AndroidCaixa ac = new AndroidCaixa();

            ac.setId(cursor.getLong(0));
            try{
                String d = cursor.getString(1);
                ac.setDataRecebimento(d);

            }catch (Exception e){
                ac.setDataRecebimento(null);
            }
            ac.setDataTransmissao(null);
            ac.setValor(cursor.getDouble(3));
            ac.setAndroidVendaCabecalho(cursor.getLong(4));
            ac.setVendaCabecalho(cursor.getLong(5));
            ac.setUsuario(cursor.getLong(6));
            ac.setCliente(cursor.getLong(7));

            listAndroidCaixa.add(ac);

            cursor.moveToNext();
        }

        return (listAndroidCaixa);
    }

    private void listarAndroidCaixa() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                frag = (CaixaFragment) getSupportFragmentManager().findFragmentByTag("proFrag");
                if (frag == null) {
                    frag = new CaixaFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_itens_caixa, frag, "proFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }


    public void atualizarValores(){

        int quant = 0;
        Double total = 0.0;

        cursor = db
                .rawQuery(
                        "SELECT _id, data_recebimento, data_transmissao, valor, android_venda_cabecalho_id, venda_cabecalho_id, " +
                                "usuario_id FROM android_caixa", null);

        cursor.moveToFirst();

        quant = cursor.getCount();

        for (int i = 0; i < cursor.getCount(); i++){

            total = total + cursor.getDouble(3);

            cursor.moveToNext();
        }

        tvQtdItens.setText(String.valueOf(quant));
        tvTotal.setText("R$ " + total.toString());

    }

}
