/*     */ package sun.net;
/*     */ 
/*     */ import java.io.BufferedOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.OutputStream;
/*     */ 
/*     */ public class TelnetOutputStream extends BufferedOutputStream
/*     */ {
/*  73 */   boolean stickyCRLF = false;
/*  74 */   boolean seenCR = false;
/*     */ 
/*  76 */   public boolean binaryMode = false;
/*     */ 
/*     */   public TelnetOutputStream(OutputStream paramOutputStream, boolean paramBoolean) {
/*  79 */     super(paramOutputStream);
/*  80 */     this.binaryMode = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void setStickyCRLF(boolean paramBoolean)
/*     */   {
/*  90 */     this.stickyCRLF = paramBoolean;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/*  97 */     if (this.binaryMode) {
/*  98 */       super.write(paramInt);
/*  99 */       return;
/*     */     }
/*     */ 
/* 102 */     if (this.seenCR) {
/* 103 */       if (paramInt != 10)
/* 104 */         super.write(0);
/* 105 */       super.write(paramInt);
/* 106 */       if (paramInt != 13)
/* 107 */         this.seenCR = false;
/*     */     } else {
/* 109 */       if (paramInt == 10) {
/* 110 */         super.write(13);
/* 111 */         super.write(10);
/* 112 */         return;
/*     */       }
/* 114 */       if (paramInt == 13) {
/* 115 */         if (this.stickyCRLF) {
/* 116 */           this.seenCR = true;
/*     */         } else {
/* 118 */           super.write(13);
/* 119 */           paramInt = 0;
/*     */         }
/*     */       }
/* 122 */       super.write(paramInt);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 131 */     if (this.binaryMode)
/* 132 */       super.write(paramArrayOfByte, paramInt1, paramInt2);
/*     */     else
/*     */       while (true)
/*     */       {
/* 136 */         paramInt2--; if (paramInt2 < 0) break;
/* 137 */         write(paramArrayOfByte[(paramInt1++)]);
/*     */       }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.net.TelnetOutputStream
 * JD-Core Version:    0.6.2
 */