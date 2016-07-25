/*     */ package com.oracle.net;
/*     */ 
/*     */ import java.io.FileDescriptor;
/*     */ import java.io.IOException;
/*     */ import java.lang.reflect.AccessibleObject;
/*     */ import java.lang.reflect.Constructor;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ import java.net.SocketException;
/*     */ import java.net.SocketImpl;
/*     */ import java.nio.channels.ServerSocketChannel;
/*     */ import java.nio.channels.SocketChannel;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import sun.net.sdp.SdpSupport;
/*     */ import sun.nio.ch.Secrets;
/*     */ 
/*     */ public final class Sdp
/*     */ {
/*     */   private static final Constructor<ServerSocket> serverSocketCtor;
/*     */   private static final Constructor<SocketImpl> socketImplCtor;
/*     */ 
/*     */   private static void setAccessible(AccessibleObject paramAccessibleObject)
/*     */   {
/*  84 */     AccessController.doPrivileged(new PrivilegedAction() {
/*     */       public Void run() {
/*  86 */         this.val$o.setAccessible(true);
/*  87 */         return null;
/*     */       }
/*     */     });
/*     */   }
/*     */ 
/*     */   private static SocketImpl createSocketImpl()
/*     */   {
/*     */     try
/*     */     {
/* 106 */       return (SocketImpl)socketImplCtor.newInstance(new Object[0]);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 108 */       throw new AssertionError(localInstantiationException);
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 110 */       throw new AssertionError(localIllegalAccessException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 112 */       throw new AssertionError(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static Socket openSocket()
/*     */     throws IOException
/*     */   {
/* 128 */     SocketImpl localSocketImpl = createSocketImpl();
/* 129 */     return new SdpSocket(localSocketImpl);
/*     */   }
/*     */ 
/*     */   public static ServerSocket openServerSocket()
/*     */     throws IOException
/*     */   {
/* 145 */     SocketImpl localSocketImpl = createSocketImpl();
/*     */     try {
/* 147 */       return (ServerSocket)serverSocketCtor.newInstance(new Object[] { localSocketImpl });
/*     */     } catch (IllegalAccessException localIllegalAccessException) {
/* 149 */       throw new AssertionError(localIllegalAccessException);
/*     */     } catch (InstantiationException localInstantiationException) {
/* 151 */       throw new AssertionError(localInstantiationException);
/*     */     } catch (InvocationTargetException localInvocationTargetException) {
/* 153 */       Throwable localThrowable = localInvocationTargetException.getCause();
/* 154 */       if ((localThrowable instanceof IOException))
/* 155 */         throw ((IOException)localThrowable);
/* 156 */       if ((localThrowable instanceof RuntimeException))
/* 157 */         throw ((RuntimeException)localThrowable);
/* 158 */       throw new RuntimeException(localInvocationTargetException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public static SocketChannel openSocketChannel()
/*     */     throws IOException
/*     */   {
/* 177 */     FileDescriptor localFileDescriptor = SdpSupport.createSocket();
/* 178 */     return Secrets.newSocketChannel(localFileDescriptor);
/*     */   }
/*     */ 
/*     */   public static ServerSocketChannel openServerSocketChannel()
/*     */     throws IOException
/*     */   {
/* 198 */     FileDescriptor localFileDescriptor = SdpSupport.createSocket();
/* 199 */     return Secrets.newServerSocketChannel(localFileDescriptor);
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*     */     try
/*     */     {
/*  59 */       serverSocketCtor = ServerSocket.class.getDeclaredConstructor(new Class[] { SocketImpl.class });
/*     */ 
/*  61 */       setAccessible(serverSocketCtor);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException1) {
/*  63 */       throw new AssertionError(localNoSuchMethodException1);
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  73 */       Class localClass = Class.forName("java.net.SdpSocketImpl", true, null);
/*  74 */       socketImplCtor = localClass.getDeclaredConstructor(new Class[0]);
/*  75 */       setAccessible(socketImplCtor);
/*     */     } catch (ClassNotFoundException localClassNotFoundException) {
/*  77 */       throw new AssertionError(localClassNotFoundException);
/*     */     } catch (NoSuchMethodException localNoSuchMethodException2) {
/*  79 */       throw new AssertionError(localNoSuchMethodException2);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class SdpSocket extends Socket
/*     */   {
/*     */     SdpSocket(SocketImpl paramSocketImpl)
/*     */       throws SocketException
/*     */     {
/*  97 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.oracle.net.Sdp
 * JD-Core Version:    0.6.2
 */