public class Tunai implements MetodeBayar {
    @Override
    public String nama() {
        return "Tunai";
    }

    @Override
    public int hitungTotalBayar(int totalBelanja) {
        return totalBelanja;
    }
}
