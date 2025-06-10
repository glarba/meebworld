import java.util.Random;
public class Play {
    public static String[][][] world = Generate.world;
    public static final String BLACK = Generate.BLACK;
    public static String HUMAN = BLACK + "☺";
    public static Random random = new Random();
    public static int population = 0;
    public static int[] popX = new int[10];
    public static int[] popY = new int[10];
    public static int[] destX = new int[10];
    public static int[] destY = new int[10];
    public static char[] goal = new char[10];
    /*
     * w = wander
     * t = cut tree
     * s = mine stone
     * c = mine coal
     * i = mine iron
     */
    public static int woodStored = 0;
    public static int stoneStored = 0;
    static int stoneProgress = 0;
    public static int coalStored = 0;
    public static int ironStored = 0;

    public static int treeNum;
    public static int frameNum;

    static boolean[] taskWait = new boolean[10];
    static int[] stuck = new int[10];
    public static void run() {
        if (!App.start) {
        App.start = true;
        frameNum = 0;
        PlacePeople();
        Generate.MapPrint();
        }
        NextFrame();
    }

    public static int TreeCount() {
        int count = 0;
        for (int i = 0; i < world[0].length; i++) {
            for (int j = 0; j < world.length; j++) {
                if (world[i][j][1] != null && world[i][j][1].equals(Generate.TREE)) {
                    count++;
                }
            }
        }
        return count;
    }

    public static void PlacePeople() {
        while (population < 2) {
            int x = random.nextInt(world[0].length);
            int y = random.nextInt(world.length);
            if (world[y][x][0].equals(Generate.GRASS) && world[y][x][1] == null) {
                world[y][x][1] = Play.HUMAN;
                popX[population] = x;
                destX[population] = x;
                popY[population] = y;
                destY[population] = y;
                goal[population] = 'w';
                taskWait[population] = false;
                stuck[population] = 3;
                population++;
            }
        }
    }

    public static void NextFrame() {
        treeNum = TreeCount();
        Generate.AddTrees(3000 + (int)Math.pow(treeNum,3));
        for (int i = 0; i < population; i++) {
            HumanTask(i);
            //System.out.println("guy " + i + ": " + goal[i]);
        }
        System.out.println();
        frameNum++;
        //Generate.MapPrint();
    }

    public static void HumanTask(int id) {
        int move = 0;
        if (!taskWait[id]) {
            if (destX[id] < popX[id]) {
                move += MoveLeft(id);
            }
            else if (destX[id] > popX[id]) {
                move += MoveRight(id);
            }
            if (destY[id] < popY[id]) {
                move += MoveUp(id);
            }
            else if (destY[id] > popY[id]) {
                move += MoveDown(id);
            }
        }
        if (move > 0) {
            stuck[id] = 0;
        }
        if (stuck[id] > 1) {
            Wander(id);
            stuck[id]--;
        }
        if (move == 0 && !taskWait[id]) {
            stuck[id]++;
        }
        switch (goal[id]) {
            case 'w':
                if (popX[id] == destX[id] && popY[id] == destY[id]) {
                    NewTask(id);
                }
                break;
            case 't':
                if (world[destY[id]][destX[id]][1] == null || !world[destY[id]][destX[id]][1].equals(Generate.TREE)) {
                    NewTask(id);
                }
                else if (Math.abs(popX[id] - destX[id]) <= 1 && Math.abs(popY[id] - destY[id]) <= 1) {
                    if (taskWait[id]) {
                        world[destY[id]][destX[id]][1] = null;
                        woodStored++;
                        NewTask(id);
                        taskWait[id] = false;
                    }
                    else {
                        taskWait[id] = true;
                    }
                }
                break;
            case 's':
                if (Math.abs(popX[id] - destX[id]) <= 1 && Math.abs(popY[id] - destY[id]) <= 1) {
                    if (taskWait[id] && stoneProgress > 9) {
                        stoneStored++;
                        Wander(id);
                        taskWait[id] = false;
                    }
                    else {
                        taskWait[id] = true;
                        stoneProgress++;
                    }
                }
        }
        //System.out.println(stuck[id]);
    }

    public static void NewTask(int id) {
        //if (woodStored < 100 && treeNum > 0 && !GoalInUse(id, 't') && Search(id, Generate.TREE)) {
        if (treeNum > 0 && !GoalInUse(id, 't') && Search(id, Generate.TREE)) {
            goal[id] = 't';
            return;
        }
        //if (stoneStored < 100 && !GoalInUse(id, 's') && Search(id, Generate.ROCK)) {
        if (!GoalInUse(id, 's') && Search(id, Generate.ROCK)) {
            goal[id] = 's';
            stoneProgress = 0;
            return;
        }
        Wander(id);
        goal[id] = 'w';
    }

