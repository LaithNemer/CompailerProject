
/**
 * Created By: Basil Assi
 * ID Number: 1192308
 * Date: 6/28/2023
 * Time: 10:13 PM
 * Project Name: Compiler Project
 */


import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Driver {

	// This list will hold the Non Terminal symbols in our grammar.
	private static ArrayList<String> NTerminals;
	
	private static String nameOfFile = "";
	private static boolean exists;
	
	public static void main(String[] args) throws FileNotFoundException {
		
        // This is for user input
		Scanner scanner = new Scanner(System.in);		
		
		// Keep asking for a file until we get a valid one
		while(true) {
			 // Ask the user to type in the name of the file they want to check
            System.out.println("Please Enter file name (should end with '.txt'), for example 'code_example3.txt': ");
            String fileName = scanner.nextLine();
            
            // Check if the file name ends with ".txt"
            if(fileName.endsWith(".txt")) {
            	nameOfFile = nameOfFile +fileName;
                File tmpDir = new File(nameOfFile);
                System.out.println("file name " + fileName);
                // Check if the file actually exists
                 exists = tmpDir.exists();
                 
              // If file exists, we can break out of the loop
                if(exists){
                    break;
                } else {
                	/* 
                	 *  File doesn't exist, ask again 
                	 */
                    System.err.println("File does not exist. Please enter a correct file name.");
                }
            } else {
            	 /* File name doesn't end with ".txt", ask again */
                System.err.println("File name must end with '.txt'. Please enter a correct file name.");
            }
            System.out.println(" ");
	            
	        }
		
		 // Get the tokens from the input file
		String[][] tokens= Tokenizer.getTokens(nameOfFile);
		 // Create a new parser
		Parser parser= new Parser();
		
		// print the tokens in the console
		traverseTokens(tokens);
		System.out.println("--------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------");
		System.out.println("--------------------------------------------------------------");
		System.out.println("--------------------- After Traverse Tokens ------------------");
		System.out.println(parser.parse(tokens));
		
		// Close the scanner
		scanner.close();
	}

	
    
    // This method prints the tokens in the console
    public static void traverseTokens(String[][] tokens) {
    	int i=0;
        System.out.println("\n\nTokens:");
        for (String[] token : tokens) {
            System.out.println("index:"+i+++", line:"+token[0] + ",token:" + token[1]);
        }
        System.out.println("\n\n");
    }

    

}
