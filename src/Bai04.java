import java.util.Random;

public class Bai04 {
    static final Object lock = new Object();
    static Integer max1 = null, max2 = null;
    static boolean done = false;
    static long count = 0;

    // Quy ước: số lớn nhì là giá trị PHÂN BIỆT lớn thứ hai.
    static void capNhat(int n) {
        if (max1 == null || n > max1) {
            max2 = max1;
            max1 = n;
        } else if (n < max1 && (max2 == null || n > max2)) max2 = n;
    }

    static void task1(Random random) {
        try {
            while (true) {
                int n = random.nextInt(20001);
                synchronized (lock) {
                    capNhat(n); // Tính cả số làm dừng vòng lặp.
                    count++;
                    if (n > 10000 && n % 2021 == 0) {
                        System.out.println("Task1: so dung = " + n);
                        break;
                    }
                }
                Thread.sleep(1);
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
        finally { synchronized (lock) { done = true; lock.notifyAll(); } }
    }

    static void task2() {
        try {
            synchronized (lock) {
                while (true) {
                    System.out.printf("Task2: count=%d, max1=%s, max2=%s%n", count,
                            max1 == null ? "chua co" : max1,
                            max2 == null ? "chua co" : max2);
                    System.out.print('\007');
                    System.out.flush();
                    if (done) break;
                    lock.wait(100); // Nhả khóa cho Task1; thức ngay khi Task1 kết thúc.
                }
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public static void main(String[] args) throws InterruptedException {
        Random r = args.length == 0 ? new Random() : new Random(Long.parseLong(args[0]));
        Thread t1 = new Thread(() -> task1(r), "Task1");
        Thread t2 = new Thread(Bai04::task2, "Task2");
        t2.start();
        t1.start();
        t1.join();
        t2.join();
        System.out.println("Bai04: ca hai task da dung.");
    }
}
