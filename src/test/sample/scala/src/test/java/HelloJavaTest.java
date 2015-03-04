import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class HelloJavaTest {
    @Test
    public void checkText() {
	HelloJava testing = new HelloJava();
	assertEquals("text must be >Java<", "Javax", testing.text);
    }

    @Test
    public void checkTextCommon() {
	HelloCommonJava testing = new HelloCommonJava();
	assertEquals("text must be >Common Java<", "Common Java", testing.text);
    }
}
