import java.sql.Date;

public class Kitap {
    private int kitapId;
    private String isbn;
    private String ad;
    private String yazar;
    private String yayinEvi;
    private String kategori;
    private String yer;
    private String durum;
    private Date eklenmeTarihi;

    public Kitap() {
    	
    }
    
    

    public Kitap(int kitapId, String isbn, String ad, String yazar, String yayinEvi, String kategori, String yer, String durum, Date eklenmeTarihi) {
		this.kitapId = kitapId;
		this.isbn = isbn;
		this.ad = ad;
		this.yazar = yazar;
		this.yayinEvi = yayinEvi;
		this.kategori = kategori;
		this.yer = yer;
		this.durum = durum;
		this.eklenmeTarihi = eklenmeTarihi;
	}



	public int getKitapId() {
        return kitapId;
    }

    public void setKitapId(int kitapId) {
        this.kitapId = kitapId;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getAd() {
        return ad;
    }

    public void setAd(String ad) {
        this.ad = ad;
    }

    public String getYazar() {
        return yazar;
    }

    public void setYazar(String yazar) {
        this.yazar = yazar;
    }

    public String getYayinEvi() {
        return yayinEvi;
    }

    public void setYayinEvi(String yayinEvi) {
        this.yayinEvi = yayinEvi;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getYer() {
        return yer;
    }

    public void setYer(String yer) {
        this.yer = yer;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }

    public Date getEklenmeTarihi() {
        return eklenmeTarihi;
    }

    public void setEklenmeTarihi(Date eklenmeTarihi) {
        this.eklenmeTarihi = eklenmeTarihi;
    }
}
