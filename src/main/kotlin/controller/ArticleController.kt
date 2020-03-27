package controller

import ErrorEvent
import TyphoonEventBus
import arrow.core.Either
import arrow.core.extensions.either.applicativeError.handleErrorWith
import model.Article
import mu.KotlinLogging
import org.koin.core.KoinComponent
import repository.ArticleRepository
import repository.PrimaryKey
import repository.Result
import repository.ValidArticle
import tornadofx.Controller
import validation.ArticleValidator
import org.koin.core.inject as insert

private val logger = KotlinLogging.logger {}

object ArticleController : KoinComponent, Controller() {

    private val articleRepository: ArticleRepository by insert()
    private val validator: ArticleValidator by insert()

    /**
     * Saves the given article to the database without performing any validation
     * @param article Article
     */
    suspend fun saveArticle(article: ValidArticle): Result<PrimaryKey<Article>> =
        articleRepository.update(article).handleErrorWith {
            TyphoonEventBus += ErrorEvent.CouldNotUpdateArticle(article.a, it)
            Either.left(it)
        }

    /**
     * Creates the given article in the database
     * @param article Article
     */
    suspend fun createArticle(article: ValidArticle): Result<Article> =
        articleRepository.create(article).handleErrorWith {
            TyphoonEventBus += ErrorEvent.CouldNotCreateArticle(article.a, it)
            Either.left(it)
        }

}
