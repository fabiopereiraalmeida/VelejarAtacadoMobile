package br.com.grupocaravela.velejar.atacadomobile.fragments;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.VendaActivity;
import br.com.grupocaravela.velejar.atacadomobile.adapters.VendasAdapter;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;

/**
 * Created by fabio on 16/07/15.
 */
public class VendaFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private DBHelper dbHelper;
    private Cursor produtoCursor;
    private SQLiteDatabase db;

    private RecyclerView mRecyclerView;
    private List<AndroidVendaDetalhe> mList;

    private AlertDialog alerta;

    private ContentValues contentValues;

    private String desconto;
    private String quantidade;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_itens_venda, container, false);

        dbHelper = new DBHelper(getActivity(), "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_produto_list); //Dentro de "fragment_empresa"
        mRecyclerView.setHasFixedSize(true); //Definindo o tamanho do recleView como sempre o mesmo

        mRecyclerView.addOnItemTouchListener(new RecyclerViewTouchListener(getActivity(), mRecyclerView, this)); //Colocando novo dectar clique

        //LinearLayoutManager para a Lista RecycleView
        LinearLayoutManager linearLayout = new LinearLayoutManager(getActivity());
        linearLayout.setOrientation(LinearLayoutManager.VERTICAL);
        //linearLayout.setReverseLayout(true); //Inverte a lista
        mRecyclerView.setLayoutManager(linearLayout);

        mList = ((VendaActivity) getActivity()).getSetAndroidVendaDetalheListCompleta();
        VendasAdapter adapter = new VendasAdapter(getActivity(), mList, ImageLoader.getInstance());
        adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
        mRecyclerView.setAdapter(adapter);

        return view;
    }

    //################ Inicio Metodos do clique e longClique ##############
    @Override
    public void onClickListener(View view, int position) {

        //Toast.makeText(getActivity(), "Passei aki " + position, Toast.LENGTH_SHORT).show();

        //Intent it = new Intent(getActivity(), DetalhesProdutoActivity.class);
        //it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        //startActivity(it);

    }

    @Override
    public void onLongClickListener(View view, int position) {

        //Toast.makeText(getActivity(), "onLongClickListener: " + position, Toast.LENGTH_SHORT).show();
        listaOpcoes(position);
        //Todo os metodos para o Long clique aki
/*
        ProdutosDestaqueAdapter adapter = (ProdutosDestaqueAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);
*/
    }
    //################ Fim Metodos do clique e longClique ##############

    // LISTAR OPCOES
    private void listaOpcoes(final int position) {
        // Lista de itens
        ArrayList<String> itens = new ArrayList<String>();
        itens.add("Excluir");
        itens.add("Desconto");
        itens.add("Alterar quantidade");
        itens.add("Observação");

        // adapter utilizando um layout customizado (TextView)
        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.list_opcoes, itens);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Opções do produto");

        // define o di�logo como uma lista, passa o adapter.
        builder.setSingleChoiceItems(adapter, 0,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        // #################### OPCAO DE EXCLUIR
                        // #####################
                        if (arg1 == 0) {
                            db.delete("android_venda_detalhe", "_id=?", new String[]{String.valueOf(mList.get(position).getId())});
                            atualizarLista();
                        }

                        //############################# OPÇAO DESCONTO ################

                        if (arg1 == 1) {
                            showInputDialogValorDesconto(position);
                        }

                        //############################# FIM OPÇAO DESCONTO ################

                        // ############################## Opção Alterar quantidade ##############################
                        if (arg1 == 2){
                            showInputDialogAlterarQuantidade(position);
                        }
                        //###################################### fim Opção Alterar quantidade ####################

                        // ############################## Opção inserir Observação ##############################
                        if (arg1 == 3){
                            showInputDialogObservacao(position);
                        }
                        //###################################### fim Opção Alterar quantidade ####################

                        alerta.dismiss();
                    }
                });


        alerta = builder.create();
        alerta.show();
    }

        public void showInputDialogValorDesconto(final int posicao) {

        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("DESCONTO");

        final EditText input = new EditText(getActivity());
        //input.setInputType(InputType.TYPE_CLASS_NUMBER); //Problema com a virgula e ponto
        alert.setView(input);

        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio do desconto########################
                        contentValues.clear();

                        //Double valorTotal = mList.get(posicao).getValorTotal();
                        Double valorUnitario = (mList.get(posicao).getValorParcial()) / (mList.get(posicao).getQuantidade());
                        Double valorTotal = 0.0;

                        try {
                            desconto = input.getText().toString().replace(",", ".");

                            if(desconto.indexOf("%") != -1){ //Se for diferente de -1 é pq existe o caracter.

                                desconto = desconto.replace("%", "");

                                Double porcentagem = (mList.get(posicao).getValorParcial()) / 100;
                                final Double desconto2 = porcentagem * Double.valueOf(desconto);

                                valorTotal = mList.get(posicao).getValorParcial() - desconto2;
                                //contentValues.put("valor_desconto", desconto2);

                                Double verificador = verificaDesconto(mList.get(posicao).getProduto().toString(), (valorTotal / mList.get(posicao).getQuantidade()));

                                if (verificador > 0){
                                    contentValues.put("valor_desconto", desconto2);
                                    contentValues.put("valor_total", valorTotal);

                                    db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                                    atualizarLista();

                                } else {

                                    //##############################################
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("O desconto ultrapassou o permitido em R$ " + arredontar(verificador * (-1)) + "");

                                    final Double finalValorTotal = valorTotal;

                                    alertDialogBuilder
                                            .setMessage(
                                                    "Ao optar por continuar, a diferênça será descontado de seus créditos se os tiver. Deseja continuar?")
                                            .setCancelable(false)
                                            .setPositiveButton("Sim",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            contentValues.put("valor_desconto", desconto2);
                                                            contentValues.put("valor_total", finalValorTotal);

                                                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
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


                            }else{

                                Double desconto3 = Double.valueOf(desconto);
                                valorTotal = (mList.get(posicao).getValorParcial()) - desconto3;

                                Double verificador = verificaDesconto(mList.get(posicao).getProduto().toString(), (valorTotal / mList.get(posicao).getQuantidade()));

                                if (verificador > 0){
                                    contentValues.put("valor_desconto", desconto);
                                    contentValues.put("valor_total", valorTotal);

                                    db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                                    atualizarLista();

                                } else {

                                    //##############################################
                                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("O desconto ultrapassou o permitido em R$ " + arredontar(verificador * (-1)) + "");

                                    final Double finalValorTotal1 = valorTotal;
                                    alertDialogBuilder
                                            .setMessage(
                                                    "Ao optar por continuar, a diferênça será descontado de seus créditos se os tiver. Deseja continuar?")
                                            .setCancelable(false)
                                            .setPositiveButton("Sim",
                                                    new DialogInterface.OnClickListener() {
                                                        public void onClick(DialogInterface dialog, int id) {

                                                            contentValues.put("valor_desconto", desconto);
                                                            contentValues.put("valor_total", finalValorTotal1);

                                                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
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


                            }

                            //contentValues.put("valor_total", valorTotal);

                            //db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            //atualizarLista();

                        } catch (Exception e) {
                            desconto = "0.0";

                            valorUnitario = valorUnitario - Double.valueOf(desconto);

                            contentValues.put("valor_desconto", desconto);
                            contentValues.put("valor_total", valorUnitario);

                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            atualizarLista();

                        }

                    }
                });

        alert.show();

    }

    public void showInputDialogAlterarQuantidade(final int posicao) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        alert.setTitle("Quantidade");

        final EditText input = new EditText(getActivity());
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);

        alert.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog,
                                        int whichButton) {
                        //##################Inicio do desconto########################
                        contentValues.clear();

                        Double valorParcial = mList.get(posicao).getValorParcial();
                        Double quantidadeAtual = mList.get(posicao).getQuantidade();
                        Double desconto = mList.get(posicao).getValorDesconto();
                        Double totalGeral = 0.0;

                        Double valorUnitario = valorParcial/quantidadeAtual;

                        try {
                            quantidade = input.getText().toString().replace(",", ".");

                            valorParcial = valorUnitario * Double.valueOf(quantidade);
                            totalGeral = valorParcial - desconto;

                            contentValues.put("quantidade", quantidade);
                            contentValues.put("valor_parcial", valorParcial);
                            contentValues.put("valor_total", totalGeral);

                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            atualizarLista();

                        } catch (Exception e) {
                            quantidade = "1.0";

                            valorParcial = valorUnitario * Double.valueOf(quantidade);
                            totalGeral = valorParcial - desconto;

                            contentValues.put("quantidade", quantidade);
                            contentValues.put("valor_parcial", valorParcial);
                            contentValues.put("valor_total", totalGeral);

                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            atualizarLista();

                        }
                    }
                });

        alert.show();
    }

    public void showInputDialogObservacao(final int posicao) {
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

                            contentValues.put("obs", obs);

                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            atualizarLista();

                        } catch (Exception e) {
                            obs = "";

                            contentValues.put("obs", obs);

                            db.update("android_venda_detalhe", contentValues, "_id=?", new String[]{String.valueOf(mList.get(posicao).getId())});
                            atualizarLista();
                        }
                    }
                });

        alert.show();
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

        mList = ((VendaActivity) getActivity()).getSetAndroidVendaDetalheListCompleta();
        VendasAdapter adapter = new VendasAdapter(getActivity(), mList, ImageLoader.getInstance());
        //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
        mRecyclerView.setAdapter(adapter);

        ((VendaActivity) getActivity()).atualizarValores(); //Atualiza os valores totais do rodapé
    }

    public Double arredontar(double valor) {
        Double retorno = null;
        BigDecimal a = new BigDecimal(valor);
        a = a.setScale(2, BigDecimal.ROUND_HALF_UP);
        retorno = a.doubleValue();
        return retorno;
    }

    private Double verificaDesconto(String idProfuto, Double valor){

        Double retorno = null;
        Double valorMinimo = 0.0;

        try {
            produtoCursor = db
                    .rawQuery(
                            "SELECT _id, valor_minimo_venda FROM produto where _id like '" + idProfuto + "'", null);

            if (produtoCursor.moveToFirst()){
                valorMinimo = produtoCursor.getDouble(1);

                retorno = valor - valorMinimo;

            }
        }catch (Exception e){

        }

        return retorno;
    }
}
