package newQuizLogic;

import DataModel.Answer;
import DataModel.DataSource;
import DataModel.Question;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import org.controlsfx.control.textfield.CustomTextField;
import javafx.scene.Node;
import javafx.scene.control.*;


import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.util.Callback;
import javafx.scene.control.ToggleGroup;

import java.sql.SQLException;
import java.util.*;


public class NewQuizController{

    @FXML
    private BorderPane newQuizDetailsWrapper;





    @FXML
    private Label selectedQuestion;

    @FXML
    private VBox selectedAnswer;

    @FXML
    private Button quizSubmission;

    @FXML
    private VBox questionDisplay;

    @FXML
    private VBox forEssay;

    @FXML
    private VBox addLogicDisplay;

    @FXML
    private ScrollPane scrollForMulti;

    @FXML
    private TableView<Question> questionPreview;

    @FXML
    private TextArea question_input;

    @FXML
    private TextField quizName;

    @FXML
    private ChoiceBox<String> optionSet;


    @FXML
    private VBox forMultiChoice;

    @FXML
    private HBox forBoolean;

    @FXML
    private Button addChoice;

    @FXML
    private Button removeChoice;

    @FXML
    private Button editSubmission;

    @FXML
    private Button addQuestion;

    @FXML
    private ContextMenu listContextMenu;

    @FXML
    private TableColumn<Question, String> question_panel;



    //action listener for the edit pane - adding more questions
    public void addinit() {
        question_input.clear();
        forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
        forBoolean.getChildren().remove(0,forBoolean.getChildren().size());
        forEssay.getChildren().remove(0,forEssay.getChildren().size());
//        optionSet.getSelectionModel().clearSelection();
//        optionSet.setValue("True/False");
        quizSubmission.setVisible(true);
        questionPreview.setDisable(true);
        quizSubmission.setOnMouseClicked(e -> {
            questionPreview.setDisable(false);
            editQuestion(DataSource.getInstance().quizQuestionLinkReturn());
        });
        editSubmission.setDisable(true);
        editSubmission.setVisible(false);
        addQuestion.setVisible(false);

    }

    //initialize buttons for new quiz pane (hiding buttons used for edit pane)
    private void buttonInit() {
        quizSubmission.setVisible(true);
        editSubmission.setDisable(true);
        addQuestion.setVisible(false);
    }


    public String getNewQuiz() {
        return quizName.getText();
    }

    public boolean emptyQuizName() {
        if (quizName.getText() == null || quizName.getText().trim().isEmpty()) {
            return true;
        } else {
            return false;
        }
    }

    private static TextArea essay = new TextArea();
    private static int multi = 0;
    private static int t_f = 0;
    private static int written = 0;
    private static ArrayList<RadioButton> t_f_choices = new ArrayList<>();

    //clear default texts when user types in
    public void exampleFill() {
        quizName.clear();
        quizName.setStyle("-fx-opacity: 1.0");
    }

    //action listener for the drop down menu for choosing question types(T/F,Multi-choice,written)

    public void returnChoice() {
        String choice = optionSet.getSelectionModel().getSelectedItem();
        forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
        forBoolean.getChildren().remove(0, forBoolean.getChildren().size());
        t_f_choices.clear();
        forEssay.getChildren().remove(0,forEssay.getChildren().size());
        addChoice.setVisible(false);
        removeChoice.setVisible(false);
        scrollForMulti.setVisible(false); scrollForMulti.setManaged(false);
        if (choice.equals("Multiple Choice")) {
            t_f = 0;
            written = 0;
            multi = 1;
            addChoice.setVisible(true);
            removeChoice.setVisible(true);
            scrollForMulti.setVisible(true); scrollForMulti.setManaged(true);
        } else if (choice.equals("True/False")) {
            multi = 0;
            written = 0;
            t_f = 1;
            ToggleGroup grouped_boolean = new ToggleGroup();
            Label true_choice = new Label("True");
            RadioButton click_true = new RadioButton();
            click_true.setId("trueAnswer");
            t_f_choices.add(click_true);
            Label false_choice = new Label("False");
            RadioButton click_false = new RadioButton();
            click_false.setId("falseAnswer");
            t_f_choices.add(click_false);
            click_true.setToggleGroup(grouped_boolean);
            click_true.setSelected(true);
            click_false.setToggleGroup(grouped_boolean);
            forBoolean.getChildren().addAll(true_choice, click_true, false_choice, click_false);

        } else if (choice.equals("Written Response")) {
            if(multi == 1 || t_f == 1) {
                essay.clear();
            }
            multi = 0;
            t_f = 0;
            written = 1;
            essay.setWrapText(true);
            forEssay.getChildren().add(essay);

        }
    }



