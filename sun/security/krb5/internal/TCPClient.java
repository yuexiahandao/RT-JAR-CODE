/*     */ package sun.security.krb5.internal;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetSocketAddress;
/*     */ import java.net.Socket;
/*     */ import sun.misc.IOUtils;
/*     */ 
/*     */ class TCPClient extends NetClient
/*     */ {
/*     */   private Socket tcpSocket;
/*     */   private BufferedOutputStream out;
/*     */   private BufferedInputStream in;
/*     */ 
/*     */   TCPClient(String paramString, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/*  64 */     this.tcpSocket = new Socket();
/*  65 */     this.tcpSocket.connect(new InetSocketAddress(paramString, paramInt1), paramInt2);
/*  66 */     this.out = new BufferedOutputStream(this.tcpSocket.getOutputStream());
/*  67 */     this.in = new BufferedInputStream(this.tcpSocket.getInputStream());
/*  68 */     this.tcpSocket.setSoTimeout(paramInt2);
/*     */   }
/*     */ 
/*     */   public void send(byte[] paramArrayOfByte) throws IOException
/*     */   {
/*  73 */     byte[] arrayOfByte = new byte[4];
/*  74 */     intToNetworkByteOrder(paramArrayOfByte.length, arrayOfByte, 0, 4);
/*  75 */     this.out.write(arrayOfByte);
/*     */ 
/*  77 */     this.out.write(paramArrayOfByte);
/*  78 */     this.out.flush();
/*     */   }
/*     */ 
/*     */   public byte[] receive() throws IOException
/*     */   {
/*  83 */     byte[] arrayOfByte = new byte[4];
/*  84 */     int i = readFully(arrayOfByte, 4);
/*     */ 
/*  86 */     if (i != 4) {
/*  87 */       if (Krb5.DEBUG) {
/*  88 */         System.out.println(">>>DEBUG: TCPClient could not read length field");
/*     */       }
/*     */ 
/*  91 */       return null;
/*     */     }
/*     */ 
/*  94 */     int j = networkByteOrderToInt(arrayOfByte, 0, 4);
/*  95 */     if (Krb5.DEBUG) {
/*  96 */       System.out.println(">>>DEBUG: TCPClient reading " + j + " bytes");
/*     */     }
/*     */ 
/*  99 */     if (j <= 0) {
/* 100 */       if (Krb5.DEBUG) {
/* 101 */         System.out.println(">>>DEBUG: TCPClient zero or negative length field: " + j);
/*     */       }
/*     */ 
/* 104 */       return null;
/*     */     }
/*     */     try
/*     */     {
/* 108 */       return IOUtils.readFully(this.in, j, true);
/*     */     } catch (IOException localIOException) {
/* 110 */       if (Krb5.DEBUG) {
/* 111 */         System.out.println(">>>DEBUG: TCPClient could not read complete packet (" + j + "/" + i + ")");
/*     */       }
/*     */     }
/*     */ 
/* 115 */     return null;
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 121 */     this.tcpSocket.close();
/*     */   }
/*     */ 
/*     */   private int readFully(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 129 */     int j = 0;
/*     */ 
/* 131 */     while (paramInt > 0) {
/* 132 */       int i = this.in.read(paramArrayOfByte, j, paramInt);
/*     */ 
/* 134 */       if (i == -1) {
/* 135 */         return j == 0 ? -1 : j;
/*     */       }
/* 137 */       j += i;
/* 138 */       paramInt -= i;
/*     */     }
/* 140 */     return j;
/*     */   }
/*     */ 
/*     */   private static int networkByteOrderToInt(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/* 148 */     if (paramInt2 > 4) {
/* 149 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */ 
/* 153 */     int i = 0;
/*     */ 
/* 155 */     for (int j = 0; j < paramInt2; j++) {
/* 156 */       i <<= 8;
/* 157 */       i |= paramArrayOfByte[(paramInt1 + j)] & 0xFF;
/*     */     }
/* 159 */     return i;
/*     */   }
/*     */ 
/*     */   private static void intToNetworkByteOrder(int paramInt1, byte[] paramArrayOfByte, int paramInt2, int paramInt3)
/*     */   {
/* 168 */     if (paramInt3 > 4) {
/* 169 */       throw new IllegalArgumentException("Cannot handle more than 4 bytes");
/*     */     }
/*     */ 
/* 173 */     for (int i = paramInt3 - 1; i >= 0; i--) {
/* 174 */       paramArrayOfByte[(paramInt2 + i)] = ((byte)(paramInt1 & 0xFF));
/* 175 */       paramInt1 >>>= 8;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.krb5.internal.TCPClient
 * JD-Core Version:    0.6.2
 */