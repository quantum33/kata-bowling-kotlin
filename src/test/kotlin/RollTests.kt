package kata.bowling.kotlin.test

import com.kata.bowlingkt.Constants
import com.kata.bowlingkt.Roll
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test

class RollTests {

    @Test
    fun constructor_withValidDownPinValues_success() {

        for (i in 0..Constants.MAX_PIN_COUNT) {
            Roll(i)
        }
    }

    @Test
    fun constructor_withOufOfBoundsCount_throwIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Roll(Constants.MAX_PIN_COUNT + 1)
        }
    }

    @Test
    fun constructor_withNegativeDownPinCount_throwIllegalArgumentException() {

        Assertions.assertThrows(IllegalArgumentException::class.java) {
            Roll(-1)
        }
    }
}