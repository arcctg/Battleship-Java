package battleship;

import java.util.*;
import java.util.stream.Collectors;

public class App {
    private final Scanner scanner = new Scanner(System.in);
    private final String[][] firstField = {
            "\s 1 2 3 4 5 6 7 8 9 10".split(" "),
            "A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
    };

    private final String[][] secondField = {
            "\s 1 2 3 4 5 6 7 8 9 10".split(" "),
            "A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
    };

    private final String[][] foggedFirstField = {
            "\s 1 2 3 4 5 6 7 8 9 10".split(" "),
            "A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
    };

    private final String[][] foggedSecondField = {
            "\s 1 2 3 4 5 6 7 8 9 10".split(" "),
            "A ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "B ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "C ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "D ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "E ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "F ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "G ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "H ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "I ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
            "J ~ ~ ~ ~ ~ ~ ~ ~ ~ ~".split(" "),
    };

    private final List<String> ships = List.of("Aircraft Carrier (5 cells)", "Battleship (4 cells)",
            "Submarine (3 cells)", "Cruiser (3 cells)", "Destroyer (2 cells)");
    public void execute() {
        preparation(1, firstField);
        start();
    }

    private void start() {
        String message = "";

        while (!message.contains("won")) {
            print(foggedSecondField, firstField);

            System.out.println("\nPlayer 1, it's your turn:\n");
            message = getGameMessage(scanner.nextLine(), secondField, foggedSecondField);
            if (message.contains("won")) {
                break;
            }

            System.out.println(message + "\n\nPress Enter and pass the move to another player");
            scanner.nextLine();
            clear();

            print(foggedFirstField, secondField);

            System.out.println("\nPlayer 2, it's your turn:\n");
            message = getGameMessage(scanner.nextLine(), firstField, foggedFirstField);
            if (message.contains("won")) {
                break;
            }

            System.out.println(message + "\n\nPress Enter and pass the move to another player");
            scanner.nextLine();
            clear();
        }

        System.out.println(message);
    }

    private String getGameMessage(String cord, String[][] enemyField, String[][] foggedEnemyField) {
        boolean doo;
        String message = "";

        do {
            try {
                message = shot(cord, enemyField, foggedEnemyField);
            } catch (RuntimeException e) {
                doo = true;
                System.out.printf("\nError! %s Try again:\n\n", e.getMessage());
                cord = scanner.nextLine();
                continue;
            }
            doo = false;
        } while (doo);

        return message;
    }

    private String shot(String cord, String[][] enemyField, String[][] foggedEnemyField) throws RuntimeException {
        int a1 = Character.getNumericValue(cord.charAt(0)) - 9;
        int b1 = Integer.parseInt(cord.substring(1));
        String message;

        if (a1 <= 0 || b1 <= 0 || a1 >= 11 || b1 >= 11) {
            throw new RuntimeException("You entered wrong coordinates!");
        }

        if (enemyField[a1][b1].equals("O") || enemyField[a1][b1].equals("X")) {
            enemyField[a1][b1] = "X";
            foggedEnemyField[a1][b1] = "X";

            if (sankShip(a1, b1, enemyField)) {
                message = "\nYou sank a ship!";
            } else {
                message = "\nYou hit a ship!";
            }
        } else {
            enemyField[a1][b1] = "M";
            foggedEnemyField[a1][b1] = "M";
            message = "\nYou missed!";
        }

        if (checkShips(enemyField)) {
            return "\nYou sank the last ship. You won. Congratulations!";
        } else {
            return message;
        }
    }

    private boolean sankShip(int a1, int b1, String[][] enemyField) {
        String gg;

        for (int i = b1; i < 11; i++) {
            gg = enemyField[a1][i];
            if (gg.equals("O")) {
                return false;
            } else if (gg.equals("~") || gg.equals("M")) {
                break;
            }
        }

        for (int i = b1; i > -1; i--) {
            gg = enemyField[a1][i];
            if (gg.equals("O")) {
                return false;
            } else if (gg.equals("~") || gg.equals("M")) {
                break;
            }
        }

        for (int i = a1; i < 11; i++) {
            gg = enemyField[i][b1];
            if (gg.equals("O")) {
                return false;
            } else if (gg.equals("~") || gg.equals("M")) {
                break;
            }
        }

        for (int i = a1; i > -1; i--) {
            gg = enemyField[i][b1];
            if (gg.equals("O")) {
                return false;
            } else if (gg.equals("~") || gg.equals("M")) {
                break;
            }
        }

        return true;
    }

