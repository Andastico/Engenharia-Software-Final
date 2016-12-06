package com.example.vitalate.engenhariasoftware;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import model.Aplicacao;

public class actEditEndereco extends AppCompatActivity {
    private ListView listaEnderecos;
    private ArrayList<String> listItems;
    private EditText rua;
    private EditText numero;
    private EditText bairro;
    private EditText complemento;
    private int selected;
    private String textoSelecionado;
    private Button remover;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_edit_endereco);

        rua         = (EditText) findViewById(R.id.edt_rua);
        numero      = (EditText) findViewById(R.id.edt_numero);
        bairro      = (EditText) findViewById(R.id.edt_bairro);
        complemento = (EditText) findViewById(R.id.edt_complemento);

        listaEnderecos = (ListView) findViewById(R.id.lst_enderecos);

        remover = (Button) findViewById(R.id.btn_remover);


        listItems = new ArrayList<>();
        listItems = getIntent().getStringArrayListExtra("enderecos");
        addLista();

        visibilidadeBotao();

        listaEnderecos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                textoSelecionado = (String) (listaEnderecos.getItemAtPosition(myItemInt));
                selected = myItemInt;
            }
        });

    }
    private void mostrarToast(String mensagem)
    {
        Toast.makeText(actEditEndereco.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private boolean verificarFormulario()
    {
        if(rua.getText().toString().length() == 0)
        {
            mostrarToast("Insira um endereço valido!");
            return false;
        }
        else if(numero.getText().toString().length() == 0)
        {
            mostrarToast("Insira um numero valido!");
            return false;
        }
        else
            return true;
    }

    private void limpaTela()
    {
        rua.setText("");
        numero.setText("");
        bairro.setText("");
        complemento.setText("");
    }

    private void visibilidadeBotao()
    {
        remover.setVisibility(listaEnderecos.getCount() != 0? View.VISIBLE : View.INVISIBLE);
    }

    private void addLista()
    {
        try {
            if (!listItems.isEmpty() && listItems != null) {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                listaEnderecos.setAdapter(arrayAdapter);
                visibilidadeBotao();

            }
        }catch(Exception e)
        {
            mostrarToast("Erro em adicionar a lista :" + e.getMessage());
        }
    }




    public void removerClicked(View v)
    {
        try
        {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) listaEnderecos.getAdapter();
            ad.remove(textoSelecionado);
            listaEnderecos.setAdapter(ad);
            visibilidadeBotao();
            /*ArrayAdapter<String> ad = (ArrayAdapter<String>) listaEnderecos.getAdapter();
            ad.remove(ad.getItem(listaEnderecos.getSelectedItemPosition()));
            */
        }
        catch (Exception e)
        {
            mostrarToast("Erro ao remover da Lista :" +e.getMessage());
        }
    }

    public void salvarClicked(View v)
    {
        if(verificarFormulario())
        {
            try
            {
                String add = rua.getText().toString().replace(":", "")
                        + ":" + numero.getText().toString().replace(":", "")
                        + ":" + bairro.getText().toString().replace(":", "")
                        + ":" + complemento.getText().toString().replace(":", "");
                listItems.add(add);
                addLista();
                limpaTela();
            }
            catch (Exception e)
            {
                mostrarToast("Erro ao salvar :" +e.getMessage());
            }
        }
    }

    public void retornarClicked(View v)
    {
        //Aplicacao app = (Aplicacao) getApplicationContext();
        //app.setLista(listaEnderecos);
        Intent it =  new Intent();
        it.putStringArrayListExtra("array",listItems);
        setResult(Activity.RESULT_OK,it);
        finish();
    }
}
