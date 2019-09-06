
import DataModel.DataSource;
import DataModel.Quiz;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import newQuizLogic.NewQuizController;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;
import java.io.InputStreamReader;


public class Controller extends Main {

    @FXML
    private GridPane mainGridPane;

    @FXML
    private Button quizPrep;

    @FXML
    private ListView<Quiz> quizPreview;

    @FXML
    private BorderPane quizListWrapper;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private Label showQuizName;

    @FXML
    private Button edit;


    //event handler for building new quiz
    public void buildNewQuiz() {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.initStyle(StageStyle.TRANSPARENT);
        dialog.getDialogPane().getStylesheets().add("additionalStyles.css");
        dialog.initOwner(mainGridPane.getScene().getWindow());

        dialog.setTitle("Add new DataModel.Quiz");
        dialog.setHeaderText("Use this dialog to create a new DataModel.Quiz");
        FXMLLoader fxmlLoader = new FXMLLoader();
        fxmlLoader.setLocation(getClass().getResource("newQuizLogic/addNewQuiz.fxml"));
        try {
            dialog.getDialogPane().setContent(fxmlLoader.load());
        } catch (IOException e) {
            System.out.println("Error: Couldn't print the dialog");
            e.printStackTrace();
            return;
        }

        //adding buttons to the dialogue
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.CANCEL);


        Optional<ButtonType> result = dialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {

            NewQuizController newquizcontroller = fxmlLoader.getController();
            //empty quiz warning
            if(newquizcontroller.emptyQuizName()){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Unfinished Field");
                alert.setHeaderText("The Name of the DataModel.Quiz cannot be Empty");
                alert.showAndWait();
            } else {


                //checking whether the same name exists in the database
                System.out.println("" + newquizcontroller.getNewQuiz());
                ArrayList<Quiz> existingQuiz = new ArrayList<>();
                existingQuiz.addAll(DataSource.getInstance().queryQuiz(DataSource.ORDER_BY_ASC));
                int count = 0;
                for (int i = 0; i < existingQuiz.size(); i++) {
                    if (newquizcontroller.getNewQuiz().equals(existingQuiz.get(i).getName())) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Error 101");
                        alert.setHeaderText("The Name of the DataModel.Quiz already exists!");
                        alert.showAndWait();
                        count++;
                        break;
                    }
                }
                //if there is no matching name in the database,
                if(count == 0) {
                    try {

                        //load quiz details pane
                        FXMLLoader main_root = new FXMLLoader(getClass().getResource("newQuizLogic/newQuizDetails.fxml"));
                        Parent parent = main_root.load();
                        parent.getStylesheets().add("additionalStyles.css");
                        Scene new_scene = new Scene(parent,900,800);
                        Stage secondStage = new Stage();

                        secondStage.setScene(new_scene);
                        secondStage.show();
                        secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                            @Override
                            public void handle(WindowEvent event) {
                                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                                alert.setTitle("Exit New DataModel.Quiz Window");
                                alert.setHeaderText("If OK, no data will be saved!!!");
                                alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");
                                Optional<ButtonType> result = alert.showAndWait();

                                if(result.isPresent() && (result.get() == ButtonType.OK)) {
                                    try {
                                        DataSource.getInstance().DeleteQuiz();
                                    } catch (Exception e) {
                                        System.out.println("Couldn't execute delete query: " + e.getMessage());
                                    }
                                    secondStage.close();
                                }
                                else {
                                    event.consume();
                                }
                            }
                        });


                    } catch(Exception e) {
                        System.out.println("Error: couldn't print the quiz details");
                        e.printStackTrace();
                    }
                    //link database with the user-input
                    Quiz quiz = new Quiz();

                    quiz.setName(newquizcontroller.getNewQuiz());
                    try {
                        DataSource.getInstance().insertQuiz(quiz.getName());

                    } catch (Exception e) {
                        System.out.println("insertQuiz Exception: " + e.getMessage());
                    }

