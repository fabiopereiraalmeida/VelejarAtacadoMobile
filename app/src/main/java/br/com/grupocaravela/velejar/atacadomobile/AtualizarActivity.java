package br.com.grupocaravela.velejar.atacadomobile;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidCaixaDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidContaReceberDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidVendaCabecalhoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.AndroidVendaDetalheDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CargoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CategoriaClienteDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CategoriaDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CidadeDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.ClienteDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.ContaReceberDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CreditoUsuarioDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.CreditoUsuarioDetalhesDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.EmpresaDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.EnderecoClienteDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.EstadoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.FormaPagamentoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.MarcaDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.ProdutoDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.RotaDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.RotaVendedorDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.UnidadeDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.UsuarioDAO;
import br.com.grupocaravela.velejar.atacadomobile.dao.VendaCabecalhoDAO;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidCaixa;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidContaReceber;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cargo;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Categoria;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CategoriaCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cidade;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CreditoUsuario;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CreditoUsuarioDetalhes;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Empresa;
import br.com.grupocaravela.velejar.atacadomobile.objeto.EnderecoCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Estado;
import br.com.grupocaravela.velejar.atacadomobile.objeto.FormaPagamento;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Marca;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Rota;
import br.com.grupocaravela.velejar.atacadomobile.objeto.RotaVendedor;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Unidade;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Usuario;
import br.com.grupocaravela.velejar.atacadomobile.objeto.VendaCabecalho;

public class AtualizarActivity extends ActionBarActivity {

    private Cursor cursor;

    private Toolbar mainToolbarTop;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private ContentValues contentValues;

    private int idEmpresa;

    private Cargo cargo = null;
    private Categoria categoria = null;
    private CategoriaCliente categoriaCliente = null;
    private Unidade unidade = null;
    private Marca marca = null;
    private Cidade cidade = null;
    private Estado estado = null;
    private Cliente cliente = null;
    private ContaReceber contaReceber = null;
    private CreditoUsuario creditoUsuario = null;
    private CreditoUsuarioDetalhes creditoUsuarioDetalhes = null;
    private EnderecoCliente enderecoCliente = null;
    private FormaPagamento formaPagamento = null;
    //private Produto produto = null;
    private Usuario usuario = null;
    private Rota rota = null;
    private RotaVendedor rotaVendedor = null;
    private Empresa empresa = null;
    private VendaCabecalho vendaCabecalho = null;

    private AndroidCaixa androidCaixa = null;
    private AndroidContaReceber androidContaReceber = null;
    private AndroidVendaCabecalho androidVendaCabecalho = null;
    private AndroidVendaDetalhe androidVendaDetalhe = null;

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    //private SimpleDateFormat formatDataHoraUSAEstenso = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28

    private Long ultimoIdVendaCabecalho;
    private Long ultimoIdCliente;
    private int idVendaCabecalho;

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_atualizar);

        //Toobar superior
        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Atualizações");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        idEmpresa = buscaIdEmpresaUsuarioLogado();
    }

    public void clickAtualizarInformacoes(View v) {

        new Atualize().execute();
    }

    public void clickEnviarVendas(View v) {

        new clickEnviarNovosClientes().execute();
        try {
            Thread.sleep(5000);
        } catch (Exception e) {

        }
        new EnviarVendas().execute();

    }

    public void clickEnviarCaixaRecebidas(View v) {

        new EnviarCaixaRecebidas().execute();

    }

    public void clickEnviarNovosClientes(View v) {

        new clickEnviarNovosClientes().execute();
    }

