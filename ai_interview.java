import java.util.*;

// ========================= USER CLASS =========================
class User {
    String userId;
    String name;
    int totalPoints;
    double engagementPercent;
    int rank;
    String preferenceTag;
    Date lastActivityDate;

    int pointsToday; // Naya addition for tracking daily points

    public User(String userId, String name) {
        this.userId = userId;
        this.name = name;
        this.totalPoints = 0;
        this.engagementPercent = 0.0;
        this.rank = 0;
        this.preferenceTag = "⚠️ Low Engaged";
        this.lastActivityDate = new Date();
        this.pointsToday = 0;
    }

    public void addPoints(int points) {
        this.totalPoints += points;
        this.pointsToday += points;
        this.lastActivityDate = new Date();
    }

    public void resetPointsToday() {
        this.pointsToday = 0;
    }

    public void displayEngagement() {
        System.out.println("User: " + name);
        System.out.println("Total Points: " + totalPoints);
        System.out.printf("Engagement Percent: %.2f%%\n", engagementPercent);
        System.out.println("Rank: " + rank);
        System.out.println("Preference Tag: " + preferenceTag);
        System.out.println("Points Earned Today: " + pointsToday);
        System.out.println("--------------------------------------");
    }
}

// ========================= LEAD CLASS =========================
class Lead {
    String leadOwner;
    String question;
    String category;
    String source;

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
        System.out.println("-----------------------------");
    }
}

// ================== MAIN MANAGEMENT CLASS ==================
public class LeadManagementSystem {

    static List<User> users = new ArrayList<>();
    static List<Lead> leadDatabase = new ArrayList<>();
    static int MAX_POSSIBLE_POINTS = 100;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // Step 1: Create Users Dynamically
        System.out.print("Kitne users add karne hain? : ");
        int userCount = sc.nextInt();
        sc.nextLine();

        for (int i = 1; i <= userCount; i++) {
            System.out.print("User " + i + " ka naam daalo: ");
            String name = sc.nextLine();
            String userId = "U" + i;
            users.add(new User(userId, name));
        }

        // Step 2: Login Process for All Users + 10 Points
        List<User> loggedInUsers = new ArrayList<>();
        for (int i = 0; i < userCount; i++) {
            User loggedInUser = login(sc);
            if (loggedInUser != null) {
                loggedInUser.addPoints(10); // Auto 10 points on login
                System.out.println(loggedInUser.name + " ko login ke 10 points diye gaye.\n");
                loggedInUsers.add(loggedInUser);
            }
        }

        if (loggedInUsers.isEmpty()) {
            System.out.println("Koi user login nahi hua! Exiting...");
            sc.close();
            return;
        }

        // Step 3: Menu for each logged-in user
        for (User user : loggedInUsers) {
            System.out.println("\n========== Welcome " + user.name + "! ==========");
            mainMenu(sc, user);
        }

