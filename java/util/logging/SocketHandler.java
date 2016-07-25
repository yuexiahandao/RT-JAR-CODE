/*     */ package java.util.logging;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.Socket;
/*     */ 
/*     */ public class SocketHandler extends StreamHandler
/*     */ {
/*     */   private Socket sock;
/*     */   private String host;
/*     */   private int port;
/*     */   private String portProperty;
/*     */ 
/*     */   private void configure()
/*     */   {
/*  77 */     LogManager localLogManager = LogManager.getLogManager();
/*  78 */     String str = getClass().getName();
/*     */ 
/*  80 */     setLevel(localLogManager.getLevelProperty(str + ".level", Level.ALL));
/*  81 */     setFilter(localLogManager.getFilterProperty(str + ".filter", null));
/*  82 */     setFormatter(localLogManager.getFormatterProperty(str + ".formatter", new XMLFormatter()));
/*     */     try {
/*  84 */       setEncoding(localLogManager.getStringProperty(str + ".encoding", null));
/*     */     } catch (Exception localException1) {
/*     */       try {
/*  87 */         setEncoding(null);
/*     */       }
/*     */       catch (Exception localException2)
/*     */       {
/*     */       }
/*     */     }
/*  93 */     this.port = localLogManager.getIntProperty(str + ".port", 0);
/*  94 */     this.host = localLogManager.getStringProperty(str + ".host", null);
/*     */   }
/*     */ 
/*     */   public SocketHandler()
/*     */     throws IOException
/*     */   {
/* 108 */     this.sealed = false;
/* 109 */     configure();
/*     */     try
/*     */     {
/* 112 */       connect();
/*     */     } catch (IOException localIOException) {
/* 114 */       System.err.println("SocketHandler: connect failed to " + this.host + ":" + this.port);
/* 115 */       throw localIOException;
/*     */     }
/* 117 */     this.sealed = true;
/*     */   }
/*     */ 
/*     */   public SocketHandler(String paramString, int paramInt)
/*     */     throws IOException
/*     */   {
/* 136 */     this.sealed = false;
/* 137 */     configure();
/* 138 */     this.sealed = true;
/* 139 */     this.port = paramInt;
/* 140 */     this.host = paramString;
/* 141 */     connect();
/*     */   }
/*     */ 
/*     */   private void connect() throws IOException
/*     */   {
/* 146 */     if (this.port == 0) {
/* 147 */       throw new IllegalArgumentException("Bad port: " + this.port);
/*     */     }
/* 149 */     if (this.host == null) {
/* 150 */       throw new IllegalArgumentException("Null host name: " + this.host);
/*     */     }
/*     */ 
/* 154 */     this.sock = new Socket(this.host, this.port);
/* 155 */     OutputStream localOutputStream = this.sock.getOutputStream();
/* 156 */     BufferedOutputStream localBufferedOutputStream = new BufferedOutputStream(localOutputStream);
/* 157 */     setOutputStream(localBufferedOutputStream);
/*     */   }
/*     */ 
/*     */   public synchronized void close()
/*     */     throws SecurityException
/*     */   {
/* 167 */     super.close();
/* 168 */     if (this.sock != null)
/*     */       try {
/* 170 */         this.sock.close();
/*     */       }
/*     */       catch (IOException localIOException)
/*     */       {
/*     */       }
/* 175 */     this.sock = null;
/*     */   }
/*     */ 
/*     */   public synchronized void publish(LogRecord paramLogRecord)
/*     */   {
/* 185 */     if (!isLoggable(paramLogRecord)) {
/* 186 */       return;
/*     */     }
/* 188 */     super.publish(paramLogRecord);
/* 189 */     flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.util.logging.SocketHandler
 * JD-Core Version:    0.6.2
 */