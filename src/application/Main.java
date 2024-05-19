package application;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.controlsfx.control.CheckComboBox;


/* ----------------------  creating the main class ------------------------*/

public class Main extends Application {
	
	
	
	/* ----------------------  Declaring the attributes of the main class ------------------------*/
	
	private Stage primaryStage;
    private int rows;
    private int columns;
    private List<List<CheckComboBox<Integer>>> checkBoxMatrix;
    private int [][] transitionMatrix;// (transition matrix for DFA) 
    private List<Integer> finalStates;
    private int initialState;
    private List<Character> characterList;
    private Label errorLabel;

	
	
	
	
    /* ----------------------  The launch method that launches the application ------------------------*/
    
    public static void main(String[] args) {
        launch(args);
    }

    /* ----------------------  the start method that launches stage and first scene  ------------------------*/
    @Override
    public void start(Stage primaryStage) {
        this.primaryStage = primaryStage;
        showInputScene();
    }
	
	
    /* ----------------------  the method of the first scene  ------------------------*/
    
private void showInputScene() {
    
	/* ----------------------  creating the grid and the UI elements  ------------------------*/
	VBox inputLayout = new VBox(10);
    inputLayout.setStyle("-fx-background-color: lightblue; -fx-padding: 20px;");

    Label rowsLabel = new Label("Enter number of states:");
    TextField rowsField = new TextField();

    Label charsLabel = new Label("Enter any characters:");
    TextField charsField = new TextField();

    Button submitButton = new Button("Submit");

    errorLabel = new Label();
    errorLabel.setStyle("-fx-text-fill: red;");

    
    /* ----------------------  set the submit button on action  ------------------------*/
    submitButton.setOnAction(e -> {
        String rowsText = rowsField.getText().trim(); //extracting the text from the text field and removing white space
        String charsText = charsField.getText().trim();

        if (rowsText.isEmpty() || charsText.isEmpty()) {
            errorLabel.setText("Error: Both fields are required.");
            return;}// return to the caller method
        
     
    /* ----------------------  checking and extracting the elements  ------------------------*/

        try {
            int inputRows = Integer.parseInt(rowsText); // getting the number of states
            
            
            String[] characters = charsText.split("\\s+"); // sub-strings are stored in the var characters  

            characterList = new ArrayList<>();
            for (String row : characters) {                // the loop processes each sub_string to extract the characters
                for (char c : row.toCharArray()) {
                    if (characterList.contains(c)) {
                        errorLabel.setText("Error: Duplicate character '" + c + "'. Please correct.");
                        return;
                    }
                    characterList.add(c); // it adds it if it doesn't exist already in the list
                }
            }

    /* ----------------------  Assigning the rows and columns attribute and calling the next scene if there isn't an error  ------------------------*/
            
            rows = inputRows;
            columns = characterList.size();

            errorLabel.setText("");

            showTableSelectionScene();
            
        } catch (NumberFormatException ex) {
            errorLabel.setText("Error: Invalid input for number of states. Please enter an integer value.");
            ex.printStackTrace();
        }
    });

    inputLayout.getChildren().addAll(rowsLabel, rowsField, charsLabel, charsField, submitButton, errorLabel);

    Scene inputScene = new Scene(inputLayout, 400, 250);

    primaryStage.setScene(inputScene);
    primaryStage.getIcons().add(new Image("artificial-intelligence.png"));
    primaryStage.setTitle("Automaton");
    primaryStage.centerOnScreen();
    primaryStage.show();
}


private void showTableSelectionScene() {
    
	/* ----------------------  creating 3 grids in our scene TableSelectionScene ------------------------*/
	
	GridPane tableGrid = new GridPane();
    HBox initialfinalestates = new HBox(10);
    HBox buttonGrid = new HBox(10);

    /* ----------------------  setting up the labels of each row and column ------------------------*/
    
    for (int j = 0; j < columns; j++) {
        Label label = new Label(String.valueOf(characterList.get(j))); // string.valueOf transforms the character at the index j in the characterList
        label.setPadding(new Insets(0, 10, 0, 10)); // padding (empty space) around the content of the label , (top,right,bottom,left)
        tableGrid.add(label, j + 1, 0);    // add label to the grid in the first row (0) , starts from 2 column of the table grid
    }

    for (int i = 0; i < rows; i++) {
        Label label = new Label("State " + (i) + ":");
        label.setPadding(new Insets(0, 20, 0, 20));
        tableGrid.add(label, 0, i + 1);
    }

    /* ----------------------  creating the transition table ------------------------*/
    
    
    checkBoxMatrix = new ArrayList<>(); //initializes checkBoxMatrix to an empty list of lists of sets .
    List<Integer> items = IntStream.rangeClosed(0, rows-1).boxed().collect(Collectors.toList()); // the numbers from 0 to row-1 that we put in every checkcobox
    
    for (int i = 0; i < rows; i++) {
        List<CheckComboBox<Integer>> row = new ArrayList<>(); // for the first iteration (as well as the others ) it creates a row to put in the checkcomboboxes of that row  
        for (int j = 0; j < columns; j++) {
            CheckComboBox<Integer> checkComboBox = new CheckComboBox<>();
            checkComboBox.setStyle("-fx-font-size: 10px;");
            
            checkComboBox.getItems().addAll(items);
            tableGrid.add(checkComboBox, j + 1, i + 1); // adding each checkcombobox at once
            row.add(checkComboBox);
        }
        checkBoxMatrix.add(row); 
    }

    /* ----------------------  setting the initial and final states comboboxes  ------------------------*/
    
    Label initialStateLabel = new Label("Initial State:");
    ComboBox<String> initialStateComboBox = new ComboBox<>();   // initial state can be only one value so i'm using only a combobox
    
    initialStateComboBox.setPrefWidth(150);  // methods of that object to set height and width
    initialStateComboBox.setPrefHeight(25);
    
    for (int i = 0; i < rows; i++) {
        initialStateComboBox.getItems().add("" + i );  // i can't use the items list because it is a list of integers not strings and i already created the code .
    }

    Label finalStateLabel = new Label("Final State(s):");
    CheckComboBox<String> finalStateCheckComboBox = new CheckComboBox<>();
    
    finalStateCheckComboBox.setPrefWidth(150);
    finalStateCheckComboBox.setPrefHeight(25);
    
    for (int i = 0; i < rows; i++) {
        finalStateCheckComboBox.getItems().add("" + i );
    }
    
    /* ----------------------  creating the back and submit buttons and their corresponding actions ------------------------*/

    Button backButton = new Button("Back");
    backButton.setOnAction(e -> {
        characterList = null;               //reinitializing the characterList and number of states to null 
        rows = 0;
        showInputScene();   // return back
    });


    Button submitButton = new Button("Submit");
    errorLabel = new Label();
    errorLabel.setStyle("-fx-text-fill: red;");

    submitButton.setOnAction(e -> {    // start of the submit button action
        
    	if (initialStateComboBox.getSelectionModel().isEmpty() || finalStateCheckComboBox.getCheckModel().getCheckedItems().isEmpty()) {
            errorLabel.setText("Error: Initial state and final state(s) selection is required.");
            return;
        }

        errorLabel.setText("");

    /* ----------------------  Assigning to the final and initial states attributes   ------------------------*/
        
        this.initialState = Integer.parseInt(initialStateComboBox.getSelectionModel().getSelectedItem());
        
        this.finalStates = new ArrayList<>(finalStateCheckComboBox.getCheckModel().getCheckedItems())
                .stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());
        
        
        
        
    	 
    	
        if (!isNFA()){
        	//Here's the DFA 
        
             /* ----------------------  creating the transition matrix  ------------------------*/
    	
        	this.transitionMatrix = new int[rows][columns];
        	for (int i = 0; i < rows; i++) {
        	    for (int j = 0; j < columns; j++) {
        	        ObservableList<Integer> checkedItems = checkBoxMatrix.get(i).get(j).getCheckModel().getCheckedItems();
        	        // Assuming you want to take the first checked item from the list
        	        if (!checkedItems.isEmpty()) {
        	            this.transitionMatrix[i][j] = checkedItems.get(0).intValue();
        	        } else {
        	            // Handle the case where there are no checked items
        	            this.transitionMatrix[i][j] = -1; // or any default value
        	        }
        	    }
        	}
            /* ----------------------  showing the entered automaton  ------------------------*/
        	//showResultScene();
        
        
        }
        
        showResultScene();

    

        
            
    }); // end of the submit button action
    
