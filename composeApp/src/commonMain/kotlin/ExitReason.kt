import kotlin.system.exitProcess

enum class ExitReason {
    EXIT_AFTER_CHILD_PROCESS_REPORTS_ZERO,
    EXIT_AFTER_CHILD_PROCESS_REPORTS_NONZERO,
    EXIT_USER_ABORTED,
    EXIT_AFTER_CHILD_PROCESS_LAUNCH_CAUSES_EXCEPTION,
}

fun exitProcessWithReason(reason: ExitReason): ExitReason {
    when (reason) {
        ExitReason.EXIT_AFTER_CHILD_PROCESS_REPORTS_ZERO -> exitProcess(0)
        ExitReason.EXIT_AFTER_CHILD_PROCESS_REPORTS_NONZERO -> exitProcess(-1)
        ExitReason.EXIT_USER_ABORTED -> exitProcess(1)
        ExitReason.EXIT_AFTER_CHILD_PROCESS_LAUNCH_CAUSES_EXCEPTION -> exitProcess(-2)
    }
}
