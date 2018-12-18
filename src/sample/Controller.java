package sample;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.util.ArrayList;

public class Controller {
    @FXML
    TextArea Text_input;
    @FXML
    TreeView<String> tv;
    @FXML
    TextArea Text_token;

    @FXML
    private void btn_click(){
        Main.OpenFile();
        if(Main.input!=""){
            Text_input.setText(Main.input.substring(0,Main.input.length()-1));
            ArrayList<Token> tokens = Lex.lexAnalysis(Main.input);//词法分析
            Text_token.setText(Lex.tokenResult);
            Lex.tokenResult="";
            Grammar.graAnalysis(tokens);//语法分析
            tv.refresh();
            TreeItem<String> item = new TreeItem<>("Program");
            tv.setRoot(item);
            //PostVisit(Grammar.root);
            item.getChildren().addAll(PostVisit(Grammar.root));
        }
    }
    public static TreeItem<String> PostVisit(TreeNode root){
        TreeItem<String> i1=null;
        TreeItem<String> i2=null;
        TreeItem<String> i3=null;
        TreeItem<String> i4=null;
        if (root.mLeft != null) i1 = PostVisit(root.mLeft);
        if(root.mMiddle != null) i2 = PostVisit(root.mMiddle);
        if(root.mRight != null) i3 = PostVisit(root.mRight);
        if(root.mNext != null) i4 = PostVisit(root.mNext);

        TreeItem<String> it = visit(root);
        if(i1!=null)it.getChildren().add(i1);
            else it.getChildren().add(new TreeItem<>("null"));
        if(i2!=null)it.getChildren().add(i2);
            else it.getChildren().add(new TreeItem<>("null"));
        if(i3!=null)it.getChildren().add(i3);
            else it.getChildren().add(new TreeItem<>("null"));
        if(i4!=null)it.getChildren().add(i4);
            else it.getChildren().add(new TreeItem<>("null"));
        return it;
    }
    public static TreeItem<String> visit(TreeNode node){
        return new TreeItem<>(node.getString());
    }
}
