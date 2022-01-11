package br.com.grupocaravela.velejar.atacadomobile.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cidade;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Estado;

/**
 * Created by fabio on 29/10/15.
 */
public class EstadoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/EstadoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/EstadoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTAR_ESTADO = "listaEstado";



    public ArrayList<Estado> listarEstado(String nomeBanco){
        ArrayList<Estado> lista = new ArrayList<Estado>();

        SoapObject listarEstadoSO = new SoapObject(NAMESPACE, LISTAR_ESTADO);
        listarEstadoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarEstadoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTAR_ESTADO, envelopeSO); //Realiza o envio da mensagem

            Vector<SoapObject> resposta = (Vector<SoapObject>) envelopeSO.getResponse(); //Pega a mensagem de resposta

            for (SoapObject soapObject : resposta){
                Estado estado = new Estado();

                estado.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                estado.setNome(soapObject.getProperty("nome").toString());
                estado.setUf_estado(soapObject.getProperty("uf_estado").toString());
                estado.setCodigo(soapObject.getProperty("codigo").toString());

                lista.add(estado);
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
