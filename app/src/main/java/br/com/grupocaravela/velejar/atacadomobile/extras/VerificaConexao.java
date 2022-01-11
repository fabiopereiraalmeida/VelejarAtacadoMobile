package br.com.grupocaravela.velejar.atacadomobile.extras;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

/**
 * Created by fabio on 25/07/15.
 */
public class VerificaConexao {
    public static boolean verifyConnection(Context context){
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()){
            return true;
        } else {
            return false;
        }
    }
}
