import java.util.*;

// ========================= USER CLASS =========================
class User {
    String userId;
    String name;
    String password;
    int totalPoints;
    double engagementPercent;
    int rank;
    String preferenceTag;
    Date lastActivityDate;
    boolean loggedInThisSession = false;
    int dailyLoginBonus = 5;

    public User(String userId, String name, String password) {
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.totalPoints = 5;
        this.engagementPercent = 0.0;
        this.rank = 0;
        this.preferenceTag = "⚠️ Low Engaged";
        this.lastActivityDate = new Date();
    }

    public void addPoints(int points) {
        this.totalPoints += points;
        this.lastActivityDate = new Date();
    }

    public void displayEngagement() {
        System.out.println("User: " + name);
        System.out.println("Total Points: " + totalPoints);
        System.out.printf("Engagement Percent: %.2f%%\n", engagementPercent);
        System.out.println("Rank: " + rank);
        System.out.println("Preference Tag: " + preferenceTag);
        System.out.println("--------------------------------------");
    }
}

// ========================= LEAD CLASS =========================
class Lead {
    String leadOwner;
    String question;
    String category;
    String source;
    String assignedTo = "Not Assigned";

    public Lead(String leadOwner, String question, String category, String source) {
        this.leadOwner = leadOwner;
        this.question = question;
        this.category = category;
        this.source = source;
    }

    public void displayLead() {
        System.out.println("Lead Owner: " + leadOwner);
        System.out.println("Question: " + question);
        System.out.println("Category: " + category);
        System.out.println("Source: " + source);
        System.out.println("Assigned To: " + assignedTo);
        System.out.println("-----------------------------");
    }
}

// ================== MAIN MANAGEMENT CLASS ==================
public class LeadManagementSystem {

    static List<User> users = new ArrayList<>();
    static List<Lead> leadDatabase = new ArrayList<>();
    static int MAX_POSSIBLE_POINTS = 200;

    public static void main(String[] args) {

        Scanner sc = new Scanner(System.in);
        boolean running = true;

        System.out.println("======== WELCOME TO LEAD MANAGEMENT SYSTEM ========");

        while (running) {
            System.out.println("\n==== HOME MENU ====");
            System.out.println("1. Signup");
            System.out.println("2. Login & Start Operations");
            System.out.println("3. Exit");

            System.out.print("Choice daalo: ");
            int homeChoice = sc.nextInt();
            sc.nextLine();

            switch (homeChoice) {
                case 1:
                    signup(sc);
                    break;
                case 2:
                    User loggedInUser = login(sc);
                    if (loggedInUser != null) {
                        mainMenu(sc, loggedInUser);
                    }
                    break;
                case 3:
                    running = false;
                    System.out.println("System se exit kar rahe ho. Bye!");
                    break;
                default:
                    System.out.println("Galat option hai! Dobara try karo.");
            }
        }

        sc.close();
    }

    // ================== SIGNUP FUNCTION ==================
    public static void signup(Scanner sc) {
        System.out.print("Apna naam daalo: ");
        String name = sc.nextLine();
        System.out.print("Password set karo: ");
        String password = sc.nextLine();

        String userId = "U" + (users.size() + 1);
        User newUser = new User(userId, name, password);
        users.add(newUser);

        System.out.println("✅ Signup successful! User ID: " + userId + ", 5 points diye gaye.");
    }

    // ================== LOGIN FUNCTION ==================
    public static User login(Scanner sc) {
        System.out.println("\n==== USER LOGIN ====");
        displayAllUsersShort();

        System.out.print("User ID daalo (U1, U2...): ");
        String userId = sc.nextLine();

        User user = getUserById(userId);

        if (user != null) {
            System.out.print("Password daalo: ");
            String password = sc.nextLine();

            if (password.equals(user.password)) {
                if (!user.loggedInThisSession) {
                    user.addPoints(10 + user.dailyLoginBonus);
                    user.loggedInThisSession = true;
                    logActivity(user.name + " logged in and earned 10 + daily bonus points");
                }
                System.out.println("✅ Login successful for " + user.name);
                return user;
            } else {
                System.out.println("❌ Password galat!");
            }
        } else {
            System.out.println("❌ User ID galat!");
        }

        return null;
    }

