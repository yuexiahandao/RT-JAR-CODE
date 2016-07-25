/*     */ package javax.management.openmbean;
/*     */ 
/*     */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*     */ import com.sun.jmx.mbeanserver.Util;
/*     */ import java.io.IOException;
/*     */ import java.io.ObjectInputStream;
/*     */ import java.io.Serializable;
/*     */ import java.security.AccessController;
/*     */ import java.util.ArrayList;
/*     */ import java.util.Arrays;
/*     */ import java.util.Collection;
/*     */ import java.util.Collections;
/*     */ import java.util.HashMap;
/*     */ import java.util.Iterator;
/*     */ import java.util.LinkedHashMap;
/*     */ import java.util.List;
/*     */ import java.util.Map;
/*     */ import java.util.Map.Entry;
/*     */ import java.util.Set;
/*     */ 
/*     */ public class TabularDataSupport
/*     */   implements TabularData, Map<Object, Object>, Cloneable, Serializable
/*     */ {
/*     */   static final long serialVersionUID = 5720150593236309827L;
/*     */   private Map<Object, CompositeData> dataMap;
/*     */   private final TabularType tabularType;
/*     */   private transient String[] indexNamesArray;
/*     */ 
/*     */   public TabularDataSupport(TabularType paramTabularType)
/*     */   {
/* 117 */     this(paramTabularType, 16, 0.75F);
/*     */   }
/*     */ 
/*     */   public TabularDataSupport(TabularType paramTabularType, int paramInt, float paramFloat)
/*     */   {
/* 139 */     if (paramTabularType == null) {
/* 140 */       throw new IllegalArgumentException("Argument tabularType cannot be null.");
/*     */     }
/*     */ 
/* 145 */     this.tabularType = paramTabularType;
/* 146 */     List localList = paramTabularType.getIndexNames();
/* 147 */     this.indexNamesArray = ((String[])localList.toArray(new String[localList.size()]));
/*     */ 
/* 152 */     String str = (String)AccessController.doPrivileged(new GetPropertyAction("jmx.tabular.data.hash.map"));
/*     */ 
/* 154 */     boolean bool = "true".equalsIgnoreCase(str);
/*     */ 
/* 158 */     this.dataMap = (bool ? new HashMap(paramInt, paramFloat) : new LinkedHashMap(paramInt, paramFloat));
/*     */   }
/*     */ 
/*     */   public TabularType getTabularType()
/*     */   {
/* 174 */     return this.tabularType;
/*     */   }
/*     */ 
/*     */   public Object[] calculateIndex(CompositeData paramCompositeData)
/*     */   {
/* 199 */     checkValueType(paramCompositeData);
/*     */ 
/* 203 */     return internalCalculateIndex(paramCompositeData).toArray();
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object paramObject)
/*     */   {
/*     */     Object[] arrayOfObject;
/*     */     try
/*     */     {
/* 228 */       arrayOfObject = (Object[])paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 230 */       return false;
/*     */     }
/*     */ 
/* 233 */     return containsKey(arrayOfObject);
/*     */   }
/*     */ 
/*     */   public boolean containsKey(Object[] paramArrayOfObject)
/*     */   {
/* 247 */     return paramArrayOfObject == null ? false : this.dataMap.containsKey(Arrays.asList(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public boolean containsValue(CompositeData paramCompositeData)
/*     */   {
/* 261 */     return this.dataMap.containsValue(paramCompositeData);
/*     */   }
/*     */ 
/*     */   public boolean containsValue(Object paramObject)
/*     */   {
/* 274 */     return this.dataMap.containsValue(paramObject);
/*     */   }
/*     */ 
/*     */   public Object get(Object paramObject)
/*     */   {
/* 287 */     return get((Object[])paramObject);
/*     */   }
/*     */ 
/*     */   public CompositeData get(Object[] paramArrayOfObject)
/*     */   {
/* 311 */     checkKeyType(paramArrayOfObject);
/*     */ 
/* 315 */     return (CompositeData)this.dataMap.get(Arrays.asList(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public Object put(Object paramObject1, Object paramObject2)
/*     */   {
/* 345 */     internalPut((CompositeData)paramObject2);
/* 346 */     return paramObject2;
/*     */   }
/*     */ 
/*     */   public void put(CompositeData paramCompositeData) {
/* 350 */     internalPut(paramCompositeData);
/*     */   }
/*     */ 
/*     */   private CompositeData internalPut(CompositeData paramCompositeData)
/*     */   {
/* 358 */     List localList = checkValueAndIndex(paramCompositeData);
/*     */ 
/* 362 */     return (CompositeData)this.dataMap.put(localList, paramCompositeData);
/*     */   }
/*     */ 
/*     */   public Object remove(Object paramObject)
/*     */   {
/* 380 */     return remove((Object[])paramObject);
/*     */   }
/*     */ 
/*     */   public CompositeData remove(Object[] paramArrayOfObject)
/*     */   {
/* 403 */     checkKeyType(paramArrayOfObject);
/*     */ 
/* 407 */     return (CompositeData)this.dataMap.remove(Arrays.asList(paramArrayOfObject));
/*     */   }
/*     */ 
/*     */   public void putAll(Map<?, ?> paramMap)
/*     */   {
/* 448 */     if ((paramMap == null) || (paramMap.size() == 0))
/*     */     {
/*     */       return;
/*     */     }
/*     */ 
/*     */     CompositeData[] arrayOfCompositeData;
/*     */     try
/*     */     {
/* 456 */       arrayOfCompositeData = (CompositeData[])paramMap.values().toArray(new CompositeData[paramMap.size()]);
/*     */     }
/*     */     catch (ArrayStoreException localArrayStoreException) {
/* 459 */       throw new ClassCastException("Map argument t contains values which are not instances of <tt>CompositeData</tt>");
/*     */     }
/*     */ 
/* 464 */     putAll(arrayOfCompositeData);
/*     */   }
/*     */ 
/*     */   public void putAll(CompositeData[] paramArrayOfCompositeData)
/*     */   {
/* 500 */     if ((paramArrayOfCompositeData == null) || (paramArrayOfCompositeData.length == 0)) {
/* 501 */       return;
/*     */     }
/*     */ 
/* 505 */     ArrayList localArrayList = new ArrayList(paramArrayOfCompositeData.length + 1);
/*     */ 
/* 511 */     for (int i = 0; i < paramArrayOfCompositeData.length; i++)
/*     */     {
/* 513 */       List localList = checkValueAndIndex(paramArrayOfCompositeData[i]);
/*     */ 
/* 515 */       if (localArrayList.contains(localList)) {
/* 516 */         throw new KeyAlreadyExistsException("Argument elements values[" + i + "] and values[" + localArrayList.indexOf(localList) + "] have the same indexes, " + "calculated according to this TabularData instance's tabularType.");
/*     */       }
/*     */ 
/* 521 */       localArrayList.add(localList);
/*     */     }
/*     */ 
/* 526 */     for (i = 0; i < paramArrayOfCompositeData.length; i++)
/* 527 */       this.dataMap.put(localArrayList.get(i), paramArrayOfCompositeData[i]);
/*     */   }
/*     */ 
/*     */   public void clear()
/*     */   {
/* 536 */     this.dataMap.clear();
/*     */   }
/*     */ 
/*     */   public int size()
/*     */   {
/* 550 */     return this.dataMap.size();
/*     */   }
/*     */ 
/*     */   public boolean isEmpty()
/*     */   {
/* 560 */     return size() == 0;
/*     */   }
/*     */ 
/*     */   public Set<Object> keySet()
/*     */   {
/* 589 */     return this.dataMap.keySet();
/*     */   }
/*     */ 
/*     */   public Collection<Object> values()
/*     */   {
/* 615 */     return (Collection)Util.cast(this.dataMap.values());
/*     */   }
/*     */ 
/*     */   public Set<Map.Entry<Object, Object>> entrySet()
/*     */   {
/* 651 */     return (Set)Util.cast(this.dataMap.entrySet());
/*     */   }
/*     */ 
/*     */   public Object clone()
/*     */   {
/*     */     try
/*     */     {
/* 669 */       TabularDataSupport localTabularDataSupport = (TabularDataSupport)super.clone();
/* 670 */       localTabularDataSupport.dataMap = new HashMap(localTabularDataSupport.dataMap);
/* 671 */       return localTabularDataSupport;
/*     */     }
/*     */     catch (CloneNotSupportedException localCloneNotSupportedException) {
/* 674 */       throw new InternalError(localCloneNotSupportedException.toString());
/*     */     }
/*     */   }
/*     */ 
/*     */   public boolean equals(Object paramObject)
/*     */   {
/* 700 */     if (paramObject == null) {
/* 701 */       return false;
/*     */     }
/*     */ 
/*     */     TabularData localTabularData;
/*     */     try
/*     */     {
/* 708 */       localTabularData = (TabularData)paramObject;
/*     */     } catch (ClassCastException localClassCastException) {
/* 710 */       return false;
/*     */     }
/*     */ 
/* 717 */     if (!getTabularType().equals(localTabularData.getTabularType())) {
/* 718 */       return false;
/*     */     }
/*     */ 
/* 726 */     if (size() != localTabularData.size()) {
/* 727 */       return false;
/*     */     }
/* 729 */     for (CompositeData localCompositeData : this.dataMap.values()) {
/* 730 */       if (!localTabularData.containsValue(localCompositeData)) {
/* 731 */         return false;
/*     */       }
/*     */ 
/*     */     }
/*     */ 
/* 737 */     return true;
/*     */   }
/*     */ 
/*     */   public int hashCode()
/*     */   {
/* 760 */     int i = 0;
/*     */ 
/* 762 */     i += this.tabularType.hashCode();
/* 763 */     for (Iterator localIterator = values().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/* 764 */       i += localObject.hashCode();
/*     */     }
/* 766 */     return i;
/*     */   }
/*     */ 
/*     */   public String toString()
/*     */   {
/* 782 */     return getClass().getName() + "(tabularType=" + this.tabularType.toString() + ",contents=" + this.dataMap.toString() + ")";
/*     */   }
/*     */ 
/*     */   private List<?> internalCalculateIndex(CompositeData paramCompositeData)
/*     */   {
/* 810 */     return Collections.unmodifiableList(Arrays.asList(paramCompositeData.getAll(this.indexNamesArray)));
/*     */   }
/*     */ 
/*     */   private void checkKeyType(Object[] paramArrayOfObject)
/*     */   {
/* 823 */     if ((paramArrayOfObject == null) || (paramArrayOfObject.length == 0)) {
/* 824 */       throw new NullPointerException("Argument key cannot be null or empty.");
/*     */     }
/*     */ 
/* 831 */     if (paramArrayOfObject.length != this.indexNamesArray.length) {
/* 832 */       throw new InvalidKeyException("Argument key's length=" + paramArrayOfObject.length + " is different from the number of item values, which is " + this.indexNamesArray.length + ", specified for the indexing rows in this TabularData instance.");
/*     */     }
/*     */ 
/* 840 */     for (int i = 0; i < paramArrayOfObject.length; i++) {
/* 841 */       OpenType localOpenType = this.tabularType.getRowType().getType(this.indexNamesArray[i]);
/* 842 */       if ((paramArrayOfObject[i] != null) && (!localOpenType.isValue(paramArrayOfObject[i])))
/* 843 */         throw new InvalidKeyException("Argument element key[" + i + "] is not a value for the open type expected for " + "this element of the index, whose name is \"" + this.indexNamesArray[i] + "\" and whose open type is " + localOpenType);
/*     */     }
/*     */   }
/*     */ 
/*     */   private void checkValueType(CompositeData paramCompositeData)
/*     */   {
/* 861 */     if (paramCompositeData == null) {
/* 862 */       throw new NullPointerException("Argument value cannot be null.");
/*     */     }
/*     */ 
/* 867 */     if (!this.tabularType.getRowType().isValue(paramCompositeData))
/* 868 */       throw new InvalidOpenTypeException("Argument value's composite type [" + paramCompositeData.getCompositeType() + "] is not assignable to " + "this TabularData instance's row type [" + this.tabularType.getRowType() + "].");
/*     */   }
/*     */ 
/*     */   private List<?> checkValueAndIndex(CompositeData paramCompositeData)
/*     */   {
/* 890 */     checkValueType(paramCompositeData);
/*     */ 
/* 895 */     List localList = internalCalculateIndex(paramCompositeData);
/*     */ 
/* 897 */     if (this.dataMap.containsKey(localList)) {
/* 898 */       throw new KeyAlreadyExistsException("Argument value's index, calculated according to this TabularData instance's tabularType, already refers to a value in this table.");
/*     */     }
/*     */ 
/* 904 */     return localList;
/*     */   }
/*     */ 
/*     */   private void readObject(ObjectInputStream paramObjectInputStream)
/*     */     throws IOException, ClassNotFoundException
/*     */   {
/* 912 */     paramObjectInputStream.defaultReadObject();
/* 913 */     List localList = this.tabularType.getIndexNames();
/* 914 */     this.indexNamesArray = ((String[])localList.toArray(new String[localList.size()]));
/*     */   }
/*     */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.openmbean.TabularDataSupport
 * JD-Core Version:    0.6.2
 */