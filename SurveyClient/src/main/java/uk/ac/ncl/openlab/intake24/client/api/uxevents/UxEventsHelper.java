package uk.ac.ncl.openlab.intake24.client.api.uxevents;

import com.google.gwt.core.client.GWT;
import com.google.gwt.storage.client.Storage;
import org.fusesource.restygwt.client.Method;
import org.fusesource.restygwt.client.MethodCallback;
import uk.ac.ncl.openlab.intake24.client.BrowserConsole;
import uk.ac.ncl.openlab.intake24.client.api.survey.UxEventsSettings;
import uk.ac.ncl.openlab.intake24.client.api.uxevents.associatedfoods.*;
import uk.ac.ncl.openlab.intake24.client.survey.UUID;

import java.util.Arrays;

public class UxEventsHelper {
    private static final String STORAGE_KEY = "intake24_session_id";

    private static final UxEventsService service = GWT.create(UxEventsService.class);

    public static final UUID sessionId = getSessionId();
    public static final String SESSION_CATEGORY = "session";
    public static final String SEARCH_CATEGORY = "search";
    public static final String ASSOCIATED_FOODS_CATEGORY = "associatedFoods";

    private static UxEventsSettings settings = UxEventsSettings.DEFAULT;

    private static final MethodCallback<Void> uxEventCallback = new MethodCallback<Void>() {
        @Override
        public void onFailure(Method method, Throwable exception) {
            BrowserConsole.error("Failed to log UX event: " + exception.getClass().getName());
        }

        @Override
        public void onSuccess(Method method, Void response) {

        }
    };

    public static void applySettings(UxEventsSettings settings) {
        UxEventsHelper.settings = settings;
    }

    public static void postSearchResultsReceived(SearchResult result) {
        if (settings.enableSearchEvents)
            service.postSearchResultsReceived(new UxEvent<SearchResult>("SearchResultsReceived", Arrays.asList(SEARCH_CATEGORY), result), uxEventCallback);
    }

    public static void postBrowseResultsReceived(BrowseCategoryResult result) {
        if (settings.enableSearchEvents)
            service.postBrowseResultsReceived(new UxEvent<BrowseCategoryResult>("BrowseCategoryResult", Arrays.asList(SEARCH_CATEGORY), result), uxEventCallback);
    }

    public static void postSearchButtonClicked(SearchButtonData data) {
        if (settings.enableSearchEvents)
            service.postSearchButtonClicked(new UxEvent<SearchButtonData>("SearchButtonClicked", Arrays.asList(SEARCH_CATEGORY), data), uxEventCallback);
    }

    public static void postCantFindButtonClicked() {
        if (settings.enableSearchEvents)
            service.postCantFindButtonClicked(new UxEvent<NoData>("CantFindButtonClicked", Arrays.asList(SEARCH_CATEGORY), new NoData()), uxEventCallback);
    }

    public static void postBrowseAllFoodsButtonClicked() {
        if (settings.enableSearchEvents)
            service.postBrowseAllFoodsButtonClicked(new UxEvent<NoData>("BrowseAllFoodsButtonClicked", Arrays.asList(SEARCH_CATEGORY), new NoData()), uxEventCallback);
    }

    public static void postBrowseBackButtonClicked(BackButtonData data) {
        if (settings.enableSearchEvents)
            service.postBrowseBackButtonClicked(new UxEvent<BackButtonData>("BrowseBackButtonClicked", Arrays.asList(SEARCH_CATEGORY), data), uxEventCallback);
    }

    public static void postSearchResultSelected(SearchResultSelectionData data) {
        if (settings.enableSearchEvents)
            service.postSearchResultSelected(new UxEvent<SearchResultSelectionData>("SearchResultSelected", Arrays.asList(SEARCH_CATEGORY), data), uxEventCallback);
    }

    public static void postManualAssociatedFoodReceived(ManualReceivedData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postManualAssociatedFoodReceived(new UxEvent<ManualReceivedData>("ManualAssociatedFoodReceived",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postManualAssociatedFoodConfirmed(ManualConfirmedData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postManualAssociatedFoodConfirmed(new UxEvent<ManualConfirmedData>("ManualAssociatedFoodConfirmed",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postManualAssociatedFoodRejected(ManualRejectedData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postManualAssociatedFoodRejected(new UxEvent<ManualRejectedData>("ManualAssociatedFoodRejected",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postManualAssociatedFoodAlreadyReported(ManualAlreadyReportedData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postManualAssociatedFoodAlreadyReported(new UxEvent<ManualAlreadyReportedData>("ManualAssociatedFoodAlreadyReported",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postAutomaticAssociatedFoodsResponse(AutomaticData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postAutomaticAssociatedFoodsResponse(new UxEvent<>("AutomaticAssociatedFoodsResponse",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postAutomaticAssociatedFoodsReceived(AutomaticData data) {
        if (settings.enableAssociatedFoodsEvents)
            service.postAutomaticAssociatedFoodsResponse(new UxEvent<>("AutomaticAssociatedFoodsReceived",
                    Arrays.asList(ASSOCIATED_FOODS_CATEGORY), data), uxEventCallback);
    }

    public static void postPageOpen() {
        if (settings.enableAssociatedFoodsEvents)
            service.postCantFindButtonClicked(new UxEvent<NoData>("PageOpen", Arrays.asList(SESSION_CATEGORY), new NoData()), uxEventCallback);
    }

    public static void postPageClose() {
        if (settings.enableAssociatedFoodsEvents)
            service.postCantFindButtonClicked(new UxEvent<NoData>("PageClose", Arrays.asList(SESSION_CATEGORY), new NoData()), uxEventCallback);
    }

    public static void postTimeWidgetLimit() {
        if (settings.enableAssociatedFoodsEvents)
            service.postCantFindButtonClicked(new UxEvent<NoData>("TimeWidgetLimit", Arrays.asList(SESSION_CATEGORY), new NoData()), uxEventCallback);
    }

    public static void cleanSessionId() {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        localStorage.removeItem(STORAGE_KEY);
    }

    private static void saveSessionIdToLocalStorage(UUID sessionId) {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        localStorage.setItem(STORAGE_KEY, sessionId.toString());
    }

    private static UUID getSessionId() {
        Storage localStorage = Storage.getLocalStorageIfSupported();
        String saved = localStorage.getItem(STORAGE_KEY);
        UUID sessionId = saved == null ? UUID.randomUUID() : UUID.fromString(saved);
        saveSessionIdToLocalStorage(sessionId);
        return sessionId;
    }

}
