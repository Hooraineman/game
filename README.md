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



