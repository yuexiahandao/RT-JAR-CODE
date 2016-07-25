/*     */ package javax.sound.sampled;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AudioFormat
/*     */ {
/*     */   protected Encoding encoding;
/*     */   protected float sampleRate;
/*     */   protected int sampleSizeInBits;
/*     */   protected int channels;
/*     */   protected int frameSize;
/*     */   protected float frameRate;
/*     */   protected boolean bigEndian;
/*     */   private HashMap<String, Object> properties;
/*     */ 
/*     */   public AudioFormat(Encoding paramEncoding, float paramFloat1, int paramInt1, int paramInt2, int paramInt3, float paramFloat2, boolean paramBoolean)
/*     */   {
/* 187 */     this.encoding = paramEncoding;
/* 188 */     this.sampleRate = paramFloat1;
/* 189 */     this.sampleSizeInBits = paramInt1;
/* 190 */     this.channels = paramInt2;
/* 191 */     this.frameSize = paramInt3;
/* 192 */     this.frameRate = paramFloat2;
/* 193 */     this.bigEndian = paramBoolean;
/* 194 */     this.properties = null;
/*     */   }
/*     */ 
/*     */   public AudioFormat(Encoding paramEncoding, float paramFloat1, int paramInt1, int paramInt2, int paramInt3, float paramFloat2, boolean paramBoolean, Map<String, Object> paramMap)
/*     */   {
/* 222 */     this(paramEncoding, paramFloat1, paramInt1, paramInt2, paramInt3, paramFloat2, paramBoolean);
/*     */ 
/* 224 */     this.properties = new HashMap(paramMap);
/*     */   }
/*     */ 
/*     */   public AudioFormat(float paramFloat, int paramInt1, int paramInt2, boolean paramBoolean1, boolean paramBoolean2)
/*     */   {
/* 245 */     this(paramBoolean1 == true ? Encoding.PCM_SIGNED : Encoding.PCM_UNSIGNED, paramFloat, paramInt1, paramInt2, (paramInt2 == -1) || (paramInt1 == -1) ? -1 : (paramInt1 + 7) / 8 * paramInt2, paramFloat, paramBoolean2);
/*     */   }
/*     */ 
/*     */   public Encoding getEncoding()
/*     */   {
/* 267 */     return this.encoding;
/*     */   }
/*     */ 
/*     */   public float getSampleRate()
/*     */   {
/* 289 */     return this.sampleRate;
/*     */   }
/*     */ 
/*     */   public int getSampleSizeInBits()
/*     */   {
/* 311 */     return this.sampleSizeInBits;
/*     */   }
/*     */ 
/*     */   public int getChannels()
/*     */   {
/* 329 */     return this.channels;
/*     */   }
/*     */ 
/*     */   public int getFrameSize()
/*     */   {
/* 349 */     return this.frameSize;
/*     */   }
/*     */ 
/*     */   public float getFrameRate()
/*     */   {
/* 369 */     return this.frameRate;
/*     */   }
/*     */ 
/*     */   public boolean isBigEndian()
/*     */   {
/* 382 */     return this.bigEndian;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> properties()
/*     */   {
/*     */     Object localObject;
/* 400 */     if (this.properties == null)
/* 401 */       localObject = new HashMap(0);
/*     */     else {
/* 403 */       localObject = (Map)this.properties.clone();
/*     */     }
/* 405 */     return Collections.unmodifiableMap((Map)localObject);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 426 */     if (this.properties == null) {
/* 427 */       return null;
/*     */     }
/* 429 */     return this.properties.get(paramString);
/*     */   }
/*     */ 
/*     */   public boolean matches(AudioFormat paramAudioFormat)
/*     */   {
/* 449 */     if ((paramAudioFormat.getEncoding().equals(getEncoding())) && ((paramAudioFormat.getChannels() == -1) || (paramAudioFormat.getChannels() == getChannels())) && ((paramAudioFormat.getSampleRate() == -1.0F) || (paramAudioFormat.getSampleRate() == getSampleRate())) && ((paramAudioFormat.getSampleSizeInBits() == -1) || (paramAudioFormat.getSampleSizeInBits() == getSampleSizeInBits())) && ((paramAudioFormat.getFrameRate() == -1.0F) || (paramAudioFormat.getFrameRate() == getFrameRate())) && ((paramAudioFormat.getFrameSize() == -1) || (paramAudioFormat.getFrameSize() == getFrameSize())) && ((getSampleSizeInBits() <= 8) || (paramAudioFormat.isBigEndian() == isBigEndian())))
/*     */     {
/* 462 */       return true;
/*     */     }
/* 464 */     return false;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 476 */     String str1 = "";
/* 477 */     if (getEncoding() != null)
/* 478 */       str1 = getEncoding().toString() + " ";
/*     */     String str2;
/* 482 */     if (getSampleRate() == -1.0F)
/* 483 */       str2 = "unknown sample rate, ";
/*     */     else
/* 485 */       str2 = "" + getSampleRate() + " Hz, ";
/*     */     String str3;
/* 489 */     if (getSampleSizeInBits() == -1.0F)
/* 490 */       str3 = "unknown bits per sample, ";
/*     */     else
/* 492 */       str3 = "" + getSampleSizeInBits() + " bit, ";
/*     */     String str4;
/* 496 */     if (getChannels() == 1) {
/* 497 */       str4 = "mono, ";
/*     */     }
/* 499 */     else if (getChannels() == 2) {
/* 500 */       str4 = "stereo, ";
/*     */     }
/* 502 */     else if (getChannels() == -1)
/* 503 */       str4 = " unknown number of channels, ";
/*     */     else
/* 505 */       str4 = "" + getChannels() + " channels, ";
/*     */     String str5;
/* 510 */     if (getFrameSize() == -1.0F)
/* 511 */       str5 = "unknown frame size, ";
/*     */     else {
/* 513 */       str5 = "" + getFrameSize() + " bytes/frame, ";
/*     */     }
/*     */ 
/* 516 */     String str6 = "";
/* 517 */     if (Math.abs(getSampleRate() - getFrameRate()) > 1.E-005D) {
/* 518 */       if (getFrameRate() == -1.0F)
/* 519 */         str6 = "unknown frame rate, ";
/*     */       else {
/* 521 */         str6 = getFrameRate() + " frames/second, ";
/*     */       }
/*     */     }
/*     */ 
/* 525 */     String str7 = "";
/* 526 */     if (((getEncoding().equals(Encoding.PCM_SIGNED)) || (getEncoding().equals(Encoding.PCM_UNSIGNED))) && ((getSampleSizeInBits() > 8) || (getSampleSizeInBits() == -1)))
/*     */     {
/* 530 */       if (isBigEndian())
/* 531 */         str7 = "big-endian";
/*     */       else {
/* 533 */         str7 = "little-endian";
/*     */       }
/*     */     }
/*     */ 
/* 537 */     return str1 + str2 + str3 + str4 + str5 + str6 + str7;
/*     */   }
/*     */ 
/*     */   public static class Encoding
/*     */   {
/* 587 */     public static final Encoding PCM_SIGNED = new Encoding("PCM_SIGNED");
/*     */ 
/* 592 */     public static final Encoding PCM_UNSIGNED = new Encoding("PCM_UNSIGNED");
/*     */ 
/* 599 */     public static final Encoding PCM_FLOAT = new Encoding("PCM_FLOAT");
/*     */ 
/* 604 */     public static final Encoding ULAW = new Encoding("ULAW");
/*     */ 
/* 609 */     public static final Encoding ALAW = new Encoding("ALAW");
/*     */     private String name;
/*     */ 
/*     */     public Encoding(String paramString)
/*     */     {
/* 627 */       this.name = paramString;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 637 */       if (toString() == null) {
/* 638 */         return (paramObject != null) && (paramObject.toString() == null);
/*     */       }
/* 640 */       if ((paramObject instanceof Encoding)) {
/* 641 */         return toString().equals(paramObject.toString());
/*     */       }
/* 643 */       return false;
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 650 */       if (toString() == null) {
/* 651 */         return 0;
/*     */       }
/* 653 */       return toString().hashCode();
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 665 */       return this.name;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.AudioFormat
 * JD-Core Version:    0.6.2
 */