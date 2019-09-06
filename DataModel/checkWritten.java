package DataModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class checkWritten {
    private String correct;
    private String userInput;
    private String[] common = new String[]{"the","a","be","and","is","are","were","has","have","had","been"};
    private String[] negative = new String[]{"not","never","^[a-z]+(?:nt|n't)+$","none"};
    private ArrayList<String> common_list = new ArrayList<>(Arrays.asList(common));
    private ArrayList<String> negative_list = new ArrayList<>(Arrays.asList(negative));
    private ArrayList<String> neg = new ArrayList<>();

    public checkWritten(String correct, String userInput) {
        this.correct = correct;
        this.userInput = userInput;

    }


    public ArrayList<String> tokenizeAnswer(String input) {
        //use "." to cut out each sentences
        if(input.charAt(input.length()-1) != '.') {
            input = input + ".";
        }
        ArrayList<String> sentence = new ArrayList<>();
        StringBuilder sb = new StringBuilder();
        StringTokenizer st = new StringTokenizer(input);
        ArrayList<String> scan_words = new ArrayList<>();
        //get tokens (each word)
        while(st.hasMoreTokens()){
            scan_words.add(st.nextToken());
        }
        //find '.' for the end of sentence or word
        for(int i = 0; i < scan_words.size();i++) {
            sb.append(scan_words.get(i) + " ");
            for(int n = 0; n < scan_words.get(i).length();n++) {
                if(scan_words.get(i).charAt(n) == '.' && n == scan_words.get(i).length()-1) {
                    sentence.add(sb.toString());
                    sb.delete(0,sb.length());
                }
            }
        }
        return sentence;
    }
    //if the word is negative, it will return that word, if not, empty string
    //negative words can be useful when the user had typed completely opposite, but similar inputs:
    /*
    Standard -> I like coding
    User -> I don't like coding
     */
    private static String negativeWord(ArrayList<String> list, String word) {
        for (int n = 0; n < list.size(); n++) {
            Pattern pattern = Pattern.compile(list.get(n));
            Matcher matcher = pattern.matcher(word);
            if(matcher.matches()) {
                return word;
            }

        }


        return "";
    }

    //comparing two inputs
    public String compareTwo(String one, String two) {
        //equalize
        one = one.trim().toLowerCase(); two = two.trim().toLowerCase();

        if (one.equals(two)) {
            return one;
        } else {
            StringTokenizer st = new StringTokenizer(one);
            StringTokenizer st_2 = new StringTokenizer(two);
            ArrayList<String> standardList = new ArrayList<>();
            ArrayList<String> filteredList = new ArrayList<>();

            //filter out the standard answer of any common words and catch any negative words for clearer comparison
            while (st.hasMoreTokens()) {
                String temp = st.nextToken();
                for(int n = 0; n < common_list.size();n++) {
                    if (temp.equals(common_list.get(n))) {
                        temp = "";
                    } else {
                        String neg_word = negativeWord(negative_list,temp);
                        if(!standardList.contains(neg_word)) {
                            standardList.add(neg_word);
                            neg.add(neg_word);
                        }
                    }
                }

                if(temp != "") {
                    standardList.add(temp);
                }



            }
            //filter out the user's input of any common words and catch any negative words for clearer comparison
            while (st_2.hasMoreTokens()) {

                String temp = st_2.nextToken();


                for (int i = 0; i < standardList.size(); i++) {
                    if (temp.equals(standardList.get(i))) {
                        filteredList.add(standardList.get(i));
                        break;
                    } else {
                        String neg_word = negativeWord(negative_list,temp);
                        if(!filteredList.contains(neg_word)) {
                            filteredList.add(neg_word);
                            neg.add(neg_word);
                        }


                    }

                }

            }

            //gave 20% as a rate of difference to margin out some errors such as typos etc.
            if(Math.abs(standardList.size() - filteredList.size()) <= Math.round(standardList.size()*(0.2))) {
                for(int n = 0; n < neg.size();n++) {
                    if(filteredList.contains(neg.get(n)) && !standardList.contains(neg.get(n))) {
                        return "";
                    }
                }

                return one;
            }
            return "";

        }
    }


}
