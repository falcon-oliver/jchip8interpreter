package org.chip8.display;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.HashMap;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

public class Gui extends JFrame implements KeyListener {
    private final int SCALE = 15;
    private final int PIXEL_ON = 0xFFFFFF;
    private final int PIXEL_OFF = 0x000000;
    private final String NO_ROM = "Please insert ROM";
    private int currentKeyCode;
    private boolean romInserted = false;
    private boolean keyPressed = false, pause = false;
    private BufferedImage gameScreen;
    private BufferStrategy bs;
    private Canvas gameCanvas;
    private File loadedRom;
    private Graphics g;
    private HashMap<Integer, Integer> keyMapper;

    public Gui(int width, int height) {
        loadedRom = null;
        this.setTitle("Interpreter");
        gameCanvas = createCanvas(width, height);
        gameScreen = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        gameCanvas.addKeyListener(this);
        this.setPreferredSize(new Dimension(width * SCALE, height * SCALE));
        this.setSize(new Dimension(width * SCALE, height * SCALE));
        this.add(gameCanvas);
        this.pack();
        this.setJMenuBar(getMenu());
        this.setLocationRelativeTo(null);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.addKeyListener(this);
        this.update(gameCanvas.getGraphics());
        this.setVisible(true);
        keyMapper = getMappedKeys();
        gameCanvas.createBufferStrategy(2);
        bs = gameCanvas.getBufferStrategy();
    }

    private JMenuBar getMenu() {
        JMenuBar menubar = new JMenuBar();
        JMenu file = new JMenu("File");
        JMenuItem openRom = new JMenuItem("Open ROM");
        JMenuItem flowButton = new JMenuItem("Pause");
        openRom.addActionListener((ActionEvent e) -> loadedRom = getInputFile());
        flowButton.addActionListener((ActionEvent e) -> {
            pause = !pause;
            flowButton.setText(pause?"Play":"Pause");
        });  
        file.add(openRom);
        menubar.add(file);
        menubar.add(flowButton);
        return menubar;
    }

    public void noRomInsertedScreen() {
        g = bs.getDrawGraphics();
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, this.getWidth(), this.getHeight());
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString(NO_ROM, (int) ((this.getWidth() / 2) - ((NO_ROM.length() / 2) * 10)) - 10, (int) (0.5 * (this.getHeight()) - 40));
        bs.show();
    }

    public void renderGame() {
        g = bs.getDrawGraphics();
        g.drawImage(gameScreen, 0, 0, gameCanvas.getWidth(), gameCanvas.getHeight(), null);
        bs.show();
    }

    private Canvas createCanvas(int width, int height) {
        Canvas canvas = new Canvas();
        canvas.setPreferredSize(new Dimension(width, height));
        return canvas;
    }

    private File getInputFile() {
        JFileChooser fileSelect = new JFileChooser("");
        fileSelect.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) {
                    return true;
                } else {
                    String filename = file.getName().toLowerCase();
                    return filename.endsWith(".ch8");
                }
            }
            @Override
            public String getDescription() {
                return "Chip8 files (*.ch8)";
            }
        });
        fileSelect.setAcceptAllFileFilterUsed(false);

        if ((romInserted=(fileSelect.showOpenDialog(null) == JFileChooser.APPROVE_OPTION)))
            return fileSelect.getSelectedFile();
        return null;
    }

    private HashMap<Integer, Integer> getMappedKeys() {
        HashMap<Integer, Integer> keyMapper = new HashMap<>();
        keyMapper.put(KeyEvent.VK_1, 0x1);
        keyMapper.put(KeyEvent.VK_2, 0x2);
        keyMapper.put(KeyEvent.VK_3, 0x3);
        keyMapper.put(KeyEvent.VK_4, 0xC);
        keyMapper.put(KeyEvent.VK_Q, 0x4);
        keyMapper.put(KeyEvent.VK_W, 0x5);
        keyMapper.put(KeyEvent.VK_E, 0x6);
        keyMapper.put(KeyEvent.VK_R, 0xD);
        keyMapper.put(KeyEvent.VK_A, 0x7);
        keyMapper.put(KeyEvent.VK_S, 0x8);
        keyMapper.put(KeyEvent.VK_D, 0x9);
        keyMapper.put(KeyEvent.VK_F, 0xE);
        keyMapper.put(KeyEvent.VK_Z, 0xA);
        keyMapper.put(KeyEvent.VK_X, 0x0);
        keyMapper.put(KeyEvent.VK_C, 0xB);
        keyMapper.put(KeyEvent.VK_V, 0xF);
        keyMapper.put(KeyEvent.VK_UP, 0x2);
        keyMapper.put(KeyEvent.VK_DOWN, 0x8);
        keyMapper.put(KeyEvent.VK_LEFT, 0x4);
        keyMapper.put(KeyEvent.VK_RIGHT, 0x6);
        return keyMapper;
    }
    
    public void updateGameScreen(int frameBuffer[][]) {
        for (int x = 0; x < frameBuffer.length; x++) {
            for (int y = 0; y < frameBuffer[x].length; y++) {
                if (frameBuffer[x][y] == 1)
                    gameScreen.setRGB(x, y, PIXEL_ON);
                else
                    gameScreen.setRGB(x, y, PIXEL_OFF);
            }
        }
    }

    private int getMapedKeyCode(int keyCode) {
        return keyMapper.getOrDefault(keyCode, -1);
    }

    @Override
    public void keyPressed(KeyEvent e) {
        currentKeyCode = getMapedKeyCode(e.getKeyCode());
        if (currentKeyCode != -1)
            keyPressed = true;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int releasedKey = getMapedKeyCode(e.getKeyCode());
        if (releasedKey == currentKeyCode) {
            keyPressed = false;
            currentKeyCode = -1;
        }
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    public File getLoadedRom() {
        return loadedRom;
    }

    public BufferedImage getGameScreen() {
        return gameScreen;
    }

    public int getKeyCode() {
        return currentKeyCode;
    }

    public boolean isKeyPressed() {
        return keyPressed;
    }

    public boolean getPause() {
        return pause;
    }
    public boolean romInserted() {
        return romInserted;
    }
}
