package model;

import android.app.Application;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

/**
 * Created by vitalate on 30/09/16.
 */
public class Aplicacao extends AppCompatActivity
{
    private ListView lista;

    public ListView getLista() {
        return lista;
    }

    public void setLista(ListView lista) {
        this.lista = lista;
    }
}
