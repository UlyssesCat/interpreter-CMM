package sample;
import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;

import static sample.Lex.readTxtFile;

public class Main extends Application {

    static Stage  stage;
    static String input="";
    @Override
    public void start(Stage primaryStage) throws Exception{
        Parent root = FXMLLoader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 862, 620));
        primaryStage.show();
        stage = primaryStage;
    }

    public static void OpenFile(){

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Resource File");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("CMM", "*.cmm"),new FileChooser.ExtensionFilter("Text Files", "*.txt"));
        fileChooser.setInitialDirectory(new File("E://"));
        File selectedFile = fileChooser.showOpenDialog(stage);
        if (selectedFile != null) {
            input = "";
            String pathName = selectedFile.getPath();
            input = readTxtFile(pathName);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
