import java.util.ArrayList;

public class Transaksi {

    private int id;
    private String nama;
    private String tanggalMasuk;
    private String tanggalSelesai;
    private String tanggalAmbil;
    private boolean antar;
    private ArrayList<ServiceItem> items;
    private int subtotal, diskon, total;

    public Transaksi(int id, String nama, String tanggalMasuk, String tanggalSelesai, boolean antar, ArrayList<ServiceItem> items, int subtotal, int diskon, int total) {
        this.id = id;
        this.nama = nama;
        this.tanggalMasuk = tanggalMasuk;
        this.tanggalSelesai = tanggalSelesai;
        this.antar = antar;
        this.items = items;
        this.subtotal = subtotal;
        this.diskon = diskon;
        this.total = total;
    }

    // GETTER
    public int getId() { return id; }
    public String getNama() { return nama; }
    public int getTotal() { return total; }
    public String getTanggalMasuk() { return tanggalMasuk; }
    public String getTanggalSelesai() { return tanggalSelesai; }
    public String getTanggalAmbil() { return tanggalAmbil; }
    public void setTanggalAmbil(String tanggalAmbil) { this.tanggalAmbil = tanggalAmbil; }
    public boolean hasAntar() { return antar; }

    // Helper to display a compact layanan description for history
    public String getLayananSummary() {
        if (items == null || items.isEmpty()) return "-";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < items.size(); i++) {
            ServiceItem it = items.get(i);
            if (i > 0) sb.append(" + ");
            sb.append(it.getNama());
        }
        return sb.toString();
    }

    // CETAK STRUK =============================
    public void cetakStruk() {
        System.out.println("======= STRUK LAUNDRY =======");
        System.out.println("ID Transaksi : " + id);
        System.out.println("Pelanggan    : " + nama);
        System.out.println("Tanggal Masuk: " + tanggalMasuk);
        System.out.println("Tanggal Selesai: " + tanggalSelesai);
        if (antar) System.out.println("Antar Jemput: Ya (+Rp 5000)");

        System.out.println("\n--- Rincian Layanan ---");
        for (ServiceItem it : items) {
            String antar = it.hasAntar() ? " | Antar Jemput" : "";
            System.out.printf("%s | %d %s | %s | %s | %s%s\n",
                it.getNama(), it.getJumlah(), it.getSatuan(), it.getMode(), it.getActionType(), LayananLaundry.formatRupiah(it.getSubtotal()), antar);
            if (it.hasParfum()) System.out.println("  + Parfum");
            if (it.hasBesar()) System.out.println("  + Besar");
        }

        System.out.println("\nSubtotal : " + LayananLaundry.formatRupiah(subtotal));
        System.out.println("Diskon   : " + LayananLaundry.formatRupiah(diskon));
        System.out.println("TOTAL    : " + LayananLaundry.formatRupiah(total));
        if (tanggalAmbil != null && !tanggalAmbil.isEmpty()) {
            System.out.println("Tanggal Ambil: " + tanggalAmbil);
        }
        System.out.println("==============================");
    }
}