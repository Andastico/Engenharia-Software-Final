package com.example.vitalate.engenhariasoftware;

/**
 * Created by Admin on 11/11/2016.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class actConsultaBlacklist extends AppCompatActivity {

	/**
	 * ListView onde é mostrado todos os clientes da blacklist. é linkada com a view atraves do metodo onCreate.
	 */
	private ListView listaClientesBlc;
	private List<Cliente> clientesBlc;
	private EditText consultaBlc;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_consulta_blacklist);


		consultaBlc = (EditText) findViewById(R.id.edt_conBlacklist);
		listaClientesBlc = (ListView) findViewById(R.id.lst_conBlacklist);
		clientesBlc = new ArrayList<>();

		consultaBlc.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

			}

			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
				exibirClientes();
			}

			@Override
			public void afterTextChanged(Editable editable) {

			}
		});

		/* dispara o metodo detalhar cliente quando um item do listView é clicado!*/
		listaClientesBlc.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
			{
				String textoSelecionado = (String) (listaClientesBlc.getItemAtPosition(myItemInt));
				String aux[] = textoSelecionado.split(" : ");

				detalharCliente(clientesBlc.get(myItemInt).getId());
			}
		});

		exibirClientes();

	}

    private void detalharCliente(int idCliente)
    {
        Intent it = new Intent(this,actDetalhesCliente.class);
        it.putExtra("idCliente", idCliente);
        startActivityForResult(it,0);
    }



	/**
	 * Verifica se existe algum cliente, caso exista ele mostrara na list view com o formato "id : nome"
	 */
	private void exibirClientes()
	{
		listaClientesBlc.setVisibility(View.VISIBLE);
		DbHelper db = new DbHelper(this);
		if(consultaBlc.getText().toString().isEmpty())
			clientesBlc = db.selectBlacklist();
		else
			clientesBlc = db.consultaBlacklist(consultaBlc.getText().toString());


		if(clientesBlc == null||clientesBlc.isEmpty()  )
		{
			listaClientesBlc.setVisibility(View.INVISIBLE);
			mostrarToast("Nenhum cliente encontrado na blacklist");
		}
		else
		{
			ArrayList<String> cliString = new ArrayList<>();
			for (Cliente c : clientesBlc)
				cliString.add(c.getNome());

			listaClientesBlc.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cliString));
		}
	}

	/**
	 * Mostra uma mensagem ao usuario
	 * @param mensagem
	 * mensagem que sera exibida
	 */
	private void mostrarToast(String mensagem)
	{
		Toast.makeText(actConsultaBlacklist.this, mensagem, Toast.LENGTH_LONG).show();
	}

	/**
	 * metodo para exibir todas as informações do cliente em uma nova activity.
	 * //@param idCliente
	 * id do cliente que sera consultado
	 */
    /*private void detalharCliente(int idCliente)
    {
        Intent it = new Intent(this,actDetalhesCliente.class);
        it.putExtra("idCliente", idCliente);
        startActivityForResult(it,0);
    }*/

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		exibirClientes();
	}

}