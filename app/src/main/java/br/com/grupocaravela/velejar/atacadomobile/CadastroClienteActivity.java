package br.com.grupocaravela.velejar.atacadomobile;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.extras.MaskUtil;


public class CadastroClienteActivity extends ActionBarActivity {

    static final int REQUEST_IMAGE_OPEN = 1;
    static final int RESPOSTA_IMAGEM = 2;
    private String selectedImagePath;

    private Toolbar mainToolbarTop;

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues cliente;

    private Cursor cursor;

    private ImageView ivUsuario;

    private TextWatcher cepMask;
    private TextWatcher telMask;

    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //private EditText edtNome;
    private EditText edtRazaoSocial;
    private EditText edtFantasia;
    private EditText edtCnpj;
    private EditText edtCpf;
    private EditText edtInscricaoEstadual;
    private EditText edtTelefoneFixo;
    private EditText edtTelefoneOpcional;
    //private EditText edtLimiteCredito;
    private EditText edtEmail;
    private EditText edtObservacao;

    private EditText edtEndereco;
    private EditText edtNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtUf;
    private EditText edtCep;
    private EditText edtComplento;

/*
    private EditText edtCep;
    private EditText edtTel;
    private EditText edtEndereco;
    private EditText edtEnderecoNumero;
    private EditText edtBairro;
    private EditText edtCidade;
    private EditText edtEmail;
    private EditText edtUsuario;
    private EditText edtSenha;
*/
    private Bitmap bitmap = null;
    private boolean alterolFoto = false;

