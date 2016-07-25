/*     */ package com.sun.corba.se.impl.monitoring;
/*     */ 
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredAttribute;
/*     */ import com.sun.corba.se.spi.monitoring.MonitoredObject;
/*     */ import java.util.Collection;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.Map;
/*     */ 
/*     */ public class MonitoredObjectImpl
/*     */   implements MonitoredObject
/*     */ {
/*     */   private final String name;
/*     */   private final String description;
/*  41 */   private Map children = new HashMap();
/*     */ 
/*  44 */   private Map monitoredAttributes = new HashMap();
/*     */ 
/*  46 */   private MonitoredObject parent = null;
/*     */ 
/*     */   MonitoredObjectImpl(String paramString1, String paramString2)
/*     */   {
/*  51 */     this.name = paramString1;
/*  52 */     this.description = paramString2;
/*     */   }
/*     */ 
/*     */   public MonitoredObject getChild(String paramString) {
/*  56 */     synchronized (this) {
/*  57 */       return (MonitoredObject)this.children.get(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection getChildren() {
/*  62 */     synchronized (this) {
/*  63 */       return this.children.values();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addChild(MonitoredObject paramMonitoredObject) {
/*  68 */     if (paramMonitoredObject != null)
/*  69 */       synchronized (this) {
/*  70 */         this.children.put(paramMonitoredObject.getName(), paramMonitoredObject);
/*  71 */         paramMonitoredObject.setParent(this);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void removeChild(String paramString)
/*     */   {
/*  77 */     if (paramString != null)
/*  78 */       synchronized (this) {
/*  79 */         this.children.remove(paramString);
/*     */       }
/*     */   }
/*     */ 
/*     */   public synchronized MonitoredObject getParent()
/*     */   {
/*  85 */     return this.parent;
/*     */   }
/*     */ 
/*     */   public synchronized void setParent(MonitoredObject paramMonitoredObject) {
/*  89 */     this.parent = paramMonitoredObject;
/*     */   }
/*     */ 
/*     */   public MonitoredAttribute getAttribute(String paramString) {
/*  93 */     synchronized (this) {
/*  94 */       return (MonitoredAttribute)this.monitoredAttributes.get(paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public Collection getAttributes() {
/*  99 */     synchronized (this) {
/* 100 */       return this.monitoredAttributes.values();
/*     */     }
/*     */   }
/*     */ 
/*     */   public void addAttribute(MonitoredAttribute paramMonitoredAttribute) {
/* 105 */     if (paramMonitoredAttribute != null)
/* 106 */       synchronized (this) {
/* 107 */         this.monitoredAttributes.put(paramMonitoredAttribute.getName(), paramMonitoredAttribute);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void removeAttribute(String paramString)
/*     */   {
/* 113 */     if (paramString != null)
/* 114 */       synchronized (this) {
/* 115 */         this.monitoredAttributes.remove(paramString);
/*     */       }
/*     */   }
/*     */ 
/*     */   public void clearState()
/*     */   {
/* 125 */     synchronized (this) {
/* 126 */       Iterator localIterator = this.monitoredAttributes.values().iterator();
/*     */ 
/* 128 */       while (localIterator.hasNext()) {
/* 129 */         ((MonitoredAttribute)localIterator.next()).clearState();
/*     */       }
/* 131 */       localIterator = this.children.values().iterator();
/*     */ 
/* 133 */       while (localIterator.hasNext())
/* 134 */         ((MonitoredObject)localIterator.next()).clearState();
/*     */     }
/*     */   }
/*     */ 
/*     */   public String getName()
/*     */   {
/* 140 */     return this.name;
/*     */   }
/*     */ 
/*     */   public String getDescription() {
/* 144 */     return this.description;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.monitoring.MonitoredObjectImpl
 * JD-Core Version:    0.6.2
 */