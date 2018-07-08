package me.devnatan.pickupore.nbt;

import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;
import java.util.Set;
import java.util.Stack;

public class NBTReflectionUtil {

    private static final String version = Version.getVersionPlain();

    @SuppressWarnings("rawtypes")
    private static Class getCraftItemStack() {
        try {
            return Class.forName("org.bukkit.craftbukkit." + version + ".inventory.CraftItemStack");
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    @SuppressWarnings("rawtypes")
    protected static Class getNBTBase() {
        return ReflectionUtil.getNMSClass("NBTBase");
    }

    @SuppressWarnings("rawtypes")
    protected static Class getNBTTagCompound() {
        return ReflectionUtil.getNMSClass("NBTTagCompound");
    }

    public static Object getNewNBTTag() {
        try {
            return ReflectionUtil.getNMSClass("NBTTagCompound").newInstance();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }

        return null;
    }

    public static Object setNBTTag(Object NBTTag, Object NMSItem) {
        try {
            Method method;
            method = NMSItem.getClass().getMethod("setTag", NBTTag.getClass());
            method.invoke(NMSItem, NBTTag);
            return NMSItem;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object getNMSItemStack(ItemStack item) {
        @SuppressWarnings("rawtypes")
        Class clazz = getCraftItemStack();
        Method method;
        try {
            method = clazz.getMethod("asNMSCopy", ItemStack.class);
            Object answer = method.invoke(clazz, item);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public static ItemStack getBukkitItemStack(Object item) {
        @SuppressWarnings("rawtypes")
        Class clazz = getCraftItemStack();
        Method method;
        try {
            method = clazz.getMethod("asCraftMirror", item.getClass());
            Object answer = method.invoke(clazz, item);
            return (ItemStack) answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings({"unchecked"})
    public static Object getItemRootNBTTagCompound(Object nmsitem) {
        @SuppressWarnings("rawtypes")
        Class clazz = nmsitem.getClass();
        Method method;
        try {
            method = clazz.getMethod("getTag");
            Object answer = method.invoke(nmsitem);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Object getSubNBTTagCompound(Object compound, String name) {
        @SuppressWarnings("rawtypes")
        Class c = compound.getClass();
        Method method;
        try {
            method = c.getMethod("getCompound", String.class);
            Object answer = method.invoke(compound, name);
            return answer;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void addNBTTagCompound(NBTCompound comp, String name) {
        if (name == null) {
            remove(comp, name);
            return;
        }
        Object nbttag = comp.getCompound();
        if (nbttag == null) {
            nbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(nbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("set", String.class, getNBTBase());
            method.invoke(workingtag, name, getNBTTagCompound().newInstance());
            comp.setCompound(nbttag);
            return;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return;
    }

    public static Boolean valideCompound(NBTCompound comp) {
        Object root = comp.getCompound();
        if (root == null) {
            root = getNewNBTTag();
        }
        return (gettoCompount(root, comp)) != null;
    }

    public static Object gettoCompount(Object nbttag, NBTCompound comp) {
        Stack<String> structure = new Stack<>();
        while (comp.getParent() != null) {
            structure.add(comp.getName());
            comp = comp.getParent();
        }
        while (!structure.isEmpty()) {
            nbttag = getSubNBTTagCompound(nbttag, structure.pop());
            if (nbttag == null) {
                return null;
            }
        }
        return nbttag;
    }

    public static void setString(NBTCompound comp, String key, String text) {
        if (text == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setString", String.class, String.class);
            method.invoke(workingtag, key, text);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static String getString(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getString", String.class);
            return (String) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static String getContent(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("get", String.class);
            return method.invoke(workingtag, key).toString();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setInt(NBTCompound comp, String key, Integer i) {
        if (i == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setInt", String.class, int.class);
            method.invoke(workingtag, key, i);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Integer getInt(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getInt", String.class);
            return (Integer) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setFloat(NBTCompound comp, String key, Float f) {
        if (f == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setFloat", String.class, float.class);
            method.invoke(workingtag, key, f);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Float getFloat(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getFloat", String.class);
            return (Float) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setLong(NBTCompound comp, String key, Long f) {
        if (f == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setLong", String.class, long.class);
            method.invoke(workingtag, key, f);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Long getLong(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getLong", String.class);
            return (Long) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setShort(NBTCompound comp, String key, Short f) {
        if (f == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setShort", String.class, short.class);
            method.invoke(workingtag, key, (short) f);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Short getShort(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getShort", String.class);
            return (Short) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static void setDouble(NBTCompound comp, String key, Double d) {
        if (d == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setDouble", String.class, double.class);
            method.invoke(workingtag, key, d);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Double getDouble(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getDouble", String.class);
            return (Double) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    public static byte getType(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return 0;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod(MethodNames.getTypeMethodName(), String.class);
            return (byte) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return 0;
    }

    public static void setBoolean(NBTCompound comp, String key, Boolean d) {
        if (d == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("setBoolean", String.class, boolean.class);
            method.invoke(workingtag, key, d);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static boolean getBoolean(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return false;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("getBoolean", String.class);
            return (boolean) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return false;
    }

    public static void set(NBTCompound comp, String key, Object val) {
        if (val == null) {
            remove(comp, key);
            return;
        }
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) {
            new Throwable("InvalideCompound").printStackTrace();
            return;
        }
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("set", String.class, getNBTBase());
            method.invoke(workingtag, key, val);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void remove(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("remove", String.class);
            method.invoke(workingtag, key);
            comp.setCompound(rootnbttag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static Boolean hasKey(NBTCompound comp, String key) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("hasKey", String.class);
            return (Boolean) method.invoke(workingtag, key);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

    @SuppressWarnings("unchecked")
    public static Set<String> getKeys(NBTCompound comp) {
        Object rootnbttag = comp.getCompound();
        if (rootnbttag == null) {
            rootnbttag = getNewNBTTag();
        }
        if (!valideCompound(comp)) return null;
        Object workingtag = gettoCompount(rootnbttag, comp);
        Method method;
        try {
            method = workingtag.getClass().getMethod("c");
            return (Set<String>) method.invoke(workingtag);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }

}