package br.com.grupocaravela.velejar.atacadomobile;

import android.Manifest;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
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
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import br.com.grupocaravela.comprefacil.velejaratacado.R;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.fragments.ContaReceberFragment;
import br.com.grupocaravela.velejar.atacadomobile.objeto.CategoriaCliente;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;

import static br.com.grupocaravela.velejar.atacadomobile.Util.LogUtils.LOGE;

public class ContasReceberActivity extends ActionBarActivity implements DatePickerDialog.OnDateSetListener, DialogInterface.OnCancelListener {

    private DBHelper dbHelper;
    private SQLiteDatabase db;
    private ContentValues contentValues;
    private ContaReceberFragment frag;

    private Cursor cursor;

    private int posicao = 0;
    private boolean finalLista = false;
    private Toolbar mainToolbarTop;

    private ImageLoader mImageLoader;

    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    //private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28

    private Intent mIntent;
    private boolean clienteUnico = false;
    private boolean porData = false;

    private int ano, mes, dia;
    private Calendar cDefault = Calendar.getInstance();

    private List<ContaReceber> listaPesquisa = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contas_receber);

        ActivityCompat.requestPermissions(ContasReceberActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, PackageManager.PERMISSION_GRANTED);

        mIntent = getIntent();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        posicao = 0;
        finalLista = false;

        //Configuração inicial
        dbHelper = new DBHelper(this, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Contas a receber");
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

        ImageLoaderConfiguration conf = new ImageLoaderConfiguration.Builder(ContasReceberActivity.this)
                .defaultDisplayImageOptions(mDisplayImageOptions)
                .memoryCacheSize(50 * 1024 * 1024)
                .diskCacheSize(100 * 1024 * 1024)
                .threadPoolSize(5)
                .writeDebugLogs() //Mostra o debug
                .build();
        mImageLoader = ImageLoader.getInstance();
        mImageLoader.init(conf);

        //######################## FIM CONFIGURAÇÃO DO IMAGE LOADER ##############################

        if (mIntent.getLongExtra("id", 0) != 0){
            clienteUnico = true;
        }

        listarContaReceber();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contas_receber, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        initDateTimeData();
        this.cDefault.set(ano, mes, dia);

        Intent it = null;

        switch (item.getItemId()) {

            case R.id.action_informar_vencimento:

                DatePickerDialog datePickerDialog = new DatePickerDialog().newInstance(
                        this,
                        cDefault.get(Calendar.YEAR),
                        cDefault.get(Calendar.MONTH),
                        cDefault.get(Calendar.DAY_OF_MONTH)
                );

                //Calendar cMin = Calendar.getInstance();
                //Calendar cMax = Calendar.getInstance();
                //cMax.set(ano, 11, 31);
                //datePickerDialog.setMinDate(cMin);
                //datePickerDialog.setMaxDate(cMax);


                List<Calendar> dayList = new LinkedList<>();
                Calendar[] daysArray;
                Calendar cAux = Calendar.getInstance();
/*
                while (cAux.getTimeInMillis() <= cMax.getTimeInMillis()) { //Verifica data limite
                    if (cAux.get(Calendar.DAY_OF_WEEK) != 1 && cAux.get(Calendar.DAY_OF_WEEK) != 7) {
                        Calendar c = Calendar.getInstance();
                        c.setTimeInMillis(cAux.getTimeInMillis());

                        dayList.add(c);
                    }
                    cAux.setTimeInMillis(cAux.getTimeInMillis() + (24 * 60 * 60 * 1000));
                }

                daysArray = new Calendar[dayList.size()];

                for (int i = 0; i < daysArray.length; i++) {
                    daysArray[i] = dayList.get(i);
                }

                datePickerDialog.setSelectableDays(daysArray);
                datePickerDialog.setSelectableDays(daysArray);f

 */
                datePickerDialog.setOnCancelListener(this);
                datePickerDialog.show(getFragmentManager(), "Informe a data");

                break;

            case R.id.action_imprimir_lista:
                createPdf(Environment.getExternalStorageDirectory().getPath() + "/lista_contas_receber_.pdf" , listaPesquisa, buscaNomeUsuario());
                break;

            case android.R.id.home:
                finish();
                break;
        }
        //return true;
        return super.onOptionsItemSelected(item);
    }
