/*     */ package javax.swing.text.html;
/*     */ 
/*     */ import java.awt.Color;
/*     */ import java.awt.Component;
/*     */ import java.beans.BeanInfo;
/*     */ import java.beans.IntrospectionException;
/*     */ import java.beans.Introspector;
/*     */ import java.beans.PropertyDescriptor;
/*     */ import java.io.PrintStream;
/*     */ import java.lang.reflect.Method;
/*     */ import javax.swing.JLabel;
/*     */ import javax.swing.text.AttributeSet;
/*     */ import javax.swing.text.ComponentView;
/*     */ import javax.swing.text.Element;
/*     */ import sun.reflect.misc.MethodUtil;
/*     */ import sun.reflect.misc.ReflectUtil;
/*     */ 
/*     */ public class ObjectView extends ComponentView
/*     */ {
/*     */   public ObjectView(Element paramElement)
/*     */   {
/*  81 */     super(paramElement);
/*     */   }
/*     */ 
/*     */   protected Component createComponent()
/*     */   {
/*  90 */     AttributeSet localAttributeSet = getElement().getAttributes();
/*  91 */     String str = (String)localAttributeSet.getAttribute(HTML.Attribute.CLASSID);
/*     */     try {
/*  93 */       ReflectUtil.checkPackageAccess(str);
/*  94 */       Class localClass = Class.forName(str, true, Thread.currentThread().getContextClassLoader());
/*     */ 
/*  96 */       Object localObject = localClass.newInstance();
/*  97 */       if ((localObject instanceof Component)) {
/*  98 */         Component localComponent = (Component)localObject;
/*  99 */         setParameters(localComponent, localAttributeSet);
/* 100 */         return localComponent;
/*     */       }
/*     */     }
/*     */     catch (Throwable localThrowable)
/*     */     {
/*     */     }
/*     */ 
/* 107 */     return getUnloadableRepresentation();
/*     */   }
/*     */ 
/*     */   Component getUnloadableRepresentation()
/*     */   {
/* 117 */     JLabel localJLabel = new JLabel("??");
/* 118 */     localJLabel.setForeground(Color.red);
/* 119 */     return localJLabel;
/*     */   }
/*     */ 
/*     */   private void setParameters(Component paramComponent, AttributeSet paramAttributeSet)
/*     */   {
/* 128 */     Class localClass = paramComponent.getClass();
/*     */     BeanInfo localBeanInfo;
/*     */     try
/*     */     {
/* 131 */       localBeanInfo = Introspector.getBeanInfo(localClass);
/*     */     } catch (IntrospectionException localIntrospectionException) {
/* 133 */       System.err.println("introspector failed, ex: " + localIntrospectionException);
/* 134 */       return;
/*     */     }
/* 136 */     PropertyDescriptor[] arrayOfPropertyDescriptor = localBeanInfo.getPropertyDescriptors();
/* 137 */     for (int i = 0; i < arrayOfPropertyDescriptor.length; i++)
/*     */     {
/* 139 */       Object localObject = paramAttributeSet.getAttribute(arrayOfPropertyDescriptor[i].getName());
/* 140 */       if ((localObject instanceof String))
/*     */       {
/* 142 */         String str = (String)localObject;
/* 143 */         Method localMethod = arrayOfPropertyDescriptor[i].getWriteMethod();
/* 144 */         if (localMethod == null)
/*     */         {
/* 146 */           return;
/*     */         }
/* 148 */         Class[] arrayOfClass = localMethod.getParameterTypes();
/* 149 */         if (arrayOfClass.length != 1)
/*     */         {
/* 151 */           return;
/*     */         }
/* 153 */         Object[] arrayOfObject = { str };
/*     */         try {
/* 155 */           MethodUtil.invoke(localMethod, paramComponent, arrayOfObject);
/*     */         } catch (Exception localException) {
/* 157 */           System.err.println("Invocation failed");
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.swing.text.html.ObjectView
 * JD-Core Version:    0.6.2
 */