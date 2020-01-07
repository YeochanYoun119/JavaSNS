/**
 * Project Name: cs400-Final A-Team Project
 * Filename: GraphADT.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: The interface that the Graph class implements.  
 */
package application;

import java.util.Set;

public interface GraphADT {
	/**
	 * Add edge between to vertices
	 * return false if p1 or p2 is null or name is blank or p1 and p2 are same person, return true otherwise
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean addEdge(Person p1, Person p2);
	
	/**
	 * remove edge between two vertices
	 * return false if p1 or p2 is null or name is blank or p1 and p2 are same 
	 * person or no edge between two vertices, return true otherwise
	 * @param p1
	 * @param p2
	 * @return
	 */
	public boolean removeEdge(Person p1, Person p2);
	
	/**
	 * add new vertex to the graph
	 * return false is person does not have proper name or already exist in the graph, return true otherwise
	 * @param person
	 * @return
	 */
	public boolean addNode(Person person);
	
	/**
	 * remove vertex from the graph
	 * return false if person does not have proper name or does not exist in the graph, return true otherwise
	 * @param person
	 * @return
	 */
	public boolean removeNode(Person person);
	
	/**
	 * return set of adjecent vertices
	 * return null if person is null or does not exist, otherwise return set of adjacent vertices   
	 * @param person
	 * @return
	 */
	public Set<Person> getNeighbors(Person person);
	
	/**
	 * return target person who has input name
	 * @param name
	 * @return
	 */
	public Person getNode(String name);
	
	/**
	 * return all nodes in the graph
	 * @return
	 */
	public Set<Person> getAllNodes();

	/**
	 * Used to determine the order of number of nodes
	 * in the graph.
	 * @return
	 */
	int getAdjListSize();

	/**
	 *  Returns the number of unique edges in the graph
	 * at any given time. Because this graph is undirected
	 * an edge going from A to B and from B to A is counted
	 * as 1 friendship
	 * @return
	 */
	int getFriendshipSize();
}