/**
 * Created By: Basil Assi
 * ID Number: 1192308
 * Date: 6/12/2023
 * Time: 7:33 PM
 * Project Name: Compiler Project
 */
import java.util.ArrayList;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.TreeSet;

public class Parser {
	
	/*This attribute is used to hold the list of tokens that will be parsed by the methods in the Parser class. 
	  Each token is an array where the first element is the line number and the second element is the token itself.
	  It's a 2D array because you can have multiple tokens on multiple lines of code. 
	  */
	private String[][] tokens;
	/*This is a counter that is used to keep track of the current position while traversing through the tokens array. 
	When the parsing starts, position is set to 0.*/
	private int position; 

    // This method accepts a 2D String array of tokens, and attempts to parse them according to specific rules.
    // Returns a success message if parsing was successful, or the error message if a ParseException occurred.
	public String parse(String[][] tokens) {
		try {
			this.tokens = tokens;
			projectDeclaration();
			if (position != this.tokens.length) {
				return "Error: Unexpected token '" + this.tokens[position][1] + "' at line " + this.tokens[position][0];
			}
			return "Successful parsing.";
		} catch (ParseException e) {
			return e.getMessage();
		}

	}
    // This method begins the parsing process for a project declaration.
    // A project declaration must start with the project keyword, then the project name, and then a period.
	// Check if the project is properly started and ended.
	private void projectDeclaration() {
		project();
		consume(".");
	}
	
	
	 // The project method manages the parsing of an entire project declaration.
    // It consists of a project heading, a series of declarations, and a compound statement.
	  // Check the full structure of a project.
	private void project() {
		projectHeading();
		declarations();
		compoundStmt();
	}
	
	
	
	 // The projectHeading method manages the parsing of the project heading.
    // A project heading starts with the 'project' keyword, followed by a name, and then a semicolon.
	// Check if a project has the right beginning.
	private void projectHeading() {
		consume("project");
		consume("name");
		consume(";");
	}
	
	
	
	// Check for all the declarations in a project.
	private void declarations() {
		constDecl();
		varDecl();
		subroutineDecl();
	}

	
	 // The constDecl method manages the parsing of a constant declaration.
    // A constant declaration starts with the 'const' keyword, followed by a list of constants.
	  // Check if the constants are declared correctly.
	private void constDecl() {
		if (lookahead()!= null && lookahead().equals("const")) {
			consume("const");
			constList();
		}
	}
	
	
	 // The constList method handles the parsing of a list of constants within a constant declaration.
    // Each constant in the list must have a name, followed by an equals sign, an integer value, and a semicolon.
	// Check if the list of constants is correct.
	private void constList() {
		if (lookahead()!= null && lookahead().equals("name")) {
			consume("name");
			consume("=");
			consume("integer-value");
			consume(";");
			constList();
		}
		
		
	}//lookahead()!= null &&

	
	// Check if the variables are declared correctly.
	private void varDecl() {
		if ( lookahead().equals("var")) {
			consume("var");
			varList();
		}
	}

	
	 // Check if the list of variables is correct.
	private void varList() {
		if (lookahead()!= null && lookahead().equals("name")) {
			varItem();
			consume(";");
			varList();
		}
	}

	// Check if a variable item is correct.
	private void varItem() {
		nameList();
		consume(":");
		consume("int");
	}
	
	
	// Check if the list of names is correct.
	private void nameList() {
		consume("name");
		moreNames();
	}

	 // Check for more names in the list.
	private void moreNames() {
		if (lookahead()!= null && lookahead().equals(",")) {
			consume(",");
			nameList();
		}
	}

	// Check if the routine (a piece of code to do a specific task) is declared correctly.
	private void subroutineDecl() {
		if (lookahead()!= null && lookahead().equals("routine")) {
			subroutineHeading();
			declarations();
			compoundStmt();
			consume(";");
		}
	}

	// Check if the routine has the right beginning.
	private void subroutineHeading() {
		consume("routine");
		consume("name");
		consume(";");
	}
	
    // Check if a group of statements is correctly wrapped between 'start' and 'end'.

	private void compoundStmt() {
		consume("start");
		stmtList();
		consume("end");
	}

    // Check if the list of statements is correct.
	private void stmtList() {
		if (lookahead()!= null && (lookahead().equals("name") || lookahead().equals("input") || lookahead().equals("output") || lookahead().equals("if") || lookahead().equals("loop") || lookahead().equals("start"))) {
			statement();
			consume(";");
			stmtList();
		}
	}
	
