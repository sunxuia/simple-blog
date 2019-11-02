package net.sunxu.demo.sb.exception;


public class SimpleBlogException extends RuntimeException {
    private SimpleBlogException(String message) {
        super(message);
    }

    private SimpleBlogException(Throwable cause) {
        super(cause);
    }

    private SimpleBlogException(String message, Throwable cause) {
        super(message, cause);
    }

    public static SimpleBlogException newException(String message, Object... paras) {
        return new SimpleBlogException(format(message, paras));
    }

    public static SimpleBlogException newException(Throwable cause, String message, Object... paras) {
        return new SimpleBlogException(format(message, paras), cause);
    }

    @FunctionalInterface
    public interface ActionWithException {
        void run() throws Exception;
    }

    public static SimpleBlogException wrapException(Throwable err) {
        return new SimpleBlogException(err);
    }

    public static void wrapException(ActionWithException action) {
        try {
            action.run();
        } catch (Exception err) {
            throw new SimpleBlogException(err);
        }
    }

    public static void wrapException(ActionWithException action, String message, Object... paras) {
        try {
            action.run();
        } catch (Exception err) {
            throw new SimpleBlogException(format(message, paras), err);
        }
    }

    @FunctionalInterface
    public interface FunctionWithException<R> {
        R get() throws Exception;
    }

    public static <R> R wrapException(FunctionWithException<R> function) {
        try {
            return function.get();
        } catch (Exception err) {
            throw new SimpleBlogException(err);
        }
    }

    public static <R> R wrapException(FunctionWithException<R> function, String message, Object... paras) {
        try {
            return function.get();
        } catch (Exception err) {
            throw new SimpleBlogException(format(message, paras), err);
        }
    }

    private static String format(String str, Object... para) {
        if (para.length == 0) {
            return str;
        }
        return String.format(str, para);
    }
}
