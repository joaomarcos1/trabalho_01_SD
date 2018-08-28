/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_workloadgenerator;

import java.io.*;
import java.net.*;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

/**
 *
 * @author nupasd-ufpi
 */
public class Servidor_WorkLoadGenerator extends JFrame{

    JMenuBar menuBar = new JMenuBar();
    JMenu filmes = new JMenu("Filmes");
    JMenuItem cadastrar_filme = new JMenuItem("Cadastrar");
    JMenuItem alterar_filme = new JMenuItem("Alterar");
    JMenuItem excluir_filme = new JMenuItem("Excluir");
    JMenuItem listar_filmes = new JMenuItem("Listar");
    JMenuItem buscar_filmes = new JMenuItem("Buscar");
      
    JMenu busca = new JMenu("Realizar Busca");
    JMenu sair = new JMenu("Sair");

    JLabel logo = new JLabel();
    
    JLabel aparelho1 = new JLabel("Aparelho 01");
    JLabel aparelho2 = new JLabel("Aparelho 02");
    JLabel aparelho3 = new JLabel("Aparelho 03");
    JLabel info1 = new JLabel();
    JLabel info2 = new JLabel();
    JLabel info3 = new JLabel();
    
    JButton fechar = new JButton("Iniciar Monitoramento");
    
    GeradorGrafico grafico = new GeradorGrafico();

