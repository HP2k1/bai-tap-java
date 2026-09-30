#include <windows.h>
#include <conio.h>
#include <iomanip>
#include <iostream>
#include <mutex>

char c = '\0';
std::mutex khoa;
HANDLE ketThuc = nullptr;

VOID CALLBACK nhapKyTu(PVOID, BOOLEAN) {
    std::lock_guard<std::mutex> guard(khoa);

    if (c == '*' || !_kbhit())
        return;

    int value = _getch();

    if (value == 0 || value == 224) {
        _getch();
        return;
    }

    c = static_cast<char>(value);

    std::cout << "Ky tu: " << c
              << " - Hexa: 0x"
              << std::uppercase << std::hex
              << std::setw(2) << std::setfill('0')
              << static_cast<unsigned int>(
                     static_cast<unsigned char>(c))
              << std::dec << std::setfill(' ')
              << '\n';

    if (c == '*')
        SetEvent(ketThuc);
}

VOID CALLBACK phatBeep(PVOID, BOOLEAN) {
    std::lock_guard<std::mutex> guard(khoa);

    if (c != '*')
        Beep(750, 1);
}

int main() {
    HANDLE timer1 = nullptr;
    HANDLE timer2 = nullptr;

    ketThuc = CreateEventA(nullptr, TRUE, FALSE, nullptr);

    if (ketThuc == nullptr)
        return 1;

    HANDLE hangDoi = CreateTimerQueue();

    if (hangDoi == nullptr) {
        CloseHandle(ketThuc);
        return 1;
    }

    std::cout << "Nhap ky tu. Nhan * de dung.\n";

    BOOL taoTimer1 = CreateTimerQueueTimer(
        &timer1,
        hangDoi,
        nhapKyTu,
        nullptr,
        0,
        15,
        WT_EXECUTEDEFAULT
    );

    if (!taoTimer1) {
        DeleteTimerQueueEx(hangDoi, INVALID_HANDLE_VALUE);
        CloseHandle(ketThuc);
        return 1;
    }

    BOOL taoTimer2 = CreateTimerQueueTimer(
        &timer2,
        hangDoi,
        phatBeep,
        nullptr,
        0,
        7,
        WT_EXECUTEDEFAULT
    );

    if (!taoTimer2) {
        DeleteTimerQueueEx(hangDoi, INVALID_HANDLE_VALUE);
        CloseHandle(ketThuc);
        return 1;
    }

    WaitForSingleObject(ketThuc, INFINITE);

    DeleteTimerQueueEx(hangDoi, INVALID_HANDLE_VALUE);
    CloseHandle(ketThuc);

    return 0;
}
