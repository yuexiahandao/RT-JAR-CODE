/*     */ package javax.sound.midi;
/*     */ 
/*     */ public class MetaMessage extends MidiMessage
/*     */ {
/*     */   public static final int META = 255;
/*  79 */   private int dataLength = 0;
/*     */   private static final long mask = 127L;
/*     */ 
/*     */   public MetaMessage()
/*     */   {
/*  90 */     this(new byte[] { -1, 0 });
/*     */   }
/*     */ 
/*     */   public MetaMessage(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 112 */     super(null);
/* 113 */     setMessage(paramInt1, paramArrayOfByte, paramInt2);
/*     */   }
/*     */ 
/*     */   protected MetaMessage(byte[] paramArrayOfByte)
/*     */   {
/* 125 */     super(paramArrayOfByte);
/*     */ 
/* 127 */     if (paramArrayOfByte.length >= 3) {
/* 128 */       this.dataLength = (paramArrayOfByte.length - 3);
/* 129 */       for (int i = 2; 
/* 130 */         (i < paramArrayOfByte.length) && ((paramArrayOfByte[i] & 0x80) != 0); 
/* 131 */         i++) this.dataLength -= 1;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setMessage(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 157 */     if ((paramInt1 >= 128) || (paramInt1 < 0)) {
/* 158 */       throw new InvalidMidiDataException("Invalid meta event with type " + paramInt1);
/*     */     }
/* 160 */     if (((paramInt2 > 0) && (paramInt2 > paramArrayOfByte.length)) || (paramInt2 < 0)) {
/* 161 */       throw new InvalidMidiDataException("length out of bounds: " + paramInt2);
/*     */     }
/*     */ 
/* 164 */     this.length = (2 + getVarIntLength(paramInt2) + paramInt2);
/* 165 */     this.dataLength = paramInt2;
/* 166 */     this.data = new byte[this.length];
/* 167 */     this.data[0] = -1;
/* 168 */     this.data[1] = ((byte)paramInt1);
/* 169 */     writeVarInt(this.data, 2, paramInt2);
/* 170 */     if (paramInt2 > 0)
/* 171 */       System.arraycopy(paramArrayOfByte, 0, this.data, this.length - this.dataLength, this.dataLength);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 181 */     if (this.length >= 2) {
/* 182 */       return this.data[1] & 0xFF;
/*     */     }
/* 184 */     return 0;
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/* 200 */     byte[] arrayOfByte = new byte[this.dataLength];
/* 201 */     System.arraycopy(this.data, this.length - this.dataLength, arrayOfByte, 0, this.dataLength);
/* 202 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 212 */     byte[] arrayOfByte = new byte[this.length];
/* 213 */     System.arraycopy(this.data, 0, arrayOfByte, 0, arrayOfByte.length);
/*     */ 
/* 215 */     MetaMessage localMetaMessage = new MetaMessage(arrayOfByte);
/* 216 */     return localMetaMessage;
/*     */   }
/*     */ 
/*     */   private int getVarIntLength(long paramLong)
/*     */   {
/* 222 */     int i = 0;
/*     */     do {
/* 224 */       paramLong >>= 7;
/* 225 */       i++;
/* 226 */     }while (paramLong > 0L);
/* 227 */     return i;
/*     */   }
/*     */ 
/*     */   private void writeVarInt(byte[] paramArrayOfByte, int paramInt, long paramLong)
/*     */   {
/* 233 */     int i = 63;
/*     */ 
/* 235 */     while ((i > 0) && ((paramLong & 127L << i) == 0L)) i -= 7;
/*     */ 
/* 237 */     while (i > 0) {
/* 238 */       paramArrayOfByte[(paramInt++)] = ((byte)(int)((paramLong & 127L << i) >> i | 0x80));
/* 239 */       i -= 7;
/*     */     }
/* 241 */     paramArrayOfByte[paramInt] = ((byte)(int)(paramLong & 0x7F));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MetaMessage
 * JD-Core Version:    0.6.2
 */