    public static boolean GoalInUse(int id, char type) {
        for (int i = 0; i < population; i++) {
            if (i != id && goal[i] == type) {
                return true;
            }
        }
        return false;
    }

    public static boolean Search(int id, String target) {
        int range = 1;
        while (true) {
            for (int i = -range; i < range; i++) {
                if (popX[id] + i > -1 && popX[id] + i < world[0].length && popY[id] - range > -1 && popY[id] - range < world.length && 
                world[popY[id] - range][popX[id] + i][1] != null && world[popY[id] - range][popX[id] + i][1].equals(target)) {
                    destX[id] = popX[id] + i;
                    destY[id] = popY[id] - range;
                    return true;
                }
                if (popX[id] + range > -1 && popX[id] + range < world[0].length && popY[id] + i > -1 && popY[id] + i < world.length && 
                world[popY[id] + i][popX[id] + range][1] != null && world[popY[id] + i][popX[id] + range][1].equals(target)) {
                    destX[id] = popX[id] + range;
                    destY[id] = popY[id] + i;
                    return true;
                }
                if (popX[id] - i > -1 && popX[id] - i < world[0].length && popY[id] + range > -1 && popY[id] + range < world.length && 
                world[popY[id] + range][popX[id] - i][1] != null && world[popY[id] + range][popX[id] - i][1].equals(target)) {
                    destX[id] = popX[id] - i;
                    destY[id] = popY[id] + range;
                    return true;
                }
                if (popX[id] - range > -1 && popX[id] - range < world[0].length && popY[id] - i > -1 && popY[id] - i < world.length && 
                world[popY[id] - i][popX[id] - range][1] != null && world[popY[id] - i][popX[id] - range][1].equals(target)) {
                    destX[id] = popX[id] - range;
                    destY[id] = popY[id] - i;
                    return true;
                }
            }
            if (range < 46) {
                range++;
            }
            else {
                return false;
            }
        }
    }

    public static int MoveLeft(int id) {
        if (world[popY[id]][popX[id] - 1][1] == null && !world[popY[id]][popX[id] - 1][0].equals(Generate.WATER)) {
            world[popY[id]][popX[id]][1] = null;
            popX[id]--;
            world[popY[id]][popX[id]][1] = HUMAN;
            return 1;
        }
        else {
            return 0;
        }
    }

    public static int MoveRight(int id) {
        if (world[popY[id]][popX[id] + 1][1] == null && !world[popY[id]][popX[id] + 1][0].equals(Generate.WATER)) {
            world[popY[id]][popX[id]][1] = null;
            popX[id]++;
            world[popY[id]][popX[id]][1] = HUMAN;
            return 1;
        }
        else {
            return 0;
        }
    }

    public static int MoveUp(int id) {
        if (world[popY[id] - 1][popX[id]][1] == null && !world[popY[id] - 1][popX[id]][0].equals(Generate.WATER)) {
            world[popY[id]][popX[id]][1] = null;
            popY[id]--;
            world[popY[id]][popX[id]][1] = HUMAN;
            return 1;
        }
        else {
            return 0;
        }
    }

    public static int MoveDown(int id) {
        if (world[popY[id] + 1][popX[id]][1] == null && !world[popY[id] + 1][popX[id]][0].equals(Generate.WATER)) {
            world[popY[id]][popX[id]][1] = null;
            popY[id]++;
            world[popY[id]][popX[id]][1] = HUMAN;
            return 1;
        }
        else {
            return 0;
        }
    }

    public static void Wander(int id) {
        boolean dest = false;
        int newX = 0;
        int newY = 0;
        while (!dest) {
            boolean cont = false;
            while (!cont) {
                newX = popX[id] + random.nextInt(11) - 5;
                if (newX < world[0].length && newX > -1) {
                    cont = true;
                }
            }
            cont = false;
            while (!cont) {
                newY = popY[id] + random.nextInt(11) - 5;
                if (newY < world.length && newY > -1) {
                    cont = true;
                }
            }
            if (world[newY][newX][1] == null && !world[newY][newX][0].equals(Generate.WATER) && !world[newY][newX][0].equals(Generate.OCEAN)) {
                destX[id] = newX;
                destY[id] = newY;
                goal[id] = 'w';
                dest = true;
            }
        }
    }

    /* old
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
    */
}
