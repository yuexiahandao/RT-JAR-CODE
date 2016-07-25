/*     */ package com.sun.corba.se.impl.legacy.connection;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.spi.ior.IOR;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPAddress;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfile;
/*     */ import com.sun.corba.se.spi.ior.iiop.IIOPProfileTemplate;
/*     */ import com.sun.corba.se.spi.legacy.connection.GetEndPointInfoAgainException;
/*     */ import com.sun.corba.se.spi.legacy.connection.ORBSocketFactory;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.SocketInfo;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ 
/*     */ public class DefaultSocketFactory
/*     */   implements ORBSocketFactory
/*     */ {
/*     */   private com.sun.corba.se.spi.orb.ORB orb;
/*  56 */   private static ORBUtilSystemException wrapper = ORBUtilSystemException.get("rpc.transport");
/*     */ 
/*     */   public void setORB(com.sun.corba.se.spi.orb.ORB paramORB)
/*     */   {
/*  65 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/*  72 */     if (!paramString.equals("IIOP_CLEAR_TEXT"))
/*  73 */       throw wrapper.defaultCreateServerSocketGivenNonIiopClearText(paramString);
/*     */     ServerSocket localServerSocket;
/*  78 */     if (this.orb.getORBData().acceptorSocketType().equals("SocketChannel")) {
/*  79 */       ServerSocketChannel localServerSocketChannel = ServerSocketChannel.open();
/*     */ 
/*  81 */       localServerSocket = localServerSocketChannel.socket();
/*     */     } else {
/*  83 */       localServerSocket = new ServerSocket();
/*     */     }
/*  85 */     localServerSocket.bind(new InetSocketAddress(paramInt));
/*  86 */     return localServerSocket;
/*     */   }
/*     */ 
/*     */   public SocketInfo getEndPointInfo(org.omg.CORBA.ORB paramORB, IOR paramIOR, SocketInfo paramSocketInfo)
/*     */   {
/*  93 */     IIOPProfileTemplate localIIOPProfileTemplate = (IIOPProfileTemplate)paramIOR.getProfile().getTaggedProfileTemplate();
/*     */ 
/*  95 */     IIOPAddress localIIOPAddress = localIIOPProfileTemplate.getPrimaryAddress();
/*     */ 
/*  97 */     return new EndPointInfoImpl("IIOP_CLEAR_TEXT", localIIOPAddress.getPort(), localIIOPAddress.getHost().toLowerCase());
/*     */   }
/*     */ 
/*     */   public Socket createSocket(SocketInfo paramSocketInfo)
/*     */     throws IOException, GetEndPointInfoAgainException
/*     */   {
/*     */     Socket localSocket;
/* 109 */     if (this.orb.getORBData().acceptorSocketType().equals("SocketChannel")) {
/* 110 */       InetSocketAddress localInetSocketAddress = new InetSocketAddress(paramSocketInfo.getHost(), paramSocketInfo.getPort());
/*     */ 
/* 113 */       SocketChannel localSocketChannel = SocketChannel.open(localInetSocketAddress);
/* 114 */       localSocket = localSocketChannel.socket();
/*     */     } else {
/* 116 */       localSocket = new Socket(paramSocketInfo.getHost(), paramSocketInfo.getPort());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 122 */       localSocket.setTcpNoDelay(true);
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 126 */     return localSocket;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.legacy.connection.DefaultSocketFactory
 * JD-Core Version:    0.6.2
 */