package javax.imageio.metadata;

import java.util.Locale;
import javax.imageio.ImageTypeSpecifier;

public abstract interface IIOMetadataFormat
{
  public static final int CHILD_POLICY_EMPTY = 0;
  public static final int CHILD_POLICY_ALL = 1;
  public static final int CHILD_POLICY_SOME = 2;
  public static final int CHILD_POLICY_CHOICE = 3;
  public static final int CHILD_POLICY_SEQUENCE = 4;
  public static final int CHILD_POLICY_REPEAT = 5;
  public static final int CHILD_POLICY_MAX = 5;
  public static final int VALUE_NONE = 0;
  public static final int VALUE_ARBITRARY = 1;
  public static final int VALUE_RANGE = 2;
  public static final int VALUE_RANGE_MIN_INCLUSIVE_MASK = 4;
  public static final int VALUE_RANGE_MAX_INCLUSIVE_MASK = 8;
  public static final int VALUE_RANGE_MIN_INCLUSIVE = 6;
  public static final int VALUE_RANGE_MAX_INCLUSIVE = 10;
  public static final int VALUE_RANGE_MIN_MAX_INCLUSIVE = 14;
  public static final int VALUE_ENUMERATION = 16;
  public static final int VALUE_LIST = 32;
  public static final int DATATYPE_STRING = 0;
  public static final int DATATYPE_BOOLEAN = 1;
  public static final int DATATYPE_INTEGER = 2;
  public static final int DATATYPE_FLOAT = 3;
  public static final int DATATYPE_DOUBLE = 4;

  public abstract String getRootName();

  public abstract boolean canNodeAppear(String paramString, ImageTypeSpecifier paramImageTypeSpecifier);

  public abstract int getElementMinChildren(String paramString);

  public abstract int getElementMaxChildren(String paramString);

  public abstract String getElementDescription(String paramString, Locale paramLocale);

  public abstract int getChildPolicy(String paramString);

  public abstract String[] getChildNames(String paramString);

  public abstract String[] getAttributeNames(String paramString);

  public abstract int getAttributeValueType(String paramString1, String paramString2);

  public abstract int getAttributeDataType(String paramString1, String paramString2);

  public abstract boolean isAttributeRequired(String paramString1, String paramString2);

  public abstract String getAttributeDefaultValue(String paramString1, String paramString2);

  public abstract String[] getAttributeEnumerations(String paramString1, String paramString2);

  public abstract String getAttributeMinValue(String paramString1, String paramString2);

  public abstract String getAttributeMaxValue(String paramString1, String paramString2);

  public abstract int getAttributeListMinLength(String paramString1, String paramString2);

  public abstract int getAttributeListMaxLength(String paramString1, String paramString2);

  public abstract String getAttributeDescription(String paramString1, String paramString2, Locale paramLocale);

  public abstract int getObjectValueType(String paramString);

  public abstract Class<?> getObjectClass(String paramString);

  public abstract Object getObjectDefaultValue(String paramString);

  public abstract Object[] getObjectEnumerations(String paramString);

  public abstract Comparable<?> getObjectMinValue(String paramString);

  public abstract Comparable<?> getObjectMaxValue(String paramString);

  public abstract int getObjectArrayMinLength(String paramString);

  public abstract int getObjectArrayMaxLength(String paramString);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.imageio.metadata.IIOMetadataFormat
 * JD-Core Version:    0.6.2
 */