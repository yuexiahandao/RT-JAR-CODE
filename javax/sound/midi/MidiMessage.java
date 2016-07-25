/*     */ package javax.sound.midi;
/*     */ 
/*     */ public abstract class MidiMessage
/*     */   implements Cloneable
/*     */ {
/*     */   protected byte[] data;
/*  93 */   protected int length = 0;
/*     */ 
/*     */   protected MidiMessage(byte[] paramArrayOfByte)
/*     */   {
/* 109 */     this.data = paramArrayOfByte;
/* 110 */     if (paramArrayOfByte != null)
/* 111 */       this.length = paramArrayOfByte.length;
/*     */   }
/*     */ 
/*     */   protected void setMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 123 */     if ((paramInt < 0) || ((paramInt > 0) && (paramInt > paramArrayOfByte.length))) {
/* 124 */       throw new IndexOutOfBoundsException("length out of bounds: " + paramInt);
/*     */     }
/* 126 */     this.length = paramInt;
/*     */ 
/* 128 */     if ((this.data == null) || (this.data.length < this.length)) {
/* 129 */       this.data = new byte[this.length];
/*     */     }
/* 131 */     System.arraycopy(paramArrayOfByte, 0, this.data, 0, paramInt);
/*     */   }
/*     */ 
/*     */   public byte[] getMessage()
/*     */   {
/* 146 */     byte[] arrayOfByte = new byte[this.length];
/* 147 */     System.arraycopy(this.data, 0, arrayOfByte, 0, this.length);
/* 148 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public int getStatus()
/*     */   {
/* 161 */     if (this.length > 0) {
/* 162 */       return this.data[0] & 0xFF;
/*     */     }
/* 164 */     return 0;
/*     */   }
/*     */ 
/*     */   public int getLength()
/*     */   {
/* 178 */     return this.length;
/*     */   }
/*     */ 
/*     */   public abstract Object clone();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiMessage
 * JD-Core Version:    0.6.2
 */