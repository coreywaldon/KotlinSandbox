package twod

import java.lang.Float.max
import java.lang.Float.min

class Vec2d(val x: Float, val y: Float) {

    constructor(x: Number, y: Number) : this(x.toFloat(), y.toFloat())

    operator fun times(that: Vec2d) : Vec2d {
        return Vec2d(this.x * that.x, this.y * that.y)
    }

    operator fun times(that: Number) : Vec2d {
        return Vec2d(this.x * that.toFloat(), this.y * that.toFloat())
    }

    operator fun plus(that: Vec2d) : Vec2d {
        return Vec2d(this.x + that.x, this.y + that.y)
    }

    operator fun plus(that: Number) : Vec2d {
        return Vec2d(this.x + that.toFloat(), this.y + that.toFloat())
    }

    operator fun minus(that: Vec2d) : Vec2d {
        return plus(-that)
    }

    operator fun unaryMinus() : Vec2d {
        return times(-1)
    }

    infix fun clamp(other: Vec2d) : Vec2d {
        var that = other
        if (other.x > other.y)
            that = Vec2d(that.y, that.x)
        return Vec2d(if (that.x > this.x) that.x else if (that.y < this.x) that.y else x,
                     if (that.x > this.y) that.x else if (that.y < this.y) that.y else y)
    }

    infix fun clampMin(other: Float) : Vec2d {
        return Vec2d(max(other, this.x), max(other, this.y))
    }

    infix fun clampMax(other: Float) : Vec2d {
        return Vec2d(min(other, this.x), max(other, this.y))
    }

    override fun toString(): String {
        return "$x, $y"
    }

    val xi : Int
        get() = x.toInt()

    val yi : Int
        get() = y.toInt()
}

infix fun Number.v(that: Number): Vec2d {
    return Vec2d(this, that)
}