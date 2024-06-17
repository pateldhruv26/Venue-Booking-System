import java.util.*;
import java.io.*;
import java.util.ArrayList;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

class ReservationItem {
    String id;
    String name;
    String location;

    ReservationItem() {
        this.id = null;
        this.name = null;
        this.location = null;
    }

    ReservationItem(String id, String name, String location) {
        this.id = id;
        this.name = name;
        this.location = location;
    }
}


class Venue extends ReservationItem {
    String vid;
    Venue head;
    HashMap<String, Boolean> date = new HashMap<String, Boolean>();
    String price;
    String name;
    String location;
    Venue next;

    Venue() {
        super();
        this.date = new HashMap<String, Boolean>();
        this.price = null;
        this.next = null;
    }

    Venue(String vid, String name, String location, HashMap<String, Boolean> date, String price) {
        super(vid, name, location);
        this.vid = vid;
        this.name = name;
        this.location = location;
        this.date = date;
        this.price = price;
        this.next = null;
    }

    public void cancelBooking(String bookingId, String bookingDate) {
        if (date.containsKey(bookingDate) && !date.get(bookingDate)) {
            date.put(bookingDate, true);
        }
    }
}


class User extends ReservationItem {
    User head;
    String uid;
    String u_name;
    String mobile;
    String email;
    String bid;
    String bdate;
    User next;

    User() {
        super();
        this.uid = null;
        this.mobile = null;
        this.email = null;
        this.bid = null;
        this.bdate = null;
        this.next = null;
    }

    User(String uid, String name, String mobile, String email) {
        super(uid, name, null);
        this.uid = uid;
        this.u_name = name;
        this.mobile = mobile;
        this.email = email;
        this.bid = null;
        this.bdate = null;
        this.next = null;
    }

    User(String uid, String name, String mobile, String email, String date, String vid) {
        super(uid, name, null);
        this.uid = uid;
        this.u_name = name;
        this.mobile = mobile;
        this.email = email;
        this.bid = vid;
        this.bdate = date;
        this.next = null;
    }

    public void cancelBooking(Venue venue) {
        if (this.bid != null) {
            venue.cancelBooking(this.bid, this.bdate);
            this.bid = null;
            this.bdate = null;
        }
    }

}


class Agent {

    Venue addvenue(Venue list, Venue new_node) {

        if (list.head == null) {
            list.head = new_node;
        } else {
            Venue last = list.head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = new_node;
        }
        return list;
    }

