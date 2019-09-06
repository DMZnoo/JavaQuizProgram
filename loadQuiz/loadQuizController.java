package loadQuiz;

import DataModel.DataSource;
import DataModel.Quiz;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Callback;
import takeQuizLogic.quizPaneController;

import java.io.File;

public class loadQuizController{


    @FXML
    private ListView<Quiz> quizPreview;


    @FXML
    private DialogPane loadPage;




    public void initialize(){
        final ObservableList<Quiz> quiz_list = FXCollections.observableArrayList(DataSource.getInstance().queryQuiz(DataSource.ORDER_BY_ASC));



        quizPreview.setItems(quiz_list);
        quizPreview.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Quiz> call(ListView<Quiz> param) {

                ListCell<Quiz> cell = new ListCell<>() {
                    @Override
                    protected void updateItem(Quiz item, boolean empty) {
                        super.updateItem(item,empty);

                        if(empty){
                            setText(null);
                        } else {
                            setText(item.getName());
                        }
                    }

                };


                return cell;

            }
        });

    }


    public void pullQuiz() {
        try {
            if(quizPreview.getSelectionModel().getSelectedItem() == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("No DataModel.Quiz Selected!");
                alert.setHeaderText("Please select one of the quiz");
                alert.showAndWait();
            } else {
                try {
                    DataSource.getInstance().quizQuestionLink(quizPreview.getSelectionModel().getSelectedItem().getName());
                    loadPage.getScene().getWindow().hide();
                    FXMLLoader takeQuiz_controller = new FXMLLoader(getClass().getResource("../takeQuizLogic/quizPane.fxml"));
                    Parent parent = takeQuiz_controller.load();
//                    Parent parent = takeQuiz_controller.load();
                    parent.getStylesheets().add("additionalStyles.css");
                    Scene new_scene = new Scene(parent,800,800);
                    Stage loadStage = new Stage();

                    loadStage.setScene(new_scene);
                    loadStage.setMaximized(true);
                    loadStage.show();
                    loadStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                        @Override
                        public void handle(WindowEvent event) {
                            quizPaneController quizpanecontroller = takeQuiz_controller.getController();
                            if(quizpanecontroller.prematureExit()) {
                                Alert alert = new Alert(Alert.AlertType.ERROR);
                                alert.setTitle("DataModel.Quiz On Progress");
                                alert.setHeaderText("Please finish the quiz");
                                alert.showAndWait();
                                event.consume();

                            }

                        }
                    });


                } catch(Exception e) {
                    System.out.println("Error: couldn't print the quiz list");
                    e.printStackTrace();
                }
            }
        } catch(Exception e) {
            System.out.println("Cannot pass on DataModel.Quiz Name: " + e.getMessage());
        }
    }



}
