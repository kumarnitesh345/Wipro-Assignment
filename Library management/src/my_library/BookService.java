package my_library;
import java.sql.*;
import java.util.Scanner;

public class BookService {

    // 📘 ADD BOOK
    public static void addBook(Scanner sc) {
        try (Connection con = DBConnection.getConnection()) {

            System.out.println("\n==================================================");
            System.out.println("📘           ADD NEW BOOK                         ");
            System.out.println("==================================================");

            System.out.print("Title: ");
            String title = sc.nextLine();

            System.out.print("Author: ");
            String author = sc.nextLine();

            System.out.print("Genre: ");
            String genre = sc.nextLine();

            System.out.print("Quantity: ");
            int qty = sc.nextInt();
            sc.nextLine();

            String sql = "INSERT INTO books (title, author, genre, quantity) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, title);
            ps.setString(2, author);
            ps.setString(3, genre);
            ps.setInt(4, qty);

            ps.executeUpdate();

            System.out.println("--------------------------------------------------");
            System.out.println("✅ Book added successfully.");
            System.out.println("==================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 📚 VIEW BOOKS
    public static void viewBooks() {
        try (Connection con = DBConnection.getConnection()) {

            String sql = "SELECT * FROM books";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            System.out.println("\n==============================================================");
            System.out.println("📚                BOOK COLLECTION                            ");
            System.out.println("==============================================================");

            System.out.printf("%-5s %-20s %-20s %-12s %-5s\n",
                    "ID", "Title", "Author", "Genre", "Qty");

            System.out.println("--------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-20s %-12s %-5d\n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("quantity"));
            }

            if (!found) {
                System.out.println("❌ No books found.");
            }

            System.out.println("==============================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🔍 SEARCH BOOK
    public static void searchBook(Scanner sc) {
        try (Connection con = DBConnection.getConnection()) {

            System.out.println("\n==================================================");
            System.out.println("🔍           SEARCH BOOK                         ");
            System.out.println("==================================================");

            System.out.println("1. Author");
            System.out.println("2. Title");
            System.out.println("3. Genre");
            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            String column = "";
            if (choice == 1) column = "author";
            else if (choice == 2) column = "title";
            else if (choice == 3) column = "genre";
            else {
                System.out.println("❌ Invalid choice");
                return;
            }

            System.out.print("Enter value: ");
            String value = sc.nextLine();

            String sql = "SELECT * FROM books WHERE " + column + " LIKE ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, "%" + value + "%");

            ResultSet rs = ps.executeQuery();

            System.out.println("\n--------------------------------------------------------------");
            System.out.printf("%-5s %-20s %-20s %-12s %-5s\n",
                    "ID", "Title", "Author", "Genre", "Qty");
            System.out.println("--------------------------------------------------------------");

            boolean found = false;

            while (rs.next()) {
                found = true;
                System.out.printf("%-5d %-20s %-20s %-12s %-5d\n",
                        rs.getInt("book_id"),
                        rs.getString("title"),
                        rs.getString("author"),
                        rs.getString("genre"),
                        rs.getInt("quantity"));
            }

            if (!found) {
                System.out.println("❌ No matching books found.");
            }

            System.out.println("==================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 🗑️ DELETE BOOK
    public static void deleteBook(Scanner sc) {
        try (Connection con = DBConnection.getConnection()) {

            System.out.println("\n==================================================");
            System.out.println("🗑️          DELETE BOOK                          ");
            System.out.println("==================================================");

            System.out.print("Enter Book ID to delete: ");
            int bookId = sc.nextInt();

            System.out.print("Are you sure? (Y/N): ");
            char ch = sc.next().charAt(0);

            if (ch != 'Y' && ch != 'y') {
                System.out.println("❌ Deletion cancelled.");
                return;
            }

            String sql = "DELETE FROM books WHERE book_id = ?";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, bookId);

            int rows = ps.executeUpdate();

            if (rows > 0) {
                System.out.println("✅ Book deleted successfully.");
            } else {
                System.out.println("❌ Book not found.");
            }

            System.out.println("==================================================");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}