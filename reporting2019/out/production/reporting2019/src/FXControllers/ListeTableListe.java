package FXControllers;

import javafx.collections.transformation.FilteredList;
import javafx.fxml.Initializable;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import models.FileParser;
import models.Tweet;

import java.lang.reflect.Field;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

public class ListeTableListe implements Initializable {

    public TextField filterTextField;

    public TableView<Tweet> tableView;

    FilteredList<Tweet> filteredList;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        tableView.getColumns().addAll(tableColumns());


        filteredList = new FilteredList<>(FileParser.getTweets());

        tableView.setItems(filteredList);
        filterTextField.textProperty().addListener((o, old, newv) -> {
            filteredList.setPredicate(tweet -> {
                if (newv == null || newv.isEmpty())
                    return true;
                String lowerCaseFilter = newv.toLowerCase();
                if (tweet.getUsername().toLowerCase().contains(lowerCaseFilter))
                    return true;
                else if (tweet.getTweetContent().toLowerCase().contains(lowerCaseFilter))
                    return true;
                return false;
            });
        });
    }


    /*************************************************************/

    public List<TableColumn<Tweet, String>> tableColumns() {
        List<TableColumn<Tweet, String>> list = new ArrayList<>();
        for (Field declaredField : Tweet.class.getDeclaredFields()) {
            if (declaredField.getType() == SimpleDateFormat.class)
                continue;
            if (declaredField.getType() == GregorianCalendar.class)
                continue;
            TableColumn<Tweet, String> cols = new TableColumn<>(declaredField.getName());
            cols.setCellValueFactory(new PropertyValueFactory<Tweet, String>(declaredField.getName()));
            list.add(cols);
        }
        return list;
    }
}
