/*     */ package javax.print;
/*     */ 
/*     */ import java.io.ByteArrayInputStream;
/*     */ import java.io.CharArrayReader;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.io.Reader;
/*     */ import java.io.StringReader;
/*     */ import javax.print.attribute.AttributeSetUtilities;
/*     */ import javax.print.attribute.DocAttributeSet;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public final class SimpleDoc
/*     */   implements Doc
/*     */ {
/*     */   private DocFlavor flavor;
/*     */   private DocAttributeSet attributes;
/*     */   private Object printData;
/*     */   private Reader reader;
/*     */   private InputStream inStream;
/*     */ 
/*     */   public SimpleDoc(Object paramObject, DocFlavor paramDocFlavor, DocAttributeSet paramDocAttributeSet)
/*     */   {
/*  88 */     if ((paramDocFlavor == null) || (paramObject == null)) {
/*  89 */       throw new IllegalArgumentException("null argument(s)");
/*     */     }
/*     */ 
/*  92 */     Class localClass = null;
/*     */     try {
/*  94 */       String str = paramDocFlavor.getRepresentationClassName();
/*  95 */       ReflectUtil.checkPackageAccess(str);
/*  96 */       localClass = Class.forName(str, false, Thread.currentThread().getContextClassLoader());
/*     */     }
/*     */     catch (Throwable localThrowable) {
/*  99 */       throw new IllegalArgumentException("unknown representation class");
/*     */     }
/*     */ 
/* 102 */     if (!localClass.isInstance(paramObject)) {
/* 103 */       throw new IllegalArgumentException("data is not of declared type");
/*     */     }
/*     */ 
/* 106 */     this.flavor = paramDocFlavor;
/* 107 */     if (paramDocAttributeSet != null) {
/* 108 */       this.attributes = AttributeSetUtilities.unmodifiableView(paramDocAttributeSet);
/*     */     }
/* 110 */     this.printData = paramObject;
/*     */   }
/*     */ 
/*     */   public DocFlavor getDocFlavor()
/*     */   {
/* 120 */     return this.flavor;
/*     */   }
/*     */ 
/*     */   public DocAttributeSet getAttributes()
/*     */   {
/* 140 */     return this.attributes;
/*     */   }
/*     */ 
/*     */   public Object getPrintData()
/*     */     throws IOException
/*     */   {
/* 159 */     return this.printData;
/*     */   }
/*     */ 
/*     */   public Reader getReaderForText()
/*     */     throws IOException
/*     */   {
/* 190 */     if ((this.printData instanceof Reader)) {
/* 191 */       return (Reader)this.printData;
/*     */     }
/*     */ 
/* 194 */     synchronized (this) {
/* 195 */       if (this.reader != null) {
/* 196 */         return this.reader;
/*     */       }
/*     */ 
/* 199 */       if ((this.printData instanceof char[])) {
/* 200 */         this.reader = new CharArrayReader((char[])this.printData);
/*     */       }
/* 202 */       else if ((this.printData instanceof String)) {
/* 203 */         this.reader = new StringReader((String)this.printData);
/*     */       }
/*     */     }
/* 206 */     return this.reader;
/*     */   }
/*     */ 
/*     */   public InputStream getStreamForBytes()
/*     */     throws IOException
/*     */   {
/* 238 */     if ((this.printData instanceof InputStream)) {
/* 239 */       return (InputStream)this.printData;
/*     */     }
/*     */ 
/* 242 */     synchronized (this) {
/* 243 */       if (this.inStream != null) {
/* 244 */         return this.inStream;
/*     */       }
/*     */ 
/* 247 */       if ((this.printData instanceof byte[])) {
/* 248 */         this.inStream = new ByteArrayInputStream((byte[])this.printData);
/*     */       }
/*     */     }
/* 251 */     return this.inStream;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.print.SimpleDoc
 * JD-Core Version:    0.6.2
 */