    @FXML
    private static ArrayList<RadioButton> multi_button = new ArrayList<>();
    private static ArrayList<CustomTextField> multi_text = new ArrayList<>();
    @FXML
    private static ToggleGroup multi_group = new ToggleGroup();

    //adding more choice when user clicked -> for multi-choice
    public void addMoreChoice() {
        scrollForMulti.setVisible(true); scrollForMulti.setManaged(true);
        CustomTextField new_field = new CustomTextField();
        multi_text.add(new_field);
        multi_button.add(new RadioButton());
        multi_button.get(multi_button.size() - 1).setToggleGroup(multi_group);
        multi_text.get(multi_text.size()-1).setRight(multi_button.get(multi_button.size() - 1));
        forMultiChoice.getChildren().add(multi_text.get(multi_text.size()-1));


    }

    //if intended for edits, the table must be pre-populated and different buttons should be shown.
    public void editQuestion(int quiz_id) {
        editSubmission.setDisable(false);
        editSubmission.setVisible(true);

        quizSubmission.setVisible(false);
        addQuestion.setVisible(true);
        System.out.println("EDITQUESTION: " + quiz_id);
        try {
            ObservableList<Question> questions = FXCollections.observableArrayList(DataSource.getInstance().pullAllQuestionsByQuiz(quiz_id));
            for (int i = 0; i < questions.size();i++) {
                System.out.println(questions.get(i).getContent());
                questionPreview.getItems().add(questions.get(i));
                populateQuestion(questions.get(i).getContent());
                setRowMenu();
            }

        } catch (Exception e) {
            System.out.println("Cannot get questions for edit: " + e.getMessage());
        }

    }


