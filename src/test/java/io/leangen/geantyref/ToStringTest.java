/*
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or at <a href="http://www.apache.org/licenses/LICENSE-2">apache.org</a>.
 */

package io.leangen.geantyref;

import io.leangen.geantyref.Annotations.A3;
import io.leangen.geantyref.Annotations.A4;
import org.junit.Test;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ToStringTest {

    private static final A1 a1;
    private static final A2 a2;

    static {
        try {
            Map<String, Object> a1Params = new HashMap<>();
            a1Params.put("key", "Magic Key");
            a1Params.put("value", 123);
            a1 = TypeFactory.annotation(A1.class, a1Params);
            Map<String, Object> a2Params = new HashMap<>();
            a2Params.put("meta", "Meta Data");
            a2Params.put("target", a1);
            a2 = TypeFactory.annotation(A2.class, a2Params);
        } catch (AnnotationFormatException e) {
            throw new IllegalStateException(e);
        }
    }

    @Test
    public void parameterizedTypeTest() {
        AnnotatedType annString = TypeFactory.parameterizedAnnotatedClass(String.class, new Annotation[] {a2});
        AnnotatedType type = TypeFactory.parameterizedAnnotatedClass(List.class, new Annotation[] {a1}, annString);
        assertEquals("@io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) " +
                "java.util.List<@io.leangen.geantyref.ToStringTest$A2(meta=Meta Data, " +
                "target=@io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123)) java.lang.String>", type.toString());
    }

    @Test
    public void innerTypeTest() {
        Type inner = TypeFactory.parameterizedInnerClass(ToStringTest.class, Inner.class, String.class);
        AnnotatedType type = TypeFactory.parameterizedAnnotatedType((ParameterizedType) inner, new Annotation[] {a1}, new Annotation[] {a2});
        assertEquals("@io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) " +
                "io.leangen.geantyref.ToStringTest$Inner<@io.leangen.geantyref.ToStringTest$A2" +
                "(meta=Meta Data, target=@io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123)) java.lang.String>", type.toString());
    }

    @Test
    public void arrayTypeTest() {
        AnnotatedType componentType = TypeFactory.parameterizedAnnotatedClass(String.class, new Annotation[] {a1});
        AnnotatedType arrayType = TypeFactory.arrayOf(componentType, new Annotation[] {a1});
        assertEquals("@io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) " +
                "java.lang.String @io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) []", arrayType.toString());
    }

    @Test
    public void variableTypeTest() {
        TypeVariable<?> var = P.class.getTypeParameters()[0];
        AnnotatedType[] bounds = Arrays.stream(var.getAnnotatedBounds())
                .map(GenericTypeReflector::toCanonical)
                .toArray(AnnotatedType[]::new);
        AnnotatedType type = new AnnotatedTypeVariableImpl(new TypeVariableImpl<>(var, bounds), var.getAnnotations());
        assertTrue(type.toString().matches("@io.leangen.geantyref.Annotations[$.]A3\\(\\) T"));
    }

    @Test
    public void wildcardTypeTest() {
        WildcardType wild = (WildcardType) ((ParameterizedType)(new TypeToken<Class<? extends Number>>(){}.getType()))
                .getActualTypeArguments()[0];
        AnnotatedType[] upperBounds = new AnnotatedType[] {TypeFactory.parameterizedAnnotatedClass(Number.class, new Annotation[] {a1})};
        AnnotatedType type = new AnnotatedWildcardTypeImpl(wild, new Annotation[]{}, null, upperBounds);
        assertEquals("? extends @io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) java.lang.Number", type.toString());
        wild = (WildcardType) ((ParameterizedType)(new TypeToken<Class<? super Number>>(){}.getType()))
                .getActualTypeArguments()[0];
        AnnotatedType[] lowerBounds = new AnnotatedType[] {TypeFactory.parameterizedAnnotatedClass(Number.class, new Annotation[] {a1})};
        type = new AnnotatedWildcardTypeImpl(wild, new Annotation[]{}, lowerBounds, null);
        assertEquals("? super @io.leangen.geantyref.ToStringTest$A1(key=Magic Key, value=123) java.lang.Number", type.toString());
    }

    @SuppressWarnings({"InnerClassMayBeStatic", "unused"})
    private class Inner<T> {}

    @SuppressWarnings("unused")
    private @interface A1 {
        String key();
        int value();
    }

    @SuppressWarnings("unused")
    private @interface A2 {
        String meta();
        A1 target();
    }

    @SuppressWarnings({"InnerClassMayBeStatic", "unused"})
    private class P<@A3 T extends Number & @A4 Serializable> {
    }
}
