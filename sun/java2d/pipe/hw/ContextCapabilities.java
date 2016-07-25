/*     */ package sun.java2d.pipe.hw;
/*     */ 
/*     */ public class ContextCapabilities
/*     */ {
/*     */   public static final int CAPS_EMPTY = 0;
/*     */   public static final int CAPS_RT_PLAIN_ALPHA = 2;
/*     */   public static final int CAPS_RT_TEXTURE_ALPHA = 4;
/*     */   public static final int CAPS_RT_TEXTURE_OPAQUE = 8;
/*     */   public static final int CAPS_MULTITEXTURE = 16;
/*     */   public static final int CAPS_TEXNONPOW2 = 32;
/*     */   public static final int CAPS_TEXNONSQUARE = 64;
/*     */   public static final int CAPS_PS20 = 128;
/*     */   public static final int CAPS_PS30 = 256;
/*     */   protected static final int FIRST_PRIVATE_CAP = 65536;
/*     */   protected final int caps;
/*     */   protected final String adapterId;
/*     */ 
/*     */   protected ContextCapabilities(int paramInt, String paramString)
/*     */   {
/*  71 */     this.caps = paramInt;
/*  72 */     this.adapterId = (paramString != null ? paramString : "unknown adapter");
/*     */   }
/*     */ 
/*     */   public String getAdapterId()
/*     */   {
/*  81 */     return this.adapterId;
/*     */   }
/*     */ 
/*     */   public int getCaps()
/*     */   {
/*  90 */     return this.caps;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  95 */     StringBuffer localStringBuffer = new StringBuffer("ContextCapabilities: adapter=" + this.adapterId + ", caps=");
/*     */ 
/*  98 */     if (this.caps == 0) {
/*  99 */       localStringBuffer.append("CAPS_EMPTY");
/*     */     } else {
/* 101 */       if ((this.caps & 0x2) != 0) {
/* 102 */         localStringBuffer.append("CAPS_RT_PLAIN_ALPHA|");
/*     */       }
/* 104 */       if ((this.caps & 0x4) != 0) {
/* 105 */         localStringBuffer.append("CAPS_RT_TEXTURE_ALPHA|");
/*     */       }
/* 107 */       if ((this.caps & 0x8) != 0) {
/* 108 */         localStringBuffer.append("CAPS_RT_TEXTURE_OPAQUE|");
/*     */       }
/* 110 */       if ((this.caps & 0x10) != 0) {
/* 111 */         localStringBuffer.append("CAPS_MULTITEXTURE|");
/*     */       }
/* 113 */       if ((this.caps & 0x20) != 0) {
/* 114 */         localStringBuffer.append("CAPS_TEXNONPOW2|");
/*     */       }
/* 116 */       if ((this.caps & 0x40) != 0) {
/* 117 */         localStringBuffer.append("CAPS_TEXNONSQUARE|");
/*     */       }
/* 119 */       if ((this.caps & 0x80) != 0) {
/* 120 */         localStringBuffer.append("CAPS_PS20|");
/*     */       }
/* 122 */       if ((this.caps & 0x100) != 0) {
/* 123 */         localStringBuffer.append("CAPS_PS30|");
/*     */       }
/*     */     }
/* 126 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.java2d.pipe.hw.ContextCapabilities
 * JD-Core Version:    0.6.2
 */