# PhoneBooksServer

## Программа предоставляет REST API для:
- получения списка всех пользователей (владельцев телефонных книжек)
- создания, получения (по id), удаления, редактирования пользователя
- создания, получения (по id), удаления, редактирования записи в телефонной книжке
- получения списка всех записей в телефонной книжке пользователя
- поиск пользователей по имени (или его части)*, поиск телефонной записи по номеру телефона

## Использованы следующие технологии: 
- Java, 
- Spring Boot, 
- JUnit.

## Инструкция по запуску программы:
1. Скачать все файлы из репозитория или клонировать его,
2. Из командной строки перейти в папку с файлами проекта,
3. Собрать и запустить приложение с помощью команды: 
   - *mvnw spring-boot:run* для Windows   
   - *./mvnw spring-boot:run* для MacOS/Linux
