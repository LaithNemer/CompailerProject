/**
 * Created By: Basil Assi
 * ID Number: 1192308
 * Date: 6/1/2023
 * Time: 4:55 PM
 * Project Name: Compiler Project
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tokenizer {

	
	// These are special symbols used in our programming language.
    private static final String[] SYMBOLS = {
        ":=", "<>", "<=", ">=", ".", ":", ";", ",", "+", "(", ")", "+", "-", "*", "/", "%", "=", "<", ">"
    };

    // These are reserved words in our programming language, which cannot be used as variable names.
    private static final String[] RESERVED_WORDS = {
        "project", "const", "var", "int", "routine", "start", "end", "input", "output", "if",
        "then", "endif", "else", "loop", "do"
    };


    // This ArrayList keeps track of tokens in each line.
    static ArrayList<Integer> tokensInline = new ArrayList<>();

    // This method processes a file and converts the code into a list of tokens.
    public static String[][] getTokens(String fileName) {
        ArrayList<String> tokens = new ArrayList<>();
        int lineNumber = 0;

        // This block of code reads the file line by line.
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                lineNumber++;

                line = line.trim();

                if (!line.isEmpty()) {
                    // This splits the line into words and symbols.
                    String[] words = line.split("\\s+|(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
                    // Loop through each word in the line
                    for (int i = 0; i < words.length; i++) {
                        String word = words[i];
                        if (isSymbol(word)) {
                            if (i + 1 < words.length) {
                                String compoundSymbol = word + words[i + 1];
                                if (isSymbol(compoundSymbol)) {
                                    tokens.add(compoundSymbol);
                                    i++;
                                } else {
                                    tokens.add(word);
                                }
                            } else {
                                tokens.add(word);
                            }
                        } else if (isReservedWord(word)) {
                            tokens.add(word);
                        } else if (isName(word)) {
                            tokens.add("name");
                        } else if (isIntegerValue(word)) {
                            tokens.add("integer-value");
                        } else {
//                          System.err.printf("Unknown symbol '%s' at line %d\n", word, lineNumber);
                        }
                    }
                }

                // Keep track of the number of tokens in each line.
                tokensInline.add(tokens.size());
            }
        } catch (IOException e) {
            /*
             *  Handle any error while reading the file.
             */
            System.err.printf("Failed to read file '%s': %s\n", fileName, e.getMessage());
        }
        // Convert the list of tokens into a 2D array.
        return tokensTo2D(tokens);
    }



    // Convert the list of tokens into a 2D array.
    private static String[][] tokensTo2D(ArrayList<String> tokens) {
        int l=0;
        String[][] tokenTypes = new String[tokens.size()][2];
        for (int i = 0; i < tokens.size(); i++) {
            if(i>=tokensInline.get(l)) {
                l++;
            }
            tokenTypes[i][0] = (l+1)+"";
            tokenTypes[i][1] = tokens.get(i);
        }
        return tokenTypes;
    }

    
    // This method processes a file and converts the code into a list of tokens.
//    public static String[][] getTokens(String fileName) {
//        ArrayList<String> tokens = new ArrayList<>();
//        int lineNumber = 0;
//
//        // This block of code reads the file line by line.
//        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
//            String line;
//            while ((line = reader.readLine()) != null) {
//                lineNumber++;
//                line = line.trim();
//                if (line.isEmpty()) {
//                	//lineNumber++;
//                    continue;
//                }
//                
//                // This splits the line into words and symbols.
//                String[] words = line.split("\\s+|(?<=[^a-zA-Z0-9])|(?=[^a-zA-Z0-9])");
//                // Loop through each word in the line
//                for (int i = 0; i < words.length; i++) {
//                    String word = words[i];
//                    if (isSymbol(word)) {
//                        if (i + 1 < words.length) {
//                            String compoundSymbol = word + words[i + 1];
//                            if (isSymbol(compoundSymbol)) {
//                                tokens.add(compoundSymbol);
//                                i++;
//                            } else {
//                                tokens.add(word);
//                            }
//                        } else {   
//                            tokens.add(word);
//                        }
//                    } else if (isReservedWord(word)) {
//                        tokens.add(word);
//                    } else if (isName(word)) {
//                        tokens.add("name");
//                    } else if (isIntegerValue(word)) {
//                        tokens.add("integer-value");
//                    } else {
////                        System.err.printf("Unknown symbol '%s' at line %d\n", word, lineNumber);
//                    }
//                }   // Keep track of the number of tokens in each line.
//                tokensInline.add(tokens.size());
//            }
//        } catch (IOException e) {
//        	  /*
//        	   *  Handle any error while reading the file. 
//        	   */
//            System.err.printf("Failed to read file '%s': %s\n", fileName, e.getMessage());
//        }
//        // Convert the list of tokens into a 2D array.
//        return tokensTo2D(tokens);
//    }

    // Check if a word is a symbol.
    private static boolean isSymbol(String word) {
        for (String symbol : SYMBOLS) {
            if (word.equals(symbol)) {
                return true;
            }
        }
        return false;
    }

    // Check if a word is a reserved word.
    private static boolean isReservedWord(String word) {
        for (String reservedWord : RESERVED_WORDS) {
            if (word.equals(reservedWord)) {
                return true;
            }
        }
        return false;
    }
    
    // Check if a word is a valid name.
    private static boolean isName(String word) {
        Pattern pattern = Pattern.compile("^[a-zA-Z]+[a-zA-Z0-9]*$");
        Matcher matcher = pattern.matcher(word);
        return matcher.matches();
    }
    
    // Check if a word is an integer value.
    private static boolean isIntegerValue(String word) {
        try {
            Integer.parseInt(word);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }
    
//    // Convert the list of tokens into a 2D array.
//    private static String[][] tokensTo2D(ArrayList<String> tokens) {
//    	int l=0;
//        String[][] tokenTypes = new String[tokens.size()][2];
//        for (int i = 0; i < tokens.size(); i++) {
//        	if(i>=tokensInline.get(l)) {
//	    		l++;
//	    	}
//            tokenTypes[i][0] = (l+1)+"";
//            tokenTypes[i][1] = tokens.get(i);
//        }
//        return tokenTypes;
//    }

}
