package view

import controller.OverviewController
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.control.TabPane
import model.Article
import tornadofx.SmartResize
import tornadofx.View


class ArticleOverview : View() {


    private val articles: List<Article> = listOf()
    private val interactor: OverviewController by inject()


    override val root: Parent =
        borderpane {
            top = menubar {
                menu("Allgemein")
            }
            center = tabpane {
                tab("1") {
                    isClosable = false
                    tableview<Article> {
                        readonlyColumn("ID", Article::id).weightedWidth(0.3)
                        readonlyColumn("Name", Article::title).weightedWidth(2.0)
                        readonlyColumn("Rubric", Article::rubric).weightedWidth(1.0)
                        readonlyColumn("Priority", Article::rubric).weightedWidth(1.0)
                        readonlyColumn("Target Group", Article::targetGroup).weightedWidth(1.0)
                        readonlyColumn("State", Article::state).weightedWidth(1.0)
                        readonlyColumn("Archive Date", Article::archiveDate).weightedWidth(1.0)
                        readonlyColumn("Support Type", Article::supportType).weightedWidth(1.0)
                        readonlyColumn("HasChildArticle", Article::childArticle).weightedWidth(1.0)
                        val expander = rowExpander(true) {
                            label(
                                """
                                Lorem ipsum dolor sit amet, consetetur sadipscing elitr, 
                                sed diam nonumy eirmod tempor invidunt ut labore et dolore 
                                magna aliquyam erat, sed diam voluptua. At vero eos et accusam 
                                et justo duo dolores et ea rebum. Stet clita kasd gubergren, no 
                                sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum 
                                dolor sit amet, consetetur sadipscing elitr, sed diam nonumy 
                                eirmod tempor invidunt ut labore et dolore magna aliquyam erat, 
                                sed diam voluptua. At vero eos et accusam et justo duo dolores et 
                                ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est 
                                Lorem ipsum dolor sit amet.
                            """.trimIndent()
                            )
                        }

                        expander.isVisible = false

                        columnResizePolicy = SmartResize.POLICY


                    }.itemsIO {

                        val (items) = effect { interactor.getArticles(this@tabpane.currentPage) }

                        items
                    }
                }
            }
            bottom = hbox {

                hbox {
                    label(articles.size.toString()) { alignment = Pos.BOTTOM_LEFT }

                    alignment = Pos.BOTTOM_LEFT
                }

                progressbar(1.0) {
                    contextmenu {

                        customitem {
                            vbox {
                                label("Old progress")
                                progressbar(0.5)
                            }
                        }
                    }

                    setOnMouseClicked {
                        contextMenu.show(this.scene.window, it.screenX, it.screenY)
                    }


                    alignment = Pos.BOTTOM_RIGHT
                }
            }
        }


}


private val TabPane.currentPage
    get() = selectionModel.selectedIndex