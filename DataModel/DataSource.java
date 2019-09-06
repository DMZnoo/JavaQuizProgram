package DataModel;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataSource {

    public static final String DB_NAME = "quiz.db";
    public static final String CONNECTION_STRING = "jdbc:sqlite:" + DB_NAME;
//    public static final String CONNECTION_STRING = "jdbc:sqlite:"+ DB_NAME;

    public static final String FOREIGN_KEY_ACTIVATION = "PRAGMA foreign_keys = ON";

    //DataModel.Quiz Table
    public static final String TABLE_QUIZ = "Quiz";
    public static final String COLUMN_QUIZ_ID = "quiz_id";
    public static final String COLUMN_QUIZ_NAME = "quiz_Name";
    public static final int INDEX_QUIZ_ID = 1;
    public static final int INDEX_QUIZ_NAME = 2;

    //DataModel.Question Table
    public static final String TABLE_QUESTION = "Question";
    public static final String COLUMN_QUESTION_ID = "question_id";
    public static final String COLUMN_QUESTION_NAME = "question_Name";
    public static final String COLUMN_QUESTION_CONTENT = "question_Content";
    public static final String COLUMN_QUESTION_TYPE_TF = "True_N_False";
    public static final String COLUMN_QUESTION_TYPE_MULTI = "Multiple_Choice";
    public static final String COLUMN_QUESTION_TYPE_WRITTEN = "Written_Response";
    public static final String COLUMN_QUESTION_QUIZ_LINK_ID = "quiz_id";
    public static final int INDEX_QUESTION_ID = 1;
    public static final int INDEX_QUESTION_NAME = 2;
    public static final int INDEX_QUESTION_CONTENT = 3;
    public static final int INDEX_QUESTION_TYPE_TF = 4;
    public static final int INDEX_QUESTION_TYPE_MULTI = 5;
    public static final int INDEX_QUESTION_TYPE_WRITTEN = 6;
    public static final int INDEX_QUESTION_QUIZ_LINK_ID = 7;


    //DataModel.Answer Table
    public static final String TABLE_ANSWER = "Answer";
    public static final String COLUMN_ANSWER_ID = "answer_id";
    public static final String COLUMN_ANSWER_NAME = "answer_Name";
    public static final String COLUMN_ANSWER_CONTENT = "answer_Content";
    public static final String COLUMN_ANSWER_TYPE_TF = "true_N_False";
    public static final String COLUMN_ANSWER_TYPE_MULTI = "multiple_Choice";
    public static final String COLUMN_ANSWER_TYPE_WRITTEN = "written_Response";
    public static final String COLUMN_ANSWER_CORRECT_ANSWER = "correct_Answer";
    public static final String COLUMN_QUESTION_ANSWER_LINK_ID = "question_id";
    public static final int INDEX_ANSWER_ID = 1;
    public static final int INDEX_ANSWER_NAME = 2;
    public static final int INDEX_ANSWER_CONTENT = 3;
    public static final int INDEX_ANSWER_TYPE_TF = 4;
    public static final int INDEX_ANSWER_TYPE_MULTI = 5;
    public static final int INDEX_ANSWER_TYPE_WRITTEN = 6;
    public static final int INDEX_ANSWER_CORRECT_ANSWER = 7;
    public static final int INDEX_QUESTION_ANSWER_LINK_ID = 8;

    //Order-By Clause
    public static final int ORDER_BY_NONE = 1;
    public static final int ORDER_BY_ASC = 2;
    public static final int ORDER_BY_DESC = 3;

    //Drop Table
    public static final String DROP_TABLE_QUIZ = "DROP TABLE IF EXISTS " + TABLE_QUIZ;
    public static final String DROP_TABLE_QUESTION = "DROP TABLE IF EXISTS " + TABLE_QUESTION;
    public static final String DROP_TABLE_ANSWER = "DROP TABLE IF EXISTS " + TABLE_ANSWER;


    //Create Table
    private static final String CREATE_TABLE_QUIZ = "CREATE TABLE IF NOT EXISTS " + TABLE_QUIZ +
            " (" + COLUMN_QUIZ_ID + " integer NOT NULL UNIQUE, " +
            COLUMN_QUIZ_NAME + " text, " + "PRIMARY KEY" + "( "+ COLUMN_QUIZ_ID +" )" + ")";
    private static final String CREATE_TABLE_QUESTION = "CREATE TABLE IF NOT EXISTS " + TABLE_QUESTION +
            " (" + COLUMN_QUESTION_ID + " integer NOT NULL UNIQUE, " +
            COLUMN_QUESTION_NAME + " text, " +
            COLUMN_QUESTION_CONTENT + " text, " +
            COLUMN_QUESTION_TYPE_TF + " integer " + " DEFAULT " + 0  +", " +
            COLUMN_QUESTION_TYPE_MULTI + " integer " + "DEFAULT " + 0 + ", " +
            COLUMN_QUESTION_TYPE_WRITTEN + " integer " + "DEFAULT " + 0 + ", " +
            COLUMN_QUESTION_QUIZ_LINK_ID + " integer " + ", " +
            "CHECK " + "( " + COLUMN_QUESTION_TYPE_TF + " IN " + "(0,1)), " +
            "CHECK " + "( " + COLUMN_QUESTION_TYPE_MULTI + " IN " + "(0,1)), " +
            "CHECK " + "( " + COLUMN_QUESTION_TYPE_WRITTEN + " IN " + "(0,1)), " +
            " FOREIGN KEY(" + COLUMN_QUESTION_QUIZ_LINK_ID + " ) REFERENCES " + TABLE_QUIZ + "( "+ COLUMN_QUIZ_ID + " ) ON DELETE CASCADE," +
            " PRIMARY KEY" + "( "+ COLUMN_QUESTION_ID +" )" + " )";
    private static final String CREATE_TABLE_ANSWER = "CREATE TABLE IF NOT EXISTS " + TABLE_ANSWER +
            " (" + COLUMN_ANSWER_ID + " integer NOT NULL UNIQUE, " +
            COLUMN_ANSWER_NAME + " text, " +
            COLUMN_ANSWER_CONTENT + " text, " +
            COLUMN_ANSWER_TYPE_TF + " integer " +  "DEFAULT " + 0 + ", " +
            COLUMN_ANSWER_TYPE_MULTI + " integer " + "DEFAULT " + 0 + ", " +
            COLUMN_ANSWER_TYPE_WRITTEN + " integer " + "DEFAULT " + 0 + ", " +
            COLUMN_ANSWER_CORRECT_ANSWER + " integer " + " DEFAULT " + 0 + ", " +
            COLUMN_QUESTION_ANSWER_LINK_ID + " integer, " +
            "CHECK " + "( " + COLUMN_ANSWER_TYPE_TF + " IN " + "(0,1)), " +
            "CHECK " + "( " + COLUMN_ANSWER_TYPE_MULTI + " IN " + "(0,1)), " +
            "CHECK " + "( " + COLUMN_ANSWER_TYPE_WRITTEN + " IN " + "(0,1)), " +
            "CHECK " + "( " + COLUMN_ANSWER_CORRECT_ANSWER + " IN " + "(0,1)), " +
            " FOREIGN KEY(" + COLUMN_QUESTION_ANSWER_LINK_ID + " ) REFERENCES " + TABLE_QUESTION + "( "+ COLUMN_QUESTION_ID + " ) ON DELETE CASCADE," +
            " PRIMARY KEY" + "(" + COLUMN_ANSWER_ID + " )" + ")";

    //Insert Values
    private static final String INSERT_QUIZ = "INSERT INTO " + TABLE_QUIZ + " ( " + COLUMN_QUIZ_NAME + ") VALUES(?)";


    private static final String INSERT_QUESTION = "INSERT INTO " + TABLE_QUESTION + " ( " +
//                                                COLUMN_QUESTION_ID + ", " +
            COLUMN_QUESTION_NAME +", " +
            COLUMN_QUESTION_CONTENT + ", " +
            COLUMN_QUESTION_TYPE_TF + ", "+
            COLUMN_QUESTION_TYPE_MULTI + ", " +
            COLUMN_QUESTION_TYPE_WRITTEN + ", " +
            COLUMN_QUESTION_QUIZ_LINK_ID +
            ") VALUES(?, ?, ?, ?, ?,? )";
    private static final String INSERT_ANSWER = "INSERT INTO " + TABLE_ANSWER + "( " +
//                                                COLUMN_ANSWER_ID + ", " +
            COLUMN_ANSWER_NAME + ", " +
            COLUMN_ANSWER_CONTENT + ", " +
            COLUMN_ANSWER_TYPE_TF + ", " +
            COLUMN_ANSWER_TYPE_MULTI + ", " +
            COLUMN_ANSWER_TYPE_WRITTEN + ", " +
            COLUMN_ANSWER_CORRECT_ANSWER + ", "+
            COLUMN_QUESTION_ANSWER_LINK_ID +
            ") VALUES(?, ?, ?, ?, ?, ?, ? )";


    //Query ID
    private static final String QUERY_QUIZ_ID = "SELECT " + COLUMN_QUIZ_ID + " FROM " + TABLE_QUIZ + " WHERE " + COLUMN_QUIZ_NAME + " = ?";
    private static final String QUERY_QUESTION_ID = "SELECT " + COLUMN_QUESTION_ID + " FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_NAME + " = ?";
    private static final String QUERY_ANSWER_ID = "SELECT " + COLUMN_ANSWER_ID + " FROM " + TABLE_ANSWER + " WHERE " + COLUMN_ANSWER_NAME + " = ?";
    private static final String QUERY_QUESTION_ID_ALL = "SELECT " + COLUMN_QUESTION_ID + " FROM " + TABLE_QUESTION;
    private static final String QUERY_ANSWER_ID_ALL = "SELECT " + COLUMN_ANSWER_ID + " FROM " + TABLE_ANSWER;
    //Query Name
    private static final String QUERY_QUIZ_NAME = "SELECT " + COLUMN_QUIZ_NAME + " FROM " + TABLE_QUIZ + " WHERE " + COLUMN_QUIZ_ID + " = ?";
    private static final String QUERY_QUESTION_NAME = "SELECT " + COLUMN_QUESTION_NAME + " FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_ID + " = ?";
    private static final String QUERY_ANSWER_NAME = "SELECT " + COLUMN_ANSWER_NAME + " FROM " + TABLE_ANSWER + " WHERE " + COLUMN_ANSWER_ID + " = ?";

    //Query Content
    private static final String QUERY_QUESTION_ALL = "SELECT * FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = ? and " + COLUMN_QUESTION_CONTENT + " = ?";
    private static final String QUERY_ANSWER_CONTENT = "SELECT " + COLUMN_ANSWER_CONTENT + " FROM " + TABLE_ANSWER + " WHERE " + COLUMN_QUESTION_ANSWER_LINK_ID + " IN (" + QUERY_QUESTION_ID + ")";

    //Update Column
    private static final String UPDATE_QUIZ = "UPDATE " + TABLE_QUIZ + " SET " + COLUMN_QUIZ_ID + " = ? ";


    //Delete Column
    private static final String DELETE_QUIZ = "DELETE FROM " + TABLE_QUIZ + " WHERE " + COLUMN_QUIZ_NAME + " = ?";
    private static final String DELETE_QUESTION = "DELETE FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_NAME + " = ?";
    private static final String DELETE_ANSWER = "DELETE FROM " + TABLE_ANSWER + " WHERE " + COLUMN_ANSWER_NAME + " = ?";


    private static DataSource instance = new DataSource();
    private Connection conn;
    private PreparedStatement foreignKeyActivate;

    private PreparedStatement insertQuiz;
    private PreparedStatement insertQuestion;
    private PreparedStatement insertAnswer;

    private PreparedStatement queryQuiz_ID;
    private PreparedStatement queryQuestion_ID;
    private PreparedStatement queryQuestion_ID_ALL;
    private PreparedStatement queryAnswer_ID;
    private PreparedStatement queryAnswer_ID_ALL;

    private PreparedStatement queryQuiz_Name;
    private PreparedStatement queryQuestion_Name;
    private PreparedStatement queryAnswer_Name;
    private PreparedStatement queryQuestionByQuizID;
    private PreparedStatement queryAnswerByQuestionID;

    private PreparedStatement deleteQuiz;
    private PreparedStatement deleteQuestion;
    private PreparedStatement deleteAnswer;


    private DataSource() {

    }

    public static DataSource getInstance() {
        return instance;
    }


    public boolean open() {
        try {
            Class.forName("org.sqlite.JDBC");
            conn = DriverManager.getConnection(CONNECTION_STRING);
            foreignKeyActivate = conn.prepareStatement(FOREIGN_KEY_ACTIVATION);
            Statement stmt = conn.createStatement();
//            stmt.executeUpdate(DROP_TABLE_ANSWER);
//            stmt.executeUpdate(DROP_TABLE_QUESTION);
//            stmt.executeUpdate(DROP_TABLE_QUIZ);
            stmt.executeUpdate(CREATE_TABLE_QUIZ);
            stmt.executeUpdate(CREATE_TABLE_QUESTION);
            stmt.executeUpdate(CREATE_TABLE_ANSWER);
            insertQuiz = conn.prepareStatement(INSERT_QUIZ, Statement.RETURN_GENERATED_KEYS);
            insertQuestion = conn.prepareStatement(INSERT_QUESTION, Statement.RETURN_GENERATED_KEYS);
            insertAnswer = conn.prepareStatement(INSERT_ANSWER, Statement.RETURN_GENERATED_KEYS);
            queryQuiz_ID = conn.prepareStatement(QUERY_QUIZ_ID);
            queryQuestion_ID = conn.prepareStatement(QUERY_QUESTION_ID);
            queryQuestion_ID_ALL = conn.prepareStatement(QUERY_QUESTION_ID_ALL);
            queryAnswer_ID_ALL = conn.prepareStatement(QUERY_ANSWER_ID_ALL);
            queryAnswer_ID = conn.prepareStatement(QUERY_ANSWER_ID);
            queryQuiz_Name = conn.prepareStatement(QUERY_QUIZ_NAME);
            queryQuestion_Name = conn.prepareStatement(QUERY_QUESTION_NAME);
            queryAnswer_Name = conn.prepareStatement(QUERY_ANSWER_NAME);
            queryQuestionByQuizID = conn.prepareStatement(QUERY_QUESTION_ALL);
            queryAnswerByQuestionID = conn.prepareStatement(QUERY_ANSWER_CONTENT);

            deleteQuiz = conn.prepareStatement(DELETE_QUIZ);
            deleteQuestion = conn.prepareStatement(DELETE_QUESTION);
            deleteAnswer = conn.prepareStatement(DELETE_ANSWER);

            return true;

        } catch (SQLException | ClassNotFoundException e) {
            System.out.println("Couldn't connect to database: " + e.getMessage());
            return false;
        }

    }

    public void close() {
        try {
            if(insertQuiz != null) {
                insertQuiz.close();
            }
            if(insertQuestion != null) {
                insertQuestion.close();
            }
            if(insertAnswer != null) {
                insertAnswer.close();
            }
            if(queryQuiz_ID != null) {
                queryQuiz_ID.close();
            }
            if(queryQuestion_ID != null) {
                queryQuestion_ID.close();
            }
            if(queryQuestion_ID_ALL != null) {
                queryQuestion_ID_ALL.close();
            }
            if(queryAnswer_ID_ALL != null) {
                queryAnswer_ID_ALL.close();
            }
            if(queryAnswer_ID != null) {
                queryAnswer_ID.close();
            }
            if(queryQuiz_Name != null) {
                queryQuiz_Name.close();
            }
            if(queryQuestion_Name != null) {
                queryQuestion_Name.close();
            }
            if(queryAnswer_Name != null) {
                queryAnswer_Name.close();
            }
            if(queryQuestionByQuizID != null) {
                queryQuestionByQuizID.close();
            }
            if(queryAnswerByQuestionID != null) {
                queryAnswerByQuestionID.close();
            }
            if(deleteQuiz != null) {
                deleteQuiz.close();
            }
            if(deleteQuestion != null) {
                deleteQuestion.close();
            }
            if(deleteAnswer != null) {
                deleteAnswer.close();
            }

            if(foreignKeyActivate != null) {
                foreignKeyActivate.close();
            }



            if (conn != null) {
                conn.close();
            }


        } catch (SQLException e) {
            System.out.println("Couldn't close connection: " + e.getMessage());
        }
    }

    public int insertQuiz(String quiz_name) throws SQLException {
        insertQuiz.setString(1,quiz_name);
        insertQuiz.executeUpdate();
        ResultSet generatedKeys = insertQuiz.getGeneratedKeys();
        if(generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get _id for quiz");
        }
    }

    public int insertQuestion(String name, String content, int type_tf, int type_multi, int type_written, int question_quiz_link) throws SQLException {

        queryQuestion_ID.setString(1, name);
        ResultSet results = queryQuestion_ID.executeQuery();
        ResultSet generatedKeys = insertQuestion.getGeneratedKeys();

        if(results.next()) {
            return results.getInt(1);
        } else {

            // Insert the question
            insertQuestion.setString(1, name);
            insertQuestion.setString(2,content);
            insertQuestion.setInt(3,type_tf);
            insertQuestion.setInt(4,type_multi);
            insertQuestion.setInt(5,type_written);
            insertQuestion.setInt(6,question_quiz_link);
            int affectedRows = insertQuestion.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert question!");
            }
        }
        if(generatedKeys.next()) {
            return generatedKeys.getInt(1);
        } else {
            throw new SQLException("Couldn't get _id for question");
        }
    }



    public int insertAnswer(String name, String content, int type_tf, int type_multi, int type_written, int correct_answer, int question_answer_link) throws SQLException {

        queryAnswer_ID.setString(1,name);
        ResultSet results = queryAnswer_ID.executeQuery();
        if(results.next()) {
            return results.getInt(1);
        } else {
            // Insert the answer
            insertAnswer.setString(1, name);
            insertAnswer.setString(2,content);
            insertAnswer.setInt(3,type_tf);
            insertAnswer.setInt(4,type_multi);
            insertAnswer.setInt(5,type_written);
            insertAnswer.setInt(6,correct_answer);
            insertAnswer.setInt(7,question_answer_link);
            int affectedRows = insertAnswer.executeUpdate();

            if(affectedRows != 1) {
                throw new SQLException("Couldn't insert answer!");
            }

            ResultSet generatedKeys = insertAnswer.getGeneratedKeys();
            if(generatedKeys.next()) {
                return generatedKeys.getInt(1);
            } else {
                throw new SQLException("Couldn't get _id for answer");
            }
        }
    }



    private static int quiz_id;
    public void quizQuestionLink(String name) throws SQLException{
        PreparedStatement queryQuiz = conn.prepareStatement(QUERY_QUIZ_ID);
        queryQuiz.setString(1,name);
        ResultSet results = queryQuiz.executeQuery();

        if (results.next()) {
            quiz_id = results.getInt(1);
        }
//        queryQuiz.close();

    }
    public int quizQuestionLinkReturn() {
        return quiz_id;
    }

    private static int question_id_count;
    public int queryQuestionIDForAll() throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT COUNT( " + COLUMN_QUESTION_ID + ") FROM ");
        sb.append(TABLE_QUESTION + " WHERE ");
        sb.append(COLUMN_QUESTION_QUIZ_LINK_ID + " IN (");
        sb.append("SELECT " + "MAX(" + COLUMN_QUIZ_ID + ") FROM " + TABLE_QUIZ + ") ");
        Statement statement = conn.createStatement();
        ResultSet results = statement.executeQuery(sb.toString());


