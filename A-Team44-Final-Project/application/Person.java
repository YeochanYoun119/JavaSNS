/**
 * Project Name: cs400-Final A-Team Project
 * Filename: Person.java
 * Name: Eric Sterwald, Jordan Wilkins, Yeochan Youn, Donghyun Kim, & Safwat Rahmen
 * E-mail: Refer to ReadMe.txt for information
 * Lecture: 001 
 * Description: New data type that defines a user in the social network
 */
package application;

/**
 * Class that defines a person for use in the social network. 
 **/
public class Person {
	private String name;
	
	// Default constructor
	public Person(){}
	
	// Constructor to create a person with a name
	public Person(String n) {
		this.name = n;
	}
	
	/**
	 * Method that returns the name of the person.
	 * @return name
	 */
	public String getName() {
		return this.name;
	}
}