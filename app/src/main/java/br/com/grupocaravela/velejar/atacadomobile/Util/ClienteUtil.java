package br.com.grupocaravela.velejar.atacadomobile.Util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;

import br.com.grupocaravela.velejar.atacadomobile.bancoDados.DBHelper;
import br.com.grupocaravela.velejar.atacadomobile.objeto.ContaReceber;

/**
 * Created by fabio on 01/02/17.
 */

public class ClienteUtil {

    private Cursor cursor;
    private SQLiteDatabase db;
    private DBHelper dbHelper;
    private ContentValues contentValues;

    private SimpleDateFormat formatData = new SimpleDateFormat("dd/MM/yyyy");

    public ClienteUtil(Context c) {
        dbHelper = new DBHelper(c, "velejar.db", 1); // Banco
        db = dbHelper.getWritableDatabase(); // Banco
        contentValues = new ContentValues(); // banco
    }

    public boolean pendencia(String idCliente){

        boolean retorno = false;

       cursor = db
               .rawQuery(
                       "SELECT _id, valor_devido, vencimento, cliente_id, venda_cabecalho_id " +
                               "FROM conta_receber where cliente_id like '" + idCliente + "'", null);

       if (cursor.moveToFirst()) {

           //int tamanhoListaContaReceber = cursor.getCount();

           for (int i = 0; i < cursor.getCount(); i++) {

               //ContaReceber c = new ContaReceber();

               try {
                   Date dataVencimento = formatData.parse(cursor.getString(2));

                   if (dataVencimento.before(new Date())){
                       retorno = true;
                   }

               }catch (Exception e){
                    retorno = false;
               }
           }
       }

       return retorno;
   }

}
