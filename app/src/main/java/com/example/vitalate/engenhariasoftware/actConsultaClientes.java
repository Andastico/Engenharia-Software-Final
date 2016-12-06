package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dao.DbHelper;
import model.Cliente;

public class actConsultaClientes extends AppCompatActivity {

	/**
	 * ListView onde é mostrado todos os clientes. é linkada com a view atraves do metodo onCreate.
	 */
	private ListView listaClientes;
	private List<Cliente> clientes;
	private EditText consulta;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_consulta_clientes);

		consulta = (EditText) findViewById(R.id.edt_consulta);
		listaClientes = (ListView) findViewById(R.id.lst_conCliClientes);

		consulta.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
			{
				exibirClientes();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		/* dispara o metodo detalhar cliente quando um item do listView é clicado!*/
		listaClientes.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
			{
				String textoSelecionado = (String) (listaClientes.getItemAtPosition(myItemInt));
				String aux[] = textoSelecionado.split(" : ");

				detalharCliente(clientes.get(myItemInt).getId());
			}
		});

		exibirClientes();

    }

	/**
	 * Verifica se existe algum cliente, caso exista ele mostrara na list view com o formato "id : nome"
	 */
	private void exibirClientes()
	{
		listaClientes.setVisibility(View.VISIBLE);
		DbHelper db = new DbHelper(this);
		if(consulta.getText().toString().isEmpty())
			clientes = db.selectClientes();
		else
			clientes = db.consultaClientes(consulta.getText().toString());

		if(clientes == null || clientes.isEmpty())
		{
			listaClientes.setVisibility(View.INVISIBLE);
			mostrarToast("Nenhum cliente encontrado");
		}
		else
		{
			ArrayList<String> cliString = new ArrayList<>();
			for (Cliente c : clientes)
				cliString.add(c.getNome());

			listaClientes.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cliString));
		}
	}

	/**
	 * Mostra uma mensagem ao usuario
	 * @param mensagem
	 * mensagem que sera exibida
	 */
    private void mostrarToast(String mensagem)
    {
        Toast.makeText(actConsultaClientes.this, mensagem, Toast.LENGTH_LONG).show();
    }

	/**
	 * metodo para exibir todas as informações do cliente em uma nova activity.
	 * @param idCliente
	 * id do cliente que sera consultado
	 */
	private void detalharCliente(int idCliente)
	{
		Intent it = new Intent(this,actDetalhesCliente.class);
		it.putExtra("idCliente", idCliente);
		startActivityForResult(it,0);
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		exibirClientes();
	}
}
