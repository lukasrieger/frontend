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
import org.koin.core.inject as insert

const val articlesPerPage = 40

class OverviewController : KoinComponent, Controller() {

    private val articleRepository: ArticleRepository by insert()
    private val contactRepository: ContactReader by insert()

    suspend fun getArticles(page: Int, query: Query? = null) =
        articleRepository.byQuery(
            query = query ?: ArticlesTable.selectAll(),
            limit = articlesPerPage,
            offset = articlesPerPage * page
        )

    suspend fun getContactPartners() = contactRepository.getContactPartners()

    suspend fun testCreate() {
        for (it in 1..20) {
            val article = Article(
                title = "Test $it",
                text = "Text of Article $it",
                rubric = Rubric.BMBF,
                priority = 1,
                targetGroup = TargetGroup.Experienced,
                supportType = SupportType.Monetized,
                subject = Subject.LIFE_SCIENCES,
                state = ArticleState.Corrected,
                archiveDate = DateTime.now().plusMonths(4),
                recurrentInfo = None,
                applicationDeadline = DateTime.now().plusMonths(2),
                contactPartner = None,
                childArticle = None,
                parentArticle = None
            )

            // articleRepository.create(article)
        }
    }
}
