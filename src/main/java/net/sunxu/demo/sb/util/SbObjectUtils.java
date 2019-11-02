package net.sunxu.demo.sb.util;

import net.sunxu.demo.sb.exception.SimpleBlogException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class SbObjectUtils {
    public static <T, R> R copyBean(T source, R target) {
        Class<T> sourceClass = (Class<T>) source.getClass();
        Class<R> targetClass = (Class<R>) target.getClass();

        try {
            BeanInfo targetBi = Introspector.getBeanInfo(targetClass, Object.class);
            Map<String, PropertyDescriptor> targetPds = Arrays.stream(targetBi.getPropertyDescriptors())
                    .collect(Collectors.toMap(pd -> pd.getName(), pd -> pd));

            BeanInfo sourceBi = Introspector.getBeanInfo(sourceClass, Object.class);
            for (PropertyDescriptor sourcePd : sourceBi.getPropertyDescriptors()) {
                PropertyDescriptor targetPd = targetPds.get(sourcePd.getName());
                if (targetPd == null || !targetPd.getPropertyType().equals(sourcePd.getPropertyType())) {
                    continue;
                }
                Object value = sourcePd.getReadMethod().invoke(source);
                targetPd.getWriteMethod().invoke(target, value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while copy bean from %s to %s", sourceClass.getName(), targetClass.getName());
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while copy bean from %s to %s", sourceClass.getName(), targetClass.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while copy bean from %s to %s", sourceClass.getName(), targetClass.getName());
        }
        return target;
    }

    public static <T, R> List<R> copyBeans(Collection<T> source, Class<R> targetType) {
        List<R> res = new ArrayList<>(source.size());
        try {
            for (T item : source) {
                R target = targetType.getConstructor().newInstance();
                copyBean(item, target);
                res.add(target);
            }
            return res;
        } catch (Exception err) {
            err.printStackTrace();
            throw SimpleBlogException.wrapException(err);
        }
    }

    public static <T> Map<String, Object> getProperties(T source, Class<T> sourceClass) {
        Map<String, Object> res = new HashMap<>();
        try {
            BeanInfo sourceBi = Introspector.getBeanInfo(sourceClass, Object.class);
            for (PropertyDescriptor pd : sourceBi.getPropertyDescriptors()) {
                Object value = pd.getReadMethod().invoke(source);
                res.put(pd.getName(), value);
            }
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while get bean property from %s", sourceClass.getName());
        } catch (IntrospectionException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while get bean property from %s", sourceClass.getName());
        } catch (InvocationTargetException e) {
            e.printStackTrace();
            throw SimpleBlogException.newException(e,
                    "error while get bean property from %s", sourceClass.getName());
        }
        return res;
    }

    public static String date2String(Date date) {
        if (date == null) {
            return "";
        }
        long interval = System.currentTimeMillis() - date.getTime();
        if (interval < 1000 * 60) {
            return "刚才";
        } else if (interval < 1000 * 60 * 60) {
            return Math.round(interval / 1000 / 60) + "分钟前";
        } else if (interval < 1000 * 60 * 60 * 24) {
            return Math.round(interval / 1000 / 60 / 60) + "小时前";
        } else if (interval < 1000 * 60 * 60 * 24 * 30) {
            return Math.round(interval / 1000 / 60 / 60 / 24) + "天前";
        } else {
            DateFormat formatter = new SimpleDateFormat("yyyy年MM月dd日");
            String res = formatter.format(date);
            return res;
        }
    }

    public static <T, R> Page<R> convertPage(Page<T> page, Function<T, R> converter) {
        var rs = page.stream().map(converter::apply).collect(Collectors.toList());
        return new PageImpl<R>(rs, page.getPageable(), page.getTotalElements());
    }

    public static byte[] inputStream2Array(InputStream inputStream) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        byte[] buf = new byte[1024];
        int len;
        while ((len = inputStream.read(buf)) != -1) {
            outputStream.write(buf, 0, len);
        }
        return outputStream.toByteArray();
    }
}
