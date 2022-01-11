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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cargo;

/**
 * Created by fabio on 29/10/15.
 */
public class CargoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/CargoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/CargoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.america.grupocaravela.com.br";

    private static final String LISTA_CARGO = "listaCargo";



    public ArrayList<Cargo> listarCargo(String nomeBanco){
        ArrayList<Cargo> lista = new ArrayList<Cargo>();

        SoapObject listarCargoSO = new SoapObject(NAMESPACE, LISTA_CARGO);
        listarCargoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarCargoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_CARGO, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Cargo cargo = new Cargo();

                    cargo.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    cargo.setNome(soapObject.getProperty("nome").toString());
                    cargo.setObservacao(soapObject.getProperty("observacao").toString());
                    cargo.setAcessoAndroid(Boolean.parseBoolean(soapObject.getProperty("acessoAndroid").toString()));
                    cargo.setAcessoCategorias(Boolean.parseBoolean(soapObject.getProperty("acessoCategorias").toString()));
                    cargo.setAcessoCidades(Boolean.parseBoolean(soapObject.getProperty("acessoCidades").toString()));
                    cargo.setAcessoClientes(Boolean.parseBoolean(soapObject.getProperty("acessoClientes").toString()));
                    cargo.setAcessoCompras(Boolean.parseBoolean(soapObject.getProperty("acessoCompras").toString()));
                    cargo.setAcessoConfiguracao(Boolean.parseBoolean(soapObject.getProperty("acessoConfiguracao").toString()));
                    cargo.setAcessoContasPagar(Boolean.parseBoolean(soapObject.getProperty("acessoContasPagar").toString()));
                    cargo.setAcessoContasReceber(Boolean.parseBoolean(soapObject.getProperty("acessoContasReceber").toString()));
                    cargo.setAcessoFormaPagamento(Boolean.parseBoolean(soapObject.getProperty("acessoFormaPagamento").toString()));
                    cargo.setAcessoFornecedores(Boolean.parseBoolean(soapObject.getProperty("acessoFornecedores").toString()));
                    cargo.setAcessoProdutos(Boolean.parseBoolean(soapObject.getProperty("acessoProdutos").toString()));
                    cargo.setAcessoRelatorios(Boolean.parseBoolean(soapObject.getProperty("acessoRelatorios").toString()));
                    cargo.setAcessoRotas(Boolean.parseBoolean(soapObject.getProperty("acessoRotas").toString()));
                    cargo.setAcessoUnidade(Boolean.parseBoolean(soapObject.getProperty("acessoUnidade").toString()));
                    cargo.setAcessoUsuarios(Boolean.parseBoolean(soapObject.getProperty("acessoUsuarios").toString()));
                    cargo.setAcessoVendas(Boolean.parseBoolean(soapObject.getProperty("acessoVendas").toString()));

                    lista.add(cargo);
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
