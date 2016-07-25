/*     */ package sun.management.jmxremote;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.net.InetAddress;
/*     */ import java.net.NetworkInterface;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.rmi.server.RMIServerSocketFactory;
/*     */ import java.util.Enumeration;
/*     */ 
/*     */ public final class LocalRMIServerSocketFactory
/*     */   implements RMIServerSocketFactory
/*     */ {
/*     */   public ServerSocket createServerSocket(int paramInt)
/*     */     throws IOException
/*     */   {
/*  49 */     return new ServerSocket(paramInt)
/*     */     {
/*     */       public Socket accept() throws IOException {
/*  52 */         Socket localSocket = super.accept();
/*  53 */         InetAddress localInetAddress1 = localSocket.getInetAddress();
/*     */         Object localObject;
/*  59 */         if (localInetAddress1 == null)
/*     */         {
/*  64 */           localObject = "";
/*  65 */           if (localSocket.isClosed())
/*  66 */             localObject = " Socket is closed.";
/*  67 */           else if (!localSocket.isConnected())
/*  68 */             localObject = " Socket is not connected";
/*     */           try
/*     */           {
/*  71 */             localSocket.close();
/*     */           }
/*     */           catch (Exception localException)
/*     */           {
/*     */           }
/*  76 */           throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported. Couldn't determine client address." + (String)localObject);
/*     */         }
/*     */ 
/*  79 */         if (localInetAddress1.isLoopbackAddress())
/*     */         {
/*  81 */           return localSocket;
/*     */         }
/*     */ 
/*     */         try
/*     */         {
/*  86 */           localObject = NetworkInterface.getNetworkInterfaces();
/*     */         } catch (SocketException localSocketException) {
/*     */           try {
/*  89 */             localSocket.close();
/*     */           }
/*     */           catch (IOException localIOException2) {
/*     */           }
/*  93 */           throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported.", localSocketException);
/*     */         }
/*     */ 
/*  98 */         while (((Enumeration)localObject).hasMoreElements()) {
/*  99 */           NetworkInterface localNetworkInterface = (NetworkInterface)((Enumeration)localObject).nextElement();
/* 100 */           Enumeration localEnumeration = localNetworkInterface.getInetAddresses();
/* 101 */           while (localEnumeration.hasMoreElements()) {
/* 102 */             InetAddress localInetAddress2 = (InetAddress)localEnumeration.nextElement();
/* 103 */             if (localInetAddress2.equals(localInetAddress1)) {
/* 104 */               return localSocket;
/*     */             }
/*     */           }
/*     */         }
/*     */         try
/*     */         {
/* 110 */           localSocket.close();
/*     */         }
/*     */         catch (IOException localIOException1) {
/*     */         }
/* 114 */         throw new IOException("The server sockets created using the LocalRMIServerSocketFactory only accept connections from clients running on the host where the RMI remote objects have been exported.");
/*     */       }
/*     */     };
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 125 */     return paramObject instanceof LocalRMIServerSocketFactory;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 133 */     return getClass().hashCode();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.management.jmxremote.LocalRMIServerSocketFactory
 * JD-Core Version:    0.6.2
 */