/*    */ package sun.reflect;
/*    */ 
/*    */ class ByteVectorImpl
/*    */   implements ByteVector
/*    */ {
/*    */   private byte[] data;
/*    */   private int pos;
/*    */ 
/*    */   public ByteVectorImpl()
/*    */   {
/* 33 */     this(100);
/*    */   }
/*    */ 
/*    */   public ByteVectorImpl(int paramInt) {
/* 37 */     this.data = new byte[paramInt];
/* 38 */     this.pos = -1;
/*    */   }
/*    */ 
/*    */   public int getLength() {
/* 42 */     return this.pos + 1;
/*    */   }
/*    */ 
/*    */   public byte get(int paramInt) {
/* 46 */     if (paramInt >= this.data.length) {
/* 47 */       resize(paramInt);
/* 48 */       this.pos = paramInt;
/*    */     }
/* 50 */     return this.data[paramInt];
/*    */   }
/*    */ 
/*    */   public void put(int paramInt, byte paramByte) {
/* 54 */     if (paramInt >= this.data.length) {
/* 55 */       resize(paramInt);
/* 56 */       this.pos = paramInt;
/*    */     }
/* 58 */     this.data[paramInt] = paramByte;
/*    */   }
/*    */ 
/*    */   public void add(byte paramByte) {
/* 62 */     if (++this.pos >= this.data.length) {
/* 63 */       resize(this.pos);
/*    */     }
/* 65 */     this.data[this.pos] = paramByte;
/*    */   }
/*    */ 
/*    */   public void trim() {
/* 69 */     if (this.pos != this.data.length - 1) {
/* 70 */       byte[] arrayOfByte = new byte[this.pos + 1];
/* 71 */       System.arraycopy(this.data, 0, arrayOfByte, 0, this.pos + 1);
/* 72 */       this.data = arrayOfByte;
/*    */     }
/*    */   }
/*    */ 
/*    */   public byte[] getData() {
/* 77 */     return this.data;
/*    */   }
/*    */ 
/*    */   private void resize(int paramInt) {
/* 81 */     if (paramInt <= 2 * this.data.length) {
/* 82 */       paramInt = 2 * this.data.length;
/*    */     }
/* 84 */     byte[] arrayOfByte = new byte[paramInt];
/* 85 */     System.arraycopy(this.data, 0, arrayOfByte, 0, this.data.length);
/* 86 */     this.data = arrayOfByte;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.ByteVectorImpl
 * JD-Core Version:    0.6.2
 */