import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Panel extends JPanel implements ActionListener {
    static final int UNIT_SIZE = 20;
    static final int SCREEN_SIZE = 47 * UNIT_SIZE;
    static final int GAME_UNITS = (SCREEN_SIZE * SCREEN_SIZE) / UNIT_SIZE;
    public static final int DELAY = 50;
    boolean running = false;
    public static int genFrame = 0;
    Timer timer;
    public static boolean pause = false;
    static int gameSpeed = 1;
    static int realFrameNum = 0;
    Panel() {
        this.setPreferredSize(new Dimension(SCREEN_SIZE,SCREEN_SIZE));
        this.setBackground(Color.blue);
        this.setFocusable(true);
        this.addKeyListener(new Listener());
        start();
    }
    public void start() {
        running = true;
        timer = new Timer(DELAY, this);
        timer.start();

        //Play.run();
    }
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        draw(g);
    }
    public void draw(Graphics g) {
        //boolean cb = true;
        Image grass1 = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\grass\\grass1_j.png");
        Image grass2 = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\grass\\grass2_j.png");
        Image grass3 = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\grass\\grass3_j.png");
        
        Image ocean = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\ocean_j.png");
        Image beach = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\beach_j.png");
        Image water = Toolkit.getDefaultToolkit().getImage("resources\\default\\floor\\water_j.png");
        Image tree = Toolkit.getDefaultToolkit().getImage("resources\\default\\object\\tree_j.png");
        Image rock = Toolkit.getDefaultToolkit().getImage("resources\\default\\object\\rock_j.png");
        Image coal = Toolkit.getDefaultToolkit().getImage("resources\\default\\object\\coal_j.png");
        Image iron = Toolkit.getDefaultToolkit().getImage("resources\\default\\object\\iron_j.png");
        Image smiley = Toolkit.getDefaultToolkit().getImage("resources\\default\\meeb\\smiley_j.png");
        Image smileys = Toolkit.getDefaultToolkit().getImage("resources\\default\\meeb\\smileys_j.png");
        Image smileyt = Toolkit.getDefaultToolkit().getImage("resources\\default\\meeb\\smileyt_j.png");

        Image paused = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\pause.png");
        Image pause0 = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\pause0.png");
        Image onex = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\1x.png");
        Image onex0 = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\1x0.png");
        Image twox = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\2x.png");
        Image twox0 = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\2x0.png");
        Image threex = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\3x.png");
        Image threex0 = Toolkit.getDefaultToolkit().getImage("resources\\default\\ui\\3x0.png");
        //if (cb) {
        //    grass1 = Toolkit.getDefaultToolkit().getImage("resources\\cb\\grass.png");
        //    grass2 = Toolkit.getDefaultToolkit().getImage("resources\\cb\\grass2.png");
        //    grass3 = Toolkit.getDefaultToolkit().getImage("resources\\cb\\grass3.png");
        //}
        String[][][] world = Generate.world;
        for (int i = 0; i < world.length; i++) {
            for (int j = 0; j < world.length; j++) {
                switch (world[i][j][0]) {
                    case Generate.OCEAN:
                        g.drawImage(ocean, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        break;
                    case Generate.GRASS:
                        if (Play.frameNum % 32 < 8) {
                            g.drawImage(grass1, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                        else if (Play.frameNum % 32 < 16 || Play.frameNum % 32 > 24) {
                            g.drawImage(grass2, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                        else {
                            g.drawImage(grass3, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                        break;
                    case Generate.BEACH:
                        g.drawImage(beach, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        break;
                    case Generate.WATER:
                        g.drawImage(water, j * UNIT_SIZE, i * UNIT_SIZE, this);
                }
                if (world[i][j][1] != null) {
                    switch (world[i][j][1]) {
                        case Generate.TREE:
                            g.drawImage(tree, j * UNIT_SIZE, i * UNIT_SIZE, this);
                            break;
                        case Generate.ROCK:
                            g.drawImage(rock, j * UNIT_SIZE, i * UNIT_SIZE, this);
                            break;
                        case Generate.COAL:
                            g.drawImage(coal, j * UNIT_SIZE, i * UNIT_SIZE, this);
                            break;
                        case Generate.IRON:
                            g.drawImage(iron, j * UNIT_SIZE, i * UNIT_SIZE, this);
                    }
                    for (int k = 0; k < Play.population; k++) {
                        switch (Play.goal[k]) {
                            case 's':
                                g.drawImage(smileys, Play.popX[k] * UNIT_SIZE, Play.popY[k] * UNIT_SIZE, this);
                                break;
                            case 't':
                                g.drawImage(smileyt, Play.popX[k] * UNIT_SIZE, Play.popY[k] * UNIT_SIZE, this);
                                break;
                            default:
                                g.drawImage(smiley, Play.popX[k] * UNIT_SIZE, Play.popY[k] * UNIT_SIZE, this);
                        }
                    }

                    /* 
                    if(world[i][j][1].equals(Play.HUMAN)) {
                        if (world[i][j][2] == null) {
                            g.drawImage(smiley, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                        else if (world[i][j][2].equals("s")) {
                            g.drawImage(smileys, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                        else if (world[i][j][2].equals("t")) {
                            g.drawImage(smileyt, j * UNIT_SIZE, i * UNIT_SIZE, this);
                        }
                    }
                    */
                }
            }
        }
        if (App.start) {
            if (gameSpeed == 0) {
                g.drawImage(paused,0,0,this);
                g.drawImage(onex0,40,0,this);
                g.drawImage(twox0,80,0,this);
                g.drawImage(threex0,120,0,this);
            }
            else {
                g.drawImage(pause0,0,0,this);
                switch (gameSpeed) {
                    case 1:
                        g.drawImage(onex,40,0,this);
                        g.drawImage(twox0,80,0,this);
                        g.drawImage(threex0,120,0,this);
                        break;
                    case 2:
                        g.drawImage(onex0,40,0,this);
                        g.drawImage(twox,80,0,this);
                        g.drawImage(threex0,120,0,this);
                        break;
                    case 3:
                        g.drawImage(onex0,40,0,this);
                        g.drawImage(twox0,80,0,this);
                        g.drawImage(threex,120,0,this);
                        break;
                }
            }
        }
    }
    public void actionPerformed(ActionEvent e) {
        if (realFrameNum % (int)Math.pow(4 - gameSpeed,1.5) == 0 && gameSpeed != 0) {
            if (genFrame < 6) {
                Generate.run();
                genFrame++;
                System.out.println(genFrame);
            }
            else {
                Play.run();
            }
        }
        realFrameNum++;
        revalidate();
        repaint();
    }
}

class Listener extends KeyAdapter {
    public void keyPressed(KeyEvent e) {
        int gameSpeedStored = 1;
        switch (e.getKeyCode()) {
            case KeyEvent.VK_SPACE:
            case KeyEvent.VK_BACK_QUOTE:
            case KeyEvent.VK_0:
                if (Panel.gameSpeed == 0) {
                    Panel.gameSpeed = gameSpeedStored;
                }
                else {
                    gameSpeedStored = Panel.gameSpeed;
                    Panel.gameSpeed = 0;
                }
                break;
            case KeyEvent.VK_1:
                Panel.gameSpeed = 1;
                break;
            case KeyEvent.VK_2:
                Panel.gameSpeed = 2;
                break;
            case KeyEvent.VK_3:
                Panel.gameSpeed = 3;
                break;
        }
    }
}