<p align="center">
  <img src="https://img.shields.io/badge/Build-Success-brightgreen" alt="Build Success" />
  <img src="https://img.shields.io/badge/AI-Automation-purple" alt="AI Automation" />
  <img src="https://img.shields.io/badge/Java-17-orange" alt="Java 17" />
  <img src="https://img.shields.io/badge/AWS-Lambda-yellow" alt="AWS Lambda" />
  <img src="https://img.shields.io/badge/AWS-S3-orange" alt="Amazon S3" />
  <img src="https://img.shields.io/badge/Architecture-Serverless-success" alt="Serverless Architecture" />
      <img src="https://img.shields.io/badge/Email-SMTP-blue" alt="SMTP Email" />

</p>


<h1 align="center">🤖 AiNewsMailer ✉️</h1>

## Introduction
AiNewsMailer is a Java-based application that automatically creates news content using AI and sends it to people through email. The main purpose of this project is to reduce the manual effort required to prepare and send regular news or information emails.
The application can be run locally for development and testing, or deployed on AWS Lambda for serverless execution in production environments.
## Overview
AiNewsMailer is a simple application that generates news content using AI and sends it to recipients via email. The list of recipient email addresses is stored in an Amazon S3 bucket, allowing updates without modifying or redeploying the application code.



* When the application runs, it reads the email list from the S3 bucket, generates the news content using AI, and sends the emails using SMTP service. This application can be run locally for testing or deployed on AWS Lambda for automatic, scalable execution.

<!-- # This is a Heading h1
## This is a Heading h2
###### This is a Heading h6 -->

<!-- ## Emphasis

*This text will be italic*  
_This will also be italic_

**This text will be bold**  
__This will also be bold__

_You **can** combine them_ -->

## Use Case
AiNewsMailer can be used to automatically send AI-generated news or informational emails to a group of recipients without manual effort. It is especially useful for scenarios where emails need to be sent regularly or to a changing list of users.
* Sending daily or weekly news updates

<!-- ### Ordered

1. Item 1
2. Item 2
3. Item 3
    1. Item 3a
    2. Item 3b -->

<!-- ## Images

![This is an alt text.](/image/Markdown-mark.svg "This is a sample image.") -->
<!-- 
## Links

You may be using [Markdown Live Preview](https://markdownlivepreview.com/). -->

<!-- ## Blockquotes

> Markdown is a lightweight markup language with plain-text-formatting syntax, created in 2004 by John Gruber with Aaron Swartz.
>
>> Markdown is often used to format readme files, for writing messages in online discussion forums, and to create rich text using a plain text editor. -->
## Prerequisites
* Java 17 or later
* Maven 3.8+
* An AWS account
* An SMTP-enabled email account (e.g., Gmail with App Password)
* An OpenAI API key 


## AWS SetUp ☁️
---
## S3 🪣
1. Sign in using your AWS account 
2. In the top search bar, type S3 and open the Amazon S3 service
3. Create a bucket, Enter your bucket name,Select region,Leave other settings default

### Create Folder  in the Bucket :

1. Open the newly created S3 bucket
2. Click Create folder.
3. Enter the folder name:
```
config
```
### Create Email List File :
1. Open the config folder.
2. Click Upload.
3. Click Add files.
4. Create a local text file named:
```
email-list.txt
```
5. Add one email address per line:
```
user1@example.com
user2@example.com
user3@example.com
```
6. Upload the file.

### Note the S3 Path :
```
s3://ainewsbot/config/email-list.txt
```
### IAM Policy Configuration :
This section shows how to give AWS Lambda permission to read email addresses from S3 and save logs.

1. In the AWS Management Console search bar, type IAM.
2. Open the IAM service
3. From the left navigation menu, click Policies.
4. Click Create policy.
5. In the policy editor, select the JSON tab.Enter the Name of policy
6. Remove any existing content.
7. Paste the following policy

```
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Action": "s3:GetObject",
      "Resource": "arn:aws:s3:::ainewsbot/config/email-list.txt"
    },
    {
      "Effect": "Allow",
      "Action": [
        "logs:CreateLogGroup",
        "logs:CreateLogStream",
        "logs:PutLogEvents"
      ],
      "Resource": "*"
    }
  ]
}

```
8.Enter Policy name:  AiNewsMailerS3AndLogsPolicy

### Create IAM Role
1. The IAM role allows AWS Lambda to use the policy you created.
2. In the IAM service, click Roles from the left menu.
3. Click Create role.
4. Choose AWS service.
5. For Service or use case, select Lambda. Click Next
6. On the Add permissions screen, search for: Name of policy
7. Select the policy. Click next
8. Name and Create the Role
9. ✅ IAM role is now created.

##  Environment Variables (For Loacal)
1. Open your project in IntelliJ IDEA.
2. In the top menu, click Run → Edit Configurations….
3. In the Run/Debug Configurations window, find the Environment variables field.
4. Click the Edit (📄) button next to it.
5. Add the following variables one by one:

| Key | Value |
| ------------- |:-------------:|
|OPENAI_API_KEY     | Enter Your API KEY     |
| SMTP_PASS     | Enter Your SMTP pass   |
|FROM_EMAIL    | Senders Email    |

## Deploying to AWS Lambda
* Package the application:
```
mvn clean package
```
* Upload the generated JAR file to AWS Lambda
* Set the handler:
```
com.yourpackage.yourClassName::handleRequest
```
## Assign the IAM role created earlier
* In the Lambda function page, click the Configuration tab.
* Click Permissions from the left menu
* In the Execution role section, click Edit.
* Select Use an existing role.
* choose the IAM role you created earlier.

## Configure environment variables in the Lambda console
* In the Lambda function page, click the Configuration tab.
* From the left-side menu, click Environment variables.
* Click the Edit button. Add these.

| Key | Value |
| ------------- |:-------------:|
|OPENAI_API_KEY     | Enter Your API KEY     |
| SMTP_PASS     | Enter Your SMTP pass   |
|FROM_EMAIL    | Senders Email    |

## Triggering Lambda
* Triggering the Lambda Function Using Event Bridge Or Manual Invocation
* Run The Lambda Function Check execution status and errors.
<!-- ## Blocks of code

```
let message = 'Hello world';
alert(message);
``` -->
<!-- 
## Inline code

This web site is using `markedjs/marked`. -->
