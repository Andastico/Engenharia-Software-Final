package com.example.vitalate.engenhariasoftware;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
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

public class actCadCliente extends AppCompatActivity {

    private ListView lstEndereco;
    private ArrayList<String> enderecos;
    private ListView lstTelefone;
    private ArrayList<String> telefones;
    private EditText nome;
    private RadioButton[] sexo;
    private Button btnCadastrar;
	private EditText dia;
	private EditText mes;
	private EditText ano;

    boolean editMode = false;
    int idCliente;

	private int ultimaTela; //Gambiarra... 0 para enderecos, 1 para telefone.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_cad_cliente);



        btnCadastrar = (Button) findViewById(R.id.btn_cadastrarCliente);

        enderecos = new ArrayList<>();
        telefones = new ArrayList<>();

		lstTelefone = (ListView) findViewById(R.id.lst_telefone);
        lstEndereco = (ListView) findViewById(R.id.lst_endereco);

        nome = (EditText) findViewById(R.id.edt_cadCliNome);

        sexo = new RadioButton[2];
        sexo[0] = (RadioButton) findViewById(R.id.rdb_cadCliFeminino);
        sexo[1] = (RadioButton) findViewById(R.id.rdb_cadCliMasculino);

		dia = (EditText) findViewById(R.id.edt_cadCliDia);
		mes = (EditText) findViewById(R.id.edt_cadCliMes);
		ano = (EditText) findViewById(R.id.edt_cadCliAno);

