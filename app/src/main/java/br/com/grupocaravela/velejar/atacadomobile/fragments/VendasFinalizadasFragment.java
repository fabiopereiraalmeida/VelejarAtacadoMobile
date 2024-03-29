package br.com.grupocaravela.velejar.atacadomobile.fragments;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.MenuPrincipalActivity;
import br.com.grupocaravela.velejar.atacadomobile.Util.FileUtils;
import br.com.grupocaravela.velejar.atacadomobile.VendaActivity;
import br.com.grupocaravela.velejar.atacadomobile.adapters.VendasFinalizadasAdapter;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

import static br.com.grupocaravela.velejar.atacadomobile.Util.LogUtils.LOGE;

/**
 * Created by fabio on 16/07/15.
 */
public class VendasFinalizadasFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private DBHelper dbHelper;
    //private Cursor l;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private AlertDialog alerta;

    private RecyclerView mRecyclerView;
    private List<AndroidVendaCabecalho> mList;
    //private EditText edtLocalizarProduto;
    private ImageView ivLocalizarProduto;

    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    private boolean buncando = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itens_venda, container, false);

        dbHelper = new DBHelper(getActivity(), "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        //edtLocalizarProduto = (EditText) view.findViewById(R.id.edt_localizar_cliente);
        //ivLocalizarProduto = (ImageView) view.findViewById(R.id.iv_localizar_produto);
        //ivLocalizarProduto.setBackgroundResource(R.drawable.buscar_azul_48x48);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_produto_list); //Dentro de "fragment_empresa"
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
                    VendasFinalizadasAdapter adapter = (VendasFinalizadasAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
                        List<AndroidVendaCabecalho> listAux = ((MenuPrincipalActivity) getActivity()).getSetAndroidVendaCabecalhoList(7);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }

                    }

                }
            }

        });

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this)); //Colocando novo dectar clique

        //LinearLayoutManager para a Lista RecycleView
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayout.setReverseLayout(true); //Inverte a lista
        mRecyclerView.setLayoutManager(linearLayout);

        if (buncando == false) {

            mList = ((MenuPrincipalActivity) getActivity()).getSetAndroidVendaCabecalhoList(7);
            VendasFinalizadasAdapter adapter = new VendasFinalizadasAdapter(getActivity(), mList, ImageLoader.getInstance());
            adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
            mRecyclerView.setAdapter(adapter);
        }

        return view;
    }

    //################ Inicio Metodos do clique e longClique ##############
    @Override
    public void onClickListener(View view, int position) {

        //Toast.makeText(getActivity(), "onClickListener: " + position, Toast.LENGTH_SHORT).show();

        //Toast.makeText(getActivity(), "Chama o Produto: " + position, Toast.LENGTH_SHORT).show();
/*
        Intent it = new Intent(getActivity(), VendaActivity.class);
        it.putExtra("op", 101); //Acção para carregar o cabeçalho existente
        it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        startActivity(it);
*/
    }

    @Override
    public void onLongClickListener(View view, int position) {

        listaOpcoes(position);

        //Toast.makeText(getActivity(), "onLongClickListener: " + position, Toast.LENGTH_SHORT).show();
    }
    //################ Fim Metodos do clique e longClique ##############

    // LISTAR OPCOES
    private void listaOpcoes(final int position) {
        // Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Excluir venda");
        itens.add("Editar venda");
        itens.add("Visualizar recibo");
        itens.add("Gerar catalogo");

        // adapter utilizando um layout customizado (TextView)
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_opcoes, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Opções da venda");

        // define o di�logo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // #################### OPCAO DE EXCLUIR
                        // #####################
                        if (arg1 == 0) {

                            //##############################################
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                            alertDialogBuilder.setTitle("EXCLUIR VENDA");

                            alertDialogBuilder
                                    .setMessage(
                                            "Gostaria de excluir a venda selecionada?")
                                    .setCancelable(false)
                                    .setPositiveButton("Sim",
                                            new DialogInterface.OnClickListener() {
                                                public void onClick(DialogInterface dialog, int id) {

                                                    db.delete("android_venda_cabecalho", "_id=?", new String[]{String.valueOf(mList.get(position).getId())});
                                                    atualizarLista();

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

                        //############################# OPÇAO EDITAR ################

                        if (arg1 == 1) {
                            Intent it = new Intent(getActivity(), VendaActivity.class);
                            it.putExtra("op", 101); //Acção para carregar o cabeçalho existente
                            it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
                            //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
                            startActivityForResult(it, 0);
                            //startActivity(it);
                        }

                        if (arg1 == 2) {

                            Cliente cliente = new Cliente();
                            List<AndroidVendaDetalhe> androidVendaDetalhesList = new ArrayList<>();

                            cursor = db
                                    .rawQuery(
                                            "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                                    "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + mList.get(position).getId() + "'", null);

                            if (cursor.moveToFirst()) {

                                for (int i = 0; i < cursor.getCount(); i++) {

                                    AndroidVendaDetalhe androidVendaDetalhe = new AndroidVendaDetalhe();

                                    androidVendaDetalhe.setProduto(cursor.getLong(2));
                                    androidVendaDetalhe.setQuantidade(cursor.getDouble(3));
                                    androidVendaDetalhe.setValorDesconto(cursor.getDouble(5));
                                    androidVendaDetalhe.setValorParcial(cursor.getDouble(4));
                                    androidVendaDetalhe.setValorTotal(cursor.getDouble(6));
                                    androidVendaDetalhe.setValorUnitario(cursor.getDouble(8));

                                    androidVendaDetalhesList.add(androidVendaDetalhe);

                                    cursor.moveToNext();
                                }
                            }

                            cursor = db.rawQuery(
                                    "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                            "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                            "rota_id, empresa_id, novo, alterado FROM cliente where _id like '" + mList.get(position).getCliente().toString() + "'", null);


                            if (cursor.moveToFirst()) {
                                cliente.setId(cursor.getLong(0));
                                cliente.setRazaoSocial(cursor.getString(1));
                                cliente.setFantasia(cursor.getString(2));
                                cliente.setInscricaoEstadual(cursor.getString(3));
                                cliente.setCpf(cursor.getString(4));
                                cliente.setCnpj(cursor.getString(5));
                                try {
                                    cliente.setDataNascimento(cursor.getString(6));
                                } catch (Exception e) {
                                    cliente.setDataNascimento(null);
                                }
                                try {
                                    cliente.setDataCadastro(cursor.getString(7));
                                } catch (Exception e) {
                                    cliente.setDataCadastro(null);
                                }
                                cliente.setEmail(cursor.getString(8));
                                cliente.setLimiteCredito(cursor.getDouble(9));
                                //c.setAtivo(Boolean.parseBoolean(cursor.getString(10)));
                                Log.d("IMPORTANTE", "ATIVO+-+-+: " + cursor.getString(10));

                                if (cursor.getString(10).equals("1")) {
                                    cliente.setAtivo(true);
                                } else {
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


                            createRomaneioPdf(Environment.getExternalStorageDirectory().getPath() + "/recibo_venda.pdf", androidVendaDetalhesList, mList.get(position).getDataVenda(), mList.get(position).getValorParcial(), mList.get(position).getValorDesconto(), mList.get(position).getValorTotal(), cliente, buscaNomeUsuario());
                        }

                        if (arg1 == 3) {

                            List<AndroidVendaDetalhe> androidVendaDetalhesList = new ArrayList<>();
                            List<Produto> produtoList = new ArrayList<>();

                            cursor = db
                                    .rawQuery(
                                            "SELECT _id, android_venda_cabecalho_id, produto_id, quantidade, valor_parcial, valor_desconto, valor_total, empresa_id, valor_unitario " +
                                                    "FROM android_venda_detalhe where android_venda_cabecalho_id like '" + mList.get(position).getId() + "'", null);

                            if (cursor.moveToFirst()) {

                                for (int i = 0; i < cursor.getCount(); i++) {

                                    AndroidVendaDetalhe androidVendaDetalhe = new AndroidVendaDetalhe();

                                    androidVendaDetalhe.setProduto(cursor.getLong(2));
                                    androidVendaDetalhe.setQuantidade(cursor.getDouble(3));
                                    androidVendaDetalhe.setValorDesconto(cursor.getDouble(5));
                                    androidVendaDetalhe.setValorParcial(cursor.getDouble(4));
                                    androidVendaDetalhe.setValorTotal(cursor.getDouble(6));
                                    androidVendaDetalhe.setValorUnitario(cursor.getDouble(8));

                                    androidVendaDetalhesList.add(androidVendaDetalhe);

                                    cursor.moveToNext();
                                }

                                for (int i = 0; i < androidVendaDetalhesList.size(); i++) {

                                    cursor = db
                                            .rawQuery(
                                                    "SELECT _id, codigo, nome, valor_desejavel_venda, unidade_id, imagem, codigo_ref FROM produto where _id like '" + androidVendaDetalhesList.get(i).getProduto().toString() + "'", null);

                                    if (cursor.moveToFirst()) {
                                        Produto p = new Produto();

                                        p.setId(cursor.getLong(0));
                                        p.setCodigo(cursor.getString(1));
                                        p.setNome(cursor.getString(2));
                                        p.setValorDesejavelVenda(cursor.getDouble(3));
                                        p.setUnidade(cursor.getLong(4));
                                        p.setImagem(cursor.getBlob(5));
                                        p.setCodigo_ref(cursor.getString(6));

                                        //listProdutos.add(p);
                                        produtoList.add(p);
                                    }
                                }
                            }

                            //gerarCatalogo(FileUtils.getAppPath(getContext()) + "catalogo_produtos_venda.pdf", produtoList);
                            gerarCatalogo(Environment.getExternalStorageDirectory().getPath() + "/catalogo_produtos_venda.pdf", produtoList);
                        }
                        alerta.dismiss();
                    }
                });

        alerta = builder.create();
        alerta.show();
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

    public void atualizarLista(){

        mList = ((MenuPrincipalActivity) getActivity()).getSetAndroidVendaDetalheListCompleta();
        VendasFinalizadasAdapter adapter = new VendasFinalizadasAdapter(getActivity(), mList, ImageLoader.getInstance());
        //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
        mRecyclerView.setAdapter(adapter);

    }

    public void createRomaneioPdf(String dest, List<AndroidVendaDetalhe> androidVendaDetalhesList, String data, Double subTotal, Double valorDesconto, Double totalGeral, Cliente cliente, String usuario) {
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

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        //Toast.makeText(getActivity(), "INICIANDO PDF... :)", Toast.LENGTH_SHORT).show();

        try {
            /**
             * Creating Document
             */
            Document document = new Document();
            Rectangle one = new Rectangle(420, 1000);
            //document.setPageSize(PageSize.A4);
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
            Font mOrderDetailsEmpresaFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaChunk = new Chunk(buscaInfoEmpresa(1), mOrderDetailsEmpresaFont);
            Paragraph mOrderDetailsEmpresaParagraph = new Paragraph(mOrderDetailsEmpresaChunk);
            mOrderDetailsEmpresaParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaParagraph);

            Font mOrderDetailsEmpresaCnpjFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaCnpjChunk = new Chunk(buscaInfoEmpresa(2), mOrderDetailsEmpresaCnpjFont);
            Paragraph mOrderDetailsEmpresaCnpjParagraph = new Paragraph(mOrderDetailsEmpresaCnpjChunk);
            mOrderDetailsEmpresaCnpjParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaCnpjParagraph);

            Font mOrderDetailsEmpresaEnderecoFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaEnderecoChunk = new Chunk(buscaInfoEmpresa(3), mOrderDetailsEmpresaEnderecoFont);
            Paragraph mOrderDetailsEmpresaEnderecoParagraph = new Paragraph(mOrderDetailsEmpresaEnderecoChunk);
            mOrderDetailsEmpresaEnderecoParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaEnderecoParagraph);

            Font mOrderDetailsEmpresaTelefoneFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaTelefoneChunk = new Chunk(buscaInfoEmpresa(4), mOrderDetailsEmpresaTelefoneFont);
            Paragraph mOrderDetailsEmpresaTelefoneParagraph = new Paragraph(mOrderDetailsEmpresaTelefoneChunk);
            mOrderDetailsEmpresaTelefoneParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaTelefoneParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font mOrderDetailsTitleFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("DADOS DO CLIENTE", mOrderDetailsTitleFont);
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

            Log.d("IMPORTANTE", "CLIENTE: " + cliente.getRazaoSocial());
/*
            Font mOrderClienteValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderClienteValueChunk = new Chunk(cliente.getRazaoSocial(), mOrderClienteValueFont);
            Paragraph mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
            document.add(mOrderClienteValueParagraph);
*/
            Font mOrderClienteValueFont = new Font(urName, 18.0f, Font.NORMAL, BaseColor.BLACK);
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

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            mOrderDetailsTitleFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            mOrderDetailsTitleChunk = new Chunk("DADOS DA VENDA", mOrderDetailsTitleFont);
            mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            // Adding Chunks for Title and value
            Font mOrderIdFont = new Font(urName, 18.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderIdChunk = new Chunk("VENDEDOR: " + usuario, mOrderIdFont);
            Paragraph mOrderIdParagraph = new Paragraph(mOrderIdChunk);
            document.add(mOrderIdParagraph);

            // Adding Line Breakable Space....
            document.add(new Paragraph(""));

            Font mOrderDatePagamentoFont = new Font(urName, 18.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDatePagamentoChunk = new Chunk("DATA VENDA: " + data, mOrderDatePagamentoFont);
            Paragraph mOrderDatePagamentoParagraph = new Paragraph(mOrderDatePagamentoChunk);
            document.add(mOrderDatePagamentoParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            mOrderDetailsTitleFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            mOrderDetailsTitleChunk = new Chunk("DADOS DOS ITENS", mOrderDetailsTitleFont);
            mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            for (int i = 0; i < androidVendaDetalhesList.size(); i++) {

                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, valor_desejavel_venda, unidade_id, imagem, codigo_ref FROM produto where _id like '" + androidVendaDetalhesList.get(i).getProduto().toString() + "'", null);

                if (cursor.moveToFirst()) {

                    mOrderClienteValueFont = new Font(urName, 18.0f, Font.NORMAL, BaseColor.BLACK);
                    try {
                        mOrderClienteValueChunk = new Chunk( cursor.getString(2), mOrderClienteValueFont);
                    } catch (Exception e) {
                        mOrderClienteValueChunk = new Chunk("Nome: Não encontrado", mOrderClienteValueFont);
                    }
                    mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                    document.add(mOrderClienteValueParagraph);
                }
                mOrderClienteValueChunk = new Chunk("qtd: " + androidVendaDetalhesList.get(i).getQuantidade() + " - V. Unit.: R$ " + String.format("%.2f", androidVendaDetalhesList.get(i).getValorUnitario()) + " - Desc.: " + String.format("%.2f", androidVendaDetalhesList.get(i).getValorDesconto()) + " - V. Total: R$ " + String.format("%.2f", androidVendaDetalhesList.get(i).getValorTotal()), mOrderClienteValueFont);
                mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                document.add(mOrderClienteValueParagraph);

                document.add(new Paragraph(""));
                document.add(new Chunk(lineSeparator));

            }

            // Fields of Order Details...
            Font mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderAcNameChunk = new Chunk("SUB. TOTAL: " + "R$ " + String.format("%.2f", subTotal), mOrderAcNameFont);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);

            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("DESCONTO: " + "R$ " + String.format("%.2f", valorDesconto), mOrderAcNameFont);
            mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);

            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("TOTAL GERAL: " + "R$ " + String.format("%.2f", totalGeral), mOrderAcNameFont);
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
/*
            Font mOrderNotaFont = new Font(urName, 16.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderNotaChunk = new Chunk("NOTA: ", mOrderNotaFont);
            Paragraph mOrderNotaParagraph = new Paragraph(mOrderNotaChunk);
            document.add(mOrderNotaParagraph);
            */

/*
            Font mOrderNotaInfoFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderNotaInfoChunk = new Chunk("COVID 19: A melhor maneira de se previnir e o isolamento social. Evite aglomerações!!!", mOrderNotaInfoFont);
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
        } catch (IOException | DocumentException ie) {
            Log.e("ERRO", "createPdf: Error " + ie.getLocalizedMessage());
            //Toast.makeText(getActivity(), "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            LOGE("createPdf: Error " + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(getActivity(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    public void gerarCatalogo(String dest, List<Produto> listaProdutos) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        //Toast.makeText(this, "INICIANDO PDF... :)", Toast.LENGTH_SHORT).show();

        try {
            /**
             * Creating Document
             */
            Document document = new Document();
            Rectangle one = new Rectangle(420, 500);
            //document.setPageSize(PageSize.A4);
            document.setPageSize(one);
            document.setMargins(20, 2, 2, 2);

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            document.addCreationDate();
            document.addAuthor("Velejar Software");
            document.addCreator("Catalogo de produtos");

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

            for (int i = 0; i < listaProdutos.size(); i++) {
                //if (listaProdutos.get(i).getImagem() != null) {

                    Font mOrderDetailsEmpresaFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
                    Chunk mOrderDetailsEmpresaChunk = new Chunk(buscaInfoEmpresa(1), mOrderDetailsEmpresaFont);
                    Paragraph mOrderDetailsEmpresaParagraph = new Paragraph(mOrderDetailsEmpresaChunk);
                    mOrderDetailsEmpresaParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderDetailsEmpresaParagraph);

                    Font mOrderDetailsEmpresaCnpjFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
                    Chunk mOrderDetailsEmpresaCnpjChunk = new Chunk(buscaInfoEmpresa(2), mOrderDetailsEmpresaCnpjFont);
                    Paragraph mOrderDetailsEmpresaCnpjParagraph = new Paragraph(mOrderDetailsEmpresaCnpjChunk);
                    mOrderDetailsEmpresaCnpjParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderDetailsEmpresaCnpjParagraph);

                    // Adding Line Breakable Space....
                    document.add(new Paragraph(""));
                    // Adding Horizontal Line...
                    document.add(new Chunk(lineSeparator));
                    // Adding Line Breakable Space....
                    document.add(new Paragraph(""));// Fields of Order Details...

                if (listaProdutos.get(i).getImagem() != null) {
                    Image image = Image.getInstance(listaProdutos.get(i).getImagem());
                    image.scaleAbsolute(300, 300);
                    image.setAlignment(Element.ALIGN_CENTER);
                    document.add(image);
                }else{
                    Drawable d = getResources().getDrawable(R.drawable.sem_foto);
                    BitmapDrawable bitDw = ((BitmapDrawable) d);
                    Bitmap bmp = bitDw.getBitmap();
                    ByteArrayOutputStream stream = new ByteArrayOutputStream();
                    bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                    Image image = Image.getInstance(stream.toByteArray());

                    //Image image = Image.getInstance(R.drawable.sem_foto);
                    image.scaleAbsolute(300, 300);
                    image.setAlignment(Element.ALIGN_CENTER);
                    document.add(image);
                }

                    Font mOrderProdutoValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
                    Chunk mOrderProdutoValueChunk = new Chunk(listaProdutos.get(i).getNome(), mOrderProdutoValueFont);
                    Paragraph mOrderProdutoValueParagraph = new Paragraph(mOrderProdutoValueChunk);
                    mOrderProdutoValueParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderProdutoValueParagraph);

                    Font mOrderProdutoValorValueFont = new Font(urName, 45.0f, Font.NORMAL, BaseColor.BLUE);
                    Chunk mOrderProdutoValorValueChunk = new Chunk("R$ " + String.format("%.2f", listaProdutos.get(i).getValorDesejavelVenda()), mOrderProdutoValorValueFont);
                    Paragraph mOrderProdutoValorValueParagraph = new Paragraph(mOrderProdutoValorValueChunk);
                    mOrderProdutoValorValueParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderProdutoValorValueParagraph);

                    document.newPage();
                //}
            }

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
            Toast.makeText(getContext(), "No application found to open this file.", Toast.LENGTH_SHORT).show();
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
