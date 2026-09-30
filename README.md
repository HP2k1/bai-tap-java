# Bài tập Lập trình hệ thống

- Họ tên: Phạm Viết Hiếu
- Mã sinh viên: 19810310671
- Lớp: DH.10
- Ngôn ngữ: Java

## Các bài tập

| Bài | File | Nội dung |
|---|---|---|
| 01 | `src/CT1.java`, `src/CT2.java` | Hai chương trình trao đổi số nguyên qua file `dulieu.dat` |
| 02 | `src/Bai02.java` | Hai luồng nhập và hiển thị chuỗi, dừng khi nhập `bye` |
| 03 | `src/Bai03.java` | Hai timer đọc ký tự, in mã hexa và phát beep, dừng khi nhập `*` |
| 04 | `src/Bai04.java` | Sinh số ngẫu nhiên, hiển thị số lớn nhất và lớn nhì |

## Cách chạy

Cài JDK 17. Mở terminal tại thư mục chứa README rồi biên dịch:

```sh
javac -encoding UTF-8 -d out src/*.java
```

**Bài 01:** Mở hai terminal trong cùng thư mục, chạy CT2 ở terminal thứ nhất và CT1 ở terminal thứ hai:

```sh
java -cp out CT2
```

```sh
java -cp out CT1
```

Trước khi chạy lại, đóng hai chương trình và xóa file `dulieu.dat` cũ nếu có.

**Bài 02:**

```sh
java -cp out Bai02
```

Nhập chuỗi rồi nhấn Enter. Nhập `bye` để kết thúc.

**Bài 03:**

```sh
java -cp out Bai03
```

Nhập ký tự rồi nhấn Enter. Nhập `*` để kết thúc.

**Bài 04:**

```sh
java -cp out Bai04
```

Chương trình tự dừng khi sinh được số lớn hơn 10000 và chia hết cho 2021.

## Ghi chú

- Bài 01: CT1 mặc định ghi cả số kết thúc để CT2 nhận được và dừng. Nếu chạy `java -cp out CT1 --strict`, CT1 dừng trước khi ghi số đó theo nguyên văn đề; khi đó phải dùng Ctrl+C để dừng CT2.
- Bài 03: Timer1 chạy mỗi 15ms, Timer2 mỗi 7ms. Mỗi lần timer thực hiện một bước xử lý để không bị chặn bởi vòng lặp chờ nhập. Tiếng beep phụ thuộc terminal có hỗ trợ và bật chuông hay không.
- Bài 04: Số lớn nhì được tính là giá trị lớn thứ hai khác số lớn nhất.
