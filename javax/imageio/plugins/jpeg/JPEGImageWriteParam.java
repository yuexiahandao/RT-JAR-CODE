/*     */ package javax.imageio.plugins.jpeg;
/*     */ 
/*     */ import java.util.Locale;
/*     */ import javax.imageio.ImageWriteParam;
/*     */ 
/*     */ public class JPEGImageWriteParam extends ImageWriteParam
/*     */ {
/*  94 */   private JPEGQTable[] qTables = null;
/*  95 */   private JPEGHuffmanTable[] DCHuffmanTables = null;
/*  96 */   private JPEGHuffmanTable[] ACHuffmanTables = null;
/*  97 */   private boolean optimizeHuffman = false;
/*  98 */   private String[] compressionNames = { "JPEG" };
/*  99 */   private float[] qualityVals = { 0.0F, 0.3F, 0.75F, 1.0F };
/* 100 */   private String[] qualityDescs = { "Low quality", "Medium quality", "Visually lossless" };
/*     */ 
/*     */   public JPEGImageWriteParam(Locale paramLocale)
/*     */   {
/* 118 */     super(paramLocale);
/* 119 */     this.canWriteProgressive = true;
/* 120 */     this.progressiveMode = 0;
/* 121 */     this.canWriteCompressed = true;
/* 122 */     this.compressionTypes = this.compressionNames;
/* 123 */     this.compressionType = this.compressionTypes[0];
/* 124 */     this.compressionQuality = 0.75F;
/*     */   }
/*     */ 
/*     */   public void unsetCompression()
/*     */   {
/* 137 */     if (getCompressionMode() != 2) {
/* 138 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*     */     }
/*     */ 
/* 141 */     this.compressionQuality = 0.75F;
/*     */   }
/*     */ 
/*     */   public boolean isCompressionLossless()
/*     */   {
/* 154 */     if (getCompressionMode() != 2) {
/* 155 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*     */     }
/*     */ 
/* 158 */     return false;
/*     */   }
/*     */ 
/*     */   public String[] getCompressionQualityDescriptions() {
/* 162 */     if (getCompressionMode() != 2) {
/* 163 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*     */     }
/*     */ 
/* 166 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*     */     {
/* 168 */       throw new IllegalStateException("No compression type set!");
/*     */     }
/* 170 */     return (String[])this.qualityDescs.clone();
/*     */   }
/*     */ 
/*     */   public float[] getCompressionQualityValues() {
/* 174 */     if (getCompressionMode() != 2) {
/* 175 */       throw new IllegalStateException("Compression mode not MODE_EXPLICIT!");
/*     */     }
/*     */ 
/* 178 */     if ((getCompressionTypes() != null) && (getCompressionType() == null))
/*     */     {
/* 180 */       throw new IllegalStateException("No compression type set!");
/*     */     }
/* 182 */     return (float[])this.qualityVals.clone();
/*     */   }
/*     */ 
/*     */   public boolean areTablesSet()
/*     */   {
/* 190 */     return this.qTables != null;
/*     */   }
/*     */ 
/*     */   public void setEncodeTables(JPEGQTable[] paramArrayOfJPEGQTable, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2)
/*     */   {
/* 216 */     if ((paramArrayOfJPEGQTable == null) || (paramArrayOfJPEGHuffmanTable1 == null) || (paramArrayOfJPEGHuffmanTable2 == null) || (paramArrayOfJPEGQTable.length > 4) || (paramArrayOfJPEGHuffmanTable1.length > 4) || (paramArrayOfJPEGHuffmanTable2.length > 4) || (paramArrayOfJPEGHuffmanTable1.length != paramArrayOfJPEGHuffmanTable2.length))
/*     */     {
/* 223 */       throw new IllegalArgumentException("Invalid JPEG table arrays");
/*     */     }
/* 225 */     this.qTables = ((JPEGQTable[])paramArrayOfJPEGQTable.clone());
/* 226 */     this.DCHuffmanTables = ((JPEGHuffmanTable[])paramArrayOfJPEGHuffmanTable1.clone());
/* 227 */     this.ACHuffmanTables = ((JPEGHuffmanTable[])paramArrayOfJPEGHuffmanTable2.clone());
/*     */   }
/*     */ 
/*     */   public void unsetEncodeTables()
/*     */   {
/* 237 */     this.qTables = null;
/* 238 */     this.DCHuffmanTables = null;
/* 239 */     this.ACHuffmanTables = null;
/*     */   }
/*     */ 
/*     */   public JPEGQTable[] getQTables()
/*     */   {
/* 253 */     return this.qTables != null ? (JPEGQTable[])this.qTables.clone() : null;
/*     */   }
/*     */ 
/*     */   public JPEGHuffmanTable[] getDCHuffmanTables()
/*     */   {
/* 267 */     return this.DCHuffmanTables != null ? (JPEGHuffmanTable[])this.DCHuffmanTables.clone() : null;
/*     */   }
/*     */ 
/*     */   public JPEGHuffmanTable[] getACHuffmanTables()
/*     */   {
/* 283 */     return this.ACHuffmanTables != null ? (JPEGHuffmanTable[])this.ACHuffmanTables.clone() : null;
/*     */   }
/*     */ 
/*     */   public void setOptimizeHuffmanTables(boolean paramBoolean)
/*     */   {
/* 303 */     this.optimizeHuffman = paramBoolean;
/*     */   }
/*     */ 
/*     */   public boolean getOptimizeHuffmanTables()
/*     */   {
/* 318 */     return this.optimizeHuffman;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.plugins.jpeg.JPEGImageWriteParam
 * JD-Core Version:    0.6.2
 */