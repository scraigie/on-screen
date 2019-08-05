package uk.co.scraigie.onscreen.core.framework.behaviors

import com.nhaarman.mockito_kotlin.mock
import com.nhaarman.mockito_kotlin.verify
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

internal class PresenterBehaviorTest {

    interface MockView: IView

    private var view: MockView = mock()

    private val presenter: IPresenter<MockView> = mock()

    private val presenterProvider = { presenter }

    private lateinit var sut: PresenterBehavior<MockView>

    @BeforeEach
    fun setUp() {
        sut = PresenterBehavior(view, presenterProvider)
    }

    @Test
    fun `presenter onAttach is called on onStart`() {
        sut.onStart()
        verify(presenter).onAttach(view)
    }

    @Test
    fun `presenter onDetach is called on onStop`() {
        sut.onStop()
        verify(presenter).onDetach()
    }
}