	// Check each type of statement.
	private void statement() {
		String lookahead = lookahead();
		if (lookahead == null)
			return;
		switch (lookahead) {
		case "name":
			assStmt();
			break;
		case "input":
		case "output":
			inoutStmt();
			break;
		case "if":
			ifStmt();
			break;
		case "loop":
			loopStmt();
			break;
		case "start":
			compoundStmt();
			break;
		default:
			System.err.println("Error: Unexpected token '" + lookahead + "' at line " + tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
		}
	}

    // Check if assignment statements are correct.
	private void assStmt() {
		consume("name");
		consume(":=");
		arithExp();
	}
	
	
	// Check arithmetic expressions.
	private void arithExp() {
	    term();
	    arithExpPrime();
	}

	 // Check more of arithmetic expressions.
	private void arithExpPrime() {
	    if (lookahead() != null && (lookahead().equals("+") || lookahead().equals("-"))) {
	        addSign();
	        term();
	        arithExpPrime();
	    }
	}

    // Check terms in arithmetic expressions.
	private void term() {
	    factor();
	    termPrime();
	}
	
	 // Check more terms in arithmetic expressions.
	private void termPrime() {
	    if (lookahead() != null && (lookahead().equals("*") || lookahead().equals("/") || lookahead().equals("%"))) {
	        mulSign();
	        factor();
	        termPrime();
	    }
	}

	 // Check each part of an arithmetic expression.
	private void factor() {
	    if (lookahead() != null) {
	        if (lookahead().equals("(")) {
	            consume("(");
	            arithExp();
	            consume(")");
	        } else {
	            nameValue();
	        }
	    } else {
	        System.err.println("Error: Expected factor but found nothing at line " + tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	}
   
	  // Check the name or value in an arithmetic expression.
	private void nameValue() {
	    if (lookahead() != null && (lookahead().equals("name") || lookahead().equals("integer-value"))) {
	        consume(lookahead());
	    } else {
	        System.err.println("Error: Expected name or integer value but found '" + lookahead() + "' at line " + tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	}
	// Check '+' or '-' signs.
	private void addSign() {
	    if (lookahead() != null && (lookahead().equals("+") || lookahead().equals("-"))) {
	        consume(lookahead());
	    } else {
	        System.err.println("Error: Expected '+' or '-' but found '" + lookahead() + "' at line " + tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	}

	// Check '*', '/' or '%' signs.
	
	private void mulSign() {
	    if (lookahead() != null && (lookahead().equals("*") || lookahead().equals("/") || lookahead().equals("%"))) {
	        consume(lookahead());
	    } else {
	        System.err.println("Error: Expected '*', '/' or '%' but found '" + lookahead() + "' at line "+ tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	}
	
	 // Check input or output statements.
	
	private void inoutStmt() {
		if (lookahead().equals("input")) {
			consume("input");
			consume("(");
			consume("name");
			consume(")");
		} else if (lookahead().equals("output")) {
			consume("output");
			consume("(");
			nameValue();
			consume(")");
		}
	}
	
	 // Check 'if' statements.
	
	private void ifStmt() {
		consume("if");
		consume("(");
		boolExp();
		consume(")");
		consume("then");
		statement();
		elsePart();
	//	consume("endif");
	}
	
	
	 // Check the boolean expressions in 'if' and 'loop' statements.
	private void boolExp() {
		nameValue();
		relationalOper();
		nameValue();
	}
	
	
	// Check the operators used in boolean expressions.
	
	private void relationalOper() {
	    String lookahead = lookahead();
	    if (lookahead == null) {
	        System.err.println("Error: Expected relational operator but found nothing at line " + tokens[position][0]);
			throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	    
	    switch (lookahead) {
	        case "=":
	        case "<>":
	        case "<":
	        case "<=":
	        case ">":
	        case ">=":
	            consume(lookahead);
	            break;
	        default:
	            System.err.println("Error: Expected relational operator but found '" + lookahead + "' at line " + tokens[position][0]);
				throw new ParseException("detect error in line: "+tokens[position][0]);
	    }
	}


    // Check the 'else' part in 'if' statements.

	private void elsePart() {
		if (lookahead().equals("else")) {
			consume("else");
			statement();
		}
	}
	 // Check 'loop' statements.
	private void loopStmt() {
		consume("loop");
		consume("(");
		boolExp();
		consume(")");
		consume("do");
		statement();
	}

    // Check the next word without moving the position.
	
	private String lookahead() {
		if (position < tokens.length) {
			return tokens[position][1];
		}
		return null;
	}

	// Move to the next word if it's what we expect.
	
	private void consume(String expectedToken) {
		if (position < tokens.length && tokens[position][1].equals(expectedToken)) {
			position++;
		} else {
			System.err.println("Error: Expected '" + expectedToken + "' but found '" + tokens[position][1]
				+", at line:"+tokens[position][0]);	//+ "' at line " + lineNumber);
			throw new ParseException("detect error in line: "+tokens[position][0]);
		}
	}

	// This exception is used when something unexpected is found.
	public static class ParseException extends RuntimeException {
		public ParseException(String message) {
			super(message);
		}
	}
}