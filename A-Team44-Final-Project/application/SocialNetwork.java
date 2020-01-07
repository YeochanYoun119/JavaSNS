/**
 * Project Name: cs400-Final A-Team Project
 * Filename: SocialNetwork.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: This class acts as a wrapper class for the main GUI to interact with the 
 * graph class but also serves the purpose of back-end work such as writing the log files
 * and the file when exporting the network.
 */
package application;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.ArrayList;

public class SocialNetwork implements SocialNetworkADT {
	private Graph graph = new Graph();
	private File logFile = new File("log.txt");
	private File exportFile = null;
	private String centralUser = null;
	private static boolean newSession = true;

	/**
	 * Add friendship into the social network
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Override
	public boolean addFriends(String name1, String name2) throws IOException {
		//create temporary person
		Person p1 = new Person(name1);
		Person p2 = new Person(name2);
		//add to the graph
		boolean outcome = this.graph.addEdge(p1, p2);
		//when addEdge success, write log
		if (outcome) {
			WriteLogFile("a", name1, name2);
		}
		return outcome;
	}

	/**
	 * Remove friendship from the social network
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Override
	public boolean removeFriends(String name1, String name2) throws IOException {
		//create temporary person
		Person p1 = new Person(name1);
		Person p2 = new Person(name2);
		//remove from the graph
		boolean outcome = this.graph.removeEdge(p1, p2);
		//when removeEdge success, write log
		if (outcome) {
			WriteLogFile("r", name1, name2);
		}
		return outcome;
	}

	/**
	 * Add an user to the social network
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Override
	public boolean addUser(String name) throws IOException {
		//create temporary person
		Person person = new Person(name);
		//Add to the graph
		boolean outcome = this.graph.addNode(person);
		//when addNode success, write log
		if (outcome) {
			WriteLogFile("a", name, null);
		}
		return outcome;
	}

	/**
	 * Remove an user to the social network
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Override
	public boolean removeUser(String name) throws IOException {
		//create temporary person
		Person person = new Person(name);
		//Remove from the graph
		boolean outcome = this.graph.removeNode(person);
		//when removeNode success, write log
		if (outcome) {
			WriteLogFile("r", name, null);
		}
		return outcome;
	}

	/**
	 * Get the set of friends of a person with given name
	 * @author Donghyun Kim
	 */
	@Override
	public Set<Person> getFriends(String name) {
		//create temporary person
		Person person = new Person(name);
		//Return getNeighbors of the person
		return this.graph.getNeighbors(person);
	}

	/**
	 * Returns a set of type Person. The set only contains the people that are in
	 * both sets i.e. mutual friends of both parties.
	 * 
	 * @author Eric Sterwald
	 */
	@Override
	public Set<Person> getMutualFriends(String name1, String name2) {
		Person p1 = this.graph.getNode(name1);
		Person p2 = this.graph.getNode(name2);
		if (p1 == null || p2 == null) {
			return null;
		}
		Set<Person> mutualFriends = this.graph.getNeighbors(p1);
		Set<Person> p2Friends = this.graph.getNeighbors(p2);
		mutualFriends.retainAll(p2Friends); // Gets a set with people in both sets
		return mutualFriends;
	}

	/**
	 * get shortest path between two people return null if two people are connected
	 * 
	 * @author Yeochan Youn
	 */
	@Override
	public List<Person> getShortestPath(String name1, String name2) {
		if (name1 == null || name2 == null || !this.exists(name1) || !this.exists(name2) || getFriends(name1).isEmpty()
				|| getFriends(name2).isEmpty()) { // filter for unacceptable names
			return null;
		}

		Person p1 = this.graph.getNode(name1); // person starting from
		Person p2 = this.graph.getNode(name2); // person to find path

		List<Person> visited = new ArrayList<Person>(); // list to store visited node to prevent cycle
		List<Person> ret = getShortestPathHelper(p1, p2, visited); // list which is going to be returned

		if (ret.size() < 2) {
			return null;
		} // if returning list contains less than two people, means fail to find proper
			// path. return null
		return ret; // return found shortest path
	}

