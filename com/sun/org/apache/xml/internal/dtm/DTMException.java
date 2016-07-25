/*     */ package com.sun.org.apache.xml.internal.dtm;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.res.XMLMessages;
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.xml.transform.SourceLocator;
/*     */ 
/*     */ public class DTMException extends RuntimeException
/*     */ {
/*     */   static final long serialVersionUID = -775576419181334734L;
/*     */   SourceLocator locator;
/*     */   Throwable containedException;
/*     */ 
/*     */   public SourceLocator getLocator()
/*     */   {
/*  53 */     return this.locator;
/*     */   }
/*     */ 
/*     */   public void setLocator(SourceLocator location)
/*     */   {
/*  63 */     this.locator = location;
/*     */   }
/*     */ 
/*     */   public Throwable getException()
/*     */   {
/*  77 */     return this.containedException;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/*  87 */     return this.containedException == this ? null : this.containedException;
/*     */   }
/*     */ 
/*     */   public synchronized Throwable initCause(Throwable cause)
/*     */   {
/* 118 */     if ((this.containedException == null) && (cause != null)) {
/* 119 */       throw new IllegalStateException(XMLMessages.createXMLMessage("ER_CANNOT_OVERWRITE_CAUSE", null));
/*     */     }
/*     */ 
/* 122 */     if (cause == this) {
/* 123 */       throw new IllegalArgumentException(XMLMessages.createXMLMessage("ER_SELF_CAUSATION_NOT_PERMITTED", null));
/*     */     }
/*     */ 
/* 127 */     this.containedException = cause;
/*     */ 
/* 129 */     return this;
/*     */   }
/*     */ 
/*     */   public DTMException(String message)
/*     */   {
/* 139 */     super(message);
/*     */ 
/* 141 */     this.containedException = null;
/* 142 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public DTMException(Throwable e)
/*     */   {
/* 152 */     super(e.getMessage());
/*     */ 
/* 154 */     this.containedException = e;
/* 155 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public DTMException(String message, Throwable e)
/*     */   {
/* 170 */     super((message == null) || (message.length() == 0) ? e.getMessage() : message);
/*     */ 
/* 174 */     this.containedException = e;
/* 175 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public DTMException(String message, SourceLocator locator)
/*     */   {
/* 190 */     super(message);
/*     */ 
/* 192 */     this.containedException = null;
/* 193 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   public DTMException(String message, SourceLocator locator, Throwable e)
/*     */   {
/* 207 */     super(message);
/*     */ 
/* 209 */     this.containedException = e;
/* 210 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   public String getMessageAndLocation()
/*     */   {
/* 219 */     StringBuffer sbuffer = new StringBuffer();
/* 220 */     String message = super.getMessage();
/*     */ 
/* 222 */     if (null != message) {
/* 223 */       sbuffer.append(message);
/*     */     }
/*     */ 
/* 226 */     if (null != this.locator) {
/* 227 */       String systemID = this.locator.getSystemId();
/* 228 */       int line = this.locator.getLineNumber();
/* 229 */       int column = this.locator.getColumnNumber();
/*     */ 
/* 231 */       if (null != systemID) {
/* 232 */         sbuffer.append("; SystemID: ");
/* 233 */         sbuffer.append(systemID);
/*     */       }
/*     */ 
/* 236 */       if (0 != line) {
/* 237 */         sbuffer.append("; Line#: ");
/* 238 */         sbuffer.append(line);
/*     */       }
/*     */ 
/* 241 */       if (0 != column) {
/* 242 */         sbuffer.append("; Column#: ");
/* 243 */         sbuffer.append(column);
/*     */       }
/*     */     }
/*     */ 
/* 247 */     return sbuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getLocationAsString()
/*     */   {
/* 258 */     if (null != this.locator) {
/* 259 */       StringBuffer sbuffer = new StringBuffer();
/* 260 */       String systemID = this.locator.getSystemId();
/* 261 */       int line = this.locator.getLineNumber();
/* 262 */       int column = this.locator.getColumnNumber();
/*     */ 
/* 264 */       if (null != systemID) {
/* 265 */         sbuffer.append("; SystemID: ");
/* 266 */         sbuffer.append(systemID);
/*     */       }
/*     */ 
/* 269 */       if (0 != line) {
/* 270 */         sbuffer.append("; Line#: ");
/* 271 */         sbuffer.append(line);
/*     */       }
/*     */ 
/* 274 */       if (0 != column) {
/* 275 */         sbuffer.append("; Column#: ");
/* 276 */         sbuffer.append(column);
/*     */       }
/*     */ 
/* 279 */       return sbuffer.toString();
/*     */     }
/* 281 */     return null;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 291 */     printStackTrace(new PrintWriter(System.err, true));
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 301 */     printStackTrace(new PrintWriter(s));
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s)
/*     */   {
/* 312 */     if (s == null) {
/* 313 */       s = new PrintWriter(System.err, true);
/*     */     }
/*     */     try
/*     */     {
/* 317 */       String locInfo = getLocationAsString();
/*     */ 
/* 319 */       if (null != locInfo) {
/* 320 */         s.println(locInfo);
/*     */       }
/*     */ 
/* 323 */       super.printStackTrace(s);
/*     */     } catch (Throwable e) {
/*     */     }
/* 326 */     boolean isJdk14OrHigher = false;
/*     */     try {
/* 328 */       Throwable.class.getMethod("getCause", (Class[])null);
/* 329 */       isJdk14OrHigher = true;
/*     */     }
/*     */     catch (NoSuchMethodException nsme)
/*     */     {
/*     */     }
/*     */ 
/* 337 */     if (!isJdk14OrHigher) {
/* 338 */       Throwable exception = getException();
/*     */ 
/* 340 */       for (int i = 0; (i < 10) && (null != exception); i++) {
/* 341 */         s.println("---------");
/*     */         try
/*     */         {
/* 344 */           if ((exception instanceof DTMException)) {
/* 345 */             String locInfo = ((DTMException)exception).getLocationAsString();
/*     */ 
/* 349 */             if (null != locInfo) {
/* 350 */               s.println(locInfo);
/*     */             }
/*     */           }
/*     */ 
/* 354 */           exception.printStackTrace(s);
/*     */         } catch (Throwable e) {
/* 356 */           s.println("Could not print stack trace...");
/*     */         }
/*     */         try
/*     */         {
/* 360 */           Method meth = exception.getClass().getMethod("getException", (Class[])null);
/*     */ 
/* 364 */           if (null != meth) {
/* 365 */             Throwable prev = exception;
/*     */ 
/* 367 */             exception = (Throwable)meth.invoke(exception, (Object[])null);
/*     */ 
/* 369 */             if (prev == exception)
/*     */               break;
/*     */           }
/*     */           else {
/* 373 */             exception = null;
/*     */           }
/*     */         } catch (InvocationTargetException ite) {
/* 376 */           exception = null;
/*     */         } catch (IllegalAccessException iae) {
/* 378 */           exception = null;
/*     */         } catch (NoSuchMethodException nsme) {
/* 380 */           exception = null;
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xml.internal.dtm.DTMException
 * JD-Core Version:    0.6.2
 */