package com.example.vitalate.engenhariasoftware;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;

public class actDetalhesAtrasados extends AppCompatActivity {

    private EditText nome;
    private EditText preco;
    private EditText observacao;
    private EditText dtInicial;
    private EditText dtLimite;
    private EditText dtFinal;
    private Button enviarBlackList;
    private Button finalizarAtrasado;
    private Pedido pedido;
    private Cliente cliente;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalhes_atrasados);

        nome = (EditText) findViewById(R.id.edtNomeAtraso);
        preco = (EditText) findViewById(R.id.edtPrecoAtraso);
        observacao = (EditText) findViewById(R.id.edtObservacaoAtraso);
        dtInicial = (EditText) findViewById(R.id.edtDtInicialAtraso);
        dtLimite = (EditText) findViewById(R.id.edtDtLimiteAtraso);
        dtFinal = (EditText) findViewById(R.id.edtDtFinalAtraso);
        enviarBlackList = (Button) findViewById(R.id.btnEnviaBlackListAtraso);
        finalizarAtrasado = (Button) findViewById(R.id.btnFinalizaAtraso);

        infoAtrasados(this.getIntent().getExtras().getInt("pedId"));
        System.out.println(pedido.isPago());
    }

    private void infoAtrasados(int pedId)
    {
        DbHelper db = new DbHelper(this);
        pedido = db.selectPedidosId(pedId);
        cliente = db.detalhesCliente(pedido.getCodCliente());

        nome.setText(cliente.getNome());
        preco.setText(String.valueOf(pedido.getPreco()));
        observacao.setText(pedido.getObservacao());
        dtInicial.setText((pedido.getDataInicial()));
        dtLimite.setText(pedido.getDataLimite());

        if (pedido.isPago() == 1)
        {
            dtFinal.setText(pedido.getDataFinal());
        }
        else
        {
            dtFinal.setText("");
        }
    }

    public void finalizarAtrasadoClicked(View v)
    {
        try
        {
            DbHelper db = new DbHelper(this);
            SimpleDateFormat formataData = new SimpleDateFormat("dd-MM-yyyy");
            long date = System.currentTimeMillis();
            String dataAtual = formataData.format(date);

            db.updateFinalizaPedido(pedido, dataAtual);
            finalizarAtrasado.setEnabled(false);
            infoAtrasados(this.getIntent().getExtras().getInt("pedId"));
            mostrarToast("Pedido finalizado");
        }
        catch (Exception e)
        {
            mostrarToast("Erro a finalizar o pedido atrasado");
        }
    }

    public void enviarBlackListClicked(View v)
    {
        try
        {
            DbHelper db = new DbHelper(this);

            if(!db.isOnBlackList(pedido.getCodCliente()))
            {
                db.adicionarBlacklist(pedido.getCodCliente());
                db.deleteAtraso(pedido.getId());
                enviarBlackList.setEnabled(false);
                mostrarToast("Este cliente foi enviado para Lista negra");
            }
            else
            {
                mostrarToast("Este cliente já está na Lista Negra");
            }
        }
        catch(Exception e)
        {
            mostrarToast("Erro ao enviar para Lista Negra");
        }
    }

    public void mostrarToast(String mensagem)
    {
        Toast.makeText(actDetalhesAtrasados.this, mensagem, Toast.LENGTH_LONG).show();
    }
}
