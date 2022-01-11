package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidContaReceber;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;

/**
 * Created by fabio on 25/11/15.
 */
public class AndroidContaReceberDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/ContaReceberDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/AndroidContaReceberDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    //private static final String INSERIR_ANDROID_CONTA_RECEBER = "inserirAndroidContaReceber";
    private static final String ATUALIZAR_CONTA_RECEBER = "atualizarContaReceber";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
/*
    public boolean inserirAndroidCOntaReceber(AndroidContaReceber androidContaReceber, String nomeBanco){

        SoapObject inserirAndroidContaReceberSO = new SoapObject(NAMESPACE, INSERIR_ANDROID_CONTA_RECEBER);
        inserirAndroidContaReceberSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapObject AndroidContaReceberSO = new SoapObject(NAMESPACE, "androidContaReceber");

        AndroidContaReceberSO.addProperty("cliente", androidContaReceber.getCliente().toString());
        AndroidContaReceberSO.addProperty("usuario", androidContaReceber.getUsuario().toString());
        try {
            //AndroidContaReceberSO.addProperty("dataTransmissao", formatSoap.format(androidContaReceber.getDataTransmissao()));
            AndroidContaReceberSO.addProperty("dataTransmissao", formatSoap.format(dataAtual().getTime()));
        }catch (Exception e){
            AndroidContaReceberSO.addProperty("dataTransmissao", "2000-01-01");
        }

        try {
            AndroidContaReceberSO.addProperty("dataRecebimento", formatSoap.format(androidContaReceber.getDataRecebimento()));
        }catch (Exception e){
            AndroidContaReceberSO.addProperty("dataRecebimento", "2000-01-01");
        }
        AndroidContaReceberSO.addProperty("valorDevido", androidContaReceber.getValorDevido().toString());
        AndroidContaReceberSO.addProperty("vendaCabecalho", androidContaReceber.getVendaCabecalho().toString());
        AndroidContaReceberSO.addProperty("ativo", "true");

        inserirAndroidContaReceberSO.addSoapObject(AndroidContaReceberSO);

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(inserirAndroidContaReceberSO);
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + INSERIR_ANDROID_CONTA_RECEBER, envelopeSO); //Realiza o envio da mensagem

            SoapPrimitive resposta = (SoapPrimitive) envelopeSO.getResponse(); //Pega a mensagem de resposta

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
*/

}
