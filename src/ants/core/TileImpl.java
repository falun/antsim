/*     */ package ants.core;
/*     */ 
/*     */ import ants.Ant;
/*     */ import ants.Direction;
/*     */ import ants.Tile;
/*     */ import ants.core.ui.ImageLoader;
/*     */ import java.awt.Color;
/*     */ import java.awt.Graphics;
/*     */ import java.awt.geom.AffineTransform;
/*     */ import java.awt.image.AffineTransformOp;
/*     */ import java.awt.image.BufferedImage;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TileImpl
/*     */   implements Tile
/*     */ {
/*  21 */   private static final BufferedImage grass = ImageLoader.load("grass.gif");
/*  22 */   private static final BufferedImage water = ImageLoader.load("water.gif");
/*  23 */   private static final BufferedImage antHill = ImageLoader.load("anthill.gif");
/*  24 */   private static final BufferedImage antEmpty = ImageLoader.load("ant_empty.gif");
/*  25 */   private static final BufferedImage antCarry = ImageLoader.load("ant_carry.gif");
/*     */ 
/*  27 */   private static final Color foodColor = new Color(255, 255, 0);
/*     */   public static final int SIZE = 32;
/*     */   public static final int MAX_NUM_FOOD = 100;
/*  32 */   private static Map<String, BufferedImage> antImageCache = new HashMap();
/*     */   private final int i;
/*     */   private final int j;
/*     */   private final TileType type;
/*     */   private int numFood;
/*  37 */   private int numAnts = 0;
/*  38 */   private final transient Map<Ant, Direction> ants = new HashMap();
/*     */ 
/*     */   public TileImpl(int i, int j, TileType type, int numFood) {
/*  41 */     this.i = i;
/*  42 */     this.j = j;
/*  43 */     this.type = type;
/*  44 */     this.numFood = numFood;
/*     */   }
/*     */ 
/*     */   public synchronized void render(Graphics g, Set<Ant> antsWithFood) {
/*  48 */     int x = this.i * 32; int y = this.j * 32;
/*     */ 
/*  50 */     if (this.type == TileType.GRASS) {
/*  51 */       g.drawImage(grass, x, y, 32, 32, null);
/*  52 */     } else if (this.type == TileType.WATER) {
/*  53 */       g.drawImage(water, x, y, 32, 32, null);
/*  54 */     } else if (this.type == TileType.HOME) {
/*  55 */       g.drawImage(grass, x, y, 32, 32, null);
/*  56 */       g.drawImage(antHill, x, y, 32, 32, null);
/*     */     }
/*     */ 
/*  59 */     if (this.numFood > 0) {
/*  60 */       g.setColor(foodColor);
/*  61 */       int foodSize = (int)(1.0D * this.numFood / 100.0D * 32.0D);
/*  62 */       if (this.type == TileType.HOME) {
/*  63 */         foodSize = (int)(1.0D * this.numFood / 500.0D * 32.0D / 2.0D);
/*     */       }
/*  65 */       foodSize = Math.max(foodSize, 4);
/*  66 */       g.fillOval(x + (32 - foodSize) / 2, y + (32 - foodSize) / 2, foodSize, foodSize);
/*     */     }
/*     */ 
/*  69 */     for (Map.Entry entry : this.ants.entrySet()) {
/*  70 */       BufferedImage img = getImage((Ant)entry.getKey(), (Direction)entry.getValue(), antsWithFood.contains(entry.getKey()));
/*  71 */       g.drawImage(img, x, y, 32, 32, null);
/*     */     }
/*     */   }
/*     */ 
/*     */   private BufferedImage getImage(Ant ant, Direction direction, boolean hasFood) {
/*  76 */     String key = direction.toString() + hasFood;
/*  77 */     BufferedImage ret = (BufferedImage)antImageCache.get(key);
/*  78 */     if (ret == null) {
/*  79 */       ret = hasFood ? antCarry : antEmpty;
/*  80 */       double rotation = getRotation(direction);
/*  81 */       if (rotation != 0.0D) {
/*  82 */         AffineTransform transform = new AffineTransform();
/*  83 */         transform.rotate(rotation, ret.getWidth() / 2, ret.getHeight() / 2);
/*  84 */         AffineTransformOp op = new AffineTransformOp(transform, 3);
/*  85 */         ret = op.filter(ret, null);
/*     */       }
/*  87 */       antImageCache.put(key, ret);
/*     */     }
/*  89 */     return ret;
/*     */   }
/*     */ 
/*     */   private double getRotation(Direction direction) {
/*  93 */     if (direction == Direction.NORTH)
/*  94 */       return 0.0D;
/*  95 */     if (direction == Direction.EAST)
/*  96 */       return 1.570796326794897D;
/*  97 */     if (direction == Direction.SOUTH)
/*  98 */       return 3.141592653589793D;
/*  99 */     if (direction == Direction.WEST) {
/* 100 */       return -1.570796326794897D;
/*     */     }
/* 102 */     throw new IllegalStateException();
/*     */   }
/*     */ 
/*     */   public int getAmountOfFood()
/*     */   {
/* 108 */     return this.numFood;
/*     */   }
/*     */ 
/*     */   public int getNumAnts()
/*     */   {
/* 113 */     return this.numAnts;
/*     */   }
/*     */ 
/*     */   public boolean isTravelable()
/*     */   {
/* 118 */     return this.type != TileType.WATER;
/*     */   }
/*     */ 
/*     */   public synchronized void addAnt(Ant ant, Direction direction) {
/* 122 */     if (this.ants.containsKey(ant)) {
/* 123 */       throw new IllegalArgumentException("Cannot add an ant to a tile it is already on!");
/*     */     }
/*     */ 
/* 126 */     this.numAnts += 1;
/* 127 */     this.ants.put(ant, direction);
/*     */   }
/*     */ 
/*     */   public synchronized void removeAnt(Ant ant) {
/* 131 */     if (this.ants.remove(ant) == null) {
/* 132 */       throw new IllegalArgumentException("Could not find the ant to remove: " + ant);
/*     */     }
/* 134 */     this.numAnts -= 1;
/*     */   }
/*     */ 
/*     */   public Map<Ant, Direction> getAnts() {
/* 138 */     return Collections.unmodifiableMap(this.ants);
/*     */   }
/*     */ 
/*     */   public void decrementFood() {
/* 142 */     if (this.numFood <= 0) {
/* 143 */       throw new RuntimeException("Cannot decrement food when there is no food left!");
/*     */     }
/* 145 */     this.numFood -= 1;
/*     */   }
/*     */ 
/*     */   public void incrementFood() {
/* 149 */     this.numFood += 1;
/*     */   }
/*     */ 
/*     */   public TileType getType() {
/* 153 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getI() {
/* 157 */     return this.i;
/*     */   }
/*     */ 
/*     */   public int getJ() {
/* 161 */     return this.j;
/*     */   }
/*     */ 
/*     */   public void setAmountOfFood(int foodAmount)
/*     */   {
/* 169 */     this.numFood = foodAmount;
/*     */   }
/*     */ 
/*     */   public static enum TileType
/*     */   {
/* 165 */     HOME, GRASS, WATER;
/*     */   }
/*     */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.TileImpl
 * JD-Core Version:    0.6.0
 */