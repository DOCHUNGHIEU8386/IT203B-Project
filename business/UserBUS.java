package business;

import entity.User;
import utils.DBConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserBUS {

    // ================= REGISTER =================
    public boolean register(User user) {
        String sql = "INSERT INTO users(username, password, role) VALUES(?,?,?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());

            // luôn là EMPLOYEE
            ps.setString(3, "EMPLOYEE");

            ps.executeUpdate();
            return true;

        } catch (SQLIntegrityConstraintViolationException e) {
            System.out.println("Username da ton tai!");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    // ================= LOGIN =================
    public User login(String username, String password) {
        String sql = "SELECT * FROM users WHERE username=? AND password=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setString(1, username);
            ps.setString(2, password);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                User u = new User();

                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));

                return u;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    // ================= GET ALL =================
    public List<User> getAll() {
        List<User> list = new ArrayList<>();

        String sql = "SELECT * FROM users";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                User u = new User();

                u.setId(rs.getInt("id"));
                u.setUsername(rs.getString("username"));
                u.setRole(rs.getString("role"));

                list.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    // ================= DELETE =================
    public void delete(int id) {
        String sql = "DELETE FROM users WHERE id=?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}