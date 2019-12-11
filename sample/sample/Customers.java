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


public class Customers extends Application {

    public static void main(String[] args) {
        launch(args);
    }
    @Override
    public void start(Stage stage) throws Exception {

        Text txtName = new Text("Name: ");
        Text txtPhone = new Text("Phone ");
        Text txtEmail = new Text("Email");
        Text txtRegister = new Text("Registered");

        TextField textFieldName = new TextField();
        TextField textFieldPhone = new TextField();
        TextField textFieldEmail = new TextField();

        ComboBox comboBoxRegitered = new ComboBox();

        Button btnSaveCustomer = new Button("Save Customer");
        Button btnRemoveCustomer = new Button("Remove Customer");

        GridPane gridPane = new GridPane();

        gridPane.setMinSize(500, 300);

        gridPane.setVgap(10);
        gridPane.setHgap(10);

        gridPane.setAlignment(Pos.CENTER);

        gridPane.add(txtName,0,0);
        gridPane.add(textFieldName,1,0);
        gridPane.add(txtPhone,0,1);
        gridPane.add(textFieldPhone,1,1);
        gridPane.add(txtEmail,0,2);
        gridPane.add(textFieldEmail,1,2);
        gridPane.add(btnSaveCustomer,1,3);

        gridPane.add(txtRegister,0,4);
        gridPane.add(comboBoxRegitered,1,4);
        gridPane.add(btnRemoveCustomer,1,5);

        btnSaveCustomer.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );
        btnRemoveCustomer.setStyle("-fx-background-color: darkslateblue; -fx-text-fill: white" );

        txtPhone.setStyle("-fx-font: normal bold 20px 'serif'");
        txtName.setStyle("-fx-font: normal bold 20px 'serif'");
        txtEmail.setStyle("-fx-font: normal bold 20px 'serif'");
        txtRegister.setStyle("-fx-font: normal bold 20px 'serif'");

        
        gridPane.setStyle("-fx-background-color: beige");

        Scene scene = new Scene(gridPane);

        stage.setTitle("Movie Library System");

        stage.setScene(scene);

        stage.show();
    }

}
