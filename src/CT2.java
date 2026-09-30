import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;

public class CT2 {
    static Integer doc() throws IOException {
        try (FileChannel f = FileChannel.open(Path.of("dulieu.dat"),
                StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
             FileLock lock = f.lock()) {
            if (f.size() != 4) return null;
            ByteBuffer b = ByteBuffer.allocate(4);
            while (b.hasRemaining()) if (f.read(b) == -1) return null;
            b.flip();
            return b.getInt();
        }
    }

    static void docSo() {
        try {
            while (true) {
                Integer n = doc(); // File đã đóng trước khi hiển thị.
                if (n != null) {
                    System.out.println("CT2: doc duoc " + n);
                    if (n % 2021 == 0) {
                        System.out.println("CT2: ket thuc.");
                        break;
                    }
                }
                Thread.sleep(10); // Tránh vòng chờ chiếm toàn bộ CPU.
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException("Khong doc duoc dulieu.dat", e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t = new Thread(CT2::docSo, "CT2-reader");
        t.setUncaughtExceptionHandler((thread, error) -> { error.printStackTrace(); System.exit(1); });
        t.start();
        t.join();
    }
}
