package br.com.grupocaravela.velejar.atacadomobile;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.AddFormaPagamentoFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.FormaPagamento;

public class AddFormaPagamentoActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private int posicao = 0;
    private boolean finalLista = false;
    private Toolbar mainToolbarTop;

    private ImageLoader mImageLoader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_forma_pagamento);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Formas de pagamentos");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
/*
        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                        //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(AddClientesActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################
*/
        listarFormaPagamento();
    }

    public List<FormaPagamento> getSetFormaPagamentoList(int qtd) {
        List<FormaPagamento> listAux = new ArrayList<>();
        ArrayList<FormaPagamento> listFormaPagamento = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, juros, nome, numero_dias, numero_parcelas, valor_minimo, empresa_id FROM forma_pagamento ORDER BY nome ASC", null);

        cursor.moveToFirst();

        int tamanhoListaFormaPagamento = cursor.getCount();

        for (int i = 0; i < tamanhoListaFormaPagamento; i++) {

            FormaPagamento c = new FormaPagamento();

            c.setId(cursor.getLong(0));
            c.setNome(cursor.getString(2));
            c.setNumeroParcelas(cursor.getInt(4));
            c.setEmpresa(cursor.getLong(6));
            c.setJuros(cursor.getDouble(1));
            c.setNumeroDias(cursor.getInt(3));
            c.setValorMinimo(cursor.getDouble(5));

            listFormaPagamento.add(c);

            cursor.moveToNext();
        }

        Log.i("TAMANHO", "TAMANHO DO LISTFORMAPAGAMENTO " + listFormaPagamento.size() + " ");
        if (finalLista == false) {

            for (int y = 0; y < qtd; y++) {
                if (posicao == listFormaPagamento.size()) {
                    //posicao = 1;
                    finalLista = true;
                } else {
                    listAux.add(listFormaPagamento.get(posicao));
                    posicao++;
                    //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                }

            }
        }
        //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
        //Log.i("POSICAO", "POSICAO " + posicao + " ");
        return (listAux);
    }

    private void listarFormaPagamento() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                AddFormaPagamentoFragment frag = (AddFormaPagamentoFragment) getSupportFragmentManager().findFragmentByTag("cliFrag");
                if (frag == null) {
                    frag = new AddFormaPagamentoFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_forma_pagamento, frag, "cliFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mImageLoader.clearMemoryCache();
        //mImageLoader.stop();
    }

}
