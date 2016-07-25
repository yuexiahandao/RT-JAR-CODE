/*     */ package sun.net;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.PrintStream;
/*     */ import java.util.Vector;
/*     */ 
/*     */ public class TransferProtocolClient extends NetworkClient
/*     */ {
/*     */   static final boolean debug = false;
/*  47 */   protected Vector serverResponse = new Vector(1);
/*     */   protected int lastReplyCode;
/*     */ 
/*     */   public int readServerResponse()
/*     */     throws IOException
/*     */   {
/*  58 */     StringBuffer localStringBuffer = new StringBuffer(32);
/*     */ 
/*  60 */     int j = -1;
/*     */ 
/*  64 */     this.serverResponse.setSize(0);
/*     */     label76: int k;
/*     */     while (true)
/*     */     {
/*     */       int i;
/*  66 */       if ((i = this.serverInput.read()) != -1) {
/*  67 */         if ((i == 13) && 
/*  68 */           ((i = this.serverInput.read()) != 10)) {
/*  69 */           localStringBuffer.append('\r');
/*     */         }
/*  71 */         localStringBuffer.append((char)i);
/*  72 */         if (i == 10)
/*  73 */           break label76;
/*     */       } else {
/*  75 */         String str = localStringBuffer.toString();
/*  76 */         localStringBuffer.setLength(0);
/*     */ 
/*  81 */         if (str.length() == 0) {
/*  82 */           k = -1;
/*     */         } else {
/*     */           try {
/*  85 */             k = Integer.parseInt(str.substring(0, 3));
/*     */           } catch (NumberFormatException localNumberFormatException) {
/*  87 */             k = -1;
/*     */           }
/*     */           catch (StringIndexOutOfBoundsException localStringIndexOutOfBoundsException) {
/*     */           }
/*  91 */           continue;
/*     */         }
/*     */ 
/*  94 */         this.serverResponse.addElement(str);
/*  95 */         if (j != -1)
/*     */         {
/*  97 */           if ((k == j) && ((str.length() < 4) || (str.charAt(3) != '-')))
/*     */           {
/* 102 */             j = -1;
/* 103 */             break;
/*     */           }
/*     */         } else { if ((str.length() < 4) || (str.charAt(3) != '-')) break;
/* 106 */           j = k;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 113 */     return this.lastReplyCode = k;
/*     */   }
/*     */ 
/*     */   public void sendServer(String paramString)
/*     */   {
/* 118 */     this.serverOutput.print(paramString);
/*     */   }
/*     */ 
/*     */   public String getResponseString()
/*     */   {
/* 126 */     return (String)this.serverResponse.elementAt(0);
/*     */   }
/*     */ 
/*     */   public Vector getResponseStrings()
/*     */   {
/* 131 */     return this.serverResponse;
/*     */   }
/*     */ 
/*     */   public TransferProtocolClient(String paramString, int paramInt) throws IOException
/*     */   {
/* 136 */     super(paramString, paramInt);
/*     */   }
/*     */ 
/*     */   public TransferProtocolClient()
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.TransferProtocolClient
 * JD-Core Version:    0.6.2
 */