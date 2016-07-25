/*     */ package java.beans;
/*     */ 
/*     */ import com.sun.beans.decoder.DocumentHandler;
/*     */ import java.io.Closeable;
/*     */ import java.io.IOException;
/*     */ import java.io.InputStream;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ 
/*     */ public class XMLDecoder
/*     */   implements AutoCloseable
/*     */ {
/*  67 */   private final AccessControlContext acc = AccessController.getContext();
/*  68 */   private final DocumentHandler handler = new DocumentHandler();
/*     */   private final InputSource input;
/*     */   private Object owner;
/*     */   private Object[] array;
/*     */   private int index;
/*     */ 
/*     */   public XMLDecoder(InputStream paramInputStream)
/*     */   {
/*  83 */     this(paramInputStream, null);
/*     */   }
/*     */ 
/*     */   public XMLDecoder(InputStream paramInputStream, Object paramObject)
/*     */   {
/*  95 */     this(paramInputStream, paramObject, null);
/*     */   }
/*     */ 
/*     */   public XMLDecoder(InputStream paramInputStream, Object paramObject, ExceptionListener paramExceptionListener)
/*     */   {
/* 108 */     this(paramInputStream, paramObject, paramExceptionListener, null);
/*     */   }
/*     */ 
/*     */   public XMLDecoder(InputStream paramInputStream, Object paramObject, ExceptionListener paramExceptionListener, ClassLoader paramClassLoader)
/*     */   {
/* 128 */     this(new InputSource(paramInputStream), paramObject, paramExceptionListener, paramClassLoader);
/*     */   }
/*     */ 
/*     */   public XMLDecoder(InputSource paramInputSource)
/*     */   {
/* 145 */     this(paramInputSource, null, null, null);
/*     */   }
/*     */ 
/*     */   private XMLDecoder(InputSource paramInputSource, Object paramObject, ExceptionListener paramExceptionListener, ClassLoader paramClassLoader)
/*     */   {
/* 162 */     this.input = paramInputSource;
/* 163 */     this.owner = paramObject;
/* 164 */     setExceptionListener(paramExceptionListener);
/* 165 */     this.handler.setClassLoader(paramClassLoader);
/* 166 */     this.handler.setOwner(this);
/*     */   }
/*     */ 
/*     */   public void close()
/*     */   {
/* 174 */     if (parsingComplete()) {
/* 175 */       close(this.input.getCharacterStream());
/* 176 */       close(this.input.getByteStream());
/*     */     }
/*     */   }
/*     */ 
/*     */   private void close(Closeable paramCloseable) {
/* 181 */     if (paramCloseable != null)
/*     */       try {
/* 183 */         paramCloseable.close();
/*     */       }
/*     */       catch (IOException localIOException) {
/* 186 */         getExceptionListener().exceptionThrown(localIOException);
/*     */       }
/*     */   }
/*     */ 
/*     */   private boolean parsingComplete()
/*     */   {
/* 192 */     if (this.input == null) {
/* 193 */       return false;
/*     */     }
/* 195 */     if (this.array == null) {
/* 196 */       if ((this.acc == null) && (null != System.getSecurityManager())) {
/* 197 */         throw new SecurityException("AccessControlContext is not set");
/*     */       }
/* 199 */       AccessController.doPrivileged(new PrivilegedAction() {
/*     */         public Void run() {
/* 201 */           XMLDecoder.this.handler.parse(XMLDecoder.this.input);
/* 202 */           return null;
/*     */         }
/*     */       }
/*     */       , this.acc);
/*     */ 
/* 205 */       this.array = this.handler.getObjects();
/*     */     }
/* 207 */     return true;
/*     */   }
/*     */ 
/*     */   public void setExceptionListener(ExceptionListener paramExceptionListener)
/*     */   {
/* 221 */     if (paramExceptionListener == null) {
/* 222 */       paramExceptionListener = Statement.defaultExceptionListener;
/*     */     }
/* 224 */     this.handler.setExceptionListener(paramExceptionListener);
/*     */   }
/*     */ 
/*     */   public ExceptionListener getExceptionListener()
/*     */   {
/* 236 */     return this.handler.getExceptionListener();
/*     */   }
/*     */ 
/*     */   public Object readObject()
/*     */   {
/* 250 */     return parsingComplete() ? this.array[(this.index++)] : null;
/*     */   }
/*     */ 
/*     */   public void setOwner(Object paramObject)
/*     */   {
/* 263 */     this.owner = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getOwner()
/*     */   {
/* 274 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public static DefaultHandler createHandler(Object paramObject, ExceptionListener paramExceptionListener, ClassLoader paramClassLoader)
/*     */   {
/* 300 */     DocumentHandler localDocumentHandler = new DocumentHandler();
/* 301 */     localDocumentHandler.setOwner(paramObject);
/* 302 */     localDocumentHandler.setExceptionListener(paramExceptionListener);
/* 303 */     localDocumentHandler.setClassLoader(paramClassLoader);
/* 304 */     return localDocumentHandler;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.XMLDecoder
 * JD-Core Version:    0.6.2
 */