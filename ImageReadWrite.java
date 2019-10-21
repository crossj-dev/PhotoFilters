/*
 * CS1021-081
 * Winter 2018-2019
 * File header contains class ImageIO
 * Name: crossj
 * Created 2/1/2019
 */
package LabNine.LabNine;

import javafx.embed.swing.SwingFXUtils;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;

import java.awt.image.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Scanner;


/**
 * CS1021-081 Winter 2018-2019
 * Class purpose: Reading and Writing images
 *
 * @author crossj
 * @version created on 2/1/2019 at 9:36 AM
 */
public class ImageReadWrite {

    /**
     * Reads a msoe image
     * @param path
     * @return
     * @throws IOException
     * @throws NullPointerException
     */
    public Image readMSOE(Path path) throws IOException, NullPointerException{

        //reads in the file from the corresponding path
        File file = path.toFile();
        Scanner scan = new Scanner(file);
        WritableImage image;

        //makes sure that it is an MSOE image
        if(scan.nextLine().equalsIgnoreCase("msoe") && scan.hasNextLine()){

            int width = Integer.parseInt(scan.next());
            int height = Integer.parseInt(scan.next());

            image = new WritableImage(width, height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color newColor = Color.web(scan.next());
                    image.getPixelWriter().setColor(x, y, newColor);
                }
            }

        } else {
            throw new NullPointerException();
        }
        return image;
    }


    /**
     * writes information to make a msoe image
     * @param image
     * @param path
     * @throws IOException
     * @throws NullPointerException
     */
    public void writeMSOE(Image image, Path path) throws IOException, NullPointerException {
        BufferedImage bufferedImag = SwingFXUtils.fromFXImage(image, (BufferedImage)null);
        PrintWriter print = new PrintWriter(path.toFile());
        PixelReader pixel = image.getPixelReader();

        print.println("MSOE\n");
        int height = bufferedImag.getHeight();
        int width = bufferedImag.getWidth();
        print.println(width + " " + height);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                //getting pixel and getting color
                int color = pixel.getArgb(x, y);

                //adjusting color to a correct format to be written
                int r = 0xFF & ( color >> 16);
                int b = 0xFF & ( color  >> 0 );
                int g = 0xFF & ( color >> 8 );
                String hex = String.format("#%02x%02x%02x", r, g, b);

                //makes sure that spaces and new lines are in the correct places
                if (x == width - 1) {
                    print.println(hex);
                } else {
                    print.print(hex + " ");
                }
            }
        }
        print.close();

    }


    /**
     * writes a .jpg, .png, .tiff image
     * @param path
     * @param image
     * @throws IOException
     */
    public static void writeImage(Path path, Image image) throws IOException {
        BufferedImage bufferedImag = SwingFXUtils.fromFXImage(image, (BufferedImage)null);
        String extension = getExtension(path);
        String[] validExtensions = new String[]{"gif", "jpg", "png", "tiff"};
        if (Arrays.binarySearch(validExtensions, extension) < 0) {
            throw new IllegalArgumentException(extension);
        } else {
            OutputStream out = Files.newOutputStream(path);

            try {
                if (extension.equals("jpg")) {
                    BufferedImage convertedImage = new BufferedImage(bufferedImag.getWidth(), bufferedImag.getHeight(), 5);
                    convertedImage.getGraphics().drawImage(bufferedImag, 0, 0, (ImageObserver)null);
                    convertedImage.getGraphics().dispose();
                    bufferedImag = convertedImage;
                }

                javax.imageio.ImageIO.write(bufferedImag, extension, out);
            } catch (Throwable var9) {
                if (out != null) {
                    try {
                        out.close();
                    } catch (Throwable var8) {
                        var9.addSuppressed(var8);
                    }
                }

                throw var9;
            }

            if (out != null) {
                out.close();
            }

        }
    }

    /**
     * reads a .jpg, .png, .tiff image
     * @param path
     * @return
     * @throws IOException
     */
    public static Image readImage(Path path) throws IOException {
        if (path == null) {
            throw new IllegalArgumentException("path cannot be null");
        } else {
            InputStream in = Files.newInputStream(path);

            WritableImage var2;
            try {
                var2 = SwingFXUtils.toFXImage(javax.imageio.ImageIO.read(in), (WritableImage)null);
            } catch (Throwable var5) {
                if (in != null) {
                    try {
                        in.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }

                throw var5;
            }

            if (in != null) {
                in.close();
            }

            return var2;
        }
    }

    public static Image convolve(Image image, double[] kernel) {
        int[] validKernelSizes = new int[]{1, 4, 9, 16, 25, 36};
        if (Arrays.binarySearch(validKernelSizes, kernel.length) < 0) {
            throw new IllegalArgumentException("The kernel array must be a power of two no greater than 36");
        } else {
            float[] kernelFloats = new float[kernel.length];

            int kernelSize;
            for(kernelSize = 0; kernelSize < kernel.length; ++kernelSize) {
                kernelFloats[kernelSize] = (float)kernel[kernelSize];
            }

            kernelSize = (int)Math.sqrt((double)kernel.length);
            BufferedImageOp op = new ConvolveOp(new Kernel(kernelSize, kernelSize, kernelFloats));
            BufferedImage result = op.filter(SwingFXUtils.fromFXImage(image, (BufferedImage)null), (BufferedImage)null);
            return SwingFXUtils.toFXImage(result, (WritableImage)null);
        }
    }

    /**
     * gets the extension from the path
     * @param path
     * @return
     */
    private static String getExtension(Path path) {
        String filename = path.toString();
        int dotIndex = filename.lastIndexOf(46);
        if (dotIndex != -1 && dotIndex != filename.length() - 1) {
            return filename.substring(dotIndex + 1).toLowerCase();
        } else {
            throw new IllegalArgumentException(path.toString());
        }
    }

    /**
     * reads in bmsoe image files
     * @param path
     * @return
     * @throws IOException
     */
    public Image readBMSOE(Path path) throws IOException {

        FileInputStream in = new FileInputStream(path.toFile());
        DataInputStream fileContents = new DataInputStream(in);
        WritableImage image;

        char B = (char)fileContents.readByte();
        char M = (char)fileContents.readByte();
        char S = (char)fileContents.readByte();
        char O = (char)fileContents.readByte();
        char E = (char)fileContents.readByte();

        boolean checkingIfBMSOEFile = false;
        if(B==66&&M==77&&S==83&&O==79&&E==69){
            checkingIfBMSOEFile = true;
        }

        if(checkingIfBMSOEFile) {

            int width = fileContents.readInt();
            int height = fileContents.readInt();

            image = new WritableImage(width, height);

            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int colorInt = fileContents.readInt();
                    Color newColor = intToColor(colorInt);
                    image.getPixelWriter().setColor(x, y, newColor);
                }
            }


            fileContents.close();

        } else {
            throw new IOException();
        }

        return image;
    }

    /**
     * writes bmsoe image files
     * @param path
     * @param image
     * @throws IOException
     */
    public void writeBMSOE(Path path, Image image) throws IOException{

        OutputStream out = Files.newOutputStream(path);
        DataOutputStream dOut = new DataOutputStream(out);
        PixelReader pixel = image.getPixelReader();

        dOut.writeBytes("BMSOE");

        dOut.writeInt((int)image.getWidth());
        dOut.writeInt((int)image.getHeight());

        for (int y = 0; y < (int)image.getHeight(); y++) {
            for (int x = 0; x < (int)image.getWidth(); x++) {
                //getting pixel and to write the color
                int color = colorToInt(pixel.getColor(x,y));
                dOut.writeInt(color);
            }
        }

        dOut.close();
    }

    /**
     * sets the integer binary number to a color
     * @param color
     * @return
     */
    private static Color intToColor(int color) {
        double red = ((color >> 16) & 0x000000FF)/255.0;
        double green = ((color >> 8) & 0x000000FF)/255.0;
        double blue = (color & 0x000000FF)/255.0;
        double alpha = ((color >> 24) & 0x000000FF)/255.0;
        return new Color(red, green, blue, alpha);
    }

    /**
     * sets the color to an int
     * @param color
     * @return
     */
    private static int colorToInt(Color color) {
        int red = ((int)(color.getRed()*255)) & 0x000000FF;
        int green = ((int)(color.getGreen()*255)) & 0x000000FF;
        int blue = ((int)(color.getBlue()*255)) & 0x000000FF;
        int alpha = ((int)(color.getOpacity()*255)) & 0x000000FF;
        return (alpha << 24) + (red << 16) + (green << 8) + blue;
    }

    /**
     * transforms an image
     * @param image
     * @param transform
     * @return
     * @throws NullPointerException
     */
    public static Image transformImage(Image image, Transformable transform) throws NullPointerException {
        WritableImage writeImage = null;
        try {
            PixelReader pixel = image.getPixelReader();
            writeImage = new WritableImage(pixel, (int) image.getWidth(), (int) image.getHeight());
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    Color color = pixel.getColor(x, y);
                    Color newColor = transform.apply(y, color);
                    writeImage.getPixelWriter().setColor(x, y, newColor);
                }
            }

            return writeImage;
        } catch (NullPointerException npe) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Dialog");
            alert.setHeaderText("Photo Error");
            alert.setContentText("Open Image First");
            alert.showAndWait();

        }

        return writeImage;
    }
}
