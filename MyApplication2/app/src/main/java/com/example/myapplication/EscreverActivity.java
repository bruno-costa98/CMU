package com.example.myapplication;


import java.io.FileOutputStream;
import java.io.IOException;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import java.io.PrintWriter;
import android.app.Activity;


public class EscreverActivity extends Activity {

    private EditText edit;

    @Override
    //Renderiza a tela
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_escrever);


        // Recebe as informações vindas da tela de gravação, mas
        //especificamente do textview
        edit = (EditText) findViewById(R.id.edit);

    }

    public void write(View view) {
        try {
            //Recupera o texto em forma de String
            String texto = edit.getText().toString();
            //Invoca o método gravar abaixo para gravar a informação no
            //arquivo
            gravar(texto);
            //Exibe uma mensagem curta na tela informando que o arquivo
            //foi gravado com sucesso
            Toast.makeText(this, "Arquivo gravado em com sucesso",
                    Toast.LENGTH_LONG).show();
            finish();

        } catch (IOException e) {
            // Exibe uma mensagem longa na tela caso ocorra algum erro
            Toast.makeText(this, "Erro: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // A maior parte dos conceitos desse método vem da programação java pura
    //com exceção do parâmetro MODE_PRIVATE que serve para dizer que este
    //é um arquivo privado da aplicação. Os outros itens dispensam
    //apresentação.
    private void gravar(String text) throws IOException {
        FileOutputStream out = openFileOutput("teste.txt", MODE_PRIVATE);
        PrintWriter print_writer = new PrintWriter(out);

        print_writer.print(text);
        print_writer.close();

    }


}
