import com.code.controller.CodeController;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @title: CdoeTest
 * @projectName: code-generator
 * @description: TODO
 * @author: Zack_Tzh
 * @date: 2020/3/22  10:00
 */
public class CdoeTest {


    @Autowired
    private CodeController codeController;

    @Test
    public void codeControllerTest() throws Exception {

        codeController.AutoCode(1,1,1);
    }
}
