package view

import javafx.scene.Parent
import tornadofx.*

class LoadingScreen : View() {

    override val root: Parent =
        vbox {
            label("Work in progress")
            button("Switch to Overview") {
                action {
                    replaceWith<ArticleOverview>()
                }
            }
        }

}