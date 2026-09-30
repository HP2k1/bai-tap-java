import java.util.Scanner;

public class Bai02 {
    // volatile bảo đảm Task2 thấy giá trị mới nhất do Task1 gán.
    static volatile String st = "";

    static void task1() {
        try (Scanner input = new Scanner(System.in)) {
            while (true) {
                System.out.println("Task1: nhap chuoi (bye de dung):");
                if (!input.hasNextLine()) { st = "bye"; break; }
                st = input.nextLine().strip();
                if (st.equals("bye")) break;
            }
        }
    }

    static void task2() {
        try {
            while (true) {
                String snapshot = st;
                System.out.println("Task2: st = [" + snapshot + "]");
                if (snapshot.equals("bye")) break;
                Thread.sleep(200);
            }
        } catch (InterruptedException e) { Thread.currentThread().interrupt(); }
    }

    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(Bai02::task1, "Task1");
        Thread t2 = new Thread(Bai02::task2, "Task2");
        t1.start();
        t2.start();
        t1.join();
        t2.join();
        System.out.println("Bai02: ca hai task da dung.");
    }
}
