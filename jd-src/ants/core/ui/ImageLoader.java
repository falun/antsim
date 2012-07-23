/*    */ package ants.core.ui;
/*    */ 
/*    */ import java.awt.image.BufferedImage;
/*    */ import java.io.InputStream;
/*    */ import javax.imageio.ImageIO;
/*    */ 
/*    */ public class ImageLoader
/*    */ {
/*    */   public static BufferedImage load(String s)
/*    */   {
/*    */     try
/*    */     {
/* 12 */       s = "rez/" + s;
/* 13 */       InputStream is = ImageLoader.class.getResourceAsStream(s);
/* 14 */       return ImageIO.read(is); } catch (Exception e) {
/*    */     }
/* 16 */     throw new RuntimeException("Could not find: " + s);
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.ImageLoader
 * JD-Core Version:    0.6.0
 */