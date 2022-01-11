package br.com.grupocaravela.velejar.atacadomobile;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Locale;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.VendaFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.FormaPagamento;
import br.com.grupocaravela.velejar.atacadomobile.objeto.VendaCabecalho;
import br.com.grupocaravela.velejar.atacadomobile.objeto.VendaDetalhe;

public class VendaActivity extends ActionBarActivity {

    private Toolbar mainToolbarTop;

    private Cursor cursor;
    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues vcContentValues, vdContentValues, produtoContentValues;

    private boolean formaPagamentoObrigatoria = false;

    private Cliente cliente = null;
    private FormaPagamento formaPagamento = null;
    private VendaCabecalho vendaCabecalho = null;
    private VendaDetalhe vendaDetalhe = null;

    private Double localizacao;
    //private List<VendaDetalhe> listVendaDetalhes = null;

    private TextView tvNomeCliente, tvTotal, tvQtdItens, tvFormaPagamento, tvNomeCidade;

    private ImageLoader mImageLoader;

    private VendaFragment frag;

    private AlertDialog alerta;

    private Intent intent;

    private int idAndroidVendaCabecalho;

    private boolean finalLista = false;
    private boolean medicao = true;
    private int posicao = 0;

    private Menu mn = null;

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //yyyy-MM-dd'T'HH:mm:ss'Z'
    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //yyyy-MM-dd'T'HH:mm:ss'Z'
    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_venda);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        //Toobar superior
        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Venda!");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvNomeCliente = (TextView) findViewById(R.id.tv_venda_nome_cliente);
        tvNomeCidade = (TextView) findViewById(R.id.tv_venda_cidade_cliente);
        tvQtdItens = (TextView) findViewById(R.id.tv_venda_qtd_itens);
        tvTotal = (TextView) findViewById(R.id.tv_venda_valor_parcial);
        tvFormaPagamento = (TextView) findViewById(R.id.tv_forma_pagamento);

        vendaCabecalho = new VendaCabecalho(); //Cria novo cabeçalho

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        //contentValues = new ContentValues(); // banco

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

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(VendaActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        //listarVendasItens();

        verificaAcao();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_venda, menu);

        //menu.getItem(1).setEnabled(false);
        //menu.getItem(2).setEnabled(false);

        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
