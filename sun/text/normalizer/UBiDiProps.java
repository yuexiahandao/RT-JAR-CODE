/*     */ package sun.text.normalizer;
/*     */ 
/*     */ import java.io.BufferedInputStream;
/*     */ import java.io.DataInputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ 
/*     */ public final class UBiDiProps
/*     */ {
/* 115 */   private static UBiDiProps gBdp = null;
/*     */ 
/* 126 */   private static UBiDiProps gBdpDummy = null;
/*     */   private int[] indexes;
/*     */   private int[] mirrors;
/*     */   private byte[] jgArray;
/*     */   private CharTrie trie;
/*     */   private static final String DATA_FILE_NAME = "/sun/text/resources/ubidi.icu";
/* 162 */   private static final byte[] FMT = { 66, 105, 68, 105 };
/*     */   private static final int IX_INDEX_TOP = 0;
/*     */   private static final int IX_MIRROR_LENGTH = 3;
/*     */   private static final int IX_JG_START = 4;
/*     */   private static final int IX_JG_LIMIT = 5;
/*     */   private static final int IX_TOP = 16;
/*     */   private static final int CLASS_MASK = 31;
/*     */ 
/*     */   public UBiDiProps()
/*     */     throws IOException
/*     */   {
/*  59 */     InputStream localInputStream = ICUData.getStream("/sun/text/resources/ubidi.icu");
/*  60 */     BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream, 4096);
/*  61 */     readData(localBufferedInputStream);
/*  62 */     localBufferedInputStream.close();
/*  63 */     localInputStream.close();
/*     */   }
/*     */ 
/*     */   private void readData(InputStream paramInputStream) throws IOException
/*     */   {
/*  68 */     DataInputStream localDataInputStream = new DataInputStream(paramInputStream);
/*     */ 
/*  71 */     ICUBinary.readHeader(localDataInputStream, FMT, new IsAcceptable(null));
/*     */ 
/*  75 */     int j = localDataInputStream.readInt();
/*  76 */     if (j < 0) {
/*  77 */       throw new IOException("indexes[0] too small in /sun/text/resources/ubidi.icu");
/*     */     }
/*  79 */     this.indexes = new int[j];
/*     */ 
/*  81 */     this.indexes[0] = j;
/*  82 */     for (int i = 1; i < j; i++) {
/*  83 */       this.indexes[i] = localDataInputStream.readInt();
/*     */     }
/*     */ 
/*  87 */     this.trie = new CharTrie(localDataInputStream, null);
/*     */ 
/*  90 */     j = this.indexes[3];
/*  91 */     if (j > 0) {
/*  92 */       this.mirrors = new int[j];
/*  93 */       for (i = 0; i < j; i++) {
/*  94 */         this.mirrors[i] = localDataInputStream.readInt();
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/*  99 */     j = this.indexes[5] - this.indexes[4];
/* 100 */     this.jgArray = new byte[j];
/* 101 */     for (i = 0; i < j; i++)
/* 102 */       this.jgArray[i] = localDataInputStream.readByte();
/*     */   }
/*     */ 
/*     */   public static final synchronized UBiDiProps getSingleton()
/*     */     throws IOException
/*     */   {
/* 119 */     if (gBdp == null) {
/* 120 */       gBdp = new UBiDiProps();
/*     */     }
/* 122 */     return gBdp;
/*     */   }
/*     */ 
/*     */   private UBiDiProps(boolean paramBoolean)
/*     */   {
/* 129 */     this.indexes = new int[16];
/* 130 */     this.indexes[0] = 16;
/* 131 */     this.trie = new CharTrie(0, 0, null);
/*     */   }
/*     */ 
/*     */   public static final synchronized UBiDiProps getDummy()
/*     */   {
/* 141 */     if (gBdpDummy == null) {
/* 142 */       gBdpDummy = new UBiDiProps(true);
/*     */     }
/* 144 */     return gBdpDummy;
/*     */   }
/*     */ 
/*     */   public final int getClass(int paramInt) {
/* 148 */     return getClassFromProps(this.trie.getCodePointValue(paramInt));
/*     */   }
/*     */ 
/*     */   private static final int getClassFromProps(int paramInt)
/*     */   {
/* 176 */     return paramInt & 0x1F;
/*     */   }
/*     */ 
/*     */   private final class IsAcceptable
/*     */     implements ICUBinary.Authenticate
/*     */   {
/*     */     private IsAcceptable()
/*     */     {
/*     */     }
/*     */ 
/*     */     public boolean isDataVersionAcceptable(byte[] paramArrayOfByte)
/*     */     {
/* 109 */       return (paramArrayOfByte[0] == 1) && (paramArrayOfByte[2] == 5) && (paramArrayOfByte[3] == 2);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.text.normalizer.UBiDiProps
 * JD-Core Version:    0.6.2
 */