package pl.amitec.jtry.ai.gpt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.theokanning.openai.completion.chat.*;
import com.theokanning.openai.service.FunctionExecutor;
import com.theokanning.openai.service.OpenAiService;
import io.github.cdimascio.dotenv.Dotenv;

import java.util.*;
import java.util.function.Function;

/**
 * This example shows how to use GPT-3 to translate and structure messages.
 * The messages are translated to English and then could be saved to database.
 */
public class StructuredMessageTranslationFunctionChat {

    public static class TranslatedFormSubmission {
        @JsonPropertyDescription("English summary of the message")
        public String summary;

        @JsonPropertyDescription("English translation of the message content")
        public String content;

        @JsonPropertyDescription("The original message language ISO code")
        @JsonProperty(required = true)
        public String languageCode;
    }

    public static class FormSubmissionResult {
        public String submissionNumber;

        public FormSubmissionResult(String submissionNumber) {
            this.submissionNumber = submissionNumber;
        }
    }


    public static void main(String[] args) {
        // Load environment variables from .env file
        Dotenv dotenv = Dotenv.load();

        OpenAiService service = new OpenAiService(dotenv.get("OPENAI_API_KEY"));

        String supportQuery = """
        Niestety od wczoraj nie działa wysyłanie plików, próbuję wysłać 20 plików wideo naraz.
        Kiedy wysyłanie zbliża się do końca wyskakuje błąd "Przekroczono limit" i wysyłanie zostaje przerwane.
        Proszę o pomoc w rozwiązaniu problemu.
        """;


        String sanitizedSupportQuery = supportQuery.replace("[[[", "")
                .replace("]]]", "").replace("\"", "'");

        List<ChatMessage> messages = List.of(
                new ChatMessage(ChatMessageRole.SYSTEM.value(),
                        "You are a helpful web app support assistant translating messages coming from users." +
                        "Translate only content which is enclosed by [[[ ]]] brackets."),
                new ChatMessage(ChatMessageRole.USER.value(),
                        "Save the message: [[[" + sanitizedSupportQuery + "]]]")
        );

        final Function<TranslatedFormSubmission, FormSubmissionResult> saveMessage = (submission) -> {
            System.out.println("Saving translated message: ");
            System.out.println("- Title: " + submission.summary);
            System.out.println("- Content: " + submission.content);
            System.out.println("- Language code: " + submission.languageCode);
            return new FormSubmissionResult(UUID.randomUUID().toString());
        };

        FunctionExecutor functionExecutor = new FunctionExecutor(Collections.singletonList(ChatFunction.builder()
                .name("save_message")
                .description("Submit message translated to English")
                .executor(TranslatedFormSubmission.class, saveMessage::apply)
                .build()));

        ChatCompletionRequest chatCompletionRequest = ChatCompletionRequest
                .builder()
                .model("gpt-3.5-turbo")
                .messages(messages)
                .functions(functionExecutor.getFunctions())
                .functionCall(ChatCompletionRequest.ChatCompletionRequestFunctionCall.of("auto"))
                .n(1)
                .logitBias(new HashMap<>())
                .maxTokens(256).build();

        //service.createChatCompletion(chatCompletionRequest).getChoices().forEach(System.out::println);
        ChatMessage responseMessage = service.createChatCompletion(chatCompletionRequest).getChoices().get(0).getMessage();

        ChatFunctionCall functionCall = responseMessage.getFunctionCall();
        if(functionCall == null || responseMessage.getContent() != null) {
            System.err.println("Error, function call was expected: " + responseMessage.getContent());
            return;
        } else {
            System.out.println("Function call requested: " + functionCall.getName());
            Optional<ChatMessage> result = functionExecutor.executeAndConvertToMessageSafely(functionCall);
            // message should contain UUID, GPT can get it back to format a response message and send it with another function
        }

    }
}
