package view.editor

import arrow.fx.IO
import arrow.fx.extensions.fx
import javafx.scene.web.WebView
import kotlinx.coroutines.Dispatchers
import org.fxmisc.richtext.InlineCssTextArea
import tornadofx.View
import tornadofx.insets
import typhoonErrorHandler
import viewmodel.ArticleModel

class ArticleEditor : View("Article Editor") {

    private lateinit var editor: InlineCssTextArea


    private lateinit var webView: WebView


    private val articleModel: ArticleModel by inject()

    override val root = borderpane {

        center = hbox {
            this += InlineCssTextArea().also {
                editor = it
                it.setMinSize(800.0, 300.0)
                it.isWrapText = true
                it.updateOnChange()
                it.style = "-fx-font-family: consolas; -fx-font-size: 15px; -fx-font-smoothing-type: gray;"

            }
            separator()
            webview {
                engine.userStyleSheetLocation = "data:,body { font: 18px consolas; }"
                webView = this
            }

        }

        top = hbox {

            label(articleModel.title)
            label("TestLabel")

            button("BOLD") {
                insets(20, 20, 20, 20)
                action {
                    val boldText = "**${editor.selectedText}**"
                    editor.replaceSelection(boldText)
                }
            }


            separator()

            button("ITALIC") {
                insets(20, 20, 20, 20)
                action {
                    val italicText = "*${editor.selectedText}*"
                    editor.replaceSelection(italicText)
                }


            }
        }
    }


    private fun InlineCssTextArea.updateOnChange() {
        editor
            .textProperty()
            .onChange { content ->
                IO.fx {
                    val (rendered, styles) =
                        !MarkdownHandlerIO.handleTextChange(content ?: "")

                    continueOn(Dispatchers.Main)

                    if (styles.length() <= editor.text.length) {
                        setStyleSpans(0, styles)
                    }
                    webView.engine.loadContent(rendered)

                }

                    .unsafeRunAsync { either ->
                        either.fold(
                            ifLeft = { typhoonErrorHandler("Konnte Markdown-Highlighting nicht aktualisieren!", it) },
                            ifRight = {}
                        )
                    }

            }
    }
}
