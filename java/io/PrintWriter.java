/*      */ package java.io;
/*      */ 
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.charset.IllegalCharsetNameException;
/*      */ import java.nio.charset.UnsupportedCharsetException;
/*      */ import java.security.AccessController;
/*      */ import java.util.Formatter;
/*      */ import java.util.Locale;
/*      */ import java.util.Objects;
/*      */ import sun.security.action.GetPropertyAction;
/*      */ 
/*      */ public class PrintWriter extends Writer
/*      */ {
/*      */   protected Writer out;
/*      */   private final boolean autoFlush;
/*   67 */   private boolean trouble = false;
/*      */   private Formatter formatter;
/*   69 */   private PrintStream psOut = null;
/*      */   private final String lineSeparator;
/*      */ 
/*      */   private static Charset toCharset(String paramString)
/*      */     throws UnsupportedEncodingException
/*      */   {
/*   85 */     Objects.requireNonNull(paramString, "charsetName");
/*      */     try {
/*   87 */       return Charset.forName(paramString);
/*      */     } catch (IllegalCharsetNameException|UnsupportedCharsetException localIllegalCharsetNameException) {
/*      */     }
/*   90 */     throw new UnsupportedEncodingException(paramString);
/*      */   }
/*      */ 
/*      */   public PrintWriter(Writer paramWriter)
/*      */   {
/*  100 */     this(paramWriter, false);
/*      */   }
/*      */ 
/*      */   public PrintWriter(Writer paramWriter, boolean paramBoolean)
/*      */   {
/*  113 */     super(paramWriter);
/*  114 */     this.out = paramWriter;
/*  115 */     this.autoFlush = paramBoolean;
/*  116 */     this.lineSeparator = ((String)AccessController.doPrivileged(new GetPropertyAction("line.separator")));
/*      */   }
/*      */ 
/*      */   public PrintWriter(OutputStream paramOutputStream)
/*      */   {
/*  131 */     this(paramOutputStream, false);
/*      */   }
/*      */ 
/*      */   public PrintWriter(OutputStream paramOutputStream, boolean paramBoolean)
/*      */   {
/*  148 */     this(new BufferedWriter(new OutputStreamWriter(paramOutputStream)), paramBoolean);
/*      */ 
/*  151 */     if ((paramOutputStream instanceof PrintStream))
/*  152 */       this.psOut = ((PrintStream)paramOutputStream);
/*      */   }
/*      */ 
/*      */   public PrintWriter(String paramString)
/*      */     throws FileNotFoundException
/*      */   {
/*  184 */     this(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramString))), false);
/*      */   }
/*      */ 
/*      */   private PrintWriter(Charset paramCharset, File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/*  192 */     this(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile), paramCharset)), false);
/*      */   }
/*      */ 
/*      */   public PrintWriter(String paramString1, String paramString2)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/*  232 */     this(toCharset(paramString2), new File(paramString1));
/*      */   }
/*      */ 
/*      */   public PrintWriter(File paramFile)
/*      */     throws FileNotFoundException
/*      */   {
/*  263 */     this(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(paramFile))), false);
/*      */   }
/*      */ 
/*      */   public PrintWriter(File paramFile, String paramString)
/*      */     throws FileNotFoundException, UnsupportedEncodingException
/*      */   {
/*  303 */     this(toCharset(paramString), paramFile);
/*      */   }
/*      */ 
/*      */   private void ensureOpen() throws IOException
/*      */   {
/*  308 */     if (this.out == null)
/*  309 */       throw new IOException("Stream closed");
/*      */   }
/*      */ 
/*      */   public void flush()
/*      */   {
/*      */     try
/*      */     {
/*  318 */       synchronized (this.lock) {
/*  319 */         ensureOpen();
/*  320 */         this.out.flush();
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  324 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void close()
/*      */   {
/*      */     try
/*      */     {
/*  336 */       synchronized (this.lock) {
/*  337 */         if (this.out == null)
/*  338 */           return;
/*  339 */         this.out.close();
/*  340 */         this.out = null;
/*      */       }
/*      */     }
/*      */     catch (IOException localIOException) {
/*  344 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public boolean checkError()
/*      */   {
/*  356 */     if (this.out != null) {
/*  357 */       flush();
/*      */     }
/*  359 */     if ((this.out instanceof PrintWriter)) {
/*  360 */       PrintWriter localPrintWriter = (PrintWriter)this.out;
/*  361 */       return localPrintWriter.checkError();
/*  362 */     }if (this.psOut != null) {
/*  363 */       return this.psOut.checkError();
/*      */     }
/*  365 */     return this.trouble;
/*      */   }
/*      */ 
/*      */   protected void setError()
/*      */   {
/*  376 */     this.trouble = true;
/*      */   }
/*      */ 
/*      */   protected void clearError()
/*      */   {
/*  389 */     this.trouble = false;
/*      */   }
/*      */ 
/*      */   public void write(int paramInt)
/*      */   {
/*      */     try
/*      */     {
/*  403 */       synchronized (this.lock) {
/*  404 */         ensureOpen();
/*  405 */         this.out.write(paramInt);
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  409 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  412 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  424 */       synchronized (this.lock) {
/*  425 */         ensureOpen();
/*  426 */         this.out.write(paramArrayOfChar, paramInt1, paramInt2);
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  430 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  433 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(char[] paramArrayOfChar)
/*      */   {
/*  443 */     write(paramArrayOfChar, 0, paramArrayOfChar.length);
/*      */   }
/*      */ 
/*      */   public void write(String paramString, int paramInt1, int paramInt2)
/*      */   {
/*      */     try
/*      */     {
/*  454 */       synchronized (this.lock) {
/*  455 */         ensureOpen();
/*  456 */         this.out.write(paramString, paramInt1, paramInt2);
/*      */       }
/*      */     }
/*      */     catch (InterruptedIOException localInterruptedIOException) {
/*  460 */       Thread.currentThread().interrupt();
/*      */     }
/*      */     catch (IOException localIOException) {
/*  463 */       this.trouble = true;
/*      */     }
/*      */   }
/*      */ 
/*      */   public void write(String paramString)
/*      */   {
/*  473 */     write(paramString, 0, paramString.length());
/*      */   }
/*      */ 
/*      */   private void newLine() {
/*      */     try {
/*  478 */       synchronized (this.lock) {
/*  479 */         ensureOpen();
/*  480 */         this.out.write(this.lineSeparator);
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
/*      */   public void print(boolean paramBoolean)
/*      */   {
/*  505 */     write(paramBoolean ? "true" : "false");
/*      */   }
/*      */ 
/*      */   public void print(char paramChar)
/*      */   {
/*  517 */     write(paramChar);
/*      */   }
/*      */ 
/*      */   public void print(int paramInt)
/*      */   {
/*  531 */     write(String.valueOf(paramInt));
/*      */   }
/*      */ 
/*      */   public void print(long paramLong)
/*      */   {
/*  545 */     write(String.valueOf(paramLong));
/*      */   }
/*      */ 
/*      */   public void print(float paramFloat)
/*      */   {
/*  559 */     write(String.valueOf(paramFloat));
/*      */   }
/*      */ 
/*      */   public void print(double paramDouble)
/*      */   {
/*  573 */     write(String.valueOf(paramDouble));
/*      */   }
/*      */ 
/*      */   public void print(char[] paramArrayOfChar)
/*      */   {
/*  587 */     write(paramArrayOfChar);
/*      */   }
/*      */ 
/*      */   public void print(String paramString)
/*      */   {
/*  600 */     if (paramString == null) {
/*  601 */       paramString = "null";
/*      */     }
/*  603 */     write(paramString);
/*      */   }
/*      */ 
/*      */   public void print(Object paramObject)
/*      */   {
/*  617 */     write(String.valueOf(paramObject));
/*      */   }
/*      */ 
/*      */   public void println()
/*      */   {
/*  629 */     newLine();
/*      */   }
/*      */ 
/*      */   public void println(boolean paramBoolean)
/*      */   {
/*  640 */     synchronized (this.lock) {
/*  641 */       print(paramBoolean);
/*  642 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(char paramChar)
/*      */   {
/*  654 */     synchronized (this.lock) {
/*  655 */       print(paramChar);
/*  656 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(int paramInt)
/*      */   {
/*  668 */     synchronized (this.lock) {
/*  669 */       print(paramInt);
/*  670 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(long paramLong)
/*      */   {
/*  682 */     synchronized (this.lock) {
/*  683 */       print(paramLong);
/*  684 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(float paramFloat)
/*      */   {
/*  696 */     synchronized (this.lock) {
/*  697 */       print(paramFloat);
/*  698 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(double paramDouble)
/*      */   {
/*  710 */     synchronized (this.lock) {
/*  711 */       print(paramDouble);
/*  712 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(char[] paramArrayOfChar)
/*      */   {
/*  724 */     synchronized (this.lock) {
/*  725 */       print(paramArrayOfChar);
/*  726 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(String paramString)
/*      */   {
/*  738 */     synchronized (this.lock) {
/*  739 */       print(paramString);
/*  740 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public void println(Object paramObject)
/*      */   {
/*  754 */     String str = String.valueOf(paramObject);
/*  755 */     synchronized (this.lock) {
/*  756 */       print(str);
/*  757 */       println();
/*      */     }
/*      */   }
/*      */ 
/*      */   public PrintWriter printf(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  804 */     return format(paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public PrintWriter printf(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*  855 */     return format(paramLocale, paramString, paramArrayOfObject);
/*      */   }
/*      */ 
/*      */   public PrintWriter format(String paramString, Object[] paramArrayOfObject)
/*      */   {
/*      */     try
/*      */     {
/*  900 */       synchronized (this.lock) {
/*  901 */         ensureOpen();
/*  902 */         if ((this.formatter == null) || (this.formatter.locale() != Locale.getDefault()))
/*      */         {
/*  904 */           this.formatter = new Formatter(this);
/*  905 */         }this.formatter.format(Locale.getDefault(), paramString, paramArrayOfObject);
/*  906 */         if (this.autoFlush)
/*  907 */           this.out.flush();
/*      */       }
/*      */     } catch (InterruptedIOException localInterruptedIOException) {
/*  910 */       Thread.currentThread().interrupt();
/*      */     } catch (IOException localIOException) {
/*  912 */       this.trouble = true;
/*      */     }
/*  914 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintWriter format(Locale paramLocale, String paramString, Object[] paramArrayOfObject)
/*      */   {
/*      */     try
/*      */     {
/*  960 */       synchronized (this.lock) {
/*  961 */         ensureOpen();
/*  962 */         if ((this.formatter == null) || (this.formatter.locale() != paramLocale))
/*  963 */           this.formatter = new Formatter(this, paramLocale);
/*  964 */         this.formatter.format(paramLocale, paramString, paramArrayOfObject);
/*  965 */         if (this.autoFlush)
/*  966 */           this.out.flush();
/*      */       }
/*      */     } catch (InterruptedIOException localInterruptedIOException) {
/*  969 */       Thread.currentThread().interrupt();
/*      */     } catch (IOException localIOException) {
/*  971 */       this.trouble = true;
/*      */     }
/*  973 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintWriter append(CharSequence paramCharSequence)
/*      */   {
/* 1001 */     if (paramCharSequence == null)
/* 1002 */       write("null");
/*      */     else
/* 1004 */       write(paramCharSequence.toString());
/* 1005 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintWriter append(CharSequence paramCharSequence, int paramInt1, int paramInt2)
/*      */   {
/* 1041 */     CharSequence localCharSequence = paramCharSequence == null ? "null" : paramCharSequence;
/* 1042 */     write(localCharSequence.subSequence(paramInt1, paramInt2).toString());
/* 1043 */     return this;
/*      */   }
/*      */ 
/*      */   public PrintWriter append(char paramChar)
/*      */   {
/* 1063 */     write(paramChar);
/* 1064 */     return this;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.io.PrintWriter
 * JD-Core Version:    0.6.2
 */