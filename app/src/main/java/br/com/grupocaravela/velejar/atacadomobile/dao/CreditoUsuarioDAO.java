package br.com.grupocaravela.velejar.atacadomobile.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
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
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_CREDITO_USUARIO = "listaCreditoUsuario";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private SimpleDateFormat dateformatSoap = new SimpleDateFormat("yyyy-MM-dd");

    public ArrayList<CreditoUsuario> listaCreditoUsuario(int id, String nomeBanco){
        ArrayList<CreditoUsuario> lista = new ArrayList<CreditoUsuario>();

        SoapObject listaCreditoUsuarioSO = new SoapObject(NAMESPACE, LISTA_CREDITO_USUARIO);
        //listaCreditoUsuarioSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());
        listaCreditoUsuarioSO.addProperty("idEmpresa", id);
        listaCreditoUsuarioSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listaCreditoUsuarioSO);
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
                    CreditoUsuario creditoUsuario = new CreditoUsuario();

                    creditoUsuario.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    try {
                        creditoUsuario.setValor(Double.parseDouble(soapObject.getProperty("valor").toString()));
                    }catch (Exception e){
                        creditoUsuario.setValor(0.0);
                    }

                    try {
                        creditoUsuario.setData(soapObject.getProperty("data").toString());
                    }catch (Exception e){
                        creditoUsuario.setData(null);
                    }

                    try {
                        creditoUsuario.setVendaDetalhe(Long.parseLong(soapObject.getProperty("vendaDetalhe").toString()));
                    }catch (Exception e){
                        creditoUsuario.setVendaDetalhe(null);
                    }

                    try {
                        creditoUsuario.setUsuario(Long.parseLong(soapObject.getProperty("usuario").toString()));
                    }catch (Exception e){
                        creditoUsuario.setUsuario(null);
                    }

                    try {
                        creditoUsuario.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        creditoUsuario.setEmpresa(null);
                    }

                    lista.add(creditoUsuario);
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
