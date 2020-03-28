package view.editor

import arrow.fx.IO
import arrow.fx.extensions.fx
import com.vladsch.flexmark.ast.Emphasis
import com.vladsch.flexmark.ast.Heading
import com.vladsch.flexmark.ast.StrongEmphasis
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.ast.Node
import com.vladsch.flexmark.util.ast.NodeVisitor
import com.vladsch.flexmark.util.ast.VisitHandler
import com.vladsch.flexmark.util.data.MutableDataSet
import kotlinx.coroutines.Dispatchers
import org.fxmisc.richtext.model.StyleSpans
import org.fxmisc.richtext.model.StyleSpansBuilder


enum class Styles {
    Bold,
    Cursive,
    Heading
}

class MarkdownMatcher {


    private var lastEnd = 0
    private val styleBuilder = StyleSpansBuilder<String>()

    private val styles = mapOf(
        Styles.Bold to "-fx-font-weight: bold;",
        Styles.Cursive to "-fx-font-style: italic;",
        Styles.Heading to "-fx-font-weight: bold;"
    )

    private fun handleNode(node: Node) {
        val style = when (node) {
            is StrongEmphasis -> styles[Styles.Bold]
            is Emphasis -> styles[Styles.Cursive]
            is Heading -> styles[Styles.Heading]
            else -> ""
        }

        val startOffset = node.startOffset
        val endOffset = node.endOffset

        styleBuilder.add("", startOffset - lastEnd)
        styleBuilder.add(style, endOffset - startOffset)

        lastEnd = endOffset

    }


    fun highlightMarkdown(doc: Document): StyleSpans<String> {
        val visitors = NodeVisitor(
            VisitHandler(StrongEmphasis::class.java, ::handleNode),
            VisitHandler(Emphasis::class.java, ::handleNode),
            VisitHandler(Heading::class.java, ::handleNode)
        )


        visitors.visit(doc)
        styleBuilder.add("", doc.textLength - lastEnd)

        return styleBuilder.create()
    }


}

fun highlightMarkdown(doc: Document) = MarkdownMatcher().highlightMarkdown(doc)


object MarkdownHandlerIO {

    private val renderer = HtmlRenderer.builder().build()

    private val parser = Parser.builder(
        MutableDataSet().also {
            it[HtmlRenderer.SOFT_BREAK] = "<br />\n"
        }).build()


    fun handleTextChange(content: String) = IO.fx {

        continueOn(Dispatchers.Default)

        val parsed = !effect { parser.parse(content) }

        val renderIO = effect { renderer.render(parsed) }
        val highlightIO = effect { highlightMarkdown(parsed) }

        val (renderAndHighlightPar) = dispatchers()
            .default()
            .parMapN(
                renderIO,
                highlightIO, ::Pair
            )

        renderAndHighlightPar

    }
}