/*
        if(item.getItemId() == R.menu.menu_venda){
            // ( 1 ) add a new item
            // ( 2 ) change add to remove
        }
        else{
            // if a the new item is clicked show "Toast" message.
        }
*/
        int id = item.getItemId();

        Intent it = null;

        switch (item.getItemId()) {

            case R.id.action_novo_cliente:

                it = new Intent(this, AddClientesActivity.class);

                startActivityForResult(it, 112);

                break;

            case R.id.action_add_produto:

                if (cliente != null) {

                    //it = new Intent(this, AddProdutosActivity.class);
                    //startActivityForResult(it, 113);

                    it = new Intent(this, AddProdutosActivity.class);
                    it.putExtra("idCabecalho", idAndroidVendaCabecalho);
                    it.putExtra("localizacao", localizacao);
                    startActivityForResult(it, 113);

                    break;
                }else{
                    Toast.makeText(VendaActivity.this, "Antes de inserir os produtos, informe o cliente!!!", Toast.LENGTH_LONG).show();
                    break;
                }


            case R.id.action_forma_pagamento:

                if (formaPagamentoObrigatoria){
                    Toast.makeText(VendaActivity.this, "A forma de pagamento do cliente ja foi definida pelo sistema!", Toast.LENGTH_LONG).show();
                    break;
                }else {

                    it = new Intent(this, AddFormaPagamentoActivity.class);
                    //it.putExtra("id")
                /*
                Intent it = new Intent(getActivity(), DetalhesContaReceberActivity.class);
                it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
                startActivity(it);

                 */

                    startActivityForResult(it, 114);
                    break;
                }

            case R.id.action_finalizar:

                if (formaPagamento != null) {

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
                } else {
                    Toast.makeText(VendaActivity.this, "Antes de finalizar, informe a forma de pagamento!!!", Toast.LENGTH_LONG).show();
                    break;
                }

            case R.id.action_observacao:

                showInputDialogObservacao();
/*
                LayoutInflater li = getLayoutInflater();

                //inflamos o layout alerta.xml na view
                View view = li.inflate(R.layout.observacao_venda, null);
                //definimos para o botão do layout um clickListener
                view.findViewById(R.id.bt_salvar_configuracao).setOnClickListener(new View.OnClickListener() {
                    public void onClick(View arg0) {
                        //exibe um Toast informativo.
                        Toast.makeText(VendaActivity.this, "alerta.dismiss()", Toast.LENGTH_SHORT).show();
                        //desfaz o alerta.
                        alerta.dismiss();
                    }
                });

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Titulo");
                builder.setView(view);
                alerta = builder.create();
                alerta.show();
                */
        }


        //RETORNA A PAGINA ANTERIOR COM A SETA DO PAINEL DO TOPO
        if (id == android.R.id.home) {
            finish();
        }


        return super.onOptionsItemSelected(item);
    }

    public void showInputDialogObservacao() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Observação");

        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        alert.setView(input);

        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio do desconto########################
                        vcContentValues.clear();

                        String obs;
                        try {
                            obs = input.getText().toString();

                            vcContentValues.put("observacao", obs);

                            db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});

                        } catch (Exception e) {

                            //db.execSQL("Alter table android_venda_cabecalho Add observacao VARCHAR(250)");

                            obs = input.getText().toString();
                            //obs = "";

                            vcContentValues.put("observacao", obs);

                            db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});

                        }
                    }
                });

        alert.show();
    }

    private void baixarEstoque(){

        //Toast.makeText(VendaActivity.this, "BAIXAR ESTOQUE", Toast.LENGTH_LONG).show();
        Log.d("ESTOQUE", "BAIXAR ESTOQUE");

        produtoContentValues = new ContentValues();

        Cursor cursorVend = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);

        //Toast.makeText(VendaActivity.this, "TAMANHO DA LISTA: " + cursorVend.getCount(), Toast.LENGTH_LONG).show();
        Log.d("ESTOQUE", "TAMANHO DA LISTA: " + cursorVend.getCount());

        if (cursorVend.moveToFirst()) {

            for (int i = 0; i < cursorVend.getCount(); i++) {

                Integer idProduto = cursorVend.getInt(2);

                Cursor cursorProd = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, ativo, peso, empresa_id, imagem, codigo_ref " +
                                        "FROM produto where _id like '" + idProduto + "'", null);

                if (cursorProd.moveToFirst()) {

                    Double qtd = cursorProd.getDouble(3) - cursorVend.getDouble(3);

                    Log.d("ESTOQUE", "QTD: " + qtd);
                   //Toast.makeText(VendaActivity.this, "QTD: " + qtd, Toast.LENGTH_LONG).show();

                    produtoContentValues.clear();
                    produtoContentValues.put("estoque", qtd);
                    db.update("produto", produtoContentValues, "_id=?", new String[]{String.valueOf(cursorVend.getInt(2))});

                    Log.d("ESTOQUE", "ATUALIZEI");
                    //Toast.makeText(VendaActivity.this, "ATUALIZEI", Toast.LENGTH_LONG).show();

                }
                cursorVend.moveToNext();
            }
        }
    }

    private void devolverEstoque(){

        //Toast.makeText(VendaActivity.this, "BAIXAR ESTOQUE", Toast.LENGTH_LONG).show();
        Log.d("ESTOQUE", "DEVOLVER ESTOQUE");

        produtoContentValues = new ContentValues();

        Cursor cursorVend = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);

        //Toast.makeText(VendaActivity.this, "TAMANHO DA LISTA: " + cursorVend.getCount(), Toast.LENGTH_LONG).show();
        Log.d("ESTOQUE", "TAMANHO DA LISTA: " + cursorVend.getCount());

        if (cursorVend.moveToFirst()) {

            for (int i = 0; i < cursorVend.getCount(); i++) {

                Integer idProduto = cursorVend.getInt(2);

                Cursor cursorProd = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, ativo, peso, empresa_id, imagem, codigo_ref " +
                                        "FROM produto where _id like '" + idProduto + "'", null);

                if (cursorProd.moveToFirst()) {

                    Double qtd = cursorProd.getDouble(3) + cursorVend.getDouble(3);

                    Log.d("ESTOQUE", "QTD: " + qtd);
                    //Toast.makeText(VendaActivity.this, "QTD: " + qtd, Toast.LENGTH_LONG).show();

                    produtoContentValues.clear();
                    produtoContentValues.put("estoque", qtd);
                    db.update("produto", produtoContentValues, "_id=?", new String[]{String.valueOf(cursorVend.getInt(2))});

                    Log.d("ESTOQUE", "ATUALIZEI");
                    //Toast.makeText(VendaActivity.this, "ATUALIZEI", Toast.LENGTH_LONG).show();

                }
                cursorVend.moveToNext();
            }
        }
    }

    private void finalizarVenda(){

        vcContentValues.clear();

        vcContentValues.put("venda_aprovada", Boolean.TRUE);
        vcContentValues.put("valor_desconto", vendaCabecalho.getValorDesconto());
        vcContentValues.put("valor_parcial", vendaCabecalho.getValorParcial());
        vcContentValues.put("valor_total", vendaCabecalho.getValorTotal());

        db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});
        //db.execSQL("INSERT INTO android_venda_cabecalho VALUES (null, datetime()) ");

        baixarEstoque();

        //db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});
        Toast.makeText(VendaActivity.this, "Venda finalizada com sucesso!!!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public void onActivityResult(int codigo, int resultado, Intent intent) {
        if (resultado == 112) {


            //Toast.makeText(VendaActivity.this, "Id resposta do Cliente selecionado: " + intent.getLongExtra("idResposta", 0), Toast.LENGTH_SHORT).show();

            vcContentValues = new ContentValues();

            cursor = db
                    .rawQuery(
                            "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                    "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                    "rota_id, empresa_id, novo, alterado, forma_pagamento_id, localizacao FROM cliente WHERE _id LIKE '" + intent.getLongExtra("idResposta", 0) + "'", null);

            if (cursor.moveToFirst()) {

                try {
                    Log.d("CIDADE", "ID : " + cursor.getLong(18));
                    Cursor cursorCidade = db
                            .rawQuery(
                                    "SELECT _id, estado_id, nome, ibge FROM cidade WHERE _id LIKE '" + cursor.getLong(18) + "'", null);

                    if (cursorCidade.moveToFirst()) {
                        Log.d("CIDADE", "NOME : " + cursor.getString(2));
                        tvNomeCidade.setText(cursorCidade.getString(2));
                    }
                }catch (Exception e){
                    tvNomeCidade.setText("");
                }

                cliente = new Cliente();
                //Verificando se ja foi definido uma forma de pagament obrigatoria para o cliente selecionado
                Long f = null;
                //Log.d("IMPORTANTE", "Forma pagamento : " + cursor.getLong(25));
                try {
                    f = cursor.getLong(25);
                }catch (Exception e){
                    f = Long.valueOf(0);
                }
                //cliente.setFormaPagamento(cursor.getLong(25));

                tvNomeCliente.setText(cursor.getString(1));
                try {
                    localizacao = Double.parseDouble(cursor.getString(26));
                }catch (Exception e){
                    localizacao = 0.0;
                }

                Log.d("LOCALIZACAO", "NUMERO " + localizacao + "");

                //vcContentValues.put("data_venda", sdf.format(dataAtual()));
                vcContentValues.put("dataVenda", formatSoapHora.format(dataAtual()));
                vcContentValues.put("valor_desconto", 0.0);
                vcContentValues.put("valor_parcial", 0.0);
                vcContentValues.put("valor_total", 0.0);
                vcContentValues.put("venda_aprovada", false);
                vcContentValues.put("enviado", false);
                vcContentValues.put("cliente_id", cursor.getLong(0));

                if (cursor.getString(23).equals("1")){
                    Log.d("BOOLEAN", "ENTREI NO NOVO " + cursor.getString(23) + "");
                    vcContentValues.put("cliente_mobile_id", cursor.getLong(0));
                    Log.d("BOOLEAN", "ID NOVO CLIENTE " + cursor.getLong(0) + "");
                }

                //Log.d("BOOLEAN", "NOVO " + novo + "");
                Log.d("BOOLEAN", "NOVO " + cursor.getString(23) + "");

                if (f != 0){
                    vcContentValues.put("forma_pagamento_id", f);
                    formaPagamentoObrigatoria = true;
                    //definirFormaPagamento(f);

                    Cursor cursorPagamento = db
                            .rawQuery(
                                    "SELECT _id, juros, nome, numero_dias, numero_parcelas, observacao, " +
                                            "valor_minimo FROM forma_pagamento WHERE _id LIKE '" + f + "'", null);

                    if (cursorPagamento.moveToFirst()) {

                        formaPagamento = new FormaPagamento();

                        tvFormaPagamento.setText(cursorPagamento.getString(2));

                        //####### CALCULANDO DATA VENVIMENTO ##########
                        GregorianCalendar gcGregorian = new GregorianCalendar();
                        gcGregorian.setTime(dataAtual());

                        int qtdDias = cursorPagamento.getInt(3);

                        gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

                        //vcContentValues.put("dataPrimeiroVencimento", gcGregorian.getTime().toString());
                        vcContentValues.put("dataPrimeiroVencimento", formatSoap.format(gcGregorian.getTime()));

                        Log.d("IMPORTANTE", "DATA PRIMEIRO VENCIMENTO: " + gcGregorian.getTime().toString());
                        //###################

                        //db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});


                    }
                }else{
                    vcContentValues.put("forma_pagamento_id", 0);
                    formaPagamentoObrigatoria = false;
                }


                //Log.d("IMPORTANTE", "DATA VENDA ++: " + datetime());

                //################## INICIO PEGANDO O USUARIO LOGADO NO SISTEMA #########################
                cursor = db
                        .rawQuery(
                                "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado WHERE _id LIKE '1'", null);

                cursor.moveToFirst();

                vcContentValues.put("usuario_id", cursor.getInt(1));
                vcContentValues.put("rota_id", cursor.getInt(7));
                vcContentValues.put("empresa_id", cursor.getInt(6));

                //#################### FIM PEGANDO O USUARIO LOGADO NO SISTEMA ###########################

                db.insert("android_venda_cabecalho", null, vcContentValues); //SALVANDO A VENDA CABEÇALHO

                Cursor c = db
                        .rawQuery(
                                "SELECT _id FROM android_venda_cabecalho order by _id desc limit 1",
                                null); // Pegando id da ultima tabela criada

                if (c.moveToNext()) {
                    idAndroidVendaCabecalho = c.getInt(0);
                }

                listarVendasItens();

            }
        }

        if (resultado == 113){

            vdContentValues = new ContentValues();

            //vendaDetalhe = new VendaDetalhe();

            cursor = db
                    .rawQuery(
                            "SELECT _id, codigo, nome, estoque, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, ativo, peso, " +
                                    "empresa_id, imagem, codigo_ref FROM produto WHERE _id LIKE '" + intent.getLongExtra("idResposta", 0) + "'", null);

            cursor.moveToFirst();

            Log.i("LOCALIZACAO", "LOCALIZACAO: " + localizacao);

            if (medicao){
                Log.i("LOCALIZACAO", "MEDICAO");
                vdContentValues.put("quantidade", (intent.getDoubleExtra("quantidade", 1.0) - localizacao));
                vdContentValues.put("valor_parcial", ((intent.getDoubleExtra("quantidade", 1.0) - localizacao) * (intent.getDoubleExtra("valor", cursor.getDouble(4)))));
                vdContentValues.put("valor_total", ((intent.getDoubleExtra("quantidade", 1.0) - localizacao) * (intent.getDoubleExtra("valor", cursor.getDouble(4)))));
            }else{
                Log.i("LOCALIZACAO", "NAO MEDICAO");
                vdContentValues.put("quantidade", intent.getDoubleExtra("quantidade", 1.0));
                vdContentValues.put("valor_parcial", ((intent.getDoubleExtra("quantidade", 1.0)) * (intent.getDoubleExtra("valor", cursor.getDouble(4)))));
                vdContentValues.put("valor_total", ((intent.getDoubleExtra("quantidade", 1.0)) * (intent.getDoubleExtra("valor", cursor.getDouble(4)))));
            }

            vdContentValues.put("valor_desconto", 0.0);
            vdContentValues.put("valor_unitario", (intent.getDoubleExtra("valor", cursor.getDouble(4))));

            vdContentValues.put("android_venda_cabecalho_id", idAndroidVendaCabecalho);
            vdContentValues.put("produto_id", cursor.getInt(0));
            vdContentValues.put("empresa_id", cursor.getInt(10));

            db.insert("android_venda_detalhe", null, vdContentValues); //SALVANDO A VENDA DETALHE

            atualizarValores();

            //listarVendasItens();
            try {
                frag.atualizarLista();
            }catch (Exception e){

            }

        }

        if (resultado == 114){

            vcContentValues = new ContentValues();

            //vendaDetalhe = new VendaDetalhe();

            cursor = db
                    .rawQuery(
                            "SELECT _id, juros, nome, numero_dias, numero_parcelas, observacao, " +
                                    "valor_minimo FROM forma_pagamento WHERE _id LIKE '" + intent.getLongExtra("idResposta", 0) + "'", null);

            if (cursor.moveToFirst()) {

                formaPagamento = new FormaPagamento();

                tvFormaPagamento.setText(cursor.getString(2));

                vcContentValues.put("forma_pagamento_id", cursor.getInt(0));

                //####### CALCULANDO DATA VENVIMENTO ##########
                GregorianCalendar gcGregorian = new GregorianCalendar();
                gcGregorian.setTime(dataAtual());

                int qtdDias = cursor.getInt(3);

                gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

                //vcContentValues.put("dataPrimeiroVencimento", gcGregorian.getTime().toString());
                vcContentValues.put("dataPrimeiroVencimento", formatSoap.format(gcGregorian.getTime()));

                Log.d("IMPORTANTE", "DATA PRIMEIRO VENCIMENTO: " + gcGregorian.getTime().toString());
                //###################

                db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});


            }
        }
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
    public List<AndroidVendaDetalhe> getSetAndroidVendaDetalheList(int qtd) {
        List<AndroidVendaDetalhe> listAux = new ArrayList<>();
        ArrayList<AndroidVendaDetalhe> listAndroidVendaDetalhes = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_parcial " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);

        if (cursor.moveToFirst()) {

            int tamanhoListaAndroidVendaDetalhe = cursor.getCount();

            for (int i = 0; i < tamanhoListaAndroidVendaDetalhe; i++) {

                AndroidVendaDetalhe avd = new AndroidVendaDetalhe();

                avd.setId(cursor.getLong(0));
                avd.setQuantidade(cursor.getDouble(3));
                avd.setValorDesconto(cursor.getDouble(5));
                avd.setValorParcial(cursor.getDouble(4));
                avd.setValorTotal(cursor.getDouble(6));
                avd.setVendaCabecalho(cursor.getLong(1));
                avd.setProduto(cursor.getLong(2));
                avd.setEmpresa(cursor.getLong(7));
                avd.setValorUnitario(cursor.getDouble(8));

                listAndroidVendaDetalhes.add(avd);

                cursor.moveToNext();
                //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
            }
        }

        Log.i("TAMANHO", "TAMANHO DO LISTPRODUTO " + listAndroidVendaDetalhes.size() + " ");
        if (finalLista == false) {

            for (int y = 0; y < qtd; y++) {
                if (posicao == listAndroidVendaDetalhes.size()) {
                    //posicao = 1;
                    finalLista = true;
                } else {
                    listAux.add(listAndroidVendaDetalhes.get(posicao));
                    posicao++;
                    //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                }

            }
        }
        //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
        //Log.i("POSICAO", "POSICAO " + posicao + " ");
        return (listAux);
    }

    public List<AndroidVendaDetalhe> getSetAndroidVendaDetalheListCompleta() {
        ArrayList<AndroidVendaDetalhe> listAndroidVendaDetalhes = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);

        if (cursor.moveToFirst()) {

            int tamanhoListaAndroidVendaDetalhe = cursor.getCount();

            for (int i = 0; i < tamanhoListaAndroidVendaDetalhe; i++) {

                AndroidVendaDetalhe avd = new AndroidVendaDetalhe();

                avd.setId(cursor.getLong(0));
                avd.setQuantidade(cursor.getDouble(3));
                avd.setValorDesconto(cursor.getDouble(5));
                avd.setValorParcial(cursor.getDouble(4));
                avd.setValorTotal(cursor.getDouble(6));
                avd.setVendaCabecalho(cursor.getLong(1));
                avd.setProduto(cursor.getLong(2));
                avd.setEmpresa(cursor.getLong(7));
                avd.setValorUnitario(cursor.getDouble(8));

                listAndroidVendaDetalhes.add(avd);

                cursor.moveToNext();
                //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
            }
        }

        return (listAndroidVendaDetalhes);
    }

    private void listarVendasItens() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                frag = (VendaFragment) getSupportFragmentManager().findFragmentByTag("proFrag");
                try {
                    if (frag == null) {
                        frag = new VendaFragment();
                        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                        ft.replace(R.id.rl_fragment_container_itens_venda, frag, "proFrag");
                        ft.commit();
                    }
                }catch (Exception e){

                }
                // FRAGMENT

            }
        });
        t.start();
    }

    public void excluirItemLista(String id){
        db.delete("android_venda_detalhe", "_id=?", new String[]{id});
    }

    public void atualizarValores(){

        Double qtd = 0.0;
        Double parcial = 0.0;
        Double desconto = 0.0;
        Double total = 0.0;

        cursor = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + idAndroidVendaCabecalho + "'", null);

        if (cursor.moveToFirst()) {

            for (int i = 0; i < cursor.getCount(); i++) {

                qtd = qtd + cursor.getDouble(3);
                parcial = parcial + cursor.getDouble(4);
                desconto = desconto + cursor.getDouble(5);
                total = total + cursor.getDouble(6);

                cursor.moveToNext();
            }
        }

        tvQtdItens.setText(qtd.toString());
        tvTotal.setText("R$ " + arredontar(total).toString());

        vendaCabecalho.setValorParcial(parcial);
        vendaCabecalho.setValorTotal(total);
        vendaCabecalho.setValorDesconto(desconto);

        if (vcContentValues == null){
            vcContentValues = new ContentValues();
        }

        vcContentValues.clear();
        vcContentValues.put("venda_aprovada", Boolean.FALSE);
        vcContentValues.put("valor_desconto", vendaCabecalho.getValorDesconto());
        vcContentValues.put("valor_parcial", vendaCabecalho.getValorParcial());
        vcContentValues.put("valor_total", vendaCabecalho.getValorTotal());

        db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});

    }

    private void verificaAcao(){

        int op = intent.getIntExtra("op", 100);
        Long idCab = intent.getLongExtra("id", 0);
        idAndroidVendaCabecalho = 0;

        if (op == 101){

            cliente = new Cliente(); //Somente para a lógica de informar que ja exixte um cliente selecionado

            //Toast.makeText(VendaActivity.this, "PASSEI AKI", Toast.LENGTH_SHORT).show();

            idAndroidVendaCabecalho = Integer.parseInt(idCab.toString());

            Cursor cvc = db.rawQuery("SELECT _id, cliente_id, forma_pagamento_id FROM android_venda_cabecalho where _id like '" + idAndroidVendaCabecalho + "'", null);
            cvc.moveToFirst();

            //Toast.makeText(VendaActivity.this, "ID CLIENTE" + cvc.getInt(1), Toast.LENGTH_SHORT).show();

            cursor = db
                    .rawQuery(
                            "SELECT _id, juros, nome, numero_dias, numero_parcelas, observacao, " +
                                    "valor_minimo FROM forma_pagamento WHERE _id LIKE '" + cvc.getInt(2) + "'", null);

            if (cursor.moveToFirst()) {

                formaPagamento = new FormaPagamento();

                tvFormaPagamento.setText(cursor.getString(2));
            }

            Cursor cc = db
                    .rawQuery(
                            "SELECT _id, razaoSocial, cidade_id FROM cliente where _id like '" + cvc.getInt(1) + "'", null);

            cc.moveToFirst();

            tvNomeCliente.setText(cc.getString(1));

            try {
                Cursor cursorCidade = db
                        .rawQuery(
                                "SELECT _id, nome FROM cidade WHERE _id LIKE '" + cc.getLong(2) + "'", null);

                if (cursorCidade.moveToFirst()) {
                    tvNomeCidade.setText(cursorCidade.getString(1));
                }
            }catch (Exception e){
                tvNomeCidade.setText("");
            }

            listarVendasItens();

            atualizarValores();

            devolverEstoque();
        }
    }

    public Double arredontar(double valor) {
        Double retorno = null;
        BigDecimal a = new BigDecimal(valor);
        a = a.setScale(2, BigDecimal.ROUND_HALF_UP);
        retorno = a.doubleValue();
        return retorno;
    }

    public void onRestart() {
        super.onRestart();

        listarVendasItens();

        atualizarValores();

        try {
            frag.atualizarLista();
        }catch (Exception e){

        }

    }

    public void onResume() {
        super.onResume();

        listarVendasItens();

        atualizarValores();

        try {
            frag.atualizarLista();
        }catch (Exception e){

        }

    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }
