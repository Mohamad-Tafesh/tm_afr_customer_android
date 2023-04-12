package com.tedmob.afrimoney.util.html

import android.os.Build
import android.text.Html
import android.text.Spanned
import com.tedmob.afrimoney.util.html.FakeTags.DD
import com.tedmob.afrimoney.util.html.FakeTags.LI
import com.tedmob.afrimoney.util.html.FakeTags.OL
import com.tedmob.afrimoney.util.html.FakeTags.UL

//Html.FROM_HTML_MODE_LEGACY: adds \n\n between blocks, and at the end
//Html.FROM_HTML_MODE_COMPACT: adds \n between blocks, and at the end (only above SDK 24, or Android 7.0)

inline fun String.html(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY)
    else
        Html.fromHtml(this)

inline fun String.html(tagHandler: Html.TagHandler): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this, Html.FROM_HTML_MODE_LEGACY, null, tagHandler)
    else
        Html.fromHtml(this, null, tagHandler)

inline fun String.htmlList(): Spanned =
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
        Html.fromHtml(this.swapHtmlListTags(), Html.FROM_HTML_MODE_LEGACY, null, ListTagHandler())
    else
        Html.fromHtml(this.swapHtmlListTags(), null, ListTagHandler())

inline fun String.swapHtmlListTags(): String {
    return replace("<ul", "<$UL")
        .replace("</ul>", "</$UL>")
        .replace("<ol", "<$OL")
        .replace("</ol>", "</$OL>")
        .replace("<li", "<$LI")
        .replace("</li>", "</$LI>")
        .replace("<dd", "<$DD")
        .replace("</dd>", "</$DD>")
}