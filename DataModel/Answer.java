package DataModel;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

public class Answer {

    private SimpleIntegerProperty id;
    private SimpleStringProperty name;
    private SimpleStringProperty content;
    private SimpleIntegerProperty true_false;
    private SimpleIntegerProperty multi_choice;
    private SimpleIntegerProperty written;
    private SimpleIntegerProperty correct_answer;
    private SimpleIntegerProperty question_link;

    public Answer() {
        this.id = new SimpleIntegerProperty();
        this.name = new SimpleStringProperty();
        this.content = new SimpleStringProperty();
        this.true_false = new SimpleIntegerProperty();
        this.multi_choice = new SimpleIntegerProperty();
        this.written = new SimpleIntegerProperty();
        this.correct_answer = new SimpleIntegerProperty();
        this.question_link = new SimpleIntegerProperty();
    }


    public int getQuestion_link() {
        return question_link.get();
    }

    public SimpleIntegerProperty question_linkProperty() {
        return question_link;
    }

    public void setQuestion_link(int question_link) {
        this.question_link.set(question_link);
    }

    public int getTrue_false() {
        return true_false.get();
    }

    public SimpleIntegerProperty true_falseProperty() {
        return true_false;
    }

    public void setTrue_false(int true_false) {
        this.true_false.set(true_false);
    }

    public int getMulti_choice() {
        return multi_choice.get();
    }

    public SimpleIntegerProperty multi_choiceProperty() {
        return multi_choice;
    }

    public void setMulti_choice(int multi_choice) {
        this.multi_choice.set(multi_choice);
    }

    public int getWritten() {
        return written.get();
    }

    public SimpleIntegerProperty writtenProperty() {
        return written;
    }

    public void setWritten(int written) {
        this.written.set(written);
    }

    public int getCorrect_answer() {
        return correct_answer.get();
    }

    public SimpleIntegerProperty correct_answerProperty() {
        return correct_answer;
    }

    public void setCorrect_answer(int correct_answer) {
        this.correct_answer.set(correct_answer);
    }

    public String getContent() { return content.get(); }

    public SimpleStringProperty contentProperty() { return content; }

    public void setContent(String content) { this.content.set(content); }

    public int getId() {
        return id.get();
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getName() {
        return name.get();
    }

    public void setName(String name) {
        this.name.set(name);
    }
}
