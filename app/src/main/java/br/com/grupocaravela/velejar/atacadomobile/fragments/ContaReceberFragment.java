package br.com.grupocaravela.velejar.atacadomobile.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.ClientesActivity;
import br.com.grupocaravela.velejar.atacadomobile.ContasReceberActivity;
import br.com.grupocaravela.velejar.atacadomobile.DetalhesContaReceberActivity;
import br.com.grupocaravela.velejar.atacadomobile.Util.FileUtils;
import br.com.grupocaravela.velejar.atacadomobile.Util.PermissionsActivity;
import br.com.grupocaravela.velejar.atacadomobile.Util.PermissionsChecker;
import br.com.grupocaravela.velejar.atacadomobile.adapters.ClientesAdapter;
import br.com.grupocaravela.velejar.atacadomobile.adapters.ContasReceberAdapter;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CategoriaCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;

import static br.com.grupocaravela.velejar.atacadomobile.Util.LogUtils.LOGE;
import static br.com.grupocaravela.velejar.atacadomobile.Util.PermissionsActivity.PERMISSION_REQUEST_CODE;
import static br.com.grupocaravela.velejar.atacadomobile.Util.PermissionsChecker.REQUIRED_PERMISSION;

/**
 * Created by fabio on 16/07/15.
 */
