package business;

import entity.Service;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class ServiceBUS {

    public void add(Service s) {
        String sql = "INSERT INTO services(name, price) VALUES(?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, s.getName());
            ps.setDouble(2, s.getPrice());
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public List<Service> getAll() {
        List<Service> list = new ArrayList<>();
        String sql = "SELECT * FROM services";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Service s = new Service();
                s.setId(rs.getInt("id"));
                s.setName(rs.getString("name"));
                s.setPrice(rs.getDouble("price"));
                list.add(s);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public void update(int id, String name, double price) {
        String sql = "UPDATE services SET name=?, price=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setDouble(2, price);
            ps.setInt(3, id);

            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM services WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}