    void listbookedvenue(Venue list) {

        if (list == null) {
            System.out.println("                                                    No venue Available");
        } else {
            Venue temp = list.head;
            while (temp != null) {
                System.out.println("                                     ------------------------------------------------");
                System.out.println();
                System.out.println("                                                    Venue id : " + temp.vid);
                System.out.println("                                                    Venue name : " + temp.name);
                System.out.println("                                                    Venue location : " + temp.location);
                System.out.println("                                                    Printing all the Booked dates : ");
                int ct = 0;
                Iterator it = temp.date.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    if (pair.getValue().equals(false)) {
                        System.out.print("                                                    ");
                        System.out.println(pair.getKey());
                        ct++;
                    }

                }
                if (ct == 0) {
                    System.out.println("                                                    No Booked Dates");
                }
                System.out.println();
                System.out.println("                                                    Venue price : " + temp.price);
                temp = temp.next;
                System.out.println();

            }
        }
    }

    void write_to_file(Venue list, Venue new_node) {

        String dates = "";
        String Bookdates = "";
        Iterator it = new_node.date.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            // System.out.println(pair.getKey() + " = " + pair.getValue());
            if (pair.getValue().equals(true)) {
                dates += pair.getKey() + " ";
            } else {
                Bookdates += pair.getKey() + " ";
            }
            it.remove();
        }

        File file = new File("Venues.csv");
        try {

            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter out = new PrintWriter(br);

            out.printf("%s, %s, %s, %s, %s, %s\n", new_node.vid, new_node.name, new_node.location, dates, Bookdates,
                    new_node.price);

            out.close();
            br.close();
            fr.close();
        } catch (IOException e) {

            e.printStackTrace();
        } finally {
        }
    }

    void add(Venue list, int t) {
        while (t > 0) {
            Scanner sc = new Scanner(System.in);
            String name = new String();
            String location = new String();
            int n;
            String vid = new String();
            String temp = new String();
            ArrayList<String> date = new ArrayList<String>();
            String price = new String();
            System.out.println("                                     ------------------------------------------------");
            System.out.println("                                                    Enter venue Id : ");
            System.out.print("                                                    ");
            vid = sc.next();
            System.out.println("                                                    Enter venue price : ");
            System.out.print("                                                    ");
            price = sc.next();
            System.out.println("                                                    Enter venue name : ");
            System.out.print("                                                    ");
            name = sc.next();
            System.out.println("                                                    Enter venue location : ");
            System.out.print("                                                    ");
            location = sc.next();
            System.out.println("                                                    Enter the number of avialable dates : ");
            System.out.print("                                                    ");
            n = sc.nextInt();
            HashMap<String, Boolean> tdate = new HashMap<String, Boolean>();
            for (int i = 0; i < n; i++) {
                System.out.println("                                                    Enter venue date in (dd/mm/yyyy) format : ");
                System.out.print("                                                    ");
                temp = sc.next();
                tdate.put(temp, true);
            }

            t--;
            Venue new_node = new Venue(vid, name, location, tdate, price);

            list = addvenue(list, new_node);

            write_to_file(list, new_node);
            // write_all_venues_to_file(list);
        }

    }

    void listallvenue(Venue list) {
        list.head = null;
        list = readfromcsv(list);
        if (list == null) {
            System.out.println("No venue");
        } else {
            Venue temp = list.head;
            while (temp != null) {
                System.out.println("                                     ------------------------------------------------");
                System.out.println();
                System.out.println("                                                    Venue Id : " + temp.vid);
                System.out.println("                                                    Venue name : " + temp.name);
                System.out.println("                                                    Venue location : " + temp.location);
                System.out.println("                                                    Printing all the available dates : ");
                Iterator it = temp.date.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    if (pair.getValue().equals(true)) {
                        System.out.print("                                                    ");
                        System.out.println(pair.getKey());
                    }

                }
                System.out.println("                                                    Venue price : " + temp.price);
                temp = temp.next;
                System.out.println();

            }

        }
    }

    Venue readfromcsv(Venue list) {
        Path pathToFile = Paths.get("Venues.csv");

        try {
            BufferedReader br = Files.newBufferedReader(pathToFile,
                    StandardCharsets.US_ASCII);
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {

                String[] attributes = line.split(",");
                String vid = attributes[0];
                String n = attributes[1];
                String l = attributes[2];
                String d[] = attributes[3].split(" ");
                HashMap<String, Boolean> date = new HashMap<String, Boolean>();

                for (String text : d) {
                    date.put(text, true);
                }
                if (attributes[4] != null) {
                    d = attributes[4].split(" ");
                    for (String text : d) {
                        date.put(text, false);
                    }

                }
                String p = attributes[5];

                line = br.readLine();
                Venue new_n = new Venue(vid, n, l, date, p);
                list = addvenue(list, new_n);

            }

        } catch (

                IOException ioe) {
            ioe.printStackTrace();
        }

        return list;
    }

}

class Test {

