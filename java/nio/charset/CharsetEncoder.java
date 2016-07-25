/*     */ package java.nio.charset;
/*     */ 
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public abstract class CharsetEncoder
/*     */ {
/*     */   private final Charset charset;
/*     */   private final float averageBytesPerChar;
/*     */   private final float maxBytesPerChar;
/*     */   private byte[] replacement;
/* 143 */   private CodingErrorAction malformedInputAction = CodingErrorAction.REPORT;
/*     */ 
/* 145 */   private CodingErrorAction unmappableCharacterAction = CodingErrorAction.REPORT;
/*     */   private static final int ST_RESET = 0;
/*     */   private static final int ST_CODING = 1;
/*     */   private static final int ST_END = 2;
/*     */   private static final int ST_FLUSHED = 3;
/* 155 */   private int state = 0;
/*     */ 
/* 157 */   private static String[] stateNames = { "RESET", "CODING", "CODING_END", "FLUSHED" };
/*     */ 
/* 306 */   private WeakReference<CharsetDecoder> cachedDecoder = null;
/*     */ 
/*     */   protected CharsetEncoder(Charset paramCharset, float paramFloat1, float paramFloat2, byte[] paramArrayOfByte)
/*     */   {
/* 187 */     this.charset = paramCharset;
/* 188 */     if (paramFloat1 <= 0.0F) {
/* 189 */       throw new IllegalArgumentException("Non-positive averageBytesPerChar");
/*     */     }
/* 191 */     if (paramFloat2 <= 0.0F) {
/* 192 */       throw new IllegalArgumentException("Non-positive maxBytesPerChar");
/*     */     }
/* 194 */     if ((!Charset.atBugLevel("1.4")) && 
/* 195 */       (paramFloat1 > paramFloat2)) {
/* 196 */       throw new IllegalArgumentException("averageBytesPerChar exceeds maxBytesPerChar");
/*     */     }
/*     */ 
/* 200 */     this.replacement = paramArrayOfByte;
/* 201 */     this.averageBytesPerChar = paramFloat1;
/* 202 */     this.maxBytesPerChar = paramFloat2;
/* 203 */     replaceWith(paramArrayOfByte);
/*     */   }
/*     */ 
/*     */   protected CharsetEncoder(Charset paramCharset, float paramFloat1, float paramFloat2)
/*     */   {
/* 226 */     this(paramCharset, paramFloat1, paramFloat2, new byte[] { 63 });
/*     */   }
/*     */ 
/*     */   public final Charset charset()
/*     */   {
/* 237 */     return this.charset;
/*     */   }
/*     */ 
/*     */   public final byte[] replacement()
/*     */   {
/* 247 */     return this.replacement;
/*     */   }
/*     */ 
/*     */   public final CharsetEncoder replaceWith(byte[] paramArrayOfByte)
/*     */   {
/* 276 */     if (paramArrayOfByte == null)
/* 277 */       throw new IllegalArgumentException("Null replacement");
/* 278 */     int i = paramArrayOfByte.length;
/* 279 */     if (i == 0)
/* 280 */       throw new IllegalArgumentException("Empty replacement");
/* 281 */     if (i > this.maxBytesPerChar) {
/* 282 */       throw new IllegalArgumentException("Replacement too long");
/*     */     }
/* 284 */     if (!isLegalReplacement(paramArrayOfByte)) {
/* 285 */       throw new IllegalArgumentException("Illegal replacement");
/*     */     }
/* 287 */     this.replacement = paramArrayOfByte;
/* 288 */     implReplaceWith(paramArrayOfByte);
/* 289 */     return this;
/*     */   }
/*     */ 
/*     */   protected void implReplaceWith(byte[] paramArrayOfByte)
/*     */   {
/*     */   }
/*     */ 
/*     */   public boolean isLegalReplacement(byte[] paramArrayOfByte)
/*     */   {
/* 325 */     WeakReference localWeakReference = this.cachedDecoder;
/* 326 */     CharsetDecoder localCharsetDecoder = null;
/* 327 */     if ((localWeakReference == null) || ((localCharsetDecoder = (CharsetDecoder)localWeakReference.get()) == null)) {
/* 328 */       localCharsetDecoder = charset().newDecoder();
/* 329 */       localCharsetDecoder.onMalformedInput(CodingErrorAction.REPORT);
/* 330 */       localCharsetDecoder.onUnmappableCharacter(CodingErrorAction.REPORT);
/* 331 */       this.cachedDecoder = new WeakReference(localCharsetDecoder);
/*     */     } else {
/* 333 */       localCharsetDecoder.reset();
/*     */     }
/* 335 */     ByteBuffer localByteBuffer = ByteBuffer.wrap(paramArrayOfByte);
/* 336 */     CharBuffer localCharBuffer = CharBuffer.allocate((int)(localByteBuffer.remaining() * localCharsetDecoder.maxCharsPerByte()));
/*     */ 
/* 338 */     CoderResult localCoderResult = localCharsetDecoder.decode(localByteBuffer, localCharBuffer, true);
/* 339 */     return !localCoderResult.isError();
/*     */   }
/*     */ 
/*     */   public CodingErrorAction malformedInputAction()
/*     */   {
/* 350 */     return this.malformedInputAction;
/*     */   }
/*     */ 
/*     */   public final CharsetEncoder onMalformedInput(CodingErrorAction paramCodingErrorAction)
/*     */   {
/* 367 */     if (paramCodingErrorAction == null)
/* 368 */       throw new IllegalArgumentException("Null action");
/* 369 */     this.malformedInputAction = paramCodingErrorAction;
/* 370 */     implOnMalformedInput(paramCodingErrorAction);
/* 371 */     return this;
/*     */   }
/*     */ 
/*     */   protected void implOnMalformedInput(CodingErrorAction paramCodingErrorAction)
/*     */   {
/*     */   }
/*     */ 
/*     */   public CodingErrorAction unmappableCharacterAction()
/*     */   {
/* 391 */     return this.unmappableCharacterAction;
/*     */   }
/*     */ 
/*     */   public final CharsetEncoder onUnmappableCharacter(CodingErrorAction paramCodingErrorAction)
/*     */   {
/* 410 */     if (paramCodingErrorAction == null)
/* 411 */       throw new IllegalArgumentException("Null action");
/* 412 */     this.unmappableCharacterAction = paramCodingErrorAction;
/* 413 */     implOnUnmappableCharacter(paramCodingErrorAction);
/* 414 */     return this;
/*     */   }
/*     */ 
/*     */   protected void implOnUnmappableCharacter(CodingErrorAction paramCodingErrorAction)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final float averageBytesPerChar()
/*     */   {
/* 435 */     return this.averageBytesPerChar;
/*     */   }
/*     */ 
/*     */   public final float maxBytesPerChar()
/*     */   {
/* 447 */     return this.maxBytesPerChar;
/*     */   }
/*     */ 
/*     */   public final CoderResult encode(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer, boolean paramBoolean)
/*     */   {
/* 551 */     int i = paramBoolean ? 2 : 1;
/* 552 */     if ((this.state != 0) && (this.state != 1) && ((!paramBoolean) || (this.state != 2)))
/*     */     {
/* 554 */       throwIllegalStateException(this.state, i);
/* 555 */     }this.state = i;
/*     */     while (true)
/*     */     {
/*     */       CoderResult localCoderResult;
/*     */       try
/*     */       {
/* 561 */         localCoderResult = encodeLoop(paramCharBuffer, paramByteBuffer);
/*     */       } catch (BufferUnderflowException localBufferUnderflowException) {
/* 563 */         throw new CoderMalfunctionError(localBufferUnderflowException);
/*     */       } catch (BufferOverflowException localBufferOverflowException) {
/* 565 */         throw new CoderMalfunctionError(localBufferOverflowException);
/*     */       }
/*     */ 
/* 568 */       if (localCoderResult.isOverflow()) {
/* 569 */         return localCoderResult;
/*     */       }
/* 571 */       if (localCoderResult.isUnderflow()) {
/* 572 */         if ((paramBoolean) && (paramCharBuffer.hasRemaining())) {
/* 573 */           localCoderResult = CoderResult.malformedForLength(paramCharBuffer.remaining());
/*     */         }
/*     */         else {
/* 576 */           return localCoderResult;
/*     */         }
/*     */       }
/*     */ 
/* 580 */       CodingErrorAction localCodingErrorAction = null;
/* 581 */       if (localCoderResult.isMalformed())
/* 582 */         localCodingErrorAction = this.malformedInputAction;
/* 583 */       else if (localCoderResult.isUnmappable()) {
/* 584 */         localCodingErrorAction = this.unmappableCharacterAction;
/*     */       }
/* 586 */       else if (!$assertionsDisabled) throw new AssertionError(localCoderResult.toString());
/*     */ 
/* 588 */       if (localCodingErrorAction == CodingErrorAction.REPORT) {
/* 589 */         return localCoderResult;
/*     */       }
/* 591 */       if (localCodingErrorAction == CodingErrorAction.REPLACE) {
/* 592 */         if (paramByteBuffer.remaining() < this.replacement.length)
/* 593 */           return CoderResult.OVERFLOW;
/* 594 */         paramByteBuffer.put(this.replacement);
/*     */       }
/*     */ 
/* 597 */       if ((localCodingErrorAction == CodingErrorAction.IGNORE) || (localCodingErrorAction == CodingErrorAction.REPLACE))
/*     */       {
/* 600 */         paramCharBuffer.position(paramCharBuffer.position() + localCoderResult.length());
/*     */       }
/* 604 */       else if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final CoderResult flush(ByteBuffer paramByteBuffer)
/*     */   {
/* 649 */     if (this.state == 2) {
/* 650 */       CoderResult localCoderResult = implFlush(paramByteBuffer);
/* 651 */       if (localCoderResult.isUnderflow())
/* 652 */         this.state = 3;
/* 653 */       return localCoderResult;
/*     */     }
/*     */ 
/* 656 */     if (this.state != 3) {
/* 657 */       throwIllegalStateException(this.state, 3);
/*     */     }
/* 659 */     return CoderResult.UNDERFLOW;
/*     */   }
/*     */ 
/*     */   protected CoderResult implFlush(ByteBuffer paramByteBuffer)
/*     */   {
/* 677 */     return CoderResult.UNDERFLOW;
/*     */   }
/*     */ 
/*     */   public final CharsetEncoder reset()
/*     */   {
/* 691 */     implReset();
/* 692 */     this.state = 0;
/* 693 */     return this;
/*     */   }
/*     */ 
/*     */   protected void implReset()
/*     */   {
/*     */   }
/*     */ 
/*     */   protected abstract CoderResult encodeLoop(CharBuffer paramCharBuffer, ByteBuffer paramByteBuffer);
/*     */ 
/*     */   public final ByteBuffer encode(CharBuffer paramCharBuffer)
/*     */     throws CharacterCodingException
/*     */   {
/* 776 */     int i = (int)(paramCharBuffer.remaining() * averageBytesPerChar());
/* 777 */     Object localObject = ByteBuffer.allocate(i);
/*     */ 
/* 779 */     if ((i == 0) && (paramCharBuffer.remaining() == 0))
/* 780 */       return localObject;
/* 781 */     reset();
/*     */     while (true) {
/* 783 */       CoderResult localCoderResult = paramCharBuffer.hasRemaining() ? encode(paramCharBuffer, (ByteBuffer)localObject, true) : CoderResult.UNDERFLOW;
/*     */ 
/* 785 */       if (localCoderResult.isUnderflow()) {
/* 786 */         localCoderResult = flush((ByteBuffer)localObject);
/*     */       }
/* 788 */       if (localCoderResult.isUnderflow())
/*     */         break;
/* 790 */       if (localCoderResult.isOverflow()) {
/* 791 */         i = 2 * i + 1;
/* 792 */         ByteBuffer localByteBuffer = ByteBuffer.allocate(i);
/* 793 */         ((ByteBuffer)localObject).flip();
/* 794 */         localByteBuffer.put((ByteBuffer)localObject);
/* 795 */         localObject = localByteBuffer;
/*     */       }
/*     */       else {
/* 798 */         localCoderResult.throwException();
/*     */       }
/*     */     }
/* 800 */     ((ByteBuffer)localObject).flip();
/* 801 */     return localObject;
/*     */   }
/*     */ 
/*     */   private boolean canEncode(CharBuffer paramCharBuffer)
/*     */   {
/* 883 */     if (this.state == 3)
/* 884 */       reset();
/* 885 */     else if (this.state != 0)
/* 886 */       throwIllegalStateException(this.state, 1);
/* 887 */     CodingErrorAction localCodingErrorAction1 = malformedInputAction();
/* 888 */     CodingErrorAction localCodingErrorAction2 = unmappableCharacterAction();
/*     */     try {
/* 890 */       onMalformedInput(CodingErrorAction.REPORT);
/* 891 */       onUnmappableCharacter(CodingErrorAction.REPORT);
/* 892 */       encode(paramCharBuffer);
/*     */     } catch (CharacterCodingException localCharacterCodingException) {
/* 894 */       return false;
/*     */     } finally {
/* 896 */       onMalformedInput(localCodingErrorAction1);
/* 897 */       onUnmappableCharacter(localCodingErrorAction2);
/* 898 */       reset();
/*     */     }
/* 900 */     return true;
/*     */   }
/*     */ 
/*     */   public boolean canEncode(char paramChar)
/*     */   {
/* 927 */     CharBuffer localCharBuffer = CharBuffer.allocate(1);
/* 928 */     localCharBuffer.put(paramChar);
/* 929 */     localCharBuffer.flip();
/* 930 */     return canEncode(localCharBuffer);
/*     */   }
/*     */ 
/*     */   public boolean canEncode(CharSequence paramCharSequence)
/*     */   {
/*     */     CharBuffer localCharBuffer;
/* 957 */     if ((paramCharSequence instanceof CharBuffer))
/* 958 */       localCharBuffer = ((CharBuffer)paramCharSequence).duplicate();
/*     */     else
/* 960 */       localCharBuffer = CharBuffer.wrap(paramCharSequence.toString());
/* 961 */     return canEncode(localCharBuffer);
/*     */   }
/*     */ 
/*     */   private void throwIllegalStateException(int paramInt1, int paramInt2)
/*     */   {
/* 968 */     throw new IllegalStateException("Current state = " + stateNames[paramInt1] + ", new state = " + stateNames[paramInt2]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.charset.CharsetEncoder
 * JD-Core Version:    0.6.2
 */