// Kitap Yönetim Ekranı. Kitapların bilgilerini görüntüleyebilir, yeni kitap ekleyebilir. kitapları düzenleyebilir ve silebilersiniz.

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

public class KitapYonetimEkrani extends JFrame {
    private JTable kitapTablosu;
    private DefaultTableModel model;
    private JTextField aramaCubugu, isbnAlani, adAlani, yazarAlani, yayineviAlani, kategoriAlani, yerAlani;
    private JComboBox<String> filtreSecimi;
    private JButton ekleButonu, guncelleButonu, silButonu;
    private JLabel toplamKitapSayisiLabel;

    public KitapYonetimEkrani() {
        setTitle("Kütüpedia - Kitap Yönetim Sistemi");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        //Logo Icon
        ImageIcon iconKitap = new ImageIcon("src/resimler/kitap.png");
        JLabel iconKitapLabel = new JLabel(iconKitap);
        iconKitapLabel.setBounds(540, 20, 40, 40); 
        add(iconKitapLabel);

        // Başlık Alanı
        JLabel baslikLabel = new JLabel("KİTAP YÖNETİM SİSTEMİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 24));
        baslikLabel.setBounds(550, 20, 400, 40);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        // Kitap Tablosu
        model = new DefaultTableModel(new String[]{"Kitap ID", "ISBN", "Ad", "Yazar", "Yayınevi", "Kategori", "Yer", "Durum", "Eklenme Tarihi"}, 0);
        kitapTablosu = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(kitapTablosu);
        scrollPane.setBounds(270, 110, 950, 300);
        add(scrollPane);

        // Tablo sıralama özelliği
        kitapTablosu.setAutoCreateRowSorter(true);

        // Arama Alanı
        JLabel aramaLabel = new JLabel("Arama:");
        aramaLabel.setBounds(840, 65, 100, 30);
        add(aramaLabel);

        aramaCubugu = new JTextField();
        aramaCubugu.setBounds(910, 65, 150, 30);
        add(aramaCubugu);

        // Aramanın hangi kritere göre seçileceğinin belirlendiği yer.
        filtreSecimi = new JComboBox<>(new String[]{"Kitap ID", "ISBN", "Ad", "Yazar", "Yayınevi", "Kategori"});
        filtreSecimi.setBounds(1070, 65, 150, 30);
        add(filtreSecimi);

        // Arama çubuğuna herhangir bir karakter girildiğinde arama işlemi gerçekleşir.
        aramaCubugu.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                String aramaMetni = aramaCubugu.getText().trim();
                String secilenKriter = filtreSecimi.getSelectedItem().toString();
                kitapListesiniAra(secilenKriter, aramaMetni);
            }
        });
        
        // Toplam kitap sayısı
        toplamKitapSayisiLabel = new JLabel("Toplam Kitap Sayısı: 0");
        toplamKitapSayisiLabel.setBounds(1020, 420, 200, 30);
        toplamKitapSayisiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        toplamKitapSayisiLabel.setForeground(Color.decode("#2E3B4E"));
        add(toplamKitapSayisiLabel);

        // Form Alanları
        JLabel isbnLabel = new JLabel("ISBN:");
        isbnLabel.setBounds(270, 430, 120, 30);
        add(isbnLabel);

        isbnAlani = new JTextField();
        isbnAlani.setBounds(390, 430, 200, 30);
        add(isbnAlani);

        JLabel adLabel = new JLabel("Ad:");
        adLabel.setBounds(270, 470, 120, 30);
        add(adLabel);

        adAlani = new JTextField();
        adAlani.setBounds(390, 470, 200, 30);
        add(adAlani);

        JLabel yazarLabel = new JLabel("Yazar:");
        yazarLabel.setBounds(270, 510, 120, 30);
        add(yazarLabel);

        yazarAlani = new JTextField();
        yazarAlani.setBounds(390, 510, 200, 30);
        add(yazarAlani);

        JLabel yayineviLabel = new JLabel("Yayınevi:");
        yayineviLabel.setBounds(270, 550, 120, 30);
        add(yayineviLabel);

        yayineviAlani = new JTextField();
        yayineviAlani.setBounds(390, 550, 200, 30);
        add(yayineviAlani);

        JLabel kategoriLabel = new JLabel("Kategori:");
        kategoriLabel.setBounds(270, 590, 120, 30);
        add(kategoriLabel);

        kategoriAlani = new JTextField();
        kategoriAlani.setBounds(390, 590, 200, 30);
        add(kategoriAlani);

        JLabel yerLabel = new JLabel("Yer:");
        yerLabel.setBounds(270, 630, 120, 30);
        add(yerLabel);

        yerAlani = new JTextField();
        yerAlani.setBounds(390, 630, 200, 30);
        add(yerAlani);

        // Butonlar
        ekleButonu = new JButton("Ekle");
        ekleButonu.setBounds(650, 450, 150, 40);
        ekleButonu.setBackground(Color.decode("#007BFF"));
        ekleButonu.setForeground(Color.WHITE);
        add(ekleButonu);
        
        // Ekle Butonu için ActionListener
        ekleButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kitapEkle();
            }
        });

        guncelleButonu = new JButton("Güncelle");
        guncelleButonu.setBounds(650, 510, 150, 40);
        guncelleButonu.setBackground(Color.decode("#28A745"));
        guncelleButonu.setForeground(Color.WHITE);
        add(guncelleButonu);
        
        // Güncelle Butonu için ActionListener
        guncelleButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kitapGuncelle(); 
            }
        });

        silButonu = new JButton("Sil");
        silButonu.setBounds(650, 570, 150, 40);
        silButonu.setBackground(Color.decode("#DC3545"));
        silButonu.setForeground(Color.WHITE);
        add(silButonu);
        
        // Sil Butonu için ActionListener
        silButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                kitapSil(); 
            }
        });
        
        // Tablodaki herhangi bir alana tıklandığına bilgilerin ilgili form alanlarına doldurulması için MouseListener.
        kitapTablosu.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                kitapBilgileriniDoldur();
            }
        });

        // Ekran ilk açıldığına tabloya kitapların getirilmesi
        kitapListesiniGetir();
    }
    
    private void kitapListesiniGetir() {
        model.setRowCount(0);
        String sorgu = "SELECT * FROM kitaplar";
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
                kitap.setYayinEvi(rs.getString("yayin_evi"));
                kitap.setKategori(rs.getString("kategori"));
                kitap.setYer(rs.getString("yer"));
                kitap.setDurum(rs.getString("durum"));
                kitap.setEklenmeTarihi(rs.getDate("eklenme_tarihi"));

                String durumYazisi = kitap.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                model.addRow(new Object[]{
                    kitap.getKitapId(),
                    kitap.getIsbn(),
                    kitap.getAd(),
                    kitap.getYazar(),
                    kitap.getYayinEvi(),
                    kitap.getKategori(),
                    kitap.getYer(),
                    durumYazisi,
                    kitap.getEklenmeTarihi()
                });
            }

            toplamKitapSayisiniGuncelle();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }
    
    
    private void kitapBilgileriniDoldur() {
        int secilenSatir = kitapTablosu.getSelectedRow();
        if (secilenSatir != -1) {
            isbnAlani.setText(model.getValueAt(secilenSatir, 1).toString());
            adAlani.setText(model.getValueAt(secilenSatir, 2).toString());
            yazarAlani.setText(model.getValueAt(secilenSatir, 3).toString());
            yayineviAlani.setText(model.getValueAt(secilenSatir, 4).toString());
            kategoriAlani.setText(model.getValueAt(secilenSatir, 5).toString());
            yerAlani.setText(model.getValueAt(secilenSatir, 6).toString());
        }
    }
    
    
    // Tablodaki satır sayısına göre toplam kitap sayısını belirler ve günceller.
    private void toplamKitapSayisiniGuncelle() {
        toplamKitapSayisiLabel.setText("Toplam Kitap Sayısı: " + model.getRowCount());
    }
    
    private void kitapListesiniAra(String kriter, String aramaMetni) {
        model.setRowCount(0);
        String sutun = "";
        switch (kriter) {
            case "Kitap ID": sutun = "kitap_id"; break;
            case "ISBN": sutun = "isbn"; break;
            case "Ad": sutun = "ad"; break;
            case "Yazar": sutun = "yazar"; break;
            case "Yayınevi": sutun = "yayin_evi"; break;
            case "Kategori": sutun = "kategori"; break;
        }

        String sorgu = "SELECT * FROM kitaplar WHERE " + sutun + " LIKE ?";
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
                kitap.setYayinEvi(rs.getString("yayin_evi"));
                kitap.setKategori(rs.getString("kategori"));
                kitap.setYer(rs.getString("yer"));
                kitap.setDurum(rs.getString("durum"));
                kitap.setEklenmeTarihi(rs.getDate("eklenme_tarihi"));

                String durumYazisi = kitap.getDurum().equals("rafta") ? "Rafta" : "Ödünç alınmış";
                model.addRow(new Object[]{
                    kitap.getKitapId(),
                    kitap.getIsbn(),
                    kitap.getAd(),
                    kitap.getYazar(),
                    kitap.getYayinEvi(),
                    kitap.getKategori(),
                    kitap.getYer(),
                    durumYazisi,
                    kitap.getEklenmeTarihi()
                });
            }

        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage());
        }
    }

    private void kitapEkle() {
        String isbn = isbnAlani.getText().trim();
        String ad = adAlani.getText().trim();
        String yazar = yazarAlani.getText().trim();
        String yayinevi = yayineviAlani.getText().trim();
        String kategori = kategoriAlani.getText().trim();
        String yer = yerAlani.getText().trim();

        String sorgu = "INSERT INTO kitaplar (isbn, ad, yazar, yayin_evi, kategori, yer) VALUES (?, ?, ?, ?, ?, ?)";
        try{
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, isbn);
            ps.setString(2, ad);
            ps.setString(3, yazar);
            ps.setString(4, yayinevi);
            ps.setString(5, kategori);
            ps.setString(6, yer);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kitap başarıyla eklendi!");
            kitapListesiniGetir();
            toplamKitapSayisiniGuncelle();
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void kitapGuncelle() {
        int secilenSatir = kitapTablosu.getSelectedRow();
        if (secilenSatir == -1) {
            JOptionPane.showMessageDialog(this, "Güncellenecek bir kitap seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int kitapId = (int) model.getValueAt(secilenSatir, 0);
        String isbn = isbnAlani.getText().trim();
        String ad = adAlani.getText().trim();
        String yazar = yazarAlani.getText().trim();
        String yayinevi = yayineviAlani.getText().trim();
        String kategori = kategoriAlani.getText().trim();
        String yer = yerAlani.getText().trim();
        
        if (!isbn.matches("\\d+")) { // Girilen ISBN değerinin bir sayı olup olmadığı kontrol eder.
            JOptionPane.showMessageDialog(this, "ISBN 13 haneli bir sayı olmalıdır!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String sorgu = "UPDATE kitaplar SET isbn = ?, ad = ?, yazar = ?, yayin_evi = ?, kategori = ?, yer = ? WHERE kitap_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setString(1, isbn);
            ps.setString(2, ad);
            ps.setString(3, yazar);
            ps.setString(4, yayinevi);
            ps.setString(5, kategori);
            ps.setString(6, yer);
            ps.setInt(7, kitapId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kitap başarıyla güncellendi!");
            kitapListesiniGetir(); // Yeni kitap eklendikten sonra veritabanından kitap listesinin yeni halini çeker.
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    private void kitapSil() {
        int secilenSatir = kitapTablosu.getSelectedRow();
        if (secilenSatir == -1) {
            JOptionPane.showMessageDialog(this, "Silinecek bir kitap seçin!", "Hata", JOptionPane.ERROR_MESSAGE);
            return;
        }
        int kitapId = (int) model.getValueAt(secilenSatir, 0);
        String sorgu = "DELETE FROM kitaplar WHERE kitap_id = ?";
        try {
            Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            ps.setInt(1, kitapId);
            ps.executeUpdate();
            JOptionPane.showMessageDialog(this, "Kitap başarıyla silindi!");
            kitapListesiniGetir(); // Kitabın bilgileri güncellendikten sonra veritabanından kitap listesinin yeni halini çeker.
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(this, "Hata: " + e.getMessage(), "Veritabanı Hatası", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new KitapYonetimEkrani().setVisible(true));
    }
}
