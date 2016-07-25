package sun.reflect.generics.visitor;

import sun.reflect.generics.tree.ArrayTypeSignature;
import sun.reflect.generics.tree.BooleanSignature;
import sun.reflect.generics.tree.BottomSignature;
import sun.reflect.generics.tree.ByteSignature;
import sun.reflect.generics.tree.CharSignature;
import sun.reflect.generics.tree.ClassTypeSignature;
import sun.reflect.generics.tree.DoubleSignature;
import sun.reflect.generics.tree.FloatSignature;
import sun.reflect.generics.tree.FormalTypeParameter;
import sun.reflect.generics.tree.IntSignature;
import sun.reflect.generics.tree.LongSignature;
import sun.reflect.generics.tree.ShortSignature;
import sun.reflect.generics.tree.SimpleClassTypeSignature;
import sun.reflect.generics.tree.TypeVariableSignature;
import sun.reflect.generics.tree.VoidDescriptor;
import sun.reflect.generics.tree.Wildcard;

public abstract interface TypeTreeVisitor<T>
{
  public abstract T getResult();

  public abstract void visitFormalTypeParameter(FormalTypeParameter paramFormalTypeParameter);

  public abstract void visitClassTypeSignature(ClassTypeSignature paramClassTypeSignature);

  public abstract void visitArrayTypeSignature(ArrayTypeSignature paramArrayTypeSignature);

  public abstract void visitTypeVariableSignature(TypeVariableSignature paramTypeVariableSignature);

  public abstract void visitWildcard(Wildcard paramWildcard);

  public abstract void visitSimpleClassTypeSignature(SimpleClassTypeSignature paramSimpleClassTypeSignature);

  public abstract void visitBottomSignature(BottomSignature paramBottomSignature);

  public abstract void visitByteSignature(ByteSignature paramByteSignature);

  public abstract void visitBooleanSignature(BooleanSignature paramBooleanSignature);

  public abstract void visitShortSignature(ShortSignature paramShortSignature);

  public abstract void visitCharSignature(CharSignature paramCharSignature);

  public abstract void visitIntSignature(IntSignature paramIntSignature);

  public abstract void visitLongSignature(LongSignature paramLongSignature);

  public abstract void visitFloatSignature(FloatSignature paramFloatSignature);

  public abstract void visitDoubleSignature(DoubleSignature paramDoubleSignature);

  public abstract void visitVoidDescriptor(VoidDescriptor paramVoidDescriptor);
}

/* Location:           C:\Program Files\Java\jdk1.7.0_79\jre\lib\rt.jar
 * Qualified Name:     sun.reflect.generics.visitor.TypeTreeVisitor
 * JD-Core Version:    0.6.2
 */