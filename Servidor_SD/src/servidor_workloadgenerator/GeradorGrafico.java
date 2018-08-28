/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_workloadgenerator;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

/**
 *
 * @author Helbert Monteiro
 */
public class GeradorGrafico {
    
    private DefaultCategoryDataset dado = new DefaultCategoryDataset();
    private JFreeChart             grafico;
    private OutputStream           arquivo;
    private ChartUtilities         chartUtilities;
    private ChartPanel             chartPanel;
    private JFrame                 frame = new JFrame();
    
    public void addValor(double y, int x, String linha){
        dado.addValue(y, linha, x + "s");
    }
    
    private void criaGrafico(){
        grafico = ChartFactory.createBarChart("Gráfico", "Tempo", "Decibeis", dado, PlotOrientation.VERTICAL, true, true, false);
    }
    
    public void exibeGrafico(){
        criaGrafico();
        
        chartPanel = new ChartPanel(grafico);
        
        frame.add(chartPanel);
        
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }
    
}
