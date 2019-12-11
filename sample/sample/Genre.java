package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;


public class Genre extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception{

        Text text1 = new Text("Name: ");
        Text text2 = new Text("Registered: ");

        TextField textField1 = new TextField();

        ComboBox comboBox = new ComboBox();

        Button button1 = new Button("Save");
        Button button2 = new Button("Remove");

        GridPane gridPane = new GridPane();


        gridPane.setMinSize(600, 400);
        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(text1,0,0);
        gridPane.add(textField1,1,0);
        gridPane.add(button1,1,1);

        gridPane.add(text2,0,2);
        gridPane.add(comboBox,1,2);

        gridPane.add(button2,1,3);

        button1.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );
        button2.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );

        text1.setStyle("-fx-font: normal bold 20px 'serif'");
        text2.setStyle("-fx-font: normal bold 20px 'serif'");

        gridPane.setStyle("-fx-background-color: beige");

        Scene scene = new Scene(gridPane);

        stage.setTitle("Movie Library System");

        stage.setScene(scene);

        stage.show();

    }

}
