package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Produto;

/**
 * Created by fabio on 29/10/15.
 */
public class ProdutoDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/ProdutoDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/ProdutoDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_PRODUTO = "listaProduto";

    public ArrayList<Produto> listarProduto(int id, String nomeBanco){
        ArrayList<Produto> lista = new ArrayList<>();

        SoapObject listarProdutoSO = new SoapObject(NAMESPACE, LISTA_PRODUTO);
        listarProdutoSO.addProperty("idEmpresa", id);
        listarProdutoSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarProdutoSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_PRODUTO, envelopeSO); //Realiza o envio da mensagem
            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta
            //SoapObject resposta = (SoapObject) envelopeSO.getResponse(); //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Produto produto = new Produto();

                    produto.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    produto.setCodigo(soapObject.getProperty("codigo").toString());
                    Log.i("CODIGO", soapObject.getProperty("codigo").toString());
                    /*
                    try {
                        produto.setCodigo(soapObject.getProperty("codigo").toString());
                        Log.i("CODIGO", soapObject.getProperty("codigo").toString());
                    }catch (Exception e){
                        produto.setCodigo(null);
                        Log.i("CODIGO", "NULO");
                    }
*/
                    produto.setNome(soapObject.getProperty("nome").toString());
                    /*
                    try {
                        produto.setNome(soapObject.getProperty("nome").toString());
                    }catch (Exception e){
                        produto.setNome(null);
                    }
*/
                    try {
                        produto.setEstoque(Double.parseDouble(soapObject.getProperty("estoque").toString()));
                    }catch (Exception e){
                        produto.setEstoque(0.0);
                    }

                    try {
                        produto.setExpositor(Double.parseDouble(soapObject.getProperty("expositor").toString()));
                    }catch (Exception e){
                        produto.setEstoque(0.0);
                    }

                    try {
                        produto.setValorDesejavelVenda(Double.parseDouble(soapObject.getProperty("valorDesejavelVenda").toString()));
                    }catch (Exception e) {
                        produto.setValorDesejavelVenda(0.0);
                    }

                    try {
                        produto.setValorMinimoVenda(Double.parseDouble(soapObject.getProperty("valorMinimoVenda").toString()));
                    }catch (Exception e){
                        produto.setValorMinimoVenda(0.0);
                    }

                    try {
                        produto.setCategoria(Long.parseLong(soapObject.getProperty("categoria").toString()));
                    }catch (Exception e){
                        produto.setCategoria(null);
                    }

                    try {
                        produto.setUnidade(Long.parseLong(soapObject.getProperty("unidade").toString()));
                    }catch (Exception e){
                        produto.setUnidade(null);
                    }

                    try {
                        produto.setAtivo(Boolean.parseBoolean(soapObject.getProperty("ativo").toString()));
                    }catch (Exception e){
                        produto.setAtivo(false);
                    }

                    try {
                        produto.setPeso(Double.parseDouble(soapObject.getProperty("peso").toString()));
                    }catch (Exception e){
                        produto.setPeso(0.0);
                    }

                    try {
                        produto.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        produto.setEmpresa(null);
                    }

                    try {

                        String img = soapObject.getProperty("imagem").toString();
                        byte[] bt = Base64.decode(img, Base64.DEFAULT);
                        //byte[] bt = img.getBytes();
                        produto.setImagem(bt);

                    }catch (Exception e){
                        Log.i("IMAGEM", "ERRO AO IMPORTAR IMAGEM: " + e);
                        produto.setImagem(null);
                    }

                    try {
                        produto.setCodigo_ref(soapObject.getProperty("codigo_ref").toString());
                    }catch (Exception e){
                        produto.setCodigo_ref(null);
                    }

                    lista.add(produto);
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
