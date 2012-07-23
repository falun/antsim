/*    */ package ants.core;
/*    */ 
/*    */ import ants.Direction;
/*    */ import ants.Surroundings;
/*    */ import ants.Tile;
/*    */ 
/*    */ public class SurroundingsImpl
/*    */   implements Surroundings
/*    */ {
/*    */   private final Tile current;
/*    */   private final Tile north;
/*    */   private final Tile east;
/*    */   private final Tile south;
/*    */   private final Tile west;
/*    */ 
/*    */   public SurroundingsImpl(TileImpl tile, TileImpl[][] map)
/*    */   {
/* 13 */     this(tile, map[tile.getI()][(tile.getJ() - 1)], map[(tile.getI() + 1)][tile.getJ()], map[tile.getI()][(tile.getJ() + 1)], 
/* 13 */       map[(tile.getI() - 1)][tile.getJ()]);
/*    */   }
/*    */ 
/*    */   public SurroundingsImpl(Tile current, Tile north, Tile east, Tile south, Tile west) {
/* 17 */     this.current = current;
/* 18 */     this.north = north;
/* 19 */     this.east = east;
/* 20 */     this.south = south;
/* 21 */     this.west = west;
/*    */   }
/*    */ 
/*    */   public Tile getCurrentTile()
/*    */   {
/* 26 */     return this.current;
/*    */   }
/*    */ 
/*    */   public Tile getTile(Direction direction)
/*    */   {
/* 31 */     if (direction == null) {
/* 32 */       throw new NullArgumentException("direction");
/*    */     }
/*    */ 
/* 35 */     if (direction == Direction.NORTH)
/* 36 */       return this.north;
/* 37 */     if (direction == Direction.EAST)
/* 38 */       return this.east;
/* 39 */     if (direction == Direction.SOUTH)
/* 40 */       return this.south;
/* 41 */     if (direction == Direction.WEST) {
/* 42 */       return this.west;
/*    */     }
/* 44 */     throw new IllegalStateException("Unknown direction: " + direction);
/*    */   }
/*    */ }

/* Location:           C:\Users\Richard\Projects\Ants\sim-src\
 * Qualified Name:     ants.core.SurroundingsImpl
 * JD-Core Version:    0.6.0
 */