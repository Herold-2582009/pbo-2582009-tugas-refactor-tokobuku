/** Satu-satunya class yang tahu tata letak struk. */
public class StrukPrinter {
    public void cetak(String pelanggan, Keranjang keranjang, int total,
                      MetodeBayar metode, int bayar) {
        System.out.println("================ STRUK =================");
        System.out.println("Pelanggan : " + pelanggan);
        for (Produk p : keranjang.getIsi()) {
            System.out.println(p.getJudul() + " (" + p.getPenulis() + ")");
            System.out.println("  Harga       : " + FormatRupiah.format(p.getHarga()));
            System.out.println("  Keterangan  : " + p.keterangan());
            System.out.println("  Harga akhir : " + FormatRupiah.format(p.hargaAkhir()));
        }
        System.out.println("-----------------------------------------");
        System.out.println("Total belanja : " + FormatRupiah.format(total));
        System.out.println("Metode bayar  : " + metode.nama());
        System.out.println("Total bayar   : " + FormatRupiah.format(bayar));
        System.out.println("Berat kiriman : " + keranjang.totalBeratGram() + " gram");

        System.out.println("-------------- LINK UNDUH ---------------");
        for (BisaDiunduh d : keranjang.getDiunduh()) {
            System.out.println(d.linkUnduh());
        }
        System.out.println("-----------------------------------------");
    }
}
