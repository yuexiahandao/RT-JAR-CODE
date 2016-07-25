/*     */ package java.beans;
/*     */ 
/*     */ import java.awt.Image;
/*     */ import java.awt.Toolkit;
/*     */ import java.awt.image.ImageProducer;
/*     */ import java.net.URL;
/*     */ 
/*     */ public class SimpleBeanInfo
/*     */   implements BeanInfo
/*     */ {
/*     */   public BeanDescriptor getBeanDescriptor()
/*     */   {
/*  51 */     return null;
/*     */   }
/*     */ 
/*     */   public PropertyDescriptor[] getPropertyDescriptors()
/*     */   {
/*  59 */     return null;
/*     */   }
/*     */ 
/*     */   public int getDefaultPropertyIndex()
/*     */   {
/*  67 */     return -1;
/*     */   }
/*     */ 
/*     */   public EventSetDescriptor[] getEventSetDescriptors()
/*     */   {
/*  75 */     return null;
/*     */   }
/*     */ 
/*     */   public int getDefaultEventIndex()
/*     */   {
/*  83 */     return -1;
/*     */   }
/*     */ 
/*     */   public MethodDescriptor[] getMethodDescriptors()
/*     */   {
/*  91 */     return null;
/*     */   }
/*     */ 
/*     */   public BeanInfo[] getAdditionalBeanInfo()
/*     */   {
/* 100 */     return null;
/*     */   }
/*     */ 
/*     */   public Image getIcon(int paramInt)
/*     */   {
/* 108 */     return null;
/*     */   }
/*     */ 
/*     */   public Image loadImage(String paramString)
/*     */   {
/*     */     try
/*     */     {
/* 124 */       URL localURL = getClass().getResource(paramString);
/* 125 */       if (localURL != null) {
/* 126 */         ImageProducer localImageProducer = (ImageProducer)localURL.getContent();
/* 127 */         if (localImageProducer != null)
/* 128 */           return Toolkit.getDefaultToolkit().createImage(localImageProducer);
/*     */       }
/*     */     }
/*     */     catch (Exception localException) {
/*     */     }
/* 133 */     return null;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.SimpleBeanInfo
 * JD-Core Version:    0.6.2
 */