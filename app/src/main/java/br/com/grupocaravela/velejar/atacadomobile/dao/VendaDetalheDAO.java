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
import br.com.grupocaravela.velejar.atacadomobile.objeto.VendaDetalhe;

/**
 * Created by fabio on 29/10/15.
 */
public class VendaDetalheDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/VendaDetalheDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/VendaDetalheDAO?wsdl";
    private static final String NAMESPACE = "http://dao.america.grupocaravela.com.br";

    private static final String LISTA_VENDA_DETALHE = "listaVendaDetalhe";



    public ArrayList<VendaDetalhe> listarVendaDetalhe(String nomeBanco){
        ArrayList<VendaDetalhe> lista = new ArrayList<VendaDetalhe>();

        SoapObject listarVendaDetalheSO = new SoapObject(NAMESPACE, LISTA_VENDA_DETALHE);
        listarVendaDetalheSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarVendaDetalheSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_VENDA_DETALHE, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    VendaDetalhe vendaDetalhe = new VendaDetalhe();

                    vendaDetalhe.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    vendaDetalhe.setQuantidade(Double.parseDouble(soapObject.getProperty("quantidade").toString()));
                    vendaDetalhe.setValorDesconto(Double.parseDouble(soapObject.getProperty("valorDesconto").toString()));
                    vendaDetalhe.setValorParcial(Double.parseDouble(soapObject.getProperty("valorParcial").toString()));
                    vendaDetalhe.setValorTotal(Double.parseDouble(soapObject.getProperty("valorTotal").toString()));
                    vendaDetalhe.setProduto(Long.parseLong(soapObject.getProperty("produto").toString()));
                    vendaDetalhe.setVendaCabecalho(Long.parseLong(soapObject.getProperty("vendaCabecalho").toString()));

                    lista.add(vendaDetalhe);
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
