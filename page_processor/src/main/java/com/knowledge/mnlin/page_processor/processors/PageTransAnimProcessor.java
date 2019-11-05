package com.knowledge.mnlin.page_processor.processors;

import com.google.auto.service.AutoService;
import com.knowledge.mnlin.page_annotation.annotations.InjectPageTransAnim;
import com.knowledge.mnlin.page_annotation.consts.PageGenClassConst;
import com.knowledge.mnlin.page_annotation.consts.PageGenPackageConst;
import com.knowledge.mnlin.page_processor.consts.ProcessorConst;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;

import java.util.Iterator;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic;

import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.COMMON_CLASS_PAGE;
import static com.knowledge.mnlin.page_processor.consts.ProcessorConst.PAGE_MODULE_WARNING_TIP;

@AutoService(Processor.class)
@SupportedAnnotationTypes(ProcessorConst.PATH_ANNOTATION_PAGE_INJECT_TRANS_ANIM)
@SupportedSourceVersion(value = SourceVersion.RELEASE_8)
public class PageTransAnimProcessor extends AbstractProcessor {
    private static final String TAG = "PageTransAnimProcessor";

    private Messager messager;

    private Elements elementUtils;

    private Types typeUtils;

    private Filer filer;

    @Override
    public void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        messager = processingEnvironment.getMessager();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();
        filer = processingEnvironment.getFiler();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        Set<? extends Element> elementsAnnotatedWith =
                roundEnvironment.getElementsAnnotatedWith(InjectPageTransAnim.class);
        Iterator<? extends Element> iterator = elementsAnnotatedWith.iterator();
        TypeElement typeElement;

        messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " : BEGIN-PROCESS ");

        // will create more than one file
        while (iterator.hasNext()) {
            typeElement = (TypeElement) iterator.next();

            // page-annotation-package
            // gen-target-package
            String interface_page = COMMON_CLASS_PAGE.reflectionName();
            String package_target;

            try {
                if (!typeUtils.isSubtype(typeElement.asType(),
                        elementUtils.getTypeElement(interface_page).asType())) {
                    messager.printMessage(Diagnostic.Kind.ERROR, ">>>> " + TAG
                            + " : Only 'page' type class can inject '@InjectPageTransAnim' annotation "
                            + elementUtils.getTypeElement(interface_page).asType());
                } else {
                    // count package-path
                    PackageElement pkgElement = (PackageElement) typeElement.getEnclosingElement();
                    if (pkgElement.isUnnamed()) {
                        package_target = "";
                    } else {
                        package_target = pkgElement.getQualifiedName().toString();
                    }

                    // count annotations-string
                    InjectPageTransAnim annotation = typeElement.getAnnotation(InjectPageTransAnim.class);
                    String[] animations = annotation.animations();

                    messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " )");

                    JavaFile.builder(package_target, createClass(typeElement, animations))
                            .addFileComment(PAGE_MODULE_WARNING_TIP)
                            .build()
                            .writeTo(filer);
                }
            } catch (Exception e) {
                messager.printMessage(Diagnostic.Kind.WARNING, ">>>> " + TAG + " :  " + e.toString());
                break;
            }
        }

        return true;
    }

    private TypeSpec createClass(TypeElement typeElement, String[] animations) {
        String className = typeElement.getSimpleName() + PageGenClassConst.CLASS_PAGE_GEN_TRANS_ANIM_SUFFIX;

        return TypeSpec.classBuilder(className)
                .addJavadoc(PAGE_MODULE_WARNING_TIP)
                .addMethod(createMethod(animations))
                .addModifiers(Modifier.FINAL)
                .build();
    }

    private MethodSpec createMethod(String[] animations) {
        // create return type
        TypeElement type =
                elementUtils.getTypeElement(ProcessorConst.COMMON_CLASS_PAGE_TRANS_ANIMATION.reflectionName());
        TypeName returnType = TypeName.get(type.asType());

        // create annotations
        StringBuilder builder = new StringBuilder();
        for (String animation : animations) {
            builder.append("\"")
                    .append(animation)
                    .append("\",");
        }
        if (builder.length() != 0) {
            builder.deleteCharAt(builder.length() - 1);
        }

        return MethodSpec.methodBuilder(ProcessorConst.PAGE_TRANS_ANIM_METHOD)
                .addModifiers(Modifier.STATIC)
                .returns(returnType)
                .addAnnotation(ProcessorConst.COMMON_ANNOTATION_NULLABLE)
                .addComment("Annotation class must have a default 'public'' constructor")
                .addStatement("return $T.getInstance().createPageTransAnimation($L)",
                        ClassName.get(PageGenPackageConst.PAGE_MODULE_MAIN_PACKAGE + ".factory", "PageTransAnimFactory"),
                        builder.toString())
                .build();
    }
}