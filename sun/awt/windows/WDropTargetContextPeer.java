/*    */ package sun.awt.windows;
/*    */ 
/*    */ import java.io.FileInputStream;
/*    */ import java.io.IOException;
/*    */ import sun.awt.PeerEvent;
/*    */ import sun.awt.SunToolkit;
/*    */ import sun.awt.dnd.SunDropTargetContextPeer;
/*    */ import sun.awt.dnd.SunDropTargetContextPeer.EventDispatcher;
/*    */ import sun.awt.dnd.SunDropTargetEvent;
/*    */ 
/*    */ final class WDropTargetContextPeer extends SunDropTargetContextPeer
/*    */ {
/*    */   static WDropTargetContextPeer getWDropTargetContextPeer()
/*    */   {
/* 53 */     return new WDropTargetContextPeer();
/*    */   }
/*    */ 
/*    */   private static FileInputStream getFileStream(String paramString, long paramLong)
/*    */     throws IOException
/*    */   {
/* 71 */     return new WDropTargetContextPeerFileStream(paramString, paramLong);
/*    */   }
/*    */ 
/*    */   private static Object getIStream(long paramLong)
/*    */     throws IOException
/*    */   {
/* 79 */     return new WDropTargetContextPeerIStream(paramLong);
/*    */   }
/*    */ 
/*    */   protected Object getNativeData(long paramLong) {
/* 83 */     return getData(getNativeDragContext(), paramLong);
/*    */   }
/*    */ 
/*    */   protected void doDropDone(boolean paramBoolean1, int paramInt, boolean paramBoolean2)
/*    */   {
/* 92 */     dropDone(getNativeDragContext(), paramBoolean1, paramInt);
/*    */   }
/*    */ 
/*    */   protected void eventPosted(final SunDropTargetEvent paramSunDropTargetEvent) {
/* 96 */     if (paramSunDropTargetEvent.getID() != 502) {
/* 97 */       Runnable local1 = new Runnable() {
/*    */         public void run() {
/* 99 */           paramSunDropTargetEvent.getDispatcher().unregisterAllEvents();
/*    */         }
/*    */       };
/* 105 */       PeerEvent localPeerEvent = new PeerEvent(paramSunDropTargetEvent.getSource(), local1, 0L);
/* 106 */       SunToolkit.executeOnEventHandlerThread(localPeerEvent);
/*    */     }
/*    */   }
/*    */ 
/*    */   private native Object getData(long paramLong1, long paramLong2);
/*    */ 
/*    */   private native void dropDone(long paramLong, boolean paramBoolean, int paramInt);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.awt.windows.WDropTargetContextPeer
 * JD-Core Version:    0.6.2
 */