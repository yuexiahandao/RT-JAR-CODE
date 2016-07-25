/*     */ package java.beans;
/*     */ 
/*     */ import java.util.EventObject;
/*     */ 
/*     */ public class PropertyChangeEvent extends EventObject
/*     */ {
/*     */   private static final long serialVersionUID = 7042693688939648123L;
/*     */   private String propertyName;
/*     */   private Object newValue;
/*     */   private Object oldValue;
/*     */   private Object propagationId;
/*     */ 
/*     */   public PropertyChangeEvent(Object paramObject1, String paramString, Object paramObject2, Object paramObject3)
/*     */   {
/*  60 */     super(paramObject1);
/*  61 */     this.propertyName = paramString;
/*  62 */     this.newValue = paramObject3;
/*  63 */     this.oldValue = paramObject2;
/*     */   }
/*     */ 
/*     */   public String getPropertyName()
/*     */   {
/*  73 */     return this.propertyName;
/*     */   }
/*     */ 
/*     */   public Object getNewValue()
/*     */   {
/*  83 */     return this.newValue;
/*     */   }
/*     */ 
/*     */   public Object getOldValue()
/*     */   {
/*  93 */     return this.oldValue;
/*     */   }
/*     */ 
/*     */   public void setPropagationId(Object paramObject)
/*     */   {
/* 102 */     this.propagationId = paramObject;
/*     */   }
/*     */ 
/*     */   public Object getPropagationId()
/*     */   {
/* 116 */     return this.propagationId;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 152 */     StringBuilder localStringBuilder = new StringBuilder(getClass().getName());
/* 153 */     localStringBuilder.append("[propertyName=").append(getPropertyName());
/* 154 */     appendTo(localStringBuilder);
/* 155 */     localStringBuilder.append("; oldValue=").append(getOldValue());
/* 156 */     localStringBuilder.append("; newValue=").append(getNewValue());
/* 157 */     localStringBuilder.append("; propagationId=").append(getPropagationId());
/* 158 */     localStringBuilder.append("; source=").append(getSource());
/* 159 */     return "]";
/*     */   }
/*     */ 
/*     */   void appendTo(StringBuilder paramStringBuilder)
/*     */   {
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.PropertyChangeEvent
 * JD-Core Version:    0.6.2
 */