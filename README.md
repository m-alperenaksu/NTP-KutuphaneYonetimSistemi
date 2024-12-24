### Program Hakkında

## Kütüpedia - Kütüphane Yönetim Sistemi
Kütüpedia, kütüphane personellerinin kullanımına sunulmak üzere hazırlanmış bir Kütüphane Yönetim Sistemi programıdır.

---

## Programın Kullanılması Üzerine Bilgiler

### 1. Giriş Ekranı
Personelin programa kullanıcı adı ve şifreleri ile giriş yapacakları ekrandır. Bu kullanıcı adı ve şifreler önceden veritabanına kaydedilmiştir.

---

### 2. Ana Ekran
Giriş yapmış olan personelin gerekli işlem ekranına ulaşması için kullanılan ekrandır. Burada giriş yapmış personelin bilgileri yer alır. Personel, yapmak istediği işlemin butonuna tıklayarak o ekranı açar ve gerekli işlemleri yapar.

---

### 3. Üye Yönetim Sistemi
Bu ekranda mevcut üyelerin bilgilerini görebilir, yeni üye ekleyebilir, var olan üyelerin bilgilerini güncelleyebilir ve üyeleri silebiliriz. Üye silmek veya güncellemek için işlem yapmak istediğimiz üyenin üzerine tıklayıp, gerekli değişiklikleri formlarda yapar ve ilgili butona basarız.

---

### 4. Kitap Yönetim Sistemi
Bu ekranda mevcut kitapların bilgilerini görebilir, yeni kitap ekleyebilir, var olan kitapların bilgilerini güncelleyebilir ve kitapları silebiliriz. Kitap silmek veya güncellemek için işlem yapmak istediğimiz kitabın üstüne tıklayıp, formlarda gerekli değişikleri yapar ve ilgili butona basarız.

---

### 5. Ödünç Verme
Bu ekranda:
- Solda durumu "rafta" olan kitaplar listelenir.
- Sağda ise tüm üyeler listelenir.

**İşlem Süreci:**
1. Ödünç verilecek kitabı ve alacak olan üyeyi seçin.
2. Uygun olan gün sayısını belirleyin.
3. "Ödünç Ver" butonuna basarak işlemi tamamlayın.

**Şartlar:**
- Her üye en fazla 2 kitap ödünç alabilir.
- Halihazırda iade tarihi geçmiş kitabı bulunan üyeler yeni bir kitap ödünç alamaz.
- Ödenmemiş cezası olan üyeler, cezalarını ödeyene kadar yeni kitap ödünç alamaz.
- Halk üyelik türündeki üyeler en fazla **15 gün**, Öğrenci üyelik türündeki üyeler en fazla **30 gün**, Akademisyen üyelik türündeki üyeler ise en fazla **45 gün** kitabı ödünç alabilir.

---

### 6. İade Alma
Bu ekranda daha önce ödünç alınmış kitapların ve ödünç alan üyelerin bilgilerini içeren bir tablo bulunur. İlgili satıra tıklayıp "İade Al" butonuna basarak işlemi tamamlayabilirsiniz.

**Ceza Durumu:**
- Eğer üye kitabı iade etmesi gereken tarihten daha geç iade etmişse, her bir gün için **5 TL ceza** eklenir.

---

### 7. Süre Uzatma
Bu ekranda ödünç alınmış kitapların ve ödünç alan üyelerin bilgilerini içeren bir tablo bulunur. İlgili satıra tıklayıp "Süre Uzat" butonuna basarak işlemi tamamlayabilirsiniz.

**Şartlar:**
- Bir kitabın süresi en fazla **2 kere** uzatılabilir.
- Süresi uzatılmak istenen kitabın iade tarihi geçmişse, süre uzatılamaz.

---

### 8. Ceza Ödemesi Al
Bu ekranda ceza miktarı bulunan bütün üyeler bilgileriyle tabloda listelenir. Ceza ödemesi alınan üyenin üstüne tıkladıktan sonra "Ceza Ödemesini Al" butonuna basarak işlem tamamlanır.

---