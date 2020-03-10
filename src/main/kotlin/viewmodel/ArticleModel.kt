package viewmodel

import model.Article
import tornadofx.ItemViewModel


class ArticleModel : ItemViewModel<Article>() {
    val id = bind(Article::id)
    val title = bind(Article::title)
    val text = bind(Article::text)
    val rubric = bind(Article::rubric)
    val priority = bind(Article::priority)
    val targetGroup = bind(Article::targetGroup)
    val supportType = bind(Article::supportType)
    val subject = bind(Article::subject)
    val state = bind(Article::state)
    val archiveDate = bind(Article::archiveDate)
    val recurrentInfo = bind(Article::recurrentInfo)
    val applicationDeadline = bind(Article::applicationDeadline)
    val contactPartner = bind(Article::contactPartner)
    val childArticle = bind(Article::childArticle)
    val parentArticle = bind(Article::parentArticle)


    override fun onCommit() {
        item = Article(
            id = id.value,
            title = title.value,
            text = text.value,
            rubric=  rubric.value,
            priority = priority.value,
            targetGroup = targetGroup.value,
            supportType = supportType.value,
            subject = subject.value,
            state = state.value,
            archiveDate = archiveDate.value,
            recurrentInfo = recurrentInfo   .value,
            applicationDeadline = applicationDeadline.value,
            contactPartner = contactPartner.value,
            childArticle = childArticle.value,
            parentArticle = parentArticle.value
        )
    }

}
