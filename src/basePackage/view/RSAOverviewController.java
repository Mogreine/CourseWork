package basePackage.view;

import basePackage.MainApp;
import basePackage.model.RSA.AlgorithmRSA;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextArea;

import java.io.*;
import java.util.StringTokenizer;
import java.util.concurrent.Exchanger;

public class RSAOverviewController {

    @FXML
    private Label systemMessage;

    @FXML
    private TextArea getText;

    @FXML
    private TextArea sendText;

    @FXML
    private ProgressIndicator indicator;

    private class GenThread extends Thread {
        String message;
        Exchanger<String> ex;

        public GenThread(Exchanger<String> ex) {
            this.ex = ex;
        }

        @Override
        public void run() {
            try {
                user.genKeys();
                message = "Ключи сгенерированы";
            } catch (InterruptedException e) {
                message = "Генерация ключей прервана.";
            }
            try {
                ex.exchange(message);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void interrupt() {
            if (!Thread.interrupted()) {
                user.stopGeneration();
            }
        }

    }

    private MainApp mainApp;
    private AlgorithmRSA user;
    private GenThread genThread;
    private Exchanger<String> message;
    public RSAOverviewController() {

    }

    @FXML
    private void initialize() {
        message = new Exchanger<>();
        genThread = new GenThread(message);
        systemMessage.setText("Все в порядке");
    }

    @FXML
    private void generate() throws InterruptedException {
        if (!genThread.isAlive()) {
            if (!user.areKeysGenerated()) {
                genThread.start();
                String m = "asd";
                m = message.exchange(m);
                systemMessage.setText(m);
            } else {
                systemMessage.setText("Ключи уже сгенерированы");
            }
        }
        else {
            systemMessage.setText("Ключи еще генерируются!");
        }
    }

    @FXML
    private void stopGenerating() {
        if (genThread.isAlive()) {
            genThread.interrupt();
        }
        else {
            systemMessage.setText("Процесс генерации не запущен!");
        }
    }

    @FXML
    private void sendMessage() {
        if (sendText.getText().equals("")) {
            systemMessage.setText("Введите сообщение!");
            return;
        }
        if (!user.areKeysGenerated()) {
            systemMessage.setText("Ключи генерируются или еще не сгенерированы!");
            return;
        }
        String encodedMessage = user.encoding(sendText.getText(), user.getPublicKey());
        try (FileWriter out = new FileWriter(new File("src/basePackage/Sender/pass.txt"), false)) {
            out.write(encodedMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void getMessage() {
        StringBuilder encodedMessage = new StringBuilder();
        try (BufferedReader in = new BufferedReader(new FileReader("src/basePackage/Sender/pass.txt"))) {
            String tmp;
            while ((tmp = in.readLine()) != null) {
                encodedMessage.append(tmp).append("\n");
            }
        } catch (IOException e) {
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