                    try {
                        DataSource.getInstance().quizQuestionLink(quiz.getName());
                    } catch (Exception e) {
                        System.out.println("quizQuestionLink Exception: " + e.getMessage());
                    }
                } else {
                    buildNewQuiz();
                }


            }



        }
    }


    //Action listener for quiz loading pane
    @FXML
    public void loadQuiz(){
        quizPrep.setDisable(true);
        try {

            FXMLLoader loadQuiz = new FXMLLoader(getClass().getResource("loadQuiz/loadQuiz.fxml"));
            Parent parent = loadQuiz.load();

            parent.getStylesheets().add("additionalStyles.css");

            Scene new_scene = new Scene(parent,450,450);
            Stage loadStage = new Stage();

            loadStage.initOwner(mainGridPane.getScene().getWindow());
            loadStage.initStyle(StageStyle.DECORATED);

            loadStage.setOpacity(0.7);
            loadStage.setScene(new_scene);
            loadStage.show();


            loadStage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    quizPrep.setDisable(false);
                    mainGridPane.getScene().getWindow();
                }
            });

            loadStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    quizPrep.setDisable(false);
                    mainGridPane.getScene().getWindow();
                }
            });




        } catch(Exception e) {
            System.out.println("Error: couldn't print the quiz list");
            e.printStackTrace();
        }






    }

    //Action Listener for edit pane(choosing quiz)
    public void editQuiz() {
        edit.setDisable(true);
        try {

            FXMLLoader editQuiz = new FXMLLoader(getClass().getResource("newQuizLogic/editQuiz/editQuiz.fxml"));
            Parent parent = editQuiz.load();

            parent.getStylesheets().add("additionalStyles.css");

            Scene new_scene = new Scene(parent,450,450);
            Stage loadStage = new Stage();

            loadStage.initOwner(mainGridPane.getScene().getWindow());
            loadStage.initStyle(StageStyle.DECORATED);

            loadStage.setOpacity(0.7);
            loadStage.setScene(new_scene);
            loadStage.show();


            loadStage.setOnHidden(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    edit.setDisable(false);
                    mainGridPane.getScene().getWindow();
                }
            });

            loadStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent event) {
                    edit.setDisable(false);
                    mainGridPane.getScene().getWindow();
                }
            });




        } catch(Exception e) {
            System.out.println("Error: couldn't print the quiz list");
            e.printStackTrace();
        }
    }


    public void pullScore() {
        try {
            FXMLLoader scoreViewController = new FXMLLoader(getClass().getResource("Scores" + File.separator + "scoreView.fxml"));
            Parent parent = scoreViewController.load();
            parent.getStylesheets().add("additionalStyles.css");

            Scene new_scene = new Scene(parent,450,450);
            Stage loadStage = new Stage();

            loadStage.initOwner(mainGridPane.getScene().getWindow());
            loadStage.initStyle(StageStyle.DECORATED);

            loadStage.setOpacity(0.7);
            loadStage.setScene(new_scene);
            loadStage.show();

        } catch(Exception e) {
            System.out.println("Error: couldn't print the quiz list");
            e.printStackTrace();
        }

    }

    public void guideLine() {
        try {
            BorderPane guide_pane = new BorderPane();
            ScrollPane sp = new ScrollPane();
            guide_pane.setCenter(sp);
            ArrayList<Label> guideLineWords = new ArrayList<>();
            VBox container = new VBox();
            //list all files on directory

            BufferedReader br = new BufferedReader(new InputStreamReader(Main.class.getResourceAsStream("guideLine.txt")));
            String input;
            try {
                while ((input = br.readLine()) != null) {
                    String[] itemPieces = input.split("\n");
                    String content = itemPieces[0];
                    if(content.trim().equals("<picture>")) {
                        Image test = new Image(ImageController.class.getResourceAsStream("takeQuizLogic/Image" + File.separator + "respect.jpg"));
                        ImageView troll = new ImageView(test);
                        troll.setFitHeight(20);
                        troll.setFitWidth(50);
                        container.getChildren().add(troll);


                    } else {
                        guideLineWords.add(new Label(content));
                        guideLineWords.get(guideLineWords.size() - 1).setWrapText(true);
                        container.getChildren().add(guideLineWords.get(guideLineWords.size() - 1));
                    }
                }

            } finally {
                if (br != null) {
                    br.close();
                }
            }
//            }



            sp.setContent(container);




            Scene scene = new Scene(guide_pane, 600, 600);
            scene.getStylesheets().add("additionalStyles.css");
            Stage stage = new Stage();
            stage.setScene(scene);
            stage.show();
        } catch(Exception e) {
            System.out.println("Error: couldn't get score");
            e.printStackTrace();
        }
    }


    public void bye() {
        System.exit(0);
    }


}



