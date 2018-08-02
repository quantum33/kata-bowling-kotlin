package com.kata.bowlingkt

class Roll {

    constructor(downPinCount: Int) {
        if (downPinCount !in 0..Constants.MAX_PIN_COUNT) {
            throw IllegalArgumentException("downPinCount value $downPinCount is out of bounds (${0..Constants.MAX_PIN_COUNT})")
        }
        this.downPinCount = downPinCount
    }

    val downPinCount: Int
}