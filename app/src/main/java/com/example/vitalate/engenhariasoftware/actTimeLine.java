package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;

public class actTimeLine extends AppCompatActivity {

	private ArrayList pashupatinath;
	private ArrayList<String> teste;
	private ListView dtNiver;
	private SeekBar barraDias;
	private TextView labelApresentacao;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_act_time_line);


		dtNiver = (ListView) findViewById(R.id.lst_tImeline);

		barraDias = (SeekBar) findViewById(R.id.sek_dias);

		labelApresentacao = (TextView) findViewById(R.id.lbl_timeLineExibirDias);


		barraDias.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
			@Override
			public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
				consultar(i+1);
                alterarLabel(i+1);
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {

			}

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {

			}
		});

		dtNiver.setOnItemClickListener(new AdapterView.OnItemClickListener()
		{
			public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
			{
				//String textoSelecionado = (String) (dtNiver.getItemAtPosition(myItemInt));
				//String aux[] = textoSelecionado.split(" : ");
		Object obj = pashupatinath.get(myItemInt);
				if(obj instanceof Cliente)
				{
					int id = ((Cliente)obj).getId();
					detalharCliente(id);
				}
                else
                {
                    Pedido ped = (Pedido) obj;
                    Cliente c = new DbHelper(myView.getContext()).detalhesCliente(ped.getCodCliente());
                    Intent whatsappIntent = new Intent(Intent.ACTION_SEND);
                    whatsappIntent.setType("text/plain");
                    whatsappIntent.setPackage("com.whatsapp");
                    whatsappIntent.putExtra(Intent.EXTRA_TEXT, "Oi " + c.getNome() + " Tudo bem? Gostaria de avisar que a data limite de seu pedido expira " + ped.getDataLimite() + " ok? podemos combinar algo?");
                    try {
                        startActivity(whatsappIntent);
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(actTimeLine.this, "Não possui whatsapp instalado!", Toast.LENGTH_SHORT).show();
                    }
                }

			}
		});

		consultar(1);
		alterarLabel(1);


	}

	private void alterarLabel(int meses)
	{
		int dias = meses * 30;
		labelApresentacao.setText("Proximos " + dias + " dias");
	}

	private void detalharCliente(int idCliente)
	{
		Intent it = new Intent(this,actDetalhesCliente.class);
		it.putExtra("idCliente", idCliente);
		startActivityForResult(it,0);
	}

	private void addLista() {
		try {
			if (teste != null)
			{
				ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, teste);
				dtNiver.setAdapter(arrayAdapter);

			}
		} catch (Exception e) {
			mostrarToast("Erro " + e.getMessage());
		}
	}

	private void mostrarToast(String s) {
		Toast.makeText(actTimeLine.this, s, Toast.LENGTH_LONG).show();
	}

	private void consultar(int progress)
	{
		DbHelper dbHelper = new DbHelper(this);

		try {
			pashupatinath = dbHelper.getTimeLine(progress);
		} catch (ParseException e) {
			e.printStackTrace();
		}

		teste = new ArrayList<>();

		if(!pashupatinath.isEmpty()) {
			for (Object obj : pashupatinath)
			{
				String addparsa;

				if (obj instanceof Cliente)
					addparsa = ((Cliente) obj).getData().replace("-", "/") + " Aniversário de " + ((Cliente) obj).getNome();
				else {
					int codCLiente = ((Pedido) obj).getCodCliente();
					Cliente c = new Cliente();
					c = dbHelper.detalhesCliente(codCLiente);
					addparsa = ((Pedido) obj).getDataLimite().replace("-", "/") + " Data limite do pedido de " + c.getNome();
				}
				teste.add(addparsa);
			}
		}
		else
			teste = new ArrayList<>();

		addLista();
	}
}
