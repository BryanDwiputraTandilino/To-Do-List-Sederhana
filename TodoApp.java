import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TodoApp {

    private static final String FILE_NAME = "tasks.txt";
    private List<Task> tasks;
    private Scanner scanner;

    public TodoApp() {
        tasks = new ArrayList<>();
        scanner = new Scanner(System.in);
        loadTasks(); // Muat tugas saat aplikasi dimulai
    }

    // --- Fungsionalitas Core To-Do List ---

    public void addTask(String description) {
        if (description == null || description.trim().isEmpty()) {
            System.out.println("Deskripsi tugas tidak boleh kosong.");
            return;
        }
        tasks.add(new Task(description));
        System.out.println("Tugas berhasil ditambahkan: " + description);
        saveTasks();
    }

    public void listTasks() {
        if (tasks.isEmpty()) {
            System.out.println("\nTidak ada tugas dalam daftar.");
            return;
        }
        System.out.println("\n--- DAFTAR TUGAS ---");
        for (int i = 0; i < tasks.size(); i++) {
            Task task = tasks.get(i);
            System.out.println((i + 1) + ". " + task.toString());
        }
        System.out.println("--------------------");
    }

    public void markTaskAsDone(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("Nomor tugas tidak valid.");
            return;
        }
        Task task = tasks.get(taskNumber - 1);
        if (task.isDone()) {
            System.out.println("Tugas '" + task.getDescription() + "' sudah selesai.");
        } else {
            task.markAsDone();
            System.out.println("Tugas '" + task.getDescription() + "' ditandai sebagai selesai.");
            saveTasks();
        }
    }

    public void deleteTask(int taskNumber) {
        if (taskNumber < 1 || taskNumber > tasks.size()) {
            System.out.println("Nomor tugas tidak valid.");
            return;
        }
        Task removedTask = tasks.remove(taskNumber - 1);
        System.out.println("Tugas '" + removedTask.getDescription() + "' berhasil dihapus.");
        saveTasks();
    }

    // --- Penyimpanan dan Pemuatan File ---

    private void loadTasks() {
        try (BufferedReader reader = new BufferedReader(new FileReader(FILE_NAME))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("[DONE] ")) {
                    String description = line.substring("[DONE] ".length());
                    Task task = new Task(description);
                    task.markAsDone(); // Tandai sebagai selesai jika ada penanda
                    tasks.add(task);
                } else {
                    tasks.add(new Task(line));
                }
            }
        } catch (FileNotFoundException e) {
            // File belum ada, tidak masalah, daftar tugas akan kosong
            System.out.println("File tugas belum ditemukan. Membuat yang baru.");
        } catch (IOException e) {
            System.err.println("Error saat membaca file: " + e.getMessage());
        }
    }

    private void saveTasks() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_NAME))) {
            for (Task task : tasks) {
                writer.write(task.toString()); // Gunakan toString() dari objek Task
                writer.newLine();
            }
        } catch (IOException e) {
            System.err.println("Error saat menulis ke file: " + e.getMessage());
        }
    }

    // --- Logika Menu Utama CLI ---

    public void run() {
        while (true) {
            System.out.println("\n--- MENU TO-DO LIST ---");
            System.out.println("1. Tambah Tugas");
            System.out.println("2. Daftar Tugas");
            System.out.println("3. Tandai Selesai");
            System.out.println("4. Hapus Tugas");
            System.out.println("5. Keluar");
            System.out.print("Pilih opsi: ");

            String choice = scanner.nextLine();

            switch (choice) {
                case "1":
                    System.out.print("Masukkan deskripsi tugas baru: ");
                    String description = scanner.nextLine();
                    addTask(description);
                    break;
                case "2":
                    listTasks();
                    break;
                case "3":
                    listTasks(); // Tampilkan dulu daftar tugas agar pengguna tahu nomornya
                    if (!tasks.isEmpty()) {
                        System.out.print("Masukkan nomor tugas yang selesai: ");
                        try {
                            int taskNum = Integer.parseInt(scanner.nextLine());
                            markTaskAsDone(taskNum);
                        } catch (NumberFormatException e) {
                            System.out.println("Input tidak valid. Harap masukkan angka.");
                        }
                    }
                    break;
                case "4":
                    listTasks(); // Tampilkan dulu daftar tugas agar pengguna tahu nomornya
                    if (!tasks.isEmpty()) {
                        System.out.print("Masukkan nomor tugas yang akan dihapus: ");
                        try {
                            int taskNum = Integer.parseInt(scanner.nextLine());
                            deleteTask(taskNum);
                        } catch (NumberFormatException e) {
                            System.out.println("Input tidak valid. Harap masukkan angka.");
                        }
                    }
                    break;
                case "5":
                    System.out.println("Terima kasih telah menggunakan To-Do List!");
                    scanner.close(); // Tutup scanner sebelum keluar
                    return; // Keluar dari loop dan program
                default:
                    System.out.println("Pilihan tidak valid. Silakan coba lagi.");
            }
        }
    }

    public static void main(String[] args) {
        TodoApp app = new TodoApp();
        app.run();
    }
}