    public Servidor_WorkLoadGenerator() {//construtor da classe
        JPanel painel = new JPanel();
        painel.setLayout(null);
        painel.setBackground(Color.white);
        
        painel.add(fechar);
        fechar.setBounds(30, 30, 300, 40);
        fechar.addActionListener(//evento anônimo para o clique no botão
                
        new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){//É verificado se o usuário deseja realemente sair do software
                
                //INSERIR MÉTODOS DE MONITORAMENTO AQUI
                Thread thread1;
                thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        
                        try {
                            // Instancia o ServerSocket ouvindo a porta 12345
                            ServerSocket servidor = new ServerSocket(9002);
                            System.out.println("Servidor ouvindo a porta 9002");
                            int tempo = 3;
                            double media = 0;
                            double soma = 0;
                            int indice = 1;
                            while(true) {
                                // o método accept() bloqueia a execução até que
                                // o servidor receba um pedido de conexão
                                Socket cliente = servidor.accept();
                                System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                                ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                                
                                Scanner entrada = new Scanner(cliente.getInputStream());
                                //while(entrada.hasNextLine()){
                                double valor = Double.parseDouble(entrada.nextLine());
                                System.out.println("Celular 01: "+ valor);
                                //}
                                
                                //GERANDO GRAFICO
                                grafico.addValor(valor, tempo, "Celular 01");
                                tempo += 3;
                                grafico.exibeGrafico();
                                
                                //CALCULANDO MEDIA
                                soma += valor;
                                media = soma / indice;
                                indice ++;
                                
                                aparelho1.setText("Monitorado: " + cliente.getInetAddress().getHostAddress() + " - Média: " + media);
                                
                                saida.flush();
                                saida.writeObject(new Date());
                                saida.close();
                                cliente.close();
                            }
                        }catch(Exception a) {
                            System.out.println("Erro: " + a.getMessage());
                        }
                    }});
                thread1.start();
                
                
                //Thread 2 - -Celular 2
                
                   Thread thread2 = new Thread(new Runnable() {
                           @Override
                           public void run() {
                 
                               
                               
                                 try {
                        // Instancia o ServerSocket ouvindo a porta 12345
                        ServerSocket servidor = new ServerSocket(9003);
                        System.out.println("Servidor ouvindo a porta 9003");
                        int tempo = 3;
                        double media = 0;
                        double soma = 0;
                        int indice = 1;
                        while(true) {
                          // o método accept() bloqueia a execução até que
                          // o servidor receba um pedido de conexão
                          Socket cliente = servidor.accept();
                          System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                          ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                        
                          
                          Scanner entrada = new Scanner(cliente.getInputStream());
                            //while(entrada.hasNextLine()){
                          double valor = Double.parseDouble(entrada.nextLine());
                                System.out.println("Celular 02: "+ valor);
                                //}
                                
                                //GERANDO GRAFICO
                                grafico.addValor(valor, tempo, "Celular 02");
                                tempo += 3;
                                grafico.exibeGrafico();
                                
                                //CALCULANDO MEDIA
                                soma += valor;
                                media = soma / indice;
                                indice ++;
                                
                                aparelho2.setText("Monitorado: " + cliente.getInetAddress().getHostAddress() + " - Média: " + media);
                                
                                saida.flush();
                                saida.writeObject(new Date());
                                saida.close();
                                cliente.close();
                        }  
                    }catch(Exception a) {
                         System.out.println("Erro: " + a.getMessage());
                    }
                               
                               
                               
                               
                               
                               
                       }});
                thread2.start();
                
                
                //Iniciando a Thread 03 - Celular 03 monitorando o ambiente utilizando porta 9004
                
                   Thread thread3 = new Thread(new Runnable() {
                           @Override
                           public void run() {
                 
                               
                               
                                 try {
                        // Instancia o ServerSocket ouvindo a porta 12345
                        ServerSocket servidor = new ServerSocket(9004);
                        System.out.println("Servidor ouvindo a porta 9004");
                        int tempo = 3;
                        double media = 0;
                        double soma = 0;
                        int indice = 1;
                        while(true) {
                          // o método accept() bloqueia a execução até que
                          // o servidor receba um pedido de conexão
                          Socket cliente = servidor.accept();
                          System.out.println("Cliente conectado: " + cliente.getInetAddress().getHostAddress());
                          ObjectOutputStream saida = new ObjectOutputStream(cliente.getOutputStream());
                        
                          
                          Scanner entrada = new Scanner(cliente.getInputStream());
                            //while(entrada.hasNextLine()){
                          double valor = Double.parseDouble(entrada.nextLine());
                                System.out.println("Celular 03: "+ valor);
                                //}
                                
                                //GERANDO GRAFICO
                                grafico.addValor(valor, tempo, "Celular 03");
                                tempo += 3;
                                grafico.exibeGrafico();
                                
                                //CALCULANDO MEDIA
                                soma += valor;
                                media = soma / indice;
                                indice ++;
                                
                                aparelho3.setText("Monitorado: " + cliente.getInetAddress().getHostAddress() + " - Média: " + media);
                                
                                saida.flush();
                                saida.writeObject(new Date());
                                saida.close();
                                cliente.close();
                        }  
                    }catch(Exception a) {
                         System.out.println("Erro: " + a.getMessage());
                    }
                                
                       }});
                thread3.start();
                
            }
            
        }
        );
        
        painel.add(aparelho1);
        aparelho1.setBounds(30, 80, 350, 40);
        
        painel.add(aparelho2);
        aparelho2.setBounds(30, 140, 350, 40);
        
        painel.add(aparelho3);
        aparelho3.setBounds(30, 200, 350, 40);
        
        add(painel);
        setVisible(true);
        setSize(440, 350);
        setLocation(440, 100);
        setDefaultCloseOperation(EXIT_ON_CLOSE);

    }
    
    public static void main(String[] args) {
        // TODO code application logic here
        
     //INSERIR PORTA MANUALMENTE       
    
     new Servidor_WorkLoadGenerator(); 
            
            /*
    try{
        int port= 12000;    //INSERIR A PORTA A SER USADA PELA CONEXÃO AQUI
        ServerSocket servidor = new ServerSocket(port);
        System.out.println("Server listening on " + port);      
        Socket cliente = servidor.accept();
        System.out.println("Client " + cliente.getInetAddress().getHostAddress() + " connected on " + port); 
    
      
    }catch(IOException e){
        System.out.println("Server Error");
    }

*/
    
}
    
}
