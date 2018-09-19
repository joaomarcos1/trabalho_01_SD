/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_workloadgenerator;

import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;
import javax.swing.JLabel;

/**
 *
 * @author Helbert Monteiro
 */
public class CriarThread {
    
    GeradorGrafico grafico = new GeradorGrafico();
    
    private ServerSocket servidor;
    private Socket       cliente;
    
    ObjectOutputStream saida;
    Scanner            entrada;
    
    private int    tempo, indice;
    private double media, valor, soma;
    
    
    
    public void run(String celular, JLabel aparelho, int porta){
        try {
            servidor = new ServerSocket(porta);
            System.out.println("Servidor ouvindo a porta " + porta);

            tempo  = 3;
            indice = 3;
            soma   = 0;

            while(true) {
                cliente = servidor.accept();
                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                saida = new ObjectOutputStream(cliente.getOutputStream());


                entrada = new Scanner(cliente.getInputStream());
                valor = Double.parseDouble(entrada.nextLine());
                System.out.println(celular + ": "+ valor);
                
                
                grafico.addValor(valor, tempo, celular);
                grafico.exibeGrafico();
                tempo += 3;
                
                soma += valor;
                media = soma / indice;
                indice ++;

                aparelho.setText("Monitorado: " + cliente.getInetAddress().getHostAddress() + " - Média: " + media);

                saida.flush();
                saida.writeObject(new Date());
                saida.close();
                cliente.close();
            }
        }catch(Exception a) {
            System.out.println("Erro: " + a.getMessage());
        }
    }
}