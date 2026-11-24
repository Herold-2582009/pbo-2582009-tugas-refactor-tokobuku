import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Menyimpan isi belanja. Produk didaftarkan lewat metode sesuai perannya,
 * sehingga tidak perlu memeriksa tipe produk saat menghitung berat atau
 * mengumpulkan tautan unduh.
 */
public class Keranjang {
    private final List<Produk> semua = new ArrayList<>();
    private final List<BisaDikirim> dikirim = new ArrayList<>();
    private final List<BisaDiunduh> diunduh = new ArrayList<>();

    /** Produk biasa tanpa peran khusus (tidak dikirim, tidak diunduh). */
    public void tambah(Produk produk) {
        semua.add(produk);
    }

    public <T extends Produk & BisaDikirim> void tambahDikirim(T produk) {
        semua.add(produk);
        dikirim.add(produk);
    }

    public <T extends Produk & BisaDiunduh> void tambahDiunduh(T produk) {
        semua.add(produk);
        diunduh.add(produk);
    }

    public List<Produk> getIsi() {
        return Collections.unmodifiableList(semua);
    }

    public List<BisaDiunduh> getDiunduh() {
        return Collections.unmodifiableList(diunduh);
    }

    public int jumlah() {
        return semua.size();
    }

    public int totalBelanja() {
        int total = 0;
        for (Produk p : semua) {
            total += p.hargaAkhir();
        }
        return total;
    }

    public int totalBeratGram() {
        int total = 0;
        for (BisaDikirim d : dikirim) {
            total += d.getBeratGram();
        }
        return total;
    }
}
