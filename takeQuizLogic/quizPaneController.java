package takeQuizLogic;

import DataModel.Answer;
import DataModel.DataSource;
import DataModel.Question;
import DataModel.checkWritten;
import Scores.scoreKeeper;
import javafx.animation.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Rectangle2D;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

import javafx.scene.image.Image;
import javafx.scene.Node;


public class quizPaneController{



    @FXML
    private BorderPane takeQuizPane;

    @FXML
    private Label questionsCountTotal;

    @FXML
    private Label currentQuestionNum;

    @FXML
    private Label currentScore;

    @FXML
    private ProgressBar quizProgress;

    @FXML
    private BorderPane quizPaneTop;

    private static final ArrayList<Question> list_of_questions = new ArrayList<>();
    private static final ArrayList<Answer> list_of_answers = new ArrayList<>();


    //change display by the correctness of the user
    private static String mainImagePath = "";
    private static String additionalImagePath = "";
    private void changeDisplay(boolean correct) {
        if(correct) {
            mainImagePath = "Image" + File.separator + "correct.png";
            additionalImagePath = "Image" + File.separator + "correct_additional.png";
        } else {
            mainImagePath = "Image" + File.separator + "wrong_answer.png";
            additionalImagePath = "Image" + File.separator + "wrong_answer_additional.png";
        }
    }

