package sample;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class Rentals extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage stage) throws Exception {
        Text txtCustomer = new Text("Customer: ");
        Text txtGenre = new Text("Genre: ");
        Text txtMovies = new Text("Movies: ");
        Text txtBorrowed = new Text("Borrowed: ");
        Text txtReturned = new Text("Returned: ");

        ComboBox comboBoxCustomer = new ComboBox();
        ComboBox comboBoxGenre = new ComboBox();
        ComboBox comboBoxMovies = new ComboBox();
        ComboBox comboBoxBorrowed = new ComboBox();
        ComboBox comboBoxReturned = new ComboBox();

        Button btnSaveRental = new Button("Save Rental");
        Button btnReturnMovie = new Button("Return Rental");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(500, 400);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(txtCustomer,0,0);
        gridPane.add(comboBoxCustomer,1,0);

        gridPane.add(txtGenre,0,1);
        gridPane.add(comboBoxGenre,1,1);

        gridPane.add(txtMovies,0,2);
        gridPane.add(comboBoxMovies,1,2);

        gridPane.add(btnSaveRental,1,3);

        gridPane.add(txtBorrowed,0,4);
        gridPane.add(comboBoxBorrowed,1,4);

        gridPane.add(btnReturnMovie,1,5);

        gridPane.add(comboBoxReturned,1,6);

        btnSaveRental.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );
        btnReturnMovie.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );

        txtCustomer.setStyle("-fx-font: normal bold 20px 'serif'");
        txtGenre.setStyle("-fx-font: normal bold 20px 'serif'");
        txtMovies.setStyle("-fx-font: normal bold 20px 'serif'");
        txtBorrowed.setStyle("-fx-font: normal bold 20px 'serif'");
        txtReturned.setStyle("-fx-font: normal bold 20px 'serif'");


        gridPane.setStyle("-fx-background-color: beige");

        Scene scene = new Scene(gridPane);

        stage.setTitle("Movie Library System");

        stage.setScene(scene);

        stage.show();

    }

   

}
