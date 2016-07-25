/*    */ package javax.swing;
/*    */ 
/*    */ import java.awt.Color;
/*    */ import java.io.PrintStream;
/*    */ import java.util.Hashtable;
/*    */ 
/*    */ class DebugGraphicsInfo
/*    */ {
/* 37 */   Color flashColor = Color.red;
/* 38 */   int flashTime = 100;
/* 39 */   int flashCount = 2;
/*    */   Hashtable<JComponent, Integer> componentToDebug;
/* 41 */   JFrame debugFrame = null;
/* 42 */   PrintStream stream = System.out;
/*    */ 
/*    */   void setDebugOptions(JComponent paramJComponent, int paramInt) {
/* 45 */     if (paramInt == 0) {
/* 46 */       return;
/*    */     }
/* 48 */     if (this.componentToDebug == null) {
/* 49 */       this.componentToDebug = new Hashtable();
/*    */     }
/* 51 */     if (paramInt > 0)
/* 52 */       this.componentToDebug.put(paramJComponent, Integer.valueOf(paramInt));
/*    */     else
/* 54 */       this.componentToDebug.remove(paramJComponent);
/*    */   }
/*    */ 
/*    */   int getDebugOptions(JComponent paramJComponent)
/*    */   {
/* 59 */     if (this.componentToDebug == null) {
/* 60 */       return 0;
/*    */     }
/* 62 */     Integer localInteger = (Integer)this.componentToDebug.get(paramJComponent);
/*    */ 
/* 64 */     return localInteger == null ? 0 : localInteger.intValue();
/*    */   }
/*    */ 
/*    */   void log(String paramString)
/*    */   {
/* 69 */     this.stream.println(paramString);
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.DebugGraphicsInfo
 * JD-Core Version:    0.6.2
 */