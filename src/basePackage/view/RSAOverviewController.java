package basePackage.view;

import basePackage.MainApp;
import basePackage.model.RSA.AlgorithmRSA;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.*;

public class RSAOverviewController {

    @FXML
    private Label systemMessage;

    @FXML
    private TextArea getText;

    @FXML
    private TextArea encodedText;

    @FXML
    private TextArea sendText;


    private MainApp mainApp;
    private AlgorithmRSA user;

    public RSAOverviewController() {

    }

    @FXML
    private void initialize() {
        systemMessage.setText("Все в порядке");
    }

    @FXML
    private void gen() {
        if (!user.areKeysGenerated()) {
            user.genKeys();
            try (FileWriter out = new FileWriter(new File("src/basePackage/Sender/pass.txt"), false)) {
                out.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
            systemMessage.setText("Ключи успешно сгенерированы!");
        } else {
            systemMessage.setText("Ключи уже сгенерированы");
        }
    }

    @FXML
    private void sendMessage() {
        if (sendText.getText().equals("")) {
            systemMessage.setText("Введите сообщение!");
            return;
        }
        if (!user.areKeysGenerated()) {
            systemMessage.setText("Ключи еще не сгенерированы!");
            return;
        }
        String encodedMessage = user.encoding(sendText.getText(), user.getPublicKey());
        encodedText.setText(encodedMessage);
        try (FileWriter out = new FileWriter(new File("src/basePackage/Sender/pass.txt"), false)) {
            out.write(encodedMessage);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getMessage() {
        if (!user.areKeysGenerated()) {
            systemMessage.setText("Ключи еще не сгенерированы");
            return;
        }
        StringBuilder encodedMessage = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("src/basePackage/Sender/pass.txt"))) {
            String tmp;
            while ((tmp = in.readLine()) != null) {
                encodedMessage.append(tmp).append("\n");
            }
        }
        catch (IOException e) {
            systemMessage.setText("Нет данных для расшифровки!");
            e.printStackTrace();
        }
        if (encodedMessage.toString().equals("null\n")) {
            systemMessage.setText("Файл для расшифровки пуст!");
            return;
        }
        getText.setText(user.decoding(encodedMessage.toString()));
    }

    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    public void initUser() {
        this.user = mainApp.getUser();
    }

}
