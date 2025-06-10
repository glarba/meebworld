import java.util.Random;
public class Generate {
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = ";38;5;0m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = ";38;5;15m";
    public static final String CLEAR = "\033[H\033[2J";
    public static final String BROWN = ";38;5;52m";
    public static final String GREY = ";38;5;245m";
    public static final String IRON_COLOR = ";38;5;132m";

    public static final String BLACK_BACKGROUND = "\u001B[40";
    public static final String RED_BACKGROUND = "\u001B[41";
    public static final String GREEN_BACKGROUND = "\033[48;5;2";
    public static final String YELLOW_BACKGROUND = "\033[48;5;228";
    public static final String BLUE_BACKGROUND = "\u001B[44";
    public static final String PURPLE_BACKGROUND = "\u001B[45";
    public static final String CYAN_BACKGROUND = "\u001B[46";
    public static final String WHITE_BACKGROUND = "\u001B[47";

    public static final String GRASS = GREEN_BACKGROUND;
    public static final String OCEAN = BLUE_BACKGROUND;
    public static final String BEACH = YELLOW_BACKGROUND;
    public static final String WATER = CYAN_BACKGROUND;

    public static final String TREE = BROWN + "♣";
    public static final String ROCK = GREY + "⯊";
    public static final String IRON = IRON_COLOR + "⯊";
    public static final String COAL = BLACK + "⯊";

    public static Random random = new Random();

    public static String[][][] world;

    public static int grassCount = 0;
    public static void run() {
        switch (Panel.genFrame) {
            case 0:
                GenerateLandSquare(47);
                MapPrint();
                break;
            default:
                GenerateBeach();
                MapPrint();
                IslandDelete();
                MapPrint();
                break;
            case 3:
                GenerateDepth();
                MapPrint();
                break;
            case 4:
                AddTrees(33);
                MapPrint();
                break;
            case 5:
                AddRocks();
                MapPrint();
        }
    }

    public static void generateLandRectangle(int x, int y) {
        world = new String[y][x][3];
        for (int i = 0; i < world.length; i++, System.out.println()) {
            for (int j = 0; j < world[0].length; j++) {
                double distance = Math.sqrt((Math.pow(Math.abs(i - 12),2) + Math.pow(Math.abs(j - 37),2)/10));
                if (distance < 6 + random.nextDouble(0,5)) {
                    world[i][j][0] = GRASS;
                }
                else {
                    world[i][j][0] = OCEAN;
                }
            }
        }
    }

    public static void GenerateLandSquare(int x) {
        world = new String[x][x][3];
        for (int i = 0; i < x; i++, System.out.println()) {
            for (int j = 0; j < x; j++) {
                double distance = Math.sqrt((Math.pow(Math.abs(i - (x/2)),2) + Math.pow(Math.abs(j - (x/2)),2)));
                if (distance < random.nextDouble(12,22)) {
                    world[i][j][0] = GRASS;
                } else {
                    world[i][j][0] = OCEAN;
                }
            }
        }
    }

    public static void GenerateBeach() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j][0].equals(GRASS) && BeachCheck(j, i)) {
                    world[i][j][0] = BEACH;
                }
            }
        }
    }

    public static boolean BeachCheck(int x, int y) {
        for (int i = y - 1; i < y + 2; i++) {
            for (int j = x - 1; j < x + 2; j++) {
                if (world[i][j][0].equals(OCEAN)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void IslandDelete() {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j][0].equals(BEACH) && IslandCheck(j, i)) {
                    world[i][j][0] = OCEAN;
                }
            }
        }
        for (int i = world.length - 1; i >= 0; i--) {
            for (int j = world[0].length - 1; j >= 0; j--) {
                if (world[i][j][0].equals(BEACH) && IslandCheck(j, i)) {
                    world[i][j][0] = OCEAN;
                }
            }
        }
    }

    public static boolean IslandCheck(int x, int y) {
        int water = 0;
        int grass = 0;
        if (world[y+1][x][0].equals(OCEAN)) {
            water++;
        }
        if (world[y-1][x][0].equals(OCEAN)) {
            water++;
        }
        if (world[y][x+1][0].equals(OCEAN)) {
            water++;
        }
        if (world[y][x-1][0].equals(OCEAN)) {
            water++;
        }
        if (water > 2) {
            return true;
        }
        for (int i = y - 2; i < y + 3; i++) {
            for (int j = x - 2; j < x + 3; j++) {
                if (world[i][j][0].equals(GRASS)) {
                    grass++;
                }
            }
        }
        if (grass > 1) {
            return false;
        }
        return true;
    }

    public static void GenerateDepth() {
        String[][] worldBig = new String[world.length + 6][world[0].length + 6];
        for (int i = 0; i < worldBig.length; i++) {
            for (int j = 0; j < worldBig[0].length; j++) {
                worldBig[i][j] = " ";
            }
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                worldBig[i + 3][j + 3] = world[i][j][0];
            }
        }
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (worldBig[i + 3][j + 3].equals(OCEAN) && ShallowCheck(worldBig, j + 3, i + 3)) {
                    world[i][j][0] = WATER;
                }
            }
        }
    }

    public static boolean ShallowCheck(String[][] world, int x, int y) {
        for (int i = y - 3; i < y + 4; i++) {
            for (int j = x - 3; j < x + 4; j++) {
                if (world[i][j].equals(BEACH)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static void AddTrees(int chance) {
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j][1] == null && world[i][j][0].equals(GRASS) && random.nextInt(chance) == 0) {
                    world[i][j][1] = TREE;
                }
            }
        }
    }

    public static void AddRocks() {
        int count = 0;
        while (count < 3) {
            int x = random.nextInt(world[0].length);
            int y = random.nextInt(world.length);
            if (world[y][x][0].equals(GRASS) && world[y][x][1] == null) {
                switch (count) {
                    case 0:
                        world[y][x][1] = ROCK;
                        break;
                    case 1:
                        world[y][x][1] = IRON;
                        break;
                    case 2:
                        world[y][x][1] = COAL;
                }
                count++;
            }
        }
    }

    public static void MapPrint() {
        
        String frame = "";
        System.out.print(CLEAR + RESET);
        for (int i = 0; i < world.length; i++, frame += "\n") {
            for (int j = 0; j < world[0].length; j++) {
                if (world[i][j][1] == null) {
                    frame += world[i][j][0] + "m ";
                }
                else {
                    frame += world[i][j][0] + world[i][j][1];
                }
            }
            if (App.start) {
                frame += BLACK_BACKGROUND + WHITE;
                switch (i) {
                    case 0:
                        frame += " Wood:  " + Play.woodStored;
                        break;
                    case 1:
                        frame += " Stone: " + Play.stoneStored;
                        break;
                    case 3:
                        frame += " DEBUG";
                        break;
                    case 4:
                        frame += " Trees: " + Play.treeNum;
                        break;
                    case 5:
                        frame += " Pop 1: " + Play.goal[0];
                        break;
                    case 6:
                        frame += " Pop 2: " + Play.goal[1];
                        break;
                    case 7:
                        frame += " Frame " + Play.frameNum;
                }
            }
        }
        System.out.println(frame + RESET);
    }
}
