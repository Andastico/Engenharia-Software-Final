package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dao.DbHelper;
import model.Cliente;
import model.Endereco;
import model.Telefone;

public class actDetalhesCliente extends AppCompatActivity
{

	private Cliente cliente;
	private TextView txtNome;
	private TextView txtData;
	private TextView txtSexo;
	private ListView listaEnderecos;
	private ListView listaTelefones;
	private ArrayList<String> enderecosString;
	private ArrayList<String> telefonesString;
	private TextView labelTelefone;
	private TextView labelEndereco;
	private TextView labelEstatisticas;
	private List<Endereco> enderecos;
	private List<Telefone> telefones;
    private Button tirarBlacklist;


	private boolean editMode = false;


	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_detalhes_cliente);

		txtNome = (TextView) findViewById(R.id.txt_nomeCliente);
		txtData = (TextView) findViewById(R.id.txt_aniversarioCliente);
		txtSexo = (TextView) findViewById(R.id.txt_sexoCliente);
		listaEnderecos = (ListView) findViewById(R.id.lst_detCliEndereco);
		listaTelefones = (ListView) findViewById(R.id.lst_detCliTelefones);
		labelTelefone = (TextView) findViewById(R.id.lbl_telefones);
		labelEndereco = (TextView) findViewById(R.id.lbl_enderecos);
		labelEstatisticas = (TextView) findViewById(R.id.lbl_detCliEstatisticas);
        tirarBlacklist = (Button) findViewById(R.id.btn_tirarBlacklist);
		infoClientes(this.getIntent().getExtras().getInt("idCliente"));


		listaTelefones.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
				Telefone t = telefones.get(i);
				Intent chamada = new Intent(Intent.ACTION_DIAL);
				chamada.setData(Uri.parse("tel:" + t.getNumero()));
				startActivity(chamada);
				return true;
			}
		});

	}

    public void getEstatisticas(int id)
    {
        DbHelper db = new DbHelper(this);
        ArrayList<String> estatisticas = db.estatisticasCliente(id);
        labelEstatisticas.setText("");
        for(String s : estatisticas)
            labelEstatisticas.setText(labelEstatisticas.getText() + s + "\n\n");
        db.close();
    }

	public void teste()
	{
		Uri agenda = ContactsContract.Contacts.CONTENT_URI;
		Cursor cursor = getContentResolver().query(agenda,null,null,null,null);
		while(cursor.moveToNext())
		{
			String nome = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME));
            String tel = cursor.getString(cursor.getColumnIndex(ContactsContract.PhoneLookup.NUMBER));
			txtData.setText(txtData.getText() + "\n" + nome);
			Toast.makeText(actDetalhesCliente.this, nome, Toast.LENGTH_SHORT).show();
		}
		cursor.close();
	}


	private void infoClientes(int idCliente)
	{
		DbHelper db = new DbHelper(this);

		cliente = db.detalhesCliente(idCliente);
        if(db.isOnBlackList(idCliente))
            tirarBlacklist.setVisibility(View.VISIBLE);

		enderecos = db.selectEnderecoCliente(idCliente);
		telefones = db.selectTelefoneCliente(idCliente);

		txtNome.setText(cliente.getNome());
		txtData.setText(cliente.getData().replace("-", "/"));
		txtSexo.setText(cliente.getSexo() == 0 ? "Feminino" : "Masculino");

		if(!telefones.isEmpty())
		{
			telefonesString = new ArrayList<>();

			for (Telefone t : telefones)
				telefonesString.add(t.getNumero() + ", " + t.getTipo());

			listaTelefones.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefonesString));
		}
		else
		{
			labelTelefone.setVisibility(View.INVISIBLE);

			listaTelefones.setVisibility(View.INVISIBLE);
		}

		if(!enderecos.isEmpty())
		{
			enderecosString = new ArrayList<>();

			for (Endereco e : enderecos)
				enderecosString.add(e.getRua() + ", " + e.getNumero() + ", " + e.getBairro() + ", " + e.getComplemento());
			listaEnderecos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enderecosString));
		}
		else
		{
			labelEndereco.setVisibility(View.INVISIBLE);
			listaEnderecos.setVisibility(View.INVISIBLE);
		}

        getEstatisticas(idCliente);

		db.close();
		//ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listItems);
		//.setAdapter(arrayAdapter);
	}

	public void editarClicked(View v)
	{
		Intent it = new Intent(this,actCadCliente.class);
		it.putExtra("cliId",cliente.getId());
		startActivityForResult(it,0);
	}

	protected void onActivityResult(int requestCode, int resultCode, Intent data)
	{
		infoClientes(cliente.getId());
	}

    public void pedidosMesClicked(View v)
    {
        Intent it = new Intent(this, actDetalhesPedidosMes.class);
        it.putExtra("cliId", cliente.getId());
        startActivityForResult(it, 0);
    }

    public void todosPedidosClicked(View v)
    {
        Intent it = new Intent(this, actTodosPedidos.class);
        it.putExtra("cliId", cliente.getId());
        startActivityForResult(it, 0);
    }

    public void tirarBlacklistClicked(View v)
    {
        DbHelper dbHelper = new DbHelper(this);
        dbHelper.removerBlacklist(cliente.getId());
        getEstatisticas(cliente.getId());
        tirarBlacklist.setVisibility(View.INVISIBLE);
    }

}

