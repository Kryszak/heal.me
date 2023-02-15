package net.kryszak.healme.visit

class VisitTimeAlreadyTakenException : Exception {
    constructor() : super()
    constructor(message: String) : super(message)
}