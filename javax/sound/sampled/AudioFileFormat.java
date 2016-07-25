/*     */ package javax.sound.sampled;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class AudioFileFormat
/*     */ {
/*     */   private Type type;
/*     */   private int byteLength;
/*     */   private AudioFormat format;
/*     */   private int frameLength;
/*     */   private HashMap<String, Object> properties;
/*     */ 
/*     */   protected AudioFileFormat(Type paramType, int paramInt1, AudioFormat paramAudioFormat, int paramInt2)
/*     */   {
/* 149 */     this.type = paramType;
/* 150 */     this.byteLength = paramInt1;
/* 151 */     this.format = paramAudioFormat;
/* 152 */     this.frameLength = paramInt2;
/* 153 */     this.properties = null;
/*     */   }
/*     */ 
/*     */   public AudioFileFormat(Type paramType, AudioFormat paramAudioFormat, int paramInt)
/*     */   {
/* 168 */     this(paramType, -1, paramAudioFormat, paramInt);
/*     */   }
/*     */ 
/*     */   public AudioFileFormat(Type paramType, AudioFormat paramAudioFormat, int paramInt, Map<String, Object> paramMap)
/*     */   {
/* 189 */     this(paramType, -1, paramAudioFormat, paramInt);
/* 190 */     this.properties = new HashMap(paramMap);
/*     */   }
/*     */ 
/*     */   public Type getType()
/*     */   {
/* 205 */     return this.type;
/*     */   }
/*     */ 
/*     */   public int getByteLength()
/*     */   {
/* 214 */     return this.byteLength;
/*     */   }
/*     */ 
/*     */   public AudioFormat getFormat()
/*     */   {
/* 222 */     return this.format;
/*     */   }
/*     */ 
/*     */   public int getFrameLength()
/*     */   {
/* 231 */     return this.frameLength;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> properties()
/*     */   {
/*     */     Object localObject;
/* 248 */     if (this.properties == null)
/* 249 */       localObject = new HashMap(0);
/*     */     else {
/* 251 */       localObject = (Map)this.properties.clone();
/*     */     }
/* 253 */     return Collections.unmodifiableMap((Map)localObject);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 274 */     if (this.properties == null) {
/* 275 */       return null;
/*     */     }
/* 277 */     return this.properties.get(paramString);
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 287 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 290 */     if (this.type != null)
/* 291 */       localStringBuffer.append(this.type.toString() + " (." + this.type.getExtension() + ") file");
/*     */     else {
/* 293 */       localStringBuffer.append("unknown file format");
/*     */     }
/*     */ 
/* 296 */     if (this.byteLength != -1) {
/* 297 */       localStringBuffer.append(", byte length: " + this.byteLength);
/*     */     }
/*     */ 
/* 300 */     localStringBuffer.append(", data format: " + this.format);
/*     */ 
/* 302 */     if (this.frameLength != -1) {
/* 303 */       localStringBuffer.append(", frame length: " + this.frameLength);
/*     */     }
/*     */ 
/* 306 */     return new String(localStringBuffer);
/*     */   }
/*     */ 
/*     */   public static class Type
/*     */   {
/* 322 */     public static final Type WAVE = new Type("WAVE", "wav");
/*     */ 
/* 327 */     public static final Type AU = new Type("AU", "au");
/*     */ 
/* 332 */     public static final Type AIFF = new Type("AIFF", "aif");
/*     */ 
/* 337 */     public static final Type AIFC = new Type("AIFF-C", "aifc");
/*     */ 
/* 342 */     public static final Type SND = new Type("SND", "snd");
/*     */     private final String name;
/*     */     private final String extension;
/*     */ 
/*     */     public Type(String paramString1, String paramString2)
/*     */     {
/* 368 */       this.name = paramString1;
/* 369 */       this.extension = paramString2;
/*     */     }
/*     */ 
/*     */     public final boolean equals(Object paramObject)
/*     */     {
/* 379 */       if (toString() == null) {
/* 380 */         return (paramObject != null) && (paramObject.toString() == null);
/*     */       }
/* 382 */       if ((paramObject instanceof Type)) {
/* 383 */         return toString().equals(paramObject.toString());
/*     */       }
/* 385 */       return false;
/*     */     }
/*     */ 
/*     */     public final int hashCode()
/*     */     {
/* 392 */       if (toString() == null) {
/* 393 */         return 0;
/*     */       }
/* 395 */       return toString().hashCode();
/*     */     }
/*     */ 
/*     */     public final String toString()
/*     */     {
/* 404 */       return this.name;
/*     */     }
/*     */ 
/*     */     public String getExtension()
/*     */     {
/* 412 */       return this.extension;
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.AudioFileFormat
 * JD-Core Version:    0.6.2
 */