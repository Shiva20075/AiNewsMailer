package com.shiva.main;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.shiva.S3.S3EmailReader;
import com.shiva.aiService.AiNewsProvider;
import com.shiva.emailTempBuilder.EmailTemplateBuilder;
import com.shiva.mailService.EmailService;
import java.util.List;

/**
 * AiNewsMailerHandler is an AWS Lambda handler that generates AI-based daily
 * news content and sends it as an HTML email to multiple recipients.

 * The handler performs the following steps:
 * - Fetches news using AiNewsProvider
 * - Builds an email template using EmailTemplateBuilder
 * - Reads recipient emails from S3 using S3EmailReader
 * - Send emails using EmailService

 * The class is stateless and works in both local and AWS Lambda environments.

 * Required environment variables:
 * - OPENAI_API_KEY
 * - SMTP_PASS

 * Required IAM permission:
 * - s3:GetObject on the email list file
 *
 */

public class AiNewsMailerHandler implements RequestHandler<Object, String> {

    @Override
    public String handleRequest(Object input, Context context) {

        try {
            String news = AiNewsProvider.fetchNews();
            String template = EmailTemplateBuilder.buildNewsTemplate(news);
            List<String> emails = S3EmailReader.readEmails();

            for (String email : emails) {
                EmailService.sendHtmlMail(email, template);
                context.getLogger().log("Sent to: " + email);
            }
            return "SUCCESS: Sent to " + emails.size() + " recipients";
        } catch (Exception e) {
            context.getLogger().log("ERROR: " + e.getMessage());
            return "FAILURE";
        }
    }

    public static void main(String[] args) throws Exception {

        String news = AiNewsProvider.fetchNews();
        String template = EmailTemplateBuilder.buildNewsTemplate(news);
        List<String> emails = S3EmailReader.readEmails();

        for (String email : emails) {
            EmailService.sendHtmlMail(email, template);
            System.out.println("Sent to: " + email);
        }
    }
}
