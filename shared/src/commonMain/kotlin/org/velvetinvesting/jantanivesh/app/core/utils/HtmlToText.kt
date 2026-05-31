package org.velvetinvesting.jantanivesh.app.core.utils

fun String.parseHtmlToReadableText(): String {
    var text = this

    // Normalize line breaks first
    text = text.replace("<br>", "\n")
        .replace("<br/>", "\n")
        .replace("<br />", "\n")

    // Paragraph handling
    text = text.replace("<p>", "")
        .replace("</p>", "\n\n")

    // List handling
    text = text.replace("<ul>", "")
        .replace("</ul>", "\n")

    text = text.replace("<ol>", "")
        .replace("</ol>", "\n")

    // List items → bullet points
    text = text.replace("<li>", "• ")
        .replace("</li>", "\n")

    // Remove all remaining HTML tags
    text = text.replace(Regex("<.*?>"), "")

    // Decode common HTML entities
    text = text.replace("&nbsp;", " ")
        .replace("&amp;", "&")
        .replace("&lt;", "<")
        .replace("&gt;", ">")

    // Clean extra spaces / newlines
    text = text.lines().joinToString("\n") { it.trim() }
        .replace(Regex("\n{3,}"), "\n\n")

    return text.trim()
}
