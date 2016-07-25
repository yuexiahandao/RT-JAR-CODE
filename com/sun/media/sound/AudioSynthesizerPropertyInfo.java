/*    */ package com.sun.media.sound;
/*    */ 
/*    */ public final class AudioSynthesizerPropertyInfo
/*    */ {
/*    */   public String name;
/* 61 */   public String description = null;
/*    */ 
/* 66 */   public Object value = null;
/*    */ 
/* 71 */   public Class valueClass = null;
/*    */ 
/* 77 */   public Object[] choices = null;
/*    */ 
/*    */   public AudioSynthesizerPropertyInfo(String paramString, Object paramObject)
/*    */   {
/* 44 */     this.name = paramString;
/* 45 */     if ((paramObject instanceof Class)) {
/* 46 */       this.valueClass = ((Class)paramObject);
/*    */     }
/*    */     else {
/* 49 */       this.value = paramObject;
/* 50 */       if (paramObject != null)
/* 51 */         this.valueClass = paramObject.getClass();
/*    */     }
/*    */   }
/*    */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.media.sound.AudioSynthesizerPropertyInfo
 * JD-Core Version:    0.6.2
 */