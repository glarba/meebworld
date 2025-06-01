import java.util.Random;
public class Play {
    public static String[][][] world = Generate.world;
    public static final String BLACK = Generate.BLACK;
    public static String HUMAN = BLACK + "☺";
    public static Random random = new Random();
    public static int population = 0;
    public static int[] popX = new int[10];
    public static int[] popY = new int[10];
    public static void main(String[] args) throws Exception {
        PlacePeople();
        Generate.MapPrint();
        for (int i = 0; i < 100; i++) {
            NextFrame();
        }
    }

    public static void PlacePeople() {
        while (population < 2) {
            int x = random.nextInt(world[0].length);
            int y = random.nextInt(world.length);
            if (world[y][x][0].equals(Generate.GRASS) && world[y][x][1] == null) {
                world[y][x][1] = Play.HUMAN;
                popX[population] = x;
                popY[population] = y;
                population++;
            }
        }
    }

    public static void NextFrame() throws Exception {
        for (int i = 0; i < population; i++) {
            MovePerson(i);
        }
        Generate.MapPrint();
        Thread.sleep(500);
    }

    public static void MovePerson(int id) {
        int newX = popX[id] + random.nextInt(3) - 1;
        int newY = popY[id] + random.nextInt(3) - 1;
        if (world[newY][newX][1] == null && !world[newY][newX][0].equals(Generate.WATER)) {
            world[popY[id]][popX[id]][1] = null;
            world[newY][newX][1] = HUMAN;
            popX[id] = newX;
            popY[id] = newY;
        }
    }
}
