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
import java.util.ArrayList;
import java.util.Date;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;

/**
 * Created by fabio on 29/10/15.
 */
public class ContaReceberDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/ContaReceberDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/ContaReceberDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTAR_CONTA_RECEBER = "listaContaReceber";
    private static final String ATUALIZAR_CONTA_RECEBER = "atualizarContaReceber";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("dd/MM/yyyy");
/*
    private SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
    private SimpleDateFormat formatSoapCompleto = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    private SimpleDateFormat formatDataHoraBRA = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSA = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    private SimpleDateFormat formatDataHoraUSAEstenso = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy"); //2015-11-10 10:27:28
*/
    public ArrayList<ContaReceber> listarContaReceber(int id, String nomeBanco){
        ArrayList<ContaReceber> lista = new ArrayList<ContaReceber>();

        SoapObject listarContaReceberSO = new SoapObject(NAMESPACE, LISTAR_CONTA_RECEBER);
        listarContaReceberSO.addProperty("idEmpresa", id);
        listarContaReceberSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarContaReceberSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTAR_CONTA_RECEBER, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    ContaReceber contaReceber = new ContaReceber();

                    contaReceber.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    //Log.i("CONTA RECEBER", "PASSEI AKI 01!");

                    try {
                        contaReceber.setCliente(Long.parseLong(soapObject.getProperty("cliente").toString()));
                    }catch (Exception e){
                        contaReceber.setCliente(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 02!");

                    try {
                        contaReceber.setVendaCabecalho(Long.parseLong(soapObject.getProperty("vendaCabecalho").toString()));
                    }catch (Exception e){
                        contaReceber.setVendaCabecalho(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 03!");

                    try {
                        contaReceber.setValorDevido(Double.parseDouble(soapObject.getProperty("valorDevido").toString()));
                    }catch (Exception e){
                        contaReceber.setValorDevido(0.0);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 04!");

                    try {
                        Date v = formatSoap.parse(soapObject.getProperty("vencimento").toString());
                        //contaReceber.setVencimento(soapObject.getProperty("vencimento").toString());
                        contaReceber.setVencimento(formatSoapHora.format(v));
                    } catch (Exception e) {
                        contaReceber.setVencimento(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 05!");

                    try {
                        Date v = formatSoap.parse(soapObject.getProperty("dataPagamento").toString());
                        //contaReceber.setDataPagamento(soapObject.getProperty("dataPagamento").toString());
                        contaReceber.setDataPagamento(formatSoapHora.format(v));
                    }catch (Exception e){
                        contaReceber.setDataPagamento(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 06!");

                    try {
                        contaReceber.setQuitada(Boolean.parseBoolean(soapObject.getProperty("quitada").toString()));
                    }catch (Exception e){
                        contaReceber.setQuitada(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 07!");

                    try {
                        contaReceber.setAtrasada(Boolean.parseBoolean(soapObject.getProperty("atrasada").toString()));
                    }catch (Exception e){
                        contaReceber.setAtrasada(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 08!");

                    try {
                        contaReceber.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        contaReceber.setEmpresa(null);
                    }

                    //Log.i("CONTA RECEBER", "PASSEI AKI 09!");

                    try {
                        contaReceber.setValorDesconto(Double.parseDouble(soapObject.getProperty("valorDesconto").toString()));
                    }catch (Exception e){
                        contaReceber.setValorDesconto(0.0);
                    }

                    lista.add(contaReceber);
                }
            }

        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }
        //Log.i("CONTA RECEBER", "PASSEI AKI 10!");
        return lista;
    }

    public boolean inserirObservacao(ContaReceber contaReceber, String nomeBanco){

        SoapObject inserirAndroidContaReceberSO = new SoapObject(NAMESPACE, ATUALIZAR_CONTA_RECEBER);
        //inserirAndroidContaReceberSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapObject AndroidContaReceberSO = new SoapObject(NAMESPACE, "contaReceber");

        Log.d("OBSERVACOES", "PASSEI AKI 07");

        AndroidContaReceberSO.addProperty("atrasada", false);
        AndroidContaReceberSO.addProperty("cliente", contaReceber.getCliente().toString());
        Log.d("OBSERVACOES", "PASSEI AKI 08");
        try {
            //AndroidContaReceberSO.addProperty("dataTransmissao", formatSoap.format(androidContaReceber.getDataTransmissao()));
            AndroidContaReceberSO.addProperty("dataPagamento", formatSoap.format(dataAtual().getTime()));
        }catch (Exception e){
            AndroidContaReceberSO.addProperty("dataPagamento", "2000-01-01");
        }
        Log.d("OBSERVACOES", "PASSEI AKI 09");
        AndroidContaReceberSO.addProperty("empresa", contaReceber.getEmpresa().toString());
        AndroidContaReceberSO.addProperty("id", contaReceber.getId().toString());
        AndroidContaReceberSO.addProperty("observacao", contaReceber.getObservacao());
        AndroidContaReceberSO.addProperty("quitada", false);
        Log.d("OBSERVACOES", "PASSEI AKI 10");
        AndroidContaReceberSO.addProperty("valorDesconto", contaReceber.getValorDesconto().toString());
        AndroidContaReceberSO.addProperty("valorDevido", contaReceber.getValorDevido().toString());
        AndroidContaReceberSO.addProperty("vencimento", contaReceber.getVencimento().toString());
        AndroidContaReceberSO.addProperty("vendaCabecalho", contaReceber.getVendaCabecalho().toString());
        Log.d("OBSERVACOES", "PASSEI AKI 11");
        Log.d("OBSERVACOES", "VALOR: " + AndroidContaReceberSO.getProperty("valorDevido"));

        inserirAndroidContaReceberSO.addSoapObject(AndroidContaReceberSO);
        inserirAndroidContaReceberSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(inserirAndroidContaReceberSO);
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + ATUALIZAR_CONTA_RECEBER, envelopeSO); //Realiza o envio da mensagem

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

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }
}