    /* ----------------------  Setting up the second scene  ------------------------*/

    initialfinalestates.getChildren().addAll(initialStateLabel ,initialStateComboBox,finalStateLabel,finalStateCheckComboBox,errorLabel);
    buttonGrid.getChildren().addAll(backButton,submitButton);


    VBox root = new VBox(10);
    root.getChildren().addAll(tableGrid,initialfinalestates, buttonGrid);
    Scene scene = new Scene(root, 1000, 500);

    primaryStage.setResizable(false);
    primaryStage.getIcons().add(new Image("artificial-intelligence.png"));
    primaryStage.setTitle("Automaton");
    primaryStage.setScene(scene);
    primaryStage.centerOnScreen();
    primaryStage.show();
}
    
/* ----------------------  A private method that tests if the automaton in an NFA or DFA  ------------------------*/

    private boolean isNFA() {
       for (int i = 0; i < rows; i++) {
           for (int j = 0; j < columns; j++) {
             CheckComboBox<Integer> comboBox = checkBoxMatrix.get(i).get(j);
             if (comboBox.getCheckModel().getCheckedItems().size() > 1) {
            return true;
             }
           }
       }
       return false;
}


    
    
    /* ----------------------  a Method for Displaying the DFA created  ------------------------*/
    
private void showResultScene() {
  
	 /* ----------------------  setting up the proper label for the scene  ------------------------*/
	
	
	Label isdetermined = new Label();
	
	boolean isNfa = isNFA();
	
   if (isNfa) {
       isdetermined.setText("Here's the NFA : ");
   } else {
   	isdetermined.setText("Here's the DFA : ");
   }
	
    /* ----------------------  Displaying the results  ------------------------*/
	
	
	TextArea transitionMatrixTextArea = new TextArea();
   transitionMatrixTextArea.setEditable(false);
   transitionMatrixTextArea.setWrapText(true);
   transitionMatrixTextArea.setPrefRowCount(this.rows);
   transitionMatrixTextArea.setPrefColumnCount(this.columns);

   StringBuilder transitionMatrixBuilder = new StringBuilder();
   for (int i = 0; i < this.rows; i++) {
       for (int j = 0; j < this.columns; j++) {
           transitionMatrixBuilder.append(checkBoxMatrix.get(i).get(j).getCheckModel().getCheckedItems()).append("\t"); 
       }
       transitionMatrixBuilder.append("\n");
   }
   transitionMatrixTextArea.setText(transitionMatrixBuilder.toString());

   TextArea initialStateTextArea = new TextArea();
   initialStateTextArea.setEditable(false);
   initialStateTextArea.setWrapText(true);
   initialStateTextArea.setPrefRowCount(1);
   StringBuilder initialStateBuilder = new StringBuilder();
   initialStateBuilder.append("State"+initialState).append("\n");
   initialStateTextArea.setText(initialStateBuilder.toString());
   
   TextArea finalStatesTextArea = new TextArea();
   finalStatesTextArea.setEditable(false);
   finalStatesTextArea.setWrapText(true);
   finalStatesTextArea.setPrefRowCount(finalStates.size());
   finalStatesTextArea.setPrefColumnCount(1);

   StringBuilder finalStatesBuilder = new StringBuilder();
   for (int finalState : finalStates) {
       finalStatesBuilder.append("State"+finalState).append("\n");
   }
   finalStatesTextArea.setText(finalStatesBuilder.toString());

   
   
   
   Button nextButton = new Button("Next");
  
// Simplified version to debug the button actions
   if (!isNfa) {
       nextButton.setOnAction(e -> {
           
           testWordScene();
       });
   } else {
       nextButton.setOnAction(e -> {
           
           return;
       });
   }

   	
   
   
   
   
   VBox resultLayout = new VBox(10);
   resultLayout.setPadding(new Insets(20));
   
   if (!isNFA()) {
   resultLayout.getChildren().addAll(isdetermined, new Label("DFA Transition Matrix:"), transitionMatrixTextArea,new Label("DFA initial State:"),initialStateTextArea,
           new Label("DFA Final State(s):"), finalStatesTextArea, nextButton );
   }else {
	   resultLayout.getChildren().addAll(isdetermined, new Label("NFA Transition Matrix:"), transitionMatrixTextArea,new Label("NFA initial State:"),initialStateTextArea,
	           new Label("NFA Final State(s):"), finalStatesTextArea, nextButton );
   }

   Scene resultScene = new Scene(resultLayout, 500, 500);

   primaryStage.setScene(resultScene);
   primaryStage.setTitle("DFA Result");
   primaryStage.centerOnScreen();
   primaryStage.show();
}






