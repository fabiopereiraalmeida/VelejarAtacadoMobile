package br.com.grupocaravela.velejar.atacadomobile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.ByteArrayInputStream;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Imagem;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;

public class DetalhesProdutoActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private Intent mIntent;

    private Toolbar mainToolbarTop;

    private Cliente cliente;

    private TextView tvId;
    private TextView tvCodigo;
    private TextView tvDescricao;
    private TextView tvUnidade;
    private TextView tvPeso;
    private TextView tvEstoque;
    private TextView tvCategoria;
    private TextView tvPreco;
    private ImageView ivProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_produto);

        mIntent = getIntent();

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Detalhes de produtos");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvId = (TextView) findViewById(R.id.tv_conta_receber_id);
        tvCodigo = (TextView) findViewById(R.id.tv_conta_receber_razao_social_cliente);
        tvDescricao = (TextView) findViewById(R.id.tv_conta_receber_fantasia_cliente);
        tvUnidade = (TextView) findViewById(R.id.tv_cliente_telefone);
        tvPeso = (TextView) findViewById(R.id.tv_conta_receber_cnpj_cliente);
        tvEstoque = (TextView) findViewById(R.id.tv_produto_estoque);
        tvCategoria = (TextView) findViewById(R.id.tv_produto_categoria);
        tvPreco = (TextView) findViewById(R.id.tv_produto_preco);

        ivProduto = (ImageView) findViewById(R.id.iv_produto_detalhes);


/*
        Bitmap imagem = Imagem.carregarImagem(this, "produto_" + cursor.getInt(0) + ".png");

        if (imagem != null){
            ivProduto.setImageBitmap(imagem);
        }else{
            ivProduto.setImageResource(R.drawable.sem_foto);
        }
*/
        //Toast.makeText(DetalhesClienteActivity.this, "Id do Cliente selecionado: " + mIntent.getLongExtra("id", 0), Toast.LENGTH_SHORT).show();
        carregarCampos();

    }

    private void carregarCampos(){
        cursor = db
                .rawQuery(
                        "SELECT _id, codigo, nome, estoque, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, ativo, peso, " +
                                "empresa_id, imagem, codigo_ref FROM produto WHERE _id LIKE '" + mIntent.getLongExtra("id", 0) + "'", null);

        cursor.moveToFirst();

        tvId.setText(String.valueOf(cursor.getInt(0)));
        tvCodigo.setText(cursor.getString(1));
        tvDescricao.setText(cursor.getString(2));
        tvUnidade.setText(String.valueOf(cursor.getInt(7)));
        tvEstoque.setText(String.valueOf(cursor.getDouble(3)));
        tvCategoria.setText(String.valueOf(cursor.getInt(6)));
        tvPreco.setText(String.valueOf(cursor.getDouble(4)));
        tvPeso.setText(cursor.getString(9));

        try {
            mainToolbarTop.setTitle("Detalhes de " + cursor.getString(2));
        }catch (Exception e){

        }

        if (cursor.getBlob(11) != null){

            Log.i("IMAGEM", "NAO NULO");

            ByteArrayInputStream imageStream = new ByteArrayInputStream(cursor.getBlob(11));
            Bitmap theImage = BitmapFactory.decodeStream(imageStream);

            ivProduto.setImageBitmap(theImage);
        }else{
            ivProduto.setImageResource(R.drawable.sem_foto);
        }
/*
        Bitmap imagem = Imagem.carregarImagem(this, "produto_" + cursor.getString(0) + ".png");

        if (imagem != null){
            ivProduto.setImageBitmap(imagem);
        }else{
            ivProduto.setImageResource(R.drawable.sem_foto);
        }
*/

        //ivProduto.setImageResource(R.drawable.sem_foto);

        //########## Inicio pegando o cliente #############

        //Cliente c = cursor.get

        // c.setId(cursor.getLong(0));
        //c.setRazaoSocial(cursor.getString(1));
        //c.setFantasia(cursor.getString(2));
        //c.setApelido(cursor.getString(3));


        //ObjetoProdutoDAO objetoProdutoDAO = new ObjetoProdutoDAO();
        //mProduto = objetoProdutoDAO.buscaProduto(mIntent.getIntExtra("codProduto", 0));
        //########## Fim pegando o cliente #############
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return true;
    }
}
