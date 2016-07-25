/*     */ package javax.sound.midi;
/*     */ 
/*     */ public class SysexMessage extends MidiMessage
/*     */ {
/*     */   public static final int SYSTEM_EXCLUSIVE = 240;
/*     */   public static final int SPECIAL_SYSTEM_EXCLUSIVE = 247;
/*     */ 
/*     */   public SysexMessage()
/*     */   {
/* 117 */     this(new byte[2]);
/*     */ 
/* 119 */     this.data[0] = -16;
/* 120 */     this.data[1] = -9;
/*     */   }
/*     */ 
/*     */   public SysexMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 143 */     super(null);
/* 144 */     setMessage(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public SysexMessage(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 167 */     super(null);
/* 168 */     setMessage(paramInt1, paramArrayOfByte, paramInt2);
/*     */   }
/*     */ 
/*     */   protected SysexMessage(byte[] paramArrayOfByte)
/*     */   {
/* 180 */     super(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   public void setMessage(byte[] paramArrayOfByte, int paramInt)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 193 */     int i = paramArrayOfByte[0] & 0xFF;
/* 194 */     if ((i != 240) && (i != 247)) {
/* 195 */       throw new InvalidMidiDataException("Invalid status byte for sysex message: 0x" + Integer.toHexString(i));
/*     */     }
/* 197 */     super.setMessage(paramArrayOfByte, paramInt);
/*     */   }
/*     */ 
/*     */   public void setMessage(int paramInt1, byte[] paramArrayOfByte, int paramInt2)
/*     */     throws InvalidMidiDataException
/*     */   {
/* 209 */     if ((paramInt1 != 240) && (paramInt1 != 247)) {
/* 210 */       throw new InvalidMidiDataException("Invalid status byte for sysex message: 0x" + Integer.toHexString(paramInt1));
/*     */     }
/* 212 */     if ((paramInt2 < 0) || (paramInt2 > paramArrayOfByte.length)) {
/* 213 */       throw new IndexOutOfBoundsException("length out of bounds: " + paramInt2);
/*     */     }
/* 215 */     this.length = (paramInt2 + 1);
/*     */ 
/* 217 */     if ((this.data == null) || (this.data.length < this.length)) {
/* 218 */       this.data = new byte[this.length];
/*     */     }
/*     */ 
/* 221 */     this.data[0] = ((byte)(paramInt1 & 0xFF));
/* 222 */     if (paramInt2 > 0)
/* 223 */       System.arraycopy(paramArrayOfByte, 0, this.data, 1, paramInt2);
/*     */   }
/*     */ 
/*     */   public byte[] getData()
/*     */   {
/* 234 */     byte[] arrayOfByte = new byte[this.length - 1];
/* 235 */     System.arraycopy(this.data, 1, arrayOfByte, 0, this.length - 1);
/* 236 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/* 246 */     byte[] arrayOfByte = new byte[this.length];
/* 247 */     System.arraycopy(this.data, 0, arrayOfByte, 0, arrayOfByte.length);
/* 248 */     SysexMessage localSysexMessage = new SysexMessage(arrayOfByte);
/* 249 */     return localSysexMessage;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.SysexMessage
 * JD-Core Version:    0.6.2
 */