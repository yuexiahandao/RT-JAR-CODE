/*    */ package com.sun.media.sound;
/*    */ 
/*    */ public final class ModelStandardDirector
/*    */   implements ModelDirector
/*    */ {
/*    */   ModelPerformer[] performers;
/*    */   ModelDirectedPlayer player;
/* 37 */   boolean noteOnUsed = false;
/* 38 */   boolean noteOffUsed = false;
/*    */ 
/*    */   public ModelStandardDirector(ModelPerformer[] paramArrayOfModelPerformer, ModelDirectedPlayer paramModelDirectedPlayer)
/*    */   {
/* 42 */     this.performers = paramArrayOfModelPerformer;
/* 43 */     this.player = paramModelDirectedPlayer;
/* 44 */     for (int i = 0; i < paramArrayOfModelPerformer.length; i++) {
/* 45 */       ModelPerformer localModelPerformer = paramArrayOfModelPerformer[i];
/* 46 */       if (localModelPerformer.isReleaseTriggered())
/* 47 */         this.noteOffUsed = true;
/*    */       else
/* 49 */         this.noteOnUsed = true;
/*    */     }
/*    */   }
/*    */ 
/*    */   public void close()
/*    */   {
/*    */   }
/*    */ 
/*    */   public void noteOff(int paramInt1, int paramInt2) {
/* 58 */     if (!this.noteOffUsed)
/* 59 */       return;
/* 60 */     for (int i = 0; i < this.performers.length; i++) {
/* 61 */       ModelPerformer localModelPerformer = this.performers[i];
/* 62 */       if ((localModelPerformer.getKeyFrom() <= paramInt1) && (localModelPerformer.getKeyTo() >= paramInt1) && 
/* 63 */         (localModelPerformer.getVelFrom() <= paramInt2) && (localModelPerformer.getVelTo() >= paramInt2) && 
/* 64 */         (localModelPerformer.isReleaseTriggered()))
/* 65 */         this.player.play(i, null);
/*    */     }
/*    */   }
/*    */ 
/*    */   public void noteOn(int paramInt1, int paramInt2)
/*    */   {
/* 73 */     if (!this.noteOnUsed)
/* 74 */       return;
/* 75 */     for (int i = 0; i < this.performers.length; i++) {
/* 76 */       ModelPerformer localModelPerformer = this.performers[i];
/* 77 */       if ((localModelPerformer.getKeyFrom() <= paramInt1) && (localModelPerformer.getKeyTo() >= paramInt1) && 
/* 78 */         (localModelPerformer.getVelFrom() <= paramInt2) && (localModelPerformer.getVelTo() >= paramInt2) && 
/* 79 */         (!localModelPerformer.isReleaseTriggered()))
/* 80 */         this.player.play(i, null);
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.ModelStandardDirector
 * JD-Core Version:    0.6.2
 */