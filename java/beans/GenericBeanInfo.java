/*      */ package java.beans;
/*      */ 
/*      */ import com.sun.beans.finder.BeanInfoFinder;
/*      */ import java.awt.Image;
/*      */ import java.lang.ref.Reference;
/*      */ import java.lang.ref.SoftReference;
/*      */ 
/*      */ class GenericBeanInfo extends SimpleBeanInfo
/*      */ {
/*      */   private BeanDescriptor beanDescriptor;
/*      */   private EventSetDescriptor[] events;
/*      */   private int defaultEvent;
/*      */   private PropertyDescriptor[] properties;
/*      */   private int defaultProperty;
/*      */   private MethodDescriptor[] methods;
/*      */   private Reference<BeanInfo> targetBeanInfoRef;
/*      */ 
/*      */   public GenericBeanInfo(BeanDescriptor paramBeanDescriptor, EventSetDescriptor[] paramArrayOfEventSetDescriptor, int paramInt1, PropertyDescriptor[] paramArrayOfPropertyDescriptor, int paramInt2, MethodDescriptor[] paramArrayOfMethodDescriptor, BeanInfo paramBeanInfo)
/*      */   {
/* 1468 */     this.beanDescriptor = paramBeanDescriptor;
/* 1469 */     this.events = paramArrayOfEventSetDescriptor;
/* 1470 */     this.defaultEvent = paramInt1;
/* 1471 */     this.properties = paramArrayOfPropertyDescriptor;
/* 1472 */     this.defaultProperty = paramInt2;
/* 1473 */     this.methods = paramArrayOfMethodDescriptor;
/* 1474 */     this.targetBeanInfoRef = (paramBeanInfo != null ? new SoftReference(paramBeanInfo) : null);
/*      */   }
/*      */ 
/*      */   GenericBeanInfo(GenericBeanInfo paramGenericBeanInfo)
/*      */   {
/* 1485 */     this.beanDescriptor = new BeanDescriptor(paramGenericBeanInfo.beanDescriptor);
/*      */     int i;
/*      */     int j;
/* 1486 */     if (paramGenericBeanInfo.events != null) {
/* 1487 */       i = paramGenericBeanInfo.events.length;
/* 1488 */       this.events = new EventSetDescriptor[i];
/* 1489 */       for (j = 0; j < i; j++) {
/* 1490 */         this.events[j] = new EventSetDescriptor(paramGenericBeanInfo.events[j]);
/*      */       }
/*      */     }
/* 1493 */     this.defaultEvent = paramGenericBeanInfo.defaultEvent;
/* 1494 */     if (paramGenericBeanInfo.properties != null) {
/* 1495 */       i = paramGenericBeanInfo.properties.length;
/* 1496 */       this.properties = new PropertyDescriptor[i];
/* 1497 */       for (j = 0; j < i; j++) {
/* 1498 */         PropertyDescriptor localPropertyDescriptor = paramGenericBeanInfo.properties[j];
/* 1499 */         if ((localPropertyDescriptor instanceof IndexedPropertyDescriptor)) {
/* 1500 */           this.properties[j] = new IndexedPropertyDescriptor((IndexedPropertyDescriptor)localPropertyDescriptor);
/*      */         }
/*      */         else {
/* 1503 */           this.properties[j] = new PropertyDescriptor(localPropertyDescriptor);
/*      */         }
/*      */       }
/*      */     }
/* 1507 */     this.defaultProperty = paramGenericBeanInfo.defaultProperty;
/* 1508 */     if (paramGenericBeanInfo.methods != null) {
/* 1509 */       i = paramGenericBeanInfo.methods.length;
/* 1510 */       this.methods = new MethodDescriptor[i];
/* 1511 */       for (j = 0; j < i; j++) {
/* 1512 */         this.methods[j] = new MethodDescriptor(paramGenericBeanInfo.methods[j]);
/*      */       }
/*      */     }
/* 1515 */     this.targetBeanInfoRef = paramGenericBeanInfo.targetBeanInfoRef;
/*      */   }
/*      */ 
/*      */   public PropertyDescriptor[] getPropertyDescriptors() {
/* 1519 */     return this.properties;
/*      */   }
/*      */ 
/*      */   public int getDefaultPropertyIndex() {
/* 1523 */     return this.defaultProperty;
/*      */   }
/*      */ 
/*      */   public EventSetDescriptor[] getEventSetDescriptors() {
/* 1527 */     return this.events;
/*      */   }
/*      */ 
/*      */   public int getDefaultEventIndex() {
/* 1531 */     return this.defaultEvent;
/*      */   }
/*      */ 
/*      */   public MethodDescriptor[] getMethodDescriptors() {
/* 1535 */     return this.methods;
/*      */   }
/*      */ 
/*      */   public BeanDescriptor getBeanDescriptor() {
/* 1539 */     return this.beanDescriptor;
/*      */   }
/*      */ 
/*      */   public Image getIcon(int paramInt) {
/* 1543 */     BeanInfo localBeanInfo = getTargetBeanInfo();
/* 1544 */     if (localBeanInfo != null) {
/* 1545 */       return localBeanInfo.getIcon(paramInt);
/*      */     }
/* 1547 */     return super.getIcon(paramInt);
/*      */   }
/*      */ 
/*      */   private BeanInfo getTargetBeanInfo() {
/* 1551 */     if (this.targetBeanInfoRef == null) {
/* 1552 */       return null;
/*      */     }
/* 1554 */     BeanInfo localBeanInfo = (BeanInfo)this.targetBeanInfoRef.get();
/* 1555 */     if (localBeanInfo == null) {
/* 1556 */       localBeanInfo = (BeanInfo)ThreadGroupContext.getContext().getBeanInfoFinder().find(this.beanDescriptor.getBeanClass());
/*      */ 
/* 1558 */       if (localBeanInfo != null) {
/* 1559 */         this.targetBeanInfoRef = new SoftReference(localBeanInfo);
/*      */       }
/*      */     }
/* 1562 */     return localBeanInfo;
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     java.beans.GenericBeanInfo
 * JD-Core Version:    0.6.2
 */