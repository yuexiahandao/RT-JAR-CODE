/*     */ package com.sun.org.apache.xml.internal.serialize;
/*     */ 
/*     */ import java.io.IOException;
/*     */ import java.io.StringWriter;
/*     */ import java.io.Writer;
/*     */ 
/*     */ public class IndentPrinter extends Printer
/*     */ {
/*     */   private StringBuffer _line;
/*     */   private StringBuffer _text;
/*     */   private int _spaces;
/*     */   private int _thisIndent;
/*     */   private int _nextIndent;
/*     */ 
/*     */   public IndentPrinter(Writer writer, OutputFormat format)
/*     */   {
/*  80 */     super(writer, format);
/*     */ 
/*  82 */     this._line = new StringBuffer(80);
/*  83 */     this._text = new StringBuffer(20);
/*  84 */     this._spaces = 0;
/*  85 */     this._thisIndent = (this._nextIndent = 0);
/*     */   }
/*     */ 
/*     */   public void enterDTD()
/*     */   {
/* 101 */     if (this._dtdWriter == null) {
/* 102 */       this._line.append(this._text);
/* 103 */       this._text = new StringBuffer(20);
/* 104 */       flushLine(false);
/* 105 */       this._dtdWriter = new StringWriter();
/* 106 */       this._docWriter = this._writer;
/* 107 */       this._writer = this._dtdWriter;
/*     */     }
/*     */   }
/*     */ 
/*     */   public String leaveDTD()
/*     */   {
/* 120 */     if (this._writer == this._dtdWriter) {
/* 121 */       this._line.append(this._text);
/* 122 */       this._text = new StringBuffer(20);
/* 123 */       flushLine(false);
/* 124 */       this._writer = this._docWriter;
/* 125 */       return this._dtdWriter.toString();
/*     */     }
/* 127 */     return null;
/*     */   }
/*     */ 
/*     */   public void printText(String text)
/*     */   {
/* 142 */     this._text.append(text);
/*     */   }
/*     */ 
/*     */   public void printText(StringBuffer text)
/*     */   {
/* 148 */     this._text.append(text.toString());
/*     */   }
/*     */ 
/*     */   public void printText(char ch)
/*     */   {
/* 154 */     this._text.append(ch);
/*     */   }
/*     */ 
/*     */   public void printText(char[] chars, int start, int length)
/*     */   {
/* 160 */     this._text.append(chars, start, length);
/*     */   }
/*     */ 
/*     */   public void printSpace()
/*     */   {
/* 188 */     if (this._text.length() > 0)
/*     */     {
/* 193 */       if ((this._format.getLineWidth() > 0) && (this._thisIndent + this._line.length() + this._spaces + this._text.length() > this._format.getLineWidth()))
/*     */       {
/* 195 */         flushLine(false);
/*     */         try
/*     */         {
/* 198 */           this._writer.write(this._format.getLineSeparator());
/*     */         }
/*     */         catch (IOException except)
/*     */         {
/* 202 */           if (this._exception == null) {
/* 203 */             this._exception = except;
/*     */           }
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/* 209 */       while (this._spaces > 0) {
/* 210 */         this._line.append(' ');
/* 211 */         this._spaces -= 1;
/*     */       }
/* 213 */       this._line.append(this._text);
/* 214 */       this._text = new StringBuffer(20);
/*     */     }
/*     */ 
/* 218 */     this._spaces += 1;
/*     */   }
/*     */ 
/*     */   public void breakLine()
/*     */   {
/* 231 */     breakLine(false);
/*     */   }
/*     */ 
/*     */   public void breakLine(boolean preserveSpace)
/*     */   {
/* 238 */     if (this._text.length() > 0) {
/* 239 */       while (this._spaces > 0) {
/* 240 */         this._line.append(' ');
/* 241 */         this._spaces -= 1;
/*     */       }
/* 243 */       this._line.append(this._text);
/* 244 */       this._text = new StringBuffer(20);
/*     */     }
/* 246 */     flushLine(preserveSpace);
/*     */     try
/*     */     {
/* 249 */       this._writer.write(this._format.getLineSeparator());
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 253 */       if (this._exception == null)
/* 254 */         this._exception = except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void flushLine(boolean preserveSpace)
/*     */   {
/* 270 */     if (this._line.length() > 0)
/*     */       try
/*     */       {
/* 273 */         if ((this._format.getIndenting()) && (!preserveSpace))
/*     */         {
/* 275 */           int indent = this._thisIndent;
/* 276 */           if ((2 * indent > this._format.getLineWidth()) && (this._format.getLineWidth() > 0)) {
/* 277 */             indent = this._format.getLineWidth() / 2;
/*     */           }
/*     */ 
/* 280 */           while (indent > 0) {
/* 281 */             this._writer.write(32);
/* 282 */             indent--;
/*     */           }
/*     */         }
/* 285 */         this._thisIndent = this._nextIndent;
/*     */ 
/* 290 */         this._spaces = 0;
/* 291 */         this._writer.write(this._line.toString());
/*     */ 
/* 293 */         this._line = new StringBuffer(40);
/*     */       }
/*     */       catch (IOException except)
/*     */       {
/* 297 */         if (this._exception == null)
/* 298 */           this._exception = except;
/*     */       }
/*     */   }
/*     */ 
/*     */   public void flush()
/*     */   {
/* 310 */     if ((this._line.length() > 0) || (this._text.length() > 0))
/* 311 */       breakLine();
/*     */     try {
/* 313 */       this._writer.flush();
/*     */     }
/*     */     catch (IOException except)
/*     */     {
/* 317 */       if (this._exception == null)
/* 318 */         this._exception = except;
/*     */     }
/*     */   }
/*     */ 
/*     */   public void indent()
/*     */   {
/* 328 */     this._nextIndent += this._format.getIndent();
/*     */   }
/*     */ 
/*     */   public void unindent()
/*     */   {
/* 337 */     this._nextIndent -= this._format.getIndent();
/* 338 */     if (this._nextIndent < 0) {
/* 339 */       this._nextIndent = 0;
/*     */     }
/*     */ 
/* 342 */     if (this._line.length() + this._spaces + this._text.length() == 0)
/* 343 */       this._thisIndent = this._nextIndent;
/*     */   }
/*     */ 
/*     */   public int getNextIndent()
/*     */   {
/* 349 */     return this._nextIndent;
/*     */   }
/*     */ 
/*     */   public void setNextIndent(int indent)
/*     */   {
/* 355 */     this._nextIndent = indent;
/*     */   }
/*     */ 
/*     */   public void setThisIndent(int indent)
/*     */   {
/* 361 */     this._thisIndent = indent;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.serialize.IndentPrinter
 * JD-Core Version:    0.6.2
 */