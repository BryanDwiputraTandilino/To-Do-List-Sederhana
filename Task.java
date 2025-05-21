public class Task {
    private String description;
    private boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false; // Secara default, tugas baru belum selesai
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    public void markAsDone() {
        this.isDone = true;
    }

    // Metode untuk representasi string dari objek Task, penting untuk penyimpanan
    // file
    @Override
    public String toString() {
        return (isDone ? "[DONE] " : "") + description;
    }
}