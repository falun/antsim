/*    */ package ants.core.ui;
/*    */ 
/*    */ import ants.core.AntsGameModel;
/*    */ import java.awt.event.ActionEvent;
/*    */ import java.awt.event.ActionListener;
/*    */ import javax.swing.JButton;
/*    */ import javax.swing.JLabel;
/*    */ import javax.swing.JPanel;
/*    */ import net.miginfocom.swing.MigLayout;
/*    */ 
/*    */ public class SimulationControls extends JPanel
/*    */ {
/* 16 */   private static final int[] SPEEDS = { 1, 2, 4, 8, 16, 32, 64 };
/*    */   private final AntsGameModel model;
/*    */   private JButton pauseButton;
/*    */   private JLabel speedLabel;
/*    */   private JButton minusButton;
/*    */   private JButton plusButton;
/* 83 */   private final ActionListener actionListener = new ActionListener()
/*    */   {
/*    */     public void actionPerformed(ActionEvent e) {
/* 86 */       Object src = e.getSource();
/* 87 */       if (src == SimulationControls.this.minusButton)
/* 88 */         SimulationControls.this.adjustSpeed(-1);
/* 89 */       else if (src == SimulationControls.this.plusButton)
/* 90 */         SimulationControls.this.adjustSpeed(1);
/* 91 */       else if (src == SimulationControls.this.pauseButton)
/* 92 */         SimulationControls.this.pause();
/*    */     }
/* 83 */   };
/*    */ 
/*    */   public SimulationControls(AntsGameModel model)
/*    */   {
/* 25 */     this.model = model;
/*    */ 
/* 27 */     initUI();
/*    */ 
/* 29 */     initializeListeners();
/* 30 */     syncLabel();
/*    */   }
/*    */ 
/*    */   private void initUI() {
/* 34 */     setLayout(new MigLayout("insets 10, gap 10"));
/*    */ 
/* 36 */     this.pauseButton = new JButton("Pause");
/* 37 */     this.speedLabel = new JLabel();
/* 38 */     this.minusButton = new JButton("-");
/* 39 */     this.plusButton = new JButton("+");
/*    */ 
/* 41 */     this.speedLabel.setHorizontalAlignment(4);
/*    */ 
/* 43 */     add(this.pauseButton, "");
/* 44 */     add(this.speedLabel, "width 150!");
/* 45 */     add(this.minusButton, "");
/* 46 */     add(this.plusButton, "");
/*    */   }
/*    */ 
/*    */   private void initializeListeners() {
/* 50 */     this.minusButton.addActionListener(this.actionListener);
/* 51 */     this.plusButton.addActionListener(this.actionListener);
/* 52 */     this.pauseButton.addActionListener(this.actionListener);
/*    */   }
/*    */ 
/*    */   private void adjustSpeed(int t)
/*    */   {
/* 57 */     for (int i = 0; i < SPEEDS.length; i++) {
/* 58 */       if (SPEEDS[i] == this.model.getSpeed())
/*    */       {
/*    */         break;
/*    */       }
/*    */     }
/* 63 */     i += t;
/*    */ 
/* 65 */     i = Math.max(i, 0);
/* 66 */     i = Math.min(i, SPEEDS.length - 1);
/*    */ 
/* 68 */     this.model.setSpeed(SPEEDS[i]);
/*    */ 
/* 70 */     syncLabel();
/*    */   }
/*    */ 
/*    */   private void pause() {
/* 74 */     if (this.pauseButton.getText().equals("Pause")) {
/* 75 */       this.model.setPaused(true);
/* 76 */       this.pauseButton.setText("Resume");
/*    */     } else {
/* 78 */       this.model.setPaused(false);
/* 79 */       this.pauseButton.setText("Pause");
/*    */     }
/*    */   }
/*    */ 
/*    */   private void syncLabel()
/*    */   {
/* 98 */     this.speedLabel.setText(this.model.getSpeed() + "x speed");
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.SimulationControls
 * JD-Core Version:    0.6.0
 */