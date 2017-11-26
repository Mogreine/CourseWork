package basePackage.view;

import basePackage.MainApp;
import basePackage.model.RSA.AlgorithmRSA;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import java.io.*;

public class RSAOverviewController {

    @FXML
    private Label systemMessage;

    @FXML
    private TextArea getText;

    @FXML
    private TextArea sendText;

    @FXML
    private ProgressIndicator indicator;

    private MainApp mainApp;
    private AlgorithmRSA user;

    public RSAOverviewController() {

    }

    @FXML
    private void initialize() {
        user = mainApp.getUser();
        systemMessage.setText("Все в порядке");
    }

    @FXML
    private void generate() {
        user.genKeys();
    }

    @FXML
    private void sendMessage() {
        if (sendText.getText().equals("")) {
            systemMessage.setText("Введите сообщение!");
            return;
        }
        String encodedMessage = user.encoding(sendText.getText(), user.getPublicKey());
        try (FileWriter out = new FileWriter(new File("/Sender/pass.txt"), false)) {
            out.write(encodedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getMessage() {
        StringBuilder encodedMessage = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("/Sender/pass.txt"))) {
            while (in.readLine() != null) {
                encodedMessage.append(in.readLine()).append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (encodedMessage.toString().equals("")) {
            systemMessage.setText("Файл для расшифровки пуст!");
            return;
        }
        getText.setText(encodedMessage.toString());
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

}
