import com.cnqisoft.fastcode.CodeGenerator;
import org.junit.Test;

/**
 * @author Binary on 2020/6/3
 */
public class GenerateCodeTest {

    @Test
    public void test() {
        String configFilePath = "C:\\Users\\Binary\\Desktop\\fast-code\\src\\test\\resources\\model-form.json";
        String outputPath = "C:\\Users\\Binary\\Desktop\\fast-code\\src\\test\\java\\com\\cnqisoft";
        String packageName = "com.cnqisoft";

        CodeGenerator codeGenerator = new CodeGenerator("C:\\Users\\Binary\\Desktop\\fast-code\\src\\main\\resources");
        codeGenerator.generateCode(configFilePath, outputPath, packageName);
    }
}
