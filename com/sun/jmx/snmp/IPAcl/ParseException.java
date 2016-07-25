/*     */ package com.sun.jmx.snmp.IPAcl;
/*     */ 
/*     */ class ParseException extends Exception
/*     */ {
/*     */   private static final long serialVersionUID = -3695190720704845876L;
/*     */   protected boolean specialConstructor;
/*     */   public Token currentToken;
/*     */   public int[][] expectedTokenSequences;
/*     */   public String[] tokenImage;
/* 165 */   protected String eol = System.getProperty("line.separator", "\n");
/*     */ 
/*     */   public ParseException(Token paramToken, int[][] paramArrayOfInt, String[] paramArrayOfString)
/*     */   {
/*  58 */     super("");
/*  59 */     this.specialConstructor = true;
/*  60 */     this.currentToken = paramToken;
/*  61 */     this.expectedTokenSequences = paramArrayOfInt;
/*  62 */     this.tokenImage = paramArrayOfString;
/*     */   }
/*     */ 
/*     */   public ParseException()
/*     */   {
/*  77 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public ParseException(String paramString) {
/*  81 */     super(paramString);
/*  82 */     this.specialConstructor = false;
/*     */   }
/*     */ 
/*     */   public String getMessage()
/*     */   {
/* 124 */     if (!this.specialConstructor) {
/* 125 */       return super.getMessage();
/*     */     }
/* 127 */     String str1 = "";
/* 128 */     int i = 0;
/* 129 */     for (int j = 0; j < this.expectedTokenSequences.length; j++) {
/* 130 */       if (i < this.expectedTokenSequences[j].length) {
/* 131 */         i = this.expectedTokenSequences[j].length;
/*     */       }
/* 133 */       for (int k = 0; k < this.expectedTokenSequences[j].length; k++) {
/* 134 */         str1 = str1 + this.tokenImage[this.expectedTokenSequences[j][k]] + " ";
/*     */       }
/* 136 */       if (this.expectedTokenSequences[j][(this.expectedTokenSequences[j].length - 1)] != 0) {
/* 137 */         str1 = str1 + "...";
/*     */       }
/* 139 */       str1 = str1 + this.eol + "    ";
/*     */     }
/* 141 */     String str2 = "Encountered \"";
/* 142 */     Token localToken = this.currentToken.next;
/* 143 */     for (int m = 0; m < i; m++) {
/* 144 */       if (m != 0) str2 = str2 + " ";
/* 145 */       if (localToken.kind == 0) {
/* 146 */         str2 = str2 + this.tokenImage[0];
/* 147 */         break;
/*     */       }
/* 149 */       str2 = str2 + add_escapes(localToken.image);
/* 150 */       localToken = localToken.next;
/*     */     }
/* 152 */     str2 = str2 + "\" at line " + this.currentToken.next.beginLine + ", column " + this.currentToken.next.beginColumn + "." + this.eol;
/* 153 */     if (this.expectedTokenSequences.length == 1)
/* 154 */       str2 = str2 + "Was expecting:" + this.eol + "    ";
/*     */     else {
/* 156 */       str2 = str2 + "Was expecting one of:" + this.eol + "    ";
/*     */     }
/* 158 */     str2 = str2 + str1;
/* 159 */     return str2;
/*     */   }
/*     */ 
/*     */   protected String add_escapes(String paramString)
/*     */   {
/* 173 */     StringBuffer localStringBuffer = new StringBuffer();
/*     */ 
/* 175 */     for (int i = 0; i < paramString.length(); i++) {
/* 176 */       switch (paramString.charAt(i))
/*     */       {
/*     */       case '\000':
/* 179 */         break;
/*     */       case '\b':
/* 181 */         localStringBuffer.append("\\b");
/* 182 */         break;
/*     */       case '\t':
/* 184 */         localStringBuffer.append("\\t");
/* 185 */         break;
/*     */       case '\n':
/* 187 */         localStringBuffer.append("\\n");
/* 188 */         break;
/*     */       case '\f':
/* 190 */         localStringBuffer.append("\\f");
/* 191 */         break;
/*     */       case '\r':
/* 193 */         localStringBuffer.append("\\r");
/* 194 */         break;
/*     */       case '"':
/* 196 */         localStringBuffer.append("\\\"");
/* 197 */         break;
/*     */       case '\'':
/* 199 */         localStringBuffer.append("\\'");
/* 200 */         break;
/*     */       case '\\':
/* 202 */         localStringBuffer.append("\\\\");
/* 203 */         break;
/*     */       default:
/*     */         char c;
/* 205 */         if (((c = paramString.charAt(i)) < ' ') || (c > '~')) {
/* 206 */           String str = "0000" + Integer.toString(c, 16);
/* 207 */           localStringBuffer.append("\\u" + str.substring(str.length() - 4, str.length()));
/*     */         } else {
/* 209 */           localStringBuffer.append(c);
/*     */         }
/*     */         break;
/*     */       }
/*     */     }
/* 214 */     return localStringBuffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.snmp.IPAcl.ParseException
 * JD-Core Version:    0.6.2
 */