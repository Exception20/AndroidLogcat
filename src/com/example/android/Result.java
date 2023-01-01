package com.example.android;

public final class Result<R> {

    private final R result;

    private final Throwable error;


    public Result(R result, Throwable error) {
        this.result = result;
        this.error = error;
    }


    public static <R> Result<R> result(R result) {
        return new Result<R>(result, null);
    }

    public static <R> Result<R> error(Throwable error) {
        if (error == null) {
            throw new NullPointerException();
        }
        return new Result<R>(null, error);
    }


    public final Throwable getError() {
        return error;
    }

    public final <E extends Throwable> Result<R> throwAs(Class<E> exType) throws E {
        Throwable cause = getError();
        if (cause == null) {
            return this;
        }
        E exception;
        try {
            exception = exType.getConstructor().newInstance();
            exception.initCause(cause);
        } catch (Throwable ignore) {
            throw new RuntimeException(cause);
        }
        throw exception;
    }

    public final R get() {
        if (getError() != null) {
            throwAs(null);
        }
        return result;
    }

    public final R getNotCheck() {
        return result;
    }

}