/*
    @Override
    protected void onResume() {
        super.onResume();
    }
*/
    private void initDateTimeData(){
        if (ano == 0){
            Calendar c = Calendar.getInstance();
            ano = c.get(Calendar.YEAR);
            mes = c.get(Calendar.MONTH);
            dia = c.get(Calendar.DAY_OF_MONTH);
        }
    }

    public List<ContaReceber> getSetContaReceberListNome(String nome, CategoriaCliente categoriaCliente) {   //BUSCAR POR NOME
        List<ContaReceber> listAux = new ArrayList<>();
        //List<ContaReceber> listContaReceber = new ArrayList<>();

            try {

                if (categoriaCliente != null){
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial FROM cliente WHERE categoria_cliente_id LIKE '" + categoriaCliente.getId() + "' AND razaoSocial LIKE '%" + nome + "%' OR fantasia LIKE '%" + nome + "%' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 200", null);



                }else{
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, razaoSocial FROM cliente WHERE razaoSocial LIKE '%" + nome + "%' OR fantasia LIKE '%" + nome + "%' ORDER BY razaoSocial COLLATE NOCASE ASC LIMIT 200", null);


                }


                if (cursor.moveToFirst()) {

                    for (int i = 0; i < cursor.getCount(); i++) {

                        Cursor c;

                        if (porData) {
                            Log.i("VENCIMENTO", "INICIO DA DATA 003");

                            c = db
                                    .rawQuery(
                                            "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto " +
                                                    "FROM conta_receber where cliente_id LIKE '" + cursor.getInt(0) + "' AND vencimento LIKE '" + modificarString(2, String.valueOf(dia)) + "/" + modificarString(2, String.valueOf(mes)) + "/" + ano + "'", null);

                        } else {
                            Log.i("VENCIMENTO", "NAO INICIO DA DATA 003");
                            c = db
                                    .rawQuery(
                                            "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto " +
                                                    "FROM conta_receber where cliente_id like '" + cursor.getInt(0) + "'", null);
                        }


                        if (c.moveToFirst()) {

                            Log.i("TAMANHO", "TAMANHO DO LIST DE NOTAS " + c.getCount() + " ");

                            for (int y = 0; y < c.getCount(); y++) {

                                ContaReceber contaR = new ContaReceber();

                                contaR.setId(c.getLong(0));
                                contaR.setValorDevido(c.getDouble(1));
                                try {
                                    String d = c.getString(2);
                                    //avc.setDataVenda(d);
                                    contaR.setVencimento(d);

                                } catch (Exception e) {
                                }
                                //c.setVencimento(c.getString(2));
                                contaR.setCliente(c.getLong(3));
                                contaR.setVendaCabecalho(c.getLong(4));
                                contaR.setValorDesconto(c.getDouble(5));

                                //Log.i("VALOR DESCONTO", "DESCONTO_2 " + c.getDouble(5));

                                listAux.add(contaR);
                                //listContaReceber.add(contaR);

                                c.moveToNext();
                            }
                        }

                        cursor.moveToNext();

                    }

                }

            } catch (Exception e) {

            }

        listaPesquisa = listAux;
        return (listAux);
    }

    //public List<ContaReceber> getSetContaReceberList(int qtd) {
    public List<ContaReceber> getSetContaReceberList(CategoriaCliente categoriaCliente) {
        List<ContaReceber> listAux = new ArrayList<>();
        ArrayList<ContaReceber> listContaReceber = new ArrayList<>();


            if (clienteUnico) {
                if (porData) {
                    //Log.i("VENCIMENTO", "INICIO DA DATA 001");
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto FROM conta_receber where cliente_id like '" + mIntent.getLongExtra("id", 0) + "' AND vencimento like '" + modificarString(2, String.valueOf(dia)) + "/" + modificarString(2, String.valueOf(mes)) + "/" + ano + "' LIMIT 200", null);
                } else {
                    //Log.i("VENCIMENTO", "NAO INICIO DA DATA 001");
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto FROM conta_receber where cliente_id like '" + mIntent.getLongExtra("id", 0) + "' LIMIT 200", null);
                }
            } else {
                if (porData) {
                    //Log.i("VENCIMENTO", "INICIO DA DATA 002");
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto FROM conta_receber where vencimento like '" + modificarString(2, String.valueOf(dia)) + "/" + modificarString(2, String.valueOf(mes)) + "/" + ano + "' LIMIT 200", null);
                } else {
                    //Log.i("VENCIMENTO", "NAO INICIO DA DATA 002");
                    cursor = db
                            .rawQuery(
                                    "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id, valor_desconto FROM conta_receber LIMIT 50", null);
                }
            }



        if (cursor.moveToFirst()) {

            int tamanhoListaContaReceber = cursor.getCount();

            for (int i = 0; i < tamanhoListaContaReceber; i++) {

                ContaReceber c = new ContaReceber();

                c.setId(cursor.getLong(0));
                c.setValorDevido(cursor.getDouble(1));
                try {
                    String d = cursor.getString(2);
                    //avc.setDataVenda(d);
                    c.setVencimento(d);

                } catch (Exception e) {
                }
                //c.setVencimento(cursor.getString(2));
                c.setCliente(cursor.getLong(3));
                c.setVendaCabecalho(cursor.getLong(4));
                c.setValorDesconto(cursor.getDouble(5));

                cursor.moveToNext();
            }
        }

        listaPesquisa = listContaReceber;
        return listContaReceber;
    }

    public List<CategoriaCliente> getCategoriaClienteList() {
        ArrayList<CategoriaCliente> listCliente = new ArrayList<>();

        cursor = db
                .rawQuery(
                        "SELECT _id, nome, empresa_id FROM categoria_cliente ORDER BY nome COLLATE NOCASE ASC", null); //WHERE rota_id = '"+ buscaRotaUsuarioLogado() +"' OR rota_id = '0'

        if (cursor.moveToFirst()) {

            int tamanhoListaCategoria = cursor.getCount();

            CategoriaCliente catVazia = new CategoriaCliente(null, "Selecione a categoria", null);
            listCliente.add(catVazia);

            for (int i = 0; i < tamanhoListaCategoria; i++) {

                CategoriaCliente c = new CategoriaCliente();

                c.setId(cursor.getLong(0));
                c.setNome(cursor.getString(1));
                c.setEmpresa(cursor.getLong(2));

                listCliente.add(c);

                cursor.moveToNext();
            }

            Log.i("TAMANHO", "TAMANHO DO LISTCATEGORIA " + listCliente.size() + " ");

            //Log.i("TAMANHO", "TAMANHO DO LISTAUX " + listAux.size() + " ");
            //Log.i("POSICAO", "POSICAO " + posicao + " ");
        }
        return (listCliente);
    }

    private void listarContaReceber() {
        Thread t = new Thread(new Runnable() {

            @Override
            public void run() {

                // FRAGMENT
                frag = (ContaReceberFragment) getSupportFragmentManager().findFragmentByTag("cliFrag");
                if (frag == null) {
                    frag = new ContaReceberFragment();
                    FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.rl_fragment_container_contasReceber, frag, "cliFrag");
                    ft.commit();
                }
            }
        });
        t.start();
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        ano = mes = dia = 0;
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        //Calendar tDefault = Calendar.getInstance();
        //tDefault.set(ano, mes, dia);
        porData = true;
        ano = year;
        mes = monthOfYear + 1;
        dia = dayOfMonth;

        Log.i("VENCIMENTO", "DATA SELECIONDA " + modificarString(2, String.valueOf(dia)) + "/" + modificarString(2, String.valueOf(mes)) + "/" + ano);

        //listarContaReceber();
        frag.atualizarLista();
    }

    private static String modificarString(int casas, String texto){
        while (texto.length() < casas){
            texto = "0" + texto;
        }
        return texto;
    }

    public void createPdf(String dest, List<ContaReceber> contaReceberList, String usuario) {

        Double valorSubTotal = 0.0;
        Double desconto = 0.0;
        Double saldo = 0.0;

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

        if (new File(dest).exists()) {
            new File(dest).delete();
        }

        //Toast.makeText(getActivity(), "INICIANDO PDF... :)", Toast.LENGTH_SHORT).show();

        try {
            /**
             * Creating Document
             */
            Document document = new Document();
            Rectangle one = new Rectangle(420, 1000);
            //document.setPageSize(PageSize.A4);
            document.setPageSize(one);
            document.setMargins(20, 2, 2, 2);

            // Location to save
            PdfWriter.getInstance(document, new FileOutputStream(dest));

            // Open to write
            document.open();

            // Document Settings
            //Rectangle one = new Rectangle(210, 500);
            //document.setPageSize(PageSize.A4);
            //document.setPageSize(one);
            //document.setMargins(2, 2, 2, 2);
            document.addCreationDate();
            document.addAuthor("Velejar Software");
            document.addCreator("Contas a receber");

            /**
             * How to USE FONT....
             */
            BaseFont urName = BaseFont.createFont(BaseFont.HELVETICA_BOLD, "UTF-8", BaseFont.EMBEDDED);

            // LINE SEPARATOR
            LineSeparator lineSeparator = new LineSeparator();
            //lineSeparator.setLineColor(new BaseColor(0, 0, 0, 68));
            lineSeparator.setLineColor(BaseColor.BLACK);

            // Title Order Details...
            // Adding Title....
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

            Font mOrderDetailsEmpresaEnderecoFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaEnderecoChunk = new Chunk(buscaInfoEmpresa(3), mOrderDetailsEmpresaEnderecoFont);
            Paragraph mOrderDetailsEmpresaEnderecoParagraph = new Paragraph(mOrderDetailsEmpresaEnderecoChunk);
            mOrderDetailsEmpresaEnderecoParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaEnderecoParagraph);

            Font mOrderDetailsEmpresaTelefoneFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsEmpresaTelefoneChunk = new Chunk(buscaInfoEmpresa(4), mOrderDetailsEmpresaTelefoneFont);
            Paragraph mOrderDetailsEmpresaTelefoneParagraph = new Paragraph(mOrderDetailsEmpresaTelefoneChunk);
            mOrderDetailsEmpresaTelefoneParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsEmpresaTelefoneParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font mOrderDatePagamentoFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderDatePagamentoChunk = new Chunk("DATA IMPRESSAO: " + formatDataHoraBRA.format(dataAtual()), mOrderDatePagamentoFont);
            Paragraph mOrderDatePagamentoParagraph = new Paragraph(mOrderDatePagamentoChunk);
            document.add(mOrderDatePagamentoParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font mOrderDetailsTitleFont = new Font(urName, 22.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderDetailsTitleChunk = new Chunk("CONTAS A PAGAR", mOrderDetailsTitleFont);
            Paragraph mOrderDetailsTitleParagraph = new Paragraph(mOrderDetailsTitleChunk);
            mOrderDetailsTitleParagraph.setAlignment(Element.ALIGN_CENTER);
            document.add(mOrderDetailsTitleParagraph);

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));
            document.add(new Paragraph(""));

            Font mOrderClienteValueFont = new Font(urName, 14.0f, Font.NORMAL, BaseColor.BLACK);
            for (int i = 0; i < contaReceberList.size(); i++) {

                cursor = db
                        .rawQuery(
                                "SELECT _id, razaoSocial FROM cliente where _id like '" + contaReceberList.get(i).getCliente().toString() + "'", null);

                if (cursor.moveToFirst()) {
                    Chunk mOrderClienteValueChunk = new Chunk( "Cliente: " + cursor.getString(1), mOrderClienteValueFont);
                    Paragraph mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                    document.add(mOrderClienteValueParagraph);
                }

                Chunk mOrderClienteValueChunk = new Chunk( "Valor: R$ " + String.format("%.2f", contaReceberList.get(i).getValorDevido()) + " - Desconto: R$ " + String.format("%.2f", contaReceberList.get(i).getValorDesconto()), mOrderClienteValueFont);
                Paragraph mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                document.add(mOrderClienteValueParagraph);

                mOrderClienteValueChunk = new Chunk( "Vencimento: " + contaReceberList.get(i).getVencimento(), mOrderClienteValueFont);
                mOrderClienteValueParagraph = new Paragraph(mOrderClienteValueChunk);
                document.add(mOrderClienteValueParagraph);

                document.add(new Paragraph(""));
                document.add(new Chunk(lineSeparator));

                //valorSubTotal = valorSubTotal + contaReceberList.get(i).getValorDevido();
                //desconto = desconto + contaReceberList.get(i).getValorDesconto();
                saldo = saldo + contaReceberList.get(i).getValorDevido();

            }

            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));

            // Fields of Order Details...
            Font mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderAcNameChunk = new Chunk("TOTAL GERAL: " + "R$ " + String.format("%.2f", saldo), mOrderAcNameFont);
            Paragraph mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);
