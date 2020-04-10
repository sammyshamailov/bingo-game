package application;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;

public class Player implements Runnable {

	//generator for random nubmers in the board
	private SecureRandom generator = new SecureRandom();
	//an array for board representation
	private int[][] board;
	//current number that was randomly generated
	private int curNum;
	//size of the board
	private int size;
	//grid pane for the board in gui representation
	private GridPane sp1;
	//an boolean flag for checking if the number was already generated
	private boolean valid=false;
	//an arraylist for numbers that were already generated
	private ArrayList<Integer> alreadyUsed = new ArrayList<Integer>();
	//the number of the player
	private int playerNumber;
	//the engine that checks for winners
	private Engine eng;
	//a lock for the board
    private ReentrantLock lock = new ReentrantLock(); 

    //constructor
	public Player(int num,GridPane tmp, int pnumber, Engine en){
		board = new int[num][num];
		playerNumber = pnumber;
		size=num;
		eng = en;
		this.sp1=tmp;
		//a nested loop for board creation 
		for(int i=0;i<num;i++){
			for(int j=0;j<num;j++){
				//while the number that is generated wasn't drawn already
				while(!valid){
					curNum=1+ generator.nextInt(100);
					if(!alreadyUsed.contains(curNum)){
						valid=true;
						alreadyUsed.add(curNum);
					}
				}
				board[i][j]=curNum;
				valid=false;
			}
		}
		
	}
	
	//method for returning the board as a grid pane for gui printing
	public GridPane print(){
		
		for(int rowIndex = 0; rowIndex < size; rowIndex++){
            for(int columnIndex = 0; columnIndex < size; columnIndex++){

                // Iterate the Index using the loops
            	//A temporary button that represents a number and goes to the grid pane that represents the board
                Button button = new Button();
                button.setPrefHeight(36);
                button.setPrefWidth(36);
                button.setAlignment(Pos.CENTER);
                button.setText(""+board[rowIndex][columnIndex]);
                button.setOnMouseClicked(new EventHandler<Event>(){

                	//Method for getting and mouse event on a board
					@Override
					public void handle(Event arg0) {
						// TODO Auto-generated method stub
						if (!button.getTextFill().equals(Color.DARKORCHID))
						{
							markAndCheck(Integer.valueOf(button.getText()));
						}
						else
						{
							unmark(Integer.valueOf(button.getText()));
						}
						
					}
                	
                });
                sp1.add(button, columnIndex, rowIndex);
                
            }
        }
		return sp1;

        
    }
	
	//check if the bingo is correct through the engine class (announces if the player wins or was wrong)
	public void check(int rowIndex, int columnIndex)
	{
		ArrayList<Integer> results = new ArrayList<Integer>(size);
		int i=0;
	   	for(i = 0; i < size; i++){
	   		if (board[rowIndex][i] <= 100 )
	   			break;
	   		results.add(board[rowIndex][i] - 100);
	   	}
	   	//checking for row bingo
	   	if ( results.size() == size)
	   	{
	   		ArrayList<Integer> incorrects = eng.checkBingo(results, playerNumber+1);
	   		for(int j=0;j<size;j++)
	   		{
	   			lock.lock();
	   			Button btn = (Button) sp1.getChildren().get(rowIndex*size + j);
	   			if (incorrects.contains(Integer.valueOf(btn.getText())))
				{
	   				btn.setOpacity(1.0);
					btn.setTextFill(Color.RED);
					board[rowIndex][j] -= 100;
				}
        		lock.unlock();
	   		}
	   		return;
	   	}
	   	results.clear();
	   	i=0;
	   	for(i = 0; i < size; i++){
	   		if (board[i][columnIndex] <= 100 )
	   			break;
	   		results.add(board[i][columnIndex] - 100);	   		
	   	}
	   	//checking for column bingo
	   	if ( results.size() == size){	   		
	   		ArrayList<Integer> incorrects = eng.checkBingo(results, playerNumber+1);
	   		for(int j=0;j<size;j++)
	   		{
	   			lock.lock();
	   			Button btn = (Button) sp1.getChildren().get(j*size + columnIndex);
	   			if (incorrects.contains(Integer.valueOf(btn.getText())))
				{
	   				btn.setOpacity(1.0);
					btn.setTextFill(Color.RED);
					board[j][columnIndex] -= 100;
				}
        		lock.unlock();
	   		}
	   		return;
	   	}
	   	results.clear();
	   	i=0;
	   	for(i = 0; i < size; i++){
	   		if (board[i][size-(i+1)] <= 100 )
	   			break;
	   		results.add(board[i][size-(i+1)] - 100);
	   	}
	   	//checking for secondary diagonal bingo
	   	if ( results.size() == size){
	   		ArrayList<Integer> incorrects = eng.checkBingo(results, playerNumber+1);
	   		int k=size-(0+1);
	   		for(int j=0;j<size;j++)
	   		{
	   			lock.lock();
	   			Button btn = (Button) sp1.getChildren().get(j*size+k);
	   			k--;
	   			if (incorrects.contains(Integer.valueOf(btn.getText())))
				{
	   				btn.setOpacity(1.0);
					btn.setTextFill(Color.RED);
					board[j][size-(j+1)] -= 100;
				}
        		lock.unlock();
	   		}
	   		return;
	   	}
	   	if (columnIndex!=rowIndex)
	   		return;
	   	results.clear();
	   	i=0;
	   	for(i = 0; i < size; i++){
	   		if (board[i][i] <= 100 )
	   			break;
	   		results.add(board[i][i] - 100);
	   	}
	   	//checking for main diagonal bingo
	   	if ( results.size() == size){
	   		ArrayList<Integer> incorrects = eng.checkBingo(results, playerNumber+1);
	   		int k=0;
	   		for(int j=0;j<size;j++)
	   		{
	   			lock.lock();
	   			Button btn = (Button) sp1.getChildren().get(j*size+k);
	   			k++;
	   			if (incorrects.contains(Integer.valueOf(btn.getText())))
				{
	   				btn.setOpacity(1.0);
					btn.setTextFill(Color.RED);
					board[j][j] -= 100;
				}
        		lock.unlock();
	   		}
	   		return;
	   	}

	   	return;
	   	
	}
	
	//Method for marking a number in the board
	public void markAndCheck(int markedNumber)
	{
	   	for(int rowIndex = 0; rowIndex < size; rowIndex++){
            for(int columnIndex = 0; columnIndex < size; columnIndex++){
            	//if the random nubmer is in the board
            	if (board[rowIndex][columnIndex] == markedNumber)
            	{
            		lock.lock();
            		Button btn = (Button) sp1.getChildren().get(rowIndex*size + columnIndex);
            		btn.setOpacity(0.8);
    	   			btn.setTextFill(Color.DARKORCHID);
            		lock.unlock();
            		board[rowIndex][columnIndex]+=100;
            		//check if there is a bingo
            		check(rowIndex,columnIndex);
            	}
            }	
	   	}
	}

	//Method in case that the player wants to unmark a number that was marked mistakenly
	public void unmark(int numNum)
	{
	   	for(int y = 0; y < size; y++){
            for(int x = 0; x < size; x++){
            	if (board[y][x] == numNum+100)
            	{
            		lock.lock();
            		Button btn = (Button) sp1.getChildren().get(y*size + x);
            		btn.setOpacity(1.0);
    	   			btn.setTextFill(Color.BLACK);
            		lock.unlock();
            		board[y][x]-=100;
            	}
            }	
	   	}
	}
	public void run(){
		try {
			while (true) {

				Thread.sleep(50);
			}
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
