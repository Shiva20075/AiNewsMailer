AiNewsMailer

AiNewsMailer is a serverless Java 17 application that generates AI-curated news content and delivers it to recipients via email.

Overview

AiNewsMailer automates the end-to-end process of content generation and email distribution. The application uses AI services to generate curated news, formats the content into structured email templates, and sends emails using SMTP.

The system is designed to run on AWS Lambda, enabling serverless execution, automatic scaling, and minimal infrastructure management. Recipient email addresses and runtime configuration are externalized using AWS services, allowing operational changes without redeploying the application.

Key Features

AI-generated news content

Serverless execution with AWS Lambda

Email delivery via SMTP

Recipient management using Amazon S3

Modular and maintainable architecture

Secure configuration through environment variables

Architecture Overview

The application follows an event-driven, serverless architecture:

AWS Lambda executes the application logic

Amazon S3 stores recipient email lists

AI services generate dynamic content

SMTP is used for email delivery

CloudWatch captures logs and execution metrics

Project Structure
src/main/java/com/shiva
│
├── aiService
│   └── AiNewsService          # AI-based content generation
│
├── emailTempBuilder
│   └── EmailTemplateBuilder  # Email template construction
│
├── mailService
│   └── EmailService          # SMTP email delivery
│
├── S3
│   └── S3EmailReader         # Reads email list from Amazon S3
│
└── main
    └── AiNewsMailer          # AWS Lambda entry point

Execution Flow

AWS Lambda function is invoked

Environment variables are loaded

Recipient email list is read from Amazon S3

AI generates curated news content

Email templates are constructed

Emails are sent via SMTP

Logs are written to CloudWatch

Requirements
Software

Java 17

Maven 3.8+

AWS Services

AWS Lambda

Amazon S3

IAM

CloudWatch Logs

External Services

AI provider (via API key)

SMTP email provider

Configuration
Environment Variables
OPENAI_API_KEY
SMTP_HOST
SMTP_PORT
SMTP_USERNAME
SMTP_PASSWORD
SMTP_FROM


All sensitive configuration must be provided via environment variables. No secrets are stored in source code.

Amazon S3 Setup
Purpose

Amazon S3 is used to store recipient email addresses.

Bucket Structure
s3://<bucket-name>/config/email-list.txt

File Format
user1@example.com
user2@example.com
user3@example.com


One email address per line.

AWS Lambda Setup

Runtime: Java 17

Handler:

com.shiva.main.AiNewsMailer::handleRequest


Memory: 512–1024 MB

Timeout: 30–60 seconds

IAM Permissions

Minimum required policy:

{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::<bucket-name>/config/email-list.txt"
    },
    {
      "Effect": "Allow",
      "Action": "logs:*",
      "Resource": "*"
    }
  ]
}

Build Instructions
mvn clean package


The shaded JAR will be generated in:

target/ai-news-mailer-<version>.jar

Running Locally (Optional)
export OPENAI_API_KEY=...
export SMTP_HOST=...
export SMTP_PORT=587
export SMTP_USERNAME=...
export SMTP_PASSWORD=...

java -jar target/ai-news-mailer.jar

Logging and Monitoring

Logs are written to Amazon CloudWatch

Errors are logged with stack traces

Failures in S3, AI, or SMTP are explicitly reported

Limitations

Lambda execution time limits apply

SMTP rate limits depend on provider

Network access is required for AI and SMTP services

Future Enhancements

Amazon SES integration

Retry and DLQ support

Multiple templates and categories

Scheduled execution using EventBridge

Author

Palle Shiva Charan
GitHub: https://github.com/Shiva20075
