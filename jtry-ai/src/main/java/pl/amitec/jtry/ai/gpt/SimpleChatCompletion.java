package pl.amitec.jtry.ai.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.List;

/**
 * Simple chat completion example - translates English text to French.
 */
public class SimpleChatCompletion {
    public static void main(String[] args) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        OpenAiService service = new OpenAiService(dotenv.get("OPENAI_API_KEY"));

        String englishText = "Hello, how are you?";

        List<ChatMessage> messages = List.of(
                new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "You are a helpful assistant."),
                new ChatMessage(ChatMessageRole.USER.value(),
                        "Translate the following English text to French: \"" + englishText + "\"")
        );

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(256).build();

        //service.createChatCompletion(chatCompletionRequest).getChoices().forEach(System.out::println);
        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();

        // Expected output: "Bonjour, comment ça va?"
        System.out.println(responseMessage.getContent());

    }
}
