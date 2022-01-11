package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;


/**
 * Created by fabio on 16/07/15.
 */
public class VendasFinalizadasAdapter extends RecyclerView.Adapter<VendasFinalizadasAdapter.MyViewHolder> {

    private List<AndroidVendaCabecalho> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSAEstenso = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");

    private ImageLoader mImageLoader;

    public SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues contentValues;
    private Cursor cursor;

    public VendasFinalizadasAdapter(Context c, List<AndroidVendaCabecalho> l, ImageLoader mImageLoader) {

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
        View v = mLayoutInflater.inflate(R.layout.item_vendas_finalizadas, viewGroup, false);
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

        holder.iv_venda_item.setImageResource(R.drawable.sem_foto);

        try {
            //Date dt = formatDataHoraUSAEstenso.parse(mList.get(position).getDataVenda());
            //holder.tv_vendas_data_venda.setText(formatSoapHora.format(dt));
            holder.tv_vendas_data_venda.setText(mList.get(position).getDataVenda());
        }catch (Exception e){
           holder.tv_vendas_data_venda.setText("");
        }
        holder.tv_vendas_valor_venda.setText("R$ " + String.format("%.2f", mList.get(position).getValorTotal()).toString());
        holder.tv_vendas_estado_venda.setText(mList.get(position).getVendaAprovada().toString());
        if (mList.get(position).getVendaAprovada().equals(true)){
            holder.tv_vendas_estado_venda.setText("Finalizada");
            holder.tv_vendas_estado_venda.setTextColor(Color.GREEN);
        }else {
            holder.tv_vendas_estado_venda.setText("Não finalizada");
            holder.tv_vendas_estado_venda.setTextColor(Color.RED);
        }

        try {

            cursor = db
                    .rawQuery(
                            "SELECT _id, razaoSocial FROM cliente where _id like '" + mList.get(position).getCliente() + "'", null);

            cursor.moveToFirst();

            holder.tv_vendas_nome_cliente.setText(cursor.getString(1));
        }catch (Exception e){
            holder.tv_vendas_nome_cliente.setText("Erro!");
        }

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

    public void addListItem(AndroidVendaCabecalho androidVendaCabecalho, int posicao) {

        mList.add(androidVendaCabecalho);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView iv_venda_item;
        public TextView tv_vendas_nome_cliente;
        public TextView tv_vendas_data_venda;
        public TextView tv_vendas_valor_venda;
        public TextView tv_vendas_estado_venda;

        public MyViewHolder(View itemView) {
            super(itemView);

            iv_venda_item = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
            tv_vendas_nome_cliente = (TextView) itemView.findViewById(R.id.tv_caixa_nome_cliente);
            tv_vendas_data_venda = (TextView) itemView.findViewById(R.id.tv_vendas_data_venda);
            tv_vendas_valor_venda = (TextView) itemView.findViewById(R.id.tv_vendas_valor_venda);
            tv_vendas_estado_venda = (TextView) itemView.findViewById(R.id.tv_vendas_estado_venda);

            itemView.setOnClickListener(this); //define o toque o item
        }

        @Override
        public void onClick(View v) {
            if (mRecyclerViewOnClickListenerHack != null) {
                mRecyclerViewOnClickListenerHack.onClickListener(v, getPosition());
            }
        }
    }
/*
    public Double arredontar(double valor) {
        Double retorno = null;
        BigDecimal a = new BigDecimal(valor);
        a = a.setScale(2, BigDecimal.ROUND_HALF_UP);
        retorno = a.doubleValue();
        return retorno;
    }
*/

}
