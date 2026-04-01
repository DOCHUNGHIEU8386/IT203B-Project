package business;

import entity.Equipment;
import utils.DBConnection;

import java.sql.*;
import java.util.*;

public class EquipmentBUS {

    public void add(Equipment e) {
        String sql = "INSERT INTO equipments(name, quantity) VALUES(?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, e.getName());
            ps.setInt(2, e.getQuantity());
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public List<Equipment> getAll() {
        List<Equipment> list = new ArrayList<>();
        String sql = "SELECT * FROM equipments";

        try (Connection conn = DBConnection.getConnection();
             Statement st = conn.createStatement()) {

            ResultSet rs = st.executeQuery(sql);

            while (rs.next()) {
                Equipment e = new Equipment();
                e.setId(rs.getInt("id"));
                e.setName(rs.getString("name"));
                e.setQuantity(rs.getInt("quantity"));
                list.add(e);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return list;
    }

    public void update(int id, String name, int quantity) {
        String sql = "UPDATE equipments SET name=?, quantity=? WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, name);
            ps.setInt(2, quantity);
            ps.setInt(3, id);
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void delete(int id) {
        String sql = "DELETE FROM equipments WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}