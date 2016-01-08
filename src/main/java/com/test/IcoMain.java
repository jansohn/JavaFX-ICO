package com.test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class IcoMain extends Application
{
    private static final Logger log = LoggerFactory.getLogger(IcoMain.class);
    private Stage primaryStage = null;
    private BorderPane rootLayout = null;

    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage)
    {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("JavaFX-ICO");

//        this.primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream("images/vkontakte.png")));
        try
        {
            this.primaryStage.getIcons().addAll(convertIcoToFxImages("images/vkontakte.ico"));
        }
        catch (IOException | URISyntaxException e)
        {
            log.error(e.getMessage(), e);
        }

        this.initRootLayout();
    }

    /**
     * Initializes the root layout.
     */
    public void initRootLayout()
    {
        try
        {
            // Load root layout from fxml file.
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(IcoMain.class.getResource("view/RootView.fxml"));
            this.rootLayout = (BorderPane) loader.load();

            // Show the scene containing the root layout.
            Scene scene = new Scene(this.rootLayout);
            this.primaryStage.setScene(scene);
            this.primaryStage.show();
        }
        catch (IOException e)
        {
            log.error(e.getMessage(), e);
        }
    }

    private List<Image> convertIcoToFxImages(String icoPath) throws IOException, URISyntaxException
    {
        Iterator<ImageReader> readers = ImageIO.getImageReadersByFormatName("ICO");
        while (readers.hasNext())
        {
            log.info("reader: {}", readers.next());
        }

        ImageReader reader = null;

        try (ImageInputStream imageInputStream = ImageIO.createImageInputStream(getClass().getClassLoader().getResourceAsStream(icoPath)))
        {
            reader = ImageIO.getImageReaders(imageInputStream).next();

            reader.setInput(imageInputStream);
            int count = reader.getNumImages(true);

            List<Image> fxImages = new ArrayList<>(count);

            for (int i = 0; i < count; i++)
            {
                BufferedImage bufferedImage = reader.read(i, null);
                Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);
                fxImages.add(fxImage);
            }

            return fxImages;
        }
        finally
        {
            if (reader != null)
            {
                reader.dispose();
            }
        }
    }

}
