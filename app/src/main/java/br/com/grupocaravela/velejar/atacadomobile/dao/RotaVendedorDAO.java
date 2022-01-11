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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Rota;
import br.com.grupocaravela.velejar.atacadomobile.objeto.RotaVendedor;

/**
 * Created by fabio on 29/10/15.
 */
public class RotaVendedorDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/RotaVendedorDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/RotaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_ROTA_VENDEDOR = "listaRotaVendedor";



    public ArrayList<RotaVendedor> listarRotaVendedor(int id, String nomeBanco){
        ArrayList<RotaVendedor> lista = new ArrayList<RotaVendedor>();

        SoapObject listarRotaSO = new SoapObject(NAMESPACE, LISTA_ROTA_VENDEDOR);
        listarRotaSO.addProperty("idEmpresa", id);
        listarRotaSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarRotaSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_ROTA_VENDEDOR, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    RotaVendedor rotaVendedor = new RotaVendedor();

                    rotaVendedor.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    rotaVendedor.setRota(Long.parseLong(soapObject.getProperty("rota").toString()));
                    rotaVendedor.setUsuario(Long.parseLong(soapObject.getProperty("usuario").toString()));

                    lista.add(rotaVendedor);
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
