package com.shiva.S3;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

/**
 * S3EmailReader is a utility class responsible for retrieving
 * a list of email addresses stored in an Amazon S3 object.

 * The class connects to Amazon S3 using the AWS SDK for Java v2 and
 * reads a plain text file where each line represents a single email
 * address. Blank lines and leading/trailing whitespace are ignored.

 * This class is designed to work seamlessly in both local development
 * environments and AWS Lambda
 * Local execution – Uses AWS credentials configured via
 * aws/credentials} or environment variables
 * AWS Lambda – Uses the IAM execution role attached to the Lambda function

 * The S3 bucket name and object key are defined as constants to
 * ensure consistency and to avoid hardcoding values throughout the
 * application
 */

public class S3EmailReader {

    private static final String BUCKET = "ainewsbot";
    private static final String KEY = "config/email-list.txt";

    public static List<String> readEmails() throws Exception {

        S3Client s3 = S3Client.create();
        BufferedReader reader = new BufferedReader(new InputStreamReader(s3.getObject(GetObjectRequest.builder().bucket(BUCKET).key(KEY).build())));
        return reader.lines().map(String::trim).filter(s -> !s.isEmpty()).collect(Collectors.toList());
    }
}
