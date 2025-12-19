import java.util.ArrayList;

public class ServiceType {
    private String name;
    private int price;
    private String satuan;
    private boolean bisaBesar;
    private boolean isActive;

    public ServiceType(String name, int price, String satuan, boolean bisaBesar) {
        this.name = name;
        this.price = price;
        this.satuan = satuan;
        this.bisaBesar = bisaBesar;
        this.isActive = true; // default aktif
    }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    public String getSatuan() { return satuan; }
    public void setSatuan(String satuan) { this.satuan = satuan; }
    public boolean isBisaBesar() { return bisaBesar; }
    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { this.isActive = active; }

    // ===================== INISIALISASI LAYANAN =====================
    public static void initServiceTypes(ArrayList<ServiceType> daftarSatuan) {
        daftarSatuan.add(new ServiceType("Baju/Celana/Kaos", 4000, "pcs", false));
        daftarSatuan.add(new ServiceType("Jaket/Sweater", 6000, "pcs", false));
        daftarSatuan.add(new ServiceType("Jas", 12000, "pcs", false));
        daftarSatuan.add(new ServiceType("Kebaya", 12000, "pcs", false));
        daftarSatuan.add(new ServiceType("Tas", 20000, "buah", true));
        daftarSatuan.add(new ServiceType("Sepatu", 25000, "pasang", false));
        daftarSatuan.add(new ServiceType("Sprei", 20000, "lembar", true));
        daftarSatuan.add(new ServiceType("Bed Cover", 20000, "lembar", true));
        daftarSatuan.add(new ServiceType("Selimut", 20000, "lembar", true));
        daftarSatuan.add(new ServiceType("Gorden", 15000, "meter", false));
        daftarSatuan.add(new ServiceType("Karpet", 15000, "meter", false));
    }
}