public class ContaReceberFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private DBHelper dbHelper;
    //private Cursor l;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private AlertDialog alerta;

    private RecyclerView mRecyclerView;
    private List<ContaReceber> mList;
    private EditText edtLocalizarContaReceber;
    private Spinner spinnerCategoria;
    private ImageView ivLocalizarContaReceber;

    private Double valorAbater;

    private boolean buncando = false;

    private CategoriaCliente categoriaCliente = null;

    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    PermissionsChecker checker;

    private Cursor cursor;

    private String codContaReceber;
    private String dataContaReceber;
    private String dataVencimento;
    private String valorContaReceber;
    private String valorContaReceberDesconto;
    private String valorContaReceberSaldo;
    private Cliente cliente;

    private ContasReceberAdapter adapterContasReceber;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contas_receber, container, false);

        dbHelper = new DBHelper(getActivity(), "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        checker = new PermissionsChecker(getContext());

        //createPdf(FileUtils.getAppPath(getContext()) + "123.pdf");

        edtLocalizarContaReceber = (EditText) view.findViewById(R.id.edt_localizar_contas_receber);

        List<CategoriaCliente> listAux = ((ContasReceberActivity) getActivity()).getCategoriaClienteList();

        ArrayAdapter<CategoriaCliente> adapterCategotia = new ArrayAdapter<CategoriaCliente>(getActivity(), android.R.layout.simple_spinner_item, listAux);
        adapterCategotia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = view.findViewById(R.id.spinnerCategoriasPesquisaContasReceber);
        spinnerCategoria.setAdapter(adapterCategotia);

        spinnerCategoria.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (spinnerCategoria.getSelectedItemPosition() != 0){
                    categoriaCliente = (CategoriaCliente) spinnerCategoria.getSelectedItem();
                    Toast.makeText(getContext(), "CATEGORIA SELECIONDA: " + categoriaCliente.getNome(), Toast.LENGTH_LONG).show();
                }else{
                    categoriaCliente = null;
                }

                String nome = String.valueOf(edtLocalizarContaReceber.getText());

                mList = ((ContasReceberActivity) getActivity()).getSetContaReceberListNome(nome, categoriaCliente);
                adapterContasReceber = new ContasReceberAdapter(getActivity(), mList, ImageLoader.getInstance());
                //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
                mRecyclerView.setAdapter(adapterContasReceber);

                if (nome == "") {
                    buncando = false;
                } else {
                    buncando = true;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ivLocalizarContaReceber = (ImageView) view.findViewById(R.id.iv_localizar_contas_receber);
        ivLocalizarContaReceber.setBackgroundResource(R.drawable.buscar_azul_48x48);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_contas_receber_list); //Dentro de "fragment_empresa"
        mRecyclerView.setHasFixedSize(true); //Definindo o tamanho do recleView como sempre o mesmo
        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //Log.i("PASSEI", "AKI 00 ");

                if (buncando == false) {

                    //LinearLayoutManager para a Lista RecycleView
                    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    ContasReceberAdapter adapter = (ContasReceberAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
                        //List<ContaReceber> listAux = ((ContasReceberActivity) getActivity()).getSetContaReceberList(7);
                        List<ContaReceber> listAux = ((ContasReceberActivity) getActivity()).getSetContaReceberList(categoriaCliente);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }

                    }

                }

                String nome = String.valueOf(edtLocalizarContaReceber.getText());

                //if (nome.length() > 2){

                if (buncando == true) {

                        //LinearLayoutManager para a Lista RecycleView
                        LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                        ContasReceberAdapter adapter = (ContasReceberAdapter) mRecyclerView.getAdapter();

                        //Log.i("PASSEI", "AKI 01 ");

                        if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
/*
                            List<ContaReceber> listAux = ((ContasReceberActivity) getActivity()).getSetContaReceberListNome(nome);

                            for (int i = 0; i < listAux.size(); i++) {
                                adapter.addListItem(listAux.get(i), mList.size());
                            }
*/
                        }
                    }
               // }
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this)); //Colocando novo dectar clique

        //LinearLayoutManager para a Lista RecycleView
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayout.setReverseLayout(true); //Inverte a lista
        mRecyclerView.setLayoutManager(linearLayout);

        if (buncando == false) {

            //mList = ((ContasReceberActivity) getActivity()).getSetContaReceberList(7);
            mList = ((ContasReceberActivity) getActivity()).getSetContaReceberList(categoriaCliente);
            ContasReceberAdapter adapter = new ContasReceberAdapter(getActivity(), mList, ImageLoader.getInstance());
            adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
            mRecyclerView.setAdapter(adapter);

        }

        //#################### INICIO DO FILTRO PELA BUSCA ###########################
        edtLocalizarContaReceber.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String nome = String.valueOf(edtLocalizarContaReceber.getText());

               // if (nome.length() > 2) {

                    mList = ((ContasReceberActivity) getActivity()).getSetContaReceberListNome(nome, categoriaCliente);
                    ContasReceberAdapter adapter = new ContasReceberAdapter(getActivity(), mList, ImageLoader.getInstance());
                    //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
                    mRecyclerView.setAdapter(adapter);

                    if (nome == "") {
                        buncando = false;
                    } else {
                        buncando = true;
                    }
               // }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        //#################### FIM DO FILTRO PELA BUSCA ###########################

        return view;
    }

    //################ Inicio Metodos do clique e longClique ##############
    @Override
    public void onClickListener(View view, int position) {

        Intent it = new Intent(getActivity(), DetalhesContaReceberActivity.class);
        it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        startActivity(it);

    }

    @Override
    public void onLongClickListener(View view, int position) {

        listaOpcoes(position);
    }
    //################ Fim Metodos do clique e longClique ##############

    // LISTAR OPCOES
    private void listaOpcoes(final int position) {
        // Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Quitar Nota");
        itens.add("Abater");
        itens.add("Observação");

        // adapter utilizando um layout customizado (TextView)
        ArrayAdapter adapterArray = new ArrayAdapter(getActivity(), R.layout.list_opcoes, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Opções de contas a receber");

        // define o di�logo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapterArray, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // #################### OPCAO DE EXCLUIR
                        // #####################

                        try {
                            codContaReceber = mList.get(position).getId().toString();
                            dataVencimento = mList.get(position).getVencimento();
                            dataContaReceber = formatSoapHora.format(dataAtual());
                            valorContaReceber =  String.format("%.2f", mList.get(position).getValorDevido());
                            valorContaReceberDesconto =  String.format("%.2f", mList.get(position).getValorDesconto());
                            valorContaReceberSaldo =  String.format("%.2f", (mList.get(position).getValorDevido() - mList.get(position).getValorDesconto()));
                            cliente = new Cliente();

                            //Configuração inicial
                            //dbHelper = new DBHelper(getContext(), "velejar.db", 1); // Banco
                            //db = dbHelper.getWritableDatabase(); // Banco

                            cursor = db.rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado FROM cliente where _id like '" + mList.get(position).getCliente().toString() + "'", null);


                            //"SELECT _id, razaoSocial, endereco, endereco_numero, bairro FROM cliente where _id like '" + mList.get(position).getCliente().toString() + "'", null);
                            if (cursor.moveToFirst()) {
                                cliente.setId(cursor.getLong(0));
                                cliente.setRazaoSocial(cursor.getString(1));
                                cliente.setFantasia(cursor.getString(2));
                                cliente.setInscricaoEstadual(cursor.getString(3));
                                cliente.setCpf(cursor.getString(4));
                                cliente.setCnpj(cursor.getString(5));
                                try {
                                    cliente.setDataNascimento(cursor.getString(6));
                                }catch (Exception e){
                                    cliente.setDataNascimento(null);
                                }
                                try {
                                    cliente.setDataCadastro(cursor.getString(7));
                                }catch (Exception e){
                                    cliente.setDataCadastro(null);
                                }
                                cliente.setEmail(cursor.getString(8));
                                cliente.setLimiteCredito(cursor.getDouble(9));
                                //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                                Log.d("IMPORTANTE", "ATIVO+-+-+: " + cursor.getString(10));

                                if (cursor.getString(10).equals("1")) {
                                    cliente.setAtivo(true);
                                }else{
                                    cliente.setAtivo(false);
                                }

                                cliente.setObservacao(cursor.getString(11));
                                cliente.setTelefone1(cursor.getString(12));
                                cliente.setTelefone2(cursor.getString(13));
                                cliente.setEndereco(cursor.getString(14));
                                cliente.setEnderecoNumero(cursor.getString(15));
                                cliente.setComplemento(cursor.getString(16));
                                cliente.setBairro(cursor.getString(17));
                                cliente.setCidade_id(cursor.getLong(18));
                                cliente.setEstado_id(cursor.getLong(19));
                                cliente.setCep(cursor.getString(20));
                                cliente.setRota(cursor.getLong(21));
                                cliente.setEmpresa(cursor.getLong(22));
                                cliente.setNovo(Boolean.parseBoolean(cursor.getString(23)));
                                cliente.setAlterado(Boolean.parseBoolean(cursor.getString(24)));
                                //Log.i("INFO", "CLIENTE NOME: " + cursor.getString(1));
                            }

                        } catch (Exception e){
                            Log.i("ERRO", "Erro ao buscar cliente!");
                        }

                        if (arg1 == 0) {

                            //##############################################
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("QUITAR A CONTA");

                            alertDialogBuilder
                                    .setMessage(
                                            "Gostaria de quitar a nota do cliente " + cliente.getRazaoSocial() + " no valor de saldo R$ " + valorContaReceberSaldo + "?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sim",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    boolean bAndroidCaixa = false;
                                                    boolean bAndroidContaReceber = false;

                                                    try {

                                                        //CRIAR ANDROID CAIXA
                                                        contentValues = new ContentValues();
                                                        contentValues.put("data_recebimento", formatSoapHora.format(dataAtual()));
                                                        contentValues.put("data_transmissao", "null");
                                                        contentValues.put("valor", (mList.get(position).getValorDevido() - mList.get(position).getValorDesconto()));
                                                        contentValues.put("android_venda_cabecalho_id", "null");
                                                        contentValues.put("venda_cabecalho_id", mList.get(position).getVendaCabecalho());
                                                        contentValues.put("conta_receber_id", mList.get(position).getId());
                                                        contentValues.put("empresa_id", buscaIdEmpresaUsuarioLogado());

                                                        try {
                                                            Cursor cursor = db
                                                                    .rawQuery(
                                                                            "SELECT _id, id_usuario FROM usuario_logado where _id like '1'", null);

                                                            cursor.moveToFirst();

                                                            contentValues.put("usuario_id", cursor.getInt(1));
                                                        }catch (Exception e){
                                                            contentValues.put("usuario_id", "null");
                                                        }

                                                        contentValues.put("cliente_id", mList.get(position).getCliente());
                                                        Log.i("INFO", "Cliente nº " + mList.get(position).getCliente());

                                                        db.insert("android_caixa", null, contentValues); //SALVANDO A VENDA CABEÇALHO
                                                        bAndroidCaixa = true;
                                                        Log.i("INFO", "Caixa criado com sucesso!");
                                                    } catch (Exception e){
                                                        Log.i("ERRO", "Erro ao criar caixa!");
                                                        bAndroidCaixa = false;
                                                    }

                                                    try {
                                                        //CRIAR ANDROID CONTA RECEBER (RECEBIDA)
                                                        contentValues = new ContentValues();
                                                        contentValues.put("dataTransmissao", "null");
                                                        contentValues.put("dataRecebimento", formatSoapHora.format(dataAtual()));
                                                        contentValues.put("valor_devido", mList.get(position).getValorDevido());
                                                        contentValues.put("cliente_id", Integer.parseInt(mList.get(position).getCliente().toString()));

                                                        try {
                                                            Cursor cursor = db
                                                                    .rawQuery(
                                                                            "SELECT _id, id_usuario FROM usuario_logado where _id like '1'", null);

                                                            cursor.moveToFirst();

                                                            contentValues.put("usuario_id", cursor.getInt(1));
                                                        }catch (Exception e){
                                                            contentValues.put("usuario_id", "null");
                                                        }

                                                        contentValues.put("venda_cabecalho_id", Integer.parseInt(mList.get(position).getId().toString()));

                                                        db.insert("android_conta_receber", null, contentValues); //SALVANDO A VENDA CABEÇALHO
                                                        bAndroidContaReceber = true;
                                                        Log.i("INFO", "Conta android recebida criada com sucesso!");
                                                    } catch (Exception e){
                                                        Log.i("Erro", "Erro ao criar conta android recebida!");
                                                        bAndroidContaReceber = false;
                                                    }

                                                    if (bAndroidCaixa == true && bAndroidContaReceber == true){
                                                        //SE TUDO DEU CERTO, APAGO CONTA A RECEBER
                                                        db.delete("conta_receber", "_id=?", new String[]{String.valueOf(mList.get(position).getId())});

                                                        Toast.makeText(getActivity(), "Conta " + cliente.getRazaoSocial() + " recebida com sucesso!!!", Toast.LENGTH_SHORT).show();
                                                    }

                                                    mList.remove(position);
                                                    adapterContasReceber.notifyDataSetChanged();

                                                    if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                                                        PermissionsActivity.startActivityForResult(getActivity(), PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
                                                    } else {
                                                        createPdf(Environment.getExternalStorageDirectory().getPath() + "/" + codContaReceber + ".pdf", codContaReceber, dataContaReceber, valorContaReceber, cliente, buscaNomeUsuario(), dataVencimento, valorContaReceberDesconto, valorContaReceberSaldo);
                                                    }
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

                        //############################# OPÇAO ABATER ################

                        if (arg1 == 1) {

                            //##############################################
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("ABATER A NOTA");

                            alertDialogBuilder
                                    .setMessage(
                                            "Gostaria de abater na nota mse20072do cliente " + cliente.getRazaoSocial() + " no valor de saldo R$ " + valorContaReceberSaldo + "?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sim",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    valorAbater = 0.0;

                                                    AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                                                    alert.setTitle("Valor a ser abatido");

                                                    final EditText input = new EditText(getActivity());
                                                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                                                    alert.setView(input);

                                                    alert.setPositiveButton("Ok",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog,
                                                                                    int whichButton) {
                                                                    //##################Inicio do desconto########################

                                                                    try {
                                                                        valorAbater = Double.parseDouble(input.getText().toString().replace(",", "."));
                                                                    } catch (Exception e) {
                                                                        valorAbater = 0.0;
                                                                    }

                                                                    boolean bAndroidCaixa = false;
                                                                    boolean bAndroidContaReceber = false;

                                                                    try {
                                                                        //CRIAR ANDROID CAIXA
                                                                        contentValues = new ContentValues();
                                                                        contentValues.put("data_recebimento", formatSoapHora.format(dataAtual()));
                                                                        contentValues.put("data_transmissao", "null");
                                                                        contentValues.put("valor", valorAbater);
                                                                        contentValues.put("android_venda_cabecalho_id", "null");
                                                                        contentValues.put("venda_cabecalho_id", mList.get(position).getVendaCabecalho());
                                                                        contentValues.put("conta_receber_id", mList.get(position).getId());
                                                                        contentValues.put("empresa_id", buscaIdEmpresaUsuarioLogado());

                                                                        try {
                                                                            Cursor cursor = db
                                                                                    .rawQuery(
                                                                                            "SELECT _id, id_usuario FROM usuario_logado where _id like '1'", null);

                                                                            cursor.moveToFirst();

                                                                            contentValues.put("usuario_id", cursor.getInt(1));
                                                                        }catch (Exception e){
                                                                            contentValues.put("usuario_id", "null");
                                                                        }

                                                                        contentValues.put("cliente_id", mList.get(position).getCliente());
                                                                        Log.i("INFO", "Cliente nº " + mList.get(position).getCliente());

                                                                        db.insert("android_caixa", null, contentValues); //SALVANDO A VENDA CABEÇALHO
                                                                        bAndroidCaixa = true;
                                                                        //Log.i("INFO", "Caixa criado com sucesso!");
                                                                    } catch (Exception e){
                                                                        Log.i("ERRO", "Erro ao criar caixa!");
                                                                        bAndroidCaixa = false;
                                                                    }

                                                                    try {
                                                                        //CRIAR ANDROID CONTA RECEBER (RECEBIDA)
                                                                        contentValues = new ContentValues();
                                                                        contentValues.put("dataTransmissao", "null");
                                                                        contentValues.put("dataRecebimento", formatSoapHora.format(dataAtual()));
                                                                        contentValues.put("valor_devido", valorAbater);
                                                                        contentValues.put("cliente_id", Integer.parseInt(mList.get(position).getCliente().toString()));
                                                                        try {
                                                                            Cursor cursor = db
                                                                                    .rawQuery(
                                                                                            "SELECT _id, id_usuario FROM usuario_logado where _id like '1'", null);

                                                                            cursor.moveToFirst();

                                                                            contentValues.put("usuario_id", cursor.getInt(1));
                                                                        }catch (Exception e){
                                                                            contentValues.put("usuario_id", "null");
                                                                        }
                                                                        contentValues.put("venda_cabecalho_id", Integer.parseInt(mList.get(position).getId().toString()));

                                                                        db.insert("android_conta_receber", null, contentValues); //SALVANDO A VENDA CABEÇALHO
                                                                        bAndroidContaReceber = true;
                                                                        //Log.i("INFO", "Conta android recebida criada com sucesso!");
                                                                    } catch (Exception e){
                                                                        Log.i("Erro", "Erro ao criar conta android recebida!");
                                                                        bAndroidContaReceber = false;
                                                                    }

                                                                    if (bAndroidCaixa == true && bAndroidContaReceber == true){
                                                                        Double valorTotal = mList.get(position).getValorDevido();

                                                                        Double restante = valorTotal - valorAbater;

                                                                        contentValues = new ContentValues();
                                                                        contentValues.put("valor_devido", restante);

                                                                        //SE TUDO DEU CERTO, ATUALIZAR CONTA A RECEBER
                                                                        db.update("conta_receber", contentValues, "_id=?", new String[]{String.valueOf(mList.get(position).getId())});

                                                                        Toast.makeText(getActivity(), "Conta recebida com sucesso!!!", Toast.LENGTH_SHORT).show();
                                                                    }

                                                                    if (checker.lacksPermissions(REQUIRED_PERMISSION)) {
                                                                        PermissionsActivity.startActivityForResult(getActivity(), PERMISSION_REQUEST_CODE, REQUIRED_PERMISSION);
                                                                    } else {
                                                                        //createPdf(FileUtils.getAppPath(getContext()) + "recibo.pdf", codContaReceber, dataContaReceber, valorContaReceber, cliente, buscaNomeUsuario(), dataVencimento, valorContaReceberDesconto, valorAbater.toString());
                                                                        createPdf(Environment.getExternalStorageDirectory().getPath() + "/" + codContaReceber + ".pdf", codContaReceber, dataContaReceber, valorContaReceber, cliente, buscaNomeUsuario(), dataVencimento, valorContaReceberDesconto, valorAbater.toString());
                                                                    }

                                                                    atualizarLista();

                                                                }
                                                            });

                                                    alert.show();



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

                        if (arg1 == 2) {
                            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                            alert.setTitle("Observação");

                            final EditText input = new EditText(getActivity());
                            input.setInputType(InputType.TYPE_CLASS_TEXT);
                            alert.setView(input);

                            alert.setPositiveButton("Ok",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog,
                                                            int whichButton) {
                                            //##################Inicio do desconto########################
                                            contentValues.clear();

                                            String obs;
                                            try {
                                                obs = input.getText().toString();

                                                contentValues.put("observacao", formatSoapHora.format(new Date()) + " - " + obs);

                                                db.update("conta_receber", contentValues, "_id=?", new String[]{String.valueOf(mList.get(position).getId())});

                                                Log.i("OBSERVACAO", "ID: " + mList.get(position).getId());

                                            } catch (Exception e) {

                                                Log.e("ERRO", "Altarando tabela contas a receber");

                                                db.execSQL("Alter table conta_receber Add observacao VARCHAR(200)");

                                                obs = input.getText().toString();
                                                //obs = "";

                                                contentValues.put("observacao", formatSoapHora.format(new Date()) + " - " + obs);

                                                db.update("conta_receber", contentValues, "_id=?", new String[]{String.valueOf(mList.get(position).getId())});
                                            }
                                        }
                                    });

                            alert.show();
                        }
                        //############################# FIM EDITAR ################

                        alerta.dismiss();
                    }
                });

        alerta = builder.create();
        alerta.show();

    }

    public void atualizarLista(){

        String nome = String.valueOf(edtLocalizarContaReceber.getText());

        mList = ((ContasReceberActivity) getActivity()).getSetContaReceberListNome(nome, categoriaCliente);
        //ContasReceberAdapter adapter = new ContasReceberAdapter(getActivity(), mList, ImageLoader.getInstance());
        adapterContasReceber = new ContasReceberAdapter(getActivity(), mList, ImageLoader.getInstance());
        mRecyclerView.setAdapter(adapterContasReceber);

        if (nome == "") {
            buncando = false;
        } else {
            buncando = true;
        }

    }

    public void createPdf(String dest, String codContaRecever, String data, String valor, Cliente cliente, String usuario, String dataReferencia, String valorContaReceberDesconto, String valorContaReceberSaldo) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }
