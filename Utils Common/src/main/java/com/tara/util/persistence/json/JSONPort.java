package com.tara.util.persistence.json;

import com.tara.util.annotation.Persistable;
import com.tara.util.helper.lambda.ThrowingFunction;
import com.tara.util.persistence.field.JGPAEntity;
import com.tara.util.tools.Charsets;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class JSONPort {
    private static final String K = "\"([a-zA-Z_][a-zA-Z_0-9]*)\":";
    private static final String V_PRIMITIVE = "[^,\\{\\}]*";
    private static final String V_ARRAY = "\\[.*\\]";
    private static final String V_OBJECT = "\\{.*\\}";
    private static final Pattern V = Pattern.compile("(" + V_OBJECT + "|" + V_ARRAY + "|" + V_PRIMITIVE + ")");
    private static final Pattern KV = Pattern.compile("\\{?" + K + V.pattern() + "[,\\}]");

    private JSONPort() {
    }

    private static void jsonStart(StringBuilder sb) {
        sb.append(JSONSymbol.BRACE_OPEN);
    }

    private static void jsonEnd(StringBuilder sb) {
        sb.append(JSONSymbol.BRACE_CLOSE);
    }

    private static void writeKey(StringBuilder sb, String key) {
        sb.append(JSONSymbol.QUOT_MARK)
          .append(key)
          .append(JSONSymbol.QUOT_MARK)
          .append(JSONSymbol.COLON);
    }

    private static void writeObject(StringBuilder sb, Object value) {
        if (value instanceof Map) {
            toJSON0(sb, castMap(value));
        } else if (value == null) {
            writeNull(sb);
        } else if (value instanceof Object[]) {
            writeArray(sb, (Object[]) value);
        } else if (value instanceof Collection) {
            writeArray(sb, ((Collection<?>) value).toArray());
        } else {
            writeWithMarshaller(sb, value);
        }
    }

    private static void writeNull(StringBuilder sb) {
        sb.append(JSONSymbol.NULL);
    }

    private static void writeWithMarshaller(StringBuilder sb, Object obj) {
        boolean stringFlag = !(obj instanceof Number) && !(obj instanceof Boolean);
        if (stringFlag) {
            sb.append(JSONSymbol.QUOT_MARK);
        }
        sb.append(StringCodecRegistry.instance().marshal(obj));
        if (stringFlag) {
            sb.append(JSONSymbol.QUOT_MARK);
        }
    }

    private static void writeArray(StringBuilder sb, Object[] array) {
        sb.append(JSONSymbol.BRACKET_OPEN);
        boolean first = true;
        for (Object obj : array) {
            if (first) {
                first = false;
            } else {
                sb.append(JSONSymbol.COMMA);
            }
            writeObject(sb, obj);
        }
        sb.append(JSONSymbol.BRACKET_CLOSE);
    }

    private static void toJSON0(StringBuilder json, Map<String, Object> kvs) {
        jsonStart(json);
        boolean first = true;
        for (Map.Entry<String, Object> kv : kvs.entrySet()) {
            if (first) {
                first = false;
            } else {
                json.append(JSONSymbol.COMMA);
            }
            writeKey(json, kv.getKey());
            writeObject(json, kv.getValue());
        }
        jsonEnd(json);
    }

    public static <VO> String toJSON(JGPAEntity<VO> boundGateway) {
        Map<String, Object> valueMap = boundGateway.getValueMap();
        StringBuilder json = new StringBuilder();
        toJSON0(json, valueMap);
        return json.toString();
    }

    private static Object getPrimitive(Class<?> expected, String v) throws FormatException {
        if (v.contains("\"")) {
            v = v.replace("\"", "");
        }
        try {
            return StringCodecRegistry.instance().unmarshal(v, expected);
        } catch (ParseException ex) {
            throw new FormatException("ParseException occurred during primitive parsing", ex);
        }
    }

    private static Object[] getUnmarshalledArray(ThrowingFunction<String, Object, FormatException> producer, List<String> array) throws FormatException {
        Object[] valuesArray = new Object[array.size()];
        int i = 0;
        for (String v : array) {
            valuesArray[i++] = producer.apply(v);
        }
        return valuesArray;
    }

    private static Collection<Object> getUnmarshalledCollection(ThrowingFunction<String, Object, FormatException> producer, List<String> array) throws FormatException {
        Collection<Object> valuesCollection = new ArrayList<>(array.size());
        for (String v : array) {
            valuesCollection.add(producer.apply(v));
        }
        return valuesCollection;
    }

    private static Object getJSONArray(Type expected, String value) throws FormatException {
        List<String> array = new ArrayList<>();
        value = value.replace("[", "");
        value = value.replace("]", "");
        Matcher vm = V.matcher(value);
        while (vm.find()) {
            String val = vm.group(1);
            if (!("").equals(val)) {
                array.add(val);
            }
        }

        boolean isArray;
        Class<?> expectedClass = null;
        if (expected instanceof Class) {
            expectedClass = (Class<?>) expected;
            if (!expectedClass.isArray()) {
                throw new FormatException("Expected JSON Array type, but got " + expectedClass.toString());
            }
            expectedClass = expectedClass.getComponentType();
            isArray = true;
        } else {
            expected = ((ParameterizedType) expected).getActualTypeArguments()[0];
            if (expected instanceof Class) {
                expectedClass = (Class<?>) expected;
            }
            isArray = false;
        }

        ThrowingFunction<String, Object, FormatException> producer;
        if (expectedClass != null && expectedClass.isAnnotationPresent(Persistable.class)) {
            final Class<?> finalExpected = expectedClass;
            producer = v -> {
                JGPAEntity<Object> newGateway = new JGPAEntity<>(finalExpected);
                newGateway.bindEmpty();
                return fromJSON0(newGateway, v);
            };
        } else if (expectedClass != null && expectedClass.isArray()) {
            final Class<?> finalExpected = expectedClass.getComponentType();
            producer = v -> getJSONArray(finalExpected, v);
        } else if (!(expected instanceof Class)) {
            final Type finalExpected = expected;
            producer = v -> getJSONArray(finalExpected, v);
        } else {
            Class<?> finalExpected = expectedClass;
            producer = v -> getPrimitive(finalExpected, v);
        }
        if (isArray) {
            return getUnmarshalledArray(producer, array);
        } else {
            return getUnmarshalledCollection(producer, array);
        }
    }

    private static <VO> Object fromJSON0(JGPAEntity<VO> boundGateway, String json) throws FormatException {
        Matcher m = KV.matcher(json);
        Object assign;
        while (m.find()) {
            String key = m.group(1);
            String value = m.group(2);
            if (!boundGateway.hasField(key)) {
                throw new FormatException("Unmapped Field found in JSON: " + key);
            }
            if (value.matches(V_PRIMITIVE)) {
                assign = getPrimitive(boundGateway.getField(key).getType(), value);
            } else if (value.matches(V_ARRAY)) {
                Type expected = boundGateway.getField(key).getType();
                if (!((Class<?>) expected).isArray()) {
                    expected = boundGateway.getField(key).getter().getGenericReturnType();
                }
                assign = getJSONArray(expected, value);
            } else if (value.matches(V_OBJECT)) {
                assign = fromJSON0(boundGateway.getBoundCompositeField(key), value);
            } else {
                throw new FormatException("Invalid JSON input");
            }
            boundGateway.set(key, assign);
        }
        return boundGateway.getVO();
    }

    public static <VO> void fromJSON(JGPAEntity<VO> boundGateway, String json) throws FormatException {
        json = sanitize(json);
        fromJSON0(boundGateway, json.substring(json.indexOf("{"), json.lastIndexOf("}") + 1));
    }

    @SuppressWarnings("unchecked")
    private static Map<String, Object> castMap(Map<?, ?> map) {
        return (Map<String, Object>) map;
    }

    private static Map<String, Object> castMap(Object map) {
        return castMap((Map<?, ?>) map);
    }

    private static String sanitize(String json) {
        char c;
        StringBuilder sanitized = new StringBuilder(json.length());
        boolean quot = false;
        for (int i = 0; i < json.length(); i++) {
            c = json.charAt(i);
            if (c == '\"') {
                quot = !quot;
            }
            if (quot || Charsets.JSON_UNQUOT.conforms(c)) {
                sanitized.append(c);
            }
        }
        return sanitized.toString();
    }
}
