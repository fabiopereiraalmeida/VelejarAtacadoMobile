package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.InputStream;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidCaixa;


/**
 * Created by fabio on 16/07/15.
 */
public class CaixaAdapter extends RecyclerView.Adapter<CaixaAdapter.MyViewHolder> {

    private List<AndroidCaixa> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private ImageLoader mImageLoader;

    public SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues contentValues;
    private Cursor cursorcliente;

    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy");

    public CaixaAdapter(Context c, List<AndroidCaixa> l, ImageLoader mImageLoader) {

        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mImageLoader = mImageLoader;

        //Configuração inicial
        dbHelper = new DBHelper(c, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //Só é chamado na hora de cria uma nova view

        Log.i("LOG", "onCreateViewHolder");
        //View v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        View v = mLayoutInflater.inflate(R.layout.item_caixa, viewGroup, false);
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

        holder.ivContaReceber.setImageResource(R.drawable.sem_foto);

        Log.i("INFO", "Cliente nº " + mList.get(position).getCliente());

        try {
            cursorcliente = db
                            .rawQuery(
                            "SELECT _id, razaoSocial FROM cliente where _id like '" + mList.get(position).getCliente().toString() + "'", null);

            cursorcliente.moveToFirst();

            //Log.i("INFO", "Cliente nº " + mList.get(position).getCliente());
            //Log.i("INFO", "Cliente nome " + cursorcliente.getString(1));

            holder.tvRazaoSocialCliente.setText(cursorcliente.getString(1));
        }catch (Exception e){
            holder.tvRazaoSocialCliente.setText("Cliente não encontrado!");
        }

        holder.tvValor.setText("R$ " + String.format("%.2f", mList.get(position).getValor()));

        try {
            //holder.tvDataPagamento.setText(formatDataHoraBRA.format(mList.get(position).getDataRecebimento()));
            holder.tvDataPagamento.setText(mList.get(position).getDataRecebimento());
        }catch (Exception e){

        }

/*
        if (mList.get(position).getAtivo().equals(true)){
            holder.tvEstado.setText("Ativo");
            holder.tvEstado.setTextColor(Color.GREEN);
        }else {
            holder.tvEstado.setText("Bloqueado");
            holder.tvEstado.setTextColor(Color.RED);
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

    public void addListItem(AndroidCaixa androidCaixa, int posicao) {

        mList.add(androidCaixa);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivContaReceber;
        public TextView tvRazaoSocialCliente;
        public TextView tvValor;
        public TextView tvDataPagamento;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivContaReceber = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
            tvRazaoSocialCliente = (TextView) itemView.findViewById(R.id.tv_caixa_nome_cliente);
            tvValor = (TextView) itemView.findViewById(R.id.tv_caixa_valor);
            tvDataPagamento = (TextView) itemView.findViewById(R.id.tv_caixa_data_recebimento);

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
