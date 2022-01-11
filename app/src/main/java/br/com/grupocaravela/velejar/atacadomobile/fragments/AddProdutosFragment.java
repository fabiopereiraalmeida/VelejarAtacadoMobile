package br.com.grupocaravela.velejar.atacadomobile.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.AddProdutosActivity;
import br.com.grupocaravela.velejar.atacadomobile.adapters.ProdutosAdapter;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

/**
 * Created by fabio on 16/07/15.
 */
public class AddProdutosFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<Produto> mList;
    private EditText edtLocalizarProduto;
    private ImageView ivLocalizarProduto;

    private String quantidade;

    private boolean buncando = false;

    private SQLiteDatabase db;
    private DBHelper dbHelper;

    private Intent mIntent;

    private Double valorSelecionado = null;
    private Double valorAlternativo = null;
    private List<String> valores;

    private Spinner mSpinner;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

        //Configuração inicial

        dbHelper = new DBHelper(getActivity(), "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        mIntent = getActivity().getIntent();

        edtLocalizarProduto = (EditText) view.findViewById(R.id.edt_localizar_contas_receber);
        edtLocalizarProduto.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        ivLocalizarProduto = (ImageView) view.findViewById(R.id.iv_localizar_produto);
        ivLocalizarProduto.setBackgroundResource(R.drawable.buscar_azul_48x48);

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
                    ProdutosAdapter adapter = (ProdutosAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
                        List<Produto> listAux = ((AddProdutosActivity) getActivity()).getSetProdutoList(7);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }
                    }
                }

                if (buncando == true) {

                    //LinearLayoutManager para a Lista RecycleView
                    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    ProdutosAdapter adapter = (ProdutosAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida

                        //FUNCAO UTILIZADA PARA RECARREGAR A LISTA QUANDO A MESMA CHEGA AO FIM
                        /*
                        String nome = String.valueOf(edtLocalizarProduto.getText());
                        List<Produto> listAux = ((AddProdutosActivity) getActivity()).getSetProdutoListNome(nome);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }
                         */
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

            mList = ((AddProdutosActivity) getActivity()).getSetProdutoList(7);
            ProdutosAdapter adapter = new ProdutosAdapter(getActivity(), mList, ImageLoader.getInstance());
            adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
            mRecyclerView.setAdapter(adapter);

        }

        //#################### INICIO DO FILTRO PELA BUSCA ###########################
        edtLocalizarProduto.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {


                String nome = String.valueOf(edtLocalizarProduto.getText());

                mList = ((AddProdutosActivity) getActivity()).getSetProdutoListNome(nome);
                ProdutosAdapter adapter = new ProdutosAdapter(getActivity(), mList, ImageLoader.getInstance());
                //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
                mRecyclerView.setAdapter(adapter);

                if (nome == "") {
                    buncando = false;
                } else {
                    buncando = true;
                }
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

        showInputDialogQuantidade(position);
        //showInputDialogQuantidadeLongClick(position);

    }

    public void showInputDialogQuantidade(final int posicao) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater(null).inflate(R.layout.opcao_add_produto, null);

        mBuilder.setTitle("Opções");

        //final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner_valor);
        mSpinner = (Spinner) mView.findViewById(R.id.spinner_valor);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //Log.i("POSICAO", "Posicao " + mSpinner.getSelectedItem().toString());

                if (mSpinner.getSelectedItem().toString().equals("Informar")) {
                    informarValorAlternativo(mList.get(posicao).getValorMinimoVenda());
                    //mSpinner.setSelection(valores.size(), true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        final EditText inputQtd = (EditText) mView.findViewById(R.id.edt_add_produto_quantidade);

        valores = new ArrayList<String>();

        //valores.add("R$ " + String.format("%.2f", mList.get(posicao).getValorDesejavelVenda()));
        //valores.add("R$ " + String.format("%.2f", mList.get(posicao).getValorMinimoVenda()));
        valores.add("R$ " + mList.get(posicao).getValorDesejavelVenda());
        valores.add("R$ " + mList.get(posicao).getValorMinimoVenda());
        valores.add("Informar");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, valores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);

        //final EditText input = new EditText(getActivity());
        //input.setInputType(InputType.TYPE_CLASS_NUMBER);
        mBuilder.setView(mView);

        mBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio Quantidade########################

                        try {
                            Double v = 0.0;
                            if (mSpinner.getSelectedItemPosition() == 2) {
                                v = valorSelecionado;
                            }else{
                                v = Double.parseDouble(mSpinner.getSelectedItem().toString().replace("R$ ", ""));
                            }

                            addProdutoLista(posicao, Double.parseDouble(inputQtd.getText().toString()), v);

                            getActivity().recreate();

                        } catch (Exception e) {

                            quantidade = "1.0";

                            Double v = 0.0;
                            if (mSpinner.getSelectedItemPosition() == 2) {
                                v = valorSelecionado;
                            }else{
                                v = Double.parseDouble(mSpinner.getSelectedItem().toString().replace("R$ ", ""));
                            }

                            addProdutoLista(posicao, Double.parseDouble(quantidade), v);

                            getActivity().recreate();


                        }
                    }
                });

        mBuilder.show();

    }

    private void informarValorAlternativo(final Double valorMinimo){

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        mBuilder.setTitle("Informe o valor");

        final  EditText input = new EditText(getActivity());
        //input.setInputType(InputType.TYPE_CLASS_NUMBER);
        mBuilder.setView(input);

        mBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio Quantidade########################

                        valorSelecionado = Double.parseDouble(input.getText().toString().replace(",", "."));

                        if (valorSelecionado < valorMinimo){
                            valorSelecionado = valorMinimo;
                            Toast.makeText(getActivity(), "ATENCAO, O VALOR INFORMADO É MENOR QUE O MINIMO POSSIVEL!!!", Toast.LENGTH_LONG).show();
                            //valores.add("R$ " + String.format("%.2f", valorSelecionado).toString());
                            valores.add("R$ " + valorSelecionado);
                        }else{
                            //valores.add("R$ " + String.format("%.2f", valorSelecionado).toString());
                            valores.add("R$ " + valorSelecionado);
                        }
                        mSpinner.setSelection((valores.size() - 1), true);

                        //valores.remove(2);
                        //valores.add("R$ " + String.format("%.2f", valorSelecionado).toString());

                    }
                });

        mBuilder.show();
    }

