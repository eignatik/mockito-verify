package core;

import org.mockito.Mockito;
import org.mockito.exceptions.base.MockitoAssertionError;
import org.mockito.verification.VerificationMode;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Small extension to operate with verify calledMethod in Mockito library.
 * This class works as builder and provides us delayed verification that some mock methods have been invoked or not.
 * @since v0.1
 */
public class Verify {
    private Object mock;
    private VerificationMode mode;
    private Method method;
    private List<Matcher> args = new ArrayList<>();

    private String invocationDetails;

    /**
     * Chechk that concrete mock have been involved with some calledMethod invocations some times.
     * Specify concrete mock as first argument, verification mode as second.
     * @param mock any Mock or Spy object that need to be checked
     * @param mode VerificationMode.class objects from Mockito, e.g. Mockito.times(3), etc
     * @return instance of current Verify checker
     */
    public Verify checkThat(Object mock, VerificationMode mode) {
        this.mock = mock;
        this.mode = mode;
        return this;
    }

    /**
     * Set method name that you want to check and its arguments types if needed
     * Dark evil that is called reflection will accessed this method to invoke it later
     * e.g.:
     *      new Verify().checkThat(someMock, times(3)).calledMethod("catGetter", String.class);
     * TODO: implement another approach based on lambdas and delayed invoke
     * @since v0.1
     * @param methodName String method name
     * @param argClasses enumerate all Classes that are using as arguments in this method
     * @return instance of current Verify checker
     */
    public Verify calledMethod(String methodName, Class ...argClasses) {
        try {
            this.method = mock.getClass().getMethod(methodName, argClasses);
        } catch (NoSuchMethodException e) {
            throw new MockitoAssertionError(e.getCause().getMessage());
        }
        return this;
    }

    /**
     * Specify all arguments that will be used as exact matcher that will be invoked in method.
     * It works and looks like matchers in Mockito. Just specify any value here to check then exactly this value
     * e.g.:
     *      new Verify().checkThat(someMock, times(3)).calledMethod("catGetter", String.class).eq("kittyCat");
     * @param equalValue any Object value
     * @return instance of current Verify checker
     */
    public Verify eq(Object equalValue) {
        args.add(new Equals(equalValue));
        return this;
    }

    /**
     * Specify all arguments that will be used as class matcher that will be invoked in method.
     * It works and looks like matchers in Mockito. Just specify any value here to check then exactly this value
     * e.g.:
     *      new Verify().checkThat(someMock, times(3)).calledMethod("catGetter", String.class).eq(String.class);
     * TODO: add a few methods like Mockito::anyString, etc
     * @param equalValue any Class value
     * @return instance of current Verify checker
     */
    public Verify any(Object equalValue) {
        args.add(new Any(equalValue));
        return this;
    }

    /**
     * Invoke verifying of all matchers and options that are set before.
     * It also provides kind of report which invocation was expected
     * @see Verify::setInvocationDetails
     */
    public void verify() {
        try {
            if (args != null) this.selectImplementation();
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new MockitoAssertionError(e.getCause().getMessage());
        }
    }

    /**
     * Getting invocation details after verifying
     * @see Verify::setInvocationDetails
     * @return String with json formatted details
     */
    public String getInvocationDetails() {
        return invocationDetails;
    }

    private void selectImplementation() throws IllegalAccessException, InvocationTargetException {
        switch(args.size()) {
            case 0: method.invoke(Mockito.verify(mock, mode)); break;
            case 1: method.invoke(Mockito.verify(mock, mode), selectMatcher(0)); break;
            case 2: method.invoke(Mockito.verify(mock, mode), selectMatcher(0), selectMatcher(1)); break;
            case 3: method.invoke(Mockito.verify(mock, mode), selectMatcher(0), selectMatcher(1), selectMatcher(2)); break;
            case 4: method.invoke(Mockito.verify(mock, mode), selectMatcher(0), selectMatcher(1), selectMatcher(2), selectMatcher(3)); break;
            case 5: method.invoke(Mockito.verify(mock, mode), selectMatcher(0), selectMatcher(1), selectMatcher(2), selectMatcher(3), selectMatcher(4)); break;
            case 6: method.invoke(Mockito.verify(mock, mode), selectMatcher(0), selectMatcher(1), selectMatcher(2), selectMatcher(3), selectMatcher(4), selectMatcher(5)); break;
            default: throw new MockitoAssertionError("There are no available matchers");
        }
        setInvocationDetails();
    }

    private Object selectMatcher(int i) {
        return isEqualsImpl(args.get(i)) ? Mockito.eq(args.get(i).getValue()) : Mockito.any((Class) args.get(i).getValue());
    }

    private void setInvocationDetails() {
        invocationDetails = "'verifyObject':'" + mock.getClass() + "',\n'verificationMode':'" + mode.toString() + "'\n'calledMethod':'" + method.getName() + "',\nexpectedInvocations:{\n" + getExpectedInvocations() + "}";
    }

    private String getExpectedInvocations() {
        StringBuilder builder = new StringBuilder();
        int invocationNumber = 1;
        for (Matcher matcher : args) {
            builder.append("\t'inv").append(invocationNumber++).append("':'").append(matcher.getValue()).append(invocationNumber >= args.size() ? "'\n" : "',\n");
        }
        return builder.toString();
    }

    private boolean isEqualsImpl(Matcher matcher) {
        return matcher instanceof Equals;
    }

    private abstract static class Matcher {
        abstract Object getValue();
    }

    private static class Equals extends Matcher {
        Object value;
        Equals(Object value) {
            this.value = value;
        }

        Object getValue() {
            return value;
        }
    }

    private static class Any extends Matcher {
        Object value;
        Any(Object value) {
            this.value = value;
        }

        Object getValue() {
            return value;
        }
    }
}
