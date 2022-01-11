package br.com.grupocaravela.velejar.atacadomobile.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.FormaPagamento;

/**
 * Created by fabio on 29/10/15.
 */
public class FormaPagamentoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/FormaPagamentoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/FormaPagamentoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_FORMA_PAGAMENTO = "listaFormaPagamento";

    public ArrayList<FormaPagamento> listarFormaPagamento(int id, String nomeBanco){
        ArrayList<FormaPagamento> lista = new ArrayList<FormaPagamento>();

        SoapObject listarFormaPagamentoSO = new SoapObject(NAMESPACE, LISTA_FORMA_PAGAMENTO);
        listarFormaPagamentoSO.addProperty("idEmpresa", id);
        listarFormaPagamentoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarFormaPagamentoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_FORMA_PAGAMENTO, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    FormaPagamento formaPagamento = new FormaPagamento();

                    formaPagamento.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    try {
                        formaPagamento.setJuros(Double.parseDouble(soapObject.getProperty("juros").toString()));
                    }catch (Exception e){
                        formaPagamento.setJuros(0.0);
                    }

                    try {
                        formaPagamento.setNome(soapObject.getProperty("nome").toString());
                    }catch (Exception e){
                        formaPagamento.setNome(null);
                    }

                    try {
                        formaPagamento.setNumeroDias(Integer.parseInt(soapObject.getProperty("numeroDias").toString()));
                    }catch (Exception e){
                        formaPagamento.setNumeroDias(0);
                    }

                    try {
                        formaPagamento.setNumeroParcelas(Integer.parseInt(soapObject.getProperty("numeroParcelas").toString()));
                    }catch (Exception e){
                        formaPagamento.setNumeroParcelas(1);
                    }

                    try {
                        formaPagamento.setObservacao(soapObject.getProperty("observacao").toString());
                    }catch (Exception e){
                        formaPagamento.setObservacao(null);
                    }

                    try {
                        formaPagamento.setValorMinimo(Double.parseDouble(soapObject.getProperty("valorMinimo").toString()));
                    }catch (Exception e){
                        formaPagamento.setValorMinimo(0.0);
                    }

                    try {
                        formaPagamento.setGeral(Boolean.parseBoolean(soapObject.getProperty("geral").toString()));
                    }catch (Exception e){
                        formaPagamento.setGeral(false);
                    }

                    try {
                        formaPagamento.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        formaPagamento.setEmpresa(null);
                    }

                    lista.add(formaPagamento);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

        return lista;
    }
}
