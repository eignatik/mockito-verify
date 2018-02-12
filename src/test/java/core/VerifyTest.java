package core;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import testUtil.Bar;
import testUtil.Foo;

public class VerifyTest {

    @Mock
    private Bar bar;

    @InjectMocks
    private Foo foo;

    @BeforeMethod
    private void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testGetString() {
        /** predefine verifying conditions **/
        Verify saveAndNotify1 = new Verify()
                .checkThat(bar, Mockito.times(1))
                .calledMethod("saveAndNotify", String.class)
                .eq("fooBar");

        /** predefine verifying conditions **/

        /** predefine verifying conditions **/
        foo.getString();

        /** predefine verifying conditions **/
        saveAndNotify1.verify();
        System.out.println(saveAndNotify1.getInvocationDetails());
    }

    @Test
    public void testGetString1() {
        /** predefine verifying conditions **/
        Verify saveAndNotify1 = new Verify()
                .checkThat(bar, Mockito.times(1))
                .calledMethod("saveAndNotify", String.class)
                .any(String.class);

        /** predefine verifying conditions **/

        /** predefine verifying conditions **/
        foo.getString();

        /** predefine verifying conditions **/
        saveAndNotify1.verify();
    }


    //TODO: example with DataProvider
    @Test
    public void testGetPropertyCat() {
        String property = "cat";
        Verify verify = new Verify()
                .checkThat(bar, Mockito.times(1))
                .calledMethod("saveAndNotify", String.class, String.class, Object.class, String.class, String.class, String.class)
                .any(String.class).eq("cat").any(Object.class).any(String.class).any(String.class).any(String.class);

        foo.getProperty(property);

        verify.verify();
        System.out.println(verify.getInvocationDetails());
    }
}
