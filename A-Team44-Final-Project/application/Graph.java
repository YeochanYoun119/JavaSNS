/**
 * Project Name: cs400-Final A-Team Project
 * Filename: Graph.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: The graph class is the overall ADT structure of the entire social network program.
 * The graph class has the methods for interacting with an undirected, unweighted graph. 
 */
package application;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;
import java.util.HashSet;

public class Graph implements GraphADT {
	private HashMap<Person, List<Person>> adjacencyList = new HashMap<Person, List<Person>>();
	private int friendshipCount = 0;

	/**
	 * Add edge between to vertices
	 * return false if p1 or p2 is null or name is blank or p1 and p2 are same person, return true otherwise
	 * 
	 * @author Jordan Wilkins
	 * @edit Yeochan Youn, Eric Sterwald
	 */
	@Override
	public boolean addEdge(Person p1, Person p2) {
		// base case: null input returns false
		if (p1 == null || p2 == null || p1.getName() == null || p2.getName() == null ||
			p1.getName().isBlank() || p2.getName().isBlank() || p1.getName().equals(p2.getName())) {
			return false;
		}
		// Check the list of of nodes to get the matching Person (do this because the person
		// in our list has a different ID in java than the Person passed in
		Person matchingPersonForP1 = getNode(p1.getName());
		Person matchingPersonForP2 = getNode(p2.getName());
		// look for both people, create them if they do not exist
		if (matchingPersonForP1 == null) {
			addNode(p1);
			matchingPersonForP1 = p1;
		}
		if (matchingPersonForP2 == null) {
			addNode(p2);
			matchingPersonForP2 = p2;
		}
		// add the friendship between them
		List<Person> p1Friends = adjacencyList.get(matchingPersonForP1);
		List<Person> p2Friends = adjacencyList.get(matchingPersonForP2);
		if (!p1Friends.contains(matchingPersonForP2) || !p2Friends.contains(matchingPersonForP1)) {
			p1Friends.add(matchingPersonForP2);
			p2Friends.add(matchingPersonForP1);
			friendshipCount++;
			return true;
		}
		return false;
	}

	/**
	 * remove edge between two vertices
	 * return false if p1 or p2 is null or name is blank or p1 and p2 are same 
	 * person or no edge between two vertices, return true otherwise
	 * 
	 * @author Jordan Wilkins
	 * @edit Yeochan Youn, Eric Sterwald
	 */
	@Override
	public boolean removeEdge(Person p1, Person p2) {
		// base case if either person is null, return false
		if (p1 == null || p2 == null || p1.getName() == null || p2.getName() == null ||
			p1.getName().isBlank() || p2.getName().isBlank() || p1.getName().equals(p2.getName())) {
			return false;
		}
		// Check the list of of nodes to get the matching Person (do this because the person
		// in our list has a different ID in java than the Person passed in
		Person matchingPersonForP1 = getNode(p1.getName());
		Person matchingPersonForP2 = getNode(p2.getName());
		// check that they are friends first
		if ((adjacencyList.get(matchingPersonForP1) == null || 
				!adjacencyList.get(matchingPersonForP1).contains(matchingPersonForP2)) && 
			(adjacencyList.get(matchingPersonForP2) == null || 
				!adjacencyList.get(matchingPersonForP2).contains(matchingPersonForP1))) {
			return false; // this means there was no edge to remove 
		}
		// otherwise remove p1 from p2 and vice versa
		adjacencyList.get(matchingPersonForP1).remove(matchingPersonForP2);
		adjacencyList.get(matchingPersonForP2).remove(matchingPersonForP1);
		friendshipCount--;
		return true; // return true if all done
	}

	/**
	 * add new vertex to the graph
	 * return false is person does not have proper name or already exist in the graph, return true otherwise
	 * 
	 * @author Jordan Wilkins
	 * @edit Eric Sterwald
	 */
	@Override
	public boolean addNode(Person person) {
		// base case if invalid person is passed in
		if (person == null || person.getName() == null || person.getName().isBlank()) {
			return false;
		}
		
		// base case, ignore add if the person already exists
		// allow duplicate for people have same name, but different person
		
		// Check the list of of nodes to get the matching Person (do this because the person
		// in our list has a different ID in java than the Person passed in
		Person matchingPerson = getNode(person.getName());
		if (matchingPerson != null) {
		  return false; 
		}
		 
		// otherwise add the person to the adjacencyList and return true
		adjacencyList.put(person, new ArrayList<Person>());
		return true;
	}

	/**
	 * remove vertex from the graph
	 * return false if person does not have proper name or does not exist in the graph, return true otherwise
	 *  
	 * @author Jordan Wilkins
	 * @edit Yeochan Youn, Eric Sterwald
	 */
	@Override
	public boolean removeNode(Person person) {
		// base case: check if input is null
		if (person == null || person.getName() == null || person.getName().isBlank()) {
			return false;
		}
		// check if person is in the adjacencyList
		
		// Check the list of of nodes to get the matching Person (do this because the person
		// in our list has a different ID in java than the Person passed in
		Person matchingPerson = getNode(person.getName());
		if (matchingPerson == null) {
			return false; 
		}
		
		// remove person from the key list
		adjacencyList.remove(matchingPerson);
		// loop through all other people and remove person from their friendslist
		for(Person i : this.getAllNodes()) { 
			if (adjacencyList.get(i).remove(matchingPerson)) {
				friendshipCount--;
			}
		}
		return true; // return true if all done
	}

	/**
	 * return set of adjecent vertices
	 * return null if person is null or does not exist, otherwise return set of adjacent vertices   
	 * @author Jordan Wilkins
	 * @edit Yeochan Youn, Eric Sterwald
	 */
	@Override
	public Set<Person> getNeighbors(Person person) {
		// base case: null input
		if (person == null) {
			return null;
		}
		// second case: check if the person exists
		Person matchingPerson = getNode(person.getName());
		if (matchingPerson == null) {
			return null;
		}
		// otherwise return a set of their friends
		List<Person> friends = adjacencyList.get(matchingPerson);
		Set<Person> set = new HashSet<Person>(friends); // convert list to set	
		return set;
	}

	/**
	 * return target person who has input name
	 * 
	 * @author Jordan Wilkins
	 * @edit Yeochan Youn
	 */
	@Override
	public Person getNode(String name) {
		// base case: null input
		if (name == null || name.isBlank()) {
			return null;
		}
		
		// look through the graph at all people and compare their name to the given
		for(Person i : getAllNodes()) {
			if(i.getName().equals(name)) {
				return i;
			}
		}
		return null;
	}

	/**
	 * return all nodes in the graph
	 * 
	 * @author Jordan Wilkins
	 */
	@Override
	public Set<Person> getAllNodes() {
		return adjacencyList.keySet();
	}
	
	/**
	 * Used to determine the order of number of nodes
	 * in the graph.
	 * 
	 * @Author Eric Sterwald
	 * @return int
	 */
	@Override
	public int getAdjListSize(){
		return adjacencyList.size();
	}

	/**
	 * Returns the number of unique edges in the graph
	 * at any given time. Because this graph is undirected
	 * an edge going from A to B and from B to A is counted
	 * as 1 friendship
	 * 
	 * @author Eric Sterwald
	 * @return int
	 */
	@Override
	public int getFriendshipSize() {
		return friendshipCount;
	}
}