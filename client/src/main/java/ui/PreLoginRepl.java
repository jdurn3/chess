package ui;

import server.ServerFacade;

import java.util.Scanner;

public class PreLoginRepl {
    private final ServerFacade server;
    private final String serverUrl;
    private State state = State.SIGNEDOUT;

    public PreLoginRepl(ServerFacade server, String serverUrl) {
        this.server = server;
        this.serverUrl = serverUrl;
    }

    public void run() {
        System.out.println("\uD83D\uDC36 Welcome to the Chess Client. Sign in or register to start.");
        displayHelp();

        Scanner scanner = new Scanner(System.in);
        String inputCommand;
        do {
            printPrompt();
            inputCommand = scanner.nextLine().trim().toLowerCase();
            processCommand(inputCommand);
        } while (!inputCommand.equals("quit"));
    }

    private void processCommand(String command) {
        switch (command) {
            case "help":
                displayHelp();
                break;
            case "login":
                login();
                break;
            case "register":
                register();
                break;
            case "quit":
                System.out.println("Exiting Chess Client. Goodbye!");
                break;
            default:
                System.out.println("Invalid command. Please try again.");
        }
    }

    private void displayHelp() {
        System.out.println("Available commands:");
        System.out.println("help - Display available commands.");
        System.out.println("register <USERNAME> <PASSWORD> <EMAIL> - Register a new account.");
        System.out.println("login <USERNAME> <PASSWORD> - Log in to play.");
        System.out.println("quit - Exit the Chess Client.");
    }

    private void login() {

    }

    private void register() {

    }

    private void printPrompt() {
        System.out.print(">>> ");
    }
}
