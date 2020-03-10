import javafx.application.Application
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import model.Article
import org.jetbrains.exposed.sql.selectAll
import org.koin.core.KoinComponent
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import repository.*
import repository.dao.ArticlesTable
import repository.extensions.createRecurrentArticles
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




    // this is redundant but sadly necessary because tornadofx throws an exception when closing the application
    // if no primaryView was specified
    override val primaryView = LoadingScreen::class

    init {
        startup()
    }


    private fun startup() {
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

    Application.launch(TyphoonApp::class.java) }