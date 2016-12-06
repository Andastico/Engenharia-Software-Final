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

public class actEditTelefone extends AppCompatActivity {

    private EditText telefone;
    private EditText tipo;
    private Button remover;
    private String selecionado;
    private ListView listaTelefones;
    private ArrayList<String> listItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_edit_telefone);

        /*Linka as variaveis com suas views no layout*/
        telefone = (EditText) findViewById(R.id.edt_editTelNumero);
        tipo     = (EditText) findViewById(R.id.edt_editTelTipo);

        listaTelefones = (ListView) findViewById(R.id.lst_telefones);

        remover  = (Button) findViewById(R.id.btn_editTelRemover);

        listItems = getIntent().getStringArrayListExtra("telefones");
        addLista();
        visibilidadeBotao();

        /*Define um metodo Listener que é ativado quando um item da lista é clickado*/
        listaTelefones.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                selecionado = (String) (listaTelefones.getItemAtPosition(myItemInt));
            }
        });
    }

    /**
     * Metodo para evitar que o botão remover continue visivel mesmo quando não existe
     * um item na lista de telefones
     */
    private void visibilidadeBotao()
    {
        remover.setVisibility(listaTelefones.getCount() != 0 ? View.VISIBLE : View.INVISIBLE);
    }


    /**
     * Adiciona o conteudo da arrayList ListItems ao listView listaTelefones
     */
    private void addLista() {
        try {
            if (!listItems.isEmpty() && listItems != null)
            {
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
                listaTelefones.setAdapter(arrayAdapter);
                visibilidadeBotao();
            }
        } catch (Exception e) {
            mostrarToast("Erro " + e.getMessage());
        }
    }


    /**
     * Metodo para simplificar os avisos de tela
     * @param mensagem
     * Mensagem a ser exibida via toast
     */
    private void mostrarToast(String mensagem)
    {
        Toast.makeText(actEditTelefone.this, mensagem, Toast.LENGTH_LONG).show();
    }


    /**
     * Verifica se o formulario esta correto e como deve ser preenchido
     * @return
     * retorna true se tudo estiver de acordo
     * retorna false caso os dados estejam incorretos e mostra uma mensagem indicando o que esta errado
     */
    private boolean checarFormulario()
    {
        if(telefone.getText().toString().length() == 8)
        {
            mostrarToast("Insira um telefone valido");
            return false;
        }
        if(tipo.getText().toString().length() == 0)
        {

            mostrarToast("Insira um tipo valido");
            return false;
        }
        else return true;
    }

    /**
     * Função que retira todos os textos das editTexts desta tela
     */
    private void limpaTela()
    {
        telefone.setText("");
        tipo.setText("");
    }


    /**
     * Metodo onClick do botão btn_salvar
     * @param v
     * parametro obrigatorio, indica que a ação esta sendo acionada da View
     */
    public void salvarClicked(View v)
    {
        try
        {
            if(checarFormulario())
            {
                String add = "";
                add += telefone.getText().toString().replace(":","") + ":";
                add += tipo.getText().toString().replace(":","");
                listItems.add(add);
                addLista();
                limpaTela();
            }
        }
        catch (Exception ex)
        {
            mostrarToast("Erro " + ex);
        }
    }

    public void retornarClicked(View v)
    {
        Intent it =  new Intent();
        it.putStringArrayListExtra("array",listItems);
        setResult(Activity.RESULT_OK,it);
        finish();
    }
    public void removerClicked(View v)
    {
        try
        {
            ArrayAdapter<String> ad = (ArrayAdapter<String>) listaTelefones.getAdapter();
            ad.remove(selecionado);
            listaTelefones.setAdapter(ad);
            visibilidadeBotao();
        }
        catch(Exception e)
        {
            mostrarToast("Erro "+e.getMessage());
        }
    }
}
