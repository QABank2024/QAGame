public class Grid {
    private char[][] grid;
    private int size;

    public Grid(int size) {
        this.size = size;
        grid = new char[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                grid[i][j] = '.'; // Empty cell
            }
        }
    }

    public void updatePosition(int x, int y, char symbol) {
        grid[y][x] = symbol;
    }

    public void clearPosition(int x, int y) {
        grid[y][x] = '.'; // Clear the position
    }

    public void displayGrid() {
        System.out.println("Current Grid:");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(grid[i][j] + " ");
            }
            System.out.println();
        }
        System.out.println();
    }

    public boolean isObstacle(int x, int y) {
        return grid[y][x] == 'O'; // Return true if there is an obstacle
    }

    public void placeObstacle(int x, int y) {
        grid[y][x] = 'O'; // Place an obstacle
    }
}
