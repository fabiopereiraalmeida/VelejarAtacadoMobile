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
import br.com.grupocaravela.velejar.atacadomobile.objeto.TelefoneCliente;

/**
 * Created by fabio on 29/10/15.
 */
public class TelefoneClienteDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/TelefoneClienteDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/TelefoneClienteDAO?wsdl";
    private static final String NAMESPACE = "http://dao.america.grupocaravela.com.br";

    private static final String LISTA_TELEFONE_CLIENTE = "listaTelefoneCliente";



    public ArrayList<TelefoneCliente> listarTelefoneCliente(String nomeBanco){
        ArrayList<TelefoneCliente> lista = new ArrayList<TelefoneCliente>();

        SoapObject listarTelefoneClienteSO = new SoapObject(NAMESPACE, LISTA_TELEFONE_CLIENTE);
        listarTelefoneClienteSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarTelefoneClienteSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_TELEFONE_CLIENTE, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    TelefoneCliente telefoneCliente = new TelefoneCliente();

                    telefoneCliente.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    telefoneCliente.setTelefone(soapObject.getProperty("telefone").toString());
                    telefoneCliente.setTipoTelefone(soapObject.getProperty("tipoTelefone").toString());
                    telefoneCliente.setCliente(Long.parseLong(soapObject.getProperty("cliente").toString()));

                    lista.add(telefoneCliente);
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
