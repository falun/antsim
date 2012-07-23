/*    */ package ants.core.ui;
/*    */ 
/*    */ import java.awt.Container;
/*    */ import javax.swing.JFrame;
/*    */ 
/*    */ public class Ants extends JFrame
/*    */ {
/*  9 */   private static Ants instance = null;
/*    */ 
/*    */   public static Ants get() {
/* 12 */     if (instance == null) {
/* 13 */       instance = new Ants();
/*    */     }
/* 15 */     return instance;
/*    */   }
/*    */ 
/*    */   public Ants() {
/* 19 */     super("Ants!");
/*    */ 
/* 21 */     setDefaultCloseOperation(3);
/* 22 */     setContentPane(OptionsUI.get());
/* 23 */     pack();
/* 24 */     setResizable(false);
/* 25 */     setLocationRelativeTo(null);
/* 26 */     setVisible(true);
/*    */   }
/*    */ 
/*    */   public void setContentPane(Container contentPane)
/*    */   {
/* 31 */     super.setContentPane(contentPane);
/* 32 */     invalidate();
/* 33 */     validate();
/* 34 */     pack();
/* 35 */     setLocationRelativeTo(null);
/*    */   }
/*    */ 
/*    */   public static void main(String[] args) throws Exception {
/* 39 */     get();
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.Ants
 * JD-Core Version:    0.6.0
 */