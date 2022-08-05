package br.com.grupocaravela.velejar.atacadomobile;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.github.clans.fab.FloatingActionMenu;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeader;
import com.mikepenz.materialdrawer.accountswitcher.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SectionDrawerItem;
import com.mikepenz.materialdrawer.model.SwitchDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.OnCheckedChangeListener;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.VendasFinalizadasFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Usuario;


public class MenuPrincipalActivity extends ActionBarActivity {

    private static String TAG = "LOG";
    private Toolbar mainToolbarTop;
    //private Toolbar mainToolbarBottom;
    private Drawer navigationDrawerLeft;// = new DrawerBuilder().withActivity(this).build();;
    //private Drawer navigationDrawerRigth;
    private AccountHeader heraderNavigationLeft;
    /*
    private OnCheckedChangeListener mOnCheckedChangeListener = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {
            Toast.makeText(MenuPrincipalActivity.this, "onCheckedChanged: " + (b ? "treu" : "false"), Toast.LENGTH_SHORT).show();
        }
    };
    */
/*
    private OnCheckedChangeListener mOnCheckedChangeListener1 = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {

            //ConfiguracaoServidor.setIpLocal(b);
            //Toast.makeText(MenuPrincipalActivity.this, "Servidor ip definido para: " + ConfiguracaoServidor.getIpServidor(), Toast.LENGTH_SHORT).show();
        }
    };
*/


