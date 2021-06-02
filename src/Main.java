import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.HashMap;

import static javafx.application.Application.launch;

public class Main extends Application {
    public static void main(String[] args) throws Exception {
        launch(args);
    }



    @Override
    public void start(Stage primaryStage) throws Exception {

        Time.setJD(2020, 12, 22, 0, 0, 0);

        double daysec = 60*60*24;
        ArrayList<Double> dayDur = yearRotation(0);
//        Gnomon.tau=0;
//        ArrayList<Double> dayDurSum = yearRotation(1);
//        Gnomon.tau=0;
//        ArrayList<Double> dayDurWin = yearRotation(-1);

        Earth e1 = new Earth(Time.JD*daysec, (Time.JD+1)*daysec, 60, Time.v22122020);
        DormanPrinceIntegrator dormanPrinceIntegrator = new DormanPrinceIntegrator();
        dormanPrinceIntegrator.setPrecision(1e-14);
        dormanPrinceIntegrator.setCorrectStep(false);
        dormanPrinceIntegrator.run(e1);

        Gnomon g1 = new Gnomon(1, e1, 37, 55);
        g1.rotation();



//        dormanPrinceIntegrator.setCorrectStep(false);

//        dormanPrinceIntegrator.run(e1);


        NumberAxis xAxis = new NumberAxis();
        NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Дни");
        yAxis.setLabel("Продолжительность, ч");


        LineChart<Number, Number> lineChart =
                new LineChart<Number, Number>(xAxis, yAxis);

        lineChart.setAxisSortingPolicy(LineChart.SortingPolicy.NONE);
        lineChart.setTitle("Проолжительность светового дня");
        lineChart.setPrefHeight(700);
        lineChart.setPrefWidth(900);
        lineChart.setCreateSymbols(false);
        XYChart.Series series = new XYChart.Series();
        XYChart.Series series1 = new XYChart.Series();
        XYChart.Series series2 = new XYChart.Series();
        series.setName("");
        series1.setName("Продолжительность светового дня только с летним временем");
        series2.setName("Продолжительность светового дня только с зимним временем");

        double t=0;
//        for (int i = 0; i < g1.listsh.size(); i+=1) {
//            series.getData().addAll(new XYChart.Data(g1.listsh.get(i).data[2], g1.listsh.get(i).data[0]));
//        }

        for (int i = 0; i < dayDur.size(); i++) {
            series.getData().addAll(new XYChart.Data(i, dayDur.get(i)));
//            series1.getData().addAll(new XYChart.Data(i, dayDurSum.get(i)));
//            series2.getData().addAll(new XYChart.Data(i, dayDurWin.get(i)));
            t+=1;
        }
        lineChart.getData().addAll(series);

        Group root = new Group(lineChart);

        //Creating a scene object
        Scene scene = new Scene(root, 1000  , 800);

        //Setting title to the Stage
        primaryStage.setTitle("Line Chart");

        //Adding scene to the stage
        primaryStage.setScene(scene);

        //Displaying the contents of the stage
        primaryStage.show();
    }

    public ArrayList<Double> yearRotation (int d) throws Exception
    {
        ArrayList<Double> dayDuration = new ArrayList<>();
        HashMap<Double, Vector> coords = new HashMap<>();
        coords.put(2459022.5, Time.v22062020);
        coords.put(2458929.5, Time.v21032020);
        coords.put(2459115.5, Time.v23092020);
        coords.put(2459205.5, Time.v22122020);

        double daysec = 60*60*24;
        Time.setJD(2458849.5);
        Vector x0 = Time.v01012020;


        DormanPrinceIntegrator dormanPrinceIntegrator = new DormanPrinceIntegrator();
        dormanPrinceIntegrator.setPrecision(1e-14);
        dormanPrinceIntegrator.setCorrectStep(false);
        for (int i = 0; i < 365; i++) {
            if(d == 1)
                Time.localtime = 4; //летнее время всегда
            else if (d == -1)
                Time.localtime = 3; //всегда зимнее время
            else {
                if (i == 89)
                    Time.localtime=4;
                if (i == 298)
                    Time.localtime=3;
            }
            double curday = (Time.JD+1);
            Time.setJD(curday);
            Earth earth = new Earth((Time.JD)*daysec, (Time.JD+1)*daysec, 60, x0);
//            if (coords.containsKey(curday)) {
//                x0 = coords.get(curday);
//                earth.setInitialCondition(x0);
//            }
            dormanPrinceIntegrator.run(earth);
            Gnomon g = new Gnomon(1, earth, 37, 55);
            g.rotation2();
            double daylight = g.daylight;
            dayDuration.add(daylight);
            x0 = earth.x0;
        }

        return dayDuration;
    }

}
