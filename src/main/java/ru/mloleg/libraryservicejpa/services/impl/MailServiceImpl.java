package ru.mloleg.libraryservicejpa.services.impl;

import freemarker.template.Configuration;
import jakarta.mail.internet.MimeMessage;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import ru.mloleg.libraryservicejpa.models.Person;
import ru.mloleg.libraryservicejpa.services.MailService;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

@Service
public class MailServiceImpl implements MailService {
    private final JavaMailSender javaMailSender;
    private final Configuration configuration;

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    public MailServiceImpl(JavaMailSender javaMailSender, Configuration configuration) {
        this.javaMailSender = javaMailSender;
        this.configuration = configuration;
    }

    @SneakyThrows
    public void sendConfirmationCode(Person person) {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage,
                false,
                "UTF-8");
        helper.setSubject("Thank you for registration, " + person.getCredentials());
        helper.setFrom(from);
        helper.setTo(person.getEmail());
        String emailContent = getRegistrationEmailContent(person);
        helper.setText(emailContent, true);
        javaMailSender.send(mimeMessage);
    }

    @SneakyThrows
    private String getRegistrationEmailContent(Person person) {
        StringWriter writer = new StringWriter();
        Map<String, Object> model = new HashMap<>();
        model.put("credentials", person.getCredentials());
        model.put("activationCode", person.getActivationCode());
        configuration.getTemplate("registration.ftlh")
                .process(model, writer);
        return writer.getBuffer().toString();
    }

}
