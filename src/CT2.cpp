#include <windows.h>
#include <cstdint>
#include <iostream>
#include <thread>

void docSo() {
    while (true) {
        HANDLE file = CreateFileA(
            "dulieu.dat",
            GENERIC_READ,
            0,
            nullptr,
            OPEN_EXISTING,
            FILE_ATTRIBUTE_NORMAL,
            nullptr
        );

        if (file == INVALID_HANDLE_VALUE) {
            Sleep(10);
            continue;
        }

        std::int32_t n = 0;
        DWORD soByteDaDoc = 0;

        BOOL thanhCong = ReadFile(
            file, &n, sizeof(n), &soByteDaDoc, nullptr
        );

        CloseHandle(file);

        if (thanhCong && soByteDaDoc == sizeof(n)) {
            std::cout << n << '\n';

            if (n % 2021 == 0)
                break;
        }

        Sleep(10);
    }
}

int main() {
    std::thread t(docSo);
    t.join();
    return 0;
}
