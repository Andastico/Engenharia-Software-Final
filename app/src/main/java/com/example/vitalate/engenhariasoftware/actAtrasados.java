package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;

public class actAtrasados extends AppCompatActivity {
    private ListView listaAtrasados;
    private List<Pedido> pedidosAtrasados;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_atrasados);

        listaAtrasados = (ListView) findViewById(R.id.lstAtrasados);
        pedidosAtrasados = new ArrayList<>();

        listaAtrasados.setVisibility(View.VISIBLE);
        exibirAtrasados();

        listaAtrasados.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                detalharAtrasados(pedidosAtrasados.get(myItemInt).getId());
            }
        });
    }

    //O banco está com um monte de código de cliente cadastrado errado então vai bugar
    private void exibirAtrasados()
    {
        try
        {
            int dias;
            DbHelper db = new DbHelper(this);
            Cliente cliente = new Cliente();
            List<Pedido> pedidos = new ArrayList<>();
            ArrayList<String> adapterClientes = new ArrayList<>();

            SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
            long date = System.currentTimeMillis();
            String dataString = formataData.format(date);

            pedidos = db.selectPedidos();

            for (Pedido p : pedidos)
            {
                if(!p.getDataLimite().isEmpty())
                {
                    Date dataAtual = (Date) formataData.parse(dataString);
                    Date dataLimite = (Date) formataData.parse(p.getDataLimite());

                    cliente = db.detalhesCliente(p.getCodCliente());

                    for (dias = 0; dataAtual.after(dataLimite); dias++)
                    {
                        dataAtual.setDate(dataAtual.getDate() - 1);
                    }

                    if(dias > 0 && p.isPago() == 0)
                    {
                        if(!db.selectExisteAtraso(p.getId()))
                        {
                            db.insertAtrasos(p.getId());
                        }

                        if(db.selectExisteAtraso(p.getId()))
                        {
                            adapterClientes.add("Nome: " + cliente.getNome() + "\nPreço: " + p.getPreco() + "\nData do pedido: " + p.getDataInicial() + "\nData limite: " + p.getDataLimite() + "\nDias atrasados: " + dias);
                            pedidosAtrasados.add(p);
                        }
                    }
                }
            }

            if (adapterClientes.isEmpty())
            {
                listaAtrasados.setVisibility(View.INVISIBLE);
                mostrarToast("Nenhum pedido atrasado encontrado");
            }
            else
            {
                listaAtrasados.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, adapterClientes));
            }
        }
        catch (ParseException e)
        {
            e.printStackTrace();
        }
    }

    private void detalharAtrasados(int pedId)
    {
        Intent it = new Intent(this,actDetalhesAtrasados.class);
        it.putExtra("pedId", pedId);
        startActivityForResult(it,0);
    }

    private void mostrarToast(String mensagem)
    {
        Toast.makeText(actAtrasados.this, mensagem, Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        exibirAtrasados();
    }
}