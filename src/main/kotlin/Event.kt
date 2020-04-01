import model.Article


sealed class Event

sealed class ErrorEvent(val ex: Throwable) : Event() {

    data class CouldNotCreateArticle(val article: Article, val err: Throwable) : ErrorEvent(err)
    data class CouldNotUpdateArticle(val article: Article, val err: Throwable) : ErrorEvent(err)

    data class Undefined(val err: Throwable) : ErrorEvent(err)

}

sealed class InfoEvent : Event() {

    object AppStarted : InfoEvent()
    class ArticleSaved(val article: Article) : InfoEvent()
    class ArticleCreated(val article: Article) : InfoEvent()
}