@Override
    public void onLongClickListener(View view, int position) {

        //Toast.makeText(this.getActivity(), "Produto inserido na lista!!!", Toast.LENGTH_LONG).show();
        //showInputDialogQuantidadeLongClick(position);

    }


/*
    public void showInputDialogQuantidadeLongClick(final int posicao) {

        final AlertDialog.Builder mBuilder = new AlertDialog.Builder(getActivity());
        View mView = getLayoutInflater(null).inflate(R.layout.opcao_add_produto, null);

        mBuilder.setTitle("Opções");

        //final Spinner mSpinner = (Spinner) mView.findViewById(R.id.spinner_valor);
        mSpinner = (Spinner) mView.findViewById(R.id.spinner_valor);
        mSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {

                //Log.i("POSICAO", "Posicao " + mSpinner.getSelectedItem().toString());

                if (mSpinner.getSelectedItem().toString().equals("Informar")) {
                    informarValorAlternativo(mList.get(posicao).getValorMinimoVenda());
                    //mSpinner.setSelection(valores.size(), true);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }

        });
        final EditText inputQtd = (EditText) mView.findViewById(R.id.edt_add_produto_quantidade);

        valores = new ArrayList<String>();

        //valores.add("R$ " + String.format("%.2f", mList.get(posicao).getValorDesejavelVenda()));
        //valores.add("R$ " + String.format("%.2f", mList.get(posicao).getValorMinimoVenda()));
        valores.add("R$ " + mList.get(posicao).getValorDesejavelVenda());
        valores.add("R$ " + mList.get(posicao).getValorMinimoVenda());
        valores.add("Informar");

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item, valores);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSpinner.setAdapter(dataAdapter);

        //final EditText input = new EditText(getActivity());
        //input.setInputType(InputType.TYPE_CLASS_NUMBER);
        mBuilder.setView(mView);

        mBuilder.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio Quantidade########################

                        try {
                            Double v = 0.0;
                            if (mSpinner.getSelectedItemPosition() == 2) {
                                v = valorSelecionado;
                            }else{
                                v = Double.parseDouble(mSpinner.getSelectedItem().toString().replace("R$ ", ""));
                            }

                            addProdutoLista(posicao, Double.parseDouble(inputQtd.getText().toString()), v);

                            getActivity().recreate();

                        } catch (Exception e) {

                            quantidade = "1.0";

                            Double v = 0.0;
                            if (mSpinner.getSelectedItemPosition() == 2) {
                                v = valorSelecionado;
                            }else{
                                v = Double.parseDouble(mSpinner.getSelectedItem().toString().replace("R$ ", ""));
                            }

                            addProdutoLista(posicao, Double.parseDouble(quantidade), v);

                            getActivity().recreate();


                        }
                    }
                });

        mBuilder.show();

    }
*/
    //################ Fim Metodos do clique e longClique ##############

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

    private void addProdutoLista(final int posicao, Double qtd, Double valorUnitario){

        ContentValues vdContentValues = new ContentValues();

        Cursor cursor;
/*
        Cursor cursor = db
                .rawQuery(
                        "SELECT _id, codigo, nome, estoque, " +
                                "valor_desejavel_venda, valor_minimo_venda, imagem " +
                                "FROM produto WHERE _id LIKE '" + mList.get(posicao).getId() + "'", null);

        cursor.moveToFirst();
*/
        vdContentValues.put("quantidade", qtd - mIntent.getDoubleExtra("localizacao", 0));
        vdContentValues.put("valor_desconto", 0.0);
        vdContentValues.put("valor_unitario", valorUnitario);
        vdContentValues.put("valor_parcial", (qtd - mIntent.getDoubleExtra("localizacao", 0)) * valorUnitario);
        vdContentValues.put("valor_total", (qtd- mIntent.getDoubleExtra("localizacao", 0)) * valorUnitario);
        vdContentValues.put("android_venda_cabecalho_id", mIntent.getIntExtra("idCabecalho", 0));
        vdContentValues.put("produto_id", mList.get(posicao).getId());

        //################## INICIO PEGANDO O USUARIO LOGADO NO SISTEMA #########################
        cursor = db
                .rawQuery(
                        "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado WHERE _id LIKE '1'", null);

        cursor.moveToFirst();

        vdContentValues.put("empresa_id", cursor.getInt(6));

        //#################### FIM PEGANDO O USUARIO LOGADO NO SISTEMA ###########################

        db.insert("android_venda_detalhe", null, vdContentValues); //SALVANDO A VENDA CABEÇALHO

        Toast.makeText(this.getActivity(), "Produto inserido na lista!!!", Toast.LENGTH_LONG).show();

    }
}
