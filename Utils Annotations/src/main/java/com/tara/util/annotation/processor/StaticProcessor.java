package com.tara.util.annotation.processor;

import com.google.auto.service.AutoService;
import com.tara.util.annotation.Static;
import lombok.AllArgsConstructor;
import lombok.Data;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@SupportedAnnotationTypes("com.tara.util.annotation.Static")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
@AutoService(Processor.class)
public class StaticProcessor extends AbstractProcessor {
    private static final char SPACE = ' ';
    private static final String NEWLINE = System.lineSeparator();
    public static final String OBJ_NAME = "obj";

    @Data
    @AllArgsConstructor
    private static class AnnotationInfo {
        private final String className;
        private final String packageName;
        private final boolean nullChecks;
    }

    @Data
    @AllArgsConstructor
    private static class MethodInfo {
        private final Element method;
        private final String originalFullname;
        private final String newClassname;
        private final String newPackageName;
        private final boolean nullChecks;

        public String getOriginalClassname() {
            int delim = Math.max(originalFullname.lastIndexOf('.'), 0);
            return originalFullname.substring(delim + 1);
        }

        public String getOriginalPackagename() {
            int delim = Math.max(originalFullname.lastIndexOf('.'), 0);
            return originalFullname.substring(0, delim + 1);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Map<Element, AnnotationInfo> toImplement = new HashMap<>();
        for (TypeElement annotation : annotations) {
            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
            for (Element elem : annotatedElements) {
                if (!elem.getModifiers().contains(Modifier.PUBLIC) || elem.getModifiers().contains(Modifier.STATIC)) {
                    processingEnv.getMessager().printMessage(Kind.ERROR, "@Static only allowed for public non-static methods");
                } else {
                    toImplement.put(elem, new AnnotationInfo(
                            elem.getAnnotation(Static.class).classname(),
                            elem.getAnnotation(Static.class).packagename(),
                            elem.getAnnotation(Static.class).nullchecks()
                    ));
                }
            }
        }
        try {
            generate(toImplement);
            return true;
        } catch (IOException ex) {
            processingEnv.getMessager().printMessage(Kind.ERROR, "@Static annotation processing encountered IOException: " + ex.toString());
            return false;
        }
    }

    private void generate(Map<Element, AnnotationInfo> toImplement) throws IOException {
        Map<String, List<MethodInfo>> files = new HashMap<>();
        for (Map.Entry<Element, AnnotationInfo> entry : toImplement.entrySet()) {
            String fullName = ((TypeElement) entry.getKey().getEnclosingElement()).getQualifiedName().toString();
            int delim = Math.max(fullName.lastIndexOf(Operator.PERIOD.toString()), 0);
            MethodInfo info = new MethodInfo(
                    entry.getKey(),
                    fullName,
                    resolveClassName(fullName, entry.getValue().getClassName(), delim),
                    resolvePackageName(fullName, entry.getValue().getPackageName(), delim),
                    entry.getValue().isNullChecks()
            );
            String newName = info.getNewPackageName() + Operator.PERIOD + info.getNewClassname();
            if (files.containsKey(newName)) {
                files.get(newName).add(info);
            } else {
                files.put(newName, new ArrayList<>(Collections.singleton(info)));
            }
        }

        for (Map.Entry<String, List<MethodInfo>> entry : files.entrySet()) {
            writeFile(entry.getKey(), entry.getValue());
        }
    }

    private String resolveClassName(String orig, String custom, int delim) {
        return custom.isEmpty()
                ? orig.substring(delim + 1) + "Statics"
                : custom;
    }

    private String resolvePackageName(String orig, String custom, int delim) {
        if (custom.isEmpty()) {
            return orig.substring(0, delim)
                    + (
                    orig.contains(Operator.PERIOD.toString())
                            ? Operator.PERIOD.toString()
                            : ""
            )
                    + "staticimpl";
        } else {
            return custom;
        }
    }

    private void writeFile(String on, List<MethodInfo> infos) throws IOException {
        JavaFileObject file = processingEnv.getFiler().createSourceFile(on);
        PrintWriter out = new PrintWriter(file.openWriter());
        header(out, infos.get(0));
        for (MethodInfo info : infos) {
            method(out, info);
        }
        out.println(Operator.BRACE_CLOSE);
        out.close();
    }