    private OnCheckedChangeListener mOnCheckedChangeListener2 = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {

            Configuracao.setProdutoSemEstoque(b);

            if (b){
                Toast.makeText(MenuPrincipalActivity.this, "Serão exibidos os produtos sem estoque", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MenuPrincipalActivity.this, "Serão exibidos os produtos somente com estoque", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private OnCheckedChangeListener mOnCheckedChangeListener3 = new OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(IDrawerItem iDrawerItem, CompoundButton compoundButton, boolean b) {

            Configuracao.setVisualizarCards(b);
            if (b){
                Toast.makeText(MenuPrincipalActivity.this, "Produtos serão visualizados em cards", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(MenuPrincipalActivity.this, "Produtos serão visualizados em lista", Toast.LENGTH_SHORT).show();
            }

        }
    };

    private FloatingActionMenu fab;

    private ImageLoader mImageLoader;

    public SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues contentValues;
    private Cursor cursor;

    private VendasFinalizadasFragment frag;

    private Usuario usuario;

    private int posicao = 0;
    private boolean finalLista = false;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    //private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28

    public MenuPrincipalActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_principal);

        ActivityCompat.requestPermissions(MenuPrincipalActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        posicao = 0;
        finalLista = false;

        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################
        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(MenuPrincipalActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);
        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        //######################## INICIO CONFIGURAÇÃO DO TOOBAR ######################################
        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Velejar ATACADO MOBILE");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setSubtitle("Vendas realizadas");
        mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        //######################## FIM CONFIGURAÇÃO DO TOOBAR ######################################

        //############### INICIO IMPLEMENTAÇÃO DO NAVIGATION DRAWER ESQUERDO ######################
        heraderNavigationLeft = new AccountHeaderBuilder()
                .withActivity(this)
                .withCompactStyle(false)
                .withSavedInstance(savedInstanceState)
                .withThreeSmallProfileImages(false)
                .withHeaderBackground(R.drawable.logo_wallpaper3)
                .addProfiles(
                        new ProfileDrawerItem()
                                .withName(buscaNomeUsuario())
                                .withEmail("Crédito R$ " + String.format("%.2f", buscaCreditoUsuario()))
                                //.withEmail("usuario@dominio.com")
                                .withIcon(getResources().getDrawable(R.drawable.sem_foto_perfil))
                )
                .build();

        navigationDrawerLeft = new DrawerBuilder()
                .withActivity(MenuPrincipalActivity.this)
                .withToolbar(mainToolbarTop)
                .withDisplayBelowToolbar(true)
                .withActionBarDrawerToggleAnimated(true)
                .withDrawerGravity(Gravity.LEFT)
                .withSavedInstance(savedInstanceState)
                .withSelectedItem(0)
                .withAccountHeader(heraderNavigationLeft)
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {

                    @Override
                    public boolean onItemClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {

                        Intent it = null;

                        switch (i) {
                            case 0:
                                //Toast.makeText(MenuPrincipalActivity.this, "Nova Venda! ", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, VendaActivity.class);
                                break;

                            case 1:
                                //Toast.makeText(MenuPrincipalActivity.this, "Notas a receber! ", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, ContasReceberActivity.class);
                                break;

                            case 2:
                                //Toast.makeText(MenuPrincipalActivity.this, "Caixa", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, CaixaActivity.class);
                                break;

                            case 4:
                                //Toast.makeText(MenuPrincipalActivity.this, "Clientes! ", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, ClientesActivity.class);
                                break;

                            case 5:
                                //Toast.makeText(MenuPrincipalActivity.this, "Produtos! ", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, ProdutosActivity.class);
                                break;

                            case 7:
                                it = new Intent(MenuPrincipalActivity.this, AtualizarActivity.class);
                                break;

                            case 8:
                                //Toast.makeText(MenuPrincipalActivity.this, "Historico! ", Toast.LENGTH_SHORT).show();
                                it = new Intent(MenuPrincipalActivity.this, HistoricoVendasActivity.class);
                                break;
/*
                            case 9:
                                Toast.makeText(MenuPrincipalActivity.this, "Sistema! ", Toast.LENGTH_SHORT).show();
                                //it = new Intent(MenuPrincipalActivity.this, ProdutosActivity.class);
                                break;
*/

                        }

                        if (it != null) {
                            //startActivity(it);
                            startActivityForResult(it, 0);
                        }

                        return false;
                    }
                }).withOnDrawerItemLongClickListener(new Drawer.OnDrawerItemLongClickListener() {

                    @Override
                    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l, IDrawerItem iDrawerItem) {
                        Toast.makeText(MenuPrincipalActivity.this, "onItemLongClick: " + i, Toast.LENGTH_SHORT).show();
                        return false;
                    }
                }).build();

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Nova venda").withIcon(R.drawable.venda_detalhada_24));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Notas a receber").withIcon(R.drawable.contas_receber_24));
        //navigationDrawerLeft.addItem(new DividerDrawerItem());

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Caixa").withIcon(R.drawable.caixa_24));

        navigationDrawerLeft.addItem(new DividerDrawerItem());

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Clientes").withIcon(R.drawable.clientes_24));

        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Produtos").withIcon(R.drawable.produtos_24));

        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Transferências"));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Atualizações").withIcon(R.drawable.nuvem_upload_24));
        navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Histórico").withIcon(R.drawable.historico_24));
        //navigationDrawerLeft.addItem(new PrimaryDrawerItem().withName("Sistema").withIcon(R.drawable.empresa_24x24));

        navigationDrawerLeft.addItem(new SectionDrawerItem().withName("Configurações"));
        //navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Utilizar servidor Local").withChecked(false).withOnCheckedChangeListener(mOnCheckedChangeListener1));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Visualizar produtos sem estoque").withChecked(Configuracao.getProdutoSemEstoque()).withOnCheckedChangeListener(mOnCheckedChangeListener2));
        navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Visualiza CardView nos produtos").withChecked(Configuracao.getVisualizarCards()).withOnCheckedChangeListener(mOnCheckedChangeListener3));


        //navigationDrawerLeft.addItem(new SwitchDrawerItem().withName("Receber Promoções").withChecked(true).withOnCheckedChangeListener(mOnCheckedChangeListener));
        //############### FIM IMPLEMENTAÇÃO DO NAVIGATION DRAWER ESQUERDO ######################

        fab = (FloatingActionMenu) findViewById(R.id.fab_menu);

        listarAndroidVendaCabecalho();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;    //Quando retorna "true", não continua o processamento
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        Intent it = null;

        switch (item.getItemId()) {

            case R.id.action_zerar_banco:

                //Creating an alert dialog to logout
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
                alertDialogBuilder.setMessage("ATENÇÃO!!! Está opçõa irá apagar tudas as informações do app, inclusive vendas não transmitidas. Deseja continuar?");
                alertDialogBuilder.setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                zerarBanco();
                                //atualiza activity
                                finish();
                                startActivity(getIntent());
                            }
                        });

                alertDialogBuilder.setNegativeButton("Não",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                            }
                        });

                //Showing the alert dialog
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

