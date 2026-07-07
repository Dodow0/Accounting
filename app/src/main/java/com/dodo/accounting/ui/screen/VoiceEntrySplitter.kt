package com.dodo.accounting.ui.screen

internal object VoiceEntrySplitter {
    fun split(text: String): List<String> {
        val explicitParts = text
            .split(Regex("""[，,。；;\n]+|(?:然后|另外|还有|接着)"""))
            .map { it.trim() }
            .filter { it.isNotBlank() }
        val parts = if (explicitParts.size > 1) explicitParts else splitCompact(text)
        return parts.ifEmpty { listOf(text) }
    }

    private fun splitCompact(text: String): List<String> {
        val matches = VoiceAmountParser.amountSegmentRegex
            .findAll(text.normalizeVoiceText())
            .filter { it.value.isLikelyAmountSegment() && VoiceAmountParser.parseAmountSegment(it.value) != null }
            .toList()
        if (matches.size <= 1) return listOf(text.trim()).filter { it.isNotBlank() }
        return matches.mapIndexed { index, match ->
            val start = if (index == 0) 0 else matches[index - 1].range.last + 1
            val end = match.range.last + 1
            text.substring(start, end).trim()
        }.filter { it.isNotBlank() }
    }
}
