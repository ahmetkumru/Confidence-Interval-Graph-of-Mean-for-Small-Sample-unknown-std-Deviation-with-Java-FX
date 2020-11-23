/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package javafxapplication1;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import org.apache.commons.math3.distribution.TDistribution;

/**
 *
 * @author ahmet
 */
public class FXMLDocumentController implements Initializable {

    @FXML
    private LineChart chart;
    @FXML
    private TextField mean;

    @FXML
    private TextField confidenceİnterval;

    @FXML
    private TextField sd;

    @FXML
    private TextField n;

    @FXML
    private Button button;

    @FXML
    private NumberAxis x;

    @FXML
    private NumberAxis y;

    @FXML
    private Text lower;

    @FXML
    private Text upper;

    @FXML
    private Text margin;
    
      @FXML
    private Text tValue;

    XYChart.Series s1 = new XYChart.Series();
    XYChart.Series s2 = new XYChart.Series();
    XYChart.Series s3 = new XYChart.Series();

    public double editDouble(double number) {
        String newNumber = "";
        String exNumber = String.valueOf(number);
        int lenghtCounter = exNumber.length();
        for (int j = 0; j < lenghtCounter; j++) {
            if (j < exNumber.length()) {
                if (exNumber.charAt(j) == '.') {
                    lenghtCounter = j + 4;
                }
                newNumber += exNumber.charAt(j);
            }
        }
        return Double.valueOf(newNumber);
    }

    public void drawLine(double sample, double begining, double finish) {
        TDistribution d1 = new TDistribution(sample - 1);
        XYChart.Series series1 = new XYChart.Series<>();
        XYChart.Series series2 = new XYChart.Series<>();
        series1.getData().add(new XYChart.Data<>(begining, d1.density(begining)));
        series1.getData().add(new XYChart.Data<>(begining, 0));

        series2.getData().add(new XYChart.Data<>(finish, d1.density(finish)));
        series2.getData().add(new XYChart.Data<>(finish, 0));

        chart.getData().addAll(series1, series2);
    }

    public void drawGraph() {
        double ortalama = Double.parseDouble(mean.getText());
        double conf = Double.parseDouble(confidenceİnterval.getText());
        double standart = Double.parseDouble(sd.getText());
        double sample = Double.parseDouble(n.getText());

        double alpha = (100 - conf) / 100;
        double alphabolu2 = alpha / 2;

        TDistribution d1 = new TDistribution(sample - 1);
        double t = d1.inverseCumulativeProbability((1 - alphabolu2));

        double üstDeger = ortalama + (t * (standart / Math.sqrt(sample)));
        double altDeger = ortalama - (t * (standart / Math.sqrt(sample)));

        upper.setText("Upper Bound : " + editDouble(üstDeger));
        lower.setText("Lower Bound : " + editDouble(altDeger));
        margin.setText("Margin of Error (E) : " + editDouble(t * (standart / Math.sqrt(sample))));
        tValue.setText("t(α/2 : )"+editDouble(t));

        for (double i = -5; i <= 5; i += 0.1) {
            s1.getData().add(new XYChart.Data<>(i, d1.density(i)));

        }

        int x = (int) ((100 - conf) / 2);

        if (0 < conf && conf < 10) {
            drawLine(sample, -0.25, 0.25);
        } else if (10 <= conf && conf < 20) {
            drawLine(sample, -0.35, 0.35);
        } else if (20 <= conf && conf < 30) {
            drawLine(sample, -0.45, 0.45);
        } else if (30 <= conf && conf < 40) {
            drawLine(sample, -0.55, 0.55);
        } else if (40 <= conf && conf < 50) {
            drawLine(sample, -0.75, 0.75);
        } else if (50 <= conf && conf < 60) {
            drawLine(sample, -0.90, 0.90);
        } else if (60 <= conf && conf < 70) {
            drawLine(sample, -1.15, 1.15);
        } else if (70 <= conf && conf < 80) {
            drawLine(sample, -1.5, 1.5);
        } else if (80 <= conf && conf < 90) {
            drawLine(sample, -2, 2);

        } else if (90 <= conf && conf < 95) {
            drawLine(sample, -2.5, 2.5);
        } else if (95 <= conf && conf <= 100) {
            drawLine(sample, -3, 3);
        }

        chart.getData().add(s1);

    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        button.setOnMousePressed((event) -> {

            s1.getData().clear();
            s2.getData().clear();
            s3.getData().clear();

            drawGraph();
        });

        chart.setCreateSymbols(false);

        chart.setLegendVisible(false);

    }

}