//        ResultSet results = queryQuestion_ID_ALL.executeQuery();
        if(results.next()) {
            question_id_count = results.getInt(1)+1;
        }
        return question_id_count;
    }

    private static int answer_id_count = 0;
    public int queryAnswerIDForAll() throws SQLException {
        ResultSet results = queryAnswer_ID_ALL.executeQuery();
        if(results.next()) {
            answer_id_count = results.getInt(1);
        }
        return answer_id_count;
    }

    private static String quiz_name;
    public String queryQuizName(int id) throws SQLException{
        PreparedStatement getQuizName = conn.prepareStatement(QUERY_QUIZ_NAME);
        getQuizName.setInt(1,id);
        ResultSet results = getQuizName.executeQuery();
        if(results.next()) {
            quiz_name = results.getString(1);
        }
//        getQuizName.close();
        return quiz_name;
    }

    private static int question_id;
    public int questionAnswerLink(String name) throws SQLException{
        PreparedStatement queryQuestion = conn.prepareStatement(QUERY_QUESTION_ID);
        queryQuestion.setString(1,name);
        ResultSet results = queryQuestion.executeQuery();
        if(results.next()){
            question_id = results.getInt(1);
        }
//        queryQuestion.close();
        return question_id;

    }
    //    private static String question_content;
    public Question queryQuestionContent(int quiz_id, String question_content) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quiz_id  + " AND ");
        sb.append(COLUMN_QUESTION_CONTENT + " = " + "'" + question_content + "'");


        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {
            List<Question> questions = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Question question = new Question();
                question.setId(results.getInt(INDEX_QUESTION_ID));
                question.setName(results.getString(INDEX_QUESTION_NAME));
                question.setContent(results.getString(INDEX_QUESTION_CONTENT));
                question.setMulti_choice(results.getInt(INDEX_QUESTION_TYPE_MULTI));
                question.setTrue_false(results.getInt(INDEX_QUESTION_TYPE_TF));
                question.setWritten(results.getInt(INDEX_QUESTION_TYPE_WRITTEN));
                questions.add(question);
            }
            if (questions.isEmpty()) {
                return null;
            } else {
                return questions.get(0);
            }
        } catch (SQLException e) {
            System.out.println("Query pullSelectedQuestionByQuiz failed: " + e.getMessage());
            return null;
        }

    }

    public void setDeleteQuestion(String question_name) throws SQLException {
        try {
            deleteQuestion.setString(1, question_name);
            deleteQuestion.executeUpdate();
        }catch(Exception e) {
            System.out.println("Cannot delete selected DataModel.Question: " + e.getMessage());
        }
    }

    public void setDeleteAnswer(String question_name) throws SQLException {
        try {
            deleteAnswer.setString(1,question_name);
            deleteAnswer.executeUpdate();
        }catch(Exception e) {
            System.out.println("Cannot delete selected DataModel.Answer: " + e.getMessage());
        }
    }

    public void setDeleteQuiz(String quiz_name) throws SQLException {
        try {
            deleteQuiz.setString(1,quiz_name);
            deleteQuiz.executeUpdate();
        }catch(Exception e) {
            System.out.println("Cannot delete selected DataModel.Quiz: " + e.getMessage());
        }
    }

    public void deleteAnswerByName(String answer_name) {
        StringBuilder sb_1 = new StringBuilder("DELETE FROM ");
        sb_1.append(TABLE_ANSWER + " WHERE " + COLUMN_QUESTION_ANSWER_LINK_ID + " IN ( " + " SELECT " + COLUMN_QUESTION_ID + " FROM " + TABLE_QUESTION + " WHERE " +
                COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quizQuestionLinkReturn() + ") AND " + COLUMN_ANSWER_NAME + " = '" + answer_name + "'");

        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(sb_1.toString());

        }catch (Exception e) {
            System.out.println("Cannot Delete DataModel.Quiz: " + e.getMessage());
        }

    }




    public List<Question> pullSelectedQuestionByQuiz(int quiz_id,String question_name) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quiz_id + " AND " + COLUMN_QUESTION_NAME + " = '" + question_name + "'");
