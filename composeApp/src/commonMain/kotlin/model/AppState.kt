package model

enum class AppState {
    START,
    SEARCHING_ROOT,
    SEARCHING_FILE,
    WAIT_FOR_ACKNOWLEDGE,
    START_BACKUP,
}
