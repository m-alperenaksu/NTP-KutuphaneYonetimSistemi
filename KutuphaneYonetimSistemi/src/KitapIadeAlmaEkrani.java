// Daha önce ödünç verilmiş kitapları iade almak için kullanılan ekran.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KitapIadeAlmaEkrani extends JFrame {
    private JTable islemlerTablosu;
    private DefaultTableModel islemlerModel;
    private JTextField aramaAlani;
    private JComboBox<String> aramaKriteri;
    private JButton iadeAlButonu;

    public KitapIadeAlmaEkrani() {
        setTitle("Kütüpedia - Kitap İade Alma Ekranı");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        //Logo Icon
        ImageIcon iconIade = new ImageIcon("src/resimler/iade.png"); 
        JLabel iconIadeLabel = new JLabel(iconIade);
        iconIadeLabel.setBounds(585, 20, 40, 40); 
        add(iconIadeLabel);

        // Başlık Alanı
        JLabel baslikLabel = new JLabel("İADE ALMA İŞLEMİ", SwingConstants.CENTER);
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

        // Arama işleminin hangi kritere göre yapılacağının seçimi
        aramaKriteri = new JComboBox<>(new String[]{"Kitap ID", "Kitap Ad", "Üye ID", "Üye Ad-Soyad"});
        aramaKriteri.setBounds(1060, 80, 150, 30);
        add(aramaKriteri);

        // İşlemler Tablosu
        islemlerModel = new DefaultTableModel(new String[]{"İşlem ID", "Kitap ID", "Kitap Ad", "Üye ID", "Üye Ad-Soyad", "Ödünç Tarihi", "İade Tarihi"}, 0);
        islemlerTablosu = new JTable(islemlerModel);
        islemlerTablosu.setAutoCreateRowSorter(true);
        JScrollPane scrollPane = new JScrollPane(islemlerTablosu);
        scrollPane.setBounds(270, 120, 940, 400);
        add(scrollPane);

        // Arama çubuğuna herhangi bir karakter yazıldığında otomatik olarak arama işlemi yapması için KeyListener
        aramaAlani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = aramaAlani.getText().trim();
                String secilenKriter = aramaKriteri.getSelectedItem().toString();
                islemleriAra(secilenKriter, aramaMetni);
            }
        });

        // İade Al Butonu
        iadeAlButonu = new JButton("İade Al");
        iadeAlButonu.setBounds(650, 550, 200, 40);
        iadeAlButonu.setBackground(Color.decode("#007BFF"));
        iadeAlButonu.setForeground(Color.WHITE);
        add(iadeAlButonu);

        iadeAlButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                iadeAl(); 
            }
        });


        // Ekran ilk açıldığında işlemleri yükle
        islemleriGetir();
    }

    
    // islemler tablosunda durumu aktif olan islemleri tabloya yazdırır.
    private void islemleriGetir() {
        islemlerModel.setRowCount(0);
        String sorgu = "SELECT islem_id, kitap_id, kitap_ad, uye_id, uye_ad_soyad, odunc_tarihi, iade_tarihi FROM islemler WHERE durum = 'aktif'";
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

                islemlerModel.addRow(new Object[]{
                    islem.getIslemId(),
                    islem.getKitapId(),
                    islem.getKitapAd(),
                    islem.getUyeId(),
                    islem.getUyeAdSoyad(),
                    islem.getOduncTarihi(),
                    islem.getIadeTarihi()
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
            case "Kitap ID":
                sutun = "kitap_id";
                break;
            case "Kitap Ad":
                sutun = "kitap_ad";
                break;
            case "Üye ID":
                sutun = "uye_id";
                break;
            case "Üye Ad-Soyad":
                sutun = "uye_ad_soyad";
                break;
            default:
                sutun = "kitap_ad";
        }
        String sorgu = "SELECT islem_id, kitap_id, kitap_ad, uye_id, uye_ad_soyad, odunc_tarihi, iade_tarihi FROM islemler WHERE durum = 'aktif' AND " + sutun + " LIKE ?";
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

                islemlerModel.addRow(new Object[]{
                    islem.getIslemId(),
                    islem.getKitapId(),
                    islem.getKitapAd(),
                    islem.getUyeId(),
                    islem.getUyeAdSoyad(),
                    islem.getOduncTarihi(),
                    islem.getIadeTarihi()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void iadeAl() {
        int seciliSatir = islemlerTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir işlem seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int islemId = (int) islemlerModel.getValueAt(seciliSatir, 0);
        int kitapId = (int) islemlerModel.getValueAt(seciliSatir, 1);
        int uyeId = (int) islemlerModel.getValueAt(seciliSatir, 3);

        // islemler tablosunda islemin iade_tarihini bugünün tarihi olarak günceller.
        String islemGuncelleSorgusu = "UPDATE islemler SET durum = 'tamamlandi', iade_tarihi = NOW() WHERE islem_id = ?";
        
        // kitapalr tablosunda kitabın durumunu rafta olarak günceller
        String kitapGuncelleSorgusu = "UPDATE kitaplar SET durum = 'rafta' WHERE kitap_id = ?";
        
        //uyeler tablosunda üyenin kitap_sayisi ni 1 azaltır.
        String uyeninKitapSayisiniAzaltmaSorgusu = "UPDATE uyeler SET kitap_sayisi = kitap_sayisi - 1 WHERE uye_id = ?";
        
        // iade_tarihi ile bugün arasında kaç gün fark olduğunu hesaplar.
        String gunFarkiSorgusu = "SELECT DATEDIFF(CURDATE(), iade_tarihi) FROM islemler WHERE islem_id = ?";
        
        // eğer ki iade tarihi bugünün tarihinden önceyse gerekli cezayı uyeler tablosunda üyenin ceza_miktari na ekler.
        String cezaGuncellemeSorgusu = "UPDATE uyeler SET ceza_miktari = ceza_miktari + ? WHERE uye_id = ?";

        try {
            Connection conn = Baglanti.baglan();

            // Kitap durumunu güncelle
            PreparedStatement psKitap = conn.prepareStatement(kitapGuncelleSorgusu);
            psKitap.setInt(1, kitapId);
            psKitap.executeUpdate();
            
            // Üyenin sahip olduğu kitap sayısını bir azalt
            PreparedStatement psSayi = conn.prepareStatement(uyeninKitapSayisiniAzaltmaSorgusu);
            psSayi.setInt(1, uyeId);
            psSayi.executeUpdate();

            // Gün farkını hesapla
            PreparedStatement psGunFarki = conn.prepareStatement(gunFarkiSorgusu);
            psGunFarki.setInt(1, islemId);
            ResultSet rsGunFarki = psGunFarki.executeQuery();

            int gunFarki = 0;
            if (rsGunFarki.next()) {
                gunFarki = rsGunFarki.getInt(1);
            }

            // Ceza miktarını hesapla ve güncelle
            if (gunFarki > 0) { // Eğer gün farkı pozitifse ceza uygula
                int cezaMiktari = gunFarki * 5; // Her gün için 5 lira ceza
                System.out.println("Hesaplanan Ceza Miktarı: " + cezaMiktari);
                PreparedStatement psCeza = conn.prepareStatement(cezaGuncellemeSorgusu);
                psCeza.setInt(1, cezaMiktari);
                psCeza.setInt(2, uyeId);
                psCeza.executeUpdate();
            }

            // İşlemi güncelle
            PreparedStatement psIslem = conn.prepareStatement(islemGuncelleSorgusu);
            psIslem.setInt(1, islemId);
            psIslem.executeUpdate();
            
            JOptionPane.showMessageDialog(this, "Kitap iade alma işlemi başarılı!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            islemleriGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KitapIadeAlmaEkrani().setVisible(true));
    }
}
