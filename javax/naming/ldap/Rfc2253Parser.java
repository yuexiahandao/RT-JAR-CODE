/*     */ package javax.naming.ldap;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import javax.naming.InvalidNameException;
/*     */ 
/*     */ final class Rfc2253Parser
/*     */ {
/*     */   private final String name;
/*     */   private final char[] chars;
/*     */   private final int len;
/*  41 */   private int cur = 0;
/*     */ 
/*     */   Rfc2253Parser(String paramString)
/*     */   {
/*  47 */     this.name = paramString;
/*  48 */     this.len = paramString.length();
/*  49 */     this.chars = paramString.toCharArray();
/*     */   }
/*     */ 
/*     */   List parseDn()
/*     */     throws InvalidNameException
/*     */   {
/*  58 */     this.cur = 0;
/*     */ 
/*  63 */     ArrayList localArrayList = new ArrayList(this.len / 3 + 10);
/*     */ 
/*  66 */     if (this.len == 0) {
/*  67 */       return localArrayList;
/*     */     }
/*     */ 
/*  70 */     localArrayList.add(doParse(new Rdn()));
/*  71 */     while (this.cur < this.len) {
/*  72 */       if ((this.chars[this.cur] == ',') || (this.chars[this.cur] == ';')) {
/*  73 */         this.cur += 1;
/*  74 */         localArrayList.add(0, doParse(new Rdn()));
/*     */       } else {
/*  76 */         throw new InvalidNameException("Invalid name: " + this.name);
/*     */       }
/*     */     }
/*  79 */     return localArrayList;
/*     */   }
/*     */ 
/*     */   Rdn parseRdn()
/*     */     throws InvalidNameException
/*     */   {
/*  86 */     return parseRdn(new Rdn());
/*     */   }
/*     */ 
/*     */   Rdn parseRdn(Rdn paramRdn)
/*     */     throws InvalidNameException
/*     */   {
/*  93 */     paramRdn = doParse(paramRdn);
/*  94 */     if (this.cur < this.len) {
/*  95 */       throw new InvalidNameException("Invalid RDN: " + this.name);
/*     */     }
/*  97 */     return paramRdn;
/*     */   }
/*     */ 
/*     */   private Rdn doParse(Rdn paramRdn)
/*     */     throws InvalidNameException
/*     */   {
/* 106 */     while (this.cur < this.len) {
/* 107 */       consumeWhitespace();
/* 108 */       String str1 = parseAttrType();
/* 109 */       consumeWhitespace();
/* 110 */       if ((this.cur >= this.len) || (this.chars[this.cur] != '=')) {
/* 111 */         throw new InvalidNameException("Invalid name: " + this.name);
/*     */       }
/* 113 */       this.cur += 1;
/* 114 */       consumeWhitespace();
/* 115 */       String str2 = parseAttrValue();
/* 116 */       consumeWhitespace();
/*     */ 
/* 118 */       paramRdn.put(str1, Rdn.unescapeValue(str2));
/* 119 */       if ((this.cur >= this.len) || (this.chars[this.cur] != '+')) {
/*     */         break;
/*     */       }
/* 122 */       this.cur += 1;
/*     */     }
/* 124 */     paramRdn.sort();
/* 125 */     return paramRdn;
/*     */   }
/*     */ 
/*     */   private String parseAttrType()
/*     */     throws InvalidNameException
/*     */   {
/* 137 */     int i = this.cur;
/* 138 */     while (this.cur < this.len) {
/* 139 */       char c = this.chars[this.cur];
/* 140 */       if ((!Character.isLetterOrDigit(c)) && (c != '.') && (c != '-') && (c != ' '))
/*     */       {
/*     */         break;
/*     */       }
/* 144 */       this.cur += 1;
/*     */     }
/*     */ 
/* 150 */     while ((this.cur > i) && (this.chars[(this.cur - 1)] == ' ')) {
/* 151 */       this.cur -= 1;
/*     */     }
/*     */ 
/* 154 */     if (i == this.cur) {
/* 155 */       throw new InvalidNameException("Invalid name: " + this.name);
/*     */     }
/* 157 */     return new String(this.chars, i, this.cur - i);
/*     */   }
/*     */ 
/*     */   private String parseAttrValue()
/*     */     throws InvalidNameException
/*     */   {
/* 166 */     if ((this.cur < this.len) && (this.chars[this.cur] == '#'))
/* 167 */       return parseBinaryAttrValue();
/* 168 */     if ((this.cur < this.len) && (this.chars[this.cur] == '"')) {
/* 169 */       return parseQuotedAttrValue();
/*     */     }
/* 171 */     return parseStringAttrValue();
/*     */   }
/*     */ 
/*     */   private String parseBinaryAttrValue() throws InvalidNameException
/*     */   {
/* 176 */     int i = this.cur;
/* 177 */     this.cur += 1;
/* 178 */     while ((this.cur < this.len) && (Character.isLetterOrDigit(this.chars[this.cur])))
/*     */     {
/* 180 */       this.cur += 1;
/*     */     }
/* 182 */     return new String(this.chars, i, this.cur - i);
/*     */   }
/*     */ 
/*     */   private String parseQuotedAttrValue() throws InvalidNameException
/*     */   {
/* 187 */     int i = this.cur;
/* 188 */     this.cur += 1;
/*     */ 
/* 190 */     while ((this.cur < this.len) && (this.chars[this.cur] != '"')) {
/* 191 */       if (this.chars[this.cur] == '\\') {
/* 192 */         this.cur += 1;
/*     */       }
/* 194 */       this.cur += 1;
/*     */     }
/* 196 */     if (this.cur >= this.len) {
/* 197 */       throw new InvalidNameException("Invalid name: " + this.name);
/*     */     }
/* 199 */     this.cur += 1;
/*     */ 
/* 201 */     return new String(this.chars, i, this.cur - i);
/*     */   }
/*     */ 
/*     */   private String parseStringAttrValue() throws InvalidNameException
/*     */   {
/* 206 */     int i = this.cur;
/* 207 */     int j = -1;
/*     */ 
/* 209 */     while ((this.cur < this.len) && (!atTerminator())) {
/* 210 */       if (this.chars[this.cur] == '\\') {
/* 211 */         this.cur += 1;
/* 212 */         j = this.cur;
/*     */       }
/* 214 */       this.cur += 1;
/*     */     }
/* 216 */     if (this.cur > this.len) {
/* 217 */       throw new InvalidNameException("Invalid name: " + this.name);
/*     */     }
/*     */ 
/* 222 */     for (int k = this.cur; (k > i) && 
/* 223 */       (isWhitespace(this.chars[(k - 1)])) && (j != k - 1); k--);
/* 227 */     return new String(this.chars, i, k - i);
/*     */   }
/*     */ 
/*     */   private void consumeWhitespace() {
/* 231 */     while ((this.cur < this.len) && (isWhitespace(this.chars[this.cur])))
/* 232 */       this.cur += 1;
/*     */   }
/*     */ 
/*     */   private boolean atTerminator()
/*     */   {
/* 241 */     return (this.cur < this.len) && ((this.chars[this.cur] == ',') || (this.chars[this.cur] == ';') || (this.chars[this.cur] == '+'));
/*     */   }
/*     */ 
/*     */   private static boolean isWhitespace(char paramChar)
/*     */   {
/* 251 */     return (paramChar == ' ') || (paramChar == '\r');
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.naming.ldap.Rfc2253Parser
 * JD-Core Version:    0.6.2
 */