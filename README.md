# NerkhNameh | نرخ‌نامه

**NerkhNameh** is a professional financial Android application designed to track real-time gold prices, various coins, and global market rates. It features a custom analysis engine to help users make informed investment decisions.

---

## 📸 Screenshots

| Dashboard | Market Analysis | Price View |
| :---: | :---: | :---: |
| ![Main Screen](https://github.com/hosseinDevAt/Nerkh-Naame/blob/b09c65621cb0e49325ba8553524f49f9a4e31c59/Picture1.png) | ![Analysis View](https://github.com/hosseinDevAt/Nerkh-Naame/blob/b09c65621cb0e49325ba8553524f49f9a4e31c59/Picture3.png) | ![Price View](https://github.com/hosseinDevAt/Nerkh-Naame/blob/b09c65621cb0e49325ba8553524f49f9a4e31c59/Picture2.png) |

---

## ✨ Key Features

* **Real-time Rates:** Live tracking of domestic gold, coins, and global gold spot prices.
* **Date Display:** Shows the current Persian and Gregorian dates within the app.
* **Smart Analysis Section:** Integration with a custom **Laravel API** that records and displays the price at the exact moment of analysis.
* **Custom Visualization:** Beautifully rendered price charts built using **Canvas**.
* **Investment Signals:** Automated "Buy", "Sell", or "Wait" suggestions based on market data.
* **Advanced Connectivity Handling:** High-level internet status detection for a seamless user experience.

---

## 🛠 Tech Stack & Architecture

This project is built using modern Android development standards to ensure scalability and performance:

* **Architecture:** Clean Architecture + MVVM (Model-View-ViewModel).
* **UI Framework:** Jetpack Compose with **Single Activity** pattern.
* **Dependency Injection:** Dagger Hilt.
* **Networking:** Retrofit for efficient API communication.
* **Navigation:** Navigation Compose for fluid screen transitions.
* **Graphics:** Custom-built charts using the **Canvas** API.

---

## 🔗 Backend API

The analysis module of this application relies on a dedicated backend built with Laravel. This API handles the logic for market snapshots and price analysis.

**[View Laravel API Repository](https://github.com/hosseinDevAt/Nerkh-app-api)**

---

## 🚀 Installation & Setup

1. Clone the repository: `git clone https://github.com/hosseinDevAt/Nerkh-Naame.git`
2. Open the project in **Android Studio**.
3. Sync Gradle and run the application on an emulator or physical device.
