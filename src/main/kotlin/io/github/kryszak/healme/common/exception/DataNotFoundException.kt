package io.github.kryszak.healme.common.exception

class DataNotFoundException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}
