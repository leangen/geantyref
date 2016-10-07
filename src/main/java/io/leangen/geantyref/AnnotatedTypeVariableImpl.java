package io.leangen.geantyref;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.AnnotatedTypeVariable;
import java.lang.reflect.TypeVariable;
import java.util.Arrays;

import static io.leangen.geantyref.GenericTypeReflector.typeArraysEqual;

class AnnotatedTypeVariableImpl extends AnnotatedTypeImpl implements AnnotatedTypeVariable {

    private AnnotatedType[] annotatedBounds;

    public AnnotatedTypeVariableImpl(TypeVariable<?> type) {
        super(type, type.getAnnotations());
        this.annotatedBounds = type.getAnnotatedBounds();
    }

    public AnnotatedTypeVariableImpl(TypeVariable<?> type, Annotation[] annotations) {
        super(type, annotations);
        this.annotatedBounds = type.getAnnotatedBounds();
    }

    @Override
    public AnnotatedType[] getAnnotatedBounds() {
        return annotatedBounds;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof AnnotatedTypeVariable) || !super.equals(other)) {
            return false;
        }
        return typeArraysEqual(annotatedBounds, ((AnnotatedTypeVariable) other).getAnnotatedBounds());
    }

    @Override
    public int hashCode() {
        return super.hashCode() + Arrays.hashCode(annotatedBounds);
    }
}