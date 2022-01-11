package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.ClienteUtil;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;


/**
 * Created by fabio on 16/07/15.
 */
public class ClientesAdapter extends RecyclerView.Adapter<ClientesAdapter.MyViewHolder> {

    private ClienteUtil clienteUtil;

    private List<Cliente> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    //private Context c;

    private ImageLoader mImageLoader;

    public ClientesAdapter(Context c, List<Cliente> l, ImageLoader mImageLoader) {

        //this.c = c;
        clienteUtil = new ClienteUtil(c);
        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mImageLoader = mImageLoader;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //Só é chamado na hora de cria uma nova view

        Log.i("LOG", "onCreateViewHolder");
        //View v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        View v = mLayoutInflater.inflate(R.layout.item_cliente, viewGroup, false);
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

        if (clienteUtil.pendencia(mList.get(position).getId().toString())){
            //holder.rlCliente.setBackgroundColor(Color.parseColor("#FF0000"));
            //holder.tvRazaoSocialCliente.setBackgroundColor(Color.parseColor("#FF0000"));
            holder.tvRazaoSocialCliente.setTextColor(Color.parseColor("#FF0000"));
        }else{
            holder.tvRazaoSocialCliente.setTextColor(Color.parseColor("#000000"));
        }

        //holder.ivCliente.setImageResource(R.drawable.sem_foto);
        if (mList.get(position).getImagem() != null){

            Log.i("IMAGEM", "NAO NULO");

            ByteArrayInputStream imageStream = new ByteArrayInputStream(mList.get(position).getImagem());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            //Bitmap theImage = BitmapFactory.decodeByteArray(mList.get(position).getImagem(), 0, mList.get(position).getImagem().length);

            holder.ivCliente.setImageBitmap(theImage);
        }else{
            holder.ivCliente.setImageResource(R.drawable.sem_foto);
        }

        holder.tvRazaoSocialCliente.setText(mList.get(position).getRazaoSocial());
        holder.tvFantasiaCliente.setText(mList.get(position).getFantasia());
        holder.tvApelidoCliente.setText(mList.get(position).getCnpj());

        if (mList.get(position).getAtivo().toString().equals("true")){
            holder.tvEstado.setText("Ativo");
            holder.tvEstado.setTextColor(Color.GREEN);
        }
        if (mList.get(position).getAtivo().equals(false)){
            holder.tvEstado.setText("Bloqueado");
            holder.tvEstado.setTextColor(Color.RED);
        }

        //Log.d("IMPORTANTE", "ATIVO: " + mList.get(position).getAtivo());

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

    public void addListItem(Cliente cliente, int posicao) {

        mList.add(cliente);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public RelativeLayout rlCliente;
        public ImageView ivCliente;
        public TextView tvRazaoSocialCliente;
        public TextView tvFantasiaCliente;
        public TextView tvApelidoCliente;
        public TextView tvEstado;

        public MyViewHolder(View itemView) {
            super(itemView);

            //clienteUtil = new ClienteUtil(this.c);

            rlCliente = itemView.findViewById(R.id.rl_cliente);
            ivCliente = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
            tvRazaoSocialCliente = (TextView) itemView.findViewById(R.id.tv_caixa_nome_cliente);
            tvFantasiaCliente = (TextView) itemView.findViewById(R.id.tv_venda_preco_unitario);
            tvApelidoCliente = (TextView) itemView.findViewById(R.id.tv_apelido_cliente_item);
            tvEstado = (TextView) itemView.findViewById(R.id.tv_situacao_cliente_item);

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
