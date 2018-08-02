package com.kata.bowlingkt

import java.util.*
import javax.naming.OperationNotSupportedException

class Frame {

    constructor(rank: Int) {

        if (rank !in 0 until Constants.MAX_FRAME_COUNT) {
            throw IllegalArgumentException("rank value $rank is not valid")
        }
        this.rank = rank
    }

    val rank: Int

    val rolls: List<Roll> get() = _rolls

    fun addRoll(roll: Roll): Frame {
        if (this.notIsTenthFrame()) {
            _rolls.add(roll)
        } else if (isTenthFrame() && !isMaxRollLimitReached()) {
            _rolls.add(roll)
        } else if (isTenthFrame() && isEligibleForExtraBall()) {
            _rolls.add(roll)
        } else if (isTenthFrame() && isMaxRollLimitReached()) {
            //todo: custom exceptions
            throw IllegalArgumentException("cannot add a new roll to the last: maximum roll limit is reached")
        } else {
            throw IllegalArgumentException("cannot add a new roll to the currant frame. Frame rank is [$rank] and roll count [${_rolls.size}]")
        }

        return this
    }

    fun addRollWith(downPinCount: Int): Frame = addRoll(Roll(downPinCount))

    fun getScore(nextFrame: Optional<Frame>): Int = getDownPinCount() + getBonus(nextFrame)

    fun isTenthFrame(): Boolean = rank == Constants.MAX_FRAME_COUNT - 1

    fun notIsTenthFrame(): Boolean = !isTenthFrame()

    fun isStrike(): Boolean = isAllPinDown() && hasSingleRoll()

    fun isSpare(): Boolean = isAllPinDown() && !hasSingleRoll()

    fun isMaxRollLimitReached() = rolls.count() == Constants.MAX_ROLL_COUNT

    internal fun isEligibleForExtraBall() = isTenthFrame() && (isStrike() || isSpare())

    internal fun getBonus(nextFrame: Optional<Frame>): Int =
        if (this.isStrike() && nextFrame.get().isTenthFrame()) {
            nextFrame.get().rolls.take(2).sumBy { it.downPinCount }
        } else if (this.isStrike() && nextFrame.get().notIsTenthFrame()) {
            nextFrame.get().rolls.sumBy { it.downPinCount }
        } else if (this.isSpare()) {
            nextFrame.get().rolls.firstOrNull()?.downPinCount ?: 0
        } else 0

    private fun getDownPinCount(): Int = rolls.sumBy { it.downPinCount }

    private fun isAllPinDown() = getDownPinCount() == Constants.MAX_PIN_COUNT

    private fun hasSingleRoll(): Boolean = rolls.count() == 1

    private val _rolls: MutableList<Roll> = mutableListOf()
}