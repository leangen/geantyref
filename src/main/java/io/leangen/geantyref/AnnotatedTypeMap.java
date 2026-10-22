/*
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or at <a href="http://www.apache.org/licenses/LICENSE-2">apache.org</a>.
 */

package io.leangen.geantyref;

import java.lang.reflect.AnnotatedType;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;

import static io.leangen.geantyref.GenericTypeReflector.toCanonical;

/**
 * A {@link Map} implementation keyed by {@link AnnotatedType}.
 * The standard maps do not usually suffice as {@link AnnotatedType} implements neither {@code equals} nor {@code hashCode}.
 * This implementation overcomes that limitation by transparently turning each {@link AnnotatedType} used as the key
 * into the canonical form using {@link GenericTypeReflector#toCanonical(AnnotatedType)}.
 * By default, {@code AnnotatedTypeMap} instances are backed by a {@link HashMap}, but any map can be used instead.
 * The guarantees of {@code AnnotatedTypeMap} are then the same as of the map it is backed by.
 *
 * @param <V> the type of mapped values
 *
 * @see AnnotatedTypeSet
 */
public class AnnotatedTypeMap<K extends AnnotatedType, V> implements Map<K, V> {

    private final Map<K, V> inner;

    /**
     * Constructs an instance backed by a {@link HashMap}
     */
    public AnnotatedTypeMap() {
        this(new HashMap<>());
    }
    
    /**
     * Constructs an instance backed by the provided map, keeping its guarantees
     *
     * @param inner A non-null map instance that will back the constructed {@code AnnotatedTypeMap}
     */
    @SuppressWarnings("WeakerAccess")
    public AnnotatedTypeMap(Map<K, V> inner) {
        Objects.requireNonNull(inner);
        if (!inner.isEmpty()) {
            throw new IllegalArgumentException("The provided map must be empty");
        }
        this.inner = inner;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int size() {
        return inner.size();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isEmpty() {
        return inner.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsKey(Object key) {
        return key instanceof AnnotatedType && inner.containsKey(toCanonical((AnnotatedType) key));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean containsValue(Object value) {
        return inner.containsValue(value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V get(Object key) {
        return key instanceof AnnotatedType ? inner.get(toCanonical((AnnotatedType) key)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V put(K key, V value) {
        return inner.put(toCanonical(key), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V remove(Object key) {
        return key instanceof AnnotatedType ? inner.remove(toCanonical((AnnotatedType) key)) : null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void putAll(Map<? extends K, ? extends V> m) {
        m.forEach(this::put);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clear() {
        inner.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<K> keySet() {
        return inner.keySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Collection<V> values() {
        return inner.values();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Set<Entry<K, V>> entrySet() {
        return inner.entrySet();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    @SuppressWarnings("EqualsWhichDoesntCheckParameterClass")
    public boolean equals(Object o) {
        return inner.equals(o);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return inner.hashCode();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V getOrDefault(Object key, V defaultValue) {
        return key instanceof AnnotatedType ? inner.getOrDefault(toCanonical((AnnotatedType) key), defaultValue) : defaultValue;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void forEach(BiConsumer<? super K, ? super V> action) {
        inner.forEach(action);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        inner.replaceAll(function);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V putIfAbsent(K key, V value) {
        return inner.putIfAbsent(toCanonical(key), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean remove(Object key, Object value) {
        return key instanceof AnnotatedType && inner.remove(toCanonical((AnnotatedType) key), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean replace(K key, V oldValue, V newValue) {
        return inner.replace(toCanonical(key), oldValue, newValue);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V replace(K key, V value) {
        return inner.replace(toCanonical(key), value);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V computeIfAbsent(K key, Function<? super K, ? extends V> mappingFunction) {
        return inner.computeIfAbsent(toCanonical(key), mappingFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V computeIfPresent(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.computeIfPresent(toCanonical(key), remappingFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V compute(K key, BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return inner.compute(toCanonical(key), remappingFunction);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public V merge(K key, V value, BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return inner.merge(toCanonical(key), value, remappingFunction);
    }
}
