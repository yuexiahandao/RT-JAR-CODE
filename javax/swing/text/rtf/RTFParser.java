/*     */ package javax.swing.text.rtf;
/*     */ 
/*     */ import java.io.ByteArrayOutputStream;
/*     */ import java.io.IOException;
/*     */ import java.io.PrintStream;
/*     */ 
/*     */ abstract class RTFParser extends AbstractFilter
/*     */ {
/*     */   public int level;
/*     */   private int state;
/*     */   private StringBuffer currentCharacters;
/*     */   private String pendingKeyword;
/*     */   private int pendingCharacter;
/*     */   private long binaryBytesLeft;
/*     */   ByteArrayOutputStream binaryBuf;
/*     */   private boolean[] savedSpecials;
/*     */   protected PrintStream warnings;
/*  62 */   private final int S_text = 0;
/*  63 */   private final int S_backslashed = 1;
/*  64 */   private final int S_token = 2;
/*  65 */   private final int S_parameter = 3;
/*     */ 
/*  67 */   private final int S_aftertick = 4;
/*  68 */   private final int S_aftertickc = 5;
/*     */ 
/*  70 */   private final int S_inblob = 6;
/*     */ 
/*  95 */   static final boolean[] rtfSpecialsTable = (boolean[])noSpecialsTable.clone();
/*     */ 
/*     */   public abstract boolean handleKeyword(String paramString);
/*     */ 
/*     */   public abstract boolean handleKeyword(String paramString, int paramInt);
/*     */ 
/*     */   public abstract void handleText(String paramString);
/*     */ 
/*     */   public void handleText(char paramChar)
/*     */   {
/*  83 */     handleText(String.valueOf(paramChar));
/*     */   }
/*     */ 
/*     */   public abstract void handleBinaryBlob(byte[] paramArrayOfByte);
/*     */ 
/*     */   public abstract void begingroup();
/*     */ 
/*     */   public abstract void endgroup();
/*     */ 
/*     */   public RTFParser()
/*     */   {
/* 105 */     this.currentCharacters = new StringBuffer();
/* 106 */     this.state = 0;
/* 107 */     this.pendingKeyword = null;
/* 108 */     this.level = 0;
/*     */ 
/* 111 */     this.specialsTable = rtfSpecialsTable;
/*     */   }
/*     */ 
/*     */   public void writeSpecial(int paramInt)
/*     */     throws IOException
/*     */   {
/* 119 */     write((char)paramInt);
/*     */   }
/*     */ 
/*     */   protected void warning(String paramString) {
/* 123 */     if (this.warnings != null)
/* 124 */       this.warnings.println(paramString);
/*     */   }
/*     */ 
/*     */   public void write(String paramString)
/*     */     throws IOException
/*     */   {
/* 131 */     if (this.state != 0) {
/* 132 */       int i = 0;
/* 133 */       int j = paramString.length();
/* 134 */       while ((i < j) && (this.state != 0)) {
/* 135 */         write(paramString.charAt(i));
/* 136 */         i++;
/*     */       }
/*     */ 
/* 139 */       if (i >= j) {
/* 140 */         return;
/*     */       }
/* 142 */       paramString = paramString.substring(i);
/*     */     }
/*     */ 
/* 145 */     if (this.currentCharacters.length() > 0)
/* 146 */       this.currentCharacters.append(paramString);
/*     */     else
/* 148 */       handleText(paramString);
/*     */   }
/*     */ 
/*     */   public void write(char paramChar)
/*     */     throws IOException
/*     */   {
/*     */     boolean bool;
/* 156 */     switch (this.state)
/*     */     {
/*     */     case 0:
/* 159 */       if ((paramChar != '\n') && (paramChar != '\r'))
/*     */       {
/* 161 */         if (paramChar == '{') {
/* 162 */           if (this.currentCharacters.length() > 0) {
/* 163 */             handleText(this.currentCharacters.toString());
/* 164 */             this.currentCharacters = new StringBuffer();
/*     */           }
/* 166 */           this.level += 1;
/* 167 */           begingroup();
/* 168 */         } else if (paramChar == '}') {
/* 169 */           if (this.currentCharacters.length() > 0) {
/* 170 */             handleText(this.currentCharacters.toString());
/* 171 */             this.currentCharacters = new StringBuffer();
/*     */           }
/* 173 */           if (this.level == 0)
/* 174 */             throw new IOException("Too many close-groups in RTF text");
/* 175 */           endgroup();
/* 176 */           this.level -= 1;
/* 177 */         } else if (paramChar == '\\') {
/* 178 */           if (this.currentCharacters.length() > 0) {
/* 179 */             handleText(this.currentCharacters.toString());
/* 180 */             this.currentCharacters = new StringBuffer();
/*     */           }
/* 182 */           this.state = 1;
/*     */         } else {
/* 184 */           this.currentCharacters.append(paramChar);
/*     */         }
/*     */       }
/* 186 */       break;
/*     */     case 1:
/* 188 */       if (paramChar == '\'') {
/* 189 */         this.state = 4;
/*     */       }
/* 192 */       else if (!Character.isLetter(paramChar)) {
/* 193 */         char[] arrayOfChar = new char[1];
/* 194 */         arrayOfChar[0] = paramChar;
/* 195 */         if (!handleKeyword(new String(arrayOfChar))) {
/* 196 */           warning("Unknown keyword: " + arrayOfChar + " (" + (byte)paramChar + ")");
/*     */         }
/* 198 */         this.state = 0;
/* 199 */         this.pendingKeyword = null;
/*     */       }
/*     */       else
/*     */       {
/* 204 */         this.state = 2;
/*     */       }break;
/*     */     case 2:
/* 207 */       if (Character.isLetter(paramChar)) {
/* 208 */         this.currentCharacters.append(paramChar);
/*     */       } else {
/* 210 */         this.pendingKeyword = this.currentCharacters.toString();
/* 211 */         this.currentCharacters = new StringBuffer();
/*     */ 
/* 214 */         if ((Character.isDigit(paramChar)) || (paramChar == '-')) {
/* 215 */           this.state = 3;
/* 216 */           this.currentCharacters.append(paramChar);
/*     */         } else {
/* 218 */           bool = handleKeyword(this.pendingKeyword);
/* 219 */           if (!bool)
/* 220 */             warning("Unknown keyword: " + this.pendingKeyword);
/* 221 */           this.pendingKeyword = null;
/* 222 */           this.state = 0;
/*     */ 
/* 225 */           if (!Character.isWhitespace(paramChar))
/* 226 */             write(paramChar);  }  } break;
/*     */     case 3:
/* 231 */       if (Character.isDigit(paramChar)) {
/* 232 */         this.currentCharacters.append(paramChar);
/*     */       }
/* 235 */       else if (this.pendingKeyword.equals("bin")) {
/* 236 */         long l = Long.parseLong(this.currentCharacters.toString());
/* 237 */         this.pendingKeyword = null;
/* 238 */         this.state = 6;
/* 239 */         this.binaryBytesLeft = l;
/* 240 */         if (this.binaryBytesLeft > 2147483647L)
/* 241 */           this.binaryBuf = new ByteArrayOutputStream(2147483647);
/*     */         else
/* 243 */           this.binaryBuf = new ByteArrayOutputStream((int)this.binaryBytesLeft);
/* 244 */         this.savedSpecials = this.specialsTable;
/* 245 */         this.specialsTable = allSpecialsTable;
/*     */       }
/*     */       else
/*     */       {
/* 249 */         int i = Integer.parseInt(this.currentCharacters.toString());
/* 250 */         bool = handleKeyword(this.pendingKeyword, i);
/* 251 */         if (!bool) {
/* 252 */           warning("Unknown keyword: " + this.pendingKeyword + " (param " + this.currentCharacters + ")");
/*     */         }
/* 254 */         this.pendingKeyword = null;
/* 255 */         this.currentCharacters = new StringBuffer();
/* 256 */         this.state = 0;
/*     */ 
/* 259 */         if (!Character.isWhitespace(paramChar))
/* 260 */           write(paramChar);
/*     */       }
/* 262 */       break;
/*     */     case 4:
/* 264 */       if (Character.digit(paramChar, 16) == -1) {
/* 265 */         this.state = 0;
/*     */       } else {
/* 267 */         this.pendingCharacter = Character.digit(paramChar, 16);
/* 268 */         this.state = 5;
/*     */       }
/* 270 */       break;
/*     */     case 5:
/* 272 */       this.state = 0;
/* 273 */       if (Character.digit(paramChar, 16) != -1)
/*     */       {
/* 275 */         this.pendingCharacter = (this.pendingCharacter * 16 + Character.digit(paramChar, 16));
/* 276 */         paramChar = this.translationTable[this.pendingCharacter];
/* 277 */         if (paramChar != 0)
/* 278 */           handleText(paramChar);  } break;
/*     */     case 6:
/* 282 */       this.binaryBuf.write(paramChar);
/* 283 */       this.binaryBytesLeft -= 1L;
/* 284 */       if (this.binaryBytesLeft == 0L) {
/* 285 */         this.state = 0;
/* 286 */         this.specialsTable = this.savedSpecials;
/* 287 */         this.savedSpecials = null;
/* 288 */         handleBinaryBlob(this.binaryBuf.toByteArray());
/* 289 */         this.binaryBuf = null;
/*     */       }
/*     */       break;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */     throws IOException
/*     */   {
/* 301 */     super.flush();
/*     */ 
/* 303 */     if ((this.state == 0) && (this.currentCharacters.length() > 0)) {
/* 304 */       handleText(this.currentCharacters.toString());
/* 305 */       this.currentCharacters = new StringBuffer();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void close()
/*     */     throws IOException
/*     */   {
/* 314 */     flush();
/*     */ 
/* 316 */     if ((this.state != 0) || (this.level > 0)) {
/* 317 */       warning("Truncated RTF file.");
/*     */ 
/* 324 */       while (this.level > 0) {
/* 325 */         endgroup();
/* 326 */         this.level -= 1;
/*     */       }
/*     */     }
/*     */ 
/* 330 */     super.close();
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/*  96 */     rtfSpecialsTable[10] = true;
/*  97 */     rtfSpecialsTable[13] = true;
/*  98 */     rtfSpecialsTable[123] = true;
/*  99 */     rtfSpecialsTable[125] = true;
/* 100 */     rtfSpecialsTable[92] = true;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.RTFParser
 * JD-Core Version:    0.6.2
 */