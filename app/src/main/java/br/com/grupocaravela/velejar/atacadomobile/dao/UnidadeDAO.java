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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Unidade;

/**
 * Created by fabio on 29/10/15.
 */
public class UnidadeDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/UnidadeDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/UnidadeDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_UNIDADE = "listaUnidade";



    public ArrayList<Unidade> listarUnidade(int id, String nomeBanco){

        ArrayList<Unidade> lista = new ArrayList<Unidade>();

        SoapObject listarUnidadeSO = new SoapObject(NAMESPACE, LISTA_UNIDADE);
        listarUnidadeSO.addProperty("idEmpresa", id);
        listarUnidadeSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarUnidadeSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_UNIDADE, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Unidade unidade = new Unidade();

                    unidade.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    unidade.setNome(soapObject.getProperty("nome").toString());
                    unidade.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));

                    lista.add(unidade);
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
