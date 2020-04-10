package application;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;
import javax.swing.JOptionPane;


public class Engine {
	//array of players
    private ArrayList<Boolean> numberOfPlayersCheckedBoard = new ArrayList<Boolean>();   
    //number of players
    private int numberOfPlayers;
    //array for drawn numbers
    private ArrayList<Integer> alreadyUsed = new ArrayList<Integer>();
    private ReentrantLock lock = new ReentrantLock(); 
    //generator for random numbers
    private SecureRandom generator = new SecureRandom();

    //constructor
    public Engine(int numofPlayers){
    	numberOfPlayers = numofPlayers;
		for (int i=0;i<numberOfPlayers;++i)
		{
			numberOfPlayersCheckedBoard.add(true);
		}
    }
    
    //method for drawing new number
    public int DrawNumber()
    {
    	lock.lock();
    	boolean valid = false;
		int curNum=0;
		while(!valid){
			curNum=1+ generator.nextInt(100);
			if(!alreadyUsed.contains(curNum)){
				valid=true;
				alreadyUsed.add(curNum);
			}
			//if all numbers are drawn return -1
			if (alreadyUsed.size()==100)
			{
				lock.unlock();
				return -1;
			}
		}
		for (int i=0;i<numberOfPlayers;++i)
		{
			numberOfPlayersCheckedBoard.set(i, false);
		}
		lock.unlock();
		return curNum;
    }
    
    //popping a message of player won
	public void popUpWinner(int playerNumber)
	{
		JOptionPane.showMessageDialog(null, "Player "+ playerNumber + " Yelled bingo and is the winner!!" + "\n" + "Game will be closed now");
		System.exit(0);
	}

	//popping a message of wrong bingo
	public void popUpLoser(int playerNumber)
	{
		JOptionPane.showMessageDialog(null, "Player "+ playerNumber + " Yelled bingo but was incorrect" + "\n" + "Game will continue");
	}
	
	//method for right message: bingo or no
	public ArrayList<Integer> checkBingo(ArrayList<Integer> results, int playerNumber)
    {
    	
    	results.removeAll(alreadyUsed);
    	if (!results.isEmpty())
    	{
    		popUpLoser(playerNumber);
    	}
    	else
    	{
    		popUpWinner(playerNumber);
    	}
		return results;
    }
	
}
