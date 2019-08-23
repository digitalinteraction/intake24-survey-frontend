package uk.ac.ncl.openlab.intake24.client.survey.scheme.ndns;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.pcollections.PSet;
import org.pcollections.PVector;
import org.pcollections.TreePVector;
import org.workcraft.gwt.shared.client.Option;
import uk.ac.ncl.openlab.intake24.client.survey.*;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.MultipleChoiceQuestionOption;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.messages.PromptMessages;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.simple.CheckListPrompt;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.simple.RadioButtonPrompt;

public class AskAboutDiet implements PromptRule<Survey, SurveyOperation> {

    public static final String DIET_COMPLETE = "dietComplete";
    public static final String DIET_KEY = "diet";


    final private PVector<MultipleChoiceQuestionOption> supplementOptions = TreePVector.<MultipleChoiceQuestionOption>empty()
            .plus(new MultipleChoiceQuestionOption("To lose weight"))
            .plus(new MultipleChoiceQuestionOption("To gain weight"))
            .plus(new MultipleChoiceQuestionOption("For medical reasons e.g. to lower cholesterol"))
            .plus(new MultipleChoiceQuestionOption("Gluten free"))
            .plus(new MultipleChoiceQuestionOption("Wheat free"))
            .plus(new MultipleChoiceQuestionOption("Dairy free"))
            .plus(new MultipleChoiceQuestionOption("Vegetarian"))
            .plus(new MultipleChoiceQuestionOption("Vegan"))
            .plus(new MultipleChoiceQuestionOption("Other (please specify):", "Other", true))
            .plus(new MultipleChoiceQuestionOption("Not on a special diet"));


    @Override
    public Option<Prompt<Survey, SurveyOperation>> apply(Survey state, SelectionMode selectionType, PSet<String> surveyFlags) {
        if (!state.flags.contains(DIET_COMPLETE) && state.portionSizeComplete()) {

            SafeHtml promptText = SafeHtmlUtils.fromSafeConstant("<p>Are you following any kind of special diet?</p>" +
                    "<p><small>If yes, please tick the options below that best describes your diet " +
                    "(you can tick more than one e.g. vegetarian and gluten free)</small></p>");

            CheckListPrompt prompt = new CheckListPrompt(promptText, AskAboutDiet.class.getSimpleName(),
                    supplementOptions, PromptMessages.INSTANCE.mealComplete_continueButtonLabel());

            return Option.some(PromptUtil.asSurveyPrompt(prompt, answers -> {
                if (!answers.isEmpty()) {
                    String dietValue = answers.stream().map(answer -> answer.getValue()).reduce("", (s1, s2) -> s1 + (s1.isEmpty() ? "" : ", ") + s2);
                    return SurveyOperation.update(survey -> survey.withData(DIET_KEY, dietValue).withFlag(DIET_COMPLETE));
                } else
                    return SurveyOperation.update(survey -> survey.withFlag(DIET_COMPLETE));
            }));
        } else {
            return Option.none();
        }
    }

    public static WithPriority<PromptRule<Survey, SurveyOperation>> withPriority(int priority) {
        return new WithPriority<>(new AskAboutDiet(), priority);
    }
}
