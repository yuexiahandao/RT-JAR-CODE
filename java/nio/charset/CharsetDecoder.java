/*     */ package java.nio.charset;
/*     */ 
/*     */ import java.nio.BufferOverflowException;
/*     */ import java.nio.BufferUnderflowException;
/*     */ import java.nio.ByteBuffer;
/*     */ import java.nio.CharBuffer;
/*     */ 
/*     */ public abstract class CharsetDecoder
/*     */ {
/*     */   private final Charset charset;
/*     */   private final float averageCharsPerByte;
/*     */   private final float maxCharsPerByte;
/*     */   private String replacement;
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
/*     */   private CharsetDecoder(Charset paramCharset, float paramFloat1, float paramFloat2, String paramString)
/*     */   {
/* 187 */     this.charset = paramCharset;
/* 188 */     if (paramFloat1 <= 0.0F) {
/* 189 */       throw new IllegalArgumentException("Non-positive averageCharsPerByte");
/*     */     }
/* 191 */     if (paramFloat2 <= 0.0F) {
/* 192 */       throw new IllegalArgumentException("Non-positive maxCharsPerByte");
/*     */     }
/* 194 */     if ((!Charset.atBugLevel("1.4")) && 
/* 195 */       (paramFloat1 > paramFloat2)) {
/* 196 */       throw new IllegalArgumentException("averageCharsPerByte exceeds maxCharsPerByte");
/*     */     }
/*     */ 
/* 200 */     this.replacement = paramString;
/* 201 */     this.averageCharsPerByte = paramFloat1;
/* 202 */     this.maxCharsPerByte = paramFloat2;
/* 203 */     replaceWith(paramString);
/*     */   }
/*     */ 
/*     */   protected CharsetDecoder(Charset paramCharset, float paramFloat1, float paramFloat2)
/*     */   {
/* 226 */     this(paramCharset, paramFloat1, paramFloat2, "�");
/*     */   }
/*     */ 
/*     */   public final Charset charset()
/*     */   {
/* 237 */     return this.charset;
/*     */   }
/*     */ 
/*     */   public final String replacement()
/*     */   {
/* 247 */     return this.replacement;
/*     */   }
/*     */ 
/*     */   public final CharsetDecoder replaceWith(String paramString)
/*     */   {
/* 276 */     if (paramString == null)
/* 277 */       throw new IllegalArgumentException("Null replacement");
/* 278 */     int i = paramString.length();
/* 279 */     if (i == 0)
/* 280 */       throw new IllegalArgumentException("Empty replacement");
/* 281 */     if (i > this.maxCharsPerByte) {
/* 282 */       throw new IllegalArgumentException("Replacement too long");
/*     */     }
/*     */ 
/* 287 */     this.replacement = paramString;
/* 288 */     implReplaceWith(paramString);
/* 289 */     return this;
/*     */   }
/*     */ 
/*     */   protected void implReplaceWith(String paramString)
/*     */   {
/*     */   }
/*     */ 
/*     */   public CodingErrorAction malformedInputAction()
/*     */   {
/* 350 */     return this.malformedInputAction;
/*     */   }
/*     */ 
/*     */   public final CharsetDecoder onMalformedInput(CodingErrorAction paramCodingErrorAction)
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
/*     */   public final CharsetDecoder onUnmappableCharacter(CodingErrorAction paramCodingErrorAction)
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
/*     */   public final float averageCharsPerByte()
/*     */   {
/* 435 */     return this.averageCharsPerByte;
/*     */   }
/*     */ 
/*     */   public final float maxCharsPerByte()
/*     */   {
/* 447 */     return this.maxCharsPerByte;
/*     */   }
/*     */ 
/*     */   public final CoderResult decode(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer, boolean paramBoolean)
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
/* 561 */         localCoderResult = decodeLoop(paramByteBuffer, paramCharBuffer);
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
/* 572 */         if ((paramBoolean) && (paramByteBuffer.hasRemaining())) {
/* 573 */           localCoderResult = CoderResult.malformedForLength(paramByteBuffer.remaining());
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
/* 592 */         if (paramCharBuffer.remaining() < this.replacement.length())
/* 593 */           return CoderResult.OVERFLOW;
/* 594 */         paramCharBuffer.put(this.replacement);
/*     */       }
/*     */ 
/* 597 */       if ((localCodingErrorAction == CodingErrorAction.IGNORE) || (localCodingErrorAction == CodingErrorAction.REPLACE))
/*     */       {
/* 600 */         paramByteBuffer.position(paramByteBuffer.position() + localCoderResult.length());
/*     */       }
/* 604 */       else if (!$assertionsDisabled) throw new AssertionError();
/*     */     }
/*     */   }
/*     */ 
/*     */   public final CoderResult flush(CharBuffer paramCharBuffer)
/*     */   {
/* 649 */     if (this.state == 2) {
/* 650 */       CoderResult localCoderResult = implFlush(paramCharBuffer);
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
/*     */   protected CoderResult implFlush(CharBuffer paramCharBuffer)
/*     */   {
/* 677 */     return CoderResult.UNDERFLOW;
/*     */   }
/*     */ 
/*     */   public final CharsetDecoder reset()
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
/*     */   protected abstract CoderResult decodeLoop(ByteBuffer paramByteBuffer, CharBuffer paramCharBuffer);
/*     */ 
/*     */   public final CharBuffer decode(ByteBuffer paramByteBuffer)
/*     */     throws CharacterCodingException
/*     */   {
/* 776 */     int i = (int)(paramByteBuffer.remaining() * averageCharsPerByte());
/* 777 */     Object localObject = CharBuffer.allocate(i);
/*     */ 
/* 779 */     if ((i == 0) && (paramByteBuffer.remaining() == 0))
/* 780 */       return localObject;
/* 781 */     reset();
/*     */     while (true) {
/* 783 */       CoderResult localCoderResult = paramByteBuffer.hasRemaining() ? decode(paramByteBuffer, (CharBuffer)localObject, true) : CoderResult.UNDERFLOW;
/*     */ 
/* 785 */       if (localCoderResult.isUnderflow()) {
/* 786 */         localCoderResult = flush((CharBuffer)localObject);
/*     */       }
/* 788 */       if (localCoderResult.isUnderflow())
/*     */         break;
/* 790 */       if (localCoderResult.isOverflow()) {
/* 791 */         i = 2 * i + 1;
/* 792 */         CharBuffer localCharBuffer = CharBuffer.allocate(i);
/* 793 */         ((CharBuffer)localObject).flip();
/* 794 */         localCharBuffer.put((CharBuffer)localObject);
/* 795 */         localObject = localCharBuffer;
/*     */       }
/*     */       else {
/* 798 */         localCoderResult.throwException();
/*     */       }
/*     */     }
/* 800 */     ((CharBuffer)localObject).flip();
/* 801 */     return localObject;
/*     */   }
/*     */ 
/*     */   public boolean isAutoDetecting()
/*     */   {
/* 817 */     return false;
/*     */   }
/*     */ 
/*     */   public boolean isCharsetDetected()
/*     */   {
/* 847 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   public Charset detectedCharset()
/*     */   {
/* 875 */     throw new UnsupportedOperationException();
/*     */   }
/*     */ 
/*     */   private void throwIllegalStateException(int paramInt1, int paramInt2)
/*     */   {
/* 968 */     throw new IllegalStateException("Current state = " + stateNames[paramInt1] + ", new state = " + stateNames[paramInt2]);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.nio.charset.CharsetDecoder
 * JD-Core Version:    0.6.2
 */