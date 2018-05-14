package cn.edu.ncu.football;

import cn.edu.ncu.football.controller.Main;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class App extends Application {
    private ConfigurableApplicationContext applicationContext;

    @Autowired
    private FakeDataGenerator fakeDataGenerator;

    @Override
    public void init() {
        applicationContext = SpringApplication.run(getClass());
        applicationContext.getAutowireCapableBeanFactory().autowireBean(this);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("/main.fxml"));
        fxmlLoader.setControllerFactory(applicationContext::getBean);

        Parent root = fxmlLoader.load();

        Scene scene = new Scene(root, 1200, 480);

        primaryStage.setScene(scene);
        primaryStage.setTitle("FootBall Match");
        primaryStage.setMinWidth(1200);
        primaryStage.show();

        Main main = fxmlLoader.getController();
        MockThread mockThread = new MockThread(main);
        mockThread.start();
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        applicationContext.stop();
    }

    public static void main(String[] args) {
        Application.launch(App.class, args);
    }

    class MockThread extends Thread {
        private Main mainController;

        MockThread(Main mainController) {
            this.mainController = mainController;
        }

        @Override
        public void run() {
            try {
                fakeDataGenerator.run(mainController);
            } catch (Exception e) {
                System.err.println(e.getMessage());
                e.printStackTrace();
                System.exit(-1);
            }
        }
    }
}
