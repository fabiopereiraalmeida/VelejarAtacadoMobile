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
import android.view.Menu;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.AddProdutosFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

public class AddProdutosActivity extends ActionBarActivity {

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
        setContentView(R.layout.activity_produtos);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Lista de produtos");
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

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(AddProdutosActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        listarProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
/*
        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
*/
        if (id == android.R.id.home) {
            finish();
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }

    public List<Produto> getSetProdutoListNome(String nome) {
        List<Produto> listAux = new ArrayList<>();
        List<Produto> listProdutos = new ArrayList<>();

        try {
/*
            if (Configuracao.getProdutoSemEstoque()){
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' " +
                                        "OR codigo LIKE '%" + nome + "%' OR codigo_ref LIKE '%" + nome + "%' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }else{
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' AND estoque > '0'" +
                                        "OR codigo LIKE '%" + nome + "%' AND estoque > '0' OR codigo_ref LIKE '%" + nome + "%' AND estoque > '0' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }
 */
            if (Configuracao.getProdutoSemEstoque()){
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' " +
                                        "OR codigo LIKE '%" + nome + "%' OR codigo_ref LIKE '%" + nome + "%' ORDER BY nome COLLATE NOCASE ASC", null);
            }else{
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' AND estoque > '0'" +
                                        "OR codigo LIKE '%" + nome + "%' AND estoque > '0' OR codigo_ref LIKE '%" + nome + "%' AND estoque > '0' ORDER BY nome COLLATE NOCASE ASC", null);
            }

            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getCount(); i++) {

                    Produto p = new Produto();
/*
                    p.setId(cursor.getLong(0));
                    p.setNome(cursor.getString(2));
                    p.setEstoque(cursor.getDouble(3));
                    p.setExpositor(cursor.getDouble(4));
                    p.setValorDesejavelVenda(cursor.getDouble(5));
                    p.setCodigo(cursor.getString(1));
                    p.setValorMinimoVenda(cursor.getDouble(6));
                    p.setImagem(cursor.getBlob(12));
                    p.setCodigo_ref(cursor.getString(13));
*/
                    p.setId(cursor.getLong(0));
                    p.setCodigo(cursor.getString(1));
                    p.setNome(cursor.getString(2));
                    p.setEstoque(cursor.getDouble(3));
                    p.setExpositor(cursor.getDouble(4));
                    p.setValorDesejavelVenda(cursor.getDouble(5));
                    p.setValorMinimoVenda(cursor.getDouble(6));
                    p.setCategoria(cursor.getLong(7));
                    p.setUnidade(cursor.getLong(8));
                    if (cursor.getInt(9) == 1){
                        p.setAtivo(true);
                    }else{
                        p.setAtivo(false);
                    }
                    p.setPeso(cursor.getDouble(10));
                    p.setEmpresa(cursor.getLong(11));
                    p.setImagem(cursor.getBlob(12));
                    p.setCodigo_ref(cursor.getString(13));
                    p.setMarca(cursor.getLong(14));
                    //listProdutos.add(p);
                    listAux.add(p);

                    cursor.moveToNext();
                }
/*
            if (finalLista == false) {

                for (int y = 0; y < 7; y++) {
                    if (posicao == listProdutos.size()) {
                        //posicao = 1;
                        finalLista = true;
                    } else {
                        listAux.add(listProdutos.get(posicao));
                        posicao++;
                    }
                }
            }
            */
            }

        }catch (Exception e){

        }

        return (listAux);
    }

    public List<Produto> getSetProdutoList(int qtd) {
        List<Produto> listAux = new ArrayList<>();
        ArrayList<Produto> listProduto = new ArrayList<>();

        try {

            if (Configuracao.getProdutoSemEstoque()){
                /*
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
                                        */
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto ORDER BY nome COLLATE NOCASE ASC", null);

            }else {
                /*
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE estoque > '0' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
                                        */
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE estoque > '0' ORDER BY nome COLLATE NOCASE ASC", null);

            }
        }catch (Exception e){

        }
        cursor.moveToFirst();

        int tamanhoListaProduto = cursor.getCount();

        for (int i = 0; i < tamanhoListaProduto; i++) {

            Produto p = new Produto();

            p.setId(cursor.getLong(0));
            p.setNome(cursor.getString(2));
            p.setEstoque(cursor.getDouble(3));
            p.setExpositor(cursor.getDouble(4));
            p.setValorDesejavelVenda(cursor.getDouble(5));
            p.setCodigo(cursor.getString(1));
            p.setValorMinimoVenda(cursor.getDouble(6));
            p.setCategoria(cursor.getLong(7));
            p.setUnidade(cursor.getLong(8));
            p.setImagem(cursor.getBlob(12));
            p.setCodigo_ref(cursor.getString(13));
            p.setMarca(cursor.getLong(14));

            listProduto.add(p);

            cursor.moveToNext();
            //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
        }

        Log.i("TAMANHO", "TAMANHO DO LISTPRODUTO " + listProduto.size() + " ");
        if (finalLista == false) {

            for (int y = 0; y < qtd; y++) {
                if (posicao == listProduto.size()) {
                    //posicao = 1;
                    finalLista = true;
                } else {
                    listAux.add(listProduto.get(posicao));
                    posicao++;
                    //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                }

            }
        }
        //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
        //Log.i("POSICAO", "POSICAO " + posicao + " ");
        return (listAux);
    }

    private void listarProdutos() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                AddProdutosFragment frag = (AddProdutosFragment) getSupportFragmentManager().findFragmentByTag("proFrag");
                if (frag == null) {
                    frag = new AddProdutosFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_produtos, frag, "proFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageLoader.clearMemoryCache();
        mImageLoader.stop();
    }
}
