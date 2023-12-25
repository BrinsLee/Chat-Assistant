package com.brins.lib_base.extensions

import android.graphics.Typeface
import android.text.Spannable
import android.text.SpannableString
import android.text.style.StyleSpan

internal fun String.getOccurrenceRanges(items: List<String>? = null, ignoreCase: Boolean = false): List<IntRange> {
    val regexOptions: Set<RegexOption> = setOfNotNull(RegexOption.IGNORE_CASE.takeIf { ignoreCase })
    return items?.flatMap { item ->
        Regex(item, regexOptions).findAll(this).map { it.range }
    } ?: listOf(0 until length)
}
internal fun String.applyTypeface(typeface: Int, ranges: List<IntRange>): SpannableString =
    SpannableString(this).apply {
        ranges.forEach {
            setSpan(StyleSpan(typeface), it.first, it.last + 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        }
    }

internal fun String.bold(items: List<String>? = null, ignoreCase: Boolean = false): SpannableString =
    applyTypeface(Typeface.BOLD, getOccurrenceRanges(items, ignoreCase))

internal fun String.italicize(items: List<String>? = null, ignoreCase: Boolean = false): SpannableString =
    applyTypeface(Typeface.ITALIC, getOccurrenceRanges(items, ignoreCase))