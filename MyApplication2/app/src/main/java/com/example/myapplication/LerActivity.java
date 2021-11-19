package com.example.myapplication;

import java.util.Scanner;
import android.app.Activity;
import java.io.FileInputStream;
import java.io.IOException;
import android.widget.Toast;
import android.os.Bundle;
import android.widget.TextView;

public class LerActivity extends Activity {

    private TextView Texto;

    @Override
    //Renderiza a tela
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ler);

        //Referência ao item da tela referênciado pelo id que tem o nome
        // texto
        Texto = (TextView) findViewById(R.id.texto);

        try {
            // Invoca o método que cai fazer a leitura do arquivo
            ler();

        } catch (IOException e) {
            // Exibe uma mensagem curta na tela caso ocorra algum erro
            Toast.makeText(this, "Erro: " + e.getMessage(),
                    Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    // Método que faz a leitura do arquivo
    // Essencialmente Java, dispensa apresentações
    private void ler() throws IOException {
        FileInputStream input = openFileInput("teste.txt");

        Scanner scanner = new Scanner(input);

        try {
            StringBuilder sb = new StringBuilder();

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                sb.append(line).append("\n");
            }

            Texto.setText(sb.toString());

        } finally {
            scanner.close();
        }
    }

}
