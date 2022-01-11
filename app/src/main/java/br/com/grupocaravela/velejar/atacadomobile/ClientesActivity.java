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
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.ClientesFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Categoria;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CategoriaCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.RotaVendedor;

public class ClientesActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private SimpleDateFormat dateformatSoap = new SimpleDateFormat("yyyy-MM-dd");

    private int posicao = 0;
    private boolean finalLista = false;
    private Toolbar mainToolbarTop;

    private ImageLoader mImageLoader;
    private Spinner spinnerCategoria;

    //private List<RotaVendedor> rotaVendedorList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clientes);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Lista de clientes");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //#####################################
        /*
        List<CategoriaCliente> listAux = getCategoriaClienteList();

        ArrayAdapter<CategoriaCliente> adapterCategotia = new ArrayAdapter<CategoriaCliente>(this, android.R.layout.simple_spinner_item, listAux);
        adapterCategotia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = findViewById(R.id.spinnerCategoriasPesquisaClientes);
        spinnerCategoria.setAdapter(adapterCategotia);
*/
        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(ClientesActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        listarClientes();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_clientes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (item.getItemId()) {

            case R.id.action_cadastrar_cliente:
                //##############################################
                Intent it = null;
                it = new Intent(this, CadastroClienteActivity.class);
                //startActivityForResult(it, 01);
                startActivity(it);
                //##############################################
                break;
        }
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

    private Integer buscaRotaUsuarioLogado(){
        Integer retorno = 0;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, usuario_id, rota_id FROM rota_vendedor where usuario_id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getInt(3);
        }catch (Exception e){
            retorno = 0;
        }

        return retorno;
    }

    public List<Cliente> getSetClienteListNome(String razaoSocial, CategoriaCliente categoriaCliente) {
        List<Cliente> listAux = new ArrayList<>();

        ArrayList<RotaVendedor> rotaVendedorList = new ArrayList<>();
        rotaVendedorList = getRotasVendedorList();

        try {
            if (rotaVendedorList != null && rotaVendedorList.size() > 0) {
                for (int i = 0; i < rotaVendedorList.size(); i++) {

                    Log.i("PESQUISA", "1 PESQUISANDO PELA ROTA DO USUARIO " + rotaVendedorList.get(i).getRota() + " ");

                    if (categoriaCliente != null) {
                        cursor = db
                                .rawQuery(
                                        "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                                "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                                "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE razaoSocial LIKE '%" + razaoSocial +
                                                "%' AND categoria_cliente_id LIKE '" + categoriaCliente.getId() + "' AND rota_id LIKE '" + rotaVendedorList.get(i).getRota() +
                                                "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null);
                        //AND rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
                    } else {
                        cursor = db
                                .rawQuery(
                                        "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                                "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                                "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE razaoSocial LIKE '%" + razaoSocial +
                                                "%' AND rota_id LIKE '" + rotaVendedorList.get(i).getRota() + "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null);
                        //AND rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
                    }

                    if (cursor.moveToFirst()) {

                        for (int iZ = 0; iZ < cursor.getCount(); iZ++) {

                            Cliente c = new Cliente();

                            c.setId(cursor.getLong(0));
                            c.setRazaoSocial(cursor.getString(1));
                            c.setFantasia(cursor.getString(2));
                            c.setInscricaoEstadual(cursor.getString(3));
                            c.setCpf(cursor.getString(4));
                            c.setCnpj(cursor.getString(5));
                            try {
                                c.setDataNascimento(cursor.getString(6));
                            } catch (Exception e) {
                                c.setDataNascimento(null);
                            }
                            try {
                                c.setDataCadastro(cursor.getString(7));
                            } catch (Exception e) {
                                c.setDataCadastro(null);
                            }
                            c.setEmail(cursor.getString(8));
                            c.setLimiteCredito(cursor.getDouble(9));
                            //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                            Log.d("IMPORTANTE", "ATIVO++++: " + cursor.getString(10));

                            if (cursor.getString(10).equals("1")) {
                                c.setAtivo(true);
                            } else {
                                c.setAtivo(false);
                            }

                            c.setObservacao(cursor.getString(11));
                            c.setTelefone1(cursor.getString(12));
                            c.setTelefone2(cursor.getString(13));
                            c.setEndereco(cursor.getString(14));
                            c.setEnderecoNumero(cursor.getString(15));
                            c.setComplemento(cursor.getString(16));
                            c.setBairro(cursor.getString(17));
                            c.setCidade_id(cursor.getLong(18));
                            c.setEstado_id(cursor.getLong(19));
                            c.setCep(cursor.getString(20));
                            c.setRota(cursor.getLong(21));
                            c.setEmpresa(cursor.getLong(22));
                            c.setNovo(Boolean.parseBoolean(cursor.getString(23)));
                            c.setAlterado(Boolean.parseBoolean(cursor.getString(24)));
                            c.setImagem(cursor.getBlob(25));

                            //c.setAtivo(cursor.getInt(4));
                            listAux.add(c);

                            cursor.moveToNext();
                        }
                    }

                }
            }else {

                if (categoriaCliente != null) {
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE razaoSocial LIKE '%" + razaoSocial + "%' AND categoria_cliente_id LIKE '" + categoriaCliente.getId() + "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null);
                    //AND rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
                } else {
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE razaoSocial LIKE '%" + razaoSocial + "%' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null);
                    //AND rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
                }


        if (cursor.moveToFirst()) {

            for (int i = 0; i < cursor.getCount(); i++) {

                Cliente c = new Cliente();

                c.setId(cursor.getLong(0));
                c.setRazaoSocial(cursor.getString(1));
                c.setFantasia(cursor.getString(2));
                c.setInscricaoEstadual(cursor.getString(3));
                c.setCpf(cursor.getString(4));
                c.setCnpj(cursor.getString(5));
                try {
                    c.setDataNascimento(cursor.getString(6));
                } catch (Exception e) {
                    c.setDataNascimento(null);
                }
                try {
                    c.setDataCadastro(cursor.getString(7));
                } catch (Exception e) {
                    c.setDataCadastro(null);
                }
                c.setEmail(cursor.getString(8));
                c.setLimiteCredito(cursor.getDouble(9));
                //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                Log.d("IMPORTANTE", "ATIVO++++: " + cursor.getString(10));

                if (cursor.getString(10).equals("1")) {
                    c.setAtivo(true);
                } else {
                    c.setAtivo(false);
                }

                c.setObservacao(cursor.getString(11));
                c.setTelefone1(cursor.getString(12));
                c.setTelefone2(cursor.getString(13));
                c.setEndereco(cursor.getString(14));
                c.setEnderecoNumero(cursor.getString(15));
                c.setComplemento(cursor.getString(16));
                c.setBairro(cursor.getString(17));
                c.setCidade_id(cursor.getLong(18));
                c.setEstado_id(cursor.getLong(19));
                c.setCep(cursor.getString(20));
                c.setRota(cursor.getLong(21));
                c.setEmpresa(cursor.getLong(22));
                c.setNovo(Boolean.parseBoolean(cursor.getString(23)));
                c.setAlterado(Boolean.parseBoolean(cursor.getString(24)));
                c.setImagem(cursor.getBlob(25));

                //c.setAtivo(cursor.getInt(4));
                listAux.add(c);

                cursor.moveToNext();
            }
        }
/*
            if (finalLista == false) {

                for (int y = 0; y < 7; y++) {
                    if (posicao == listClientes.size()) {
                        //posicao = 1;
                        finalLista = true;
                    } else {
                        listAux.add(listClientes.get(posicao));
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

    public List<Cliente> getSetClienteList(int qtd, CategoriaCliente categoriaCliente) {
        List<Cliente> listAux = new ArrayList<>();
        ArrayList<Cliente> listCliente = new ArrayList<>();

        ArrayList<RotaVendedor> rotaVendedorList = new ArrayList<>();
        rotaVendedorList = getRotasVendedorList();

        if (rotaVendedorList != null && rotaVendedorList.size() > 0) {
            for (int i = 0; i < rotaVendedorList.size(); i++) {

                Log.i("PESQUISA", "PESQUISANDO PELA ROTA DO USUSARIO " + rotaVendedorList.get(i).getRota() + " ");

                if (categoriaCliente != null) {
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE categoria_cliente_id LIKE '" + categoriaCliente.getId() +
                                            "' AND rota_id LIKE '" + rotaVendedorList.get(i).getRota() + "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'

                } else {
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE rota_id LIKE '" + rotaVendedorList.get(i).getRota() +
                                            "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
                }

                if (cursor.moveToFirst()) {

                    for (int iZ = 0; iZ < cursor.getCount(); iZ++) {

                        Cliente c = new Cliente();

                        c.setId(cursor.getLong(0));
                        c.setRazaoSocial(cursor.getString(1));
                        c.setFantasia(cursor.getString(2));
                        c.setInscricaoEstadual(cursor.getString(3));
                        c.setCpf(cursor.getString(4));
                        c.setCnpj(cursor.getString(5));
                        try {
                            c.setDataNascimento(cursor.getString(6));
                        } catch (Exception e) {
                            c.setDataNascimento(null);
                        }
                        try {
                            c.setDataCadastro(cursor.getString(7));
                        } catch (Exception e) {
                            c.setDataCadastro(null);
                        }
                        c.setEmail(cursor.getString(8));
                        c.setLimiteCredito(cursor.getDouble(9));
                        //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                        Log.d("IMPORTANTE", "ATIVO++++: " + cursor.getString(10));

                        if (cursor.getString(10).equals("1")) {
                            c.setAtivo(true);
                        } else {
                            c.setAtivo(false);
                        }

                        c.setObservacao(cursor.getString(11));
                        c.setTelefone1(cursor.getString(12));
                        c.setTelefone2(cursor.getString(13));
                        c.setEndereco(cursor.getString(14));
                        c.setEnderecoNumero(cursor.getString(15));
                        c.setComplemento(cursor.getString(16));
                        c.setBairro(cursor.getString(17));
                        c.setCidade_id(cursor.getLong(18));
                        c.setEstado_id(cursor.getLong(19));
                        c.setCep(cursor.getString(20));
                        c.setRota(cursor.getLong(21));
                        c.setEmpresa(cursor.getLong(22));
                        c.setNovo(Boolean.parseBoolean(cursor.getString(23)));
                        c.setAlterado(Boolean.parseBoolean(cursor.getString(24)));
                        c.setImagem(cursor.getBlob(25));

                        //c.setAtivo(cursor.getInt(4));
                        listAux.add(c);

                        cursor.moveToNext();
                    }
                }
            }
        }else {
            if (categoriaCliente != null) {
                cursor = db
                        .rawQuery(
                                "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                        "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                        "rota_id, empresa_id, novo, alterado, imagem FROM cliente WHERE categoria_cliente_id LIKE '" + categoriaCliente.getId() + "' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'

            } else {
                cursor = db
                        .rawQuery(
                                "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                        "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                        "rota_id, empresa_id, novo, alterado, imagem FROM cliente ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 50", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'
            }

        if (cursor.moveToFirst()) {

            int tamanhoListaCliente = cursor.getCount();

            for (int i = 0; i < tamanhoListaCliente; i++) {

                Cliente c = new Cliente();

                c.setId(cursor.getLong(0));
                c.setRazaoSocial(cursor.getString(1));
                c.setFantasia(cursor.getString(2));
                c.setInscricaoEstadual(cursor.getString(3));
                c.setCpf(cursor.getString(4));
                c.setCnpj(cursor.getString(5));
                try {
                    c.setDataNascimento(cursor.getString(6));
                } catch (Exception e) {
                    c.setDataNascimento(null);
                }
                try {
                    c.setDataCadastro(cursor.getString(7));
                } catch (Exception e) {
                    c.setDataCadastro(null);
                }
                c.setEmail(cursor.getString(8));
                c.setLimiteCredito(cursor.getDouble(9));
                //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                Log.d("IMPORTANTE", "ATIVO+-+-+: " + cursor.getString(10));

                if (cursor.getString(10).equals("1")) {
                    c.setAtivo(true);
                } else {
                    c.setAtivo(false);
                }

                c.setObservacao(cursor.getString(11));
                c.setTelefone1(cursor.getString(12));
                c.setTelefone2(cursor.getString(13));
                c.setEndereco(cursor.getString(14));
                c.setEnderecoNumero(cursor.getString(15));
                c.setComplemento(cursor.getString(16));
                c.setBairro(cursor.getString(17));
                c.setCidade_id(cursor.getLong(18));
                c.setEstado_id(cursor.getLong(19));
                c.setCep(cursor.getString(20));
                c.setRota(cursor.getLong(21));
                c.setEmpresa(cursor.getLong(22));
                c.setNovo(Boolean.parseBoolean(cursor.getString(23)));
                c.setAlterado(Boolean.parseBoolean(cursor.getString(24)));
                c.setImagem(cursor.getBlob(25));

                listCliente.add(c);

                cursor.moveToNext();
            }
        }

        /*
            Log.i("TAMANHO", "TAMANHO DO LISTCLIENTE " + listCliente.size() + " ");
            if (finalLista == false) {

                for (int y = 0; y < qtd; y++) {
                    if (posicao == listCliente.size()) {
                        //posicao = 1;
                        finalLista = true;
                    } else {
                        listAux.add(listCliente.get(posicao));
                        posicao++;
                        //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                    }

                }
            }
            */
            //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
            //Log.i("POSICAO", "POSICAO " + posicao + " ");
        }
        return (listAux);
    }

    public List<CategoriaCliente> getCategoriaClienteList() {
        ArrayList<CategoriaCliente> listCategoriaCliente = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, nome, empresa_id FROM categoria_cliente ORDER BY nome COLLATE NOCASE ASC", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'

        if (cursor.moveToFirst()) {

            int tamanhoListaCategoria = cursor.getCount();

            CategoriaCliente catVazia = new CategoriaCliente(null, "Selecione a categoria", null);
            listCategoriaCliente.add(catVazia);

            for (int i = 0; i < tamanhoListaCategoria; i++) {

                CategoriaCliente c = new CategoriaCliente();

                c.setId(cursor.getLong(0));
                c.setNome(cursor.getString(1));
                c.setEmpresa(cursor.getLong(2));

                listCategoriaCliente.add(c);

                cursor.moveToNext();
            }

            Log.i("TAMANHO", "TAMANHO DO LISTCATEGORIA " + listCategoriaCliente.size() + " ");

            //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
            //Log.i("POSICAO", "POSICAO " + posicao + " ");
        }
        return (listCategoriaCliente);
    }

    public ArrayList<RotaVendedor> getRotasVendedorList() {
        ArrayList<RotaVendedor> rotaVendedorList = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, rota_id, usuario_id, empresa_id FROM rota_vendedor WHERE usuario_id = '" + buscaIdUsuarioLogado() + "'", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'

        if (cursor.moveToFirst()) {

            int tamanhoListaCategoria = cursor.getCount();

            for (int i = 0; i < tamanhoListaCategoria; i++) {

                RotaVendedor c = new RotaVendedor();

                c.setId(cursor.getLong(0));
                c.setRota(cursor.getLong(1));
                c.setUsuario(cursor.getLong(2));
                c.setEmpresa(cursor.getLong(3));

                rotaVendedorList.add(c);

                cursor.moveToNext();
            }

            Log.i("TAMANHO", "TAMANHO DO LIST ROTA VENDEDOR  " + rotaVendedorList.size() + " ");

            //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
            //Log.i("POSICAO", "POSICAO " + posicao + " ");
        }
        return (rotaVendedorList);
    }

    private Integer buscaIdUsuarioLogado(){
        Integer retorno = 0;

        Cursor cursor;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            Log.i("LOG", "ID USUARIO: " + cursor.getInt(1) + " ");
            retorno = cursor.getInt(1);
        }catch (Exception e){
            retorno = null;
        }

        return retorno;
    }

    private void listarClientes() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                ClientesFragment frag = (ClientesFragment) getSupportFragmentManager().findFragmentByTag("cliFrag");
                if (frag == null) {
                    frag = new ClientesFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_clientes, frag, "cliFrag");
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
