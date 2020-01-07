package application;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SocialNetworkTest {
	static SocialNetwork socialNetwork;

	@BeforeAll
	static void setUpBeforeClass() throws Exception {
	}

	@BeforeEach
	public void setUp() throws Exception {
		socialNetwork = new SocialNetwork();
	}

	@AfterEach
	public void tearDown() throws Exception {
		socialNetwork = null;
	}

	/**
	 * Tests add user method
	 * @author Yeochan Youn
	 * @throws IOException
	 */
	@Test
	void test000_addUser() throws IOException {
		String[] a = new String[] { "1", "2", "3" };
		for (String i : a) { // add user "1", "2", "3"
			socialNetwork.addUser(i);
		}
		if (socialNetwork.getSize() != a.length) { // if size of socialNetwork does not match with number of added
													// users, fail
			fail("social network size should be " + a.length + ", but " + socialNetwork.getSize());
		}
	}

	/**
	 * Test add user with improper names
	 * @author Yeochan Youn
	 * @throws IOException
	 */
	@Test
	void test001_addUserWithImproperName() throws IOException {

		if (socialNetwork.addUser("")) { // test to add user with blank space. Should throw false
			fail("social network should not allow user name blank space, but it did");
		}

		if (socialNetwork.addUser(null)) { // test to add null user. Should throw false
			fail("social network should not allow user name null, but it did");
		}
	}

	/**
	 * tests remove user method
	 * @author Yeochan Youn
	 * @throws IOException
	 */
	@Test
	void test002_removeUser() throws IOException {
		String[] a = new String[] { "1", "2", "3" };

		for (String i : a) {
			socialNetwork.addUser(i);
		}

		socialNetwork.removeUser("1"); // remove one user
		if (socialNetwork.getSize() != a.length - 1) { // check the size of socialNetwork. Fail if size does not match
														// with expected.
			fail("social network size should be " + (a.length - 1) + ", but " + socialNetwork.getSize());
		}
	}

	/**
	 * tests remove user method using improper names
	 * @author Yeochan Youn
	 * @throws IOException
	 */
	@Test
	void test003_removeImproperuser() throws IOException {
		if (socialNetwork.removeUser("1")) { // test removing non existing user. Should throw false
			fail("socialNetwork should not remove not existing user, but it does");
		}

		if (socialNetwork.removeUser("")) { // test removing user with blank space. Should throw false
			fail("socialNetwork should not remove blank name user, but it does");
		}

		if (socialNetwork.removeUser(null)) { // test removing null user. Should throw false
			fail("socialNetwork should not remove null user, but it does");
		}
	}

	/**
	 * basic test of the shortestPath method for length 3 path
	 * 
	 * @author Jordan Wilkins
	 * @throws IOException
	 * @edit Yeochan Youn
	 */
	// @Test
	void test004_shortestPathSmall() throws IOException {
		socialNetwork.addFriends("Jon", "Pat");
		if (socialNetwork.getShortestPath("Jon", "Peter") != null) { // case for finding path between not connected
																		// people
			for (Person i : socialNetwork.getShortestPath("Jon", "Peter")) {
				System.out.print(i.getName() + "-");
			}
			fail("should return null for not connected people, but it returns above");
		}

		socialNetwork.addFriends("Jon", "Jill"); // distance btwn Jon and Jill = 2 nodes
		if (socialNetwork.getShortestPath("Jon", "Jill").size() != 2) {
			for (Person i : socialNetwork.getShortestPath("Jon", "Jill")) {
				System.out.print(i.getName() + "-");
			}
			fail("mission failed");
		}
		socialNetwork.addFriends("Jill", "Slugs"); // distance btwn Jon and Slugs = 3 nodes
		if (socialNetwork.getShortestPath("Jon", "Slugs").size() != 3) {
			for (Person i : socialNetwork.getShortestPath("Jon", "Slugs")) {
				System.out.print(i.getName() + "-");
			}
			fail("mission failed");
		}
		socialNetwork.addFriends("Slugs", "Pugs"); // distance btwn Jon and Pugs = 4 nodes
		if (socialNetwork.getShortestPath("Jon", "Pugs").size() != 4) {
			for (Person i : socialNetwork.getShortestPath("Jon", "Pugs")) {
				System.out.print(i.getName() + "-");
			}
			fail("mission failed");
		}
	}
	
	/**
	 * Test getConnectedComponents to check proper friendship
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Test
	void test006_getconnectedComponents() throws IOException {
		//Add three isolated friendships
		socialNetwork.addFriends("Carhartt", "Wip");
		socialNetwork.addFriends("Calvin", "Klein");
		socialNetwork.addFriends("Shin", "Ramen");
		//Get the result of getConnectedComponents
		Set<Graph> connectedComponents = socialNetwork.getConnectedComponents();
		Graph[] graphs = new Graph[3];
		connectedComponents.toArray(graphs);
		Person[] friends = new Person[2];
		graphs[0].getAllNodes().toArray(friends);
		//Check whether the connected components have a proper friend
		if (friends[0].getName() == "Ramen") {
			switch (friends[1].getName()) {
			//When the friend of "Ramen" is "Shin", it is completed
			case "Shin":
				break;
			//The other cases mean it is failed
			case "Calvin":
				fail("getconnectedComponents does not work");
			case "Carhartt":
				fail("getconnectedComponents does not work");
			default:
				fail("getconnectedComponents does not work");
			}
		}else {
			fail("getconnectedComponents does not work");
		}
	}

	/**
	 * Test getShortestPath in case of the straight friend line
	 * 
	 * @author Donghyun Kim
	 * @throws IOException
	 */
	@Test
	void test007_getshortest_in_straigt_line() throws IOException {
		//Add friends in a straight line
		socialNetwork.addFriends("A", "B");
		socialNetwork.addFriends("B", "C");
		socialNetwork.addFriends("C", "D");
		List<Person> path = new ArrayList<Person>();
		path = socialNetwork.getShortestPath("A", "D");		
		//check the first contents is "A"
		if ((path.get(0).getName() != "A")) {
			fail("The order is not correct");
		}
		//check the last contents is "D"
		if ((path.get(3).getName() != "D")) {
			fail("The order is not correct");
		}
	}
}