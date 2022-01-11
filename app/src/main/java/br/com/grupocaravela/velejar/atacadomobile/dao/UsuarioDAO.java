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
import br.com.grupocaravela.velejar.atacadomobile.objeto.Usuario;

/**
 * Created by fabio on 29/10/15.
 */
public class UsuarioDAO {

    //private static final String URL = "http://192.168.0.10/atacadoMobile/services/UsuarioDAO?wsdl";
    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/UsuarioDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/UsuarioDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_USUARIO = "listaUsuario";

    public ArrayList<Usuario> listarUsuario(String nomeBanco){
        ArrayList<Usuario> lista = new ArrayList<Usuario>();

        SoapObject listarUsuarioSO = new SoapObject(NAMESPACE, LISTA_USUARIO);
        listarUsuarioSO.addProperty("nomeBanco", "cnpj" + nomeBanco);

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarUsuarioSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_USUARIO, envelopeSO); //Realiza o envio da mensagem

            //Vector<SoapObject> resposta = (Vector<SoapObject>) envelopeSO.getResponse(); //Pega a mensagem de resposta

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);

                    Usuario usuario = new Usuario();

                    usuario.setId(Long.parseLong(soapObject.getProperty("id").toString()));
                    try {
                        usuario.setAtivo(Boolean.valueOf(soapObject.getProperty("ativo").toString()));
                    }catch (Exception e){
                        usuario.setAtivo(false);
                    }

                    try {
                        usuario.setNome(soapObject.getProperty("nome").toString());
                    }catch (Exception e){
                        usuario.setNome(null);
                    }

                    try {
                        usuario.setSenha(soapObject.getProperty("senha").toString());
                    }catch (Exception e){
                        usuario.setSenha(null);
                    }

                    try {
                        usuario.setEmail(soapObject.getProperty("email").toString());
                    }catch (Exception e){
                        usuario.setEmail(null);
                    }

                    try {
                        usuario.setCredito(Double.parseDouble(soapObject.getProperty("credito").toString()));
                    }catch (Exception e){
                        usuario.setCredito(0.0);
                    }

                    try {
                        usuario.setTelefone(soapObject.getProperty("telefone").toString());
                    }catch (Exception e){
                        usuario.setCredito(0.0);
                    }

                    try {
                        usuario.setRota(Long.parseLong(soapObject.getProperty("rota").toString()));
                    }catch (Exception e){
                        usuario.setRota(null);
                    }

                    try {
                        usuario.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        usuario.setRota(null);
                    }

                    lista.add(usuario);
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
