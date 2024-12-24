// MySQL veritabanına kolayca bağlanmak için bunu ayrı bir class içinde yapıyorum.

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Baglanti {
    private static final String URL = "jdbc:mysql://localhost:3306/kutuphane_yonetim";
    private static final String KULLANICI_ADI = "root";
    private static final String SIFRE = "alperen1234"; 

    public static Connection baglan() {
        try {
            return DriverManager.getConnection(URL, KULLANICI_ADI, SIFRE);
        } catch (SQLException e) {
            System.out.println("Veritabanı bağlantı hatası: " + e.getMessage());
            return null;
        }
    }
}
