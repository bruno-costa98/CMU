package com.example.contactrecycler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    ArrayList<Contact> contacts;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvContacts = (RecyclerView) findViewById(R.id.rvContacts);

        //preencher lista com 20 contactos
        contacts = Contact.createContactsList(20);
        // Criar um adaptador de contactos com os contactos da lista
        ContactsAdapter adapter = new ContactsAdapter(contacts);
        // Anexar o adaptador á RecyleView para preencher os dados
        rvContacts.setAdapter(adapter);
        // Mudar o layoutManager para posicionar os itens
        rvContacts.setLayoutManager(new LinearLayoutManager(this));

    }

}