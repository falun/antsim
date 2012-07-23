/*    */ package ants.core.ui;
/*    */ 
/*    */ import ants.core.AntsGameModel;
/*    */ import java.beans.PropertyChangeEvent;
/*    */ import java.beans.PropertyChangeListener;
/*    */ import javax.swing.JOptionPane;
/*    */ import javax.swing.JPanel;
/*    */ import net.miginfocom.swing.MigLayout;
/*    */ 
/*    */ public class AntsView extends JPanel
/*    */ {
/*    */   private final AntsGameModel model;
/*    */   private MapView mapView;
/*    */   private SimulationControls controls;
/*    */ 
/*    */   public AntsView(AntsGameModel model)
/*    */   {
/* 20 */     this.model = model;
/*    */ 
/* 22 */     initUI();
/*    */ 
/* 24 */     model.addListener(new PropertyChangeListener(model)
/*    */     {
/*    */       public void propertyChange(PropertyChangeEvent evt) {
/* 27 */         String name = evt.getPropertyName();
/*    */ 
/* 29 */         if (name.equals(AntsGameModel.PROP_TURN)) {
/* 30 */           AntsView.this.mapView.repaint();
/* 31 */         } else if (name.equals(AntsGameModel.PROP_GAME_OVER)) {
/* 32 */           JOptionPane.showMessageDialog(null, "You won after " + this.val$model.getTurn() + " turns.");
/* 33 */           Ants.get().setContentPane(OptionsUI.get());
/*    */         }
/*    */       } } );
/*    */   }
/*    */ 
/*    */   private void initUI() {
/* 40 */     setLayout(new MigLayout("insets 0, gap 0"));
/*    */ 
/* 42 */     this.mapView = new MapView(this.model.getMap(), this.model.getAntsCarryingFood());
/* 43 */     this.controls = new SimulationControls(this.model);
/*    */ 
/* 45 */     add(this.mapView, "width pref!, height pref!, wrap");
/* 46 */     add(this.controls, "width 100%");
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.ui.AntsView
 * JD-Core Version:    0.6.0
 */