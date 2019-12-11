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

public class Movie extends Application {

    public static void main(String[] args) {
        launch(args);
    }	

    @Override
    public void start(Stage stage) throws Exception {

        Text txtGenres = new Text("Genres: ");
        Text txtName = new Text("Name: ");
        Text txtRegistered = new Text("Registered: ");

        ComboBox comboBoxGenre = new ComboBox();
        ComboBox comboBoxName = new ComboBox();

        TextField tfName = new TextField();

        Button btnSave = new Button("Save Movie");
        Button btnRemove = new Button("Remove Movie");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(500, 300);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(txtGenres,0,0);
        gridPane.add(comboBoxGenre,1,0);
        gridPane.add(txtName,0,1);
        gridPane.add(tfName,1,1);
        gridPane.add(btnSave,1,3);
        gridPane.add(txtRegistered,0,4);
        gridPane.add(comboBoxName,1,4);
        gridPane.add(btnRemove,1,5);

        btnSave.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );
        btnRemove.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );

        txtGenres.setStyle("-fx-font: normal bold 20px 'serif'");
        txtName.setStyle("-fx-font: normal bold 20px 'serif'");
        txtRegistered.setStyle("-fx-font: normal bold 20px 'serif'");


        gridPane.setStyle("-fx-background-color: beige");

        Scene scene = new Scene(gridPane);

        stage.setTitle("Movie Library System");

        stage.setScene(scene);

        stage.show();
    }

   
}
