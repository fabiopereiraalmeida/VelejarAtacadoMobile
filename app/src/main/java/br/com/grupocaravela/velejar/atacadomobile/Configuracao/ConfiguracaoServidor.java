package br.com.grupocaravela.velejar.atacadomobile.Configuracao;

/**
 * Created by fabio on 01/02/17.
 */

public class ConfiguracaoServidor {

    private static String ipServidor = "191.252.2.213";
    //private static String ipServidor = "localhost"; //Teste
    //private static String ipServidor = "192.168.1.2"; //Teste

    private static String portaTomCat = "80";
    //private static String portaTomCat = "8080"; //Teste

    public static String getIpServidor() {
        return ipServidor;
    }

    public static void setIpServidor(String ipServidor) {
        ConfiguracaoServidor.ipServidor = ipServidor;
    }

    public static String getPortaTomCat() {
        return portaTomCat;
    }

    public static void setPortaTomCat(String portaTomCat) {
        ConfiguracaoServidor.portaTomCat = portaTomCat;
    }
}
