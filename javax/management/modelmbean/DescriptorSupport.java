/*      */ package javax.management.modelmbean;
/*      */ 
/*      */ import com.sun.jmx.defaults.JmxProperties;
/*      */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import java.io.IOException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.security.AccessController;
/*      */ import java.util.Collection;
/*      */ import java.util.HashMap;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ import java.util.SortedMap;
/*      */ import java.util.StringTokenizer;
/*      */ import java.util.TreeMap;
/*      */ import java.util.logging.Level;
/*      */ import java.util.logging.Logger;
/*      */ import javax.management.Descriptor;
/*      */ import javax.management.ImmutableDescriptor;
/*      */ import javax.management.MBeanException;
/*      */ import javax.management.RuntimeOperationsException;
/*      */ import sun.reflect.misc.ReflectUtil;
/*      */ 
/*      */ public class DescriptorSupport
/*      */   implements Descriptor
/*      */ {
/*      */   private static final long oldSerialVersionUID = 8071560848919417985L;
/*      */   private static final long newSerialVersionUID = -6292969195866300415L;
/*  101 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("descriptor", HashMap.class), new ObjectStreamField("currClass", String.class) };
/*      */ 
/*  108 */   private static final ObjectStreamField[] newSerialPersistentFields = { new ObjectStreamField("descriptor", HashMap.class) };
/*      */   private static final long serialVersionUID;
/*      */   private static final ObjectStreamField[] serialPersistentFields;
/*      */   private static final String serialForm;
/*      */   private transient SortedMap<String, Object> descriptorMap;
/*      */   private static final String currClass = "DescriptorSupport";
/*      */   private static final String[] entities;
/*      */   private static final Map<String, Character> entityToCharMap;
/*      */   private static final String[] charToEntityMap;
/*      */ 
/*      */   public DescriptorSupport()
/*      */   {
/*  166 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  167 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "DescriptorSupport()", "Constructor");
/*      */     }
/*      */ 
/*  171 */     init(null);
/*      */   }
/*      */ 
/*      */   public DescriptorSupport(int paramInt)
/*      */     throws MBeanException, RuntimeOperationsException
/*      */   {
/*  190 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  191 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(initNumFields = " + paramInt + ")", "Constructor");
/*      */     }
/*      */ 
/*  196 */     if (paramInt <= 0) {
/*  197 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  198 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(initNumFields)", "Illegal arguments: initNumFields <= 0");
/*      */       }
/*      */ 
/*  203 */       String str = "Descriptor field limit invalid: " + paramInt;
/*      */ 
/*  205 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(str);
/*  206 */       throw new RuntimeOperationsException(localIllegalArgumentException, str);
/*      */     }
/*  208 */     init(null);
/*      */   }
/*      */ 
/*      */   public DescriptorSupport(DescriptorSupport paramDescriptorSupport)
/*      */   {
/*  221 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  222 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(Descriptor)", "Constructor");
/*      */     }
/*      */ 
/*  226 */     if (paramDescriptorSupport == null)
/*  227 */       init(null);
/*      */     else
/*  229 */       init(paramDescriptorSupport.descriptorMap);
/*      */   }
/*      */ 
/*      */   public DescriptorSupport(String paramString)
/*      */     throws MBeanException, RuntimeOperationsException, XMLParseException
/*      */   {
/*  270 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  271 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String = '" + paramString + "')", "Constructor");
/*      */     }
/*      */ 
/*  275 */     if (paramString == null) {
/*  276 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  277 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String = null)", "Illegal arguments");
/*      */       }
/*      */ 
/*  282 */       localObject1 = new IllegalArgumentException("String in parameter is null");
/*  283 */       throw new RuntimeOperationsException((RuntimeException)localObject1, "String in parameter is null");
/*      */     }
/*      */ 
/*  286 */     String str1 = paramString.toLowerCase();
/*  287 */     if ((!str1.startsWith("<descriptor>")) || (!str1.endsWith("</descriptor>")))
/*      */     {
/*  289 */       throw new XMLParseException("No <descriptor>, </descriptor> pair");
/*      */     }
/*      */ 
/*  293 */     init(null);
/*      */ 
/*  298 */     Object localObject1 = new StringTokenizer(paramString, "<> \t\n\r\f");
/*      */ 
/*  300 */     int i = 0;
/*  301 */     int j = 0;
/*  302 */     Object localObject2 = null;
/*  303 */     Object localObject3 = null;
/*      */ 
/*  306 */     while (((StringTokenizer)localObject1).hasMoreTokens()) {
/*  307 */       String str2 = ((StringTokenizer)localObject1).nextToken();
/*      */ 
/*  309 */       if (str2.equalsIgnoreCase("FIELD")) {
/*  310 */         i = 1;
/*  311 */       } else if (str2.equalsIgnoreCase("/FIELD")) {
/*  312 */         if ((localObject2 != null) && (localObject3 != null)) {
/*  313 */           localObject2 = ((String)localObject2).substring(((String)localObject2).indexOf('"') + 1, ((String)localObject2).lastIndexOf('"'));
/*      */ 
/*  316 */           Object localObject4 = parseQuotedFieldValue((String)localObject3);
/*      */ 
/*  318 */           setField((String)localObject2, localObject4);
/*      */         }
/*  320 */         localObject2 = null;
/*  321 */         localObject3 = null;
/*  322 */         i = 0;
/*  323 */       } else if (str2.equalsIgnoreCase("DESCRIPTOR")) {
/*  324 */         j = 1;
/*  325 */       } else if (str2.equalsIgnoreCase("/DESCRIPTOR")) {
/*  326 */         j = 0;
/*  327 */         localObject2 = null;
/*  328 */         localObject3 = null;
/*  329 */         i = 0;
/*  330 */       } else if ((i != 0) && (j != 0))
/*      */       {
/*  332 */         int k = str2.indexOf("=");
/*      */         String str3;
/*  333 */         if (k > 0) {
/*  334 */           str3 = str2.substring(0, k);
/*  335 */           String str4 = str2.substring(k + 1);
/*  336 */           if (str3.equalsIgnoreCase("NAME")) {
/*  337 */             localObject2 = str4;
/*  338 */           } else if (str3.equalsIgnoreCase("VALUE")) {
/*  339 */             localObject3 = str4;
/*      */           } else {
/*  341 */             String str5 = "Expected `name' or `value', got `" + str2 + "'";
/*      */ 
/*  343 */             throw new XMLParseException(str5);
/*      */           }
/*      */         } else {
/*  346 */           str3 = "Expected `keyword=value', got `" + str2 + "'";
/*      */ 
/*  348 */           throw new XMLParseException(str3);
/*      */         }
/*      */       }
/*      */     }
/*  352 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST))
/*  353 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(XMLString)", "Exit");
/*      */   }
/*      */ 
/*      */   public DescriptorSupport(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*      */     throws RuntimeOperationsException
/*      */   {
/*  382 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  383 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", "Constructor");
/*      */     }
/*      */ 
/*  388 */     if ((paramArrayOfString == null) || (paramArrayOfObject == null) || (paramArrayOfString.length != paramArrayOfObject.length))
/*      */     {
/*  390 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  391 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", "Illegal arguments");
/*      */       }
/*      */ 
/*  399 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Null or invalid fieldNames or fieldValues");
/*  400 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Null or invalid fieldNames or fieldValues");
/*      */     }
/*      */ 
/*  404 */     init(null);
/*  405 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*      */     {
/*  408 */       setField(paramArrayOfString[i], paramArrayOfObject[i]);
/*      */     }
/*  410 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST))
/*  411 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(fieldNames,fieldObjects)", "Exit");
/*      */   }
/*      */ 
/*      */   public DescriptorSupport(String[] paramArrayOfString)
/*      */   {
/*  446 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  447 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Constructor");
/*      */     }
/*      */ 
/*  451 */     init(null);
/*  452 */     if ((paramArrayOfString == null) || (paramArrayOfString.length == 0)) {
/*  453 */       return;
/*      */     }
/*  455 */     init(null);
/*      */ 
/*  457 */     for (int i = 0; i < paramArrayOfString.length; i++)
/*  458 */       if ((paramArrayOfString[i] != null) && (!paramArrayOfString[i].equals("")))
/*      */       {
/*  461 */         int j = paramArrayOfString[i].indexOf("=");
/*  462 */         if (j < 0)
/*      */         {
/*  464 */           if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  465 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Illegal arguments: field does not have '=' as a name and value separator");
/*      */           }
/*      */ 
/*  472 */           localObject = new IllegalArgumentException("Field in invalid format: no equals sign");
/*  473 */           throw new RuntimeOperationsException((RuntimeException)localObject, "Field in invalid format: no equals sign");
/*      */         }
/*      */ 
/*  476 */         String str = paramArrayOfString[i].substring(0, j);
/*  477 */         Object localObject = null;
/*  478 */         if (j < paramArrayOfString[i].length())
/*      */         {
/*  480 */           localObject = paramArrayOfString[i].substring(j + 1);
/*      */         }
/*      */ 
/*  483 */         if (str.equals("")) {
/*  484 */           if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  485 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Illegal arguments: fieldName is empty");
/*      */           }
/*      */ 
/*  492 */           IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Field in invalid format: no fieldName");
/*  493 */           throw new RuntimeOperationsException(localIllegalArgumentException, "Field in invalid format: no fieldName");
/*      */         }
/*      */ 
/*  496 */         setField(str, localObject);
/*      */       }
/*  498 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST))
/*  499 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "Descriptor(String... fields)", "Exit");
/*      */   }
/*      */ 
/*      */   private void init(Map<String, ?> paramMap)
/*      */   {
/*  506 */     this.descriptorMap = new TreeMap(String.CASE_INSENSITIVE_ORDER);
/*      */ 
/*  508 */     if (paramMap != null)
/*  509 */       this.descriptorMap.putAll(paramMap);
/*      */   }
/*      */ 
/*      */   public synchronized Object getFieldValue(String paramString)
/*      */     throws RuntimeOperationsException
/*      */   {
/*  518 */     if ((paramString == null) || (paramString.equals(""))) {
/*  519 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  520 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValue(String fieldName)", "Illegal arguments: null field name");
/*      */       }
/*      */ 
/*  526 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException("Fieldname requested is null");
/*  527 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Fieldname requested is null");
/*      */     }
/*  529 */     Object localObject = this.descriptorMap.get(paramString);
/*  530 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  531 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValue(String fieldName = " + paramString + ")", "Returns '" + localObject + "'");
/*      */     }
/*      */ 
/*  536 */     return localObject;
/*      */   }
/*      */ 
/*      */   public synchronized void setField(String paramString, Object paramObject)
/*      */     throws RuntimeOperationsException
/*      */   {
/*      */     IllegalArgumentException localIllegalArgumentException;
/*  543 */     if ((paramString == null) || (paramString.equals(""))) {
/*  544 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  545 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Illegal arguments: null or empty field name");
/*      */       }
/*      */ 
/*  552 */       localIllegalArgumentException = new IllegalArgumentException("Field name to be set is null or empty");
/*  553 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Field name to be set is null or empty");
/*      */     }
/*      */ 
/*  556 */     if (!validateField(paramString, paramObject)) {
/*  557 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  558 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Illegal arguments");
/*      */       }
/*      */ 
/*  564 */       String str = "Field value invalid: " + paramString + "=" + paramObject;
/*      */ 
/*  566 */       localIllegalArgumentException = new IllegalArgumentException(str);
/*  567 */       throw new RuntimeOperationsException(localIllegalArgumentException, str);
/*      */     }
/*      */ 
/*  570 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  571 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setField(fieldName,fieldValue)", "Entry: setting '" + paramString + "' to '" + paramObject + "'");
/*      */     }
/*      */ 
/*  580 */     this.descriptorMap.put(paramString, paramObject);
/*      */   }
/*      */ 
/*      */   public synchronized String[] getFields() {
/*  584 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  585 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Entry");
/*      */     }
/*      */ 
/*  589 */     int i = this.descriptorMap.size();
/*      */ 
/*  591 */     String[] arrayOfString = new String[i];
/*  592 */     Set localSet = this.descriptorMap.entrySet();
/*      */ 
/*  594 */     int j = 0;
/*      */ 
/*  596 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  597 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Returning " + i + " fields");
/*      */     }
/*      */ 
/*  601 */     Iterator localIterator = localSet.iterator();
/*  602 */     for (; localIterator.hasNext(); j++) {
/*  603 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*      */ 
/*  605 */       if (localEntry == null) {
/*  606 */         if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  607 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Element is null");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  612 */         Object localObject = localEntry.getValue();
/*  613 */         if (localObject == null) {
/*  614 */           arrayOfString[j] = ((String)localEntry.getKey() + "=");
/*      */         }
/*  616 */         else if ((localObject instanceof String)) {
/*  617 */           arrayOfString[j] = ((String)localEntry.getKey() + "=" + localObject.toString());
/*      */         }
/*      */         else {
/*  620 */           arrayOfString[j] = ((String)localEntry.getKey() + "=(" + localObject.toString() + ")");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  628 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  629 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFields()", "Exit");
/*      */     }
/*      */ 
/*  634 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public synchronized String[] getFieldNames() {
/*  638 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  639 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Entry");
/*      */     }
/*      */ 
/*  643 */     int i = this.descriptorMap.size();
/*      */ 
/*  645 */     String[] arrayOfString = new String[i];
/*  646 */     Set localSet = this.descriptorMap.entrySet();
/*      */ 
/*  648 */     int j = 0;
/*      */ 
/*  650 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  651 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Returning " + i + " fields");
/*      */     }
/*      */ 
/*  657 */     Iterator localIterator = localSet.iterator();
/*  658 */     for (; localIterator.hasNext(); j++) {
/*  659 */       Map.Entry localEntry = (Map.Entry)localIterator.next();
/*      */ 
/*  661 */       if ((localEntry == null) || (localEntry.getKey() == null)) {
/*  662 */         if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  663 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Field is null");
/*      */         }
/*      */       }
/*      */       else
/*      */       {
/*  668 */         arrayOfString[j] = ((String)localEntry.getKey()).toString();
/*      */       }
/*      */     }
/*      */ 
/*  672 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  673 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldNames()", "Exit");
/*      */     }
/*      */ 
/*  678 */     return arrayOfString;
/*      */   }
/*      */ 
/*      */   public synchronized Object[] getFieldValues(String[] paramArrayOfString)
/*      */   {
/*  683 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  684 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", "Entry");
/*      */     }
/*      */ 
/*  691 */     int i = paramArrayOfString == null ? this.descriptorMap.size() : paramArrayOfString.length;
/*      */ 
/*  693 */     Object[] arrayOfObject = new Object[i];
/*      */ 
/*  695 */     int j = 0;
/*      */ 
/*  697 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST))
/*  698 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", "Returning " + i + " fields");
/*      */     Iterator localIterator;
/*  704 */     if (paramArrayOfString == null)
/*  705 */       for (localIterator = this.descriptorMap.values().iterator(); localIterator.hasNext(); ) { Object localObject = localIterator.next();
/*  706 */         arrayOfObject[(j++)] = localObject; }
/*      */     else {
/*  708 */       for (j = 0; j < paramArrayOfString.length; j++) {
/*  709 */         if ((paramArrayOfString[j] == null) || (paramArrayOfString[j].equals("")))
/*  710 */           arrayOfObject[j] = null;
/*      */         else {
/*  712 */           arrayOfObject[j] = getFieldValue(paramArrayOfString[j]);
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  717 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  718 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "getFieldValues(String... fieldNames)", "Exit");
/*      */     }
/*      */ 
/*  723 */     return arrayOfObject;
/*      */   }
/*      */ 
/*      */   public synchronized void setFields(String[] paramArrayOfString, Object[] paramArrayOfObject)
/*      */     throws RuntimeOperationsException
/*      */   {
/*  730 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  731 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Entry");
/*      */     }
/*      */ 
/*  736 */     if ((paramArrayOfString == null) || (paramArrayOfObject == null) || (paramArrayOfString.length != paramArrayOfObject.length))
/*      */     {
/*  738 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  739 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Illegal arguments");
/*      */       }
/*      */ 
/*  746 */       IllegalArgumentException localIllegalArgumentException1 = new IllegalArgumentException("fieldNames and fieldValues are null or invalid");
/*  747 */       throw new RuntimeOperationsException(localIllegalArgumentException1, "fieldNames and fieldValues are null or invalid");
/*      */     }
/*      */ 
/*  750 */     for (int i = 0; i < paramArrayOfString.length; i++) {
/*  751 */       if ((paramArrayOfString[i] == null) || (paramArrayOfString[i].equals(""))) {
/*  752 */         if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  753 */           JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Null field name encountered at element " + i);
/*      */         }
/*      */ 
/*  759 */         IllegalArgumentException localIllegalArgumentException2 = new IllegalArgumentException("fieldNames is null or invalid");
/*  760 */         throw new RuntimeOperationsException(localIllegalArgumentException2, "fieldNames is null or invalid");
/*      */       }
/*  762 */       setField(paramArrayOfString[i], paramArrayOfObject[i]);
/*      */     }
/*  764 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST))
/*  765 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "setFields(fieldNames,fieldValues)", "Exit");
/*      */   }
/*      */ 
/*      */   public synchronized Object clone()
/*      */     throws RuntimeOperationsException
/*      */   {
/*  781 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  782 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "clone()", "Entry");
/*      */     }
/*      */ 
/*  786 */     return new DescriptorSupport(this);
/*      */   }
/*      */ 
/*      */   public synchronized void removeField(String paramString) {
/*  790 */     if ((paramString == null) || (paramString.equals(""))) {
/*  791 */       return;
/*      */     }
/*      */ 
/*  794 */     this.descriptorMap.remove(paramString);
/*      */   }
/*      */ 
/*      */   public synchronized boolean equals(Object paramObject)
/*      */   {
/*  824 */     if (paramObject == this)
/*  825 */       return true;
/*  826 */     if (!(paramObject instanceof Descriptor))
/*  827 */       return false;
/*  828 */     if ((paramObject instanceof ImmutableDescriptor))
/*  829 */       return paramObject.equals(this);
/*  830 */     return new ImmutableDescriptor(this.descriptorMap).equals(paramObject);
/*      */   }
/*      */ 
/*      */   public synchronized int hashCode()
/*      */   {
/*  857 */     int i = this.descriptorMap.size();
/*      */ 
/*  860 */     return Util.hashCode((String[])this.descriptorMap.keySet().toArray(new String[i]), this.descriptorMap.values().toArray(new Object[i]));
/*      */   }
/*      */ 
/*      */   public synchronized boolean isValid()
/*      */     throws RuntimeOperationsException
/*      */   {
/*  901 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  902 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Entry");
/*      */     }
/*      */ 
/*  908 */     Set localSet = this.descriptorMap.entrySet();
/*      */ 
/*  910 */     if (localSet == null) {
/*  911 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  912 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Returns false (null set)");
/*      */       }
/*      */ 
/*  916 */       return false;
/*      */     }
/*      */ 
/*  919 */     String str1 = (String)getFieldValue("name");
/*  920 */     String str2 = (String)getFieldValue("descriptorType");
/*      */ 
/*  922 */     if ((str1 == null) || (str2 == null) || (str1.equals("")) || (str2.equals("")))
/*      */     {
/*  924 */       return false;
/*      */     }
/*      */ 
/*  929 */     for (Map.Entry localEntry : localSet) {
/*  930 */       if ((localEntry != null) && 
/*  931 */         (localEntry.getValue() != null))
/*      */       {
/*  933 */         if (!validateField(((String)localEntry.getKey()).toString(), localEntry.getValue().toString()))
/*      */         {
/*  937 */           if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  938 */             JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Field " + (String)localEntry.getKey() + "=" + localEntry.getValue() + " is not valid");
/*      */           }
/*      */ 
/*  944 */           return false;
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  951 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/*  952 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "isValid()", "Returns true");
/*      */     }
/*      */ 
/*  956 */     return true;
/*      */   }
/*      */ 
/*      */   private boolean validateField(String paramString, Object paramObject)
/*      */   {
/*  976 */     if ((paramString == null) || (paramString.equals("")))
/*  977 */       return false;
/*  978 */     String str = "";
/*  979 */     int i = 0;
/*  980 */     if ((paramObject != null) && ((paramObject instanceof String))) {
/*  981 */       str = (String)paramObject;
/*  982 */       i = 1;
/*      */     }
/*      */ 
/*  985 */     int j = (paramString.equalsIgnoreCase("Name")) || (paramString.equalsIgnoreCase("DescriptorType")) ? 1 : 0;
/*      */ 
/*  988 */     if ((j != 0) || (paramString.equalsIgnoreCase("SetMethod")) || (paramString.equalsIgnoreCase("GetMethod")) || (paramString.equalsIgnoreCase("Role")) || (paramString.equalsIgnoreCase("Class")))
/*      */     {
/*  993 */       if ((paramObject == null) || (i == 0))
/*  994 */         return false;
/*  995 */       if ((j != 0) && (str.equals("")))
/*  996 */         return false;
/*  997 */       return true;
/*      */     }
/*      */     long l;
/*  998 */     if (paramString.equalsIgnoreCase("visibility"))
/*      */     {
/* 1000 */       if ((paramObject != null) && (i != 0))
/* 1001 */         l = toNumeric(str);
/* 1002 */       else if ((paramObject instanceof Integer))
/* 1003 */         l = ((Integer)paramObject).intValue();
/* 1004 */       else return false;
/*      */ 
/* 1006 */       if ((l >= 1L) && (l <= 4L)) {
/* 1007 */         return true;
/*      */       }
/* 1009 */       return false;
/* 1010 */     }if (paramString.equalsIgnoreCase("severity"))
/*      */     {
/* 1013 */       if ((paramObject != null) && (i != 0))
/* 1014 */         l = toNumeric(str);
/* 1015 */       else if ((paramObject instanceof Integer))
/* 1016 */         l = ((Integer)paramObject).intValue();
/* 1017 */       else return false;
/*      */ 
/* 1019 */       return (l >= 0L) && (l <= 6L);
/* 1020 */     }if (paramString.equalsIgnoreCase("PersistPolicy")) {
/* 1021 */       return (paramObject != null) && (i != 0) && ((str.equalsIgnoreCase("OnUpdate")) || (str.equalsIgnoreCase("OnTimer")) || (str.equalsIgnoreCase("NoMoreOftenThan")) || (str.equalsIgnoreCase("Always")) || (str.equalsIgnoreCase("Never")) || (str.equalsIgnoreCase("OnUnregister")));
/*      */     }
/*      */ 
/* 1028 */     if ((paramString.equalsIgnoreCase("PersistPeriod")) || (paramString.equalsIgnoreCase("CurrencyTimeLimit")) || (paramString.equalsIgnoreCase("LastUpdatedTimeStamp")) || (paramString.equalsIgnoreCase("LastReturnedTimeStamp")))
/*      */     {
/* 1034 */       if ((paramObject != null) && (i != 0))
/* 1035 */         l = toNumeric(str);
/* 1036 */       else if ((paramObject instanceof Number))
/* 1037 */         l = ((Number)paramObject).longValue();
/* 1038 */       else return false;
/*      */ 
/* 1040 */       return l >= -1L;
/* 1041 */     }if (paramString.equalsIgnoreCase("log")) {
/* 1042 */       return ((paramObject instanceof Boolean)) || ((i != 0) && ((str.equalsIgnoreCase("T")) || (str.equalsIgnoreCase("true")) || (str.equalsIgnoreCase("F")) || (str.equalsIgnoreCase("false"))));
/*      */     }
/*      */ 
/* 1051 */     return true;
/*      */   }
/*      */ 
/*      */   public synchronized String toXMLString()
/*      */   {
/* 1084 */     StringBuilder localStringBuilder = new StringBuilder("<Descriptor>");
/* 1085 */     Set localSet = this.descriptorMap.entrySet();
/* 1086 */     for (Map.Entry localEntry : localSet) {
/* 1087 */       String str1 = (String)localEntry.getKey();
/* 1088 */       Object localObject = localEntry.getValue();
/* 1089 */       String str2 = null;
/*      */ 
/* 1095 */       if ((localObject instanceof String)) {
/* 1096 */         String str3 = (String)localObject;
/* 1097 */         if ((!str3.startsWith("(")) || (!str3.endsWith(")")))
/* 1098 */           str2 = quote(str3);
/*      */       }
/* 1100 */       if (str2 == null)
/* 1101 */         str2 = makeFieldValue(localObject);
/* 1102 */       localStringBuilder.append("<field name=\"").append(str1).append("\" value=\"").append(str2).append("\"></field>");
/*      */     }
/*      */ 
/* 1105 */     localStringBuilder.append("</Descriptor>");
/* 1106 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static boolean isMagic(char paramChar)
/*      */   {
/* 1141 */     return (paramChar < charToEntityMap.length) && (charToEntityMap[paramChar] != null);
/*      */   }
/*      */ 
/*      */   private static String quote(String paramString)
/*      */   {
/* 1152 */     int i = 0;
/* 1153 */     for (int j = 0; j < paramString.length(); j++) {
/* 1154 */       if (isMagic(paramString.charAt(j))) {
/* 1155 */         i = 1;
/* 1156 */         break;
/*      */       }
/*      */     }
/* 1159 */     if (i == 0)
/* 1160 */       return paramString;
/* 1161 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1162 */     for (int k = 0; k < paramString.length(); k++) {
/* 1163 */       char c = paramString.charAt(k);
/* 1164 */       if (isMagic(c))
/* 1165 */         localStringBuilder.append(charToEntityMap[c]);
/*      */       else
/* 1167 */         localStringBuilder.append(c);
/*      */     }
/* 1169 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String unquote(String paramString) throws XMLParseException {
/* 1173 */     if ((!paramString.startsWith("\"")) || (!paramString.endsWith("\"")))
/* 1174 */       throw new XMLParseException("Value must be quoted: <" + paramString + ">");
/* 1175 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1176 */     int i = paramString.length() - 1;
/* 1177 */     for (int j = 1; j < i; j++) {
/* 1178 */       char c = paramString.charAt(j);
/*      */       int k;
/*      */       Character localCharacter;
/* 1181 */       if ((c == '&') && ((k = paramString.indexOf(';', j + 1)) >= 0) && ((localCharacter = (Character)entityToCharMap.get(paramString.substring(j, k + 1))) != null))
/*      */       {
/* 1185 */         localStringBuilder.append(localCharacter);
/* 1186 */         j = k;
/*      */       } else {
/* 1188 */         localStringBuilder.append(c);
/*      */       }
/*      */     }
/* 1190 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   private static String makeFieldValue(Object paramObject)
/*      */   {
/* 1199 */     if (paramObject == null) {
/* 1200 */       return "(null)";
/*      */     }
/* 1202 */     Class localClass = paramObject.getClass();
/*      */     try {
/* 1204 */       localClass.getConstructor(new Class[] { String.class });
/*      */     } catch (NoSuchMethodException localNoSuchMethodException) {
/* 1206 */       String str2 = "Class " + localClass + " does not have a public " + "constructor with a single string arg";
/*      */ 
/* 1209 */       IllegalArgumentException localIllegalArgumentException = new IllegalArgumentException(str2);
/* 1210 */       throw new RuntimeOperationsException(localIllegalArgumentException, "Cannot make XML descriptor");
/*      */     }
/*      */     catch (SecurityException localSecurityException)
/*      */     {
/*      */     }
/*      */ 
/* 1218 */     String str1 = quote(paramObject.toString());
/*      */ 
/* 1220 */     return "(" + localClass.getName() + "/" + str1 + ")";
/*      */   }
/*      */ 
/*      */   private static Object parseQuotedFieldValue(String paramString)
/*      */     throws XMLParseException
/*      */   {
/* 1237 */     paramString = unquote(paramString);
/* 1238 */     if (paramString.equalsIgnoreCase("(null)"))
/* 1239 */       return null;
/* 1240 */     if ((!paramString.startsWith("(")) || (!paramString.endsWith(")")))
/* 1241 */       return paramString;
/* 1242 */     int i = paramString.indexOf('/');
/* 1243 */     if (i < 0)
/*      */     {
/* 1245 */       return paramString.substring(1, paramString.length() - 1);
/*      */     }
/* 1247 */     String str1 = paramString.substring(1, i);
/*      */     Constructor localConstructor;
/*      */     try {
/* 1251 */       ReflectUtil.checkPackageAccess(str1);
/* 1252 */       ClassLoader localClassLoader = Thread.currentThread().getContextClassLoader();
/*      */ 
/* 1254 */       Class localClass = Class.forName(str1, false, localClassLoader);
/*      */ 
/* 1256 */       localConstructor = localClass.getConstructor(new Class[] { String.class });
/*      */     } catch (Exception localException1) {
/* 1258 */       throw new XMLParseException(localException1, "Cannot parse value: <" + paramString + ">");
/*      */     }
/*      */ 
/* 1261 */     String str2 = paramString.substring(i + 1, paramString.length() - 1);
/*      */     try {
/* 1263 */       return localConstructor.newInstance(new Object[] { str2 });
/*      */     } catch (Exception localException2) {
/* 1265 */       String str3 = "Cannot construct instance of " + str1 + " with arg: <" + paramString + ">";
/*      */ 
/* 1268 */       throw new XMLParseException(localException2, str3);
/*      */     }
/*      */   }
/*      */ 
/*      */   public synchronized String toString()
/*      */   {
/* 1290 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/* 1291 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Entry");
/*      */     }
/*      */ 
/* 1296 */     String str = "";
/* 1297 */     String[] arrayOfString = getFields();
/*      */ 
/* 1299 */     if ((arrayOfString == null) || (arrayOfString.length == 0)) {
/* 1300 */       if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/* 1301 */         JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Empty Descriptor");
/*      */       }
/*      */ 
/* 1305 */       return str;
/*      */     }
/*      */ 
/* 1308 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/* 1309 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Printing " + arrayOfString.length + " fields");
/*      */     }
/*      */ 
/* 1314 */     for (int i = 0; i < arrayOfString.length; i++) {
/* 1315 */       if (i == arrayOfString.length - 1)
/* 1316 */         str = str.concat(arrayOfString[i]);
/*      */       else {
/* 1318 */         str = str.concat(arrayOfString[i] + ", ");
/*      */       }
/*      */     }
/*      */ 
/* 1322 */     if (JmxProperties.MODELMBEAN_LOGGER.isLoggable(Level.FINEST)) {
/* 1323 */       JmxProperties.MODELMBEAN_LOGGER.logp(Level.FINEST, DescriptorSupport.class.getName(), "toString()", "Exit returning " + str);
/*      */     }
/*      */ 
/* 1328 */     return str;
/*      */   }
/*      */ 
/*      */   private long toNumeric(String paramString)
/*      */   {
/*      */     try
/*      */     {
/* 1335 */       return Long.parseLong(paramString); } catch (Exception localException) {
/*      */     }
/* 1337 */     return -2L;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/* 1348 */     ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1349 */     Map localMap = (Map)Util.cast(localGetField.get("descriptor", null));
/* 1350 */     init(null);
/* 1351 */     if (localMap != null)
/* 1352 */       this.descriptorMap.putAll(localMap);
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1372 */     ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 1373 */     boolean bool = "1.0".equals(serialForm);
/* 1374 */     if (bool) {
/* 1375 */       localPutField.put("currClass", "DescriptorSupport");
/*      */     }
/*      */ 
/* 1383 */     Object localObject = this.descriptorMap;
/* 1384 */     if (((SortedMap)localObject).containsKey("targetObject")) {
/* 1385 */       localObject = new TreeMap(this.descriptorMap);
/* 1386 */       ((SortedMap)localObject).remove("targetObject");
/*      */     }
/*      */     HashMap localHashMap;
/* 1390 */     if ((bool) || ("1.2.0".equals(serialForm)) || ("1.2.1".equals(serialForm)))
/*      */     {
/* 1392 */       localHashMap = new HashMap();
/* 1393 */       for (Map.Entry localEntry : ((SortedMap)localObject).entrySet())
/* 1394 */         localHashMap.put(((String)localEntry.getKey()).toLowerCase(), localEntry.getValue());
/*      */     } else {
/* 1396 */       localHashMap = new HashMap((Map)localObject);
/*      */     }
/* 1398 */     localPutField.put("descriptor", localHashMap);
/* 1399 */     paramObjectOutputStream.writeFields();
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*  121 */     String str1 = null;
/*  122 */     int j = 0;
/*      */     try {
/*  124 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  125 */       str1 = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  126 */       j = "1.0".equals(str1);
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  130 */     serialForm = str1;
/*  131 */     if (j != 0) {
/*  132 */       serialPersistentFields = oldSerialPersistentFields;
/*  133 */       serialVersionUID = 8071560848919417985L;
/*      */     } else {
/*  135 */       serialPersistentFields = newSerialPersistentFields;
/*  136 */       serialVersionUID = -6292969195866300415L;
/*      */     }
/*      */ 
/* 1109 */     entities = new String[] { " &#32;", "\"&quot;", "<&lt;", ">&gt;", "&&amp;", "\r&#13;", "\t&#9;", "\n&#10;", "\f&#12;" };
/*      */ 
/* 1120 */     entityToCharMap = new HashMap();
/*      */ 
/* 1125 */     int i = 0;
/*      */     char c;
/* 1126 */     for (j = 0; j < entities.length; j++) {
/* 1127 */       c = entities[j].charAt(0);
/* 1128 */       if (c > i)
/* 1129 */         i = c;
/*      */     }
/* 1131 */     charToEntityMap = new String[i + 1];
/* 1132 */     for (int k = 0; k < entities.length; k++) {
/* 1133 */       c = entities[k].charAt(0);
/* 1134 */       String str2 = entities[k].substring(1);
/* 1135 */       charToEntityMap[c] = str2;
/* 1136 */       entityToCharMap.put(str2, Character.valueOf(c));
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.modelmbean.DescriptorSupport
 * JD-Core Version:    0.6.2
 */