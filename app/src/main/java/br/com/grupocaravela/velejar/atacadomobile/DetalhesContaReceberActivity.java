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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;

public class DetalhesContaReceberActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private Intent mIntent;

    private Toolbar mainToolbarTop;

    private Cliente cliente;

    private TextView tvIdContaReceber;
    private TextView tvRazaoSocial;
    private TextView tvFanatsia;
    private TextView tvCnpj;
    private TextView tvVencimento;
    private TextView tvValorDevido;

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatDataBRA = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatSoapCompleto = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSAEstenso = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalhes_conta_receber);

        mIntent = getIntent();

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Detalhes de Nota a receber");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvIdContaReceber = (TextView) findViewById(R.id.tv_conta_receber_id);
        tvRazaoSocial = (TextView) findViewById(R.id.tv_conta_receber_razao_social_cliente);
        tvCnpj = (TextView) findViewById(R.id.tv_conta_receber_cnpj_cliente);
        tvFanatsia = (TextView) findViewById(R.id.tv_conta_receber_fantasia_cliente);
        tvVencimento = (TextView) findViewById(R.id.tv_conta_receber_vencimento);
        tvValorDevido = (TextView) findViewById(R.id.tv_conta_receber_valor_devido);

        //Toast.makeText(DetalhesClienteActivity.this, "Id do Cliente selecionado: " + mIntent.getLongExtra("id", 0), Toast.LENGTH_SHORT).show();
        carregarCampos();
    }

    private void carregarCampos(){

        cursor = db
                .rawQuery(
                        "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id " +
                                "FROM conta_receber WHERE _id LIKE '" + mIntent.getLongExtra("id", 0) + "'", null);

        cursor.moveToFirst();

        Cursor c = db
                .rawQuery(
                        "SELECT _id, razaoSocial, cnpj, fantasia " +
                                "FROM cliente WHERE _id LIKE '" + cursor.getInt(3) + "'", null);

        c.moveToFirst();

        tvIdContaReceber.setText(String.valueOf(cursor.getInt(0)));
        tvRazaoSocial.setText(c.getString(1));
        tvCnpj.setText(c.getString(2));
        tvFanatsia.setText(c.getString(3));
        tvValorDevido.setText("R$ " + cursor.getString(1));

        try {
            String dt = cursor.getString(2);
            tvVencimento.setText(formatDataBRA.format(dt));
        } catch (Exception e) {
            e.printStackTrace();
        }


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

