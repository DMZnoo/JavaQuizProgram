package Scores;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class scoreKeeper {
    private static scoreKeeper instance = new scoreKeeper();

    private static String filename = "";
    private ObservableList<String> score;

    public static scoreKeeper getInstance() {
        return instance;
    }


    public void addText(String input) {
        score.add(input);
    }

    public void makeQuizDir(String dir_name) {
        filename = dir_name + ".txt";
        Path path = Paths.get(System.getProperty("user.dir")+ File.separator + "src" +File.separator + "Scores"+ File.separator,filename);

        try {
            Files.createFile(path);
            System.out.println("Success!");


        } catch(Exception e) {
            System.out.println("Cannot create file: " + e.getMessage());
        }

    }

    public void loadScore() throws IOException {
        score = FXCollections.observableArrayList();
        Path path = Paths.get(System.getProperty("user.dir")+ File.separator + "src" +File.separator + "Scores"+ File.separator,filename);
        BufferedReader br = Files.newBufferedReader(path);
        String input;

        try {

            while((input = br.readLine()) != null) {
                String[] itemPieces = input.split("\n");
                String content = itemPieces[0];
                score.add(content);
            }



        } finally {
            if(br != null) {
                br.close();
            }
        }
    }

    public void storeScore() throws IOException {
        Path path = Paths.get(System.getProperty("user.dir")+ File.separator + "src" +File.separator + "Scores"+ File.separator,filename);
        BufferedWriter bw = Files.newBufferedWriter(path);
        try {
            Iterator<String> iter = score.iterator();
            while(iter.hasNext()) {
                String content = iter.next();
                bw.write(String.format("%s\n", content));
                bw.newLine();
            }
        }finally {
            if(bw != null) {
                bw.close();
            }
        }

    }








}
