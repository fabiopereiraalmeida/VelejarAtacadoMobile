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
import br.com.grupocaravela.velejar.atacadomobile.ClientesActivity;
import br.com.grupocaravela.velejar.atacadomobile.ContasReceberActivity;
import br.com.grupocaravela.velejar.atacadomobile.DetalhesClienteActivity;
import br.com.grupocaravela.velejar.atacadomobile.adapters.ClientesAdapter;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CategoriaCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;

/**
 * Created by fabio on 16/07/15.
 */
public class ClientesFragment extends Fragment implements RecyclerViewOnClickListenerHack {

    private RecyclerView mRecyclerView;
    private List<Cliente> mList;
    private EditText edtLocalizarClientes;
    private Spinner spinnerCategoria;
    private ImageView ivLocalizarCliente;

    private boolean buncando = false;
    private CategoriaCliente categoriaCliente = null;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clientes, container, false);

        edtLocalizarClientes = (EditText) view.findViewById(R.id.edt_localizar_clientes);
        edtLocalizarClientes.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

        List<CategoriaCliente> listAux = ((ClientesActivity) getActivity()).getCategoriaClienteList();

        ArrayAdapter<CategoriaCliente> adapterCategotia = new ArrayAdapter<CategoriaCliente>(getActivity(), android.R.layout.simple_spinner_item, listAux);
        adapterCategotia.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerCategoria = view.findViewById(R.id.spinnerCategoriasPesquisaClientes);
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

                String nome = String.valueOf(edtLocalizarClientes.getText());

                mList = ((ClientesActivity) getActivity()).getSetClienteListNome(nome, categoriaCliente);
                ClientesAdapter adapter = new ClientesAdapter(getActivity(), mList, ImageLoader.getInstance());
                //adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
                mRecyclerView.setAdapter(adapter);

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

        ivLocalizarCliente = (ImageView) view.findViewById(R.id.iv_localizar_clientes);
        ivLocalizarCliente.setBackgroundResource(R.drawable.buscar_azul_48x48);

        mRecyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_cliente_list); //Dentro de "fragment_empresa"
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
                    ClientesAdapter adapter = (ClientesAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
                        List<Cliente> listAux = ((ClientesActivity) getActivity()).getSetClienteList(7, categoriaCliente);

                        for (int i = 0; i < listAux.size(); i++) {
                            adapter.addListItem(listAux.get(i), mList.size());
                        }

                    }

                }

                if (buncando == true) {

                    //LinearLayoutManager para a Lista RecycleView
                    LinearLayoutManager llm = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                    ClientesAdapter adapter = (ClientesAdapter) mRecyclerView.getAdapter();

                    //Log.i("PASSEI", "AKI 01 ");

                    if (mList.size() == llm.findLastCompletelyVisibleItemPosition() + 1) { //se o tamanho da lista 'mList' for igual a ultima posição exibida
                        /*
                        String nome = String.valueOf(edtLocalizarClientes.getText());
                        List<Cliente> listAux = ((ClientesActivity) getActivity()).getSetClienteListNome(nome, categoriaCliente);

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

            mList = ((ClientesActivity) getActivity()).getSetClienteList(7, categoriaCliente);
            ClientesAdapter adapter = new ClientesAdapter(getActivity(), mList, ImageLoader.getInstance());
            adapter.setRecyclerViewOnClickListenerHack(this); //Responsavel pelo clique
            mRecyclerView.setAdapter(adapter);

        }


        //#################### INICIO DO FILTRO PELA BUSCA ###########################
        edtLocalizarClientes.addTextChangedListener(new TextWatcher() {


            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String nome = String.valueOf(edtLocalizarClientes.getText());

                mList = ((ClientesActivity) getActivity()).getSetClienteListNome(nome, categoriaCliente);
                ClientesAdapter adapter = new ClientesAdapter(getActivity(), mList, ImageLoader.getInstance());
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

        //Toast.makeText(getActivity(), "Chama o Cliente: " + position, Toast.LENGTH_SHORT).show();

        Intent it = new Intent(getActivity(), DetalhesClienteActivity.class);
        it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        startActivity(it);

    }

    @Override
    public void onLongClickListener(View view, int position) {

        //Toast.makeText(getActivity(), "onLongClickListener: " + position, Toast.LENGTH_SHORT).show();

        Intent it = new Intent(getActivity(), ContasReceberActivity.class);
        it.putExtra("id", mList.get(position).getId()); //Passando o id (Código) do Cliente
        //Toast.makeText(getActivity(), "Id do Cliente selecionado: " + mList.get(position).getId(), Toast.LENGTH_SHORT).show();
        startActivity(it);

        //Todo os metodos para o Long clique aki
/*
        ClientesDestaqueAdapter adapter = (ClientesDestaqueAdapter) mRecyclerView.getAdapter();
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