    //SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_cliente);

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Novo Cliente!");
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        dbHelper = new DBHelper(this, "velejar.db.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        //cliente = new ContentValues(); // banco

        ivUsuario = (ImageView) findViewById(R.id.iv_imagem_cliente);

        //edtNome = (EditText) findViewById(R.id.edt_nome_cadastro_cliente);
        edtRazaoSocial = (EditText) findViewById(R.id.edt_razao_social_cadastro_cliente);
        edtFantasia = (EditText) findViewById(R.id.edt_fantasia_cadastro_cliente);
        edtCnpj = (EditText) findViewById(R.id.edt_cnpj_cadastro_cliente);
        edtInscricaoEstadual = (EditText) findViewById(R.id.edt_inscricao_estadual_cadastro_cliente);
        edtCpf = (EditText) findViewById(R.id.edt_cpf_cadastro_cliente);
        edtTelefoneFixo = (EditText) findViewById(R.id.edt_telefone_fixo_cadastro_cliente);
        edtTelefoneOpcional = (EditText) findViewById(R.id.edt_telefone_opcional_cadastro_cliente);
        //edtLimiteCredito = (EditText) findViewById(R.id.edt_limite_credito_cadastro_cliente);
        edtEmail = (EditText) findViewById(R.id.edt_email_cadastro_cliente);
        edtObservacao = (EditText) findViewById(R.id.edt_obs_cadastro_cliente);

        edtEndereco = (EditText) findViewById(R.id.edt_endereco_cadastro_cliente);
        edtNumero = (EditText) findViewById(R.id.edt_mumero_cadastro_cliente);
        edtBairro = (EditText) findViewById(R.id.edt_bairro_cadastro_cliente);
        edtCidade = (EditText) findViewById(R.id.edt_cidade_cadastro_cliente);
        edtUf = (EditText) findViewById(R.id.edt_uf_cadastro_cliente);
        edtCep = (EditText) findViewById(R.id.edt_cep_cadastro_cliente);
        edtComplento = (EditText) findViewById(R.id.edt_complemento_cadastro_cliente);

        edtCnpj.addTextChangedListener(MaskUtil.insert(edtCnpj, "cnpj"));
        edtCpf.addTextChangedListener(MaskUtil.insert(edtCpf, "cpf"));
        edtCep.addTextChangedListener(MaskUtil.insert(edtCep, "cep"));


        //Aplicando as mascaras
        //cepMask = Mask.insert("##.###-###", edtCep);
       // edtCep.addTextChangedListener(cepMask);

        //telMask = Mask.insert("(##)####-####", edtTelefoneFixo);
        //edtTelefoneFixo.addTextChangedListener(telMask);

        //edtTelefoneOpcional.addTextChangedListener(telMask);

        edtUf.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 2) {
                    edtUf.setText("");//Apaga o conteudo
                    Toast.makeText(CadastroClienteActivity.this, "O campo UF deve ter 2 caracteres!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    public void selecionarImagem(){

        //Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT, null);
        intent.setType("image/*");
        //intent.putExtra("crop", "true");
        startActivityForResult(intent, RESPOSTA_IMAGEM);

    }

    public void selectImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.putExtra("crop", "true");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == RESPOSTA_IMAGEM && resultCode == RESULT_OK) {
            Uri imagemSelecionada = data.getData();

            String[] colunas = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(imagemSelecionada, colunas, null, null, null);
            cursor.moveToFirst();

            int indexColuna = cursor.getColumnIndex(colunas[0]);
            String pathImg = cursor.getString(indexColuna);
            cursor.close();

            try {
                Bitmap bitmapImg = BitmapFactory.decodeFile(pathImg);
                bitmapImg = getThumbnail(imagemSelecionada);

                ivUsuario.setImageBitmap(bitmapImg);

                bitmap = bitmapImg; //Passa a imagem para o Bitmap da Classe para enviar para o servidor

                alterolFoto = true; //Diz que a foto foi alterada

            } catch (IOException e) {
                e.printStackTrace();
            }
/*
            try {
                bitmap = getThumbnail(imagemSelecionada);

                ivUsuario.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }
*/
        }

        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK) {
            Uri fullPhotoUri = data.getData();

            //Bitmap bitmap = null;

            try {
                bitmap = getThumbnail(fullPhotoUri);

                ivUsuario.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public Bitmap getThumbnail(Uri uri) throws IOException {
        InputStream input = this.getContentResolver().openInputStream(uri);

        int imageWidth = 250;
        int imageHeight = 250;

        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        //bitmapOptions.inSampleSize = calculateInSampleSize(bitmapOptions,100,100);
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.ARGB_8888;//optional
        input = this.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);


        //####################################
        final int maxSize = 340;
        int outWidth;
        int outHeight;
        int inWidth = bitmap.getWidth();
        int inHeight = bitmap.getHeight();
        if(inWidth > inHeight){
            outWidth = maxSize;
            outHeight = (inHeight * maxSize) / inWidth;
        } else {
            outHeight = maxSize;
            outWidth = (inWidth * maxSize) / inHeight;
        }

        Bitmap resizedBitmap = Bitmap.createScaledBitmap(bitmap, outWidth, outHeight, true);
        //#####################################

        if (resizedBitmap.getWidth() >= resizedBitmap.getHeight()){

            resizedBitmap = Bitmap.createBitmap(
                    resizedBitmap,
                    resizedBitmap.getWidth()/2 - resizedBitmap.getHeight()/2,
                    0,
                    resizedBitmap.getHeight(),
                    resizedBitmap.getHeight()
            );

        }else{

            resizedBitmap = Bitmap.createBitmap(
                    resizedBitmap,
                    0,
                    resizedBitmap.getHeight()/2 - resizedBitmap.getWidth()/2,
                    resizedBitmap.getWidth(),
                    resizedBitmap.getWidth()
            );
        }

        //Bitmap reduzida = Bitmap.createScaledBitmap(bitmap, imageWidth, imageHeight, false);
        //Bitmap result = Bitmap.createBitmap(resizedBitmap, 0, 0, 250, 250);

        input.close();

        return resizedBitmap;
    }

    private void enviarFotoPerfil(String loginUsuario, Bitmap bitmap)throws IOException, InterruptedException{

        int totalBytes;
        int byteTrasferred;

        HttpURLConnection httpUrlConnection = (HttpURLConnection) new URL("http://177.38.244.53:8686/caculecomprefacil/imagens/usuarios/upload.php?filename=" + loginUsuario +".png").openConnection();
        httpUrlConnection.setDoOutput(true);
        httpUrlConnection.setRequestMethod("POST");
        OutputStream os = httpUrlConnection.getOutputStream();
        Thread.sleep(1000);
//        BufferedInputStream fis = new BufferedInputStream(new FileInputStream("tmpfile.tmp"));
//        BufferedInputStream fis = new BufferedInputStream(new FileInputStream(arquivo));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        if (alterolFoto == true) {
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }else{
            bitmap = BitmapFactory.decodeResource(this.getResources(),
                    R.drawable.sem_foto_perfil);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        }
        InputStream is = new ByteArrayInputStream(stream.toByteArray());

        BufferedInputStream fis = new BufferedInputStream(is);
        totalBytes = fis.available();

        for (int i = 0; i < totalBytes; i++) {
            os.write(fis.read());
            byteTrasferred = i + 1;
        }

        os.close();
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        httpUrlConnection.getInputStream()));

        String s = null;
        while ((s = in.readLine()) != null) {
            System.out.println(s);
        }
        in.close();
        fis.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_cadastro_usuario, menu);
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
        if (id == android.R.id.home) {
            finish();
        }

        return true;
        //return super.onOptionsItemSelected(itehttp://localhost/phpmyadmin/index.php?db=compre_facil_servidor&table=empresa&target=sql.php&token=34e42139b2b104cad41fccbaa32efa09#PMAURL-0:index.php?db=compre_facil_servidor&table=empresa&server=1&target=sql.php&token=34e42139b2b104cad41fccbaa32efa09m);
    }

    private void criarCliente(){

        cliente = new ContentValues(); // banco
        //Caso não exista a tabela de usuario, será criada uma
        db = openOrCreateDatabase("velejar.db", Context.MODE_PRIVATE, null);

        StringBuilder sqlCliente = new StringBuilder();
        sqlCliente
                .append("CREATE TABLE IF NOT EXISTS [cliente](");
        sqlCliente
                .append("[_id] INTEGER PRIMARY KEY, ");
        sqlCliente
                .append("ativo BOOLEAN, ");
        sqlCliente
                .append("cnpj VARCHAR(20), ");
        sqlCliente
                .append("cpf VARCHAR(20), ");
        sqlCliente
                .append("data_cadastro VARCHAR(20), ");
        sqlCliente
                .append("email VARCHAR(70), ");
        sqlCliente
                .append("fantasia VARCHAR(30), ");
        sqlCliente
                .append("observacao TEXT, ");
        sqlCliente
                .append("razaoSocial VARCHAR(80), ");
        sqlCliente
                .append("rota_id INTEGER, ");
        sqlCliente
                .append("empresa_id INTEGER, ");
        sqlCliente
                .append("telefone1 VARCHAR(18), ");
        sqlCliente
                .append("telefone2 VARCHAR(18), ");
        sqlCliente
                .append("novo BOOLEAN, ");
        sqlCliente
                .append("bairro VARCHAR(30), ");
        sqlCliente
                .append("cep VARCHAR(12), ");
        sqlCliente
                .append("complemento VARCHAR(30), ");
        sqlCliente
                .append("endereco VARCHAR(100), ");
        sqlCliente
                .append("endereco_numero VARCHAR(5), ");
        sqlCliente
                .append("uf VARCHAR(2), ");
        sqlCliente
                .append("cidade VARCHAR(30))");

        db.execSQL(sqlCliente.toString());
        Log.i("Banco",
                "O Banco Clientes foi criado");


        //cliente.put("_id", 1);
        //cliente.put("nome", edtNome.getText().toString()); // Adicionando
        cliente.put("ativo", true);
        cliente.put("cnpj", edtCnpj.getText().toString()); // Adicionando
        cliente.put("inscricaoEstadual", edtInscricaoEstadual.getText().toString()); // Adicionando
        cliente.put("cpf", edtCpf.getText().toString()); // Adicionando

        cliente.put("data_cadastro", formatSoapHora.format(dataAtual())); // Adicionando
        cliente.put("email", edtEmail.getText().toString()); // Adicionando
        cliente.put("fantasia", edtFantasia.getText().toString()); // Adicionando
        //cliente.put("limite_credito", edtLimiteCredito.getText().toString()); // Adicionando
        cliente.put("observacao", edtObservacao.getText().toString()); // Adicionando
        cliente.put("razaoSocial", edtRazaoSocial.getText().toString()); // Adicionando

        //cliente.put("rota_id", buscaRotaUsuarioLogado().toString());

        cliente.put("telefone1", edtTelefoneFixo.getText().toString()); // Adicionando
        cliente.put("telefone2", edtTelefoneOpcional.getText().toString().replace(" ", "")); // Adicionando

        cliente.put("novo", true); // Adicionando
        cliente.put("bairro", edtBairro.getText().toString()); // Adicionando
        cliente.put("cep", edtCep.getText().toString()); // Adicionando
        cliente.put("complemento", edtComplento.getText().toString()); // Adicionando
        cliente.put("endereco", edtEndereco.getText().toString()); // Adicionando
        cliente.put("endereco_numero", edtNumero.getText().toString()); // Adicionando
        cliente.put("uf", edtUf.getText().toString()); // Adicionando
        cliente.put("cidade", edtCidade.getText().toString()); // Adicionando

        //################## INICIO PEGANDO O USUARIO LOGADO NO SISTEMA #########################
        cursor = db
                .rawQuery(
                        "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado WHERE _id LIKE '1'", null);

        cursor.moveToFirst();

        //cliente.put("usuario_id", cursor.getInt(1));
        cliente.put("empresa_id", cursor.getInt(6));

        db.insert("cliente", null, cliente); //Salvando o usuario

        Toast.makeText(this, "O CLIENTE FOI CRIADO COM SUCESSO ", Toast.LENGTH_LONG).show();
/*
        Cursor c = db
                .rawQuery(
                        "SELECT _id FROM usuario order by _id desc limit 1",
                        null); // Pegando id da ultima tabela criada

        if (c.moveToNext()) {
            _idUsuario = c.getInt(0); // Atribuindo o id a

            Toast.makeText(this, "O ID DO USUARIO É " +
                    _idUsuario, Toast.LENGTH_LONG).show();

            enviarUsuario();

            finish();
        } else {
            Toast.makeText(this, "O Usuario não foi criado!!!",
                    Toast.LENGTH_LONG).show();
        }
*/

        finish();
    }

    private Integer buscaRotaUsuarioLogado(){
        Integer retorno = 0;

        Cursor cursor;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, cargo, rota FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getInt(3);
        }catch (Exception e){
            retorno = 0;
        }

        return retorno;
    }

    private void fecharPilha(){

        Intent it = new Intent(CadastroClienteActivity.this, MenuPrincipalActivity.class);
        startActivity(it);
        finish();
    }

    public void clickSalvarCadastro(View v) {    //Clique do botão salvar

        //String verificaInscricaoEstadual = edt.getText().toString();
        String verificaFantasia = edtFantasia.getText().toString();
        String verificaRazaoSocial = edtRazaoSocial.getText().toString();
        String verificaEndereco = edtEndereco.getText().toString();
        String verificaBairro = edtBairro.getText().toString();
        String verificaCidade = edtCidade.getText().toString();
        String verificaUf = edtUf.getText().toString();


        if (verificaFantasia == null || verificaFantasia.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR UMA FANATASIA PARA O CLIENTE", Toast.LENGTH_LONG).show();
        } else if (verificaRazaoSocial == null || verificaRazaoSocial.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR UMA RAZÃO SOCIAL PARA O CLIENTE", Toast.LENGTH_LONG).show();
        } else if (verificaEndereco == null || verificaEndereco.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR UM ENDEREÇO PARA O CLIENTE", Toast.LENGTH_LONG).show();
        } else if (verificaBairro == null || verificaBairro.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR UM BAIRRO NO CAMPO BAIRRO", Toast.LENGTH_LONG).show();
        } else if (verificaCidade == null || verificaCidade.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR UMA CIDADE NO CAMPO CIDADE", Toast.LENGTH_LONG).show();
        } else if (verificaUf == null || verificaUf.equals("")) {
            Toast.makeText(this, "FAVOR INSERIR A UF - ESTADO DO CLIENTE", Toast.LENGTH_LONG).show();
        } else {
            //try {

                criarCliente();

            //} catch (Exception e) {
             //   Toast.makeText(this, "ERRO!!! " + e, Toast.LENGTH_LONG).show();
           // }

        }
    }

    public void clickCancelarCadastro(View v) throws IOException, InterruptedException {  //Clique do botão cancelar
        //enviarFotoPerfil(001, bitmap);
    }

    public void clickFotoPerfil(View v) { //Clique da foto
        Toast.makeText(this, "Escolha a foto", Toast.LENGTH_LONG).show();
        //selectImage();
        selecionarImagem();
    }
