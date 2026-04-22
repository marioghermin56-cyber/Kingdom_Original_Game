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

public class SwingView implements IView{
	private JFrame frame;
    private IModel model;
    private GamePanel gamePanel;
    
    private JButton archerButton, mageButton, cannonButton, barracksButton, rallyButton, upgradeButton;
    private Font font, mainFont, winLoseFont;
    private BufferedImage playIcon, menuImage;
    
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

        // 1. PRIMA CREIAMO I BOTTONI! (Così esistono in memoria)
        archerButton = createTransparentButton();
        mageButton = createTransparentButton();
        barracksButton = createTransparentButton();
        cannonButton = createTransparentButton();
        
        rallyButton = createTransparentButton();
        rallyButton.setText("MOVE");
        rallyButton.setOpaque(true);
        rallyButton.setBackground(new Color(0, 0, 255, 150));
        
        upgradeButton = createTransparentButton();
        upgradeButton.setOpaque(true);
        upgradeButton.setBackground(new Color(255, 215, 0, 200));

        // 2. SOLO ORA CREIAMO IL PANNELLO!
        // (Adesso quando loadAssets cercherà archerButton, lo troverà pronto!)
        gamePanel = new GamePanel();
        gamePanel.setLayout(null); 

        // 3. ORA POSSIAMO AGGIUNGERLI AL PANNELLO
        gamePanel.add(archerButton);
        gamePanel.add(mageButton);
        gamePanel.add(cannonButton);
        gamePanel.add(barracksButton);
        gamePanel.add(rallyButton);
        gamePanel.add(upgradeButton);