/*
        File file = new File(dest);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Uri apkURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", file);
            //intent.setDataAndType(apkURI, "image/jpg");
            intent.setDataAndType(apkURI, "application/pdf");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        } else {
            intent.setDataAndType(Uri.fromFile(file), "application/pdf");
        }
        startActivity(intent);
*/
        Log.i("INFO", "CLIENTE NOME: " + cliente.getRazaoSocial());
/*
        if (new File(dest).exists()) {
            new File(dest).delete();
        }
*/
        //Toast.makeText(getActivity(), "INICIANDO PDF... :)", Toast.LENGTH_SHORT).show();

        try {
            /**
             * Creating Document
             */
            Document document = new Document();
            Rectangle one = new Rectangle(420, 1000);
            //document.setPageSize(PageSize.A4)
            document.setPageSize(one);
            document.setMargins(20, 2, 2, 2);

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            //Rectangle one = new Rectangle(210, 500);
            //document.setPageSize(PageSize.A4);
            //document.setPageSize(one);
            //document.setMargins(2, 2, 2, 2);
            document.addCreationDate();
            document.addAuthor("Velejar Software");
            document.addCreator("Recibo");

            /***
             * Variables for further use....
             */
            //BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            //float mHeadingFontSize = 25.0f;
            //float mValueFontSize = 25.0f;

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            //lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setLineColor(BaseColor.BLACK);

            // Title Order Details...
            // Adding Title....
            Font mOrderDetailsEmpresaFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaChunk = new Chunk(buscaInfoEmpresa(1), mOrderDetailsEmpresaFont);
            Paragraph mOrderDetailsEmpresaParagraph = new Paragraph(mOrderDetailsEmpresaChunk);
            mOrderDetailsEmpresaParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaParagraph);

            Font mOrderDetailsEmpresaCnpjFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaCnpjChunk = new Chunk(buscaInfoEmpresa(2), mOrderDetailsEmpresaCnpjFont);
            Paragraph mOrderDetailsEmpresaCnpjParagraph = new Paragraph(mOrderDetailsEmpresaCnpjChunk);
            mOrderDetailsEmpresaCnpjParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaCnpjParagraph);

            Font mOrderDetailsEmpresaEnderecoFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaEnderecoChunk = new Chunk(buscaInfoEmpresa(3), mOrderDetailsEmpresaEnderecoFont);
            Paragraph mOrderDetailsEmpresaEnderecoParagraph = new Paragraph(mOrderDetailsEmpresaEnderecoChunk);
            mOrderDetailsEmpresaEnderecoParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaEnderecoParagraph);

            Font mOrderDetailsEmpresaTelefoneFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaTelefoneChunk = new Chunk(buscaInfoEmpresa(4), mOrderDetailsEmpresaTelefoneFont);
            Paragraph mOrderDetailsEmpresaTelefoneParagraph = new Paragraph(mOrderDetailsEmpresaTelefoneChunk);
            mOrderDetailsEmpresaTelefoneParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaTelefoneParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font mOrderDetailsTitleFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("TAXA ADMINISTRATIVA SEMANAL", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));


            // Fields of Order Details...
            // Adding Chunks for Title and value
            Font mOrderClienteFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderClienteChunk = new Chunk("CLIENTE: " + cliente.getRazaoSocial(), mOrderClienteFont);
            Paragraph mOrderClienteParagraph = new Paragraph(mOrderClienteChunk);
            document.add(mOrderClienteParagraph);
