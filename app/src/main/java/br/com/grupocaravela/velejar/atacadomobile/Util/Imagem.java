package br.com.grupocaravela.velejar.atacadomobile.Util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.StrictMode;
import android.util.Log;

//import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by fabio on 26/01/16.
 */
public class Imagem {
/*
    public static void downloadImagem(Context c, String endereco, String nome){

        File fileWithinMyDir = c.getFilesDir();
        try
        {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                    .permitAll().build();
            StrictMode.setThreadPolicy(policy);
            URL url = new URL(endereco);
            //File file = new File( fileWithinMyDir.getAbsolutePath()  + "/" + nome +".png");
            File file = new File( fileWithinMyDir.getAbsolutePath()  + "/" + nome);
            URLConnection ucon = url.openConnection();
            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);

            Bitmap bm = null;
            bm = BitmapFactory.decodeStream(bis);



            ByteArrayBuffer baf = new ByteArrayBuffer(50);
            int current = 0;
            while ((current = bis.read()) != -1)
            {
                baf.append((byte) current);
            }

            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baf.toByteArray());
            fos.close();
        }
        catch (IOException e)
        {
            Log.e("download", e.getMessage());
        }

    }

    public static Bitmap carregarImagem(Context c, String nome){

        Bitmap retorno = null;

        File fileWithinMyDir = c.getFilesDir();

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Bitmap bitmap = BitmapFactory.decodeFile(fileWithinMyDir.getAbsolutePath() + "/" + nome);

        retorno = bitmap;

        return retorno;
    }
*/
    public static Bitmap carregarImagemBytes(Context c, byte[] image){

        Bitmap retorno = null;

        Log.i("IMAGEM", "ANTES DA IMAGEM");

        if (image!= null){

            retorno = getImage(image);

            Log.i("IMAGEM", "PEGUEI IMAGEM");
        }

        return retorno;
    }

    public static Bitmap getImage(byte[] image) {
        return BitmapFactory.decodeByteArray(image, 0, image.length);
    }

    /*
    public static void saveFile(Context context, Bitmap b, String picName){
        FileOutputStream fos;
        try {
            fos = context.openFileOutput(picName, Context.MODE_PRIVATE);
            b.compress(Bitmap.CompressFormat.PNG, 100, fos);
            fos.close();
        }
        catch (FileNotFoundException e) {
            Log.d("ERRO", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("ERRO", "io exception");
            e.printStackTrace();
        }
    }

    public static Bitmap loadBitmap(Context context, String picName){
        Bitmap b = null;
        FileInputStream fis;
        try {
            fis = context.openFileInput(picName);
            b = BitmapFactory.decodeStream(fis);
            fis.close();

        }
        catch (FileNotFoundException e) {
            Log.d("ERRO", "file not found");
            e.printStackTrace();
        }
        catch (IOException e) {
            Log.d("ERRO", "io exception");
            e.printStackTrace();
        }
        return b;
    }
    */
}
