package com.github.nomisrev

import arrow.core.*
import arrow.data.*
import arrow.typeclasses.*
import arrow.instances.*

typealias Stack = Option<Nel<String>>

fun pop() = State<Stack, Option<String>> { stack ->
    stack.fold(
        ifEmpty = { None toT None },
        ifSome = { Nel.fromList(it.tail) toT it.head.some() }
    )
}

fun push(s: String) = State<Stack, Unit> { stack ->
    stack.fold(
        ifEmpty = { Nel.of(s).some() toT Unit },
        ifSome = { Nel(s, it.all).some() toT Unit }
    )
}

fun stackOperations() = ForState<Stack>() extensions {
//        binding<StatePartialOf<Stack>, Option<String>> {  //Works fine
    binding { // Should work fine, IDEA claims it does.
        val a = push("a").bind()
        val b = pop().bind()
        val c = pop().bind()
        c
    }.fix()
}

fun main(args: Array<String>) {
    stackOperations()
        .run(Nel("one", "two").some())
        .let(::println)
}