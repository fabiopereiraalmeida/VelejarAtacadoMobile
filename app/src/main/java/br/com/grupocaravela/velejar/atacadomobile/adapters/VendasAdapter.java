package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;


/**
 * Created by fabio on 16/07/15.
 */
public class VendasAdapter extends RecyclerView.Adapter<VendasAdapter.MyViewHolder> {

    private List<AndroidVendaDetalhe> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private ImageLoader mImageLoader;

    private Cursor cursor;
    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private Context context;

    public VendasAdapter(Context c, List<AndroidVendaDetalhe> l, ImageLoader mImageLoader) {

        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.mImageLoader = mImageLoader;

        //Configuração inicial
        dbHelper = new DBHelper(c, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //Só é chamado na hora de cria uma nova view

        Log.i("LOG", "onCreateViewHolder");
        //View v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        View v = mLayoutInflater.inflate(R.layout.item_venda_produto, viewGroup, false);
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

        //holder.tvPrecoUnitario.setText("R$ " + String.format("%.2f", (mList.get(position).getValorParcial() / mList.get(position).getQuantidade())).toString()); //String.format("%.2f", d)
        holder.tvPrecoUnitario.setText("R$ " + String.format("%.2f", mList.get(position).getValorUnitario()).toString());
        holder.tvQuantidade.setText(mList.get(position).getQuantidade().toString());
        holder.tvValorTotal.setText("R$ " + String.format("%.2f", mList.get(position).getValorParcial()).toString());
        holder.tvDesconto.setText("R$ " + String.format("%.2f", mList.get(position).getValorDesconto()).toString());
        holder.tvTotalGeral.setText("R$ " + String.format("%.2f", mList.get(position).getValorTotal()).toString());
        //holder.tvObs.setText(mList.get(position).getObs());

        //holder.tvUnidade.setText(mList.get(position).getProduto().toString());

        cursor = db
                .rawQuery(
                        "SELECT _id, codigo, nome, valor_desejavel_venda, unidade_id, imagem, codigo_ref, marca_id FROM produto where _id like '" + mList.get(position).getProduto().toString() + "'", null);


        if (cursor.moveToFirst()) {

            try {
                holder.tvDescricaoProduto.setText(cursor.getString(2));
            }catch (Exception e){
                holder.tvDescricaoProduto.setText("ERRO!");
            }
            /*
            try {
                holder.tvPrecoUnitario.setText("R$ " + String.format("%.2f", cursor.getDouble(3))); //String.format("%.2f", d)
            }catch (Exception e){
                holder.tvPrecoUnitario.setText("ERRO");
            }
            */

            holder.tvCodigo.setText(cursor.getString(1));

            int idUnidade = cursor.getInt(4);
            Cursor cursorUnidade;
            try {
                cursorUnidade = db
                        .rawQuery(
                                "SELECT _id, nome FROM unidade where _id like '" + idUnidade + "'", null);
                cursorUnidade.moveToFirst();
                holder.tvUnidade.setText(cursorUnidade.getString(1));
            } catch (Exception e) {
                holder.tvUnidade.setText("");
            }

            int idMarca = cursor.getInt(7);
            Cursor cursorMarca;
            try {
                cursorMarca = db
                        .rawQuery(
                                "SELECT _id, nome FROM marca where _id like '" + idMarca + "'", null);

                cursorMarca.moveToFirst();

                holder.tvMarca.setText(cursorMarca.getString(1));
            } catch (Exception e) {
                holder.tvMarca.setText("");
            }

            if (cursor.getBlob(5) != null){

                Log.i("IMAGEM", "NAO NULO");

                ByteArrayInputStream imageStream = new ByteArrayInputStream(cursor.getBlob(5));
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                //Bitmap theImage = BitmapFactory.decodeByteArray(mList.get(position).getImagem(), 0, mList.get(position).getImagem().length);

                holder.ivProduto.setImageBitmap(theImage);
            }else{
                holder.ivProduto.setImageResource(R.drawable.sem_foto);
            }

/*
            try {
                holder.tvObs.setText(mList.get(position).getObs());
            }catch (Exception e){
                holder.tvObs.setText("ERRO!");
            }
            */
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

    public void addListItem(AndroidVendaDetalhe avd, int posicao) {

        mList.add(avd);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProduto;
        public TextView tvDescricaoProduto;
        public TextView tvMarca;
        public TextView tvPrecoUnitario;
        public TextView tvQuantidade;
        public TextView tvExpositor;
        public TextView tvValorTotal;
        public TextView tvDesconto;
        public TextView tvTotalGeral;
        public TextView tvUnidade;
        public TextView tvCodigo;
        public TextView tvObs;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivProduto = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
            tvDescricaoProduto = (TextView) itemView.findViewById(R.id.tv_venda_descricao_produto);
            tvMarca = (TextView) itemView.findViewById(R.id.textViewVendaMarca);
            tvPrecoUnitario = (TextView) itemView.findViewById(R.id.tv_venda_valor_unitario);
            tvQuantidade = (TextView) itemView.findViewById(R.id.tv_venda_qtd);
            tvValorTotal = (TextView) itemView.findViewById(R.id.tv_venda_valor_parcial);
            tvDesconto = (TextView) itemView.findViewById(R.id.tv_venda_desconto);
            tvTotalGeral = (TextView) itemView.findViewById(R.id.tv_venda_total_geral);
            tvUnidade = (TextView) itemView.findViewById(R.id.tv_venda_estoque_unidade);
            tvCodigo = (TextView) itemView.findViewById(R.id.tv_venda_codigo);
            //tvObs = (TextView) itemView.findViewById(R.id.tv_obs_produto);

            //tvPrecoUnitario.addTextChangedListener(new MascaraMonetaria((EditText) tvPrecoUnitario));
            //tvValorTotal.addTextChangedListener(new MascaraMonetaria((EditText) tvValorTotal));
            //tvDesconto.addTextChangedListener(new MascaraMonetaria((EditText) tvDesconto));
            //tvTotalGeral.addTextChangedListener(new MascaraMonetaria((EditText) tvTotalGeral));

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