/*
                zerarBanco();
                //atualiza activity
                finish();
                startActivity(getIntent());
 */
                break;

            case R.id.action_sair:
                android.os.Process.killProcess(android.os.Process.myPid());
                break;
        }

        //return true;

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (navigationDrawerLeft.isDrawerOpen()) {
            navigationDrawerLeft.closeDrawer();
        } else if (fab.isOpened()) {
            fab.close(true);
            //} else if (navigationDrawerRigth.isDrawerOpen()) {
            //   navigationDrawerRigth.closeDrawer();
        } else {
            super.onBackPressed();
        }

        //super.onBackPressed();
    }

    private void listarAndroidVendaCabecalho() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                frag = (VendasFinalizadasFragment) getSupportFragmentManager().findFragmentByTag("mainFrag");
                if (frag == null) {
                    frag = new VendasFinalizadasFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container, frag, "mainFrag");
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

    private void zerarBanco(){
        dbHelper.deleteBancoCompleto(db); //Apagando todas as tabelas do banco //Não apaga as vendas
        Log.i("Banco", "As tabelas do banco foram excluidas com sucesso!");
        dbHelper.onCreate(db); //Cria todas as tabelas
        Log.i("Banco", "As tabelas do banco foram criadas com sucesso!");
    }

    public List<AndroidVendaCabecalho> getSetAndroidVendaCabecalhoList(int qtd) {

        List<AndroidVendaCabecalho> listAux = new ArrayList<>();

     try {

        ArrayList<AndroidVendaCabecalho> listAndroidVendaCabecalho = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado " +
                                "FROM android_venda_cabecalho WHERE enviado LIKE '0'", null);

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
            //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
        }

        //Log.i("TAMANHO", "TAMANHO DO LISTANDROIDVENDACABECALHO " + listAndroidVendaCabecalho.size() + " ");
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
        //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
        //Log.i("POSICAO", "POSICAO " + posicao + " ");

        }catch (Exception e){
            //zerarBanco();
        }

        return (listAux);
    }

    public List<AndroidVendaCabecalho> getSetAndroidVendaDetalheListCompleta() {
        ArrayList<AndroidVendaCabecalho> listAndroidVendaDetalhes = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado " +
                                "FROM android_venda_cabecalho WHERE enviado LIKE '0'", null);

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
    public void onActivityResult(int codigo, int resultado, Intent intent) {
        //Toast.makeText(MenuPrincipalActivity.this, "VOLTEI", Toast.LENGTH_SHORT).show();
        frag.atualizarLista();
        //listarAndroidVendaCabecalho();
    }

    private String buscaNomeUsuario(){
        String retorno = null;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getString(2);
        }catch (Exception e){
            retorno = "Não encontrado";
        }

        return retorno;
    }

    private Double buscaCreditoUsuario(){
        Double retorno = 0.0;

        cursor = db
                .rawQuery(
                        "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);
        cursor.moveToFirst();

        int id = cursor.getInt(1);

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, data, empresa_id, valor, usuario_id, venda_detalhe_id FROM credito_usuario where usuario_id like '" + id + "'", null);

            cursor.moveToFirst();

            int tamanhoLista = cursor.getCount();

            for (int i = 0; i < tamanhoLista; i++) {
                retorno = retorno + cursor.getDouble(3);
            }
        }catch (Exception e){
            retorno = 0.0;
        }

        return retorno;
    }



}


