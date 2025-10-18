package database;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Vector;

/**
 * Repository class for handling CRUD operations on the {@code pcs} table.
 * <p>
 * Each PC entry represents a unique client machine identified by its
 * name and IP address.
 */
public class PCRepository {

    private final static DatabaseManager db = DatabaseManager.getInstance();

    /**
     * Registers a new PC in the database if it doesn’t already exist.
     *
     * @param name the name of the PC
     * @param ip the IP address of the PC
     * @return the assigned PC ID or the existing one if already registered
     */
    public static int registerPC(String name, String ip) {
        try (Connection conn = db.getConnection()) {
            PreparedStatement check = conn.prepareStatement("SELECT id FROM pcs WHERE ip = ?");
            check.setString(1, ip);
            ResultSet rs = check.executeQuery();
            if (rs.next()) return rs.getInt("id");

            PreparedStatement insert = conn.prepareStatement(
                "INSERT INTO pcs (name, ip) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS);
            insert.setString(1, name);
            insert.setString(2, ip);
            insert.executeUpdate();

            ResultSet keys = insert.getGeneratedKeys();
            if (keys.next()) return keys.getInt(1);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * Retrieves the name of a PC given its database ID.
     *
     * @param id the PC ID
     * @return the PC’s name, or {@code null} if not found
     */
    public String getPCNameById(int id) {
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT name FROM pcs WHERE id = ?")) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getString("name");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Retrieves a PC’s ID by its name.
     *
     * @param name the PC name
     * @return the PC ID, or -1 if not found
     */
    public int getPCIdByName(String name) {
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT id FROM pcs WHERE name = ?")) {
            ps.setString(1, name);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }
    
    public static Optional<String> getUuidById(int id) {
        try (Connection conn = db.getConnection();
             PreparedStatement ps = conn.prepareStatement("SELECT uuid FROM pcs WHERE id = ?")) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(rs.getString("uuid"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }

    /**
     * Retrieves all registered PC names.
     *
     * @return a list of PC names
     */
    public Vector<String> getAllPCNames() {
    	Vector<String> pcs = new Vector<>();
        try (Connection conn = db.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT name FROM pcs")) {
            while (rs.next()) pcs.add(rs.getString("name"));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return pcs;
    }
    
    public static Vector<Integer> getAllPcId(){
    	Vector<Integer> ids = new Vector<>();
    	
    	Connection conn = db.getConnection();
    	synchronized(conn) {
    		try (Statement stmt = conn.createStatement();
    	         ResultSet rs = stmt.executeQuery("SELECT id FROM pcs")) {
    	            while (rs.next()) ids.add(rs.getInt("id"));
    	            
    	            
    	        } catch (SQLException e) {
    	            e.printStackTrace();
    	        }
    	}
    	
    	
    	return ids;
    }
    
}















