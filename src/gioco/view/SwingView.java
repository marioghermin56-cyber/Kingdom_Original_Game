package gioco.view;

import gioco.model.*;
import javax.swing.*;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SwingView implements IView {
    private JFrame frame;
    private IModel model;
    private GamePanel gamePanel;
    
    private JButton archerButton, mageButton, cannonButton, barracksButton, rallyButton, upgradeButton, musicButton, soundButton;
    private JButton pauseBtn, resumeBtn, restartBtn, quitBtn;
    private Font font, mainFont, winLoseFont;
    
    private JButton btnStart;
    private JPanel levelsPanel;
    private javax.swing.Timer flashTimer; 
    
    private JButton btnLevel1, btnLevel2, btnLevel3;
    private JButton btnBackMenu; 
    
    private BufferedImage playIcon, menuImage;
    private BufferedImage musicOnIcon, musicOffIcon, soundOnIcon, soundOffIcon, homeIcon, infoIcon; 
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    private JPanel menuPanel;
    private JPanel pausePanel;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    
    public SwingView() {
        frame = new JFrame("Pseudo Kingdom Rush - MVC Alpha");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        archerButton = createTransparentButton();
        mageButton = createTransparentButton();
        barracksButton = createTransparentButton();
        cannonButton = createTransparentButton();
        
        rallyButton = createUpgradeButton();
        
        pauseBtn = createTransparentButton();
        pauseBtn.setIcon(scaleIcon(loadImage("/assets/background/button_pause.png"), 40, 40)); 
        pauseBtn.setVisible(true);
        
        gamePanel = new GamePanel();
        gamePanel.setLayout(null); 

        pausePanel = new JPanel(new GridBagLayout());
        pausePanel.setBounds(0, 0, 1056, 864);
        pausePanel.setBackground(new Color(0, 0, 0, 150));
        pausePanel.setVisible(false);
        
        resumeBtn = createTransparentButton();
        resumeBtn.setIcon(scaleIcon(loadImage("/assets/background/button_left.png"), 60, 60)); 
        resumeBtn.setFocusPainted(false);
        resumeBtn.setVisible(true);
        resumeBtn.setAlignmentX(java.awt.Component.CENTER_ALIGNMENT); 

        JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 30, 10));
        buttonPanel.setOpaque(false); 
        
        restartBtn = createTransparentButton();
        restartBtn.setIcon(scaleIcon(loadImage("/assets/background/restart_button.png"), 117, 70)); 
        restartBtn.setFocusPainted(false); 
        restartBtn.setVisible(true);
        
        quitBtn = createTransparentButton();
        quitBtn.setIcon(scaleIcon(loadImage("/assets/background/quit_button.png"), 117, 70)); 
        quitBtn.setFocusPainted(false); 
        quitBtn.setVisible(true);
        
        buttonPanel.add(restartBtn);
        buttonPanel.add(quitBtn);

        JPanel wrapperPanel = new JPanel();
        wrapperPanel.setLayout(new BoxLayout(wrapperPanel, BoxLayout.Y_AXIS));
        wrapperPanel.setOpaque(false);
        
        wrapperPanel.add(resumeBtn); 
        wrapperPanel.add(javax.swing.Box.createRigidArea(new java.awt.Dimension(0, 20))); 
        wrapperPanel.add(buttonPanel); 
        
        buttonPanel.setPreferredSize(new Dimension(250, 120)); 
        pausePanel.add(wrapperPanel);
        
        upgradeButton = createUpgradeButton();

        gamePanel.add(pausePanel);
        gamePanel.add(pauseBtn);
        gamePanel.add(archerButton);
        gamePanel.add(mageButton);
        gamePanel.add(cannonButton);
        gamePanel.add(barracksButton);
        gamePanel.add(rallyButton);
        gamePanel.add(upgradeButton);

        initMenuPanel();
        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(gamePanel, "GAME");
        frame.add(mainContainer);
        
        cardLayout.show(mainContainer, "MENU");
        frame.setVisible(true);
    }
    
    public double getScaleX() { return scaleX; }
    public double getScaleY() { return scaleY; }
    
    @Override
    public void switchToMenu() {
        if (btnStart != null && levelsPanel != null) {
            btnStart.setVisible(true);
            levelsPanel.setVisible(false);
            if (btnBackMenu != null) btnBackMenu.setVisible(false); 
            
            if (flashTimer != null && !flashTimer.isRunning()) {
                flashTimer.start();
            }
        }
        cardLayout.show(mainContainer, "MENU");
        mainContainer.revalidate();
        mainContainer.repaint();
    }
    
    @Override
    public void updateUnlockedLevels(int maxUnlockedLevel, java.util.Map<Integer, Integer> levelStars) {
        String starStyle = "font-family: Arial; font-size: 26px; vertical-align: 2px; color: #FFD700;"; 

        if (btnLevel1 != null) {
            btnLevel1.setEnabled(maxUnlockedLevel >= 1);
            btnLevel1.setText("<html>LIVELLO 1 &nbsp;<span style='" + starStyle + "'>" + getStarString(levelStars.getOrDefault(1, 0)) + "</span></html>");
        }
        
        if (btnLevel2 != null) {
            btnLevel2.setEnabled(maxUnlockedLevel >= 2);
            btnLevel2.setText(maxUnlockedLevel >= 2 ? "<html>LIVELLO 2 &nbsp;<span style='" + starStyle + "'>" + getStarString(levelStars.getOrDefault(2, 0)) + "</span></html>" : "LIVELLO 2");
            btnLevel2.setForeground(maxUnlockedLevel >= 2 ? Color.WHITE : Color.GRAY);
            btnLevel2.setCursor(new Cursor(maxUnlockedLevel >= 2 ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        }
        
        if (btnLevel3 != null) {
            btnLevel3.setEnabled(maxUnlockedLevel >= 3);
            btnLevel3.setText(maxUnlockedLevel >= 3 ? "<html>LIVELLO 3 &nbsp;<span style='" + starStyle + "'>" + getStarString(levelStars.getOrDefault(3, 0)) + "</span></html>" : "LIVELLO 3");
            btnLevel3.setForeground(maxUnlockedLevel >= 3 ? Color.WHITE : Color.GRAY);
            btnLevel3.setCursor(new Cursor(maxUnlockedLevel >= 3 ? Cursor.HAND_CURSOR : Cursor.DEFAULT_CURSOR));
        }
        
        if (menuPanel != null) {
            menuPanel.repaint();
        }
    }

    private String getStarString(int stars) {
        if (stars == 3) return "★★★";
        if (stars == 2) return "★★☆";
        if (stars == 1) return "★☆☆";
        return "☆☆☆";
    }
    
    @Override
    public void addPauseListener(ActionListener listener) { pauseBtn.addActionListener(listener); }
    @Override
    public void addResumeListener(ActionListener listener) { resumeBtn.addActionListener(listener); }
    @Override
    public void addRestartListener(ActionListener listener) { restartBtn.addActionListener(listener); }
    @Override
    public void addQuitListener(ActionListener listener) { quitBtn.addActionListener(listener); }
    @Override
    public void addUpgradeListener(ActionListener listener) { upgradeButton.addActionListener(listener); }    
    @Override
    public void addArcherListener(ActionListener listener) { archerButton.addActionListener(listener); }
    @Override
    public void addMageListener(ActionListener listener) { mageButton.addActionListener(listener); }
    @Override
    public void addBarracksListener(ActionListener listener) { barracksButton.addActionListener(listener); }
    @Override
    public void addCannonListener(ActionListener listener) { cannonButton.addActionListener(listener); }
    @Override
    public void addRallyListener(ActionListener listener) { rallyButton.addActionListener(listener); }
    @Override
    public void addMusicListener(ActionListener listener) { musicButton.addActionListener(listener); }
    @Override
    public void addSoundListener(ActionListener listener) { soundButton.addActionListener(listener); }
    
    public JPanel getGamePanel() { return this.gamePanel; }
    
    @Override
    public void updateSoundIcon(boolean isMuted) {
        if (isMuted) {
            if (soundOffIcon != null) soundButton.setIcon(scaleIcon(soundOffIcon, 60, 60));
            else soundButton.setText("SOUND OFF");
        } else {
            if (soundOnIcon != null) soundButton.setIcon(scaleIcon(soundOnIcon, 60, 60));
            else soundButton.setText("SOUND ON");
        }
        soundButton.repaint();
    }
    
    @Override
    public void updateMusicIcon(boolean isMuted) {
        if (isMuted) {
            if (musicOffIcon != null) musicButton.setIcon(scaleIcon(musicOffIcon, 60, 60));
            else musicButton.setText("MUSIC OFF");
        } else {
            if (musicOnIcon != null) musicButton.setIcon(scaleIcon(musicOnIcon, 60, 60));
            else musicButton.setText("MUSIC ON");
        }
        musicButton.repaint();
    }

    @Override
    public void setStartButtonListener(ActionListener listener) {
    	if (btnLevel1 != null) btnLevel1.addActionListener(listener);
        if (btnLevel2 != null) btnLevel2.addActionListener(listener);
        if (btnLevel3 != null) btnLevel3.addActionListener(listener);
    }
    
    private ImageIcon scaleIcon(BufferedImage img, int width, int height) {
        if (img == null) return null;
        Image scaledImg = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        return new ImageIcon(scaledImg);
    }
    
    private void initMenuPanel() {
        menuImage = loadImage("/assets/background/menu.jpg");
        
        musicOnIcon = loadImage("/assets/background/button_music.png");
        musicOffIcon = loadImage("/assets/background/button_music_off.png");
        soundOnIcon = loadImage("/assets/background/button_sound.png");
        soundOffIcon = loadImage("/assets/background/button_sound_off.png");
        homeIcon = loadImage("/assets/background/button_left.png"); 
        
        menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuImage != null) {
                    g.drawImage(menuImage, 0, 0, getWidth(), getHeight(), null);
                }
                if (font != null) {
                    g.setFont(font.deriveFont(100f));
                    g.setColor(new Color(0, 0, 0));
                    String title = "KINGDOM RUSH";
                    int x = (getWidth() - g.getFontMetrics().stringWidth(title)) / 2;
                    g.drawString(title, x, 200);
                }
            }
        };
        
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel topPanel = new JPanel(new BorderLayout());
        topPanel.setOpaque(false);

        JPanel backContainer = new JPanel(new FlowLayout(FlowLayout.LEFT, 20, 0));
        backContainer.setOpaque(false);
        
        btnBackMenu = createTransparentButton();
        if (homeIcon != null) {
            btnBackMenu.setIcon(scaleIcon(homeIcon, 60, 60));
        } else {
            btnBackMenu.setText("\u2302"); 
            btnBackMenu.setFont(new Font("Arial", Font.PLAIN, 50));
        }
        btnBackMenu.setVisible(false); 
        
        btnBackMenu.addActionListener(e -> {
            btnStart.setVisible(true);
            levelsPanel.setVisible(false);
            btnBackMenu.setVisible(false); 
            if (flashTimer != null && !flashTimer.isRunning()) {
                flashTimer.start();
            }
        });
        backContainer.add(btnBackMenu);

        JPanel audioPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        audioPanel.setOpaque(false);

        musicButton = createTransparentButton();
        if (musicOnIcon != null) musicButton.setIcon(scaleIcon(musicOnIcon, 60, 60));
        else musicButton.setText("MUSIC ON");
        musicButton.setActionCommand("MUSIC");
        musicButton.setVisible(true);
        
        soundButton = createTransparentButton();
        if (soundOnIcon != null) soundButton.setIcon(scaleIcon(soundOnIcon, 60, 60));
        else soundButton.setText("SOUND ON");
        soundButton.setActionCommand("SOUND");
        soundButton.setVisible(true);

        audioPanel.add(musicButton);
        audioPanel.add(soundButton);
        topPanel.add(backContainer, BorderLayout.WEST);
        topPanel.add(audioPanel, BorderLayout.EAST);
       
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; 
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.NORTH;
        gbc.insets = new Insets(20, 0, 0, 20); 
        menuPanel.add(topPanel, gbc);
        
        gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu.wav");

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.setOpaque(false);
        
        levelsPanel = new JPanel(new GridBagLayout()); 
        levelsPanel.setOpaque(false); 
        levelsPanel.setVisible(false); 

        JPanel buttonsGrid = new JPanel(new GridLayout(3, 1, 0, 20));
        buttonsGrid.setOpaque(false);

        btnLevel1 = createLevelButton("LIVELLO 1", "1");
        btnLevel2 = createLevelButton("LIVELLO 2", "2");
        btnLevel3 = createLevelButton("LIVELLO 3", "3");

        buttonsGrid.add(btnLevel1);
        buttonsGrid.add(btnLevel2);
        buttonsGrid.add(btnLevel3);

        GridBagConstraints gbcLevels = new GridBagConstraints();
        gbcLevels.gridx = 0;
        gbcLevels.gridy = 0;
        gbcLevels.insets = new Insets(0, 0, 80, 0); 
        levelsPanel.add(buttonsGrid, gbcLevels);

        JButton btnExtra = createTransparentButton();
        btnExtra.setPreferredSize(new Dimension(60, 60));
        
        BufferedImage infoIcon = loadImage("/assets/background/button_info.png");
        
        if (infoIcon != null) {
            btnExtra.setIcon(scaleIcon(infoIcon, 60, 60));
        } else {
            System.out.println("ATTENZIONE: Immagine button_info.png non trovata!");
        }
        
        btnExtra.setVisible(true);
        btnExtra.addActionListener(e -> showInfoDialog());

        gbcLevels.gridy = 1;
        gbcLevels.insets = new Insets(0, 0, 0, 0);
        levelsPanel.add(btnExtra, gbcLevels); 

        btnStart = createOvalStartButton("START", "START");
        
        flashTimer = new javax.swing.Timer(600, e -> {
            if (btnStart.getForeground().getAlpha() == 255) {
                btnStart.setForeground(new Color(255, 255, 255, 0));
            } else {
                btnStart.setForeground(Color.WHITE);
            }
        });
        flashTimer.start(); 

        btnStart.addActionListener(e -> {
            flashTimer.stop(); 
            btnStart.setForeground(Color.WHITE); 
            btnStart.setVisible(false);   
            levelsPanel.setVisible(true); 
            btnBackMenu.setVisible(true); 
        });

        GridBagConstraints gbcInner = new GridBagConstraints();
        gbcInner.gridx = 0;
        gbcInner.gridy = 0;
        
        gbcInner.insets = new Insets(0, 0, 0, 0);
        centerPanel.add(btnStart, gbcInner);

        gbcInner.insets = new Insets(220, 0, 0, 0); 
        centerPanel.add(levelsPanel, gbcInner);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weightx = 1.0;
        gbc.weighty = 0.9;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(-120, 0, 0, 0); 
        menuPanel.add(centerPanel, gbc);
    }
    
    @Override
    public void render(IModel model) {
        this.model = model;
        gamePanel.repaint();
    }

    @Override
    public void showMessage(String message) {
        JOptionPane.showMessageDialog(frame, message);
    }

    @Override
    public void switchToGame() {
        cardLayout.show(mainContainer, "GAME");
    }
    
    private BufferedImage loadImage(String path) {
        try {
            return ImageIO.read(getClass().getResourceAsStream(path));
        } catch (Exception e) {
            System.err.println("Impossibile caricare asset: " + path);
            return null; 
        }
    }

    private JButton createTransparentButton() {
        JButton button = new JButton();
        button.setOpaque(false);
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setVisible(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); 
        button.setRolloverEnabled(false);
        return button;
    }
    
    private JButton createLevelButton(String text, String command) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                if (isEnabled()) {
                    g.setColor(new Color(0, 0, 0, 180)); 
                } else {
                    g.setColor(new Color(20, 20, 20, 200)); 
                }
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g); 
            }
        };
        
        button.setFont(mainFont != null ? mainFont.deriveFont(24f) : new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE); 
        button.setOpaque(false); 
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setActionCommand(command); 
        button.setPreferredSize(new Dimension(300, 60));
        
        return button;
    }
    
    private JButton createOvalStartButton(String text, String command) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(0, 0, 0, 180)); 
                g2.fillOval(0, 0, getWidth(), getHeight());
                
                g2.dispose();
                super.paintComponent(g); 
            }
        };
        
        button.setFont(mainFont != null ? mainFont.deriveFont(28f) : new Font("Arial", Font.BOLD, 60));
        button.setForeground(Color.WHITE); 
        button.setOpaque(false); 
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setActionCommand(command); 
        button.setPreferredSize(new Dimension(200, 64)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        return button;
    }
    
    private void showInfoDialog() {
        JDialog infoDialog = new JDialog((java.awt.Frame) null, "Info", JDialog.ModalityType.APPLICATION_MODAL);
        infoDialog.setUndecorated(true); 
        infoDialog.setSize(500, 380);
        infoDialog.setLocationRelativeTo(null); 
        
        JPanel contentPanel = new JPanel(new BorderLayout());
        contentPanel.setBackground(new Color(35, 35, 35)); 
        contentPanel.setBorder(BorderFactory.createLineBorder(new Color(120, 120, 120), 2)); 
        
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 10));
        topPanel.setOpaque(false); 
        
        JButton btnBack = new JButton("<");
        btnBack.setFont(new Font("Arial", Font.BOLD, 18));
        btnBack.setForeground(Color.WHITE);
        btnBack.setBackground(new Color(70, 70, 70)); 
        btnBack.setFocusPainted(false);
        btnBack.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btnBack.setMargin(new Insets(5, 10, 5, 10));
        btnBack.addActionListener(e -> infoDialog.dispose()); 
        
        topPanel.add(btnBack);
        contentPanel.add(topPanel, BorderLayout.NORTH);
        
        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BoxLayout(textPanel, BoxLayout.Y_AXIS));
        textPanel.setOpaque(false);
        textPanel.setBorder(BorderFactory.createEmptyBorder(0, 20, 20, 20));
        
        JLabel lblTitle = new JLabel("I nemici non devono passare!");
        lblTitle.setFont(new Font("Arial", Font.BOLD, 24));
        lblTitle.setForeground(new Color(255, 215, 0)); 
        lblTitle.setAlignmentX(Component.CENTER_ALIGNMENT); 
        
        JLabel lblDesc = new JLabel("SUGGERIMENTI");
        lblDesc.setFont(new Font("Arial", Font.PLAIN, 14)); 
        lblDesc.setForeground(Color.WHITE);
        lblDesc.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        JPanel towersPanel = new JPanel(new GridLayout(4, 1, 0, 10)); 
        towersPanel.setOpaque(false);
        towersPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 10, 20));
        
        towersPanel.add(createCustomLabel("🏹ARCIERE: fuoco rapido ed economico, ottimo contro i Goblin."));
        towersPanel.add(createCustomLabel("⚔️CASERMA: schiera soldati per bloccare l'avanzata."));
        towersPanel.add(createCustomLabel("🔮MAGO: attacchi letali, indispensabile contro gli Orchi."));
        towersPanel.add(createCustomLabel("💣CANNONE: danni ad area contro gli sciami nemici."));
      
        textPanel.add(lblTitle);
        textPanel.add(Box.createRigidArea(new Dimension(0, 10))); 
        textPanel.add(lblDesc);
        textPanel.add(towersPanel);
        
        contentPanel.add(textPanel, BorderLayout.CENTER);
        
        infoDialog.setContentPane(contentPanel);
        infoDialog.setVisible(true);
    }
    
    private JLabel createCustomLabel(String text) {
        String htmlText = "<html><span style='font-family: \"Segoe UI Emoji\", \"Apple Color Emoji\", Arial; font-size: 14px; color: white;'>" + text + "</span></html>";
        JLabel label = new JLabel(htmlText);
        return label;
    }

    private class GamePanel extends JPanel {
        
        private Map<Integer, BufferedImage[]> towerAssets = new HashMap<>();
        private Map<Integer, BufferedImage> projectileAssets = new HashMap<>();
        private Map<Integer, BufferedImage[]> enemyAssets = new HashMap<>();
        
        private Map<Integer, BufferedImage[]> enemyAttackAssets = new HashMap<>();
        private Map<Integer, BufferedImage[]> enemyDeathAssets = new HashMap<>();
        private BufferedImage[] soldierDeathFrames = new BufferedImage[20];
        
        private BufferedImage lifespan, redBar;
        private BufferedImage hoveredSlot;
        private BufferedImage[] soldierFrames = new BufferedImage[20];
        private BufferedImage[] soldierFightFrames = new BufferedImage[20];
        private BufferedImage[] soldierIdleFrames = new BufferedImage[20];
        private BufferedImage radialMenuImage;
        
        private Map<Projectile, Double> previousX = new HashMap<>();
        private Map<Projectile, Double> previousY = new HashMap<>();
        private Map<Projectile, Double> previousAngle = new HashMap<>();
        
        public GamePanel() {
            this.setPreferredSize(new Dimension(1056,864 ));
            setBackground(Color.BLUE);
            loadAssets();
        }
        
        private void loadAssets() {
        	radialMenuImage = loadImage("/assets/background/slotMenu6.png");
            
            towerAssets.put(Tower.ARCHER_TYPE, new BufferedImage[]{
                loadImage("/assets/ARCHER_TOWER/7.png"), loadImage("/assets/ARCHER_TOWER/8.png"), loadImage("/assets/ARCHER_TOWER/9.png")
            });
            towerAssets.put(Tower.MAGE_TYPE, new BufferedImage[]{
                loadImage("/assets/MAGE_TOWER/11.png"), loadImage("/assets/MAGE_TOWER/12.png"), loadImage("/assets/MAGE_TOWER/13.png")
            });
            towerAssets.put(Tower.CANNON_TYPE, new BufferedImage[]{
                loadImage("/assets/CANNON_TOWER/15.png"), loadImage("/assets/CANNON_TOWER/16.png"), loadImage("/assets/CANNON_TOWER/17.png")
            });
            towerAssets.put(Tower.BARRACKS_TYPE, new BufferedImage[]{
                loadImage("/assets/BARRACK_TOWER/7.png"), loadImage("/assets/BARRACK_TOWER/8.png"), loadImage("/assets/BARRACK_TOWER/9.png")
            });
           
            projectileAssets.put(Projectile.ARCHER_PROJECTILE, loadImage("/assets/ARCHER_TOWER/37.png"));
            projectileAssets.put(Projectile.MAGE_PROJECTILE, loadImage("/assets/MAGE_TOWER/10.png"));
            projectileAssets.put(Projectile.CANNON_PROJECTILE, loadImage("/assets/CANNON_TOWER/29.png"));
            lifespan = loadImage("/assets/HEALTHBAR/health_bar-05.png");
            redBar = loadImage("/assets/HEALTHBAR/health_bar-04.png");
            hoveredSlot = loadImage("/assets/background/39.png");

            for (int i = 0; i < 20; i++) {
                soldierFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_%03d.png", i));
                soldierIdleFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_idle_%03d.png", i));
                soldierFightFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_attack_%03d.png", i));
                soldierDeathFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_die_%03d.png", i));
            }
            
            // --- CARICAMENTO ORCO ---
            BufferedImage[] orcFrames = new BufferedImage[19];
            for (int i = 0; i < 19; i++) {
                orcFrames[i] = loadImage(String.format("/assets/ORC/5_enemies_1_walk_%03d.png", i + 1));
            }
            BufferedImage[] orcAttackFrames = new BufferedImage[20];
            BufferedImage[] orcDeathFrames = new BufferedImage[20];
            for (int i = 0; i < 20; i++) {
                orcAttackFrames[i] = loadImage(String.format("/assets/ORC/5_enemies_1_attack_%03d.png", i));
                orcDeathFrames[i] = loadImage(String.format("/assets/ORC/5_enemies_1_die_%03d.png", i));
            }
            enemyAssets.put(Enemy.ORC_TYPE, orcFrames);
            enemyAttackAssets.put(Enemy.ORC_TYPE, orcAttackFrames);
            enemyDeathAssets.put(Enemy.ORC_TYPE, orcDeathFrames);
            
            BufferedImage[] scorpionFrames = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                scorpionFrames[i] = loadImage(String.format("/assets/SCORPION/1_enemies_1_walk_%03d.png", i + 1));
            }
            BufferedImage[] scorpionAttackFrames = new BufferedImage[10];
            BufferedImage[] scorpionDeathFrames = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                scorpionAttackFrames[i] = loadImage(String.format("/assets/SCORPION/1_enemies_1_attack_%03d.png", i));
                scorpionDeathFrames[i] = loadImage(String.format("/assets/SCORPION/1_enemies_1_die_%03d.png", i));
            }
            enemyAssets.put(Enemy.SCORPION_TYPE, scorpionFrames);
            enemyAttackAssets.put(Enemy.SCORPION_TYPE, scorpionAttackFrames);
            enemyDeathAssets.put(Enemy.SCORPION_TYPE, scorpionDeathFrames);
            
            BufferedImage[] goblinFrames = new BufferedImage[15];
            for (int i = 0; i < 15; i++) {
                goblinFrames[i] = loadImage(String.format("/assets/GOBLIN/3_enemies_1_walk_%03d.png", i + 1));
            }
            BufferedImage[] goblinAttackFrames = new BufferedImage[15];
            BufferedImage[] goblinDeathFrames = new BufferedImage[15];
            for (int i = 0; i < 15; i++) {
                goblinAttackFrames[i] = loadImage(String.format("/assets/GOBLIN/3_enemies_1_attack_%03d.png", i));
                goblinDeathFrames[i] = loadImage(String.format("/assets/GOBLIN/3_enemies_1_die_%03d.png", i));
            }
            enemyAssets.put(Enemy.GOBLIN_TYPE, goblinFrames);
            enemyAttackAssets.put(Enemy.GOBLIN_TYPE, goblinAttackFrames);
            enemyDeathAssets.put(Enemy.GOBLIN_TYPE, goblinDeathFrames);
            
            BufferedImage[] bossFrames = new BufferedImage[9];
            for (int i = 0; i < 9; i++) {
                bossFrames[i] = loadImage(String.format("/assets/BOSS/2_enemies_1_WALK_%03d.png", i + 1));
            }
            BufferedImage[] bossAttackFrames = new BufferedImage[10];
            BufferedImage[] bossDeathFrames = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                bossAttackFrames[i] = loadImage(String.format("/assets/BOSS/2_enemies_1_ATTACK_%03d.png", i));
                bossDeathFrames[i] = loadImage(String.format("/assets/BOSS/2_enemies_1_DIE_%03d.png", i));
            }
            enemyAssets.put(4, bossFrames);
            enemyAttackAssets.put(4, bossAttackFrames);
            enemyDeathAssets.put(4, bossDeathFrames);

            try {
                java.io.InputStream is = getClass().getResourceAsStream("/assets/background/Grandover.ttf");
                font = Font.createFont(Font.TRUETYPE_FONT, is);
                GraphicsEnvironment.getLocalGraphicsEnvironment().registerFont(font);
                mainFont = font.deriveFont(20f);
                winLoseFont = font.deriveFont(60f);
            } catch (Exception e) {
                mainFont = new Font("Arial", Font.BOLD, 18);
                font = new Font("Arial", Font.BOLD, 50);
                winLoseFont = new Font("Arial", Font.BOLD, 60);
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (model == null) return;
            
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            scaleX = getWidth() / 1056.0;
            scaleY = getHeight() / 864.0;
            
            pauseBtn.setBounds((int)(980 * scaleX), (int)(20 * scaleY), 50, 50);
            pausePanel.setVisible(model.isPaused());
            pausePanel.setBounds(0, 0, (int)(1056 * scaleX), (int)(864 * scaleY));

            AffineTransform oldTransform = g2d.getTransform();
            g2d.scale(scaleX, scaleY);

            Level currentLevel = model.getCurrentLevel();
            if (currentLevel.getBackLayerImage() != null) {
                g2d.drawImage(currentLevel.getBackLayerImage(), 0, 0, 1056, 864, null); 
            }
            
            drawEnemies(g2d);
            drawSoldiers(g2d);
            
            if (currentLevel.getTopLayerImage() != null) {
                g2d.drawImage(currentLevel.getTopLayerImage(), 0, 0, 1056, 864, null); 
            }
            
            drawSlots(g2d);
            drawProjectiles(g2d);
            
            g2d.setTransform(oldTransform);
            drawUI(g2d);
            
            TowerSlot selectedSlot = model.getSelectedBuildSlot();
            if (selectedSlot != null && !selectedSlot.isOccupied() && radialMenuImage != null) {
                int logicalCx = selectedSlot.getX() + (selectedSlot.getWidth() / 2);
                int logicalCy = selectedSlot.getY() + (selectedSlot.getHeight() / 2);
                int screenCx = (int) (logicalCx * scaleX);
                int screenCy = (int) (logicalCy * scaleY);
                double uniformScale = Math.min(scaleX, scaleY);
                int screenMenuSize = (int) (180 * uniformScale); 
                g2d.drawImage(radialMenuImage, screenCx - (screenMenuSize / 2), screenCy - (screenMenuSize / 2), screenMenuSize, screenMenuSize, null);
            }
            
            updateMenuButton();
        }
        
        private void drawShadowText(Graphics2D g, String text, int x, int y, Color color, int shadowOffset) {
            g.setColor(Color.BLACK);
            g.drawString(text, x + shadowOffset, y + shadowOffset); 
            g.setColor(color);
            g.drawString(text, x, y);
        }

        private void drawSlots(Graphics2D g) {
            for (TowerSlot slot : model.getAvailableSlots()) {
                int cx = slot.getX() + (slot.getWidth() / 2);
                int cy = slot.getY() + (slot.getHeight() / 2);

                if (slot.isOccupied()) {
                    Tower tower = slot.getTower();
                    BufferedImage[] levelsArray = towerAssets.get(tower.getType());
                    int imageIndex = tower.getLvl() - 1;
                    
                    if (levelsArray != null && imageIndex < levelsArray.length && levelsArray[imageIndex] != null) {
                        g.drawImage(levelsArray[imageIndex], cx - 27, cy - 55, 60, 70, null);
                    } else {
                        if (tower.getType() == Tower.ARCHER_TYPE) g.setColor(new Color(34, 139, 34)); 
                        else g.setColor(new Color(128, 0, 128));
                        g.fill3DRect(cx - 25, cy - 25, 50, 50, true);
                        g.setColor(Color.WHITE);
                        g.drawString("Lvl " + tower.getLvl(), cx - 10, cy);
                    } 
                } else if (slot == model.getHoveredSlot()) {
                    if (hoveredSlot != null) {
                        g.drawImage(hoveredSlot, slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight(), null);
                    } else {
                        g.setColor(new Color(255, 215, 0, 70)); 
                        g.fillRect(slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight());
                    }
                }
            }
            
            TowerSlot slotToHighlight = null;
            TowerSlot selectedSlot = model.getSelectedBuildSlot();
            
            if (selectedSlot != null && selectedSlot.isOccupied()) {
                int cx = selectedSlot.getX() + (selectedSlot.getWidth() / 2);
                int cy = selectedSlot.getY() + (selectedSlot.getHeight() / 2);
                int r = selectedSlot.getTower().getRange();
                
                g.setColor(new Color(255, 255, 255, 30)); 
                g.fillOval(cx - r, cy - r, r * 2, r * 2);
                g.setColor(new Color(255, 255, 255, 100));
                g.drawOval(cx - r, cy - r, r * 2, r * 2);
                
                if (selectedSlot.getTower().getType() == Tower.BARRACKS_TYPE) slotToHighlight = selectedSlot;
            } else if (model.isSettingRallyPoint()) {
                slotToHighlight = model.getActiveBarracksSlot();
            }

            if (slotToHighlight != null) {
                int cx = slotToHighlight.getX() + (slotToHighlight.getWidth() / 2);
                int cy = slotToHighlight.getY() + (slotToHighlight.getHeight() / 2);
                int r = slotToHighlight.getTower().getRange(); 
                
                g.setColor(new Color(0, 0, 255, 30)); 
                g.fillOval(cx - r, cy - r, r * 2, r * 2);
                g.setColor(new Color(255, 255, 255, 100));
                g.drawOval(cx - r, cy - r, r * 2, r * 2);
            }
        }
        
        private void updateMenuButton() {
            TowerSlot slot = model.getSelectedBuildSlot();
            
            if (slot == null) {
                archerButton.setVisible(false);
                mageButton.setVisible(false);
                cannonButton.setVisible(false);
                barracksButton.setVisible(false);
                rallyButton.setVisible(false);
                upgradeButton.setVisible(false);
                return;
            }
            
            int logicalCx = slot.getX() + (slot.getWidth() / 2);
            int logicalCy = slot.getY() + (slot.getHeight() / 2);
            int screenCx = (int) (logicalCx * scaleX);
            int screenCy = (int) (logicalCy * scaleY);
            
            int menuSize = 150;
            double uniformScale = Math.min(scaleX, scaleY);
            int halfSize = (int) ((menuSize / 2.0) * uniformScale);

            if (!slot.isOccupied()) {
                archerButton.setBounds(screenCx - halfSize, screenCy - halfSize, halfSize, halfSize); 
                barracksButton.setBounds(screenCx, screenCy - halfSize, halfSize, halfSize);       
                mageButton.setBounds(screenCx - halfSize, screenCy, halfSize, halfSize);           
                cannonButton.setBounds(screenCx, screenCy, halfSize, halfSize);                 
                
                archerButton.setVisible(true);
                mageButton.setVisible(true);
                cannonButton.setVisible(true);
                barracksButton.setVisible(true);
                
                rallyButton.setVisible(false);
                upgradeButton.setVisible(false);
            } else {
                archerButton.setVisible(false);
                mageButton.setVisible(false);
                cannonButton.setVisible(false);
                barracksButton.setVisible(false);
                
                Tower tower = slot.getTower();
                
                if (tower.getType() == Tower.BARRACKS_TYPE) {
                	int btnWidth = 85;
                    int btnHeight = 35;
                    
                	rallyButton.setBounds(screenCx - (btnWidth / 2), screenCy - 85, btnWidth, btnHeight);
                	rallyButton.setFont(mainFont != null ? mainFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16));
                    rallyButton.setText("MOVE");
                    Color moveColor = new Color(103, 76, 62, 220);
                    rallyButton.setBackground(moveColor);
                    rallyButton.setVisible(true);
                } else {
                    rallyButton.setVisible(false);
                }
                
                if (tower.canUpgrade()) {
                	int btnWidth = 170;
                    int btnHeight = 35;
                    
                	upgradeButton.setBounds(screenCx - (btnWidth / 2), screenCy + 15, btnWidth, btnHeight);
                    upgradeButton.setText("UPGRADE: " + tower.getUpgradeCost() + "g");
                    
                    Color affordableColor = new Color(104, 163, 87, 220); 
                    Color unaffordableColor = new Color(160, 70, 63, 220); 
                    
                    upgradeButton.setBackground(model.getGold() < tower.getUpgradeCost() ? unaffordableColor : affordableColor);
                    upgradeButton.setVisible(true);
                }else {
                    upgradeButton.setVisible(false);
                }
            }
        }
        
        private void drawProjectiles(Graphics2D g) {
            previousX.keySet().retainAll(model.getActiveProjectiles());
            previousY.keySet().retainAll(model.getActiveProjectiles());
            previousAngle.keySet().retainAll(model.getActiveProjectiles());

            for (Projectile p : model.getActiveProjectiles()) {
                double maxArcHeight;
                
                switch (p.getType()) {
                    case Projectile.ARCHER_PROJECTILE -> maxArcHeight = 0.0;  
                    case Projectile.MAGE_PROJECTILE -> maxArcHeight = 0.0;    
                    case Projectile.CANNON_PROJECTILE -> maxArcHeight = 90.0; 
                    default -> maxArcHeight = 0.0;
                }
                
                double totalDist = p.getTotalDistanceToTravel(); 
                double distTraveled = p.getDistanceTraveled();
                
                double arcScale = Math.min(1.0, totalDist / 150.0);
                double adjustedArcHeight = maxArcHeight * arcScale;

                double progress = 0;
                if (totalDist > 0) {
                    progress = distTraveled / totalDist;
                }
                if (Double.isNaN(progress) || progress > 1.0) {
                    progress = 1.0; 
                }

                double parabolaFactor = 4.0 * progress * (1.0 - progress);
                double currentHeightOffset = adjustedArcHeight * parabolaFactor;

                double exactX = p.getX();
                double exactY = p.getY() - currentHeightOffset;

                boolean isFirstFrame = !previousX.containsKey(p);
                double oldX = previousX.getOrDefault(p, exactX);
                double oldY = previousY.getOrDefault(p, exactY);

                double finalRenderAngle = previousAngle.getOrDefault(p, 0.0);

                if (Math.abs(exactX - oldX) > 0.001 || Math.abs(exactY - oldY) > 0.001) {
                    finalRenderAngle = Math.atan2(exactY - oldY, exactX - oldX);
                } 

                previousX.put(p, exactX);
                previousY.put(p, exactY);
                previousAngle.put(p, finalRenderAngle);

                if (isFirstFrame) continue;

                int vx = (int) exactX;
                int vy = (int) exactY;

                BufferedImage img = projectileAssets.get(p.getType());

                if (img != null) {
                    AffineTransform oldTransform = g.getTransform();
                    g.translate(vx, vy); 
                    g.rotate(finalRenderAngle);
                    
                    int imgW = 22; 
                    int imgH = 7;  
                    g.drawImage(img, -imgW / 2, -imgH / 2, imgW, imgH, null); 
                    
                    g.setTransform(oldTransform);
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval(vx - 5, vy - 5, 10, 10); 
                }
            }
        }
        
        private void drawSoldiers(Graphics2D g) {
            for (Soldier s : model.getActiveSoldier()) {
                int sx = (int) s.getX();
                int sy = (int) s.getY();
                
                int shadowX = s.isFacingRight() ? (sx - 20) : (sx - 12);
                
                g.setColor(new Color(0, 0, 0, 80)); 
                g.fillOval(shadowX, sy + 7, 14, 10);
                
                BufferedImage imgToDraw = null;

                if (s.isDead() && soldierDeathFrames != null && soldierDeathFrames[0] != null) {
                    int frameIndex = Math.min((s.getDeathTickCounter() / 2), soldierDeathFrames.length - 1);
                    imgToDraw = soldierDeathFrames[frameIndex];
                } else if (s.isMoving() && soldierFrames != null && soldierFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 2) % soldierFrames.length;
                    imgToDraw = soldierFrames[frameIndex];
                } else if (s.isBusy() && soldierFightFrames != null && soldierFightFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 2) % soldierFightFrames.length;
                    imgToDraw = soldierFightFrames[frameIndex];
                } else if (soldierIdleFrames != null && soldierIdleFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 5) % soldierIdleFrames.length;
                    imgToDraw = soldierIdleFrames[frameIndex];
                }

                if (imgToDraw != null) {
                    int drawX = sx - 24;
                    int drawY = sy - 24;
                    int drawW = 30;
                    int drawH = 40;

                    if (s.isFacingRight()) {
                        g.drawImage(imgToDraw, drawX, drawY, drawW, drawH, null);
                    } else {
                        g.drawImage(imgToDraw, 
                                    drawX + drawW, drawY, drawX, drawY + drawH, 
                                    0, 0, imgToDraw.getWidth(), imgToDraw.getHeight(), 
                                    null);
                    }
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval(sx - 12, sy - 12, 24, 24);
                }
                
                if (!s.isDead()) {
                    drawHealthBar(g, shadowX, sy - 25, s.getHealth(), s.getMaxHealth());
                }
            }
        }

        private void drawEnemies(Graphics2D g) {
            for (Enemy e : model.getActiveEnemies()) {
                int ex = (int) e.getX();
                int ey = (int) e.getY();
                
                boolean isBoss = (e.getType() == Enemy.YETI_TYPE);
                
                int drawW = isBoss ? 72 : 36;
                int drawH = isBoss ? 72 : 36;
                
                int drawX = ex - (drawW / 2);
                int drawY = ey - (drawH / 2);
                
                int shadowW = isBoss ? 28 : 14;
                int shadowH = isBoss ? 16 : 10;
                int shadowX = e.isFacingRight() ? (ex - (shadowW / 2) - 4) : (ex - (shadowW / 4));
                int shadowY = ey + (isBoss ? 20 : 9);
                
                g.setColor(new Color(0, 0, 0, 80)); 
                g.fillOval(shadowX, shadowY, shadowW, shadowH);
                
                BufferedImage[] frames = null;
                boolean playOnce = false;
                int frameIndex = 0;
                int animationSpeed = isBoss ? 4 : 2; 

                if (e.isDying()) {
                    frames = enemyDeathAssets.get(e.getType());
                    playOnce = true;
                    if (frames != null) {
                        frameIndex = Math.min((e.getDeathTickCounter() / animationSpeed), frames.length - 1);
                    }
                } else if (e.isBlocked()) { 
                    frames = enemyAttackAssets.get(e.getType());
                    if (frames != null) {
                        frameIndex = (e.getTikCounter() / animationSpeed) % frames.length;
                    }
                } else {
                    frames = enemyAssets.get(e.getType()); 
                    if (frames != null) {
                        frameIndex = (e.getTikCounter() / animationSpeed) % frames.length;
                    }
                }

                if (frames != null && frames.length > 0 && frames[frameIndex] != null) {
                    BufferedImage imgToDraw = frames[frameIndex];
                    
                    if (e.isFacingRight()) {
                        g.drawImage(imgToDraw, drawX, drawY, drawW, drawH, null);
                    } else {
                        g.drawImage(imgToDraw, 
                                    drawX + drawW, drawY, drawX, drawY + drawH, 
                                    0, 0, imgToDraw.getWidth(), imgToDraw.getHeight(), 
                                    null);
                    }
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval(drawX, drawY, drawW, drawH);
                }
                
                if (!e.isDying()) {
                    int healthBarY = ey - (isBoss ? 45 : 25);
                    drawHealthBar(g, shadowX, healthBarY, e.getHealth(), e.getMaxHealth());
                }
            }
        }
        
        private void drawHealthBar(Graphics2D g, int x, int y, int currentHealth, int maxHealth) {
            if (redBar == null || lifespan == null) return;
            
            int fullBarWidth = 13;
            int barHeight = 5;
            double healthPercentage = (double) currentHealth / maxHealth;
            int currentBarWidth = (int) (fullBarWidth * healthPercentage); 
            
            g.drawImage(redBar, x, y, fullBarWidth, barHeight, null);
            if (currentBarWidth > 0) {
                int sourceCropWidth = (int) (lifespan.getWidth() * healthPercentage);
                g.drawImage(lifespan, 
                    x, y, x + currentBarWidth, y + barHeight,  
                    0, 0, sourceCropWidth, lifespan.getHeight(), null);
            }
        }
        
        private void drawUI(Graphics2D g) {
            g.setFont(mainFont);
            Color menuColor = new Color(236, 204, 120);
            Color titleColor = new Color(75, 83, 32);
            
            drawShadowText(g, "VITE: " + model.getPlayerHealth(), 15, 40, menuColor, 5);
            drawShadowText(g, "ORO: " + model.getGold(), 15, 70, menuColor, 5);
            
            int ondataMostrata = Math.min(model.getCurrentWaveNumber(), model.getTotalWaves());
            drawShadowText(g, "ONDATA: " + ondataMostrata + "/" + model.getTotalWaves(), 15, 100, menuColor, 5);
            
            g.setFont(winLoseFont);
            if (model.getCurrentWaveNumber() > model.getTotalWaves() && model.getActiveEnemies().isEmpty()) {
                drawShadowText(g, "VITTORIA!", 520, 380, titleColor, 8);
            }
            if (model.isGameOver()) {
                drawShadowText(g, "GAME OVER", 520, 380, titleColor, 8);
            }
        }
    }
        
    private JButton createUpgradeButton() {
        JButton button = new JButton() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); 

                g2.setColor(Color.WHITE);
                g2.setStroke(new BasicStroke(2f)); 
                g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);

                g2.dispose();
                super.paintComponent(g); 
            }
        };
        
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        button.setForeground(Color.WHITE);
        button.setFont(mainFont != null ? mainFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16)); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        
        return button;
    }
}