	/**
	 * 
	 * @author Yeochan Youn
	 * @param name1
	 * @param name2
	 * @param visited
	 * @return
	 */
	private List<Person> getShortestPathHelper(Person name1, Person name2, List<Person> visited) {
		if (this.graph.getNeighbors(name1).size() == 0 && !name1.equals(name2)) {
			return null;
		} // if current node does not have any friends and current node is not a target
			// node, path does not exist. return null

		List<Person> path = new ArrayList<Person>(); // list of people on the shortest path
		if (visited.contains(name1)) {
			return path;
		} // return path without adding current node if current node is already visited
		path.add(name1); // add current node to path
		visited.add(name1); // add current node to visited list

		for (Person i : this.graph.getNeighbors(name1)) { // run for each person in current node's friend list
			List<Person> temp = getShortestPathHelper(i, name2, visited); // temporary list to store path
			if (temp == null) {
				return null;
			} // if temp is null, return null

			if (temp.contains(name2)) { // if temp contains target person
				if (path.contains(name2)) { // if path also contains target person
					if (temp.size() < path.size()) { // if temp is smaller than current path
						path.removeAll(path); // remove all the node in the path
						path.add(name1); // add current node
						path.addAll(temp); // add all the people in the temp
					} // create new path with shorter path
				} else { // if path does not contain target person yet

					for (Person j : path) { // remove duplicate people with path from temp list
						temp.remove(j);
					}

					path.addAll(temp); // add all new people to the temp
				}
			}
		}
		visited.remove(name1); // remove current node from visited list
		return path; // return path
	}

	/**
	 * gets the index of a person in the graph
	 * 
	 * @param Person p
	 * @authors Donghyun Kim
	 */
	private int getLocation(Person p) {
		// stores the index
		int result = 0;
		// changes getAllNodes to an array
		Person[] allnodes = new Person[this.graph.getAllNodes().size()];
		this.graph.getAllNodes().toArray(allnodes);
		// finds the same person in the array
		for (int i = 0; i < allnodes.length; i++) {
			if (allnodes[i].equals(p)) {
				result = i;
			}
		}
		return result;// return the index
	}

	/**
	 * get the connected components in the social network as a set of Graph
	 * Each graph is not connected
	 * @author Donghyun Kim
	 */
	@Override
	public Set<Graph> getConnectedComponents() {
		Set<Graph> connectedComponents = new HashSet<Graph>();
		//make a array which is for checking visited components
		boolean[] visited = new boolean[this.graph.getAllNodes().size()];
		// marks all as unvisited
		for (int i = 0; i < visited.length; i++) {
			visited[i] = false;
		}
		//pass through all of the components in the social network
		for (int j = 0; j < visited.length; j++) {
			if (visited[j] == false) {
				Graph tmp = new Graph(); //Create a new Graph
				getconnectedHelper(j, visited, tmp);// call helper
				connectedComponents.add(tmp);// add the current graph
			}
		}//return the set of Graph
		return connectedComponents;
	}

	/**
	 * Helper method for getConnectedComponents 
	 * 
	 * @author Donghyun Kim
	 * @param int v
	 * @param boolean[] visited
	 * @param Graph tmp
	 */
	private void getconnectedHelper(int v, boolean[] visited, Graph tmp) {
		visited[v] = true;// mark as visited
		Person[] allnodes = new Person[this.graph.getAllNodes().size()];
		this.graph.getAllNodes().toArray(allnodes);//change it to an array
		//Array which stores neighbors 
		Person[] reachable = new Person[this.graph.getNeighbors(allnodes[v]).size()];
		this.graph.getNeighbors(allnodes[v]).toArray(reachable);//change it to an array
		// When a person is not in the current graph
		if (tmp.getAllNodes().contains(allnodes[v]) == false) {
			tmp.addNode(allnodes[v]); // add current person
		} // visit neighbor vertices
		for (int i = 0; i < reachable.length; i++) {
			int location = getLocation(reachable[i]);
			if (visited[location] == false) {
				tmp.addEdge(allnodes[v], reachable[i]);// add friendship
				// call the helper method again
				getconnectedHelper(location, visited, tmp);
			}
		}

	}

