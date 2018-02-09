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
    public void testGetString() throws Exception {
        Verify saveAndNotify1 = new Verify<>()
                .checkThat(bar, Mockito.times(1))
                .called(bar.getClass().getMethod("saveAndNotify", String.class))
                .eqArgs("fooBar");

        //lines of logic

        //invocation is here
        foo.getString();

        //verify now
        saveAndNotify1.verify();
    }
}
