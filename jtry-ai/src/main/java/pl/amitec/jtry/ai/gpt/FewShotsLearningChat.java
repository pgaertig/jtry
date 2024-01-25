package pl.amitec.jtry.ai.gpt;

import com.theokanning.openai.completion.chat.ChatCompletionRequest;
import com.theokanning.openai.completion.chat.ChatMessage;
import com.theokanning.openai.completion.chat.ChatMessageRole;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.theokanning.openai.completion.chat.ChatMessageRole.*;

public class FewShotsLearningChat {
    public static void main(String[] args) {

        OpenAiService service = new OpenAiService(Dotenv.load().get("OPENAI_API_KEY"));

        String prompt = "Describe the following movie using emojis: %s";

        Map<String, String> examples = Map.of(
                "The Matrix", "🕶️💊💥👾🔮🌃👨🏻‍💻🔁🔓💪",
                "Titanic", "🛳️🌊❤️🧊🎶🔥🚢💔👫💑"
        );

        List<ChatMessage> messages = new ArrayList<>();

        messages.add(new ChatMessage(SYSTEM.value(), "You are a helpful assistant."));
        examples.forEach((movie, emojis) -> {
            messages.add(new ChatMessage(USER.value(), String.format(prompt, movie)));
            messages.add(new ChatMessage(ASSISTANT.value(), emojis));
        });
        messages.add(new ChatMessage(USER.value(), String.format(prompt, "Good, Bad and Ugly")));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .maxTokens(256).build();

        var response = service.createChatCompletion(chatCompletionRequest);

        response.getChoices().forEach(System.out::println);

        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();

        // Expected output similar to: 👍😈👎💀🌵🔫🎶🏜️⚰️💰
        System.out.println(responseMessage.getContent());
    }
}
