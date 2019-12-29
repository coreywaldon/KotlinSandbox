package math

open class Physics(
    private var position    : Vector,
    private var velocity    : Vector = 0 v 0 v 0,
    private var acceleration: Vector = 0 v 0 v 0) {

    /*
        Updates the position and velocity of the object.
        @param delta: Delta, as in the change in time. Given as a percent of a second.
     */
    open fun update(delta: Float) {
        position += velocity * delta
        velocity += acceleration * delta
    }

    fun distance(that: Physics) : Float {
        return position.distance(that.position)
    }

    var x : Float
        get() = position.x
        set(x) { position.x = x }

    val xi : Int
        get() = position.xi

    var y : Float
        get() = position.y
        set(y) { position.y = y }

    val yi : Int
        get() = position.yi

    var z : Float
        get() = position.z
        set(z) { position.z = z }

    var pos: Vector
        get() = position
        set(pos) { position = pos }

    var v : Vector
        get() = velocity
        set(v) { velocity = v }

    var a : Vector
        get() = acceleration
        set(a) { acceleration = a}

    override fun toString(): String {
        return "$position @ $velocity + $acceleration"
    }
}