    // ================== MAIN MENU ==================
    public static void mainMenu(Scanner sc, User loggedInUser) {
        boolean running = true;

        while (running) {
            calculateEngagement();
            System.out.println("\n==== Main Menu (" + loggedInUser.name + ") ====");
            System.out.println("1. Perform Activity");
            System.out.println("2. Engagement Summary");
            System.out.println("3. Lead Management");
            System.out.println("4. Update Profile");
            System.out.println("5. Leaderboard");
            System.out.println("6. Logout & Exit");

            System.out.print("Apna choice batao: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    performUserActivity(sc, loggedInUser);
                    break;
                case 2:
                    displayAllUsers();
                    break;
                case 3:
                    leadManagementMenu(sc, loggedInUser);
                    break;
                case 4:
                    updateProfile(sc, loggedInUser);
                    break;
                case 5:
                    displayLeaderBoard();
                    break;
                case 6:
                    running = false;
                    loggedInUser.loggedInThisSession = false;
                    System.out.println("Logout ho gaye. Dhanyavaad!");
                    break;
                default:
                    System.out.println("Invalid choice! Dobara try karo.");
            }
        }
    }

    // ================== USER ACTIVITIES ==================
    public static void performUserActivity(Scanner sc, User user) {
        System.out.println("\nAvailable Actions:");
        System.out.println("1. View Pre-made Questions (+20 points)");
        System.out.println("2. Schedule Interview (+10 points)");
        System.out.println("3. View Leads (+20 points)");
        System.out.println("4. Send Follow-up (+15 points)");
        System.out.print("Action number daalo (1-4): ");

        int action = sc.nextInt();
        sc.nextLine();

        switch (action) {
            case 1:
                viewPreMadeQuestions();
                user.addPoints(20);
                logActivity(user.name + " viewed pre-made questions and earned 20 points");
                break;
            case 2:
                user.addPoints(10);
                logActivity(user.name + " scheduled an interview and earned 10 points");
                System.out.println("Interview schedule kar diya!");
                break;
            case 3:
                displayLeads();
                user.addPoints(20);
                logActivity(user.name + " viewed leads and earned 20 points");
                break;
            case 4:
                user.addPoints(15);
                logActivity(user.name + " sent a follow-up and earned 15 points");
                System.out.println("Follow-up bhej diya!");
                break;
            default:
                System.out.println("Galat action number daala hai!");
        }
    }

    public static void viewPreMadeQuestions() {
        System.out.println("\n==== Pre-made Questions ====");
        System.out.println("1. Explain OOPs Concepts.");
        System.out.println("2. What is Polymorphism?");
        System.out.println("3. How to optimize SQL queries?");
        System.out.println("4. Difference between Abstract Class and Interface.");
    }

    // ================== LEAD MANAGEMENT MENU ==================
    public static void leadManagementMenu(Scanner sc, User loggedInUser) {
        boolean leadRunning = true;

        while (leadRunning) {
            System.out.println("\n==== Lead Management Menu ====");
            System.out.println("1. Add new lead manually");
            System.out.println("2. View all leads");
            System.out.println("3. Auto-fetch Quora CS leads");
            System.out.println("4. Auto-fetch Stack Overflow CS leads");
            System.out.println("5. Search lead by category/owner");
            System.out.println("6. Assign lead to user");
            System.out.println("7. Delete lead");
            System.out.println("8. Exit Lead Management");

            System.out.print("Apna choice batao: ");
            int leadChoice = sc.nextInt();
            sc.nextLine();

            switch (leadChoice) {
                case 1:
                    addLead(sc, loggedInUser);
                    break;
                case 2:
                    displayLeads();
                    break;
                case 3:
                    autoFetchQuoraLeads();
                    break;
                case 4:
                    autoFetchStackOverflowLeads();
                    break;
                case 5:
                    searchLead(sc);
                    break;
                case 6:
                    assignLead(sc);
                    break;
                case 7:
                    deleteLead(sc);
                    break;
                case 8:
                    leadRunning = false;
                    break;
                default:
                    System.out.println("Invalid option!");
            }
        }
    }

    public static void addLead(Scanner sc, User loggedInUser) {
        System.out.print("Question ya requirement batao: ");
        String question = sc.nextLine();
        System.out.print("Category daalo (IT, Finance, etc.): ");
        String category = sc.nextLine();

        Lead lead = new Lead(loggedInUser.name, question, category, "Manual");
        leadDatabase.add(lead);

        loggedInUser.addPoints(15);
        logActivity(loggedInUser.name + " added a manual lead and earned 15 points");

        System.out.println("✅ Lead successfully add ho gaya by " + loggedInUser.name);
    }

    public static void displayLeads() {
        if (leadDatabase.isEmpty()) {
            System.out.println("❌ Koi lead nahi mili abhi tak.");
            return;
        }

        System.out.println("\n===== All Leads =====");
        for (Lead lead : leadDatabase) {
            lead.displayLead();
        }
    }

