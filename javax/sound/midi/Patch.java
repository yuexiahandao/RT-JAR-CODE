/*     */ package javax.sound.midi;
/*     */ 
/*     */ public class Patch
/*     */ {
/*     */   private final int bank;
/*     */   private final int program;
/*     */ 
/*     */   public Patch(int paramInt1, int paramInt2)
/*     */   {
/*  82 */     this.bank = paramInt1;
/*  83 */     this.program = paramInt2;
/*     */   }
/*     */ 
/*     */   public int getBank()
/*     */   {
/*  95 */     return this.bank;
/*     */   }
/*     */ 
/*     */   public int getProgram()
/*     */   {
/* 110 */     return this.program;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.midi.Patch
 * JD-Core Version:    0.6.2
 */