package controller

import ErrorEvent
import arrow.core.Either
import arrow.core.ValidatedNel
import model.Article
import mu.KotlinLogging
import org.koin.core.KoinComponent
import repository.ArticleRepository
import repository.PrimaryKey
import repository.ValidArticle
import repository.mapAsyncV
import tornadofx.Controller
import validation.ArticleValidationError
import validation.ArticleValidator
import org.koin.core.inject as insert


private val logger = KotlinLogging.logger {}

object ArticleController : KoinComponent, Controller() {

    private val articleRepository: ArticleRepository by insert()
    private val validator: ArticleValidator by insert()

    /**
     * Saves the given article to the database without performing any validation
     * @param article Article
     * @return Completable
     */
    suspend fun saveArticle(article: ValidArticle): Either<ErrorEvent.CouldNotUpdateArticle, PrimaryKey<Article>> =
        articleRepository.update(article).mapLeft {
            ErrorEvent.CouldNotUpdateArticle(article.a, it)
        }

    /**
     * Attempt to validate the given article first, if the validation succeeds save the article in the database,
     * return the validation errors otherwise
     * @param article Article
     * @return ValidatedNel<ArticleValidationError, Result<PrimaryKey<Article>>>
     */
    suspend fun validateAndSaveArticle(
        article: Article
    ): ValidatedNel<ArticleValidationError, Either<ErrorEvent.CouldNotUpdateArticle, PrimaryKey<Article>>> =
        validateMap(article, ::saveArticle)


    /**
     * Creates the given article in the database
     * @param article Article
     */
    suspend fun createArticle(article: ValidArticle): Either<ErrorEvent.CouldNotCreateArticle, Article> =
        articleRepository.create(article).mapLeft { ErrorEvent.CouldNotCreateArticle(article.a, it) }

    /**
     * Attempt to validate the given article first, if the validation succeeds create the article in the database,
     * return the validation errors otherwise
     * @param article Article
     * @return ValidatedNel<ArticleValidationError, Result<Article>>
     */
    suspend fun validateAndCreateArticle(
        article: Article
    ): ValidatedNel<ArticleValidationError, Either<ErrorEvent.CouldNotCreateArticle, Article>> =
        validateMap(article, ::createArticle)


    private suspend fun <T, V> validateMap(
        article: Article,
        f: suspend (ValidArticle) -> Either<T, V>
    ): ValidatedNel<ArticleValidationError, Either<T, V>> =
        validator
            .validate(article)
            .mapAsyncV(f)

}