    private boolean checkShips(String[][] field) {
        List<String> collection = Arrays.stream(field)
                .flatMap(Arrays::stream)
                .collect(Collectors.toList());
        return !collection.contains("O");
    }

    private void preparation(int player, String[][] field) {
        System.out.printf("Player %d, place your ships on the game field\n", player);
        print(field);
        for (int i = 0; i < 5; i++) {
            Ship ship = createShip(i);
            int[] arr = new int[0];
            String cords;
            boolean go;

            System.out.printf("\nEnter the coordinates of the %s:\n\n", ships.get(i));
            cords = scanner.nextLine();
            do {
                try {
                    arr = getCords(cords, ship);
                    checkNear(arr, field);
                } catch (RuntimeException e) {
                    go = true;
                    System.out.printf("\nError! %s Try again:\n\n", e.getMessage());
                    cords = scanner.nextLine();
                    continue;
                }
                go = false;
            } while (go);

            draw(arr, field);
            print(field);
        }

        System.out.println("\nPress Enter and pass the move to another player");
        if (scanner.hasNextLine()) {
            scanner.nextLine();
            clear();
            if (player != 2) {
                preparation(2, secondField);
            }
        }
    }

    private void clear() {
        System.out.println("...\n");
    }

    private void checkNear(int[] arr, String[][] field) throws RuntimeException {
        int a;
        int min;
        int max;
        RuntimeException exception = new RuntimeException("You placed it too close to another one or outside the field.");
        if (arr[0] == arr[1]) {
            a = arr[0];
            min = Math.min(arr[2], arr[3]);
            max = Math.max(arr[2], arr[3]);
            for (int i = min; i <= max; i++) {
                if (field[a - 1][i].equals("O") || a != 10 && field[a + 1][i].equals("O") ||
                        field[a][i - 1].equals("O") || i != 10 && field[a][i + 1].equals("O")) {
                    throw exception;
                }
            }
        } else {
            a = arr[2];
            min = Math.min(arr[0], arr[1]);
            max = Math.max(arr[0], arr[1]);
            for (int i = min; i <= max; i++) {
                if (field[i][a - 1].equals("O") || a != 10 && field[i][a + 1].equals("O") ||
                        field[i - 1][a].equals("O") || i != 10 && field[i + 1][a].equals("O")) {
                    throw exception;
                }
            }
        }
    }

    private void draw(int[] arr, String[][] field) {
        if (arr[0] == arr[1]) {
            for (int i = Math.min(arr[2], arr[3]); i <= Math.max(arr[2], arr[3]); i++) {
                field[arr[0]][i] = "O";
            }
        } else {
            for (int i = Math.min(arr[0], arr[1]); i <= Math.max(arr[0], arr[1]); i++) {
                field[i][arr[2]] = "O";
            }
        }
    }

    private Ship createShip(int i) {
        Ship ship = null;
        switch (i) {
            case 0 -> ship = new AircraftCarrier();
            case 1 -> ship = new Battleship();
            case 2 -> ship = new Submarine();
            case 3 -> ship = new Cruiser();
            case 4 -> ship = new Destroyer();
        }
        return ship;
    }

    private int[] getCords(String cords, Ship ship) throws RuntimeException {
        String[] arr = cords.split(" ");
        char a1 = arr[0].charAt(0);
        char b1 = arr[1].charAt(0);
        int a2 = Integer.parseInt(arr[0].substring(1));
        int b2 = Integer.parseInt(arr[1].substring(1));
        if (a1 != b1 && a2 != b2) {
            throw new RuntimeException("Wrong ship location!");
        } else if (a1 == b1 && Math.abs(a2 - b2) + 1 != ship.getCells() ||
                a2 == b2 && Math.abs(a1 - b1) + 1 != ship.getCells()) {
            throw new RuntimeException(String.format("Wrong length of the %s!", ship.getName()));
        }
        return new int[]{Character.getNumericValue(a1) - 9, Character.getNumericValue(b1) - 9, a2, b2};
    }

    private void print(String[][] field) {
        System.out.println();
        for (String[] gg : field) {
            for (String hh : gg) {
                System.out.print(hh + " ");
            }
            System.out.println();
        }
    }

    private void print(String[][] field1, String[][] field2) {
        System.out.println();
        for (String[] gg : field1) {
            for (String hh : gg) {
                System.out.print(hh + " ");
            }
            System.out.println();
        }
        System.out.print("---------------------");
        print(field2);
    }
}