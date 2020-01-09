package Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import models.FileParser;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("../FXViews/MainView.fxml"));
        primaryStage.setTitle("Reporting");
        primaryStage.setScene(new Scene(root, 640, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {

        // deplacer les fichier climat.txt et Foot.txt dans le Dossier REPORT

        FileParser.load(
                30000    /** de Climat **/
                ,
                30000  /** de Foot **/
        );
        launch(args);


    }
}
