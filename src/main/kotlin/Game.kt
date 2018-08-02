package com.kata.bowlingkt

import java.util.*
import javax.activity.InvalidActivityException

class Game {

    val frame: List<Frame> get() = _frames

    fun roll(numberOfDownPins: Int) {
        val currentRoll = Roll(numberOfDownPins)
        getOrAddFrame().addRoll(currentRoll)
    }

    fun getScore(): Int {
        validateForScoring()
        return _frames.sumBy { frame ->
            frame.getScore(getNextFrame(frame))
        }
    }

    fun createFrame(): Optional<Frame> =
            if (_frames.hasNoElement()) {
                Optional.of(Frame(0))
            } else if (_frames.last().notIsTenthFrame() && _frames.last().isMaxRollLimitReached()) {
                Optional.of(Frame(_frames.last().rank + 1))
            } else if (_frames.last().notIsTenthFrame() && _frames.last().isStrike()) {
                Optional.of(Frame(_frames.last().rank + 1))
            } else {
                Optional.empty()
            }

    private fun validateForScoring() {
        if (frame.size != Constants.MAX_FRAME_COUNT) {
            throw InvalidActivityException("maximum frame count is NOT reached (${frame.size}). You must continue gaming!")
        }
    }

    private fun getOrAddFrame(): Frame {
        createFrame().ifPresent {
            _frames.add(it)
        }
        return _frames.last()
    }

    private fun getNextFrame(currentFrame: Frame): Optional<Frame> =
            if (currentFrame.isTenthFrame()) {
                Optional.empty()
            } else {
                Optional.of(_frames.elementAt(currentFrame.rank + 1))
            }

    private fun List<Frame>.hasNoElement(): Boolean = !this.any()

    private val _frames: MutableList<Frame> = mutableListOf()
}