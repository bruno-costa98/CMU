package com.example.contactrecycler;
import java.util.ArrayList;

public class Contact {
    private String mName;
    private boolean mOnline;

    public Contact(String name, boolean online) {
        mName = name;
        mOnline = online;
    }

    //obter nome
    public String getName() {
        return mName;
    }

    //metodo que retorna o estado online ou nao
    public boolean isOnline() {
        return mOnline;
    }

    private static int lastContactId = 0;

    //criar o arrayList de contactos
    public static ArrayList<Contact> createContactsList(int numContacts) {
        ArrayList<Contact> contacts = new ArrayList<Contact>();

        for (int i = 1; i <= numContacts; i++) {
            contacts.add(new Contact("Person " + ++lastContactId, i <= numContacts / 2));
        }
        return contacts;
    }
}
