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
    private Font font, mainFont, winLoseFont;
    
    private JButton btnLevel1, btnLevel2, btnLevel3;
    
    private BufferedImage playIcon, menuImage;
    private BufferedImage musicOnIcon, musicOffIcon, soundOnIcon, soundOffIcon;
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    private JPanel menuPanel;
    private double scaleX = 1.0;
    private double scaleY = 1.0;
    
    public SwingView() {
        frame = new JFrame("Pseudo Kingdom Rush - MVC Alpha");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true); 
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 

        // 1. PRIMA CREIAMO I BOTTONI!
        archerButton = createTransparentButton();
        mageButton = createTransparentButton();
        barracksButton = createTransparentButton();
        cannonButton = createTransparentButton();
        
        rallyButton = createTransparentButton();
        rallyButton.setText("MOVE");
        rallyButton.setOpaque(true);
        rallyButton.setBackground(new Color(0, 0, 255, 150));
        
        upgradeButton = createUpgradeButton();

        // 2. SOLO ORA CREIAMO IL PANNELLO!
        gamePanel = new GamePanel();
        gamePanel.setLayout(null); 

        // 3. ORA POSSIAMO AGGIUNGERLI AL PANNELLO
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
    public void addUpgradeListener(ActionListener listener) {
        upgradeButton.addActionListener(listener);
    }    
    
    @Override
    public void addArcherListener(ActionListener listener) {
        archerButton.addActionListener(listener);
    }
    @Override
    public void addMageListener(ActionListener listener) {
        mageButton.addActionListener(listener);
    }
    @Override
    public void addBarracksListener(ActionListener listener) {
        barracksButton.addActionListener(listener);
    }
    @Override
    public void addCannonListener(ActionListener listener) {
        cannonButton.addActionListener(listener);
    }
    @Override
    public void addRallyListener(ActionListener listener) {
        rallyButton.addActionListener(listener);
    }
    @Override
    public void addMusicListener(ActionListener listener) {
        musicButton.addActionListener(listener);
    }
    @Override
    public void addSoundListener(ActionListener listener) {
        soundButton.addActionListener(listener);
    }
    public JPanel getGamePanel() {
        return this.gamePanel;
    }
    
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
        
        // Caricamento icone audio
        musicOnIcon = loadImage("/assets/background/button_music.png");
        musicOffIcon = loadImage("/assets/background/button_music_off.png");
        
        soundOnIcon = loadImage("/assets/background/button_sound.png");
        soundOffIcon = loadImage("/assets/background/button_sound_off.png");
        
        menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuImage != null) {
                    g.drawImage(menuImage, 0, 0, getWidth(), getHeight(), null);
                }
                if (font != null) {
                    g.setFont(font.deriveFont(60f));
                    g.setColor(new Color(0, 0, 0));
                    String title = "KINGDOM RUSH";
                    int x = (getWidth() - g.getFontMetrics().stringWidth(title)) / 2;
                    g.drawString(title, x, 200);
                }
            }
        };
        
        
        GridBagConstraints gbc = new GridBagConstraints();

        // --- PANNELLO AUDIO (In alto a destra) ---
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
       
        
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 0.1; 
        gbc.anchor = GridBagConstraints.NORTHEAST;
        gbc.insets = new Insets(20, 0, 0, 20); 
        menuPanel.add(audioPanel, gbc);
        
        gioco.utils.SoundManager.playMusic("/assets/audio/audioMenu.wav");

        // --- BOTTONE START (Al centro) ---
        JPanel levelsPanel = new JPanel(new GridLayout(2, 1, 0, 20)); // 2 righe, 1 colonna, spazio di 20px
        levelsPanel.setOpaque(false); // Rendiamo trasparente il contenitore

        btnLevel1 = createLevelButton("COLFIORITO", "1");
        btnLevel2 = createLevelButton("TRASIMENO", "2");
        btnLevel3 = createLevelButton("NORCIA", "3");

        levelsPanel.add(btnLevel1);
        levelsPanel.add(btnLevel2);
        levelsPanel.add(btnLevel3);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        gbc.weighty = 0.9;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.insets = new Insets(0, 0, 0, 0);
        menuPanel.add(levelsPanel, gbc);
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

    // ECCO IL METODO CHE ERA SPARITO!
    private JButton createTransparentButton() {
        JButton button = new JButton();
        button.setBorderPainted(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setVisible(false);
        button.setHorizontalTextPosition(SwingConstants.CENTER);
        button.setVerticalTextPosition(SwingConstants.CENTER);
        button.setForeground(Color.WHITE);
        button.setFont(new Font("Arial", Font.BOLD, 14));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Aggiunto cursore a manina per comodità!
        button.setRolloverEnabled(false);
        return button;
    }
    
    private JButton createLevelButton(String text, String command) {
        // Creiamo un bottone personalizzato che disegna lo sfondo da solo
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                g.setColor(new Color(0, 0, 0, 180)); // Il nostro nero trasparente
                g.fillRect(0, 0, getWidth(), getHeight());
                super.paintComponent(g); // Disegna il testo normalmente
            }
        };
        
        button.setFont(mainFont != null ? mainFont.deriveFont(24f) : new Font("Arial", Font.BOLD, 24));
        button.setForeground(Color.WHITE);
        
        // I 3 comandi fondamentali per evitare glitch visivi!
        button.setOpaque(false); 
        button.setContentAreaFilled(false); 
        button.setBorderPainted(false);
        button.setFocusPainted(false);
        
        button.setActionCommand(command); 
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        button.setPreferredSize(new Dimension(300, 60));
        return button;
    }

    private class GamePanel extends JPanel {
        
        private Map<Integer, BufferedImage[]> towerAssets = new HashMap<>();
        private Map<Integer, BufferedImage> projectileAssets = new HashMap<>();
        private Map<Integer, BufferedImage[]> enemyAssets = new HashMap<>();
        private BufferedImage lifespan, redBar;
        private BufferedImage hoveredSlot;
        private BufferedImage[] soldierFrames = new BufferedImage[20];
        private BufferedImage[] soldierFightFrames = new BufferedImage[20];
        private BufferedImage[] soldierIdleFrames = new BufferedImage[20];
        private BufferedImage radialMenuImage;
        
        public GamePanel() {
            this.setPreferredSize(new Dimension(1056,864 ));
            setBackground(Color.BLUE);
            loadAssets();
        }
        
        private void loadAssets() {
        	radialMenuImage = loadImage("/assets/background/slotMenu6.png");
            
            // Torri
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
            
            // Proiettili, Sfondo e Barre
           
            projectileAssets.put(Projectile.ARCHER_PROJECTILE, loadImage("/assets/ARCHER_TOWER/37.png"));
            projectileAssets.put(Projectile.MAGE_PROJECTILE, loadImage("/assets/MAGE_TOWER/10.png"));
            projectileAssets.put(Projectile.CANNON_PROJECTILE, loadImage("/assets/CANNON_TOWER/29.png"));
            lifespan = loadImage("/assets/HEALTHBAR/health_bar-05.png");
            redBar = loadImage("/assets/HEALTHBAR/health_bar-04.png");
            hoveredSlot = loadImage("/assets/background/39.png");

            // --- ANIMAZIONI ---
            for (int i = 0; i < 20; i++) {
                soldierFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_%03d.png", i));
            }
            
            for (int i = 0; i < 20; i++) {
                soldierIdleFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_idle_%03d.png", i));
            }
            
            for(int i = 0;i < 20; i++) {
            	soldierFightFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_attack_%03d.png", i));
            }
            
            BufferedImage[] orcFrames = new BufferedImage[19];
            for (int i = 0; i < 19; i++) {
                orcFrames[i] = loadImage(String.format("/assets/ORC/5_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.ORC_TYPE, orcFrames);
            
            BufferedImage[] scorpionFrames = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                scorpionFrames[i] = loadImage(String.format("/assets/SCORPION/1_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.SCORPION_TYPE, scorpionFrames);
            
            BufferedImage[] goblinFrames = new BufferedImage[15];
            for (int i = 0; i < 15; i++) {
                goblinFrames[i] = loadImage(String.format("/assets/GOBLIN/3_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.GOBLIN_TYPE, goblinFrames);

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
                // 1. Centro logico
                int logicalCx = selectedSlot.getX() + (selectedSlot.getWidth() / 2);
                int logicalCy = selectedSlot.getY() + (selectedSlot.getHeight() / 2);
                
                // 2. Centro su schermo
                int screenCx = (int) (logicalCx * scaleX);
                int screenCy = (int) (logicalCy * scaleY);
                
                // IL SEGRETO: Usiamo la scala minore tra X e Y per tenerlo sempre 1:1 !
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

        private void drawTextMenu(Graphics2D g, String text, int x, int y, Color color) {
            g.setColor(Color.BLACK);
            g.drawString(text, x + 5, y + 5); 
            g.setColor(color);
            g.drawString(text, x, y);
        }

        private void drawBigMessage(Graphics2D g, String text, int x, int y, Color color) {
            g.setColor(Color.BLACK);
            g.drawString(text, x + 8, y + 8);
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
            
            // 1. SE NON C'È NESSUNO SLOT APERTO, NASCONDI TUTTO E FERMATI QUI
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
                // 2. SE IL MENU È VUOTO: Mostra le armi, nascondi gli upgrade
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
                // 3. SE C'È GIÀ UNA TORRE: Nascondi le armi, gestisci gli upgrade
                archerButton.setVisible(false);
                mageButton.setVisible(false);
                cannonButton.setVisible(false);
                barracksButton.setVisible(false);
                
                Tower tower = slot.getTower();
                
                if (tower.getType() == Tower.BARRACKS_TYPE) {
                    // Usiamo screenCx per centrarlo perfettamente e screenCy per abbassarlo
                    rallyButton.setBounds(screenCx - 15, screenCy + 40, 30, 30);
                    rallyButton.setVisible(true);
                } else {
                    rallyButton.setVisible(false);
                }
                
                if (tower.canUpgrade()) {
                	int btnWidth = 170;
                    int btnHeight = 35;
                    
                	upgradeButton.setBounds(screenCx - (btnWidth / 2), screenCy + 15, btnWidth, btnHeight);
                    upgradeButton.setText("UPGRADE: " + tower.getUpgradeCost() + "g");
                    
                    // Colori più belli: Rosso scuro se non hai soldi, Verde prato se li hai
                    Color affordableColor = new Color(104, 163, 87, 220); // verde foresta
                    Color unaffordableColor = new Color(160, 70, 63, 220); // Rosso Mattone
                    
                    upgradeButton.setBackground(model.getGold() < tower.getUpgradeCost() ? unaffordableColor : affordableColor);
                    upgradeButton.setVisible(true);
                }else {
                    upgradeButton.setVisible(false);
                }
            }
        }
        
        private void drawProjectiles(Graphics2D g) {
            for (Projectile p : model.getActiveProjectiles()) {
                double visualTilt = Math.toRadians(70); 
                double maxArcHeight;
                
                switch (p.getType()) {
                    case Projectile.ARCHER_PROJECTILE -> maxArcHeight = 60.0;
                    case Projectile.MAGE_PROJECTILE -> maxArcHeight = 10.0;
                    case Projectile.CANNON_PROJECTILE -> maxArcHeight = 120.0;
                    default -> maxArcHeight = 40.0;
                }
                
                // Ora getTotalDistanceToTravel() è dinamico![cite: 8]
                double totalDist = p.getTotalDistanceToTravel(); 
                double distTraveled = p.getDistanceTraveled();
                
                // 3. APPIATTIMENTO DELLA PARABOLA
                // Se la distanza è minore di 150 pixel, riduciamo l'altezza massima dell'arco.
                // Questo evita che la freccia faccia "voli pindarici" altissimi per percorrere 2 metri.
                double arcScale = Math.min(1.0, totalDist / 150.0);
                double adjustedArcHeight = maxArcHeight * arcScale;

                double progress = distTraveled / totalDist;
                if (Double.isNaN(progress) || progress > 1.0) progress = 1.0; 

                // Usiamo adjustedArcHeight invece di maxArcHeight
                double parabolaFactor = 4.0 * progress * (1.0 - progress);
                double currentHeightOffset = adjustedArcHeight * parabolaFactor;

                int vx = (int)p.getX();
                int vy = (int)(p.getY() - currentHeightOffset);

                boolean facingRight = p.isFacingRight();
                double tiltDirection = facingRight ? 1.0 : -1.0;

                double baseAngle = p.getAngle();
                double arcTilt = (1.0 - 2.0 * progress) * visualTilt * tiltDirection;
                double finalRenderAngle = baseAngle - arcTilt; 

                BufferedImage img = projectileAssets.get(p.getType());

                if (img != null) {
                    AffineTransform oldTransform = g.getTransform();
                    g.translate(vx, vy); 
                    g.rotate(finalRenderAngle);
                    
                    if (!facingRight) {
                        // Specchio per direzione sinistra (pancia in basso)
                        g.drawImage(img, -14, 4, 14, -8, null); 
                    } else {
                        // Normale per direzione destra
                        g.drawImage(img, -14, -4, 14, 8, null); 
                    }
                    
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

                if (s.isMoving() && soldierFrames != null && soldierFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 2) % soldierFrames.length;
                    imgToDraw = soldierFrames[frameIndex];
                    
                } else if (s.isBusy() && soldierFightFrames != null && soldierFightFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 2) % soldierFightFrames.length;
                    imgToDraw = soldierFightFrames[frameIndex];
                    
                } else if (soldierIdleFrames != null && soldierIdleFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 5) % soldierIdleFrames.length;
                    imgToDraw = soldierIdleFrames[frameIndex];
                }

                // 2. Disegniamola!
                if (imgToDraw != null) {
                    int drawX = sx - 24;
                    int drawY = sy - 24;
                    int drawW = 30;
                    int drawH = 40;

                    if (s.isFacingRight()) {
                        // Disegno NORMALE (verso destra)
                        g.drawImage(imgToDraw, drawX, drawY, drawW, drawH, null);
                    } else {
                        // Disegno SPECCHIATO (verso sinistra)
                        // Usiamo una funzione speciale di Java che inverte le coordinate di destinazione sulla X
                        g.drawImage(imgToDraw, 
                                    drawX + drawW, drawY, drawX, drawY + drawH, // Coordinate Schermo (Destra -> Sinistra)
                                    0, 0, imgToDraw.getWidth(), imgToDraw.getHeight(), // Coordinate Immagine 
                                    null);
                    }
                } else {
                    // Fallback d'emergenza se mancano tutte le immagini
                    g.setColor(Color.RED); 
                    g.fillOval(sx - 12, sy - 12, 24, 24);
                }
                drawHealthBar(g, shadowX, sy - 25, s.getHealth(), s.getMaxHealth());
            }
        }

        private void drawEnemies(Graphics2D g) {
            for (Enemy e : model.getActiveEnemies()) {
                int ex = (int) e.getX();
                int ey = (int) e.getY();
                
                int shadowX = e.isFacingRight() ? (ex - 11) : (ex - 3);
                // Disegno dell'ombra
                g.setColor(new Color(0, 0, 0, 80)); 
                g.fillOval(shadowX, ey + 9, 14, 10);
                
                BufferedImage[] frames = enemyAssets.get(e.getType());
                if (frames != null && frames.length > 0) {
                    // Nota: ho aggiunto un /2 o /3 fittizio, se l'animazione dei nemici ti sembra 
                    // troppo veloce rispetto a quella dei soldati, aggiungilo come hai fatto per loro!
                    int frameIndex = (e.getTikCounter() / 2) % frames.length; 
                    BufferedImage imgToDraw = frames[frameIndex];
                    
                    int drawX = ex - 18;
                    int drawY = ey - 18;
                    int drawW = 36;
                    int drawH = 36;
                    
                    if (e.isFacingRight()) {
                        // Disegno NORMALE (verso destra)
                        g.drawImage(imgToDraw, drawX, drawY, drawW, drawH, null);
                    } else {
                        // Disegno SPECCHIATO (verso sinistra)
                        g.drawImage(imgToDraw, 
                                    drawX + drawW, drawY, drawX, drawY + drawH, 
                                    0, 0, imgToDraw.getWidth(), imgToDraw.getHeight(), 
                                    null);
                    }
                } else {
                    // Fallback
                    g.setColor(Color.RED); 
                    g.fillOval(ex - 12, ey - 12, 24, 24);
                }
                
                // Barra della vita sempre dritta sopra al nemico
                drawHealthBar(g, shadowX, ey - 25, e.getHealth(), e.getMaxHealth());
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
                    // Attiviamo l'antialiasing per non avere bordi seghettati
                    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                    // Disegniamo lo sfondo arrotondato usando il colore che gli passiamo dinamicamente
                    g2.setColor(getBackground());
                    g2.fillRoundRect(0, 0, getWidth(), getHeight(), 15, 15); // 15 è la rotondità degli angoli

                    // Disegniamo un bordino bianco per farlo risaltare
                    g2.setColor(Color.WHITE);
                    g2.setStroke(new BasicStroke(2f)); // Spessore del bordo
                    g2.drawRoundRect(1, 1, getWidth() - 3, getHeight() - 3, 15, 15);

                    g2.dispose();
                    super.paintComponent(g); // Questo disegna il testo ("UP: 100g") sopra a tutto
                }
            };
            
            // Rimuoviamo gli stili brutti di default
            button.setOpaque(false);
            button.setContentAreaFilled(false);
            button.setBorderPainted(false);
            button.setFocusPainted(false);
            
            // Impostiamo il testo in bianco con un bel font cicciotto
            button.setForeground(Color.WHITE);
            // Se hai mainFont caricato, usa quello, altrimenti Arial
            button.setFont(mainFont != null ? mainFont.deriveFont(16f) : new Font("Arial", Font.BOLD, 16)); 
            button.setCursor(new Cursor(Cursor.HAND_CURSOR));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            
            return button;
        }
    }