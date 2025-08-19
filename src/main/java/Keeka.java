import java.util.Objects;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class Keeka {

    public static void main(String[] args) {

        String name = "Keeka";
        Scanner scanner = new Scanner(System.in);
        String userInput = "";
        ArrayList<String> textList = new ArrayList<>();

        // Greeting
        System.out.println("_________________________________________________");
        System.out.println("Hello! I'm " + name);
        System.out.println("What can I do for you?");
        System.out.println("_________________________________________________");

        // Append to list
        userInput = scanner.nextLine();
        while (!Objects.equals(userInput, "bye")) {

            if (Objects.equals(userInput, "list")) {
                listReader(textList);
                userInput = scanner.nextLine();
                continue;
            }

            textList.add(userInput);
            System.out.println("_________________________________________________");
            System.out.println("Text successfully added: " + userInput);
            System.out.println("_________________________________________________");
            userInput = scanner.nextLine();
        }

        // Exit
        System.out.println("_________________________________________________");
        System.out.println("Bye. Hope to see you again soon!");
        System.out.println("_________________________________________________");

    }

    // Text list printer
    private static void listReader(ArrayList<String> textList) {

        if (textList.isEmpty()) {
            System.out.println("_________________________________________________");
            System.out.println("List is empty! Add texts to display");
            System.out.println("_________________________________________________");
            return;
        }

        System.out.println("_________________________________________________");

        for (int i = 0; i < textList.size(); i++) {
            String currentText = textList.get(i);
            System.out.println((i + 1) + ". " + currentText);
        }

        System.out.println("_________________________________________________");

    }
}

