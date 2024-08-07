package battleship;

public class Ship {
    int length;
    char startRow;
    char endRow;
    int startCol;
    int endCol;
    String[] parts;
    int damagedParts;

    Ship(String coordinates, int length) {
        String[] coordinatesArr = coordinates.split(" ");
        String start = coordinatesArr[0];
        String end = coordinatesArr[1];
        this.startRow = start.charAt(0);
        this.endRow = end.charAt(0);
        this.startCol = Integer.parseInt(start.substring(1));
        this.endCol = Integer.parseInt(end.substring(1));
        this.length = length;
        this.parts = getParts(this.startRow, this.endRow, this.startCol, this.endCol);
        this.damagedParts = 0;
    }

    String[] getParts(char startRow, char endRow, int startCol, int endCol) {
        String[] parts = new String[this.length];

        if (startRow == endRow) {
            int start = Math.min(startCol, endCol);
            int end = Math.max(startCol, endCol);

            for (int i = start; i <= end; i++) {
                parts[i - start] = String.valueOf(startRow) + i;
            }
        } else {
            char start = startRow > endRow ? endRow : startRow;
            char end = startRow > endRow ? startRow : endRow;

            for (char i = start; i <= end; i++) {
                parts[i - start] = String.valueOf(Character.valueOf(i)) + startCol;
            }
        }

        return parts;
    }

    void destroyPart() {
        this.damagedParts++;
    }

    boolean validateIfSunk() {
        return this.damagedParts >= this.length;
    }

    boolean validateShipPart(String target) {
        for (String part: this.parts) {
            if (part.equals(target)) {
                return true;
            }
        }

        return false;
    }
}
