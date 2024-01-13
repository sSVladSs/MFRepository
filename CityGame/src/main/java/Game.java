import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class Game {
    public Game(Scanner scanner) {
        this.scanner = scanner;
    }

    Scanner scanner;
    private static int COUNTER = 0;
    private static String temp;
    private static final List<String> REPLAY = new ArrayList<>();
    private static final List<String> KINGDOMS = new ArrayList<>(getKingdoms());

    private static List<String> getKingdoms() {
        Path KINGDOMS = Path.of("src/main/resources/country.txt");
        try {
            return Files.readAllLines(KINGDOMS);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static final List<String> CITIES = new ArrayList<>(getCities());

    private static List<String> getCities() {
        Path CITIES = Path.of("src/main/resources/cities.txt");
        try {
            return Files.readAllLines(CITIES);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /*
               Начало действий!
         */
    public void startGame(Scanner scanner) {
        String regex = "([1-5])";
        String input;

        System.out.println("Введите:");
        System.out.println("\"1\" - Для одиночной игры ");
        System.out.println("\"2,3,4,5\" - Для многопользовательской игры ");
        System.out.println();

        while (!scanner.hasNext(regex)) {
            System.out.println("Не корректный ввод, повторите попытку -> ");
            scanner.next();
        }
        input = scanner.next();

        while (input.equals("1")) {
            userInput(scanner);
        }

        Queue<Player> players = createPlayer(input);
        play(players);
    }

    private void play(Queue<Player> pl) {
        Queue<Player> players = new ArrayDeque<>(pl);
        String exit = "Start";
        Player winner = new Player("Winner");
        winner.setCounter(0);
        while (!exit.equals("Выход")) {
            Player peek = players.remove();
            exit = playerInput(peek);
            players.add(peek);
        }
        System.out.println(players);
        for (Player player : players) {
            if (player.getCounter() > winner.getCounter()) {
                winner = player;
            }
        }
        System.out.println("------------------------------>");
        System.out.println("Победитель: " + winner);
    }

    private Queue<Player> createPlayer(String input) {
        int count = Integer.parseInt(input);
        int x = 1;
        Queue<Player> players = new ArrayDeque<>();
        while (x <= count) {
            String name;
            Player player = new Player(null);
            do {
                System.out.println("Введите имя " + x + "го игрока.");
                name = scanner.next();
                player.setName(name);
                if (players.contains(player)) {
                    System.out.println("Такой игрок уже существует!");
                }
            } while (players.contains(player));
            players.add(player);
            x++;
        }
        System.out.println();

        return players;
    }

    private void userInput(Scanner scanner) {
        if (COUNTER == 0) {
            System.out.println("Одиночная игра с компьютером!");
        }
        System.out.println("-----------------------------");

        String regex = "([А-Яа-я])+-?([А-Яа-яё])+-?([А-Яа-яё])+?";
        String result;
        do {
            do {
                do {
                    do {
                        System.out.println("Введите название города ->");
                        while (!scanner.hasNext(regex)) {
                            System.out.println("Не корректный ввод, повторите попытку -> ");
                            scanner.next();
                        }
                        result = scanner.next();

                        if (containsKingdoms(result)) {
                            System.out.println("Вы ввели название страны, пожалуйста введите город!");
                        }
                        if (result.equals("Выход")) {
                            break;
                        }

                    } while (containsKingdoms(result));

                } while (containsCity(result));

            } while (isReplayInput(result));

        } while (isEndChar(result, temp));

        COUNTER++;
        System.out.println("Количество угаданных городов: " + COUNTER);

        temp = getAnswer(result);
    }

    private String playerInput(Player player) {
        System.out.println("-----------------------------");
        System.out.println("Ход игрока: " + player.getName());

        String regex = "([А-Яа-я])+-?([А-Яа-яё])+-?([А-Яа-яё])+?";
        String result;
        do {
            do {
                do {
                    do {
                        System.out.println("Введите название города ->");
                        while (!scanner.hasNext(regex)) {
                            System.out.println("Не корректный ввод, повторите попытку -> ");
                            scanner.next();
                        }
                        result = scanner.next();

                        if (containsKingdoms(result)) {
                            System.out.println("Вы ввели название страны, пожалуйста введите город!");
                        }
                        if (result.equals("Выход")) {
                            return result;
                        }

                    } while (containsKingdoms(result));

                } while (containsCity(result));

            } while (isReplayInput(result));

        } while (isEndChar(result, temp));

        temp = result;
        player.incrementCounter();
        System.out.println("Количество угаданных городов: " + player.getCounter());
        return result;
    }

    private boolean isEndChar(String result, String start) {
        if (start == null) {
            start = result.substring(0, 1).toLowerCase();
        }
        String out;
        char temp = start.charAt(start.length() - 1);
        if (temp == 'ъ' || temp == 'ь' || temp == 'ё' || temp == 'ы') {
            out = start.substring(start.length() - 2, start.length() - 1).toUpperCase();
        } else {
            out = start.substring(start.length() - 1).toUpperCase();
        }

        if (result.startsWith(out)) {
            return false;
        } else {
            System.out.println("Название данного города не начинается на букву: " + out);
            REPLAY.remove(result);
            return true;
        }
    }

    private String getAnswer(String player) {
        char temp = player.charAt(player.length() - 1);
        String end;
        String result = null;

        if (temp == 'ъ' || temp == 'ь' || temp == 'ё') {
            end = player.substring(player.length() - 2, player.length() - 1).toUpperCase();
        } else {
            end = player.substring(player.length() - 1).toUpperCase();
        }

        Random random = new Random();
        int count = 0;
        List<String> list = new ArrayList<>();
        for (String city : CITIES) {
            if (city.startsWith(end)) {
                list.add(city);
            }
        }
        int r = random.nextInt(0, list.size());
        while (count < list.size()) {
            if (!isReplayInput(list.get(r))) {
                result = list.get(r);
                System.out.println("Ответ: " + result);
                CITIES.remove(result);
                break;
            }
            count++;
        }
        return result;
    }

    private boolean isReplayInput(String result) {
        if (REPLAY.contains(result)) {
            List<String> list = new ArrayList<>();
            for (String s : REPLAY) {
                if (s.startsWith(result.substring(0, 1))) {
                    list.add(s);
                }
            }
            System.out.println("Данный город уже называли!");
            System.out.println(list);
            System.out.println("Попробуйте снова!");
            System.out.println();
            return true;
        } else {
            REPLAY.add(result);
            return false;
        }
    }

    private boolean containsCity(String city) {
        if (CITIES.contains(city)) {
            return false;
        } else {
            System.out.println("Такого города не существует!");
            return true;
        }
    }

    private boolean containsKingdoms(String result) {
        return KINGDOMS.contains(result);
    }
}