    static void agent_opt(Venue list, Agent a) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("                                     ------------------------------------------------");
            System.out.println("                                                    1.ADD VENUE \n                                                    2.SHOW ALL BOOKINGS \n                                                    3.SHOW ALL VENUES \n                                                    4.RETURN TO MAIN MENU");
            System.out.println("                                     ------------------------------------------------");
            System.out.println("                                                    Enter your choice:");
            System.out.print("                                                    ");
            int choice1 = sc.nextInt();
            switch (choice1) {
                case 1: {
                    System.out.println("                                              Enter the no of venues to be added: ");
                    System.out.print("                                                    ");
                    int t = sc.nextInt();
                    a.add(list, t);
                    System.out.println("                                                 Venues added successfully");
                    break;
                }
                case 2: {
                    System.out.println("\n                                              Printing all the booked venues : ");
                    a.listbookedvenue(list);
                    break;
                }
                case 3:
                    System.out.println("                                                 Available Venues are:");
                    System.out.println("");
                    a.listallvenue(list);
                    break;
                case 4:

                    return;
            }
        }
    }

    static User useradd(User first, User new_node) {
        if (first.head == null) {
            first.head = new_node;

        } else {

            User last = first.head;
            while (last.next != null) {
                last = last.next;
            }
            last.next = new_node;
        }
        return first;
    }

    static void write_all_venues_to_file(Venue list) {
        Venue new_node = new Venue();
        new_node = list.head;
        if (new_node == null) {
            return;
        } else {
            try {
                File file = new File("Venues.csv");

                FileWriter fr = new FileWriter(file, false);
                BufferedWriter br = new BufferedWriter(fr);
                PrintWriter out = new PrintWriter(br);

                out.printf("%s, %s, %s, %s, %s, %s\n", "Venue Id", "Venue Name", "Venue Location",
                        "Venue Available Dates", "Venue Booked Dates", "Venue Price");
                while (new_node != null) {

                    String dates = "";
                    String Bookdates = "";
                    Iterator it = new_node.date.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry pair = (Map.Entry) it.next();
                        if (pair.getValue().equals(true)) {
                            dates += pair.getKey() + " ";
                        } else {
                            Bookdates += pair.getKey() + " ";
                        }
                        it.remove();
                    }

                    out.printf("%s, %s, %s, %s, %s, %s\n", new_node.vid, new_node.name, new_node.location, dates,
                            Bookdates, new_node.price);

                    new_node = new_node.next;
                }

                out.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static void  write_to_userfile(User first, User new_node) {

        File file = new File("User.csv");
        try {

            FileWriter fr = new FileWriter(file, true);
            BufferedWriter br = new BufferedWriter(fr);
            PrintWriter out = new PrintWriter(br);

            out.printf("%s, %s, %s, %s, %s, %s\n", new_node.uid, new_node.u_name, new_node.mobile, new_node.email,
                    new_node.bdate, new_node.bid);

            out.close();
            br.close();
            fr.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
    }

    static void newuser(User first) {

        Scanner sc = new Scanner(System.in);
        String name = new String();
        String mo = new String();
        String email = new String();
        Random rand = new Random();
        int upperbound = 25;
        String id = Integer.toString(rand.nextInt(upperbound));

        System.out.println("                                                    Enter User name : ");
        System.out.print("                                                    ");
        name = sc.next();
        System.out.println("                                                    Enter the mobile number of user : ");
        System.out.print("                                                    ");
        mo = sc.next();
        System.out.println("                                                    Enter the email id of user : ");
        System.out.print("                                                    ");
        email = sc.next();
        System.out.print("                                                    Your User ID is: " + id);
        System.out.print("                                                    ");
        User new_node = new User(id, name, mo, email);

        first = useradd(first, new_node);

        write_to_userfile(first, new_node);
    }

    static void book_venue(String uid, User first, Venue list, String id, String date) {
        Venue temp = list.head;
        User temp1 = first.head;
        if (temp1 == null) {
            System.out.println("                                                    No User Found in database !!");
            return;
        } else {
            while (temp1 != null && temp1.uid.equals(uid) != true) {
                temp1 = temp1.next;
            }
        }
        if (temp1 == null) {
            System.out.println("                                                    No User Found in database !!");
            return;
        }

        if (temp == null) {
            System.out.println("                                                    No Venue Found");
            return;
        } else {
            while (temp != null && temp.vid.equals(id) != true) {
                temp = temp.next;
            }
        }
        if (temp == null) {
            System.out.println("                                                    No venue found");
            return;
        }
        temp1.bdate = date;
        temp1.bid = id;

        temp.date.put(date, false);
        System.out.println("                                                    Booking date: " + date + " and venue id " + id + " has been booked successfully");
        add_all_user_to_file(first);
        write_all_venues_to_file(list);


//        if (choice2 == 2) {
//            temp1.cancelBooking(temp);
//            System.out.println("Booking canceled successfully.");
//            add_all_user_to_file(first);
//            write_all_venues_to_file(list);
//        }
    }

    static void add_all_user_to_file(User first) {
        User new_node = new User();
        new_node = first.head;
        if (new_node == null) {
            return;
        } else {
            try {
                File file = new File("User.csv");

                FileWriter fr = new FileWriter(file, false);
                BufferedWriter br = new BufferedWriter(fr);
                PrintWriter out = new PrintWriter(br);

                out.printf("%s, %s, %s, %s, %s, %s\n", "User Id", "User Name", "User Mobile No",
                        "User E-mail Id", "Venue Booked Date", "Venue Booking Id");
                while (new_node != null) {

                    // String dates = "";
                    // String Bookdates = "";
                    // Iterator it = new_node.date.entrySet().iterator();
                    // while (it.hasNext()) {
                    // Map.Entry pair = (Map.Entry) it.next();
                    // if (pair.getValue().equals(true)) {
                    // dates += pair.getKey() + " ";
                    // } else {
                    // Bookdates += pair.getKey() + " ";
                    // }
                    // it.remove();
                    // }

                    out.printf("%s, %s, %s, %s, %s, %s\n", new_node.uid, new_node.u_name, new_node.mobile,
                            new_node.email,
                            new_node.bdate, new_node.bid);

                    new_node = new_node.next;
                }

                out.close();
                br.close();
                fr.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    static User read_all_user(User first) {
        Path pathToFile = Paths.get("User.csv");

        try {
            BufferedReader br = Files.newBufferedReader(pathToFile,
                    StandardCharsets.US_ASCII);
            String line = br.readLine();
            line = br.readLine();

            while (line != null) {

                String[] attributes = line.split(",");
                String uid = attributes[0];
                String n = attributes[1];
                String mo = attributes[2];
                String e = attributes[3];
                String d = attributes[4];

                String vid = attributes[5];

                line = br.readLine();
                User new_n = new User(uid, n, mo, e, d, vid);
                first = useradd(first, new_n);

            }

        } catch (

                IOException ioe) {
            ioe.printStackTrace();
        }

        return first;
    }

    static void user_listallvenue(Venue list) {

        if (list == null) {
            System.out.println("                                                    No venue");
        } else {
            Venue temp = list.head;
            while (temp != null) {
                System.out.println();
                System.out.println("                                                    Venue Id : " + temp.vid);
                System.out.println("                                                    Venue name : " + temp.name);
                System.out.println("                                                    Venue location : " + temp.location);
                System.out.println("                                                    Printing all the available dates : ");

                Iterator it = temp.date.entrySet().iterator();
                while (it.hasNext()) {
                    Map.Entry pair = (Map.Entry) it.next();

                    if (pair.getValue().equals(true)) {
                        System.out.println(pair.getKey());
                    }

                }
                System.out.println();
                System.out.println("                                                    Venue price : " + temp.price);
                temp = temp.next;
                System.out.println();

            }

        }
    }

    static void user_opt(User first, Agent a, Venue list) {
        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("                                     ------------------------------------------------");
            System.out.println( "                                                    1.Create New User \n                                                    2.Book Venue \n                                                    3.RETURN TO MAIN MENU");
            System.out.println("                                     ------------------------------------------------");
            System.out.println("                                                    Enter your choice:");
            System.out.print("                                                    ");
            int choice1 = sc.nextInt();
            switch (choice1) {
                case 1: {
                    newuser(first);
                    System.out.println("");
                    System.out.println("                                                    User Created Successfully");
                    break;
                }
                case 2: {
                    String uid = new String();
                    System.out.println("                                                    Enter your user id : ");
                    System.out.print("                                                    ");
                    uid = sc.next();
                    System.out.println("                                                    Printing all the Available venues : ");
                    a.listallvenue(list);
                    System.out.println("                                                    Enter the Venue id to be booked : ");
                    String id = new String();
                    String date = new String();
                    System.out.print("                                                    ");
                    id = sc.next();
                    System.out.println("                                                    Enter the Date to be booked : ");
                    System.out.print("                                                    ");
                    date = sc.next();
                    book_venue(uid, first, list, id, date);
                    break;
                }
                case 3:

                    return;
            }
        }
    }

    public static void main(String[] args) {
        Venue list = new Venue();
        User first = new User();
        Agent a = new Agent();
        first = read_all_user(first);
        list = a.readfromcsv(list);
        Scanner sc = new Scanner(System.in);
        System.out.println();
        while (true) {

            System.out.println("                                     ------------- VENUE BOOKING SYSTEM -------------                                                                \n");
            System.out.println("                                                    1.  USER PORTAL\n                                                    2.  AGENT PORTAL\n                                                    3.  EXIT \n ");
            System.out.println("                                     ------------------------------------------------");
            System.out.println("                                                    Enter your Choice: ");
            System.out.print("                                                          ");
            char choice = sc.next().charAt(0);
            if (choice == '1') {
                user_opt(first, a, list);
            } else if (choice == '2') {
                agent_opt(list, a);
            } else if (choice == '3') {
                System.exit(0);
            }
        }
    }
}
