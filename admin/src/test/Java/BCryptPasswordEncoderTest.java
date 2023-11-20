import org.example.AdminBlogApplication;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootTest(classes = AdminBlogApplication.class)
public class BCryptPasswordEncoderTest {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void bcryptPassword(){

        String encode = passwordEncoder.encode("123");
        System.out.println(encode);

    }

}
