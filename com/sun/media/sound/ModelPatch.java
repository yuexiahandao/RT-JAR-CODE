/*    */ package com.sun.media.sound;
/*    */ 
/*    */ import javax.sound.midi.Patch;
/*    */ 
/*    */ public final class ModelPatch extends Patch
/*    */ {
/* 38 */   private boolean percussion = false;
/*    */ 
/*    */   public ModelPatch(int paramInt1, int paramInt2) {
/* 41 */     super(paramInt1, paramInt2);
/*    */   }
/*    */ 
/*    */   public ModelPatch(int paramInt1, int paramInt2, boolean paramBoolean) {
/* 45 */     super(paramInt1, paramInt2);
/* 46 */     this.percussion = paramBoolean;
/*    */   }
/*    */ 
/*    */   public boolean isPercussion() {
/* 50 */     return this.percussion;
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelPatch
 * JD-Core Version:    0.6.2
 */