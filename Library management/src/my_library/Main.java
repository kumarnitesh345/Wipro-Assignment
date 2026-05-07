package my_library;
import java.util.Scanner;

public class Main {

    //  Add method here (outside main)
    public static void waitForUser(Scanner sc) {
        System.out.println("\n🔙 Press Enter to return to Main Menu...");
        sc.nextLine();
    }

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        System.out.print("Username: ");
        String user = sc.nextLine();
        System.out.print("Password: ");
        String pass = sc.nextLine();

        if (!LoginService.login(user, pass)) {
            System.out.println("❌ Login Failed");
            return;
        }

        while (true) {
            System.out.println("\n==================================================");
            System.out.println("📚        WELCOME TO NITESH LIBRARY        📚");
            System.out.println("==================================================");

            System.out.println("1. Add Book");
            System.out.println("2. View Books");
            System.out.println("3. Add Member");
            System.out.println("4. View Members");
            System.out.println("5. Issue Book");
            System.out.println("6. View Issued Books");
            System.out.println("7. Return Book");
            System.out.println("8. Search Book");
            System.out.println("9. Delete Book");
            System.out.println("10. Delete Member");  
            System.out.println("11. Exit");

            System.out.print("Choose option: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1 -> {
                    BookService.addBook(sc);
                    waitForUser(sc);   // 
                }
                case 2 -> {
                    BookService.viewBooks();
                    waitForUser(sc);
                }
                case 3 -> {
                    MemberService.addMember(sc);
                    waitForUser(sc);
                }
                case 4 -> {
                    MemberService.viewMembers();
                    waitForUser(sc);
                }
                case 5 -> {
                    IssueService.issueBook(sc);
                    waitForUser(sc);
                }
                case 6 -> {
                    IssueService.viewIssuedBooks();
                    waitForUser(sc);
                }
                case 7 -> {
                    IssueService.returnBook(sc);
                    waitForUser(sc);
                }
                case 8 -> {
                    BookService.searchBook(sc);
                    waitForUser(sc);
                }
                case 9 -> {
                    BookService.deleteBook(sc);
                    waitForUser(sc);
                }
                case 10 -> {
                    MemberService.deleteMember(sc);   // ✅ NEW
                    waitForUser(sc);
                }
                case 11 -> {
                    System.out.println("👋 Goodbye!");
                    return;
                }
                default -> System.out.println("❌ Invalid option");
            }
        }
    }
}