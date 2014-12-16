package net.coacoas.gradle.plugins

/**
 * Transform a configuration object into stringified Lisp expressions
 */
class SExp {
    static String format(Object object) {
        if (object instanceof Map) {
            return '(' + object.entrySet().collect { ":${it.key} " + format(it.value)}.join('\n') + ')'
        } else if (object instanceof List) {
            return '(' + object.collect { format(it) }.join(' ') + ')'
        } else {
            return '"' + object.toString().replaceAll("\\\\", "\\\\\\\\") + '"'
        }
    }
}