/*
            Font mOrderClienteValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderClienteValueChunk = new Chunk(cliente.getRazaoSocial(), mOrderClienteValueFont);
            Paragraph mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
            document.add(mOrderClienteValueParagraph);
*/
            Font mOrderClienteValueFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderClienteValueChunk = new Chunk("END.: " + cliente.getEndereco() + " " + cliente.getEnderecoNumero(), mOrderClienteValueFont);
            Paragraph mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
            document.add(mOrderClienteValueParagraph);

            mOrderClienteValueChunk = new Chunk("BAIRRO: " + cliente.getBairro(), mOrderClienteValueFont);
            mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
            document.add(mOrderClienteValueParagraph);

            cursor = db.rawQuery(
                    "SELECT _id, estado_id, nome FROM cidade where _id like '" + cliente.getCidade_id().toString() + "'", null);

            if (cursor.moveToFirst()) {

                String nomeCidade = cursor.getString(2);
                Integer idEstado = cursor.getInt(1);

                cursor = db.rawQuery(
                        "SELECT _id, uf, nome FROM estado where _id like '" + idEstado.toString() + "'", null);

                if (cursor.moveToFirst()) {
                    mOrderClienteValueChunk = new Chunk("CIDADE: " + nomeCidade + " UF: " + cursor.getString(1), mOrderClienteValueFont);
                    mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                    document.add(mOrderClienteValueParagraph);
                }
            }

            cursor = db.rawQuery(
                    "SELECT _id, nome FROM categoria_cliente where _id like '" + cliente.getCidade_id().toString() + "'", null);

            if (cursor.moveToFirst()) {
                    mOrderClienteValueChunk = new Chunk("CATEGORIA: " + cursor.getString(1), mOrderClienteValueFont);
                    mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                    document.add(mOrderClienteValueParagraph);
            }

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            document.add(new Paragraph(""));// Fields of Order Details...


            Font mOrderIdValueFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderIdChunk = new Chunk("NUMERO CONTA: " + codContaRecever, mOrderIdValueFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            // Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
             mOrderIdChunk = new Chunk("VENDEDOR: " + usuario, mOrderIdFont);
             mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));
            // Adding Horizontal Line...
            //document.add(new Chunk(lineSeparator));
            // Adding Line Breakable Space....
            //document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderDatePagamentoFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDatePagamentoChunk = new Chunk("DATA PAGAMENTO: " + data, mOrderDatePagamentoFont);
            Paragraph mOrderDatePagamentoParagraph = new Paragraph(mOrderDatePagamentoChunk);
            document.add(mOrderDatePagamentoParagraph);
