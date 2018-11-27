package myprojects.automation.assignment5.model;

import java.util.Random;

public class UserDate {
    private String Email;
    private String firstName;
    private String lastName;
    private String address;
    private String postcode;
    private String city;
    private static String charsInName = "qwertyuiopasdfghjklzxcvbnm";

    public String getAddress() {
        return address;
    }

    public String getPostcode() {
        return postcode;
    }

    public String getCity() {
        return city;
    }

    public UserDate(String Email, String firstName, String lastName){
        this.Email = Email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = "Garden street 20, build 15, apartment 3";
        this.postcode = "12345";
        this.city = "Krivoy Rog";

    }

    public static UserDate generateRandom(){
        String firstName = "FirstName" + generateString(new Random(System.currentTimeMillis()), charsInName, 5);
        String lastName = "LastName" + generateString(new Random(System.currentTimeMillis() - 1000), charsInName, 5);
        String Email = System.currentTimeMillis() + "@" + "gmail.com";

        return new UserDate(Email, firstName, lastName);
    }

    public String getEmail() {
        return Email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    private static String generateString(Random rng, String characters, int length)
    {
        char[] text = new char[length];
        for (int i = 0; i < length; i++)
        {
            text[i] = characters.charAt(rng.nextInt(characters.length()));
        }
        return new String(text);
    }
}
