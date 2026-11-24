/** Utilitas format mata uang. Cukup satu method statis (KISS). */
public final class FormatRupiah {
    private FormatRupiah() {
    }

    public static String format(int angka) {
        return String.format("Rp%,d", angka).replace(',', '.');
    }
}
