package controller

import arrow.core.None
import model.*
import org.jetbrains.exposed.sql.Query
import org.jetbrains.exposed.sql.selectAll
import org.joda.time.DateTime
import org.koin.core.KoinComponent
import repository.ArticleRepository
import repository.ContactReader
import repository.dao.ArticlesTable
import repository.extensions.getContactPartners
import tornadofx.Controller
import util.long
import validation.ArticleValidator
import org.koin.core.inject as insert

class OverviewController : KoinComponent, Controller() {

    private val articlesPerPage = 40

    private val articleRepository: ArticleRepository by insert()
    private val contactRepository: ContactReader by insert()
    private val articleValidator: ArticleValidator by insert()


    suspend fun getArticles(page: Int, query: Query? = null) =
        articleRepository.byQuery(
            query = query ?: ArticlesTable.selectAll(),
            limit = articlesPerPage,
            offset = (articlesPerPage * page).long
        )

    suspend fun getContactPartners() = contactRepository.getContactPartners()

    suspend fun testCreate() = (1..20).forEach {
        val article = Article(
            title = "",
            text = "Text of Article $it",
            rubric = Rubric.BMBF,
            priority = Priority.Medium,
            targetGroup = TargetGroup.Experienced,
            supportType = SupportType.IndividualResearch,
            subject = Subject.LifeSciences,
            state = ArticleState.Corrected,
            archiveDate = DateTime.now().plusMonths(2),
            recurrentInfo = None,
            applicationDeadline = DateTime.now().plusMonths(4),
            contactPartner = None,
            childArticle = None,
            parentArticle = None
        )

        val articleOk = articleValidator.validate(article)
        articleOk.leftMap {
            it.all.forEach(::println)
            it
        }

        // articleRepository.create(article)
    }
}