/*
    public void clickAtualizarImagens(View v) {

        //##############################################
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("ATUALIZAR IMAGENS");

        alertDialogBuilder
                .setMessage(
                        "ATENÇÃO!!! A atualização de imagens costuma demorar muito, tenha certeza de estar com uma conexão rapida com a internet. Gostaria de continuar?")
                .setCancelable(false)
                .setPositiveButton("Sim",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                new AtualizarImagens().execute();

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
    }
*/

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Atualizar Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }

    public class Atualize extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(AtualizarActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setTitle("Atualizando banco de dados...");
            dialog.setMessage("Favor aguardar....");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean retorno = false;

            try {

                db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

                if (verificarClienteNovo()){

                    Log.i("VELEJAR", "ENTREI NO VERIFICA CLIENTE NOVO");

                    enviarNovoCliente(dialog);
                    //enviarAndroidVendaCabecalho(dialog);

                    //Toast.makeText(AtualizarActivity.this, "Existem vendas pendentes de sincronismo!!!", Toast.LENGTH_LONG).show();
                    //Toast.makeText(AtualizarActivity.this, "Favor enviar as vendas antes de atualizar a base de dados!!!", Toast.LENGTH_LONG).show();

                    //db.close();
                    //dialog.setProgress(100);
                    //retorno = false;
                }//else {

                    dbHelper.deleteBanco(db); //Apagando todas as tabelas do banco //Não apaga as vendas
                    Log.i("Banco", "As tabelas do banco foram excluidas com sucesso!");
                    dbHelper.onCreate(db); //Cria todas as tabelas
                    Log.i("Banco", "As tabelas do banco foram criadas com sucesso!");

                    atualizarUsuario(dialog);
                    atualizarCreditoUsuarioDetalhes(dialog);
                    atualizarEmpresa(dialog);
                    if (!verificarBancoCidades()){
                        atualizarCidade(dialog);
                    }
                    if (!verificarBancoEstado()){
                        atualizarEstado(dialog);
                    }
                    atualizarCliente(dialog);
                    atualizarRota(dialog);
                    atualizarRotaVendedor(dialog);
                    atualizarUnidade(dialog);
                    atualizarMarca(dialog);
                    atualizarCategoria(dialog);
                    atualizarCategoriaCliente(dialog);
                    atualizarProduto(dialog);
                    atualizarFormaPagamento(dialog);
                    atualizarVendaCabecalho(dialog);
                    atualizarContaReceber(dialog);

                    //atualizarCargo();
                    //atualizarCreditoUsuarioDetalhes();

                    //db.close();

                    dialog.setProgress(100);

                    retorno = true;
               // }
            } catch (Exception e) {
                Log.e("ERRO", "ERRO: " + e);
                retorno = false;
            }

            return retorno;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(AtualizarActivity.this, "Atualização do banco de dados comcluido com sucesso!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(AtualizarActivity.this, "Falha na atualização do banco de dados!!!", Toast.LENGTH_LONG).show();
                Toast.makeText(AtualizarActivity.this, "Favor atualize novamente!!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
            finish();
        }

        //@Override
        protected void onProgressUpdate(String... progress) {
            Log.v("count",progress[0]);
            dialog.setProgress(Integer.parseInt(progress[0]));
        }
    }

    public class EnviarVendas extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(AtualizarActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Enviando vendas, favor aguardar....");
            dialog.show();

            dialog.setTitle("Enviando vendas...");
            dialog.setMessage("Favor aguardar....");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean resultado = false;

            try {

                //enviarNovoCliente(dialog);
                enviarAndroidVendaCabecalho(dialog);
                ////////////enviarAndroidVendaDetalhe();
                //enviarAndroidCaixa();
                //enviarAndroidContaReceber();

                resultado = true;

                //####################################################################
            } catch (Exception e) {
                resultado = false;
            }

            return resultado;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(AtualizarActivity.this, "Envio de vendas comcluido com sucesso!!!", Toast.LENGTH_SHORT).show();

            } else {
                Toast.makeText(AtualizarActivity.this, "Falha no envio de vendas comcluido com sucesso!!!", Toast.LENGTH_SHORT).show();
            }

            dialog.dismiss();

            finish();
        }
    }

    public class EnviarCaixaRecebidas extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(AtualizarActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Enviando Caixas, favor aguardar....");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean resultado = false;

            try {

                enviarAndroidCaixa();
                enviarObservacoesContasReceber();

                resultado = true;

                //####################################################################
            } catch (Exception e) {
                resultado = false;
            }

            return resultado;
        }



        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(AtualizarActivity.this, "Envio de caixa e contas recebidas comcluido com sucesso!!!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(AtualizarActivity.this, "Falha no envio de caixa e contas recebidas!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    public class EnviarObservacoesContasReceber extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(AtualizarActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Enviando observações, favor aguardar....");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean resultado = false;

            try {

                enviarAndroidCaixa();

                resultado = true;

                //####################################################################
            } catch (Exception e) {
                resultado = false;
            }

            return resultado;
        }



        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(AtualizarActivity.this, "Envio de caixa e contas recebidas comcluido com sucesso!!!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(AtualizarActivity.this, "Falha no envio de caixa e contas recebidas!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    public class clickEnviarNovosClientes extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(AtualizarActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setMessage("Enviando informações, favor aguardar....");
            dialog.show();

        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean resultado = false;

            try {

                enviarNovoCliente(dialog);

                resultado = true;

                //####################################################################
            } catch (Exception e) {
                resultado = false;
            }

            return resultado;
        }



        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(AtualizarActivity.this, "Envio de novos clientes concluido com sucesso!!!", Toast.LENGTH_LONG).show();

            } else {
                Toast.makeText(AtualizarActivity.this, "Falha no envio de novos clientes!!!", Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    private void enviarNovoCliente(ProgressDialog dialog) {

        Log.d("ENVIO CLIENTE", "ESTOU COMECANDO A ENVIAR");

        //####################################################################
        cursor = db
                .rawQuery(
                        "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade, uf, cep, " +
                                "rota_id, empresa_id, novo, alterado FROM cliente where novo like '1'", null);

        if (cursor.moveToFirst()) {

            Log.i("VELEJAR", "ENTREI NO VERIFICA CLIENTE NOVO");

            dialog.setMax(cursor.getCount());
            dialog.setProgress(0);

            //Toast.makeText(AtualizarActivity.this, "TAMANHO DE CLIENTES" + cursor.getCount(), Toast.LENGTH_SHORT).show();
            Log.d("ENVIO CLIENTE", "TAMANHO DA LISTA DE CLIENTES: " + cursor.getCount() + "");

            for (int i = 0; i < cursor.getCount(); i++) {

                ClienteDAO clienteDAO = new ClienteDAO();

                Cliente cliente = new Cliente();

                //cliente.setId(ultimoIdCliente);
                cliente.setFantasia(cursor.getString(2));
                cliente.setAtivo(false);
                try {
                    cliente.setCnpj(cursor.getString(5));
                }catch (Exception ex){
                    cliente.setCnpj(null);
                }

                try {
                    cliente.setCpf(cursor.getString(4));
                }catch (Exception ex){
                    cliente.setCpf(null);
                }

                //Data dadastro - 5

                try {
                    cliente.setInscricaoEstadual(cursor.getString(3).replace(".", "").replace("-", "").replace(" ", ""));
                }catch (Exception ex){
                    cliente.setInscricaoEstadual(null);
                }

                try {
                    cliente.setDataCadastro(cursor.getString(7));
                }catch (Exception ex){
                    cliente.setDataCadastro(null);
                }

                try {
                    cliente.setEmail(cursor.getString(8).replace(" ", ""));
                }catch (Exception ex){
                    cliente.setEmail(null);
                }

                //cliente.setFantasia(cursor.getString(2));
                try {
                    cliente.setLimiteCredito(cursor.getDouble(9));
                }catch (Exception ex){
                    cliente.setLimiteCredito(null);
                }

                try {
                    cliente.setObservacao(cursor.getString(11));
                }catch (Exception ex){
                    cliente.setObservacao(null);
                }

                try {
                    cliente.setRazaoSocial(cursor.getString(1));
                }catch (Exception ex){
                    cliente.setRazaoSocial(null);
                }

                try {
                    cliente.setRota(cursor.getLong(21));
                }catch (Exception ex){
                    cliente.setRota(null);
                }

                try {
                    cliente.setTelefone1(cursor.getString(12).replace(" ", ""));
                }catch (Exception ex){
                    cliente.setTelefone1(null);
                }

                try {
                    cliente.setTelefone2(cursor.getString(13).replace(" ", ""));
                }catch (Exception ex){
                    cliente.setTelefone2(null);
                }


                //Novo - 14
                try {
                    cliente.setBairro(cursor.getString(17));
                }catch (Exception ex){
                    cliente.setBairro(null);
                }

                try {
                    cliente.setCep(cursor.getString(20));
                }catch (Exception ex){
                    cliente.setCep(null);
                }

                try {
                    cliente.setComplemento(cursor.getString(16));
                }catch (Exception ex){
                    cliente.setComplemento(null);
                }

                try {
                    cliente.setEndereco(cursor.getString(14));
                }catch (Exception ex){
                    cliente.setEndereco(null);
                }

                try {
                    cliente.setEnderecoNumero(cursor.getString(15).replace(" ", ""));
                }catch (Exception ex){
                    cliente.setEnderecoNumero(null);
                }

                try {
                    cliente.setUf(cursor.getString(19).replace(" ", ""));
                }catch (Exception ex){
                    cliente.setUf(null);
                }

                try {
                    cliente.setCidade(cursor.getString(18));
                }catch (Exception ex){
                    cliente.setCidade(null);
                }

                cliente.setEmpresa(cursor.getLong(22));

                boolean result = clienteDAO.inserirCliente(cliente, Configuracao.getCnpj());

                //Toast.makeText(AtualizarActivity.this, "RESULTADO " + result, Toast.LENGTH_SHORT).show();
                //Log.d("AMERICA", "ROTA ID DO CLIENTE " + cursor.getLong(11) + "");
                Log.d("AMERICA", "RESULTADO " + result + "");

                if (result) {

                    ultimoIdCliente = clienteDAO.buscarUltimoIdClienteSalvo(Configuracao.getCnpj());

                    //Toast.makeText(AtualizarActivity.this, "ULTIMO ID " + ultimoIdCliente, Toast.LENGTH_SHORT).show();
                    Log.d("AMERICA", "ULTIMO ID " + ultimoIdCliente + "");

                    contentValues.clear();
                    //contentValues.put("_id", ultimoIdCliente);
                    contentValues.put("novo", Boolean.FALSE);
                    db.update("cliente", contentValues, "_id=?", new String[]{String.valueOf(cursor.getInt(0))});

                    contentValues.clear();

                    //contentValues.put("cliente_id", ultimoIdCliente);
                    contentValues.put("cliente_mobile_id", ultimoIdCliente);
                    db.update("android_venda_cabecalho", contentValues, "cliente_id=?", new String[]{String.valueOf(cursor.getInt(0))});

                    db.close();
                }

                cursor.moveToNext();

                dialog.setProgress(i);
            }
        }
        //###########################################################################################
    }

    private void enviarAndroidVendaCabecalho(ProgressDialog dialog) {

        idVendaCabecalho = 0;

        //####################################################################
        cursor = db
                .rawQuery(
                        "SELECT _id, codVenda, observacao, cliente_id, entrada, juros, valor_parcial, valor_desconto, valor_total, " +
                                "dataVenda, dataPrimeiroVencimento, usuario_id, forma_pagamento_id, empresa_id, venda_aprovada, enviado, cliente_mobile_id, observacao " +
                                "FROM android_venda_cabecalho where venda_aprovada like '1'AND enviado like '0'", null);
        if (cursor.moveToFirst()) {
            //Toast.makeText(AtualizarActivity.this, "TAMANHO DA LISTA CABECALHO" + cursor.getCount(), Toast.LENGTH_SHORT).show();
            dialog.setMax(cursor.getCount());
            dialog.setProgress(0);

            for (int i = 0; i < cursor.getCount(); i++) {

                idVendaCabecalho = cursor.getInt(0);

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

                Log.d("IMPORTANTE", "CLIENTE MOBILE ++: " + cursor.getLong(16));
                androidVendaCabecalho.setClienteMobile(cursor.getLong(16));

                androidVendaCabecalho.setObservacao(cursor.getString(17));
                /*
                if (cursor.getString(14).equals("1")){
                    androidVendaCabecalho.setVendaAprovada(true);
                }else{
                    androidVendaCabecalho.setVendaAprovada(false);
                }
                if (cursor.getString(15).equals("1")){
                    androidVendaCabecalho.setEnviado(true);
                }else{
                    androidVendaCabecalho.setEnviado(false);
                }
                */
                //Log.d("IMPORTANTE", "PASSEI AKI 06");
                AndroidVendaCabecalhoDAO androidVendaCabecalhoDAO = new AndroidVendaCabecalhoDAO();

                boolean result = androidVendaCabecalhoDAO.inserirAndroidVendaCabecalho(androidVendaCabecalho, Configuracao.getCnpj());
                Log.d("AMERICA", "RESULT " + result + "");

                if (result) {

                    //AndroidVendaCabecalhoDAO avcDAO = new AndroidVendaCabecalhoDAO();
                    ultimoIdVendaCabecalho = androidVendaCabecalhoDAO.buscarUltimoIdVendaCabecalho(Configuracao.getCnpj());

                    Log.d("AMERICA", "id Cabeçalho n " + idVendaCabecalho + "");
                    Log.d("AMERICA", "ultimo id Cabeçalho n " + ultimoIdVendaCabecalho + "");

                    //VendaCabecalho = cursor.getInt(0);

                    //enviarAndroidVendaDetalhe(cursor.getInt(0));
/*
                    db.delete("android_venda_cabecalho", "_id=?", new String[]{String.valueOf(cursor.getInt(0))});
                    Log.d("CaculeCompreFacil", "Apagado venda cabeçalho n " + cursor.getInt(0) + "");
*/
                    ContentValues vcCabecalho = new ContentValues();

                    vcCabecalho.put("enviado", Boolean.TRUE);

                    db.update("android_venda_cabecalho", vcCabecalho, "_id=?", new String[]{String.valueOf(cursor.getInt(0))});

                    //enviarAndroidVendaDetalhe(cursor.getInt(0));
                    enviarAndroidVendaDetalhe(idVendaCabecalho);
                }

                cursor.moveToNext();

                dialog.setProgress(i);
            }
        }

        //db.close();

        //###########################################################################################
    }

    private void enviarAndroidVendaDetalhe(int id) {

        //####################################################################
        Cursor cursorDetalhes = db
                .rawQuery(
                        "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + id + "'", null); // where android_venda_cabecalho_id like '"+ id +"'

        if (cursorDetalhes.moveToFirst()) {

            //Toast.makeText(AtualizarActivity.this, "TAMANHO DA LISTA " + cursor.getCount(), Toast.LENGTH_SHORT).show();

            for (int i = 0; i < cursorDetalhes.getCount(); i++) {
                AndroidVendaDetalhe androidVendaDetalhe = new AndroidVendaDetalhe();

                androidVendaDetalhe.setVendaCabecalho(ultimoIdVendaCabecalho);
                androidVendaDetalhe.setProduto(cursorDetalhes.getLong(2));
                androidVendaDetalhe.setQuantidade(cursorDetalhes.getDouble(3));
                androidVendaDetalhe.setValorParcial((cursorDetalhes.getDouble(4) / cursorDetalhes.getDouble(3)));
                androidVendaDetalhe.setValorDesconto(cursorDetalhes.getDouble(5));
                androidVendaDetalhe.setValorTotal(cursorDetalhes.getDouble(6));
                androidVendaDetalhe.setEmpresa(cursorDetalhes.getLong(7));
                androidVendaDetalhe.setValorUnitario(cursorDetalhes.getDouble(8));

                AndroidVendaDetalheDAO androidVendaDetalheDAO = new AndroidVendaDetalheDAO();

                boolean result = androidVendaDetalheDAO.inserirAndroidVendaDetalhe(androidVendaDetalhe, Configuracao.getCnpj());
                Log.d("AMERICA", "Resultado do envio detalhes " + result + "");
/*
            while (result == false){
                result = androidVendaDetalheDAO.inserirAndroidVendaDetalhe(androidVendaDetalhe);
                Log.d("CaculeCompreFacil", "Dentro do While " + result + "");
            }
*/
                if (result) {
                    Log.d("AMERICA", "Venda detalhes enviada com sucesso!");
                    cursorDetalhes.moveToNext();
                    /*
                    db.delete("android_venda_detalhe", "_id=?", new String[]{String.valueOf(cursorDetalhes.getInt(0))});
                    Log.d("CaculeCompreFacil", "Apagado venda detalhe n " + cursorDetalhes.getInt(0) + "");
                    */
                }else{
                    Log.d("AMERICA", "Falha ao enviar venda detalhes");
                    i = i--;
                }

                //cursorDetalhes.moveToNext();
            }
        }

        //db.close();
        //###########################################################################################
    }

    private void enviarAndroidCaixa() {

        Log.d("CAIXA", "PASSEI AKI 01");

        //####################################################################
        Cursor cursorCaixa = db
                .rawQuery(
                        "SELECT _id, data_recebimento, data_transmissao, valor, android_venda_cabecalho_id, venda_cabecalho_id, " +
                                "usuario_id, cliente_id, conta_receber_id FROM android_caixa", null); // where android_venda_cabecalho_id like '"+ id +"'

        Log.d("CAIXA", "PASSEI AKI 02");
        if (cursorCaixa.moveToFirst()) {

            Log.d("CAIXA", "PASSEI AKI 03");
            //Toast.makeText(AtualizarActivity.this, "TAMANHO DA LISTA " + cursor.getCount(), Toast.LENGTH_SHORT).show();

            for (int i = 0; i < cursorCaixa.getCount(); i++) {
                Log.d("CAIXA", "PASSEI AKI 04");
                AndroidCaixa androidCaixa = new AndroidCaixa();
                try {
                    //Date dt = formatSoap.parse(cursor.getString(1));
                    //androidCaixa.setDataRecebimento(dt);
                    androidCaixa.setDataRecebimento(cursorCaixa.getString(1));
                    Log.d("CAIXA", "PASSEI AKI 05.1");
                } catch (Exception e) {
                    androidCaixa.setDataRecebimento(dataAtual().toString());
                    Log.d("CAIXA", "PASSEI AKI 05.2");
                }
                androidCaixa.setDataTransmissao(dataAtual().toString());
                androidCaixa.setValor(cursorCaixa.getDouble(3));
                androidCaixa.setAndroidVendaCabecalho(Long.parseLong(String.valueOf(cursorCaixa.getInt(4))));
                androidCaixa.setVendaCabecalho(Long.parseLong(String.valueOf(cursorCaixa.getInt(5))));
                androidCaixa.setUsuario(Long.parseLong(String.valueOf(cursorCaixa.getInt(6))));
                androidCaixa.setCliente(Long.parseLong(String.valueOf(cursorCaixa.getInt(7))));
                androidCaixa.setContaReceber(Long.parseLong(String.valueOf(cursorCaixa.getInt(8))));
                androidCaixa.setEmpresa(Long.parseLong(String.valueOf(buscaIdEmpresaUsuarioLogado())));

                Log.d("CAIXA", "PASSEI AKI 06");

                AndroidCaixaDAO androidCaixaDAO = new AndroidCaixaDAO();
                boolean result = androidCaixaDAO.inserirAndroidCaixa(androidCaixa, Configuracao.getCnpj());
                Log.d("CaculeCompreFacil", result + "");

                if (result) {
                    db.delete("android_caixa", "_id=?", new String[]{String.valueOf(cursorCaixa.getInt(0))});
                    Log.d("CaculeCompreFacil", "Apagado AndroidCaixa n " + cursorCaixa.getInt(0) + "");
                    cursorCaixa.moveToNext();
                }
            }
        }

        //db.close();
        //###########################################################################################
    }

    private void enviarObservacoesContasReceber() {

        Log.d("OBSERVACOES", "PASSEI AKI 01");

        //####################################################################
        Cursor cursorObservacaoContaReceber = db
                .rawQuery(
                        "SELECT _id, cliente_id, venda_cabecalho_id, valor_devido, vencimento, data_pagamento, " +
                                "quitada, atrasada, empresa_id, valor_desconto, observacao FROM conta_receber WHERE observacao IS NOT NULL", null); // where android_venda_cabecalho_id like '"+ id +"'

        Log.d("OBSERVACOES", "PASSEI AKI 02");
        if (cursorObservacaoContaReceber.moveToFirst()) {

            Log.d("OBSERVACOES", "PASSEI AKI 03");
            //Toast.makeText(AtualizarActivity.this, "TAMANHO DA LISTA " + cursorObservacaoContaReceber.getCount(), Toast.LENGTH_SHORT).show();

            Log.d("OBSERVACOES", "TAMANHO DA LISTA: " + cursorObservacaoContaReceber.getCount());

            for (int i = 0; i < cursorObservacaoContaReceber.getCount(); i++) {
                Log.d("OBSERVACOES", "PASSEI AKI 04");
                ContaReceber androidContaReceber = new ContaReceber();

                androidContaReceber.setId(Long.parseLong(String.valueOf(cursorObservacaoContaReceber.getInt(0))));
                androidContaReceber.setCliente(Long.parseLong(String.valueOf(cursorObservacaoContaReceber.getInt(1))));
                try {
                    androidContaReceber.setVendaCabecalho(Long.parseLong(String.valueOf(cursorObservacaoContaReceber.getInt(2))));
                }catch (Exception e){

                }
                androidContaReceber.setValorDevido(cursorObservacaoContaReceber.getDouble(3));
                androidContaReceber.setVencimento(cursorObservacaoContaReceber.getString(4));
                androidContaReceber.setDataPagamento(cursorObservacaoContaReceber.getString(5));

                androidContaReceber.setEmpresa(Long.parseLong(String.valueOf(cursorObservacaoContaReceber.getInt(8))));
                androidContaReceber.setValorDesconto(cursorObservacaoContaReceber.getDouble(9));
                androidContaReceber.setObservacao(cursorObservacaoContaReceber.getString(10));

                Log.d("OBSERVACOES", "PASSEI AKI 06");
                Log.d("OBSERVACOES", "OBSERVACAO: " + androidContaReceber.getObservacao() + " - " + androidContaReceber.getId());

                ContaReceberDAO contaReceberDAO = new ContaReceberDAO();
                boolean result = contaReceberDAO.inserirObservacao(androidContaReceber, Configuracao.getCnpj());
                Log.d("CaculeCompreFacil", result + "");

                if (result) {

                    contentValues.putNull("observacao");
                    db.update("conta_receber", contentValues, "_id=?", new String[]{String.valueOf(cursorObservacaoContaReceber.getInt(0))});

                    cursorObservacaoContaReceber.moveToNext();
                }
            }
        }

        //db.close();
        //###########################################################################################
    }

    /*
        private void enviarCliente(){

            //####################################################################
            cursor = db
                    .rawQuery(
                            "SELECT _id, nome, ativo, cnpj, cpf, data_cadastro, email, fantasia, limite_credito, observacao, razao_social, rota_id, endereco_id, " +
                                    "telefone1, telefone2, novo FROM cliente WHERE novo = '1'", null); // where android_venda_cabecalho_id like '"+ id +"'

            if (cursor.moveToFirst()) {


                //Toast.makeText(AtualizarActivity.this, "TAMANHO DA LISTA " + cursor.getCount(), Toast.LENGTH_SHORT).show();

                for (int i = 0; i < cursor.getCount(); i++) {
                    Cliente cliente = new Cliente();

                    cliente.setDataCadastro(dataAtual());
                    cliente.setAtivo(false);
                    cliente.setCnpj(cursor.getString(3));
                    cliente.setNome(cursor.getString(1));
                    cliente.setCpf(cursor.getString(4));
                    cliente.setEmail(cursor.getString(6));
                    cliente.setFantasia(cursor.getString(7));
                    cliente.setLimiteCredito(cursor.getDouble(8));
                    cliente.setObservacao(cursor.getString(9));
                    cliente.setRazaoSocial(cursor.getString(10));
                    cliente.setRota(Long.parseLong(String.valueOf(cursor.getInt(11))));
                    cliente.setEndereco(Long.parseLong(String.valueOf(cursor.getInt(12))));
                    cliente.setTelefone1(cursor.getString(13));
                    cliente.setTelefone2(cursor.getString(14));

                    ClienteDAO clienteDAO = new ClienteDAO();
                    boolean result = clienteDAO.inserirCliente(cliente);
                    Log.d("CaculeCompreFacil", result + "");

                    if (result) {
                        db.delete("android_caixa", "_id=?", new String[]{String.valueOf(cursor.getInt(0))});
                        Log.d("CaculeCompreFacil", "Apagado AndroidCaixa n " + cursor.getInt(0) + "");
                    }

                    cursor.moveToNext();
                }
            }

            //db.close();
            //###########################################################################################
        }
    */
    /*
    private void enviarAndroidContaReceber() {

        //####################################################################
        cursor = db
                .rawQuery(
                        "SELECT _id, dataTransmissao, valor_devido, cliente_id, venda_cabecalho_id, dataRecebimento, usuario_id FROM android_conta_receber", null);

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            AndroidContaReceber androidContaReceber = new AndroidContaReceber();

            try {
                String dt = cursor.getString(1);
                androidContaReceber.setDataTransmissao(dt);
            } catch (Exception e) {
                androidContaReceber.setDataTransmissao(null);
            }
            androidContaReceber.setValorDevido(cursor.getDouble(2));
            androidContaReceber.setCliente(cursor.getLong(3));
            androidContaReceber.setVendaCabecalho(cursor.getLong(4));

            try {
                //Date dt = formatSoap.parse(cursor.getString(5).toString());
                //androidContaReceber.setDataRecebimento(dt);
                androidContaReceber.setDataRecebimento(cursor.getString(5));
            } catch (Exception e) {
                androidContaReceber.setDataRecebimento(null);
            }
            androidContaReceber.setUsuario(cursor.getLong(6));

            AndroidContaReceberDAO androidContaReceberDAO = new AndroidContaReceberDAO();

            boolean result = androidContaReceberDAO.inserirAndroidCOntaReceber(androidContaReceber, Configuracao.getCnpj());
            Log.d("CaculeCompreFacil", result + "");

            if (result) {
                db.delete("android_conta_receber", "_id=?", new String[]{String.valueOf(cursor.getInt(0))});
                Log.d("CaculeCompreFacil", "Apagado AndroidContaReceber n " + cursor.getInt(0) + "");
            }

            cursor.moveToNext();
        }

        //db.close();
        //###########################################################################################
    }
*/
    private void atualizarRota(ProgressDialog dialog) {

        RotaDAO rotaDAO = new RotaDAO();
        ArrayList<Rota> listaRotas = rotaDAO.listarRota(idEmpresa, Configuracao.getCnpj());

        //Integer progresso = divisor / listaUsuarios.size();
        //dialog.setMessage("Atualizando os usuarios, favor aguardar....");
        dialog.setProgress(1);

        for (int i = 0; i < listaRotas.size(); i++) {

            contentValues.clear();

            rota = listaRotas.get(i);

            contentValues.put("_id", rota.getId());
            contentValues.put("nome", rota.getNome()); // Adicionando
            contentValues.put("observacao", rota.getObservacao()); // Adicionando
            contentValues.put("empresa_id", rota.getEmpresa()); // Adicionando

            db.insert("rota", null, contentValues); //Salvando o cargo

            Log.i("Banco", "a ROTA " + rota.getNome() + " foi criada com sucesso!");

            dialog.setProgress(i);

        }
    }

    private void atualizarRotaVendedor(ProgressDialog dialog) {

        RotaVendedorDAO rotaVendedorDAO = new RotaVendedorDAO();
        ArrayList<RotaVendedor> listaRotasVendedores = rotaVendedorDAO.listarRotaVendedor(idEmpresa, Configuracao.getCnpj());

        //Integer progresso = divisor / listaUsuarios.size();
        //dialog.setMessage("Atualizando os usuarios, favor aguardar....");
        dialog.setProgress(1);

        for (int i = 0; i < listaRotasVendedores.size(); i++) {

            contentValues.clear();

            rotaVendedor = listaRotasVendedores.get(i);

            contentValues.put("_id", rotaVendedor.getId());
            contentValues.put("rota_id", rotaVendedor.getRota()); // Adicionando
            contentValues.put("usuario_id", rotaVendedor.getUsuario()); // Adicionando

            db.insert("rota_vendedor", null, contentValues); //Salvando o cargo

            Log.i("Banco", "a ROTA VENDEDOR id: " + rotaVendedor.getId() + " foi criada com sucesso!");

            dialog.setProgress(i);
        }
    }

    private void atualizarCargo() {
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

        CargoDAO cargoDAO = new CargoDAO();
        ArrayList<Cargo> listaCargos = cargoDAO.listarCargo(Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");

        for (int i = 0; i < listaCargos.size(); i++) {

            contentValues.clear();

            cargo = listaCargos.get(i);

            contentValues.put("_id", cargo.getId());
            contentValues.put("acessoAndroid", cargo.isAcessoAndroid()); // Adicionando
            contentValues.put("acessoCategorias", cargo.isAcessoCategorias()); // Adicionando
            contentValues.put("acessoCidades", cargo.isAcessoCidades()); // Adicionando
            contentValues.put("acessoClientes", cargo.isAcessoClientes()); // Adicionando
            contentValues.put("acessoCompras", cargo.isAcessoCompras()); // Adicionando
            contentValues.put("acessoConfiguracao", cargo.isAcessoConfiguracao()); // Adicionando
            contentValues.put("acessoContasPagar", cargo.isAcessoContasPagar()); // Adicionando
            contentValues.put("acessoContasReceber", cargo.isAcessoContasReceber()); // Adicionando
            contentValues.put("acessoFormaPagamento", cargo.isAcessoFormaPagamento()); // Adicionando
            contentValues.put("acessoFornecedores", cargo.isAcessoFornecedores()); // Adicionando
            contentValues.put("acessoProdutos", cargo.isAcessoProdutos()); // Adicionando
            contentValues.put("acessoRelatorios", cargo.isAcessoRelatorios()); // Adicionando
            contentValues.put("acessoRotas", cargo.isAcessoRotas()); // Adicionando
            contentValues.put("acessoUnidade", cargo.isAcessoUnidade()); // Adicionando
            contentValues.put("acessoUsuarios", cargo.isAcessoUsuarios()); // Adicionando
            contentValues.put("acessoVendas", cargo.isAcessoVendas()); // Adicionando
            contentValues.put("nome", cargo.getNome()); // Adicionando
            contentValues.put("observacao", cargo.getObservacao()); // Adicionando

            db.insert("cargo", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O Cargo " + cargo.getNome() + " foi criado com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

        }
    }

    private void atualizarCategoria(ProgressDialog dialog) {
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS categoria");

        CategoriaDAO categoriaDAO = new CategoriaDAO();
        ArrayList<Categoria> listaCategorias = categoriaDAO.listarCategoria(idEmpresa, Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");
        dialog.setMax(listaCategorias.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaCategorias.size(); i++) {

            contentValues.clear();

            categoria = listaCategorias.get(i);

            contentValues.put("_id", categoria.getId());
            contentValues.put("nome", categoria.getNome()); // Adicionando
            contentValues.put("empresa_id", categoria.getEmpresa());

            db.insert("categoria", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A Categoria " + categoria.getNome() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);
        }
    }

    private void atualizarCategoriaCliente(ProgressDialog dialog) {
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS categoria");

        CategoriaClienteDAO categoriaClienteDAO = new CategoriaClienteDAO();
        ArrayList<CategoriaCliente> listaCategorias = categoriaClienteDAO.listarCategoria(idEmpresa, Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");
        dialog.setMax(listaCategorias.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaCategorias.size(); i++) {

            contentValues.clear();

            categoriaCliente = listaCategorias.get(i);

            contentValues.put("_id", categoriaCliente.getId());
            try{
                contentValues.put("nome", categoriaCliente.getNome()); // Adicionando
            } catch (Exception e) {
                contentValues.put("nome", " "); // Adicionando
            }
            contentValues.put("empresa_id", categoriaCliente.getEmpresa());

            db.insert("categoria_cliente", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A Categoria Cliente " + categoriaCliente.getNome() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);
        }
    }

    private void atualizarUnidade(ProgressDialog dialog) {
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS unidade");

        UnidadeDAO unidadeDAO = new UnidadeDAO();
        ArrayList<Unidade> unidades = unidadeDAO.listarUnidade(idEmpresa, Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");
        //dialog.setMessage("Atualizando as unidade, favor aguardar....");
        dialog.setMax(unidades.size());
        dialog.setProgress(0);

        for (int i = 0; i < unidades.size(); i++) {

            contentValues.clear();

            unidade = unidades.get(i);

            contentValues.put("_id", unidade.getId());
            contentValues.put("nome", unidade.getNome()); // Adicionando
            contentValues.put("empresa_id", unidade.getEmpresa()); // Adicionando

            db.insert("unidade", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A Unidade " + unidade.getNome() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);
        }
    }

    private void atualizarMarca(ProgressDialog dialog) {
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);
        //db.execSQL("DROP TABLE IF EXISTS marca");

        MarcaDAO marcaDAO = new MarcaDAO();
        ArrayList<Marca> marcas = marcaDAO.listarMarca(idEmpresa, Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");
        //dialog.setMessage("Atualizando as marcas, favor aguardar....");
        dialog.setMax(marcas.size());
        dialog.setProgress(0);

        for (int i = 0; i < marcas.size(); i++) {

            contentValues.clear();

            marca = marcas.get(i);

            contentValues.put("_id", marca.getId());
            contentValues.put("nome", marca.getNome()); // Adicionando
            contentValues.put("empresa_id", marca.getEmpresa()); // Adicionando

            db.insert("marca", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A Marca " + marca.getNome() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);
        }
    }

    private void atualizarCidade(ProgressDialog dialog) {

        CidadeDAO cidadeDAO = new CidadeDAO();
        ArrayList<Cidade> listaCidades = cidadeDAO.listarCidade(Configuracao.getCnpj());

        dialog.setMax(listaCidades.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaCidades.size(); i++) {

            contentValues.clear();

            cidade = listaCidades.get(i);

            contentValues.put("_id", cidade.getId());
            contentValues.put("estado_id", cidade.getId_estado());
            contentValues.put("nome", cidade.getNome()); // Adicionando
            contentValues.put("ibge", cidade.getIbge()); // Adicionando

            db.insert("cidade", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A Cidade " + cidade.getNome() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);

        }
    }

    private void atualizarEstado(ProgressDialog dialog) {

        EstadoDAO estadoDAO = new EstadoDAO();
        ArrayList<Estado> listaEstados = estadoDAO.listarEstado(Configuracao.getCnpj());

        dialog.setMax(listaEstados.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaEstados.size(); i++) {

            contentValues.clear();

            estado = listaEstados.get(i);

            contentValues.put("_id", estado.getId());
            contentValues.put("nome", estado.getNome()); // Adicionando
            contentValues.put("uf", estado.getUf_estado()); // Adicionando
            contentValues.put("codigo", estado.getCodigo()); // Adicionando

            db.insert("estado", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O Estado " + estado.getNome() + " foi criado com sucesso!");

            dialog.setProgress(i);

        }
    }

    private void atualizarCliente(ProgressDialog dialog) {
        Log.i("Banco", "Entrei no atualizar clientes");
        //Caso não exista a tabela de usuario, será criada uma
        //db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

        ClienteDAO clienteDAO = new ClienteDAO();
        ArrayList<Cliente> listaClientes = clienteDAO.listarCliente(idEmpresa, Configuracao.getCnpj());
//        Log.d("CaculeCompreFacil", listaCargos + "");

        dialog.setMax(listaClientes.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaClientes.size(); i++) {

            contentValues.clear();

            cliente = listaClientes.get(i);

            contentValues.put("_id", cliente.getId());
            contentValues.put("razaoSocial", cliente.getRazaoSocial()); // Adicionando
            contentValues.put("fantasia", cliente.getFantasia()); // Adicionando
            contentValues.put("inscricaoEstadual", cliente.getInscricaoEstadual()); // Adicionando
            contentValues.put("cpf", cliente.getCpf()); // Adicionando
            contentValues.put("cnpj", cliente.getCnpj()); // Adicionando
            try {
                contentValues.put("data_nascimento", dateFormat.format(cliente.getDataNascimento())); // Adicionando
            }catch (Exception e){

            }
            try {
                contentValues.put("data_cadastro", dateFormat.format(cliente.getDataCadastro())); // Adicionando
            }catch (Exception e){

            }
            contentValues.put("email", cliente.getEmail()); // Adicionando
            contentValues.put("limite_credito", cliente.getLimiteCredito()); // Adicionando
            contentValues.put("ativo", cliente.getAtivo()); // Adicionando
            contentValues.put("observacao", cliente.getObservacao()); // Adicionando
            contentValues.put("telefone1", cliente.getTelefone1()); // Adicionando
            contentValues.put("telefone2", cliente.getTelefone2()); // Adicionando
            contentValues.put("endereco", cliente.getEndereco()); // Adicionando
            contentValues.put("endereco_numero", cliente.getEnderecoNumero()); // Adicionando
            contentValues.put("complemento", cliente.getComplemento()); // Adicionando
            contentValues.put("bairro", cliente.getBairro()); // Adicionando
            contentValues.put("cidade_id", cliente.getCidade_id()); // Adicionando
            Log.d("CIDADE", "ID: " + cliente.getCidade_id());
            contentValues.put("estado_id", cliente.getEstado_id()); // Adicionando
            contentValues.put("cep", cliente.getCep()); // Adicionando
            contentValues.put("rota_id", cliente.getRota()); // Adicionando
            contentValues.put("empresa_id", cliente.getEmpresa()); // Adicionando
            contentValues.put("novo", cliente.getNovo()); // Adicionando
            contentValues.put("alterado", cliente.getAlterado()); // Adicionando
            contentValues.put("forma_pagamento_id", cliente.getFormaPagamento()); // Adicionando
            contentValues.put("categoria_cliente_id", cliente.getCategoriaCliente()); // Adicionando
            contentValues.put("imagem", cliente.getImagem()); // Adicionando
            contentValues.put("localizacao", cliente.getLocalizacao());

            //Log.d("IMPORTANTE", "PASSEI AKI03");

            db.insert("cliente", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O Cliente " + cliente.getRazaoSocial() + " com fantasia " + cliente.getFantasia() + " e Cidade_id " + cliente.getCidade() + " foi criado com sucesso!");
            Log.i("Banco", "O Cliente esta ativo: " + cliente.getAtivo());
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();

            dialog.setProgress(i);
        }
    }

    private void atualizarContaReceber(ProgressDialog dialog) {

        ContaReceberDAO contaReceberDAO = new ContaReceberDAO();
        ArrayList<ContaReceber> listaContaReceber = contaReceberDAO.listarContaReceber(idEmpresa, Configuracao.getCnpj());

        dialog.setMax(listaContaReceber.size());
        dialog.setProgress(0);

        //Log.i("CONTA RECEBER", "PASSEI AKI 11!");

        for (int i = 0; i < listaContaReceber.size(); i++) {

            //Log.i("CONTA RECEBER", "PASSEI AKI 12!");
            contentValues.clear();
            //Log.i("CONTA RECEBER", "PASSEI AKI 13!");
            contaReceber = listaContaReceber.get(i);
            //Log.i("CONTA RECEBER", "PASSEI AKI 14!");
            contentValues.put("_id", contaReceber.getId());
            //Log.i("CONTA RECEBER", "PASSEI AKI 15!");
            contentValues.put("cliente_id", contaReceber.getCliente()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 16!");
            contentValues.put("venda_cabecalho_id", contaReceber.getVendaCabecalho()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 17!");
            contentValues.put("valor_devido", contaReceber.getValorDevido()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 18!");
            try {
                //contentValues.put("vencimento", format.format(contaReceber.getVencimento())); // Adicionando
                contentValues.put("vencimento", contaReceber.getVencimento()); // Adicionando
                Log.i("CONTA RECEBER", "VENCIMENTO: " + contaReceber.getVencimento());
            }catch (Exception e){
                contentValues.put("vencimento", ""); // Adicionando
            }
            //Log.i("CONTA RECEBER", "PASSEI AKI 19!");
            try {
                //contentValues.put("data_pagamento", dateFormat.format(contaReceber.getVencimento())); // Adicionando
                contentValues.put("data_pagamento", contaReceber.getVencimento()); // Adicionando
            }catch (Exception e){
                contentValues.put("vencimento", ""); // Adicionando
            }

           // Log.i("CONTA RECEBER", "PASSEI AKI 20!");
            contentValues.put("quitada", contaReceber.getQuitada()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 21!");
            contentValues.put("atrasada", contaReceber.getAtrasada()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 22!");
            contentValues.put("empresa_id", contaReceber.getEmpresa()); // Adicionandof
            //Log.i("CONTA RECEBER", "PASSEI AKI 23!");
            contentValues.put("valor_desconto", contaReceber.getValorDesconto()); // Adicionando
            //Log.i("CONTA RECEBER", "VALOR DESCONTO: " + contaReceber.getValorDesconto());

            db.insert("conta_receber", null, contentValues); //Salvando o cargo

           // Log.i("CONTA RECEBER", "PASSEI AKI 24!");

            Log.i("Banco", "A Conta Receber do cliente cod: " + contaReceber.getCliente() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();
            dialog.setProgress(i);
        }
    }

    private void atualizarVendaCabecalho(ProgressDialog dialog) {

        VendaCabecalhoDAO vendaCabecalhoDAO = new VendaCabecalhoDAO();
        ArrayList<VendaCabecalho> listaVendaCabecalho = vendaCabecalhoDAO.listarVendaCabecalho(idEmpresa, Configuracao.getCnpj());

        dialog.setMax(listaVendaCabecalho.size());
        dialog.setProgress(0);

        //Log.i("CONTA RECEBER", "PASSEI AKI 11!");

        for (int i = 0; i < listaVendaCabecalho.size(); i++) {

            //Log.i("CONTA RECEBER", "PASSEI AKI 12!");
            contentValues.clear();
            //Log.i("CONTA RECEBER", "PASSEI AKI 13!");
            vendaCabecalho = listaVendaCabecalho.get(i);
            //Log.i("CONTA RECEBER", "PASSEI AKI 14!");
            contentValues.put("_id", vendaCabecalho.getId());
            //Log.i("CONTA RECEBER", "PASSEI AKI 15!");
            try {
                //contentValues.put("vencimento", format.format(contaReceber.getVencimento())); // Adicionando
                contentValues.put("data_primeiro_vencimento", vendaCabecalho.getDataPrimeiroVencimento()); // Adicionando
            }catch (Exception e){
                contentValues.put("data_primeiro_vencimento", ""); // Adicionando
            }
            try {
                //contentValues.put("vencimento", format.format(contaReceber.getVencimento())); // Adicionando
                contentValues.put("data_venda", vendaCabecalho.getDataVenda()); // Adicionando
            }catch (Exception e){
                contentValues.put("data_venda", ""); // Adicionando
            }
            //Log.i("CONTA RECEBER", "PASSEI AKI 16!");
            contentValues.put("entrada", vendaCabecalho.getEntrada()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 17!");
            contentValues.put("juros", vendaCabecalho.getJuros()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 18!");
            contentValues.put("observacao", vendaCabecalho.getObservacao()); // Adicionando
            // Log.i("CONTA RECEBER", "PASSEI AKI 19!");
            contentValues.put("valor_desconto", vendaCabecalho.getValorDesconto()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 20!");
            contentValues.put("valor_parcial", vendaCabecalho.getValorParcial()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 21!");
            contentValues.put("valor_total", vendaCabecalho.getValorTotal()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 22!");
            contentValues.put("cliente_id", vendaCabecalho.getCliente()); // Adicionando
            //Log.i("CONTA RECEBER", "PASSEI AKI 23!");
            contentValues.put("empresa_id", vendaCabecalho.getEmpresa()); // Adicionandof
            //Log.i("CONTA RECEBER", "PASSEI AKI 24!");
            contentValues.put("forma_pagamento_id", vendaCabecalho.getFormaPagamento()); // Adicionandof
            //Log.i("CONTA RECEBER", "PASSEI AKI 25!");
            contentValues.put("usuario_id", vendaCabecalho.getUsuario()); // Adicionandof
            //Log.i("CONTA RECEBER", "PASSEI AKI 26!");
            contentValues.put("status", vendaCabecalho.getStatus()); // Adicionandof
            //Log.i("CONTA RECEBER", "PASSEI AKI 27!");

            db.insert("venda_cabecalho", null, contentValues); //Salvando o cargo

            Log.i("VENDA CABECALHO", "PASSEI AKI 24!");

            Log.i("Banco", "A VendaCabecalho do cliente cod: " + vendaCabecalho.getCliente() + " foi criada com sucesso!");
            //Toast.makeText(this, "O USUARIO_LOGADO FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();
            dialog.setProgress(i);
        }
    }
/*
    private void atualizarCreditoUsuario(ProgressDialog dialog) {

        CreditoUsuarioDAO creditoUsuarioDAO = new CreditoUsuarioDAO();
        ArrayList<CreditoUsuario> listaCreditoUsuario = creditoUsuarioDAO.listarCreditoUsuario(idEmpresa, Configuracao.getCnpj());

        dialog.setMax(listaCreditoUsuario.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaCreditoUsuario.size(); i++) {

            contentValues.clear();

            creditoUsuario = listaCreditoUsuario.get(i);

            contentValues.put("_id", creditoUsuario.getId());
            contentValues.put("valor", creditoUsuario.getValor()); // Adicionando
            contentValues.put("usuario_id", creditoUsuario.getUsuario()); // Adicionando

            db.insert("credito_usuario", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O crédito usuario do usuario cod: " + creditoUsuario.getUsuario() + " no valor de " + creditoUsuario.getValor() + "foi criado com sucesso!");

        }
    }
*/
    private void atualizarCreditoUsuarioDetalhes(ProgressDialog dialog) {

        CreditoUsuarioDetalhesDAO creditoUsuarioDetalhesDAO = new CreditoUsuarioDetalhesDAO();
        ArrayList<CreditoUsuarioDetalhes> listaCreditoUsuarioDetalhes = creditoUsuarioDetalhesDAO.listarCreditoUsuarioDetalhes(idEmpresa, Configuracao.getCnpj());

        dialog.setMax(listaCreditoUsuarioDetalhes.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaCreditoUsuarioDetalhes.size(); i++) {

            contentValues.clear();

            creditoUsuarioDetalhes = listaCreditoUsuarioDetalhes.get(i);

            contentValues.put("_id", creditoUsuarioDetalhes.getId());
            try {
                //contentValues.put("vencimento", format.format(contaReceber.getVencimento())); // Adicionando
                contentValues.put("data", creditoUsuarioDetalhes.getData()); // Adicionando
            }catch (Exception e){
                contentValues.put("data", ""); // Adicionando
            }
            contentValues.put("empresa_id", creditoUsuarioDetalhes.getEmpresa()); // Adicionando
            contentValues.put("valor", creditoUsuarioDetalhes.getValor()); // Adicionando
            contentValues.put("usuario_id", creditoUsuarioDetalhes.getUsuario()); // Adicionando
            contentValues.put("venda_detalhe_id", creditoUsuarioDetalhes.getVendaDetalhe()); // Adicionando

            db.insert("credito_usuario_detalhes", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O crédito usuario detalhes do usuario cod: " + creditoUsuarioDetalhes.getUsuario() + " foi criado com sucesso!");
            dialog.setProgress(i);
        }
    }
/*
    private void atualizarEnderecoCliente() {

        EnderecoClienteDAO enderecoClienteDAO = new EnderecoClienteDAO();
        ArrayList<EnderecoCliente> listaEnderecoCliente = enderecoClienteDAO.listarEnderecoCliente(Configuracao.getCnpj());

        for (int i = 0; i < listaEnderecoCliente.size(); i++) {

            contentValues.clear();

            enderecoCliente = listaEnderecoCliente.get(i);

            contentValues.put("_id", enderecoCliente.getId());
            contentValues.put("bairro", enderecoCliente.getBairro()); // Adicionando
            contentValues.put("cep", enderecoCliente.getCep()); // Adicionando
            contentValues.put("complemento", enderecoCliente.getComplemento()); // Adicionando
            contentValues.put("endereco", enderecoCliente.getEndereco()); // Adicionando
            contentValues.put("numero", enderecoCliente.getNumero()); // Adicionando
            contentValues.put("uf", enderecoCliente.getUf()); // Adicionando
            contentValues.put("cidade_id", enderecoCliente.getCidade()); // Adicionando
            contentValues.put("cliente_id", enderecoCliente.getCliente()); // Adicionando

            db.insert("endereco_cliente", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O Endereço do cliente cod: " + enderecoCliente.getCliente() + " foi criado com sucesso!");

        }
    }
*/
    private void atualizarFormaPagamento(ProgressDialog dialog) {

        FormaPagamentoDAO formaPagamentoDAO = new FormaPagamentoDAO();
        ArrayList<FormaPagamento> listaFormaPagamento = formaPagamentoDAO.listarFormaPagamento(idEmpresa, Configuracao.getCnpj());

        //Integer progresso = divisor / listaFormaPagamento.size();
        dialog.setMax(listaFormaPagamento.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaFormaPagamento.size(); i++) {

            contentValues.clear();

            formaPagamento = listaFormaPagamento.get(i);

            contentValues.put("_id", formaPagamento.getId());
            contentValues.put("juros", formaPagamento.getJuros()); // Adicionando
            contentValues.put("nome", formaPagamento.getNome()); // Adicionando
            contentValues.put("numero_dias", formaPagamento.getNumeroDias()); // Adicionando
            contentValues.put("numero_parcelas", formaPagamento.getNumeroParcelas()); // Adicionando
            contentValues.put("observacao", formaPagamento.getObservacao()); // Adicionando
            contentValues.put("valor_minimo", formaPagamento.getValorMinimo()); // Adicionando
            contentValues.put("geral", formaPagamento.getGeral()); // Adicionando
            contentValues.put("empresa_id", formaPagamento.getEmpresa()); // Adicionando

            db.insert("forma_pagamento", null, contentValues); //Salvando o cargo

            Log.i("Banco", "A forma de pagamento " + formaPagamento.getNome() + " com " + formaPagamento.getNumeroParcelas() + " parcelas foi criado com sucesso!");

            dialog.setProgress(i);
        }
    }

    private void atualizarProduto(ProgressDialog dialog) {

        ProdutoDAO produtoDAO = new ProdutoDAO();
        ArrayList<Produto> listaProdutos = produtoDAO.listarProduto(idEmpresa, Configuracao.getCnpj());

        dialog.setMax(listaProdutos.size());
        dialog.setProgress(0);

        Log.i("PRODUTOS", "Tamanho da lista de produtos: " + listaProdutos.size());

        for (int i = 0; i < listaProdutos.size(); i++) {

            contentValues.clear();

            //produto = listaProdutos.get(i);

            contentValues.put("_id", listaProdutos.get(i).getId());
            contentValues.put("codigo", listaProdutos.get(i).getCodigo()); // Adicionando
            contentValues.put("nome", listaProdutos.get(i).getNome()); // Adicionando
            contentValues.put("estoque", listaProdutos.get(i).getEstoque()); // Adicionando
            contentValues.put("expositor", listaProdutos.get(i).getExpositor()); // Adicionando
            contentValues.put("valor_desejavel_venda", listaProdutos.get(i).getValorDesejavelVenda()); // Adicionando
            contentValues.put("valor_minimo_venda", listaProdutos.get(i).getValorMinimoVenda()); // Adicionando
            contentValues.put("categoria_id", listaProdutos.get(i).getCategoria()); // Adicionando
            contentValues.put("unidade_id", listaProdutos.get(i).getUnidade()); // Adicionando
            contentValues.put("ativo", listaProdutos.get(i).getAtivo()); // Adicionando
            contentValues.put("peso", listaProdutos.get(i).getPeso()); // Adicionando
            contentValues.put("empresa_id", listaProdutos.get(i).getEmpresa()); // Adicionando
            contentValues.put("imagem", listaProdutos.get(i).getImagem()); // Adicionando
            contentValues.put("codigo_ref", listaProdutos.get(i).getCodigo_ref()); // Adicionando

            db.insert("produto", null, contentValues); //Salvando o produto

            Log.i("Banco", "O Produto " + listaProdutos.get(i).getNome() + " com o expositor de " + listaProdutos.get(i).getExpositor() + " e código " + listaProdutos.get(i).getCodigo() + " foi criado com sucesso!");

            dialog.setProgress(i);

        }
    }

    private void atualizarUsuario(ProgressDialog dialog) {

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> listaUsuarios = usuarioDAO.listarUsuario(Configuracao.getCnpj());

        //Integer progresso = divisor / listaUsuarios.size();
        //dialog.setMessage("Atualizando os usuarios, favor aguardar....");
         dialog.setProgress(1);

        for (int i = 0; i < listaUsuarios.size(); i++) {

            contentValues.clear();

            usuario = listaUsuarios.get(i);

            contentValues.put("_id", usuario.getId());
            contentValues.put("ativo", String.valueOf(usuario.getAtivo())); // Adicionando
            contentValues.put("nome", usuario.getNome()); // Adicionando
            contentValues.put("senha", usuario.getSenha()); // Adicionando
            contentValues.put("email", usuario.getEmail()); // Adicionando
            contentValues.put("credito", usuario.getCredito()); // Adicionando
            contentValues.put("telefone", usuario.getTelefone()); // Adicionando
            //contentValues.put("cargo_id", usuario.getCargo()); // Adicionando
            contentValues.put("rota_id", usuario.getRota()); // Adicionando
            contentValues.put("empresa_id", usuario.getEmpresa()); // Adicionando

            db.insert("usuario", null, contentValues); //Salvando o cargo

            Log.i("Banco", "O usuario " + usuario.getNome() + " com Empresa_id " + usuario.getEmpresa() + " e senha " + usuario.getSenha() + " foi criado com sucesso!");

            dialog.setProgress(i);

        }
    }

    private int buscaIdEmpresaUsuarioLogado(){
        int retorno = 0;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getInt(6);
        }catch (Exception e){
            retorno = 0;
        }

        Log.i("Banco", "O usuario logado tem o id: " + cursor.getInt(6));
        return retorno;
    }

    private void atualizarEmpresa(ProgressDialog dialog) {

        Log.i("Banco", "Entrei no atualizar empresa");

        EmpresaDAO empresaDAO = new EmpresaDAO();
        empresa = empresaDAO.buscaEmpresa(idEmpresa, Configuracao.getCnpj());

        //dialog.setMessage("Atualizando a empresa, favor aguardar....");
        dialog.setMax(1);
        dialog.setProgress(0);

//        for (int i = 0; i < listaUsuarios.size(); i++) {

        contentValues.clear();

//            usuario = listaUsuarios.get(i);

        contentValues.put("_id", empresa.getId());
        contentValues.put("razaoSocial", empresa.getRazaoSocial());
        contentValues.put("fantasia", empresa.getFantasia()); // Adicionando
        contentValues.put("cnpj", empresa.getCnpj()); // Adicionando
        contentValues.put("inscricao_estadual", empresa.getInscricaoEstadual()); // Adicionando
        contentValues.put("telefone_1", empresa.getTelefone1()); // Adicionando
        contentValues.put("telefone_2", empresa.getTelefone2()); // Adicionando
        contentValues.put("endereco", empresa.getEndereco()); // Adicionando
        contentValues.put("endereco_numero", empresa.getEnderecoNumero()); // Adicionando
        contentValues.put("bairro", empresa.getBairro()); // Adicionando
        contentValues.put("cidade_id", empresa.getCidade()); // Adicionando
        contentValues.put("estado_id", empresa.getEstado()); // Adicionando
        contentValues.put("email", empresa.getEmail()); // Adicionando
        contentValues.put("complemento", empresa.getComplemento()); // Adicionando

        db.insert("empresa", null, contentValues); //Salvando o cargo

        Log.i("Banco", "A Empresa " + empresa.getRazaoSocial() + " foi criada com sucesso!");

        dialog.setProgress(1);

//        }
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

    private Date dataAtual() {

        Date hoje = new Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }

    private boolean verificarClienteNovo(){

        boolean retorno = false;

        cursor = db
                .rawQuery(
                        "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                "rota_id, empresa_id, novo, alterado FROM cliente where novo like '1'", null);

        if (cursor.moveToFirst()) {
            retorno = true;
        }else{
            retorno = false;
        }
        return retorno;
    }

    private boolean verificarBancoCidades(){
        boolean retorno = false;

        cursor = db
                .rawQuery(
                        "SELECT * FROM cidade", null);

        if (cursor.moveToFirst()) {
            retorno = true;
        }else{
            retorno = false;
        }

        return retorno;
    }

    private boolean verificarBancoEstado(){
        boolean retorno = false;

        cursor = db
                .rawQuery(
                        "SELECT * FROM estado", null);

        if (cursor.moveToFirst()) {
            retorno = true;
        }else{
            retorno = false;
        }

        return retorno;
    }
}
