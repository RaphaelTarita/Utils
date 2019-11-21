package com.tara.util.helper.nil;

public class NullResolverInstance {
    private static final NullResolver CUSTOM = new InternalNullResolverCustom();
    private static final NullResolver OPTIONAL = new InternalNullResolverOptional();
    private static final NullResolverImpl DEFAULT_IMPL = NullResolverImpl.CUSTOM;
    private static NullResolver instance;

    static {
        reset();
    }

    private NullResolverInstance() {
    }

    public static void setImpl(NullResolverImpl impl) {
        switch (impl) {
            case CUSTOM:
                instance = CUSTOM;
                break;
            case OPTIONAL:
                instance = OPTIONAL;
                break;
            default:
                throw new UnsupportedOperationException("Implementation \'" + impl.name() + "\' is not supported");
        }
    }

    public static NullResolver instance() {
        return instance;
    }

    public static void reset() {
        setImpl(DEFAULT_IMPL);
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof NullResolverInstance;
    }

    @Override
    public int hashCode() {
        return 0;
    }
}
