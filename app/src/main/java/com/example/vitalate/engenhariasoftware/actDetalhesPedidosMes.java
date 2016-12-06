package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.DbHelper;
import model.Pedido;

public class actDetalhesPedidosMes extends AppCompatActivity {
    private ListView pedidosMes;
    private List<Pedido> pedidos;
    private List<Pedido> pedidosAuxiliar;
    private String mesAtual;
    private String anoAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalhes_pedidos_mes);

        pedidosMes = (ListView) findViewById(R.id.lstPedidosMes);

        exibirTodosPedidosMes();

        pedidosMes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                detalharPedido(pedidosAuxiliar.get(myItemInt).getId());
            }
        });
    }

    private void exibirTodosPedidosMes()
    {
        DbHelper db = new DbHelper(this);
        pedidosAuxiliar = new ArrayList<>();
        pedidos = db.selectPedidosCodCliente(this.getIntent().getExtras().getInt("cliId"));

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long date = System.currentTimeMillis();
        String dataAtual = dateFormat.format(date);
        mesAtual = dataAtual.substring(3, 5);
        anoAtual = dataAtual.substring(6, 10);

        ArrayList<String> arrayPedidosMes = new ArrayList<>();

        for(Pedido p : pedidos)
        {
            String mes = p.getDataInicial().substring(3, 5);
            String ano = p.getDataInicial().substring(6, 10);

            if(mesAtual.equals(mes) && anoAtual.equals(ano))
            {
                if(p.isPago() == 1)
                {
                    arrayPedidosMes.add("Preço: " + p.getPreco() + "\nData do Pedido: " + p.getDataInicial() + "\nData Limite: " + p.getDataLimite() + "\nPago: Sim");
                }
                else
                {
                    arrayPedidosMes.add("Preço: " + p.getPreco() + "\nData do Pedido: " + p.getDataInicial() + "\nData Limite: " + p.getDataLimite() + "\nPago: Não");
                }

                pedidosAuxiliar.add(p);
            }
        }

        if (arrayPedidosMes.isEmpty())
        {
            pedidosMes.setVisibility(View.INVISIBLE);
            mostrarToast("Nenhum pedido encontrado");
        }
        else
        {
            pedidosMes.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, arrayPedidosMes));
        }
    }

    private void mostrarToast(String mensagem) {
        Toast.makeText(actDetalhesPedidosMes.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void detalharPedido(int pedId)
    {
        Intent it = new Intent(this,actDetalhesPedido.class);
        it.putExtra("pedId", pedId);
        startActivityForResult(it,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        exibirTodosPedidosMes();
    }
}