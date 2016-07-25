/*     */ package javax.security.auth.callback;
/*     */ 
/*     */ import java.io.Serializable;
/*     */ 
/*     */ public class ConfirmationCallback
/*     */   implements Callback, Serializable
/*     */ {
/*     */   private static final long serialVersionUID = -9095656433782481624L;
/*     */   public static final int UNSPECIFIED_OPTION = -1;
/*     */   public static final int YES_NO_OPTION = 0;
/*     */   public static final int YES_NO_CANCEL_OPTION = 1;
/*     */   public static final int OK_CANCEL_OPTION = 2;
/*     */   public static final int YES = 0;
/*     */   public static final int NO = 1;
/*     */   public static final int CANCEL = 2;
/*     */   public static final int OK = 3;
/*     */   public static final int INFORMATION = 0;
/*     */   public static final int WARNING = 1;
/*     */   public static final int ERROR = 2;
/*     */   private String prompt;
/*     */   private int messageType;
/* 137 */   private int optionType = -1;
/*     */   private int defaultOption;
/*     */   private String[] options;
/*     */   private int selection;
/*     */ 
/*     */   public ConfirmationCallback(int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 189 */     if ((paramInt1 < 0) || (paramInt1 > 2) || (paramInt2 < 0) || (paramInt2 > 2))
/*     */     {
/* 191 */       throw new IllegalArgumentException();
/*     */     }
/* 193 */     switch (paramInt2) {
/*     */     case 0:
/* 195 */       if ((paramInt3 != 0) && (paramInt3 != 1))
/* 196 */         throw new IllegalArgumentException();
/*     */       break;
/*     */     case 1:
/* 199 */       if ((paramInt3 != 0) && (paramInt3 != 1) && (paramInt3 != 2))
/*     */       {
/* 201 */         throw new IllegalArgumentException();
/*     */       }break;
/*     */     case 2:
/* 204 */       if ((paramInt3 != 3) && (paramInt3 != 2)) {
/* 205 */         throw new IllegalArgumentException();
/*     */       }
/*     */       break;
/*     */     }
/* 209 */     this.messageType = paramInt1;
/* 210 */     this.optionType = paramInt2;
/* 211 */     this.defaultOption = paramInt3;
/*     */   }
/*     */ 
/*     */   public ConfirmationCallback(int paramInt1, String[] paramArrayOfString, int paramInt2)
/*     */   {
/* 248 */     if ((paramInt1 < 0) || (paramInt1 > 2) || (paramArrayOfString == null) || (paramArrayOfString.length == 0) || (paramInt2 < 0) || (paramInt2 >= paramArrayOfString.length))
/*     */     {
/* 251 */       throw new IllegalArgumentException();
/*     */     }
/* 253 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 254 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].length() == 0)) {
/* 255 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 258 */     this.messageType = paramInt1;
/* 259 */     this.options = paramArrayOfString;
/* 260 */     this.defaultOption = paramInt2;
/*     */   }
/*     */ 
/*     */   public ConfirmationCallback(String paramString, int paramInt1, int paramInt2, int paramInt3)
/*     */   {
/* 302 */     if ((paramString == null) || (paramString.length() == 0) || (paramInt1 < 0) || (paramInt1 > 2) || (paramInt2 < 0) || (paramInt2 > 2))
/*     */     {
/* 305 */       throw new IllegalArgumentException();
/*     */     }
/* 307 */     switch (paramInt2) {
/*     */     case 0:
/* 309 */       if ((paramInt3 != 0) && (paramInt3 != 1))
/* 310 */         throw new IllegalArgumentException();
/*     */       break;
/*     */     case 1:
/* 313 */       if ((paramInt3 != 0) && (paramInt3 != 1) && (paramInt3 != 2))
/*     */       {
/* 315 */         throw new IllegalArgumentException();
/*     */       }break;
/*     */     case 2:
/* 318 */       if ((paramInt3 != 3) && (paramInt3 != 2)) {
/* 319 */         throw new IllegalArgumentException();
/*     */       }
/*     */       break;
/*     */     }
/* 323 */     this.prompt = paramString;
/* 324 */     this.messageType = paramInt1;
/* 325 */     this.optionType = paramInt2;
/* 326 */     this.defaultOption = paramInt3;
/*     */   }
/*     */ 
/*     */   public ConfirmationCallback(String paramString, int paramInt1, String[] paramArrayOfString, int paramInt2)
/*     */   {
/* 367 */     if ((paramString == null) || (paramString.length() == 0) || (paramInt1 < 0) || (paramInt1 > 2) || (paramArrayOfString == null) || (paramArrayOfString.length == 0) || (paramInt2 < 0) || (paramInt2 >= paramArrayOfString.length))
/*     */     {
/* 371 */       throw new IllegalArgumentException();
/*     */     }
/* 373 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/* 374 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].length() == 0)) {
/* 375 */         throw new IllegalArgumentException();
/*     */       }
/*     */     }
/* 378 */     this.prompt = paramString;
/* 379 */     this.messageType = paramInt1;
/* 380 */     this.options = paramArrayOfString;
/* 381 */     this.defaultOption = paramInt2;
/*     */   }
/*     */ 
/*     */   public String getPrompt()
/*     */   {
/* 393 */     return this.prompt;
/*     */   }
/*     */ 
/*     */   public int getMessageType()
/*     */   {
/* 405 */     return this.messageType;
/*     */   }
/*     */ 
/*     */   public int getOptionType()
/*     */   {
/* 427 */     return this.optionType;
/*     */   }
/*     */ 
/*     */   public String[] getOptions()
/*     */   {
/* 440 */     return this.options;
/*     */   }
/*     */ 
/*     */   public int getDefaultOption()
/*     */   {
/* 459 */     return this.defaultOption;
/*     */   }
/*     */ 
/*     */   public void setSelectedIndex(int paramInt)
/*     */   {
/* 478 */     this.selection = paramInt;
/*     */   }
/*     */ 
/*     */   public int getSelectedIndex()
/*     */   {
/* 499 */     return this.selection;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.security.auth.callback.ConfirmationCallback
 * JD-Core Version:    0.6.2
 */