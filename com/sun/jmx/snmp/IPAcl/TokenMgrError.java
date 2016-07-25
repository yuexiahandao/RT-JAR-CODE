/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ class TokenMgrError extends Error
/*     */ {
/*     */   private static final long serialVersionUID = -6373071623408870347L;
/*     */   static final int LEXICAL_ERROR = 0;
/*     */   static final int STATIC_LEXER_ERROR = 1;
/*     */   static final int INVALID_LEXICAL_STATE = 2;
/*     */   static final int LOOP_DETECTED = 3;
/*     */   int errorCode;
/*     */ 
/*     */   protected static final String addEscapes(String paramString)
/*     */   {
/*  68 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/*  70 */     for (int i = 0; i < paramString.length(); i++) {
/*  71 */       switch (paramString.charAt(i))
/*     */       {
/*     */       case '\000':
/*  74 */         break;
/*     */       case '\b':
/*  76 */         localStringBuffer.append("\\b");
/*  77 */         break;
/*     */       case '\t':
/*  79 */         localStringBuffer.append("\\t");
/*  80 */         break;
/*     */       case '\n':
/*  82 */         localStringBuffer.append("\\n");
/*  83 */         break;
/*     */       case '\f':
/*  85 */         localStringBuffer.append("\\f");
/*  86 */         break;
/*     */       case '\r':
/*  88 */         localStringBuffer.append("\\r");
/*  89 */         break;
/*     */       case '"':
/*  91 */         localStringBuffer.append("\\\"");
/*  92 */         break;
/*     */       case '\'':
/*  94 */         localStringBuffer.append("\\'");
/*  95 */         break;
/*     */       case '\\':
/*  97 */         localStringBuffer.append("\\\\");
/*  98 */         break;
/*     */       default:
/*     */         char c;
/* 100 */         if (((c = paramString.charAt(i)) < ' ') || (c > '~')) {
/* 101 */           String str = "0000" + Integer.toString(c, 16);
/* 102 */           localStringBuffer.append("\\u" + str.substring(str.length() - 4, str.length()));
/*     */         } else {
/* 104 */           localStringBuffer.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 109 */     return localStringBuffer.toString();
/*     */   }
/*     */ 
/*     */   private static final String LexicalError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar)
/*     */   {
/* 125 */     return "Lexical error at line " + paramInt2 + ", column " + paramInt3 + ".  Encountered: " + (paramBoolean ? "<EOF> " : new StringBuilder().append("\"").append(addEscapes(String.valueOf(paramChar))).append("\"").append(" (").append(paramChar).append("), ").toString()) + "after : \"" + addEscapes(paramString) + "\"";
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 142 */     return super.getMessage();
/*     */   }
/*     */ 
/*     */   public TokenMgrError()
/*     */   {
/*     */   }
/*     */ 
/*     */   public TokenMgrError(String paramString, int paramInt)
/*     */   {
/* 153 */     super(paramString);
/* 154 */     this.errorCode = paramInt;
/*     */   }
/*     */ 
/*     */   public TokenMgrError(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, String paramString, char paramChar, int paramInt4) {
/* 158 */     this(LexicalError(paramBoolean, paramInt1, paramInt2, paramInt3, paramString, paramChar), paramInt4);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.TokenMgrError
 * JD-Core Version:    0.6.2
 */