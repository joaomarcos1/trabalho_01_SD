package servidor_workloadgenerator;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Servidor_WorkLoadGenerator extends JFrame{
    
    private final JPanel painel = new JPanel();
    private JLabel aparelho1 = new JLabel("Aparelho 01"), 
                   aparelho2 = new JLabel("Aparelho 02"), 
                   aparelho3 = new JLabel("Aparelho 03");
    private final JButton monitorar = new JButton("Iniciar Monitoramento");
    private Thread thread1, thread2, thread3;
    
    public Servidor_WorkLoadGenerator(){
        monitorar.setBounds(30, 30, 300, 40);
        monitorar.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e){
                thread1 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new CriarThread().run("Celular 01", aparelho1, 9002);
                    }});
                thread1.start();
                
                thread2 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new CriarThread().run("Celular 02", aparelho2, 9003);
                    }});
                thread2.start();
                
                thread3 = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        new CriarThread().run("Celular 03", aparelho3, 9004);
                }});
                thread3.start();
            }
        });
        
        aparelho1.setBounds(30, 80, 350, 40);
        aparelho2.setBounds(30, 140, 350, 40);
        aparelho3.setBounds(30, 200, 350, 40);
        
        painel.add(monitorar);
        painel.add(aparelho1);
        painel.add(aparelho2);
        painel.add(aparelho3);
        
        painel.setLayout(null);
        painel.setBackground(Color.white);
        
        add(painel);
        setTitle("Servidor de Monitoramento");
        setVisible(true);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }
    
    public static void main(String[] args) {
        new Servidor_WorkLoadGenerator();
    }   
    
}