/*
            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("DESCONTO: " + "R$ " + String.format("%.2f", valorDesconto), mOrderAcNameFont);
            mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);

            mOrderAcNameFont = new Font(urName, 18.0f, Font.BOLD, BaseColor.BLACK);
            mOrderAcNameChunk = new Chunk("TOTAL GERAL: " + "R$ " + String.format("%.2f", totalGeral), mOrderAcNameFont);
            mOrderAcNameParagraph = new Paragraph(mOrderAcNameChunk);
            mOrderAcNameParagraph.setAlignment(Element.ALIGN_RIGHT);
            document.add(mOrderAcNameParagraph);
*/
            document.add(new Paragraph(""));
            document.add(new Chunk(lineSeparator));

            Font mOrderNotaFont = new Font(urName, 14.0f, Font.BOLD, BaseColor.BLACK);
            Chunk mOrderNotaChunk = new Chunk("NOTA: ", mOrderNotaFont);
            Paragraph mOrderNotaParagraph = new Paragraph(mOrderNotaChunk);
            document.add(mOrderNotaParagraph);
/*
            Font mOrderNotaInfoFont = new Font(urName, 16.0f, Font.NORMAL, BaseColor.BLACK);
            Chunk mOrderNotaInfoChunk = new Chunk("COVID 19: A melhor maneira de se previnir e o isolamento social. Evite aglomerações!!!", mOrderNotaInfoFont);
            Paragraph mOrderNotaInfoParagraph = new Paragraph(mOrderNotaInfoChunk);
            document.add(mOrderNotaInfoParagraph);
*/
            document.close();

            //Toast.makeText(getActivity(), "Created... :)", Toast.LENGTH_SHORT).show();
            //FileUtils.openFile(getContext(), new File(dest));

/*
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
*/
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

    private String buscaNomeUsuario(){
        String retorno = null;

        try {
            cursor = db
                    .rawQuery(
                            "SELECT _id, id_usuario, nome, email, senha, credito, empresa_id, rota_id FROM usuario_logado where _id like '1'", null);

            cursor.moveToFirst();

            retorno = cursor.getString(2);
        }catch (Exception e){
            retorno = "Não encontrado";
        }

        return retorno;
    }

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }


}
