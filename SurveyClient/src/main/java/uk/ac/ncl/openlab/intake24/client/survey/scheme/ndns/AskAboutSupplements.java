package uk.ac.ncl.openlab.intake24.client.survey.scheme.ndns;

import com.google.gwt.safehtml.shared.SafeHtml;
import com.google.gwt.safehtml.shared.SafeHtmlUtils;
import org.pcollections.PVector;
import org.pcollections.TreePVector;
import uk.ac.ncl.openlab.intake24.client.survey.PromptRule;
import uk.ac.ncl.openlab.intake24.client.survey.Survey;
import uk.ac.ncl.openlab.intake24.client.survey.SurveyOperation;
import uk.ac.ncl.openlab.intake24.client.survey.WithPriority;
import uk.ac.ncl.openlab.intake24.client.survey.prompts.MultipleChoiceQuestionOption;
import uk.ac.ncl.openlab.intake24.client.survey.scheme.base.Supplements;

public class AskAboutSupplements extends Supplements {

    public AskAboutSupplements(SafeHtml promptText, PVector<MultipleChoiceQuestionOption> options) {
        super(promptText, options);
    }

    public static WithPriority<PromptRule<Survey, SurveyOperation>> withPriority(int priority) {
        return new WithPriority<>(new AskAboutSupplements(SafeHtmlUtils.fromSafeConstant(
                "<p>Did you take any dietary supplements (e.g. vitamins)?</p>"),
                TreePVector.<MultipleChoiceQuestionOption>empty()
                        .plus(new MultipleChoiceQuestionOption("Multivitamin"))
                        .plus(new MultipleChoiceQuestionOption("Multivitamin and mineral"))
                        .plus(new MultipleChoiceQuestionOption("Vitamin A"))
                        .plus(new MultipleChoiceQuestionOption("Vitamin B complex"))
                        .plus(new MultipleChoiceQuestionOption("Vitamin C"))
                        .plus(new MultipleChoiceQuestionOption("Vitamin D"))
                        .plus(new MultipleChoiceQuestionOption("Vitamin E"))
                        .plus(new MultipleChoiceQuestionOption("Calcium"))
                        .plus(new MultipleChoiceQuestionOption("Iron"))
                        .plus(new MultipleChoiceQuestionOption("Magnesium"))
                        .plus(new MultipleChoiceQuestionOption("Selenium"))
                        .plus(new MultipleChoiceQuestionOption("Zinc"))
                        .plus(new MultipleChoiceQuestionOption("Cod liver/ Fish oil"))
                        .plus(new MultipleChoiceQuestionOption("Evening Primrose oil"))
                        .plus(new MultipleChoiceQuestionOption("Chondroitin"))
                        .plus(new MultipleChoiceQuestionOption("Glucosamine"))
                        .plus(new MultipleChoiceQuestionOption("Other (please specify):", "Other", true))
        ), priority);
    }
}
