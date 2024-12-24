// Daha önde ödünç alınmış bir kitabın süresini uzatmak için kullanılan ekran.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class SureUzatmaEkrani extends JFrame {
    private JTable islemlerTablosu;
    private DefaultTableModel islemlerModel;
    private JTextField aramaAlani;
    private JComboBox<String> aramaKriteri;
    private JButton sureUzatButonu;

    public SureUzatmaEkrani() {
        setTitle("Kütüpedia - Süre Uzatma Ekranı");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        // Logo Icon
        ImageIcon iconSure = new ImageIcon("src/resimler/saat.png"); 
        JLabel iconSureLabel = new JLabel(iconSure);
        iconSureLabel.setBounds(550, 20, 40, 40); 
        add(iconSureLabel);

        // Başlık Alanı
        JLabel baslikLabel = new JLabel("SÜRE UZATMA İŞLEMİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 24));
        baslikLabel.setBounds(550, 20, 400, 40);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        // Arama Alanı
        JLabel aramaLabel = new JLabel("Arama:");
        aramaLabel.setBounds(770, 80, 100, 30);
        add(aramaLabel);

        aramaAlani = new JTextField();
        aramaAlani.setBounds(850, 80, 200, 30);
        add(aramaAlani);

        aramaKriteri = new JComboBox<>(new String[]{"Kitap ID", "Kitap Ad", "Üye ID",  "Üye Ad-Soyad"});
        aramaKriteri.setBounds(1060, 80, 150, 30);
        add(aramaKriteri);

        // İşlemler Tablosu
        islemlerModel = new DefaultTableModel(new String[]{"İşlem ID", "Kitap ID", "Kitap Ad", "Üye ID", "Üye Ad-Soyad",  "Ödünç Tarihi", "İade Tarihi", "Uzatma Sayısı", "Durum"}, 0);
        islemlerTablosu = new JTable(islemlerModel);
        islemlerTablosu.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(islemlerTablosu);
        scrollPane.setBounds(270, 120, 940, 400);
        add(scrollPane);

        aramaAlani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = aramaAlani.getText().trim();
                String secilenKriter = aramaKriteri.getSelectedItem().toString();
                islemleriAra(secilenKriter, aramaMetni);
            }
        });

        // Süre Uzatma Butonu
        sureUzatButonu = new JButton("Süreyi Uzat (15 GÜN)");
        sureUzatButonu.setBounds(650, 550, 200, 40);
        sureUzatButonu.setBackground(Color.decode("#007BFF"));
        sureUzatButonu.setForeground(Color.WHITE);
        add(sureUzatButonu);

        sureUzatButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                sureUzat();
            }
        });

        // İşlemleri Yükle
        islemleriGetir();
    }

    // islemler tablosundan durumu aktif olan işlemleri tabloya yazdırır.
    private void islemleriGetir() {
        islemlerModel.setRowCount(0);
        String sorgu = "SELECT islem_id, kitap_id, kitap_ad, uye_id, uye_ad_soyad,  odunc_tarihi, iade_tarihi, uzatma_sayisi, durum FROM islemler WHERE durum = 'aktif'";
        try {
            Connection conn = Baglanti.baglan();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            while (rs.next()) {
                Islem islem = new Islem();
                islem.setIslemId(rs.getInt("islem_id"));
                islem.setKitapId(rs.getInt("kitap_id"));
                islem.setKitapAd(rs.getString("kitap_ad"));
                islem.setUyeId(rs.getInt("uye_id"));
                islem.setUyeAdSoyad(rs.getString("uye_ad_soyad"));
                islem.setOduncTarihi(rs.getDate("odunc_tarihi"));
                islem.setIadeTarihi(rs.getDate("iade_tarihi"));
                islem.setUzatmaSayisi(rs.getInt("uzatma_sayisi"));
                islem.setDurum(rs.getString("durum"));

                String durumYazisi = islem.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                islemlerModel.addRow(new Object[]{
                    islem.getIslemId(),
                    islem.getKitapId(),
                    islem.getKitapAd(),
                    islem.getUyeId(),
                    islem.getUyeAdSoyad(),
                    islem.getOduncTarihi(),
                    islem.getIadeTarihi(),
                    islem.getUzatmaSayisi(),
                    durumYazisi
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void islemleriAra(String kriter, String aramaMetni) {
        islemlerModel.setRowCount(0);
        String sutun;
        switch (kriter) {
            case "Üye ID":
                sutun = "uye_id";
                break;
            case "Üye Ad-Soyad":
                sutun = "uye_ad_soyad";
                break;
            case "Kitap ID":
                sutun = "kitap_id";
                break;
            case "Kitap Ad":
                sutun = "kitap_ad";
                break;
            default:
                sutun = "uye_ad_soyad";
        }
        String sorgu = "SELECT islem_id, uye_id, uye_ad_soyad, kitap_id, kitap_ad, odunc_tarihi, iade_tarihi, uzatma_sayisi, durum FROM islemler WHERE durum = 'aktif' AND " + sutun + " LIKE ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, "%" + aramaMetni + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Islem islem = new Islem();
                islem.setIslemId(rs.getInt("islem_id"));
                islem.setKitapId(rs.getInt("kitap_id"));
                islem.setKitapAd(rs.getString("kitap_ad"));
                islem.setUyeId(rs.getInt("uye_id"));
                islem.setUyeAdSoyad(rs.getString("uye_ad_soyad"));
                islem.setOduncTarihi(rs.getDate("odunc_tarihi"));
                islem.setIadeTarihi(rs.getDate("iade_tarihi"));
                islem.setUzatmaSayisi(rs.getInt("uzatma_sayisi"));
                islem.setDurum(rs.getString("durum"));

                String durumYazisi = islem.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                islemlerModel.addRow(new Object[]{
                    islem.getIslemId(),
                    islem.getKitapId(),
                    islem.getKitapAd(),
                    islem.getUyeId(),
                    islem.getUyeAdSoyad(),
                    islem.getOduncTarihi(),
                    islem.getIadeTarihi(),
                    islem.getUzatmaSayisi(),
                    durumYazisi
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void sureUzat() {
        int seciliSatir = islemlerTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir işlem seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int islemId = (int) islemlerModel.getValueAt(seciliSatir, 0);
        
        // süresi uzatılmak istenen kitabın iade tarihinin geçip geçmediğinin kontrolü
        String gecikmeKontrolSorgusu = "SELECT COUNT(*) FROM islemler WHERE islem_id = ? AND iade_tarihi < NOW()"; 
        try{
            Connection conn = Baglanti.baglan();
            PreparedStatement psKontrol = conn.prepareStatement(gecikmeKontrolSorgusu);

            psKontrol.setInt(1, islemId);
            ResultSet rs = psKontrol.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "İade tarihi geçmiş işlemler için süre uzatılamaz!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            return;
        }
        
        // kitabın en fazla 2 kez süresinin uzatılabileceğinin kontrolü
        String uzatmaSayisiKontrolSorgusu = "SELECT uzatma_sayisi FROM islemler WHERE islem_id = ?";
        try{
            Connection conn = Baglanti.baglan();
            PreparedStatement psKontrol = conn.prepareStatement(uzatmaSayisiKontrolSorgusu);

            psKontrol.setInt(1, islemId);
            ResultSet rs = psKontrol.executeQuery();

            if (rs.next() && rs.getInt("uzatma_sayisi") >= 2) {
                JOptionPane.showMessageDialog(this, "Bir kitabın ödünç süresi en fazla 2 defa uzatılabilir!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            return;
        }

        // Süre uzatmanın önünde hiçbir engel yoksa islemler tablsounda iade_tarihi 15 gün ekler ve uzatma_sayisina 1 ekler.
        String uzatmaSorgusu = "UPDATE islemler SET iade_tarihi = DATE_ADD(iade_tarihi, INTERVAL 15 DAY), uzatma_sayisi = uzatma_sayisi + 1 WHERE islem_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(uzatmaSorgusu);

            ps.setInt(1, islemId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Süre başarıyla uzatıldı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            islemleriGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SureUzatmaEkrani().setVisible(true));
    }
}
