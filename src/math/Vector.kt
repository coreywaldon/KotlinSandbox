package math

import kotlin.math.*

class Vector(x: Number, y: Number, z: Number = 0) {

    var x: Float
    var y: Float
    var z: Float

    init {
        this.x = x.toFloat()
        this.y = y.toFloat()
        this.z = z.toFloat()
    }

    operator fun unaryMinus() : Vector {
        return Vector(-x, -y, -z)
    }

    operator fun plus(that: Vector) : Vector {
        val x = this.x + that.x
        val y = this.y + that.y
        val z = this.z + that.z
        return Vector(x, y, z)
    }

    operator fun minus(that: Vector) : Vector {
        return plus(-that)
    }

    operator fun times(that: Number) : Vector {
        val x = this.x * that.toFloat()
        val y = this.y * that.toFloat()
        val z = this.z * that.toFloat()
        return Vector(x, y, z)
    }

    operator fun times(that: Vector) : Vector {
        val x = this.x * that.x
        val y = this.y * that.y
        val z = this.z * that.z
        return Vector(x, y, z)
    }

    operator fun div(that: Number) : Vector {
        val x = this.x / that.toFloat()
        val y = this.y / that.toFloat()
        val z = this.z / that.toFloat()
        return Vector(x, y, z)
    }

    infix fun x(that: Vector) : Vector {
        val x = this.y * that.z - (that.y * this.z)
        val y = -(this.x * that.z - (that.x * this.z))
        val z = this.x * that.y - (that.x * this.y)
        return Vector(x, y, z)
    }

    fun distance(that: Vector) : Float {
        return sqrt((that.x - this.x).pow(2) + (that.y - this.y).pow(2))
    }

    private fun normal() : Vector {
        return this / mag
    }

    private fun magnitude() : Float {
        return sqrt(x*x + y*y + z*z)
    }

    fun clamp(min : Number = 0, max : Number = 0) {
        val min = min.toFloat()
        val max = max.toFloat()
        if (x < min) x = min
        if (x > max) x = max
        if (y < min) y = min
        if (y > max) y = max
        if (z < min) z = min
        if (z > max) z = max
    }

    fun setMagnitude(that: Number) {
        val result = n * that
        this.x = result.x
        this.y = result.y
        this.z = result.z
    }

    val xi : Int
        get() = this.x.toInt()

    val yi : Int
        get() = this.y.toInt()

    val zi : Int
        get() = this.z.toInt()

    val n : Vector
        get() = this.normal()

    val mag : Float
        get() = this.magnitude()

    override fun toString(): String {
        return "($x, $y)"
    }
}

infix fun Number.v(that: Number) : Vector {
    return Vector(this, that)
}

infix fun Vector.v(that: Number) : Vector {
    return Vector(this.x, this.y, this.z + that.toFloat())
}
