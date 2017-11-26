package basePackage;

import basePackage.view.RSAOverviewController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import basePackage.model.RSA.AlgorithmRSA;

import java.io.IOException;

public class MainApp extends Application {

    private Stage primaryStage;
    private BorderPane rootLayout;

    private AlgorithmRSA user;

    public MainApp() {
        user = new AlgorithmRSA(10);
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        this.primaryStage.setTitle("RSA App");

        initRootLayout();
        showRSAOverview();
    }

    /**
     * Инициализация коневого макета
     */

    public void initRootLayout() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/rootLayout.fxml"));
            rootLayout = loader.load();

            Scene scene = new Scene(rootLayout);
            primaryStage.setScene(scene);
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Показ панели управления для шифрования
     */

    public void showRSAOverview() {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource("view/sample.fxml"));
            AnchorPane anchor = loader.load();

            rootLayout.setCenter(anchor);
            RSAOverviewController controller = loader.getController();
            controller.setMainApp(this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Возвращает главную сцену
     * @return внешний слой
     */
    public Stage getPrimaryStage() {
        return primaryStage;
    }

    public AlgorithmRSA getUser() {
        return user;
    }
}
