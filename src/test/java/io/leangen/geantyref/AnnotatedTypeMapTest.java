/*
 * License: Apache License, Version 2.0
 * See the LICENSE file in the root directory or at <a href="http://www.apache.org/licenses/LICENSE-2">apache.org</a>.
 */

package io.leangen.geantyref;

import org.junit.Test;

import java.lang.reflect.AnnotatedType;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AnnotatedTypeMapTest {
    @Test
    public void putAllAcceptsNullValues() {
        AnnotatedType stringType = GenericTypeReflector.annotate(String.class);
        AnnotatedType integerType = GenericTypeReflector.annotate(Integer.class);
        Map<AnnotatedType, String> source = new HashMap<>();
        source.put(stringType, null);
        AnnotatedTypeMap<AnnotatedType, String> target = new AnnotatedTypeMap<>();
        target.put(integerType, "existing");

        target.putAll(source);

        assertTrue(target.containsKey(stringType));
        assertNull(target.get(stringType));
        assertEquals("existing", target.get(integerType));
        assertEquals(2, target.size());
    }

    @Test
    public void putAllAcceptsKeysWithTheSameCanonicalForm() {
        AnnotatedType first = GenericTypeReflector.annotate(String.class);
        AnnotatedType second = GenericTypeReflector.annotate(String.class);
        Map<AnnotatedType, String> source = new IdentityHashMap<>();
        source.put(first, "first");
        source.put(second, "second");
        assertEquals(2, source.size());
        AnnotatedTypeMap<AnnotatedType, String> expected = new AnnotatedTypeMap<>();
        source.forEach(expected::put);
        AnnotatedTypeMap<AnnotatedType, String> target = new AnnotatedTypeMap<>();

        target.putAll(source);

        assertEquals(1, target.size());
        assertEquals(expected.get(first), target.get(first));
    }
}
