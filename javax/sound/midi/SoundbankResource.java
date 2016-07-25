/*     */ package javax.sound.midi;
/*     */ 
/*     */ public abstract class SoundbankResource
/*     */ {
/*     */   private final Soundbank soundBank;
/*     */   private final String name;
/*     */   private final Class dataClass;
/*     */ 
/*     */   protected SoundbankResource(Soundbank paramSoundbank, String paramString, Class<?> paramClass)
/*     */   {
/* 110 */     this.soundBank = paramSoundbank;
/* 111 */     this.name = paramString;
/* 112 */     this.dataClass = paramClass;
/*     */   }
/*     */ 
/*     */   public Soundbank getSoundbank()
/*     */   {
/* 121 */     return this.soundBank;
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 131 */     return this.name;
/*     */   }
/*     */ 
/*     */   public Class<?> getDataClass()
/*     */   {
/* 144 */     return this.dataClass;
/*     */   }
/*     */ 
/*     */   public abstract Object getData();
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.SoundbankResource
 * JD-Core Version:    0.6.2
 */