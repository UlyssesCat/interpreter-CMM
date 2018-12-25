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
    static String dotFormat="";
    static String name="";
    static int i=0;
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
    public static void createDotGraph(String dotFormat,String fileName)
    {
        GraphViz gv=new GraphViz();
        gv.addln(gv.start_graph());
        gv.add(dotFormat);
        gv.addln(gv.end_graph());
        String type = "jpg";  //输出图文件的格式，以.jpg为例
        gv.decreaseDpi();
        gv.decreaseDpi();
        File out = new File(fileName+"."+ type);
        gv.writeGraphToFile( gv.getGraph( gv.getDotSource(), type ), out );
    }
    public static String getformat(TreeNode root) {

        if(root.mLeft!=null)
        {
            dotFormat+=root.name+"->"+root.mLeft.name+";";

            getformat(root.mLeft);

        }
        if(root.mMiddle!=null)
        {
            dotFormat+=root.name+"->"+root.mMiddle.name+";";

            getformat(root.mMiddle);
        }
        if(root.mRight!=null)
        {
            dotFormat+=root.name+"->"+root.mRight.name+";";

            getformat(root.mRight);
        }
        if(root.mNext!=null)
        {
            dotFormat+=root.name+"->"+root.mNext.name+";";
            getformat(root.mNext);
        }
    	/*getformat(root.mLeft);
    	getformat(root.mMiddle);
    	getformat(root.mRight);
    	getformat(root.mNext);*/
        return dotFormat;
    }
    public static void main(String[] args) {
        launch(args);
        getformat(Grammar.root);
        createDotGraph(dotFormat, "DotGraph");
    }
}
