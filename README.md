# 🏠 HouseRenta

Мобильное Android-приложение для размещения и поиска объявлений об аренде жилья.

## 📱 О приложении

**HouseRenta** — платформа для аренды жилья, позволяющая арендодателям размещать объявления, а арендаторам — находить подходящее жильё и связываться с владельцами напрямую.

## ✨ Функциональность

- 🔐 Регистрация и вход с хэшированием паролей SHA-256
- 🏘️ Каталог всех объявлений с фотографиями
- ➕ Добавление объявления с загрузкой фото из галереи
- 📋 Управление собственными объявлениями (редактирование, удаление)
- 🔍 Детальный просмотр объявления
- 💬 Связь с арендодателем через встроенный мессенджер
- 🗺️ Карта на основе Яндекс MapKit
- 👤 Личный кабинет с настройками видимости контактов
- 🔒 Безопасное хранение сессии через SharedPreferences

## 🛠️ Стек технологий

| Компонент | Технология |
|-----------|-----------|
| Язык | Kotlin |
| UI | Jetpack Compose + Material3 |
| Архитектура | MVVM |
| Облачная БД | Firebase Realtime Database |
| Локальная БД | Room (SQLite) |
| Навигация | Navigation Compose |
| Карты | Яндекс MapKit |
| Сессия | SharedPreferences |

## 🏗️ Архитектура

```
house_rental_app/
├── dao/               # DAO-интерфейсы Room
├── data/              # ViewModels (MVVM)
├── database/          # Room Database
├── entity/            # Сущности (HouseEntity, UserEntity)
├── navigation/        # Граф навигации
├── repository/        # Репозитории (Firebase + Room)
├── theme/screens/     # Compose-экраны
├── FirebaseHelper.kt  # Облачные операции
├── SessionManager.kt  # Управление сессией
└── MainActivity.kt    # Точка входа
```

## 🚀 Установка и запуск

1. Клонируй репозиторий:
```bash
git clone https://github.com/solomaxuiuiyuyuyu-crypto/house-rental-app.git
```

2. Открой проект в **Android Studio Ladybug** или новее

3. Добавь файл `local.properties` с ключом Яндекс MapKit:
```
YANDEX_MAPKIT_API_KEY=ваш_ключ
```

4. Подключи `google-services.json` от Firebase в папку `app/`

5. Запусти на эмуляторе или устройстве с Android 8.0 (API 26)+

## 📋 Требования

- Android 8.0 (API 26) и выше
- Подключение к интернету (Firebase)
- Разрешение на доступ к галерее (для загрузки фото)

