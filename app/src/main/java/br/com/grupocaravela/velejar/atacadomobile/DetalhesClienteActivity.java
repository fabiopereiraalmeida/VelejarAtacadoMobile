package br.com.grupocaravela.velejar.atacadomobile;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;

public class DetalhesClienteActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;
    private Cursor cursorCidade;
    private Cursor cursorEstado;

    private Intent mIntent;

    private Toolbar mainToolbarTop;

    private Cliente cliente;

    private TextView tvId;
    private TextView tvRazaoSocial;
    private TextView tvFanatsia;
    private TextView tvTelefone1;
    private TextView tvCnpj;
    private TextView tvEmail;
    private TextView tvLimite;

    private TextView tvEndereco;
    private TextView tvNumero;
    private TextView tvBairro;
    private TextView tvCidade;
    private TextView tvUf;
    private TextView tvCep;
    private TextView tvComplemento;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_cliente);

        mIntent = getIntent();

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Detalhes de Clientes");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvId = (TextView) findViewById(R.id.tv_conta_receber_id);
        tvTelefone1 = (TextView) findViewById(R.id.tv_cliente_telefone);
        tvCnpj = (TextView) findViewById(R.id.tv_conta_receber_cnpj_cliente);
        tvEmail = (TextView) findViewById(R.id.tv_produto_estoque);
        tvFanatsia = (TextView) findViewById(R.id.tv_conta_receber_fantasia_cliente);
        tvLimite = (TextView) findViewById(R.id.tv_produto_categoria);
        tvRazaoSocial = (TextView) findViewById(R.id.tv_conta_receber_razao_social_cliente);

        tvEndereco = (TextView) findViewById(R.id.tv_endereco_cliente);
        tvNumero = (TextView) findViewById(R.id.tv_numero_cliente);
        tvCidade = (TextView) findViewById(R.id.tv_cidade_cliente);
        tvBairro = (TextView) findViewById(R.id.tv_bairro_cliente);
        tvUf = (TextView) findViewById(R.id.tv_estado_cliente);
        tvCep = (TextView) findViewById(R.id.tv_cep_cliente);
        tvComplemento = (TextView) findViewById(R.id.tv_complemento_cliente);


        //Toast.makeText(DetalhesClienteActivity.this, "Id do Cliente selecionado: " + mIntent.getLongExtra("id", 0), Toast.LENGTH_SHORT).show();
        carregarCampos();

    }

    private void carregarCampos(){
        cursor = db
                .rawQuery(
                        "SELECT _id, razaoSocial, fantasia, inscricaoEstadual, cpf, cnpj, data_nascimento, data_cadastro, email, limite_credito, " +
                                "ativo, observacao, telefone1, telefone2, endereco, endereco_numero, complemento, bairro, cidade_id, estado_id, cep, " +
                                "rota_id, empresa_id, novo, alterado FROM cliente WHERE _id LIKE '" + mIntent.getLongExtra("id", 0) + "'", null);

        cursor.moveToFirst();

        tvId.setText(String.valueOf(cursor.getInt(0)));
        tvRazaoSocial.setText(cursor.getString(1));
        tvLimite.setText(String.valueOf(cursor.getDouble(9)));
        tvFanatsia.setText(cursor.getString(2));
        tvTelefone1.setText(cursor.getString(12));
        tvCnpj.setText(cursor.getString(5));
        tvEmail.setText(cursor.getString(8));

        tvEndereco.setText(cursor.getString(14));
        tvNumero.setText(cursor.getString(15));
        tvBairro.setText(cursor.getString(17));

        cursorCidade = db
                .rawQuery(
                        "SELECT _id, estado_id, nome, ibge FROM cidade WHERE _id LIKE '" + cursor.getString(18) + "'", null);
        if (cursorCidade.moveToFirst()) {
            //cursorCidade.moveToFirst();
            tvCidade.setText(cursorCidade.getString(2));
        }else{
            tvCidade.setText("");
        }

        cursorEstado = db
                .rawQuery(
                        "SELECT _id, nome, uf, codigo FROM estado WHERE _id LIKE '" + cursor.getString(19) + "'", null);
        if (cursorEstado.moveToFirst()) {
            //cursorEstado.moveToFirst();

            tvUf.setText(cursorEstado.getString(2));
        }else{
            tvUf.setText("");
        }

        tvCep.setText(cursor.getString(20));
        tvComplemento.setText(cursor.getString(16));


        try {
            mainToolbarTop.setTitle("Detalhes de " + cursor.getString(1));
        }catch (Exception e){

        }
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
