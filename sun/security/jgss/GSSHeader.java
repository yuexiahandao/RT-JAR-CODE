/*     */ package sun.security.jgss;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.PrintStream;
/*     */ import org.ietf.jgss.GSSException;
/*     */ import sun.security.util.DerOutputStream;
/*     */ import sun.security.util.DerValue;
/*     */ import sun.security.util.ObjectIdentifier;
/*     */ 
/*     */ public class GSSHeader
/*     */ {
/*  55 */   private ObjectIdentifier mechOid = null;
/*  56 */   private byte[] mechOidBytes = null;
/*  57 */   private int mechTokenLength = 0;
/*     */   public static final int TOKEN_ID = 96;
/*     */ 
/*     */   public GSSHeader(ObjectIdentifier paramObjectIdentifier, int paramInt)
/*     */     throws IOException
/*     */   {
/*  75 */     this.mechOid = paramObjectIdentifier;
/*  76 */     DerOutputStream localDerOutputStream = new DerOutputStream();
/*  77 */     localDerOutputStream.putOID(paramObjectIdentifier);
/*  78 */     this.mechOidBytes = localDerOutputStream.toByteArray();
/*  79 */     this.mechTokenLength = paramInt;
/*     */   }
/*     */ 
/*     */   public GSSHeader(InputStream paramInputStream)
/*     */     throws IOException, GSSException
/*     */   {
/*  92 */     int i = paramInputStream.read();
/*     */ 
/*  96 */     if (i != 96) {
/*  97 */       throw new GSSException(10, -1, "GSSHeader did not find the right tag");
/*     */     }
/*     */ 
/* 100 */     int j = getLength(paramInputStream);
/*     */ 
/* 102 */     DerValue localDerValue = new DerValue(paramInputStream);
/* 103 */     this.mechOidBytes = localDerValue.toByteArray();
/* 104 */     this.mechOid = localDerValue.getOID();
/*     */ 
/* 108 */     this.mechTokenLength = (j - this.mechOidBytes.length);
/*     */   }
/*     */ 
/*     */   public ObjectIdentifier getOid()
/*     */   {
/* 119 */     return this.mechOid;
/*     */   }
/*     */ 
/*     */   public int getMechTokenLength()
/*     */   {
/* 129 */     return this.mechTokenLength;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 137 */     int i = this.mechOidBytes.length + this.mechTokenLength;
/* 138 */     return 1 + getLenFieldSize(i) + this.mechOidBytes.length;
/*     */   }
/*     */ 
/*     */   public static int getMaxMechTokenSize(ObjectIdentifier paramObjectIdentifier, int paramInt)
/*     */   {
/* 157 */     int i = 0;
/*     */     try {
/* 159 */       DerOutputStream localDerOutputStream = new DerOutputStream();
/* 160 */       localDerOutputStream.putOID(paramObjectIdentifier);
/* 161 */       i = localDerOutputStream.toByteArray().length;
/*     */     }
/*     */     catch (IOException localIOException)
/*     */     {
/*     */     }
/* 166 */     paramInt -= 1 + i;
/*     */ 
/* 169 */     paramInt -= 5;
/*     */ 
/* 171 */     return paramInt;
/*     */   }
/*     */ 
/*     */   private int getLenFieldSize(int paramInt)
/*     */   {
/* 199 */     int i = 1;
/* 200 */     if (paramInt < 128)
/* 201 */       i = 1;
/* 202 */     else if (paramInt < 256)
/* 203 */       i = 2;
/* 204 */     else if (paramInt < 65536)
/* 205 */       i = 3;
/* 206 */     else if (paramInt < 16777216)
/* 207 */       i = 4;
/*     */     else {
/* 209 */       i = 5;
/*     */     }
/* 211 */     return i;
/*     */   }
/*     */ 
/*     */   public int encode(OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 221 */     int i = 1 + this.mechOidBytes.length;
/* 222 */     paramOutputStream.write(96);
/* 223 */     int j = this.mechOidBytes.length + this.mechTokenLength;
/* 224 */     i += putLength(j, paramOutputStream);
/* 225 */     paramOutputStream.write(this.mechOidBytes);
/* 226 */     return i;
/*     */   }
/*     */ 
/*     */   private int getLength(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 238 */     return getLength(paramInputStream.read(), paramInputStream);
/*     */   }
/*     */ 
/*     */   private int getLength(int paramInt, InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/* 252 */     int j = paramInt;
/*     */     int i;
/* 253 */     if ((j & 0x80) == 0) {
/* 254 */       i = j;
/*     */     } else {
/* 256 */       j &= 127;
/*     */ 
/* 262 */       if (j == 0)
/* 263 */         return -1;
/* 264 */       if ((j < 0) || (j > 4)) {
/* 265 */         throw new IOException("DerInputStream.getLength(): lengthTag=" + j + ", " + (j < 0 ? "incorrect DER encoding." : "too big."));
/*     */       }
/*     */ 
/* 269 */       for (i = 0; j > 0; j--) {
/* 270 */         i <<= 8;
/* 271 */         i += (0xFF & paramInputStream.read());
/*     */       }
/* 273 */       if (i < 0) {
/* 274 */         throw new IOException("Invalid length bytes");
/*     */       }
/*     */     }
/* 277 */     return i;
/*     */   }
/*     */ 
/*     */   private int putLength(int paramInt, OutputStream paramOutputStream)
/*     */     throws IOException
/*     */   {
/* 290 */     int i = 0;
/* 291 */     if (paramInt < 128) {
/* 292 */       paramOutputStream.write((byte)paramInt);
/* 293 */       i = 1;
/*     */     }
/* 295 */     else if (paramInt < 256) {
/* 296 */       paramOutputStream.write(-127);
/* 297 */       paramOutputStream.write((byte)paramInt);
/* 298 */       i = 2;
/*     */     }
/* 300 */     else if (paramInt < 65536) {
/* 301 */       paramOutputStream.write(-126);
/* 302 */       paramOutputStream.write((byte)(paramInt >> 8));
/* 303 */       paramOutputStream.write((byte)paramInt);
/* 304 */       i = 3;
/*     */     }
/* 306 */     else if (paramInt < 16777216) {
/* 307 */       paramOutputStream.write(-125);
/* 308 */       paramOutputStream.write((byte)(paramInt >> 16));
/* 309 */       paramOutputStream.write((byte)(paramInt >> 8));
/* 310 */       paramOutputStream.write((byte)paramInt);
/* 311 */       i = 4;
/*     */     }
/*     */     else {
/* 314 */       paramOutputStream.write(-124);
/* 315 */       paramOutputStream.write((byte)(paramInt >> 24));
/* 316 */       paramOutputStream.write((byte)(paramInt >> 16));
/* 317 */       paramOutputStream.write((byte)(paramInt >> 8));
/* 318 */       paramOutputStream.write((byte)paramInt);
/* 319 */       i = 5;
/*     */     }
/*     */ 
/* 322 */     return i;
/*     */   }
/*     */ 
/*     */   private void debug(String paramString)
/*     */   {
/* 327 */     System.err.print(paramString);
/*     */   }
/*     */ 
/*     */   private String getHexBytes(byte[] paramArrayOfByte, int paramInt)
/*     */     throws IOException
/*     */   {
/* 333 */     StringBuffer localStringBuffer = new StringBuffer();
/* 334 */     for (int i = 0; i < paramInt; i++)
/*     */     {
/* 336 */       int j = paramArrayOfByte[i] >> 4 & 0xF;
/* 337 */       int k = paramArrayOfByte[i] & 0xF;
/*     */ 
/* 339 */       localStringBuffer.append(Integer.toHexString(j));
/* 340 */       localStringBuffer.append(Integer.toHexString(k));
/* 341 */       localStringBuffer.append(' ');
/*     */     }
/* 343 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.jgss.GSSHeader
 * JD-Core Version:    0.6.2
 */