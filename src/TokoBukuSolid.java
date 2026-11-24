/** Composition root: satu-satunya tempat object disusun. */
public class TokoBukuSolid {
    public static void main(String[] args) {
        Keranjang keranjang = new Keranjang();
        keranjang.tambahDikirim(new BukuCetak("Pemrograman Java", "John Doe", 120000, 750));
        keranjang.tambahDikirim(new BukuCetak("Algoritma Dasar", "Jane Smith", 95000, 400));
        keranjang.tambahDiunduh(new BukuDigital("Belajar OOP", "Ani Wijaya", 80000, 12.5));
        keranjang.tambahDiunduh(new BukuAudio("Kisah Programmer", "Budi Santoso", 60000, 240));

        LayananCheckout layanan = new LayananCheckout(
                new StrukPrinter(),
                new FileRiwayat(),
                new EmailSender());

        layanan.checkout("Sari", "sari@mail.com", keranjang, new EWallet());
    }
}
