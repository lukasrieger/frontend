package util


import ErrorEvent
import TyphoonEventBus
import arrow.fx.IO
import arrow.fx.extensions.fx
import javafx.scene.control.ListView
import javafx.scene.control.TableView
import repository.QueryResult
import typhoonErrorHandler


fun <T> TableView<T>.itemsIO(producer: suspend () -> QueryResult<T>) =
    IO.fx { !effect { producer() } }
        .unsafeRunAsync { result ->
            result.fold({
                typhoonErrorHandler("Konnte TableView nicht laden", it)
                TyphoonEventBus += ErrorEvent.Undefined(it)
            }, { (_, articles) ->
                items.setAll(articles)
            })
        }

fun <T> ListView<T>.itemsIO(producer: suspend () -> Collection<T>) =
    IO.fx { !effect { producer() } }
        .unsafeRunAsync { result ->
            result.fold({
                typhoonErrorHandler("Konnte ListView nicht laden", it)
                TyphoonEventBus += ErrorEvent.Undefined(it)
            }, {
                items.setAll(it)
            })
        }

