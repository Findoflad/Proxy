/*
Паттерн "Прокси" (Proxy) - это структурный паттерн проектирования,
который предоставляет заместитель или заполнитель для другого объекта, чтобы контролировать доступ к нему.
Прокси может выступать в роли заместителя, предоставляя тот же интерфейс, что и реальный объект,
или выполнять дополнительные функции, например, кеширование запросов, ленивая инициализация, контроль доступа и т. д.

Основные компоненты паттерна "Прокси":

    Субъект (Subject): Определяет общий интерфейс для реальных и прокси объектов.
        Это может быть интерфейс или абстрактный класс.

    Реальный субъект (Real Subject): Реальный объект, доступ к которому контролируется прокси.

    Прокси (Proxy): Хранит ссылку на реальный субъект и реализует тот же интерфейс, что и субъект.
        Прокси может контролировать доступ к реальному субъекту и добавлять дополнительное поведение.
*/

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

class Server {
    public boolean saveUser(int id) {
        // Логика сохранения пользователя
        return true;
    }

    public Object getUser(int id) {
        // Логика получения пользователя
        return new Object();
    }

    // Прочие методы сервера
}

interface IServerProxy {
    boolean saveUser(int id);
    Object getUser(int id);
}

class ServerProxy implements IServerProxy {
    private enum Response { SUCCESS, ERROR }

    private static class LogEntry {
        private Date date;
        private String methodName;
        private Response response;

        LogEntry(String methodName, Response response) {
            this.date = new Date();
            this.methodName = methodName;
            this.response = response;
        }

        @Override
        public String toString() {
            return "Date: " + date + ", Method: " + methodName + ", Response: " + response;
        }
    }

    private List<LogEntry> logs = new ArrayList<>();
    private Server server = new Server();

    public String getLogs() {
        return logs.stream().map(LogEntry::toString)
                .collect(Collectors.joining("\n"));
    }

    @Override
    public boolean saveUser(int id) {
        boolean result = server.saveUser(id);
        Response response = (result) ? Response.SUCCESS : Response.ERROR;
        logs.add(new LogEntry("SaveUser", response));

        return result;
    }

    @Override
    public Object getUser(int id) {
        Object result = server.getUser(id);
        Response response = (result != null) ? Response.SUCCESS : Response.ERROR;
        logs.add(new LogEntry("GetUser", response));

        return result;
    }
}

public class Main {
    public static void main(String[] args) {
        IServerProxy server = new ServerProxy();

        server.saveUser(1);
        server.saveUser(2);
        server.saveUser(3);

        server.getUser(1);
        server.getUser(4);

        System.out.println(((ServerProxy) server).getLogs());
    }
}
