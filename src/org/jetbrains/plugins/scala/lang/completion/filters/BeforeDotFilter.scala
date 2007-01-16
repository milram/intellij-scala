package org.jetbrains.plugins.scala.lang.completion.filters

import com.intellij.psi.tree.IElementType;
import com.intellij.psi.PsiElement;
import com.intellij.psi.impl.source.tree.LeafPsiElement;
import com.intellij.psi.filters.TrueFilter;
import com.intellij.psi.filters.TextFilter;
import com.intellij.psi.filters.AndFilter;
import com.intellij.psi.filters.NotFilter;
import com.intellij.psi.filters.position.LeftNeighbour;
import org.jetbrains.plugins.scala.lang.lexer.ScalaTokenTypes;


class BeforeDotFilter(elems : IElementType*) extends LeftNeighbour {

  trait SeqContainer[T] requires SeqContainer[T] extends Seq[T]{
    def contains(elem: T): Boolean = {
      var list = this.toList
      list.contains(elem)
    }
  }

  def contains[T](seq: Seq[T], elem: T): Boolean = {
    seq.toList.contains(elem)
  }

  override def isAcceptable (element: java.lang.Object, context: PsiElement): Boolean = {
    if (!(element.isInstanceOf[PsiElement])) return false
    val elementType = element.asInstanceOf[PsiElement].getNode().getElementType()
    contains(elems, elementType)
  }
}