        // ... (il resto del codice, initMenuPanel, CardLayout, ecc.) ...
        
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
    };
    @Override
    public void addMageListener(ActionListener listener) {
    	mageButton.addActionListener(listener);
    };
    @Override
    public void addBarracksListener(ActionListener listener) {
    	barracksButton.addActionListener(listener);
    };
    @Override
    public void addCannonListener(ActionListener listener) {
    	cannonButton.addActionListener(listener);
    };
    @Override
    public void addRallyListener(ActionListener listener) {
        rallyButton.addActionListener(listener);
    }
    
    private void initMenuPanel() {
        menuImage = loadImage("/assets/background/bg.png");
        
        menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (menuImage != null) {
                    g.drawImage(menuImage, 0, 0, getWidth(), getHeight(), null);
                }
                if (font != null) {
                    g.setFont(font.deriveFont(60f));
                    g.setColor(new Color(236, 204, 120));
                    String title = "KINGDOM RUSH";
                    int x = (getWidth() - g.getFontMetrics().stringWidth(title)) / 2;
                    g.drawString(title, x, 200);
                }
            }
        };
        
        JButton startButton = new JButton();
        playIcon = loadImage("/assets/background/button_play.png");
        if (playIcon != null) startButton.setIcon(new ImageIcon(playIcon));
        else startButton.setText("START");
        
        startButton.setContentAreaFilled(false);
        startButton.setBorderPainted(false);
        startButton.setOpaque(false);
        startButton.setFocusPainted(false);
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
        startButton.setActionCommand("START");

        menuPanel.add(startButton);
    }
    
    @Override
    public void setStartButtonListener(ActionListener listener) {
        for (Component c : menuPanel.getComponents()) {
            if (c instanceof JButton) {
                ((JButton) c).addActionListener(listener);
            }
        }
    }
    
    @Override
    public void switchToGame() {
        cardLayout.show(mainContainer, "GAME");
        mainContainer.revalidate();
        mainContainer.repaint();
        gamePanel.requestFocusInWindow();
    }
    
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
        return button;
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
    
    public JPanel getGamePanel() { return gamePanel; }
    
    private BufferedImage loadImage(String path) {
            try {
                return ImageIO.read(getClass().getResourceAsStream(path));
            } catch (Exception e) {
                System.err.println("Impossibile caricare asset: " + path);
                return null; // Restituisce null per innescare i blocchi di fallback
            }
        }

    private class GamePanel extends JPanel {
        
    	
    	private Map<Integer, BufferedImage[]> towerAssets = new HashMap<>();
        private Map<Integer, BufferedImage> projectileAssets = new HashMap<>();
        private Map<Integer, BufferedImage[]> enemyAssets = new HashMap<>();
        private BufferedImage background, backgroundTop, lifespan, redBar;
        private BufferedImage hoveredSlot;
        private BufferedImage[] soldierFrames = new BufferedImage[20];
    	
    	
        public GamePanel() {
        	
        	this.setPreferredSize(new Dimension(1056,864 ));
            setBackground(Color.BLUE); // Un bel verde erba
            loadAssets();
        }
        
        
        
        private void loadAssets() {
            // Icone dei bottoni
            archerButton.setIcon(new ImageIcon(loadImage("/assets/background/75_2.png")));
            mageButton.setIcon(new ImageIcon(loadImage("/assets/background/100_9.png")));
            cannonButton.setIcon(new ImageIcon(loadImage("/assets/background/125_9.png")));
            barracksButton.setIcon(new ImageIcon(loadImage("/assets/background/75-1_9.png")));
            
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
            background = loadImage("/assets/background/tail_6.png");
            backgroundTop = loadImage("/assets/background/tail_6_2.png");
            projectileAssets.put(Projectile.ARCHER_PROJECTILE, loadImage("/assets/ARCHER_TOWER/37.png"));
            projectileAssets.put(Projectile.MAGE_PROJECTILE, loadImage("/assets/MAGE_TOWER/10.png"));
            projectileAssets.put(Projectile.CANNON_PROJECTILE, loadImage("/assets/CANNON_TOWER/29.png"));
            lifespan = loadImage("/assets/HEALTHBAR/health_bar-05.png");
            redBar = loadImage("/assets/HEALTHBAR/health_bar-04.png");
            hoveredSlot = loadImage("/assets/background/39.png");

            // --- ANIMAZIONI: CARICAMENTO SMART CON CICLI FOR ---
            
            // Soldati (000 a 019)
            for (int i = 0; i < 20; i++) {
                soldierFrames[i] = loadImage(String.format("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_%03d.png", i));
            }
            
            // Orchi (001 a 010)
            BufferedImage[] orcFrames = new BufferedImage[19];
            for (int i = 0; i < 19; i++) {
                orcFrames[i] = loadImage(String.format("/assets/ORC/5_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.ORC_TYPE, orcFrames);
            
            // Scorpioni (001 a 010)
            BufferedImage[] scorpionFrames = new BufferedImage[10];
            for (int i = 0; i < 10; i++) {
                scorpionFrames[i] = loadImage(String.format("/assets/SCORPION/1_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.SCORPION_TYPE, scorpionFrames);
            
            // Goblin (001 a 015)
            BufferedImage[] goblinFrames = new BufferedImage[15];
            for (int i = 0; i < 15; i++) {
                goblinFrames[i] = loadImage(String.format("/assets/GOBLIN/3_enemies_1_walk_%03d.png", i + 1));
            }
            enemyAssets.put(Enemy.GOBLIN_TYPE, goblinFrames);

            // Caricamento Font
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
            Graphics2D g2d = (Graphics2D) g;
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            // 1. Calcoliamo la scala elastica in base alla dimensione attuale dello schermo
            // 1408 e 1152 sono le coordinate "logiche" intoccabili della tua mappa
            scaleX = getWidth() / 1056.0;
            scaleY = getHeight() / 864.0;

            // Salviamo il pennello originale
            AffineTransform oldTransform = g2d.getTransform();

            // 2. STIRIAMO IL PENNELLO!
            g2d.scale(scaleX, scaleY);

            // =================================================================
            // DA QUI IN POI, DISEGNI TUTTO ESATTAMENTE COME HAI SEMPRE FATTO!
            // Usa le coordinate normali, Java le rimpicciolirà/ingrandirà da solo
            // =================================================================

            if (background != null) {
                g2d.drawImage(background, 0, 0, 1056, 864, null); 
            }
            
            
            drawEnemies(g2d);
            drawSoldiers(g2d);
            
            
            
            if (backgroundTop != null) {
                g2d.drawImage(backgroundTop, 0, 0, 1056, 864, null); 
            }
            // Ripristiniamo il pennello PRIMA di disegnare la UI o aggiornare i bottoni
            
            drawSlots(g2d);
            drawProjectiles(g2d);
            g2d.setTransform(oldTransform);
            
            
            
            // Disegniamo la UI (testi, vite) SENZA scala, così i font restano nitidi
            drawUI(g2d);
            
            // Aggiorniamo la posizione dei bottoni
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
                
                // Calcoliamo il VERO centro della piazzola per centrare torri e cerchi
                int cx = slot.getX() + (slot.getWidth() / 2);
                int cy = slot.getY() + (slot.getHeight() / 2);

                if (slot.isOccupied()) {
                    Tower tower = slot.getTower();
                    BufferedImage[] levelsArray = towerAssets.get(tower.getType());
                    int imageIndex = tower.getLvl() - 1;
                    
                    if (levelsArray != null && imageIndex < levelsArray.length && levelsArray[imageIndex] != null) {
                        // Usiamo cx e cy al posto di getX e getY per mantenere le torri centrate!
                        g.drawImage(levelsArray[imageIndex], cx - 27, cy - 55, 60, 70, null);
                    } else {
                        // Fallback Torri
                        if (tower.getType() == Tower.ARCHER_TYPE) g.setColor(new Color(34, 139, 34)); 
                        else g.setColor(new Color(128, 0, 128));
                        g.fill3DRect(cx - 25, cy - 25, 50, 50, true);
                        g.setColor(Color.WHITE);
                        g.drawString("Lvl " + tower.getLvl(), cx - 10, cy);
                    } 
                } else if (slot == model.getHoveredSlot()) {
                    
                    // LA TUA NUOVA IMMAGINE HOVER!
                    if (hoveredSlot != null) {
                        // Disegna il png perfettamente sopra il rettangolo di Tiled
                        g.drawImage(hoveredSlot, slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight(), null);
                    } else {
                        // Fallback di emergenza
                        g.setColor(new Color(255, 215, 0, 70)); 
                        g.fillRect(slot.getX(), slot.getY(), slot.getWidth(), slot.getHeight());
                    }
                }
            }
            
            // Logica dei Cerchi del Range
            TowerSlot slotToHighlight = null;
            TowerSlot selectedSlot = model.getSelectedBuildSlot();
            
            if (selectedSlot != null && selectedSlot.isOccupied()) {
                // Ricalcoliamo il centro per far partire il cerchio dal cuore della torre
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
            
            // Nascondiamo tutto di default
            archerButton.setVisible(false);
            mageButton.setVisible(false);
            cannonButton.setVisible(false);
            barracksButton.setVisible(false);
            rallyButton.setVisible(false);
            upgradeButton.setVisible(false);
            
            if (slot == null) return;
            
            int x = (int) (slot.getX() * scaleX); // Moltiplica per la scala!
            int y = (int) (slot.getY() * scaleY); // Moltiplica per la scala!

            if (!slot.isOccupied()) {
                // Scaliamo anche la grandezza dei bottoni
                int w = (int) (200 * scaleX);
                int h = (int) (200 * scaleY);
                
                archerButton.setBounds(x - w - (int)(15*scaleX), y - h - (int)(15*scaleY), w, h);
                mageButton.setBounds(x + (int)(15*scaleX), y - h - (int)(15*scaleY), w, h);
                cannonButton.setBounds(x - w - (int)(15*scaleX), y + (int)(15*scaleY), w, h);
                barracksButton.setBounds(x + (int)(15*scaleX), y + (int)(15*scaleY), w, h);
                
                archerButton.setVisible(true);
                mageButton.setVisible(true);
                cannonButton.setVisible(true);
                barracksButton.setVisible(true);
            } else {
                Tower tower = slot.getTower();
                
                if (tower.getType() == Tower.BARRACKS_TYPE) {
                    rallyButton.setBounds(x - 15, y + 40, 30, 30);
                    rallyButton.setVisible(true);
                }
                
                if (tower.canUpgrade()) {
                    upgradeButton.setBounds(x - 45, y - 60, 90, 30);
                    upgradeButton.setText("UP: " + tower.getUpgradeCost() + "g");
                    upgradeButton.setBackground(model.getGold() < tower.getUpgradeCost() ? 
                        new Color(255, 0, 0, 150) : new Color(255, 215, 0, 200));
                    upgradeButton.setVisible(true);
                }
            }
        }
        
        private void drawProjectiles(Graphics2D g) {
        	

        	
        	
        	

        	for (Projectile p : model.getActiveProjectiles()) {
        		
        		double visualTilt = Math.toRadians(70); 
        		double maxArcHeight;
        		
        		if(p.getType() == Projectile.ARCHER_PROJECTILE) {
        			maxArcHeight = 60.0;
        		} else if(p.getType() == Projectile.MAGE_PROJECTILE) {
        			maxArcHeight = 20.0;
        		} else if(p.getType() == Projectile.CANNON_PROJECTILE) {
        			maxArcHeight = 120.0;
        		} else {
        			maxArcHeight = 40.0;
        		}
        		
        	    double lx = p.getX() ;
        	    double ly = p.getY() ;


        	    double totalDist = p.getTotalDistanceToTravel(); 
        	    double distTraveled = p.getDistanceTraveled();

        	    double progress = Math.min(1.0, distTraveled / totalDist);
        	    if (Double.isNaN(progress)) progress = 1.0; 


        	    double parabolaFactor = 4.0 * progress * (1.0 - progress);

        	    double currentHeightOffset = maxArcHeight * parabolaFactor;

        	    int vx = (int)lx;
        	    int vy = (int)(ly - currentHeightOffset); 

        	    double baseAngle = p.getAngle();
        	    double tiltDirection = (Math.cos(baseAngle) >= 0) ? 1.0 : -1.0;
    
        	    double arcTilt = (1.0 - 2.0 * progress) * visualTilt*tiltDirection;
        	    
        	    double finalRenderAngle = baseAngle - arcTilt; 


        	    BufferedImage img = projectileAssets.get(p.getType());

        	    if (img != null) {
        	        AffineTransform oldTransform = g.getTransform();
        	        g.translate(vx, vy); 
        	        g.rotate(finalRenderAngle);
        	        g.drawImage(img, -14, -4, 14, 8, null); 
        	        g.setTransform(oldTransform);
        	    } else {
        	        // Fallback drawing segue comunque la parabola
        	        g.setColor(p.getType() == Tower.ARCHER_TYPE ? new Color(34, 139, 34) : new Color(128, 0, 128)); 
        	        g.fillOval(vx - 5, vy - 5, 10, 10); // Più piccolo e centrato
        	    }
        	    
        	}
        }
        
        private void drawSoldiers(Graphics2D g) {
            for (Soldier s : model.getActiveSoldier()) {
                int sx = (int) s.getX();
                int sy = (int) s.getY();
                
                g.setColor(new Color(0, 0, 0, 80)); // Ombra
                g.fillOval(sx - 20, sy + 7, 14, 10);
                
                if (s.isMoving() && soldierFrames[0] != null) {
                    int frameIndex = (s.getTikCounter() / 2) % soldierFrames.length;
                    g.drawImage(soldierFrames[frameIndex], sx - 24, sy - 24, 30, 40, null);
                } else if (soldierFrames[0] != null) {
                    g.drawImage(soldierFrames[0], sx - 24, sy - 24, 30, 40, null);
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval(sx - 12, sy - 12, 24, 24);
                }
                
                drawHealthBar(g, sx - 20, sy - 25, s.getHealth(), s.getMaxHealth());
            }
        }
        		
        

        private void drawEnemies(Graphics2D g) {
            for (Enemy e : model.getActiveEnemies()) {
                int ex = (int) e.getX();
                int ey = (int) e.getY();
                
                g.setColor(new Color(0, 0, 0, 80)); // Ombra
                g.fillOval(ex - 11, ey + 9, 14, 10);
                
                BufferedImage[] frames = enemyAssets.get(e.getType());
                if (frames != null && frames.length > 0) {
                    int frameIndex = e.getTikCounter() % frames.length;
                    g.drawImage(frames[frameIndex], ex - 18, ey - 18, 36, 36, null);
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval(ex - 12, ey - 12, 24, 24);
                }
                
                drawHealthBar(g, ex - 15, ey - 25, e.getHealth(), e.getMaxHealth());
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
            
            drawShadowText(g, "VITE: " + model.getPlayerHealth(), 1160, 40, menuColor, 5);
            drawShadowText(g, "ORO: " + model.getGold(), 1160, 70, menuColor, 5);
            
            int ondataMostrata = Math.min(model.getCurrentWaveNumber(), model.getTotalWaves());
            drawShadowText(g, "ONDATA: " + ondataMostrata + "/" + model.getTotalWaves(), 1160, 100, menuColor, 5);
            
            g.setFont(winLoseFont);
            if (model.getCurrentWaveNumber() > model.getTotalWaves() && model.getActiveEnemies().isEmpty()) {
                drawShadowText(g, "VITTORIA!", 520, 380, titleColor, 8);
            }
            if (model.isGameOver()) {
                drawShadowText(g, "GAME OVER", 520, 380, titleColor, 8);
            }
        }
    }
}
