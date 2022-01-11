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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Rota;

/**
 * Created by fabio on 29/10/15.
 */
public class RotaDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/RotaDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/RotaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_ROTA = "listaRota";



    public ArrayList<Rota> listarRota(int id, String nomeBanco){
        ArrayList<Rota> lista = new ArrayList<Rota>();

        Log.i("LOG", "--- 01 ---");

        SoapObject listarRotaSO = new SoapObject(NAMESPACE, LISTA_ROTA);
        listarRotaSO.addProperty("idEmpresa", id);
        listarRotaSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        Log.i("LOG", "--- 02 ---");

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarRotaSO);

        Log.i("LOG", "--- 03 ---");

        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            Log.i("LOG", "--- 04 ---");

            httpTransportSE.call("urn:" + LISTA_ROTA, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            Log.i("LOG", "--- 05 ---");

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){

                Log.i("LOG", "--- 05.1 ---");

                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Rota rota = new Rota();

                    Log.i("LOG", "--- 06 ---");

                    rota.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    Log.i("LOG", "--- 06.1 ---");
                    rota.setNome(soapObject.getProperty("nome").toString());
                    Log.i("LOG", "--- 06.2 ---");
                    try {
                        rota.setObservacao(soapObject.getProperty("observacao").toString());
                    }catch (Exception e){
                        rota.setObservacao(null);
                    }

                    Log.i("LOG", "--- 07 ---");

                    lista.add(rota);
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