/*
    private void enviarUsuario() throws IOException, InterruptedException {

        ObjetoUsuario usuario = new ObjetoUsuario();

        usuario.setNomeUsuario(edtNome.getText().toString());

        usuario.setTelefoneUsuario(edtTel.getText().toString()); // Adicionando
        usuario.setEnderecoUsuario(edtEndereco.getText().toString()); // Adicionando
        usuario.setEnderecoNumeroUsuario(edtEnderecoNumero.getText().toString()); // Adicionando
        usuario.setBairroUsuario(edtBairro.getText().toString()); // Adicionando
        usuario.setCidadeUsuario(edtCidade.getText().toString()); // Adicionando
        usuario.setCepUsuario(edtCep.getText().toString()); // Adicionando
        usuario.setEmailUsuario(edtEmail.getText().toString()); // Adicionando
        usuario.setUsuarioUsuario(edtUsuario.getText().toString()); // Adicionando
        usuario.setSenhaUsuario(edtSenha.getText().toString()); // Adicionando

        ObjetoUsuarioDAO objetoUsuarioDAO = new ObjetoUsuarioDAO();
        objetoUsuarioDAO.inserirUsuario(usuario);

        enviarFotoPerfil(edtUsuario.getText().toString(), bitmap);
    }

    private void buscarUsuarioLogado(String usuario, String senha) {

    }

    private Boolean verificaIgualdadeLogin(String login) {

        boolean retorno = false;

        ObjetoUsuarioDAO objetoUsuarioDAO = new ObjetoUsuarioDAO();
        ArrayList<ObjetoUsuario> usuarioArrayList = objetoUsuarioDAO.listarUsuarios();

        for (int i = 0; i < usuarioArrayList.size(); i++) {

            ObjetoUsuario usuarioVerifica = usuarioArrayList.get(i);

            if (usuarioVerifica.getUsuarioUsuario().equals(login)) {
                retorno = true;
            }
        }

        return retorno;

    }

    private ObjetoUsuario buscaUsuario(){

        ObjetoUsuario obUsuario = null;

        String usuario = edtUsuario.getText().toString();
        String senha = edtSenha.getText().toString();

        ObjetoUsuarioDAO objetoUsuarioDAO = new ObjetoUsuarioDAO();
        ArrayList<ObjetoUsuario> listaUsuarios = objetoUsuarioDAO.listarUsuarios();
        Log.d("CaculeCompreFacil", listaUsuarios + "");

        for (int i = 0; i < listaUsuarios.size(); i++) {
            ObjetoUsuario obju = listaUsuarios.get(i);

            if (obju.getUsuarioUsuario().equals(usuario) && obju.getSenhaUsuario().equals(senha)){
                obUsuario = obju;
            }
        }

        return obUsuario;
    }
    */

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }

    }
