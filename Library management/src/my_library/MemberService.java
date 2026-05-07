package my_library;
import java.sql.*;
import java.util.Scanner;
public class MemberService {

	public static void addMember(Scanner sc) {
        try (Connection con = DBConnection.getConnection()) {
            System.out.print("Name: ");
            String name = sc.nextLine();
            System.out.print("Phone: ");
            String phone = sc.nextLine();
            System.out.print("Email: ");
            String email = sc.nextLine();

            String sql = "INSERT INTO members (name, phone, email) VALUES (?, ?, ?)";
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setString(1, name);
            ps.setString(2, phone);
            ps.setString(3, email);

            ps.executeUpdate();
            System.out.println("✅ Member added.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
	public static void viewMembers() {
	    try (Connection con = DBConnection.getConnection()) {

	        ResultSet rs = con.createStatement().executeQuery("SELECT * FROM members");

	        System.out.println("\n==============================================================");
	        System.out.println("👥                 MEMBER LIST                               ");
	        System.out.println("==============================================================");

	        System.out.printf("%-10s %-20s %-15s %-25s\n",
	                "ID", "Name", "Phone", "Email");

	        System.out.println("--------------------------------------------------------------");

	        while (rs.next()) {
	            System.out.printf("%-10d %-20s %-15s %-25s\n",
	                    rs.getInt("member_id"),
	                    rs.getString("name"),
	                    rs.getString("phone"),
	                    rs.getString("email"));
	        }

	        System.out.println("==============================================================");

	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	 public static void deleteMember(Scanner sc) {
		    try (Connection con = DBConnection.getConnection()) {

		        System.out.println("\n----------------------------------------");
		        System.out.println("🗑️        DELETE MEMBER PANEL        ");
		        System.out.println("----------------------------------------");

		        System.out.print("Enter Member ID to delete: ");
		        int memberId = sc.nextInt();

		        // check if exists
		        String check = "SELECT * FROM members WHERE member_id = ?";
		        PreparedStatement psCheck = con.prepareStatement(check);
		        psCheck.setInt(1, memberId);
		        ResultSet rs = psCheck.executeQuery();

		        if (!rs.next()) {
		            System.out.println("❌ Member not found.");
		            return;
		        }

		        // confirmation
		        System.out.print("Are you sure? (Y/N): ");
		        char ch = sc.next().charAt(0);

		        if (ch != 'Y' && ch != 'y') {
		            System.out.println("❌ Deletion cancelled.");
		            return;
		        }

		        // delete
		        String sql = "DELETE FROM members WHERE member_id = ?";
		        PreparedStatement ps = con.prepareStatement(sql);
		        ps.setInt(1, memberId);

		        int rows = ps.executeUpdate();

		        if (rows > 0) {
		            System.out.println("✅ Member deleted successfully.");
		        }

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}
	 


}
