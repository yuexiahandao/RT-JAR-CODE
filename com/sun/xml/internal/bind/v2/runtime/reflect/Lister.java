/*     */ package com.sun.xml.internal.bind.v2.runtime.reflect;
/*     */ 
/*     */ import com.sun.istack.internal.SAXException2;
/*     */ import com.sun.xml.internal.bind.api.AccessorException;
/*     */ import com.sun.xml.internal.bind.v2.ClassFactory;
/*     */ import com.sun.xml.internal.bind.v2.TODO;
/*     */ import com.sun.xml.internal.bind.v2.model.core.Adapter;
/*     */ import com.sun.xml.internal.bind.v2.model.core.ID;
/*     */ import com.sun.xml.internal.bind.v2.model.nav.Navigator;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JAXBContextImpl;
/*     */ import com.sun.xml.internal.bind.v2.runtime.JaxBeanInfo;
/*     */ import com.sun.xml.internal.bind.v2.runtime.XMLSerializer;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.LocatorEx.Snapshot;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.Patcher;
/*     */ import com.sun.xml.internal.bind.v2.runtime.unmarshaller.UnmarshallingContext;
/*     */ import java.lang.ref.WeakReference;
/*     */ import java.lang.reflect.Array;
/*     */ import java.lang.reflect.ParameterizedType;
/*     */ import java.lang.reflect.Type;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.HashSet;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedList;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Stack;
/*     */ import java.util.TreeSet;
/*     */ import java.util.WeakHashMap;
/*     */ import java.util.concurrent.Callable;
/*     */ import javax.xml.bind.JAXBException;
/*     */ import org.xml.sax.SAXException;
/*     */ 
/*     */ public abstract class Lister<BeanT, PropT, ItemT, PackT>
/*     */ {
/*     */   private static final Map<Class, WeakReference<Lister>> arrayListerCache;
/*     */   static final Map<Class, Lister> primitiveArrayListers;
/* 456 */   public static final Lister ERROR = new Lister() {
/*     */     public ListIterator iterator(Object o, XMLSerializer context) {
/* 458 */       return Lister.EMPTY_ITERATOR;
/*     */     }
/*     */ 
/*     */     public Object startPacking(Object o, Accessor accessor) {
/* 462 */       return null;
/*     */     }
/*     */ 
/*     */     public void addToPack(Object o, Object o1)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void endPacking(Object o, Object o1, Accessor accessor)
/*     */     {
/*     */     }
/*     */ 
/*     */     public void reset(Object o, Accessor accessor)
/*     */     {
/*     */     }
/* 456 */   };
/*     */ 
/* 475 */   private static final ListIterator EMPTY_ITERATOR = new ListIterator() {
/*     */     public boolean hasNext() {
/* 477 */       return false;
/*     */     }
/*     */ 
/*     */     public Object next() {
/* 481 */       throw new IllegalStateException();
/*     */     }
/* 475 */   };
/*     */ 
/* 485 */   private static final Class[] COLLECTION_IMPL_CLASSES = { ArrayList.class, LinkedList.class, HashSet.class, TreeSet.class, Stack.class };
/*     */ 
/*     */   public abstract ListIterator<ItemT> iterator(PropT paramPropT, XMLSerializer paramXMLSerializer);
/*     */ 
/*     */   public abstract PackT startPacking(BeanT paramBeanT, Accessor<BeanT, PropT> paramAccessor)
/*     */     throws AccessorException;
/*     */ 
/*     */   public abstract void addToPack(PackT paramPackT, ItemT paramItemT)
/*     */     throws AccessorException;
/*     */ 
/*     */   public abstract void endPacking(PackT paramPackT, BeanT paramBeanT, Accessor<BeanT, PropT> paramAccessor)
/*     */     throws AccessorException;
/*     */ 
/*     */   public abstract void reset(BeanT paramBeanT, Accessor<BeanT, PropT> paramAccessor)
/*     */     throws AccessorException;
/*     */ 
/*     */   public static <BeanT, PropT, ItemT, PackT> Lister<BeanT, PropT, ItemT, PackT> create(Type fieldType, ID idness, Adapter<Type, Class> adapter)
/*     */   {
/* 118 */     Class rawType = (Class)Utils.REFLECTION_NAVIGATOR.erasure(fieldType);
/*     */     Lister l;
/* 122 */     if (rawType.isArray()) {
/* 123 */       Class itemType = rawType.getComponentType();
/* 124 */       l = getArrayLister(itemType);
/*     */     }
/*     */     else
/*     */     {
/*     */       Lister l;
/* 126 */       if (Collection.class.isAssignableFrom(rawType)) {
/* 127 */         Type bt = (Type)Utils.REFLECTION_NAVIGATOR.getBaseClass(fieldType, Collection.class);
/*     */         Class itemType;
/*     */         Class itemType;
/* 128 */         if ((bt instanceof ParameterizedType))
/* 129 */           itemType = (Class)Utils.REFLECTION_NAVIGATOR.erasure(((ParameterizedType)bt).getActualTypeArguments()[0]);
/*     */         else
/* 131 */           itemType = Object.class;
/* 132 */         l = new CollectionLister(getImplClass(rawType));
/*     */       } else {
/* 134 */         return null;
/*     */       }
/*     */     }
/*     */     Lister l;
/*     */     Class itemType;
/* 136 */     if (idness == ID.IDREF) {
/* 137 */       l = new IDREFS(l, itemType);
/*     */     }
/* 139 */     if (adapter != null) {
/* 140 */       l = new AdaptedLister(l, (Class)adapter.adapterType);
/*     */     }
/* 142 */     return l;
/*     */   }
/*     */ 
/*     */   private static Class getImplClass(Class<?> fieldType) {
/* 146 */     return ClassFactory.inferImplClass(fieldType, COLLECTION_IMPL_CLASSES);
/*     */   }
/*     */ 
/*     */   private static Lister getArrayLister(Class componentType)
/*     */   {
/* 159 */     Lister l = null;
/* 160 */     if (componentType.isPrimitive()) {
/* 161 */       l = (Lister)primitiveArrayListers.get(componentType);
/*     */     } else {
/* 163 */       WeakReference wr = (WeakReference)arrayListerCache.get(componentType);
/* 164 */       if (wr != null)
/* 165 */         l = (Lister)wr.get();
/* 166 */       if (l == null) {
/* 167 */         l = new ArrayLister(componentType);
/* 168 */         arrayListerCache.put(componentType, new WeakReference(l));
/*     */       }
/*     */     }
/* 171 */     assert (l != null);
/* 172 */     return l;
/*     */   }
/*     */ 
/*     */   public static <A, B, C, D> Lister<A, B, C, D> getErrorInstance()
/*     */   {
/* 453 */     return ERROR;
/*     */   }
/*     */ 
/*     */   static
/*     */   {
/* 152 */     arrayListerCache = Collections.synchronizedMap(new WeakHashMap());
/*     */ 
/* 236 */     primitiveArrayListers = new HashMap();
/*     */ 
/* 240 */     PrimitiveArrayListerBoolean.register();
/* 241 */     PrimitiveArrayListerByte.register();
/* 242 */     PrimitiveArrayListerCharacter.register();
/* 243 */     PrimitiveArrayListerDouble.register();
/* 244 */     PrimitiveArrayListerFloat.register();
/* 245 */     PrimitiveArrayListerInteger.register();
/* 246 */     PrimitiveArrayListerLong.register();
/* 247 */     PrimitiveArrayListerShort.register();
/*     */   }
/*     */ 
/*     */   private static final class ArrayLister<BeanT, ItemT> extends Lister<BeanT, ItemT[], ItemT, Lister.Pack<ItemT>>
/*     */   {
/*     */     private final Class<ItemT> itemType;
/*     */ 
/*     */     public ArrayLister(Class<ItemT> itemType)
/*     */     {
/* 187 */       this.itemType = itemType;
/*     */     }
/*     */ 
/*     */     public ListIterator<ItemT> iterator(final ItemT[] objects, XMLSerializer context) {
/* 191 */       return new ListIterator() {
/* 192 */         int idx = 0;
/*     */ 
/* 194 */         public boolean hasNext() { return this.idx < objects.length; }
/*     */ 
/*     */         public ItemT next()
/*     */         {
/* 198 */           return objects[(this.idx++)];
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public Lister.Pack startPacking(BeanT current, Accessor<BeanT, ItemT[]> acc) {
/* 204 */       return new Lister.Pack(this.itemType);
/*     */     }
/*     */ 
/*     */     public void addToPack(Lister.Pack<ItemT> objects, ItemT o) {
/* 208 */       objects.add(o);
/*     */     }
/*     */ 
/*     */     public void endPacking(Lister.Pack<ItemT> pack, BeanT bean, Accessor<BeanT, ItemT[]> acc) throws AccessorException {
/* 212 */       acc.set(bean, pack.build());
/*     */     }
/*     */ 
/*     */     public void reset(BeanT o, Accessor<BeanT, ItemT[]> acc) throws AccessorException {
/* 216 */       acc.set(o, (Object[])Array.newInstance(this.itemType, 0));
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class CollectionLister<BeanT, T extends Collection> extends Lister<BeanT, T, Object, T>
/*     */   {
/*     */     private final Class<? extends T> implClass;
/*     */ 
/*     */     public CollectionLister(Class<? extends T> implClass)
/*     */     {
/* 262 */       this.implClass = implClass;
/*     */     }
/*     */ 
/*     */     public ListIterator iterator(T collection, XMLSerializer context) {
/* 266 */       final Iterator itr = collection.iterator();
/* 267 */       return new ListIterator() {
/*     */         public boolean hasNext() {
/* 269 */           return itr.hasNext();
/*     */         }
/*     */         public Object next() {
/* 272 */           return itr.next();
/*     */         }
/*     */       };
/*     */     }
/*     */ 
/*     */     public T startPacking(BeanT bean, Accessor<BeanT, T> acc) throws AccessorException {
/* 278 */       Collection collection = (Collection)acc.get(bean);
/* 279 */       if (collection == null) {
/* 280 */         collection = (Collection)ClassFactory.create(this.implClass);
/* 281 */         if (!acc.isAdapted())
/* 282 */           acc.set(bean, collection);
/*     */       }
/* 284 */       collection.clear();
/* 285 */       return collection;
/*     */     }
/*     */ 
/*     */     public void addToPack(T collection, Object o) {
/* 289 */       collection.add(o);
/*     */     }
/*     */ 
/*     */     public void endPacking(T collection, BeanT bean, Accessor<BeanT, T> acc)
/*     */       throws AccessorException
/*     */     {
/*     */       try
/*     */       {
/* 302 */         acc.set(bean, collection);
/*     */       } catch (AccessorException ae) {
/* 304 */         if (acc.isAdapted()) throw ae; 
/*     */       }
/*     */     }
/*     */ 
/*     */     public void reset(BeanT bean, Accessor<BeanT, T> acc) throws AccessorException {
/* 309 */       Collection collection = (Collection)acc.get(bean);
/* 310 */       if (collection == null) {
/* 311 */         return;
/*     */       }
/* 313 */       collection.clear();
/*     */     }
/*     */   }
/*     */ 
/*     */   private static final class IDREFS<BeanT, PropT> extends Lister<BeanT, PropT, String, IDREFS<BeanT, PropT>.Pack>
/*     */   {
/*     */     private final Lister<BeanT, PropT, Object, Object> core;
/*     */     private final Class itemType;
/*     */ 
/*     */     public IDREFS(Lister core, Class itemType)
/*     */     {
/* 328 */       this.core = core;
/* 329 */       this.itemType = itemType;
/*     */     }
/*     */ 
/*     */     public ListIterator<String> iterator(PropT prop, XMLSerializer context) {
/* 333 */       ListIterator i = this.core.iterator(prop, context);
/*     */ 
/* 335 */       return new Lister.IDREFSIterator(i, context, null);
/*     */     }
/*     */ 
/*     */     public IDREFS<BeanT, PropT>.Pack startPacking(BeanT bean, Accessor<BeanT, PropT> acc) {
/* 339 */       return new Pack(bean, acc);
/*     */     }
/*     */ 
/*     */     public void addToPack(IDREFS<BeanT, PropT>.Pack pack, String item) {
/* 343 */       pack.add(item);
/*     */     }
/*     */ 
/*     */     public void endPacking(IDREFS<BeanT, PropT>.Pack pack, BeanT bean, Accessor<BeanT, PropT> acc) {
/*     */     }
/*     */ 
/*     */     public void reset(BeanT bean, Accessor<BeanT, PropT> acc) throws AccessorException {
/* 350 */       this.core.reset(bean, acc);
/*     */     }
/*     */     private class Pack implements Patcher {
/*     */       private final BeanT bean;
/* 358 */       private final List<String> idrefs = new ArrayList();
/*     */       private final UnmarshallingContext context;
/*     */       private final Accessor<BeanT, PropT> acc;
/*     */       private final LocatorEx location;
/*     */ 
/*     */       public Pack(Accessor<BeanT, PropT> bean) {
/* 364 */         this.bean = bean;
/* 365 */         this.acc = acc;
/* 366 */         this.context = UnmarshallingContext.getInstance();
/* 367 */         this.location = new LocatorEx.Snapshot(this.context.getLocator());
/* 368 */         this.context.addPatcher(this);
/*     */       }
/*     */ 
/*     */       public void add(String item) {
/* 372 */         this.idrefs.add(item);
/*     */       }
/*     */ 
/*     */       public void run()
/*     */         throws SAXException
/*     */       {
/*     */         try
/*     */         {
/* 380 */           Object pack = Lister.this.startPacking(this.bean, this.acc);
/*     */ 
/* 382 */           for (String id : this.idrefs) {
/* 383 */             Callable callable = this.context.getObjectFromId(id, Lister.IDREFS.this.itemType);
/*     */             Object t;
/*     */             try {
/* 387 */               t = callable != null ? callable.call() : null;
/*     */             } catch (SAXException e) {
/* 389 */               throw e;
/*     */             } catch (Exception e) {
/* 391 */               throw new SAXException2(e);
/*     */             }
/*     */ 
/* 394 */             if (t == null) {
/* 395 */               this.context.errorUnresolvedIDREF(this.bean, id, this.location);
/*     */             } else {
/* 397 */               TODO.prototype();
/* 398 */               Lister.this.addToPack(pack, t);
/*     */             }
/*     */           }
/*     */ 
/* 402 */           Lister.this.endPacking(pack, this.bean, this.acc);
/*     */         } catch (AccessorException e) {
/* 404 */           this.context.handleError(e);
/*     */         }
/*     */       }
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class IDREFSIterator
/*     */     implements ListIterator<String>
/*     */   {
/*     */     private final ListIterator i;
/*     */     private final XMLSerializer context;
/*     */     private Object last;
/*     */ 
/*     */     private IDREFSIterator(ListIterator i, XMLSerializer context)
/*     */     {
/* 423 */       this.i = i;
/* 424 */       this.context = context;
/*     */     }
/*     */ 
/*     */     public boolean hasNext() {
/* 428 */       return this.i.hasNext();
/*     */     }
/*     */ 
/*     */     public Object last()
/*     */     {
/* 435 */       return this.last;
/*     */     }
/*     */ 
/*     */     public String next() throws SAXException, JAXBException {
/* 439 */       this.last = this.i.next();
/* 440 */       String id = this.context.grammar.getBeanInfo(this.last, true).getId(this.last, this.context);
/* 441 */       if (id == null) {
/* 442 */         this.context.errorMissingId(this.last);
/*     */       }
/* 444 */       return id;
/*     */     }
/*     */   }
/*     */ 
/*     */   public static final class Pack<ItemT> extends ArrayList<ItemT>
/*     */   {
/*     */     private final Class<ItemT> itemType;
/*     */ 
/*     */     public Pack(Class<ItemT> itemType)
/*     */     {
/* 225 */       this.itemType = itemType;
/*     */     }
/*     */ 
/*     */     public ItemT[] build() {
/* 229 */       return super.toArray((Object[])Array.newInstance(this.itemType, size()));
/*     */     }
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     com.sun.xml.internal.bind.v2.runtime.reflect.Lister
 * JD-Core Version:    0.6.2
 */