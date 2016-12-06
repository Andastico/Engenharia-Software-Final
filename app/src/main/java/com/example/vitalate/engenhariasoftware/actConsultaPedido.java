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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;

public class actConsultaPedido extends AppCompatActivity {
    private ListView listaPedidos;
    private List<Pedido> pedidos;
    private List<Pedido> pedidosAuxiliar;
    private EditText diaComeco;
    private EditText mesComeco;
    private EditText anoComeco;
    private EditText diaFinal;
    private EditText mesFinal;
    private EditText anoFinal;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_consulta_pedido);

        diaComeco = (EditText) findViewById(R.id.edt_conPedDia1);
        mesComeco = (EditText) findViewById(R.id.edt_conPedMes1);
        anoComeco = (EditText) findViewById(R.id.edt_conPedAno1);
        diaFinal = (EditText) findViewById(R.id.edt_conPedDia2);
        mesFinal = (EditText) findViewById(R.id.edt_conPedMes2);
        anoFinal = (EditText) findViewById(R.id.edt_conPedAno2);
        listaPedidos = (ListView) findViewById(R.id.lst_conPedidos);
        pedidosAuxiliar = new ArrayList<>();
        cliente = new Cliente();

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long date = System.currentTimeMillis();
        String dataAtual = dateFormat.format(date);
        diaComeco.setText(dataAtual.substring(0, 2));
        mesComeco.setText(dataAtual.substring(3, 5));
        anoComeco.setText(dataAtual.substring(6, 10));

        diaComeco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mesComeco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        anoComeco.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        diaFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        mesFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        anoFinal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                exibirPedidos();
            }

            @Override
            public void afterTextChanged(Editable s)
            {

            }
        });

        listaPedidos.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            public void onItemClick(AdapterView<?> myAdapter, View myView, int myItemInt, long mylng)
            {
                detalharPedido(pedidosAuxiliar.get(myItemInt).getId());
            }
        });
    }

    private void exibirPedidos()
    {
        if(validarDatas())
        {
            try
            {
                listaPedidos.setVisibility(View.VISIBLE);
                DbHelper db = new DbHelper(this);
                pedidos = db.selectPedidos();
                pedidosAuxiliar.clear();

                SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
                ArrayList<String> dataInformada = new ArrayList<>();

                String stringAntes = diaComeco.getText().toString() + "-" + mesComeco.getText().toString() + "-" + anoComeco.getText().toString();
                String stringDepois = diaFinal.getText().toString() + "-" + mesFinal.getText().toString() + "-" + anoFinal.getText().toString();

                Date dataAntes = (Date) formataData.parse(stringAntes);
                Date dataDepois = (Date) formataData.parse(stringDepois);

                for (Pedido p : pedidos)
                {
                    if(!p.getDataLimite().equals(""))
                    {
                        Date dataBanco = (Date) formataData.parse(p.getDataLimite());
                        String stringDataBanco = p.getDataLimite();

                        if (dataBanco.after(dataAntes) && dataBanco.before(dataDepois) || stringDataBanco.equalsIgnoreCase(stringAntes) || stringDataBanco.equalsIgnoreCase(stringDepois))
                        {
                            cliente = db.detalhesCliente(p.getCodCliente());

                            if(p.isPago() == 1)
                            {
                                dataInformada.add("Nome: " + cliente.getNome() + "\nPreço: " + p.getPreco() + "\nData do Pedido: " + stringDataBanco + "\nPago: Sim");
                            }
                            else
                            {
                                dataInformada.add("Nome: " + cliente.getNome() + "\nPreço: " + p.getPreco() + "\nData do Pedido: " + stringDataBanco + "\nPago: Não");
                            }

                            pedidosAuxiliar.add(p);
                        }
                    }
                }

                if (dataInformada.isEmpty())
                {
                    listaPedidos.setVisibility(View.INVISIBLE);
                    mostrarToast("Nenhum pedido encontrado");
                }
                else
                {
                    listaPedidos.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, dataInformada));
                }
            }
            catch (ParseException e)
            {
                e.printStackTrace();
            }
        }
    }

    public boolean validarDatas()
    {
        if(diaComeco.length() == 2 && mesComeco.length() == 2 && anoComeco.length() == 4 && diaFinal.length() == 2 && mesFinal.length() == 2 && anoFinal.length() == 4)
        {
            if(Integer.parseInt(diaComeco.getText().toString()) > 0 && Integer.parseInt(diaComeco.getText().toString()) <= 31 && Integer.parseInt(diaFinal.getText().toString()) > 0 && Integer.parseInt(diaFinal.getText().toString()) <= 31)
            {
                if(Integer.parseInt(mesComeco.getText().toString()) > 0 && Integer.parseInt(mesComeco.getText().toString()) <= 12 && Integer.parseInt(mesFinal.getText().toString()) > 0 && Integer.parseInt(mesFinal.getText().toString()) <= 12)
                {
                    if(Integer.parseInt(anoFinal.getText().toString()) >= Integer.parseInt(anoComeco.getText().toString()))
                    {
                        return true;
                    }
                    else
                    {
                        mostrarToast("Digite um ano válido!");
                        return false;
                    }
                }
                else
                {
                    mostrarToast("Digite um mês válido!");
                    return false;
                }
            }
            else
            {
                mostrarToast("Digite um dia válido!");
                return false;
            }
        }
        else
        {
            return false;
        }
    }

    private void mostrarToast(String mensagem) {
        Toast.makeText(actConsultaPedido.this, mensagem, Toast.LENGTH_LONG).show();
    }

    private void detalharPedido(int pedId)
    {
        Intent it = new Intent(this,actDetalhesPedido.class);
        it.putExtra("pedId", pedId);
        startActivityForResult(it,0);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        exibirPedidos();
    }
}