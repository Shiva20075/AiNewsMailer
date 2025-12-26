package com.shiva.main;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.shiva.aiService.AiNewsService;
import com.shiva.emailTempBuilder.EmailTemplateBuilder;
import com.shiva.mailService.EmailService;

/**
 * AiNewsMailerHandler is the main orchestration class that generates and sends
 * AI-curated daily news emails.
 *
 * It is designed to run as an AWS Lambda function by implementing the
 * RequestHandler interface, exposing the handleRequest method as the
 * Lambda entry point.
 *
 * The class fetches news content from an AI service, builds an HTML email
 * template, and sends the email using a configured SMTP mail service.
 *
 * A main method is provided to allow local execution and testing outside
 * the AWS Lambda environment.
 *
 * Runtime errors are logged using the Lambda Context logger to aid
 * debugging and operational monitoring.
 */


 public class AiNewsMailerHandler implements RequestHandler<Object, String> {

    public String handleRequest(Object input, Context context) {
        try {
            String news = AiNewsService.fetchNews();
            String newsTemplate = EmailTemplateBuilder.buildNewsTemplate(news);
            EmailService.sendHtmlMail(newsTemplate);
            return "SUCCESS: EMAIL SENT SUCCESSFULLY";
        } catch (Exception e) {
            context.getLogger().log(e.toString());
            return "ERROR: " + e.getMessage();
        }
    }

    public static void main(String[] args) {
        try {
            String news = AiNewsService.fetchNews();
            String newsTemplate = EmailTemplateBuilder.buildNewsTemplate(news);
            EmailService.sendHtmlMail(newsTemplate);
            System.out.println("SUCCESS: EMAIL SENT SUCCESSFULLY.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
