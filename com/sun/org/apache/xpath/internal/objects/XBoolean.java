/*     */ package com.sun.org.apache.xpath.internal.objects;
/*     */ 
/*     */ import com.sun.org.apache.xml.internal.utils.WrappedRuntimeException;
/*     */ import javax.xml.transform.TransformerException;
/*     */ 
/*     */ public class XBoolean extends XObject
/*     */ {
/*     */   static final long serialVersionUID = -2964933058866100881L;
/*  38 */   public static final XBoolean S_TRUE = new XBooleanStatic(true);
/*     */ 
/*  44 */   public static final XBoolean S_FALSE = new XBooleanStatic(false);
/*     */   private final boolean m_val;
/*     */ 
/*     */   public XBoolean(boolean b)
/*     */   {
/*  60 */     this.m_val = b;
/*     */   }
/*     */ 
/*     */   public XBoolean(Boolean b)
/*     */   {
/*  73 */     this.m_val = b.booleanValue();
/*  74 */     setObject(b);
/*     */   }
/*     */ 
/*     */   public int getType()
/*     */   {
/*  85 */     return 1;
/*     */   }
/*     */ 
/*     */   public String getTypeString()
/*     */   {
/*  96 */     return "#BOOLEAN";
/*     */   }
/*     */ 
/*     */   public double num()
/*     */   {
/* 106 */     return this.m_val ? 1.0D : 0.0D;
/*     */   }
/*     */ 
/*     */   public boolean bool()
/*     */   {
/* 116 */     return this.m_val;
/*     */   }
/*     */ 
/*     */   public String str()
/*     */   {
/* 126 */     return this.m_val ? "true" : "false";
/*     */   }
/*     */ 
/*     */   public Object object()
/*     */   {
/* 137 */     if (null == this.m_obj)
/* 138 */       setObject(new Boolean(this.m_val));
/* 139 */     return this.m_obj;
/*     */   }
/*     */ 
/*     */   public boolean equals(XObject obj2)
/*     */   {
/* 157 */     if (obj2.getType() == 4) {
/* 158 */       return obj2.equals(this);
/*     */     }
/*     */     try
/*     */     {
/* 162 */       return this.m_val == obj2.bool();
/*     */     }
/*     */     catch (TransformerException te)
/*     */     {
/* 166 */       throw new WrappedRuntimeException(te);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.org.apache.xpath.internal.objects.XBoolean
 * JD-Core Version:    0.6.2
 */