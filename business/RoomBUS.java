package business;

import entity.Room;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class RoomBUS {

    public void add(Room r) {
        String sql = "INSERT INTO rooms(name, capacity, location) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, r.getName());
            ps.setInt(2, r.getCapacity());
            ps.setString(3, r.getLocation());

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Room> getAll() {
        List<Room> list = new ArrayList<>();
        String sql = "SELECT * FROM rooms";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Room r = new Room();
                r.setId(rs.getInt("id"));
                r.setName(rs.getString("name"));
                r.setCapacity(rs.getInt("capacity"));
                r.setLocation(rs.getString("location"));
                list.add(r);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void update(int id, String name, int capacity, String location) {
        String sql = "UPDATE rooms SET name=?, capacity=?, location=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, capacity);
            ps.setString(3, location);
            ps.setInt(4, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM rooms WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}