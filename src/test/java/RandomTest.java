import org.junit.Test;

import java.io.File;

/**
 * @author Binary on 2020/6/4
 */
public class RandomTest {

    @Test
    public void test() {
        File file = new File("C:/Users/Binary/Desktop/test-fast-code/src/main/resources\\schema\\user.sql");
        file.getParentFile().mkdirs();
    }
}