//        if (sortOrder != ORDER_BY_NONE) {
        sb.append(" ORDER BY ");
        sb.append(COLUMN_QUESTION_NAME);
        sb.append(" COLLATE NOCASE ");
//            if (sortOrder == ORDER_BY_DESC) {
//                sb.append("DESC");
//            } else {
        sb.append("ASC");
//            }
//        }



        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Question> questions = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Question question = new Question();
                question.setId(results.getInt(INDEX_QUESTION_ID));
                question.setName(results.getString(INDEX_QUESTION_NAME));
                question.setContent(results.getString(INDEX_QUESTION_CONTENT));
                question.setMulti_choice(results.getInt(INDEX_QUESTION_TYPE_MULTI));
                question.setTrue_false(results.getInt(INDEX_QUESTION_TYPE_TF));
                question.setWritten(results.getInt(INDEX_QUESTION_TYPE_WRITTEN));
                questions.add(question);
            }
            if(questions.isEmpty()) {
                return null;
            } else {
                return questions;
            }
        } catch (SQLException e) {
            System.out.println("Query pullSelectedQuestionByQuiz failed: " + e.getMessage());
            return null;
        }


    }
    public List<Question> pullAllQuestionsByQuiz(int quiz_id) throws SQLException {
        StringBuilder sb = new StringBuilder("SELECT * FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quiz_id);


        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Question> questions = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch (InterruptedException e) {
                    System.out.println("Interupted: " + e.getMessage());
                }
                Question question = new Question();
                question.setId(results.getInt(INDEX_QUESTION_ID));
                question.setName(results.getString(INDEX_QUESTION_NAME));
                question.setContent(results.getString(INDEX_QUESTION_CONTENT));
                question.setMulti_choice(results.getInt(INDEX_QUESTION_TYPE_MULTI));
                question.setTrue_false(results.getInt(INDEX_QUESTION_TYPE_TF));
                question.setWritten(results.getInt(INDEX_QUESTION_TYPE_WRITTEN));
                question.setQuiz_link(results.getInt(INDEX_QUESTION_QUIZ_LINK_ID));

                questions.add(question);

            }
            if(questions.isEmpty()) {
                System.out.println("THE QUIZ IS EMPTY OF QUESTIONS!");
                return null;
            } else {
                return questions;
            }

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }


    public List<Question> queryQuestion(int quiz_id) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_QUESTION + " WHERE ");
        sb.append(COLUMN_QUESTION_QUIZ_LINK_ID + " IN (");
        sb.append("SELECT " + quiz_id + " FROM " + TABLE_QUIZ + ") ");


        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Question> questions = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interupted: " + e.getMessage());
                }
                Question question = new Question();
                question.setId(results.getInt(INDEX_QUESTION_ID));
                question.setName(results.getString(INDEX_QUESTION_NAME));
                question.setContent(results.getString(INDEX_QUESTION_CONTENT));
                questions.add(question);
            }

            return questions;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }



    public List<Answer> queryAnswer(int quiz_id, String question_name) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_ANSWER + " WHERE " + COLUMN_QUESTION_ANSWER_LINK_ID + " IN (" +
                " SELECT " + COLUMN_QUESTION_ID + " FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUIZ_ID + " = " + quiz_id + " AND " +
                COLUMN_QUESTION_NAME + " = '" + question_name + "')");
