package controller

import ErrorEvent
import arrow.core.ValidatedNel
import model.Article
import mu.KotlinLogging
import org.koin.core.KoinComponent
import processErrorWith
import repository.*
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
    suspend fun saveArticle(article: ValidArticle): Result<PrimaryKey<Article>> =
        articleRepository.update(article).processErrorWith { ErrorEvent.CouldNotUpdateArticle(article.a, it) }

    /**
     * Attempt to validate the given article first, if the validation succeeds save the article in the database,
     * return the validation errors otherwise
     * @param article Article
     * @return ValidatedNel<ArticleValidationError, Result<PrimaryKey<Article>>>
     */
    suspend fun validateAndSaveArticle(article: Article): ValidatedNel<ArticleValidationError, Result<PrimaryKey<Article>>> =
        validator
            .validate(article)
            .mapAsyncV(::saveArticle)


    /**
     * Creates the given article in the database
     * @param article Article
     */
    suspend fun createArticle(article: ValidArticle): Result<Article> =
        articleRepository.create(article).processErrorWith { ErrorEvent.CouldNotCreateArticle(article.a, it) }

    /**
     * Attempt to validate the given article first, if the validation succeeds create the article in the database,
     * return the validation errors otherwise
     * @param article Article
     * @return ValidatedNel<ArticleValidationError, Result<Article>>
     */
    suspend fun validateAndCreateArticle(article: Article): ValidatedNel<ArticleValidationError, Result<Article>> =
        validator
            .validate(article)
            .mapAsyncV(::createArticle)


}
