/*     */ package com.sun.corba.se.impl.transport;
/*     */ 
/*     */ import com.sun.corba.se.pept.transport.Acceptor;
/*     */ import com.sun.corba.se.spi.orb.ORB;
/*     */ import com.sun.corba.se.spi.orb.ORBData;
/*     */ import com.sun.corba.se.spi.transport.ORBSocketFactory;
/*     */ import java.io.IOException;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ 
/*     */ public class DefaultSocketFactoryImpl
/*     */   implements ORBSocketFactory
/*     */ {
/*     */   private ORB orb;
/*  51 */   private static final boolean keepAlive = ((Boolean)AccessController.doPrivileged(new PrivilegedAction()
/*     */   {
/*     */     public Boolean run()
/*     */     {
/*  55 */       String str = System.getProperty("com.sun.CORBA.transport.enableTcpKeepAlive");
/*     */ 
/*  57 */       if (str != null) {
/*  58 */         return new Boolean(!"false".equalsIgnoreCase(str));
/*     */       }
/*  60 */       return Boolean.FALSE;
/*     */     }
/*     */   })).booleanValue();
/*     */ 
/*     */   public void setORB(ORB paramORB)
/*     */   {
/*  67 */     this.orb = paramORB;
/*     */   }
/*     */ 
/*     */   public ServerSocket createServerSocket(String paramString, InetSocketAddress paramInetSocketAddress)
/*     */     throws IOException
/*     */   {
/*  74 */     ServerSocketChannel localServerSocketChannel = null;
/*  75 */     ServerSocket localServerSocket = null;
/*     */ 
/*  77 */     if (this.orb.getORBData().acceptorSocketType().equals("SocketChannel")) {
/*  78 */       localServerSocketChannel = ServerSocketChannel.open();
/*  79 */       localServerSocket = localServerSocketChannel.socket();
/*     */     } else {
/*  81 */       localServerSocket = new ServerSocket();
/*     */     }
/*  83 */     localServerSocket.bind(paramInetSocketAddress);
/*  84 */     return localServerSocket;
/*     */   }
/*     */ 
/*     */   public Socket createSocket(String paramString, InetSocketAddress paramInetSocketAddress)
/*     */     throws IOException
/*     */   {
/*  91 */     SocketChannel localSocketChannel = null;
/*  92 */     Socket localSocket = null;
/*     */ 
/*  94 */     if (this.orb.getORBData().connectionSocketType().equals("SocketChannel")) {
/*  95 */       localSocketChannel = SocketChannel.open(paramInetSocketAddress);
/*  96 */       localSocket = localSocketChannel.socket();
/*     */     } else {
/*  98 */       localSocket = new Socket(paramInetSocketAddress.getHostName(), paramInetSocketAddress.getPort());
/*     */     }
/*     */ 
/* 103 */     localSocket.setTcpNoDelay(true);
/*     */ 
/* 105 */     if (keepAlive) {
/* 106 */       localSocket.setKeepAlive(true);
/*     */     }
/* 108 */     return localSocket;
/*     */   }
/*     */ 
/*     */   public void setAcceptedSocketOptions(Acceptor paramAcceptor, ServerSocket paramServerSocket, Socket paramSocket)
/*     */     throws SocketException
/*     */   {
/* 117 */     paramSocket.setTcpNoDelay(true);
/* 118 */     if (keepAlive)
/* 119 */       paramSocket.setKeepAlive(true);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.transport.DefaultSocketFactoryImpl
 * JD-Core Version:    0.6.2
 */