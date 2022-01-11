package br.com.grupocaravela.velejar.atacadomobile.dao;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Empresa;

/**
 * Created by fabio on 29/10/15.
 */
public class EmpresaDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/EmpresaDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/EmpresaDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String BUSCA_EMPRESA = "buscaEmpresa";


    public Empresa buscaEmpresa(int id, String nomeBanco){
        Empresa empresa = new Empresa();

        SoapObject buscarEmpresa = new SoapObject(NAMESPACE, BUSCA_EMPRESA);
        buscarEmpresa.addProperty("idEmpresa", id);
        buscarEmpresa.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(buscarEmpresa);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + BUSCA_EMPRESA, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    //Empresa empresa = new Empresa();

                    empresa.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    try {
                        empresa.setRazaoSocial(soapObject.getProperty("razaoSocial").toString());
                    }catch (Exception e){
                        empresa.setRazaoSocial(null);
                    }

                    try {
                        empresa.setFantasia(soapObject.getProperty("fantasia").toString());
                    }catch (Exception e){
                        empresa.setFantasia(null);
                    }

                    try {
                        empresa.setCnpj(soapObject.getProperty("cnpj").toString());
                    }catch (Exception e){
                        empresa.setCnpj(null);
                    }

                    try {
                        empresa.setInscricaoEstadual(soapObject.getProperty("inscricaoEstadual").toString());
                    }catch (Exception e){
                        empresa.setInscricaoEstadual(null);
                    }

                    try {
                        empresa.setTelefone1(soapObject.getProperty("telefone1").toString());
                    }catch (Exception e){
                        empresa.setTelefone1(null);
                    }

                    try {
                        empresa.setTelefone2(soapObject.getProperty("telefone2").toString());
                    }catch (Exception e){
                        empresa.setTelefone2(null);
                    }

                    try {
                        empresa.setEndereco(soapObject.getProperty("endereco").toString());
                    }catch (Exception e){
                        empresa.setEndereco(null);
                    }

                    try {
                        empresa.setEnderecoNumero(soapObject.getProperty("enderecoNumero").toString());
                    }catch (Exception e){
                        empresa.setEnderecoNumero(null);
                    }

                    try {
                        empresa.setBairro(soapObject.getProperty("bairro").toString());
                    }catch (Exception e){
                        empresa.setBairro(null);
                    }

                    try {
                        empresa.setCidade(Long.parseLong(soapObject.getProperty("cidade").toString()));
                    }catch (Exception e){
                        empresa.setCidade(null);
                    }

                    try {
                        empresa.setEstado(Long.parseLong(soapObject.getProperty("estado").toString()));
                    }catch (Exception e){
                        empresa.setEstado(null);
                    }

                    try {
                        empresa.setEmail(soapObject.getProperty("email").toString());
                    }catch (Exception e){
                        empresa.setEmail(null);
                    }

                    try {
                        empresa.setComplemento(soapObject.getProperty("complemento").toString());
                    }catch (Exception e){
                        empresa.setComplemento(null);
                    }

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            return null;
        }

        return empresa;
    }

}
