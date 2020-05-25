package com.dylanc.retrofit.helper.compiler

import com.dylanc.retrofit.helper.annotations.DefaultDomain
import com.dylanc.retrofit.helper.annotations.Domain
import com.squareup.javapoet.*
import java.io.IOException
import java.util.*
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Filer
import javax.annotation.processing.ProcessingEnvironment
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.Modifier
import javax.lang.model.element.TypeElement
import javax.lang.model.element.VariableElement
import kotlin.IllegalStateException

class DomainProcessor : AbstractProcessor() {
  private var filer: Filer? = null

  @Synchronized
  override fun init(processingEnvironment: ProcessingEnvironment) {
    super.init(processingEnvironment)
    filer = processingEnvironment.filer
  }

  override fun process(
    set: Set<TypeElement?>?,
    roundEnvironment: RoundEnvironment
  ): Boolean {
    val constructorBuilder = MethodSpec.constructorBuilder()
      .addModifiers(Modifier.PUBLIC)
    val hashMapType = ParameterizedTypeName
      .get(
        ClassName.get(HashMap::class.java),
        ClassName.get(String::class.java),
        ClassName.get(String::class.java)
      )
    var hasBinding = false
    val elements = roundEnvironment.getElementsAnnotatedWith(DefaultDomain::class.java)
    if (elements.size>1){
      throw IllegalStateException("@DefaultDomain annotations can only be used once")
    }
    for (element in elements) {
      val variableElement = element as VariableElement
      val className =
        ClassName.get(variableElement.enclosingElement.asType()).toString()
      val fieldName = variableElement.simpleName.toString()
      checkVariableValidClass(variableElement)
      constructorBuilder.addStatement("baseUrl = \$N.\$L", className, fieldName)
      hasBinding = true
    }
    constructorBuilder.addStatement("domains = new \$T()", hashMapType)
    for (element in roundEnvironment.getElementsAnnotatedWith(Domain::class.java)) {
      val variableElement = element as VariableElement
      val className =
        ClassName.get(variableElement.enclosingElement.asType()).toString()
      val fieldName = variableElement.simpleName.toString()
      val domain = variableElement.getAnnotation(Domain::class.java)
      checkVariableValidClass(variableElement)
      constructorBuilder.addStatement(
        "domains.put(\$S,\$N.\$L)",
        domain.value,
        className,
        fieldName
      )
    }
    val typeSpec =
      TypeSpec.classBuilder(ClassName.get("com.dylanc.retrofit.helper", "DomainConfig"))
        .addField(FieldSpec.builder(String::class.java, "baseUrl", Modifier.PUBLIC).build())
        .addField(FieldSpec.builder(hashMapType, "domains", Modifier.PUBLIC).build())
        .addMethod(constructorBuilder.build())
        .build()
    if (hasBinding) {
      try {
        JavaFile.builder("com.dylanc.retrofit.helper", typeSpec)
          .build()
          .writeTo(filer)
      } catch (e: IOException) {
        e.printStackTrace()
      }
    }
    return false
  }

  private fun checkVariableValidClass(element: VariableElement) {
    if (!element.modifiers.contains(Modifier.PUBLIC)) {
      throw IllegalStateException(
        String.format("The variable %s is not public", element.simpleName)
      )
    }
    if (!element.modifiers.contains(Modifier.STATIC)) {
      throw IllegalStateException(
        String.format("The variable %s is not static", element.simpleName)
      )
    }
  }

  override fun getSupportedAnnotationTypes(): Set<String> {
    return setOf(Domain::class.java.canonicalName, DefaultDomain::class.java.canonicalName)
  }

  override fun getSupportedSourceVersion(): SourceVersion {
    return SourceVersion.RELEASE_8
  }
}