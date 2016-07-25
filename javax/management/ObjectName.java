/*      */ package javax.management;
/*      */ 
/*      */ import com.sun.jmx.mbeanserver.GetPropertyAction;
/*      */ import com.sun.jmx.mbeanserver.Util;
/*      */ import java.io.IOException;
/*      */ import java.io.InvalidObjectException;
/*      */ import java.io.ObjectInputStream;
/*      */ import java.io.ObjectInputStream.GetField;
/*      */ import java.io.ObjectOutputStream;
/*      */ import java.io.ObjectOutputStream.PutField;
/*      */ import java.io.ObjectStreamField;
/*      */ import java.security.AccessController;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collections;
/*      */ import java.util.HashMap;
/*      */ import java.util.Hashtable;
/*      */ import java.util.Iterator;
/*      */ import java.util.Map;
/*      */ import java.util.Map.Entry;
/*      */ import java.util.Set;
/*      */ 
/*      */ public class ObjectName
/*      */   implements Comparable<ObjectName>, QueryExp
/*      */ {
/*      */   private static final long oldSerialVersionUID = -5467795090068647408L;
/*      */   private static final long newSerialVersionUID = 1081892073854801359L;
/*  301 */   private static final ObjectStreamField[] oldSerialPersistentFields = { new ObjectStreamField("domain", String.class), new ObjectStreamField("propertyList", Hashtable.class), new ObjectStreamField("propertyListString", String.class), new ObjectStreamField("canonicalName", String.class), new ObjectStreamField("pattern", Boolean.TYPE), new ObjectStreamField("propertyPattern", Boolean.TYPE) };
/*      */ 
/*  312 */   private static final ObjectStreamField[] newSerialPersistentFields = new ObjectStreamField[0];
/*      */   private static final long serialVersionUID;
/*      */   private static final ObjectStreamField[] serialPersistentFields;
/*  317 */   private static boolean compat = false;
/*      */ 
/*  343 */   private static final Property[] _Empty_property_array = new Property[0];
/*      */   private transient String _canonicalName;
/*      */   private transient Property[] _kp_array;
/*      */   private transient Property[] _ca_array;
/*  370 */   private transient int _domain_length = 0;
/*      */   private transient Map<String, String> _propertyList;
/*  382 */   private transient boolean _domain_pattern = false;
/*      */ 
/*  388 */   private transient boolean _property_list_pattern = false;
/*      */ 
/*  394 */   private transient boolean _property_value_pattern = false;
/*      */ 
/* 1920 */   public static final ObjectName WILDCARD = Util.newObjectName("*:*");
/*      */ 
/*      */   private void construct(String paramString)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  420 */     if (paramString == null) {
/*  421 */       throw new NullPointerException("name cannot be null");
/*      */     }
/*      */ 
/*  424 */     if (paramString.length() == 0)
/*      */     {
/*  426 */       this._canonicalName = "*:*";
/*  427 */       this._kp_array = _Empty_property_array;
/*  428 */       this._ca_array = _Empty_property_array;
/*  429 */       this._domain_length = 1;
/*  430 */       this._propertyList = null;
/*  431 */       this._domain_pattern = true;
/*  432 */       this._property_list_pattern = true;
/*  433 */       this._property_value_pattern = false;
/*  434 */       return;
/*      */     }
/*      */ 
/*  438 */     char[] arrayOfChar1 = paramString.toCharArray();
/*  439 */     int i = arrayOfChar1.length;
/*  440 */     char[] arrayOfChar2 = new char[i];
/*      */ 
/*  442 */     int j = 0;
/*  443 */     int k = 0;
/*      */ 
/*  448 */     while (k < i)
/*      */     {
/*      */       int n;
/*  449 */       switch (arrayOfChar1[k]) {
/*      */       case ':':
/*  451 */         this._domain_length = (k++);
/*  452 */         break;
/*      */       case '=':
/*  461 */         k++; n = k;
/*      */       case '\n':
/*      */       case '*':
/*      */       case '?':
/*      */       default:
/*  462 */         while ((n < i) && (arrayOfChar1[(n++)] != ':')) {
/*  463 */           if (n == i) {
/*  464 */             throw new MalformedObjectNameException("Domain part must be specified");
/*      */ 
/*  468 */             throw new MalformedObjectNameException("Invalid character '\\n' in domain name");
/*      */ 
/*  472 */             this._domain_pattern = true;
/*  473 */             k++;
/*  474 */             break;
/*      */ 
/*  476 */             k++;
/*      */           }
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/*  482 */     if (k == i) {
/*  483 */       throw new MalformedObjectNameException("Key properties cannot be empty");
/*      */     }
/*      */ 
/*  487 */     System.arraycopy(arrayOfChar1, 0, arrayOfChar2, 0, this._domain_length);
/*  488 */     arrayOfChar2[this._domain_length] = ':';
/*  489 */     j = this._domain_length + 1;
/*      */ 
/*  493 */     HashMap localHashMap = new HashMap();
/*      */ 
/*  497 */     int i2 = 0;
/*      */ 
/*  501 */     Object localObject2 = new String[10];
/*  502 */     this._kp_array = new Property[10];
/*  503 */     this._property_list_pattern = false;
/*  504 */     this._property_value_pattern = false;
/*      */ 
/*  506 */     while (k < i) {
/*  507 */       int m = arrayOfChar1[k];
/*      */ 
/*  510 */       if (m == 42) {
/*  511 */         if (this._property_list_pattern) {
/*  512 */           throw new MalformedObjectNameException("Cannot have several '*' characters in pattern property list");
/*      */         }
/*      */ 
/*  516 */         this._property_list_pattern = true;
/*  517 */         k++; if ((k < i) && (arrayOfChar1[k] != ',')) {
/*  518 */           throw new MalformedObjectNameException("Invalid character found after '*': end of name or ',' expected");
/*      */         }
/*      */ 
/*  521 */         if (k == i) {
/*  522 */           if (i2 != 0)
/*      */             break;
/*  524 */           this._kp_array = _Empty_property_array;
/*  525 */           this._ca_array = _Empty_property_array;
/*  526 */           this._propertyList = Collections.emptyMap(); break;
/*      */         }
/*      */ 
/*  531 */         k++;
/*      */       }
/*      */       else
/*      */       {
/*  538 */         int i3 = k;
/*  539 */         int i4 = i3;
/*  540 */         if (arrayOfChar1[i3] == '=')
/*  541 */           throw new MalformedObjectNameException("Invalid key (empty)");
/*      */         char c;
/*  542 */         while ((i3 < i) && ((c = arrayOfChar1[(i3++)]) != '=')) {
/*  543 */           switch (c)
/*      */           {
/*      */           case '\n':
/*      */           case '*':
/*      */           case ',':
/*      */           case ':':
/*      */           case '?':
/*  550 */             String str2 = "" + c;
/*  551 */             throw new MalformedObjectNameException("Invalid character '" + str2 + "' in key part of property");
/*      */           }
/*      */         }
/*      */ 
/*  555 */         if (arrayOfChar1[(i3 - 1)] != '=') {
/*  556 */           throw new MalformedObjectNameException("Unterminated key property part");
/*      */         }
/*  558 */         int i6 = i3;
/*  559 */         int i5 = i6 - i4 - 1;
/*      */ 
/*  562 */         int i8 = 0;
/*      */         int i1;
/*      */         int i7;
/*      */         Object localObject3;
/*  563 */         if ((i3 < i) && (arrayOfChar1[i3] == '"')) {
/*  564 */           i1 = 1;
/*      */           while (true)
/*      */           {
/*  567 */             i3++; if ((i3 >= i) || ((c = arrayOfChar1[i3]) == '"')) {
/*      */               break;
/*      */             }
/*  570 */             if (c == '\\') {
/*  571 */               i3++; if (i3 == i) {
/*  572 */                 throw new MalformedObjectNameException("Unterminated quoted value");
/*      */               }
/*  574 */               switch (c = arrayOfChar1[i3]) {
/*      */               case '"':
/*      */               case '*':
/*      */               case '?':
/*      */               case '\\':
/*      */               case 'n':
/*  580 */                 break;
/*      */               default:
/*  582 */                 throw new MalformedObjectNameException("Invalid escape sequence '\\" + c + "' in quoted value");
/*      */               }
/*      */             }
/*      */             else {
/*  586 */               if (c == '\n') {
/*  587 */                 throw new MalformedObjectNameException("Newline in quoted value");
/*      */               }
/*      */ 
/*  590 */               switch (c) {
/*      */               case '*':
/*      */               case '?':
/*  593 */                 i8 = 1;
/*      */               }
/*      */             }
/*      */           }
/*      */ 
/*  598 */           if (i3 == i) {
/*  599 */             throw new MalformedObjectNameException("Unterminated quoted value");
/*      */           }
/*  601 */           i3++; i7 = i3 - i6;
/*      */         }
/*      */         else {
/*  604 */           i1 = 0;
/*  605 */           while ((i3 < i) && ((c = arrayOfChar1[i3]) != ',')) {
/*  606 */             switch (c)
/*      */             {
/*      */             case '*':
/*      */             case '?':
/*  610 */               i8 = 1;
/*  611 */               i3++;
/*  612 */               break;
/*      */             case '\n':
/*      */             case '"':
/*      */             case ':':
/*      */             case '=':
/*  617 */               localObject3 = "" + c;
/*  618 */               throw new MalformedObjectNameException("Invalid character '" + (String)localObject3 + "' in value part of property");
/*      */             default:
/*  622 */               i3++;
/*      */             }
/*      */           }
/*  625 */           i7 = i3 - i6;
/*      */         }
/*      */ 
/*  629 */         if (i3 == i - 1) {
/*  630 */           if (i1 != 0) {
/*  631 */             throw new MalformedObjectNameException("Invalid ending character `" + arrayOfChar1[i3] + "'");
/*      */           }
/*      */ 
/*  634 */           throw new MalformedObjectNameException("Invalid ending comma");
/*      */         }
/*  636 */         i3++;
/*      */         Object localObject1;
/*  639 */         if (i8 == 0) {
/*  640 */           localObject1 = new Property(i4, i5, i7);
/*      */         } else {
/*  642 */           this._property_value_pattern = true;
/*  643 */           localObject1 = new PatternProperty(i4, i5, i7);
/*      */         }
/*  645 */         String str1 = paramString.substring(i4, i4 + i5);
/*      */ 
/*  647 */         if (i2 == localObject2.length) {
/*  648 */           localObject3 = new String[i2 + 10];
/*  649 */           System.arraycopy(localObject2, 0, localObject3, 0, i2);
/*  650 */           localObject2 = localObject3;
/*      */         }
/*  652 */         localObject2[i2] = str1;
/*      */ 
/*  654 */         addProperty((Property)localObject1, i2, localHashMap, str1);
/*  655 */         i2++;
/*  656 */         k = i3;
/*      */       }
/*      */     }
/*      */ 
/*  660 */     setCanonicalName(arrayOfChar1, arrayOfChar2, (String[])localObject2, localHashMap, j, i2);
/*      */   }
/*      */ 
/*      */   private void construct(String paramString, Map<String, String> paramMap)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  680 */     if (paramString == null) {
/*  681 */       throw new NullPointerException("domain cannot be null");
/*      */     }
/*      */ 
/*  684 */     if (paramMap == null) {
/*  685 */       throw new NullPointerException("key property list cannot be null");
/*      */     }
/*      */ 
/*  688 */     if (paramMap.isEmpty()) {
/*  689 */       throw new MalformedObjectNameException("key property list cannot be empty");
/*      */     }
/*      */ 
/*  693 */     if (!isDomain(paramString)) {
/*  694 */       throw new MalformedObjectNameException("Invalid domain: " + paramString);
/*      */     }
/*      */ 
/*  697 */     StringBuilder localStringBuilder = new StringBuilder();
/*  698 */     localStringBuilder.append(paramString).append(':');
/*  699 */     this._domain_length = paramString.length();
/*      */ 
/*  702 */     int i = paramMap.size();
/*  703 */     this._kp_array = new Property[i];
/*      */ 
/*  705 */     String[] arrayOfString = new String[i];
/*  706 */     HashMap localHashMap = new HashMap();
/*      */ 
/*  709 */     int k = 0;
/*  710 */     for (Iterator localIterator = paramMap.entrySet().iterator(); localIterator.hasNext(); ) { localObject2 = (Map.Entry)localIterator.next();
/*  711 */       if (localStringBuilder.length() > 0)
/*  712 */         localStringBuilder.append(","); localObject3 = (String)((Map.Entry)localObject2).getKey();
/*      */       String str;
/*      */       try {
/*  716 */         str = (String)((Map.Entry)localObject2).getValue();
/*      */       } catch (ClassCastException localClassCastException) {
/*  718 */         throw new MalformedObjectNameException(localClassCastException.getMessage());
/*      */       }
/*  720 */       int j = localStringBuilder.length();
/*  721 */       checkKey((String)localObject3);
/*  722 */       localStringBuilder.append((String)localObject3);
/*  723 */       arrayOfString[k] = localObject3;
/*  724 */       localStringBuilder.append("=");
/*  725 */       boolean bool = checkValue(str);
/*  726 */       localStringBuilder.append(str);
/*      */       Object localObject1;
/*  727 */       if (!bool) {
/*  728 */         localObject1 = new Property(j, ((String)localObject3).length(), str.length());
/*      */       }
/*      */       else
/*      */       {
/*  732 */         this._property_value_pattern = true;
/*  733 */         localObject1 = new PatternProperty(j, ((String)localObject3).length(), str.length());
/*      */       }
/*      */ 
/*  737 */       addProperty((Property)localObject1, k, localHashMap, (String)localObject3);
/*  738 */       k++;
/*      */     }
/*      */ 
/*  742 */     int m = localStringBuilder.length();
/*  743 */     Object localObject2 = new char[m];
/*  744 */     localStringBuilder.getChars(0, m, (char[])localObject2, 0);
/*  745 */     Object localObject3 = new char[m];
/*  746 */     System.arraycopy(localObject2, 0, localObject3, 0, this._domain_length + 1);
/*      */ 
/*  748 */     setCanonicalName((char[])localObject2, (char[])localObject3, arrayOfString, localHashMap, this._domain_length + 1, this._kp_array.length);
/*      */   }
/*      */ 
/*      */   private void addProperty(Property paramProperty, int paramInt, Map<String, Property> paramMap, String paramString)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  763 */     if (paramMap.containsKey(paramString)) throw new MalformedObjectNameException("key `" + paramString + "' already defined");
/*      */ 
/*  768 */     if (paramInt == this._kp_array.length) {
/*  769 */       Property[] arrayOfProperty = new Property[paramInt + 10];
/*  770 */       System.arraycopy(this._kp_array, 0, arrayOfProperty, 0, paramInt);
/*  771 */       this._kp_array = arrayOfProperty;
/*      */     }
/*  773 */     this._kp_array[paramInt] = paramProperty;
/*  774 */     paramMap.put(paramString, paramProperty);
/*      */   }
/*      */ 
/*      */   private void setCanonicalName(char[] paramArrayOfChar1, char[] paramArrayOfChar2, String[] paramArrayOfString, Map<String, Property> paramMap, int paramInt1, int paramInt2)
/*      */   {
/*  788 */     if (this._kp_array != _Empty_property_array) {
/*  789 */       String[] arrayOfString = new String[paramInt2];
/*  790 */       Property[] arrayOfProperty = new Property[paramInt2];
/*      */ 
/*  792 */       System.arraycopy(paramArrayOfString, 0, arrayOfString, 0, paramInt2);
/*  793 */       Arrays.sort(arrayOfString);
/*  794 */       paramArrayOfString = arrayOfString;
/*  795 */       System.arraycopy(this._kp_array, 0, arrayOfProperty, 0, paramInt2);
/*  796 */       this._kp_array = arrayOfProperty;
/*  797 */       this._ca_array = new Property[paramInt2];
/*      */ 
/*  801 */       for (int i = 0; i < paramInt2; i++) {
/*  802 */         this._ca_array[i] = ((Property)paramMap.get(paramArrayOfString[i]));
/*      */       }
/*      */ 
/*  806 */       i = paramInt2 - 1;
/*      */ 
/*  809 */       for (int k = 0; k <= i; k++) {
/*  810 */         Property localProperty = this._ca_array[k];
/*      */ 
/*  812 */         int j = localProperty._key_length + localProperty._value_length + 1;
/*  813 */         System.arraycopy(paramArrayOfChar1, localProperty._key_index, paramArrayOfChar2, paramInt1, j);
/*      */ 
/*  815 */         localProperty.setKeyIndex(paramInt1);
/*  816 */         paramInt1 += j;
/*  817 */         if (k != i) {
/*  818 */           paramArrayOfChar2[paramInt1] = ',';
/*  819 */           paramInt1++;
/*      */         }
/*      */       }
/*      */ 
/*      */     }
/*      */ 
/*  825 */     if (this._property_list_pattern) {
/*  826 */       if (this._kp_array != _Empty_property_array)
/*  827 */         paramArrayOfChar2[(paramInt1++)] = ',';
/*  828 */       paramArrayOfChar2[(paramInt1++)] = '*';
/*      */     }
/*      */ 
/*  832 */     this._canonicalName = new String(paramArrayOfChar2, 0, paramInt1).intern();
/*      */   }
/*      */ 
/*      */   private static int parseKey(char[] paramArrayOfChar, int paramInt)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  847 */     int i = paramInt;
/*  848 */     int j = paramInt;
/*  849 */     int k = paramArrayOfChar.length;
/*  850 */     while (i < k) {
/*  851 */       char c = paramArrayOfChar[(i++)];
/*  852 */       switch (c) {
/*      */       case '\n':
/*      */       case '*':
/*      */       case ',':
/*      */       case ':':
/*      */       case '?':
/*  858 */         String str = "" + c;
/*  859 */         throw new MalformedObjectNameException("Invalid character in key: `" + str + "'");
/*      */       case '=':
/*  864 */         j = i - 1;
/*  865 */         break;
/*      */       default:
/*  867 */         if (i < k) continue;
/*  868 */         j = i;
/*      */       }
/*      */     }
/*      */ 
/*  872 */     return j;
/*      */   }
/*      */ 
/*      */   private static int[] parseValue(char[] paramArrayOfChar, int paramInt)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  891 */     int i = 0;
/*      */ 
/*  893 */     int j = paramInt;
/*  894 */     int k = paramInt;
/*      */ 
/*  896 */     int m = paramArrayOfChar.length;
/*  897 */     int n = paramArrayOfChar[paramInt];
/*      */     char c;
/*  899 */     if (n == 34)
/*      */     {
/*  901 */       j++; if (j == m) throw new MalformedObjectNameException("Invalid quote");
/*      */ 
/*  951 */       for (; j < m; 
/*  951 */         throw new MalformedObjectNameException("Missing termination quote"))
/*      */       {
/*  904 */         c = paramArrayOfChar[j];
/*  905 */         if (c == '\\') {
/*  906 */           j++; if (j == m) throw new MalformedObjectNameException("Invalid unterminated quoted character sequence");
/*      */ 
/*  909 */           c = paramArrayOfChar[j];
/*  910 */           switch (c) {
/*      */           case '*':
/*      */           case '?':
/*      */           case '\\':
/*      */           case 'n':
/*  915 */             break;
/*      */           case '"':
/*  921 */             if (j + 1 != m) break; throw new MalformedObjectNameException("Missing termination quote");
/*      */           default:
/*  926 */             throw new MalformedObjectNameException("Invalid quoted character sequence '\\" + c + "'");
/*      */           }
/*      */         }
/*      */         else
/*      */         {
/*  931 */           if (c == '\n') {
/*  932 */             throw new MalformedObjectNameException("Newline in quoted value");
/*      */           }
/*  934 */           if (c == '"') {
/*  935 */             j++;
/*  936 */             break;
/*      */           }
/*  938 */           switch (c) {
/*      */           case '*':
/*      */           case '?':
/*  941 */             i = 1;
/*      */           }
/*      */ 
/*      */         }
/*      */ 
/*  945 */         j++;
/*      */ 
/*  951 */         if ((j < m) || (c == '"'));
/*      */       }
/*      */ 
/*  954 */       k = j;
/*  955 */       if ((j < m) && 
/*  956 */         (paramArrayOfChar[(j++)] != ',')) throw new MalformedObjectNameException("Invalid quote");
/*      */ 
/*      */     }
/*      */     else
/*      */     {
/*  961 */       while (j < m) {
/*  962 */         c = paramArrayOfChar[(j++)];
/*  963 */         switch (c) {
/*      */         case '*':
/*      */         case '?':
/*  966 */           i = 1;
/*  967 */           if (j >= m)
/*  968 */             k = j;
/*  969 */           break;
/*      */         case '\n':
/*      */         case ':':
/*      */         case '=':
/*  973 */           String str = "" + c;
/*  974 */           throw new MalformedObjectNameException("Invalid character `" + str + "' in value");
/*      */         case ',':
/*  978 */           k = j - 1;
/*  979 */           break;
/*      */         default:
/*  981 */           if (j >= m)
/*  982 */             k = j;
/*      */           break;
/*      */         }
/*      */       }
/*      */     }
/*  987 */     return new int[] { k, i != 0 ? 1 : 0 };
/*      */   }
/*      */ 
/*      */   private static boolean checkValue(String paramString)
/*      */     throws MalformedObjectNameException
/*      */   {
/*  998 */     if (paramString == null) throw new NullPointerException("Invalid value (null)");
/*      */ 
/* 1001 */     int i = paramString.length();
/* 1002 */     if (i == 0) {
/* 1003 */       return false;
/*      */     }
/* 1005 */     char[] arrayOfChar = paramString.toCharArray();
/* 1006 */     int[] arrayOfInt = parseValue(arrayOfChar, 0);
/* 1007 */     int j = arrayOfInt[0];
/* 1008 */     boolean bool = arrayOfInt[1] == 1;
/* 1009 */     if (j < i) throw new MalformedObjectNameException("Invalid character in value: `" + arrayOfChar[j] + "'");
/*      */ 
/* 1012 */     return bool;
/*      */   }
/*      */ 
/*      */   private static void checkKey(String paramString)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1021 */     if (paramString == null) throw new NullPointerException("Invalid key (null)");
/*      */ 
/* 1024 */     int i = paramString.length();
/* 1025 */     if (i == 0) throw new MalformedObjectNameException("Invalid key (empty)");
/*      */ 
/* 1027 */     char[] arrayOfChar = paramString.toCharArray();
/* 1028 */     int j = parseKey(arrayOfChar, 0);
/* 1029 */     if (j < i) throw new MalformedObjectNameException("Invalid character in value: `" + arrayOfChar[j] + "'");
/*      */   }
/*      */ 
/*      */   private boolean isDomain(String paramString)
/*      */   {
/* 1043 */     if (paramString == null) return true;
/* 1044 */     int i = paramString.length();
/* 1045 */     int j = 0;
/* 1046 */     while (j < i) {
/* 1047 */       int k = paramString.charAt(j++);
/* 1048 */       switch (k) {
/*      */       case 10:
/*      */       case 58:
/* 1051 */         return false;
/*      */       case 42:
/*      */       case 63:
/* 1054 */         this._domain_pattern = true;
/*      */       }
/*      */     }
/*      */ 
/* 1058 */     return true;
/*      */   }
/*      */ 
/*      */   private void readObject(ObjectInputStream paramObjectInputStream)
/*      */     throws IOException, ClassNotFoundException
/*      */   {
/*      */     String str1;
/* 1127 */     if (compat)
/*      */     {
/* 1131 */       ObjectInputStream.GetField localGetField = paramObjectInputStream.readFields();
/* 1132 */       String str2 = (String)localGetField.get("propertyListString", "");
/*      */ 
/* 1136 */       boolean bool = localGetField.get("propertyPattern", false);
/*      */ 
/* 1138 */       if (bool) {
/* 1139 */         str2 = str2 + ",*";
/*      */       }
/*      */ 
/* 1143 */       str1 = (String)localGetField.get("domain", "default") + ":" + str2;
/*      */     }
/*      */     else
/*      */     {
/* 1148 */       paramObjectInputStream.defaultReadObject();
/* 1149 */       str1 = (String)paramObjectInputStream.readObject();
/*      */     }
/*      */     try
/*      */     {
/* 1153 */       construct(str1);
/*      */     } catch (NullPointerException localNullPointerException) {
/* 1155 */       throw new InvalidObjectException(localNullPointerException.toString());
/*      */     } catch (MalformedObjectNameException localMalformedObjectNameException) {
/* 1157 */       throw new InvalidObjectException(localMalformedObjectNameException.toString());
/*      */     }
/*      */   }
/*      */ 
/*      */   private void writeObject(ObjectOutputStream paramObjectOutputStream)
/*      */     throws IOException
/*      */   {
/* 1223 */     if (compat)
/*      */     {
/* 1227 */       ObjectOutputStream.PutField localPutField = paramObjectOutputStream.putFields();
/* 1228 */       localPutField.put("domain", this._canonicalName.substring(0, this._domain_length));
/* 1229 */       localPutField.put("propertyList", getKeyPropertyList());
/* 1230 */       localPutField.put("propertyListString", getKeyPropertyListString());
/* 1231 */       localPutField.put("canonicalName", this._canonicalName);
/* 1232 */       localPutField.put("pattern", (this._domain_pattern) || (this._property_list_pattern));
/* 1233 */       localPutField.put("propertyPattern", this._property_list_pattern);
/* 1234 */       paramObjectOutputStream.writeFields();
/*      */     }
/*      */     else
/*      */     {
/* 1240 */       paramObjectOutputStream.defaultWriteObject();
/* 1241 */       paramObjectOutputStream.writeObject(getSerializedNameString());
/*      */     }
/*      */   }
/*      */ 
/*      */   public static ObjectName getInstance(String paramString)
/*      */     throws MalformedObjectNameException, NullPointerException
/*      */   {
/* 1273 */     return new ObjectName(paramString);
/*      */   }
/*      */ 
/*      */   public static ObjectName getInstance(String paramString1, String paramString2, String paramString3)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1301 */     return new ObjectName(paramString1, paramString2, paramString3);
/*      */   }
/*      */ 
/*      */   public static ObjectName getInstance(String paramString, Hashtable<String, String> paramHashtable)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1332 */     return new ObjectName(paramString, paramHashtable);
/*      */   }
/*      */ 
/*      */   public static ObjectName getInstance(ObjectName paramObjectName)
/*      */   {
/* 1365 */     if (paramObjectName.getClass().equals(ObjectName.class))
/* 1366 */       return paramObjectName;
/* 1367 */     return Util.newObjectName(paramObjectName.getSerializedNameString());
/*      */   }
/*      */ 
/*      */   public ObjectName(String paramString)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1382 */     construct(paramString);
/*      */   }
/*      */ 
/*      */   public ObjectName(String paramString1, String paramString2, String paramString3)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1403 */     Map localMap = Collections.singletonMap(paramString2, paramString3);
/* 1404 */     construct(paramString1, localMap);
/*      */   }
/*      */ 
/*      */   public ObjectName(String paramString, Hashtable<String, String> paramHashtable)
/*      */     throws MalformedObjectNameException
/*      */   {
/* 1425 */     construct(paramString, paramHashtable);
/*      */   }
/*      */ 
/*      */   public boolean isPattern()
/*      */   {
/* 1446 */     return (this._domain_pattern) || (this._property_list_pattern) || (this._property_value_pattern);
/*      */   }
/*      */ 
/*      */   public boolean isDomainPattern()
/*      */   {
/* 1458 */     return this._domain_pattern;
/*      */   }
/*      */ 
/*      */   public boolean isPropertyPattern()
/*      */   {
/* 1471 */     return (this._property_list_pattern) || (this._property_value_pattern);
/*      */   }
/*      */ 
/*      */   public boolean isPropertyListPattern()
/*      */   {
/* 1485 */     return this._property_list_pattern;
/*      */   }
/*      */ 
/*      */   public boolean isPropertyValuePattern()
/*      */   {
/* 1500 */     return this._property_value_pattern;
/*      */   }
/*      */ 
/*      */   public boolean isPropertyValuePattern(String paramString)
/*      */   {
/* 1519 */     if (paramString == null)
/* 1520 */       throw new NullPointerException("key property can't be null");
/* 1521 */     for (int i = 0; i < this._ca_array.length; i++) {
/* 1522 */       Property localProperty = this._ca_array[i];
/* 1523 */       String str = localProperty.getKeyString(this._canonicalName);
/* 1524 */       if (str.equals(paramString))
/* 1525 */         return localProperty instanceof PatternProperty;
/*      */     }
/* 1527 */     throw new IllegalArgumentException("key property not found");
/*      */   }
/*      */ 
/*      */   public String getCanonicalName()
/*      */   {
/* 1557 */     return this._canonicalName;
/*      */   }
/*      */ 
/*      */   public String getDomain()
/*      */   {
/* 1566 */     return this._canonicalName.substring(0, this._domain_length);
/*      */   }
/*      */ 
/*      */   public String getKeyProperty(String paramString)
/*      */   {
/* 1580 */     return (String)_getKeyPropertyList().get(paramString);
/*      */   }
/*      */ 
/*      */   private Map<String, String> _getKeyPropertyList()
/*      */   {
/* 1594 */     synchronized (this) {
/* 1595 */       if (this._propertyList == null)
/*      */       {
/* 1598 */         this._propertyList = new HashMap();
/* 1599 */         int i = this._ca_array.length;
/*      */ 
/* 1601 */         for (int j = i - 1; j >= 0; j--) {
/* 1602 */           Property localProperty = this._ca_array[j];
/* 1603 */           this._propertyList.put(localProperty.getKeyString(this._canonicalName), localProperty.getValueString(this._canonicalName));
/*      */         }
/*      */       }
/*      */     }
/*      */ 
/* 1608 */     return this._propertyList;
/*      */   }
/*      */ 
/*      */   public Hashtable<String, String> getKeyPropertyList()
/*      */   {
/* 1624 */     return new Hashtable(_getKeyPropertyList());
/*      */   }
/*      */ 
/*      */   public String getKeyPropertyListString()
/*      */   {
/* 1639 */     if (this._kp_array.length == 0) return "";
/*      */ 
/* 1643 */     int i = this._canonicalName.length() - this._domain_length - 1 - (this._property_list_pattern ? 2 : 0);
/*      */ 
/* 1646 */     char[] arrayOfChar1 = new char[i];
/* 1647 */     char[] arrayOfChar2 = this._canonicalName.toCharArray();
/* 1648 */     writeKeyPropertyListString(arrayOfChar2, arrayOfChar1, 0);
/* 1649 */     return new String(arrayOfChar1);
/*      */   }
/*      */ 
/*      */   private String getSerializedNameString()
/*      */   {
/* 1665 */     int i = this._canonicalName.length();
/* 1666 */     char[] arrayOfChar1 = new char[i];
/* 1667 */     char[] arrayOfChar2 = this._canonicalName.toCharArray();
/* 1668 */     int j = this._domain_length + 1;
/*      */ 
/* 1672 */     System.arraycopy(arrayOfChar2, 0, arrayOfChar1, 0, j);
/*      */ 
/* 1675 */     int k = writeKeyPropertyListString(arrayOfChar2, arrayOfChar1, j);
/*      */ 
/* 1678 */     if (this._property_list_pattern) {
/* 1679 */       if (k == j)
/*      */       {
/* 1681 */         arrayOfChar1[k] = '*';
/*      */       }
/*      */       else {
/* 1684 */         arrayOfChar1[k] = ',';
/* 1685 */         arrayOfChar1[(k + 1)] = '*';
/*      */       }
/*      */     }
/*      */ 
/* 1689 */     return new String(arrayOfChar1);
/*      */   }
/*      */ 
/*      */   private int writeKeyPropertyListString(char[] paramArrayOfChar1, char[] paramArrayOfChar2, int paramInt)
/*      */   {
/* 1704 */     if (this._kp_array.length == 0) return paramInt;
/*      */ 
/* 1706 */     char[] arrayOfChar1 = paramArrayOfChar2;
/* 1707 */     char[] arrayOfChar2 = paramArrayOfChar1;
/*      */ 
/* 1709 */     int i = paramInt;
/* 1710 */     int j = this._kp_array.length;
/* 1711 */     int k = j - 1;
/* 1712 */     for (int m = 0; m < j; m++) {
/* 1713 */       Property localProperty = this._kp_array[m];
/* 1714 */       int n = localProperty._key_length + localProperty._value_length + 1;
/* 1715 */       System.arraycopy(arrayOfChar2, localProperty._key_index, arrayOfChar1, i, n);
/*      */ 
/* 1717 */       i += n;
/* 1718 */       if (m < k) arrayOfChar1[(i++)] = ',';
/*      */     }
/* 1720 */     return i;
/*      */   }
/*      */ 
/*      */   public String getCanonicalKeyPropertyListString()
/*      */   {
/* 1737 */     if (this._ca_array.length == 0) return "";
/*      */ 
/* 1739 */     int i = this._canonicalName.length();
/* 1740 */     if (this._property_list_pattern) i -= 2;
/* 1741 */     return this._canonicalName.substring(this._domain_length + 1, i);
/*      */   }
/*      */ 
/*      */   public String toString()
/*      */   {
/* 1757 */     return getSerializedNameString();
/*      */   }
/*      */ 
/*      */   public boolean equals(Object paramObject)
/*      */   {
/* 1776 */     if (this == paramObject) return true;
/*      */ 
/* 1779 */     if (!(paramObject instanceof ObjectName)) return false;
/*      */ 
/* 1783 */     ObjectName localObjectName = (ObjectName)paramObject;
/* 1784 */     String str = localObjectName._canonicalName;
/* 1785 */     if (this._canonicalName == str) return true;
/*      */ 
/* 1789 */     return false;
/*      */   }
/*      */ 
/*      */   public int hashCode()
/*      */   {
/* 1798 */     return this._canonicalName.hashCode();
/*      */   }
/*      */ 
/*      */   public static String quote(String paramString)
/*      */   {
/* 1832 */     StringBuilder localStringBuilder = new StringBuilder("\"");
/* 1833 */     int i = paramString.length();
/* 1834 */     for (int j = 0; j < i; j++) {
/* 1835 */       char c = paramString.charAt(j);
/* 1836 */       switch (c) {
/*      */       case '\n':
/* 1838 */         c = 'n';
/* 1839 */         localStringBuilder.append('\\');
/* 1840 */         break;
/*      */       case '"':
/*      */       case '*':
/*      */       case '?':
/*      */       case '\\':
/* 1845 */         localStringBuilder.append('\\');
/*      */       }
/*      */ 
/* 1848 */       localStringBuilder.append(c);
/*      */     }
/* 1850 */     localStringBuilder.append('"');
/* 1851 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public static String unquote(String paramString)
/*      */   {
/* 1876 */     StringBuilder localStringBuilder = new StringBuilder();
/* 1877 */     int i = paramString.length();
/* 1878 */     if ((i < 2) || (paramString.charAt(0) != '"') || (paramString.charAt(i - 1) != '"'))
/* 1879 */       throw new IllegalArgumentException("Argument not quoted");
/* 1880 */     for (int j = 1; j < i - 1; j++) {
/* 1881 */       char c = paramString.charAt(j);
/* 1882 */       if (c == '\\') {
/* 1883 */         if (j == i - 2)
/* 1884 */           throw new IllegalArgumentException("Trailing backslash");
/* 1885 */         c = paramString.charAt(++j);
/* 1886 */         switch (c) {
/*      */         case 'n':
/* 1888 */           c = '\n';
/* 1889 */           break;
/*      */         case '"':
/*      */         case '*':
/*      */         case '?':
/*      */         case '\\':
/* 1894 */           break;
/*      */         default:
/* 1896 */           throw new IllegalArgumentException("Bad character '" + c + "' after backslash");
/*      */         }
/*      */       }
/*      */       else {
/* 1900 */         switch (c) {
/*      */         case '\n':
/*      */         case '"':
/*      */         case '*':
/*      */         case '?':
/* 1905 */           throw new IllegalArgumentException("Invalid unescaped character '" + c + "' in the string to unquote");
/*      */         }
/*      */ 
/*      */       }
/*      */ 
/* 1910 */       localStringBuilder.append(c);
/*      */     }
/* 1912 */     return localStringBuilder.toString();
/*      */   }
/*      */ 
/*      */   public boolean apply(ObjectName paramObjectName)
/*      */   {
/* 1945 */     if (paramObjectName == null) throw new NullPointerException();
/*      */ 
/* 1947 */     if ((paramObjectName._domain_pattern) || (paramObjectName._property_list_pattern) || (paramObjectName._property_value_pattern))
/*      */     {
/* 1950 */       return false;
/*      */     }
/*      */ 
/* 1953 */     if ((!this._domain_pattern) && (!this._property_list_pattern) && (!this._property_value_pattern))
/*      */     {
/* 1956 */       return this._canonicalName.equals(paramObjectName._canonicalName);
/*      */     }
/* 1958 */     return (matchDomains(paramObjectName)) && (matchKeys(paramObjectName));
/*      */   }
/*      */ 
/*      */   private final boolean matchDomains(ObjectName paramObjectName) {
/* 1962 */     if (this._domain_pattern)
/*      */     {
/* 1966 */       return Util.wildmatch(paramObjectName.getDomain(), getDomain());
/*      */     }
/* 1968 */     return getDomain().equals(paramObjectName.getDomain());
/*      */   }
/*      */ 
/*      */   private final boolean matchKeys(ObjectName paramObjectName)
/*      */   {
/* 1975 */     if ((this._property_value_pattern) && (!this._property_list_pattern) && (paramObjectName._ca_array.length != this._ca_array.length))
/*      */     {
/* 1978 */       return false;
/*      */     }
/*      */ 
/* 1983 */     if ((this._property_value_pattern) || (this._property_list_pattern)) {
/* 1984 */       localObject1 = paramObjectName._getKeyPropertyList();
/* 1985 */       localObject2 = this._ca_array;
/* 1986 */       String str1 = this._canonicalName;
/* 1987 */       for (int i = localObject2.length - 1; i >= 0; i--)
/*      */       {
/* 1991 */         Object localObject3 = localObject2[i];
/* 1992 */         String str2 = localObject3.getKeyString(str1);
/* 1993 */         String str3 = (String)((Map)localObject1).get(str2);
/*      */ 
/* 1996 */         if (str3 == null) return false;
/*      */ 
/* 1999 */         if ((this._property_value_pattern) && ((localObject3 instanceof PatternProperty)))
/*      */         {
/* 2002 */           if (!Util.wildmatch(str3, localObject3.getValueString(str1)))
/*      */           {
/* 2005 */             return false;
/*      */           }
/* 2007 */         } else if (!str3.equals(localObject3.getValueString(str1)))
/* 2008 */           return false;
/*      */       }
/* 2010 */       return true;
/*      */     }
/*      */ 
/* 2015 */     Object localObject1 = paramObjectName.getCanonicalKeyPropertyListString();
/* 2016 */     Object localObject2 = getCanonicalKeyPropertyListString();
/* 2017 */     return ((String)localObject1).equals(localObject2);
/*      */   }
/*      */ 
/*      */   public void setMBeanServer(MBeanServer paramMBeanServer)
/*      */   {
/*      */   }
/*      */ 
/*      */   public int compareTo(ObjectName paramObjectName)
/*      */   {
/* 2076 */     if (paramObjectName == this) return 0;
/*      */ 
/* 2080 */     int i = getDomain().compareTo(paramObjectName.getDomain());
/* 2081 */     if (i != 0) {
/* 2082 */       return i;
/*      */     }
/*      */ 
/* 2092 */     String str1 = getKeyProperty("type");
/* 2093 */     String str2 = paramObjectName.getKeyProperty("type");
/* 2094 */     if (str1 == null)
/* 2095 */       str1 = "";
/* 2096 */     if (str2 == null)
/* 2097 */       str2 = "";
/* 2098 */     int j = str1.compareTo(str2);
/* 2099 */     if (j != 0) {
/* 2100 */       return j;
/*      */     }
/*      */ 
/* 2104 */     return getCanonicalName().compareTo(paramObjectName.getCanonicalName());
/*      */   }
/*      */ 
/*      */   static
/*      */   {
/*      */     try
/*      */     {
/*  320 */       GetPropertyAction localGetPropertyAction = new GetPropertyAction("jmx.serial.form");
/*  321 */       String str = (String)AccessController.doPrivileged(localGetPropertyAction);
/*  322 */       compat = (str != null) && (str.equals("1.0"));
/*      */     }
/*      */     catch (Exception localException) {
/*      */     }
/*  326 */     if (compat) {
/*  327 */       serialPersistentFields = oldSerialPersistentFields;
/*  328 */       serialVersionUID = -5467795090068647408L;
/*      */     } else {
/*  330 */       serialPersistentFields = newSerialPersistentFields;
/*  331 */       serialVersionUID = 1081892073854801359L;
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class PatternProperty extends ObjectName.Property
/*      */   {
/*      */     PatternProperty(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/*  276 */       super(paramInt2, paramInt3);
/*      */     }
/*      */   }
/*      */ 
/*      */   private static class Property
/*      */   {
/*      */     int _key_index;
/*      */     int _key_length;
/*      */     int _value_length;
/*      */ 
/*      */     Property(int paramInt1, int paramInt2, int paramInt3)
/*      */     {
/*  239 */       this._key_index = paramInt1;
/*  240 */       this._key_length = paramInt2;
/*  241 */       this._value_length = paramInt3;
/*      */     }
/*      */ 
/*      */     void setKeyIndex(int paramInt)
/*      */     {
/*  248 */       this._key_index = paramInt;
/*      */     }
/*      */ 
/*      */     String getKeyString(String paramString)
/*      */     {
/*  255 */       return paramString.substring(this._key_index, this._key_index + this._key_length);
/*      */     }
/*      */ 
/*      */     String getValueString(String paramString)
/*      */     {
/*  262 */       int i = this._key_index + this._key_length + 1;
/*  263 */       int j = i + this._value_length;
/*  264 */       return paramString.substring(i, j);
/*      */     }
/*      */   }
/*      */ }

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.management.ObjectName
 * JD-Core Version:    0.6.2
 */