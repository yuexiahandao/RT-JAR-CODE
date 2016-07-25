/*    */ package sun.print;
/*    */ 
/*    */ import java.awt.GraphicsConfiguration;
/*    */ import java.awt.GraphicsDevice;
/*    */ import java.awt.Window;
/*    */ 
/*    */ public final class PrinterGraphicsDevice extends GraphicsDevice
/*    */ {
/*    */   String printerID;
/*    */   GraphicsConfiguration graphicsConf;
/*    */ 
/*    */   protected PrinterGraphicsDevice(GraphicsConfiguration paramGraphicsConfiguration, String paramString)
/*    */   {
/* 39 */     this.printerID = paramString;
/* 40 */     this.graphicsConf = paramGraphicsConfiguration;
/*    */   }
/*    */ 
/*    */   public int getType() {
/* 44 */     return 1;
/*    */   }
/*    */ 
/*    */   public String getIDstring() {
/* 48 */     return this.printerID;
/*    */   }
/*    */ 
/*    */   public GraphicsConfiguration[] getConfigurations() {
/* 52 */     GraphicsConfiguration[] arrayOfGraphicsConfiguration = new GraphicsConfiguration[1];
/* 53 */     arrayOfGraphicsConfiguration[0] = this.graphicsConf;
/* 54 */     return arrayOfGraphicsConfiguration;
/*    */   }
/*    */ 
/*    */   public GraphicsConfiguration getDefaultConfiguration() {
/* 58 */     return this.graphicsConf;
/*    */   }
/*    */ 
/*    */   public void setFullScreenWindow(Window paramWindow)
/*    */   {
/*    */   }
/*    */ 
/*    */   public Window getFullScreenWindow() {
/* 66 */     return null;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.print.PrinterGraphicsDevice
 * JD-Core Version:    0.6.2
 */