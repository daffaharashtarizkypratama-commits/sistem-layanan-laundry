public class ServiceItem {
    private String mode; // Kiloan or Satuan
    private String nama;
    private String satuan;
    private String estimasi;
    private int jumlah;
    private int pricePerUnit;
    private int subtotal;
    private boolean besar, parfum, antar;
    private String actionType; // "Cuci", "Setrika saja", "Cuci+Setrika"

    public ServiceItem(String mode, String nama, String satuan, String estimasi,
                       int jumlah, int pricePerUnit, String actionType,
                       boolean besar, boolean parfum, boolean antar,
                       int subtotal) {
        this.mode = mode;
        this.nama = nama;
        this.satuan = satuan;
        this.estimasi = estimasi;
        this.jumlah = jumlah;
        this.pricePerUnit = pricePerUnit;
        this.actionType = actionType;
        this.besar = besar;
        this.parfum = parfum;
        this.antar = antar;
        this.subtotal = subtotal;
    }

    public String getMode() { return mode; }
    public String getNama() { return nama; }
    public String getSatuan() { return satuan; }
    public String getEstimasi() { return estimasi; }
    public int getJumlah() { return jumlah; }
    public int getPricePerUnit() { return pricePerUnit; }
    public int getSubtotal() { return subtotal; }
    public boolean hasBesar() { return besar; }
    public boolean hasParfum() { return parfum; }
    public boolean hasAntar() { return antar; }
    public String getActionType() { return actionType; }
    public boolean hasSetrika() { return actionType != null && actionType.toLowerCase().contains("setrika"); }
}
