/*
 * CS1021-081
 * Winter 2018-2019
 * File header contains class ControllerOtherStage
 * Name: crossj
 * Created 2/6/2019
 */
package LabNine.LabNine;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;

import java.io.IOException;

/**
 * Controller for the kernel set
 */
public class ControllerOtherStage {

    @FXML
    TextField t1;
    @FXML
    TextField t2;
    @FXML
    TextField t3;
    @FXML
    TextField t4;
    @FXML
    TextField t5;
    @FXML
    TextField t6;
    @FXML
    TextField t7;
    @FXML
    TextField t8;
    @FXML
    TextField t9;
    @FXML
    Button applyButton;
    @FXML
    Button blurButton;
    @FXML
    Button sharpenButton;
    Lab9Controller controller;


    /**
     * sets the image to be blurred
     */
    public void blur() {
        //sets all the text within the text fields
                t1.setText("0");
                t2.setText("1");
                t3.setText("0");
                t4.setText("1");
                t5.setText("5");
                t6.setText("1");
                t7.setText("0");
                t8.setText("1");
                t9.setText("0");

                //creates the blur effect
                double[] blur = { 0.0,  1.0/9,  0.0,
                          1.0/9, 5.0/9, 1.0/9,
                           0.0,  1.0/9,  0.0};

                //applies effect
                controller.applyKernel(blur);
    }

    /**
     * sets the image to be sharpened
     */
    public void sharpen(){
        //sets all the text within the text fields
                t1.setText("0");
                t2.setText("-1");
                t3.setText("0");
                t4.setText("-1");
                t5.setText("5");
                t6.setText("-1");
                t7.setText("0");
                t8.setText("-1");
                t9.setText("0");

                //creates the sharpen effect
                double[] sharpen = {0.0, -1.0, 0.0,
                                -1.0, 5.0, -1.0,
                                 0.0, -1.0, 0.0};

                //applies effect
                controller.applyKernel(sharpen);
    }

    /**
     * controls apply button and sets the image to what the user wants
     */
    public void apply() {
        try {
            //gets the values within the textfields
            double[] kernel2 = new double[9];
            double t1Double = Double.parseDouble(t1.getText());
            double t2Double = Double.parseDouble(t2.getText());
            double t3Double = Double.parseDouble(t3.getText());
            double t4Double = Double.parseDouble(t4.getText());
            double t5Double = Double.parseDouble(t5.getText());
            double t6Double = Double.parseDouble(t6.getText());
            double t7Double = Double.parseDouble(t7.getText());
            double t8Double = Double.parseDouble(t8.getText());
            double t9Double = Double.parseDouble(t9.getText());

            double sum = t1Double + t2Double + t3Double + t4Double + t5Double
                    + t6Double + t7Double + t8Double + t9Double;

            //checks to make sure values add up to a positive #
            if (sum > 0) {

                kernel2[0] = t1Double;
                kernel2[1] = t2Double;
                kernel2[2] = t3Double;
                kernel2[3] = t4Double;
                kernel2[4] = t5Double;
                kernel2[5] = t6Double;
                kernel2[6] = t7Double;
                kernel2[7] = t8Double;
                kernel2[8] = t9Double;

                //applies effect
                controller.applyKernel(kernel2);

            } else {
                throw new IOException();
            }
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Kernel Error");
            alert.setContentText("Not supported set of weights, must add up to be positive #");
            alert.showAndWait();
        }
    }

    /**
     * sets the Lab9Controller to be used by the other stage and transfer data kernel
     * @param controller
     */
    public void setController(Lab9Controller controller){
            this.controller = controller;
        }


}

