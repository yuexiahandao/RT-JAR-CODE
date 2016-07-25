/*      */ package java.io;
/*      */ 
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.util.Formatter;
/*      */ import java.util.Locale;
/*      */ 
/*      */ public class PrintStream extends FilterOutputStream
/*      */   implements Appendable, Closeable
/*      */ {
/*      */   private final boolean autoFlush;
/*   62 */   private boolean trouble = false;
/*      */   private Formatter formatter;
/*      */   private BufferedWriter textOut;
/*      */   private OutputStreamWriter charOut;
/*  346 */   private boolean closing = false;
/*      */ 
/*      */   private static <T> T requireNonNull(T paramT, String paramString)
/*      */   {
/*   78 */     if (paramT == null)
/*   79 */       throw new NullPointerException(paramString);
/*   80 */     return paramT;
/*      */   }
/*      */ 
/*      */   private static Charset toCharset(String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*   91 */     requireNonNull(paramString, "charsetName");
/*      */     try {
/*   93 */       return Charset.forName(paramString);
/*      */     } catch (IllegalCharsetNameException|UnsupportedCharsetException localIllegalCharsetNameException) {
/*      */     }
/*   96 */     throw new UnsupportedEncodingException(paramString);
/*      */   }
/*      */ 
/*      */   private PrintStream(boolean paramBoolean, OutputStream paramOutputStream)
/*      */   {
/*  102 */     super(paramOutputStream);
/*  103 */     this.autoFlush = paramBoolean;
/*  104 */     this.charOut = new OutputStreamWriter(this);
/*  105 */     this.textOut = new BufferedWriter(this.charOut);
/*      */   }
/*      */ 
/*      */   private PrintStream(boolean paramBoolean, OutputStream paramOutputStream, Charset paramCharset) {
/*  109 */     super(paramOutputStream);
/*  110 */     this.autoFlush = paramBoolean;
/*  111 */     this.charOut = new OutputStreamWriter(this, paramCharset);
/*  112 */     this.textOut = new BufferedWriter(this.charOut);
/*      */   }
/*      */ 
/*      */   private PrintStream(boolean paramBoolean, Charset paramCharset, OutputStream paramOutputStream)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  123 */     this(paramBoolean, paramOutputStream, paramCharset);
/*      */   }
/*      */ 
/*      */   public PrintStream(OutputStream paramOutputStream)
/*      */   {
/*  135 */     this(paramOutputStream, false);
/*      */   }
/*      */ 
/*      */   public PrintStream(OutputStream paramOutputStream, boolean paramBoolean)
/*      */   {
/*  151 */     this(paramBoolean, (OutputStream)requireNonNull(paramOutputStream, "Null output stream"));
/*      */   }
/*      */ 
/*      */   public PrintStream(OutputStream paramOutputStream, boolean paramBoolean, String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*  175 */     this(paramBoolean, (OutputStream)requireNonNull(paramOutputStream, "Null output stream"), toCharset(paramString));
/*      */   }
/*      */ 
/*      */   public PrintStream(String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/*  208 */     this(false, new FileOutputStream(paramString));
/*      */   }
/*      */ 
/*      */   public PrintStream(String paramString1, String paramString2)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/*  248 */     this(false, toCharset(paramString2), new FileOutputStream(paramString1));
/*      */   }
/*      */ 
/*      */   public PrintStream(File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/*  279 */     this(false, new FileOutputStream(paramFile));
/*      */   }
/*      */ 
/*      */   public PrintStream(File paramFile, String paramString)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/*  319 */     this(false, toCharset(paramString), new FileOutputStream(paramFile));
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws IOException
/*      */   {
/*  324 */     if (this.out == null)
/*  325 */       throw new IOException("Stream closed");
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */   {
/*  335 */     synchronized (this) {
/*      */       try {
/*  337 */         ensureOpen();
/*  338 */         this.out.flush();
/*      */       }
/*      */       catch (IOException localIOException) {
/*  341 */         this.trouble = true;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*  355 */     synchronized (this) {
/*  356 */       if (!this.closing) {
/*  357 */         this.closing = true;
/*      */         try {
/*  359 */           this.textOut.close();
/*  360 */           this.out.close();
/*      */         }
/*      */         catch (IOException localIOException) {
/*  363 */           this.trouble = true;
/*      */         }
/*  365 */         this.textOut = null;
/*  366 */         this.charOut = null;
/*  367 */         this.out = null;
/*      */       }
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean checkError()
/*      */   {
/*  391 */     if (this.out != null)
/*  392 */       flush();
/*  393 */     if ((this.out instanceof PrintStream)) {
/*  394 */       PrintStream localPrintStream = (PrintStream)this.out;
/*  395 */       return localPrintStream.checkError();
/*      */     }
/*  397 */     return this.trouble;
/*      */   }
/*      */ 
/*      */   protected void setError()
/*      */   {
/*  410 */     this.trouble = true;
/*      */   }
/*      */ 
/*      */   protected void clearError()
/*      */   {
/*  423 */     this.trouble = false;
/*      */   }
/*      */ 
/*      */   public void write(int paramInt)
/*      */   {
/*      */     try
/*      */     {
/*  447 */       synchronized (this) {
/*  448 */         ensureOpen();
/*  449 */         this.out.write(paramInt);
/*  450 */         if ((paramInt == 10) && (this.autoFlush))
/*  451 */           this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  455 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  458 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  478 */       synchronized (this) {
/*  479 */         ensureOpen();
/*  480 */         this.out.write(paramArrayOfByte, paramInt1, paramInt2);
/*  481 */         if (this.autoFlush)
/*  482 */           this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  486 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  489 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write(char[] paramArrayOfChar)
/*      */   {
/*      */     try
/*      */     {
/*  501 */       synchronized (this) {
/*  502 */         ensureOpen();
/*  503 */         this.textOut.write(paramArrayOfChar);
/*  504 */         this.textOut.flushBuffer();
/*  505 */         this.charOut.flushBuffer();
/*  506 */         if (this.autoFlush)
/*  507 */           for (int i = 0; i < paramArrayOfChar.length; i++)
/*  508 */             if (paramArrayOfChar[i] == '\n')
/*  509 */               this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException)
/*      */     {
/*  514 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  517 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void write(String paramString) {
/*      */     try {
/*  523 */       synchronized (this) {
/*  524 */         ensureOpen();
/*  525 */         this.textOut.write(paramString);
/*  526 */         this.textOut.flushBuffer();
/*  527 */         this.charOut.flushBuffer();
/*  528 */         if ((this.autoFlush) && (paramString.indexOf('\n') >= 0))
/*  529 */           this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  533 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  536 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   private void newLine() {
/*      */     try {
/*  542 */       synchronized (this) {
/*  543 */         ensureOpen();
/*  544 */         this.textOut.newLine();
/*  545 */         this.textOut.flushBuffer();
/*  546 */         this.charOut.flushBuffer();
/*  547 */         if (this.autoFlush)
/*  548 */           this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  552 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  555 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void print(boolean paramBoolean)
/*      */   {
/*  571 */     write(paramBoolean ? "true" : "false");
/*      */   }
/*      */ 
/*      */   public void print(char paramChar)
/*      */   {
/*  583 */     write(String.valueOf(paramChar));
/*      */   }
/*      */ 
/*      */   public void print(int paramInt)
/*      */   {
/*  597 */     write(String.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public void print(long paramLong)
/*      */   {
/*  611 */     write(String.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public void print(float paramFloat)
/*      */   {
/*  625 */     write(String.valueOf(paramFloat));
/*      */   }
/*      */ 
/*      */   public void print(double paramDouble)
/*      */   {
/*  639 */     write(String.valueOf(paramDouble));
/*      */   }
/*      */ 
/*      */   public void print(char[] paramArrayOfChar)
/*      */   {
/*  653 */     write(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public void print(String paramString)
/*      */   {
/*  666 */     if (paramString == null) {
/*  667 */       paramString = "null";
/*      */     }
/*  669 */     write(paramString);
/*      */   }
/*      */ 
/*      */   public void print(Object paramObject)
/*      */   {
/*  683 */     write(String.valueOf(paramObject));
/*      */   }
/*      */ 
/*      */   public void println()
/*      */   {
/*  696 */     newLine();
/*      */   }
/*      */ 
/*      */   public void println(boolean paramBoolean)
/*      */   {
/*  707 */     synchronized (this) {
/*  708 */       print(paramBoolean);
/*  709 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(char paramChar)
/*      */   {
/*  721 */     synchronized (this) {
/*  722 */       print(paramChar);
/*  723 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(int paramInt)
/*      */   {
/*  735 */     synchronized (this) {
/*  736 */       print(paramInt);
/*  737 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(long paramLong)
/*      */   {
/*  749 */     synchronized (this) {
/*  750 */       print(paramLong);
/*  751 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(float paramFloat)
/*      */   {
/*  763 */     synchronized (this) {
/*  764 */       print(paramFloat);
/*  765 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(double paramDouble)
/*      */   {
/*  777 */     synchronized (this) {
/*  778 */       print(paramDouble);
/*  779 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(char[] paramArrayOfChar)
/*      */   {
/*  791 */     synchronized (this) {
/*  792 */       print(paramArrayOfChar);
/*  793 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(String paramString)
/*      */   {
/*  805 */     synchronized (this) {
/*  806 */       print(paramString);
/*  807 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(Object paramObject)
/*      */   {
/*  821 */     String str = String.valueOf(paramObject);
/*  822 */     synchronized (this) {
/*  823 */       print(str);
/*  824 */       newLine();
/*      */     }
/*      */   }
/*      */ 
/*      */   public PrintStream printf(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  871 */     return format(paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public PrintStream printf(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  921 */     return format(paramLocale, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public PrintStream format(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*      */     try
/*      */     {
/*  965 */       synchronized (this) {
/*  966 */         ensureOpen();
/*  967 */         if ((this.formatter == null) || (this.formatter.locale() != Locale.getDefault()))
/*      */         {
/*  969 */           this.formatter = new Formatter(this);
/*  970 */         }this.formatter.format(Locale.getDefault(), paramString, paramArrayOfObject);
/*      */       }
/*      */     } catch (InterruptedIOException localInterruptedIOException) {
/*  973 */       Thread.currentThread().interrupt();
/*      */     } catch (IOException localIOException) {
/*  975 */       this.trouble = true;
/*      */     }
/*  977 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintStream format(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*      */     try
/*      */     {
/* 1022 */       synchronized (this) {
/* 1023 */         ensureOpen();
/* 1024 */         if ((this.formatter == null) || (this.formatter.locale() != paramLocale))
/*      */         {
/* 1026 */           this.formatter = new Formatter(this, paramLocale);
/* 1027 */         }this.formatter.format(paramLocale, paramString, paramArrayOfObject);
/*      */       }
/*      */     } catch (InterruptedIOException localInterruptedIOException) {
/* 1030 */       Thread.currentThread().interrupt();
/*      */     } catch (IOException localIOException) {
/* 1032 */       this.trouble = true;
/*      */     }
/* 1034 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintStream append(CharSequence paramCharSequence)
/*      */   {
/* 1062 */     if (paramCharSequence == null)
/* 1063 */       print("null");
/*      */     else
/* 1065 */       print(paramCharSequence.toString());
/* 1066 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintStream append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 1103 */     CharSequence localCharSequence = paramCharSequence == null ? "null" : paramCharSequence;
/* 1104 */     write(localCharSequence.subSequence(paramInt1, paramInt2).toString());
/* 1105 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintStream append(char paramChar)
/*      */   {
/* 1125 */     print(paramChar);
/* 1126 */     return this;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PrintStream
 * JD-Core Version:    0.6.2
 */