package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidCaixa;

/**
 * Created by fabio on 25/11/15.
 */
public class AndroidCaixaDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CaixaDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/AndroidCaixaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String INSERIR_ANDROID_CAIXA = "inserirCaixa";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatBraHora = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

    public boolean inserirAndroidCaixa(AndroidCaixa androidCaixa, String nomeBanco){

        SoapObject inserirAndroidCaixaSO = new SoapObject(NAMESPACE, INSERIR_ANDROID_CAIXA);

        SoapObject AndroidCaixaSO = new SoapObject(NAMESPACE, "caixa");

        AndroidCaixaSO.addProperty("contaReceber", androidCaixa.getContaReceber().toString());
        Log.d("CAIXA", androidCaixa.getContaReceber().toString());
        try {
            Date dt = formatBraHora.parse(androidCaixa.getDataRecebimento());
            //AndroidCaixaSO.addProperty("data", androidCaixa.getDataRecebimento());
            AndroidCaixaSO.addProperty("data", formatSoapHora.format(dt));
            Log.d("CAIXA", "PASSEI AKI 07.1");
            Log.d("CAIXA", formatSoapHora.format(dt));
            //Log.d("IMPORTANTE 3", "dataVenda: " + androidVendaCabecalho.getDataVenda());
        }catch (Exception e){
            Log.d("CAIXA", "PASSEI AKI 07.2");
        }
        Log.d("CAIXA", "PASSEI AKI 08");
        AndroidCaixaSO.addProperty("empresa", androidCaixa.getEmpresa().toString());
        Log.d("CAIXA", "PASSEI AKI 09");
        Log.d("CAIXA", androidCaixa.getEmpresa().toString());
        AndroidCaixaSO.addProperty("pendente", "true");
        Log.d("CAIXA", "PASSEI AKI 10");
        AndroidCaixaSO.addProperty("usuario", androidCaixa.getUsuario().toString());
        Log.d("CAIXA", "PASSEI AKI 11");
        Log.d("CAIXA", androidCaixa.getUsuario().toString());
        AndroidCaixaSO.addProperty("valor", androidCaixa.getValor().toString());
        Log.d("CAIXA", "PASSEI AKI 12");
        Log.d("CAIXA", androidCaixa.getValor().toString());

        Log.d("CAIXA", "VENDA CABECALHO " + androidCaixa.getVendaCabecalho().toString());

        if (androidCaixa.getVendaCabecalho().toString().equals("0")){
            AndroidCaixaSO.addProperty("vendaCabecalho", null);
            Log.d("CAIXA", "PASSEI AKI 12 NULL");
            Log.d("CAIXA", "NULL");

        }else {
            AndroidCaixaSO.addProperty("vendaCabecalho", androidCaixa.getVendaCabecalho().toString());
            Log.d("CAIXA", "PASSEI AKI 12");
            Log.d("CAIXA", androidCaixa.getVendaCabecalho().toString());
        }

        Log.d("CAIXA", "CLIENTEAKI " + androidCaixa.getCliente().toString());
        AndroidCaixaSO.addProperty("cliente", androidCaixa.getCliente().toString());

        inserirAndroidCaixaSO.addSoapObject(AndroidCaixaSO);
        inserirAndroidCaixaSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());


        Log.d("CAIXA", "PASSEI AKI 13");
        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        Log.d("CAIXA", "PASSEI AKI 14");
        envelopeSO.setOutputSoapObject(inserirAndroidCaixaSO);
        Log.d("CAIXA", "PASSEI AKI 15");
        envelopeSO.implicitTypes = true; //Sem isso da erro
        Log.d("CAIXA", "PASSEI AKI 16");
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);
        try {
            Log.d("CAIXA", "PASSEI AKI 17");
            httpTransportSE.call("urn:" + INSERIR_ANDROID_CAIXA, envelopeSO); //Realiza o envio da mensagem
            Log.d("CAIXA", "PASSEI AKI 18");
            SoapPrimitive resposta = (SoapPrimitive) envelopeSO.getResponse(); //Pega a mensagem de resposta
            Log.d("CAIXA", "PASSEI AKI 19");
            return Boolean.parseBoolean(resposta.toString()); //Passa a resposta "true ou false" em String para o tipo boolean

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "ERRO: " + e);
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "ERRO: " + e);
            return false;
        }
    }


}
