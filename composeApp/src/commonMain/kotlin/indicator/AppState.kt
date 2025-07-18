package indicator

import model.AppState

// Todo make another flow for the license info
val AMOUNT_OF_STATES = AppState.entries.size - 1

fun appStateToIndicator(appState: AppState): String =
    "Stap " +
        when (appState) {
            AppState.START -> "${AppState.START.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.SEARCHING_ROOT -> "${AppState.SEARCHING_ROOT.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.SEARCHING_FILE -> "${AppState.SEARCHING_FILE.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.WAIT_FOR_ACKNOWLEDGE -> "${AppState.WAIT_FOR_ACKNOWLEDGE.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.START_BACKUP -> "${AppState.START_BACKUP.ordinal + 1}/$AMOUNT_OF_STATES"
            AppState.LICENSE -> "${AppState.LICENSE.ordinal + 1}/$AMOUNT_OF_STATES"
        }
