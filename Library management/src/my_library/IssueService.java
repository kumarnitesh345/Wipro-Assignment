package my_library;
import java.sql.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Scanner;
public class IssueService {
	public static void issueBook(Scanner sc) {
	    try (Connection con = DBConnection.getConnection()) {

	        System.out.print("Enter Book ID: ");
	        int bookId = sc.nextInt();

	        // ✅ Check availability
	        String check = "SELECT quantity FROM books WHERE book_id = ?";
	        PreparedStatement psCheck = con.prepareStatement(check);
	        psCheck.setInt(1, bookId);
	        ResultSet rsCheck = psCheck.executeQuery();

	        if (rsCheck.next()) {
	            int qty = rsCheck.getInt("quantity");

	            if (qty <= 0) {
	                System.out.println("❌ Book not available.");
	                return;
	            }
	        } else {
	            System.out.println("❌ Invalid Book ID.");
	            return;
	        }

	        System.out.print("Enter Member ID: ");
	        int memberId = sc.nextInt();
	        sc.nextLine();

	        System.out.print("Issue Date (YYYY-MM-DD): ");
	        String issueDate = sc.nextLine();

	        System.out.print("Return Date (YYYY-MM-DD): ");
	        String returnDate = sc.nextLine();

	        // ✅ Insert issue
	        String sql = "INSERT INTO issues (book_id, member_id, issue_date, return_date) VALUES (?, ?, ?, ?)";
	        PreparedStatement ps = con.prepareStatement(sql);
	        ps.setInt(1, bookId);
	        ps.setInt(2, memberId);
	        ps.setDate(3, Date.valueOf(issueDate));
	        ps.setDate(4, Date.valueOf(returnDate));
	        ps.executeUpdate();

	        // ✅ Decrease quantity
	        String update = "UPDATE books SET quantity = quantity - 1 WHERE book_id = ?";
	        PreparedStatement ps2 = con.prepareStatement(update);
	        ps2.setInt(1, bookId);
	        ps2.executeUpdate();

	        System.out.println("✅ Book issued successfully.");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void viewIssuedBooks() {
	    try (Connection con = DBConnection.getConnection()) {

	        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM issues");

	        System.out.println("\n==============================================================");
	        System.out.println("📄             ISSUED BOOKS RECORD                           ");
	        System.out.println("==============================================================");

	        System.out.printf("%-10s %-10s %-10s %-15s %-15s\n",
	                "IssueID", "BookID", "MemberID", "Issue Date", "Return Date");

	        System.out.println("--------------------------------------------------------------");

	        boolean found = false;

	        while (rs.next()) {
	            found = true;
	            System.out.printf("%-10d %-10d %-10d %-15s %-15s\n",
	                    rs.getInt("issue_id"),
	                    rs.getInt("book_id"),
	                    rs.getInt("member_id"),
	                    rs.getDate("issue_date"),
	                    rs.getDate("return_date"));
	        }

	        if (!found) {
	            System.out.println("❌ No issued books found.");
	        }

	        System.out.println("==============================================================");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	public static void returnBook(Scanner sc) {
	    try (Connection con = DBConnection.getConnection()) {

	        System.out.println("\n==================================================");
	        System.out.println("🔁        PROCESSING BOOK RETURN                  ");
	        System.out.println("==================================================");

	        System.out.print("Enter Issue ID: ");
	        int issueId = sc.nextInt();

	        // 🔍 Get book_id and return_date
	        String get = "SELECT book_id, return_date FROM issues WHERE issue_id = ?";
	        PreparedStatement ps1 = con.prepareStatement(get);
	        ps1.setInt(1, issueId);
	        ResultSet rs = ps1.executeQuery();

	        if (!rs.next()) {
	            System.out.println("❌ Invalid Issue ID.");
	            return;
	        }

	        int bookId = rs.getInt("book_id");

	        // ✅ Fine calculation
	        LocalDate today = LocalDate.now();
	        LocalDate returnDate = rs.getDate("return_date").toLocalDate();

	        long daysLate = ChronoUnit.DAYS.between(returnDate, today);

	        if (daysLate > 0) {
	            long fine = daysLate * 10; // ₹10 per day

	            System.out.println("\n⚠️ Book returned late!");
	            System.out.println("📅 Days late: " + daysLate);
	            System.out.println("💰 Fine to pay: ₹" + fine);
	        } else {
	            System.out.println("\n✅ Book returned on time. No fine.");
	        }

	        // 🗑️ Delete issue record
	        String del = "DELETE FROM issues WHERE issue_id = ?";
	        PreparedStatement ps2 = con.prepareStatement(del);
	        ps2.setInt(1, issueId);
	        ps2.executeUpdate();

	        // 📦 Increase quantity
	        String update = "UPDATE books SET quantity = quantity + 1 WHERE book_id = ?";
	        PreparedStatement ps3 = con.prepareStatement(update);
	        ps3.setInt(1, bookId);
	        ps3.executeUpdate();

	        System.out.println("\n✅ Book returned successfully.");
	        System.out.println("==================================================");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
}
