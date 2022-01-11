package br.com.grupocaravela.velejar.atacadomobile.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.DetalhesProdutoActivity;
import br.com.grupocaravela.velejar.atacadomobile.ProdutosActivity;
import br.com.grupocaravela.velejar.atacadomobile.adapters.ProdutosAdapter;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

/**
 * Created by fabio on 16/07/15.
 */
public class ProdutosFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<Produto> mList;
    private EditText edtLocalizarProduto;
    private ImageView ivLocalizarProduto;

    private boolean buncando = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_produtos, container, false);

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
                        List<Produto> listAux = ((ProdutosActivity) getActivity()).getSetProdutoList(7);

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
                        List<Produto> listAux = ((ProdutosActivity) getActivity()).getSetProdutoListNome(nome);

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

            mList = ((ProdutosActivity) getActivity()).getSetProdutoList(7);
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

                mList = ((ProdutosActivity) getActivity()).getSetProdutoListNome(nome);
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

        //Toast.makeText(getActivity(), "Chama o Produto: " + position, Toast.LENGTH_SHORT).show();

        Intent it = new Intent(getActivity(), DetalhesProdutoActivity.class);
        it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        startActivity(it);

    }

    @Override
    public void onLongClickListener(View view, int position) {

        Toast.makeText(getActivity(), "onLongClickListener: " + position, Toast.LENGTH_SHORT).show();

        //Todo os metodos para o Long clique aki
/*
        ProdutosDestaqueAdapter adapter = (ProdutosDestaqueAdapter) mRecyclerView.getAdapter();
        adapter.removeListItem(position);
*/
    }
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
}
