/*    */ package javax.sound.sampled.spi;
/*    */ 
/*    */ import javax.sound.sampled.Mixer;
/*    */ import javax.sound.sampled.Mixer.Info;
/*    */ 
/*    */ public abstract class MixerProvider
/*    */ {
/*    */   public boolean isMixerSupported(Mixer.Info paramInfo)
/*    */   {
/* 57 */     Mixer.Info[] arrayOfInfo = getMixerInfo();
/*    */ 
/* 59 */     for (int i = 0; i < arrayOfInfo.length; i++) {
/* 60 */       if (paramInfo.equals(arrayOfInfo[i])) {
/* 61 */         return true;
/*    */       }
/*    */     }
/* 64 */     return false;
/*    */   }
/*    */ 
/*    */   public abstract Mixer.Info[] getMixerInfo();
/*    */ 
/*    */   public abstract Mixer getMixer(Mixer.Info paramInfo);
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.sound.sampled.spi.MixerProvider
 * JD-Core Version:    0.6.2
 */