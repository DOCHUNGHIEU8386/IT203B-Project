package business;

import utils.DBConnection;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class BookingBUS {

    public void createBooking(int userId, int roomId, String start, String end, int eqId, int serviceId) {

        String sqlBooking = "INSERT INTO bookings(user_id, room_id, start_time, end_time, status) VALUES(?,?,?,?,?)";
        String sqlEq = "INSERT INTO booking_equipments(booking_id, equipment_id, quantity) VALUES(?,?,1)";
        String sqlService = "INSERT INTO booking_services(booking_id, service_id, quantity) VALUES(?,?,1)";

        try (Connection conn = DBConnection.getConnection()) {

            // insert booking
            PreparedStatement ps = conn.prepareStatement(sqlBooking, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, userId);
            ps.setInt(2, roomId);
            ps.setString(3, start);
            ps.setString(4, end);
            ps.setString(5, "PENDING");

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                int bookingId = rs.getInt(1);

                // insert equipment
                PreparedStatement psEq = conn.prepareStatement(sqlEq);
                psEq.setInt(1, bookingId);
                psEq.setInt(2, eqId);
                psEq.executeUpdate();

                // insert service
                PreparedStatement psS = conn.prepareStatement(sqlService);
                psS.setInt(1, bookingId);
                psS.setInt(2, serviceId);
                psS.executeUpdate();

                System.out.println("Dat phong thanh cong!");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public List<String> getByUser(int userId) {
        List<String> list = new ArrayList<>();

        String sql = """
        SELECT b.id, r.name, b.start_time, b.end_time, b.status
        FROM bookings b
        JOIN rooms r ON b.room_id = r.id
        WHERE b.user_id = ?
    """;

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                String s = rs.getInt("id") + " | "
                        + rs.getString("name") + " | "
                        + rs.getString("start_time") + " -> "
                        + rs.getString("end_time") + " | "
                        + rs.getString("status");

                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }
}