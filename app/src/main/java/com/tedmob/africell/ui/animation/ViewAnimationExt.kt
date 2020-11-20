package com.tedmob.africell.ui.animation

import android.view.View
import android.view.ViewPropertyAnimator
import android.view.animation.Interpolator
import androidx.dynamicanimation.animation.SpringForce
import androidx.dynamicanimation.animation.springAnimationOf
import androidx.dynamicanimation.animation.withSpringForceProperties
import kotlinx.coroutines.*

@DslMarker
annotation class ViewOnClickActionsDSLMarker


@ViewOnClickActionsDSLMarker
class ViewOnClickActionsBuilder(val view: View) {
    val actions: MutableList<suspend (View) -> Unit> = mutableListOf()
    val endActions: MutableList<suspend (View) -> Unit> = mutableListOf()

    private var job: Job? = null

    init {
        val onAttachListener = object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(v: View?) {
            }

            override fun onViewDetachedFromWindow(v: View?) {
                job?.cancel()
            }
        }
        view.addOnAttachStateChangeListener(onAttachListener)
    }


    private val concatenatedAction: ((View) -> Unit)?
        get() =
            if (actions.isEmpty() && endActions.isEmpty()) {
                null
            } else {
                { view ->
                    view.isClickable = false
                    try {
                        job = CoroutineScope(Dispatchers.Main).launch {
                            actions.forEach { it(view) }
                            endActions.forEach { it(view) }
                            view.isClickable = true
                        }
                    } catch (e: InterruptedException) {
                        view.isClickable = true
                    } finally {
                    }
                }
            }


    fun commit() {
        view.setOnClickListener(concatenatedAction)
    }
}

@ViewOnClickActionsDSLMarker
class ViewPropertyAnimatorExtension(private val propertyAnimator: ViewPropertyAnimator) {

    var interpolator: Interpolator? = null
    var startDelay: Long? = null
    var duration: Long? = null


    inline fun interpolator(interpolator: Interpolator?) {
        this.interpolator = interpolator
    }

    inline fun withStartDelay(startDelay: Long) {
        this.startDelay = startDelay
    }

    inline fun duration(duration: Long) {
        this.duration = duration
    }


    fun build(): ViewPropertyAnimator = propertyAnimator
        .runIfNotNull(interpolator) { setInterpolator(it) }
        .runIfNotNull(startDelay) { setStartDelay(it) }
        .runIfNotNull(duration) { setDuration(it) }

    private inline fun <reified T, reified A> T.runIfNotNull(
        value: A?,
        block: T.(value: A) -> T
    ): T = if (value != null) block(value) else this
}


inline fun View.actionsOnClickBuilder(): ViewOnClickActionsBuilder = ViewOnClickActionsBuilder(this)

inline fun View.actionsOnClick(block: ViewOnClickActionsBuilder.() -> Unit): View {
    actionsOnClickBuilder().apply(block).commit()
    return this
}

inline fun View.onClick(noinline action: suspend (View) -> Unit): View {
    actionsOnClickBuilder().action(action).commit()
    return this
}


inline fun ViewOnClickActionsBuilder.wait(time: Long): ViewOnClickActionsBuilder {
    actions.add { delay(time) }

    return this
}

inline fun ViewOnClickActionsBuilder.action(noinline action: suspend (View) -> Unit): ViewOnClickActionsBuilder {
    actions.add(action)

    return this
}

inline fun ViewOnClickActionsBuilder.grow(
    scaleX: Float = 1.4f,
    scaleY: Float = 1.4f,
    crossinline continueSetup: ViewPropertyAnimatorExtension.() -> Unit
): ViewOnClickActionsBuilder {
    val action: suspend (View) -> Unit = { view: View ->
        view.animate()
            .scaleX(scaleX)
            .scaleY(scaleY)
            .setDuration(200L)
            .run { ViewPropertyAnimatorExtension(this).apply(continueSetup).build() }
            .start()
    }
    val endAction: suspend (View) -> Unit = { view: View ->
        view.scaleX = 1f
        view.scaleY = 1f
    }

    actions.add(action)
    endActions.add(endAction)

    return this
}

inline fun ViewOnClickActionsBuilder.springHorizontalToOrigin(): ViewOnClickActionsBuilder {
    val action: suspend (View) -> Unit = { view: View ->
        val animation = springAnimationOf(
            setter = view::setTranslationX,
            getter = view::getTranslationX
        ).withSpringForceProperties {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = 0.2f
            finalPosition = 0f
        }
        animation.start()
    }

    actions.add(action)

    return this
}

inline fun ViewOnClickActionsBuilder.springVerticalToOrigin(): ViewOnClickActionsBuilder {
    val action: suspend (View) -> Unit = { view: View ->
        val animation = springAnimationOf(
            setter = view::setTranslationY,
            getter = view::getTranslationY
        ).withSpringForceProperties {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = 0.2f
            finalPosition = 0f
        }
        animation.start()
    }

    actions.add(action)

    return this
}

inline fun ViewOnClickActionsBuilder.springToOrigin(): ViewOnClickActionsBuilder {
    val action: suspend (View) -> Unit = { view: View ->
        val horizontalAnimation = springAnimationOf(
            setter = view::setTranslationX,
            getter = view::getTranslationX
        ).withSpringForceProperties {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = 0.2f
            finalPosition = 0f
        }

        val verticalAnimation = springAnimationOf(
            setter = view::setTranslationY,
            getter = view::getTranslationY
        ).withSpringForceProperties {
            stiffness = SpringForce.STIFFNESS_LOW
            dampingRatio = 0.2f
            finalPosition = 0f
        }

        horizontalAnimation.start()
        verticalAnimation.start()
    }

    actions.add(action)

    return this
}
