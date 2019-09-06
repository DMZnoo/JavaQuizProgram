package Scores;

import com.sun.tools.javac.Main;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.Duration;


import javax.sound.midi.SysexMessage;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;

public class ScoreViewController{
    @FXML
    private DialogPane scoreViewPane;

    @FXML
    private ListView scoreListView;

    private final ObservableList<String> score_list = FXCollections.observableArrayList(getScore());
    private static int length_of_program = Main.class.getProtectionDomain().getCodeSource().getLocation().getPath().length();
    private static String real_dir = System.getProperty("user.dir")+ File.separator + "src" +File.separator + "Scores"+ File.separator;
//    private static String real_dir = FileSystems.getDefault().getPath("Scores").toString();
    private ArrayList<String> getScore() {
        System.out.println("PATH: " + real_dir);
        File dir = new File(real_dir);


        File[] paths = dir.listFiles();
        ArrayList<String> files = new ArrayList<String>();

        try {


            if(dir.isDirectory()){
                //list all files on directory
                for(File f:paths){
                    if(f.getName().endsWith("t"))
                    files.add(f.getName());
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return files;

    }


    //Action Listener for showing the content of the text file
    public void showScore() {
        try {
            if (scoreListView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No Item Selected!");
                alert.setHeaderText("Please select one of the items!");
                alert.showAndWait();
            } else {
                BorderPane score_pane = new BorderPane();
                ScrollPane sp = new ScrollPane();
                score_pane.setCenter(sp);

                File dir = new File(real_dir);
                File[] paths = dir.listFiles();
                ArrayList<Label> score_report = new ArrayList<>();
                VBox container = new VBox();
                if (dir.isDirectory()) {
                    //list all files on directory
                    for (File f : paths) {
                        if (f.getName().equals(scoreListView.getSelectionModel().getSelectedItem())) {
                            int input;

                            try {
                                FileInputStream file = new FileInputStream(dir + File.separator + f.getName());
                                StringBuilder itemPieces = new StringBuilder();
                                while ((input = file.read()) != -1) {
                                    // convert to char and display it
                                    char x = (char) input;

                                    String y = x+"";
                                    itemPieces.append(y);

                                }
                                score_report.add(new Label(itemPieces.toString()));
                                score_report.get(score_report.size() - 1).setWrapText(true);
                                container.getChildren().add(score_report.get(score_report.size() - 1));



                                sp.setContent(container);


                                Scene scene = new Scene(score_pane, 600, 600);
                                scene.getStylesheets().add("additionalStyles.css");
                                Stage stage = new Stage();
                                stage.setScene(scene);
                                stage.show();

                            } catch (Exception e1) {
                                System.out.println("Error: couldn't get score 1");
                                e1.printStackTrace();
                            }
                        }
                    }
                }
            }
        } catch (Exception e1) {
            System.out.println("Error: couldn't get score 2");
            e1.printStackTrace();
        }
    }

    private void deleteScore(String name) {
        File dir = new File(real_dir);
        File[] paths = dir.listFiles();


        if (dir.isDirectory()) {
            //list all files on directory
            for (File f : paths) {
                if (f.getName().equals(name)) {
                    f.delete();
                }
            }

        }
    }



    //initialize
    public void initialize(){

        //show list of quizzes available for modification
        scoreListView.setItems(score_list);

        //set cell factory: delete button slide-open effect
        scoreListView.setCellFactory(new Callback<ListView, ListCell>() {
            @Override
            public ListCell call(ListView param) {

                ListCell<String> cell = new ListCell<String>() {

                    @Override
                    protected void updateItem(String item, boolean empty) {
                        super.updateItem(item,empty);

                        if(empty){
                            setText(null);
                            setGraphic(null);
                        } else {
                            Button deleteButton = new Button("Delete");
                            deleteButton.setId("delete");

                            setText(item);
                            deleteButton.setOnAction(e -> {
                                try {
                                    System.out.println(getItem());
                                    deleteScore(getItem());
                                } catch (Exception e2) {
                                    System.out.println("Cannot Delete DataModel.Quiz: " + e2.getMessage());
                                }
                                scoreListView.getItems().remove(getItem());
//                                initialize();
                            });

                            setOnMouseEntered(e -> {
                                setGraphic(deleteButton);
                                setPadding(new Insets(0,5,0,0));
                                FadeTransition ft = new FadeTransition(Duration.millis(200), deleteButton);
                                ft.setFromValue(0.0);
                                ft.setToValue(0.7);
                                ft.setCycleCount(1);
                                ft.setAutoReverse(true);

                                TranslateTransition tr =
                                        new TranslateTransition(Duration.millis(100), deleteButton);
                                tr.setFromX(0);
                                tr.setToX(0.5);
                                tr.setCycleCount(1);
                                tr.setAutoReverse(true);

                                ParallelTransition pt = new ParallelTransition();
                                pt.getChildren().addAll(
                                        ft,
                                        tr
                                );
                                pt.setCycleCount(1);
                                pt.play();


                            });
                            setOnMouseExited(e -> {
                                setGraphic(null);
                                setPadding(new Insets(5,5,5,5));
                            });
                        }
                    }

                };


                return cell;

            }
        });



    }
}
