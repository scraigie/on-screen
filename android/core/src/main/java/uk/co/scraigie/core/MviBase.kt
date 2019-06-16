package uk.co.scraigie.core

import io.reactivex.functions.BiFunction

interface MviIntent

interface MviAction

interface MviResult

interface MviState

interface MviReducer<S : MviState, R : MviResult> : BiFunction<S,R,S>