    public static void searchLead(Scanner sc) {
        System.out.print("Search by (category/owner): ");
        String search = sc.nextLine().toLowerCase();

        for (Lead lead : leadDatabase) {
            if (lead.category.toLowerCase().contains(search) || lead.leadOwner.toLowerCase().contains(search)) {
                lead.displayLead();
            }
        }
    }

    public static void assignLead(Scanner sc) {
        displayLeads();
        System.out.print("Lead number choose karo (starting from 1): ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index < 1 || index > leadDatabase.size()) {
            System.out.println("❌ Invalid lead number!");
            return;
        }

        System.out.print("Assign kis user ko karna hai (UserID): ");
        String userId = sc.nextLine();

        User user = getUserById(userId);
        if (user != null) {
            leadDatabase.get(index - 1).assignedTo = user.name;
            System.out.println("✅ Lead assigned to " + user.name);
        } else {
            System.out.println("❌ User not found!");
        }
    }

    public static void deleteLead(Scanner sc) {
        displayLeads();
        System.out.print("Delete lead number (starting from 1): ");
        int index = sc.nextInt();
        sc.nextLine();

        if (index < 1 || index > leadDatabase.size()) {
            System.out.println("❌ Invalid lead number!");
            return;
        }

        leadDatabase.remove(index - 1);
        System.out.println("✅ Lead deleted successfully!");
    }

    // ================== UPDATE PROFILE ==================
    public static void updateProfile(Scanner sc, User user) {
        System.out.println("\n==== Update Profile ====");
        System.out.println("1. Update Name");
        System.out.println("2. Update Password");
        System.out.print("Choose option: ");
        int choice = sc.nextInt();
        sc.nextLine();

        switch (choice) {
            case 1:
                System.out.print("New name daalo: ");
                String newName = sc.nextLine();
                user.name = newName;
                System.out.println("✅ Name updated!");
                break;
            case 2:
                System.out.print("New password daalo: ");
                String newPass = sc.nextLine();
                user.password = newPass;
                System.out.println("✅ Password updated!");
                break;
            default:
                System.out.println("❌ Invalid choice");
        }
    }

    // ================== LEADERBOARD ==================
    public static void displayLeaderBoard() {
        calculateEngagement();
        System.out.println("\n==== Leaderboard ====");
        for (User user : users) {
            System.out.println(user.rank + ". " + user.name + " - " + user.totalPoints + " points (" + user.preferenceTag + ")");
        }
    }

    // ================== COMMON FUNCTIONS ==================
    public static void autoFetchQuoraLeads() {
        System.out.println("Fetching filtered CS leads from Quora...");

        leadDatabase.add(new Lead("Quora System", "What is Big-O notation?", "Computer Science", "Quora"));
        leadDatabase.add(new Lead("Quora System", "What are the top AI programming languages?", "Computer Science", "Quora"));

        System.out.println("✅ 2 CS leads Quora se fetch ho gaye!");
    }

    public static void autoFetchStackOverflowLeads() {
        System.out.println("Fetching filtered CS leads from Stack Overflow...");

        leadDatabase.add(new Lead("StackOverflow System", "Best practices for REST API design?", "Computer Science", "Stack Overflow"));
        leadDatabase.add(new Lead("StackOverflow System", "How to secure web applications?", "Computer Science", "Stack Overflow"));

        System.out.println("✅ 2 CS leads Stack Overflow se fetch ho gaye!");
    }

    public static void calculateEngagement() {
        for (User user : users) {
            user.engagementPercent = (user.totalPoints / (double) MAX_POSSIBLE_POINTS) * 100;
        }

        users.sort((u1, u2) -> Double.compare(u2.engagementPercent, u1.engagementPercent));

        int currentRank = 1;
        for (User user : users) {
            user.rank = currentRank++;

            if (user.engagementPercent >= 80) {
                user.preferenceTag = "🔥 Highly Engaged";
            } else if (user.engagementPercent >= 50) {
                user.preferenceTag = "👍 Medium Engaged";
            } else {
                user.preferenceTag = "⚠️ Low Engaged";
            }
        }
    }

    public static void displayAllUsers() {
        System.out.println("\n==== User Engagement Summary ====");
        for (User user : users) {
            user.displayEngagement();
        }
    }

    public static void displayAllUsersShort() {
        if (users.isEmpty()) {
            System.out.println("❌ Koi user nahi mila. Pehle signup karo!");
            return;
        }

        System.out.println("\nRegistered Users:");
        for (User user : users) {
            System.out.println(user.userId + " - " + user.name);
        }
    }

    public static User getUserById(String userId) {
        for (User user : users) {
            if (user.userId.equalsIgnoreCase(userId)) {
                return user;
            }
        }
        return null;
    }

    public static void logActivity(String log) {
        System.out.println("LOG: " + log);
    }
}
