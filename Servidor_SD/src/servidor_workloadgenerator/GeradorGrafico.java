/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor_workloadgenerator;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JFrame;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.labels.StandardPieSectionLabelGenerator;
import org.jfree.chart.plot.PiePlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

/**
 *
 * @author Helbert Monteiro
 */
public class GeradorGrafico {
    
    private final JFrame                 frame     = new JFrame("Valor Atual"),
                                         frame2    = new JFrame("Valor Médio");
    private DefaultCategoryDataset       dadoBarra = new DefaultCategoryDataset();;
    private final DefaultPieDataset      dadoPizza = new DefaultPieDataset();
    private JFreeChart                   graficoBarra,
                                         graficoPizza;
    private PiePlot                      piePlot;
    private ChartPanel                   chartPanelBarra,
                                         chartPanelPizza;
    
    Toolkit toolkit     = Toolkit.getDefaultToolkit();
    Dimension dimension = toolkit.getScreenSize();
    
    public void addValor(double decibeis, int tempo, String linha, double media){
        dadoBarra.addValue(decibeis, linha, tempo + "s");
        dadoPizza.setValue(linha, media);
    }
    
    private void criaGrafico(){
        graficoBarra = ChartFactory.createBarChart("Valor atual", "Tempo", "Decibeis", dadoBarra, PlotOrientation.VERTICAL, true, true, false);
        graficoPizza = ChartFactory.createPieChart("Valor médio", dadoPizza, true, true, false);
        piePlot = (PiePlot) graficoPizza.getPlot();
        piePlot.setLabelGenerator(new StandardPieSectionLabelGenerator("{0} ({2})"));
    }
    
    public void exibeGrafico(){
        criaGrafico();
        
        chartPanelBarra = new ChartPanel(graficoBarra);
        chartPanelPizza = new ChartPanel(graficoPizza);
        
        frame.add(chartPanelBarra);
        frame2.add(chartPanelPizza);
        
        frame.setBounds(0, (int) dimension.getHeight() - 500, 0, 0);
        frame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
        
        frame2.setBounds((int) dimension.getWidth() - 685, (int) dimension.getHeight() - 500, 0, 0);
        frame2.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        frame2.pack();
        frame2.setVisible(true);
    }
    
}
