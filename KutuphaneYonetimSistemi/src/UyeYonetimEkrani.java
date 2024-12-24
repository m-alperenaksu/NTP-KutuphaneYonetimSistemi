// Üye Yönetim Ekranı. Üye bilgilerini görüntülemek, yen üye eklemek, varolan üye bilgilerini güncellemek ve üye silmek için kullanılır.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class UyeYonetimEkrani extends JFrame {
    private JTable uyeTablosu;
    private DefaultTableModel model;
    private JTextField adAlani, soyadAlani, epostaAlani, telefonAlani, tcKimlikNoAlani, aramaCubugu;
    private JComboBox<String> filtreSecimi, uyelikTuruSecimi;
    private JButton ekleButonu, guncelleButonu, silButonu;
    private JLabel toplamUyeSayisiLabel;

    public UyeYonetimEkrani() {
        setTitle("Kütüpedia - Üye Yönetim Sistemi");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        //Logo İcon
        ImageIcon iconUye = new ImageIcon("src/resimler/uye.png"); 
        JLabel iconUyeLabel = new JLabel(iconUye);
        iconUyeLabel.setBounds(540, 20, 40, 40);
        add(iconUyeLabel);

        // Başlık Alanı
        JLabel baslikLabel = new JLabel("ÜYE YÖNETİM SİSTEMİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 24));
        baslikLabel.setBounds(550, 20, 400, 40);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        // Üye Tablosu
        model = new DefaultTableModel(new String[]{"Üye ID", "TC Kimlik No", "Ad", "Soyad", "E-posta", "Telefon","Kitap Sayısı","Ceza Miktarı","Üyelik Türü","Üyelik Tarihi"}, 0);
        uyeTablosu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(uyeTablosu);
        scrollPane.setBounds(270, 110, 950, 300);
        add(scrollPane);

        // Tabloyu sütunlara göre küçükten büyüğe ya da büyükten küçüğe sıralama özelliğini aktive ettim.
        uyeTablosu.setAutoCreateRowSorter(true);

        // Arama Alanı
        JLabel aramaLabel = new JLabel("Arama:");
        aramaLabel.setBounds(840, 65, 100, 30);
        add(aramaLabel);

        aramaCubugu = new JTextField();
        aramaCubugu.setBounds(910, 65, 150, 30);
        add(aramaCubugu);

        // Aramanın hangi kritere göre yapılacağının belirlendiği kısım
        filtreSecimi = new JComboBox<>(new String[]{"Üye ID", "TC Kimlik No", "Ad-Soyad", "E-posta", "Telefon"});
        filtreSecimi.setBounds(1070, 65, 150, 30);
        add(filtreSecimi);
        
        
        //Arama çubuğuna herhangi bir karakter girildiğinde arama işlemini yapacak.
        aramaCubugu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = aramaCubugu.getText().trim();
                String secilenKriter = filtreSecimi.getSelectedItem().toString();
                uyeListesiniAra(secilenKriter, aramaMetni);
            }
        });
        
        toplamUyeSayisiLabel = new JLabel("Toplam Üye Sayısı: 0");
        toplamUyeSayisiLabel.setBounds(1000, 420, 200, 30);
        toplamUyeSayisiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        toplamUyeSayisiLabel.setForeground(Color.decode("#2E3B4E"));
        add(toplamUyeSayisiLabel);

        // Form Alanları
        JLabel tcKimlikNoLabel = new JLabel("TC Kimlik No:");
        tcKimlikNoLabel.setBounds(270, 460, 120, 30);
        add(tcKimlikNoLabel);

        tcKimlikNoAlani = new JTextField();
        tcKimlikNoAlani.setBounds(390, 460, 200, 30);
        add(tcKimlikNoAlani);

        JLabel adLabel = new JLabel("Ad:");
        adLabel.setBounds(270, 500, 120, 30);
        add(adLabel);

        adAlani = new JTextField();
        adAlani.setBounds(390, 500, 200, 30);
        add(adAlani);

        JLabel soyadLabel = new JLabel("Soyad:");
        soyadLabel.setBounds(270, 540, 120, 30);
        add(soyadLabel);

        soyadAlani = new JTextField();
        soyadAlani.setBounds(390, 540, 200, 30);
        add(soyadAlani);

        JLabel epostaLabel = new JLabel("E-posta:");
        epostaLabel.setBounds(270, 580, 120, 30);
        add(epostaLabel);

        epostaAlani = new JTextField();
        epostaAlani.setBounds(390, 580, 200, 30);
        add(epostaAlani);

        JLabel telefonLabel = new JLabel("Telefon:");
        telefonLabel.setBounds(270, 620, 120, 30);
        add(telefonLabel);

        telefonAlani = new JTextField();
        telefonAlani.setBounds(390, 620, 200, 30);
        add(telefonAlani);
        
        JLabel uyelikTuruLabel = new JLabel("Üyelik Türü:");
        uyelikTuruLabel.setBounds(625, 460, 120, 30);
        add(uyelikTuruLabel);
        
        uyelikTuruSecimi = new JComboBox<>(new String[]{"Halk", "Öğrenci", "Akademisyen"});
        uyelikTuruSecimi.setBounds(725, 460, 150, 30);
        add(uyelikTuruSecimi);
        

        // Ekle Butonu
        ekleButonu = new JButton("Ekle");
        ekleButonu.setBounds(1000, 460, 150, 40);
        ekleButonu.setBackground(Color.decode("#007BFF"));
        ekleButonu.setForeground(Color.WHITE);
        add(ekleButonu);
        
        // Ekle Butonu için ActionListener
        ekleButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uyeEkle();
            }
        });
        
        // Güncelle Butonu
        guncelleButonu = new JButton("Güncelle");
        guncelleButonu.setBounds(1000, 520, 150, 40);
        guncelleButonu.setBackground(Color.decode("#28A745"));
        guncelleButonu.setForeground(Color.WHITE);
        add(guncelleButonu);
        
        // Güncelle Butonu için ActionListener
        guncelleButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uyeGuncelle();
            }
        });
        
        
        // Sil Butonu
        silButonu = new JButton("Sil");
        silButonu.setBounds(1000, 580, 150, 40);
        silButonu.setBackground(Color.decode("#DC3545"));
        silButonu.setForeground(Color.WHITE);
        add(silButonu);

        // Sil Butonu için ActionListener
        silButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                uyeSil();
            }
        });

        // Tablodaki herhangi bir üyeye tıklandığında o üyenin bilgilerinin ilgili formlara otomatik gelmesi için MouseListener
        uyeTablosu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                uyeBilgileriniDoldur();
            }
        });

        // Ekran ilk açıldığında tabloda üyelerin listelenmesi için çağrılır.
        uyeListesiniGetir();
    }
    
    private void uyeListesiniGetir() {
        model.setRowCount(0);
        String sorgu = "SELECT * FROM uyeler";
        try{
        	Connection conn = Baglanti.baglan();
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sorgu);

            while (rs.next()) {
                Uye uye = new Uye();
                uye.setUyeId(rs.getInt("uye_id"));
                uye.setTcKimlikNo(rs.getString("tc_kimlik_no"));
                uye.setAd(rs.getString("ad"));
                uye.setSoyad(rs.getString("soyad"));
                uye.setEposta(rs.getString("eposta"));
                uye.setTelefon(rs.getString("telefon"));
                uye.setKitapSayisi(rs.getInt("kitap_sayisi"));
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));
                uye.setUyeTarihi(rs.getDate("uye_tarihi"));

                String cezaMiktariYazisi = uye.getCezaMiktari() + " TL";
                model.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getEposta(),
                    uye.getTelefon(),
                    uye.getKitapSayisi(),
                    cezaMiktariYazisi,
                    uye.getUyelikTuru(),
                    uye.getUyeTarihi()
                });
            }

            toplamUyeSayisiniGuncelle();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }
    private void toplamUyeSayisiniGuncelle() {
        toplamUyeSayisiLabel.setText("Toplam Üye Sayısı: " + model.getRowCount());
    }
    
    private void uyeBilgileriniDoldur() {
        int secilenSatir = uyeTablosu.getSelectedRow();
        if (secilenSatir != -1) { // Herhangi bir satıra tıklanmışsa kod bloğu çalışır.
            tcKimlikNoAlani.setText(model.getValueAt(secilenSatir, 1).toString());
            adAlani.setText(model.getValueAt(secilenSatir, 2).toString());
            soyadAlani.setText(model.getValueAt(secilenSatir, 3).toString());
            epostaAlani.setText(model.getValueAt(secilenSatir, 4).toString());
            telefonAlani.setText(model.getValueAt(secilenSatir, 5).toString());
            uyelikTuruSecimi.setSelectedItem(model.getValueAt(secilenSatir, 8).toString());
        }
    }

    private void uyeListesiniAra(String kriter, String aramaMetni) {
        model.setRowCount(0);
        String sutun = "";
        switch (kriter) { // Seçilen kriterden veritabanı tablosundaki ismini bulur.
            case "Üye ID": sutun = "uye_id"; break;
            case "TC Kimlik No": sutun = "tc_kimlik_no"; break;
            case "Ad-Soyad": sutun = "CONCAT(ad,' ',soyad)"; break;
            case "E-posta": sutun = "eposta"; break;
            case "Telefon": sutun = "telefon"; break;
        }

        String sorgu = "SELECT * FROM uyeler WHERE " + sutun + " LIKE ?";
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
                uye.setEposta(rs.getString("eposta"));
                uye.setTelefon(rs.getString("telefon"));
                uye.setKitapSayisi(rs.getInt("kitap_sayisi"));
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));
                uye.setUyeTarihi(rs.getDate("uye_tarihi"));

                String cezaMiktariYazisi = uye.getCezaMiktari() + " TL";
                model.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getEposta(),
                    uye.getTelefon(),
                    uye.getKitapSayisi(),
                    cezaMiktariYazisi,
                    uye.getUyelikTuru(),
                    uye.getUyeTarihi()
                });
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }
    
    // Belirli bir verinin benzersiz olup olmadığını kontrol eden fonksiyon. Hem ekle hem de güncelle fonksiyonlardında kullanmak için tasarladım.
    private boolean benzersizMi(String sutun, String deger, int haricId) {
        String sorgu = "SELECT COUNT(*) FROM uyeler WHERE " + sutun + " = ?";
        if (haricId != -1) { // Fonksiyonu üye eklemek için kullanıyorsam haricId -1 değerine eşittir.
            sorgu += " AND uye_id != ?";
        }
        try {
        	Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, deger);
            if (haricId != -1) {
                ps.setInt(2, haricId);
            }
            ResultSet rs = ps.executeQuery();
            if (rs.next() && rs.getInt(1) > 0) {
                return false;
            }
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
        return true;
    }
    

    private void uyeEkle() {
        String ad = adAlani.getText().trim();
        String soyad = soyadAlani.getText().trim();
        String eposta = epostaAlani.getText().trim();
        String telefon = telefonAlani.getText().trim();
        String tcKimlikNo = tcKimlikNoAlani.getText().trim();
        String uyelikTuru = uyelikTuruSecimi.getSelectedItem().toString();

        if (ad.isEmpty() || soyad.isEmpty() || eposta.isEmpty() || telefon.isEmpty() || tcKimlikNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanlar doldurulmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tcKimlikNo.length() != 11 || !tcKimlikNo.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "TC Kimlik Numarası 11 haneli bir sayı olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!benzersizMi("tc_kimlik_no", tcKimlikNo, -1)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz TC Kimlik Numarası sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!benzersizMi("eposta", eposta, -1)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz E-posta sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!benzersizMi("telefon", telefon, -1)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz Telefon Numarası sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        
        if(!telefon.matches("\\d+")) {
        	JOptionPane.showMessageDialog(this, "Telefon Numarası sadece rakamlardan oluşmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sorgu = "INSERT INTO uyeler (ad, soyad, eposta, telefon, tc_kimlik_no, uyelik_turu) VALUES (?, ?, ?, ?, ?, ?)";
        try {
        	Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);

            ps.setString(1, ad);
            ps.setString(2, soyad);
            ps.setString(3, eposta);
            ps.setString(4, telefon);
            ps.setString(5, tcKimlikNo);
            ps.setString(6, uyelikTuru);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Üye başarıyla eklendi!");
            uyeListesiniGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }


    private void uyeGuncelle() {
        int secilenSatir = uyeTablosu.getSelectedRow();
        if (secilenSatir == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir üye seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String ad = adAlani.getText().trim();
        String soyad = soyadAlani.getText().trim();
        String eposta = epostaAlani.getText().trim();
        String telefon = telefonAlani.getText().trim();
        String tcKimlikNo = tcKimlikNoAlani.getText().trim();
        String uyelikTuru = uyelikTuruSecimi.getSelectedItem().toString();

        if (ad.isEmpty() || soyad.isEmpty() || eposta.isEmpty() || telefon.isEmpty() || tcKimlikNo.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tüm alanlar doldurulmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (tcKimlikNo.length() != 11 || !tcKimlikNo.matches("\\d+")) {
            JOptionPane.showMessageDialog(this, "TC Kimlik Numarası 11 haneli bir sayı olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int uyeId = (int) model.getValueAt(secilenSatir, 0);

        if (!benzersizMi("tc_kimlik_no", tcKimlikNo, uyeId)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz TC Kimlik Numarası sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!benzersizMi("eposta", eposta, uyeId)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz E-posta sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        if (!benzersizMi("telefon", telefon, uyeId)) {
            JOptionPane.showMessageDialog(this, "Girmiş olduğunuz Telefon Numarası sistemde önceden kayıtlıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        

        String sorgu = "UPDATE uyeler SET ad = ?, soyad = ?, eposta = ?, telefon = ?, tc_kimlik_no = ?, uyelik_turu = ? WHERE uye_id = ?";
        try {
        	
        	Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);

            ps.setString(1, ad);
            ps.setString(2, soyad);
            ps.setString(3, eposta);
            ps.setString(4, telefon);
            ps.setString(5, tcKimlikNo);
            ps.setString(6, uyelikTuru);
            ps.setInt(7, uyeId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Üye başarıyla güncellendi!");
            uyeListesiniGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }


    private void uyeSil() {
        int secilenSatir = uyeTablosu.getSelectedRow();
        if (secilenSatir == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir üye seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int uyeId = (int) model.getValueAt(secilenSatir, 0);

        String sorgu = "DELETE FROM uyeler WHERE uye_id = ?";
        try {
        	Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);

            ps.setInt(1, uyeId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Üye başarıyla silindi!");
            uyeListesiniGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new UyeYonetimEkrani().setVisible(true));
    }
}
