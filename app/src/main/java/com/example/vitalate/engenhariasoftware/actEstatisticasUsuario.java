package com.example.vitalate.engenhariasoftware;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import dao.DbHelper;

public class actEstatisticasUsuario extends AppCompatActivity {

    TextView labelEstatisticas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_estatisticas_usuario);
        labelEstatisticas = (TextView) findViewById(R.id.txt_estUsuEstatisticas);
        getEstatisticas();
    }

    private void getEstatisticas()
    {
        DbHelper db = new DbHelper(this);

        for(String s : db.estatisticasUsuario())
            labelEstatisticas.setText(labelEstatisticas.getText() + s + "\n\n");
    }
}
