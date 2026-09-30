#include <chrono>
#include <iostream>
#include <mutex>
#include <string>
#include <thread>

std::string st;
std::mutex khoa;

std::string catDauCach(const std::string& s) {
    std::size_t dau = s.find_first_not_of(" \t\r\n");

    if (dau == std::string::npos)
        return "";

    std::size_t cuoi = s.find_last_not_of(" \t\r\n");
    return s.substr(dau, cuoi - dau + 1);
}

void task1() {
    while (true) {
        std::string input;

        {
            std::lock_guard<std::mutex> guard(khoa);
            std::cout << "Nhap chuoi: " << std::flush;
        }

        if (!std::getline(std::cin, input))
            input = "bye";

        input = catDauCach(input);

        {
            std::lock_guard<std::mutex> guard(khoa);
            st = input;
        }

        if (input == "bye")
            break;
    }
}

void task2() {
    while (true) {
        {
            std::lock_guard<std::mutex> guard(khoa);

            std::cout << "Chuoi hien tai: " << st << '\n';

            if (st == "bye")
                break;
        }

        std::this_thread::sleep_for(std::chrono::milliseconds(200));
    }
}

int main() {
    std::thread t1(task1);
    std::thread t2(task2);

    t1.join();
    t2.join();

    return 0;
}
