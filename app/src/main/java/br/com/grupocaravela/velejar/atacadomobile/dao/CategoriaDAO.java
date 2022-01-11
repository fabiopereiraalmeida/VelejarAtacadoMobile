package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.util.Log;

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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Categoria;

/**
 * Created by fabio on 29/10/15.
 */
public class CategoriaDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CategoriaDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/CategoriaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTAR_CATEGORIA = "listaCategoria";

    public ArrayList<Categoria> listarCategoria(int id, String nomeBanco){
        ArrayList<Categoria> lista = new ArrayList<>();

        SoapObject listarCategoriaSO = new SoapObject(NAMESPACE, LISTAR_CATEGORIA);
        listarCategoriaSO.addProperty("idEmpresa", id);
        listarCategoriaSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarCategoriaSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTAR_CATEGORIA, envelopeSO); //Realiza o envio da mensagem
            //Vector<SoapObject> resposta = (Vector<SoapObject>) envelopeSO.getResponse(); //Pega a mensagem de resposta
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {

                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Categoria categoria = new Categoria();

                    categoria.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    categoria.setNome(soapObject.getProperty("nome").toString());
                    categoria.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));

                    lista.add(categoria);
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
