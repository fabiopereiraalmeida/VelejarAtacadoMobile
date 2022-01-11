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

/**
 * Created by fabio on 29/10/15.
 */
public class CidadeDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CidadeDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/CidadeDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTAR_CIDADE = "listaCidade";



    public ArrayList<Cidade> listarCidade(String nomeBanco){
        ArrayList<Cidade> lista = new ArrayList<Cidade>();

        SoapObject listarCidadeSO = new SoapObject(NAMESPACE, LISTAR_CIDADE);
        listarCidadeSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarCidadeSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTAR_CIDADE, envelopeSO); //Realiza o envio da mensagem

            Vector<SoapObject> resposta = (Vector<SoapObject>) envelopeSO.getResponse(); //Pega a mensagem de resposta

            for (SoapObject soapObject : resposta){
                Cidade cidade = new Cidade();

                cidade.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                cidade.setId_estado(Long.parseLong(soapObject.getProperty("id_estado").toString()));
                cidade.setNome(soapObject.getProperty("nome").toString());
                cidade.setIbge(soapObject.getProperty("ibge").toString());

                lista.add(cidade);
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
