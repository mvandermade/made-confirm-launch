package indicator

import model.AppState

val AMOUNT_OF_STATES = AppState.entries.size

fun appStateToIndicator(appState: AppState): String =
    "Stap " +
        when (appState) {
            AppState.START -> "${AppState.START.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.SEARCHING_ROOT -> "${AppState.SEARCHING_ROOT.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.SEARCHING_FILE -> "${AppState.SEARCHING_FILE.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.WAIT_FOR_ACKNOWLEDGE -> "${AppState.WAIT_FOR_ACKNOWLEDGE.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.START_BACKUP -> "${AppState.START_BACKUP.ordinal + 1}/$AMOUNT_OF_STATES"
        }
