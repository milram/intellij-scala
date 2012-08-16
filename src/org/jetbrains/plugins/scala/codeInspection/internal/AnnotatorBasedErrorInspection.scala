package org.jetbrains.plugins.scala.codeInspection.internal

import org.jetbrains.plugins.scala.codeInspection.InspectionsUtil
import com.intellij.psi.{PsiElement, PsiElementVisitor}
import org.jetbrains.plugins.scala.annotator.ScalaAnnotator
import java.lang.String
import com.intellij.openapi.util.TextRange
import com.intellij.lang.ASTNode
import com.intellij.lang.annotation.{HighlightSeverity, Annotation, AnnotationHolder}
import com.intellij.codeInspection.{ProblemHighlightType, ProblemsHolder, LocalInspectionTool}
import com.intellij.lang.annotation.AnnotationSession
import com.intellij.codeHighlighting.HighlightDisplayLevel

/**
 * @author Alexander Podkhalyuzin
 */
class AnnotatorBasedErrorInspection extends LocalInspectionTool {
  override def isEnabledByDefault: Boolean = false

  override def getID: String = "AnnotatorBasedError"

  override def getGroupDisplayName: String = "Scala: Internal"

  override def getDefaultLevel: HighlightDisplayLevel = HighlightDisplayLevel.ERROR

  override def getDisplayName: String = "Error highlighting for Scala"

  override def buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor = {
    new PsiElementVisitor {
      override def visitElement(element: PsiElement) {
        if (element.getContainingFile == null || element.getContainingFile.getName != "AbstractTestRunConfiguration.scala") return
        val annotator = new ScalaAnnotator {
          override def isAdvancedHighlightingEnabled(element: PsiElement): Boolean = true
        }
        val FakeAnnotation = new com.intellij.lang.annotation.Annotation(
          0, 0, HighlightSeverity.INFO, "message", "tooltip")
        val annotationHolder = new AnnotationHolder {
          def createInfoAnnotation(range: TextRange, message: String): Annotation = FakeAnnotation

          def createInfoAnnotation(node: ASTNode, message: String): Annotation = FakeAnnotation

          def createInfoAnnotation(elt: PsiElement, message: String): Annotation = FakeAnnotation

          def createInformationAnnotation(range: TextRange, message: String): Annotation = FakeAnnotation

          def createInformationAnnotation(node: ASTNode, message: String): Annotation = FakeAnnotation

          def createInformationAnnotation(elt: PsiElement, message: String): Annotation = FakeAnnotation

          def createWarningAnnotation(range: TextRange, message: String): Annotation = FakeAnnotation

          def createWarningAnnotation(node: ASTNode, message: String): Annotation = FakeAnnotation

          def createWarningAnnotation(elt: PsiElement, message: String): Annotation = FakeAnnotation

          def createErrorAnnotation(range: TextRange, message: String): Annotation = {
            holder.registerProblem(element, s"Error detected: $message", ProblemHighlightType.ERROR)
            FakeAnnotation
          }

          def createErrorAnnotation(node: ASTNode, message: String): Annotation = {
            holder.registerProblem(element, s"Error detected: $message", ProblemHighlightType.ERROR)
            FakeAnnotation
          }

          def createErrorAnnotation(elt: PsiElement, message: String): Annotation = {
            holder.registerProblem(element, s"Error detected: $message", ProblemHighlightType.ERROR)
            FakeAnnotation
          }

          def getCurrentAnnotationSession: AnnotationSession = {
            new AnnotationSession(element.getContainingFile)
          }

          def createWeakWarningAnnotation(p1: TextRange, p2: String): Annotation = FakeAnnotation

          def createWeakWarningAnnotation(p1: ASTNode, p2: String): Annotation = FakeAnnotation

          def createWeakWarningAnnotation(p1: PsiElement, p2: String): Annotation = FakeAnnotation
        }
        annotator.annotate(element, annotationHolder)
      }
    }
  }
}