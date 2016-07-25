/*     */ package sun.security.smartcardio;
/*     */ 
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.ReadOnlyBufferException;
/*     */ import java.security.AccessController;
/*     */ import javax.smartcardio.Card;
/*     */ import javax.smartcardio.CardChannel;
/*     */ import javax.smartcardio.CardException;
/*     */ import javax.smartcardio.CommandAPDU;
/*     */ import javax.smartcardio.ResponseAPDU;
/*     */ import sun.security.action.GetPropertyAction;
/*     */ 
/*     */ final class ChannelImpl extends CardChannel
/*     */ {
/*     */   private final CardImpl card;
/*     */   private final int channel;
/*     */   private volatile boolean isClosed;
/* 118 */   private static final boolean t0GetResponse = getBooleanProperty("sun.security.smartcardio.t0GetResponse", true);
/*     */ 
/* 121 */   private static final boolean t1GetResponse = getBooleanProperty("sun.security.smartcardio.t1GetResponse", true);
/*     */ 
/* 124 */   private static final boolean t1StripLe = getBooleanProperty("sun.security.smartcardio.t1StripLe", false);
/*     */ 
/* 153 */   private static final byte[] B0 = new byte[0];
/*     */ 
/*     */   ChannelImpl(CardImpl paramCardImpl, int paramInt)
/*     */   {
/*  55 */     this.card = paramCardImpl;
/*  56 */     this.channel = paramInt;
/*     */   }
/*     */ 
/*     */   void checkClosed() {
/*  60 */     this.card.checkState();
/*  61 */     if (this.isClosed)
/*  62 */       throw new IllegalStateException("Logical channel has been closed");
/*     */   }
/*     */ 
/*     */   public Card getCard()
/*     */   {
/*  67 */     return this.card;
/*     */   }
/*     */ 
/*     */   public int getChannelNumber() {
/*  71 */     checkClosed();
/*  72 */     return this.channel;
/*     */   }
/*     */ 
/*     */   private static void checkManageChannel(byte[] paramArrayOfByte) {
/*  76 */     if (paramArrayOfByte.length < 4) {
/*  77 */       throw new IllegalArgumentException("Command APDU must be at least 4 bytes long");
/*     */     }
/*     */ 
/*  80 */     if ((paramArrayOfByte[0] >= 0) && (paramArrayOfByte[1] == 112))
/*  81 */       throw new IllegalArgumentException("Manage channel command not allowed, use openLogicalChannel()");
/*     */   }
/*     */ 
/*     */   public ResponseAPDU transmit(CommandAPDU paramCommandAPDU)
/*     */     throws CardException
/*     */   {
/*  87 */     checkClosed();
/*  88 */     this.card.checkExclusive();
/*  89 */     byte[] arrayOfByte1 = paramCommandAPDU.getBytes();
/*  90 */     byte[] arrayOfByte2 = doTransmit(arrayOfByte1);
/*  91 */     return new ResponseAPDU(arrayOfByte2);
/*     */   }
/*     */ 
/*     */   public int transmit(ByteBuffer paramByteBuffer1, ByteBuffer paramByteBuffer2) throws CardException {
/*  95 */     checkClosed();
/*  96 */     this.card.checkExclusive();
/*  97 */     if ((paramByteBuffer1 == null) || (paramByteBuffer2 == null)) {
/*  98 */       throw new NullPointerException();
/*     */     }
/* 100 */     if (paramByteBuffer2.isReadOnly()) {
/* 101 */       throw new ReadOnlyBufferException();
/*     */     }
/* 103 */     if (paramByteBuffer1 == paramByteBuffer2) {
/* 104 */       throw new IllegalArgumentException("command and response must not be the same object");
/*     */     }
/*     */ 
/* 107 */     if (paramByteBuffer2.remaining() < 258) {
/* 108 */       throw new IllegalArgumentException("Insufficient space in response buffer");
/*     */     }
/*     */ 
/* 111 */     byte[] arrayOfByte1 = new byte[paramByteBuffer1.remaining()];
/* 112 */     paramByteBuffer1.get(arrayOfByte1);
/* 113 */     byte[] arrayOfByte2 = doTransmit(arrayOfByte1);
/* 114 */     paramByteBuffer2.put(arrayOfByte2);
/* 115 */     return arrayOfByte2.length;
/*     */   }
/*     */ 
/*     */   private static boolean getBooleanProperty(String paramString, boolean paramBoolean)
/*     */   {
/* 128 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction(paramString));
/* 129 */     if (str == null) {
/* 130 */       return paramBoolean;
/*     */     }
/* 132 */     if (str.equalsIgnoreCase("true"))
/* 133 */       return true;
/* 134 */     if (str.equalsIgnoreCase("false")) {
/* 135 */       return false;
/*     */     }
/* 137 */     throw new IllegalArgumentException(paramString + " must be either 'true' or 'false'");
/*     */   }
/*     */ 
/*     */   private byte[] concat(byte[] paramArrayOfByte1, byte[] paramArrayOfByte2, int paramInt)
/*     */   {
/* 143 */     int i = paramArrayOfByte1.length;
/* 144 */     if ((i == 0) && (paramInt == paramArrayOfByte2.length)) {
/* 145 */       return paramArrayOfByte2;
/*     */     }
/* 147 */     byte[] arrayOfByte = new byte[i + paramInt];
/* 148 */     System.arraycopy(paramArrayOfByte1, 0, arrayOfByte, 0, i);
/* 149 */     System.arraycopy(paramArrayOfByte2, 0, arrayOfByte, i, paramInt);
/* 150 */     return arrayOfByte;
/*     */   }
/*     */ 
/*     */   private byte[] doTransmit(byte[] paramArrayOfByte)
/*     */     throws CardException
/*     */   {
/*     */     try
/*     */     {
/* 159 */       checkManageChannel(paramArrayOfByte);
/* 160 */       setChannel(paramArrayOfByte);
/* 161 */       int i = paramArrayOfByte.length;
/* 162 */       int j = this.card.protocol == 1 ? 1 : 0;
/* 163 */       int k = this.card.protocol == 2 ? 1 : 0;
/* 164 */       if ((j != 0) && (i >= 7) && (paramArrayOfByte[4] == 0)) {
/* 165 */         throw new CardException("Extended length forms not supported for T=0");
/*     */       }
/*     */ 
/* 168 */       if (((j != 0) || ((k != 0) && (t1StripLe))) && (i >= 7)) {
/* 169 */         m = paramArrayOfByte[4] & 0xFF;
/* 170 */         if (m != 0) {
/* 171 */           if (i == m + 6)
/* 172 */             i--;
/*     */         }
/*     */         else {
/* 175 */           m = (paramArrayOfByte[5] & 0xFF) << 8 | paramArrayOfByte[6] & 0xFF;
/* 176 */           if (i == m + 9) {
/* 177 */             i -= 2; }  }  } int m = ((j != 0) && (t0GetResponse)) || ((k != 0) && (t1GetResponse)) ? 1 : 0;
/* 182 */       int n = 0;
/* 183 */       byte[] arrayOfByte1 = B0;
/*     */       byte[] arrayOfByte2;
/*     */       int i1;
/*     */       while (true) { n++; if (n >= 32) {
/* 186 */           throw new CardException("Could not obtain response");
/*     */         }
/* 188 */         arrayOfByte2 = PCSC.SCardTransmit(this.card.cardId, this.card.protocol, paramArrayOfByte, 0, i);
/*     */ 
/* 190 */         i1 = arrayOfByte2.length;
/* 191 */         if ((m == 0) || (i1 < 2))
/*     */           break;
/* 193 */         if ((i1 == 2) && (arrayOfByte2[0] == 108))
/*     */         {
/* 195 */           paramArrayOfByte[(i - 1)] = arrayOfByte2[1];
/*     */         }
/*     */         else {
/* 198 */           if (arrayOfByte2[(i1 - 2)] != 97) {
/*     */             break;
/*     */           }
/* 201 */           if (i1 > 2) {
/* 202 */             arrayOfByte1 = concat(arrayOfByte1, arrayOfByte2, i1 - 2);
/*     */           }
/* 204 */           paramArrayOfByte[1] = -64;
/* 205 */           paramArrayOfByte[2] = 0;
/* 206 */           paramArrayOfByte[3] = 0;
/* 207 */           paramArrayOfByte[4] = arrayOfByte2[(i1 - 1)];
/* 208 */           i = 5;
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 213 */       arrayOfByte1 = concat(arrayOfByte1, arrayOfByte2, i1);
/*     */ 
/* 216 */       return arrayOfByte1;
/*     */     } catch (PCSCException localPCSCException) {
/* 218 */       this.card.handleError(localPCSCException);
/* 219 */       throw new CardException(localPCSCException);
/*     */     }
/*     */   }
/*     */ 
/*     */   private static int getSW(byte[] paramArrayOfByte) throws CardException {
/* 224 */     if (paramArrayOfByte.length < 2) {
/* 225 */       throw new CardException("Invalid response length: " + paramArrayOfByte.length);
/*     */     }
/* 227 */     int i = paramArrayOfByte[(paramArrayOfByte.length - 2)] & 0xFF;
/* 228 */     int j = paramArrayOfByte[(paramArrayOfByte.length - 1)] & 0xFF;
/* 229 */     return i << 8 | j;
/*     */   }
/*     */ 
/*     */   private static boolean isOK(byte[] paramArrayOfByte) throws CardException {
/* 233 */     return (paramArrayOfByte.length == 2) && (getSW(paramArrayOfByte) == 36864);
/*     */   }
/*     */ 
/*     */   private void setChannel(byte[] paramArrayOfByte) {
/* 237 */     int i = paramArrayOfByte[0];
/* 238 */     if (i < 0)
/*     */     {
/* 241 */       return;
/*     */     }
/*     */ 
/* 244 */     if ((i & 0xE0) == 32) {
/* 245 */       return;
/*     */     }
/*     */ 
/* 248 */     if (this.channel <= 3)
/*     */     {
/*     */       int tmp30_29 = 0;
/*     */       byte[] tmp30_28 = paramArrayOfByte; tmp30_28[tmp30_29] = ((byte)(tmp30_28[tmp30_29] & 0xBC));
/*     */       int tmp40_39 = 0;
/*     */       byte[] tmp40_38 = paramArrayOfByte; tmp40_38[tmp40_39] = ((byte)(tmp40_38[tmp40_39] | this.channel));
/* 253 */     } else if (this.channel <= 19)
/*     */     {
/*     */       int tmp63_62 = 0;
/*     */       byte[] tmp63_61 = paramArrayOfByte; tmp63_61[tmp63_62] = ((byte)(tmp63_61[tmp63_62] & 0xB0));
/*     */       int tmp73_72 = 0;
/*     */       byte[] tmp73_71 = paramArrayOfByte; tmp73_71[tmp73_72] = ((byte)(tmp73_71[tmp73_72] | 0x40));
/*     */       int tmp82_81 = 0;
/*     */       byte[] tmp82_80 = paramArrayOfByte; tmp82_80[tmp82_81] = ((byte)(tmp82_80[tmp82_81] | this.channel - 4));
/*     */     } else {
/* 260 */       throw new RuntimeException("Unsupported channel number: " + this.channel);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close() throws CardException {
/* 265 */     if (getChannelNumber() == 0) {
/* 266 */       throw new IllegalStateException("Cannot close basic logical channel");
/*     */     }
/* 268 */     if (this.isClosed) {
/* 269 */       return;
/*     */     }
/* 271 */     this.card.checkExclusive();
/*     */     try {
/* 273 */       byte[] arrayOfByte1 = { 0, 112, -128, 0 };
/* 274 */       arrayOfByte1[3] = ((byte)getChannelNumber());
/* 275 */       setChannel(arrayOfByte1);
/* 276 */       byte[] arrayOfByte2 = PCSC.SCardTransmit(this.card.cardId, this.card.protocol, arrayOfByte1, 0, arrayOfByte1.length);
/* 277 */       if (!isOK(arrayOfByte2))
/* 278 */         throw new CardException("close() failed: " + PCSC.toString(arrayOfByte2));
/*     */     }
/*     */     catch (PCSCException localPCSCException) {
/* 281 */       this.card.handleError(localPCSCException);
/* 282 */       throw new CardException("Could not close channel", localPCSCException);
/*     */     } finally {
/* 284 */       this.isClosed = true;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String toString() {
/* 289 */     return "PC/SC channel " + this.channel;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.security.smartcardio.ChannelImpl
 * JD-Core Version:    0.6.2
 */