/*     */ package javax.imageio.plugins.jpeg;
/*     */ 
/*     */ import javax.imageio.ImageReadParam;
/*     */ 
/*     */ public class JPEGImageReadParam extends ImageReadParam
/*     */ {
/*  83 */   private JPEGQTable[] qTables = null;
/*  84 */   private JPEGHuffmanTable[] DCHuffmanTables = null;
/*  85 */   private JPEGHuffmanTable[] ACHuffmanTables = null;
/*     */ 
/*     */   public boolean areTablesSet()
/*     */   {
/* 100 */     return this.qTables != null;
/*     */   }
/*     */ 
/*     */   public void setDecodeTables(JPEGQTable[] paramArrayOfJPEGQTable, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable1, JPEGHuffmanTable[] paramArrayOfJPEGHuffmanTable2)
/*     */   {
/* 127 */     if ((paramArrayOfJPEGQTable == null) || (paramArrayOfJPEGHuffmanTable1 == null) || (paramArrayOfJPEGHuffmanTable2 == null) || (paramArrayOfJPEGQTable.length > 4) || (paramArrayOfJPEGHuffmanTable1.length > 4) || (paramArrayOfJPEGHuffmanTable2.length > 4) || (paramArrayOfJPEGHuffmanTable1.length != paramArrayOfJPEGHuffmanTable2.length))
/*     */     {
/* 134 */       throw new IllegalArgumentException("Invalid JPEG table arrays");
/*     */     }
/*     */ 
/* 137 */     this.qTables = ((JPEGQTable[])paramArrayOfJPEGQTable.clone());
/* 138 */     this.DCHuffmanTables = ((JPEGHuffmanTable[])paramArrayOfJPEGHuffmanTable1.clone());
/* 139 */     this.ACHuffmanTables = ((JPEGHuffmanTable[])paramArrayOfJPEGHuffmanTable2.clone());
/*     */   }
/*     */ 
/*     */   public void unsetDecodeTables()
/*     */   {
/* 149 */     this.qTables = null;
/* 150 */     this.DCHuffmanTables = null;
/* 151 */     this.ACHuffmanTables = null;
/*     */   }
/*     */ 
/*     */   public JPEGQTable[] getQTables()
/*     */   {
/* 165 */     return this.qTables != null ? (JPEGQTable[])this.qTables.clone() : null;
/*     */   }
/*     */ 
/*     */   public JPEGHuffmanTable[] getDCHuffmanTables()
/*     */   {
/* 179 */     return this.DCHuffmanTables != null ? (JPEGHuffmanTable[])this.DCHuffmanTables.clone() : null;
/*     */   }
/*     */ 
/*     */   public JPEGHuffmanTable[] getACHuffmanTables()
/*     */   {
/* 195 */     return this.ACHuffmanTables != null ? (JPEGHuffmanTable[])this.ACHuffmanTables.clone() : null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.plugins.jpeg.JPEGImageReadParam
 * JD-Core Version:    0.6.2
 */