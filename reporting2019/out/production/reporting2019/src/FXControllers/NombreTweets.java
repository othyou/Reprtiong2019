package FXControllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import models.FileParser;

import java.net.URL;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.IsoFields;
import java.util.*;
import java.util.stream.Collectors;

public class NombreTweets implements Initializable {
    public DatePicker datePicker;
    public ComboBox combobox;
    public Label labelnombre;

    ObservableList<String> choices = FXCollections.observableArrayList("par jour"
            , "par semaine", "par mois");

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        initListeners();
        generateSerie();
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
            generateSerie();
        });
        datePicker.valueProperty().addListener((observable, oldValue, newValue) -> generateSerie());

    }


    GregorianCalendar gmt = new GregorianCalendar(TimeZone.getTimeZone("GMT"));

    private void generateSerie() {
        LocalDate date = datePicker.getValue();

        gmt.setTime(Date.from(date.atStartOfDay()
                .atZone(ZoneId.systemDefault())
                .toInstant()));

        Long valueCounts = null;

        if (combobox.getValue().equals(choices.get(0))) {

            valueCounts = FileParser.getTweets().stream().filter(tweet -> gmt.get(Calendar.DAY_OF_YEAR) == tweet.getJour()).
                    collect(Collectors.counting());
        } else if (combobox.getValue().equals(choices.get(1))) {
            valueCounts = FileParser.getTweets().stream().filter(tweet -> date.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == tweet.getSemaine())
                    .collect(Collectors.counting());
        } else if (combobox.getValue().equals(choices.get(2))) {
            valueCounts = FileParser.getTweets().stream().filter(tweet ->
                    gmt.get(Calendar.MONTH) == tweet.getMois()
            ).collect(Collectors.counting());
        }

        System.out.println(valueCounts);


        labelnombre.setText(createData(date.getDayOfMonth() + "/" + date.getMonthValue() + "/" + date.getYear(), Math.toIntExact(valueCounts)));

    }


    private String createData(String str, int nbrAuc) {


        return str + " : " + nbrAuc;
    }


}
