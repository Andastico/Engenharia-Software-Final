package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dao.DbHelper;
import model.Pedido;

public class actTodosPedidos extends AppCompatActivity {
    private ArrayList<String> arrayTodosPedidos;
    private ListView todosPedidos;
    private List<Pedido> pedidos;
    private List<Pedido> pedidosAuxiliar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_todos_pedidos);

        todosPedidos = (ListView) findViewById(R.id.lstTodosPedidos);

        exibirTodosPedidos();

        todosPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                detalharPedido(pedidosAuxiliar.get(myItemInt).getId());
            }
        });
    }

    private void exibirTodosPedidos()
    {
        DbHelper db = new DbHelper(this);
        pedidosAuxiliar = new ArrayList<>();
        arrayTodosPedidos = new ArrayList<>();
        pedidos = db.selectPedidosCodCliente(this.getIntent().getExtras().getInt("cliId"));

        for(Pedido p : pedidos)
        {
            if(p.isPago() == 1)
            {
                arrayTodosPedidos.add("Preço: " + p.getPreco() + "\nData do Pedido: " + p.getDataInicial() + "\nData Limite: " + p.getDataLimite() + "\nPago: Sim");
            }
            else
            {
                arrayTodosPedidos.add("Preço: " + p.getPreco() + "\nData do Pedido: " + p.getDataInicial() + "\nData Limite: " + p.getDataLimite() + "\nPago: Não");
            }

            pedidosAuxiliar.add(p);
        }

        if (arrayTodosPedidos.isEmpty())
        {
            todosPedidos.setVisibility(View.INVISIBLE);
            mostrarToast("Nenhum pedido encontrado");
        }
        else
        {
            todosPedidos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayTodosPedidos));
        }
    }

    private void mostrarToast(String mensagem) {
        Toast.makeText(actTodosPedidos.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void detalharPedido(int pedId)
    {
        Intent it = new Intent(this,actDetalhesPedido.class);
        it.putExtra("pedId", pedId);
        startActivityForResult(it,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        exibirTodosPedidos();
    }
}