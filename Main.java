import DataModel.DataSource;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {


//    private static FXMLLoader main_root;



    @Override
    public void start(Stage primaryStage) throws Exception{

        //launch main window
//        main_root = new FXMLLoader(getClass().getResource("mainfxml/mainwindow.fxml"));
        Parent main_pane = FXMLLoader.load(getClass().getResource("mainwindow.fxml"));

        //css font style added from the google-api
        main_pane.getStylesheets().add("https://fonts.googleapis.com/css?family=Mali&display=swap");
        main_pane.getStylesheets().add("additionalStyles.css");

        Scene main_scene = new Scene(main_pane,800,700);

        primaryStage.setResizable(false);
        primaryStage.setScene(main_scene);
        primaryStage.show();






    }



    public void init() throws Exception {
        super.init();

        //open database with error message
        if (!DataSource.getInstance().open()) {
            System.out.println("FATAL ERROR: Couldn't connect to database!");
            Platform.exit();
        }

    }

    @Override
    public void stop() throws Exception {
        super.stop();

        //close database on exit
        DataSource.getInstance().close();
    }




    public static void main(String[] args) {
        launch(args);

    }
}