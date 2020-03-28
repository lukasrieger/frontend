import model.Article


sealed class Event

sealed class ErrorEvent : Event() {

    class CouldNotCreateArticle(article: Article, ex: Throwable) : ErrorEvent()
    class CouldNotUpdateArticle(article : Article, ex: Throwable) : ErrorEvent()

    object Undefined : ErrorEvent()

}

sealed class InfoEvent : Event() {

        object AppStarted : InfoEvent()
        class ArticleSaved(val article: Article) : InfoEvent()
        class ArticleCreated(val article: Article) : InfoEvent()
}