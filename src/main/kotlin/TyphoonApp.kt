import javafx.application.Application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import repository.ArticleWriter
import repository.DbSettings
import repository.articleModule
import repository.contactModule
import tornadofx.App
import validation.validationModule
import view.LoadingScreen
import org.koin.core.inject as insert


private val modules: List<Module> = listOf(
    articleModule,
    contactModule,
    validationModule
)


class TyphoonApp : App(primaryView = LoadingScreen::class), KoinComponent {

    private val articleRepository: ArticleWriter by insert()
    override val primaryView = LoadingScreen::class

    init {
        setupDatabase()
        registerHandlers()
    }


    private fun registerHandlers() {
        GlobalScope.launch {
            // ArticlesTable.selectAll()

        }
    }

    private fun setupDatabase() = DbSettings.setup()


}

fun main() {
    startKoin {
        modules(modules)
    }
    Application.launch(TyphoonApp::class.java)
}