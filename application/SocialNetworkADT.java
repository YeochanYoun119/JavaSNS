/**
 * Project Name: cs400-Final A-Team Project
 * Filename: SocialNetworkADT.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: 
 */
package application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public interface SocialNetworkADT {
	/**
	 * Add friendship into the social network
	 * @param name1
	 * @param name2
	 * @return
	 * @throws IOException 
	 */
	public boolean addFriends(String name1, String name2) throws IOException;
	
	/**
	 * Remove friendship from the social network
	 * @param name1
	 * @param name2
	 * @return
	 * @throws IOException 
	 */
	public boolean removeFriends(String name1, String name2) throws IOException;
	
	/**
	 * Add an user to the social network
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public boolean addUser(String name) throws IOException;
	
	/**
	 * Remove an user to the social network
	 * @param name
	 * @return
	 * @throws IOException 
	 */
	public boolean removeUser(String name) throws IOException;
	
	/**
	 * Get the set of friends of a person with given name
	 * @param name
	 * @return
	 */
	public Set<Person> getFriends(String name);
	
	/**
	 * Returns a set of type Person. The set only contains the people that are in
	 * both sets i.e. mutual friends of both parties.
	 * @param name1
	 * @param name2
	 * @return
	 */
	public Set<Person> getMutualFriends(String name1, String name2);
	
	/**
	 * get shortest path between two people return null if two people are connected
	 * @param name1
	 * @param name2
	 * @return
	 */
	public List<Person> getShortestPath(String name1, String name2);
	
	/**
	 * get the connected components in the social network as a set of Graph
	 * Each graph is not connected
	 * @return
	 */
	public Set<Graph> getConnectedComponents();
	
	/**
	 * Loads the file passed and reads each line of command and inputs them into the
	 * graph of the network or if the command is to set the current user it will do
	 * that.
	 * @param file
	 * @return 
	 * @throws FileNotFoundException 
	 * @throws FileFormatException 
	 * @throws IOException 
	 */
	public String loadFromFile(File file) throws FileNotFoundException, FileFormatException, IOException;
	
	/**
	 * Method that writes the current network to a specified file. This file can
	 * then be used as input later on. If the person in the graph has friends, it
	 * adds those instead of just adding the user. If the person does not have
	 * friends then it just adds the user. At the end it sets the central user.
	 * @param file
	 * @throws IOException 
	 */
	public void saveToFile(File file) throws IOException;

	/**
	 * Method that returns false if the person does not exist in the graph and true
	 * if they do exist in the graph.
	 * @param p
	 * @return
	 */
	public boolean exists(String p);

	/**
	 * Resets the graph to a new graph therefore clearing the previous network and
	 * clears the log file.
	 * @throws IOException 
	 * 
	 */
	void reset() throws IOException;

	/**
	 * Returns an integer of the number of unique friendships in the graph.
	 * @return
	 */
	int getFriendshipSize();

	/**
	 *  returns set with all people in graph
	 * @return
	 */
	Set<Person> getAllUsers();

	/**
	 * Can be called from the main class when a central user is set since the change
	 * does not affect socialNetwork.
	 * @param n
	 * @throws IOException
	 */
	void WriteLogFileCUser(String n) throws IOException;

	/**
	 * return size of social network graph
	 * @return
	 */
	int getSize();

	/**
	 * Method that can be called by the Main GUI to get the export file information
	 * like path and name.
	 * @return
	 */
	File GetExportFile();

	/**
	 * Method that can be called by the Main GUI to get the log file information
	 * like path and name.
	 * 
	 * @return
	 */
	File GetLogFile();
}