import com.mitchellbosecke.pebble.PebbleEngine;
import com.mitchellbosecke.pebble.error.PebbleException;
import com.mitchellbosecke.pebble.template.PebbleTemplate;
import io.github.mysar.OhMyEmail;
import jetbrick.template.JetEngine;
import jetbrick.template.JetTemplate;
import org.junit.Before;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.File;
import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import static io.github.mysar.OhMyEmail.SMTP_QQ;

/**
 * @author yan
 * 2017.8.4
 */
public class OhMyEmailTest {

    @Before
    public void before() throws GeneralSecurityException {
        // 配置，一次即可
        OhMyEmail.config(SMTP_QQ(false), "system@wasis.cn", "fmjfpgxyhcepcjcc");
    }

    @Test
    public void testSendText() throws MessagingException {
        OhMyEmail.subject("这是一封测试TEXT邮件")
                .from("211的QQ邮箱")
                .to("xxx@qq.com")
                .text("信件内容")
                .send();
    }

    @Test
    public void testSendHtml() throws MessagingException {
        OhMyEmail.subject("这是一封测试HTML邮件")
                .from("闫超辉的邮箱")
                .to("xxx@qq.com")
                .html("<h1 font=red>信件内容</h1>")
                .send();
    }

    @Test
    public void testSendAttach() throws MessagingException {
        OhMyEmail.subject("这是一封测试附件邮件")
                .from("闫超辉的邮箱")
                .to("xxxx@qq.com")
                .html("<h1 font=red>信件内容</h1>")
                .attach(new File("D:\\jdk下载网址.txt"), "测试图片.jpeg")
                .send();
    }

    @Test
    public void testPebble() throws IOException, PebbleException, MessagingException {
        PebbleEngine engine = new PebbleEngine.Builder().build();
        PebbleTemplate compiledTemplate = engine.getTemplate("register.html");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "xxx");
        context.put("email", "admin@java-china.org");

        Writer writer = new StringWriter();
        compiledTemplate.evaluate(writer, context);

        String output = writer.toString();
        System.out.println(output);

        OhMyEmail.subject("这是一封测试Pebble模板邮件")
                .from("闫超辉的邮箱")
                .to("me@wasis.cn")
                .html(output)
                .send();
    }

    @Test
    public void testJetx() throws IOException, PebbleException, MessagingException {
        JetEngine engine = JetEngine.create();
        JetTemplate template = engine.getTemplate("/register.jetx");

        Map<String, Object> context = new HashMap<String, Object>();
        context.put("username", "xxx");
        context.put("email", "i@wasis.cn");
        context.put("url", "<a href='http://java.org'>https://java.org/active/asdkjajdasjdkaweoi</a>");

        StringWriter writer = new StringWriter();
        template.render(context, writer);
        String output = writer.toString();
        System.out.println(output);

        OhMyEmail.subject("这是一封测试Jetx模板邮件")
                .from("闫超辉的邮箱")
                .to("me@wasis.cn")
                .html(output)
                .send();

    }

}