package org.omg.CORBA;

import org.omg.CORBA.TypeCodePackage.BadKind;
import org.omg.CORBA.TypeCodePackage.Bounds;
import org.omg.CORBA.portable.IDLEntity;

public abstract class TypeCode
  implements IDLEntity
{
  public abstract boolean equal(TypeCode paramTypeCode);

  public abstract boolean equivalent(TypeCode paramTypeCode);

  public abstract TypeCode get_compact_typecode();

  public abstract TCKind kind();

  public abstract String id()
    throws BadKind;

  public abstract String name()
    throws BadKind;

  public abstract int member_count()
    throws BadKind;

  public abstract String member_name(int paramInt)
    throws BadKind, Bounds;

  public abstract TypeCode member_type(int paramInt)
    throws BadKind, Bounds;

  public abstract Any member_label(int paramInt)
    throws BadKind, Bounds;

  public abstract TypeCode discriminator_type()
    throws BadKind;

  public abstract int default_index()
    throws BadKind;

  public abstract int length()
    throws BadKind;

  public abstract TypeCode content_type()
    throws BadKind;

  public abstract short fixed_digits()
    throws BadKind;

  public abstract short fixed_scale()
    throws BadKind;

  public abstract short member_visibility(int paramInt)
    throws BadKind, Bounds;

  public abstract short type_modifier()
    throws BadKind;

  public abstract TypeCode concrete_base_type()
    throws BadKind;
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     org.omg.CORBA.TypeCode
 * JD-Core Version:    0.6.2
 */