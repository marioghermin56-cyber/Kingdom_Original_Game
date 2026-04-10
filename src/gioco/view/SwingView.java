package gioco.view;

import gioco.model.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import javax.swing.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage; // Importante per le immagini vere
import javax.imageio.ImageIO;      // Per caricare immagini .png
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class SwingView implements IView{
    private JFrame frame;
    private GamePanel gamePanel;
    private IModel model;
    private JButton archerButton;
    private JButton mageButton;
    private JButton cannonButton;
    private JButton barracksButton;
    private Font font;
    private Font mainFont;
    private Font winLoseFont;
    private Font menuFont;
    private JButton rallyButton;
    private JButton upgradeButton;
    private BufferedImage playIcon;
    private BufferedImage menuImage;
    
    private CardLayout cardLayout = new CardLayout();
    private JPanel mainContainer = new JPanel(cardLayout);
    private JPanel menuPanel;
    
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

    public SwingView() {
        // Creiamo la finestra principale
        frame = new JFrame("Pseudo Kingdom Rush - MVC Alpha");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //frame.setPreferredSize(new Dimension(800,600));
        frame.setLocationRelativeTo(null);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setResizable(false);
        
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

        // Creiamo il pannello di disegno interno
        gamePanel = new GamePanel();
        
        initMenuPanel();
        mainContainer.add(menuPanel, "MENU");
        mainContainer.add(gamePanel, "GAME");

        frame.add(mainContainer);
        cardLayout.show(mainContainer, "MENU"); // Mostra il menu all'inizio
        gamePanel.setLayout(null);

        frame.setVisible(true);
        
        gamePanel.add(archerButton);
        gamePanel.add(barracksButton);
        gamePanel.add(cannonButton);
        gamePanel.add(mageButton);
        gamePanel.add(rallyButton);
        gamePanel.add(upgradeButton);
        
        frame.setVisible(true);
    }
    
    private void initMenuPanel() {
    	
    	try {
    		menuImage = ImageIO.read(getClass().getResourceAsStream("/assets/background/bg.png"));
    	}catch(IOException e) {
    		e.printStackTrace();
    	}
        menuPanel = new JPanel(new GridBagLayout()) {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Sfondo marrone scuro
                g.drawImage(menuImage,0,0,getWidth() ,getHeight(),null);;
                
                if (font != null) {
                    Font menuFont = font.deriveFont(60f);
                    g.setColor(new Color(236, 204, 120)); // Colore oro
                    g.setFont(menuFont);
                    String title = "KINGDOM RUSH";
                    FontMetrics fm = g.getFontMetrics();
                    int x = (getWidth() - fm.stringWidth(title)) / 2;
                    g.drawString(title, x, 200);
                }
            }
        };
        
        JButton startButton = new JButton();
        
        try {
            playIcon = ImageIO.read(getClass().getResourceAsStream("/assets/background/button_play.png"));
            if (playIcon != null) {
                startButton.setIcon(new ImageIcon(playIcon));
            } else {
                startButton.setText("START"); // Fallback di emergenza se non trova l'immagine
            }
        } catch(Exception e) {
            e.printStackTrace();
            startButton.setText("START");
        }
        
        // --- LA MAGIA PER LA TRASPARENZA ---
        startButton.setContentAreaFilled(false); // Rimuove il "riempimento" grigio di default
        startButton.setBorderPainted(false);     // Rimuove il bordo 3D attorno al bottone
        startButton.setOpaque(false);            // Si assicura che il pannello sotto sia visibile
        
        // Estetica extra (le righe che avevi commentato)
        startButton.setFocusPainted(false);      // Rimuove il quadratino tratteggiato quando lo clicchi
        startButton.setCursor(new Cursor(Cursor.HAND_CURSOR)); // Fa apparire la "manina" del mouse
        
        // Il comando che il Controller dovrà ascoltare
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
        gamePanel.requestFocusInWindow();

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
        // Diciamo al pannello di ridisegnarsi (chiamerà paintComponent)
        gamePanel.repaint(); 
    }

    @Override
    public void showMessage(String message) {
        // Mostriamo i messaggi importanti (es. Oro insufficiente) come popup
        JOptionPane.showMessageDialog(frame, message);
    }
    
    public JPanel getGamePanel() { return gamePanel; }

    // --- Pannello interno per il disegno custom ---
    private class GamePanel extends JPanel {
        
    	private Map<Integer, BufferedImage[]> towerAssets;
    	private BufferedImage background;
    	private BufferedImage lifespan;
    	private BufferedImage redBar;
    	private BufferedImage archerIcon;
    	private BufferedImage mageIcon;
    	private BufferedImage barrackIcon;
    	private BufferedImage cannonIcon;
    	private Map<Integer, BufferedImage> projectileAssets;
    	private Map<Integer,BufferedImage[]> enemyAssets;
    	private BufferedImage[] soldierFrames = new BufferedImage[20];
    	
    	
        public GamePanel() {
        	
        	
            setBackground(Color.BLUE); // Un bel verde erba
            loadAssets();
        }
        
        private void loadAssets() {
        	towerAssets = new HashMap();
        	projectileAssets = new  HashMap();
        	enemyAssets = new HashMap();
        	try {
        		archerIcon = ImageIO.read(getClass().getResourceAsStream("/assets/background/75_2.png"));
        		mageIcon = ImageIO.read(getClass().getResourceAsStream("/assets/background/100_9.png"));
        		cannonIcon = ImageIO.read(getClass().getResourceAsStream("/assets/background/125_9.png"));
        		barrackIcon = ImageIO.read(getClass().getResourceAsStream("/assets/background/75-1_9.png"));
        	}catch(IOException e){
        		e.printStackTrace();
        	}
        	ImageIcon aIcon = new ImageIcon(archerIcon);
        	ImageIcon mIcon = new ImageIcon(mageIcon);
        	ImageIcon bIcon = new ImageIcon(barrackIcon);
        	ImageIcon cIcon = new ImageIcon(cannonIcon);
        	
        	archerButton.setIcon(aIcon);
        	mageButton.setIcon(mIcon);
        	cannonButton.setIcon(cIcon);
        	barracksButton.setIcon(bIcon);
        	try {
        		
        		BufferedImage[] archerTower = new BufferedImage[3];
        		BufferedImage[] mageTower = new BufferedImage[3];
        		BufferedImage[] cannonTower = new BufferedImage[3];
        		BufferedImage[] barrackTower = new BufferedImage[3];
        		archerTower[0] = ImageIO.read(getClass().getResourceAsStream("/assets/ARCHER_TOWER/7.png"));
        		archerTower[1] = ImageIO.read(getClass().getResourceAsStream("/assets/ARCHER_TOWER/8.png"));
        		archerTower[2] = ImageIO.read(getClass().getResourceAsStream("/assets/ARCHER_TOWER/9.png"));
        		mageTower[0] = ImageIO.read(getClass().getResourceAsStream("/assets/MAGE_TOWER/11.png"));
        		mageTower[1] = ImageIO.read(getClass().getResourceAsStream("/assets/MAGE_TOWER/12.png"));
        		mageTower[2] = ImageIO.read(getClass().getResourceAsStream("/assets/MAGE_TOWER/13.png"));
        		cannonTower[0] = ImageIO.read(getClass().getResourceAsStream("/assets/CANNON_TOWER/15.png"));
        		cannonTower[1] = ImageIO.read(getClass().getResourceAsStream("/assets/CANNON_TOWER/16.png"));
        		cannonTower[2] = ImageIO.read(getClass().getResourceAsStream("/assets/CANNON_TOWER/17.png"));
        		barrackTower[0] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/7.png"));
        		barrackTower[1] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/8.png"));
        		barrackTower[2] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/9.png"));
        		background = ImageIO.read(getClass().getResourceAsStream("/assets/background/game_background_1.jpg"));
        		towerAssets.put(Tower.MAGE_TYPE, mageTower);
        		towerAssets.put(Tower.ARCHER_TYPE, archerTower);
        		towerAssets.put(Tower.CANNON_TYPE,cannonTower);
                towerAssets.put(Tower.BARRACKS_TYPE, barrackTower);
        		projectileAssets.put(Projectile.ARCHER_PROJECTILE, ImageIO.read(getClass().getResourceAsStream("/assets/ARCHER_TOWER/37.png")));
        		projectileAssets.put(Projectile.MAGE_PROJECTILE, ImageIO.read(getClass().getResourceAsStream("/assets/MAGE_TOWER/10.png")));
        		projectileAssets.put(Projectile.CANNON_PROJECTILE, ImageIO.read(getClass().getResourceAsStream("/assets/CANNON_TOWER/29.png")));
        	
        		lifespan = ImageIO.read(getClass().getResourceAsStream("/assets/HEALTHBAR/health_bar-05.png"));
        		redBar = ImageIO.read(getClass().getResourceAsStream("/assets/HEALTHBAR/health_bar-04.png"));
        		soldierFrames[0] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_000.png"));
        		soldierFrames[1] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_001.png"));
        		soldierFrames[2] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_002.png"));
        		soldierFrames[3] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_003.png"));
        		soldierFrames[4] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_004.png"));
        		soldierFrames[5] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_005.png"));
        		soldierFrames[6] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_006.png"));
        		soldierFrames[7] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_007.png"));
        		soldierFrames[8] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_008.png"));
        		soldierFrames[9] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_009.png"));
        		soldierFrames[10] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_010.png"));
        		soldierFrames[11] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_011.png"));
        		soldierFrames[12] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_012.png"));
        		soldierFrames[13] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_013.png"));
        		soldierFrames[14] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_014.png"));
        		soldierFrames[15] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_015.png"));
        		soldierFrames[16] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_016.png"));
        		soldierFrames[17] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_017.png"));
        		soldierFrames[18] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_018.png"));
        		soldierFrames[19] = ImageIO.read(getClass().getResourceAsStream("/assets/BARRACK_TOWER/SOLDIERS/8_enemies_1_walk_019.png"));
        		BufferedImage[] goblinFrames = new BufferedImage[15];
        		BufferedImage[] orcFrames = new BufferedImage[10];
        		BufferedImage[] scorpionFrames = new BufferedImage[10];
        		orcFrames[0] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_001.png"));
        		orcFrames[2] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_003.png"));
        		orcFrames[3] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_004.png"));
        		orcFrames[4] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_005.png"));
        		orcFrames[5] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_006.png"));
        		orcFrames[6] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_007.png"));
        		orcFrames[7] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_008.png"));
        		orcFrames[8] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_009.png"));
        		orcFrames[9] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_010.png"));
        		orcFrames[1] = ImageIO.read(getClass().getResourceAsStream("/assets/ORC/5_enemies_1_walk_002.png"));
        		scorpionFrames[0] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_001.png"));
        		scorpionFrames[1] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_002.png"));
        		scorpionFrames[2] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_003.png"));
        		scorpionFrames[3] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_004.png"));
        		scorpionFrames[4] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_005.png"));
        		scorpionFrames[5] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_006.png"));
        		scorpionFrames[6] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_007.png"));
        		scorpionFrames[7] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_008.png"));
        		scorpionFrames[8] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_009.png"));
        		scorpionFrames[9] = ImageIO.read(getClass().getResourceAsStream("/assets/SCORPION/1_enemies_1_walk_010.png"));
        		goblinFrames[0] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_001.png"));
        		goblinFrames[1] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_002.png"));
        		goblinFrames[2] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_003.png"));
        		goblinFrames[3] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_004.png"));
        		goblinFrames[4] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_005.png"));
        		goblinFrames[5] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_006.png"));
        		goblinFrames[6] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_007.png"));
        		goblinFrames[7] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_008.png"));
        		goblinFrames[8] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_009.png"));
        		goblinFrames[9] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_010.png"));
        		goblinFrames[10] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_011.png"));
        		goblinFrames[11] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_012.png"));
        		goblinFrames[12] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_013.png"));
        		goblinFrames[13] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_014.png"));
        		goblinFrames[14] = ImageIO.read(getClass().getResourceAsStream("/assets/GOBLIN/3_enemies_1_walk_015.png"));
        		
        		enemyAssets.put(Enemy.GOBLIN_TYPE, goblinFrames);
        		enemyAssets.put(Enemy.SCORPION_TYPE, scorpionFrames);
        		enemyAssets.put(Enemy.ORC_TYPE, orcFrames);
        	}catch(IOException e ) {
        		e.printStackTrace();
                System.err.println("Errore caricamento asset grafici!");
        	}
        	
        	try {
        		java.io.InputStream is = getClass().getResourceAsStream("/assets/background/Grandover.ttf");
                font = Font.createFont(Font.TRUETYPE_FONT, is);
                mainFont = font.deriveFont(20f);
                winLoseFont = font.deriveFont(60f);
            
                GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
                ge.registerFont(font);
                } catch (IOException | FontFormatException e) {
                mainFont = new Font("Arial", Font.BOLD, 18);
                font = new Font("Arial", Font.BOLD, 50);
                }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2d = (Graphics2D) g;
            // Attiviamo l'anti-aliasing per bordi più morbidi
            g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);

            if (model == null) return;
            g2d.drawImage(background,0,0,getWidth() ,getHeight(),null);
          
            drawSlots(g2d);
            drawEnemies(g2d);
            drawProjectiles(g2d);
            updateMenuButton();         
            drawSoldiers(g2d);
            drawUI(g2d);
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
            	if (slot.isOccupied()) {
                    Tower tower = slot.getTower();
                    
                    // 1. Peschiamo l'INTERO ARRAY di quella torre dalla mappa
                    BufferedImage[] levelsArray = towerAssets.get(tower.getType());
                    
                    // 2. Troviamo l'indice corretto (Livello 1 -> Indice 0)
                    int imageIndex = tower.getLvl() - 1;
                    
                    // 3. Controlliamo che l'array esista e che l'indice non sbordi!
                    if (levelsArray != null && imageIndex < levelsArray.length && levelsArray[imageIndex] != null) {
                        
                        BufferedImage img = levelsArray[imageIndex];
                        g.drawImage(img, slot.getX() - 54, slot.getY() - 68, 120, 110, null);
                        
                    } else {
                        // FALLBACK: Se manca l'immagine di quel livello, disegniamo i blocchi colorati
                        if (tower.getType() == Tower.ARCHER_TYPE) {
                            g.setColor(new Color(34, 139, 34)); 
                            g.fill3DRect(slot.getX() - 25, slot.getY() - 25, 50, 50, true);
                        } else if (tower.getType() == Tower.MAGE_TYPE) {
                            g.setColor(new Color(128, 0, 128));
                            g.fill3DRect(slot.getX() - 25, slot.getY() - 25, 50, 50, true);
                        }
                        // Aggiungiamo un testo per capire il livello anche senza grafica!
                        g.setColor(Color.WHITE);
                        g.drawString("Lvl " + tower.getLvl(), slot.getX() - 10, slot.getY());
                    } 
                }else {
                    	g.setColor(new Color(0,0,0,0));
                    	g.fillRect(slot.getX() - 15, slot.getY() - 15, 30, 30);
                	}
            }
            
            TowerSlot slotToHighlight = null;
            TowerSlot selectedSlot = model.getSelectedBuildSlot();
            
            
            if(selectedSlot != null && selectedSlot.isOccupied()) {
            	int x = selectedSlot.getX();
            	int y = selectedSlot.getY();
            	int range = selectedSlot.getTower().getRange();
            	
            	 g.setColor(new Color(255, 255, 255, 30)); 
                 g.fillOval(x - range, y - range, range * 2, range * 2);
                 
                 g.setColor(new Color(255, 255, 255, 100));
                 g.drawOval(x - range, y - range, range * 2, range * 2);
            	
            	
            }
            
            // CASO A: Il giocatore ha cliccato la Caserma (il menu è aperto)
            if (selectedSlot != null && selectedSlot.isOccupied() && selectedSlot.getTower().getType() == Tower.BARRACKS_TYPE) {
                slotToHighlight = selectedSlot;
            } 
            // CASO B: Il menu è chiuso, ma stiamo piazzando la bandierina!
            else if (model.isSettingRallyPoint()) {
                slotToHighlight = model.getActiveBarracksSlot();
            }

            // SE ABBIAMO UNO SLOT VALIDO, DISEGNIAMO IL CERCHIO!
            if (slotToHighlight != null) {
                int x = slotToHighlight.getX();
                int y = slotToHighlight.getY();
                int range = slotToHighlight.getTower().getRange(); 
                
                // Area blu semitrasparente
                g.setColor(new Color(0, 0, 255, 30)); 
                g.fillOval(x - range, y - range, range * 2, range * 2);
                
                // Bordino bianco per dare l'idea del "limite"
                g.setColor(new Color(255, 255, 255, 100));
                g.drawOval(x - range, y - range, range * 2, range * 2);
            }
        }
        
        private void updateMenuButton() {
        	TowerSlot slot = model.getSelectedBuildSlot();
        	
        	if(slot==null) {
        		archerButton.setVisible(false);
        		mageButton.setVisible(false);
        		cannonButton.setVisible(false);
        		barracksButton.setVisible(false);
        		rallyButton.setVisible(false);
        		upgradeButton.setVisible(false);
        	} else {
        		
        		if(!slot.isOccupied()) {
        			int x = slot.getX() ;
        			int y = slot.getY() ;
        		
        			int w = 85;
        			int h = 70;
        		
        			archerButton.setBounds(x - w - 15, y - h - 15, w, h);
        			mageButton.setBounds(x + 15, y - h - 15, w, h);  
        			cannonButton.setBounds(x - w - 15, y + 15, w, h); 
        			barracksButton.setBounds(x + 15, y + 15, w, h);
                
        			archerButton.setVisible(true);
        			mageButton.setVisible(true);
        			cannonButton.setVisible(true);
        			barracksButton.setVisible(true);
        		} else {
        			
        			Tower tower = slot.getTower();
        			
        			if(tower.getType()==Tower.BARRACKS_TYPE) {
        				int x = slot.getX();
        				int y = slot.getY();
        				rallyButton.setBounds(x -15, y + 40, 30, 30);
        				rallyButton.setVisible(true);
        			}
        			
        			if (tower.canUpgrade()) {
        				
        				int x = slot.getX() ;
            			int y = slot.getY() ;
            			
                        upgradeButton.setBounds(x - 45, y - 60, 90, 30);
                        
                        // Scriviamo il costo direttamente sul bottone!
                        upgradeButton.setText("UP: " + tower.getUpgradeCost() + "g");
                        
                        // Se non hai abbastanza soldi, lo facciamo diventare rosso/grigio
                        if (model.getGold() < tower.getUpgradeCost()) {
                            upgradeButton.setBackground(new Color(255, 0, 0, 150)); // Rosso
                        } else {
                            upgradeButton.setBackground(new Color(255, 215, 0, 200)); // Oro
                        }
                        
                        upgradeButton.setVisible(true);
                    }
        		}
        	}
        }
        
        private void drawProjectiles(Graphics2D g) {
        	for(Projectile p : model.getActiveProjectiles()) {
        		g.setColor(Color.BLACK);
        		int px = (int) p.getX();
        		int py = (int) p.getY();
        			BufferedImage img = projectileAssets.get(p.getType());
        			if(img != null) {
                		AffineTransform oldTrasform = g.getTransform();
                		g.translate(p.getX(),p.getY());
                		g.rotate(p.getAngle());
                		int width = 14;  
                        int height = 8; 
                        g.drawImage(img, -width / 2, -height / 2, width, height, null);
                        g.setTransform(oldTrasform);
                	}
                	else {
                		if(p.getType() == Tower.ARCHER_TYPE) {
                			g.setColor(new Color(34, 139, 34)); 
                        	g.fillOval(px - 25, py - 25, 50, 50);
                		}else if (p.getType() == Tower.MAGE_TYPE) {
                			g.setColor(new Color(128, 0, 128)); 
                        	g.fillOval(px - 25, py - 25, 50, 50);
                		
        			
                		}
        		
                	}
        	}
        }
        
        private void drawSoldiers(Graphics2D g) {
        	for(Soldier s : model.getActiveSoldier()) {
        		
        		g.setColor(new Color(0, 0, 0, 80)); 
                g.fillOval((int)s.getX() - 20, (int)s.getY() + 15, 30, 10);
        		
        		if (s.isMoving() && soldierFrames != null && soldierFrames[0] != null) {
                    // È in movimento! Calcoliamo il frame corretto
                    // animationSpeed regola la velocità visiva (es. 2 = cambia frame ogni 2 tick)
                    int animationSpeed = 2; 
                    int frameIndex = (s.getTikCounter() / animationSpeed) % soldierFrames.length;
                    
                    BufferedImage currentFrame = soldierFrames[frameIndex];
                    g.drawImage(currentFrame, (int)s.getX() - 24, (int)s.getY() - 24, 48, 48, null);

                } else if (soldierFrames[0] != null) {
                    // È fermo! Disegniamo l'immagine statica di default
        			g.drawImage(soldierFrames[0], (int)s.getX() - 24, (int)s.getY() - 24, 48, 48, null);
                    
        		} else {
                    // Fallback di emergenza rosso se non carica le immagini
        			g.setColor(Color.RED); 
                    g.fillOval((int)s.getX() - 12, (int)s.getY() - 12, 24, 24);
        		}
        		
        		int fullBarWidth = 25;
            	double healthPercentage = (double) s.getHealth() / s.getMaxHealth();
            	int currentBarWidth = (int) (fullBarWidth * healthPercentage); 
            	
            	
            	int x = (int)s.getX() - 15;
            	int y = (int)s.getY() - 25;
            	
            	int barHeight = 8;
            	
            	
            	if(redBar != null) {
            		g.drawImage(redBar, x, y, fullBarWidth, barHeight, null);
            	}
            	if(lifespan != null && currentBarWidth > 0) {
            		int sourceCropWidth = (int) (lifespan.getWidth()*healthPercentage);
            		int sourceImageHeight = lifespan.getHeight();
            		g.drawImage(lifespan, 
                            x, y, x + currentBarWidth, y + barHeight,  
                            0, 0, sourceCropWidth, sourceImageHeight,  
                            null);
            	}
        	}
        }
        		
        

        private void drawEnemies(Graphics2D g) {
        	for (Enemy e : model.getActiveEnemies()) {
                
        		
        		g.setColor(new Color(0, 0, 0, 80)); 
                g.fillOval((int)e.getX() - 20, (int)e.getY() + 15, 30, 10);
        		BufferedImage[] img = enemyAssets.get(e.getType());
        		if (img != null && img.length > 0) {

                    int animationSpeed = 1; 

                    int frameIndex = (e.getTikCounter() / animationSpeed) % img.length;

                    BufferedImage currentImage = img[frameIndex];
                    g.drawImage(currentImage, (int)e.getX() - 24, (int)e.getY() - 24, 48, 48, null);
                    
                } else {
                    g.setColor(Color.RED); 
                    g.fillOval((int)e.getX() - 12, (int)e.getY() - 12, 24, 24);
                }
                
                	int fullBarWidth = 25;
                	double healthPercentage = (double) e.getHealth() / e.getMaxHealth();
                	int currentBarWidth = (int) (fullBarWidth * healthPercentage); 
                	
                	
                	int x = (int)e.getX() - 15;
                	int y = (int)e.getY() - 25;
                	
                	int barHeight = 8;
                	
                	
                	if(redBar != null) {
                		g.drawImage(redBar, x, y, fullBarWidth, barHeight, null);
                	}
                	if(lifespan != null && currentBarWidth > 0) {
                		int sourceCropWidth = (int) (lifespan.getWidth()*healthPercentage);
                		int sourceImageHeight = lifespan.getHeight();
                		g.drawImage(lifespan, 
                                x, y, x + currentBarWidth, y + barHeight,               // Dove lo disegno a schermo
                                0, 0, sourceCropWidth, sourceImageHeight,    // Che pezzo del file PNG prendo
                                null);
                	}
                	
            }
        }
        
        private void drawUI(Graphics2D g) {
            g.setFont(mainFont);
            Color menu = new Color(236, 204, 120);
            Color titolo =  new Color(75, 83, 32);
            drawTextMenu(g, "VITE: " + model.getPlayerHealth(), 1160, 40, menu);
            drawTextMenu(g, "ORO: " + model.getGold(), 1160, 70, menu);
            int ondataMostrata = Math.min(model.getCurrentWaveNumber(), model.getTotalWaves());
            drawTextMenu(g, "ONDATA: " + ondataMostrata + "/" + model.getTotalWaves(), 1160, 100, menu);
            
            g.setFont(winLoseFont);

            if (model.getCurrentWaveNumber() > model.getTotalWaves() && model.getActiveEnemies().isEmpty()) {
                drawBigMessage(g, "VITTORIA!", 520, 380, titolo);
            }

            if (model.isGameOver()) {
                drawBigMessage(g, "GAME OVER", 520, 380, titolo);
            }
        }
    }
}
