package br.com.grupocaravela.velejar.atacadomobile.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.VendaCabecalho;

/**
 * Created by fabio on 29/10/15.
 */
public class VendaCabecalhoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/VendaCabecalhoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/VendaCabecalhoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_VENDA_CABECALHO = "listaVendaCabecalho";

    private SimpleDateFormat formatSoap = new SimpleDateFormat("yyyy-MM-dd");
    private SimpleDateFormat formatSoapHora = new SimpleDateFormat("dd/MM/yyyy");

    public ArrayList<VendaCabecalho> listarVendaCabecalho(int id, String nomeBanco){
        ArrayList<VendaCabecalho> lista = new ArrayList<VendaCabecalho>();

        SoapObject listaVendaCabecalhoSO = new SoapObject(NAMESPACE, LISTA_VENDA_CABECALHO);
        listaVendaCabecalhoSO.addProperty("idEmpresa", id);
        listaVendaCabecalhoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listaVendaCabecalhoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_VENDA_CABECALHO, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    VendaCabecalho vendaCabecalho = new VendaCabecalho();

                    vendaCabecalho.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    try {
                        Date v = formatSoap.parse(soapObject.getProperty("dataPrimeiroVencimento").toString());
                        vendaCabecalho.setDataPrimeiroVencimento(formatSoapHora.format(v));
                    }catch (Exception e){
                        vendaCabecalho.setDataPrimeiroVencimento(null);
                    }

                    try {
                        Date v = formatSoap.parse(soapObject.getProperty("dataVenda").toString());
                        vendaCabecalho.setDataVenda(formatSoapHora.format(v));
                    } catch (Exception e) {
                        vendaCabecalho.setDataVenda(null);
                    }

                    vendaCabecalho.setEntrada(Double.parseDouble(soapObject.getProperty("entrada").toString()));
                    vendaCabecalho.setJuros(Double.parseDouble(soapObject.getProperty("juros").toString()));
                    try {
                        vendaCabecalho.setObservacao(soapObject.getProperty("observacao").toString());
                    }catch (Exception e) {
                        vendaCabecalho.setObservacao(null);
                    }
                    vendaCabecalho.setValorDesconto(Double.parseDouble(soapObject.getProperty("valorDesconto").toString()));
                    vendaCabecalho.setValorParcial(Double.parseDouble(soapObject.getProperty("valorParcial").toString()));
                    vendaCabecalho.setValorTotal(Double.parseDouble(soapObject.getProperty("valorTotal").toString()));
                    vendaCabecalho.setCliente(Long.parseLong(soapObject.getProperty("cliente").toString()));
                    vendaCabecalho.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    vendaCabecalho.setFormaPagamento(Long.parseLong(soapObject.getProperty("formaPagamento").toString()));
                    vendaCabecalho.setUsuario(Long.parseLong(soapObject.getProperty("usuario").toString()));

                    lista.add(vendaCabecalho);
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
