
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.Random;
import java.awt.Rectangle;
import java.awt.Color;
import java.awt.Font;

import java.io.*;
import javax.swing.JFrame;
import javax.swing.Timer;

public class FlappyBird implements ActionListener, MouseListener, KeyListener {

    public final int WIDTH = 800, HEIGHT = 800;

    public static FlappyBird flappyBird;

    public Renderer renderer;

    public Rectangle bird;

    public ArrayList<Rectangle> columns;

    public static boolean gameOver, started;

    public int ticks;

    public static int score, yMotion, highscore;

    public Random random;

    public static JFrame jframe;

    public static Timer timer;

    public FlappyBird() {
        jframe = new JFrame();
        timer = new Timer(20, this);

        renderer = new Renderer();
        random = new Random();

        jframe.add(renderer);
        jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jframe.setSize(WIDTH, HEIGHT);
        jframe.addMouseListener(this);
        jframe.addKeyListener(this);
        jframe.setResizable(false);
        jframe.setTitle("Flappy Bird");
        if (FGAME.loginFrame.isVisible()) {
            jframe.setLocationRelativeTo(FGAME.loginFrame);
            FGAME.loginFrame.setVisible(false);
        }

        jframe.setVisible(true);

        bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

        columns = new ArrayList<Rectangle>();

        addColumn(true);
        addColumn(true);
        addColumn(true);
        addColumn(true);

        highscore = getHighScore();

        timer.start();
    }

    void addColumn(boolean start) {

        int space = 300;
        int width = 100;
        int height = 50 + random.nextInt(300);

        if (start) {
            // Lower column
            columns.add(new Rectangle(WIDTH + width + columns.size() * 300, HEIGHT - height - 170, width, height));
            // Upper column
            columns.add(new Rectangle(WIDTH + width + (columns.size() - 1) * 300, 0, width, HEIGHT - height - space));
        } else {
            // Lower column
            columns.add(new Rectangle(columns.get(columns.size() - 1).x + 600, HEIGHT - height - 170, width, height));
            // Upper column
            columns.add(new Rectangle(columns.get(columns.size() - 1).x, 0, width, HEIGHT - height - space));
        }

    }

    public void paintColumn(Graphics g, Rectangle column) {
        g.setColor(new Color(0, 102, 0));
        g.fillRect(column.x, column.y, column.width, column.height);
    }

    public void jump() {
        if (gameOver) {

            timer.stop();
            bird = new Rectangle(WIDTH / 2 - 10, HEIGHT / 2 - 10, 20, 20);

            columns.clear();

            addColumn(true);
            addColumn(true);
            addColumn(true);
            addColumn(true);

            FGAME.yourscoreFrame.setVisible(true);
        }

        if (!started) {
            started = true;
        } else if (!gameOver) {
            if (yMotion > 0) // to remove drag
            {
                yMotion = 0;
            }

            yMotion -= 10;
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        int speed = 5;

        ticks++;

        if (started && !gameOver) {
            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);

                column.x -= speed;
            }

            if (ticks % 2 == 0 && yMotion < 15) {
                yMotion += 2;
            }

            for (int i = 0; i < columns.size(); i++) {
                Rectangle column = columns.get(i);

                if (column.x + column.width < 0) {
                    columns.remove(column);

                    if (column.y == 0) {
                        addColumn(false);

                    }
                }
            }

            bird.y += yMotion;

            if (bird.y < 0 || bird.y > HEIGHT - bird.height - 170) {
                gameOver = true;

            }

            for (Rectangle column : columns) {
                if (column.y == 0 && bird.x + bird.width / 2 > column.x + column.width / 2 - speed && bird.x + bird.width / 2 < column.x + column.width / 2 + speed) {
                    score++;
                }

                if (column.intersects(bird)) {
                    gameOver = true;
                }
            }
        }

        renderer.repaint();
    }

    public static int getHighScore() {
        String sql = "select max(score) from highscore;";
        DBHelper ob = new DBHelper();
        String num = ob.checkData(sql);
        if(num == null)
            return 0;
        else
            return Integer.parseInt(num);
    }

    public void repaint(Graphics g) {
        g.setColor(Color.blue);
        g.fillRect(0, 0, WIDTH, HEIGHT);

        g.setColor(new Color(102, 51, 0));
        g.fillRect(0, HEIGHT - 170, WIDTH, 150);

        g.setColor(Color.green);
        g.fillRect(0, HEIGHT - 170, WIDTH, 20);

        g.setColor(Color.red);
        g.fillRect(bird.x, bird.y, bird.width, bird.height);

        for (Rectangle column : columns) {
            paintColumn(g, column);
        }

        g.setColor(Color.white);
        g.setFont(new Font("Arial", 1, 100));

        if (!started) {
            g.drawString("Click to start!", 80, HEIGHT / 2 - 50);
        }

        if (gameOver) {

            g.drawString("Game Over!", 100, HEIGHT / 2 - 50);
            g.setFont(new Font("Arial", 1, 60));
            g.drawString("Score:" + String.valueOf(score), 250, HEIGHT / 2 + 50);
            g.setFont(new Font("Arial", 1, 40));
            
            highscore = getHighScore();
            if(score > highscore)
                highscore = score;
            g.drawString("High Score:" + String.valueOf(highscore), WIDTH - 330, 50);
        }

        if (!gameOver && started) {
            g.drawString(String.valueOf(score), WIDTH / 2 - 25, 100);
        }

    }

    public static void main(String[] args) {
//        flappyBird = new FlappyBird();
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub
        jump();
    }

    @Override
    public void mousePressed(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyTyped(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyPressed(KeyEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void keyReleased(KeyEvent e) {
        // TODO Auto-generated method stub
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump();
        }

    }
}
