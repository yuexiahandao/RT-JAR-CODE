package javax.lang.model.element;

import java.util.List;
import javax.lang.model.type.TypeMirror;

public abstract interface AnnotationValueVisitor<R, P>
{
  public abstract R visit(AnnotationValue paramAnnotationValue, P paramP);

  public abstract R visit(AnnotationValue paramAnnotationValue);

  public abstract R visitBoolean(boolean paramBoolean, P paramP);

  public abstract R visitByte(byte paramByte, P paramP);

  public abstract R visitChar(char paramChar, P paramP);

  public abstract R visitDouble(double paramDouble, P paramP);

  public abstract R visitFloat(float paramFloat, P paramP);

  public abstract R visitInt(int paramInt, P paramP);

  public abstract R visitLong(long paramLong, P paramP);

  public abstract R visitShort(short paramShort, P paramP);

  public abstract R visitString(String paramString, P paramP);

  public abstract R visitType(TypeMirror paramTypeMirror, P paramP);

  public abstract R visitEnumConstant(VariableElement paramVariableElement, P paramP);

  public abstract R visitAnnotation(AnnotationMirror paramAnnotationMirror, P paramP);

  public abstract R visitArray(List<? extends AnnotationValue> paramList, P paramP);

  public abstract R visitUnknown(AnnotationValue paramAnnotationValue, P paramP);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.lang.model.element.AnnotationValueVisitor
 * JD-Core Version:    0.6.2
 */