public static String checkWordAcceptedByDFA(int[][] transitionMatrix, int initialState, List<Integer> finalStates, List<Character> alphabet, String word) {
    int currentState = initialState;

    int index = 0; // Initialize index to track the current position in the word

    while (index < word.length()) {
        char c = word.charAt(index);

        if (Character.isWhitespace(c)) {
            index++; // Move to the next character
            continue; // Skip processing whitespace characters
        }

        int charIndex = alphabet.indexOf(c);
        if (charIndex == -1) {
            // Character not in the alphabet
            return "The word contains invalid characters.";
        }

        // Check if currentState is within the valid range for transitionMatrix
        if (currentState < 0 || currentState > transitionMatrix.length) {
            return "Invalid initial state.";
        }

        // Check if charIndex is within the valid range for transitionMatrix[currentState]
        if (charIndex < 0 || charIndex >= transitionMatrix[currentState].length) {
            return "Invalid transition for the character '" + c + "' at state " + currentState + ".";
        }

        // Get the transition for the current state and input character
        int transition = transitionMatrix[currentState][charIndex];

        // For simplicity, you could randomly choose a transition
        // or iterate through all transitions (as shown below, choosing the first one)
        currentState = transition; // Assume selecting the first transition

        index++; // Move to the next character
    }

    // Check if the final state after processing the entire word is an accepting state
    if (finalStates.contains(currentState)) {
        return "The word is accepted by the DFA.";
    } else {
        return "The word ends in a non-accepting state (" + currentState + ").";
    }
}




private void testWordScene() {
    Label wordLabel = new Label("Enter a word:");
    TextField wordField = new TextField();
    
    Button submitButton = new Button("Submit");
    Label testWord = new Label();
    
    submitButton.setOnAction(e -> {
        String word = wordField.getText().trim(); // Get the word when the button is pressed
        String result = checkWordAcceptedByDFA(this.transitionMatrix, this.initialState, 
                                                this.finalStates, this.characterList, word);
        testWord.setText(result); // Update the result label with the check result
    });
    
    VBox resultLayout = new VBox(10);
    resultLayout.setPadding(new Insets(20));
    resultLayout.getChildren().addAll(wordLabel, wordField, submitButton, testWord);

    Scene resultScene = new Scene(resultLayout, 500, 500);

    primaryStage.setScene(resultScene);
    primaryStage.setTitle("Word test");
    primaryStage.centerOnScreen();
    primaryStage.show();
}




}




