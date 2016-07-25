/*     */ package org.omg.CORBA;
/*     */ 
/*     */ public abstract class SystemException extends RuntimeException
/*     */ {
/*     */   public int minor;
/*     */   public CompletionStatus completed;
/*     */ 
/*     */   protected SystemException(String paramString, int paramInt, CompletionStatus paramCompletionStatus)
/*     */   {
/*  74 */     super(paramString);
/*  75 */     this.minor = paramInt;
/*  76 */     this.completed = paramCompletionStatus;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/*  84 */     String str = super.toString();
/*     */ 
/*  87 */     int i = this.minor & 0xFFFFF000;
/*  88 */     switch (i) {
/*     */     case 1330446336:
/*  90 */       str = str + "  vmcid: OMG";
/*  91 */       break;
/*     */     case 1398079488:
/*  93 */       str = str + "  vmcid: SUN";
/*  94 */       break;
/*     */     default:
/*  96 */       str = str + "  vmcid: 0x" + Integer.toHexString(i);
/*     */     }
/*     */ 
/* 101 */     int j = this.minor & 0xFFF;
/* 102 */     str = str + "  minor code: " + j;
/*     */ 
/* 105 */     switch (this.completed.value()) {
/*     */     case 0:
/* 107 */       str = str + "  completed: Yes";
/* 108 */       break;
/*     */     case 1:
/* 110 */       str = str + "  completed: No";
/* 111 */       break;
/*     */     case 2:
/*     */     default:
/* 114 */       str = str + " completed: Maybe";
/*     */     }
/*     */ 
/* 117 */     return str;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.SystemException
 * JD-Core Version:    0.6.2
 */