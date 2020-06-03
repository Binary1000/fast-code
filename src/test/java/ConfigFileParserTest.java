import com.cnqisoft.fastcode.ConfigFileParser;
import com.cnqisoft.fastcode.Field;
import com.cnqisoft.fastcode.Table;
import org.junit.Assert;
import org.junit.Test;

/**
 * @author Binary on 2020/6/3
 */
public class ConfigFileParserTest {

    @Test
    public void test() {
        Table table = ConfigFileParser.parse("C:\\Users\\Binary\\Desktop\\fast-code\\src\\test\\resources\\model-form.json");
        Assert.assertEquals("model", table.getName());
        Assert.assertEquals(7, table.getFields().size());
        long count = table.getFields().stream().filter(Field::isEnum).count();
        Assert.assertEquals(3, count);

    }
}
