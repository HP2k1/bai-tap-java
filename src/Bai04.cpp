#include <windows.h>
#include <chrono>
#include <condition_variable>
#include <cstdint>
#include <iostream>
#include <limits>
#include <mutex>
#include <random>
#include <thread>

std::mutex khoa;
std::condition_variable dieuKien;

std::int32_t lonNhat = 0;
std::int32_t lonNhi = 0;

bool coLonNhat = false;
bool coLonNhi = false;
bool daDung = false;

void task1() {
    std::mt19937 random(std::random_device{}());
    std::uniform_int_distribution<std::int32_t> phanPhoi(
        0, (std::numeric_limits<std::int32_t>::max)()
    );

    while (true) {
        std::int32_t n = phanPhoi(random);

        {
            std::lock_guard<std::mutex> guard(khoa);

            if (!coLonNhat || n > lonNhat) {
                if (coLonNhat) {
                    lonNhi = lonNhat;
                    coLonNhi = true;
                }

                lonNhat = n;
                coLonNhat = true;
            } else if (n < lonNhat &&
                       (!coLonNhi || n > lonNhi)) {
                lonNhi = n;
                coLonNhi = true;
            }

            if (n > 10000 && n % 2021 == 0) {
                std::cout << "So dung: " << n << '\n';
                break;
            }
        }

        std::this_thread::sleep_for(std::chrono::milliseconds(1));
    }

    {
        std::lock_guard<std::mutex> guard(khoa);
        daDung = true;
    }

    dieuKien.notify_all();
}

void task2() {
    std::unique_lock<std::mutex> guard(khoa);

    while (true) {
        std::cout << "So lon nhat: ";

        if (coLonNhat)
            std::cout << lonNhat;
        else
            std::cout << "Chua co";

        std::cout << " | So lon nhi: ";

        if (coLonNhi)
            std::cout << lonNhi;
        else
            std::cout << "Chua co";

        std::cout << '\n';

        bool dung = daDung;

        guard.unlock();
        Beep(750, 30);
        guard.lock();

        if (dung)
            break;

        dieuKien.wait_for(
            guard,
            std::chrono::milliseconds(100),
            [] { return daDung; }
        );
    }
}

int main() {
    std::thread t1(task1);
    std::thread t2(task2);

    t1.join();
    t2.join();

    return 0;
}
