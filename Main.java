package battleship;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Board playerOneBoard = new Board();
        Board playerTwoBoard = new Board();
        Scanner sc = new Scanner(System.in);

        System.out.println("Player 1, place your ships on the game field");
        playerOneBoard.displayBoardWithFog();
        placeShips(sc, playerOneBoard);
        changeTurn();

        System.out.println("Player 2, place your ships on the game field");
        playerTwoBoard.displayBoardWithFog();
        placeShips(sc, playerTwoBoard);
        changeTurn();

        int currentPlayer = 1;
        Board currentBoard = playerOneBoard;
        Board enemyBoard = playerTwoBoard;
        boolean isGameEnd = validateGameEnd(playerOneBoard, playerTwoBoard);

        while (!isGameEnd) {
            System.out.println();
            enemyBoard.displayBoardWithFog();
            System.out.println("---------------------");
            currentBoard.displayBoard();
            System.out.println();
            System.out.println("Player " + currentPlayer + ", it's your turn:");
            System.out.println();
            String target = sc.next();

            boolean isTargetValid = validateTarget(target);

            while (!isTargetValid) {
                System.out.println();
                System.out.println("Error! You entered the wrong coordinates! Try again:");
                target = sc.next();
                isTargetValid = validateTarget(target);
            }

            enemyBoard.shoot(target);
            isGameEnd = validateGameEnd(playerOneBoard, playerTwoBoard);

            if (isGameEnd) {
                break;
            }

            currentPlayer = currentPlayer == 1 ? 2 : 1;
            currentBoard = currentPlayer == 2 ? playerTwoBoard : playerOneBoard;
            enemyBoard = currentPlayer == 2 ? playerOneBoard : playerTwoBoard;
            changeTurn();
        }
    }

    enum ShipType {
        AIRCRAFT_CARRIER("Aircraft Carrier", 5),
        BATTLESHIP("Battleship", 4),
        SUBMARINE("Submarine", 3),
        CRUISER("Cruiser", 3),
        DESTROYER("Destroyer", 2);

        final String displayName;
        final int length;

        ShipType(String displayName, int length) {
            this.displayName = displayName;
            this.length = length;
        }
    }

    static void changeTurn() {
        System.out.println();
        Scanner sc = new Scanner(System.in);
        System.out.println("Press Enter and pass the move to another player");
        System.out.println("...");
        sc.nextLine();
    }

    static boolean validateGameEnd(Board playerOneBoard, Board playerTwoBoard) {
        return playerOneBoard.validateIfAllShipsAreSunk() || playerTwoBoard.validateIfAllShipsAreSunk();
    }

    static boolean validateIfNextToShip(Board board, Ship ship) {
        return board.validateShipAdjacency(ship);
    }

    static boolean validateOutOfBounds(String coordinates) {
        String[] coordinatesArr = coordinates.split(" ");
        String start = coordinatesArr[0];
        String end = coordinatesArr[1];
        char startRow = start.charAt(0);
        char endRow = end.charAt(0);
        int startCol = Integer.parseInt(start.substring(1));
        int endCol = Integer.parseInt(end.substring(1));

        if (startRow != endRow && startCol != endCol) {
            return false;
        }

        if (startRow < 'A' || startRow > 'J' || endRow < 'A' || endRow > 'J') {
            return false;
        }

        return startCol >= 1 && startCol <= 10 && endCol >= 1 && endCol <= 10;
    }

    static boolean validateLength(String coordinates, int expectedLength) {
        String[] coordinatesArr = coordinates.split(" ");
        String start = coordinatesArr[0];
        String end = coordinatesArr[1];
        char startRow = start.charAt(0);
        char endRow = end.charAt(0);
        int startCol = Integer.parseInt(start.substring(1));
        int endCol = Integer.parseInt(end.substring(1));
        int calculatedLength;

        if (startRow == endRow) {
            calculatedLength = Math.abs(startCol - endCol) + 1;
        } else {
            calculatedLength = Math.abs(startRow - endRow) + 1;
        }

        return calculatedLength == expectedLength;
    }

    static boolean validateTarget(String target) {
        char row = target.charAt(0);
        int col = Integer.parseInt(target.substring(1));

        if (row < 'A' || row > 'J') {
            return false;
        }

        return col >= 1 && col <= 10;
    }

    static void placeShips(Scanner sc, Board board) {
        for (ShipType shipType : ShipType.values()) {
            System.out.println();
            System.out.println("Enter the coordinates of the " + shipType.displayName + " (" + shipType.length + " cells):");
            String coordinates = sc.nextLine();

            boolean isValidLength = validateLength(coordinates, shipType.length);

            while (!isValidLength) {
                System.out.println("Error! Wrong length of the "  + shipType.displayName + "! Try again:");
                coordinates = sc.nextLine();
                isValidLength = validateLength(coordinates, shipType.length);
            }

            boolean isWithinBounds = validateOutOfBounds(coordinates);

            while (!isWithinBounds) {
                System.out.println("Error! Wrong ship location! Try again:");
                coordinates = sc.nextLine();
                isWithinBounds = validateOutOfBounds(coordinates);
            }

            Ship ship = new Ship(coordinates, shipType.length);
            boolean isNextToShip = validateIfNextToShip(board, ship);

            while (isNextToShip) {
                System.out.println("Error! You placed it too close to another one. Try again:");
                coordinates = sc.nextLine();
                ship = new Ship(coordinates, shipType.length);
                isNextToShip = validateIfNextToShip(board, ship);
            }

            board.addShip(ship);
            board.displayBoard();
        }
    }
}
