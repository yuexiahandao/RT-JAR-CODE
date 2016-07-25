/*     */ package javax.sound.midi;
/*     */ 
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MidiFileFormat
/*     */ {
/*     */   public static final int UNKNOWN_LENGTH = -1;
/*     */   protected int type;
/*     */   protected float divisionType;
/*     */   protected int resolution;
/*     */   protected int byteLength;
/*     */   protected long microsecondLength;
/*     */   private HashMap<String, Object> properties;
/*     */ 
/*     */   public MidiFileFormat(int paramInt1, float paramFloat, int paramInt2, int paramInt3, long paramLong)
/*     */   {
/* 156 */     this.type = paramInt1;
/* 157 */     this.divisionType = paramFloat;
/* 158 */     this.resolution = paramInt2;
/* 159 */     this.byteLength = paramInt3;
/* 160 */     this.microsecondLength = paramLong;
/* 161 */     this.properties = null;
/*     */   }
/*     */ 
/*     */   public MidiFileFormat(int paramInt1, float paramFloat, int paramInt2, int paramInt3, long paramLong, Map<String, Object> paramMap)
/*     */   {
/* 190 */     this(paramInt1, paramFloat, paramInt2, paramInt3, paramLong);
/* 191 */     this.properties = new HashMap(paramMap);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/* 201 */     return this.type;
/*     */   }
/*     */ 
/*     */   public float getDivisionType()
/*     */   {
/* 218 */     return this.divisionType;
/*     */   }
/*     */ 
/*     */   public int getResolution()
/*     */   {
/* 232 */     return this.resolution;
/*     */   }
/*     */ 
/*     */   public int getByteLength()
/*     */   {
/* 242 */     return this.byteLength;
/*     */   }
/*     */ 
/*     */   public long getMicrosecondLength()
/*     */   {
/* 253 */     return this.microsecondLength;
/*     */   }
/*     */ 
/*     */   public Map<String, Object> properties()
/*     */   {
/*     */     Object localObject;
/* 270 */     if (this.properties == null)
/* 271 */       localObject = new HashMap(0);
/*     */     else {
/* 273 */       localObject = (Map)this.properties.clone();
/*     */     }
/* 275 */     return Collections.unmodifiableMap((Map)localObject);
/*     */   }
/*     */ 
/*     */   public Object getProperty(String paramString)
/*     */   {
/* 296 */     if (this.properties == null) {
/* 297 */       return null;
/*     */     }
/* 299 */     return this.properties.get(paramString);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.MidiFileFormat
 * JD-Core Version:    0.6.2
 */