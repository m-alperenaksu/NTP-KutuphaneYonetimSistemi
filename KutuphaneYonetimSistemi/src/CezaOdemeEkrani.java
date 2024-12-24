// Üyelerin ceza ödemelerini almak için kullanılan ekran.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.sql.*;

public class CezaOdemeEkrani extends JFrame {
    private JTable uyeTablosu;
    private DefaultTableModel uyeModel;
    private JButton cezaOdeButonu;
    private JTextField aramaCubugu;
    private JComboBox<String> filtreSecimi;

    public CezaOdemeEkrani() {
        setTitle("Kütüpedia - Ceza Ödeme Ekranı");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        //Logo Icon
        ImageIcon iconOdeme = new ImageIcon("src/resimler/odeme.png"); 
        JLabel iconOdemeLabel = new JLabel(iconOdeme);
        iconOdemeLabel.setBounds(585, 20, 40, 40); 
        add(iconOdemeLabel);


        // Başlık Alanı
        JLabel baslikLabel = new JLabel("CEZA ÖDEMESİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 24));
        baslikLabel.setBounds(550, 20, 400, 40);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        // Üyeler Tablosu
        uyeModel = new DefaultTableModel(new String[]{"Üye ID", "TC Kimlik No", "Ad", "Soyad", "Ceza Miktarı", "Üyelik Türü", "Üyelik Tarihi"}, 0);
        uyeTablosu = new JTable(uyeModel);
        JScrollPane scrollPane = new JScrollPane(uyeTablosu);
        scrollPane.setBounds(270, 120, 940, 400);
        add(scrollPane);
        
        // Arama Alanı
        JLabel aramaLabel = new JLabel("Arama:");
        aramaLabel.setBounds(840, 65, 100, 30);
        add(aramaLabel);

        filtreSecimi = new JComboBox<>(new String[]{"Üye ID", "Ad-Soyad"});
        filtreSecimi.setBounds(1060, 65, 150, 30);
        add(filtreSecimi);

        aramaCubugu = new JTextField();
        aramaCubugu.setBounds(895, 65, 150, 30);
        add(aramaCubugu);

        //Arama çubuğuna herhangi bir karakter girildiğinde arama işlemini yapacak.
        aramaCubugu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = aramaCubugu.getText().trim();
                String secilenKriter = filtreSecimi.getSelectedItem().toString();
                uyeListesiniAra(secilenKriter, aramaMetni);
            }
        });

        // Ceza Ödeme Butonu
        cezaOdeButonu = new JButton("Ceza Ödemesini Al");
        cezaOdeButonu.setBounds(650, 550, 200, 40);
        cezaOdeButonu.setBackground(Color.decode("#007BFF"));
        cezaOdeButonu.setForeground(Color.WHITE);
        add(cezaOdeButonu);

        cezaOdeButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                cezaOde();
            }
        });

        // Verileri Yükle
        uyeleriGetir();
    }

    private void uyeleriGetir() {
        uyeModel.setRowCount(0);
        String sorgu = "SELECT uye_id, tc_kimlik_no, ad, soyad, ceza_miktari, uyelik_turu, uye_tarihi FROM uyeler WHERE ceza_miktari > 0";
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
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));
                uye.setUyeTarihi(rs.getDate("uye_tarihi"));

                uyeModel.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getCezaMiktari(),
                    uye.getUyelikTuru(),
                    uye.getUyeTarihi()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }
    
    private void uyeListesiniAra(String kriter, String aramaMetni) {
        uyeModel.setRowCount(0);
        String sutun = "";
        switch (kriter) { // Seçilen kriterden veritabanı tablosundaki ismini bulur.
            case "Üye ID": sutun = "uye_id"; break;
            case "Ad-Soyad": sutun = "CONCAT(ad,' ',soyad)"; break;
        }

        String sorgu = "SELECT * FROM uyeler WHERE " + sutun + " LIKE ? AND ceza_miktari > 0";
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
                uye.setCezaMiktari(rs.getInt("ceza_miktari"));
                uye.setUyelikTuru(rs.getString("uyelik_turu"));
                uye.setUyeTarihi(rs.getDate("uye_tarihi"));

                String cezaMiktariYazisi = uye.getCezaMiktari() + " TL";
                uyeModel.addRow(new Object[]{
                    uye.getUyeId(),
                    uye.getTcKimlikNo(),
                    uye.getAd(),
                    uye.getSoyad(),
                    uye.getCezaMiktari(),
                    uye.getUyelikTuru(),
                    uye.getUyeTarihi()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void cezaOde() {
        int seciliSatir = uyeTablosu.getSelectedRow();
        if (seciliSatir == -1) {
            JOptionPane.showMessageDialog(this, "Lütfen bir üye seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int uyeId = (int) uyeModel.getValueAt(seciliSatir, 0);
        String sorgu = "UPDATE uyeler SET ceza_miktari = 0 WHERE uye_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);

            ps.setInt(1, uyeId);
            ps.executeUpdate();

            JOptionPane.showMessageDialog(this, "Ceza başarıyla ödendi!", "Başarılı", JOptionPane.INFORMATION_MESSAGE);
            uyeleriGetir();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new CezaOdemeEkrani().setVisible(true));
    }
}
