package javax.annotation.processing;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.tools.Diagnostic.Kind;

public abstract interface Messager
{
  public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence);

  public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement);

  public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror);

  public abstract void printMessage(Diagnostic.Kind paramKind, CharSequence paramCharSequence, Element paramElement, AnnotationMirror paramAnnotationMirror, AnnotationValue paramAnnotationValue);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     javax.annotation.processing.Messager
 * JD-Core Version:    0.6.2
 */