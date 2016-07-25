/*     */ package com.sun.jmx.snmp.daemon;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.io.IOException;
/*     */ import java.net.DatagramPacket;
/*     */ import java.net.DatagramSocket;
/*     */ import java.net.InetAddress;
/*     */ import java.net.SocketException;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ 
/*     */ final class SnmpSocket
/*     */   implements Runnable
/*     */ {
/*  39 */   private DatagramSocket _socket = null;
/*  40 */   private SnmpResponseHandler _dgramHdlr = null;
/*  41 */   private Thread _sockThread = null;
/*  42 */   private byte[] _buffer = null;
/*  43 */   private transient boolean isClosing = false;
/*     */ 
/*  45 */   int _socketPort = 0;
/*  46 */   int responseBufSize = 1024;
/*     */ 
/*     */   public SnmpSocket(SnmpResponseHandler paramSnmpResponseHandler, InetAddress paramInetAddress, int paramInt)
/*     */     throws SocketException
/*     */   {
/*  60 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/*  61 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "constructor", "Creating new SNMP datagram socket");
/*     */     }
/*     */ 
/*  66 */     this._socket = new DatagramSocket(0, paramInetAddress);
/*  67 */     this._socketPort = this._socket.getLocalPort();
/*  68 */     this.responseBufSize = paramInt;
/*  69 */     this._buffer = new byte[this.responseBufSize];
/*  70 */     this._dgramHdlr = paramSnmpResponseHandler;
/*  71 */     this._sockThread = new Thread(this, "SnmpSocket");
/*  72 */     this._sockThread.start();
/*     */   }
/*     */ 
/*     */   public synchronized void sendPacket(byte[] paramArrayOfByte, int paramInt1, InetAddress paramInetAddress, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  88 */     DatagramPacket localDatagramPacket = new DatagramPacket(paramArrayOfByte, paramInt1, paramInetAddress, paramInt2);
/*  89 */     sendPacket(localDatagramPacket);
/*     */   }
/*     */ 
/*     */   public synchronized void sendPacket(DatagramPacket paramDatagramPacket)
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 100 */       if (isValid()) {
/* 101 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 102 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "sendPacket", "Sending DatagramPacket. Length = " + paramDatagramPacket.getLength() + " through socket = " + this._socket.toString());
/*     */         }
/*     */ 
/* 106 */         this._socket.send(paramDatagramPacket);
/*     */       } else {
/* 108 */         throw new IOException("Invalid state of SNMP datagram socket.");
/*     */       }
/*     */     } catch (IOException localIOException) { if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 111 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "sendPacket", "I/O error while sending", localIOException);
/*     */       }
/*     */ 
/* 114 */       throw localIOException;
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized boolean isValid()
/*     */   {
/* 124 */     return (this._socket != null) && (this._sockThread != null) && (this._sockThread.isAlive());
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */   {
/* 132 */     this.isClosing = true;
/*     */ 
/* 134 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 135 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "close", "Closing and destroying the SNMP datagram socket -> " + toString());
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 142 */       DatagramSocket localDatagramSocket = new DatagramSocket(0);
/* 143 */       byte[] arrayOfByte = new byte[1];
/* 144 */       DatagramPacket localDatagramPacket = new DatagramPacket(arrayOfByte, 1, InetAddress.getLocalHost(), this._socketPort);
/* 145 */       localDatagramSocket.send(localDatagramPacket);
/* 146 */       localDatagramSocket.close();
/*     */     }
/*     */     catch (Exception localException)
/*     */     {
/*     */     }
/*     */ 
/* 152 */     if (this._socket != null) {
/* 153 */       this._socket.close();
/* 154 */       this._socket = null;
/*     */     }
/*     */ 
/* 159 */     if ((this._sockThread != null) && (this._sockThread.isAlive())) {
/* 160 */       this._sockThread.interrupt();
/*     */       try
/*     */       {
/* 164 */         this._sockThread.join();
/*     */       }
/*     */       catch (InterruptedException localInterruptedException) {
/*     */       }
/* 168 */       this._sockThread = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void run()
/*     */   {
/* 177 */     Thread.currentThread().setPriority(8);
/*     */     while (true)
/*     */       try
/*     */       {
/* 181 */         DatagramPacket localDatagramPacket = new DatagramPacket(this._buffer, this._buffer.length);
/*     */ 
/* 183 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 184 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "run", "[" + Thread.currentThread().toString() + "]:" + "Blocking for receiving packet");
/*     */         }
/*     */ 
/* 188 */         this._socket.receive(localDatagramPacket);
/*     */ 
/* 192 */         if (this.isClosing) {
/*     */           break;
/*     */         }
/* 195 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 196 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "run", "[" + Thread.currentThread().toString() + "]:" + "Received a packet");
/*     */         }
/*     */ 
/* 200 */         if (localDatagramPacket.getLength() > 0)
/*     */         {
/* 203 */           if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINER)) {
/* 204 */             JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINER, SnmpSocket.class.getName(), "run", "[" + Thread.currentThread().toString() + "]:" + "Received a packet from : " + localDatagramPacket.getAddress().toString() + ", Length = " + localDatagramPacket.getLength());
/*     */           }
/*     */ 
/* 209 */           handleDatagram(localDatagramPacket);
/*     */ 
/* 213 */           if (this.isClosing) {
/*     */             break;
/*     */           }
/*     */         }
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/* 220 */         if (this.isClosing) {
/*     */           break;
/*     */         }
/* 223 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 224 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "run", "IOEXception while receiving datagram", localIOException);
/*     */         }
/*     */ 
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 231 */         if (this.isClosing) {
/*     */           break;
/*     */         }
/* 234 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST))
/* 235 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "run", "Exception in socket thread...", localException);
/*     */       }
/*     */       catch (ThreadDeath localThreadDeath)
/*     */       {
/* 239 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 240 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "run", "Socket Thread DEAD..." + toString(), localThreadDeath);
/*     */         }
/*     */ 
/* 243 */         close();
/* 244 */         throw localThreadDeath;
/*     */       } catch (Error localError) {
/* 246 */         if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 247 */           JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "run", "Got unexpected error", localError);
/*     */         }
/*     */ 
/* 250 */         handleJavaError(localError);
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized void finalize()
/*     */   {
/* 262 */     close();
/*     */   }
/*     */ 
/*     */   private synchronized void handleJavaError(Throwable paramThrowable)
/*     */   {
/* 272 */     if ((paramThrowable instanceof OutOfMemoryError)) {
/* 273 */       if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 274 */         JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "handleJavaError", "OutOfMemory error", paramThrowable);
/*     */       }
/*     */ 
/* 277 */       Thread.currentThread(); Thread.yield();
/* 278 */       return;
/*     */     }
/* 280 */     if (this._socket != null) {
/* 281 */       this._socket.close();
/* 282 */       this._socket = null;
/*     */     }
/*     */ 
/* 285 */     if (JmxProperties.SNMP_ADAPTOR_LOGGER.isLoggable(Level.FINEST)) {
/* 286 */       JmxProperties.SNMP_ADAPTOR_LOGGER.logp(Level.FINEST, SnmpSocket.class.getName(), "handleJavaError", "Global Internal error");
/*     */     }
/*     */ 
/* 289 */     Thread.currentThread(); Thread.yield();
/*     */   }
/*     */ 
/*     */   private synchronized void handleDatagram(DatagramPacket paramDatagramPacket) {
/* 293 */     this._dgramHdlr.processDatagram(paramDatagramPacket);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.daemon.SnmpSocket
 * JD-Core Version:    0.6.2
 */