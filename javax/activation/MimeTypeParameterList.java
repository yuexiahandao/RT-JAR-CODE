/*     */ package javax.activation;
/*     */ 
/*     */ import java.util.Enumeration;
/*     */ import java.util.Hashtable;
/*     */ import java.util.Locale;
/*     */ 
/*     */ public class MimeTypeParameterList
/*     */ {
/*     */   private Hashtable parameters;
/*     */   private static final String TSPECIALS = "()<>@,;:/[]?=\\\"";
/*     */ 
/*     */   public MimeTypeParameterList()
/*     */   {
/*  54 */     this.parameters = new Hashtable();
/*     */   }
/*     */ 
/*     */   public MimeTypeParameterList(String parameterList)
/*     */     throws MimeTypeParseException
/*     */   {
/*  64 */     this.parameters = new Hashtable();
/*     */ 
/*  67 */     parse(parameterList);
/*     */   }
/*     */ 
/*     */   protected void parse(String parameterList)
/*     */     throws MimeTypeParseException
/*     */   {
/*  76 */     if (parameterList == null) {
/*  77 */       return;
/*     */     }
/*  79 */     int length = parameterList.length();
/*  80 */     if (length <= 0)
/*     */       return;
/*     */     char c;
/*  85 */     for (int i = skipWhiteSpace(parameterList, 0); 
/*  86 */       (i < length) && ((c = parameterList.charAt(i)) == ';'); 
/*  87 */       i = skipWhiteSpace(parameterList, i))
/*     */     {
/*  93 */       i++;
/*     */ 
/*  98 */       i = skipWhiteSpace(parameterList, i);
/*     */ 
/* 101 */       if (i >= length) {
/* 102 */         return;
/*     */       }
/*     */ 
/* 105 */       int lastIndex = i;
/* 106 */       while ((i < length) && (isTokenChar(parameterList.charAt(i)))) {
/* 107 */         i++;
/*     */       }
/* 109 */       String name = parameterList.substring(lastIndex, i).toLowerCase(Locale.ENGLISH);
/*     */ 
/* 113 */       i = skipWhiteSpace(parameterList, i);
/*     */ 
/* 115 */       if ((i >= length) || (parameterList.charAt(i) != '=')) {
/* 116 */         throw new MimeTypeParseException("Couldn't find the '=' that separates a parameter name from its value.");
/*     */       }
/*     */ 
/* 121 */       i++;
/* 122 */       i = skipWhiteSpace(parameterList, i);
/*     */ 
/* 124 */       if (i >= length) {
/* 125 */         throw new MimeTypeParseException("Couldn't find a value for parameter named " + name);
/*     */       }
/*     */ 
/* 129 */       c = parameterList.charAt(i);
/* 130 */       if (c == '"')
/*     */       {
/* 132 */         i++;
/* 133 */         if (i >= length) {
/* 134 */           throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */         }
/*     */ 
/* 137 */         lastIndex = i;
/*     */ 
/* 140 */         while (i < length) {
/* 141 */           c = parameterList.charAt(i);
/* 142 */           if (c == '"')
/*     */             break;
/* 144 */           if (c == '\\')
/*     */           {
/* 148 */             i++;
/*     */           }
/* 150 */           i++;
/*     */         }
/* 152 */         if (c != '"') {
/* 153 */           throw new MimeTypeParseException("Encountered unterminated quoted parameter value.");
/*     */         }
/*     */ 
/* 156 */         String value = unquote(parameterList.substring(lastIndex, i));
/*     */ 
/* 158 */         i++;
/*     */       }
/*     */       else
/*     */       {
/*     */         String value;
/* 159 */         if (isTokenChar(c))
/*     */         {
/* 162 */           lastIndex = i;
/* 163 */           while ((i < length) && (isTokenChar(parameterList.charAt(i))))
/* 164 */             i++;
/* 165 */           value = parameterList.substring(lastIndex, i);
/*     */         }
/*     */         else {
/* 168 */           throw new MimeTypeParseException("Unexpected character encountered at index " + i);
/*     */         }
/*     */       }
/*     */       String value;
/* 173 */       this.parameters.put(name, value);
/*     */     }
/* 175 */     if (i < length)
/* 176 */       throw new MimeTypeParseException("More characters encountered in input than expected.");
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 187 */     return this.parameters.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 196 */     return this.parameters.isEmpty();
/*     */   }
/*     */ 
/*     */   public String get(String name)
/*     */   {
/* 207 */     return (String)this.parameters.get(name.trim().toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   public void set(String name, String value)
/*     */   {
/* 218 */     this.parameters.put(name.trim().toLowerCase(Locale.ENGLISH), value);
/*     */   }
/*     */ 
/*     */   public void remove(String name)
/*     */   {
/* 227 */     this.parameters.remove(name.trim().toLowerCase(Locale.ENGLISH));
/*     */   }
/*     */ 
/*     */   public Enumeration getNames()
/*     */   {
/* 236 */     return this.parameters.keys();
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 243 */     StringBuffer buffer = new StringBuffer();
/* 244 */     buffer.ensureCapacity(this.parameters.size() * 16);
/*     */ 
/* 247 */     Enumeration keys = this.parameters.keys();
/* 248 */     while (keys.hasMoreElements()) {
/* 249 */       String key = (String)keys.nextElement();
/* 250 */       buffer.append("; ");
/* 251 */       buffer.append(key);
/* 252 */       buffer.append('=');
/* 253 */       buffer.append(quote((String)this.parameters.get(key)));
/*     */     }
/*     */ 
/* 256 */     return buffer.toString();
/*     */   }
/*     */ 
/*     */   private static boolean isTokenChar(char c)
/*     */   {
/* 265 */     return (c > ' ') && (c < '') && ("()<>@,;:/[]?=\\\"".indexOf(c) < 0);
/*     */   }
/*     */ 
/*     */   private static int skipWhiteSpace(String rawdata, int i)
/*     */   {
/* 273 */     int length = rawdata.length();
/* 274 */     while ((i < length) && (Character.isWhitespace(rawdata.charAt(i))))
/* 275 */       i++;
/* 276 */     return i;
/*     */   }
/*     */ 
/*     */   private static String quote(String value)
/*     */   {
/* 283 */     boolean needsQuotes = false;
/*     */ 
/* 286 */     int length = value.length();
/* 287 */     for (int i = 0; (i < length) && (!needsQuotes); i++) {
/* 288 */       needsQuotes = !isTokenChar(value.charAt(i));
/*     */     }
/*     */ 
/* 291 */     if (needsQuotes) {
/* 292 */       StringBuffer buffer = new StringBuffer();
/* 293 */       buffer.ensureCapacity((int)(length * 1.5D));
/*     */ 
/* 296 */       buffer.append('"');
/*     */ 
/* 299 */       for (int i = 0; i < length; i++) {
/* 300 */         char c = value.charAt(i);
/* 301 */         if ((c == '\\') || (c == '"'))
/* 302 */           buffer.append('\\');
/* 303 */         buffer.append(c);
/*     */       }
/*     */ 
/* 307 */       buffer.append('"');
/*     */ 
/* 309 */       return buffer.toString();
/*     */     }
/* 311 */     return value;
/*     */   }
/*     */ 
/*     */   private static String unquote(String value)
/*     */   {
/* 320 */     int valueLength = value.length();
/* 321 */     StringBuffer buffer = new StringBuffer();
/* 322 */     buffer.ensureCapacity(valueLength);
/*     */ 
/* 324 */     boolean escaped = false;
/* 325 */     for (int i = 0; i < valueLength; i++) {
/* 326 */       char currentChar = value.charAt(i);
/* 327 */       if ((!escaped) && (currentChar != '\\')) {
/* 328 */         buffer.append(currentChar);
/* 329 */       } else if (escaped) {
/* 330 */         buffer.append(currentChar);
/* 331 */         escaped = false;
/*     */       } else {
/* 333 */         escaped = true;
/*     */       }
/*     */     }
/*     */ 
/* 337 */     return buffer.toString();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.activation.MimeTypeParameterList
 * JD-Core Version:    0.6.2
 */