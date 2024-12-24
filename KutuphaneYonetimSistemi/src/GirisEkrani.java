// Programı kullanmak için kullanıcı adı ve şifreyle giriş yapılması istenen ekran. Gerekli bilgiler "kullanicilar" isimli tabloda saklanıyor.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class GirisEkrani extends JFrame {
    private JTextField kullaniciAdiAlani;
    private JPasswordField sifreAlani;
    
    private JButton girisButonu;

    public GirisEkrani() {
        // Ekran başlık ve boyut ayarları
        setTitle("Kütüpedia - Giriş");
        setSize(1000, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        // Kütüpedia - Fav Icon
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        // Logo Icon
        ImageIcon iconLogo = new ImageIcon("src/resimler/logo2.png"); 
        JLabel iconLogoLabel = new JLabel(iconLogo);
        iconLogoLabel.setBounds(250, 30, 200, 164);
        add(iconLogoLabel);

        // Başlık
        JLabel baslikLabel = new JLabel("KÜTÜPEDİA", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Serif", Font.BOLD, 32));
        baslikLabel.setBounds(310, 80, 500, 50);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);
        
        // Giriş Ekranı yazısı
        JLabel girisEkraniYazisiLabel = new JLabel("Kütüphane Yönetim Sistemine Giriş", SwingConstants.CENTER);
        girisEkraniYazisiLabel.setFont(new Font("Arial", Font.ITALIC, 32));
        girisEkraniYazisiLabel.setBounds(225, 215, 550, 50);
        girisEkraniYazisiLabel.setForeground(Color.decode("#2E3B4E"));
        add(girisEkraniYazisiLabel);
        
        // Kullanıcı adı iconu
        ImageIcon iconKullanici = new ImageIcon("src/resimler/kullanici.png"); 
        JLabel iconKullaniciLabel = new JLabel(iconKullanici);
        iconKullaniciLabel.setBounds(275, 310, 40, 40);
        add(iconKullaniciLabel);

        // Kullanıcı adı alanı ve etiketi
        JLabel kullaniciAdiLabel = new JLabel("Kullanıcı Adı:");
        kullaniciAdiLabel.setBounds(325, 315, 200, 30);
        kullaniciAdiLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        kullaniciAdiLabel.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciAdiLabel);

        kullaniciAdiAlani = new JTextField();
        kullaniciAdiAlani.setBounds(500, 315, 200, 30);
        add(kullaniciAdiAlani);
        
        // Şifre iconu
        ImageIcon iconSifre = new ImageIcon("src/resimler/sifre.png"); 
        JLabel iconSifreLabel = new JLabel(iconSifre);
        iconSifreLabel.setBounds(275, 375, 40, 40);
        add(iconSifreLabel);

        // Şifre alanı ve etiketi
        JLabel sifreLabel = new JLabel("Şifre:");
        sifreLabel.setBounds(325, 375, 300, 30);
        sifreLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        sifreLabel.setForeground(Color.decode("#2E3B4E"));
        add(sifreLabel);

        sifreAlani = new JPasswordField();
        sifreAlani.setBounds(500, 375, 200, 30);
        add(sifreAlani);
        
        // "Şifreyi göster" label'ı ve checkbox kutucuğu
        
        JLabel sifreyiGosterLabel = new JLabel("Şifreyi göster");
        sifreyiGosterLabel.setFont(new Font("Arial", Font.PLAIN, 13));
        sifreyiGosterLabel.setBounds(750, 380, 100, 20);
        add(sifreyiGosterLabel);
         
        JCheckBox sifreyiGoster = new JCheckBox("Şifreyi Göster");
        sifreyiGoster.setBounds(725, 380, 20, 20);
        add(sifreyiGoster);

        // Checkbox'ın etkinliğini kontrol etmek için listener.
        sifreyiGoster.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (sifreyiGoster.isSelected()) {
                    sifreAlani.setEchoChar((char) 0); // Şifreyi göster. Şifredeki karakterler olduğu gibi görünür.
                } else {
                    sifreAlani.setEchoChar('\u2022'); // Şifreyi gizler. Her bir karakteri nokta karakterine dönüştürülür.
                }
            }
        });

        // Giriş butonu
        girisButonu = new JButton("Giriş Yap");
        girisButonu.setBounds(400, 475, 200, 40);
        girisButonu.setFont(new Font("Arial", Font.BOLD, 18));
        girisButonu.setBackground(Color.decode("#007BFF"));
        girisButonu.setForeground(Color.WHITE);
        add(girisButonu);

        // Giriş butonu için listener.
        girisButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                    kullaniciDogrula();
            }
        });

        
        //Footer yazı
        JLabel footerYazi = new JLabel("UYARI: Giriş yapmakta sorun yaşadığınız taktirde lütfen sistem yöneticinizle iletişime geçiniz...");
        footerYazi.setBounds(50,600,600,100);
        girisButonu.setFont(new Font("Arial", Font.ITALIC, 25));
        add(footerYazi);
        

        // Arka plan rengi ayarı
        getContentPane().setBackground(Color.decode("#F5F5F5"));
    }

    private void kullaniciDogrula() {
        // Kullanıcı bilgileri alınır
        String kullaniciAdi = kullaniciAdiAlani.getText();
        String sifre = new String(sifreAlani.getPassword());

        // Veritabanı sorgusu
        String sorgu = "SELECT kullanici_id, ad, soyad FROM kullanicilar WHERE kullanici_adi = ? AND sifre = ?";
        try{
        	Connection conn = Baglanti.baglan();
            PreparedStatement ps = conn.prepareStatement(sorgu);
            
            ps.setString(1, kullaniciAdi);
            ps.setString(2, sifre);

            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                int kullaniciId = rs.getInt("kullanici_id");
                String ad = rs.getString("ad");
                String soyad = rs.getString("soyad");
                JOptionPane.showMessageDialog(this, "Hoş geldiniz, " + ad + " " + soyad + "!");

                // Ana ekrana yönlendirme
                AnaEkran anaEkran = new AnaEkran(kullaniciId, ad, soyad);
                anaEkran.setVisible(true);
                dispose(); // Ana Ekran açıldıktan sonra giriş ekranını kapatır.
            } else { // Girilen kullanıcı adı ve şifre kombinasyonu "kullanıcılar" tablosunda bulunamazsa yani rs boş dönerse aşağıdaki hata mesajı ekrana gelir.
                JOptionPane.showMessageDialog(this, "Kullanıcı adı veya şifre hatalı!", "Hata", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception ex) { // Veritabanı kaynaklı bir hata oluşursa aağıdaki hata mesajı ekrana gelir.
            JOptionPane.showMessageDialog(this, "Bir hata oluştu: " + ex.getMessage(), "Hata", JOptionPane.ERROR_MESSAGE);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            GirisEkrani girisEkrani = new GirisEkrani();
            girisEkrani.setVisible(true);
        });
    }
}