	/**
	 * Loads the file passed and reads each line of command and inputs them into the
	 * graph of the network or if the command is to set the current user it will do
	 * that.
	 * 
	 * @author Eric Sterwald
	 * @throws FileFormatException
	 * @throws IOException
	 */
	@Override
	public String loadFromFile(File file) throws FileFormatException, IOException {
		boolean logFileInput = false;
		// If the file is log.txt create a temporary file so that there is not an
		// infinite loop when reading old log.txt and writing new log.txt
		if (file.getName().equals("log.txt")) {
			File tmpFile = new File("tmp.txt");
			Files.copy(file.toPath(), tmpFile.toPath());
			file = tmpFile;
			logFileInput = true;
		}
		Scanner fileScanner = new Scanner(file);
		while (fileScanner.hasNextLine()) {
			String nextLine = fileScanner.nextLine();
			if (!nextLine.isBlank()) {
				String[] input = nextLine.split(" "); // Splits the line by spaces
				String cmd = null;
				Person p1 = new Person();
				Person p2 = new Person();

				//
				if (input.length == 1) {
					throw new FileFormatException(); // Input is in bad format
				} else if (input.length == 2) { // Input has the cmd and person
					cmd = input[0];
					p1 = new Person(input[1]);
				} else if (input.length == 3) { // Input has cmd and 2 people
					cmd = input[0];
					p1 = new Person(input[1]);
					p2 = new Person(input[2]);
				} else
					throw new FileFormatException(); // If input is longer, throw error

				// If command is to add, check if adding person or friendship then call method
				if (cmd.equals("a")) {
					if (p2.getName() == null) {
						this.addUser(p1.getName());
					} else
						this.addFriends(p1.getName(), p2.getName());
				} else if (cmd.equals("r")) {
					// If command is to remove, check if removing person or friendship then call
					// method
					if (p2.getName() == null) {
						this.removeUser(p1.getName());
					} else
						this.removeFriends(p1.getName(), p2.getName());
				} else if (cmd.equals("s")) {
					// If command is to set central user, call method to set central user
					centralUser = p1.getName();
					WriteLogFile("s", p1.getName(), null);
				} else throw new FileFormatException();// If cmd is not 'a', 'r', or 's' throw format exception
			}
		}
		fileScanner.close();
		if (logFileInput) { // If the file read was the log file, delete the temporary file made
			file.delete();
			logFileInput = false;
		}
		return centralUser;
	}

	/**
	 * Method that writes the current network to a specified file. This file can
	 * then be used as input later on. If the person in the graph has friends, it
	 * adds those instead of just adding the user. If the person does not have
	 * friends then it just adds the user. At the end it sets the central user.
	 * 
	 * @author Eric Sterwald
	 * @throws IOException
	 */
	@Override
	public void saveToFile(File file) throws IOException {
		if (!file.exists()) {
			file.createNewFile();
		} else {
			file.delete();
			file.createNewFile();
		}
		PrintWriter exportWriter = new PrintWriter(new FileWriter(file, true));
		for (Person p : this.graph.getAllNodes()) {
			if (this.graph.getNeighbors(p).size() == 0) {
				exportWriter.println("a " + p.getName());
			} else {
				for (Person pf : this.graph.getNeighbors(p)) {
					exportWriter.println("a " + p.getName() + " " + pf.getName());
				}
			}
		}
		if (centralUser != null) {
			exportWriter.println("s " + centralUser);
		}
		exportFile = file;
		exportWriter.close();
	}

