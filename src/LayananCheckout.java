/** Mengatur urutan checkout; semua kolaborator disuntikkan lewat constructor. */
public class LayananCheckout {
    private final StrukPrinter printer;
    private final PenyimpanRiwayat riwayat;
    private final PengirimNotifikasi notifikasi;

    public LayananCheckout(StrukPrinter printer, PenyimpanRiwayat riwayat,
                           PengirimNotifikasi notifikasi) {
        this.printer = printer;
        this.riwayat = riwayat;
        this.notifikasi = notifikasi;
    }

    public void checkout(String pelanggan, String alamatEmail,
                         Keranjang keranjang, MetodeBayar metode) {
        int total = keranjang.totalBelanja();
        int bayar = metode.hitungTotalBayar(total);

        printer.cetak(pelanggan, keranjang, total, metode, bayar);

        String totalBayar = FormatRupiah.format(bayar);
        riwayat.simpan(pelanggan + ";" + keranjang.jumlah() + " buku;" + totalBayar);
        notifikasi.kirim(alamatEmail, "Terima kasih " + pelanggan + ", total bayar " + totalBayar);
    }
}
