/*     */ package sun.org.mozilla.javascript.internal.json;
/*     */ 
/*     */ import java.util.ArrayList;
/*     */ import java.util.List;
/*     */ import sun.org.mozilla.javascript.internal.Context;
/*     */ import sun.org.mozilla.javascript.internal.ScriptRuntime;
/*     */ import sun.org.mozilla.javascript.internal.Scriptable;
/*     */ 
/*     */ public class JsonParser
/*     */ {
/*     */   private Context cx;
/*     */   private Scriptable scope;
/*     */   private int pos;
/*     */   private int length;
/*     */   private String src;
/*     */ 
/*     */   public JsonParser(Context paramContext, Scriptable paramScriptable)
/*     */   {
/*  67 */     this.cx = paramContext;
/*  68 */     this.scope = paramScriptable;
/*     */   }
/*     */ 
/*     */   public synchronized Object parseValue(String paramString) throws JsonParser.ParseException {
/*  72 */     if (paramString == null) {
/*  73 */       throw new ParseException("Input string may not be null");
/*     */     }
/*  75 */     this.pos = 0;
/*  76 */     this.length = paramString.length();
/*  77 */     this.src = paramString;
/*  78 */     Object localObject = readValue();
/*  79 */     consumeWhitespace();
/*  80 */     if (this.pos < this.length) {
/*  81 */       throw new ParseException("Expected end of stream at char " + this.pos);
/*     */     }
/*  83 */     return localObject;
/*     */   }
/*     */ 
/*     */   private Object readValue() throws JsonParser.ParseException {
/*  87 */     consumeWhitespace();
/*  88 */     if (this.pos < this.length) {
/*  89 */       char c = this.src.charAt(this.pos++);
/*  90 */       switch (c) {
/*     */       case '{':
/*  92 */         return readObject();
/*     */       case '[':
/*  94 */         return readArray();
/*     */       case 't':
/*  96 */         return readTrue();
/*     */       case 'f':
/*  98 */         return readFalse();
/*     */       case '"':
/* 100 */         return readString();
/*     */       case 'n':
/* 102 */         return readNull();
/*     */       case '-':
/*     */       case '0':
/*     */       case '1':
/*     */       case '2':
/*     */       case '3':
/*     */       case '4':
/*     */       case '5':
/*     */       case '6':
/*     */       case '7':
/*     */       case '8':
/*     */       case '9':
/* 114 */         return readNumber(c);
/*     */       }
/* 116 */       throw new ParseException("Unexpected token: " + c);
/*     */     }
/*     */ 
/* 119 */     throw new ParseException("Empty JSON string");
/*     */   }
/*     */ 
/*     */   private Object readObject() throws JsonParser.ParseException {
/* 123 */     Scriptable localScriptable = this.cx.newObject(this.scope);
/*     */ 
/* 126 */     int i = 0;
/* 127 */     consumeWhitespace();
/* 128 */     while (this.pos < this.length) {
/* 129 */       int j = this.src.charAt(this.pos++);
/* 130 */       switch (j) {
/*     */       case 125:
/* 132 */         return localScriptable;
/*     */       case 44:
/* 134 */         if (i == 0) {
/* 135 */           throw new ParseException("Unexpected comma in object literal");
/*     */         }
/* 137 */         i = 0;
/* 138 */         break;
/*     */       case 34:
/* 140 */         if (i != 0) {
/* 141 */           throw new ParseException("Missing comma in object literal");
/*     */         }
/* 143 */         String str = readString();
/* 144 */         consume(':');
/* 145 */         Object localObject = readValue();
/*     */ 
/* 147 */         double d = ScriptRuntime.toNumber(str);
/* 148 */         int k = (int)d;
/* 149 */         if (d != k)
/* 150 */           localScriptable.put(str, localScriptable, localObject);
/*     */         else {
/* 152 */           localScriptable.put(k, localScriptable, localObject);
/*     */         }
/*     */ 
/* 155 */         i = 1;
/* 156 */         break;
/*     */       default:
/* 158 */         throw new ParseException("Unexpected token in object literal");
/*     */       }
/* 160 */       consumeWhitespace();
/*     */     }
/* 162 */     throw new ParseException("Unterminated object literal");
/*     */   }
/*     */ 
/*     */   private Object readArray() throws JsonParser.ParseException {
/* 166 */     ArrayList localArrayList = new ArrayList();
/* 167 */     int i = 0;
/* 168 */     consumeWhitespace();
/* 169 */     while (this.pos < this.length) {
/* 170 */       int j = this.src.charAt(this.pos);
/* 171 */       switch (j) {
/*     */       case 93:
/* 173 */         this.pos += 1;
/* 174 */         return this.cx.newArray(this.scope, localArrayList.toArray());
/*     */       case 44:
/* 176 */         if (i == 0) {
/* 177 */           throw new ParseException("Unexpected comma in array literal");
/*     */         }
/* 179 */         i = 0;
/* 180 */         this.pos += 1;
/* 181 */         break;
/*     */       default:
/* 183 */         if (i != 0) {
/* 184 */           throw new ParseException("Missing comma in array literal");
/*     */         }
/* 186 */         localArrayList.add(readValue());
/* 187 */         i = 1;
/*     */       }
/* 189 */       consumeWhitespace();
/*     */     }
/* 191 */     throw new ParseException("Unterminated array literal");
/*     */   }
/*     */ 
/*     */   private String readString() throws JsonParser.ParseException {
/* 195 */     StringBuilder localStringBuilder = new StringBuilder();
/* 196 */     while (this.pos < this.length) {
/* 197 */       char c = this.src.charAt(this.pos++);
/* 198 */       if (c <= '\037') {
/* 199 */         throw new ParseException("String contains control character");
/*     */       }
/* 201 */       switch (c) {
/*     */       case '\\':
/* 203 */         if (this.pos >= this.length) {
/* 204 */           throw new ParseException("Unterminated string");
/*     */         }
/* 206 */         c = this.src.charAt(this.pos++);
/* 207 */         switch (c) {
/*     */         case '"':
/* 209 */           localStringBuilder.append('"');
/* 210 */           break;
/*     */         case '\\':
/* 212 */           localStringBuilder.append('\\');
/* 213 */           break;
/*     */         case '/':
/* 215 */           localStringBuilder.append('/');
/* 216 */           break;
/*     */         case 'b':
/* 218 */           localStringBuilder.append('\b');
/* 219 */           break;
/*     */         case 'f':
/* 221 */           localStringBuilder.append('\f');
/* 222 */           break;
/*     */         case 'n':
/* 224 */           localStringBuilder.append('\n');
/* 225 */           break;
/*     */         case 'r':
/* 227 */           localStringBuilder.append('\r');
/* 228 */           break;
/*     */         case 't':
/* 230 */           localStringBuilder.append('\t');
/* 231 */           break;
/*     */         case 'u':
/* 233 */           if (this.length - this.pos < 5)
/* 234 */             throw new ParseException("Invalid character code: \\u" + this.src.substring(this.pos));
/*     */           try
/*     */           {
/* 237 */             localStringBuilder.append((char)Integer.parseInt(this.src.substring(this.pos, this.pos + 4), 16));
/* 238 */             this.pos += 4;
/*     */           } catch (NumberFormatException localNumberFormatException) {
/* 240 */             throw new ParseException("Invalid character code: " + this.src.substring(this.pos, this.pos + 4));
/*     */           }
/*     */ 
/*     */         default:
/* 244 */           throw new ParseException("Unexcpected character in string: '\\" + c + "'");
/*     */         }
/*     */         break;
/*     */       case '"':
/* 248 */         return localStringBuilder.toString();
/*     */       default:
/* 250 */         localStringBuilder.append(c);
/*     */       }
/*     */     }
/*     */ 
/* 254 */     throw new ParseException("Unterminated string literal");
/*     */   }
/*     */ 
/*     */   private Number readNumber(char paramChar) throws JsonParser.ParseException {
/* 258 */     StringBuilder localStringBuilder = new StringBuilder();
/* 259 */     localStringBuilder.append(paramChar);
/* 260 */     while (this.pos < this.length) {
/* 261 */       char c1 = this.src.charAt(this.pos);
/* 262 */       if ((!Character.isDigit(c1)) && (c1 != '-') && (c1 != '+') && (c1 != '.') && (c1 != 'e') && (c1 != 'E'))
/*     */       {
/*     */         break;
/*     */       }
/*     */ 
/* 270 */       this.pos += 1;
/* 271 */       localStringBuilder.append(c1);
/*     */     }
/* 273 */     String str = localStringBuilder.toString();
/* 274 */     int i = str.length();
/*     */     try
/*     */     {
/* 277 */       for (int j = 0; j < i; j++) {
/* 278 */         char c2 = str.charAt(j);
/* 279 */         if (Character.isDigit(c2)) {
/* 280 */           if ((c2 != '0') || (i <= j + 1) || (!Character.isDigit(str.charAt(j + 1)))) {
/*     */             break;
/*     */           }
/* 283 */           throw new ParseException("Unsupported number format: " + str);
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 288 */       double d = Double.parseDouble(str);
/* 289 */       int k = (int)d;
/* 290 */       if (k == d) {
/* 291 */         return Integer.valueOf(k);
/*     */       }
/* 293 */       return Double.valueOf(d);
/*     */     } catch (NumberFormatException localNumberFormatException) {
/*     */     }
/* 296 */     throw new ParseException("Unsupported number format: " + str);
/*     */   }
/*     */ 
/*     */   private Boolean readTrue() throws JsonParser.ParseException
/*     */   {
/* 301 */     if ((this.length - this.pos < 3) || (this.src.charAt(this.pos) != 'r') || (this.src.charAt(this.pos + 1) != 'u') || (this.src.charAt(this.pos + 2) != 'e'))
/*     */     {
/* 305 */       throw new ParseException("Unexpected token: t");
/*     */     }
/* 307 */     this.pos += 3;
/* 308 */     return Boolean.TRUE;
/*     */   }
/*     */ 
/*     */   private Boolean readFalse() throws JsonParser.ParseException {
/* 312 */     if ((this.length - this.pos < 4) || (this.src.charAt(this.pos) != 'a') || (this.src.charAt(this.pos + 1) != 'l') || (this.src.charAt(this.pos + 2) != 's') || (this.src.charAt(this.pos + 3) != 'e'))
/*     */     {
/* 317 */       throw new ParseException("Unexpected token: f");
/*     */     }
/* 319 */     this.pos += 4;
/* 320 */     return Boolean.FALSE;
/*     */   }
/*     */ 
/*     */   private Object readNull() throws JsonParser.ParseException {
/* 324 */     if ((this.length - this.pos < 3) || (this.src.charAt(this.pos) != 'u') || (this.src.charAt(this.pos + 1) != 'l') || (this.src.charAt(this.pos + 2) != 'l'))
/*     */     {
/* 328 */       throw new ParseException("Unexpected token: n");
/*     */     }
/* 330 */     this.pos += 3;
/* 331 */     return null;
/*     */   }
/*     */ 
/*     */   private void consumeWhitespace() {
/* 335 */     while (this.pos < this.length) {
/* 336 */       int i = this.src.charAt(this.pos);
/* 337 */       switch (i) {
/*     */       case 9:
/*     */       case 10:
/*     */       case 13:
/*     */       case 32:
/* 342 */         this.pos += 1;
/* 343 */         break;
/*     */       default:
/* 345 */         return;
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   private void consume(char paramChar) throws JsonParser.ParseException {
/* 351 */     consumeWhitespace();
/* 352 */     if (this.pos >= this.length) {
/* 353 */       throw new ParseException("Expected " + paramChar + " but reached end of stream");
/*     */     }
/* 355 */     char c = this.src.charAt(this.pos++);
/* 356 */     if (c == paramChar) {
/* 357 */       return;
/*     */     }
/* 359 */     throw new ParseException("Expected " + paramChar + " found " + c);
/*     */   }
/*     */ 
/*     */   public static class ParseException extends Exception
/*     */   {
/*     */     ParseException(String paramString) {
/* 365 */       super();
/*     */     }
/*     */ 
/*     */     ParseException(Exception paramException) {
/* 369 */       super();
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.org.mozilla.javascript.internal.json.JsonParser
 * JD-Core Version:    0.6.2
 */