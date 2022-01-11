package br.com.grupocaravela.velejar.atacadomobile;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.dao.UsuarioDAO;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Usuario;


public class ConfiguracaoActivity extends Activity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private Cursor cursor;

    private ContentValues contentValues;

    private EditText edtemail;
    private EditText edtsenha;
    private EditText edtcnpj;

    private Button btSalvar;
    private Button btCancelar;

    private Toolbar mainToolbarTop;

    private String email;
    private String senha;
    private String cnpj;

    private AlertDialog alerta;

    private Usuario usuario = null;
    //private Toolbar mainToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracao);

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Configurações");
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        mainToolbarTop.setTitleTextColor(Color.WHITE);

        edtemail = (EditText) findViewById(R.id.edt_configuracao_email);
        edtsenha = (EditText) findViewById(R.id.edt_configuracao_senha);
        edtcnpj = (EditText) findViewById(R.id.edt_configuracao_cnpj);

        btSalvar = (Button) findViewById(R.id.bt_salvar_configuracao);
        btCancelar = (Button) findViewById(R.id.bt_cancelar_configuraçao);

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        carregarConfiguração();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_configuracao, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/
        if (id == android.R.id.home){
            finish();
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }

    public void clickSalvarConfiguracao(View v) {

        if (verificaConexao()) {
/*
            try {
                this.getApplicationContext().deleteDatabase("velejar.db");
                Toast.makeText(ConfiguracaoActivity.this, "DROP DO BANCO CONCLUIDO!", Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                Toast.makeText(ConfiguracaoActivity.this, "ERRO NO DROP DO BANCO!", Toast.LENGTH_SHORT).show();
            }
*/
            email = edtemail.getText().toString().replaceAll(" ", "").replaceAll("\\r\\n|\\n", "");;
            senha = edtsenha.getText().toString().replaceAll(" ", "").replaceAll("\\r\\n|\\n", "");;
            cnpj = edtcnpj.getText().toString().replace(".", "").replace("-", "").replace("/", "").replace(" ", "").replaceAll("\\r\\n|\\n", "");;

            Configuracao.setEmail(email);
            Configuracao.setSenha(senha);
            Configuracao.setCnpj(cnpj);

            db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

            try {
                db.execSQL("DROP TABLE IF EXISTS configuracao");
                //db.delete("configuracao", null, null);
            } catch (Exception e) {

            }

            String tabelaConfiguracao = "CREATE TABLE IF NOT EXISTS [configuracao](_id INTEGER PRIMARY KEY, email VARCHAR(70), senha VARCHAR(16), cnpj VARCHAR(16))";

            try {
                db.execSQL(tabelaConfiguracao);

                db.execSQL("INSERT INTO configuracao(_id, email, senha, cnpj) VALUES('"
                        + 1 + "','" + email + "','" + senha + "','" + cnpj + "')");
                Log.i("Banco", "A tabela de configuração foi criada!!!");


                db.close();
                //carregarConfiguração();
                new Atualize().execute();
                //finish();// Finaliza a Activity

            } catch (Exception e) {
                Toast.makeText(ConfiguracaoActivity.this, "ERRO! " + e.toString(), Toast.LENGTH_SHORT).show();
                Log.i("ERRO", e.toString());
            }

            //db.close();
        }else{
            Toast.makeText(ConfiguracaoActivity.this, "Sem Conexão para a atualização!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clickCancelarConfiguracao(View v) {
        finish();// Finaliza a Activity
    }


    private void carregarConfiguração(){

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, email, senha, cnpj FROM configuracao", null);

            if (cursor.moveToFirst()){
                //ConfiguracaoServidor.setemail(cursor.getString(1));
                //ConfiguracaoServidor.setPortaTomcatServidor(cursor.getString(2));
                edtemail.setText(cursor.getString(1));
                edtsenha.setText(cursor.getString(2));
                edtcnpj.setText(cursor.getString(3));
            }else{
                primeiroAcesso();
            }
        }catch (Exception e){
            primeiroAcesso();
        }
    }

    private void primeiroAcesso(){
        //Cria o gerador do AlertDialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        //define o titulo
        builder.setTitle("Aviso");
        //define a mensagem
        builder.setMessage("Antes de proceguir, configure os dados do usuario!");
        //define um botão como positivo
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        //cria o AlertDialog
        alerta = builder.create();
        //Exibe
        alerta.show();

    }

    public class Atualize extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(ConfiguracaoActivity.this);

        @Override
        protected void onPreExecute() {

            dialog.setTitle("Atualizando usuarios...");
            dialog.setMessage("Favor aguardar....");
            dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            dialog.setIndeterminate(false);
            dialog.setCancelable(false);
            dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            try {

                db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

                db.execSQL("DROP TABLE IF EXISTS usuario");
                Log.i("Banco", "As tabelas de usuarios foram apagadas com sucesso!");
                //dbHelper.deleteBanco(db); //Apagando todas as tabelas do banco
                //Log.i("Banco", "As tabelas do banco foram excluidas com sucesso!");
                //dbHelper.onCreate(db); //Cria todas as tabelas
                //Log.i("Banco", "As tabelas do banco foram criadas com sucesso!");

                db.execSQL("CREATE TABLE IF NOT EXISTS [usuario](_id INTEGER PRIMARY KEY, ativo BOOLEAN, " +
                        "nome VARCHAR(45), senha VARCHAR(16), email VARCHAR(70), credito DOUBLE(11,2), rota_id INTEGER, empresa_id INTEGER)");

                atualizarUsuario(dialog);

                //db.close();

            } catch (Exception e){
                //Toast.makeText(LoginActivity.this, "ERRO! " + e.toString(), Toast.LENGTH_SHORT).show();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true){
                Toast.makeText(ConfiguracaoActivity.this, "Processamento comcluido com sucesso!!!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(ConfiguracaoActivity.this, LoginActivity.class));

            }else{
                Toast.makeText(ConfiguracaoActivity.this, "Falha no processamento!!!", Toast.LENGTH_SHORT).show();
                android.os.Process.killProcess(android.os.Process.myPid());
            }

            dialog.dismiss();
            finish();// Finaliza a Activity
        }
    }

    private void atualizarUsuario(ProgressDialog dialog){

        UsuarioDAO usuarioDAO = new UsuarioDAO();
        ArrayList<Usuario> listaUsuarios = usuarioDAO.listarUsuario(Configuracao.getCnpj());

        Log.i("LISTA", "TAMANHO DA LISTA " + listaUsuarios.size());

        dialog.setMax(listaUsuarios.size());
        dialog.setProgress(0);

        for (int i = 0; i < listaUsuarios.size(); i++) {

            try {
                contentValues.clear();

                usuario = listaUsuarios.get(i);

                contentValues.put("_id", usuario.getId());
                contentValues.put("ativo", String.valueOf(usuario.getAtivo())); // Adicionando
                contentValues.put("nome", usuario.getNome()); // Adicionando
                contentValues.put("senha", usuario.getSenha()); // Adicionando
                contentValues.put("email", usuario.getEmail()); // Adicionando
                contentValues.put("credito", usuario.getCredito()); // Adicionando
                contentValues.put("rota_id", usuario.getRota()); // Adicionando
                contentValues.put("empresa_id", usuario.getEmpresa()); // Adicionando

                db.insert("usuario", null, contentValues); //Salvando o cargo

                Log.i("Banco", "O usuario " + usuario.getNome() + " da empresa " + usuario.getEmpresa().toString() + " foi criado com sucesso!");

            }catch (Exception e){

            }

            dialog.setProgress(i);
        }
    }

    public  boolean verificaConexao() {
        boolean conectado;
        ConnectivityManager conectivtyManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conectivtyManager.getActiveNetworkInfo() != null
                && conectivtyManager.getActiveNetworkInfo().isAvailable()
                && conectivtyManager.getActiveNetworkInfo().isConnected()) {
            conectado = true;
        } else {
            conectado = false;
        }
        return conectado;
    }

}
