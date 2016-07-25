/*     */ package com.sun.xml.internal.bind.v2.runtime;
/*     */ 
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import java.util.HashMap;
/*     */ import javax.xml.bind.ValidationEventHandler;
/*     */ import javax.xml.bind.ValidationEventLocator;
/*     */ import javax.xml.bind.annotation.adapters.XmlAdapter;
/*     */ import javax.xml.bind.helpers.ValidationEventImpl;
/*     */ import org.xml.sax.ErrorHandler;
/*     */ import org.xml.sax.SAXException;
/*     */ import org.xml.sax.SAXParseException;
/*     */ 
/*     */ public abstract class Coordinator
/*     */   implements ErrorHandler, ValidationEventHandler
/*     */ {
/*  67 */   private final HashMap<Class<? extends XmlAdapter>, XmlAdapter> adapters = new HashMap();
/*     */   private Object old;
/*     */   private Object[] table;
/*     */   public Exception guyWhoSetTheTableToNull;
/*     */   private static final ThreadLocal<Object[]> activeTable;
/*     */   public static boolean debugTableNPE;
/*     */ 
/*     */   public final XmlAdapter putAdapter(Class<? extends XmlAdapter> c, XmlAdapter a)
/*     */   {
/*  72 */     if (a == null) {
/*  73 */       return (XmlAdapter)this.adapters.remove(c);
/*     */     }
/*  75 */     return (XmlAdapter)this.adapters.put(c, a);
/*     */   }
/*     */ 
/*     */   public final <T extends XmlAdapter> T getAdapter(Class<T> key)
/*     */   {
/*  85 */     XmlAdapter v = (XmlAdapter)key.cast(this.adapters.get(key));
/*  86 */     if (v == null) {
/*  87 */       v = (XmlAdapter)ClassFactory.create(key);
/*  88 */       putAdapter(key, v);
/*     */     }
/*  90 */     return v;
/*     */   }
/*     */ 
/*     */   public <T extends XmlAdapter> boolean containsAdapter(Class<T> type) {
/*  94 */     return this.adapters.containsKey(type);
/*     */   }
/*     */ 
/*     */   protected final void setThreadAffinity()
/*     */   {
/* 120 */     this.table = ((Object[])activeTable.get());
/* 121 */     assert (this.table != null);
/*     */   }
/*     */ 
/*     */   protected final void resetThreadAffinity()
/*     */   {
/* 129 */     if (activeTable != null) {
/* 130 */       activeTable.remove();
/*     */     }
/* 132 */     if (debugTableNPE)
/* 133 */       this.guyWhoSetTheTableToNull = new Exception();
/* 134 */     this.table = null;
/*     */   }
/*     */ 
/*     */   protected final void pushCoordinator()
/*     */   {
/* 141 */     this.old = this.table[0];
/* 142 */     this.table[0] = this;
/*     */   }
/*     */ 
/*     */   protected final void popCoordinator()
/*     */   {
/* 149 */     assert (this.table[0] == this);
/* 150 */     this.table[0] = this.old;
/* 151 */     this.old = null;
/*     */   }
/*     */ 
/*     */   public static Coordinator _getInstance() {
/* 155 */     return (Coordinator)((Object[])activeTable.get())[0];
/*     */   }
/*     */ 
/*     */   protected abstract ValidationEventLocator getLocation();
/*     */ 
/*     */   public final void error(SAXParseException exception)
/*     */     throws SAXException
/*     */   {
/* 177 */     propagateEvent(1, exception);
/*     */   }
/*     */ 
/*     */   public final void warning(SAXParseException exception) throws SAXException {
/* 181 */     propagateEvent(0, exception);
/*     */   }
/*     */ 
/*     */   public final void fatalError(SAXParseException exception) throws SAXException {
/* 185 */     propagateEvent(2, exception);
/*     */   }
/*     */ 
/*     */   private void propagateEvent(int severity, SAXParseException saxException)
/*     */     throws SAXException
/*     */   {
/* 191 */     ValidationEventImpl ve = new ValidationEventImpl(severity, saxException.getMessage(), getLocation());
/*     */ 
/* 194 */     Exception e = saxException.getException();
/* 195 */     if (e != null)
/* 196 */       ve.setLinkedException(e);
/*     */     else {
/* 198 */       ve.setLinkedException(saxException);
/*     */     }
/*     */ 
/* 203 */     boolean result = handleEvent(ve);
/* 204 */     if (!result)
/*     */     {
/* 207 */       throw saxException;
/*     */     }
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 159 */     activeTable = new ThreadLocal()
/*     */     {
/*     */       public Object[] initialValue() {
/* 162 */         return new Object[1];
/*     */       }
/*     */ 
/*     */     };
/*     */     try
/*     */     {
/* 215 */       debugTableNPE = Boolean.getBoolean(Coordinator.class.getName() + ".debugTableNPE");
/*     */     }
/*     */     catch (SecurityException t)
/*     */     {
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.Coordinator
 * JD-Core Version:    0.6.2
 */