package com.tara.util.persistence.json;

import com.tara.util.helper.date.DateHelper;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StringCodecRegistry {
    private static final Class<MarshallerFallback> FALLBACK = MarshallerFallback.class;
    private static final StringCodecRegistry instance = new StringCodecRegistry();

    public static StringCodecRegistry instance() {
        return instance;
    }

    private Map<Class<?>, StringMarshaller<?>> marshallerRegistry;
    private Map<Class<?>, StringUnmarshaller<?>> unmarshallerRegistry;
    private DateFormat dateFormat;

    private StringCodecRegistry() {
        marshallerRegistry = new HashMap<>();
        unmarshallerRegistry = new HashMap<>();
        dateFormat = new SimpleDateFormat("yyyy.MM.dd");

        marshallerRegistry.put(String.class, StringMarshaller.identity());
        marshallerRegistry.put(Long.class, (Long l) -> Long.toString(l));
        marshallerRegistry.put(Integer.class, (Integer i) -> Integer.toString(i));
        marshallerRegistry.put(Short.class, (Short s) -> Short.toString(s));
        marshallerRegistry.put(Boolean.class, (Boolean b) -> Boolean.toString(b));
        marshallerRegistry.put(Date.class, (Date d) -> dateFormat.format(d));
        marshallerRegistry.put(LocalDate.class, (LocalDate ld) -> dateFormat.format(DateHelper.reconvLD(ld)));
        marshallerRegistry.put(LocalTime.class, (LocalTime lt) -> dateFormat.format(DateHelper.reconvLT(lt)));
        marshallerRegistry.put(LocalDateTime.class, (LocalDateTime ldt) -> dateFormat.format(DateHelper.reconvLDT(ldt)));
        marshallerRegistry.put(MarshallerFallback.class, MarshallerFallback.defaultMarshaller());

        unmarshallerRegistry.put(String.class, StringUnmarshaller.identity());
        unmarshallerRegistry.put(Long.class, Long::parseLong);
        unmarshallerRegistry.put(Integer.class, Integer::parseInt);
        unmarshallerRegistry.put(Short.class, Short::parseShort);
        unmarshallerRegistry.put(Boolean.class, Boolean::parseBoolean);
        unmarshallerRegistry.put(Date.class, s -> dateFormat.parse(s));
        unmarshallerRegistry.put(LocalDate.class, s -> DateHelper.convertLD(dateFormat.parse(s)));
        unmarshallerRegistry.put(LocalTime.class, s -> DateHelper.convertLT(dateFormat.parse(s)));
        unmarshallerRegistry.put(LocalDateTime.class, s -> DateHelper.convertLDT(dateFormat.parse(s)));
    }

    public <P> void registerMarshaller(Class<? extends P> clazz, StringMarshaller<P> marshaller) {
        marshallerRegistry.put(clazz, marshaller);
    }

    public <R> void registerUnmarshaller(Class<? extends R> clazz, StringUnmarshaller<R> unmarshaller) {
        unmarshallerRegistry.put(clazz, unmarshaller);
    }

    public void setDateFormat(DateFormat format) {
        dateFormat = format;
    }

    @SuppressWarnings("unchecked")
    public <P> StringMarshaller<P> getMarshaller(Class<P> clazz) {
        StringMarshaller<?> rawMarshaller = marshallerRegistry.get(clazz);
        if (rawMarshaller == null) {
            rawMarshaller = marshallerRegistry.get(FALLBACK);
        }
        return (StringMarshaller<P>) rawMarshaller;
    }

    @SuppressWarnings("unchecked")
    public <R> StringUnmarshaller<R> getUnmarshaller(Class<? extends R> clazz) {
        StringUnmarshaller<?> rawUnmarshaller = unmarshallerRegistry.get(clazz);
        if (rawUnmarshaller == null) {
            throw new MarshallerNotFoundException(clazz);
        }
        return s -> {
            Object r = rawUnmarshaller.unmarshal(s);
            if (clazz.isInstance(r)) {
                return (R) r;
            } else {
                throw new ParseException("Unmarshaller class mismatch. Required: " + clazz.toString() + ", Found: " + r.getClass().toString(), 0);
            }
        };
    }

    public <R> R unmarshal(String s, Class<? extends R> clazz) throws ParseException {
        return getUnmarshaller(clazz).unmarshal(s);
    }

    public <P> String marshal(P obj) {
        return getMarshaller(getClassOf(obj)).marshal(obj);
    }

    @SuppressWarnings("unchecked")
    private <P> Class<P> getClassOf(P obj) {
        return (Class<P>) obj.getClass();
    }
}
