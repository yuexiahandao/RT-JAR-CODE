/*     */ package com.sun.corba.se.impl.presentation.rmi;
/*     */ 
/*     */ import com.sun.corba.se.impl.logging.ORBUtilSystemException;
/*     */ import com.sun.corba.se.impl.orbutil.graph.Graph;
/*     */ import com.sun.corba.se.impl.orbutil.graph.GraphImpl;
/*     */ import com.sun.corba.se.impl.orbutil.graph.Node;
/*     */ import com.sun.corba.se.spi.orbutil.proxy.InvocationHandlerFactory;
/*     */ import com.sun.corba.se.spi.presentation.rmi.DynamicMethodMarshaller;
/*     */ import com.sun.corba.se.spi.presentation.rmi.IDLNameTranslator;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.ClassData;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactory;
/*     */ import com.sun.corba.se.spi.presentation.rmi.PresentationManager.StubFactoryFactory;
/*     */ import java.lang.reflect.Method;
/*     */ import java.rmi.Remote;
/*     */ import java.util.ArrayList;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Set;
/*     */ import javax.rmi.CORBA.Tie;
/*     */ 
/*     */ public final class PresentationManagerImpl
/*     */   implements PresentationManager
/*     */ {
/*     */   private Map classToClassData;
/*     */   private Map methodToDMM;
/*     */   private PresentationManager.StubFactoryFactory staticStubFactoryFactory;
/*     */   private PresentationManager.StubFactoryFactory dynamicStubFactoryFactory;
/*  65 */   private ORBUtilSystemException wrapper = null;
/*     */   private boolean useDynamicStubs;
/*     */ 
/*     */   public PresentationManagerImpl(boolean paramBoolean)
/*     */   {
/*  70 */     this.useDynamicStubs = paramBoolean;
/*  71 */     this.wrapper = ORBUtilSystemException.get("rpc.presentation");
/*     */ 
/*  75 */     this.classToClassData = new HashMap();
/*  76 */     this.methodToDMM = new HashMap();
/*     */   }
/*     */ 
/*     */   public synchronized DynamicMethodMarshaller getDynamicMethodMarshaller(Method paramMethod)
/*     */   {
/*  86 */     if (paramMethod == null) {
/*  87 */       return null;
/*     */     }
/*  89 */     Object localObject = (DynamicMethodMarshaller)this.methodToDMM.get(paramMethod);
/*     */ 
/*  91 */     if (localObject == null) {
/*  92 */       localObject = new DynamicMethodMarshallerImpl(paramMethod);
/*  93 */       this.methodToDMM.put(paramMethod, localObject);
/*     */     }
/*     */ 
/*  96 */     return localObject;
/*     */   }
/*     */ 
/*     */   public synchronized PresentationManager.ClassData getClassData(Class paramClass)
/*     */   {
/* 101 */     Object localObject = (PresentationManager.ClassData)this.classToClassData.get(paramClass);
/* 102 */     if (localObject == null) {
/* 103 */       localObject = new ClassDataImpl(paramClass);
/* 104 */       this.classToClassData.put(paramClass, localObject);
/*     */     }
/*     */ 
/* 107 */     return localObject;
/*     */   }
/*     */ 
/*     */   public PresentationManager.StubFactoryFactory getStubFactoryFactory(boolean paramBoolean)
/*     */   {
/* 167 */     if (paramBoolean) {
/* 168 */       return this.dynamicStubFactoryFactory;
/*     */     }
/* 170 */     return this.staticStubFactoryFactory;
/*     */   }
/*     */ 
/*     */   public void setStubFactoryFactory(boolean paramBoolean, PresentationManager.StubFactoryFactory paramStubFactoryFactory)
/*     */   {
/* 176 */     if (paramBoolean)
/* 177 */       this.dynamicStubFactoryFactory = paramStubFactoryFactory;
/*     */     else
/* 179 */       this.staticStubFactoryFactory = paramStubFactoryFactory;
/*     */   }
/*     */ 
/*     */   public Tie getTie()
/*     */   {
/* 184 */     return this.dynamicStubFactoryFactory.getTie(null);
/*     */   }
/*     */ 
/*     */   public boolean useDynamicStubs()
/*     */   {
/* 189 */     return this.useDynamicStubs;
/*     */   }
/*     */ 
/*     */   private Set getRootSet(Class paramClass, NodeImpl paramNodeImpl, Graph paramGraph)
/*     */   {
/* 198 */     Set localSet = null;
/*     */ 
/* 200 */     if (paramClass.isInterface()) {
/* 201 */       paramGraph.add(paramNodeImpl);
/* 202 */       localSet = paramGraph.getRoots();
/*     */     }
/*     */     else {
/* 205 */       Class localClass = paramClass;
/* 206 */       HashSet localHashSet = new HashSet();
/* 207 */       while ((localClass != null) && (!localClass.equals(Object.class))) {
/* 208 */         NodeImpl localNodeImpl = new NodeImpl(localClass);
/* 209 */         paramGraph.add(localNodeImpl);
/* 210 */         localHashSet.add(localNodeImpl);
/* 211 */         localClass = localClass.getSuperclass();
/*     */       }
/*     */ 
/* 215 */       paramGraph.getRoots();
/*     */ 
/* 218 */       paramGraph.removeAll(localHashSet);
/* 219 */       localSet = paramGraph.getRoots();
/*     */     }
/*     */ 
/* 222 */     return localSet;
/*     */   }
/*     */ 
/*     */   private Class[] getInterfaces(Set paramSet)
/*     */   {
/* 227 */     Class[] arrayOfClass = new Class[paramSet.size()];
/* 228 */     Iterator localIterator = paramSet.iterator();
/* 229 */     int i = 0;
/* 230 */     while (localIterator.hasNext()) {
/* 231 */       NodeImpl localNodeImpl = (NodeImpl)localIterator.next();
/* 232 */       arrayOfClass[(i++)] = localNodeImpl.getInterface();
/*     */     }
/*     */ 
/* 235 */     return arrayOfClass;
/*     */   }
/*     */ 
/*     */   private String[] makeTypeIds(NodeImpl paramNodeImpl, Graph paramGraph, Set paramSet)
/*     */   {
/* 240 */     HashSet localHashSet = new HashSet(paramGraph);
/* 241 */     localHashSet.removeAll(paramSet);
/*     */ 
/* 244 */     ArrayList localArrayList = new ArrayList();
/*     */ 
/* 246 */     if (paramSet.size() > 1)
/*     */     {
/* 250 */       localArrayList.add(paramNodeImpl.getTypeId());
/*     */     }
/*     */ 
/* 253 */     addNodes(localArrayList, paramSet);
/* 254 */     addNodes(localArrayList, localHashSet);
/*     */ 
/* 256 */     return (String[])localArrayList.toArray(new String[localArrayList.size()]);
/*     */   }
/*     */ 
/*     */   private void addNodes(List paramList, Set paramSet)
/*     */   {
/* 261 */     Iterator localIterator = paramSet.iterator();
/* 262 */     while (localIterator.hasNext()) {
/* 263 */       NodeImpl localNodeImpl = (NodeImpl)localIterator.next();
/* 264 */       String str = localNodeImpl.getTypeId();
/* 265 */       paramList.add(str);
/*     */     }
/*     */   }
/*     */ 
/*     */   private class ClassDataImpl
/*     */     implements PresentationManager.ClassData
/*     */   {
/*     */     private Class cls;
/*     */     private IDLNameTranslator nameTranslator;
/*     */     private String[] typeIds;
/*     */     private PresentationManager.StubFactory sfactory;
/*     */     private InvocationHandlerFactory ihfactory;
/*     */     private Map dictionary;
/*     */ 
/*     */     public ClassDataImpl(Class arg2)
/*     */     {
/*     */       Class localClass;
/* 121 */       this.cls = localClass;
/* 122 */       GraphImpl localGraphImpl = new GraphImpl();
/* 123 */       PresentationManagerImpl.NodeImpl localNodeImpl = new PresentationManagerImpl.NodeImpl(localClass);
/* 124 */       Set localSet = PresentationManagerImpl.this.getRootSet(localClass, localNodeImpl, localGraphImpl);
/*     */ 
/* 130 */       Class[] arrayOfClass = PresentationManagerImpl.this.getInterfaces(localSet);
/* 131 */       this.nameTranslator = IDLNameTranslatorImpl.get(arrayOfClass);
/* 132 */       this.typeIds = PresentationManagerImpl.this.makeTypeIds(localNodeImpl, localGraphImpl, localSet);
/* 133 */       this.ihfactory = new InvocationHandlerFactoryImpl(PresentationManagerImpl.this, this);
/*     */ 
/* 135 */       this.dictionary = new HashMap();
/*     */     }
/*     */ 
/*     */     public Class getMyClass()
/*     */     {
/* 140 */       return this.cls;
/*     */     }
/*     */ 
/*     */     public IDLNameTranslator getIDLNameTranslator()
/*     */     {
/* 145 */       return this.nameTranslator;
/*     */     }
/*     */ 
/*     */     public String[] getTypeIds()
/*     */     {
/* 150 */       return this.typeIds;
/*     */     }
/*     */ 
/*     */     public InvocationHandlerFactory getInvocationHandlerFactory()
/*     */     {
/* 155 */       return this.ihfactory;
/*     */     }
/*     */ 
/*     */     public Map getDictionary()
/*     */     {
/* 160 */       return this.dictionary;
/*     */     }
/*     */   }
/*     */ 
/*     */   private static class NodeImpl
/*     */     implements Node
/*     */   {
/*     */     private Class interf;
/*     */ 
/*     */     public Class getInterface()
/*     */     {
/* 275 */       return this.interf;
/*     */     }
/*     */ 
/*     */     public NodeImpl(Class paramClass)
/*     */     {
/* 280 */       this.interf = paramClass;
/*     */     }
/*     */ 
/*     */     public String getTypeId()
/*     */     {
/* 285 */       return "RMI:" + this.interf.getName() + ":0000000000000000";
/*     */     }
/*     */ 
/*     */     public Set getChildren()
/*     */     {
/* 290 */       HashSet localHashSet = new HashSet();
/* 291 */       Class[] arrayOfClass = this.interf.getInterfaces();
/* 292 */       for (int i = 0; i < arrayOfClass.length; i++) {
/* 293 */         Class localClass = arrayOfClass[i];
/* 294 */         if ((Remote.class.isAssignableFrom(localClass)) && (!Remote.class.equals(localClass)))
/*     */         {
/* 296 */           localHashSet.add(new NodeImpl(localClass));
/*     */         }
/*     */       }
/* 299 */       return localHashSet;
/*     */     }
/*     */ 
/*     */     public String toString()
/*     */     {
/* 304 */       return "NodeImpl[" + this.interf + "]";
/*     */     }
/*     */ 
/*     */     public int hashCode()
/*     */     {
/* 309 */       return this.interf.hashCode();
/*     */     }
/*     */ 
/*     */     public boolean equals(Object paramObject)
/*     */     {
/* 314 */       if (this == paramObject) {
/* 315 */         return true;
/*     */       }
/* 317 */       if (!(paramObject instanceof NodeImpl)) {
/* 318 */         return false;
/*     */       }
/* 320 */       NodeImpl localNodeImpl = (NodeImpl)paramObject;
/*     */ 
/* 322 */       return localNodeImpl.interf.equals(this.interf);
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.corba.se.impl.presentation.rmi.PresentationManagerImpl
 * JD-Core Version:    0.6.2
 */