    //populate questions by their contents
    private Question populateQuestion(String question_content) {

        Question question = new Question();
        try {
            //if the question doesn't exist,
            if(DataSource.getInstance().queryQuestionContent(DataSource.getInstance().quizQuestionLinkReturn(),question_content) == null) {

                question.setName("DataModel.Question " + DataSource.getInstance().quizQuestionLinkReturn() + " - " + (DataSource.getInstance().queryQuestionIDForAll()));
                question.setContent(question_input.getText());
                questionLogic(question.getName(), question.getContent());
            //if it exists,
            } else {
                question = DataSource.getInstance().queryQuestionContent(DataSource.getInstance().quizQuestionLinkReturn(),question_content);
                questionLogic(question.getName(), question.getContent());

            }


        } catch (SQLException e) {
            System.out.println("Couldn't set question: " + e.getMessage());
        }

        return question;
    }

//  populate row menu, side pane logic
    private void setRowMenu() {

        //this toggle button would replace what was on the side pane (editor pane)
        ArrayList<Node> pull_logicDisplay = new ArrayList<>();
        pull_logicDisplay.add(newQuizDetailsWrapper.getRight());

        Button toggle = new Button("...");

        toggle.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {

                //pull in the node that was previously sitting where the toggle button was (right side of the borderpane was the editor)
                newQuizDetailsWrapper.setRight(pull_logicDisplay.get(0));
//                toggle ("...") -> editor view
                if(editSubmission.isDisabled()) {
                    editSubmission.setVisible(false);
                    editSubmission.setDisable(false);
                    question_input.clear();
                    forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                    forBoolean.getChildren().remove(0, forBoolean.getChildren().size());
                    forEssay.getChildren().remove(0,forEssay.getChildren().size());
                }
                selectedQuestion.setText("");
                selectedAnswer.getChildren().clear();
                newQuizDetailsWrapper.getCenter().setVisible(false);
                questionDisplay.setVisible(false);


            }
        });

        questionPreview.setRowFactory(new Callback<TableView<Question>, TableRow<Question>>() {
            @Override
            public TableRow<Question> call(TableView<Question> tableView) {
                final TableRow<Question> row = new TableRow<>();
                final ContextMenu contextMenu = new ContextMenu();
                final MenuItem removeMenuItem = new MenuItem("Remove");

                //removing on click
                removeMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                    @Override
                    public void handle(ActionEvent event) {
                        questionPreview.getItems().remove(row.getItem());
                        try {
                            forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                            multi_text.clear(); multi_button.clear();
                            forBoolean.getChildren().remove(0,forBoolean.getChildren().size());
                            forEssay.getChildren().remove(0,forEssay.getChildren().size());

                            DataSource.getInstance().setDeleteAnswer(row.getItem().getName());
                            DataSource.getInstance().setDeleteQuestion(row.getItem().getName());

                        } catch (Exception e) {
                            System.out.println("Cannot Delete: " + e.getMessage());
                        }



                    }
                });

                contextMenu.getItems().add(removeMenuItem);
                row.contextMenuProperty().bind(
                        Bindings.when(row.emptyProperty())
                                .then((ContextMenu) null)
                                .otherwise(contextMenu)
                );





                row.setOnMousePressed(new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent event) {

                        //pull question so that when toggle is being clicked, questions are being filled in (only for the case of editting the quiz)
                        question_input.clear();
                        try {
                            ArrayList<Question> collectQuestion = new ArrayList<>(DataSource.getInstance().pullSelectedQuestionByQuiz(DataSource.getInstance().quizQuestionLinkReturn(),questionPreview.getItems().get(row.getIndex()).getName()));
                            StringBuilder sb = new StringBuilder();
                            for(int i = 0; i < collectQuestion.size();i++) {
                                sb.append("DataModel.Question: " + collectQuestion.get(i).getContent() + "\n");
                            }
                            question_input.setText(sb.toString().substring(10,sb.toString().length()-1));
                            selectedQuestion.setText(sb.toString());
                        } catch (Exception e) {
                            System.out.println("Cannot set question content: " + e.getMessage());
                        }

                        forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                        multi_text.clear(); multi_button.clear();
                        forBoolean.getChildren().remove(0,forBoolean.getChildren().size());
                        forEssay.getChildren().remove(0,forEssay.getChildren().size());
                        //pull answers so that when toggle is being clicked, answers are populated as it were submitted (only for the case of editting the quiz)
                        try {
                            selectedAnswer.getChildren().clear();
                            ArrayList<Answer> collectAnswer = new ArrayList<>(DataSource.getInstance().queryAnswer(DataSource.getInstance().quizQuestionLinkReturn(),questionPreview.getItems().get(row.getIndex()).getName()));


                            for(int i = 0; i < collectAnswer.size();i++) {
                                StringBuilder sb = new StringBuilder("DataModel.Answer " + String.valueOf(i+1) + ": " + collectAnswer.get(i).getContent() + "\n");
                                Label label = new Label(sb.toString());
                                label.setWrapText(true);


                                if(collectAnswer.get(i).getTrue_false() == 1) {
                                    optionSet.getSelectionModel().select("True/False");
                                    returnChoice();
                                    if(collectAnswer.get(i).getCorrect_answer() == 1) {
                                        t_f_choices.get(0).setSelected(true);
                                        t_f_choices.get(0).setId("trueAnswer");
                                    } else {
                                        t_f_choices.get(1).setSelected(true);
                                        t_f_choices.get(1).setId("falseAnswer");

                                    }

                                } else if(collectAnswer.get(i).getMulti_choice() == 1) {
                                    optionSet.getSelectionModel().select("Multiple Choice");
                                    addMoreChoice();

                                    multi_text.get(multi_text.size()-1).setText(collectAnswer.get(i).getContent());
                                    if(collectAnswer.get(i).getCorrect_answer() == 1) {
                                        multi_button.get(multi_button.size()-1).setSelected(true);
                                    }
                                } else if(collectAnswer.get(i).getWritten() == 1) {
                                    optionSet.getSelectionModel().select("Written Response");
                                    essay.setText(collectAnswer.get(i).getContent());
                                    essay.setWrapText(true);
                                    returnChoice();
                                }



                                selectedAnswer.getChildren().add(label);
                            }
                        } catch (Exception e) {
                            System.out.println("Cannot get answers content: " + e.getMessage());
                        }

                        newQuizDetailsWrapper.getCenter().setVisible(true);

                        newQuizDetailsWrapper.setRight(null);
                        newQuizDetailsWrapper.setRight(toggle);

                    }
                });
                return row;
            }
        });
    }



    private void questionLogic(String name, String content) {




        try {

            Question question = new Question();
            question.setName(name);
            question.setContent(content);
            DataSource.getInstance().insertQuestion(question.getName(), question.getContent(), t_f, multi, written, DataSource.getInstance().quizQuestionLinkReturn());

        } catch (Exception e1) {
            System.out.println("DataModel.Question Exception: " + e1.getMessage());
        }
        Task<ObservableList<Question>> display_Questions = new GetAllQuestionsTask();
        questionPreview.itemsProperty().bind(display_Questions.valueProperty());

        new Thread(display_Questions).start();
        question_input.clear();


    }

    private int answerLogic() {
        Answer answer = new Answer();

        try {

            answer.setId(DataSource.getInstance().queryAnswerIDForAll());

        } catch (Exception e) {
            System.out.println("Couldn't get count for answer: " + e.getMessage());
        }
        return answer.getId();
    }

    private void errorMessaging(String title, String message, String question, String answer) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();


        try {
            question_panel.getTableView().getItems().remove(DataSource.getInstance().questionAnswerLink(question) - 1);
            DataSource.getInstance().setDeleteAnswer(answer);
            DataSource.getInstance().setDeleteQuestion(question);
        } catch (Exception e) {
            System.out.println("Cannot Delete: " + e.getMessage());
        }
    }

    private void errorMessaging(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(message);
        alert.showAndWait();

    }


    //delete multi-choice from the latest(bottom-most) when clicked
    public void deleteChoice() {
        if(!forMultiChoice.getChildren().isEmpty()) {
            forMultiChoice.getChildren().remove(forMultiChoice.getChildren().size() - 1, forMultiChoice.getChildren().size());
            multi_text.remove(multi_text.size()-1);
            multi_button.remove(multi_button.size()-1);

        }

    }

    //acition listener for the new question subimission.
    public void newQuizSubmission() {
        if(quizSubmission.isVisible()) {
            buttonInit();
        }
        if (optionSet.getValue().equals("Choose")) {
            errorMessaging("Unfinished Field", "Choose one of the choices!");
        } else if (question_input.getText().isEmpty()) {
            errorMessaging("Unfinished Field", "The DataModel.Question field cannot be Empty");
        } else {
            String choice = optionSet.getSelectionModel().getSelectedItem();
            Question question = new Question();
            Answer answer = new Answer();
            //making sure that questions aren't being duplicated(if needs change, can be edited)
            int err = 0;
            try {
                ArrayList<Question> question_list = new ArrayList<>(DataSource.getInstance().pullAllQuestionsByQuiz(DataSource.getInstance().quizQuestionLinkReturn()));
                if(question_list.size() > 1) {
                    for (int i = 0; i < question_list.size(); i++) {
                        if (question_list.get(i).getContent().equals(question_input.getText()) && !editSubmission.isVisible()) {
                            errorMessaging("Invalid field", "This DataModel.Question already exists!");
                            err++;
                            break;
                        }
                    }
                }

            } catch (Exception e) {
                System.out.println("Cannot check valid question field: " + e.getMessage());
            }

            //Choice Logic//
            if (choice.equals("Multiple Choice")) {
                if(forEssay.getChildren().size() > 0) {
                    forEssay.getChildren().remove(0,forEssay.getChildren().size());
                }
                if(t_f_choices.size() > 0) {
                    t_f_choices.clear();
                }

                int empty_count = 0;
                for (int i = 0; i < multi_text.size(); i++) {
                    if (multi_text == null || multi_text.get(i).getText().isEmpty()) {
                        empty_count += 1;
                        errorMessaging("Unfinished Field", "Please complete the DataModel.Answer field");
                        break;
                    }

                }
                //if all the fields were populated,
                if (empty_count == 0) {


                    ArrayList<Integer> non_select_count = new ArrayList<>();
                    int select_count = -1;
                    for (int x = 0; x < multi_text.size(); x++) {
                        if (multi_button.get(x).isSelected()) {
                            select_count = x;
                        }
                        if (!(multi_button.get(x).isSelected())) {
                            non_select_count.add(x);
                        }


                    }

                    //if the size of empty buttons = all buttons,
                    if (non_select_count.size() == multi_button.size()) {
                        select_count = -1;
                    }

                    if (select_count == -1) {
                        errorMessaging("Unfinished Field", "Please complete the DataModel.Answer field: No Correct DataModel.Answer chosen.");

                    } else {
                        if (err == 0) {
                            //populate question
                            question = populateQuestion(question_input.getText());


                            answer.setContent(multi_text.get(select_count).getText());
                            //Set AnswerID
                            answer.setId(answerLogic());

                            try {
                                //if being edited, previous answers are cleared out.
                                if (!editSubmission.isDisabled()) {
                                    ArrayList<Answer> check_answer = new ArrayList<>(DataSource.getInstance().queryAnswer(DataSource.getInstance().quizQuestionLinkReturn(), question.getName()));
                                    for (int i = 0; i < check_answer.size(); i++) {
                                        DataSource.getInstance().deleteAnswerByName(check_answer.get(i).getName());

                                    }

                                    DataSource.getInstance().insertAnswer("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (multi_button.indexOf(multi_button.get(select_count)) + 1), answer.getContent(), 0, 1, 0, 1, DataSource.getInstance().questionAnswerLink(question.getName()));
                                } else {
                                    DataSource.getInstance().insertAnswer("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (multi_button.indexOf(multi_button.get(select_count)) + 1), answer.getContent(), 0, 1, 0, 1, DataSource.getInstance().questionAnswerLink(question.getName()));
                                }
                            } catch (Exception e) {
                                System.out.println("DataModel.Answer Exception: " + e.getMessage());
                            }

                            for (int y = 0; y < non_select_count.size(); y++) {
                                //Set AnswerID
                                try {

                                    //fixme
                                    answer.setId(answerLogic());
                                    answer.setContent(multi_text.get(non_select_count.get(y)).getText());
                                    answer.setName("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (multi_button.indexOf(multi_button.get(non_select_count.get(y))) + 1));


                                        DataSource.getInstance().insertAnswer(answer.getName(), answer.getContent(), 0, 1, 0, 0, DataSource.getInstance().questionAnswerLink(question.getName()));
//                                    }
                                } catch (Exception e) {
                                    System.out.println("DataModel.Answer Exception: " + e.getMessage());
                                }
                            }

                            if (forMultiChoice.getChildren().size() > 0) {
                                forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                            }
                            if (multi_button.size() > 0) {
                                multi_button.clear();
                                multi_text.clear();
                            }
                            forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                            forMultiChoice.getChildren().clear();
                        }
                    }
                }






            } else if (choice.equals("True/False")) {
                forEssay.getChildren().remove(0,forEssay.getChildren().size());
                //clear out the unnecessary
                if (forMultiChoice.getChildren().size() > 0) {
                    forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                }
                if (multi_button.size() > 0) {
                    multi_button.clear();
                    multi_text.clear();
                }
                    if(err == 0) {
                        //
                        //populate question
                        question = populateQuestion(question_input.getText());
                        //if being edited, previous answers are cleared out.
                        try {
                            if (!editSubmission.isDisabled()) {
                                ArrayList<Answer> check_answer = new ArrayList<>(DataSource.getInstance().queryAnswer(DataSource.getInstance().quizQuestionLinkReturn(), question.getName()));
                                for (int i = 0; i < check_answer.size(); i++) {
                                    DataSource.getInstance().deleteAnswerByName(check_answer.get(i).getName());

                                }
                            }
                        } catch (Exception e) {
                            System.out.println("Cannot query quesitons: " + e.getMessage());
                        }
                        //Set AnswerID
                        answer.setId(answerLogic());


                            try {
                                for (int i = 0; i < t_f_choices.size(); i++) {
                                    if (t_f_choices.get(i).isSelected()) {
                                        if (t_f_choices.get(i).getId().equals("trueAnswer")) {
                                            DataSource.getInstance().insertAnswer("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (answer.getId() - (answer.getId() - 1)), "True", 1, 0, 0, 1, DataSource.getInstance().questionAnswerLink(question.getName()));
                                        } else {
                                            DataSource.getInstance().insertAnswer("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (answer.getId() - (answer.getId() - 1)), "False", 1, 0, 0, 0, DataSource.getInstance().questionAnswerLink(question.getName()));
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                System.out.println("DataModel.Answer Exception: " + e.getMessage());
                            }
                        }
                        forBoolean.getChildren().clear();

            } else if (choice.equals("Written Response")) {
                //clear out the unnecessary
                if (forMultiChoice.getChildren().size() > 0) {
                    forMultiChoice.getChildren().remove(0, forMultiChoice.getChildren().size());
                }
                if (multi_button.size() > 0) {
                    multi_button.clear();
                    multi_text.clear();
                }
                if(t_f_choices.size() > 0) {
                    t_f_choices.clear();
                }
                if(forEssay.getChildren().size() == 0) {
                    forEssay.getChildren().add(essay);
                }
                //
                if (essay.getText().isEmpty()) {
                    errorMessaging("Unfinished Field", "The DataModel.Answer field cannot be Empty");
                } else {

                    if(err == 0) {
                        //populate question
                        question = populateQuestion(question_input.getText());
                        //if being edited, previous answers are cleared out.

                        if(!editSubmission.isDisabled()) {
                            ArrayList<Answer> check_answer = new ArrayList<>(DataSource.getInstance().queryAnswer(DataSource.getInstance().quizQuestionLinkReturn(), question.getName()));
                            for (int i = 0; i < check_answer.size(); i++) {
                                DataSource.getInstance().deleteAnswerByName(check_answer.get(i).getName());

                            }
                        }

                        //Set AnswerID
                        answer.setId(answerLogic());
                        answer.setContent(essay.getText().trim().toLowerCase());

                        try {
                            DataSource.getInstance().insertAnswer("DataModel.Answer " + DataSource.getInstance().questionAnswerLink(question.getName()) + " - " + (answer.getId() - (answer.getId() - 1)), answer.getContent(), 0, 0, 1, 1, DataSource.getInstance().questionAnswerLink(question.getName()));
                        } catch (Exception e) {
                            System.out.println("Written Response Exception " + e.getMessage());
                        }
                        essay.clear();
                        forEssay.getChildren().remove(0,forEssay.getChildren().size());
                    }

                }
            }

            //set row
            setRowMenu();




        }



    }

    //saving before exit
    public void saveExit() {
        try {
            if(DataSource.getInstance().pullAllQuestionsByQuiz(DataSource.getInstance().quizQuestionLinkReturn()) == null) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error: Empty DataModel.Quiz");
                alert.setHeaderText("The quiz has no content!");
                alert.showAndWait();
            } else {
                newQuizDetailsWrapper.getScene().getWindow().hide();
            }
        } catch (Exception e) {
            System.out.println("Empty content check Failed: " + e.getMessage());
        }

    }


}






class GetAllQuestionsTask extends Task {

    @Override
    public ObservableList<Question> call()  {
        return FXCollections.observableArrayList
                (DataSource.getInstance().queryQuestion(DataSource.getInstance().quizQuestionLinkReturn()));
    }
}