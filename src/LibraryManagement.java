import Entity.Book;
import Entity.User;
import Service.BookService;
import Service.TransactionService;
import Service.UserService;

import java.util.List;
import java.util.Scanner;

public class LibraryManagement {
    private static UserService userService = new UserService();
    private static BookService bookService = new BookService();
    private static TransactionService transactionService = new TransactionService();
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        while (true) {
            System.out.println("Welcome to Library Management System");
            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Exit");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    register();
                    break;
                case 2:
                    login();
                    break;
                case 3:
                    System.exit(0);
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void register() {
        System.out.println("Enter your name:");
        String name = scanner.nextLine();
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPassword(password);

        if (userService.registerUser(user)) {
            System.out.println("Registration successful!");
        } else {
            System.out.println("Registration failed. Email may already be in use.");
        }
    }

    private static void login() {
        System.out.println("Enter your email:");
        String email = scanner.nextLine();
        System.out.println("Enter your password:");
        String password = scanner.nextLine();

        User user = userService.loginUser(email, password);
        if (user != null) {
            System.out.println("Login successful!");
            libraryMenu(user.getId());
        } else {
            System.out.println("Invalid email or password.");
        }
    }

    private static void libraryMenu(int userId) {
        while (true) {
            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Checkout Book");
            System.out.println("4. Return Book");
            System.out.println("5. Logout");
            int choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    addBook();
                    break;
                case 2:
                    viewBooks();
                    break;
                case 3:
                    checkoutBook(userId);
                    break;
                case 4:
                    returnBook(userId);
                    break;
                case 5:
                    System.out.println("Logged out.");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void addBook() {
        System.out.println("Enter book title:");
        String title = scanner.nextLine();
        System.out.println("Enter book author:");
        String author = scanner.nextLine();
        System.out.println("Enter year published:");
        int yearPublished = scanner.nextInt();
        scanner.nextLine(); // consume newline

        Book book = new Book();
        book.setTitle(title);
        book.setAuthor(author);
        book.setYearPublished(yearPublished);
        book.setAvailable(true);

        if (bookService.addBook(book)) {
            System.out.println("Book added successfully!");
        } else {
            System.out.println("Failed to add book.");
        }
    }

    private static void viewBooks() {
        List<Book> books = bookService.getAllBooks();
        if (books.isEmpty()) {
            System.out.println("No books available.");
        } else {
            for (Book book : books) {
                System.out.println("ID: " + book.getId() + ", Title: " + book.getTitle() + ", Author: " + book.getAuthor() + ", Year: " + book.getYearPublished() + ", Available: " + book.isAvailable());
            }
        }
    }

    private static void checkoutBook(int userId) {
        System.out.println("Enter book ID to checkout:");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (transactionService.checkoutBook(userId, bookId)) {
            System.out.println("Book checked out successfully!");
        } else {
            System.out.println("Failed to checkout book. It may already be checked out.");
        }
    }

    private static void returnBook(int userId) {
        System.out.println("Enter book ID to return:");
        int bookId = scanner.nextInt();
        scanner.nextLine(); // consume newline

        if (transactionService.returnBook(userId, bookId)) {
            System.out.println("Book returned successfully!");
        } else {
            System.out.println("Failed to return book.");
        }
    }
}
