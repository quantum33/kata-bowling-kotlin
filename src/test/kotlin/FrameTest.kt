import com.kata.bowlingkt.Constants
import com.kata.bowlingkt.Frame
import com.kata.bowlingkt.Roll
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import org.mockito.Spy
import java.util.*


class FrameTest {

    @Spy
    private var _frameSpy = Frame(0)

    @BeforeEach
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @AfterEach
    fun tearDown() {
    }

    @Test
    fun `constructor() when called with over ranked initializer throws exception`() {

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Frame(Constants.MAX_FRAME_COUNT)
        }
    }

    @Test
    fun `constructor(), when called, returns a frame with the specified rank and rolls property not null`() {

        val rank = (0..Constants.MAX_FRAME_COUNT).random()
        val frame = Frame(rank)

        assertNotNull(frame.rolls)
        assertEquals(rank, frame.rank)
    }

    @Test
    fun isSpare_with2RollsAt4And6Pins_returnTrue() {
        val frame = Frame(0)
            .addRoll(Roll(4))
            .addRoll(Roll(6))

        assertTrue { frame.isSpare() }
    }

    @Test
    fun isStrike_withOneRollAt10PinsDown_returnTrue() {
        val frame = Frame(0)
        frame.addRoll(Roll(10))

        assertTrue { frame.isStrike() }
    }

    @Test
    fun isStrike_with2RollsAt5_returnFalse() {

        val frame = Frame(0)
                .addRollWith(5)
                .addRollWith(5)

        assertFalse { frame.isStrike() }
    }

    @Test
    fun addRoll_forEmptyFrame_frameHasSingleRoll() {

        val roll = Roll(6)
        val frame = Frame(0)
                .addRoll(roll)

        assertTrue(frame.rolls.size == 1)
        assertTrue(frame.rolls.first().downPinCount == roll.downPinCount)
    }

    @Test
    fun `addRoll() when notIsTenthFrame() should add roll to the frame`() {
        Mockito.`when`(_frameSpy.notIsTenthFrame())
                .thenReturn(true)

        val roll = Roll(3)
        _frameSpy.addRoll(roll)

        assertEquals(1, _frameSpy.rolls.size)
        assertTrue(_frameSpy.rolls.contains(roll))
    }

    @Test
    fun `addRoll() when IsTenthFrame() and isMaxRollLimitReached() should throw IllegalArgumentException`() {
        Mockito.`when`(_frameSpy.isTenthFrame())
                .thenReturn(true)
        Mockito.`when`(_frameSpy.isMaxRollLimitReached())
                .thenReturn(true)

        val roll = RollFactory.random()
        Assertions.assertThrows(IllegalArgumentException::class.java) {
            _frameSpy.addRoll(roll)
        }
    }

    @Test
    fun `addRoll() when IsTenthFrame() and NOT isMaxRollLimitReached() should add roll to the frame`() {
        Mockito.`when`(_frameSpy.isTenthFrame())
                .thenReturn(true)
        Mockito.`when`(_frameSpy.isMaxRollLimitReached())
                .thenReturn(false)

        val roll = RollFactory.random()
        _frameSpy.addRoll(roll)

        assertTrue(_frameSpy.rolls.contains(roll))
    }

    @Test
    fun `addRoll() when IsTenthFrame() and isEligibleForExtraBall() should add roll to the frame`() {
        Mockito.`when`(_frameSpy.isTenthFrame())
                .thenReturn(true)
        Mockito.`when`(_frameSpy.isEligibleForExtraBall())
                .thenReturn(true)

        val roll = RollFactory.random()
        _frameSpy.addRoll(roll)

        assertTrue(_frameSpy.rolls.contains(roll))
    }

    @Test
    fun getBonus_withStrikeAndNextFrameIsTenthFrameWithRollsOf5And3_return8() {

        val frame = Frame(8)
                .addRollWith(10)

        Mockito.`when`(_frameSpy.isTenthFrame()).thenReturn(true)
        _frameSpy.addRollWith(5)
                .addRollWith(3)

        val bonus = frame.getBonus(nextFrame = Optional.of(_frameSpy))

        assertEquals(8, bonus)
    }

    @Test
    fun `getBonus() with strike and next frame isEligibleForExtraBall() and has rolls of 10 and 5 should return 15`() {

        val frame = Frame(8)
                .addRollWith(10)

        Mockito.`when`(_frameSpy.isTenthFrame()).thenReturn(true)
        Mockito.`when`(_frameSpy.isEligibleForExtraBall()).thenReturn(true)
        _frameSpy.addRollWith(10)
                .addRollWith(5)
                .addRollWith(2)

        val bonus = frame.getBonus(nextFrame = Optional.of(_frameSpy))

        assertEquals(15, bonus)
    }

    @Test
    fun `getBonus() with isSpare() and next frame has rolls of 3 and 2 should return 3`() {

        val frame = Frame(8)
                .addRollWith(5)
                .addRollWith(5)

        val nextFrame = Frame(1)
                .addRollWith(3)
                .addRollWith(2)

        val bonus = frame.getBonus(Optional.of(nextFrame))

        assertEquals(3, bonus)
    }

    @Test
    fun getScore_with2RollsOf2And6_return8() {

        val frame = Frame(0)
                .addRollWith(2)
                .addRollWith(6)

        val score = frame.getScore(Optional.empty())

        assertEquals(8, score)
    }

    @Test
    fun getScore_withStrikeAndNextRollOf5_return17() {

        val frame = Frame(0)
                .addRollWith(10)

        val nextFrame = Frame(1)
                .addRollWith(5)
                .addRollWith(2)

        val score = frame.getScore(Optional.of(nextFrame))

        assertEquals(17, score)
    }

    internal object RollFactory {

        fun random(): Roll
                = Roll((0..Constants.MAX_PIN_COUNT).random())
    }
}

// helper func
fun ClosedRange<Int>.random(): Int
        = Random().nextInt((this.endInclusive + 1) - this.start) +  this.start

