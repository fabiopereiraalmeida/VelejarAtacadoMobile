package br.com.grupocaravela.velejar.atacadomobile.dao;

import android.util.Base64;
import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

import br.com.grupocaravela.velejar.atacadomobile.Configuracao.ConfiguracaoServidor;
import br.com.grupocaravela.velejar.atacadomobile.Util.Configuracao;
import br.com.grupocaravela.velejar.atacadomobile.objeto.Cliente;

/**
 * Created by fabio on 29/10/15.
 */
public class ClienteDAO {

    private static final String URL = "http://" + ConfiguracaoServidor.getIpServidor() + ":" + ConfiguracaoServidor.getPortaTomCat() + "/atacadoMobile/services/ClienteDAO?wsdl";
    //private static final String URL = "http://192.168.0.10:8080/atacadoMobile/services/ClienteDAO?wsdl";
    private static final String NAMESPACE = "http://dao.velejar.grupocaravela.com.br";

    private static final String LISTA_CLIENTE = "listaCliente";
    private static final String INSERIR_CLIENTE = "inserirCliente";
    private static final String BUSCAR_ULTIMO_ID = "buscaUltimoIdCliente";

    private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    private SimpleDateFormat dateformatSoap = new SimpleDateFormat("yyyy-MM-dd");

    public ArrayList<Cliente> listarCliente(int id, String nomeBanco){
        ArrayList<Cliente> lista = new ArrayList<>();

        SoapObject listarClienteSO = new SoapObject(NAMESPACE, LISTA_CLIENTE);
        listarClienteSO.addProperty("idEmpresa", id);
        listarClienteSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(listarClienteSO);
        //envelopeSO.setAddAdornments(false); //teste
        envelopeSO.implicitTypes = true; //Sem isso da erro

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + LISTA_CLIENTE, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {
                //for (SoapObject soapObject : resposta){
                for (int i = 0; i < propsNum; i++) {

                    SoapObject soapObject = (SoapObject) resposta.getProperty(i);
                    Cliente cliente = new Cliente();

                    cliente.setId(Long.parseLong(soapObject.getProperty("id").toString()));

                    try {
                        cliente.setRazaoSocial(soapObject.getProperty("razaoSocial").toString());
                    }catch (Exception e){
                        cliente.setRazaoSocial(null);
                    }

                    try {
                        cliente.setFantasia(soapObject.getProperty("fantasia").toString());
                    }catch (Exception e){
                        cliente.setFantasia(null);
                    }

                    try {
                        cliente.setInscricaoEstadual(soapObject.getProperty("inscricaoEstadual").toString());
                    }catch (Exception e){
                        cliente.setFantasia(null);
                    }

                    try {
                        cliente.setCpf(soapObject.getProperty("cpf").toString());
                    }catch (Exception e){
                        cliente.setCpf(null);
                    }

                    try {
                        cliente.setCnpj(soapObject.getProperty("cnpj").toString());
                    }catch (Exception e){
                        cliente.setCnpj(null);
                    }

                    try {
                        //cliente.setDataNascimento((Date) soapObject.getProperty("dataNascimento"));
                        cliente.setDataNascimento(soapObject.getProperty("dataNascimento").toString());
                    }catch (Exception e){
                        cliente.setDataNascimento(null);
                    }

                    try {
                        //cliente.setDataCadastro((Date) soapObject.getProperty("dataCadastro"));
                        cliente.setDataCadastro(soapObject.getProperty("dataCadastro").toString());
                    }catch (Exception e){
                        cliente.setDataCadastro(null);
                    }

                    try {
                        cliente.setEmail(soapObject.getProperty("email").toString());
                    }catch (Exception e){
                        cliente.setEmail(null);
                    }

                    try {
                        cliente.setLimiteCredito(Double.parseDouble(soapObject.getProperty("limiteCredito").toString()));
                    }catch (Exception e){
                        cliente.setLimiteCredito(null);
                    }

                    try {
                        if (soapObject.getProperty("ativo").toString().equals("true")){
                            cliente.setAtivo(true);
                        }else{
                            cliente.setAtivo(false);
                        }
                    }catch (Exception e){
                        cliente.setAtivo(true);
                    }

                    try {
                        cliente.setObservacao(soapObject.getProperty("observacao").toString());
                    }catch (Exception e){
                        cliente.setObservacao(null);
                    }

                    try {
                        cliente.setTelefone1(soapObject.getProperty("telefone1").toString());
                    }catch (Exception e){
                        cliente.setTelefone1(null);
                    }

                    try {
                        cliente.setTelefone2(soapObject.getProperty("telefone2").toString());
                    }catch (Exception e){
                        cliente.setTelefone2(null);
                    }

                    try {
                        cliente.setEndereco(soapObject.getProperty("endereco").toString());
                    } catch (Exception e) {
                        cliente.setEmpresa(null);
                    }
                    try {
                        cliente.setEnderecoNumero(soapObject.getProperty("enderecoNumero").toString());
                    } catch (Exception e) {
                        cliente.setEnderecoNumero(null);
                    }

                    try {
                        cliente.setComplemento(soapObject.getProperty("complemento").toString());
                    } catch (Exception e) {
                        cliente.setComplemento(null);
                    }

                    try {
                        cliente.setBairro(soapObject.getProperty("bairro").toString());
                    } catch (Exception e) {
                        cliente.setBairro(null);
                    }

                    //cliente.setCidade_id(Long.parseLong(soapObject.getProperty("cidadeId").toString()));
                    try {
                        cliente.setCidade_id(Long.parseLong(soapObject.getProperty("cidadeId").toString()));
                    } catch (Exception e) {
                        cliente.setCidade(null);
                    }

                    try {
                        cliente.setEstado_id(Long.parseLong(soapObject.getProperty("estadoId").toString()));
                    } catch (Exception e) {
                        cliente.setEstado_id(null);
                    }

                    try {
                        cliente.setCep(soapObject.getProperty("cep").toString());
                    } catch (Exception e) {
                        cliente.setCep(null);
                    }

                    try {
                        cliente.setRota(Long.parseLong(soapObject.getProperty("rota").toString()));
                    }catch (Exception e){
                        cliente.setRota(null);
                    }

                    try {
                        cliente.setEmpresa(Long.parseLong(soapObject.getProperty("empresa").toString()));
                    }catch (Exception e){
                        cliente.setRota(null);
                    }

                    try {
                        cliente.setNovo(false);
                    }catch (Exception e){
                        cliente.setNovo(false);
                    }

                    try {
                        cliente.setAlterado(false);
                    }catch (Exception e){
                        cliente.setAlterado(false);
                    }

                    try {
                        cliente.setFormaPagamento(Long.parseLong(soapObject.getProperty("formaPagamento").toString()));
                        Log.d("IMPORTANTE", "Forma pagamento : " + Long.parseLong(soapObject.getProperty("formaPagamento").toString()));
                    }catch (Exception e){
                        cliente.setFormaPagamento(null);
                    }

                    try {
                        cliente.setCategoriaCliente(Long.parseLong(soapObject.getProperty("categoriaCliente").toString()));
                        Log.d("IMPORTANTE", "Categoria cliente : " + Long.parseLong(soapObject.getProperty("categoriaCliente").toString()));
                    }catch (Exception e){
                        cliente.setCategoriaCliente(null);
                    }

                    try {
                        String img = soapObject.getProperty("imagem").toString();
                        byte[] bt = Base64.decode(img, Base64.DEFAULT);
                        //byte[] bt = img.getBytes();
                        cliente.setImagem(bt);

                        Log.i("IMAGEM", "IMAGEM IMPORTADA DO CLIENTE: " + cliente.getRazaoSocial());

                    }catch (Exception e){
                        Log.i("IMAGEM", "ERRO AO IMPORTAR IMAGEM: " + e);
                        cliente
                                .setImagem(null);
                    }

                    try {
                        cliente.setLocalizacao(soapObject.getProperty("localizacao").toString());
                    }catch (Exception e){
                        cliente.setLocalizacao(null);
                    }

                    lista.add(cliente);
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

    public boolean inserirCliente(Cliente cliente, String nomeBanco){

        SoapObject inserirClienteSO = new SoapObject(NAMESPACE, INSERIR_CLIENTE);

        SoapObject clienteSO = new SoapObject(NAMESPACE, "cliente");

        Log.d("AMERICA", "PASSEI AKI 07");

        //clienteSO.addProperty("id", cliente.getId().toString());
        //Log.d("AMERICA", "ID: " + cliente.getId().toString());

        //clienteSO.addProperty("novo", cliente.getNovo().toString());
        //Log.d("AMERICA", "NOVO: " + cliente.getNovo().toString());

        clienteSO.addProperty("razaoSocial", cliente.getRazaoSocial().toString());
        Log.d("AMERICA", "RAZAO SOCIAL: " + cliente.getRazaoSocial().toString());
        //clienteSO.addProperty("razaoSocial", "razao");
        clienteSO.addProperty("fantasia", cliente.getFantasia().toString());
        Log.d("AMERICA", "FANTASIA: " + cliente.getFantasia().toString());
        //clienteSO.addProperty("fantasia", "fantasia");
        clienteSO.addProperty("cpf", cliente.getCpf().toString());
        Log.d("AMERICA", "CPF: " + cliente.getCpf().toString());
        //clienteSO.addProperty("cpf", "5454554445");
        clienteSO.addProperty("cnpj", cliente.getCnpj().toString());
        Log.d("AMERICA", "CNPJ: " + cliente.getCnpj().toString());
        //clienteSO.addProperty("cnpj", "684864864");
        clienteSO.addProperty("inscricaoEstadual", cliente.getInscricaoEstadual().toString());
        Log.d("AMERICA", "INSCRICAO ESTADUAL: " + cliente.getInscricaoEstadual().toString());
        //clienteSO.addProperty("inscricaoEstadual", "8644684684");
        clienteSO.addProperty("dataCadastro", cliente.getDataCadastro().toString());
        //clienteSO.addProperty("dataCadastro", "2017-10-10");
        Log.d("AMERICA", "DATA CADASTRO: " + cliente.getDataCadastro().toString());
        Log.d("AMERICA", "PASSEI AKI 08");
        clienteSO.addProperty("email", cliente.getEmail().toString());
        Log.d("AMERICA", "EMAIL: " + cliente.getEmail().toString());
        //clienteSO.addProperty("email", "email@gmail.com");
        clienteSO.addProperty("limiteCredito", "0");
        clienteSO.addProperty("ativo", cliente.getAtivo().toString());
        Log.d("AMERICA", "ATIVO: " + cliente.getAtivo().toString());
        clienteSO.addProperty("observacao", cliente.getObservacao().toString());
        Log.d("AMERICA", "OBSERVACAO: " + cliente.getObservacao().toString());
        //clienteSO.addProperty("observacao", "observacao");
        Log.d("AMERICA", "PASSEI AKI 09");
        clienteSO.addProperty("rota", cliente.getRota().toString());
        Log.d("AMERICA", "ROTA: " + cliente.getRota().toString());
        //clienteSO.addProperty("rota", "1");
        clienteSO.addProperty("empresa", cliente.getEmpresa().toString());
        Log.d("AMERICA", "EMPRESA: " + cliente.getEmpresa().toString());
        //clienteSO.addProperty("empresa", "1");
        clienteSO.addProperty("bairro", cliente.getBairro().toString());
        Log.d("AMERICA", "BAIRRO: " + cliente.getBairro().toString());
        //clienteSO.addProperty("bairro", "centro");
        clienteSO.addProperty("cep", cliente.getCep().toString());
        Log.d("AMERICA", "CEP: " + cliente.getCep().toString());
        //clienteSO.addProperty("cep", "46300000");
        clienteSO.addProperty("cidade", cliente.getCidade().toString());
        Log.d("AMERICA", "CIDADE: " + cliente.getCidade().toString());
        //clienteSO.addProperty("cidade", "cidade");
        clienteSO.addProperty("complemento", cliente.getComplemento().toString());
        Log.d("AMERICA", "COMPLEMENTO: " + cliente.getComplemento());
        //clienteSO.addProperty("complemento", "comlemento");
        clienteSO.addProperty("enderecoNumero", cliente.getEnderecoNumero().toString());
        //clienteSO.addProperty("enderecoNumero", "sn");
        clienteSO.addProperty("endereco", cliente.getEndereco().toString());
        Log.d("AMERICA", "ENDEREÇO: " + cliente.getEndereco().toString());
        //clienteSO.addProperty("endereco", "rua x");
        clienteSO.addProperty("uf", cliente.getUf());
        Log.d("AMERICA", "UF: " + cliente.getUf());
        //clienteSO.addProperty("uf", "ba");

        clienteSO.addProperty("telefone1", cliente.getTelefone1().toString());
        Log.d("AMERICA", "TELEFONE 1: " + cliente.getTelefone1().toString());
        //clienteSO.addProperty("telefone1", "77981000781");
        clienteSO.addProperty("telefone2", cliente.getTelefone2().toString());
        Log.d("AMERICA", "TELEFONE 2: " + cliente.getTelefone2().toString());
        //clienteSO.addProperty("telefone2", "77981202558");

        Log.d("AMERICA", "PASSEI AKI 001");

        //clienteSO.addProperty("dataCriacao", formatSoap.format(cliente.getDataCriacao()));
        //clienteSO.addProperty("colaboradorCriacao", cliente.getColaboradorCriacao());
        //clienteSO.addProperty("dataAlteracao", formatSoap.format(cliente.getDataAlteracao()));
        //clienteSO.addProperty("colaboradorAlteracao", cliente.getColaboradorAlteracao());
        //clienteSO.addProperty("representante", cliente.getRepresentante());

        inserirClienteSO.addSoapObject(clienteSO);
        inserirClienteSO.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        Log.d("AMERICA", "PASSEI AKI 002");

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11
        envelopeSO.setOutputSoapObject(inserirClienteSO);
        envelopeSO.implicitTypes = true; //Sem isso da erro

        Log.d("AMERICA", "PASSEI AKI 003");

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + INSERIR_CLIENTE, envelopeSO); //Realiza o envio da mensagem

            SoapPrimitive resposta = (SoapPrimitive) envelopeSO.getResponse(); //Pega a mensagem de resposta

            Log.d("AMERICA", "PASSEI AKI 004");

            return Boolean.parseBoolean(resposta.toString()); //Passa a resposta "true ou false" em String para o tipo boolean

        } catch (IOException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "ERRO: " + e);
            return false;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "ERRO: " + e);
            return false;
        }
    }

    private java.util.Date dataAtual() {

        java.util.Date hoje = new java.util.Date();
        // java.util.Date hoje = Calendar.getInstance().getTime();
        return hoje;
    }

    public Long buscarUltimoIdClienteSalvo(String nomeBanco){

        Long retorno = null;

        SoapObject buscarUltimoIdVendaCabecalho = new SoapObject(NAMESPACE, BUSCAR_ULTIMO_ID);
        buscarUltimoIdVendaCabecalho.addProperty("nomeBanco", "cnpj" + Configuracao.getCnpj());

        SoapSerializationEnvelope envelopeSO = new SoapSerializationEnvelope(SoapEnvelope.VER11); //Na aula usou a versão VER11

        envelopeSO.setOutputSoapObject(buscarUltimoIdVendaCabecalho);

        HttpTransportSE httpTransportSE = new HttpTransportSE(URL);

        try {

            httpTransportSE.call("urn:" + BUSCAR_ULTIMO_ID, envelopeSO); //Realiza o envio da mensagem

            SoapObject resposta = (SoapObject) envelopeSO.bodyIn; //Pega a mensagem de resposta

            int propsNum = resposta.getPropertyCount();

            if (resposta != null) {

                for (int i = 0; i < propsNum; i++) {
                    retorno = Long.parseLong(resposta.getProperty(i).toString());

                }
            }

        }catch (IOException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "erro: " + e);
            return null;
        } catch (XmlPullParserException e) {
            e.printStackTrace();
            Log.d("CaculeCompreFacil", "erro: " + e);
            return null;
        }

        return retorno;
    }
}
