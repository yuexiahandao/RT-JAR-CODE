/*     */ package javax.imageio.stream;
/*     */ 
/*     */ public class IIOByteBuffer
/*     */ {
/*     */   private byte[] data;
/*     */   private int offset;
/*     */   private int length;
/*     */ 
/*     */   public IIOByteBuffer(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */   {
/*  59 */     this.data = paramArrayOfByte;
/*  60 */     this.offset = paramInt1;
/*  61 */     this.length = paramInt2;
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/*  77 */     return this.data;
/*     */   }
/*     */ 
/*     */   public void setData(byte[] paramArrayOfByte)
/*     */   {
/*  89 */     this.data = paramArrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getOffset()
/*     */   {
/* 103 */     return this.offset;
/*     */   }
/*     */ 
/*     */   public void setOffset(int paramInt)
/*     */   {
/* 115 */     this.offset = paramInt;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 129 */     return this.length;
/*     */   }
/*     */ 
/*     */   public void setLength(int paramInt)
/*     */   {
/* 141 */     this.length = paramInt;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.stream.IIOByteBuffer
 * JD-Core Version:    0.6.2
 */