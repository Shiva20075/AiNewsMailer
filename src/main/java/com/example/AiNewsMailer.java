package com.example;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.theokanning.openai.service.OpenAiService;
import com.theokanning.openai.completion.chat.*;
import jakarta.mail.*;
import jakarta.mail.internet.*;
import java.time.Duration;
import java.util.List;
import java.util.Properties;

/**
 * AiNewsMailer is a Java application designed to run as an AWS Lambda function
 * that automatically generates a daily news summary using the OpenAI Chat
 * Completion API and delivers it via email using Gmail SMTP.
 *
 * <p>The class implements {@link RequestHandler} to expose the
 * {@code handleRequest} method as the Lambda entry point and uses the
 * {@link Context} object for runtime logging.</p>
 *
 * <p>An {@link OpenAiService} client is lazily initialized using an API key
 * stored in environment variables and reused across executions for efficiency.</p>
 *
 * <p>The application constructs a chat completion request, retrieves AI-
 * generated news content, formats it into an email body, and sends it using
 * authenticated and encrypted SMTP communication.</p>
 *
 * <p>The class also includes a {@code main} method to allow local execution
 * and testing outside the AWS Lambda environment.</p>
 */


public class AiNewsMailer implements RequestHandler<Object, String> {

    private static final String FROM_EMAIL = "palleshiva2007@gmail.com";
    private static final String TO_EMAIL   = "pallesai1995@gmail.com";
    private static OpenAiService openAiService;

    private static OpenAiService getOpenAiService() {
        if (openAiService == null) {
            String apiKey = System.getenv("OPENAI_API_KEY");
            if (apiKey == null || apiKey.isBlank()) {
                throw new RuntimeException("OPENAI_API_KEY environment variable not set");
            }
            openAiService = new OpenAiService(apiKey, Duration.ofSeconds(60));
        }
        return openAiService;
    }

    private static String fetchNewsFromAI() {
        ChatCompletionRequest request = ChatCompletionRequest.builder().model("gpt-5.1").messages(List.of(new ChatMessage(
                ChatMessageRole.USER.value(),
                "Give me the top 10 important recent news items in clear bullet points. " +
                        "Each bullet should include a short headline, a short explanation, " +
                        "and the country or region."
        ))).temperature(0.3).build();
        ChatCompletionResult result = getOpenAiService().createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }

    private static void configureMail() throws Exception {
        String aiNews = fetchNewsFromAI();
        String emailBody =
                "Hello,This is Shiva\n\n" +
                        "This Mail is From Local Main 5 \n\n" +
                        aiNews +
                        "\n\nRegards,\n" +
                        "AI(GPT) ChatBot\n" +
                        "---------------------------";
        sendEmail(emailBody);
    }

    private static void sendEmail(String body) throws Exception {

        String smtpPass = System.getenv("SMTP_PASS");
        if (smtpPass == null || smtpPass.isBlank()) {
            throw new RuntimeException("SMTP_PASS environment variable not set");
        }

        Properties props = new Properties();
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        Session session = Session.getInstance(props,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(FROM_EMAIL, smtpPass);
                    }
                }
        );

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(FROM_EMAIL));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(TO_EMAIL));
        message.setSubject("Today's Top 10 News Summary");
        message.setText(body);
        Transport.send(message);
    }

    @Override
    public String handleRequest(Object input, Context context) {
        try {
            configureMail();
            return "SUCCESS: EMAIL SENT SUCCESSFULLY";
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            return "ERROR: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        try {
            configureMail();
            System.out.println("SUCCESS: EMAIL SENT SUCCESSFULLY.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
