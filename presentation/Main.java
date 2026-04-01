package presentation;

import business.*;
import entity.*;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static UserBUS userBUS = new UserBUS();
    static ServiceBUS serviceBUS = new ServiceBUS();
    static EquipmentBUS equipmentBUS = new EquipmentBUS();
    static RoomBUS roomBUS = new RoomBUS();
    static BookingBUS bookingBUS = new BookingBUS();
    static User currentUser;

    public static void main(String[] args) {

        while (true) {
            System.out.println("\n=========================");
            System.out.println(" HE THONG QUAN LY PHONG HOP ");
            System.out.println("=========================");

            System.out.println("1. Register");
            System.out.println("2. Login");
            System.out.println("3. Thoat");

            System.out.print("Chon: ");
            int choice = sc.nextInt();
            sc.nextLine();

            if (choice == 1) {
                register();
            } else if (choice == 2) {
                User user = login();
                if (user != null) {
                    currentUser = user;

                    if (user.getRole().equals("ADMIN")) adminMenu();
                    else userMenu();
                }
            } else if (choice == 3) {
                System.out.println("Thoat chuong trinh...");
                System.exit(0);
            }
        }
    }

    // ================= AUTH =================
    static void register() {
        User u = new User();

        System.out.print("Username: ");
        u.setUsername(sc.nextLine());

        System.out.print("Password: ");
        u.setPassword(sc.nextLine());

        if (userBUS.register(u))
            System.out.println("Dang ky thanh cong!");
        else
            System.out.println("Dang ky that bai!");
    }

    static User login() {
        System.out.print("Username: ");
        String u = sc.nextLine();

        System.out.print("Password: ");
        String p = sc.nextLine();

        User user = userBUS.login(u, p);

        if (user == null)
            System.out.println("Sai tai khoan hoac mat khau!");
        else
            System.out.println("Login thanh cong!");

        return user;
    }

    // ================= ADMIN =================
    static void adminMenu() {
        while (true) {
            System.out.println("\n=== ADMIN MENU ===");
            System.out.println("1. Quan ly Service");
            System.out.println("2. Quan ly Equipment");
            System.out.println("3. Quan ly Room");
            System.out.println("4. Quan ly User");
            System.out.println("5. Quan ly Booking");
            System.out.println("0. Logout");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: serviceMenu(); break;
                case 2: equipmentMenu(); break;
                case 3: roomMenu(); break;
                case 4: userManagementMenu(); break;
                case 5: bookingMenu(); break;
                case 0: return;
            }
        }
    }

    // ================= USER =================
    static void userMenu() {
        while (true) {
            System.out.println("\n=== USER MENU ===");
            System.out.println("1. View Service");
            System.out.println("2. Dat phong");
            System.out.println("3. Xem phong da dat");
            System.out.println("0. Logout");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: viewService(); break;
                case 2: createBooking(); break;
                case 3: viewMyBooking(); break;
                case 0: return;
            }
        }
    }

    // ================= BOOKING =================
    static void createBooking() {

        System.out.println("\n=== DAT PHONG ===");

        if (roomBUS.getAll().isEmpty()) {
            System.out.println("Khong co phong!");
            return;
        }

        viewRoom();
        System.out.print("Chon room id: ");
        int roomId = sc.nextInt();
        sc.nextLine();

        System.out.print("Start (yyyy-MM-dd HH:mm): ");
        String start = sc.nextLine();

        System.out.print("End (yyyy-MM-dd HH:mm): ");
        String end = sc.nextLine();

        if (equipmentBUS.getAll().isEmpty()) {
            System.out.println("Khong co equipment!");
            return;
        }

        viewEquipment();
        System.out.print("Chon equipment id: ");
        int eqId = sc.nextInt();
        sc.nextLine();

        if (serviceBUS.getAll().isEmpty()) {
            System.out.println("Khong co service!");
            return;
        }

        viewService();
        System.out.print("Chon service id: ");
        int serviceId = sc.nextInt();
        sc.nextLine();

        bookingBUS.createBooking(
                currentUser.getId(),
                roomId,
                start,
                end,
                eqId,
                serviceId
        );

        System.out.println("Dat phong thanh cong!");
    }

    static void viewMyBooking() {
        var list = bookingBUS.getByUser(currentUser.getId());

        if (list.isEmpty()) {
            System.out.println("Ban chua dat phong!");
            return;
        }

        System.out.println("\n=== DANH SACH PHONG DA DAT ===");
        list.forEach(System.out::println);
    }

    // ================= BOOKING ADMIN =================
    static void bookingMenu() {
        while (true) {
            System.out.println("\n=== BOOKING MENU ===");
            System.out.println("1. Xem danh sach");
            System.out.println("2. Mo phong");
            System.out.println("3. Dong phong");
            System.out.println("0. Back");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: viewAllBookings(); break;
                case 2: approveBooking(); break;
                case 3: cancelBooking(); break;
                case 0: return;
            }
        }
    }

    static void viewAllBookings() {
        var list = bookingBUS.getAll();

        if (list.isEmpty()) {
            System.out.println("Chua co booking!");
            return;
        }

        System.out.println("\n=== DANH SACH BOOKING ===");
        list.forEach(System.out::println);
    }

    static void approveBooking() {
        System.out.print("Nhap booking id: ");
        int id = sc.nextInt();
        sc.nextLine();

        bookingBUS.updateStatus(id, "APPROVED");
        System.out.println("Mo phong thanh cong!");
    }

    static void cancelBooking() {
        System.out.print("Nhap booking id: ");
        int id = sc.nextInt();
        sc.nextLine();

        bookingBUS.updateStatus(id, "CANCELLED");
        System.out.println("Dong phong thanh cong!");
    }

    // ================= SERVICE =================
    static void serviceMenu() {
        while (true) {
            System.out.println("\n=== SERVICE MENU ===");
            System.out.println("1. Add");
            System.out.println("2. View");
            System.out.println("3. Update");
            System.out.println("4. Delete");
            System.out.println("0. Back");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: addService(); break;
                case 2: viewService(); break;
                case 3: updateService(); break;
                case 4: deleteService(); break;
                case 0: return;
            }
        }
    }

    static void addService() {
        Service s = new Service();

        System.out.print("Name: ");
        s.setName(sc.nextLine());

        System.out.print("Price: ");
        s.setPrice(sc.nextDouble());
        sc.nextLine();

        serviceBUS.add(s);
    }

    static void viewService() {
        var list = serviceBUS.getAll();

        if (list.isEmpty()) {
            System.out.println("Chua co service!");
            return;
        }

        for (Service s : list) {
            System.out.println(s.getId() + " | " + s.getName() + " | " + s.getPrice());
        }
    }

    static void updateService() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Price: ");
        double price = sc.nextDouble();
        sc.nextLine();

        serviceBUS.update(id, name, price);
    }

    static void deleteService() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        serviceBUS.delete(id);
    }

    // ================= EQUIPMENT =================
    static void equipmentMenu() {
        while (true) {
            System.out.println("\n=== EQUIPMENT MENU ===");
            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: addEquipment(); break;
                case 2: viewEquipment(); break;
                case 3: updateEquipment(); break;
                case 4: deleteEquipment(); break;
                case 0: return;
            }
        }
    }

    static void addEquipment() {
        Equipment e = new Equipment();

        System.out.print("Name: ");
        e.setName(sc.nextLine());

        System.out.print("Quantity: ");
        e.setQuantity(sc.nextInt());
        sc.nextLine();

        equipmentBUS.add(e);
    }

    static void viewEquipment() {
        var list = equipmentBUS.getAll();

        if (list.isEmpty()) {
            System.out.println("Chua co equipment!");
            return;
        }

        for (Equipment e : list) {
            System.out.println(e.getId() + " | " + e.getName() + " | " + e.getQuantity());
        }
    }

    static void updateEquipment() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Quantity: ");
        int quantity = sc.nextInt();
        sc.nextLine();

        equipmentBUS.update(id, name, quantity);
    }

    static void deleteEquipment() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        equipmentBUS.delete(id);
    }

    // ================= ROOM =================
    static void roomMenu() {
        while (true) {
            System.out.println("\n=== ROOM MENU ===");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: addRoom(); break;
                case 2: viewRoom(); break;
                case 3: updateRoom(); break;
                case 4: deleteRoom(); break;
                case 0: return;
            }
        }
    }

    static void addRoom() {
        Room r = new Room();

        System.out.print("Name: ");
        r.setName(sc.nextLine());

        System.out.print("Capacity: ");
        r.setCapacity(sc.nextInt());
        sc.nextLine();

        System.out.print("Location: ");
        r.setLocation(sc.nextLine());

        roomBUS.add(r);
    }

    static void viewRoom() {
        var list = roomBUS.getAll();

        if (list.isEmpty()) {
            System.out.println("Chua co phong!");
            return;
        }

        for (Room r : list) {
            System.out.println(r.getId() + " | " + r.getName() + " | " + r.getCapacity() + " | " + r.getLocation());
        }
    }

    static void updateRoom() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        System.out.print("Name: ");
        String name = sc.nextLine();

        System.out.print("Capacity: ");
        int capacity = sc.nextInt();
        sc.nextLine();

        System.out.print("Location: ");
        String location = sc.nextLine();

        roomBUS.update(id, name, capacity, location);
    }

    static void deleteRoom() {
        System.out.print("ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        roomBUS.delete(id);
    }

    // ================= USER =================
    static void userManagementMenu() {
        while (true) {
            System.out.println("\n=== USER MANAGEMENT ===");
            System.out.println("1. View Users");
            System.out.println("2. Delete User");
            System.out.println("0. Back");

            int c = sc.nextInt();
            sc.nextLine();

            switch (c) {
                case 1: viewUsers(); break;
                case 2: deleteUser(); break;
                case 0: return;
            }
        }
    }

    static void viewUsers() {
        var list = userBUS.getAll();

        if (list.isEmpty()) {
            System.out.println("Chua co user!");
            return;
        }

        for (User u : list) {
            System.out.println(u.getId() + " | " + u.getUsername() + " | " + u.getRole());
        }
    }

    static void deleteUser() {
        System.out.print("Nhap ID: ");
        int id = sc.nextInt();
        sc.nextLine();

        if (id == 1) {
            System.out.println("Khong duoc xoa admin!");
            return;
        }

        userBUS.delete(id);
        System.out.println("Xoa thanh cong!");
    }
}