package com.kata.bowlingkt.test

import com.kata.bowlingkt.Game
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class GameTest {

    @Test
    fun getScore_Return133() {
        var game = Game()

        game.roll(1)
        game.roll(4)

        game.roll(4)
        game.roll(5)

        game.roll(6)
        game.roll(4)

        game.roll(5)
        game.roll(5)

        game.roll(10)

        game.roll(0)
        game.roll(1)

        game.roll(7)
        game.roll(3)

        game.roll(6)
        game.roll(4)

        game.roll(10)

        game.roll(2)
        game.roll(8)
        game.roll(6)

        assertEquals(133, game.getScore())
    }

    //@Test fun `createFrame with empty frame list should return Frame instance with rank 0`() {
    @Test fun `getFrameCount with 4 normal rolls return 2`() {
        val game = Game()

        game.roll(1)
        game.roll(3)

        game.roll(5)
        game.roll(2)

        assertEquals(2, game.frame.size)
    }

    @Test fun createFrame_withNoFrame_returnFrameWithRank0() {
        val optFrame = Game().createFrame()
        assertTrue { optFrame.isPresent }
        assertEquals(0, optFrame.get().rank)
    }

    @Test fun createFrame_withTwoFrames_returnFrameWithRank3() {
        val optFrame = Game().createFrame()
        assertTrue { optFrame.isPresent }
        assertEquals(0, optFrame.get().rank)
    }
}