        sc.close();
    }

    // ================== LOGIN FUNCTION ==================
    public static User login(Scanner sc) {
        System.out.println("\n==== LOGIN PANEL ====");
        System.out.print("Apna User ID daalo (e.g., U1, U2...): ");
        String userId = sc.nextLine();

        User user = getUserById(userId);
        if (user != null) {
            System.out.println("Welcome " + user.name + "! ✅");
            return user;
        } else {
            System.out.println("❌ User ID galat hai!");
            return null;
        }
    }

    // ================== MAIN MENU ==================
    public static void mainMenu(Scanner sc, User loggedInUser) {
        boolean running = true;

        loggedInUser.resetPointsToday(); // reset at start of session

        while (running) {
            System.out.println("\n==== Main Menu (" + loggedInUser.name + ") ====");
            System.out.println("1. Perform Activity");
            System.out.println("2. Engagement Summary");
            System.out.println("3. Lead Management");
            System.out.println("4. Logout");

            System.out.print("Apna choice batao: ");
            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    performUserActivity(sc, loggedInUser);
                    calculateEngagement();
                    break;
                case 2:
                    calculateEngagement();
                    displayAllUsers();
                    break;
                case 3:
                    leadManagementMenu(sc, loggedInUser);
                    break;
                case 4:
                    running = false;
                    System.out.println("Logout successful for " + loggedInUser.name);
                    break;
                default:
                    System.out.println("Galat choice hai!");
            }
        }
    }

    // ================== USER ACTIVITIES ==================
    public static void performUserActivity(Scanner sc, User user) {
        System.out.println("\nAvailable Actions:");
        System.out.println("1. Add Lead (20 points)");
        System.out.println("2. Schedule Interview (10 points)");
        System.out.println("3. View Leads (20 points)");
        System.out.println("4. Send Follow-Up (15 points)");
        System.out.println("5. View Today's Points");

        System.out.print("Action number daalo (1-5): ");
        int action = sc.nextInt();
        sc.nextLine();

        switch (action) {
            case 1:
                addLead(sc, user);
                user.addPoints(20);
                System.out.println("Lead add ki aur 20 points diye.");
                break;
            case 2:
                user.addPoints(10);
                System.out.println("Interview schedule kiya. 10 points diye.");
                break;
            case 3:
                displayMyLeads(user);
                user.addPoints(20);
                System.out.println("Leads dekhi aur 20 points diye.");
                break;
            case 4:
                user.addPoints(15);
                System.out.println("Follow-up bheja. 15 points diye.");
                break;
            case 5:
                System.out.println("Aaj ke points: " + user.pointsToday);
                break;
            default:
                System.out.println("Galat action number!");
        }
    }

    // ================== LEAD MANAGEMENT ==================
    public static void leadManagementMenu(Scanner sc, User loggedInUser) {
        boolean leadRunning = true;

        while (leadRunning) {
            System.out.println("\n==== Lead Management Menu ====");
            System.out.println("1. Add New Lead");
            System.out.println("2. View All Leads");
            System.out.println("3. View My Leads");
            System.out.println("4. Delete My Lead");
            System.out.println("5. Auto-fetch Quora Leads");
            System.out.println("6. Auto-fetch Stack Overflow Leads");
            System.out.println("7. Exit Lead Management");

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
                    displayMyLeads(loggedInUser);
                    break;
                case 4:
                    deleteMyLead(sc, loggedInUser);
                    break;
                case 5:
                    autoFetchQuoraLeads();
                    break;
                case 6:
                    autoFetchStackOverflowLeads();
                    break;
                case 7:
                    leadRunning = false;
                    System.out.println("Lead management se exit.");
                    break;
                default:
                    System.out.println("Galat option!");
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

        System.out.println("Lead successfully add ho gaya by " + loggedInUser.name);
    }

    public static void displayLeads() {
        if (leadDatabase.isEmpty()) {
            System.out.println("Koi lead nahi hai.");
            return;
        }

        System.out.println("\n===== All Leads =====");
        for (Lead lead : leadDatabase) {
            lead.displayLead();
        }
    }

    public static void displayMyLeads(User user) {
        boolean found = false;
        System.out.println("\n===== " + user.name + " ke Leads =====");

        for (Lead lead : leadDatabase) {
            if (lead.leadOwner.equals(user.name)) {
                lead.displayLead();
                found = true;
            }
        }

        if (!found) {
            System.out.println("Koi lead nahi hai " + user.name + " ki.");
        }
    }

    public static void deleteMyLead(Scanner sc, User user) {
        displayMyLeads(user);

        System.out.print("Delete karne wale question ka exact naam batao: ");
        String questionToDelete = sc.nextLine();

        Iterator<Lead> iterator = leadDatabase.iterator();
        boolean found = false;

        while (iterator.hasNext()) {
            Lead lead = iterator.next();
            if (lead.leadOwner.equals(user.name) && lead.question.equals(questionToDelete)) {
                iterator.remove();
                found = true;
                System.out.println("Lead delete ho gayi: " + questionToDelete);
                break;
            }
        }

        if (!found) {
            System.out.println("Lead nahi mili jo delete karni thi.");
        }
    }

    public static void autoFetchQuoraLeads() {
        System.out.println("Fetching Quora Leads...");

        leadDatabase.add(new Lead("Quora System", "What are the best programming languages to learn in 2025?", "CS", "Quora"));
        leadDatabase.add(new Lead("Quora System", "How to crack Google software engineering interviews?", "CS", "Quora"));
        leadDatabase.add(new Lead("Quora System", "Latest AI tools for data science?", "CS", "Quora"));
        leadDatabase.add(new Lead("Quora System", "Top machine learning algorithms to know?", "CS", "Quora"));

        System.out.println("4 CS leads Quora se fetch ho gayi.");
    }

    public static void autoFetchStackOverflowLeads() {
        System.out.println("Fetching Stack Overflow Leads...");

        leadDatabase.add(new Lead("StackOverflow System", "How to optimize SQL queries?", "CS", "Stack Overflow"));
        leadDatabase.add(new Lead("StackOverflow System", "Best practices for clean Java code?", "CS", "Stack Overflow"));
        leadDatabase.add(new Lead("StackOverflow System", "Secure authentication in web apps?", "CS", "Stack Overflow"));
        leadDatabase.add(new Lead("StackOverflow System", "How to handle concurrency in Java?", "CS", "Stack Overflow"));

        System.out.println("4 CS leads Stack Overflow se fetch ho gayi.");
    }

    public static User getUserById(String userId) {
        for (User user : users) {
            if (user.userId.equalsIgnoreCase(userId)) {
                return user;
            }
        }
        return null;
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
}
