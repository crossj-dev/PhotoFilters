package LabNine.LabNine;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * controller for the main stage
 */
public class Lab9Controller {

    @FXML
    private Button openButton;
    @FXML
    private Button saveButton;
    @FXML
    private Button reloadButton;
    @FXML
    private Button grayScaleButton;
    @FXML
    private Button negativeButton;
    @FXML
    private Button redButton;
    @FXML
    private Button redGrayButton;
    @FXML
    private Button showFilterButton;
    @FXML
    private ImageView imageView;
    private Image image;
    private Image currentImage;
    private Stage subStage;
    private ImageReadWrite ImageIO = new ImageReadWrite();

    /**
     * Loads a selected image file
     */
    public void load(){
        openButton.setOnMouseClicked(e->{
            try {

                FileChooser file = new FileChooser();
                file.getExtensionFilters().add(
                        new FileChooser.ExtensionFilter("Image", "*.jpg", "*.tiff",
                                "*.png", "*.bmsoe", "*.msoe"));

                String fileName = System.getProperty("user.dir") + File.separator;
                file.setInitialDirectory(new File(fileName));
                file.setTitle("Choose a File: ");
                File fileChosen = file.showOpenDialog(null);


                Path path = Paths.get(fileChosen.getPath());
                ImageReadWrite imageReadWrite = new ImageReadWrite();


                if(path.toString().endsWith(".msoe")) {
                    image = imageReadWrite.readMSOE(path);
                } else if (path.toString().endsWith(".bmsoe")) {
                    image = imageReadWrite.readBMSOE(path);
                } else {
                    image = imageReadWrite.readImage(path);
                }

                currentImage = image;
                imageView.setImage(image);



            } catch (IOException io){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("Need correct image contents");
                alert.showAndWait();
            } catch (NullPointerException npe){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("Must select Image with .jpg, .png, .tiff, .msoe, or .bmose extension " +
                        "and in correct format");
                alert.showAndWait();
            } catch (IllegalArgumentException ill){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("File format must be .jpg, .png, .tiff, .msoe, or .bmose" +
                        " and in correct format");
                alert.showAndWait();
            }
        });
    }

    /**
     * Saves the current image the user has open
     */
    public void save(){
        saveButton.setOnMouseClicked(e -> {
            try {
                FileChooser file = new FileChooser();
                file.getExtensionFilters().addAll(
                        new FileChooser.ExtensionFilter("jpg", "*.jpg"),
                        new FileChooser.ExtensionFilter("tiff", "*.tiff"),
                        new FileChooser.ExtensionFilter("png", "*.png"),
                        new FileChooser.ExtensionFilter("bmsoe", "*.bmsoe"),
                        new FileChooser.ExtensionFilter("msoe", "*.msoe"));
                file.setSelectedExtensionFilter(file.getSelectedExtensionFilter());
                String fileName = System.getProperty("user.dir") + File.separator;
                file.setInitialDirectory(new File(fileName));
                file.setTitle("Choose a Save Location: ");
                File folderChosen = file.showSaveDialog(null);

                ImageReadWrite im = new ImageReadWrite();

                if(folderChosen.getPath().endsWith(".msoe")) {
                    im.writeMSOE(imageView.getImage(),Paths.get(folderChosen.getPath()));
                } else if (folderChosen.getPath().endsWith(".bmsoe")) {
                    im.writeBMSOE(Paths.get(folderChosen.getPath()),imageView.getImage());
                } else {
                    im.writeImage(Paths.get(folderChosen.getPath()), imageView.getImage());
                }
            } catch (IOException io){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("File format must be .jpg, .png, .tiff, or .msoe");
                alert.showAndWait();
            } catch (NullPointerException npe){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("Select place to save image or open image first");
                alert.showAndWait();
            } catch (IllegalArgumentException ill){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Dialog");
                alert.setHeaderText("Photo Error");
                alert.setContentText("Image not in correct format, need to type name with wanted .extension" +
                        " (.jpg, .png, .tiff, or .msoe)");
                alert.showAndWait();
            }

        });
    }

    /**
     * Reloads the first loaded image
     */
    public void reload(){
        try {
            reloadButton.setOnMouseClicked(e -> {
                imageView.setImage(currentImage);
            });
        } catch (NullPointerException npe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * Modifies the image to become GrayScale
     */
    public void grayScale(){
        try {
            grayScaleButton.setOnMouseClicked(e -> imageView.setImage(ImageIO.transformImage(image, (y, color) -> color.grayscale())));
        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * Modifies the image to become inverted
     */
    public void negative() {
        try {
            negativeButton.setOnMouseClicked(e -> imageView.setImage(ImageIO.transformImage(image, (y, color) -> color.invert())));
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * Modifies the image to become red
     */
    public void red(){
        try {
            redButton.setOnMouseClicked(e -> imageView.setImage(ImageIO.transformImage(image, (y, color) -> Color.color(color.getRed(), 0, 0))));
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * Modifies the image to become red-gray
     */
    public void redGray(){
        Transformable redGray = ((y, color) -> {
            Color newColor;
            if (y % 2 == 0) {
                newColor = color.grayscale();
            } else {
                newColor = Color.color(color.getRed(), 0, 0);
            }
            return newColor;
        });
        try {
            redGrayButton.setOnMouseClicked(e -> imageView.setImage(ImageIO.transformImage(image, redGray)));
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * shows the kernel stage
     */
    public void showFilter(){
        //changes the button text to opposite when showFilter button is clicked
        showFilterButton.setOnMouseClicked(e-> {
            if (showFilterButton.getText().equalsIgnoreCase("show filter")) {
                showFilterButton.setText("Hide Filter");
                subStage.show();

            } else if (showFilterButton.getText().equalsIgnoreCase("hide filter")) {
                showFilterButton.setText("Show Filter");
                subStage.close();
            }
        });

    }

    /**
     * Modifies the image to what the user chose within the kernel stage
     * @param kernel
     */
    public void applyKernel(double[] kernel){
        try {
            Image modifiedImage = ImageIO.convolve(image, kernel);
            imageView.setImage(modifiedImage);
        } catch (NullPointerException npe){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open image first");
            alert.showAndWait();
        }
    }

    /**
     * sets the stage so that the kernel stage can be loaded
     * @param s
     */
    public void setStage(Stage s){
        this.subStage = s;
    }



}


