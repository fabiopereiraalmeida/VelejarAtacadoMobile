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
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

/**
 * Created by fabio on 16/07/15.
 */
public class ProdutosAdapter extends RecyclerView.Adapter<ProdutosAdapter.MyViewHolder> {

    private List<Produto> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private ImageLoader mImageLoader;
    private Context context;

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    public ProdutosAdapter(Context c, List<Produto> l, ImageLoader mImageLoader) {

        this.mList = l;
        this.mLayoutInflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        //Configuração inicial
        dbHelper = new DBHelper(c, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        this.mImageLoader = mImageLoader;
        this.context = c;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) { //Só é chamado na hora de cria uma nova view

        Log.i("LOG", "onCreateViewHolder");
        //View v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        View v;
        if (Configuracao.getVisualizarCards()){
            v = mLayoutInflater.inflate(R.layout.item_produto_card, viewGroup, false);
        }else {
            v = mLayoutInflater.inflate(R.layout.item_produto, viewGroup, false);
        }

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

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) { //É chamado toda hora para setar os valores da lista "mList"
        if (mList.get(position).getImagem() != null){

            Log.i("IMAGEM", "NAO NULO");

            ByteArrayInputStream imageStream = new ByteArrayInputStream(mList.get(position).getImagem());
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);
            //Bitmap theImage = BitmapFactory.decodeByteArray(mList.get(position).getImagem(), 0, mList.get(position).getImagem().length);

            holder.ivProduto.setImageBitmap(theImage);
        }else{
            holder.ivProduto.setImageResource(R.drawable.sem_foto);
        }

        holder.tvCodigoProduto.setText(mList.get(position).getCodigo());

        if (mList.get(position).getMarca() != null) {
            Cursor cursorMarca;
            try {
                cursorMarca = db
                        .rawQuery(
                                "SELECT _id, nome FROM marca where _id like '" + mList.get(position).getMarca().toString() + "'", null);

                cursorMarca.moveToFirst();

                holder.tvMarcaProduto.setText(cursorMarca.getString(1));
            } catch (Exception e) {
                holder.tvMarcaProduto.setText("");
            }
            //holder.tvMarcaProduto.setText(mList.get(position).getMarca());
        }else{
            holder.tvMarcaProduto.setText("");
        }

        holder.tvDescricaoProduto.setText(mList.get(position).getNome());
        if (mList.get(position).getUnidade() != null) {
            Cursor cursorUnidade;
            try {
                cursorUnidade = db
                        .rawQuery(
                                "SELECT _id, nome FROM unidade where _id like '" + mList.get(position).getUnidade().toString() + "'", null);

                cursorUnidade.moveToFirst();

                holder.tvUnidadeProduto.setText(cursorUnidade.getString(1));
            } catch (Exception e) {
                holder.tvUnidadeProduto.setText("");
            }
            //holder.tvUnidadeProduto.setText(mList.get(position).getUnidade().toString());
        }else{
            holder.tvUnidadeProduto.setText("xx");
        }
        holder.tvPrecoProduto.setText("R$ " + String.format("%.2f", mList.get(position).getValorDesejavelVenda()));
        holder.tvEstoqueProduto.setText("EST.: " + mList.get(position).getEstoque().toString());
        try {
            holder.tvExpositorProduto.setText("EXP.: " + mList.get(position).getExpositor().toString());
        }catch (Exception e){
            holder.tvExpositorProduto.setText("ESP.: 0.0");
        }

        if (!Configuracao.getVisualizarCards()){
            try {
                holder.tvPrecoMinimo.setText("R$ " + mList.get(position).getValorMinimoVenda().toString());
            }catch (Exception e){
                holder.tvPrecoMinimo.setText("");
            }
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

    public void addListItem(Produto produto, int posicao) {

        mList.add(produto);
        notifyItemInserted(posicao);
    }

    public void removeListItem(int position) {
        mList.remove(position);
        notifyItemRemoved(position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView ivProduto;
        public TextView tvCodigoProduto;
        public TextView tvMarcaProduto;
        public TextView tvDescricaoProduto;
        public TextView tvUnidadeProduto;
        public TextView tvPrecoProduto;
        public TextView tvPrecoMinimo;
        public TextView tvEstoqueProduto;
        public TextView tvExpositorProduto;

        public MyViewHolder(View itemView) {
            super(itemView);

            if (Configuracao.getVisualizarCards()){
                ivProduto = (ImageView) itemView.findViewById(R.id.iv_produto);
                tvDescricaoProduto = (TextView) itemView.findViewById(R.id.tv_nome_produto);
                tvPrecoProduto = (TextView) itemView.findViewById(R.id.tv_preco_produto);
                //tvPrecoMinimo = (TextView) itemView.findViewById(R.id.tv_venda_preco_minimo);
                tvEstoqueProduto = (TextView) itemView.findViewById(R.id.tv_estoque_card);
            }else{
                ivProduto = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
                tvCodigoProduto = (TextView) itemView.findViewById(R.id.tv_item_codigo);
                tvMarcaProduto = (TextView) itemView.findViewById(R.id.tv_item_marca);
                tvDescricaoProduto = (TextView) itemView.findViewById(R.id.tv_caixa_nome_cliente);
                tvUnidadeProduto = (TextView) itemView.findViewById(R.id.tv_item_unidade);
                tvPrecoProduto = (TextView) itemView.findViewById(R.id.tv_venda_preco_unitario);
                tvPrecoMinimo = (TextView) itemView.findViewById(R.id.tv_venda_preco_minimo);
                tvEstoqueProduto = (TextView) itemView.findViewById(R.id.tv_venda_estoque);
                tvExpositorProduto = (TextView) itemView.findViewById(R.id.tv_venda_expositor);

            }
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
