# Модуль 2.4: HTTP + Сервлеты

### Задача 

Необходимо реализовать REST API, которое взаимодействует с файловым хранилищем и предоставляет возможность получать доступ к файлам и истории загрузок.

**Исходные данные**

Сущности:
* User -> Integer id, String name, List<Event> events
* Event -> Integer id, User user, File file
* File -> Integer id, String name, String filePath

Требования:
* Все CRUD операции для каждой из сущностей
* Придерживаться подхода MVC
* Для сборки проекта использовать Maven
* Для взаимодействия с БД - Hibernate
* Для конфигурирования Hibernate - аннотации
* Инициализация БД должна быть реализована с помощью flyway
* Взаимодействие с пользователем необходимо реализовать с помощью Postman

Технологии: Java, MySQL, Hibernate, HTTP, Servlets, Maven, Flyway, Swagger.

### Элементы реализации

**Организация базы данных**
<img alt="DB-Scheme.jpg" height="463" src="DB-Scheme.jpg" width="540"/>

**Организация REST API**


