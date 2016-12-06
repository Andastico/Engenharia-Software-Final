package com.example.vitalate.engenhariasoftware;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;

import static java.lang.Double.parseDouble;

public class actCadPedido extends AppCompatActivity {
    private EditText preco;
    private EditText observacao;
    private EditText dia;
    private EditText mes;
    private EditText ano;
    private EditText preencheSpinner;

    private Button btnSalvar;

    private CheckBox pago;

    private Spinner spinClientes;

    private List<Cliente> clientes;
    private ArrayList<Integer> auxiliarSpinnerID = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_pedido);

        preco = (EditText) findViewById(R.id.editPreco);
        observacao = (EditText) findViewById(R.id.editObservacao);
        dia = (EditText) findViewById(R.id.edt_cadPedDia);
        mes = (EditText) findViewById(R.id.edt_cadPedMes);
        ano = (EditText) findViewById(R.id.edt_cadPedAno);
        btnSalvar = (Button) findViewById(R.id.btnSalvar);
        preencheSpinner = (EditText) findViewById(R.id.edt_cliSpinner);
        pago = (CheckBox) findViewById(R.id.checkBoxPago);
        spinClientes = (Spinner) findViewById(R.id.spinnerCliente);

        //Setando o campo Ano com a data atual do sistema
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long date = System.currentTimeMillis();
        String dataAtual = dateFormat.format(date);
        int anoParametro = Integer.parseInt(dataAtual.substring(6, 10));
        ano.setText(String.valueOf(anoParametro));

        preencheSpinner.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2)
            {
                if(preencheSpinner.length() != 0)
                {
                    exibirClientesSpinner();
                }
            }

            @Override
            public void afterTextChanged(Editable editable)
            {

            }
        });
    }

    boolean verificarBlacklist(int id)
    {
        DbHelper db = new DbHelper(this);
        final boolean[] retorno = {false};
        if(db.isOnBlackList(id))
        {
            new AlertDialog.Builder(this)
                    .setTitle("Cliente em lista negra")
                    .setMessage("Este cliente se encontra atualmente na lista negra, deseja mesmo fazer este pedido?")
                    .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            retorno[0] = true;
                        }
                    })
                    .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which)
                        {
                            retorno[0] = false;
                        }
                    })
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
            return retorno[0];
        }
        else
            return true;


    }

    public void salvarPedido(View v)
    {
        String data="";
        String auxDia="";
        String auxMes="";
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        long date = System.currentTimeMillis();
        final String dataAtual = dateFormat.format(date);
        int anoParametro = Integer.parseInt(dataAtual.substring(6, 10));

        try
        {
            final DbHelper db = new DbHelper(this);

            if(validarCadastro(anoParametro))
            {
                if(pago.isChecked())
                {
                        Pedido p = new Pedido(1, auxiliarSpinnerID.get(spinClientes.getSelectedItemPosition()), Double.parseDouble(preco.getText().toString()), observacao.getText().toString(), dataAtual, dataAtual, "");
                        db.insertPedido(p);
                        mostrarToast("Cadastrado com sucesso!");
                        limpaTela();

                }
                else
                {
                    if(dia.length() == 1)
                    {
                        auxDia = 0 + dia.getText().toString();
                    }
                    else
                    {
                        auxDia = dia.getText().toString();
                    }

                    if(mes.length() == 1)
                    {
                        auxMes = 0 + mes.getText().toString();
                    }
                    else
                    {
                        auxMes = mes.getText().toString();
                    }

                    data = auxDia + "-" + auxMes + "-" + ano.getText().toString();
                    if(db.isOnBlackList(auxiliarSpinnerID.get(spinClientes.getSelectedItemPosition()))) {
                        final String finalData = data;
                        new AlertDialog.Builder(this)
                                .setTitle("Cliente em lista negra")
                                .setMessage("Este cliente se encontra atualmente na lista negra, deseja mesmo fazer este pedido?")
                                .setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        Pedido p = new Pedido(0, auxiliarSpinnerID.get(spinClientes.getSelectedItemPosition()), parseDouble(preco.getText().toString()), observacao.getText().toString(), dataAtual, "", finalData);
                                        db.insertPedido(p);
                                        mostrarToast("Cadastrado com sucesso!");
                                        limpaTela();
                                    }
                                })
                                .setNegativeButton("Não", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which)
                                    {
                                        //retorno[0] = false;
                                        finish();
                                    }
                                })
                                .setIcon(android.R.drawable.ic_dialog_alert)
                                .show();


                    }
                    else
                    {
                        Pedido p = new Pedido(0, auxiliarSpinnerID.get(spinClientes.getSelectedItemPosition()), parseDouble(preco.getText().toString()), observacao.getText().toString(), dataAtual, "", data);

                        db.insertPedido(p);
                        mostrarToast("Cadastrado com sucesso!");
                        limpaTela();
                    }
                }
            }
        }
        catch(Exception ex)
        {
            mostrarToast("Erro " + ex.getMessage());
        }
    }

    public boolean validarCadastro(int anoParametro)
    {
        //Validação dos campos da tela e inserção no banco
        boolean aux = false;

        if(preco.length() == 0)
        {
            mostrarToast("Preencha o campo Preço Total");
            return false;
        }
        else
        {
            aux = true;
        }

        if(!pago.isChecked() && dia.length() == 0 && mes.length() == 0)
        {
            mostrarToast("O pedido deve constar como pago ou possuir uma data limite");
            return false;
        }
        else
        {
            aux = true;
        }

        if(!pago.isChecked() && dia.length() == 0 && mes.length() == 0 && ano.length() == 0)
        {
            mostrarToast("O pedido deve constar como pago ou possuir uma data limite");
            return false;
        }
        else
        {
            aux = true;
        }

        if(spinClientes.getCount() == 0)
        {
            mostrarToast("Selecione um cliente");
            return false;
        }
        else
        {
            aux = true;
        }

        if(!pago.isChecked())
        {
            if(dia.length() > 0 && mes.length() > 0 && ano.length() > 0)
            {
                if (dia.length() > 0 && Integer.parseInt(dia.getText().toString()) > 0 && Integer.parseInt(dia.getText().toString()) <= 31)
                {
                    if (mes.length() > 0 && Integer.parseInt(mes.getText().toString()) > 0 && Integer.parseInt(mes.getText().toString()) <= 12)
                    {
                        if (ano.length() > 0 && Integer.parseInt(ano.getText().toString()) == anoParametro || Integer.parseInt(ano.getText().toString()) == anoParametro + 1)
                        {
                            aux = true;
                        }
                        else
                        {
                            mostrarToast("Digite um ano válido");
                            return false;
                        }
                    }
                    else
                    {
                        mostrarToast("Digite um mês válido");
                        return false;
                    }
                }
                else
                {
                    mostrarToast("Digite um dia válido");
                    return false;
                }
            }
            else
            {
                mostrarToast("Preencha todos os campos da data");
                return false;
            }
        }

        return aux;
    }

    public void exibirClientesSpinner()
    {
        //Preenchendo o spinner com os clientes
        DbHelper db = new DbHelper(this);
        ArrayList<String> cliString = new ArrayList<>();
        auxiliarSpinnerID.clear();

        clientes = db.consultaClientes(preencheSpinner.getText().toString());

        for(Cliente c : clientes)
        {
            cliString.add(c.getNome());
            auxiliarSpinnerID.add(c.getId());
        }

        ArrayAdapter<String> arrayClientes = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cliString);
        spinClientes.setAdapter(arrayClientes);
    }

    public void pagoClicked(View v)
    {
        //Caso o checkbox esteja pressionado a data limite deve ficar trancado e vice-versa
        dia.setEnabled(pago.isChecked()? false : true);
        mes.setEnabled(pago.isChecked()? false : true);
        ano.setEnabled(pago.isChecked()? false : true);

        if(pago.isChecked())
        {
            dia.setText("");
            mes.setText("");
        }
    }

    public void limpaTela()
    {
        //Limpando os elementos da tela
        preco.setText("");
        observacao.setText("");
        dia.setText("");
        mes.setText("");
        pago.setChecked(false);
        dia.setEnabled(true);
        mes.setEnabled(true);
        ano.setEnabled(true);
    }

    public void mostrarToast(String mensagem)
    {
        Toast.makeText(actCadPedido.this, mensagem, Toast.LENGTH_LONG).show();
    }

    public void cancelarPedido(View v)
    {
        finish();
    }
}