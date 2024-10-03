import java.sql.*;
import java.util.Scanner;

public class Book {
    // Константы для подключения к базе данных
    private static final String URL = "jdbc:postgresql://localhost:5432/Library";
    private static final String USERNAME = "postgres";
    private static final String PASSWORD = "200510";

    // Объявление статического объекта Scanner для ввода данных пользователем
    private static Scanner scanner = new Scanner(System.in);
    // Объявление статического объекта Connection для подключения к базе данных
    private static Connection conn;

    public static void main(String[] args) {
        // Подключение к базе данных
        try (Connection conn = DriverManager.getConnection(URL, USERNAME, PASSWORD)) {
            System.out.println("Успешно подключен к базе данных.");

            // Сохранение соединения в статической переменной для использования в других методах
            Book.conn = conn;

            // Бесконечный цикл для работы с меню
            while (true) {
                printMenu(); // Вывод меню на экран
                int choice = Integer.parseInt(scanner.nextLine()); // Чтение выбора пользователя
                switch (choice) {
                    case 1:
                        addBook(); // Добавление новой книги
                        break;
                    case 2:
                        listAllBooks(); // Вывод списка всех книг
                        break;
                    case 3:
                        updateBook(); // Обновление информации о книге
                        break;
                    case 4:
                        deleteBook(); // Удаление книги
                        break;
                    case 5:
                        System.out.println("Выход из программы.");
                        return; // Выход из программы
                    default:
                        System.out.println("Неверный выбор!");
                        break;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Метод для вывода меню на экран
    private static void printMenu() {
        System.out.println("Меню:");
        System.out.println("1. Добавить книгу");
        System.out.println("2. Перечислить все книги");
        System.out.println("3. Обновить книгу");
        System.out.println("4. Удалить книгу");
        System.out.println("5. Выход");
        System.out.print("Введите свой выбор: ");
    }

    // Метод для добавления новой книги в базу данных
    private static void addBook() throws SQLException {
        System.out.print("Название: ");
        String title = scanner.nextLine();
        System.out.print("Автор: ");
        String author = scanner.nextLine();
        System.out.print("Год издания: ");
        int year = Integer.parseInt(scanner.nextLine());

        String query = "INSERT INTO books (title, author, year) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.executeUpdate();
            System.out.println("Новая книга успешно добавлена.");
        }
    }

    // Метод для вывода списка всех книг из базы данных
    private static void listAllBooks() throws SQLException {
        String query = "SELECT * FROM books";
        try (Statement statement = conn.createStatement();
             ResultSet resultSet = statement.executeQuery(query)) {

            // Цикл для вывода всех книг
            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                String title = resultSet.getString("title");
                String author = resultSet.getString("author");
                int year = resultSet.getInt("year");
                System.out.printf("%d. %s by %s (%d)\n", id, title, author, year);
            }
        }
    }

    // Метод для обновления информации о книге в базе данных
    private static void updateBook() throws SQLException {
        System.out.print("ID книги для обновления: ");
        int id = Integer.parseInt(scanner.nextLine());
        System.out.print("Название: ");
        String title = scanner.nextLine();
        System.out.print("Автор: ");
        String author = scanner.nextLine();
        System.out.print("Год: ");
        int year = Integer.parseInt(scanner.nextLine());

        String query = "UPDATE books SET title = ?, author = ?, year = ? WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setString(1, title);
            statement.setString(2, author);
            statement.setInt(3, year);
            statement.setInt(4, id);
            statement.executeUpdate();
            System.out.println("Книга успешно обновлена.");
        }
    }

    // Метод для удаления книги из базы данных
    private static void deleteBook() throws SQLException {
        System.out.print("ID книги для удаления: ");
        int id = Integer.parseInt(scanner.nextLine());

        String query = "DELETE FROM books WHERE id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, id);
            statement.executeUpdate();
            System.out.println("Книга успешно удалена.");
        }
    }
}
