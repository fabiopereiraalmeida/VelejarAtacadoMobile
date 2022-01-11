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
import br.com.grupocaravela.velejar.atacadomobile.objeto.EnderecoCliente;

/**
 * Created by fabio on 29/10/15.
 */
public class EnderecoClienteDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/EnderecoClienteDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/EnderecoClienteDAO?wsdl";
    private static final String NAMESPACE = "http://dao.america.grupocaravela.com.br";

    private static final String LISTA_ENDERECO_CLIENTE = "listaEnderecoCliente";



    public ArrayList<EnderecoCliente> listarEnderecoCliente(String nomeBanco){
        ArrayList<EnderecoCliente> lista = new ArrayList<EnderecoCliente>();

        SoapObject listarEnderecoClienteSO = new SoapObject(NAMESPACE, LISTA_ENDERECO_CLIENTE);
        listarEnderecoClienteSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarEnderecoClienteSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_ENDERECO_CLIENTE, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    EnderecoCliente enderecoCliente = new EnderecoCliente();

                    enderecoCliente.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    enderecoCliente.setBairro(soapObject.getProperty("bairro").toString());
                    enderecoCliente.setCep(soapObject.getProperty("cep").toString());
                    enderecoCliente.setComplemento(soapObject.getProperty("complemento").toString());
                    enderecoCliente.setEndereco(soapObject.getProperty("endereco").toString());
                    enderecoCliente.setNumero(soapObject.getProperty("numero").toString());
                    enderecoCliente.setUf(soapObject.getProperty("uf").toString());
                    enderecoCliente.setCidade(Long.parseLong(soapObject.getProperty("cidade").toString()));
                    enderecoCliente.setCliente(Long.parseLong(soapObject.getProperty("cliente").toString()));


                    lista.add(enderecoCliente);
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
