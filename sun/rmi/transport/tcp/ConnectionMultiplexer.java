/*     */ package sun.rmi.transport.tcp;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.rmi.server.LogStream;
/*     */ import java.security.AccessController;
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.transport.Connection;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ final class ConnectionMultiplexer
/*     */ {
/*  49 */   static int logLevel = LogStream.parseLevel(getLogLevel());
/*     */ 
/*  57 */   static final Log multiplexLog = Log.getLog("sun.rmi.transport.tcp.multiplex", "multiplex", logLevel);
/*     */   private static final int OPEN = 225;
/*     */   private static final int CLOSE = 226;
/*     */   private static final int CLOSEACK = 227;
/*     */   private static final int REQUEST = 228;
/*     */   private static final int TRANSMIT = 229;
/*     */   private TCPChannel channel;
/*     */   private InputStream in;
/*     */   private OutputStream out;
/*     */   private boolean orig;
/*     */   private DataInputStream dataIn;
/*     */   private DataOutputStream dataOut;
/*  88 */   private Hashtable<Integer, MultiplexConnectionInfo> connectionTable = new Hashtable(7);
/*     */ 
/*  91 */   private int numConnections = 0;
/*     */   private static final int maxConnections = 256;
/*  97 */   private int lastID = 4097;
/*     */ 
/* 100 */   private boolean alive = true;
/*     */ 
/*     */   private static String getLogLevel()
/*     */   {
/*  52 */     return (String)AccessController.doPrivileged(new GetPropertyAction("sun.rmi.transport.tcp.multiplex.logLevel"));
/*     */   }
/*     */ 
/*     */   public ConnectionMultiplexer(TCPChannel paramTCPChannel, InputStream paramInputStream, OutputStream paramOutputStream, boolean paramBoolean)
/*     */   {
/* 118 */     this.channel = paramTCPChannel;
/* 119 */     this.in = paramInputStream;
/* 120 */     this.out = paramOutputStream;
/* 121 */     this.orig = paramBoolean;
/*     */ 
/* 123 */     this.dataIn = new DataInputStream(paramInputStream);
/* 124 */     this.dataOut = new DataOutputStream(paramOutputStream);
/*     */   }
/*     */ 
/*     */   public void run()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 139 */       int i = this.dataIn.readUnsignedByte();
/*     */       int j;
/*     */       MultiplexConnectionInfo localMultiplexConnectionInfo;
/*     */       int k;
/* 140 */       switch (i)
/*     */       {
/*     */       case 225:
/* 144 */         j = this.dataIn.readUnsignedShort();
/*     */ 
/* 146 */         if (multiplexLog.isLoggable(Log.VERBOSE)) {
/* 147 */           multiplexLog.log(Log.VERBOSE, "operation  OPEN " + j);
/*     */         }
/*     */ 
/* 150 */         localMultiplexConnectionInfo = (MultiplexConnectionInfo)this.connectionTable.get(Integer.valueOf(j));
/* 151 */         if (localMultiplexConnectionInfo != null) {
/* 152 */           throw new IOException("OPEN: Connection ID already exists");
/*     */         }
/* 154 */         localMultiplexConnectionInfo = new MultiplexConnectionInfo(j);
/* 155 */         localMultiplexConnectionInfo.in = new MultiplexInputStream(this, localMultiplexConnectionInfo, 2048);
/* 156 */         localMultiplexConnectionInfo.out = new MultiplexOutputStream(this, localMultiplexConnectionInfo, 2048);
/* 157 */         synchronized (this.connectionTable) {
/* 158 */           this.connectionTable.put(Integer.valueOf(j), localMultiplexConnectionInfo);
/* 159 */           this.numConnections += 1;
/*     */         }
/*     */ 
/* 162 */         ??? = new TCPConnection(this.channel, localMultiplexConnectionInfo.in, localMultiplexConnectionInfo.out);
/* 163 */         this.channel.acceptMultiplexConnection((Connection)???);
/* 164 */         break;
/*     */       case 226:
/* 168 */         j = this.dataIn.readUnsignedShort();
/*     */ 
/* 170 */         if (multiplexLog.isLoggable(Log.VERBOSE)) {
/* 171 */           multiplexLog.log(Log.VERBOSE, "operation  CLOSE " + j);
/*     */         }
/*     */ 
/* 174 */         localMultiplexConnectionInfo = (MultiplexConnectionInfo)this.connectionTable.get(Integer.valueOf(j));
/* 175 */         if (localMultiplexConnectionInfo == null) {
/* 176 */           throw new IOException("CLOSE: Invalid connection ID");
/*     */         }
/* 178 */         localMultiplexConnectionInfo.in.disconnect();
/* 179 */         localMultiplexConnectionInfo.out.disconnect();
/* 180 */         if (!localMultiplexConnectionInfo.closed)
/* 181 */           sendCloseAck(localMultiplexConnectionInfo);
/* 182 */         synchronized (this.connectionTable) {
/* 183 */           this.connectionTable.remove(Integer.valueOf(j));
/* 184 */           this.numConnections -= 1;
/*     */         }
/* 186 */         break;
/*     */       case 227:
/* 190 */         j = this.dataIn.readUnsignedShort();
/*     */ 
/* 192 */         if (multiplexLog.isLoggable(Log.VERBOSE)) {
/* 193 */           multiplexLog.log(Log.VERBOSE, "operation  CLOSEACK " + j);
/*     */         }
/*     */ 
/* 197 */         localMultiplexConnectionInfo = (MultiplexConnectionInfo)this.connectionTable.get(Integer.valueOf(j));
/* 198 */         if (localMultiplexConnectionInfo == null) {
/* 199 */           throw new IOException("CLOSEACK: Invalid connection ID");
/*     */         }
/* 201 */         if (!localMultiplexConnectionInfo.closed) {
/* 202 */           throw new IOException("CLOSEACK: Connection not closed");
/*     */         }
/* 204 */         localMultiplexConnectionInfo.in.disconnect();
/* 205 */         localMultiplexConnectionInfo.out.disconnect();
/* 206 */         synchronized (this.connectionTable) {
/* 207 */           this.connectionTable.remove(Integer.valueOf(j));
/* 208 */           this.numConnections -= 1;
/*     */         }
/* 210 */         break;
/*     */       case 228:
/* 214 */         j = this.dataIn.readUnsignedShort();
/* 215 */         localMultiplexConnectionInfo = (MultiplexConnectionInfo)this.connectionTable.get(Integer.valueOf(j));
/* 216 */         if (localMultiplexConnectionInfo == null) {
/* 217 */           throw new IOException("REQUEST: Invalid connection ID");
/*     */         }
/* 219 */         k = this.dataIn.readInt();
/*     */ 
/* 221 */         if (multiplexLog.isLoggable(Log.VERBOSE)) {
/* 222 */           multiplexLog.log(Log.VERBOSE, "operation  REQUEST " + j + ": " + k);
/*     */         }
/*     */ 
/* 226 */         localMultiplexConnectionInfo.out.request(k);
/* 227 */         break;
/*     */       case 229:
/* 231 */         j = this.dataIn.readUnsignedShort();
/* 232 */         localMultiplexConnectionInfo = (MultiplexConnectionInfo)this.connectionTable.get(Integer.valueOf(j));
/* 233 */         if (localMultiplexConnectionInfo == null)
/* 234 */           throw new IOException("SEND: Invalid connection ID");
/* 235 */         k = this.dataIn.readInt();
/*     */ 
/* 237 */         if (multiplexLog.isLoggable(Log.VERBOSE)) {
/* 238 */           multiplexLog.log(Log.VERBOSE, "operation  TRANSMIT " + j + ": " + k);
/*     */         }
/*     */ 
/* 242 */         localMultiplexConnectionInfo.in.receive(k, this.dataIn);
/* 243 */         break;
/*     */       default:
/* 246 */         throw new IOException("Invalid operation: " + Integer.toHexString(i));
/*     */       }
/*     */     }
/*     */     finally
/*     */     {
/* 251 */       shutDown();
/*     */     }
/*     */   }
/*     */ 
/*     */   public synchronized TCPConnection openConnection()
/*     */     throws IOException
/*     */   {
/*     */     int i;
/*     */     do
/*     */     {
/* 266 */       this.lastID = (++this.lastID & 0x7FFF);
/* 267 */       i = this.lastID;
/*     */ 
/* 272 */       if (this.orig)
/* 273 */         i |= 32768; 
/*     */     }
/* 274 */     while (this.connectionTable.get(Integer.valueOf(i)) != null);
/*     */ 
/* 277 */     MultiplexConnectionInfo localMultiplexConnectionInfo = new MultiplexConnectionInfo(i);
/* 278 */     localMultiplexConnectionInfo.in = new MultiplexInputStream(this, localMultiplexConnectionInfo, 2048);
/* 279 */     localMultiplexConnectionInfo.out = new MultiplexOutputStream(this, localMultiplexConnectionInfo, 2048);
/*     */ 
/* 282 */     synchronized (this.connectionTable) {
/* 283 */       if (!this.alive)
/* 284 */         throw new IOException("Multiplexer connection dead");
/* 285 */       if (this.numConnections >= 256) {
/* 286 */         throw new IOException("Cannot exceed 256 simultaneous multiplexed connections");
/*     */       }
/* 288 */       this.connectionTable.put(Integer.valueOf(i), localMultiplexConnectionInfo);
/* 289 */       this.numConnections += 1;
/*     */     }
/*     */ 
/* 293 */     synchronized (this.dataOut) {
/*     */       try {
/* 295 */         this.dataOut.writeByte(225);
/* 296 */         this.dataOut.writeShort(i);
/* 297 */         this.dataOut.flush();
/*     */       } catch (IOException localIOException) {
/* 299 */         multiplexLog.log(Log.BRIEF, "exception: ", localIOException);
/*     */ 
/* 301 */         shutDown();
/* 302 */         throw localIOException;
/*     */       }
/*     */     }
/*     */ 
/* 306 */     return new TCPConnection(this.channel, localMultiplexConnectionInfo.in, localMultiplexConnectionInfo.out);
/*     */   }
/*     */ 
/*     */   public void shutDown()
/*     */   {
/* 315 */     synchronized (this.connectionTable)
/*     */     {
/* 317 */       if (!this.alive)
/* 318 */         return;
/* 319 */       this.alive = false;
/*     */ 
/* 321 */       Enumeration localEnumeration = this.connectionTable.elements();
/*     */ 
/* 323 */       while (localEnumeration.hasMoreElements()) {
/* 324 */         MultiplexConnectionInfo localMultiplexConnectionInfo = (MultiplexConnectionInfo)localEnumeration.nextElement();
/* 325 */         localMultiplexConnectionInfo.in.disconnect();
/* 326 */         localMultiplexConnectionInfo.out.disconnect();
/*     */       }
/* 328 */       this.connectionTable.clear();
/* 329 */       this.numConnections = 0;
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 334 */       this.in.close();
/*     */     } catch (IOException localIOException1) {
/*     */     }
/*     */     try {
/* 338 */       this.out.close();
/*     */     }
/*     */     catch (IOException localIOException2)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   void sendRequest(MultiplexConnectionInfo paramMultiplexConnectionInfo, int paramInt)
/*     */     throws IOException
/*     */   {
/* 350 */     synchronized (this.dataOut) {
/* 351 */       if ((this.alive) && (!paramMultiplexConnectionInfo.closed))
/*     */         try {
/* 353 */           this.dataOut.writeByte(228);
/* 354 */           this.dataOut.writeShort(paramMultiplexConnectionInfo.id);
/* 355 */           this.dataOut.writeInt(paramInt);
/* 356 */           this.dataOut.flush();
/*     */         } catch (IOException localIOException) {
/* 358 */           multiplexLog.log(Log.BRIEF, "exception: ", localIOException);
/*     */ 
/* 360 */           shutDown();
/* 361 */           throw localIOException;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void sendTransmit(MultiplexConnectionInfo paramMultiplexConnectionInfo, byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 376 */     synchronized (this.dataOut) {
/* 377 */       if ((this.alive) && (!paramMultiplexConnectionInfo.closed))
/*     */         try {
/* 379 */           this.dataOut.writeByte(229);
/* 380 */           this.dataOut.writeShort(paramMultiplexConnectionInfo.id);
/* 381 */           this.dataOut.writeInt(paramInt2);
/* 382 */           this.dataOut.write(paramArrayOfByte, paramInt1, paramInt2);
/* 383 */           this.dataOut.flush();
/*     */         } catch (IOException localIOException) {
/* 385 */           multiplexLog.log(Log.BRIEF, "exception: ", localIOException);
/*     */ 
/* 387 */           shutDown();
/* 388 */           throw localIOException;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void sendClose(MultiplexConnectionInfo paramMultiplexConnectionInfo)
/*     */     throws IOException
/*     */   {
/* 399 */     paramMultiplexConnectionInfo.out.disconnect();
/* 400 */     synchronized (this.dataOut) {
/* 401 */       if ((this.alive) && (!paramMultiplexConnectionInfo.closed))
/*     */         try {
/* 403 */           this.dataOut.writeByte(226);
/* 404 */           this.dataOut.writeShort(paramMultiplexConnectionInfo.id);
/* 405 */           this.dataOut.flush();
/* 406 */           paramMultiplexConnectionInfo.closed = true;
/*     */         } catch (IOException localIOException) {
/* 408 */           multiplexLog.log(Log.BRIEF, "exception: ", localIOException);
/*     */ 
/* 410 */           shutDown();
/* 411 */           throw localIOException;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   void sendCloseAck(MultiplexConnectionInfo paramMultiplexConnectionInfo)
/*     */     throws IOException
/*     */   {
/* 422 */     synchronized (this.dataOut) {
/* 423 */       if ((this.alive) && (!paramMultiplexConnectionInfo.closed))
/*     */         try {
/* 425 */           this.dataOut.writeByte(227);
/* 426 */           this.dataOut.writeShort(paramMultiplexConnectionInfo.id);
/* 427 */           this.dataOut.flush();
/* 428 */           paramMultiplexConnectionInfo.closed = true;
/*     */         } catch (IOException localIOException) {
/* 430 */           multiplexLog.log(Log.BRIEF, "exception: ", localIOException);
/*     */ 
/* 432 */           shutDown();
/* 433 */           throw localIOException;
/*     */         }
/*     */     }
/*     */   }
/*     */ 
/*     */   protected void finalize()
/*     */     throws Throwable
/*     */   {
/* 443 */     super.finalize();
/* 444 */     shutDown();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.tcp.ConnectionMultiplexer
 * JD-Core Version:    0.6.2
 */