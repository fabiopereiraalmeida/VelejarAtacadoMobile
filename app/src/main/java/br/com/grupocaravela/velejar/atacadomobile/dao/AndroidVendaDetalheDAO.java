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
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaDetalhe;

/**
 * Created by fabio on 25/11/15.
 */
public class AndroidVendaDetalheDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/VendaDetalheDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/VendaDetalheDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String INSERIR_ANDROID_VENDA_DETALHE = "inserirVendaDetalhe";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public boolean inserirAndroidVendaDetalhe(AndroidVendaDetalhe androidVendaDetalhe, String nomeBanco){

        SoapObject inserirAndroidVendaDetalheSO = new SoapObject(NAMESPACE, INSERIR_ANDROID_VENDA_DETALHE);

        SoapObject AndroidVendaDetalheSO = new SoapObject(NAMESPACE, "VendaDetalhe");

        AndroidVendaDetalheSO.addProperty("empresa", androidVendaDetalhe.getEmpresa().toString());
        Log.d("VELEJAR", "EMPRESA " + androidVendaDetalhe.getEmpresa().toString());
        AndroidVendaDetalheSO.addProperty("produto", androidVendaDetalhe.getProduto().toString());
        Log.d("VELEJAR", "PRODUTO " + androidVendaDetalhe.getProduto().toString());
        AndroidVendaDetalheSO.addProperty("quantidade", androidVendaDetalhe.getQuantidade().toString());
        Log.d("VELEJAR", "QTD " + androidVendaDetalhe.getQuantidade().toString());
        AndroidVendaDetalheSO.addProperty("valorUnitario", androidVendaDetalhe.getValorUnitario().toString());
        Log.d("VELEJAR", "VALOR UNITARIO " + androidVendaDetalhe.getValorUnitario().toString());
        AndroidVendaDetalheSO.addProperty("valorDesconto", androidVendaDetalhe.getValorDesconto().toString());
        Log.d("VELEJAR", "VALOR DESCONTO " + androidVendaDetalhe.getValorDesconto().toString());
        AndroidVendaDetalheSO.addProperty("valorParcial", androidVendaDetalhe.getValorParcial().toString());
        Log.d("VELEJAR", "VALOR PARCIAL " + androidVendaDetalhe.getValorParcial().toString());
        AndroidVendaDetalheSO.addProperty("valorTotal", androidVendaDetalhe.getValorTotal().toString());
        Log.d("VELEJAR", "VALOR TOTAL " + androidVendaDetalhe.getValorTotal().toString());
        AndroidVendaDetalheSO.addProperty("vendaCabecalho", androidVendaDetalhe.getVendaCabecalho().toString());
        Log.d("VELEJAR", "ID CABECALHO " + androidVendaDetalhe.getVendaCabecalho().toString());

        inserirAndroidVendaDetalheSO.addSoapObject(AndroidVendaDetalheSO);
        inserirAndroidVendaDetalheSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(inserirAndroidVendaDetalheSO);
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + INSERIR_ANDROID_VENDA_DETALHE, envelopeSO); //Realiza o envio da mensagem

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
}
