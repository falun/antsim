/*    */ package ants.core.ui;
/*    */ 
/*    */ import ants.Ant;
/*    */ import ants.core.TileImpl;
/*    */ import java.awt.Color;
/*    */ import java.awt.Dimension;
/*    */ import java.awt.Graphics;
/*    */ import java.awt.Graphics2D;
/*    */ import java.awt.RenderingHints;
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.util.Set;
/*    */ import javax.swing.JPanel;
/*    */ 
/*    */ public class MapView extends JPanel
/*    */ {
/*    */   private final TileImpl[][] map;
/*    */   private final BufferedImage buffer;
/*    */   private final Set<Ant> antsWithFood;
/*    */ 
/*    */   public MapView(TileImpl[][] map, Set<Ant> antsWithFood)
/*    */   {
/* 23 */     this.map = map;
/* 24 */     this.antsWithFood = antsWithFood;
/*    */ 
/* 26 */     Dimension dim = new Dimension(map.length * 32, map[0].length * 32);
/* 27 */     setPreferredSize(dim);
/* 28 */     this.buffer = new BufferedImage(dim.width, dim.height, 2);
/*    */   }
/*    */ 
/*    */   protected void paintComponent(Graphics g)
/*    */   {
/* 33 */     render(g);
/*    */   }
/*    */ 
/*    */   public void render() {
/* 37 */     Graphics g = getGraphics();
/* 38 */     render(g);
/* 39 */     g.dispose();
/*    */   }
/*    */ 
/*    */   private void render(Graphics gg) {
/* 43 */     Graphics2D g = this.buffer.createGraphics();
/*    */ 
/* 45 */     g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
/*    */ 
/* 47 */     g.setColor(Color.black);
/* 48 */     g.fillRect(0, 0, getWidth(), getHeight());
/*    */ 
/* 50 */     for (int i = 0; i < this.map.length; i++) {
/* 51 */       for (int j = 0; j < this.map[i].length; j++) {
/* 52 */         TileImpl tile = this.map[i][j];
/* 53 */         tile.render(g, this.antsWithFood);
/*    */       }
/*    */     }
/*    */ 
/* 57 */     g.dispose();
/*    */ 
/* 59 */     gg.drawImage(this.buffer, 0, 0, null);
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.MapView
 * JD-Core Version:    0.6.0
 */