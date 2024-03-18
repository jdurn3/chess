//package ui;
//
//import java.util.Scanner;
//
//public class PreLoginRepl {
//    private final ChessClient client;
//
//    public preLoginRepl(String serverUrl) {
//        client = new ChessClient(serverUrl);
//    }
//
//    public void run() {
//        System.out.println("\uD83D\uDC36 Welcome to the Chess Client. Sign in or register to start.");
//        System.out.println(client.help());
//
//        Scanner scanner = new Scanner(System.in);
//        String inputCommand;
//        do {
//            printPrompt();
//            inputCommand = scanner.nextLine().trim().toLowerCase();
//            processCommand(inputCommand);
//        } while (!inputCommand.equals("quit"));
//    }
//
//    private void processCommand(String command) {
//        switch (command) {
//            case "help":
//                displayHelp();
//                break;
//            case "login":
//                login();
//                break;
//            case "register":
//                register();
//                break;
//            case "quit":
//                System.out.println("Exiting Chess Client. Goodbye!");
//                break;
//            default:
//                System.out.println("Invalid command. Please try again.");
//        }
//    }
//
//    private void displayHelp() {
//        System.out.println("Available commands:");
//        System.out.println("help - Display available commands.");
//        System.out.println("login - Log in to the Chess Client.");
//        System.out.println("register - Register a new account.");
//        System.out.println("quit - Exit the Chess Client.");
//    }
//
//    private void login() {
//        System.out.println("Logging in...");
//        // Call server login API
//        // If successful, transition to postlogin UI
//    }
//
//    private void register() {
//        System.out.println("Registering...");
//        // Call server register API
//        // If successful, transition to postlogin UI
//    }
//
//    private void printPrompt() {
//        System.out.print(">>> ");
//    }
//}