/*
    private void definirFormaPagamento(Long id){

        vcContentValues = new ContentValues();

        //vendaDetalhe = new VendaDetalhe();

        cursor = db
                .rawQuery(
                        "SELECT _id, juros, nome, numero_dias, numero_parcelas, observacao, " +
                                "valor_minimo FROM forma_pagamento WHERE _id LIKE '" + id + "'", null);

        if (cursor.moveToFirst()) {

            formaPagamento = new FormaPagamento();

            tvFormaPagamento.setText(cursor.getString(2));

            vcContentValues.put("forma_pagamento_id", cursor.getInt(0));

            //####### CALCULANDO DATA VENVIMENTO ##########
            GregorianCalendar gcGregorian = new GregorianCalendar();
            gcGregorian.setTime(dataAtual());

            int qtdDias = cursor.getInt(3);

            gcGregorian.set(GregorianCalendar.DAY_OF_MONTH, gcGregorian.get(GregorianCalendar.DAY_OF_MONTH) + (qtdDias));

            //vcContentValues.put("dataPrimeiroVencimento", gcGregorian.getTime().toString());
            vcContentValues.put("dataPrimeiroVencimento", formatSoap.format(gcGregorian.getTime()));

            Log.d("IMPORTANTE", "DATA PRIMEIRO VENCIMENTO: " + gcGregorian.getTime().toString());
            //###################

            db.update("android_venda_cabecalho", vcContentValues, "_id=?", new String[]{String.valueOf(idAndroidVendaCabecalho)});


        }
    }
*/
}

