/*     */ package javax.xml.transform;
/*     */ 
/*     */ import java.io.PrintStream;
/*     */ import java.io.PrintWriter;
/*     */ import java.lang.reflect.InvocationTargetException;
/*     */ import java.lang.reflect.Method;
/*     */ 
/*     */ public class TransformerException extends Exception
/*     */ {
/*     */   SourceLocator locator;
/*     */   Throwable containedException;
/*     */ 
/*     */   public SourceLocator getLocator()
/*     */   {
/*  47 */     return this.locator;
/*     */   }
/*     */ 
/*     */   public void setLocator(SourceLocator location)
/*     */   {
/*  57 */     this.locator = location;
/*     */   }
/*     */ 
/*     */   public Throwable getException()
/*     */   {
/*  70 */     return this.containedException;
/*     */   }
/*     */ 
/*     */   public Throwable getCause()
/*     */   {
/*  80 */     return this.containedException == this ? null : this.containedException;
/*     */   }
/*     */ 
/*     */   public synchronized Throwable initCause(Throwable cause)
/*     */   {
/* 111 */     if (this.containedException != null) {
/* 112 */       throw new IllegalStateException("Can't overwrite cause");
/*     */     }
/*     */ 
/* 115 */     if (cause == this) {
/* 116 */       throw new IllegalArgumentException("Self-causation not permitted");
/*     */     }
/*     */ 
/* 120 */     this.containedException = cause;
/*     */ 
/* 122 */     return this;
/*     */   }
/*     */ 
/*     */   public TransformerException(String message)
/*     */   {
/* 132 */     super(message);
/*     */ 
/* 134 */     this.containedException = null;
/* 135 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public TransformerException(Throwable e)
/*     */   {
/* 145 */     super(e.toString());
/*     */ 
/* 147 */     this.containedException = e;
/* 148 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public TransformerException(String message, Throwable e)
/*     */   {
/* 163 */     super((message == null) || (message.length() == 0) ? e.toString() : message);
/*     */ 
/* 167 */     this.containedException = e;
/* 168 */     this.locator = null;
/*     */   }
/*     */ 
/*     */   public TransformerException(String message, SourceLocator locator)
/*     */   {
/* 183 */     super(message);
/*     */ 
/* 185 */     this.containedException = null;
/* 186 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   public TransformerException(String message, SourceLocator locator, Throwable e)
/*     */   {
/* 200 */     super(message);
/*     */ 
/* 202 */     this.containedException = e;
/* 203 */     this.locator = locator;
/*     */   }
/*     */ 
/*     */   public String getMessageAndLocation()
/*     */   {
/* 215 */     StringBuffer sbuffer = new StringBuffer();
/* 216 */     String message = super.getMessage();
/*     */ 
/* 218 */     if (null != message) {
/* 219 */       sbuffer.append(message);
/*     */     }
/*     */ 
/* 222 */     if (null != this.locator) {
/* 223 */       String systemID = this.locator.getSystemId();
/* 224 */       int line = this.locator.getLineNumber();
/* 225 */       int column = this.locator.getColumnNumber();
/*     */ 
/* 227 */       if (null != systemID) {
/* 228 */         sbuffer.append("; SystemID: ");
/* 229 */         sbuffer.append(systemID);
/*     */       }
/*     */ 
/* 232 */       if (0 != line) {
/* 233 */         sbuffer.append("; Line#: ");
/* 234 */         sbuffer.append(line);
/*     */       }
/*     */ 
/* 237 */       if (0 != column) {
/* 238 */         sbuffer.append("; Column#: ");
/* 239 */         sbuffer.append(column);
/*     */       }
/*     */     }
/*     */ 
/* 243 */     return sbuffer.toString();
/*     */   }
/*     */ 
/*     */   public String getLocationAsString()
/*     */   {
/* 254 */     if (null != this.locator) {
/* 255 */       StringBuffer sbuffer = new StringBuffer();
/* 256 */       String systemID = this.locator.getSystemId();
/* 257 */       int line = this.locator.getLineNumber();
/* 258 */       int column = this.locator.getColumnNumber();
/*     */ 
/* 260 */       if (null != systemID) {
/* 261 */         sbuffer.append("; SystemID: ");
/* 262 */         sbuffer.append(systemID);
/*     */       }
/*     */ 
/* 265 */       if (0 != line) {
/* 266 */         sbuffer.append("; Line#: ");
/* 267 */         sbuffer.append(line);
/*     */       }
/*     */ 
/* 270 */       if (0 != column) {
/* 271 */         sbuffer.append("; Column#: ");
/* 272 */         sbuffer.append(column);
/*     */       }
/*     */ 
/* 275 */       return sbuffer.toString();
/*     */     }
/* 277 */     return null;
/*     */   }
/*     */ 
/*     */   public void printStackTrace()
/*     */   {
/* 287 */     printStackTrace(new PrintWriter(System.err, true));
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintStream s)
/*     */   {
/* 297 */     printStackTrace(new PrintWriter(s));
/*     */   }
/*     */ 
/*     */   public void printStackTrace(PrintWriter s)
/*     */   {
/* 308 */     if (s == null) {
/* 309 */       s = new PrintWriter(System.err, true);
/*     */     }
/*     */     try
/*     */     {
/* 313 */       String locInfo = getLocationAsString();
/*     */ 
/* 315 */       if (null != locInfo) {
/* 316 */         s.println(locInfo);
/*     */       }
/*     */ 
/* 319 */       super.printStackTrace(s);
/*     */     } catch (Throwable e) {
/*     */     }
/* 322 */     Throwable exception = getException();
/*     */ 
/* 324 */     for (int i = 0; (i < 10) && (null != exception); i++) {
/* 325 */       s.println("---------");
/*     */       try
/*     */       {
/* 328 */         if ((exception instanceof TransformerException)) {
/* 329 */           String locInfo = ((TransformerException)exception).getLocationAsString();
/*     */ 
/* 333 */           if (null != locInfo) {
/* 334 */             s.println(locInfo);
/*     */           }
/*     */         }
/*     */ 
/* 338 */         exception.printStackTrace(s);
/*     */       } catch (Throwable e) {
/* 340 */         s.println("Could not print stack trace...");
/*     */       }
/*     */       try
/*     */       {
/* 344 */         Method meth = exception.getClass().getMethod("getException", (Class[])null);
/*     */ 
/* 348 */         if (null != meth) {
/* 349 */           Throwable prev = exception;
/*     */ 
/* 351 */           exception = (Throwable)meth.invoke(exception, (Object[])null);
/*     */ 
/* 353 */           if (prev == exception)
/*     */             break;
/*     */         }
/*     */         else {
/* 357 */           exception = null;
/*     */         }
/*     */       } catch (InvocationTargetException ite) {
/* 360 */         exception = null;
/*     */       } catch (IllegalAccessException iae) {
/* 362 */         exception = null;
/*     */       } catch (NoSuchMethodException nsme) {
/* 364 */         exception = null;
/*     */       }
/*     */     }
/*     */ 
/* 368 */     s.flush();
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.xml.transform.TransformerException
 * JD-Core Version:    0.6.2
 */