/*     */ package com.sun.jmx.mbeanserver;
/*     */ 
/*     */ import com.sun.jmx.defaults.JmxProperties;
/*     */ import java.util.logging.Level;
/*     */ import java.util.logging.Logger;
/*     */ import javax.management.Attribute;
/*     */ import javax.management.AttributeList;
/*     */ import javax.management.AttributeNotFoundException;
/*     */ import javax.management.DynamicMBean;
/*     */ import javax.management.InvalidAttributeValueException;
/*     */ import javax.management.JMRuntimeException;
/*     */ import javax.management.MBeanAttributeInfo;
/*     */ import javax.management.MBeanException;
/*     */ import javax.management.MBeanInfo;
/*     */ import javax.management.MBeanRegistration;
/*     */ import javax.management.MBeanServer;
/*     */ import javax.management.MBeanServerDelegate;
/*     */ import javax.management.ObjectName;
/*     */ import javax.management.ReflectionException;
/*     */ import javax.management.RuntimeOperationsException;
/*     */ 
/*     */ final class MBeanServerDelegateImpl extends MBeanServerDelegate
/*     */   implements DynamicMBean, MBeanRegistration
/*     */ {
/*  56 */   private static final String[] attributeNames = { "MBeanServerId", "SpecificationName", "SpecificationVersion", "SpecificationVendor", "ImplementationName", "ImplementationVersion", "ImplementationVendor" };
/*     */ 
/*  66 */   private static final MBeanAttributeInfo[] attributeInfos = { new MBeanAttributeInfo("MBeanServerId", "java.lang.String", "The MBean server agent identification", true, false, false), new MBeanAttributeInfo("SpecificationName", "java.lang.String", "The full name of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("SpecificationVersion", "java.lang.String", "The version of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("SpecificationVendor", "java.lang.String", "The vendor of the JMX specification implemented by this product.", true, false, false), new MBeanAttributeInfo("ImplementationName", "java.lang.String", "The JMX implementation name (the name of this product)", true, false, false), new MBeanAttributeInfo("ImplementationVersion", "java.lang.String", "The JMX implementation version (the version of this product).", true, false, false), new MBeanAttributeInfo("ImplementationVendor", "java.lang.String", "the JMX implementation vendor (the vendor of this product).", true, false, false) };
/*     */   private final MBeanInfo delegateInfo;
/*     */ 
/*     */   public MBeanServerDelegateImpl()
/*     */   {
/* 101 */     this.delegateInfo = new MBeanInfo("javax.management.MBeanServerDelegate", "Represents  the MBean server from the management point of view.", attributeInfos, null, null, getNotificationInfo());
/*     */   }
/*     */ 
/*     */   public final ObjectName preRegister(MBeanServer paramMBeanServer, ObjectName paramObjectName)
/*     */     throws Exception
/*     */   {
/* 111 */     if (paramObjectName == null) return DELEGATE_NAME;
/* 112 */     return paramObjectName;
/*     */   }
/*     */ 
/*     */   public final void postRegister(Boolean paramBoolean)
/*     */   {
/*     */   }
/*     */ 
/*     */   public final void preDeregister() throws Exception {
/* 120 */     throw new IllegalArgumentException("The MBeanServerDelegate MBean cannot be unregistered");
/*     */   }
/*     */ 
/*     */   public final void postDeregister()
/*     */   {
/*     */   }
/*     */ 
/*     */   public Object getAttribute(String paramString)
/*     */     throws AttributeNotFoundException, MBeanException, ReflectionException
/*     */   {
/*     */     try
/*     */     {
/* 145 */       if (paramString == null) {
/* 146 */         throw new AttributeNotFoundException("null");
/*     */       }
/*     */ 
/* 150 */       if (paramString.equals("MBeanServerId"))
/* 151 */         return getMBeanServerId();
/* 152 */       if (paramString.equals("SpecificationName"))
/* 153 */         return getSpecificationName();
/* 154 */       if (paramString.equals("SpecificationVersion"))
/* 155 */         return getSpecificationVersion();
/* 156 */       if (paramString.equals("SpecificationVendor"))
/* 157 */         return getSpecificationVendor();
/* 158 */       if (paramString.equals("ImplementationName"))
/* 159 */         return getImplementationName();
/* 160 */       if (paramString.equals("ImplementationVersion"))
/* 161 */         return getImplementationVersion();
/* 162 */       if (paramString.equals("ImplementationVendor")) {
/* 163 */         return getImplementationVendor();
/*     */       }
/*     */ 
/* 168 */       throw new AttributeNotFoundException("null");
/*     */     }
/*     */     catch (AttributeNotFoundException localAttributeNotFoundException) {
/* 171 */       throw localAttributeNotFoundException;
/*     */     } catch (JMRuntimeException localJMRuntimeException) {
/* 173 */       throw localJMRuntimeException;
/*     */     } catch (SecurityException localSecurityException) {
/* 175 */       throw localSecurityException;
/*     */     } catch (Exception localException) {
/* 177 */       throw new MBeanException(localException, "Failed to get " + paramString);
/*     */     }
/*     */   }
/*     */ 
/*     */   public void setAttribute(Attribute paramAttribute)
/*     */     throws AttributeNotFoundException, InvalidAttributeValueException, MBeanException, ReflectionException
/*     */   {
/* 198 */     String str = paramAttribute == null ? null : paramAttribute.getName();
/* 199 */     if (str == null) {
/* 200 */       localObject = new IllegalArgumentException("Attribute name cannot be null");
/*     */ 
/* 202 */       throw new RuntimeOperationsException((RuntimeException)localObject, "Exception occurred trying to invoke the setter on the MBean");
/*     */     }
/*     */ 
/* 209 */     Object localObject = getAttribute(str);
/*     */ 
/* 215 */     throw new AttributeNotFoundException(str + " not accessible");
/*     */   }
/*     */ 
/*     */   public AttributeList getAttributes(String[] paramArrayOfString)
/*     */   {
/* 230 */     String[] arrayOfString = paramArrayOfString == null ? attributeNames : paramArrayOfString;
/*     */ 
/* 234 */     int i = arrayOfString.length;
/* 235 */     AttributeList localAttributeList = new AttributeList(i);
/*     */ 
/* 239 */     for (int j = 0; j < i; j++) {
/*     */       try {
/* 241 */         Attribute localAttribute = new Attribute(arrayOfString[j], getAttribute(arrayOfString[j]));
/*     */ 
/* 243 */         localAttributeList.add(localAttribute);
/*     */       }
/*     */       catch (Exception localException)
/*     */       {
/* 247 */         if (JmxProperties.MBEANSERVER_LOGGER.isLoggable(Level.FINEST)) {
/* 248 */           JmxProperties.MBEANSERVER_LOGGER.logp(Level.FINEST, MBeanServerDelegateImpl.class.getName(), "getAttributes", "Attribute " + arrayOfString[j] + " not found");
/*     */         }
/*     */ 
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 258 */     return localAttributeList;
/*     */   }
/*     */ 
/*     */   public AttributeList setAttributes(AttributeList paramAttributeList)
/*     */   {
/* 273 */     return new AttributeList(0);
/*     */   }
/*     */ 
/*     */   public Object invoke(String paramString, Object[] paramArrayOfObject, String[] paramArrayOfString)
/*     */     throws MBeanException, ReflectionException
/*     */   {
/* 298 */     if (paramString == null) {
/* 299 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Operation name  cannot be null");
/*     */ 
/* 301 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Exception occurred trying to invoke the operation on the MBean");
/*     */     }
/*     */ 
/* 305 */     throw new ReflectionException(new NoSuchMethodException(paramString), "The operation with name " + paramString + " could not be found");
/*     */   }
/*     */ 
/*     */   public MBeanInfo getMBeanInfo()
/*     */   {
/* 318 */     return this.delegateInfo;
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.jmx.mbeanserver.MBeanServerDelegateImpl
 * JD-Core Version:    0.6.2
 */