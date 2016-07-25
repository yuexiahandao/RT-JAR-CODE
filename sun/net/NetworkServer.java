/*     */ package sun.net;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.net.ServerSocket;
/*     */ import java.net.Socket;
/*     */ 
/*     */ public class NetworkServer
/*     */   implements Runnable, Cloneable
/*     */ {
/*  41 */   public Socket clientSocket = null;
/*     */   private Thread serverInstance;
/*     */   private ServerSocket serverSocket;
/*     */   public PrintStream clientOutput;
/*     */   public InputStream clientInput;
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/*  53 */     this.clientSocket.close();
/*  54 */     this.clientSocket = null;
/*  55 */     this.clientInput = null;
/*  56 */     this.clientOutput = null;
/*     */   }
/*     */ 
/*     */   public boolean clientIsOpen()
/*     */   {
/*  61 */     return this.clientSocket != null;
/*     */   }
/*     */ 
/*     */   public final void run() {
/*  65 */     if (this.serverSocket != null) {
/*  66 */       Thread.currentThread().setPriority(10);
/*     */       try
/*     */       {
/*     */         while (true) {
/*  70 */           Socket localSocket = this.serverSocket.accept();
/*     */ 
/*  72 */           NetworkServer localNetworkServer = (NetworkServer)clone();
/*  73 */           localNetworkServer.serverSocket = null;
/*  74 */           localNetworkServer.clientSocket = localSocket;
/*  75 */           new Thread(localNetworkServer).start();
/*     */         }
/*     */       } catch (Exception localException1) { System.out.print("Server failure\n");
/*  78 */         localException1.printStackTrace();
/*     */         try {
/*  80 */           this.serverSocket.close(); } catch (IOException localIOException2) {
/*     */         }
/*  82 */         System.out.print("cs=" + this.serverSocket + "\n");
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/*  89 */       this.clientOutput = new PrintStream(new BufferedOutputStream(this.clientSocket.getOutputStream()), false, "ISO8859_1");
/*     */ 
/*  92 */       this.clientInput = new BufferedInputStream(this.clientSocket.getInputStream());
/*  93 */       serviceRequest();
/*     */     }
/*     */     catch (Exception localException2)
/*     */     {
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 101 */       close();
/*     */     }
/*     */     catch (IOException localIOException1)
/*     */     {
/*     */     }
/*     */   }
/*     */ 
/*     */   public final void startServer(int paramInt) throws IOException {
/* 109 */     this.serverSocket = new ServerSocket(paramInt, 50);
/* 110 */     this.serverInstance = new Thread(this);
/* 111 */     this.serverInstance.start();
/*     */   }
/*     */ 
/*     */   public void serviceRequest()
/*     */     throws IOException
/*     */   {
/* 120 */     byte[] arrayOfByte = new byte[300];
/*     */ 
/* 122 */     this.clientOutput.print("Echo server " + getClass().getName() + "\n");
/* 123 */     this.clientOutput.flush();
/*     */     int i;
/* 124 */     while ((i = this.clientInput.read(arrayOfByte, 0, arrayOfByte.length)) >= 0)
/* 125 */       this.clientOutput.write(arrayOfByte, 0, i);
/*     */   }
/*     */ 
/*     */   public static void main(String[] paramArrayOfString)
/*     */   {
/*     */     try {
/* 131 */       new NetworkServer().startServer(8888);
/*     */     } catch (IOException localIOException) {
/* 133 */       System.out.print("Server failed: " + localIOException + "\n");
/*     */     }
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 142 */       return super.clone();
/*     */     } catch (CloneNotSupportedException localCloneNotSupportedException) {
/*     */     }
/* 145 */     throw new InternalError();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.NetworkServer
 * JD-Core Version:    0.6.2
 */