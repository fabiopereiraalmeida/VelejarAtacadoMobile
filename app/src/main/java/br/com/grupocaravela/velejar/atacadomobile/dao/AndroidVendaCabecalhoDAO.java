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
import br.com.grupocaravela.velejar.atacadomobile.objeto.AndroidVendaCabecalho;

/**
 * Created by fabio on 25/11/15.
 */
public class AndroidVendaCabecalhoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/VendaCabecalhoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/VendaCabecalhoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String INSERIR_ANDROID_VENDA_CABECALHO = "inserirVendaCabecalho";
    private static final String BUSCAR_ULTIMO_ID_ANDROID_VENDA_CABECALHO = "buscaUltimoIdVendaCabecalho";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSAEstenso = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");

    public boolean inserirAndroidVendaCabecalho(AndroidVendaCabecalho androidVendaCabecalho, String nomeBanco){

        SoapObject inserirAndroidVendaCabecalhoSO = new SoapObject(NAMESPACE, INSERIR_ANDROID_VENDA_CABECALHO);

        SoapObject AndroidVendaCabecalhoSO = new SoapObject(NAMESPACE, "vendaCabecalho");

        try {
            AndroidVendaCabecalhoSO.addProperty("cliente", androidVendaCabecalho.getCliente().toString());
            Log.d("IMPORTANTE 1", "Cliente: " + androidVendaCabecalho.getCliente().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("cliente", null);
        }
        /*
        try {
            AndroidVendaCabecalhoSO.addProperty("codVenda", androidVendaCabecalho.getCodVenda().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("codVenda", null);
        }
        */
        try {
            //Date dt = formatDataHoraUSAEstenso.parse(androidVendaCabecalho.getDataPrimeiroVencimento());
            //AndroidVendaCabecalhoSO.addProperty("dataPrimeiroVencimento", formatSoap.format(dt));
            AndroidVendaCabecalhoSO.addProperty("dataPrimeiroVencimento", androidVendaCabecalho.getDataPrimeiroVencimento());

            Log.d("IMPORTANTE 2", "dataPrimeiroVencimento: " + androidVendaCabecalho.getDataPrimeiroVencimento());
                    //AndroidVendaCabecalhoSO.addProperty("dataPrimeiroVencimento", "2016-10-10");
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("dataPrimeiroVencimento", null);
        }
        try {
            //Date dt = formatDataHoraUSAEstenso.parse(androidVendaCabecalho.getDataVenda());
            //AndroidVendaCabecalhoSO.addProperty("dataVenda", formatSoapHora.format(dt));
            AndroidVendaCabecalhoSO.addProperty("dataVenda", androidVendaCabecalho.getDataVenda());
            Log.d("IMPORTANTE 3", "dataVenda: " + androidVendaCabecalho.getDataVenda());
            //AndroidVendaCabecalhoSO.addProperty("dataVenda", "2012-10-10");
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("dataVenda", null);
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("empresa", androidVendaCabecalho.getEmpresa().toString());
            Log.d("IMPORTANTE 4", "empresa: " + androidVendaCabecalho.getEmpresa().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("empresa", null);
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("entrada", androidVendaCabecalho.getEntrada().toString());
            Log.d("IMPORTANTE 5", "entrada: " + androidVendaCabecalho.getEntrada().toString());
        }catch (Exception e){
            AndroidVendaCabecalhoSO.addProperty("entrada", "0.0");
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("formaPagamento", androidVendaCabecalho.getFormaPagamento().toString());
            Log.d("IMPORTANTE 6", "formaPagamento: " + androidVendaCabecalho.getFormaPagamento().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("formaPagamento", null);
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("juros", androidVendaCabecalho.getJuros().toString());
            Log.d("IMPORTANTE 7", "juros: " + androidVendaCabecalho.getJuros().toString());
        }catch (Exception e){
            AndroidVendaCabecalhoSO.addProperty("juros", "0.0");
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("observacao", androidVendaCabecalho.getObservacao().toString());
            Log.d("IMPORTANTE 8", "observacao: " + androidVendaCabecalho.getObservacao().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("observacao", null);
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("usuario", androidVendaCabecalho.getUsuario().toString());
            Log.d("IMPORTANTE 9", "usuario: " + androidVendaCabecalho.getUsuario().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("usuario", null);
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("valorDesconto", androidVendaCabecalho.getValorDesconto().toString());
            Log.d("IMPORTANTE 10", "valorDesconto: " + androidVendaCabecalho.getValorDesconto().toString());
        }catch (Exception e){
            AndroidVendaCabecalhoSO.addProperty("valorDesconto", "0.0");
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("valorParcial", androidVendaCabecalho.getValorParcial().toString());
            Log.d("IMPORTANTE 11", "valorParcial: " + androidVendaCabecalho.getValorParcial().toString());
        }catch (Exception e){
            AndroidVendaCabecalhoSO.addProperty("valorParcial", "0.0");
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("valorTotal", androidVendaCabecalho.getValorTotal().toString());
            Log.d("IMPORTANTE 12", "valorTotal: " + androidVendaCabecalho.getValorTotal().toString());
        }catch (Exception e){
            AndroidVendaCabecalhoSO.addProperty("valorTotal", "0.0");
        }
        try {
            AndroidVendaCabecalhoSO.addProperty("clienteMobile", androidVendaCabecalho.getClienteMobile().toString());
            Log.d("IMPORTANTE 13", "clienteMobile: " + androidVendaCabecalho.getClienteMobile().toString());
        }catch (Exception e){
            //AndroidVendaCabecalhoSO.addProperty("clienteMobile", null);
        }

        Log.d("IMPORTANTE", "PASSEI AKI 01");

        inserirAndroidVendaCabecalhoSO.addSoapObject(AndroidVendaCabecalhoSO);
        inserirAndroidVendaCabecalhoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        Log.d("IMPORTANTE", "PASSEI AKI 02");

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11

        Log.d("IMPORTANTE", "PASSEI AKI 03");
        envelopeSO.setOutputSoapObject(inserirAndroidVendaCabecalhoSO);
        Log.d("IMPORTANTE", "PASSEI AKI 04");
        envelopeSO.implicitTypes = true; //Sem isso da erro

        Log.d("IMPORTANTE", "PASSEI AKI 05");
        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        Log.d("IMPORTANTE", "PASSEI AKI 06");
        try {

            httpTransportSE.call("urn:" + INSERIR_ANDROID_VENDA_CABECALHO, envelopeSO); //Realiza o envio da mensagem
            Log.d("IMPORTANTE", "PASSEI AKI 07");
            SoapPrimitive resposta = (SoapPrimitive) envelopeSO.getResponse(); //Pega a mensagem de resposta
            Log.d("IMPORTANTE", "PASSEI AKI 08");
            Log.d("IMPORTANTE", "RESPOSTA: " + resposta.toString());
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

    public Long buscarUltimoIdVendaCabecalho(String nomeBanco){

        Long retorno = null;

        SoapObject buscarUltimoIdVendaCabecalho = new SoapObject(NAMESPACE, BUSCAR_ULTIMO_ID_ANDROID_VENDA_CABECALHO);
        buscarUltimoIdVendaCabecalho.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11

        envelopeSO.setOutputSoapObject(buscarUltimoIdVendaCabecalho);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + BUSCAR_ULTIMO_ID_ANDROID_VENDA_CABECALHO, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {

                for (int i = 0; i < propsNum; i++) {
                    retorno = Long.parseLong(resposta.getProperty(i).toString());

                }
            }

        }catch (IOException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "erro: " + e);
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "erro: " + e);
            return null;
        }

        return retorno;
    }
}
