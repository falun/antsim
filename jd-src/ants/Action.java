/*    */ package ants;
/*    */ 
/*    */ public class Action
/*    */ {
/* 26 */   public static Action HALT = new Action();
/*    */ 
/* 33 */   public static Action GATHER = new Action();
/*    */ 
/* 40 */   public static Action DROP_OFF = new Action();
/*    */   private final Direction direction;
/*    */ 
/*    */   public static Action move(Direction direction)
/*    */   {
/* 20 */     return new Action(direction);
/*    */   }
/*    */ 
/*    */   private Action()
/*    */   {
/* 45 */     this(null);
/*    */   }
/*    */ 
/*    */   private Action(Direction direction) {
/* 49 */     this.direction = direction;
/*    */   }
/*    */ 
/*    */   public Direction getDirection() {
/* 53 */     return this.direction;
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.Action
 * JD-Core Version:    0.6.0
 */