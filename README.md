import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class VirusEscapeGame extends JFrame {
    public VirusEscapeGame() {
        setTitle("Virus Escape");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        add(new GameBoard());
        pack();
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public static void main(String[] args) {
        new VirusEscapeGame();
    }
}

abstract class Entity {
    int x, y, size = 20;

    public Rectangle getBounds() {
        return new Rectangle(x, y, size, size);
    }

    public abstract void draw(Graphics g);
}

class Player extends Entity {
    int dx, dy;
    private static final String PLAYER_IMAGE_URL ="C:\\Users\\admin\\Downloads\\WhatsApp Image 2025-05-19 at 7.41.14 PM.jpeg";

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
        image = new ImageIcon(PLAYER_IMAGE_URL).getImage();
    }

    public void move() {
        x += dx;
        y += dy;
    }

    public void setDirection(int dx, int dy) {
        this.dx = dx;
        this.dy = dy;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}
class Virus extends Entity {
    Random rand = new Random();
    private static final String VIRUS_IMAGE_URL = "C:\\Users\\admin\\Downloads\\WhatsApp Image 2025-05-19 at 7.41.13 PM (1).jpeg";

    public Virus(int x, int y) {
        this.x = x;
        this.y = y;
        image = new ImageIcon(VIRUS_IMAGE_URL).getImage();
    }

    public void moveRandom() {
        x += (rand.nextInt(3) - 1) * size;
        y += (rand.nextInt(3) - 1) * size;
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}

class Patch extends Entity {
    private static final String PATCH_IMAGE_URL = "C:\\Users\\admin\\Downloads\\WhatsApp Image 2025-05-19 at 7.41.13 PM.jpeg";

    public Patch(int x, int y) {
        this.x = x;
        this.y = y;
        image = new ImageIcon(PATCH_IMAGE_URL).getImage();
    }

    @Override
    public void draw(Graphics g) {
        g.drawImage(image, x, y, size, size, null);
    }
}

class GameBoard extends JPanel implements ActionListener, KeyListener {
    private Player player;
    private ArrayList<Virus> viruses;
    private ArrayList<Patch> patches;
    private Timer timer;
    private int score = 0;
    private boolean gameOver = false;

    public GameBoard() {
        setPreferredSize(new Dimension(400, 400));
        setBackground(Color.BLACK);
        setFocusable(true);
        addKeyListener(this);
        initGame();
        timer = new Timer(200, this);
        timer.start();
    }
    private void initGame() {
        player = new Player(200, 200);
        viruses = new ArrayList<>();
        patches = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            viruses.add(new Virus(randCoord(), randCoord()));
        }
        for (int i = 0; i < 5; i++) {
            patches.add(new Patch(randCoord(), randCoord()));
        }
    }

    private int randCoord() {
        return (new Random().nextInt(20)) * 20;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (!gameOver) {
            player.move();
            for (Virus v : viruses) {
                v.moveRandom();
                if (player.getBounds().intersects(v.getBounds())) {
                    gameOver = true;
                }
            }
            patches.removeIf(patch -> {
                if (player.getBounds().intersects(patch.getBounds())) {
                    score++;
                    return true;
                }
                return false;
            });
            if (patches.isEmpty()) {
                for (int i = 0; i < 5; i++) {
                    patches.add(new Patch(randCoord(), randCoord()));
                }
            }
            repaint();
        }
    }