/*
            Font mOrderDatePagamentoValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDatePagamentoValueChunk = new Chunk(data, mOrderDatePagamentoValueFont);
            Paragraph mOrderDatePagamentoValueParagraph = new Paragraph(mOrderDatePagamentoValueChunk);
            document.add(mOrderDatePagamentoValueParagraph);
*/
            // Fields of Order Details...
            Font mOrderDateReferenciaFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDateRefereniaChunk = new Chunk("DATA REFERENCIA: " + dataReferencia, mOrderDateReferenciaFont);
            Paragraph mOrderDateReferenciaParagraph = new Paragraph(mOrderDateRefereniaChunk);
            document.add(mOrderDateReferenciaParagraph);
/*
            Font mOrderDateValueFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDateValueChunk = new Chunk(dataReferencia, mOrderDateValueFont);
            Paragraph mOrderDateValueParagraph = new Paragraph(mOrderDateValueChunk);
            document.add(mOrderDateValueParagraph);
*/
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            // Fields of Order Details...
            Font mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderAcNameChunk = new Chunk("VALOR DA NOTA: " + "R$ " + valor, mOrderAcNameFont);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);

            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("DESCONTO: " + "R$ " + valorContaReceberDesconto, mOrderAcNameFont);
            mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);

            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("VALOR PAGO: " + "R$ " + valorContaReceberSaldo, mOrderAcNameFont);
            mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);
