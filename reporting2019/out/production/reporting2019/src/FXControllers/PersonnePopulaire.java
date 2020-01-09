package FXControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import models.FileParser;
import models.Tweet;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

public class PersonnePopulaire implements Initializable {


    public Pane chartContent;
    public DatePicker datePicker;
    public ComboBox combobox;

    BarChart<String, Number> barChart;
    ObservableList<String> choices = FXCollections.observableArrayList("par jour"
            , "par semaine", "par mois");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initListeners();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();

        barChart = new BarChart<>(xAxis, yAxis);
        barChart.setTitle("pas d'information pour cette date");
        updateChart();
        chartContent.getChildren().add(barChart);
    }

    private void initListeners() {

        combobox.setItems(choices);
        combobox.setValue(choices.get(0));

        LocalDate minDate = LocalDate.of(2019, 1, 1);
        LocalDate maxDate = LocalDate.of(2019, 12, 31);
        datePicker.setDayCellFactory(d ->
                new DateCell() {
                    @Override
                    public void updateItem(LocalDate item, boolean empty) {
                        super.updateItem(item, empty);
                        setDisable(item.isAfter(maxDate) || item.isBefore(minDate));
                    }
                });
        datePicker.setValue(minDate);

        combobox.valueProperty().addListener((observable, oldValue, newValue) -> {
            updateChart();
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> updateChart());

    }

    public void updateChart() {
        barChart.getData().clear();
        barChart.getData().add(generateSerie());

    }

    GregorianCalendar gmt = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private XYChart.Series<String, Number> generateSerie() {
        LocalDate date = datePicker.getValue();

        gmt.setTime(Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));

        List<String> collect = null;
        Map<String, Long> valueCounts = null;

        if (combobox.getValue().equals(choices.get(0))) {

            valueCounts = FileParser.getTweets().stream().
                    filter(tweet -> gmt.get(Calendar.DAY_OF_YEAR) == tweet.getJour() && tweet.getHashTag() != null)
                    .collect(Collectors.groupingBy(Tweet::getHashTag, Collectors.counting()));

        } else if (combobox.getValue().equals(choices.get(1))) {
            valueCounts = FileParser.getTweets().stream()
                    .filter(tweet -> date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == tweet.getSemaine() && tweet.getHashTag() != null)
                    .collect(Collectors.groupingBy(Tweet::getHashTag, Collectors.counting()));

        } else if (combobox.getValue().equals(choices.get(2))) {
            valueCounts = FileParser.getTweets().stream()
                    .filter(tweet -> gmt.get(Calendar.MONTH) == tweet.getMois() && tweet.getHashTag() != null
                    ).collect(Collectors.groupingBy(Tweet::getHashTag, Collectors.counting()));
        }

        System.out.println(valueCounts.size());
        collect = valueCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Long>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());


        XYChart.Series series1 = new XYChart.Series();

        for (int i = 0; i < 10 && i < collect.size(); i++)
            series1.getData().add(createData(collect.get(i), Math.toIntExact(valueCounts.get(collect.get(i)))));


        if (!collect.isEmpty())
            barChart.setTitle("** " + collect.get(0) + " **");
        else
            barChart.setTitle("pas d'information pour cette date");

        return series1;
    }

    private XYChart.Data createData(String str, int nbrAuc) {

        XYChart.Data data = new XYChart.Data(str, nbrAuc);

        StackPane node = new StackPane();
        Label label = new Label(str + " - " + nbrAuc);
        label.setRotate(-90);
        Group group = new Group(label);
        StackPane.setAlignment(group, Pos.BOTTOM_CENTER);
        StackPane.setMargin(group, new Insets(0, 0, 5, 0));
        node.getChildren().add(group);
        data.setNode(node);

        return data;
    }
}
