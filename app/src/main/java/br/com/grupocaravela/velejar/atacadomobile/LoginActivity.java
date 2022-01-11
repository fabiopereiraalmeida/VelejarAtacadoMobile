package br.com.grupocaravela.velejar.atacadomobile;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
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

public class LoginActivity extends Activity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;

    private Cursor cursor;

    private ContentValues contentValues;

    //private EditText edtEmail;
    private EditText edtSenha;

    private Button btEntrar;
    private Button btCancelar;

    private Usuario usuario = null;

    private int idUsuario;
    private String nomeUsuario;
    private String emailUsuario;
    private String senhaUsuario;
    private int idEmpresa;
    private int idRota;
    private Double credito;

    //private String ipServidor;
    //private String portaServidor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //edtEmail = (EditText) findViewById(R.id.edt_email);
        edtSenha = (EditText) findViewById(R.id.edt_senha_login);

        btEntrar = (Button) findViewById(R.id.bt_entrar);

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        carregarConfiguração();
/*
        if (verificaConexao()){
            try {
                new Atualize().execute();
            }catch (Exception e){

            }
        }else{
            Toast.makeText(LoginActivity.this, "Sem Conexão!", Toast.LENGTH_SHORT).show();
        }
*/
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

    public void clickEntrarLogin(View v) {

        String entrada = edtSenha.getText().toString().replaceAll(" ", "");
        entrada.toLowerCase();

        if (entrada.equals("apagar")){
            try {
                this.getApplicationContext().deleteDatabase("velejar.db");
                Toast.makeText(LoginActivity.this, "BANCO DE DADOS FOI EXCLUIDO!", Toast.LENGTH_SHORT).show();

                db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

                finish();
                startActivity(new Intent(this,
                        ConfiguracaoActivity.class));

            } catch (Exception e) {
                Toast.makeText(LoginActivity.this, "ERRO AO EXCLUIR BANCO DE DADOS!", Toast.LENGTH_SHORT).show();
            }
        }else {

            try {

                if (verificarLogin() == true) {

                    criarUsuarioLogado(idUsuario, nomeUsuario, emailUsuario, senhaUsuario, idEmpresa, idRota, credito);

                    carregarConfiguração();

                    startActivity(new Intent(this,
                            MenuPrincipalActivity.class));
                    finish();// Finaliza a Activity
                } else {
                    Toast.makeText(LoginActivity.this, "Usuario / Senha incorreto(s)!", Toast.LENGTH_SHORT).show();

                if (verificaConexao()){

                    try {
                        new Atualize().execute();
                    }catch (Exception e){

                    }

                }else{
                    Toast.makeText(LoginActivity.this, "Sem Conexão para a atulização!", Toast.LENGTH_SHORT).show();
                }

                    edtSenha.setText("");
                    edtSenha.requestFocus();
                }
            } catch (Exception e) {

            }
        }
    }

    public void clickConf(View v){
        configurar();
    }

    private Boolean verificarLogin(){

        Boolean retorno = false;

        String emailEntrada = "";
        String senhaEntrada = edtSenha.getText().toString().replaceAll(" ", "");

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, email, senha FROM configuracao", null);

            if (cursor.moveToFirst()){
                emailEntrada = cursor.getString(1);
            }else{
                Toast.makeText(LoginActivity.this, "E-mail não encontrado nas configurações, favor informar antes!", Toast.LENGTH_LONG).show();
            }
        }catch (Exception e){
            Toast.makeText(LoginActivity.this, "Erro ao buscar e-mail!", Toast.LENGTH_LONG).show();
        }

        cursor = db
                .rawQuery(
                        "SELECT _id, ativo, nome, senha, email, empresa_id, rota_id, credito FROM usuario", null);

        cursor.moveToFirst();

        for (int i = 0; i < cursor.getCount(); i++) {
            if (emailEntrada.equals(cursor.getString(4)) && senhaEntrada.equals(cursor.getString(3))) {

                Log.i("LOGIN", "PASSEI!!!!");
                idUsuario = cursor.getInt(0);
                nomeUsuario = cursor.getString(2);
                emailUsuario = cursor.getString(4);
                senhaUsuario = cursor.getString(3);
                idEmpresa = cursor.getInt(5);
                idRota = cursor.getInt(6);
                credito = cursor.getDouble(7);
                retorno = true;
                //break;
            } else {
                cursor.moveToNext();
            }
        }

        return retorno;
    }

        private void criarUsuarioLogado(int idUsuario, String nome, String email, String senha, int empresaId, int rotaId, Double cred){

            db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);
            try {
                //db.delete("usuario_logado", null, null);
                db.execSQL("DROP TABLE IF EXISTS usuario_logado");

                Log.i("Banco", "Tabela usuario_logado excluido!!!");
            } catch (Exception e) {
                // TODO: handle exception
            }

            String tabelaUsuarioLogado = "CREATE TABLE IF NOT EXISTS [usuario_logado](_id INTEGER PRIMARY KEY, id_usuario INTEGER, nome VARCHAR(45), email VARCHAR(70), " +
                    "senha VARCHAR(16), credito DOUBLE(11,2), empresa_id INTEGER, rota_id INTEGER)";
            db.execSQL(tabelaUsuarioLogado);

            db.execSQL("INSERT INTO usuario_logado(_id, id_usuario, nome, email, senha, credito, empresa_id, rota_id) VALUES('"
                    + 1 + "','" + idUsuario + "','" + nome + "','" + email + "','" + senha + "','" + cred + "','" + empresaId + "','" + rotaId + "')");
            Log.i("Banco", "Tabela usuario_logado foi criada!!!");
            Log.i("USUARIO LOGADO", "ROTA: " + rotaId);
            db.close();
        }

    private void configurar(){

        finish();
        startActivity(new Intent(this,
                ConfiguracaoActivity.class));

    }

    public class Atualize extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(LoginActivity.this);

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
                Toast.makeText(LoginActivity.this, "Atualização concluida com sucesso, tente novamente!!!", Toast.LENGTH_SHORT).show();

                finish();
                startActivity(new Intent(LoginActivity.this, LoginActivity.class));

            }else{
                Toast.makeText(LoginActivity.this, "Falha no processamento!!!", Toast.LENGTH_SHORT).show();
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

    private void carregarConfiguração(){

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, email, senha, cnpj FROM configuracao", null);

            if (cursor.moveToFirst()){
                Configuracao.setEmail(cursor.getString(1));
                Configuracao.setSenha(cursor.getString(2));
                Configuracao.setCnpj(cursor.getString(3));
            } else { //Se entrou aki é porque é o primeiro acesso
                configurar();
            }
        }catch (Exception e){
            configurar();
        }
    }
}






