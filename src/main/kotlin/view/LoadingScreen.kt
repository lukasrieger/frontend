package view

import javafx.scene.Parent
import tornadofx.*
import view.editor.ArticleEditor

class LoadingScreen : View() {

    override val root: Parent =
        vbox {
            label("Work in progress")
            button("Switch to Overview") {
                action {
                    replaceWith<ArticleEditor>()
                }
            }
        }

}