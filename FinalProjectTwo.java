package LabNine.LabNine;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Creates the image manipulator application
 */
public class FinalProjectTwo extends Application {


    @Override
    public void start(Stage primaryStage) throws Exception{

        //loads in the fxml to be displayed and controlled
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("fxml.fxml"));
        Parent root = load.load();


        //sets the title and scene of the primary stage
        primaryStage.setTitle("Image Manipulator");
        primaryStage.setScene(new Scene(root));


        //loads in the fxml to be displayed and controlled
        Stage subStage = new Stage();
        FXMLLoader load2 = new FXMLLoader();
        load2.setLocation(getClass().getResource("otherstage.fxml"));
        Parent root2 = load2.load();

        //set coor. of the second stage, sets the title, sets the scene to show within the second stage
        subStage.setX(200);
        subStage.setY(300);

        subStage.setTitle("Filter Kernel");
        subStage.setScene(new Scene(root2));

        //initializes the controller and stage to allow for stages to communicate to eachother
        Lab9Controller controller = load.getController();
        controller.setStage(subStage);
        ControllerOtherStage controllerOtherStage = load2.getController();
        controllerOtherStage.setController(controller);

        primaryStage.show();

    }


    public static void main(String[] args) {
        launch(args);
    }
}
