public class Monster {
    private int x, y;
    private String name;

    public Monster(int x, int y, String name) {
        this.x = x;
        this.y = y;
        this.name = name;
    }

    public int getX() { return x; }
    public int getY() { return y; }

    public void greet() {
        System.out.println("You've been caught by " + name + "! " + name + " says: 'Gotcha!'");
    }

    // Methods for moving the monster
    public void moveRight() { x++; }
    public void moveLeft() { x--; }
    public void moveDown() { y++; }
    public void moveUp() { y--; }
}
