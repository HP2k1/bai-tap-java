import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.CountDownLatch;

public class Bai03 {
    static volatile char c = '\0';
    static final CountDownLatch done = new CountDownLatch(1);
    static final BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    static volatile IOException failure;

    // Timer thực hiện từng bước của vòng lặp, không chặn luồng timer để chờ nhập.
    static void nhapMotBuoc() {
        try {
            if (c == '*' || !input.ready()) return;
            int value = input.read();
            if (value == -1) { c = '*'; done.countDown(); return; }
            if (value == '\r' || value == '\n') return;
            c = (char) value;
            System.out.printf("Timer1: c = '%c', hexa = 0x%04X%n", c, (int) c);
            if (c == '*') done.countDown();
        } catch (IOException e) {
            failure = e;
            c = '*';
            done.countDown();
        }
    }

    static void beepMotBuoc() {
        if (c == '*') return;
        System.out.print('\007'); // BEL: terminal hỗ trợ âm thanh sẽ phát beep.
        System.out.flush();
    }

    public static void main(String[] args) throws InterruptedException, IOException {
        Timer timer1 = new Timer("Timer1");
        Timer timer2 = new Timer("Timer2");
        System.out.println("Timer1 = 15ms; Timer2 = 7ms. Nhap ky tu roi Enter; * de dung.");
        try {
            timer1.scheduleAtFixedRate(new TimerTask() {
                public void run() { nhapMotBuoc(); }
            }, 0, 15);
            timer2.scheduleAtFixedRate(new TimerTask() {
                public void run() { beepMotBuoc(); }
            }, 0, 7);
            done.await();
        } finally {
            timer1.cancel();
            timer2.cancel();
        }
        if (failure != null) throw failure;
        System.out.println("Bai03: da huy ca hai timer.");
    }
}
