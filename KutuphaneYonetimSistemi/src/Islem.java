import java.sql.Date;

public class Islem {
    private int islemId;
    private int kitapId;
    private String kitapAd;
    private int uyeId;
    private String uyeAdSoyad;
    private Date oduncTarihi;
    private Date iadeTarihi;
    private int uzatmaSayisi;
    private String durum;

    public Islem() {
    	
    }
    
    

    public Islem(int islemId, int kitapId, String kitapAd, int uyeId, String uyeAdSoyad, Date oduncTarihi, Date iadeTarihi, int uzatmaSayisi, String durum) {
		this.islemId = islemId;
		this.kitapId = kitapId;
		this.kitapAd = kitapAd;
		this.uyeId = uyeId;
		this.uyeAdSoyad = uyeAdSoyad;
		this.oduncTarihi = oduncTarihi;
		this.iadeTarihi = iadeTarihi;
		this.uzatmaSayisi = uzatmaSayisi;
		this.durum = durum;
	}



	public int getIslemId() {
        return islemId;
    }

    public void setIslemId(int islemId) {
        this.islemId = islemId;
    }

    public int getKitapId() {
        return kitapId;
    }

    public void setKitapId(int kitapId) {
        this.kitapId = kitapId;
    }

    public String getKitapAd() {
        return kitapAd;
    }

    public void setKitapAd(String kitapAd) {
        this.kitapAd = kitapAd;
    }

    public int getUyeId() {
        return uyeId;
    }

    public void setUyeId(int uyeId) {
        this.uyeId = uyeId;
    }

    public String getUyeAdSoyad() {
        return uyeAdSoyad;
    }

    public void setUyeAdSoyad(String uyeAdSoyad) {
        this.uyeAdSoyad = uyeAdSoyad;
    }

    public Date getOduncTarihi() {
        return oduncTarihi;
    }

    public void setOduncTarihi(Date oduncTarihi) {
        this.oduncTarihi = oduncTarihi;
    }

    public Date getIadeTarihi() {
        return iadeTarihi;
    }

    public void setIadeTarihi(Date iadeTarihi) {
        this.iadeTarihi = iadeTarihi;
    }

    public int getUzatmaSayisi() {
        return uzatmaSayisi;
    }

    public void setUzatmaSayisi(int uzatmaSayisi) {
        this.uzatmaSayisi = uzatmaSayisi;
    }

    public String getDurum() {
        return durum;
    }

    public void setDurum(String durum) {
        this.durum = durum;
    }
}