    private void header(PrintWriter out, MethodInfo info) {
        if (!info.getNewPackageName().isEmpty()) {
            // package [new package name]; NOSONAR
            out.println(Keyword.PACKAGE.toString() + SPACE + info.getNewPackageName() + Operator.SEMICOLON.toString());
        }
        // import [old class full name]; NOSONAR
        out.println(Keyword.IMPORT.toString() + SPACE + info.getOriginalFullname() + Operator.SEMICOLON.toString());

        // public class [new class name] { NOSONAR
        out.println(Keyword.PUBLIC.toString() + SPACE + Keyword.CLASS.toString() + SPACE + info.getNewClassname() + SPACE + Operator.BRACE_OPEN.toString());
    }

    private void method(PrintWriter out, MethodInfo info) {
        for (Modifier m : info.getMethod().getModifiers()) {
            out.print(m.toString() + ' ');
        }
        List<String> parameterNames = new ArrayList<>();
        String returnType = ((ExecutableType) info.getMethod().asType()).getReturnType().toString();

        out.println(
                Keyword.STATIC.toString() + SPACE
                        + returnType + SPACE
                        + info.getMethod().getSimpleName().toString()
                        + Operator.PARENTHESIS_OPEN.toString()
                        + getParameters(info.getOriginalClassname(), ((ExecutableType) info.getMethod().asType()).getParameterTypes(), parameterNames)
                        + Operator.PARENTHESIS_CLOSE.toString() + SPACE
                        + Operator.BRACE_OPEN
        );

        if (returnType.equals(Keyword.VOID.toString())) {
            out.println(bodyForVoid(info, parameterNames));
        } else {
            out.println(bodyForNonvoid(info, parameterNames));
        }

        out.println(Operator.BRACE_CLOSE);
    }

    private String bodyForVoid(MethodInfo info, List<String> params) {
        StringBuilder res = new StringBuilder();
        if (info.isNullChecks()) {
            res.append(Keyword.IF)
               .append(SPACE)
               .append(Operator.PARENTHESIS_OPEN)
               .append(OBJ_NAME)
               .append(SPACE)
               .append(Operator.INEQUALITY)
               .append(SPACE)
               .append(Keyword.NULL)
               .append(Operator.PARENTHESIS_CLOSE)
               .append(SPACE)
               .append(Operator.BRACE_OPEN)
               .append(NEWLINE);
        }
        res.append(OBJ_NAME)
           .append(call(info.getMethod().getSimpleName().toString(), params))
           .append(Operator.SEMICOLON);

        if (info.isNullChecks()) {
            res.append(NEWLINE)
               .append(Operator.BRACE_CLOSE);
        }
        return res.toString();
    }

    private String bodyForNonvoid(MethodInfo info, List<String> params) {
        StringBuilder res = new StringBuilder(Keyword.RETURN.toString());
        res.append(SPACE)
           .append(OBJ_NAME);

        if (info.isNullChecks()) {
            res.append(SPACE)
               .append(Operator.INEQUALITY)
               .append(SPACE)
               .append(Keyword.NULL)
               .append(NEWLINE)
               .append(Operator.TERNARY_FIRST)
               .append(SPACE)
               .append(OBJ_NAME);
        }

        res.append(call(info.getMethod().getSimpleName().toString(), params));

        if (info.isNullChecks()) {
            res.append(NEWLINE)
               .append(Operator.TERNARY_SECOND)
               .append(SPACE)
               .append(Keyword.NULL);
        }

        res.append(Operator.SEMICOLON);
        return res.toString();
    }

    private String call(String method, List<String> params) {
        StringBuilder res = new StringBuilder(Operator.PERIOD.toString());
        res.append(method)
           .append(Operator.PARENTHESIS_OPEN);

        boolean first = true;
        for (String param : params) {
            if (first) {
                first = false;
            } else {
                res.append(Operator.COMMA)
                   .append(SPACE);
            }
            res.append(param);
        }

        res.append(Operator.PARENTHESIS_CLOSE);
        return res.toString();
    }

    private String getParameters(String className, List<? extends TypeMirror> parameters, List<String> parameterNames) {
        StringBuilder res = new StringBuilder(className);
        res.append(SPACE)
           .append(OBJ_NAME);

        for (TypeMirror type : parameters) {
            res.append(Operator.COMMA)
               .append(SPACE)
               .append(type.toString())
               .append(SPACE);

            String name = ((DeclaredType) type).asElement().getSimpleName().toString().toLowerCase();
            int counter = 0;
            while (parameterNames.contains(name + counter)) {
                counter++;
            }
            name += counter;
            parameterNames.add(name);

            res.append(name);
        }

        return res.toString();
    }
}
