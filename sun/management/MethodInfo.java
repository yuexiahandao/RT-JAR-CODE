/*    */ package sun.management;
/*    */ 
/*    */ import java.io.Serializable;
/*    */ 
/*    */ public class MethodInfo
/*    */   implements Serializable
/*    */ {
/*    */   private String name;
/*    */   private long type;
/*    */   private int compileSize;
/*    */   private static final long serialVersionUID = 6992337162326171013L;
/*    */ 
/*    */   MethodInfo(String paramString, long paramLong, int paramInt)
/*    */   {
/* 38 */     this.name = paramString;
/* 39 */     this.type = paramLong;
/* 40 */     this.compileSize = paramInt;
/*    */   }
/*    */ 
/*    */   public String getName()
/*    */   {
/* 49 */     return this.name;
/*    */   }
/*    */ 
/*    */   public long getType()
/*    */   {
/* 59 */     return this.type;
/*    */   }
/*    */ 
/*    */   public int getCompileSize()
/*    */   {
/* 69 */     return this.compileSize;
/*    */   }
/*    */ 
/*    */   public String toString() {
/* 73 */     return getName() + " type = " + getType() + " compileSize = " + getCompileSize();
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.MethodInfo
 * JD-Core Version:    0.6.2
 */