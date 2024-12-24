// Programın Ana Ekranı. Menülere burdan ula şıyoruz.

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class AnaEkran extends JFrame {
    public AnaEkran(int kullaniciId, String kullaniciAdi, String kullaniciSoyadi) {
        setTitle("Kütüpedia - Ana Ekran");
        setSize(1500, 750);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);
        
        ImageIcon icon = new ImageIcon("src/resimler/icon.png");
        setIconImage(icon.getImage());
        
        ImageIcon iconLogo = new ImageIcon("src/resimler/logo2.png"); 
        JLabel iconLogoLabel = new JLabel(iconLogo);
        iconLogoLabel.setBounds(325, 40, 200, 164);
        add(iconLogoLabel);
        
        JLabel markaLabel = new JLabel("KÜTÜPEDİA");
        markaLabel.setFont(new Font("Serif", Font.BOLD, 20));
        markaLabel.setBounds(370, 195, 500, 50);
        markaLabel.setForeground(Color.decode("#2E3B4E"));
        add(markaLabel);

        JLabel baslikLabel = new JLabel("KÜTÜPHANE YÖNETİM SİSTEMİ", SwingConstants.CENTER);
        baslikLabel.setFont(new Font("Arial", Font.BOLD, 32));
        baslikLabel.setBounds(550, 50, 500, 50);
        baslikLabel.setForeground(Color.decode("#2E3B4E"));
        add(baslikLabel);

        JLabel personelBilgileriBaslik = new JLabel("Giriş Yapmış Personelin Bilgileri:");
        personelBilgileriBaslik.setFont(new Font("Arial", Font.BOLD, 18));
        personelBilgileriBaslik.setBounds(650, 120, 400, 30);
        personelBilgileriBaslik.setForeground(Color.decode("#2E3B4E"));
        add(personelBilgileriBaslik);

        JLabel kullaniciAdiLabel = new JLabel("Ad: ");
        kullaniciAdiLabel.setBounds(650, 160, 400, 30);
        kullaniciAdiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        kullaniciAdiLabel.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciAdiLabel);
        
        JLabel kullaniciAdiLabel2 = new JLabel(kullaniciAdi);
        kullaniciAdiLabel2.setBounds(750, 160, 400, 30);
        kullaniciAdiLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
        kullaniciAdiLabel2.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciAdiLabel2);

        JLabel kullaniciSoyadiLabel = new JLabel("Soyad: ");
        kullaniciSoyadiLabel.setBounds(650, 200, 400, 30);
        kullaniciSoyadiLabel.setFont(new Font("Arial", Font.BOLD, 16));
        kullaniciSoyadiLabel.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciSoyadiLabel);
        
        JLabel kullaniciSoyadiLabel2 = new JLabel(kullaniciSoyadi);
        kullaniciSoyadiLabel2.setBounds(750, 200, 400, 30);
        kullaniciSoyadiLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
        kullaniciSoyadiLabel2.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciSoyadiLabel2);

        JLabel kullaniciIdLabel = new JLabel("ID: ");
        kullaniciIdLabel.setBounds(650, 240, 400, 30);
        kullaniciIdLabel.setFont(new Font("Arial", Font.BOLD, 16));
        kullaniciIdLabel.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciIdLabel);
        
        JLabel kullaniciIdLabel2 = new JLabel("" + kullaniciId);
        kullaniciIdLabel2.setBounds(750, 240, 400, 30);
        kullaniciIdLabel2.setFont(new Font("Arial", Font.PLAIN, 16));
        kullaniciIdLabel2.setForeground(Color.decode("#2E3B4E"));
        add(kullaniciIdLabel2);

        JLabel anaMenuLabel = new JLabel("ANA MENÜ", SwingConstants.CENTER);
        anaMenuLabel.setFont(new Font("Arial", Font.BOLD, 28));
        anaMenuLabel.setBounds(625, 320, 300, 40);
        anaMenuLabel.setForeground(Color.decode("#007BFF"));
        add(anaMenuLabel);

        ImageIcon iconUye = new ImageIcon("src/resimler/uye.png"); 
        JLabel iconUyeLabel = new JLabel(iconUye);
        iconUyeLabel.setBounds(375, 395, 40, 40);
        add(iconUyeLabel);
        
        JButton uyeYonetimButonu = new JButton("Üye Yönetim Sistemi");
        uyeYonetimButonu.setBounds(425, 390, 300, 50);
        uyeYonetimButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        uyeYonetimButonu.setBackground(Color.decode("#007BFF"));
        uyeYonetimButonu.setForeground(Color.WHITE);
        add(uyeYonetimButonu);

        uyeYonetimButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame uyeYonetimEkrani = new UyeYonetimEkrani();
                uyeYonetimEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                uyeYonetimEkrani.setVisible(true);
            }
        });
        
        ImageIcon iconKitap = new ImageIcon("src/resimler/kitap.png");
        JLabel iconKitapLabel = new JLabel(iconKitap);
        iconKitapLabel.setBounds(375, 455, 40, 40);
        add(iconKitapLabel);
        
        JButton kitapYonetimButonu = new JButton("Kitap Yönetim Sistemi");
        kitapYonetimButonu.setBounds(425, 450, 300, 50);
        kitapYonetimButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        kitapYonetimButonu.setBackground(Color.decode("#007BFF"));
        kitapYonetimButonu.setForeground(Color.WHITE);
        add(kitapYonetimButonu);

        kitapYonetimButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame kitapYonetimEkrani = new KitapYonetimEkrani();
                kitapYonetimEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                kitapYonetimEkrani.setVisible(true);
            }
        });
        
        ImageIcon iconOdunc = new ImageIcon("src/resimler/odunc.png"); 
        JLabel iconOduncLabel = new JLabel(iconOdunc);
        iconOduncLabel.setBounds(375, 515, 40, 40);
        add(iconOduncLabel);

        JButton oduncVerButonu = new JButton("Ödünç Verme");
        oduncVerButonu.setBounds(425, 510, 300, 50);
        oduncVerButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        oduncVerButonu.setBackground(Color.decode("#007BFF"));
        oduncVerButonu.setForeground(Color.WHITE);
        add(oduncVerButonu);

        oduncVerButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame oduncVermeEkrani = new KitapOduncVermeEkrani();
                oduncVermeEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                oduncVermeEkrani.setVisible(true);
            }
        });

        ImageIcon iconIade = new ImageIcon("src/resimler/iade.png"); 
        JLabel iconIadeLabel = new JLabel(iconIade);
        iconIadeLabel.setBounds(800, 395, 40, 40);
        add(iconIadeLabel);
        
        JButton iadeAlButonu = new JButton("İade Alma");
        iadeAlButonu.setBounds(850, 390, 300, 50);
        iadeAlButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        iadeAlButonu.setBackground(Color.decode("#007BFF"));
        iadeAlButonu.setForeground(Color.WHITE);
        add(iadeAlButonu);

        iadeAlButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame iadeAlmaEkrani = new KitapIadeAlmaEkrani();
                iadeAlmaEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                iadeAlmaEkrani.setVisible(true);
            }
        });
        
        ImageIcon iconSure = new ImageIcon("src/resimler/saat.png"); 
        JLabel iconSureLabel = new JLabel(iconSure);
        iconSureLabel.setBounds(800, 455, 40, 40);
        add(iconSureLabel);
        
        JButton sureUzatButonu = new JButton("Süre Uzatma");
        sureUzatButonu.setBounds(850, 450, 300, 50);
        sureUzatButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        sureUzatButonu.setBackground(Color.decode("#007BFF"));
        sureUzatButonu.setForeground(Color.WHITE);
        add(sureUzatButonu);
        
        sureUzatButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame sureUzatmaEkrani = new SureUzatmaEkrani();
                sureUzatmaEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                sureUzatmaEkrani.setVisible(true);
            }
        });
        
        ImageIcon iconOdeme = new ImageIcon("src/resimler/odeme.png"); 
        JLabel iconOdemeLabel = new JLabel(iconOdeme);
        iconOdemeLabel.setBounds(800, 520, 40, 40);
        add(iconOdemeLabel);
        
        JButton cezaOdemeButonu = new JButton("Ceza Ödemesi Al");
        cezaOdemeButonu.setBounds(850, 510, 300, 50);
        cezaOdemeButonu.setFont(new Font("Arial", Font.PLAIN, 18));
        cezaOdemeButonu.setBackground(Color.decode("#007BFF"));
        cezaOdemeButonu.setForeground(Color.WHITE);
        add(cezaOdemeButonu);
        
        cezaOdemeButonu.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                JFrame CezaOdemeEkrani = new CezaOdemeEkrani();
                CezaOdemeEkrani.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                CezaOdemeEkrani.setVisible(true);
            }
        });

        getContentPane().setBackground(Color.decode("#F5F5F5"));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new AnaEkran(123, "deneme", "soyad").setVisible(true);
        });
    }
}
