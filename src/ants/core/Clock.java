/*    */ package ants.core;
/*    */ 
/*    */ import java.io.PrintStream;
/*    */ import java.util.Stack;
/*    */ 
/*    */ public class Clock
/*    */ {
/*  7 */   private static final ThreadLocal<Stack<Clocker>> stack = new ThreadLocal()
/*    */   {
/*    */     protected Stack<Clock.Clocker> initialValue() {
/* 10 */       return new Stack();
/*    */     }
/*  7 */   };
/*    */ 
/*    */   public static void in(String s)
/*    */   {
/* 15 */     ((Stack)stack.get()).add(new Clocker(s));
/*    */   }
/*    */ 
/*    */   public static void out() {
/* 19 */     Clocker pop = (Clocker)((Stack)stack.get()).pop();
/* 20 */     long t = System.nanoTime();
/* 21 */     double millis = (t - pop.time) / 1000000.0D;
/* 22 */     System.out.println(pop.s + ": " + millis + " ms");
/*    */   }
/* 26 */   private static class Clocker { private final long time = System.nanoTime();
/*    */     private final String s;
/*    */ 
/*    */     public Clocker(String s) {
/* 30 */       this.s = s;
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.Clock
 * JD-Core Version:    0.6.0
 */