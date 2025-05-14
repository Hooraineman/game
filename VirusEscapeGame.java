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

    public Player(int x, int y) {
        this.x = x;
        this.y = y;
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
        g.setColor(Color.BLUE);
        g.fillRect(x, y, size, size);
    }
}

class Virus extends Entity {
    Random rand = new Random();

    public Virus(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void moveRandom() {
        x += (rand.nextInt(3) - 1) * size;
        y += (rand.nextInt(3) - 1) * size;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, size, size);
    }
}

class Patch extends Entity {
    public Patch(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public void draw(Graphics g) {
        g.setColor(Color.GREEN);
        g.fillRect(x, y, size, size);
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

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (!gameOver) {
            player.draw(g);
            for (Virus v : viruses)
                v.draw(g);
            for (Patch p : patches)
                p.draw(g);
            g.setColor(Color.WHITE);
            g.drawString("Score: " + score, 10, 20);
        } else {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 30));
            g.drawString("Game Over!", 120, 180);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Score: " + score, 160, 210);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        switch (key) {
            case KeyEvent.VK_LEFT -> player.setDirection(-20, 0);
            case KeyEvent.VK_RIGHT -> player.setDirection(20, 0);
            case KeyEvent.VK_UP -> player.setDirection(0, -20);
            case KeyEvent.VK_DOWN -> player.setDirection(0, 20);
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
