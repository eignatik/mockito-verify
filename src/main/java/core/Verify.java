package core;

import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Verify<T> {
    private T mock;
    private VerificationMode mode;
    private Method method;
    private Object arg;
    private Class clazz;

    public Verify checkThat(T mock, VerificationMode mode) {
        this.mock = mock;
        this.mode = mode;
        return this;
    }

    public Verify called(Method method) {
        this.method = method;
        return this;
    }

    public Verify eqArgs(Object arg) {
        this.arg = arg;
        return this;
    }

    public Verify anyArgs(Class clazz) {
        this.clazz = clazz;
        return this;
    }

    public void verify() {
        if (arg != null) {
            try {
                method.invoke(Mockito.verify(mock, mode), Mockito.eq(arg));
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new MockitoAssertionError(e.getCause().getMessage());
            }
        } else {
            if (clazz != null) {
                try {
                    method.invoke(Mockito.verify(mock, mode), Mockito.any(clazz));
                } catch (IllegalAccessException | InvocationTargetException e) {
                    throw new MockitoAssertionError(e.getCause().getMessage());
                }
            }
        }
    }
}
