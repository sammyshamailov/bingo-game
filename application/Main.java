package application;


import java.util.List;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;

import java.util.concurrent.Executors;

import javax.swing.JOptionPane;

import java.util.concurrent.ExecutorService;

public class Main extends Application {
	
	@FXML // fx:id="next"
    private Button next; // Value injected by FXMLLoader

    @FXML // fx:id="curNum"
    private TextField curNum; // Value injected by FXMLLoader

    @FXML // fx:id="root"
    private AnchorPane root; // Value injected by FXMLLoader

    @FXML // fx:id="finish"
    private Button finish; // Value injected by FXMLLoader

    @FXML // fx:id="child"
    private FlowPane child; // Value injected by FXMLLoader
    //number of players and board size
    private int numOfPlayer,boardSize;
    //the root pane
    private AnchorPane root1;
    private ExecutorService executorService = Executors.newCachedThreadPool();
    //the bingo checker and announcer
	private static Engine eng;
	@Override
	public void start(Stage primaryStage) {
		try {
			Parameters params = getParameters();                    
			List<String> list = params.getRaw();
			numOfPlayer = Integer.parseInt(list.get(0));
			double temp = (double)Integer.parseInt(list.get(1));
			boardSize = (int)Math.sqrt(temp);
			//if the parameters are wrong
			if(!((numOfPlayer>=2&&numOfPlayer<=12)&&(Integer.parseInt(list.get(1))==25||Integer.parseInt(list.get(1))==36||Integer.parseInt(list.get(1))==49)))
			{
				System.out.println("Parameters are wrong");
				System.out.println("First enter number of players between 2-12");
				System.out.println("Second enter board size: 25 or 36 or 49");
				System.exit(0);
			}
			eng = new Engine(numOfPlayer);
			FXMLLoader loader = new FXMLLoader();
			root1 = loader.load(getClass().getResource("Bingo.fxml").openStream());
			Scene scene = new Scene(root1);	
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			      public void handle(WindowEvent we) {
			    	  System.exit(0);
			      }
			  });
			primaryStage.setTitle("Welcome to Bingo game");
			primaryStage.setScene(scene);
			primaryStage.show();
			root=root1;
			initialize();

		}
			catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//method for players creation and board printing in gui
	private void initialize(){
		
		child = (FlowPane) root.getChildren().get(3);
	    GridPane sp1[] = new GridPane[numOfPlayer];
		for(int k=0;k<numOfPlayer;k++)
			sp1[k]=new GridPane();
		Player players[] = new Player[numOfPlayer];
		for(int i=0; i<numOfPlayer; i++)
			players[i] = new Player(boardSize,sp1[i], i, eng);
		for(int j=0;j<numOfPlayer;j++)
			executorService.execute(players[j]);
		for(int i=0;i<numOfPlayer;i++){
			child.getChildren().add(i,players[i].print());
		}
	}
	
	//method for next button
	@FXML
    void nextNumber(ActionEvent event) {
		int drawenNumber = this.eng.DrawNumber();
		//if all numbers are drawn and no bingo was yelled by any player
		if (drawenNumber == -1) {
			JOptionPane.showMessageDialog(null, "All numbers are read, Game will be closed now");
			executorService.shutdown();
			System.exit(0);
		}
		String showNum = ""+drawenNumber;
		curNum.setText(showNum);
		curNum.setEditable(false);
		for(int i=0;i<10000;i++)
			next.setDisable(true);
		next.setDisable(false);
	}
	
	//method for finish button
	@FXML
    public void finishGame (ActionEvent event) {
		JOptionPane.showMessageDialog(null, "Game Over, The game will be closed now");
		executorService.shutdown();
		System.exit(0);
	}
	

	public static void main(String[] args) {
		launch(args);
		
	}
}
