import javafx.scene.control.ButtonType
import mu.KotlinLogging


object TyphoonEventBus {
    operator fun plusAssign(event: Event): Unit = TODO("Event subsystem not yet implemented.")
}

private val logger = KotlinLogging.logger {}
val typhoonErrorHandler: (String, Throwable) -> Unit = { message, err ->
    logger.error(err) { message }
    tornadofx.error(
        header = "Ein Fehler ist aufgetreten",
        content = """Die Anwendung hat einen Fehler festgestellt.
                    |Falls Fehler-Logging aktiviert ist, wurde der Fehler in der angegebenen Log-Datei festgehalten.
                    |Unter Umständen ist ein fehlerfreier Betrieb der Anwendung nicht länger möglich.
                    |
                    |Drücken sie auf OK um die Anwendung zu schließen.
                    |
                    |
                    |
                    |
                """.trimMargin(),
        actionFn = {
            when (it) {
                ButtonType.OK -> {
                }
                else -> {
                }
            }
        }
    )
}

inline fun <reified T> launchCatching(message: String, co: () -> T) =
    try {
        co()
    } catch (e: Throwable) {
        typhoonErrorHandler(message, e)
        throw e
    }