//        if (sortOrder != ORDER_BY_NONE) {
        sb.append(" ORDER BY ");
        sb.append(COLUMN_ANSWER_NAME);
        sb.append(" COLLATE NOCASE ");
//            if (sortOrder == ORDER_BY_DESC) {
//                sb.append("DESC");
//            } else {
        sb.append("ASC");
//            }
//        }



        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            List<Answer> answers = new ArrayList<>();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interuppted: " + e.getMessage());
                }
                Answer answer = new Answer();
                answer.setId(results.getInt(INDEX_ANSWER_ID));
                answer.setName(results.getString(INDEX_ANSWER_NAME));
                answer.setContent(results.getString(INDEX_ANSWER_CONTENT));
                answer.setMulti_choice(results.getInt(INDEX_ANSWER_TYPE_MULTI));
                answer.setTrue_false(results.getInt(INDEX_ANSWER_TYPE_TF));
                answer.setWritten(results.getInt(INDEX_ANSWER_TYPE_WRITTEN));
                answer.setCorrect_answer(results.getInt(INDEX_ANSWER_CORRECT_ANSWER));
                answer.setQuestion_link(results.getInt(INDEX_QUESTION_ANSWER_LINK_ID));
                answers.add(answer);
            }

            return answers;

        } catch (SQLException e) {
            System.out.println("QueryAnswer failed: " + e.getMessage());
            return null;
        }
    }


    public void DeleteQuiz() {
        StringBuilder sb_1 = new StringBuilder("DELETE FROM ");
        sb_1.append(TABLE_ANSWER + " WHERE " + COLUMN_QUESTION_ANSWER_LINK_ID + " IN ( " + " SELECT " + COLUMN_QUESTION_ID + " FROM " + TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quizQuestionLinkReturn() + ")");
        StringBuilder sb_2 = new StringBuilder("DELETE FROM ");
        sb_2.append(TABLE_QUESTION + " WHERE " + COLUMN_QUESTION_QUIZ_LINK_ID + " = " + quizQuestionLinkReturn());
        StringBuilder sb_3 = new StringBuilder("DELETE FROM ");
        sb_3.append(TABLE_QUIZ + " WHERE " + COLUMN_QUIZ_ID + " = " + quizQuestionLinkReturn());

        try{
            Statement statement = conn.createStatement();
            statement.executeUpdate(sb_1.toString());
            statement.executeUpdate(sb_2.toString());
            statement.executeUpdate(sb_3.toString());

        }catch (Exception e) {
            System.out.println("Cannot Delete DataModel.Quiz: " + e.getMessage());
        }

    }
    public ObservableList<Quiz> queryQuiz(int sortOrder) {

        StringBuilder sb = new StringBuilder("SELECT * FROM ");
        sb.append(TABLE_QUIZ);
        if (sortOrder != ORDER_BY_NONE) {
            sb.append(" ORDER BY ");
            sb.append(COLUMN_QUIZ_NAME);
            sb.append(" COLLATE NOCASE ");
            if (sortOrder == ORDER_BY_DESC) {
                sb.append("DESC");
            } else {
                sb.append("ASC");
            }
        }

        try (Statement statement = conn.createStatement();
             ResultSet results = statement.executeQuery(sb.toString())) {

            ObservableList<Quiz> quiz_items = FXCollections.observableArrayList();
            while (results.next()) {
                try {
                    Thread.sleep(20);
                } catch(InterruptedException e) {
                    System.out.println("Interupted: " + e.getMessage());
                }
                Quiz quiz = new Quiz();
                quiz.setId(results.getInt(INDEX_QUIZ_ID));
                quiz.setName(results.getString(INDEX_QUIZ_NAME));
                quiz_items.add(quiz);
            }

            return quiz_items;

        } catch (SQLException e) {
            System.out.println("Query failed: " + e.getMessage());
            return null;
        }
    }





}
