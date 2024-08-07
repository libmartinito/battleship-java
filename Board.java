package battleship;

import java.util.Arrays;

public class Board {
    String[][] board;
    String[][] boardWithFog;
    Ship[] ships;

    Board() {
        this.board = generateBoard();
        this.boardWithFog = generateBoard();
        this.ships = new Ship[0];
    }

    enum Direction {
        UP, DOWN, LEFT, RIGHT;

        String move(String coordinate) {
            char row = coordinate.charAt(0);
            int col = Integer.parseInt(coordinate.substring(1));

            return switch (this) {
                case UP -> (char) (row + 1) + String.valueOf(col);
                case DOWN -> (char) (row - 1) + String.valueOf(col);
                case LEFT -> String.valueOf(row) + (col - 1);
                case RIGHT -> String.valueOf(row) + (col + 1);
            };

        }
    }

    void addShip(Ship ship) {
        for (String part : ship.parts) {
            char row = part.charAt(0);
            int col = Integer.parseInt(part.substring(1));

            this.board[row - 'A' + 1][col] = "O";
        }

        this.ships = Arrays.copyOf(this.ships, this.ships.length + 1);
        this.ships[this.ships.length - 1] = ship;
    }

    void displayBoard() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(this.board[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }

    void displayBoardWithFog() {
        for (int i = 0; i < 11; i++) {
            for (int j = 0; j < 11; j++) {
                System.out.print(this.boardWithFog[i][j]);
                System.out.print(" ");
            }

            System.out.println();
        }
    }

    String[][] generateBoard() {
        String[][] board = new String[11][11];
        String[] header = new String[] {"X", "A", "B", "C", "D", "E", "F", "G", "H", "I", "J"};

        for (int row = 0; row < 11; row++) {
            for (int col = 0; col < 11; col++) {
                if (row == 0 && col == 0) {
                    board[row][col] = " ";
                } else if (row == 0) {
                    board[row][col] = String.valueOf(col);
                } else if (col == 0) {
                    board[row][col] = header[row];
                } else {
                    board[row][col] = "~";
                }
            }
        }

        return board;
    }

    void shoot(String target) {
        char row = target.charAt(0);
        int col = Integer.parseInt(target.substring(1));

        boolean isTargetValidShipPart = validateTargetIfShipPart(target);

        if (isTargetValidShipPart) {
            Ship targetedShip = null;

            for (Ship ship : this.ships) {
                if (ship.validateShipPart(target)) {
                    targetedShip = ship;
                }
            }

            this.board[row - 'A' + 1][col] = "X";
            this.boardWithFog[row - 'A' + 1][col] = "X";

            assert targetedShip != null;
            targetedShip.destroyPart();

            if (targetedShip.validateIfSunk() && !validateIfAllShipsAreSunk()) {
                System.out.println("You sank a ship!");
            } else if (validateIfAllShipsAreSunk()) {
                System.out.println("You sank the last ship. You won. Congratulations!");
            } else {
                System.out.println("You hit a ship!");
            }
        } else {
            this.board[row - 'A' + 1][col] = "M";
            this.boardWithFog[row - 'A' + 1][col] = "M";
            System.out.println("You missed!");
        }
    }

    boolean validateIfAllShipsAreSunk() {
        for (Ship ship : this.ships) {
            if (!ship.validateIfSunk()) {
                return false;
            }
        }

        return true;
    }

    boolean validateShipAdjacency(Ship shipToAdd) {
        for (Ship ship : this.ships) {
            if (ship == null) {
                continue;
            }

            for (String part : ship.parts) {
                for (Direction dir : Direction.values()) {
                    for (String partToAdd : shipToAdd.parts) {
                        if (dir.move(part).equals(partToAdd)) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

    boolean validateTargetIfShipPart(String target) {
        for (Ship ship : this.ships) {
            if (ship.validateShipPart(target)) {
                return true;
            }
        }

        return false;
    }
}
