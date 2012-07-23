/*     */ package ants.core;
/*     */ 
/*     */ import ants.Action;
/*     */ import ants.Ant;
/*     */ import ants.Direction;
/*     */ import ants.Surroundings;
/*     */ import ants.Tile;
/*     */ import java.awt.Point;
/*     */ import java.beans.PropertyChangeEvent;
/*     */ import java.beans.PropertyChangeListener;
/*     */ import java.io.PrintStream;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Random;
/*     */ import java.util.Set;
/*     */ import java.util.concurrent.Executor;
/*     */ import java.util.concurrent.Executors;
/*     */ 
/*     */ public class AntsGameModel
/*     */   implements Runnable
/*     */ {
/*  26 */   public static final String PROP_TURN = AntsGameModel.class.getSimpleName() + ".propTurn";
/*  27 */   public static final String PROP_GAME_OVER = AntsGameModel.class.getSimpleName() + ".propGameOver";
/*     */   public static final int WINNING_AMOUNT_OF_FOOD = 500;
/*  31 */   private static final Executor executor = Executors.newSingleThreadExecutor();
/*     */ 
/*  33 */   private static final Random rand = new Random();
/*     */   private static Point startingLocation;
/*     */   private final Class<? extends Ant> antClass;
/*     */   private final TileImpl[][] map;
/*     */   private final int numTurnsPerNewAnt;
/*  40 */   private final Map<Ant, TileImpl> antTiles = new HashMap();
/*  41 */   private final Set<Ant> antsCarryingFood = new HashSet();
/*  42 */   private int turn = 1;
/*  43 */   private int speed = 1;
/*  44 */   private boolean paused = false;
/*     */ 
/*  46 */   private List<PropertyChangeListener> listeners = new LinkedList();
/*     */ 
/* 140 */   private Map<Tile, Surroundings> surroundingsCache = new HashMap();
/*     */ 
/*     */   public AntsGameModel(Class<? extends Ant> antClass, int mapWidth, int mapHeight, int numStartingAnts, int numTurnsPerNewAnt)
/*     */   {
/*  49 */     this.antClass = antClass;
/*  50 */     this.numTurnsPerNewAnt = numTurnsPerNewAnt;
/*     */ 
/*  52 */     this.map = generateMap(mapWidth, mapHeight);
/*     */ 
/*  55 */     for (int k = 0; k < numStartingAnts; k++) {
/*  56 */       spawnAnt();
/*     */     }
/*     */ 
/*  59 */     executor.execute(this);
/*     */   }
/*     */ 
/*     */   public void run() {
/*     */     try {
/*     */       while (true) {
/*  65 */         if (this.turn % this.numTurnsPerNewAnt == 0) {
/*  66 */           spawnAnt();
/*     */         }
/*     */ 
/*  69 */         long now = System.nanoTime();
/*  70 */         if (!this.paused) {
/*  71 */           Clock.in("TICK");
/*  72 */           tick();
/*  73 */           Clock.out();
/*     */ 
/*  75 */           if (isWon()) {
/*  76 */             System.out.println("You won after " + this.turn + " turns!");
/*  77 */             firePropertyChange(PROP_GAME_OVER, Boolean.valueOf(false), Boolean.valueOf(true));
/*  78 */             break;
/*     */           }
/*     */ 
/*  81 */           firePropertyChange(PROP_TURN, Integer.valueOf(this.turn), Integer.valueOf(++this.turn));
/*     */         }
/*  83 */         long after = System.nanoTime();
/*  84 */         long idealTickTime = ()(750.0D / this.speed);
/*  85 */         long actualTime = ()((after - now) / 1000000.0D);
/*     */ 
/*  87 */         if (actualTime < idealTickTime)
/*  88 */           Thread.sleep(idealTickTime - actualTime);
/*     */       }
/*     */     } catch (Exception e) {
/*  91 */       e.printStackTrace();
/*     */     }
/*     */   }
/*     */ 
/*     */   public int getSpeed()
/*     */   {
/*  98 */     return this.speed;
/*     */   }
/*     */ 
/*     */   public void setSpeed(int speed) {
/* 102 */     this.speed = speed;
/*     */   }
/*     */ 
/*     */   public int getTurn() {
/* 106 */     return this.turn;
/*     */   }
/*     */ 
/*     */   public Set<Ant> getAntsCarryingFood() {
/* 110 */     return this.antsCarryingFood;
/*     */   }
/*     */ 
/*     */   public void addListener(PropertyChangeListener listener) {
/* 114 */     this.listeners.add(listener);
/*     */   }
/*     */ 
/*     */   public void removeListener(PropertyChangeListener listener) {
/* 118 */     this.listeners.remove(listener);
/*     */   }
/*     */ 
/*     */   private void firePropertyChange(String name, Object oldValue, Object newValue) {
/* 122 */     if (this.listeners.isEmpty()) {
/* 123 */       return;
/*     */     }
/* 125 */     PropertyChangeEvent e = new PropertyChangeEvent(this, name, oldValue, newValue);
/* 126 */     for (PropertyChangeListener listener : this.listeners)
/* 127 */       listener.propertyChange(e);
/*     */   }
/*     */ 
/*     */   public TileImpl[][] getMap()
/*     */   {
/* 132 */     return this.map;
/*     */   }
/*     */ 
/*     */   private boolean isWon() {
/* 136 */     TileImpl home = getHome();
/* 137 */     return home.getAmountOfFood() >= 500;
/*     */   }
/*     */ 
/*     */   private void tick()
/*     */   {
/* 143 */     for (Map.Entry entry : this.antTiles.entrySet()) {
/* 144 */       Ant ant = (Ant)entry.getKey();
/* 145 */       TileImpl tile = (TileImpl)entry.getValue();
/* 146 */       Surroundings surroundings = (Surroundings)this.surroundingsCache.get(tile);
/* 147 */       if (surroundings == null) {
/* 148 */         surroundings = new SurroundingsImpl(tile, this.map);
/* 149 */         this.surroundingsCache.put(tile, surroundings);
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/* 154 */         Clock.in("ant logic");
/* 155 */         Surroundings clonedSurroundings = (Surroundings)Serialization.clone(surroundings);
/* 156 */         Action action = ant.getAction(clonedSurroundings);
/* 157 */         Clock.out();
/*     */       } catch (Exception e) {
/* 159 */         e.printStackTrace();
/* 160 */         continue;
/*     */       }
/*     */       Action action;
/* 163 */       if (action == Action.HALT)
/*     */         continue;
/* 165 */       if (action == Action.DROP_OFF) {
/* 166 */         if (!this.antsCarryingFood.contains(ant)) {
/* 167 */           System.err.println("An ant cannot drop off food when it is not carrying any!");
/*     */         }
/*     */         else {
/* 170 */           tile.incrementFood();
/* 171 */           this.antsCarryingFood.remove(ant);
/*     */         }
/* 172 */       } else if (action == Action.GATHER) {
/* 173 */         if (this.antsCarryingFood.contains(ant)) {
/* 174 */           System.err.println("An ant cannot gather when it is already carrying food!");
/*     */         }
/* 177 */         else if (tile.getAmountOfFood() == 0) {
/* 178 */           System.err.println("Cannot gather food when there is no food remaining!");
/*     */         }
/*     */         else {
/* 181 */           tile.decrementFood();
/* 182 */           this.antsCarryingFood.add(ant);
/*     */         }
/*     */       } else {
/* 184 */         Direction direction = action.getDirection();
/* 185 */         if (direction == null) {
/* 186 */           System.err.println("Unrecognized Action: " + action);
/*     */         }
/*     */         else
/*     */         {
/* 190 */           TileImpl toTile = getTile(tile, direction);
/* 191 */           if (!toTile.isTravelable()) {
/* 192 */             System.err.println("Cannot move to a non-travelable tile!");
/*     */           }
/*     */           else
/*     */           {
/* 196 */             tile.removeAnt(ant);
/* 197 */             toTile.addAnt(ant, direction);
/* 198 */             entry.setValue(toTile);
/*     */ 
/* 200 */             if (toTile.getNumAnts() > 1) {
/* 201 */               byte[] toSend = ant.send();
/* 202 */               for (Ant ant2 : toTile.getAnts().keySet())
/* 203 */                 if (ant2 != ant) {
/* 204 */                   byte[] toSend2 = ant2.send();
/* 205 */                   ant.receive(toSend2);
/* 206 */                   ant2.receive(toSend);
/*     */                 }
/*     */             }
/*     */           }
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private TileImpl getTile(TileImpl from, Direction direction) {
/* 216 */     int i = from.getI(); int j = from.getJ();
/* 217 */     if (direction == Direction.NORTH)
/* 218 */       return this.map[i][(j - 1)];
/* 219 */     if (direction == Direction.EAST)
/* 220 */       return this.map[(i + 1)][j];
/* 221 */     if (direction == Direction.SOUTH)
/* 222 */       return this.map[i][(j + 1)];
/* 223 */     if (direction == Direction.WEST) {
/* 224 */       return this.map[(i - 1)][j];
/*     */     }
/* 226 */     throw new IllegalArgumentException("UnrecognizedDirection: " + direction);
/*     */   }
/*     */ 
/*     */   private TileImpl[][] generateMap(int width, int height)
/*     */   {
/* 231 */     if ((width <= 0) || (height <= 0)) {
/* 232 */       throw new IllegalArgumentException("width and height must be > 0");
/*     */     }
/*     */ 
/* 235 */     startingLocation = new Point((int)(1.0D + rand.nextDouble() * (width - 2)), (int)(1.0D + rand.nextDouble() * (height - 2)));
/*     */ 
/* 237 */     TileImpl[][] ret = new TileImpl[width][height];
/*     */ 
/* 239 */     for (int i = 0; i < width; i++) {
/* 240 */       for (int j = 0; j < height; j++)
/*     */       {
/*     */         TileImpl t;
/*     */         TileImpl t;
/* 242 */         if ((i == startingLocation.x) && (j == startingLocation.y)) {
/* 243 */           t = new TileImpl(i, j, TileImpl.TileType.HOME, 0);
/*     */         }
/*     */         else
/*     */         {
/*     */           TileImpl t;
/* 244 */           if ((i == 0) || (j == 0) || (i == width - 1) || (j == height - 1) || (rand.nextDouble() < 0.16D))
/* 245 */             t = new TileImpl(i, j, TileImpl.TileType.WATER, 0);
/*     */           else
/* 247 */             t = new TileImpl(i, j, TileImpl.TileType.GRASS, 0);
/*     */         }
/* 249 */         ret[i][j] = t;
/*     */       }
/*     */     }
/*     */ 
/* 253 */     int numLocations = width * height / 16;
/* 254 */     int totalFoodToDistribute = 750;
/* 255 */     int[] foodAmounts = new int[numLocations];
/* 256 */     for (int i = 0; i < foodAmounts.length; i++)
/* 257 */       foodAmounts[i] = (totalFoodToDistribute / numLocations);
/*     */     int a;
/*     */     int b;
/* 261 */     for (int i = 0; i < 1000; i++) {
/* 262 */       a = (int)(rand.nextDouble() * foodAmounts.length);
/* 263 */       b = (int)(rand.nextDouble() * foodAmounts.length);
/* 264 */       if ((foodAmounts[a] > 0) && (foodAmounts[b] < 100)) {
/* 265 */         foodAmounts[a] -= 1;
/* 266 */         foodAmounts[b] += 1;
/*     */       }
/*     */     }
/*     */ 
/* 270 */     for (int foodAmount : foodAmounts) { TileImpl t;
/*     */       do { int a = (int)(rand.nextDouble() * width);
/* 273 */         int b = (int)(rand.nextDouble() * height);
/* 274 */         t = ret[a][b]; }
/* 275 */       while ((!t.isTravelable()) || (t.getAmountOfFood() != 0));
/* 276 */       t.setAmountOfFood(foodAmount);
/*     */     }
/*     */ 
/* 282 */     return ret;
/*     */   }
/*     */ 
/*     */   private Ant spawnAnt() {
/*     */     try {
/* 287 */       TileImpl home = getHome();
/* 288 */       Ant ret = (Ant)this.antClass.newInstance();
/* 289 */       this.antTiles.put(ret, home);
/* 290 */       home.addAnt(ret, Direction.NORTH);
/* 291 */       return ret; } catch (Exception e) {
/*     */     }
/* 293 */     throw new RuntimeException(e);
/*     */   }
/*     */ 
/*     */   private TileImpl getHome()
/*     */   {
/* 298 */     return this.map[startingLocation.x][startingLocation.y];
/*     */   }
/*     */ 
/*     */   public void setPaused(boolean paused) {
/* 302 */     this.paused = paused;
/*     */   }
/*     */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.AntsGameModel
 * JD-Core Version:    0.6.0
 */