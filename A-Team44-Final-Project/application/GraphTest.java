/**
 * Project Name: cs400-Final A-Team Project
 * Filename: GraphTest.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: Graph Test is used to test the graph class.
 */
package application;

import static org.junit.jupiter.api.Assertions.*;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GraphTest {
	static Graph graph;
	
	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}
	
	@BeforeEach
    public void setUp() throws Exception {
		graph = new Graph();
    }
	
	@AfterEach
    public void tearDown() throws Exception {
    	graph = null;
    }
	 
	/**
	 * Ensures that add node is working
	 * @author Yeochan Youn
	 */
	@Test
	void test0_addNode() {
		Person personJill = new Person("Jill");
		graph.addNode(personJill);
		Person jill = graph.getNode("Jill");
		try {
			if (jill.equals(personJill) ==  false) { // test user with test string corretly added and found
				fail("Failed Test One Expected: "+ personJill + ", but returned: " + jill);
			}
		} catch (NullPointerException e) {
			fail("Expected: " + personJill + ", but returned: " + jill);
		}
	}
	
	/**
	 * Tests all edge cases of addNode method
	 * @author Eric Sterwald
	 */
	@Test 
	void test1_addNodeEdgeCases() {
		Person p1 = new Person("");
		Person p2 = new Person("Jack");
		Person p3 = new Person("Jill");
		Person p4 = new Person("ran");
		Person p5 = new Person("up");
		Person p6 = new Person("the");
		Person p7 = new Person("hill");
		Person p8 = new Person();
		// Blank name should not add
		graph.addNode(p1);
		if(graph.getAdjListSize() != 0) {
			fail("should not add p1 to graph. Adj List size is " + graph.getAdjListSize());
		}

		// Null name should not add
		graph.addNode(p8);
		if(graph.getAdjListSize() != 0) {
			fail("should not add p8 to graph. Adj List size is " + graph.getAdjListSize());
		}
		
		// Null person should not add
		graph.addNode(null);
		if(graph.getAdjListSize() != 0) {
			fail("should not add null person to graph. Adj List size is " + graph.getAdjListSize());
		}
		
		// Duplicate should not add.
		graph.addNode(p2);
		graph.addNode(p3);
		graph.addNode(p3);
		if(graph.getAdjListSize() != 2) {
			fail("Should not add duplicate person. Graph adj size is " + graph.getAdjListSize());
		}
	}
	
	/**
	 * Ensures that remove node is working
	 * @author Eric Sterwald
	 */
	@Test
	void test2_removeNode(){
		Person test = new Person("test");
		Person test2 = new Person("test2");
		graph.addNode(test);
		graph.addNode(test2);
		if(graph.getAdjListSize() != 2) {
			fail("Graph order should be 2 but is " + graph.getAdjListSize());
		}
		graph.removeNode(test);
		if(graph.getAdjListSize() != 1) {
			fail("Did not properly remove from list");
		}
		if(graph.getNode("test") != null) {
			fail("graph should not get \"test\" after remove, but it returns " + graph.getNode("test"));
		}
	}
	
	/**
	 * Tests all edge cases when removing a node from the graph
	 * @author Eric Sterwald
	 */
	@Test
	void test3_removeNodeEdgeCases() {
		Person p1 = new Person("");
		Person p2 = new Person("Jack");
		Person p3 = new Person("Jill");
		Person p4 = new Person("ran");
		Person p8 = new Person();
		// Blank name should not be removed
		graph.removeNode(p1);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove person with no name. Adj List size is " + graph.getAdjListSize());
		}

		// Null name should not attempt to remove
		graph.removeNode(p8);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove person with null name. Adj List size is " + graph.getAdjListSize());
		}
		
		// Null person should not attempt remove
		graph.removeNode(null);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove null person. Adj List size is " + graph.getAdjListSize());
		}
		
		// Cannot remove person not in graph
		graph.addNode(p2);
		graph.addNode(p3);
		graph.removeNode(p4);
		if(graph.getAdjListSize() != 2) {
			fail("Cant remove person not in graph. Graph adj size is " + graph.getAdjListSize());
		}
	}
	
	/**
	 * Ensures that addEdge is working
	 * @author Eric Sterwald
	 */
	@Test
	void test4_addEdgeExistingPerson() {
		Person test1 = new Person("test1");
		Person test2 = new Person("test2");
		Person test3 = new Person("test3");
		Person test4 = new Person("test4");
		graph.addNode(test1);
		graph.addNode(test2);
		graph.addNode(test3);
		graph.addNode(test4);
		graph.addEdge(test1, test2);
		graph.addEdge(test1, test3);
		graph.addEdge(test1, test4);
		if(!graph.getNeighbors(test1).contains(test2) || !graph.getNeighbors(test1).contains(test3) || !graph.getNeighbors(test1).contains(test4)) {
			fail("test1 should have test2, test3, and test4 in its list, but it only has " + graph.getNeighbors(test1));
		}	
	}
	
	/**
	 * Ensures that addEdge is working when the people to add were
	 * not previously added to the graph
	 * @author Yeochan Youn
	 */
	@Test
	void test5_addEdgeNonExistantPerson() {
		Person test1 = new Person("test1");
		Person test2 = new Person("test2");
		Person test3 = new Person("test3");
		Person test4 = new Person("test4");
		graph.addEdge(test1, test2);
		graph.addEdge(test1, test3);
		graph.addEdge(test1, test4);
		if(!graph.getNeighbors(test1).contains(test2) || !graph.getNeighbors(test1).contains(test3) || !graph.getNeighbors(test1).contains(test4)) { // check number of adges added
			fail("test1 should have test2, test3, and test4 in its list, but it only has " + graph.getNeighbors(test1));
		}	
	}

	/**
	 * Tests all edge cases of addEdge method
	 * @author Eric Sterwald
	 */
	@Test
	void test6_addEdgeEdgeCases() {
		Person p1 = new Person("");
		Person p2 = new Person("");
		Person p3 = new Person("Jack");
		Person p4 = new Person("Jill");
		Person p5 = new Person("ran");
		Person p6 = new Person();
		Person p7 = new Person();
		// Should not add edge when one or both people are null
		graph.addEdge(null, null);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase both people are null. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(null, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p1 is null. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(p3, null);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p2 is null. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not add edge when one or both people have blank names
		graph.addEdge(p1, p2);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase both people have no name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(p1, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p1 has no name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(p3, p1);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p2 has no name. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not add edge if one or both people have null names
		graph.addEdge(p6, p7);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase both people have null name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(p7, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p1 has null name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.addEdge(p3, p7);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge becuase p2 has null name. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not add edge between self
		graph.addEdge(p3, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant add edge between self. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not add edge if edge between people already exists
		graph.addEdge(p3, p4);
		if(graph.addEdge(p3, p4)) {
			fail("Should not add edge that already exists");
		}
	}
	
	/**
	 * Ensures that removeEdge is working
	 * @author Eric Sterwald
	 */
	@Test
	void test7_removeEdge() {
		Person p3 = new Person("Jack");
		Person p4 = new Person("Jill");
		Person p5 = new Person("ran");
		graph.addEdge(p3, p4);
		graph.addEdge(p3, p5);
		graph.removeEdge(p3, p4);
		if(graph.getNeighbors(p3).contains(p4)) {
			fail("p4 should have been removed from p3s list of friends");
		}	
		if(graph.getNeighbors(p4).contains(p3)) {
			fail("p3 should have been removed from p4s list of friends");
		}	
	}

	/**
	 * Test all edge cases of the remove edge method
	 * @author Eric Sterwald
	 */
	@Test
	void test8_removeEdgeEdgeCases() {
		Person p1 = new Person("");
		Person p2 = new Person("");
		Person p3 = new Person("Jack");
		Person p4 = new Person("Jill");
		Person p5 = new Person("ran");
		Person p6 = new Person();
		Person p7 = new Person();
		// Should not attempt to remove edge when one or both people are null
		graph.removeEdge(null, null);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase both people are null. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(null, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p1 is null. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(p3, null);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p2 is null. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not attempt to remove edge when one or both people have blank names
		graph.removeEdge(p1, p2);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase both people have no name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(p1, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p1 has no name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(p3, p1);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p2 has no name. Graph adj size is " + graph.getAdjListSize());
		}
		// Should not attempt to remove edge if one or both people have null names
		graph.removeEdge(p6, p7);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase both people have null name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(p7, p3);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p1 has null name. Graph adj size is " + graph.getAdjListSize());
		}
		graph.removeEdge(p3, p7);
		if(graph.getAdjListSize() != 0) {
			fail("Cant remove edge becuase p2 has null name. Graph adj size is " + graph.getAdjListSize());
		}
		
		// Should remove edge if edge does not exist
		graph.removeEdge(p3, p4);
		if(graph.removeEdge(p3, p5)) {
			fail("Should not remove edge that doesnt exist");
		}
	}
	
	/**
	 * Ensures that getNode is working
	 * @author Eric Sterwald
	 */
	@Test
	void test9_getNode() {
		Person p2 = new Person("Jack");
		Person p3 = new Person("Jill");
		Person p4 = new Person("ran");
		Person p5 = new Person("up");
		Person p6 = new Person("the");
		Person p7 = new Person("hill");
		graph.addNode(p2);
		graph.addNode(p3);
		graph.addNode(p4);
		graph.addNode(p5);
		graph.addNode(p6);
		graph.addNode(p7);
		graph.addEdge(p3, p5);
		graph.addEdge(p5, p7);
		graph.addEdge(p3, p7);
		// Test if persons name is null
		if (graph.getNode(null) != null) {
			fail("Should return null when name is null");
		}
		// Test if persons name is blank
		if (graph.getNode("") != null) {
			fail("Should return null when name is blank");
		}
		// Test if person does not exist in the graph
		if (graph.getNode("Josh") != null) {
			fail("Should return null when person does not exist in the graph");
		}
		// Ensure correct person is returned.
		if (!graph.getNode("Jill").equals(p3)) {
			fail("Did not return correct person");
		}
	}
	
	/**
	 * Ensures that getAllNode methods is working
	 * @author Eric Sterwald
	 */
	@Test
	void test10_getAllNode() {
		Set<Person> ans = new HashSet<Person>();
		Person p2 = new Person("Jack");
		Person p3 = new Person("Jill");
		Person p4 = new Person("ran");
		Person p5 = new Person("up");
		Person p6 = new Person("the");
		Person p7 = new Person("hill");
		graph.addNode(p2);
		graph.addNode(p3);
		graph.addNode(p4);
		graph.addNode(p5);
		graph.addNode(p6);
		graph.addNode(p7);
		ans.add(p2);
		ans.add(p3);
		ans.add(p4);
		ans.add(p5);
		ans.add(p6);
		ans.add(p7);
		if (!graph.getAllNodes().equals(ans)) {
			fail("Sets of all nodes should be the same. Graphs = " + graph.getAllNodes() + " and Answer is = " + ans);
		}
	}
}