	/**
	 * Method that returns false if the person does not exist in the graph and true
	 * if they do exist in the graph.
	 * 
	 * @author Eric Sterwald
	 */
	@Override
	public boolean exists(String p) {
		Person found = this.graph.getNode(p);
		if (found == null) {
			return false;
		}
		return true;
	}

	/**
	 * Resets the graph to a new graph therefore clearing the previous network and
	 * clears the log file.
	 * 
	 * @author Eric Sterwald
	 * @throws IOException
	 */
	@Override
	public void reset() throws IOException {
		this.graph = new Graph(); // resets graph
		if (!logFile.exists()) { // resets log file
			logFile.createNewFile();
		}
		logFile.delete();
		logFile.createNewFile();
		this.centralUser = null;
		if(!logFile.exists()){ // resets log file
            logFile.createNewFile();
    	} 
    	logFile.delete();
   		logFile.createNewFile();
	}

	/**
	 * return size of social network graph
	 * 
	 * @author Yeochan Youn
	 * @return size of graph
	 */
	@Override
	public int getSize() {
		return this.graph.getAdjListSize();
	}

	/**
	 * returns set with all people in graph
	 * 
	 * @return
	 * @author Safwat Rahman
	 */
	@Override
	public Set<Person> getAllUsers() {
		return this.graph.getAllNodes();
	}

	/**
	 * Returns an integer of the number of unique friendships in the graph.
	 * 
	 * @return
	 */
	@Override
	public int getFriendshipSize() {
		return this.graph.getFriendshipSize();
	}

	/**
	 * Can be called from the main class when a central user is set since the change
	 * does not affect socialNetwork.
	 * 
	 * @author Eric Sterwald
	 * @throws IOException
	 */
	@Override
	public void WriteLogFileCUser(String n) throws IOException {
		if (!logFile.exists()) {
			logFile.createNewFile();
		}
		if (newSession) {
			logFile.delete();
			logFile.createNewFile();
			newSession = false;
		}
		this.centralUser = n;
		PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));
		logWriter.println("s " + n);
		logWriter.close();
	}

	/**
	 * Takes in the command and 2 names of people. It uses the command and the
	 * amound of people that are not null to determine what command needs to be
	 * written to the log file.
	 * 
	 * @author Eric Sterwald
	 * @param cmd
	 * @param name1
	 * @param name2
	 * @throws IOException
	 */
	private void WriteLogFile(String cmd, String name1, String name2) throws IOException {
		if (!logFile.exists()) { // File creation if there isnt a file
			logFile.createNewFile();
		}
		if (newSession) { // If this is a new session, delete the old log file and start new
			logFile.delete();
			logFile.createNewFile();
			newSession = false;
		}
		PrintWriter logWriter = new PrintWriter(new FileWriter(logFile, true));

		// Based on cmd and people, write log file message
		if (cmd == "a") {
			if (name2 == null) {
				logWriter.println(cmd + " " + name1);
			} else {
				logWriter.println(cmd + " " + name1 + " " + name2);
			}
		} else if (cmd == "r") {
			if (name2 == null) {
				logWriter.println(cmd + " " + name1);
			} else {
				logWriter.println(cmd + " " + name1 + " " + name2);
			}
		} else if (cmd == "s") {
			logWriter.println(cmd + " " + name1);
		}
		logWriter.close(); // Close the file
	}

	/**
	 * Method that can be called by the Main GUI to get the export file information
	 * like path and name.
	 * 
	 * @author Eric Sterwald
	 * @return
	 */
	@Override
	public File GetExportFile() {
		return exportFile;
	}

	/**
	 * Method that can be called by the Main GUI to get the log file information
	 * like path and name.
	 * 
	 * @author Eric Sterwald
	 * @return
	 */
	@Override
	public File GetLogFile() {
		return logFile;
	}
}