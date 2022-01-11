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
import br.com.grupocaravela.velejar.atacadomobile.objeto.CreditoUsuarioDetalhes;

/**
 * Created by fabio on 29/10/15.
 */
public class CreditoUsuarioDetalhesDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CreditoUsuarioDetalhesDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/CreditoUsuarioDetalhesDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_CREDITO_USUARIO_DETALHES = "listaCreditoUsuarioDetalhes";



    public ArrayList<CreditoUsuarioDetalhes> listarCreditoUsuarioDetalhes(int id, String nomeBanco){
        ArrayList<CreditoUsuarioDetalhes> lista = new ArrayList<CreditoUsuarioDetalhes>();

        SoapObject listaVendaCabecalhoSO = new SoapObject(NAMESPACE, LISTA_CREDITO_USUARIO_DETALHES);
        listaVendaCabecalhoSO.addProperty("idEmpresa", id);
        listaVendaCabecalhoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listaVendaCabecalhoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_CREDITO_USUARIO_DETALHES, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    CreditoUsuarioDetalhes creditoUsuarioDetalhes = new CreditoUsuarioDetalhes();

                    creditoUsuarioDetalhes.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    try {
                        creditoUsuarioDetalhes.setData(soapObject.getProperty("data").toString());
                    } catch (Exception e) {
                        creditoUsuarioDetalhes.setData(null);
                    }
                    creditoUsuarioDetalhes.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    creditoUsuarioDetalhes.setValor(Double.parseDouble(soapObject.getProperty("valor").toString()));
                    creditoUsuarioDetalhes.setUsuario(Long.parseLong(soapObject.getProperty("usuario").toString()));
                    creditoUsuarioDetalhes.setVendaDetalhe(Long.parseLong(soapObject.getProperty("vendaDetalhe").toString()));

                    lista.add(creditoUsuarioDetalhes);
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
