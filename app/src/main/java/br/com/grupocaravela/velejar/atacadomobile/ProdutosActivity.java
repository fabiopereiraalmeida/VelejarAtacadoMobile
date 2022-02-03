package br.com.grupocaravela.velejar.atacadomobile;

import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.FileProvider;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.Util.FileUtils;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.ProdutosFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

import static br.com.grupocaravela.velejar.atacadomobile.Util.LogUtils.LOGE;

public class ProdutosActivity extends ActionBarActivity {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;

    private Cursor cursor;

    private int posicao = 0;
    private boolean finalLista = false;
    private Toolbar mainToolbarTop;

    private ImageLoader mImageLoader;

    private List<Produto> listAux = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_produtos);

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Lista de produtos");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        //mainToolbarTop.setSubtitle("Destaques!!!");
        //mainToolbarTop.setSubtitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //######################### INICIO CONFIGURAÇÃO DO IMAGE LOADER ####################

        DisplayImageOptions mDisplayImageOptions = new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .showImageForEmptyUri(R.drawable.sem_foto)
                .showImageOnFail(R.drawable.sem_foto)
                .showImageOnLoading(R.drawable.atualizar_imagem)
                //.displayer(new FadeInBitmapDisplayer(1000)) efeito "fade" no carregamento da imagem
                .build();

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(ProdutosActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        listarProdutos();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_produtos, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_gerar_catalogo_todos_produtos) {
            //gerarCatalogo(FileUtils.getAppPath(this) + "catalogo_produtos.pdf", getTodosProdutoList());
            new GerarCatalogoTodosProdutosTread().execute();
            return true;
        }

        if (id == R.id.action_gerar_catalogo_pesquisa) {
            //gerarCatalogo(FileUtils.getAppPath(this) + "catalogo_produtos.pdf", listAux);
            new GerarCatalogoPesquisaTread().execute();
            return true;
        }

        if (id == android.R.id.home) {
            finish();
        }

        return true;
        //return super.onOptionsItemSelected(item);
    }

    public List<Produto> getSetProdutoListNome(String nome) {
        listAux = new ArrayList<>();
       // List<Produto> listProdutos = new ArrayList<>();

        //Log.i("NOME", "NOME DIGITADO: " + nome);

        try {

            if (Configuracao.getProdutoSemEstoque()){
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "tivo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' " +
                                        "OR codigo LIKE '%" + nome + "%' OR codigo_ref LIKE '%" + nome + "%' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }else{
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE nome LIKE '%" + nome + "%' AND estoque > '0'" +
                                        "OR codigo LIKE '%" + nome + "%' AND estoque > '0' OR codigo_ref LIKE '%" + nome + "%' AND estoque > '0' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }

            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getCount(); i++) {

                    Produto p = new Produto();

                    p.setId(cursor.getLong(0));
                    p.setCodigo(cursor.getString(1));
                    p.setNome(cursor.getString(2));
                    p.setEstoque(cursor.getDouble(3));
                    p.setExpositor(cursor.getDouble(4));
                    p.setValorDesejavelVenda(cursor.getDouble(5));
                    p.setValorMinimoVenda(cursor.getDouble(6));
                    p.setCategoria(cursor.getLong(7));
                    p.setUnidade(cursor.getLong(8));
                    if (cursor.getInt(9) == 1){
                        p.setAtivo(true);
                    }else{
                        p.setAtivo(false);
                    }
                    p.setPeso(cursor.getDouble(10));
                    p.setEmpresa(cursor.getLong(11));
                    p.setImagem(cursor.getBlob(12));
                    p.setCodigo_ref(cursor.getString(13));
                    p.setMarca(cursor.getLong(13));

                    //listProdutos.add(p);
                    listAux.add(p);

                    if (!cursor.isLast()){
                        cursor.moveToNext();
                    }
                }
            }

        }catch (Exception e){

        }
        return (listAux);
    }

    public List<Produto> getSetProdutoList(int qtd) {
        listAux = new ArrayList<>();
        ArrayList<Produto> listProduto = new ArrayList<>();
        try {

            if (Configuracao.getProdutoSemEstoque()){
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }else{
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE estoque > '0' ORDER BY nome COLLATE NOCASE ASC LIMIT 50", null);
            }



        }catch (Exception e){

        }
        if (cursor.moveToFirst()) {

            int tamanhoListaProduto = cursor.getCount();

            for (int i = 0; i < tamanhoListaProduto; i++) {

                Produto p = new Produto();

                p.setId(cursor.getLong(0));
                p.setCodigo(cursor.getString(1));
                p.setNome(cursor.getString(2));
                p.setEstoque(cursor.getDouble(3));
                p.setExpositor(cursor.getDouble(4));
                p.setValorDesejavelVenda(cursor.getDouble(5));
                p.setValorMinimoVenda(cursor.getDouble(6));
                p.setCategoria(cursor.getLong(7));
                p.setUnidade(cursor.getLong(8));
                if (cursor.getInt(9) == 1){
                    p.setAtivo(true);
                }else{
                    p.setAtivo(false);
                }
                p.setPeso(cursor.getDouble(10));
                p.setEmpresa(cursor.getLong(11));
                p.setImagem(cursor.getBlob(12));
                p.setCodigo_ref(cursor.getString(13));
                p.setMarca(cursor.getLong(14));

                listProduto.add(p);

                cursor.moveToNext();
                //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
            }

            Log.i("TAMANHO", "TAMANHO DO LISTPRODUTO " + listProduto.size() + " ");

            if (finalLista == false) {

                for (int y = 0; y < qtd; y++) {
                    if (posicao == listProduto.size()) {
                        //posicao = 1;
                        finalLista = true;
                    } else {
                        listAux.add(listProduto.get(posicao));
                        posicao++;
                        //Log.i("NOME", "NOME " + listProduto.get(posicao).getNome() + " ");
                    }

                }
            }

            //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
            //Log.i("POSICAO", "POSICAO " + posicao + " ");
        }
        return (listAux);
    }

    public List<Produto> getTodosProdutoList() {
        List<Produto> listAux = new ArrayList<>();

        try {

            if (Configuracao.getProdutoSemEstoque()){
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto ORDER BY nome COLLATE NOCASE ASC", null);
            }else{
                cursor = db
                        .rawQuery(
                                "SELECT _id, codigo, nome, estoque, expositor, valor_desejavel_venda, valor_minimo_venda, categoria_id, unidade_id, " +
                                        "ativo, peso, empresa_id, imagem, codigo_ref, marca_id FROM produto WHERE estoque > '0' ORDER BY nome COLLATE NOCASE ASC", null);
            }

            if (cursor.moveToFirst()) {

                for (int i = 0; i < cursor.getCount(); i++) {

                    Produto p = new Produto();

                    p.setId(cursor.getLong(0));
                    p.setCodigo(cursor.getString(1));
                    p.setNome(cursor.getString(2));
                    p.setEstoque(cursor.getDouble(3));
                    p.setExpositor(cursor.getDouble(4));
                    p.setValorDesejavelVenda(cursor.getDouble(5));
                    p.setValorMinimoVenda(cursor.getDouble(6));
                    p.setCategoria(cursor.getLong(7));
                    p.setUnidade(cursor.getLong(8));
                    if (cursor.getInt(9) == 1){
                        p.setAtivo(true);
                    }else{
                        p.setAtivo(false);
                    }
                    p.setPeso(cursor.getDouble(10));
                    p.setEmpresa(cursor.getLong(11));
                    p.setImagem(cursor.getBlob(12));
                    p.setCodigo_ref(cursor.getString(13));
                    p.setMarca(cursor.getLong(14));

                    //listProdutos.add(p);
                    listAux.add(p);

                    if (!cursor.isLast()){
                        cursor.moveToNext();
                    }
                }
            }

        }catch (Exception e){

        }
        return (listAux);
    }

    private void listarProdutos() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                ProdutosFragment frag = (ProdutosFragment) getSupportFragmentManager().findFragmentByTag("proFrag");
                if (frag == null) {
                    frag = new ProdutosFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_produtos, frag, "proFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mImageLoader.clearMemoryCache();
        mImageLoader.stop();
    }

    public class GerarCatalogoTodosProdutosTread extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(ProdutosActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Criando catalogo do produtos...");
            dialog.setMessage("Esta funço costuma demorar. Favor aguardar....");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            //dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean retorno = false;

            try {
                gerarCatalogo(FileUtils.getAppPath(ProdutosActivity.this) + "catalogo_produtos.pdf", getTodosProdutoList());
                retorno = true;
            }catch (Exception e){

            }
            return retorno;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(ProdutosActivity.this, "Criação de catalogo de produtos comcluido com sucesso!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ProdutosActivity.this, "Falha na criação do catálogo!!!", Toast.LENGTH_LONG).show();
                Toast.makeText(ProdutosActivity.this, "Favor atualize novamente!!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
            finish();
        }

        //@Override
        protected void onProgressUpdate(String... progress) {
            Log.v("count",progress[0]);
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

    }

    public class GerarCatalogoPesquisaTread extends AsyncTask<String, Void, Boolean> {

        ProgressDialog dialog = new ProgressDialog(ProdutosActivity.this);

        @Override
        protected void onPreExecute() {
            dialog.setTitle("Criando catalogo do produtos...");
            dialog.setMessage("Esta funço costuma demorar. Favor aguardar....");
            dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            dialog.setIndeterminate(true);
            dialog.setCancelable(false);
            //dialog.setMax(100);
            dialog.show();
        }

        @Override
        protected Boolean doInBackground(String... urls) {

            boolean retorno = false;

            try {
                gerarCatalogo(FileUtils.getAppPath(ProdutosActivity.this) + "catalogo_produtos.pdf", listAux);
                retorno = true;
            }catch (Exception e){

            }
            return retorno;
        }

        @Override
        protected void onPostExecute(Boolean result) {

            if (result == true) {
                Toast.makeText(ProdutosActivity.this, "Criação de catalogo de produtos comcluido com sucesso!!!", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(ProdutosActivity.this, "Falha na criação do catálogo!!!", Toast.LENGTH_LONG).show();
                Toast.makeText(ProdutosActivity.this, "Favor atualize novamente!!!", Toast.LENGTH_LONG).show();
            }

            dialog.dismiss();
            finish();
        }

        //@Override
        protected void onProgressUpdate(String... progress) {
            Log.v("count",progress[0]);
            dialog.setProgress(Integer.parseInt(progress[0]));
        }

    }

    public void gerarCatalogo(String dest, List<Produto> listaProdutos) {

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        //Toast.makeText(this, "INICIANDO PDF... :)", Toast.LENGTH_SHORT).show();

        try {
            /**
             * Creating Document
             */
            Document document = new Document();
            Rectangle one = new Rectangle(420, 500);
            //document.setPageSize(PageSize.A4);
            document.setPageSize(one);
            document.setMargins(20, 2, 2, 2);

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            document.addCreationDate();
            document.addAuthor("Velejar Software");
            document.addCreator("Catalogo de produtos");

            /***
             * Variables for further use....
             */
            //BaseColor mColorAccent = new BaseColor(0, 153, 204, 255);
            //float mHeadingFontSize = 25.0f;
            //float mValueFontSize = 25.0f;

            /**
             * How to USE FONT....
             */

            BaseFont urName = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            //lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setLineColor(BaseColor.BLACK);

            for (int i = 0; i < listaProdutos.size(); i++) {
                //if (listaProdutos.get(i).getImagem() != null) {

                    Font mOrderDetailsEmpresaFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
                    Chunk mOrderDetailsEmpresaChunk = new Chunk(buscaInfoEmpresa(1), mOrderDetailsEmpresaFont);
                    Paragraph mOrderDetailsEmpresaParagraph = new Paragraph(mOrderDetailsEmpresaChunk);
                    mOrderDetailsEmpresaParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderDetailsEmpresaParagraph);

                    Font mOrderDetailsEmpresaCnpjFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
                    Chunk mOrderDetailsEmpresaCnpjChunk = new Chunk(buscaInfoEmpresa(2), mOrderDetailsEmpresaCnpjFont);
                    Paragraph mOrderDetailsEmpresaCnpjParagraph = new Paragraph(mOrderDetailsEmpresaCnpjChunk);
                    mOrderDetailsEmpresaCnpjParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderDetailsEmpresaCnpjParagraph);

                    // Adding Line Breakable Space....
                    document.add(new Paragraph(""));
                    // Adding Horizontal Line...
                    document.add(new Chunk(lineSeparator));
                    // Adding Line Breakable Space....
                    document.add(new Paragraph(""));// Fields of Order Details...

                    if (listaProdutos.get(i).getImagem() != null) {
                        Image image = Image.getInstance(listaProdutos.get(i).getImagem());
                        image.scaleAbsolute(300, 300);
                        image.setAlignment(Element.ALIGN_CENTER);
                        document.add(image);
                    }else{

                        Drawable d = getResources().getDrawable(R.drawable.sem_foto);
                        BitmapDrawable bitDw = ((BitmapDrawable) d);
                        Bitmap bmp = bitDw.getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
                        Image image = Image.getInstance(stream.toByteArray());

                        //Image image = Image.getInstance(R.drawable.sem_foto);
                        image.scaleAbsolute(300, 300);
                        image.setAlignment(Element.ALIGN_CENTER);
                        document.add(image);
                    }



                    Font mOrderProdutoValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
                    Chunk mOrderProdutoValueChunk = new Chunk(listaProdutos.get(i).getNome(), mOrderProdutoValueFont);
                    Paragraph mOrderProdutoValueParagraph = new Paragraph(mOrderProdutoValueChunk);
                    mOrderProdutoValueParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderProdutoValueParagraph);

                    Font mOrderProdutoValorValueFont = new Font(urName, 45.0f, Font.NORMAL, BaseColor.BLUE);
                    Chunk mOrderProdutoValorValueChunk = new Chunk("R$ " + String.format("%.2f", listaProdutos.get(i).getValorDesejavelVenda()), mOrderProdutoValorValueFont);
                    Paragraph mOrderProdutoValorValueParagraph = new Paragraph(mOrderProdutoValorValueChunk);
                    mOrderProdutoValorValueParagraph.setAlignment(Element.ALIGN_CENTER);
                    document.add(mOrderProdutoValorValueParagraph);

                    document.newPage();
                }
            //}

            document.close();

            //Toast.makeText(getActivity(), "Created... :)", Toast.LENGTH_SHORT).show();
            //FileUtils.openFile(getContext(), new File(dest));

            File file = new File(dest);
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                Uri apkURI = FileProvider.getUriForFile(this, this.getApplicationContext().getPackageName() + ".fileprovider", file);
                //intent.setDataAndType(apkURI, "image/jpg");
                intent.setDataAndType(apkURI, "application/pdf");
                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } else {
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
            }
            startActivity(intent);

        } catch (IOException | DocumentException ie) {
            Log.e("ERRO", "createPdf: Error " + ie.getLocalizedMessage());
            //Toast.makeText(getActivity(), "createPdf: Error " + ie.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            LOGE("createPdf: Error " + ie.getLocalizedMessage());
        } catch (ActivityNotFoundException ae) {
            Toast.makeText(this, "No application found to open this file.", Toast.LENGTH_SHORT).show();
        }
    }

    private String buscaInfoEmpresa(int info){
        String retorno = null;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, razaoSocial, cnpj, endereco, endereco_numero, telefone_1 FROM empresa", null);

            cursor.moveToFirst();

            //Nome
            if (info == 1){
                retorno = cursor.getString(1);
            }

            //Cnpj
            if (info == 2){
                retorno = cursor.getString(2);
            }

            //Endereco
            if (info == 3){
                retorno = cursor.getString(3) + " " + cursor.getString(4);
            }

            //Telefone
            if (info == 4){
                retorno = cursor.getString(5);
            }


        }catch (Exception e){
            retorno = "Não encontrado";
        }

        return retorno;
    }
}
