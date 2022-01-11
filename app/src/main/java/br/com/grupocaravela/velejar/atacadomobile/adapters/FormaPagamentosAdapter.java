package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.FormaPagamento;


/**
 * Created by fabio on 16/07/15.
 */
public class FormaPagamentosAdapter extends RecyclerView.Adapter<FormaPagamentosAdapter.MyViewHolder> {

    private List<FormaPagamento> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private ImageLoader mImageLoader;

    public FormaPagamentosAdapter(Context c, List<FormaPagamento> l, ImageLoader mImageLoader) {

        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mImageLoader = mImageLoader;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //Só é chamado na hora de cria uma nova view

        Log.i("LOG", "onCreateViewHolder");
        //View v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        View v = mLayoutInflater.inflate(R.layout.item_forma_pagamento, viewGroup, false);
        MyViewHolder mvh = new MyViewHolder(v);

        return mvh;
    }

    private Drawable LoadImageFromWebOperations(String url) {
        try {
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable drawable = Drawable.createFromStream(is, "src name");
            return drawable;
        } catch (Exception e) {
            //System.out.println("Exc="+e);
            return null;
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) { //É chamado toda hora para setar os valores da lista "mList"

        holder.tvFormaPagamento.setText(mList.get(position).getNome());
        holder.tvParcelas.setText(mList.get(position).getNumeroParcelas().toString());
        holder.tvJuros.setText(mList.get(position).getJuros().toString());

        /*
        if (mList.get(position).getNumeroParcelas().equals("anyType{}")){
            holder.tvParcelas.setText("");
        }else{
            try {
                holder.tvParcelas.setText(mList.get(position).getNumeroParcelas());
            }catch (Exception e){

            }

        }
*/
        //######### ANIMAÇÃO ###############
        try {
            YoYo.with(Techniques.Landing) //Tada / RollIn
                    .duration(700)
                    .playOn(holder.itemView);
        }catch (Exception e){

        }
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(FormaPagamento formaPagamento, int posicao) {

        mList.add(formaPagamento);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView tvFormaPagamento;
        public TextView tvParcelas;
        public TextView tvJuros;

        public MyViewHolder(View itemView) {
            super(itemView);

            tvFormaPagamento = (TextView) itemView.findViewById(R.id.tv_venda_descricao_produto);
            tvParcelas = (TextView) itemView.findViewById(R.id.tv_numero_parcelas);
            tvJuros = (TextView) itemView.findViewById(R.id.tv_forma_pagamento_juros);

            itemView.setOnClickListener(this); //define o toque o item
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }

}
