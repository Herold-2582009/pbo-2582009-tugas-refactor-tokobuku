public class BukuCetak extends Buku implements BisaDikirim {
    private static final int BERAT_PER_KELIPATAN = 500;
    private static final int ONGKIR_PER_KELIPATAN = 5000;

    private final int beratGram;

    public BukuCetak(String judul, String penulis, int harga, int beratGram) {
        super(judul, penulis, harga);
        this.beratGram = beratGram;
    }

    @Override
    public int getBeratGram() {
        return beratGram;
    }

    private int hitungOngkir() {
        int kelipatan = (beratGram + BERAT_PER_KELIPATAN - 1) / BERAT_PER_KELIPATAN;
        return kelipatan * ONGKIR_PER_KELIPATAN;
    }

    @Override
    public int hargaAkhir() {
        return getHarga() + hitungOngkir();
    }

    @Override
    public String keterangan() {
        return beratGram + " gram, ongkir " + FormatRupiah.format(hitungOngkir());
    }
}
