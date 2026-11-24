/** Kontrak minimal yang berlaku untuk SEMUA produk toko. */
public interface Produk {
    String getJudul();
    String getPenulis();
    int getHarga();
    int hargaAkhir();
    String keterangan();
}
