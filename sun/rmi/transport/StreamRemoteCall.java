/*     */ package sun.rmi.transport;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInput;
/*     */ import java.io.ObjectOutput;
/*     */ import java.io.OutputStream;
/*     */ import java.io.StreamCorruptedException;
/*     */ import java.rmi.MarshalException;
/*     */ import java.rmi.RemoteException;
/*     */ import java.rmi.UnmarshalException;
/*     */ import java.rmi.server.ObjID;
/*     */ import java.rmi.server.RemoteCall;
/*     */ import sun.rmi.runtime.Log;
/*     */ import sun.rmi.server.UnicastRef;
/*     */ import sun.rmi.transport.tcp.TCPEndpoint;
/*     */ 
/*     */ public class StreamRemoteCall
/*     */   implements RemoteCall
/*     */ {
/*  49 */   private ConnectionInputStream in = null;
/*  50 */   private ConnectionOutputStream out = null;
/*     */   private Connection conn;
/*  52 */   private boolean resultStarted = false;
/*  53 */   private Exception serverException = null;
/*     */ 
/*     */   public StreamRemoteCall(Connection paramConnection) {
/*  56 */     this.conn = paramConnection;
/*     */   }
/*     */ 
/*     */   public StreamRemoteCall(Connection paramConnection, ObjID paramObjID, int paramInt, long paramLong) throws RemoteException
/*     */   {
/*     */     try
/*     */     {
/*  63 */       this.conn = paramConnection;
/*  64 */       Transport.transportLog.log(Log.VERBOSE, "write remote call header...");
/*     */ 
/*  69 */       this.conn.getOutputStream().write(80);
/*  70 */       getOutputStream();
/*  71 */       paramObjID.write(this.out);
/*     */ 
/*  73 */       this.out.writeInt(paramInt);
/*  74 */       this.out.writeLong(paramLong);
/*     */     } catch (IOException localIOException) {
/*  76 */       throw new MarshalException("Error marshaling call header", localIOException);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Connection getConnection()
/*     */   {
/*  84 */     return this.conn;
/*     */   }
/*     */ 
/*     */   public ObjectOutput getOutputStream()
/*     */     throws IOException
/*     */   {
/*  92 */     return getOutputStream(false);
/*     */   }
/*     */ 
/*     */   private ObjectOutput getOutputStream(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/*  98 */     if (this.out == null) {
/*  99 */       Transport.transportLog.log(Log.VERBOSE, "getting output stream");
/*     */ 
/* 101 */       this.out = new ConnectionOutputStream(this.conn, paramBoolean);
/*     */     }
/* 103 */     return this.out;
/*     */   }
/*     */ 
/*     */   public void releaseOutputStream()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 112 */       if (this.out != null) {
/*     */         try {
/* 114 */           this.out.flush();
/*     */         } finally {
/* 116 */           this.out.done();
/*     */         }
/*     */       }
/* 119 */       this.conn.releaseOutputStream();
/*     */     } finally {
/* 121 */       this.out = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjectInput getInputStream()
/*     */     throws IOException
/*     */   {
/* 130 */     if (this.in == null) {
/* 131 */       Transport.transportLog.log(Log.VERBOSE, "getting input stream");
/*     */ 
/* 133 */       this.in = new ConnectionInputStream(this.conn.getInputStream());
/*     */     }
/* 135 */     return this.in;
/*     */   }
/*     */ 
/*     */   public void releaseInputStream()
/*     */     throws IOException
/*     */   {
/*     */     try
/*     */     {
/* 148 */       if (this.in != null)
/*     */       {
/*     */         try {
/* 151 */           this.in.done();
/*     */         }
/*     */         catch (RuntimeException localRuntimeException)
/*     */         {
/*     */         }
/* 156 */         this.in.registerRefs();
/*     */ 
/* 161 */         this.in.done(this.conn);
/*     */       }
/* 163 */       this.conn.releaseInputStream();
/*     */     } finally {
/* 165 */       this.in = null;
/*     */     }
/*     */   }
/*     */ 
/*     */   public ObjectOutput getResultStream(boolean paramBoolean)
/*     */     throws IOException
/*     */   {
/* 180 */     if (this.resultStarted) {
/* 181 */       throw new StreamCorruptedException("result already in progress");
/*     */     }
/* 183 */     this.resultStarted = true;
/*     */ 
/* 187 */     DataOutputStream localDataOutputStream = new DataOutputStream(this.conn.getOutputStream());
/* 188 */     localDataOutputStream.writeByte(81);
/* 189 */     getOutputStream(true);
/*     */ 
/* 191 */     if (paramBoolean)
/* 192 */       this.out.writeByte(1);
/*     */     else
/* 194 */       this.out.writeByte(2);
/* 195 */     this.out.writeID();
/* 196 */     return this.out;
/*     */   }
/*     */ 
/*     */   public void executeCall()
/*     */     throws Exception
/*     */   {
/* 207 */     DGCAckHandler localDGCAckHandler = null;
/*     */     int i;
/*     */     try
/*     */     {
/* 209 */       if (this.out != null) {
/* 210 */         localDGCAckHandler = this.out.getDGCAckHandler();
/*     */       }
/* 212 */       releaseOutputStream();
/* 213 */       DataInputStream localDataInputStream = new DataInputStream(this.conn.getInputStream());
/* 214 */       int j = localDataInputStream.readByte();
/* 215 */       if (j != 81) {
/* 216 */         if (Transport.transportLog.isLoggable(Log.BRIEF)) {
/* 217 */           Transport.transportLog.log(Log.BRIEF, "transport return code invalid: " + j);
/*     */         }
/*     */ 
/* 220 */         throw new UnmarshalException("Transport return code invalid");
/*     */       }
/* 222 */       getInputStream();
/* 223 */       i = this.in.readByte();
/* 224 */       this.in.readID();
/*     */     } catch (UnmarshalException localUnmarshalException) {
/* 226 */       throw localUnmarshalException;
/*     */     } catch (IOException localIOException) {
/* 228 */       throw new UnmarshalException("Error unmarshaling return header", localIOException);
/*     */     }
/*     */     finally {
/* 231 */       if (localDGCAckHandler != null) {
/* 232 */         localDGCAckHandler.release();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 237 */     switch (i) {
/*     */     case 1:
/* 239 */       break;
/*     */     case 2:
/*     */       Object localObject1;
/*     */       try
/*     */       {
/* 244 */         localObject1 = this.in.readObject();
/*     */       } catch (Exception localException) {
/* 246 */         throw new UnmarshalException("Error unmarshaling return", localException);
/*     */       }
/*     */ 
/* 251 */       if ((localObject1 instanceof Exception))
/* 252 */         exceptionReceivedFromServer((Exception)localObject1);
/*     */       else {
/* 254 */         throw new UnmarshalException("Return type not Exception");
/*     */       }
/*     */       break;
/*     */     }
/* 258 */     if (Transport.transportLog.isLoggable(Log.BRIEF)) {
/* 259 */       Transport.transportLog.log(Log.BRIEF, "return code invalid: " + i);
/*     */     }
/*     */ 
/* 262 */     throw new UnmarshalException("Return code invalid");
/*     */   }
/*     */ 
/*     */   protected void exceptionReceivedFromServer(Exception paramException)
/*     */     throws Exception
/*     */   {
/* 272 */     this.serverException = paramException;
/*     */ 
/* 274 */     StackTraceElement[] arrayOfStackTraceElement1 = paramException.getStackTrace();
/* 275 */     StackTraceElement[] arrayOfStackTraceElement2 = new Throwable().getStackTrace();
/* 276 */     StackTraceElement[] arrayOfStackTraceElement3 = new StackTraceElement[arrayOfStackTraceElement1.length + arrayOfStackTraceElement2.length];
/*     */ 
/* 278 */     System.arraycopy(arrayOfStackTraceElement1, 0, arrayOfStackTraceElement3, 0, arrayOfStackTraceElement1.length);
/*     */ 
/* 280 */     System.arraycopy(arrayOfStackTraceElement2, 0, arrayOfStackTraceElement3, arrayOfStackTraceElement1.length, arrayOfStackTraceElement2.length);
/*     */ 
/* 282 */     paramException.setStackTrace(arrayOfStackTraceElement3);
/*     */ 
/* 288 */     if (UnicastRef.clientCallLog.isLoggable(Log.BRIEF))
/*     */     {
/* 290 */       TCPEndpoint localTCPEndpoint = (TCPEndpoint)this.conn.getChannel().getEndpoint();
/* 291 */       UnicastRef.clientCallLog.log(Log.BRIEF, "outbound call received exception: [" + localTCPEndpoint.getHost() + ":" + localTCPEndpoint.getPort() + "] exception: ", paramException);
/*     */     }
/*     */ 
/* 296 */     throw paramException;
/*     */   }
/*     */ 
/*     */   public Exception getServerException()
/*     */   {
/* 304 */     return this.serverException;
/*     */   }
/*     */ 
/*     */   public void done()
/*     */     throws IOException
/*     */   {
/* 312 */     releaseInputStream();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.StreamRemoteCall
 * JD-Core Version:    0.6.2
 */