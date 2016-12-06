package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
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

public class actDetalhesPedido extends AppCompatActivity {

    private EditText nome;
    private EditText preco;
    private EditText observacao;
    private EditText dtInicialDia;
    private EditText dtInicialMes;
    private EditText dtInicialAno;
    private EditText dtLimiteDia;
    private EditText dtLimiteMes;
    private EditText dtLimiteAno;
    private EditText dtFinalDia;
    private EditText dtFinalMes;
    private EditText dtFinalAno;
    private Button finalizarPedido;
    private Button editarPedido;
    private Button finalizarEdicao;
    private Pedido pedido;
    private Cliente cliente;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_detalhes_pedido);

        nome = (EditText) findViewById(R.id.edtNomeDet);
        preco = (EditText) findViewById(R.id.edtPrecoDet);
        observacao = (EditText) findViewById(R.id.edtObservacaoDet);
        dtInicialDia = (EditText) findViewById(R.id.edt_detPedDiaIni);
        dtInicialMes = (EditText) findViewById(R.id.edt_detPedMesIni);
        dtInicialAno = (EditText) findViewById(R.id.edt_detPedAnoIni);
        dtLimiteDia = (EditText) findViewById(R.id.edt_detPedDiaLim);
        dtLimiteMes = (EditText) findViewById(R.id.edt_detPedMesLim);
        dtLimiteAno = (EditText) findViewById(R.id.edt_detPedAnoLim);
        dtFinalDia = (EditText) findViewById(R.id.edt_detPedDiaFin);
        dtFinalMes = (EditText) findViewById(R.id.edt_detPedMesFin);
        dtFinalAno = (EditText) findViewById(R.id.edt_detPedAnoFin);
        finalizarPedido = (Button) findViewById(R.id.btnFinalizarPedido);
        editarPedido = (Button) findViewById(R.id.btnEditarPedido);
        finalizarEdicao = (Button) findViewById(R.id.btnFinalizarEdicao);

        finalizarEdicao.setVisibility(View.INVISIBLE);
        infoPedidos(this.getIntent().getExtras().getInt("pedId"));
    }

    private void infoPedidos(int pedId)
    {
        DbHelper db = new DbHelper(this);
        pedido = db.selectPedidosId(pedId);
        cliente = db.detalhesCliente(pedido.getCodCliente());

        nome.setText(cliente.getNome());
        preco.setText(String.valueOf(pedido.getPreco()));
        observacao.setText(pedido.getObservacao());

        dtInicialDia.setText((pedido.getDataInicial().substring(0, 2)));
        dtInicialMes.setText((pedido.getDataInicial().substring(3, 5)));
        dtInicialAno.setText((pedido.getDataInicial().substring(6, 10)));

        if(!pedido.getDataLimite().equals("") || pedido.getDataLimite().length() != 0)
        {
            dtLimiteDia.setText(pedido.getDataLimite().substring(0, 2));
            dtLimiteMes.setText(pedido.getDataLimite().substring(3, 5));
            dtLimiteAno.setText(pedido.getDataLimite().substring(6, 10));
        }

        if (pedido.isPago() == 1)
        {
            dtFinalDia.setText(pedido.getDataFinal().substring(0, 2));
            dtFinalMes.setText(pedido.getDataFinal().substring(3, 5));
            dtFinalAno.setText(pedido.getDataFinal().substring(6, 10));
            finalizarPedido.setEnabled(false);
        }
        else
        {
            dtFinalDia.setText("");
            dtFinalMes.setText("");
            dtFinalAno.setText("");
            finalizarPedido.setEnabled(true);
        }
    }

    public void finalizarClicked(View v)
    {
        DbHelper db = new DbHelper(this);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long date = System.currentTimeMillis();
        String dataAtual = dateFormat.format(date);

        pedido = db.selectPedidosId(this.getIntent().getExtras().getInt("pedId"));

        if(db.updateFinalizaPedido(pedido, dataAtual))
        {
            mostrarToast("Pedido finalizado com sucesso!");
            infoPedidos(this.getIntent().getExtras().getInt("pedId"));
        }
        else
        {
            mostrarToast("Não foi possível finalizar o pedido!");
        }
    }

    public void editarPedidoClicked(View v)
    {
        preco.setEnabled(true);
        observacao.setEnabled(true);
        dtInicialDia.setEnabled(true);
        dtInicialMes.setEnabled(true);
        dtInicialAno.setEnabled(true);
        dtLimiteDia.setEnabled(true);
        dtLimiteMes.setEnabled(true);
        dtLimiteAno.setEnabled(true);
        editarPedido.setEnabled(false);
        finalizarPedido.setVisibility(View.INVISIBLE);
        finalizarEdicao.setVisibility(View.VISIBLE);

        if(!pedido.getDataFinal().equals(""))
        {
            dtFinalDia.setEnabled(true);
            dtFinalMes.setEnabled(true);
            dtFinalAno.setEnabled(true);
        }
    }

    public void finalizarEdicaoClicked(View v)
    {
        try
        {
            DbHelper db = new DbHelper(this);

            if(validarEdicao())
            {
                String auxDia="";
                String auxMes="";
                pedido.setPreco(Double.parseDouble(preco.getText().toString()));
                pedido.setObservacao(observacao.getText().toString());

                if(dtInicialDia.length() == 1)
                {
                    auxDia = 0 + dtInicialDia.getText().toString();
                }
                else
                {
                    auxDia = dtInicialDia.getText().toString();
                }

                if(dtInicialMes.length() == 1)
                {
                    auxMes = 0 + dtInicialMes.getText().toString();
                }
                else
                {
                    auxMes = dtInicialMes.getText().toString();
                }

                String auxIni = auxDia + "-" + auxMes + "-" + dtInicialAno.getText().toString();
                pedido.setDataInicial(auxIni);

                if(dtLimiteDia.length() == 0 && dtLimiteMes.length() == 0 && dtLimiteAno.length() == 0)
                {
                    pedido.setDataLimite("");
                }
                else
                {
                    if(dtLimiteDia.length() == 1)
                    {
                        auxDia = 0 + dtLimiteDia.getText().toString();
                    }
                    else
                    {
                        auxDia = dtLimiteDia.getText().toString();
                    }

                    if(dtLimiteMes.length() == 1)
                    {
                        auxMes = 0 + dtLimiteMes.getText().toString();
                    }
                    else
                    {
                        auxMes = dtLimiteMes.getText().toString();
                    }

                    String auxLim = auxDia + "-" + auxMes + "-" + dtLimiteAno.getText().toString();
                    pedido.setDataLimite(auxLim);
                }

                if(dtFinalDia.length() == 0 && dtFinalMes.length() == 0 && dtFinalAno.length() == 0)
                {
                    pedido.setPago(0);
                    pedido.setDataFinal("");
                }
                else
                {
                    if(!pedido.getDataFinal().equals(""))
                    {
                        String auxFin = dtFinalDia.getText().toString() + "-" + dtFinalMes.getText().toString() + "-" + dtFinalAno.getText().toString();
                        pedido.setPago(1);
                        pedido.setDataFinal(auxFin);
                    }
                }

                db.updateEditaPedido(pedido);
                infoPedidos(this.getIntent().getExtras().getInt("pedId"));

                preco.setEnabled(false);
                observacao.setEnabled(false);
                dtInicialDia.setEnabled(false);
                dtInicialMes.setEnabled(false);
                dtInicialAno.setEnabled(false);
                dtLimiteDia.setEnabled(false);
                dtLimiteMes.setEnabled(false);
                dtLimiteAno.setEnabled(false);
                dtFinalDia.setEnabled(false);
                dtFinalMes.setEnabled(false);
                dtFinalAno.setEnabled(false);
                editarPedido.setEnabled(true);
                finalizarPedido.setVisibility(View.VISIBLE);
                finalizarEdicao.setVisibility(View.INVISIBLE);
            }
        }
        catch(Exception e)
        {
            mostrarToast("Erro ao editar");
        }
    }

    public boolean validarEdicao()
    {
        boolean aux = false;

        if(preco.length() == 0)
        {
            mostrarToast("O pedido deve conter o campo preço");
            return false;
        }
        else
        {
            aux = true;
        }

        if(dtInicialDia.length() > 0 && dtInicialMes.length() > 0 && dtInicialAno.length() > 0)
        {
            if(dtInicialDia.length() <= 2 && Integer.parseInt(dtInicialDia.getText().toString()) > 0 && Integer.parseInt(dtInicialDia.getText().toString()) <= 31)
            {
                if(dtInicialMes.length() <= 2 && Integer.parseInt(dtInicialMes.getText().toString()) > 0 && Integer.parseInt(dtInicialMes.getText().toString()) <= 12)
                {
                    aux = true;
                }
                else
                {
                    mostrarToast("Digite um mês válido no campo data do pedido");
                    return false;
                }
            }
            else
            {
                mostrarToast("Digite um dia válido no campo data do pedido");
                return false;
            }
        }
        else
        {
            mostrarToast("Preencha todos os campos da data do pedido");
            return false;
        }

        if(dtLimiteDia.length() > 0 && dtLimiteMes.length() > 0 && dtLimiteAno.length() > 0)
        {
            if(dtLimiteDia.length() <= 2 && Integer.parseInt(dtLimiteDia.getText().toString()) > 0 && Integer.parseInt(dtLimiteDia.getText().toString()) <= 31)
            {
                if(dtLimiteMes.length() <= 2 && Integer.parseInt(dtLimiteMes.getText().toString()) > 0 && Integer.parseInt(dtLimiteMes.getText().toString()) <= 12)
                {
                    aux = true;
                }
                else
                {
                    mostrarToast("Digite um mês válido no campo data limite");
                    return false;
                }
            }
            else
            {
                mostrarToast("Digite um dia válido no campo data limite");
                return false;
            }
        }
        else
        {
            if(dtLimiteDia.length() == 0 && dtLimiteMes.length() == 0 && dtLimiteAno.length() == 0)
            {
                return true;
            }
            else
            {
                mostrarToast("Preencha todos os campos da data limite ou deixe em branco");
                return false;
            }
        }

        if(!pedido.getDataFinal().equals(""))
        {
            if(dtFinalDia.length() > 0 && dtFinalMes.length() > 0 && dtFinalAno.length() > 0)
            {
                if(dtFinalDia.length() <= 2 && Integer.parseInt(dtFinalDia.getText().toString()) > 0 && Integer.parseInt(dtFinalDia.getText().toString()) <= 31)
                {
                    if(dtFinalMes.length() <= 2 && Integer.parseInt(dtFinalMes.getText().toString()) > 0 && Integer.parseInt(dtFinalMes.getText().toString()) <= 12)
                    {
                        aux = true;
                    }
                    else
                    {
                        mostrarToast("Digite um mês válido no campo data do pagamento");
                        return false;
                    }
                }
                else
                {
                    mostrarToast("Digite um dia válido no campo data do pagamento");
                    return false;
                }
            }
            else
            {
                if(dtFinalDia.length() == 0 && dtFinalMes.length() == 0 && dtFinalAno.length() == 0)
                {
                    return true;
                }
                else
                {
                    mostrarToast("Preencha todos os campos da data do pagamento ou deixe em branco");
                    return false;
                }
            }
        }

        return aux;
    }

    public void mostrarToast(String mensagem)
    {
        Toast.makeText(actDetalhesPedido.this, mensagem, Toast.LENGTH_LONG).show();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        infoPedidos(pedido.getId());
    }
}