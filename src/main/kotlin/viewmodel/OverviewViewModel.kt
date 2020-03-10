package viewmodel


import javafx.collections.FXCollections
import model.Article
import tornadofx.ItemViewModel

class OverviewModel(private val backingArticles: Collection<Article>) : ItemViewModel<Collection<Article>>() {


    var articles = FXCollections.observableArrayList(backingArticles)


}