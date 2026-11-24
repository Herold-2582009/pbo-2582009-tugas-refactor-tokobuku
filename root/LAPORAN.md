# Laporan Refactor Toko Buku (SOLID)

Program awal (`TokoBukuKotor.java`) berjalan benar, tetapi seluruh logika menumpuk di class `Toko` dan `Buku`.
Setelah refactor, keluaran program identik dengan keluaran awal (dibandingkan baris per baris).

## S: Single Responsibility Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    ...
    void checkout(String pelanggan, String alamatEmail, String metode) {
        System.out.println("================ STRUK =================");
        ...
        int bayar = totalBayar(metode, total);
        ...
        riwayat.simpan(pelanggan + ";" + keranjang.size() + " buku;" + formatRupiah(bayar));
        email.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + formatRupiah(bayar));
    }

    String formatRupiah(int angka) { ... }
}
```

**(b) Masalahnya**

Class `Toko` punya banyak alasan untuk berubah: menyimpan isi keranjang, menghitung harga dan ongkir,
menghitung biaya metode bayar, menentukan tata letak struk, memformat rupiah, lalu mengurus riwayat dan email.
Jika tata letak struk berubah, atau aturan ongkir berubah, atau format rupiah berubah, `Toko` yang sama harus
dibuka dan diubah. Satu perubahan bisa merusak bagian lain yang tidak berkaitan.

**(c) Perbaikan**

Tiap tanggung jawab dipindah ke class sendiri:
`Keranjang` (isi belanja dan totalnya), `BukuCetak`/`BukuDigital`/`BukuAudio` (harga dan keterangan masing-masing),
`MetodeBayar` beserta implementasinya (biaya pembayaran), `StrukPrinter` (tata letak struk),
`FormatRupiah` (format angka), `FileRiwayat` dan `EmailSender` (efek samping),
serta `LayananCheckout` yang hanya mengatur urutan langkah checkout.

## O: Open/Closed Principle

**(a) Kode awal yang melanggar**

```java
int hargaAkhir(Buku b) {
    switch (b.jenis) {
        case "CETAK":   return b.getHarga() + hitungOngkir(b);
        case "DIGITAL": return b.getHarga() * 80 / 100;
        case "AUDIO":   return b.getHarga() * 90 / 100;
        default:        return b.getHarga();
    }
}
...
int totalBayar(String metode, int totalBelanja) {
    if (metode.equals("TUNAI")) { ... }
    else if (metode.equals("TRANSFER")) { ... }
    else if (metode.equals("EWALLET")) { ... }
    return totalBelanja;
}
```

**(b) Masalahnya**

Setiap jenis buku baru (misalnya buku langganan) atau metode bayar baru (misalnya kartu kredit) memaksa kita
mengedit `hargaAkhir`, `keterangan`, `namaMetode`, dan `totalBayar` di class `Toko`, yang sudah berjalan dan teruji.
Karena pemeriksaan jenis tersebar di empat method, mudah ada satu yang terlewat.

**(c) Perbaikan**

Variasi perilaku dipindah ke polimorfisme. `Produk` mendeklarasikan `hargaAkhir()` dan `keterangan()`, lalu
`BukuCetak`, `BukuDigital`, dan `BukuAudio` mengimplementasikannya masing-masing. Metode bayar menjadi interface
`MetodeBayar` dengan `Tunai`, `Transfer`, dan `EWallet`. Menambah jenis atau metode baru cukup dengan menambah
satu class baru; `Keranjang`, `StrukPrinter`, dan `LayananCheckout` tidak disentuh.

## L: Liskov Substitution Principle

**(a) Kode awal yang melanggar**

```java
public int getBeratGram() {
    if (!jenis.equals("CETAK")) {
        throw new UnsupportedOperationException(judul + " tidak punya berat");
    }
    return beratGram;
}
...
public int getDurasiMenit() {
    if (!jenis.equals("AUDIO")) {
        throw new UnsupportedOperationException(judul + " bukan buku audio");
    }
    return durasiMenit;
}
```

**(b) Masalahnya**

`Buku` mengaku sebagai `Produk` yang punya berat, link unduh, dan durasi. Padahal buku digital tidak punya berat
dan buku cetak tidak punya link unduh. Kode yang menerima `Produk` tidak bisa memanggil `getBeratGram()` dengan
aman, karena bergantung pada jenis objeknya, ia bisa melempar exception. Subtipe tidak bisa menggantikan tipe
induknya tanpa merusak program, dan itulah sebabnya kode awal harus memeriksa `jenis` sebelum memanggil.

**(c) Perbaikan**

Method yang tidak berlaku untuk semua produk dikeluarkan dari `Produk`. Semua turunan `Buku` hanya menjanjikan
hal yang benar-benar bisa mereka penuhi, sehingga setiap `Produk` aman dipakai di mana pun tanpa pemeriksaan
tipe dan tanpa exception. `getDurasiMenit()` kini hanya ada di `BukuAudio`.

## I: Interface Segregation Principle.

**(a) Kode awal yang melanggar**

```java
interface Produk {
    String getJudul();
    String getPenulis();
    int getHarga();
    int getBeratGram();      // hanya masuk akal untuk buku cetak
    String linkUnduh();      // hanya masuk akal untuk buku digital & audio
    int getDurasiMenit();    // hanya masuk akal untuk buku audio
}
```

**(b) Masalahnya**

Interface ini "gemuk": setiap implementasi dipaksa memiliki method yang tidak relevan baginya. Akibatnya
implementasi harus berpura-pura (lihat pelanggaran L) dan pemakai interface ikut bergantung pada method yang tidak
mereka butuhkan.

**(c) Perbaikan**

Interface dipecah menjadi tiga peran: `Produk` (hanya yang berlaku untuk semua produk), `BisaDikirim`
(`getBeratGram()`, dipakai `BukuCetak`), dan `BisaDiunduh` (`linkUnduh()`, dipakai `BukuDigital` dan `BukuAudio`).
`Keranjang` menyimpan produk menurut perannya (`tambahDikirim`, `tambahDiunduh`), sehingga total berat dan daftar
link dapat diambil tanpa `if` atau `instanceof`.

## D: Dependency Inversion Principle

**(a) Kode awal yang melanggar**

```java
class Toko {
    ArrayList<Buku> keranjang = new ArrayList<>();
    EmailSender email = new EmailSender();
    FileRiwayat riwayat = new FileRiwayat();
    ...
}
```

**(b) Masalahnya**

Logika checkout (level tinggi) membuat sendiri dan bergantung langsung pada implementasi konkret `EmailSender` dan
`FileRiwayat` (level rendah). Mengganti email dengan WhatsApp, atau file dengan database, berarti mengubah `Toko`.
Class ini juga tidak bisa diuji tanpa benar-benar mengirim email dan menulis file.

**(c) Perbaikan**

`PenyimpanRiwayat` dan `PengirimNotifikasi` adalah interface; `FileRiwayat` dan `EmailSender` mengimplementasikannya.
`LayananCheckout` menerima `StrukPrinter`, `PenyimpanRiwayat`, dan `PengirimNotifikasi` lewat constructor, dengan
tipe parameter berupa interface untuk riwayat dan notifikasi. Hanya `main` di `TokoBukuSolid` yang memanggil `new`
untuk class-class tersebut dan menyusunnya (composition root).

## Bagian yang tidak perlu diberi abstraksi (YAGNI/KISS)

Saya sengaja tidak membuat interface untuk `StrukPrinter`: struk hanya dicetak ke konsol dengan satu format dan
belum ada kebutuhan format lain, jadi interface hanya menambah file tanpa manfaat. Interface bisa diekstrak kapan
saja jika nanti ada struk PDF. Alasan yang sama berlaku untuk `FormatRupiah`, yang cukup berupa satu method statis
tanpa state, dan untuk perhitungan ongkir yang saya biarkan sebagai method private di `BukuCetak` tanpa membuat
`KebijakanOngkir`, karena hanya buku cetak yang memakainya dan aturannya belum bervariasi. Saya juga tidak membuat
class `Pelanggan`, factory buku, atau hierarki tambahan untuk buku unduhan; `String` dan constructor biasa sudah
cukup. Abstraksi baru layak ditambahkan ketika variasinya benar-benar muncul, bukan sekadar karena mungkin muncul.
