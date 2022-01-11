package br.com.grupocaravela.velejar.atacadomobile.adapters;

import android.content.ContentValues;
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
import java.text.SimpleDateFormat;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.interfaces.RecyclerViewOnClickListenerHack;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;


/**
 * Created by fabio on 16/07/15.
 */
public class ContasReceberAdapter extends RecyclerView.Adapter<ContasReceberAdapter.MyViewHolder> {

    private List<ContaReceber> mList;
    private LayoutInflater mLayoutInflater;
    private RecyclerViewOnClickListenerHack mRecyclerViewOnClickListenerHack;

    private ImageLoader mImageLoader;

    public SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues contentValues;
    private Cursor cursor;

    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy");

    public ContasReceberAdapter(Context c, List<ContaReceber> l, ImageLoader mImageLoader) {

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
        View v = mLayoutInflater.inflate(R.layout.item_contas_receber, viewGroup, false);
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



        cursor = db
                .rawQuery(
                        "SELECT _id, razaoSocial, imagem FROM cliente where _id like '" + mList.get(position).getCliente() + "'", null);
        try {
        if (cursor.moveToFirst()) {

            holder.tvRazaoSocialCliente.setText(cursor.getString(1));
            holder.tvValor.setText("R$ " + String.format("%.2f", mList.get(position).getValorDevido()));
            holder.tvValorDesconto.setText("R$ " + String.format("%.2f", mList.get(position).getValorDesconto()));

            try {
                //holder.tvVencimento.setText(formatDataHoraBRA.format(mList.get(position).getVencimento()));
                holder.tvVencimento.setText(mList.get(position).getVencimento());
            } catch (Exception e) {
                holder.tvVencimento.setText("Não encontrado!");
            }

            //holder.ivContaReceber.setImageResource(R.drawable.sem_foto);
            if (cursor.getBlob(2) != null){

                Log.i("IMAGEM", "NAO NULO");

                ByteArrayInputStream imageStream = new ByteArrayInputStream(cursor.getBlob(2));
                Bitmap theImage = BitmapFactory.decodeStream(imageStream);
                //Bitmap theImage = BitmapFactory.decodeByteArray(mList.get(position).getImagem(), 0, mList.get(position).getImagem().length);

                holder.ivContaReceber.setImageBitmap(theImage);
            }else{
                holder.ivContaReceber.setImageResource(R.drawable.sem_foto);
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
            } catch (Exception e) {

            }
        }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {

        return mList.size();
    }

    public void setRecyclerViewOnClickListenerHack(RecyclerViewOnClickListenerHack r) {
        mRecyclerViewOnClickListenerHack = r;
    }

    public void addListItem(ContaReceber contaReceber, int posicao) {

        mList.add(contaReceber);
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
        public TextView tvValorDesconto;
        public TextView tvVencimento;

        public MyViewHolder(View itemView) {
            super(itemView);

            ivContaReceber = (ImageView) itemView.findViewById(R.id.iv_venda_imagem_produto);
            tvRazaoSocialCliente = (TextView) itemView.findViewById(R.id.tv_caixa_nome_cliente);
            tvValor = (TextView) itemView.findViewById(R.id.tv_contas_receber_valor);
            tvValorDesconto = (TextView) itemView.findViewById(R.id.tv_contas_receber_valor_desconto);
            tvVencimento = (TextView) itemView.findViewById(R.id.tv_contas_vencimento);

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
