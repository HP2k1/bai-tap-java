import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Random;

public class CT1 {
    // int Java có đúng 4 byte. File lưu big-endian, mỗi lần chỉ giữ một số.
    static void ghi(int n) throws IOException {
        try (FileChannel f = FileChannel.open(Path.of("dulieu.dat"),
                StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
             FileLock lock = f.lock()) {
            // Chỉ xóa nội dung sau khi đã lấy khóa để CT2 không đọc dở dang.
            f.truncate(0);
            f.position(0);
            ByteBuffer b = ByteBuffer.allocate(4).putInt(n);
            b.flip();
            while (b.hasRemaining()) f.write(b);
        }
    }

    static void taoSo(boolean strict, Random random) {
        try {
            while (true) {
                int n = random.nextInt(20211); // [0, 20210], toàn số không âm
                if (n % 2021 == 0) {
                    if (!strict) ghi(n); // Gửi giá trị kết thúc cho CT2.
                    System.out.println("CT1: dung tai " + n
                            + (strict ? " (khong ghi: dung nguyen van de)" : " (da ghi tin hieu dung)"));
                    break;
                }
                ghi(n);
                System.out.println("CT1: da ghi " + n);
                Thread.sleep(2);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (IOException e) {
            throw new RuntimeException("Khong ghi duoc dulieu.dat", e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        boolean strict = false;
        Random random = new Random();
        for (String arg : args) {
            if (arg.equals("--strict")) strict = true;
            else if (arg.startsWith("--seed=")) random = new Random(Long.parseLong(arg.substring(7)));
            else throw new IllegalArgumentException("Tham so: --strict, --seed=SO");
        }
        final boolean s = strict;
        final Random r = random;
        Thread t = new Thread(() -> taoSo(s, r), "CT1-writer");
        t.setUncaughtExceptionHandler((thread, error) -> { error.printStackTrace(); System.exit(1); });
        t.start();
        t.join();
    }
}