    //dummy images are used to align and display the image in a cross-shaped form. Other panes in the javafx did not work in a desirable way
    private static final Image left_dummy = new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "Dummy.png"));
    private static final Image top_dummy = new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "Dummy.png"));
    private static final Image bottom_dummy = new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "Dummy.png"));

    private static final ImageView left_Dummy = new ImageView(left_dummy);
    private static final ImageView top_Dummy = new ImageView(top_dummy);
    private static final ImageView bottom_Dummy = new ImageView(bottom_dummy);
    //Did the quiz ended prematurely?
    private boolean premature = false;

    //Count cycles of display to indicate end of the quiz
    private int cycleCount = 0;

    //The fade-in and out animation of the images in between questions are done here
    public void displayCorrectness(VBox temp,String background){
        Image sub_left = new Image(quizPaneController.class.getResourceAsStream(additionalImagePath));
        Image main = new Image(quizPaneController.class.getResourceAsStream(mainImagePath));
        Image sub_right = new Image(quizPaneController.class.getResourceAsStream(additionalImagePath));
        Image sub_top = new Image(quizPaneController.class.getResourceAsStream(additionalImagePath));
        Image sub_bottom = new Image(quizPaneController.class.getResourceAsStream(additionalImagePath));
        ImageView mainImage = new ImageView(main);
        ImageView subLeft = new ImageView(sub_left);
        ImageView subRight = new ImageView(sub_right);
        ImageView subTop = new ImageView(sub_top);
        ImageView subBottom = new ImageView(sub_bottom);

        mainImage.setFitHeight(200);
        mainImage.setFitWidth(200);

        subLeft.setFitHeight(100);
        subLeft.setFitWidth(100);

        left_Dummy.setFitHeight(100);
        left_Dummy.setFitWidth(255);
        left_Dummy.setVisible(false);

        subRight.setFitHeight(100);
        subRight.setFitWidth(100);

        subTop.setFitHeight(150);
        subTop.setFitWidth(200);

        top_Dummy.setFitHeight(150);
        top_Dummy.setFitWidth(430);
        top_Dummy.setVisible(false);

        subBottom.setFitHeight(150);
        subBottom.setFitWidth(200);
        bottom_Dummy.setFitHeight(150);
        bottom_Dummy.setFitWidth(430);
        bottom_Dummy.setVisible(false);

        TilePane element_top = new TilePane();
        element_top.getChildren().addAll(top_Dummy,subTop);
        TilePane element_middle = new TilePane();
        element_middle.getChildren().addAll(left_Dummy,subLeft,mainImage,subRight);
        TilePane element_bottom = new TilePane();
        element_bottom.getChildren().addAll(bottom_Dummy,subBottom);




        HBox horizontal = new HBox();
        temp.getChildren().add(element_top);
        horizontal.getChildren().add(element_middle);
        temp.getChildren().add(horizontal);
        temp.getChildren().add(element_bottom);

        temp.setBackground(newBackground(background));
        takeQuizPane.setCenter(temp);
        takeQuizPane.getCenter().setStyle("-fx-padding: 10,0,0,0");


        ParallelTransition parallelTransitionIn = new ParallelTransition();
        parallelTransitionIn.getChildren().addAll(
                FadeIn(800,mainImage),
                FadeIn(1500,subLeft),
                FadeIn(1500,subRight),
                FadeIn(1500,top_Dummy),
                FadeIn(1500,left_Dummy),
                FadeIn(1500,bottom_Dummy),
                FadeIn(1500,subTop),
                FadeIn(1500,subBottom)
        );

        ParallelTransition parallelTransitionOut = new ParallelTransition();
        parallelTransitionOut.getChildren().addAll(

                FadeOut(800,mainImage),
                FadeOut(1500,subLeft),
                FadeOut(1500,subRight),
                FadeOut(1500,top_Dummy),
                FadeOut(1500,left_Dummy),
                FadeOut(1500,bottom_Dummy),
                FadeOut(1500,subTop),
                FadeOut(1500,subBottom)
        );
        SequentialTransition sq = new SequentialTransition(parallelTransitionIn,parallelTransitionOut);
        sq.play();
        cycleCount += sq.getCycleCount();
        if(cycleCount >= Integer.valueOf(questionsCountTotal.getText())) {

            sq.setOnFinished(e -> {
                premature = false;
                FinalOutput();
            });

        } else {

            sq.setOnFinished(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent event) {

                    int current_index = Integer.valueOf(currentQuestionNum.getText())+1;
                    currentQuestionNum.setText(String.valueOf(current_index));
                    initialize();
                }
            });
        }



    }

    //catching and returning boolean values for indicating proper or improper exit.
    public boolean prematureExit() {
        return premature;
}



    //scoring users based on the number of questions
    public String ScoreKeeping(String current,String scoring) {
        double curr_score = Double.valueOf(current);
        double score_scheme = 100/(Double.valueOf(scoring));
        //fixme
        double sum = Math.round(curr_score+score_scheme);
        if((99) <= sum) {
            sum = 100;
        }

        return String.valueOf((int)sum);
    }



    //change background by its input
    private Background newBackground(String background){
        BackgroundImage myBI = new BackgroundImage(new Image(quizPaneController.class.getResourceAsStream(background), 1100, 800, false, true),
                BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
                BackgroundSize.DEFAULT);
        return new Background(myBI);
    }

    //spinning question marks on each sides
    private VBox SideDisplay(boolean right) {
        VBox imageGrid_left = new VBox();
        VBox imageGrid_right = new VBox();
        for(int i = 0; i < 8; i++) {
            imageGrid_left.getChildren().add(new ImageView(new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "question.png"))));
            imageGrid_right.getChildren().add(new ImageView(new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "question.png"))));
            if(i%2 == 0) {
                RotateForward(imageGrid_left.getChildren().get(i));
                RotateForward(imageGrid_right.getChildren().get(i));
            } else {
                RotateBack(imageGrid_left.getChildren().get(i));
                RotateBack(imageGrid_right.getChildren().get(i));
            }
        }

        if(right) {
            return imageGrid_left;
        } else {
            return imageGrid_right;
        }

    }

    //side question marks rotating backwards
    private void RotateBack(Node node) {
        RotateTransition rotate = new RotateTransition(Duration.millis(2000),node);
        rotate.setByAngle(-720);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();
    }
    //side question marks rotating forwards
    private void RotateForward(Node node) {
        RotateTransition rotate = new RotateTransition(Duration.millis(2000),node);
        rotate.setByAngle(720);
        rotate.setCycleCount(Animation.INDEFINITE);
        rotate.play();
    }
    //fading in effect
    private FadeTransition FadeIn(int duration,Node node) {
        FadeTransition fadeIn = new FadeTransition(Duration.millis(duration),node);
        fadeIn.setFromValue(0.0);
        fadeIn.setToValue(1.0);
        return fadeIn;
    }
    //fading out effect
    private FadeTransition FadeOut(int duration,Node node) {
        FadeTransition fadeOut = new FadeTransition(Duration.millis(duration),node);
        fadeOut.setFromValue(1.0);
        fadeOut.setToValue(0.0);
        return fadeOut;
    }
    //populate multi-choice
    private Label MultiChoice(Answer answer) {

        Label answer_label = new Label();
        answer_label.setText(answer.getContent());
        return answer_label;

    }
    //create buttons and sentences that are being shown to user
    private HBox MultiChoiceAnswer(RadioButton button,Label answer) {
        HBox answer_container = new HBox();
        setFont(answer,"American Typewriter",15,"WHITE");
        answer_container.getChildren().add(setGap(400,0));
        answer_container.getChildren().add(button);
        answer_container.getChildren().add(setGap(10,0));
        answer_container.getChildren().add(answer);
        answer_container.getChildren().add(setGap(0,20));
        answer_container.setPrefWidth(1100);

        return answer_container;
    }

    //set fonts of any words/sentences
    private Label setFont(Label label,String font,int size,String color) {
        label.setFont(Font.font(font,size));
        color = color.toUpperCase();
        label.setTextFill(Color.valueOf(color));

        return label;
    }
    //set gaps between elements
    private Region setGap(int width, int height) {
        Region gap = new Region();
        gap.setPrefWidth(width);
        gap.setPrefHeight(height);

        return gap;
    }


    private static ArrayList<RadioButton> multi_button = new ArrayList<>();
    private static ToggleGroup multi_group = new ToggleGroup();
    private static HBox t_f_container = new HBox();
    private static RadioButton true_answer = new RadioButton();
    private static RadioButton false_answer = new RadioButton();
    private static TextArea writing_section = new TextArea();
    private static HBox writing_container = new HBox();


    //initialize
    public void initialize() {
        //clear out or re-initialize private fields
        premature = true;
        t_f_container.getChildren().clear();
        writing_container.getChildren().clear();
        multi_button.clear();
        true_answer.setId(null);
        false_answer.setId(null);
        double progressVal = Double.valueOf(currentQuestionNum.getText())/Double.valueOf(questionsCountTotal.getText());
        if(currentQuestionNum.getText().equals("1")){
            if(!currentQuestionNum.getText().equals(questionsCountTotal.getText())) {
                progressVal = 0;
            }

        }

        //if it is not repeated (it's a new instance of the quiz), incur new instance
        if(repeated == -1) {
            list_of_questions.clear();
            list_of_answers.clear();
            try {
                //make a new record. set title as the local time (to keep it distinctive)
                scoreKeeper.getInstance().makeQuizDir((LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy_MM_dd_HH:mm"))));
                scoreKeeper.getInstance().loadScore();
            } catch(IOException e) {
                System.out.println(e.getMessage());
            }
            try {
                //add quiz ID to the text
                scoreKeeper.getInstance().addText(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                scoreKeeper.getInstance().addText("QUIZ ID: " + DataSource.getInstance().quizQuestionLinkReturn());
                System.out.println("QUIZ ID: " + DataSource.getInstance().quizQuestionLinkReturn());
                //load questions
                list_of_questions.addAll(DataSource.getInstance().pullAllQuestionsByQuiz(DataSource.getInstance().quizQuestionLinkReturn()));
            } catch (Exception e) {
                System.out.println("Cannot pull question Content: " + e.getMessage());
            }

            try {

                for(int i = 0; i < list_of_questions.size();i++) {
                    System.out.println("List of questions: " + list_of_questions.get(i).getName());
                    //load answers
                    list_of_answers.addAll(DataSource.getInstance().queryAnswer(DataSource.getInstance().quizQuestionLinkReturn(),list_of_questions.get(i).getName()));
                }
            } catch(Exception e) {
                System.out.println("Cannot pull answer Content: " + e.getMessage());
            }
        }


        //spinning box on the top center
        Image spinning_box = new Image(quizPaneController.class.getResourceAsStream("Image" + File.separator + "spinning_box.jpg"));
        //each frame of the gif is being divided into columns, 3 in this case
        int COLUMNS  =   3;
        //this is how many frames there are
        int COUNT    =  361;

        int OFFSET_X =  0;
        int OFFSET_Y =  0;
        //width and height of the frame
        int WIDTH    = 340;
        int HEIGHT   = 338;



        final ImageView spinningBox = new ImageView(spinning_box);
        spinningBox.setViewport(new Rectangle2D(OFFSET_X, OFFSET_Y, WIDTH, HEIGHT));
        spinningBox.setFitWidth(100);
        spinningBox.setFitHeight(100);
        quizPaneTop.setCenter(spinningBox);
        //set time of duration
        final Animation animation = new gifAnimate(
                spinningBox,
                Duration.millis(36000),
                COUNT, COLUMNS,
                OFFSET_X, OFFSET_Y,
                WIDTH, HEIGHT
        );
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();

        //setting up the whole window
        takeQuizPane.setLeft(SideDisplay(false));
        takeQuizPane.setRight(SideDisplay(true));
        takeQuizPane.getBottom().setVisible(true);

        quizProgress.setProgress(progressVal);

        questionsCountTotal.setText(String.valueOf(list_of_questions.size()));

        takeQuizPane.setCenter(setUpQuestions(randomize(list_of_questions)));


    }



    //count change of display
    private static int repeated = -1;
    private int randomize(ArrayList<Question> questions) {
        if (repeated == -1) {
            Collections.shuffle(questions);
            repeated++;
            return repeated;
        }
        repeated++;
        if(repeated == questions.size()) {
            repeated = repeated -1;
            return repeated;
        }
        return repeated;
    }


    //set up questions
    private VBox setUpQuestions(int question_id) {

        //all questions will be submitted through a Vbox
        VBox temp = new VBox();
        temp.setBackground(newBackground("Image" + File.separator + "astro.jpg"));

            Label q_name = new Label("DataModel.Question " + currentQuestionNum.getText());
            Label q_content = new Label(list_of_questions.get(question_id).getContent());
            scoreKeeper.getInstance().addText("\n" + q_name.getText() + ": " + q_content.getText());
            q_content.setWrapText(true);

            setFont(q_name, "American Typewriter",40,"WHITE");
            setFont(q_content, "American Typewriter",40,"WHITE");
            q_name.setAlignment(Pos.CENTER);
            q_content.setAlignment(Pos.CENTER);
            q_content.setPrefWidth(1100);
            temp.getChildren().add(q_name);
            temp.getChildren().add(q_content);


            VBox answer_list = new VBox();
            Answer answer = new Answer();

            writing_section.clear();
            writing_section.setWrapText(true);


            try {
                for (int i = 0; i < list_of_answers.size(); i++) {
                    //if it is the multi-choice question, do the following
                    if (list_of_answers.get(i).getMulti_choice() == 1) {
                        if (list_of_answers.get(i).getQuestion_link() == list_of_questions.get(question_id).getId()) {
                            multi_button.add(new RadioButton());
                            multi_button.get(0).setSelected(true);
                            if (list_of_answers.get(i).getCorrect_answer() == 1) {
                                multi_button.get(multi_button.size() - 1).setId("Correct");
                                scoreKeeper.getInstance().addText(list_of_answers.get(i).getContent());
                            } else {
                                scoreKeeper.getInstance().addText(list_of_answers.get(i).getContent());
                            }
                            multi_button.get(multi_button.size() - 1).setToggleGroup(multi_group);
                            HBox multiContainer = new HBox(MultiChoiceAnswer(multi_button.get(multi_button.size() - 1),MultiChoice(list_of_answers.get(i))));
                            multiContainer.setId(String.valueOf(answer_list.getChildren().size()));
                            multiContainer.setPrefWidth(1100);
                            multiContainer.setPrefHeight(10);
                            multiContainer.setAlignment(Pos.CENTER);
                            answer_list.getChildren().add(multiContainer);
                            answer_list.setSpacing(15);
                            multiContainer.setOnMouseClicked(new EventHandler<MouseEvent>() {
                                @Override
                                public void handle(MouseEvent event) {
                                    multi_button.get(Integer.valueOf(multiContainer.getId())).setSelected(true);

                                }
                            });
                        }
                        //if it is the T/F question, do the following
                    } else if (list_of_answers.get(i).getTrue_false() == 1) {
                        if (list_of_answers.get(i).getQuestion_link() == list_of_questions.get(question_id).getId()) {
                            if (t_f_container.getChildren().size() < 2) {

                                Label t_text = new Label("True");
                                setFont(t_text, "American Typewriter",15,"WHITE");
                                Label f_text = new Label("False");
                                setFont(f_text,"American Typewriter",15,"WHITE");

                                if (list_of_answers.get(i).getCorrect_answer() == 1) {
                                    scoreKeeper.getInstance().addText("TRUE");
                                    true_answer.setId("Correct");
                                    false_answer.setId("Incorrect");
                                } else if (list_of_answers.get(i).getCorrect_answer() == 0) {
                                    scoreKeeper.getInstance().addText("FALSE");
                                    false_answer.setId("Correct");
                                    true_answer.setId("Incorrect");
                                }

                                true_answer.setToggleGroup(multi_group);
                                false_answer.setToggleGroup(multi_group);
                                true_answer.setSelected(true);

                                t_f_container.getChildren().add(setGap(550, 0));
                                t_f_container.getChildren().add(true_answer);
                                t_f_container.getChildren().add(setGap(10, 0));
                                t_f_container.getChildren().add(t_text);
                                t_f_container.getChildren().add(setGap(10, 0));
                                t_f_container.getChildren().add(false_answer);
                                t_f_container.getChildren().add(setGap(10, 0));
                                t_f_container.getChildren().add(f_text);
                            }
                        }

                    //if it is the written question, do the following
                    } else if (list_of_answers.get(i).getWritten() == 1) {
                        if (list_of_answers.get(i).getQuestion_link() == list_of_questions.get(question_id).getId()) {
                            scoreKeeper.getInstance().addText(list_of_answers.get(i).getContent());
                            writing_container.getChildren().add(setGap(300, 0));
                            writing_container.getChildren().add(writing_section);
                            writing_container.setAlignment(Pos.CENTER);
                            writing_section.setId(String.valueOf(list_of_answers.get(i).getContent().trim()));
                            if(!writing_section.getId().endsWith(".")) {
                                writing_section.setId(String.valueOf(list_of_answers.get(i).getContent().trim() + "."));
                            }
                        }
                    }
                }


                if (list_of_questions.get(question_id).getMulti_choice() == 1) {
                    temp.getChildren().add(answer_list);
                } else if (list_of_questions.get(question_id).getTrue_false() == 1) {
                    temp.getChildren().add(t_f_container);
                } else {
                    temp.getChildren().remove(answer_list);
                    temp.getChildren().add(writing_container);
                }


            } catch (Exception e) {
                System.out.println("DataModel.Answer query failed: " + e.getMessage());
            }

            temp.setAlignment(Pos.CENTER);



        return temp;
    }





    //action listener for the "next" button
    //records user's answer and determines its correctness as well
    public void nextQuestion() {
        //meaning multi-choice is being shown and being used
        if(multi_button.size() != 0) {

            //check if the user's answer matches the standard answer.
            for(int i = 0; i < multi_button.size();i++) {
                if(multi_button.get(i).isSelected()) {
                    if(multi_button.get(i).getId() == null) {
                        scoreKeeper.getInstance().addText("\nSELECTED: " + list_of_answers.get(i).getContent());
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: " + list_of_answers.get(i).getContent());
                        changeDisplay(false);

                    } else if(multi_button.get(i).getId().equals("Correct")){
                        scoreKeeper.getInstance().addText("\nSELECTED: " + list_of_answers.get(i).getContent());
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: " + list_of_answers.get(i).getContent());
                        currentScore.setText(ScoreKeeping(currentScore.getText(),questionsCountTotal.getText()));
                        changeDisplay(true);

                    }
                }
            }
        }
        //meaning T/F is being shown and being used
        if(t_f_container.getChildren().size() != 0) {

                if (true_answer.isSelected()) {
                    System.out.println("TRUE ANSWER");
                    if (true_answer.getId().equals("Correct")) {
                        scoreKeeper.getInstance().addText("\nSELECTED: TRUE");
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: TRUE");
                        currentScore.setText(ScoreKeeping(currentScore.getText(), questionsCountTotal.getText()));
                        changeDisplay(true);

                    } else {
                        scoreKeeper.getInstance().addText("\nSELECTED: FALSE");
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: TRUE");
                        changeDisplay(false);
                    }

                }
                if (false_answer.isSelected()) {
                    System.out.println("FALSE ANSWER");
                    if (false_answer.getId().equals("Correct")) {
                        scoreKeeper.getInstance().addText("\nSELECTED: FALSE");
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: FALSE");
                        currentScore.setText(ScoreKeeping(currentScore.getText(), questionsCountTotal.getText()));
                        changeDisplay(true);

                    } else {
                        scoreKeeper.getInstance().addText("\nSELECTED: TRUE");
                        scoreKeeper.getInstance().addText("CORRECT ANSWER: FALSE");

                        changeDisplay(false);
                    }

                }


        }
        //meaning written question is being shown and being used
        if(writing_container.getChildren().size() != 0) {

            scoreKeeper.getInstance().addText("\nCORRECT ANSWER: " + writing_section.getId());
            if(writing_section.getText().equals(writing_section.getId())) {
                currentScore.setText(ScoreKeeping(currentScore.getText(), questionsCountTotal.getText()));
                changeDisplay(true);
            } else {

                checkWritten checkwritten = new checkWritten(writing_section.getId(),writing_section.getText());
                StringBuilder sb = new StringBuilder();
                for(int i = 0; i < checkwritten.tokenizeAnswer(writing_section.getId()).size();) {
                    for(int n = 0; n < checkwritten.tokenizeAnswer(writing_section.getId()).size();n++) {
                        if(!checkwritten.compareTwo(checkwritten.tokenizeAnswer(writing_section.getId()).get(i),checkwritten.tokenizeAnswer(writing_section.getText().trim()).get(n)).equals("")) {
                            sb.append(checkwritten.compareTwo(checkwritten.tokenizeAnswer(writing_section.getId()).get(i),checkwritten.tokenizeAnswer(writing_section.getText().trim()).get(n)));
                        }
                    }
                    i++;
                }
                if(!sb.toString().endsWith(".")) {
                    sb.append(".");
                }

                System.out.println("CORRECT: " + writing_section.getId());
                System.out.println("INPUT: " + sb.toString());


                if(sb.toString().equals(writing_section.getId())) {
                    scoreKeeper.getInstance().addText("INPUT: " + sb.toString());
                    currentScore.setText(ScoreKeeping(currentScore.getText(), questionsCountTotal.getText()));
                    changeDisplay(true);
                } else {
                    scoreKeeper.getInstance().addText("INPUT: " + writing_section.getText());
                    changeDisplay(false);
                }
            }

        }


        VBox temp = new VBox();
        displayCorrectness(temp, "Image" + File.separator + "astro.jpg");


    }

    //Determines final display and show score and retry button.
    public void FinalOutput() {

        takeQuizPane.setLeft(SideDisplay(false));
        takeQuizPane.setRight(SideDisplay(true));
        takeQuizPane.getBottom().setVisible(false);

        VBox temp = new VBox();

        Label finalScore = new Label("Score: " + currentScore.getText());
        scoreKeeper.getInstance().addText("\nSCORE: " + currentScore.getText());
        Label scoreMessage = new Label();
        scoreMessage.setWrapText(true);
        Button retry = new Button();
        retry.setText("Retry");
        //sets everything to default
        retry.setOnAction(e -> {
                currentQuestionNum.setText("1");
                currentScore.setText("0");
                cycleCount = 0;
                repeated = -1;
                initialize();

        });
        //Change display in accordance to user's score
        if(Integer.valueOf(currentScore.getText()) >= 90) {
            scoreMessage.setText("Great Job!");
            temp.setBackground(newBackground("Image" + File.separator + "godTouch.jpg"));
        } else if(Integer.valueOf(currentScore.getText()) >= 60) {
            scoreMessage.setText("Rooms to improve");
            temp.setBackground(newBackground("Image" + File.separator + "planets.jpg"));
        } else {
            scoreMessage.setText("Study..?");
            temp.setBackground(newBackground("Image" + File.separator + "low_score.jpg"));
        }

        setFont(finalScore, "Neo Sans",40,"#33ff57");
        setFont(scoreMessage, "Neo Sans",40,"#ff5733");


        temp.getChildren().add(finalScore);
        temp.getChildren().add(scoreMessage);
        temp.getChildren().add(retry);
        temp.setAlignment(Pos.BOTTOM_CENTER);
        takeQuizPane.setCenter(temp);
        cycleCount = 0;
        repeated = -1;
        try {
            scoreKeeper.getInstance().storeScore();
        } catch(IOException e1) {
            System.out.println(e1.getMessage());
        }

    }



}
