/*     */ package sun.management.jdp;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.UnknownHostException;
/*     */ import java.util.UUID;
/*     */ 
/*     */ public final class JdpController
/*     */ {
/* 104 */   private static JDPControllerRunner controller = null;
/*     */ 
/*     */   private static int getInteger(String paramString1, int paramInt, String paramString2)
/*     */     throws JdpException
/*     */   {
/*     */     try
/*     */     {
/* 114 */       return paramString1 == null ? paramInt : Integer.parseInt(paramString1); } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 116 */     throw new JdpException(paramString2);
/*     */   }
/*     */ 
/*     */   private static InetAddress getInetAddress(String paramString1, InetAddress paramInetAddress, String paramString2) throws JdpException
/*     */   {
/*     */     try
/*     */     {
/* 123 */       return paramString1 == null ? paramInetAddress : InetAddress.getByName(paramString1); } catch (UnknownHostException localUnknownHostException) {
/*     */     }
/* 125 */     throw new JdpException(paramString2);
/*     */   }
/*     */ 
/*     */   public static synchronized void startDiscoveryService(InetAddress paramInetAddress, int paramInt, String paramString1, String paramString2)
/*     */     throws IOException, JdpException
/*     */   {
/* 142 */     int i = getInteger(System.getProperty("com.sun.management.jdp.ttl"), 1, "Invalid jdp packet ttl");
/*     */ 
/* 147 */     int j = getInteger(System.getProperty("com.sun.management.jdp.pause"), 5, "Invalid jdp pause");
/*     */ 
/* 152 */     j *= 1000;
/*     */ 
/* 155 */     InetAddress localInetAddress = getInetAddress(System.getProperty("com.sun.management.jdp.source_addr"), null, "Invalid source address provided");
/*     */ 
/* 160 */     UUID localUUID = UUID.randomUUID();
/*     */ 
/* 162 */     JdpJmxPacket localJdpJmxPacket = new JdpJmxPacket(localUUID, paramString2);
/*     */ 
/* 166 */     String str = System.getProperty("sun.java.command");
/* 167 */     if (str != null) {
/* 168 */       localObject = str.split(" ", 2);
/* 169 */       localJdpJmxPacket.setMainClass(localObject[0]);
/*     */     }
/*     */ 
/* 174 */     localJdpJmxPacket.setInstanceName(paramString1);
/*     */ 
/* 176 */     Object localObject = new JdpBroadcaster(paramInetAddress, localInetAddress, paramInt, i);
/*     */ 
/* 179 */     stopDiscoveryService();
/*     */ 
/* 181 */     controller = new JDPControllerRunner((JdpBroadcaster)localObject, localJdpJmxPacket, j, null);
/*     */ 
/* 183 */     Thread localThread = new Thread(controller, "JDP broadcaster");
/* 184 */     localThread.setDaemon(true);
/* 185 */     localThread.start();
/*     */   }
/*     */ 
/*     */   public static synchronized void stopDiscoveryService()
/*     */   {
/* 193 */     if (controller != null) {
/* 194 */       controller.stop();
/* 195 */       controller = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class JDPControllerRunner
/*     */     implements Runnable
/*     */   {
/*     */     private final JdpJmxPacket packet;
/*     */     private final JdpBroadcaster bcast;
/*     */     private final int pause;
/*  66 */     private volatile boolean shutdown = false;
/*     */ 
/*     */     private JDPControllerRunner(JdpBroadcaster paramJdpBroadcaster, JdpJmxPacket paramJdpJmxPacket, int paramInt) {
/*  69 */       this.bcast = paramJdpBroadcaster;
/*  70 */       this.packet = paramJdpJmxPacket;
/*  71 */       this.pause = paramInt;
/*     */     }
/*     */ 
/*     */     public void run()
/*     */     {
/*     */       try {
/*  77 */         while (!this.shutdown) {
/*  78 */           this.bcast.sendPacket(this.packet);
/*     */           try {
/*  80 */             Thread.sleep(this.pause);
/*     */           }
/*     */           catch (InterruptedException localInterruptedException)
/*     */           {
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException1)
/*     */       {
/*     */       }
/*     */ 
/*     */       try
/*     */       {
/*  93 */         stop();
/*  94 */         this.bcast.shutdown();
/*     */       }
/*     */       catch (IOException localIOException2) {
/*     */       }
/*     */     }
/*     */ 
/*     */     public void stop() {
/* 101 */       this.shutdown = true;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jdp.JdpController
 * JD-Core Version:    0.6.2
 */