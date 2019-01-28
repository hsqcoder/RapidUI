package pers.like.framework.main.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
public class ObjectUtils {

    public static <T> int compare(T a, T b, Comparator<? super T> c) {
        if (a == b) {
            return 0;
        }
        return c.compare(a, b);
    }

    public static boolean deepEquals(Object a, Object b) {
        if (a == null || b == null) {
            return a == b;
        } else if ((a instanceof Object[]) && (b instanceof Object[])) {
            return Arrays.deepEquals((Object[]) a, (Object[]) b);
        } else {
            if ((a instanceof boolean[]) && (b instanceof boolean[])) {
                return Arrays.equals((boolean[]) a, (boolean[]) b);
            }
            if ((a instanceof byte[]) && (b instanceof byte[])) {
                return Arrays.equals((byte[]) a, (byte[]) b);
            }
            if ((a instanceof char[]) && (b instanceof char[])) {
                return Arrays.equals((char[]) a, (char[]) b);
            }
            if ((a instanceof double[]) && (b instanceof double[])) {
                return Arrays.equals((double[]) a, (double[]) b);
            }
            if ((a instanceof float[]) && (b instanceof float[])) {
                return Arrays.equals((float[]) a, (float[]) b);
            }
            if ((a instanceof int[]) && (b instanceof int[])) {
                return Arrays.equals((int[]) a, (int[]) b);
            }
            if ((a instanceof long[]) && (b instanceof long[])) {
                return Arrays.equals((long[]) a, (long[]) b);
            }
            if ((a instanceof short[]) && (b instanceof short[])) {
                return Arrays.equals((short[]) a, (short[]) b);
            }
            return a.equals(b);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> List<T> deepCopy(List<T> src) {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream out;
        try {
            out = new ObjectOutputStream(byteOut);
            out.writeObject(src);
            ByteArrayInputStream byteIn = new ByteArrayInputStream(byteOut.toByteArray());
            ObjectInputStream in = new ObjectInputStream(byteIn);
            return (List<T>) in.readObject();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    public static boolean isEmpty(Object obj) {
        if (obj == null) {
            return true;
        } else if (obj.getClass().isArray()) {
            return Array.getLength(obj) == 0;
        } else if (obj instanceof CharSequence) {
            return ((CharSequence) obj).length() == 0;
        } else if (obj instanceof Collection) {
            return ((Collection) obj).isEmpty();
        } else {
            return obj instanceof Map && ((Map) obj).isEmpty();
        }
    }

    public static boolean nullSafeEquals(Object o1, Object o2) {
        if (o1 == o2) {
            return true;
        } else if (o1 != null && o2 != null) {
            if (o1.equals(o2)) {
                return true;
            } else {
                return (o1.getClass().isArray() && o2.getClass().isArray()) && arrayEquals(o1, o2);
            }
        } else {
            return false;
        }
    }

    private static boolean arrayEquals(Object o1, Object o2) {
        if (o1 instanceof Object[] && o2 instanceof Object[]) {
            return Arrays.equals((Object[]) o1, (Object[]) o2);
        } else if (o1 instanceof boolean[] && o2 instanceof boolean[]) {
            return Arrays.equals((boolean[]) o1, (boolean[]) o2);
        } else if (o1 instanceof byte[] && o2 instanceof byte[]) {
            return Arrays.equals((byte[]) o1, (byte[]) o2);
        } else if (o1 instanceof char[] && o2 instanceof char[]) {
            return Arrays.equals((char[]) o1, (char[]) o2);
        } else if (o1 instanceof double[] && o2 instanceof double[]) {
            return Arrays.equals((double[]) o1, (double[]) o2);
        } else if (o1 instanceof float[] && o2 instanceof float[]) {
            return Arrays.equals((float[]) o1, (float[]) o2);
        } else if (o1 instanceof int[] && o2 instanceof int[]) {
            return Arrays.equals((int[]) o1, (int[]) o2);
        } else if (o1 instanceof long[] && o2 instanceof long[]) {
            return Arrays.equals((long[]) o1, (long[]) o2);
        } else {
            return (o1 instanceof short[] && o2 instanceof short[]) && Arrays.equals((short[]) o1, (short[]) o2);
        }
    }

    public static int hash(Object... values) {
        return Arrays.hashCode(values);
    }

    public static int hashCode(Object o) {
        return o == null ? 0 : o.hashCode();
    }

    public static <T> T requireNonNull(T o) {
        if (o != null) {
            return o;
        }
        throw new NullPointerException();
    }

    public static <T> T requireNonNull(T o, String message) {
        if (o != null) {
            return o;
        }
        throw new NullPointerException(message);
    }

    public static String toString(Object o) {
        return o == null ? "null" : o.toString();
    }

    public static String toString(Object o, String nullString) {
        return o == null ? nullString : o.toString();
    }

    public static Object getField(Object object, String fieldName) {
        try {
            Field field = object.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(object);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