/*
            Font mOrderAcNameValueFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            //Chunk mOrderAcNameValueChunk = new Chunk("Pratik Butani", mOrderAcNameValueFont);
            Chunk mOrderAcNameValueChunk = new Chunk("R$ " + valor, mOrderAcNameValueFont);
            Paragraph mOrderAcNameValueParagraph = new Paragraph(mOrderAcNameValueChunk);
            document.add(mOrderAcNameValueParagraph);
*/
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));

            Font mOrderNotaFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderNotaChunk = new Chunk("NOTA: ", mOrderNotaFont);
            Paragraph mOrderNotaParagraph = new Paragraph(mOrderNotaChunk);
            document.add(mOrderNotaParagraph);
/*
            Font mOrderNotaInfoFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderNotaInfoChunk = new Chunk("COVID 19: A melhor maneira de se previnir é o isolamento social. Evite aglomerações!!!", mOrderNotaInfoFont);
            Paragraph mOrderNotaInfoParagraph = new Paragraph(mOrderNotaInfoChunk);
            document.add(mOrderNotaInfoParagraph);
*/
            document.close();

            //Toast.makeText(getActivity(), "Created... :)", Toast.LENGTH_SHORT).show();
            //FileUtils.openFile(getContext(), new File(dest));


            File file = new File(dest);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkURI = FileProvider.getUriForFile(getContext(), getContext().getApplicationContext().getPackageName() + ".fileprovider", file);
                //intent.setDataAndType(apkURI, "image/jpg");
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            }
            startActivity(intent);

        } catch (IOException | DocumentException ie) {
            Log.e("ERRO", "createPdf: Error " + ie.getLocalizedMessage());
            //Toast.makeText(getActivity(), "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
             LOGE("createPdf: Error " + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(getActivity(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private String buscaInfoEmpresa(int info){
        String retorno = null;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, razaoSocial, cnpj, endereco, endereco_numero, telefone_1 FROM empresa", null);

            cursor.moveToFirst();

            //Nome
            if (info == 1){
                retorno = cursor.getString(1);
            }

            //Cnpj
            if (info == 2){
                retorno = cursor.getString(2);
            }

            //Endereco
            if (info == 3){
                retorno = cursor.getString(3) + " " + cursor.getString(4);
            }

            //Telefone
            if (info == 4){
                retorno = cursor.getString(5);
            }


        }catch (Exception e){
            retorno = "Não encontrado";
        }

        return retorno;
    }

    private static class RecyclerViewTouchListener implements RecyclerView.OnItemTouchListener {

        private Context mContext;
        private GestureDetector mGestureDetector;   //Calsse reposnsavel pela captura do toque na tela do dispositivo
        private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

        public RecyclerViewTouchListener(Context c, final RecyclerView rv, RecyclerViewOnClickListenerHack rvoclh) {

            mContext = c;
            mRecyclerViewOnClickListenerHack = rvoclh;

            //############ CHAMADA CLICK ###############
            mGestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public void onLongPress(MotionEvent e) {
                    super.onLongPress(e);

                    View cv = rv.findChildViewUnder(e.getX(), e.getY()); //Pega as coordenadas do toque na tela

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onLongClickListener(cv,
                                rv.getChildPosition(cv));
                    }
                }

                @Override
                public boolean onSingleTapUp(MotionEvent e) {

                    View cv = rv.findChildViewUnder(e.getX(), e.getY()); //Pega as coordenadas do toque na tela

                    if (cv != null && mRecyclerViewOnClickListenerHack != null) {
                        mRecyclerViewOnClickListenerHack.onClickListener(cv,
                                rv.getChildPosition(cv));
                    }

                    //return super.onSingleTapUp(e);
                    return true;
                }
            });

        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            mGestureDetector.onTouchEvent(e); //Detectando se foi um clique simples ou se foi um long press / EXECUTA O 'CHAMADA CLICK ^^^'
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }
    }

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }

    public Double showInputDialogValorAbater() {

        valorAbater = 0.0;

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Valor a ser abatido");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio do desconto########################

                        try {
                            valorAbater = Double.parseDouble(input.getText().toString().replace(",", "."));

                        } catch (Exception e) {
                            valorAbater = 0.0;

                        }
                    }
                });

        alert.show();

        return valorAbater;
    }

    private int buscaIdEmpresaUsuarioLogado(){
        int retorno = 0;

        Cursor cursor;
        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getInt(6);
        }catch (Exception e){
            retorno = 0;
        }
        return retorno;
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
}
