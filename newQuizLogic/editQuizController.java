package newQuizLogic;

import DataModel.DataSource;
import DataModel.Quiz;
import javafx.animation.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import javafx.util.Duration;


import java.util.Optional;

public class editQuizController{

    @FXML
    private ListView<Quiz> editQuizView;

    @FXML
    private DialogPane editPage;



    //initialize
    public void initialize(){
        final ObservableList<Quiz> quiz_list = FXCollections.observableArrayList(DataSource.getInstance().queryQuiz(DataSource.ORDER_BY_ASC));

        //show list of quizzes available for modification
        editQuizView.setItems(quiz_list);
        editQuizView.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Quiz> call(ListView<Quiz> param) {

                ListCell<Quiz> cell = new ListCell<Quiz>() {

                    @Override
                    protected void updateItem(Quiz item, boolean empty) {
                        super.updateItem(item,empty);

                        if(empty){
                            setText(null);
                            setGraphic(null);
                        } else {
                            Button deleteButton = new Button("Delete");
                            deleteButton.setId("delete");

                            setText(item.getName());
                            deleteButton.setOnAction(e -> {
                                try {
                                    DataSource.getInstance().setDeleteQuiz(item.getName());
                                } catch (Exception e2) {
                                    System.out.println("Cannot Delete DataModel.Quiz: " + e2.getMessage());
                                }
                                editQuizView.getItems().remove(getItem());
                                updateItem(null,true);
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

    //Action listener for the "edit" button
    //shares the same window with the new quiz pane(to save myself from duplication)
    public void editQuiz() {
        try {
            if(editQuizView.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No DataModel.Quiz Selected!");
                alert.setHeaderText("Please select one of the quiz");
                alert.showAndWait();
            } else {
                try {
                    System.out.println("You've Selected the quiz name of : " + editQuizView.getSelectionModel().getSelectedItem().getName());
                    DataSource.getInstance().quizQuestionLink(editQuizView.getSelectionModel().getSelectedItem().getName());
                    editPage.getScene().getWindow().hide();

                    FXMLLoader main_root = new FXMLLoader();
                    main_root.setLocation(getClass().getResource("newQuizLogic/newQuizDetails.fxml"));
                    Parent parent = main_root.load();
                    NewQuizController newQuizController = main_root.getController();

                    newQuizController.editQuestion(DataSource.getInstance().quizQuestionLinkReturn());
                    System.out.println(DataSource.getInstance().quizQuestionLinkReturn());

                    parent.getStylesheets().add("additionalStyles.css");
                    Scene new_scene = new Scene(parent,900,800);
                    Stage secondStage = new Stage();

                    secondStage.setScene(new_scene);
                    secondStage.show();
                    secondStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                            alert.setTitle("Exit edit DataModel.Quiz Window");
                            alert.setHeaderText("Press OK to save");
                            alert.setContentText("Are you sure? Press OK to confirm, or cancel to back out.");
                            Optional<ButtonType> result = alert.showAndWait();

                            if(result.isPresent() && (result.get() == ButtonType.OK)) {
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
            }
        } catch(Exception e) {
            System.out.println("Cannot pass on DataModel.Quiz Name: " + e.getMessage());
        }
    }


}
