/*     */ package javax.swing.text.rtf;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.OutputStream;
/*     */ import java.io.Reader;
/*     */ 
/*     */ abstract class AbstractFilter extends OutputStream
/*     */ {
/*     */   protected char[] translationTable;
/*     */   protected boolean[] specialsTable;
/*     */   static final char[] latin1TranslationTable;
/*  67 */   static final boolean[] noSpecialsTable = new boolean[256];
/*     */   static final boolean[] allSpecialsTable;
/*     */ 
/*     */   public void readFromStream(InputStream paramInputStream)
/*     */     throws IOException
/*     */   {
/*  99 */     byte[] arrayOfByte = new byte[16384];
/*     */     while (true)
/*     */     {
/* 102 */       int i = paramInputStream.read(arrayOfByte);
/* 103 */       if (i < 0) {
/*     */         break;
/*     */       }
/* 106 */       write(arrayOfByte, 0, i);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void readFromReader(Reader paramReader)
/*     */     throws IOException
/*     */   {
/* 116 */     char[] arrayOfChar = new char[2048];
/*     */     while (true)
/*     */     {
/* 119 */       int i = paramReader.read(arrayOfChar);
/* 120 */       if (i < 0)
/*     */         break;
/* 122 */       for (int j = 0; j < i; j++)
/* 123 */         write(arrayOfChar[j]);
/*     */     }
/*     */   }
/*     */ 
/*     */   public AbstractFilter()
/*     */   {
/* 130 */     this.translationTable = latin1TranslationTable;
/* 131 */     this.specialsTable = noSpecialsTable;
/*     */   }
/*     */ 
/*     */   public void write(int paramInt)
/*     */     throws IOException
/*     */   {
/* 141 */     if (paramInt < 0)
/* 142 */       paramInt += 256;
/* 143 */     if (this.specialsTable[paramInt] != 0) {
/* 144 */       writeSpecial(paramInt);
/*     */     } else {
/* 146 */       char c = this.translationTable[paramInt];
/* 147 */       if (c != 0)
/* 148 */         write(c);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*     */     throws IOException
/*     */   {
/* 163 */     StringBuilder localStringBuilder = null;
/* 164 */     while (paramInt2 > 0) {
/* 165 */       int i = (short)paramArrayOfByte[paramInt1];
/*     */ 
/* 168 */       if (i < 0) {
/* 169 */         i = (short)(i + 256);
/*     */       }
/* 171 */       if (this.specialsTable[i] != 0) {
/* 172 */         if (localStringBuilder != null) {
/* 173 */           write(localStringBuilder.toString());
/* 174 */           localStringBuilder = null;
/*     */         }
/* 176 */         writeSpecial(i);
/*     */       } else {
/* 178 */         char c = this.translationTable[i];
/* 179 */         if (c != 0) {
/* 180 */           if (localStringBuilder == null)
/* 181 */             localStringBuilder = new StringBuilder();
/* 182 */           localStringBuilder.append(c);
/*     */         }
/*     */       }
/*     */ 
/* 186 */       paramInt2--;
/* 187 */       paramInt1++;
/*     */     }
/*     */ 
/* 190 */     if (localStringBuilder != null)
/* 191 */       write(localStringBuilder.toString());
/*     */   }
/*     */ 
/*     */   public void write(String paramString)
/*     */     throws IOException
/*     */   {
/* 207 */     int j = paramString.length();
/* 208 */     for (int i = 0; i < j; i++)
/* 209 */       write(paramString.charAt(i));
/*     */   }
/*     */ 
/*     */   protected abstract void write(char paramChar)
/*     */     throws IOException;
/*     */ 
/*     */   protected abstract void writeSpecial(int paramInt)
/*     */     throws IOException;
/*     */ 
/*     */   static
/*     */   {
/*  68 */     for (int i = 0; i < 256; i++) {
/*  69 */       noSpecialsTable[i] = false;
/*     */     }
/*  71 */     allSpecialsTable = new boolean[256];
/*  72 */     for (i = 0; i < 256; i++) {
/*  73 */       allSpecialsTable[i] = true;
/*     */     }
/*  75 */     latin1TranslationTable = new char[256];
/*  76 */     for (i = 0; i < 256; i++)
/*  77 */       latin1TranslationTable[i] = ((char)i);
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.rtf.AbstractFilter
 * JD-Core Version:    0.6.2
 */