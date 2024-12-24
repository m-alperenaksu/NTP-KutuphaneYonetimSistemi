import java.sql.Date;

public class Uye {
    private int uyeId;
    private String tcKimlikNo;
    private String ad;
    private String soyad;
    private String eposta;
    private String telefon;
    private int kitapSayisi;
    private int cezaMiktari;
    private String uyelikTuru;
    private Date uyeTarihi;

    public Uye() {
    	
    }


	public Uye(int uyeId, String tcKimlikNo, String ad, String soyad, String eposta, String telefon, int kitapSayisi, int cezaMiktari, String uyelikTuru, Date uyeTarihi) {
        this.uyeId = uyeId;
        this.tcKimlikNo = tcKimlikNo;
        this.ad = ad;
        this.soyad = soyad;
        this.eposta = eposta;
        this.telefon = telefon;
        this.kitapSayisi = kitapSayisi;
        this.cezaMiktari = cezaMiktari;
        this.uyelikTuru = uyelikTuru;
        this.uyeTarihi = uyeTarihi;
    }

    public int getUyeId() {
        return uyeId;
    }

    public void setUyeId(int uyeId) {
        this.uyeId = uyeId;
    }

    public String getTcKimlikNo() {
        return tcKimlikNo;
    }

    public void setTcKimlikNo(String tcKimlikNo) {
        this.tcKimlikNo = tcKimlikNo;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getSoyad() {
        return soyad;
    }

    public void setSoyad(String soyad) {
        this.soyad = soyad;
    }

    public String getEposta() {
        return eposta;
    }

    public void setEposta(String eposta) {
        this.eposta = eposta;
    }

    public String getTelefon() {
        return telefon;
    }

    public void setTelefon(String telefon) {
        this.telefon = telefon;
    }

    public int getKitapSayisi() {
        return kitapSayisi;
    }

    public void setKitapSayisi(int kitapSayisi) {
        this.kitapSayisi = kitapSayisi;
    }

    public int getCezaMiktari() {
        return cezaMiktari;
    }

    public void setCezaMiktari(int cezaMiktari) {
        this.cezaMiktari = cezaMiktari;
    }

    public String getUyelikTuru() {
        return uyelikTuru;
    }

    public void setUyelikTuru(String uyelikTuru) {
        this.uyelikTuru = uyelikTuru;
    }

    public Date getUyeTarihi() {
        return uyeTarihi;
    }

    public void setUyeTarihi(Date uyeTarihi) {
        this.uyeTarihi = uyeTarihi;
    }
}
