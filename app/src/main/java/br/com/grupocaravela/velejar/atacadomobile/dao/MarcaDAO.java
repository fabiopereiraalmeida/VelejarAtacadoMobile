package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Marca;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Unidade;

/**
 * Created by fabio on 29/10/15.
 */
public class MarcaDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/MarcaDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/MarcaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_MARCA = "listaMarca";



    public ArrayList<Marca> listarMarca(int id, String nomeBanco){

        ArrayList<Marca> lista = new ArrayList<Marca>();

        SoapObject listarMarcaSO = new SoapObject(NAMESPACE, LISTA_MARCA);
        listarMarcaSO.addProperty("idEmpresa", id);
        listarMarcaSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarMarcaSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_MARCA, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Marca marca = new Marca();

                    marca.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    marca.setNome(soapObject.getProperty("nome").toString());
                    marca.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));

                    lista.add(marca);
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
