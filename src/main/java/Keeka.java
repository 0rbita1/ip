import java.util.Objects;
import java.util.Scanner;

public class Keeka {

    public static void main(String[] args) {

        String name = "Keeka";
        Scanner scanner = new Scanner(System.in);
        String userInput = "";

        // Greeting
        System.out.println("_________________________________________________");
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println("_________________________________________________");

        // Echo
        userInput = scanner.nextLine();
        while (!Objects.equals(userInput, "bye")) {
            System.out.println("_________________________________________________");
            System.out.println(userInput);
            System.out.println("_________________________________________________");
            userInput = scanner.nextLine();
        }

        // Exit
        System.out.println("_________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_________________________________________________");

    }
}

