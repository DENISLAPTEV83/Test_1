package Denis;

import sun.misc.Unsafe;
import  java.util.Arrays;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.IdentityHashMap;

public class CopyUtils {

    public static <X> X deepCopy(X origin) {
        Copy copy = new Copy();
        try {
            return copy.clone(origin);
        } catch (InstantiationException e) {
            throw new IllegalStateException(e);
        }
    }

    private static final class Copy {
        private static final Unsafe UNSAFE;

        private final IdentityHashMap<Object, Object> map = new IdentityHashMap<>();

        static {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                UNSAFE = (Unsafe) unsafeField.get(null);
            } catch (Exception e) {
                throw new ExceptionInInitializerError(e);
            }
        }

        <Y> Y clone(Y origin) throws InstantiationException {
            Y newCopy = (Y) UNSAFE.allocateInstance(origin.getClass());
            map.put(origin, newCopy);
            cloneTo(origin, newCopy);
            return newCopy;
        }

        <Z> void cloneTo(Z origin, Z newCopy) throws InstantiationException {
            for (Field f : origin.getClass().getDeclaredFields()) {
                if (Modifier.isStatic(f.getModifiers()))
                    continue;
                long offset = UNSAFE.objectFieldOffset(f);
                if (f.getType() == boolean.class) {
                    UNSAFE.putBoolean(newCopy, offset, UNSAFE.getBoolean(origin, offset));
                } else if (f.getType() == byte.class) {
                    UNSAFE.putByte(newCopy, offset, UNSAFE.getByte(origin, offset));
                } else if (f.getType() == char.class) {
                    UNSAFE.putChar(newCopy, offset, UNSAFE.getChar(origin, offset));
                } else if (f.getType() == int.class) {
                    UNSAFE.putInt(newCopy, offset, UNSAFE.getInt(origin, offset));
                } else if (f.getType() == float.class) {
                    UNSAFE.putFloat(newCopy, offset, UNSAFE.getFloat(origin, offset));
                } else if (f.getType() == double.class) {
                    UNSAFE.putDouble(newCopy, offset, UNSAFE.getDouble(origin, offset));
                } else if (f.getType() == long.class) {
                    UNSAFE.putLong(newCopy, offset, UNSAFE.getLong(origin, offset));
                } else {
                    Object oldField = UNSAFE.getObject(origin, offset);
                    if (oldField == null)
                        continue;
                    Object newField = map.get(oldField);
                    if (newField == null) {
                        Class<?> fClass = oldField.getClass();
                        if (fClass.isArray()) {
                            if (fClass == boolean[].class) {
                                boolean[] oldArr = (boolean[]) oldField;
                                boolean[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == byte[].class) {
                                byte[] oldArr = (byte[]) oldField;
                                byte[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == char[].class) {
                                char[] oldArr = (char[]) oldField;
                                char[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == int[].class) {
                                int[] oldArr = (int[]) oldField;
                                int[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == float[].class) {
                                float[] oldArr = (float[]) oldField;
                                float[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == double[].class) {
                                double[] oldArr = (double[]) oldField;
                                double[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else if (fClass == long[].class) {
                                long[] oldArr = (long[]) oldField;
                                long[] newArr = Arrays.copyOf(oldArr, oldArr.length);
                                newField = newArr;
                                map.put(oldField, newArr);
                            } else {
                                Object[] oldArr = (Object[]) oldField;
                                Object[] newArr = new Object[oldArr.length];
                                for (int i = 0; i < oldArr.length; i++) {
                                    Object oldItem = oldArr[i];
                                    if (oldArr[i] == null)
                                        continue;
                                    Object newItem = map.get(oldItem);
                                    if (newItem == null) {
                                        newItem = UNSAFE.allocateInstance(oldItem.getClass());
                                        map.put(oldItem, newItem);
                                        cloneTo(oldItem, newItem);
                                    }
                                    newArr[i] = newItem;
                                }
                                newField = newArr;
                            }
                        } else {
                            newField = UNSAFE.allocateInstance(fClass);
                            map.put(oldField, newField);
                            cloneTo(oldField, newField);
                        }
                    }
                    UNSAFE.putObject(newCopy, offset, newField);
                }
            }
        }
    }
}