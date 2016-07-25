/*     */ package com.sun.beans.decoder;
/*     */ 
/*     */ import com.sun.beans.finder.ClassFinder;
/*     */ import java.beans.ExceptionListener;
/*     */ import java.io.IOException;
/*     */ import java.io.StringReader;
/*     */ import java.lang.ref.Reference;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.security.AccessControlContext;
/*     */ import java.security.AccessController;
/*     */ import java.security.PrivilegedAction;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import javax.xml.parsers.ParserConfigurationException;
/*     */ import javax.xml.parsers.SAXParser;
/*     */ import javax.xml.parsers.SAXParserFactory;
/*     */ import org.xml.sax.Attributes;
/*     */ import org.xml.sax.InputSource;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.helpers.DefaultHandler;
/*     */ import sun.misc.JavaSecurityAccess;
/*     */ import sun.misc.SharedSecrets;
/*     */ 
/*     */ public final class DocumentHandler extends DefaultHandler
/*     */ {
/*  65 */   private final AccessControlContext acc = AccessController.getContext();
/*  66 */   private final Map<String, Class<? extends ElementHandler>> handlers = new HashMap();
/*  67 */   private final Map<String, Object> environment = new HashMap();
/*  68 */   private final List<Object> objects = new ArrayList();
/*     */   private Reference<ClassLoader> loader;
/*     */   private ExceptionListener listener;
/*     */   private Object owner;
/*     */   private ElementHandler handler;
/*     */ 
/*     */   public DocumentHandler()
/*     */   {
/*  80 */     setElementHandler("java", JavaElementHandler.class);
/*  81 */     setElementHandler("null", NullElementHandler.class);
/*  82 */     setElementHandler("array", ArrayElementHandler.class);
/*  83 */     setElementHandler("class", ClassElementHandler.class);
/*  84 */     setElementHandler("string", StringElementHandler.class);
/*  85 */     setElementHandler("object", ObjectElementHandler.class);
/*     */ 
/*  87 */     setElementHandler("void", VoidElementHandler.class);
/*  88 */     setElementHandler("char", CharElementHandler.class);
/*  89 */     setElementHandler("byte", ByteElementHandler.class);
/*  90 */     setElementHandler("short", ShortElementHandler.class);
/*  91 */     setElementHandler("int", IntElementHandler.class);
/*  92 */     setElementHandler("long", LongElementHandler.class);
/*  93 */     setElementHandler("float", FloatElementHandler.class);
/*  94 */     setElementHandler("double", DoubleElementHandler.class);
/*  95 */     setElementHandler("boolean", BooleanElementHandler.class);
/*     */ 
/*  98 */     setElementHandler("new", NewElementHandler.class);
/*  99 */     setElementHandler("var", VarElementHandler.class);
/* 100 */     setElementHandler("true", TrueElementHandler.class);
/* 101 */     setElementHandler("false", FalseElementHandler.class);
/* 102 */     setElementHandler("field", FieldElementHandler.class);
/* 103 */     setElementHandler("method", MethodElementHandler.class);
/* 104 */     setElementHandler("property", PropertyElementHandler.class);
/*     */   }
/*     */ 
/*     */   public ClassLoader getClassLoader()
/*     */   {
/* 115 */     return this.loader != null ? (ClassLoader)this.loader.get() : null;
/*     */   }
/*     */ 
/*     */   public void setClassLoader(ClassLoader paramClassLoader)
/*     */   {
/* 128 */     this.loader = new WeakReference(paramClassLoader);
/*     */   }
/*     */ 
/*     */   public ExceptionListener getExceptionListener()
/*     */   {
/* 141 */     return this.listener;
/*     */   }
/*     */ 
/*     */   public void setExceptionListener(ExceptionListener paramExceptionListener)
/*     */   {
/* 152 */     this.listener = paramExceptionListener;
/*     */   }
/*     */ 
/*     */   public Object getOwner()
/*     */   {
/* 161 */     return this.owner;
/*     */   }
/*     */ 
/*     */   public void setOwner(Object paramObject)
/*     */   {
/* 170 */     this.owner = paramObject;
/*     */   }
/*     */ 
/*     */   public Class<? extends ElementHandler> getElementHandler(String paramString)
/*     */   {
/* 180 */     Class localClass = (Class)this.handlers.get(paramString);
/* 181 */     if (localClass == null) {
/* 182 */       throw new IllegalArgumentException("Unsupported element: " + paramString);
/*     */     }
/* 184 */     return localClass;
/*     */   }
/*     */ 
/*     */   public void setElementHandler(String paramString, Class<? extends ElementHandler> paramClass)
/*     */   {
/* 194 */     this.handlers.put(paramString, paramClass);
/*     */   }
/*     */ 
/*     */   public boolean hasVariable(String paramString)
/*     */   {
/* 205 */     return this.environment.containsKey(paramString);
/*     */   }
/*     */ 
/*     */   public Object getVariable(String paramString)
/*     */   {
/* 215 */     if (!this.environment.containsKey(paramString)) {
/* 216 */       throw new IllegalArgumentException("Unbound variable: " + paramString);
/*     */     }
/* 218 */     return this.environment.get(paramString);
/*     */   }
/*     */ 
/*     */   public void setVariable(String paramString, Object paramObject)
/*     */   {
/* 228 */     this.environment.put(paramString, paramObject);
/*     */   }
/*     */ 
/*     */   public Object[] getObjects()
/*     */   {
/* 237 */     return this.objects.toArray();
/*     */   }
/*     */ 
/*     */   void addObject(Object paramObject)
/*     */   {
/* 246 */     this.objects.add(paramObject);
/*     */   }
/*     */ 
/*     */   public InputSource resolveEntity(String paramString1, String paramString2)
/*     */   {
/* 254 */     return new InputSource(new StringReader(""));
/*     */   }
/*     */ 
/*     */   public void startDocument()
/*     */   {
/* 262 */     this.objects.clear();
/* 263 */     this.handler = null;
/*     */   }
/*     */ 
/*     */   public void startElement(String paramString1, String paramString2, String paramString3, Attributes paramAttributes)
/*     */     throws SAXException
/*     */   {
/* 281 */     ElementHandler localElementHandler = this.handler;
/*     */     try {
/* 283 */       this.handler = ((ElementHandler)getElementHandler(paramString3).newInstance());
/* 284 */       this.handler.setOwner(this);
/* 285 */       this.handler.setParent(localElementHandler);
/*     */     }
/*     */     catch (Exception localException) {
/* 288 */       throw new SAXException(localException);
/*     */     }
/* 290 */     for (int i = 0; i < paramAttributes.getLength(); i++) {
/*     */       try {
/* 292 */         String str1 = paramAttributes.getQName(i);
/* 293 */         String str2 = paramAttributes.getValue(i);
/* 294 */         this.handler.addAttribute(str1, str2);
/*     */       }
/*     */       catch (RuntimeException localRuntimeException) {
/* 297 */         handleException(localRuntimeException);
/*     */       }
/*     */     }
/* 300 */     this.handler.startElement();
/*     */   }
/*     */ 
/*     */   public void endElement(String paramString1, String paramString2, String paramString3)
/*     */   {
/*     */     try
/*     */     {
/* 318 */       this.handler.endElement();
/*     */     }
/*     */     catch (RuntimeException localRuntimeException) {
/* 321 */       handleException(localRuntimeException);
/*     */     }
/*     */     finally {
/* 324 */       this.handler = this.handler.getParent();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void characters(char[] paramArrayOfChar, int paramInt1, int paramInt2)
/*     */   {
/* 337 */     if (this.handler != null)
/*     */       try {
/* 339 */         while (0 < paramInt2--)
/* 340 */           this.handler.addCharacter(paramArrayOfChar[(paramInt1++)]);
/*     */       }
/*     */       catch (RuntimeException localRuntimeException)
/*     */       {
/* 344 */         handleException(localRuntimeException);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void handleException(Exception paramException)
/*     */   {
/* 356 */     if (this.listener == null) {
/* 357 */       throw new IllegalStateException(paramException);
/*     */     }
/* 359 */     this.listener.exceptionThrown(paramException);
/*     */   }
/*     */ 
/*     */   public void parse(final InputSource paramInputSource)
/*     */   {
/* 368 */     if ((this.acc == null) && (null != System.getSecurityManager())) {
/* 369 */       throw new SecurityException("AccessControlContext is not set");
/*     */     }
/* 371 */     AccessControlContext localAccessControlContext = AccessController.getContext();
/* 372 */     SharedSecrets.getJavaSecurityAccess().doIntersectionPrivilege(new PrivilegedAction() {
/*     */       public Void run() {
/*     */         try {
/* 375 */           SAXParserFactory.newInstance().newSAXParser().parse(paramInputSource, DocumentHandler.this);
/*     */         }
/*     */         catch (ParserConfigurationException localParserConfigurationException) {
/* 378 */           DocumentHandler.this.handleException(localParserConfigurationException);
/*     */         }
/*     */         catch (SAXException localSAXException) {
/* 381 */           Object localObject = localSAXException.getException();
/* 382 */           if (localObject == null) {
/* 383 */             localObject = localSAXException;
/*     */           }
/* 385 */           DocumentHandler.this.handleException((Exception)localObject);
/*     */         }
/*     */         catch (IOException localIOException) {
/* 388 */           DocumentHandler.this.handleException(localIOException);
/*     */         }
/* 390 */         return null;
/*     */       }
/*     */     }
/*     */     , localAccessControlContext, this.acc);
/*     */   }
/*     */ 
/*     */   public Class<?> findClass(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 404 */       return ClassFinder.resolveClass(paramString, getClassLoader());
/*     */     }
/*     */     catch (ClassNotFoundException localClassNotFoundException) {
/* 407 */       handleException(localClassNotFoundException);
/* 408 */     }return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.beans.decoder.DocumentHandler
 * JD-Core Version:    0.6.2
 */