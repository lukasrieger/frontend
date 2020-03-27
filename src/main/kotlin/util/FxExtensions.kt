package util


import arrow.fx.ForIO
import arrow.fx.typeclasses.ConcurrentSyntax
import javafx.scene.control.ListView
import javafx.scene.control.TableView
import repository.QueryResult


fun <T> TableView<T>.itemsIO(producer: suspend ConcurrentSyntax<ForIO>.() -> QueryResult<T>) =
    IO.fx(producer)
        .unsafeRunAsync { result ->
            result.fold({
                it.printStackTrace()
                TyphoonEventBus += ErrorEvent.Undefined
            }, { (articles) ->
                items.setAll(articles)
            })
        }

fun <T> ListView<T>.itemsIO(producer: suspend ConcurrentSyntax<ForIO>.() -> Collection<T>) =
    IO.fx(producer)
        .unsafeRunAsync { result ->
            result.fold({
                it.printStackTrace()
                TyphoonEventBus += ErrorEvent.Undefined
            }, {
                items.setAll(it)
            })
        }


fun test() {

    val x = QueryResult(1, listOf("Lele"))

}