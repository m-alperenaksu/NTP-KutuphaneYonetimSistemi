// Kitapları uygun kullanıcılara ödünç vermek için kullanılan ekran.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KitapOduncVermeEkrani extends JFrame {
    private JTable kitapTablosu, uyeTablosu;
    private DefaultTableModel kitapModel, uyeModel;
    private JTextField kitapAramaAlani, uyeAramaAlani;
    private JButton oduncVerButonu;
    private JComboBox<String> kitapAramaKriteri, uyeAramaKriteri, oduncGunSayisiSecimi;

    public KitapOduncVermeEkrani() {
        setTitle("Kütüpedia - Kitap Ödünç Verme Ekranı");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        // Logo Icon
        ImageIcon iconOdunc = new ImageIcon("src/resimler/odunc.png"); 
        JLabel iconOduncLabel = new JLabel(iconOdunc);
        iconOduncLabel.setBounds(545, 20, 40, 40); 
        add(iconOduncLabel);

        // Başlık Alanı
        JLabel baslikLabel = new JLabel("ÖDÜNÇ VERME İŞLEMİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 24));
        baslikLabel.setBounds(550, 20, 400, 40);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        // Kitap Tablosu ve Arama
        JLabel kitapAramaLabel = new JLabel("Kitap Arama:");
        kitapAramaLabel.setBounds(370, 90, 100, 30);
        add(kitapAramaLabel);

        kitapAramaAlani = new JTextField();
        kitapAramaAlani.setBounds(450, 90, 150, 30);
        add(kitapAramaAlani);
        
        // Kitapların hangi kritere göre arama işlemi yapılacağını seçilmesi
        kitapAramaKriteri = new JComboBox<>(new String[]{"Kitap ID", "ISBN", "Ad", "Yazar"});
        kitapAramaKriteri.setBounds(610, 90, 120, 30);
        add(kitapAramaKriteri);

        // Kitap Tablosu
        kitapModel = new DefaultTableModel(new String[]{"Kitap ID", "ISBN", "Ad", "Yazar", "Durum"}, 0);
        kitapTablosu = new JTable(kitapModel);
        kitapTablosu.setAutoCreateRowSorter(true);
        JScrollPane kitapScrollPane = new JScrollPane(kitapTablosu);
        kitapScrollPane.setBounds(170, 140, 560, 350);
        add(kitapScrollPane);

        // Kitap arama çubuğuna herhangi bir karakter yazıldığında otomatik çalışan KeyListener
        kitapAramaAlani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = kitapAramaAlani.getText().trim();
                String secilenKriter = kitapAramaKriteri.getSelectedItem().toString();
                kitaplariAra(secilenKriter, aramaMetni);
            }
        });

        // Üye Tablosu ve Arama
        JLabel uyeAramaLabel = new JLabel("Üye Arama:");
        uyeAramaLabel.setBounds(990, 90, 100, 30);
        add(uyeAramaLabel);

        uyeAramaAlani = new JTextField();
        uyeAramaAlani.setBounds(1070, 90, 150, 30);
        add(uyeAramaAlani);
        
        // Kitapların hangi kritere göre arama işlemi yapılacağını seçilmesi
        uyeAramaKriteri = new JComboBox<>(new String[]{"Üye ID", "Ad-Soyad"});
        uyeAramaKriteri.setBounds(1235, 90, 120, 30);
        add(uyeAramaKriteri);
        
        // Üye Tablosu
        uyeModel = new DefaultTableModel(new String[]{"Üye ID", "TC Kimlik No", "Ad", "Soyad","Kitap Sayısı","Ceza Miktarı", "Üyelik Türü"}, 0);
        uyeTablosu = new JTable(uyeModel);
        uyeTablosu.setAutoCreateRowSorter(true);
        JScrollPane uyeScrollPane = new JScrollPane(uyeTablosu);
        uyeScrollPane.setBounds(755, 140, 600, 350);
        add(uyeScrollPane);
        
        // Kitap arama çubuğuna herhangi bir karakter yazıldığında otomatik çalışan KeyListener
        uyeAramaAlani.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = uyeAramaAlani.getText().trim();
                String secilenKriter = uyeAramaKriteri.getSelectedItem().toString();
                uyeleriAra(secilenKriter, aramaMetni);
            }
        });
        
        //Ödünç verilecek gün sayısının seçimi
        oduncGunSayisiSecimi = new JComboBox<>(new String[]{"15 Gün", "30 Gün", "45 Gün"});
        oduncGunSayisiSecimi.setBounds(565, 525, 100, 30);
        add(oduncGunSayisiSecimi);

        // Ödünç Ver Butonu
        oduncVerButonu = new JButton("Ödünç Ver");
        oduncVerButonu.setBounds(700, 520, 200, 40);
        oduncVerButonu.setBackground(Color.decode("#28A745"));
        oduncVerButonu.setForeground(Color.WHITE);
        add(oduncVerButonu);

        oduncVerButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                oduncVer();
            }
        });

        // Ekran ilk açıldığında kitaplar ve üyeler tablolarının doldurulması
        kitaplariGetir();
        uyeleriGetir();
    }

    private void kitaplariGetir() {
        kitapModel.setRowCount(0);
        String sorgu = "SELECT kitap_id, isbn, ad, yazar, durum FROM kitaplar WHERE durum = 'rafta'";
        try {
            Connection conn = Baglanti.baglan();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            while (rs.next()) {
                Kitap kitap = new Kitap();
                kitap.setKitapId(rs.getInt("kitap_id"));
                kitap.setIsbn(rs.getString("isbn"));
                kitap.setAd(rs.getString("ad"));
                kitap.setYazar(rs.getString("yazar"));
                kitap.setDurum(rs.getString("durum"));

                String durumYazisi = kitap.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                kitapModel.addRow(new Object[]{
                    kitap.getKitapId(),
                    kitap.getIsbn(),
                    kitap.getAd(),
                    kitap.getYazar(),
                    durumYazisi
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void uyeleriGetir() {
        uyeModel.setRowCount(0);
        String sorgu = "SELECT uye_id, tc_kimlik_no, ad, soyad, kitap_sayisi, ceza_miktari, uyelik_turu FROM uyeler";
        try {
            Connection conn = Baglanti.baglan();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);
            while (rs.next()) {
                Uye uye = new Uye();
                uye.setUyeId(rs.getInt("uye_id"));
                uye.setTcKimlikNo(rs.getString("tc_kimlik_no"));
                uye.setAd(rs.getString("ad"));
                uye.setSoyad(rs.getString("soyad"));
                uye.setKitapSayisi(rs.getInt("kitap_sayisi"));
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));

                String cezaMiktariYazisi = uye.getCezaMiktari() + " TL";
                uyeModel.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getKitapSayisi(),
                    cezaMiktariYazisi,
                    uye.getUyelikTuru()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void kitaplariAra(String kriter, String aramaMetni) {
        kitapModel.setRowCount(0);
        String sutun;
        switch (kriter) {
            case "Kitap ID":
                sutun = "kitap_id";
                break;
            case "ISBN":
                sutun = "isbn";
                break;
            case "Ad":
                sutun = "ad";
                break;
            case "Yazar":
                sutun = "yazar";
                break;
            default:
                sutun = "ad";
        }
        String sorgu = "SELECT kitap_id, isbn, ad, yazar, durum FROM kitaplar WHERE " + sutun + " LIKE ? AND durum = 'rafta'";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, "%" + aramaMetni + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Kitap kitap = new Kitap();
                kitap.setKitapId(rs.getInt("kitap_id"));
                kitap.setIsbn(rs.getString("isbn"));
                kitap.setAd(rs.getString("ad"));
                kitap.setYazar(rs.getString("yazar"));
                kitap.setDurum(rs.getString("durum"));

                String durumYazisi = kitap.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                kitapModel.addRow(new Object[]{
                    kitap.getKitapId(),
                    kitap.getIsbn(),
                    kitap.getAd(),
                    kitap.getYazar(),
                    durumYazisi
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void uyeleriAra(String kriter, String aramaMetni) {
        uyeModel.setRowCount(0);
        String sutun = kriter.equals("Üye ID") ? "uye_id" : "CONCAT(ad, ' ', soyad)"; // CONCAT fonksiyonunu kullanarak ad ve soyadı birleştirerek arama yapabildik.
        String sorgu = "SELECT uye_id, tc_kimlik_no, ad, soyad, ceza_miktari, kitap_sayisi, uyelik_turu FROM uyeler WHERE " + sutun + " LIKE ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, "%" + aramaMetni + "%");
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Uye uye = new Uye();
                uye.setUyeId(rs.getInt("uye_id"));
                uye.setTcKimlikNo(rs.getString("tc_kimlik_no"));
                uye.setAd(rs.getString("ad"));
                uye.setSoyad(rs.getString("soyad"));
                uye.setKitapSayisi(rs.getInt("kitap_sayisi"));
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));

                String cezaMiktariYazisi = uye.getCezaMiktari() + " TL";
                uyeModel.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getKitapSayisi(),
                    cezaMiktariYazisi,
                    uye.getUyelikTuru()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void oduncVer() {
        int seciliKitap = kitapTablosu.getSelectedRow();
        int seciliUye = uyeTablosu.getSelectedRow();
        
        
        if (seciliKitap == -1 || seciliUye == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir kitap ve bir üye seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kitapId = (int) kitapModel.getValueAt(seciliKitap, 0);
        String kitapAd = (String) kitapModel.getValueAt(seciliKitap, 2);
        int uyeId = (int) uyeModel.getValueAt(seciliUye, 0);
        String uyeAdSoyad = uyeModel.getValueAt(seciliUye, 2) + " " + uyeModel.getValueAt(seciliUye, 3);
        String oduncGunSayisi = oduncGunSayisiSecimi.getSelectedItem().toString();
        String uyelikTuru = uyeTablosu.getValueAt(seciliUye, 6).toString().trim();
        
        // Üyelik türünün ödünç alınmak istenen gün sayısıyla uyumunun kontrolü
        if((uyelikTuru.equals("Halk")) && (!oduncGunSayisi.equals("15 Gün") )) {
            JOptionPane.showMessageDialog(this, "Halk üyelik türündeki üyeler en fazla 15 gün ödünç alabilir.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        else if((uyelikTuru.equals("Öğrenci")) && (oduncGunSayisi.equals("45 Gün"))) {
            JOptionPane.showMessageDialog(this, "Öğrenci üyelik türündeki üyeler en fazla 30 gün ödünç alabilir.", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        //Üyenin halihazırda iade tarihini geciktirmiş olduğu bir kitabın olup olmadığının kontrolü
        String kontrolSorgusu1 = "SELECT COUNT(*) FROM islemler WHERE uye_id = ? AND durum = 'aktif' AND iade_tarihi < NOW()";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement psKontrol = conn.prepareStatement(kontrolSorgusu1);

            psKontrol.setInt(1, uyeId);
            ResultSet rs = psKontrol.executeQuery();

            if (rs.next() && rs.getInt(1) > 0) {
                JOptionPane.showMessageDialog(this, "Bu üyenin gecikmiş kitabı bulunmaktadır. Yeni kitap ödünç verilemez!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            return;
        }
        
        
        // Üyenin aynı anda en fazla 2 kitaba sahip olabileceğinin kontrolü
        String kontrolSorgusu2 = "SELECT kitap_sayisi FROM uyeler WHERE uye_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement psKontrol = conn.prepareStatement(kontrolSorgusu2);

            psKontrol.setInt(1, uyeId);
            ResultSet rs = psKontrol.executeQuery();

            if (rs.next() && rs.getInt("kitap_sayisi") >= 2) {
                JOptionPane.showMessageDialog(this, "Bir üye aynı anda en fazla 2 kitap ödünç alabilir!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            return;
        }
        
        // Üyenin ödenmemiş cezasının olup olmadığı kontrolü
        String kontrolSorgusu3 = "SELECT ceza_miktari FROM uyeler WHERE uye_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement psKontrol = conn.prepareStatement(kontrolSorgusu3);

            psKontrol.setInt(1, uyeId);
            ResultSet rs = psKontrol.executeQuery();

            if (rs.next() && rs.getInt("ceza_miktari") > 0) {
                JOptionPane.showMessageDialog(this, "Bu üyenin ödenmemiş cezası vardır. Cezasını ödeyene kadar kitap ödünç verilemez!", "Hata", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
            return;
        }
        
        // Eğer bir engel yoksa aşağıdaki işlemlerle ödünç verme işlemi gerçekleştirilir.
        
        // islemler tablosuna bu odunc islemi kaydedilir.
        String oduncSorgusu = "INSERT INTO islemler (uye_id, kitap_id, odunc_tarihi, iade_tarihi, durum, uye_ad_soyad, kitap_ad) ";
        if(oduncGunSayisi.equals("15 Gün")) {
            oduncSorgusu += "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 15 DAY), 'aktif', ?, ?)";
        }
        else if(oduncGunSayisi.equals("30 Gün")) {
            oduncSorgusu += "VALUES (?, ?, NOW(), DATE_ADD(NOW(), INTERVAL 30 DAY), 'aktif', ?, ?)";
        }
        else{
            oduncSorgusu += "VALUES (?, ?, NOW(), DATE_ADD(NOW() , INTERVAL 45 DAY), 'aktif', ?, ?)";
        }
        
        // kitaplar tablosunda kitabın durumu odunc seklinde kaydedilir.
        String kitapDurumGuncelleSorgusu = "UPDATE kitaplar SET durum = 'odunc' WHERE kitap_id = ?";
        
        // uyler tablosunda ödünç alan üyenin kitap_sayisi 1 arttırılır.
        String uyeninKitapSayisiniArtirmaSorgusu = "UPDATE uyeler Set kitap_sayisi = kitap_sayisi + 1 WHERE uye_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement psOdunc = conn.prepareStatement(oduncSorgusu);
            PreparedStatement psDurum = conn.prepareStatement(kitapDurumGuncelleSorgusu);
            PreparedStatement psSayi = conn.prepareStatement(uyeninKitapSayisiniArtirmaSorgusu);


            psOdunc.setInt(1, uyeId);
            psOdunc.setInt(2, kitapId);
            psOdunc.setString(3, uyeAdSoyad);
            psOdunc.setString(4, kitapAd);
            psOdunc.executeUpdate();

            psDurum.setInt(1, kitapId);
            psDurum.executeUpdate();
            
            psSayi.setInt(1,uyeId);
            psSayi.executeUpdate();


            JOptionPane.showMessageDialog(this, "Ödünç verme işlemi başarılı!");
            kitaplariGetir();
            uyeleriGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KitapOduncVermeEkrani().setVisible(true));
    }
}
