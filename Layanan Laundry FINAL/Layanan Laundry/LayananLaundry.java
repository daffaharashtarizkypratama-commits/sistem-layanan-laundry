import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LayananLaundry {

    private static final Scanner sc = new Scanner(System.in);
    private static final ArrayList<Transaksi> daftarTransaksi = new ArrayList<>();
    private static final ArrayList<ServiceType> daftarSatuan = new ArrayList<>();

    static {
        ServiceType.initServiceTypes(daftarSatuan);
    }

    public static void main(String[] args) {
        headerProgram();
        mainMenu();
    }

    // ===================== MENU UTAMA =====================
    static void mainMenu() {
        int pilihan;
        do {
            System.out.println("\n===== MENU UTAMA =====");
            System.out.println("1. Buat Transaksi Baru");
            System.out.println("2. Lihat Riwayat Transaksi");
            System.out.println("3. Tambah Layanan Baru");
            System.out.println("4. Lihat Layanan");
            System.out.println("5. Edit/Nonaktifkan Layanan");
            System.out.println("6. Keluar");

            pilihan = bacaInt("Pilih (1-6): ");

            switch (pilihan) {
                case 1:
                    prosesTransaksi();
                    break;
                case 2:
                    tampilkanRiwayat();
                    break;
                case 3:
                    tambahLayananBaru();
                    break;
                case 4:
                    lihatLayanan();
                    break;
                case 5:
                    editLayanan();
                    break;
                case 6:
                    System.out.println("Terima kasih telah menggunakan layanan kami.");
                    break;
                default:
                    System.out.println("Pilihan tidak valid!");
            }

        } while (pilihan != 6);
    }

    // ===================== PROSES TRANSAKSI =====================
    static void prosesTransaksi() {
        System.out.println("\n--- FORM TRANSAKSI ---");

        System.out.print("Masukkan nama pelanggan: ");
        String nama = sc.nextLine().trim();

        // Input tanggal masuk (boleh kosong untuk default hari ini)
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.print("Masukkan tanggal masuk (dd-MM-yyyy) atau tekan Enter untuk hari ini: ");
        String inputTanggal = sc.nextLine().trim();
        String tanggalMasuk;
        if (inputTanggal.isEmpty()) {
            tanggalMasuk = LocalDate.now().format(fmt);
        } else {
            try {
                LocalDate.parse(inputTanggal, fmt);
                tanggalMasuk = inputTanggal;
            } catch (Exception e) {
                System.out.println("Format tanggal salah, menggunakan hari ini.");
                tanggalMasuk = LocalDate.now().format(fmt);
            }
        }

        ArrayList<ServiceItem> items = new ArrayList<>();
        // Tanya antar jemput sekali per transaksi (sekarang tidak lagi per-item)
        boolean antarTransaksi = yaTidak("Layanan antar jemput (+Rp 5000)? (y/t): ");
        boolean tambahLagi = true;
        while (tambahLagi) {
            System.out.println("\n--- Tambah Item Layanan ---");
            tampilkanMenuMode();
            int pilihMode = bacaInt("Pilih mode (1-2): ");

            if (pilihMode == 1) { // Kiloan
                tampilkanMenuKiloan();
                int pilihJenis = bacaInt("Pilih (1-2): ");
                int hargaPerKg = (pilihJenis == 2) ? 10000 : 7000;
                String layanan = (pilihJenis == 2) ? "Express" : "Reguler";
                String estimasi = (pilihJenis == 2) ? "1 hari" : "3 hari";
                String satuan = "kg";

                int jumlah = bacaInt("Masukkan berat (" + satuan + "): ");

                System.out.println("Pilih jenis layanan:");
                System.out.println("1. Setrika saja");
                System.out.println("2. Cuci + Setrika");
                int pilihAction = bacaInt("Pilih (1-2): ");
                String actionType = (pilihAction == 1) ? "Setrika saja" : "Cuci+Setrika";

                boolean parfum = false;
                if (actionType.contains("Cuci")) {
                    parfum = yaTidak("Tambahkan parfum premium (+Rp 1000/kg)? (y/t): ");
                }

                int pricePerUnit;
                if (actionType.equals("Setrika saja")) pricePerUnit = 3000;
                else pricePerUnit = hargaPerKg + 3000; // Cuci + Setrika

                int subtotal = jumlah * pricePerUnit;
                if (parfum) subtotal += jumlah * 1000;
                // antar handled at transaction level

                ServiceItem it = new ServiceItem("Kiloan", layanan, satuan, estimasi,
                    jumlah, pricePerUnit, actionType, false, parfum, false, subtotal);
                items.add(it);
            } else { // Satuan
                boolean pilihSatuan = true;
                while (pilihSatuan) {
                    ArrayList<ServiceType> activeSatuan = new ArrayList<>();
                    for (ServiceType s : daftarSatuan) {
                        if (s.isActive()) {
                            activeSatuan.add(s);
                        }
                    }
                    
                    for (int i = 0; i < activeSatuan.size(); i++) {
                        ServiceType s = activeSatuan.get(i);
                        String besar = s.isBisaBesar() ? " [+Besar]" : "";
                        System.out.println((i + 1) + ". " + s.getName() + " - Rp " + s.getPrice() + "/" + s.getSatuan() + besar);
                    }
                    int kode = bacaInt("Pilih jenis (kode): ");

                    if (kode < 1 || kode > activeSatuan.size()) {
                        System.out.println("Kode tidak valid.");
                        continue;
                    }

                    ServiceType st = activeSatuan.get(kode - 1);
                    int jumlah = bacaInt("Masukkan jumlah (" + st.getSatuan() + "): ");
                    boolean besar = false;

                    System.out.println("Pilih jenis layanan:");
                    System.out.println("1. Setrika saja");
                    System.out.println("2. Cuci + Setrika");
                    int pilihAction = bacaInt("Pilih (1-2): ");
                    String actionType = (pilihAction == 1) ? "Setrika saja" : "Cuci+Setrika";

                    if (st.isBisaBesar() && actionType.contains("Cuci")) {
                        besar = yaTidak("Ukuran besar (+Rp 5000/" + st.getSatuan() + ")? (y/t): ");
                    }

                    boolean parfum = false;
                    if (actionType.contains("Cuci")) {
                        parfum = yaTidak("Tambahkan parfum premium (+Rp 500/" + st.getSatuan() + ")? (y/t): ");
                    }


                    int pricePerUnit;
                    if (actionType.equals("Setrika saja")) pricePerUnit = 1000;
                    else pricePerUnit = st.getPrice() + 1000; // Cuci + Setrika

                        int subtotal = jumlah * pricePerUnit;
                        if (besar && actionType.contains("Cuci")) subtotal += jumlah * 5000;
                        if (parfum) subtotal += jumlah * 500;

                        ServiceItem it = new ServiceItem("Satuan", st.getName(), st.getSatuan(), "2 hari",
                            jumlah, pricePerUnit, actionType, besar, parfum, false, subtotal);
                    items.add(it);
                    pilihSatuan = false; // keluar loop satuan
                }
            }

            tambahLagi = yaTidak("Tambah layanan lain dalam transaksi ini? (y/t): ");
        }

        // Hitung totals
        int subtotalAll = 0;
        int diskonAll = 0;
        for (ServiceItem it : items) {
            subtotalAll += it.getSubtotal();
            // jika item kiloan dan berat > 10 kg dan termasuk cuci => diskon 10% pada item itu
            if ("Kiloan".equals(it.getMode()) && it.getJumlah() > 10 && it.getActionType() != null && it.getActionType().contains("Cuci")) {
                diskonAll += (int) (it.getSubtotal() * 0.10);
            }
        }

        // tambahkan biaya antar jemput sekali per transaksi jika dipilih
        if (antarTransaksi) subtotalAll += 5000;
        int total = pembulatanRatusan(subtotalAll - diskonAll);

        // hitung tanggal selesai berdasarkan estimasi terlama dari item
        int maxDays = 0;
        for (ServiceItem it : items) {
            String est = it.getEstimasi();
            if (est != null && !est.isEmpty()) {
                try {
                    String part = est.trim().split("\\s+")[0];
                    int days = Integer.parseInt(part);
                    int randomizedDays;
                    if (days == 1) {
                        // express: tetap 1 hari
                        randomizedDays = 1;
                    } else {
                        // tambahkan delay acak 1-3 hari supaya tidak selalu sama
                        int add = java.util.concurrent.ThreadLocalRandom.current().nextInt(1, 4); // 1..3
                        randomizedDays = days + add;
                    }
                    if (randomizedDays > maxDays) maxDays = randomizedDays;
                } catch (Exception ignored) {}
            }
        }

        String tanggalSelesai;
        try {
            java.time.LocalDate masuk = java.time.LocalDate.parse(tanggalMasuk, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            java.time.LocalDate selesai = masuk.plusDays(maxDays);
            tanggalSelesai = selesai.format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        } catch (Exception e) {
            tanggalSelesai = tanggalMasuk; // fallback
        }

        Transaksi t = new Transaksi(
            daftarTransaksi.size() + 1,
            nama,
            tanggalMasuk,
            tanggalSelesai,
            antarTransaksi,
            items,
            subtotalAll,
            diskonAll,
            total
        );

        daftarTransaksi.add(t);

        System.out.println();
        t.cetakStruk();
        System.out.println("\nTransaksi berhasil disimpan!\n");
    }

    // ===================== RIWAYAT =====================
    static void tampilkanRiwayat() {
        System.out.println("\n=== RIWAYAT TRANSAKSI ===");

        if (daftarTransaksi.isEmpty()) {
            System.out.println("Belum ada transaksi.");
            return;
        }

        // Cetak struk lengkap untuk setiap transaksi
        for (Transaksi t : daftarTransaksi) {
            System.out.println();
            t.cetakStruk();
        }
    }

    

    // ===================== METHOD BANTUAN =====================
    static void headerProgram() {
        System.out.println("=====================================");
        System.out.println("          SISTEM LAUNDRY JAVA        ");
        System.out.println("=====================================\n");
    }

    static void tampilkanMenuMode() {
        System.out.println("\nPilih mode layanan:");
        System.out.println("1. Kiloan");
        System.out.println("2. Satuan");
    }

    static void tampilkanMenuKiloan() {
        System.out.println("\nJenis kiloan:");
        System.out.println("1. Reguler: Rp 7000/kg (3 hari)");
        System.out.println("2. Express: Rp 10000/kg (1 hari)");
    }

    static void tampilkanMenuSatuan() {
        System.out.println("\nDaftar layanan satuan (yang aktif):");
        int count = 0;
        for (int i = 0; i < daftarSatuan.size(); i++) {
            ServiceType s = daftarSatuan.get(i);
            if (s.isActive()) {
                count++;
                System.out.printf("%d. %s: Rp %d/%s\n", count, s.getName(), s.getPrice(), s.getSatuan());
            }
        }
        if (count == 0) {
            System.out.println("Tidak ada layanan satuan yang aktif.");
        }
    }


    static int bacaInt(String pesan) {
        while (true) {
            try {
                System.out.print(pesan);
                return Integer.parseInt(sc.nextLine());
            } catch (Exception e) {
                System.out.println("Input harus angka!");
            }
        }
    }

    static boolean yaTidak(String pesan) {
        while (true) {
            System.out.print(pesan);
            String s = sc.nextLine().trim().toLowerCase();
            if (s.equals("y") || s.equals("ya")) return true;
            if (s.equals("t") || s.equals("tidak")) return false;
            System.out.println("Jawab y/t saja.");
        }
    }

    static int pembulatanRatusan(int nilai) {
        int sisa = nilai % 100;
        return (sisa >= 50) ? nilai + (100 - sisa) : nilai - sisa;
    }

    static String formatRupiah(int value) {
        String angka = Integer.toString(value);
        StringBuilder sb = new StringBuilder();

        int hitung = 0;
        for (int i = angka.length() - 1; i >= 0; i--) {
            sb.append(angka.charAt(i));
            hitung++;
            if (hitung == 3 && i > 0) {
                sb.append('.');
                hitung = 0;
            }
        }
        return "Rp " + sb.reverse().toString();
    }

    // ===================== KELOLA LAYANAN =====================
    static void tambahLayananBaru() {
        System.out.println("\n--- TAMBAH LAYANAN BARU ---");
        System.out.print("Nama layanan: ");
        String nama = sc.nextLine().trim();
        int harga = bacaInt("Harga per satuan (Rp): ");
        System.out.print("Satuan (contoh: pcs, lembar, meter): ");
        String satuan = sc.nextLine().trim();
        boolean bisaBesar = yaTidak("Bisa opsi ukuran besar? (y/t): ");
        
        daftarSatuan.add(new ServiceType(nama, harga, satuan, bisaBesar));
        System.out.println("✓ Layanan '" + nama + "' berhasil ditambahkan.\n");
    }

    static void lihatLayanan() {
        System.out.println("\n--- DAFTAR LAYANAN ---");
        ArrayList<ServiceType> activeServices = new ArrayList<>();
        for (ServiceType s : daftarSatuan) {
            if (s.isActive()) {
                activeServices.add(s);
            }
        }
        
        if (activeServices.isEmpty()) {
            System.out.println("Tidak ada layanan aktif.");
            return;
        }

        System.out.println();
        for (int i = 0; i < activeServices.size(); i++) {
            ServiceType s = activeServices.get(i);
            String besar = s.isBisaBesar() ? " [+Besar]" : "";
            System.out.printf("%d. %-30s | Rp %6d/%-8s%s\n", 
                i + 1, s.getName(), s.getPrice(), s.getSatuan(), besar);
        }
    }

    static void editLayanan() {
        System.out.println("\n--- EDIT/NONAKTIFKAN LAYANAN ---");
        if (daftarSatuan.isEmpty()) {
            System.out.println("Tidak ada layanan untuk diedit.");
            return;
        }

        System.out.println();
        for (int i = 0; i < daftarSatuan.size(); i++) {
            ServiceType s = daftarSatuan.get(i);
            String status = s.isActive() ? "AKTIF" : "NONAKTIF";
            String besar = s.isBisaBesar() ? " [+Besar]" : "";
            System.out.printf("%d. %-30s | Rp %6d/%-8s | %s%s\n", 
                i + 1, s.getName(), s.getPrice(), s.getSatuan(), status, besar);
        }
        
        int kode = bacaInt("\nPilih kode layanan (0 untuk batal): ");
        
        if (kode == 0) return;
        if (kode < 1 || kode > daftarSatuan.size()) {
            System.out.println("Kode tidak valid.");
            return;
        }

        ServiceType s = daftarSatuan.get(kode - 1);
        System.out.println("\n--- EDIT LAYANAN: " + s.getName() + " ---");
        System.out.println("1. Ubah nama");
        System.out.println("2. Ubah harga");
        System.out.println("3. Ubah satuan");
        System.out.println("4. Toggle aktif/nonaktif");
        System.out.println("5. Batal");
        
        int pilih = bacaInt("Pilih (1-5): ");
        
        switch (pilih) {
            case 1:
                System.out.print("Nama baru: ");
                String namaBaru = sc.nextLine().trim();
                s.setName(namaBaru);
                System.out.println("✓ Nama layanan diubah menjadi: " + namaBaru);
                break;
            case 2:
                int hargaBaru = bacaInt("Harga baru (Rp): ");
                s.setPrice(hargaBaru);
                System.out.println("✓ Harga layanan diubah menjadi: Rp " + hargaBaru);
                break;
            case 3:
                System.out.print("Satuan baru: ");
                String satuanBaru = sc.nextLine().trim();
                s.setSatuan(satuanBaru);
                System.out.println("✓ Satuan layanan diubah menjadi: " + satuanBaru);
                break;
            case 4:
                s.setActive(!s.isActive());
                String newStatus = s.isActive() ? "AKTIF" : "NONAKTIF";
                System.out.println("✓ Status layanan '" + s.getName() + "' diubah menjadi: " + newStatus);
                break;
            case 5:
                System.out.println("Dibatalkan.");
                break;
            default:
                System.out.println("Pilihan tidak valid.");
        }
    }
}