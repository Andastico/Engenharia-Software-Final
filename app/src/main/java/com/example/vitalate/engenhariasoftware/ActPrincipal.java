package com.example.vitalate.engenhariasoftware;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Objects;

import dao.DbHelper;
import model.Cliente;
import model.Pedido;
import model.Telefone;


import static java.lang.Double.parseDouble;

public class ActPrincipal extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_act_principal);
        DbHelper db = new DbHelper(this);
        db.chamarCreate();
    }

    public void cadCliClicked(View v)
    {
        new AlertDialog.Builder(this)
                .setTitle("Cadastrar cliente")
                .setMessage("Deseja cadastrar manualmente ou importar dos contatos?")
                .setPositiveButton("Manualmente", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent it =  new Intent(ActPrincipal.super.getBaseContext(),actCadCliente.class);
                        startActivity(it);
                    }
                })
                .setNegativeButton("Importar", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which)
                    {
                        Intent intent1 = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
                        startActivityForResult(intent1, 1);
                    }
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        try {
            Uri uri = data.getData();

            Log.i("TRABALHO", uri.getPath());

            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            if (cursor.moveToFirst()) {
                String id = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID));

                ArrayList<Telefone> te = new ArrayList<>();

                String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));

                if (hasPhone.equalsIgnoreCase("1")) {
                    Cursor phones = getContentResolver().query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = " + id,
                            null, null);
                    phones.moveToFirst();
                    te.add(new Telefone(phones.getString(phones.getColumnIndex("data1")), "Indefinido"));
                }
                int indexName = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME);
                Cliente cliente = new Cliente();
                cliente.setData("01-01-0001");

                cliente.setNome(cursor.getString(indexName));
                cursor.close();

                DbHelper db = new DbHelper(this);
                if (db.insertCliente(cliente, null, te)) {
                    Toast.makeText(this, "Cadastrado com sucesso", Toast.LENGTH_SHORT).show();
                    Intent it = new Intent(this, actCadCliente.class);
                    cliente.setId(db.lastCliId());
                    it.putExtra("cliId", cliente.getId());
                    startActivity(it);
                } else
                    Toast.makeText(this, "Falha ao cadastrar", Toast.LENGTH_SHORT).show();


            }
        }
        catch(Exception e)
        {

        }
    }

    public void consultarCliClicked(View v)
    {
        Intent it = new Intent(this,actConsultaClientes.class);
        startActivity(it);
    }
    public void rodarPedido(View v)
    {
        Intent it = new Intent(this, actCadPedido.class);
        startActivity(it);
    }

    public void rodarConsultaPedido(View v)
    {
        Intent it = new Intent(this, actConsultaPedido.class);
        startActivity(it);
    }

    public void rodarTimeLine(View v)
    {
        Intent it = new Intent(this, actTimeLine.class);
        startActivity(it);
    }

    public void rodarConsultaBlacklist(View v)
    {
        Intent it = new Intent(this, actConsultaBlacklist.class);
        startActivity(it);
    }

    public void rodarAtrasados(View v)
    {
        Intent it = new Intent(this, actAtrasados.class);
        startActivity(it);
    }

    public void informacoesClicked(View v)
    {
        Intent intent = new Intent(this,actEstatisticasUsuario.class);
        startActivity(intent);
    }

}
