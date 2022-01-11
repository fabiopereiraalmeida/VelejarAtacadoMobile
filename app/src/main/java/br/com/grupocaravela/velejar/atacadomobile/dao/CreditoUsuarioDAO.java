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
import br.com.grupocaravela.velejar.atacadomobile.objeto.CreditoUsuario;

/**
 * Created by fabio on 29/10/15.
 */
public class CreditoUsuarioDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CreditoUsuarioDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/CreditoUsuarioDAO?wsdl";
    private static final String NAMESPACE = "http://dao.america.grupocaravela.com.br";

    private static final String LISTA_CREDITO_USUARIO = "listarCreditoUsuario";



    public ArrayList<CreditoUsuario> listarCreditoUsuario(int id, String nomeBanco){
        ArrayList<CreditoUsuario> lista = new ArrayList<CreditoUsuario>();

        SoapObject listarCreditoUsuarioSO = new SoapObject(NAMESPACE, LISTA_CREDITO_USUARIO);
        listarCreditoUsuarioSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarCreditoUsuarioSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_CREDITO_USUARIO, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    CreditoUsuario CreditoUsuario = new CreditoUsuario();

                    CreditoUsuario.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    CreditoUsuario.setValor(Double.parseDouble(soapObject.getProperty("valor").toString()));
                    CreditoUsuario.setUsuario(Long.parseLong(soapObject.getProperty("usuario").toString()));

                    lista.add(CreditoUsuario);
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
