package br.com.grupocaravela.velejar.atacadomobile.Util;

/**
 * Created by fabio on 21/12/15.
 */
public class Configuracao {

    private static String email;
    private static String senha;
    private static String cnpj;

    private static Long cidade;

    private static Boolean semEstoque;

    private static Boolean produtoSemEstoque;
    private static Boolean visualizarCards;

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        Configuracao.email = email;
    }

    public static String getSenha() {
        return senha;
    }

    public static void setSenha(String senha) {
        Configuracao.senha = senha;
    }

    public static String getCnpj() {
        return cnpj;
    }

    public static void setCnpj(String cnpj) {
        Configuracao.cnpj = cnpj;
    }

    public static Boolean getProdutoSemEstoque() {

        if (produtoSemEstoque == null){
            return false;
        }else{
            return produtoSemEstoque;
        }

    }

    public static void setProdutoSemEstoque(Boolean produtoSemEstoque) {
        Configuracao.produtoSemEstoque = produtoSemEstoque;
    }

    public static Boolean getVisualizarCards() {
        if (visualizarCards == null){
            return false;
        }else{
            return visualizarCards;
        }
    }

    public static void setVisualizarCards(Boolean visualizarCards) {
        Configuracao.visualizarCards = visualizarCards;
    }

    public static Boolean getSemEstoque() {
        if (semEstoque == null){
            return false;
        }else{
            return semEstoque;
        }
    }

    public static void setSemEstoque(Boolean semEstoque) {
        Configuracao.semEstoque = semEstoque;
    }
}
