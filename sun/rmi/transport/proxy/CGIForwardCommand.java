/*     */ package sun.rmi.transport.proxy;
/*     */ 
/*     */ import java.io.DataInputStream;
/*     */ import java.io.DataOutputStream;
/*     */ import java.io.EOFException;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ import java.net.InetAddress;
/*     */ import java.net.Socket;
/*     */ 
/*     */ final class CGIForwardCommand
/*     */   implements CGICommandHandler
/*     */ {
/*     */   public String getName()
/*     */   {
/* 211 */     return "forward";
/*     */   }
/*     */ 
/*     */   private String getLine(DataInputStream paramDataInputStream) throws IOException
/*     */   {
/* 216 */     return paramDataInputStream.readLine();
/*     */   }
/*     */ 
/*     */   public void execute(String paramString) throws CGIClientException, CGIServerException
/*     */   {
/* 221 */     if (!CGIHandler.RequestMethod.equals("POST"))
/* 222 */       throw new CGIClientException("can only forward POST requests");
/*     */     int i;
/*     */     try
/*     */     {
/* 226 */       i = Integer.parseInt(paramString);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/* 228 */       throw new CGIClientException("invalid port number.");
/*     */     }
/* 230 */     if ((i <= 0) || (i > 65535))
/* 231 */       throw new CGIClientException("invalid port: " + i);
/* 232 */     if (i < 1024) {
/* 233 */       throw new CGIClientException("permission denied for port: " + i);
/*     */     }
/*     */ 
/*     */     Socket localSocket;
/*     */     try
/*     */     {
/* 239 */       localSocket = new Socket(InetAddress.getLocalHost(), i);
/*     */     } catch (IOException localIOException1) {
/* 241 */       throw new CGIServerException("could not connect to local port");
/*     */     }
/*     */ 
/* 247 */     DataInputStream localDataInputStream1 = new DataInputStream(System.in);
/* 248 */     byte[] arrayOfByte = new byte[CGIHandler.ContentLength];
/*     */     try {
/* 250 */       localDataInputStream1.readFully(arrayOfByte);
/*     */     } catch (EOFException localEOFException1) {
/* 252 */       throw new CGIClientException("unexpected EOF reading request body");
/*     */     } catch (IOException localIOException2) {
/* 254 */       throw new CGIClientException("error reading request body");
/*     */     }
/*     */ 
/*     */     try
/*     */     {
/* 261 */       DataOutputStream localDataOutputStream = new DataOutputStream(localSocket.getOutputStream());
/*     */ 
/* 263 */       localDataOutputStream.writeBytes("POST / HTTP/1.0\r\n");
/* 264 */       localDataOutputStream.writeBytes("Content-length: " + CGIHandler.ContentLength + "\r\n\r\n");
/*     */ 
/* 266 */       localDataOutputStream.write(arrayOfByte);
/* 267 */       localDataOutputStream.flush();
/*     */     } catch (IOException localIOException3) {
/* 269 */       throw new CGIServerException("error writing to server");
/*     */     }
/*     */ 
/*     */     DataInputStream localDataInputStream2;
/*     */     try
/*     */     {
/* 277 */       localDataInputStream2 = new DataInputStream(localSocket.getInputStream());
/*     */     } catch (IOException localIOException4) {
/* 279 */       throw new CGIServerException("error reading from server"); } String str1 = "Content-length:".toLowerCase();
/* 282 */     int j = 0;
/*     */ 
/* 284 */     int k = -1;
/*     */     String str2;
/*     */     do { try { str2 = getLine(localDataInputStream2);
/*     */       } catch (IOException localIOException5) {
/* 289 */         throw new CGIServerException("error reading from server");
/*     */       }
/* 291 */       if (str2 == null) {
/* 292 */         throw new CGIServerException("unexpected EOF reading server response");
/*     */       }
/*     */ 
/* 295 */       if (str2.toLowerCase().startsWith(str1)) {
/* 296 */         if (j != 0) {
/* 297 */           throw new CGIServerException("Multiple Content-length entries found.");
/*     */         }
/*     */ 
/* 300 */         k = Integer.parseInt(str2.substring(str1.length()).trim());
/*     */ 
/* 302 */         j = 1;
/*     */       }
/*     */     }
/*     */ 
/* 306 */     while ((str2.length() != 0) && (str2.charAt(0) != '\r') && (str2.charAt(0) != '\n'));
/*     */ 
/* 308 */     if ((j == 0) || (k < 0)) {
/* 309 */       throw new CGIServerException("missing or invalid content length in server response");
/*     */     }
/* 311 */     arrayOfByte = new byte[k];
/*     */     try {
/* 313 */       localDataInputStream2.readFully(arrayOfByte);
/*     */     } catch (EOFException localEOFException2) {
/* 315 */       throw new CGIServerException("unexpected EOF reading server response");
/*     */     }
/*     */     catch (IOException localIOException6) {
/* 318 */       throw new CGIServerException("error reading from server");
/*     */     }
/*     */ 
/* 324 */     System.out.println("Status: 200 OK");
/* 325 */     System.out.println("Content-type: application/octet-stream");
/* 326 */     System.out.println("");
/*     */     try {
/* 328 */       System.out.write(arrayOfByte);
/*     */     } catch (IOException localIOException7) {
/* 330 */       throw new CGIServerException("error writing response");
/*     */     }
/* 332 */     System.out.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.rmi.transport.proxy.CGIForwardCommand
 * JD-Core Version:    0.6.2
 */