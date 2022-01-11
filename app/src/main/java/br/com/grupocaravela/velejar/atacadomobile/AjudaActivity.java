package br.com.grupocaravela.velejar.atacadomobile;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import br.com.grupocaravela.comprefacil.velejaratacado.R;


public class AjudaActivity extends ActionBarActivity {

    private Toolbar mainToolbarTop;
    private Toolbar mainToolbarBottom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ajuda);

        mainToolbarTop = (Toolbar) findViewById(R.id.toolbar_main_top); //Cast para o toolbarTop
        mainToolbarTop.setTitle("Ajuda");
        mainToolbarTop.setTitleTextColor(Color.WHITE);
        mainToolbarTop.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(mainToolbarTop);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
/*
        //############# INICIO ALTERANDO A COR DO STATUSBAR ############
        Window window = this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(this.getResources().getColor(R.color.primary_dark));
        //############# FIM ALTERANDO A COR DO STATUSBAR ############
*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_ajuda, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        /*
        if (id == R.id.action_settings) {
            return true;
        }
*/
        if (id == android.R.id.home){
            finish();
        }

        return true;

        //return super.onOptionsItemSelected(item);
    }
}
