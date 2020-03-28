package util


import TyphoonEventBus
import arrow.fx.ForIO
import arrow.fx.IO
import arrow.fx.extensions.fx
import arrow.fx.typeclasses.ConcurrentSyntax
import javafx.scene.control.ListView
import javafx.scene.control.TableView
import repository.QueryResult
import typhoonErrorHandler


fun <T> TableView<T>.itemsIO(producer: suspend ConcurrentSyntax<ForIO>.() -> QueryResult<T>) =
    IO.fx(producer)
        .unsafeRunAsync { result ->
            result.fold({
                typhoonErrorHandler("Konnte TableView nicht laden", it)
                TyphoonEventBus += ErrorEvent.Undefined
            },{ (articles) ->
                items.setAll(articles)
            })
        }

fun <T> ListView<T>.itemsIO(producer: suspend ConcurrentSyntax<ForIO>.() -> Collection<T>) =
    IO.fx(producer)
        .unsafeRunAsync { result ->
            result.fold({
                typhoonErrorHandler("Konnte ListView nicht laden", it)
                TyphoonEventBus += ErrorEvent.Undefined
            },{
                items.setAll(it)
            })
        }

