package com.tedmob.africell.util.html

import android.text.Editable
import android.text.Html
import com.tedmob.africell.util.html.FakeTags.LI
import com.tedmob.africell.util.html.FakeTags.OL
import com.tedmob.africell.util.html.FakeTags.UL
import org.xml.sax.XMLReader

class ListTagHandler : Html.TagHandler {
    private var first = true
    private var parent: String? = null
    private var index = 1
    override fun handleTag(opening: Boolean, tag: String, output: Editable,
                           xmlReader: XMLReader) {
        if (tag == UL || tag == OL) {
            parent = tag
        }

        if (tag == LI) {
            if (parent == UL) {
                appendUnordered(output)
            } else {
                appendOrdered(output)
            }
        }
    }

    private fun appendOrdered(output: Editable) {
        if (first) {
            val prefix = if(index == 1) "" else "\n\n"
            output.append("$prefix${index++}. ")
        }
        first = !first
    }

    private fun appendUnordered(output: Editable) {
        if (first) {
            val prefix = if(index == 1) "" else "\n\n"
            output.append("$prefix• ")
            index++
        }
        first = !first
    }

}
