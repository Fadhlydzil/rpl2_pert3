package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MahasiswaDAO {
    private Connection connection;

    public MahasiswaDAO() {
       try {
           Class.forName("org.postgresql.Driver");
           connection = DriverManager.getConnection(
               "jdbc:postgresql://localhost:5432/mvc_db",
               "postgres",
               "281204"       
           );
           System.out.println("✅ Koneksi PostgreSQL berhasil!");
       } catch (ClassNotFoundException e) {
           System.out.println("❌ Driver PostgreSQL tidak ditemukan: " + e.getMessage());
       } catch (SQLException e) {
           System.out.println("❌ Gagal konek ke database: " + e.getMessage());
       }
   }

    public boolean checkConnection() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
        }
        return false;
    }

    public void addMahasiswa(ModelMahasiswa mahasiswa) {
        String sql = "INSERT INTO mahasiswa (npm, nama, semester, ipk) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mahasiswa.getNpm());
            pstmt.setString(2, mahasiswa.getNama());
            pstmt.setInt(3, mahasiswa.getSemester());
            pstmt.setFloat(4, mahasiswa.getIpk());
            pstmt.executeUpdate();
            System.out.println("✅ Data mahasiswa berhasil ditambahkan.");
        } catch (SQLException e) {
            System.out.println("❌ Gagal menambahkan data mahasiswa!");
        }
    }

    public List<ModelMahasiswa> getAllMahasiswa() {
        List<ModelMahasiswa> mahasiswaList = new ArrayList<>();
        String sql = "SELECT * FROM mahasiswa";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                mahasiswaList.add(new ModelMahasiswa(
                    rs.getInt("id"),
                    rs.getString("npm"),
                    rs.getString("nama"),
                    rs.getInt("semester"),
                    rs.getFloat("ipk")
                ));
            }
        } catch (SQLException e) {
            System.out.println("❌ Gagal mengambil data mahasiswa!");
        }
        return mahasiswaList;
    }

    public void updateMahasiswa(ModelMahasiswa mahasiswa) {
        String sql = "UPDATE mahasiswa SET npm = ?, nama = ?, semester = ?, ipk = ? WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, mahasiswa.getNpm());
            pstmt.setString(2, mahasiswa.getNama());
            pstmt.setInt(3, mahasiswa.getSemester());
            pstmt.setFloat(4, mahasiswa.getIpk());
            pstmt.setInt(5, mahasiswa.getId());
            pstmt.executeUpdate();
            System.out.println("✅ Data mahasiswa berhasil diperbarui.");
        } catch (SQLException e) {
            System.out.println("❌ Gagal memperbarui data mahasiswa!");
        }
    }

    public void deleteMahasiswa(int id) {
        String sql = "DELETE FROM mahasiswa WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Data mahasiswa berhasil dihapus.");
        } catch (SQLException e) {
            System.out.println("❌ Gagal menghapus data mahasiswa!");
        }
    }

    public void closeConnection() {
        try {
            if (connection != null) {
                connection.close();
                System.out.println("🔒 Koneksi PostgreSQL ditutup.");
            }
        } catch (SQLException e) {
        }
    }
}