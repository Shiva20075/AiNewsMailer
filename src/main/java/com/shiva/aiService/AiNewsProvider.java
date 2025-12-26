package com.shiva.aiService;
import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatCompletionResult;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import java.time.Duration;
import java.util.List;

/**
 * AiNewsService is a service class responsible for retrieving AI-generated
 * news content using the OpenAI Chat Completion API.
 *
 * It lazily initializes and reuses a singleton OpenAiService instance to
 * optimize performance and reduce connection overhead across executions.
 *
 * The class securely reads the OpenAI API key from the OPENAI_API_KEY
 * environment variable, avoiding hardcoded credentials.
 *
 * The fetchNews method constructs a controlled chat completion request with
 * strict formatting rules to generate a professional daily news digest
 * suitable for direct inclusion in an HTML email.
 *
 * A low temperature value is used to ensure factual, consistent, and
 * deterministic output.
 */

public class AiNewsProvider {

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

    public static String fetchNews() {
        ChatCompletionRequest request = ChatCompletionRequest.builder().model("gpt-4.1-mini").messages(List.of(
                new ChatMessage(ChatMessageRole.USER.value(),
                        "Generate a professional daily news digest.\n" +
                                "\n" +
                                "Requirements:\n" +
                                "- Provide exactly 10 important recent news items.\n" +
                                "- Use a numbered list from 1 to 10.\n" +
                                "- Each news item must include:\n" +
                                "  - A short, clear headline on the first line\n" +
                                "  - A concise 1–2 sentence explanation on the next line\n" +
                                "  - The country or region in parentheses at the end of the explanation\n" +
                                "\n" +
                                "Formatting rules:\n" +
                                "- Do not use markdown symbols such as **, *, or bullet points\n" +
                                "- Do not use emojis\n" +
                                "- Keep language neutral, factual, and professional\n" +
                                "- Maintain consistent spacing between items\n" +
                                "- Make the output suitable for direct use in an email or HTML template\n" +
                                "\n" +
                                "Cover a mix of global topics including politics, economy, environment, technology, and society.\n"
                        ))).temperature(0.3).build();

        ChatCompletionResult result = getOpenAiService().createChatCompletion(request);
        return result.getChoices().get(0).getMessage().getContent();
    }
}