        Bundle extras = this.getIntent().getExtras();
        if(extras != null && extras.containsKey("cliId"))
        {

            DbHelper db = new DbHelper(this);
            editMode = true;
            idCliente = extras.getInt("cliId");
            Cliente c = db.detalhesCliente(idCliente);
            nome.setText(c.getNome());
            String aux[] = c.getData().split("-");
			dia.setText(aux[0]);
			mes.setText(aux[1]);
			ano.setText(aux[2]);
            if(c.getSexo() == 0)
                sexo[0].setChecked(true);
            else
                sexo[1].setChecked(true);
            btnCadastrar.setText("Salvar edição");
			((TextView)findViewById(R.id.lbl_cadedit)).setText("Edição de cliente");

			List<Endereco> enderecosEditar = db.selectEnderecoCliente(idCliente);
			List<Telefone> telefonesEditar = db.selectTelefoneCliente(idCliente);

			if(!enderecosEditar.isEmpty() && enderecosEditar!=null) {
			enderecos = new ArrayList<>();
				for (Endereco e : enderecosEditar)
					enderecos.add(e.getRua() + ":" + e.getNumero() + ":" + e.getBairro() + e.getComplemento());

				lstEndereco.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enderecos));
			}

			if(!telefonesEditar.isEmpty() && telefonesEditar != null)
			{
				telefones = new ArrayList<>();
				for(Telefone t : telefonesEditar)
					telefones.add(t.getNumero() + ":" + t.getTipo());

				lstTelefone.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefones));
			}
            db.close();
        }
    }

	private String getDataCompleta()
	{
		return dia.getText().toString() + "-" + mes.getText().toString() + "-" + ano.getText().toString();
	}

    public void editarEnderecoClicked(View v) {
		ultimaTela = 0;
        Intent it = new Intent(this, actEditEndereco.class);
        it.putStringArrayListExtra("enderecos", enderecos);
        startActivityForResult(it, 0);
    }

    public void editarTelefoneClicked(View v) {
		ultimaTela = 1;
        Intent it = new Intent(this, actEditTelefone.class);
        it.putStringArrayListExtra("telefones", telefones);
        startActivityForResult(it, 0);
    }

    /**
     * Aqui vemos uma gambiarra fenomenal, onde tava bugando um monte pra definir qual intent tava retornando, portanto eu contei quantos
     * dois pontos tinha, onde o telefone devolve duas vezes dois pontos e o endereco devolve mais que isso....
     * sugiro que fique longe, qualquer mudança pode fazer esta função tomar atitudes irreversiveis levando em conta sua inteligencia
     * artificial de geração de comportamentos aleatorios implantada.
     *
     * @param requestCode code requested
     * @param resultCode  result code
     * @param data        intent onde coloco o respectivo arraylist
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data != null) /*Essa verificação é necessaria, caso o usuario entre na tela de editar endereço ou telefone e aperte para voltar com o
                            botão de voltar nativo do android "<" quando isso acontece o data vem como nulxl e da FatalException */ {
            ArrayList<String> al = data.getStringArrayListExtra("array");

            if (!al.isEmpty() && al.get(0).split(":").length <= 2)
			{
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
                lstTelefone.setAdapter(arrayAdapter);
                telefones = al;
            }
			else if(!al.isEmpty())
			{
                ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
                lstEndereco.setAdapter(arrayAdapter);
                enderecos = al;
            }
			else
			{
				switch (ultimaTela)
				{
					case 0:
						ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
						lstEndereco.setAdapter(arrayAdapter);
						enderecos = new ArrayList<>();
						break;
					case 1:
						ArrayAdapter<String> arrayAdapte = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, al);
						lstTelefone.setAdapter(arrayAdapte);
						telefones = new ArrayList<>();
						break;
				}
			}
        }
        /*if((telefones = data.getStringArrayListExtra("telefones")) != null)
        {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, telefones);
            lstTelefone.setAdapter(arrayAdapter);
        }
        if((enderecos = data.getStringArrayListExtra("array")) != null)
        {
            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, enderecos);
            lstEndereco.setAdapter(arrayAdapter);
        }*/
    }

    private void mostrarToast(String mensagem) {
		Toast.makeText(actCadCliente.this, mensagem, Toast.LENGTH_LONG).show();
    }

    public boolean checarFormulario() {
        if (nome.getText().toString().length() == 0) {
            mostrarToast("Insira um nome!");
            return false;
        } else if (ano.getText().toString().isEmpty() || ano.getText().toString().length() < 4) {
            mostrarToast("Insira uma ano valido!");
            return false;
        }
		else if(mes.getText().toString().isEmpty() || Integer.parseInt(mes.getText().toString()) > 12 || Integer.parseInt(mes.getText().toString()) <= 0 )
		{
			mostrarToast("Insira um mês valido!");
			return false;
		}
		else if(dia.getText().toString().isEmpty() || Integer.parseInt(dia.getText().toString()) > 31 || Integer.parseInt(dia.getText().toString()) <= 0  )
		{
			mostrarToast("Insira um dia válido!");
			return false;
		}
		else if (!sexo[0].isChecked() && !sexo[1].isChecked())
		{
            mostrarToast("Escolha o sexo");
            return false;
        }
		else return true;
    }

    private void limpaTela() {
        nome.setText("");
        dia.setText("");
		mes.setText("");
		ano.setText("");
        sexo[0].setChecked(false);
        sexo[1].setChecked(false);
        enderecos = new ArrayList<>();
        telefones = new ArrayList<>();
        lstEndereco.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
        lstTelefone.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, new ArrayList<String>()));
    }

    public void cadastrarClicked(View v) {
        if (checarFormulario())
        {
            if(editMode)
            {
                Cliente c = new Cliente(nome.getText().toString(), getDataCompleta(), sexo[0].isChecked() ? 0 : 1);//Definição do objeto cliente com ternario
                c.setId(idCliente);
                DbHelper db = new DbHelper(this);
				if(lstTelefone.getCount() != 0)
				{
					ArrayList<Telefone> telefonesEditar = new ArrayList<>();
					for(String s : telefones)
					{
						Telefone t = new Telefone();
						String aux[] = s.split(":");
						t.setNumero(aux[0]);
						t.setTipo(aux[1]);
						telefonesEditar.add(t);
					}
					if(!db.editarTelefone(telefonesEditar, idCliente));
						mostrarToast("");
				}
				if(lstEndereco.getCount() != 0)
				{
					ArrayList<Endereco> enderecosEditar = new ArrayList<>();
					for(String s : enderecos)
					{
                        String aux[] = s.split(":");
                        String complemento;
                        try {
                            complemento = aux[3];
                        }
                        catch (Exception ex)
                        {
                            complemento = "";
                        }
						Endereco e = new Endereco();
						e.setRua(aux[0]);
						e.setNumero(Integer.parseInt(aux[1]));
						e.setBairro(aux[2]);
						e.setComplemento(complemento);
						enderecosEditar.add(e);
					}
					if(!db.editarEndereco(enderecosEditar, idCliente));
					mostrarToast("");
				}

                if(db.editarCliente(c))
                {
                    mostrarToast("Cliente editado com sucesso!");
                    finish();
                }
                else
                    mostrarToast("Erro");

				//if(lstEndereco.setAdapter();)

                db.close();

            }
            else {
                Cliente c = new Cliente(nome.getText().toString(), getDataCompleta(), sexo[0].isChecked() ? 0 : 1);//Definição do objeto cliente com ternario
                DbHelper db = new DbHelper(this);
                ArrayList<Endereco> arrayEndereco = null;
                ArrayList<Telefone> arrayTelefone = null;

                if (!enderecos.isEmpty()) {
                    arrayEndereco = new ArrayList<>();
					Endereco e;
                    for (String s : enderecos) {
                        String aux[] = s.split(":");
						String complemento;
						try {
							complemento = aux[3];
						}
						catch (Exception ex)
						{
							complemento = "";
						}
						Endereco endereco = new Endereco(aux[0], Integer.parseInt(aux[1]), aux[2], complemento);
                        arrayEndereco.add(endereco);
                    }
                }

                if (!telefones.isEmpty()) {
                    arrayTelefone = new ArrayList<>();
                    for (String s : telefones) {
                        String aux[] = s.split(":");
                        Telefone t = new Telefone(aux[0], aux[1]);
                        arrayTelefone.add(t);
                    }
                }

                if (db.insertCliente(c, arrayEndereco, arrayTelefone)) {
                    mostrarToast("Cliente cadastrado com sucesso!");
                    limpaTela();
                    db.close();
                } else
                    mostrarToast("Falha ao cadastrar! favor contactar o suporte técnico");
            }
        }
    }

}