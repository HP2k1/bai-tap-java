#include <windows.h>
#include <cstdint>
#include <iostream>
#include <limits>
#include <random>
#include <thread>

void taoVaGhiSo() {
    std::mt19937 random(std::random_device{}());
    std::uniform_int_distribution<std::int32_t> phanPhoi(
        0, (std::numeric_limits<std::int32_t>::max)()
    );

    while (true) {
        std::int32_t n = phanPhoi(random);

        if (n % 2021 == 0)
            break;

        HANDLE file = CreateFileA(
            "dulieu.dat",
            GENERIC_WRITE,
            0,
            nullptr,
            CREATE_ALWAYS,
            FILE_ATTRIBUTE_NORMAL,
            nullptr
        );

        if (file == INVALID_HANDLE_VALUE) {
            Sleep(10);
            continue;
        }

        DWORD soByteDaGhi = 0;
        BOOL thanhCong = WriteFile(
            file, &n, sizeof(n), &soByteDaGhi, nullptr
        );

        CloseHandle(file);

        if (!thanhCong || soByteDaGhi != sizeof(n)) {
            std::cerr << "Khong ghi duoc du lieu.\n";
            return;
        }
    }
}

int main() {
    std::thread t(taoVaGhiSo);
    t.join();
    return 0;
}
